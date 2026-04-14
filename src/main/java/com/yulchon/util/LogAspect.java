package com.yulchon.util;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LogAspect {
	
	private static final Logger logger = Logger.getLogger(LogAspect.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		// null 값인 필드는 로그에서 제외하여 용량 절약
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

    // com.yulchon.controller 패키지 하위의 모든 클래스 및 메서드에 적용
    @Around("execution(* com.yulchon.controller..*.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	// 세션에서 사용자 정보 가져오기
        String loginUserId = "Guest"; // 기본값
        String loginUserName = "GuestName";
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("loginUserId") != null) { 
                    loginUserId = session.getAttribute("loginUserId").toString();
                    loginUserName = session.getAttribute("loginUserName").toString();
                }
            }
        } catch (Exception e) {
            loginUserId = "Unknown";
        }
        
        String type = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        Object[] argsObj = joinPoint.getArgs();
		String args = "";
		
		try {
		    if (argsObj != null && argsObj.length > 0) {
		        StringBuilder sb = new StringBuilder("[");
		        for (int i = 0; i < argsObj.length; i++) {
		            Object obj = argsObj[i];
		            
		            // 1. JSON으로 변환하면 안 되는 녀석들을 걸러냅니다.
		            if (obj instanceof javax.servlet.ServletRequest || 
		                obj instanceof javax.servlet.ServletResponse || 
		                obj instanceof javax.servlet.http.HttpSession ||
		                obj instanceof org.springframework.ui.Model) {
		                
		                sb.append(obj.getClass().getSimpleName()); // 객체 이름만 찍음 (예: RequestFacade)
		            } else {
		                // 2. 일반 DTO나 데이터는 JSON으로 변환
		                try {
		                    sb.append(objectMapper.writeValueAsString(obj));
		                } catch (Exception e) {
		                    sb.append(obj.toString());
		                }
		            }
		            if (i < argsObj.length - 1) sb.append(", ");
		        }
		        sb.append("]");
		        args = sb.toString();
		    }
		} catch (Exception e) {
		    args = Arrays.toString(argsObj);
		}

        // 메서드 실행 전 로그
        logger.info("[시작] [" + loginUserId + "(" + loginUserName + ")]" + type + "." + methodName + "() | 인자: " + args);

        try {
        	// 실제 컨트롤러 메서드 실행
            Object result = joinPoint.proceed(); 

            // [수정] 반환값도 ObjectMapper를 사용하여 null을 제외하고 변환
            String resultStr = "";
            try {
                if (result != null) {
                	if (result instanceof java.util.List) {
                        // 리스트인 경우: "List(건수)" 형식으로 출력 (예: List(150건))
                        java.util.List<?> list = (java.util.List<?>) result;
                        resultStr = "List(" + list.size() + "건)";
                    } else {
                        // 리스트가 아닌 경우만 JSON 변환, 그마저도 너무 길면 자르기
                        resultStr = objectMapper.writeValueAsString(result);
                        if (resultStr.length() > 500) {
                            resultStr = resultStr.substring(0, 500) + "...(생략)";
                        }
                    }
                }
            } catch (Exception e) {
                resultStr = String.valueOf(result); // 변환 실패 시 기본 toString
            }

            logger.info("[종료] [" + loginUserId + "(" + loginUserName + ")]" + type + "." + methodName + "() | 반환: " + resultStr);
            return result;

        } catch (Throwable e) {
            // 여기서 에러를 가로채서 errorLog.log에 저장
            // e를 인자로 넣으면 에러가 발생한 상세 경로(Stack Trace)가 다 찍힙니다.
            logger.error("[에러발생] " + type + "." + methodName + "() | 메시지: " + e.getMessage(), e);
            
            // 에러를 기록만 하고 다시 던져줘야 스프링이 에러 페이지를 보여주거나 처리를 할 수 있습니다.
            throw e; 
        }
    }

}

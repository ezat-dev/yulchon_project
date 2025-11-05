package com.tkheat.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.tkheat.controller.MainController;
import com.tkheat.controller.UserController;

public class CommProcessor {
	@Autowired
	UserController userController;
	
	//1초주기로 OPC UA 커넥션이 null일경우 연결
	@Scheduled(fixedRate = 1000)
	public void handle() {
		if(MainController.client == null) {
//			MainController.opcStart();
			MainController.commCheck();
		}		
	}
	
	//10초 주기로 알람 발생 확인 및 알람 전송
    @Scheduled(fixedRate = 10000) // 10000 밀리초 = 10초
    public void sendWorkTime() {
    	//UserController userController = new UserController();
    	//System.out.println("10초 스케줄러 실행: UserController.send() 요청");
        
        // 💡 UserController의 send() 함수를 호출합니다.
        // (단, UserController.send() 함수가 public이고 인스턴스 메서드여야 합니다.)
        userController.send();
    }
}

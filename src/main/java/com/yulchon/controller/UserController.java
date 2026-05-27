package com.yulchon.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.yulchon.domain.UserMenu;
import com.yulchon.domain.Users;
import com.yulchon.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	public static int USER_CODE = 0;
	
	/*사용자 로그인*/
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@ModelAttribute Users users, 
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		//System.out.println("로그인 아이디: " + users.getID_LOGIN());
		//System.out.println("로그인 비밀번호: " + users.getNO_PASSWORD());
		if("".equals(users.getID_LOGIN()) || users.getID_LOGIN() == null){
			rtnMap.put("data","아이디를 입력해주십시오!");
			rtnMap.put("status","NG");

			return rtnMap;
		}

		if("".equals(users.getNO_PASSWORD()) || users.getNO_PASSWORD() == null){
			rtnMap.put("data","비밀번호를 입력해주십시오!");
			rtnMap.put("status","NG");

			return rtnMap;
		}

		//로그인을 클릭한 사용자의 아이디와 비밀번호가 같을 때
		Users loginUser = userService.getLoginUser(users);

		if(loginUser == null) {
			rtnMap.put("data","등록되지 않은 사용자입니다.");
			rtnMap.put("status","NG");

			return rtnMap;			 
		}
		
		//로그인한 대상의 page정보 세션저장
		session.setAttribute("loginUserId",loginUser.getID_LOGIN());
		//session.setAttribute("loginUserPassword",loginUser.getNO_PASSWORD());
		session.setAttribute("loginUserName", loginUser.getNM_LOGIN());
		
		rtnMap.put("data", loginUser);
		rtnMap.put("status","OK");

		return rtnMap;
	}		
	 
	 //로그인한 사용자의 메뉴저장
	 @RequestMapping(value = "/user/login/menuSave", method = RequestMethod.POST) 
	 @ResponseBody 
	 public Map<String, Object> userLoginMenuSave(
			 @RequestParam int user_code,
			 @RequestParam String menu_url,
			 @RequestParam String menu_name) {
		 Map<String, Object> rtnMap = new HashMap<String, Object>();
		 
		 UserMenu userMenu = new UserMenu();
		 userMenu.setUser_code(user_code);
		 userMenu.setMenu_url(menu_url);
		 userMenu.setMenu_name(menu_name);
		 
		 userService.userLoginMenuSave(userMenu);
		 
		 rtnMap.put("data","OK");
		 
		 return rtnMap;
	 }
	 
	 //로그인한 사용자의 메뉴저장
	 @RequestMapping(value = "/user/login/menuList", method = RequestMethod.POST) 
	 @ResponseBody 
	 public Map<String, Object> userLoginMenuList(
			 @RequestParam int user_code) {
		 Map<String, Object> rtnMap = new HashMap<String, Object>();
		 
		 UserMenu userMenu = new UserMenu();
		 userMenu.setUser_code(user_code);
		 
		 List<UserMenu> userMenuList = userService.userLoginMenuList(userMenu);
		 
		 rtnMap.put("data",userMenuList);
		 
		 return rtnMap;
	 }
	 
	 //로그인한 사용자의 메뉴저장
	 @RequestMapping(value = "/user/login/menuRemove", method = RequestMethod.POST) 
	 @ResponseBody 
	 public Map<String, Object> userLoginMenuRemove(
			 @RequestParam int user_code,
			 @RequestParam String menu_url) {
		 Map<String, Object> rtnMap = new HashMap<String, Object>();
		 
		 UserMenu userMenu = new UserMenu();
		 userMenu.setUser_code(user_code);
		 userMenu.setMenu_url(menu_url);
		 
		 userService.userLoginMenuRemove(userMenu);
		 
		 rtnMap.put("data","OK");
		 
		 return rtnMap;
	 }
	 
	 
	 
	 
	//로그아웃
	@RequestMapping(value="/user/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Users users = (Users)session.getAttribute("user");
			session.removeAttribute("user");
			session.invalidate();
		return "redirect:/";
	}	
	

	 //사용자 등록 
	 @RequestMapping(value = "/user/insert", method = RequestMethod.POST)
	 @ResponseBody
	 public Map<String, Object> userInsertInsert(@ModelAttribute Users users, HttpSession session) {
		 Map<String, Object> rtnMap = new HashMap<>();
		 //System.out.println("userinert 컨트롤러");

		 //아이디 입력했는지		 
		 if("".equals(users.getUser_id())){
			 rtnMap.put("data","아이디를 입력해주십시오!");
			 
			 return rtnMap;
		 }
		 		 
		 //비밀번호 입력했는지		 
		 if("".equals(users.getUser_pw())){
			 rtnMap.put("data","비밀번호를 입력해주십시오!");
			 
			 return rtnMap;
		 }
		 
		 //동일한 사용자가 있는지
		 Users duplicateUser = userService.userDuplicateCheck(users);
		 
		 if(duplicateUser != null) {
			 rtnMap.put("data","이미 등록된 아이디입니다.");
			 
			 return rtnMap;			 
		 }
		 
		 if(users.getUser_company() != null) {
			 users.setUser_role("1");
		 }
		 String loginCompanyCode = (String)session.getAttribute("company_code");
		 if(loginCompanyCode == null || loginCompanyCode == "") {
			 rtnMap.put("status", "error");
			 rtnMap.put("message", "다시 로그인 해주세요");
		 }
		 users.setCompany_code(loginCompanyCode);
		 userService.userInsertInsert(users);
		 
		 rtnMap.put("status", "OK");
		 rtnMap.put("message", "사용자 정보가 성공적으로 저장되었습니다.\nezat@ezat.co.kr로 문의해주시기 바랍니다.");
		 return rtnMap; 
	 }
	 
	    @RequestMapping(value = "/user/selectList", method = RequestMethod.POST)
	    @ResponseBody
	    public List<Users> selectuserList(Users users, HttpSession session) {
			 String loginCompanyCode = (String)session.getAttribute("company_code");
			 users.setCompany_code(loginCompanyCode);
	        return userService.selectUserList(users);
	    }
	    
	    //작업시간
		 @RequestMapping(value = "/user/insertWorkTime", method = RequestMethod.POST)
		 @ResponseBody
		 public boolean insertWorkTime(@RequestBody List<Users> usersList) {
			 //System.out.println("받은 사용자 수: " + usersList.size());
			 boolean result = true;
			 for(Users users: usersList) {
			        boolean currentResult = userService.insertWorkTime(users);
			        //userService.userInsertInsert(users);
			        if (!currentResult) {
			        	result = false; 
			        }
			    }
			    
			    return result; 
		 }
		 
		    //알람 수신 여부 수정
			 @RequestMapping(value = "/user/updateMessage", method = RequestMethod.POST)
			 @ResponseBody
			 public boolean updateMessage(@ModelAttribute Users users) {
				
				        return userService.updateMessage(users);
			 }
			 
			 //작업시간 테이블 삭제
			 @RequestMapping(value = "/user/deleteWorkTime", method = RequestMethod.POST)
			 @ResponseBody
			 public boolean deleteWorkTime(@RequestBody List<Users> workTimeList) {
			     boolean result = false;
//			     if (workTimeList.isEmpty()) {
//			         rtnMap.put("status", "FAIL");
//			         rtnMap.put("data", "삭제할 데이터가 없습니다.");
//			         return rtnMap;
//			     }
			     
			     try {
			         for (Users users : workTimeList) {
			             result = userService.deleteWorkTime(users); 
			         }
			     } catch (Exception e) {
			         System.err.println("작업 시간 삭제 중 오류 발생: " + e.getMessage());
			     }
			     
			     return result;
			 }
			 
			 //모달 조회
			    @RequestMapping(value = "/user/selectModalList", method = RequestMethod.POST)
			    @ResponseBody
			    public List<Users> selectuserModalList(Users users) {
			    	//System.out.println("모달 조회 컨트롤러");
			        return userService.selectUserModalList(users);
			    }
			 
				//안드로이드 로그인
				@RequestMapping(value="/user/android/login", method=RequestMethod.POST)
				@ResponseBody
				public boolean androidLogin(@RequestBody Users users, HttpSession session) {
					System.out.println("안드로이드 로그인 컨트롤러 도착");
					//System.out.println("users.getUser_id()" + users.getUser_id());
					//System.out.println("users.getUser_pw()" + users.getUser_pw());
					 
					
					 if("".equals(users.getUser_id()) || users.getUser_id() == null){
						 return false;
					 }
					 
					 if("".equals(users.getUser_pw()) || users.getUser_pw() == null){
						 return false;
					 }
					
					//로그인을 클릭한 사용자의 아이디와 비밀번호가 같을 때
					Users loginUser = userService.getLoginUser(users);
					 
					 if(loginUser == null) {
						 
						 return false;			 
					 }
					 session.setAttribute("loginUser",loginUser);
					 session.setAttribute("company_code", loginUser.getCompany_code());
					return true;
				}
				
				//안드로이드 로그인 성공 시 device token 업데이트
				@RequestMapping(value="/user/android/deviceTokenUpdate", method=RequestMethod.POST)
				@ResponseBody
				public boolean deviceTokenUpdate(@RequestBody Users users) {
					//System.out.println("안드로이드 토큰 업데이트 도착");
					//System.out.println("users.getDevice_token()" + users.getDevice_token());
					return userService.deviceTokenUpdate(users);

				}
				
				//그룹 추가
				@RequestMapping(value="/user/insertGroup", method=RequestMethod.POST)
				@ResponseBody
				public boolean insertGroup(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.insertGroup(users);

				}
				//그룹 삭제
				@RequestMapping(value="/user/deleteGroup", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteGroup(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.deleteGroup(users);

				}
				//그룹 알림 스케줄 추가
				@RequestMapping(value="/user/insertGroupSchedule", method=RequestMethod.POST)
				@ResponseBody
				public String updateTime(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					userService.insertGroupSchedule(users); 
				    
				    return "SUCCESS"; 
				}
				//그룹별 조회
				@RequestMapping(value="/user/getGroupUser", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupUser(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					//System.out.println("그룹별 조회 컨트롤러");
					//System.out.println("users.getGroup_id()" + users.getGroup_id());
					return userService.getGroupUser(users); 
				}
				//그룹 스케줄 조회
				@RequestMapping(value="/user/getGroupScheduleList", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupScheduleList(Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.getGroupScheduleList(users); 
				}
//				//그룹 수신 알림 조회
//				@RequestMapping(value="/user/getGroupList", method=RequestMethod.POST)
//				@ResponseBody
//				public List<Users> getGroupList(Users users, HttpSession session) {
//					 String loginCompanyCode = (String)session.getAttribute("company_code");
//					 users.setCompany_code(loginCompanyCode);
//					return userService.getGroupList(users); 
//				}
				//그룹 수신 알림 조회
				@RequestMapping(value="/user/getGroupList", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupList(Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.getGroupRecieveAlarm(users); 
				}
				//그룹별 수신 알람 추가
				@RequestMapping(value="/user/deleteRecieveAlarm", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteRecieveAlarm(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.deleteRecieveAlarm(users); 
				}
				//그룹별 수신 알람 삭제
				//그룹별 수신 알람 업데이트
				@RequestMapping(value="/user/updateRecieveAlarm", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateRecieveAlarm(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.updateRecieveAlarm(users); 
				}
				//스케줄 삭제
				@RequestMapping(value="/user/deleteSchedule", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteSchedule(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.deleteSchedule(users); 
				}
				//회원정보 업데이트
				@RequestMapping(value="/user/updateUser", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateUser(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					 Users duplicateUser = userService.userDuplicateCheck(users);
					 if(duplicateUser != null) {
						 return false;			 
					 }
					return userService.updateUser(users);
				}
				//회원 삭제
				@RequestMapping(value="/user/deleteUser", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteUser(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.deleteUser(users);
				}
				//스케줄 업데이트
				@RequestMapping(value="/user/updateGroupSchedule", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateGroupSchedule(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.updateGroupSchedule(users);
				}
				//사람 그룹 이름 조회
				@RequestMapping(value="/user/getGroupName", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupName(Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.getGroupName(users);
				}
				//그룹 이름 업데이트
				@RequestMapping(value="/user/updateGroupName", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateGroupName(@RequestBody Users users, HttpSession session) {
					 String loginCompanyCode = (String)session.getAttribute("company_code");
					 users.setCompany_code(loginCompanyCode);
					return userService.updateGroupName(users);
				}
				//회사 추가
				@RequestMapping(value="/user/insertCompany", method=RequestMethod.POST)
				@ResponseBody
				public Users insertCompany(@RequestBody Users users, HttpSession session) {
					String newCompanyCode = userService.selectNewCompanyCode(users);
					users.setCompany_code(newCompanyCode);
					userService.insertCompany(users); 
					return users;
				}
				//회사 관리자, 그룹, 알람 그룹 추가
				@RequestMapping(value="/user/insertCompanyAdmin", method=RequestMethod.POST)
				@ResponseBody
				public boolean insertCompanyAdmin(@RequestBody Users users, HttpSession session) {
					//회사 관리자 추가
					boolean flag1 = userService.insertCompanyAdmin(users); 
					if(!flag1) {
						System.out.println("관리자 추가 실패");
					}
					
					//새 회사 정보 가져오기
					users = userService.selectNewCompany(users);
					
					//그룹 추가
					boolean flag2 = userService.insertNewGroup(users);
					if(!flag2) {
						System.out.println("그룹 추가 실패");
					}
					
					//알람 그룹 추가
					boolean flag3 = userService.insertNewAlarmGroup(users);
					if(!flag3) {
						System.out.println("알람그룹 추가 실패");
					}
					if(flag1 && flag2 && flag3) {
						return true;
					}
					return false;
				}
				//회사 조회
				@RequestMapping(value="/user/getCompanyNames", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getCompanyNames(Users users, HttpSession session) {
					return userService.getCompanyNames(users);
				}
				@RequestMapping(value="user/insertAlarmData", method=RequestMethod.POST)
				@ResponseBody
				public boolean insertAlarmData(Users users,
				    @RequestParam("company_code") String company_code,
				    @RequestParam("alarmFile") MultipartFile file) {
					if (file.isEmpty()) {
						System.out.println("파일 없음");
			            return false;
			        }
					List<Users> alarmList = new ArrayList<>();

			        try (InputStream is = file.getInputStream();
			             Workbook workbook = new XSSFWorkbook(is)) { 
			            
			            Sheet sheet = workbook.getSheetAt(0); 

			            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			                Row row = sheet.getRow(i);
			                if (row == null) continue;

			                // 1열: alarm_address (Cell Index 0)
			                Cell cell1 = row.getCell(0);
			                String alarmAddress = getCellValue(cell1);
			                System.out.println("alarmAddress: " + alarmAddress);

			                // 2열: alarm_comment (Cell Index 1)
			                Cell cell2 = row.getCell(1);
			                String alarmComment = getCellValue(cell2);
			                System.out.println("alarmComment: " + alarmComment);
			                
			                if (alarmAddress != null && !alarmAddress.trim().isEmpty()) {
			                	Users newAlarmUser = new Users();
			                	newAlarmUser.setAlarm_address(alarmAddress);
			                	newAlarmUser.setComment(alarmComment);
			                	newAlarmUser.setCompany_code(company_code);
			                    alarmList.add(newAlarmUser);
			                }
			            }
			            
			            if (alarmList.isEmpty()) {
			                System.out.println("파일에 데이터 없음");
			                return true; 
			            }

			            // 💡 2. DB 삽입은 Service에 위임
			            return userService.insertAlarmData(alarmList);

			        } catch (IOException e) {
			            e.printStackTrace();
			            return false;
			        } catch (Exception e) {
			            e.printStackTrace();
			            return false;
			        }
			    }
			    
			    // 💡 셀 값 추출 헬퍼 메서드 (컨트롤러 내부 또는 유틸리티로 이동)
			    private String getCellValue(Cell cell) {
			        if (cell == null) return null;
			        
			        switch (cell.getCellType()) {
			            case STRING:
			                return cell.getStringCellValue();
			            case NUMERIC:
			                // 숫자가 텍스트처럼 보일 경우를 대비하여 문자열로 변환
			                return String.valueOf((long)cell.getNumericCellValue());
			            default:
			                return null;
			        }
				}
}


package com.tkheat.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.tkheat.domain.Alarm;
import com.tkheat.domain.Permission;
import com.tkheat.domain.UserMenu;
import com.tkheat.domain.Users;
import com.tkheat.service.AlarmService;
import com.tkheat.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AlarmService alarmService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	public static int USER_CODE = 0;
	
	/*사용자 로그인*/
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@ModelAttribute Users users, 
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		 
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		 if("".equals(users.getUser_id()) || users.getUser_id() == null){
			 rtnMap.put("data","아이디를 입력해주십시오!");
			 rtnMap.put("status","NG");
			 
			 return rtnMap;
		 }
		 
		 if("".equals(users.getUser_pw()) || users.getUser_pw() == null){
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
		 
//		 loginUser.setUser_ip(users.getUser_ip());
//		 
//		 userService.userLoginHisSave(loginUser);
		 
		 //로그인한 대상의 page정보 세션저장
		 Permission loginPermission = userService.userLoginPermission(loginUser);
		 session.setAttribute("loginUser",loginUser);
		 session.setAttribute("loginUserPage",loginPermission);
		 
		 USER_CODE = loginUser.getUser_code();
		 
		 rtnMap.put("data", loginUser);
		 rtnMap.put("status","OK");
		 rtnMap.put("loginUserPage", loginPermission);


		return rtnMap;
	}	
	
	 //로그인한 사용자의 메뉴셋팅
	 @RequestMapping(value = "/user/login/menuSetting", method = RequestMethod.POST) 
	 @ResponseBody 
	 public Map<String, Object> userLoginMenuSetting(HttpSession session) {
		 Map<String, Object> rtnMap = new HashMap<String, Object>();
		 
		 Permission pageData = (Permission)session.getAttribute("loginUserPage");
		 
		 rtnMap.put("data",pageData);
		 
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
	 
	 
	 //사용자 권한 유저info
	 @RequestMapping(value = "/user/info", method = RequestMethod.POST)
	 @ResponseBody
	 public Map<String, Object> getUserInfo(HttpSession session) {
	     Map<String, Object> result = new HashMap<>();
	     
	     // 세션에서 데이터 가져오기
	     Users loginUser = (Users) session.getAttribute("loginUser");
	     Permission loginPermission = (Permission) session.getAttribute("loginUserPage");

	 
	     if (loginUser != null) {
	         result.put("loginUser", loginUser);
	     }
	     if (loginPermission != null) {
	         result.put("loginUserPage", loginPermission);
	     }
	     
	     return result;
	 }
	 
	 
	 
	//로그아웃
	@RequestMapping(value="/user/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Users users = (Users)session.getAttribute("user");
		if(users != null) {
			session.removeAttribute("user");
			session.invalidate();
		}
		return "redirect:/";
	}	
	

	 //사용자 등록 
	 @RequestMapping(value = "/user/insert", method = RequestMethod.POST)
	 @ResponseBody
	 public Map<String, Object> userInsertInsert(@ModelAttribute Users users) {
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
		 userService.userInsertInsert(users);
		 
		 rtnMap.put("status", "OK");
		 rtnMap.put("message", "사용자 정보가 성공적으로 저장되었습니다.\nezat@ezat.co.kr로 문의해주시기 바랍니다.");
		 return rtnMap; 
	 }
	 
	    @RequestMapping(value = "/user/selectList", method = RequestMethod.POST)
	    @ResponseBody
	    public List<Users> selectuserList(Users users) {
	    	//System.out.println("조회 컨트롤러");
	    	//System.out.println("users.getSt_day()"+users.getSt_day());
	    	//System.out.println("users.getUser_name()"+users.getUser_name());
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
				public boolean androidLogin(@RequestBody Users users) {
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
				
			    // 푸시알림
			    @RequestMapping(value = "/user/sendPush", method = RequestMethod.POST)
			    @ResponseBody
			    public void send() {
			    	try {
			    	Alarm alarm = new Alarm();
			    	Users users = new Users();
			    	//System.out.println("푸시알림 컨트롤러 도착");
			    	String title="";
			    	String content="";
			    	String send="";
//			    	
//			    	//안보낸 알람 조회
//			    	List<Alarm> alarmDatas = alarmService.getAlarmList(alarm);
//			    	System.out.println("alarmDatas.size()" + alarmDatas.size());
//			    	
//			    	//오늘 일하면서 1호기 알림 받는 사람 조회
//			    	List<Users> getAlarmUser1 = userService.getAlarmUser1(users);
//			    	System.out.println("getAlarmUser1.size()" + getAlarmUser1.size());
//			    	
//			    	//오늘 일하면서 2호기 알림 받는 사람 조회
//			    	List<Users> getAlarmUser2 = userService.getAlarmUser2(users);
//			    	System.out.println("getAlarmUser2.size()" + getAlarmUser2.size());
//			    	for(Alarm data: alarmDatas) {
//			    		List<String> tokenList = new ArrayList<>();
//			    		if(data.getA_hogi() != null && data.getA_hogi().contains("1")) {
//		                    title = "알람발생";
//		                    content = data.getA_desc();
//		                    send = "1";
//			                // 1호기 알림 받는 사람의 device token
//			                for(Users user1: getAlarmUser1) {
//			                    String deviceToken = user1.getDevice_token();
//			                    
//			                    if (deviceToken != null && !deviceToken.isEmpty()) {
//			                        tokenList.add(deviceToken);
//			                        System.out.println("  -> 1호기 수신자 토큰 추가: " + user1.getUser_id());
//			                    }
//			                }
//			    		}else if (data.getA_hogi() != null && data.getA_hogi().contains("2")) {
//		                    title = "알람발생";
//		                    content = data.getA_desc();
//		                    send = "2";
//			                for(Users user2: getAlarmUser2) {
//			                    String deviceToken = user2.getDevice_token();
//			                    
//			                    if (deviceToken != null && !deviceToken.isEmpty()) {
//			                        tokenList.add(deviceToken);
//			                        System.out.println("  -> 2호기 수신자 토큰 추가: " + user2.getUser_id());
//			                    }
//			                }
//			    		}
//			    	    if (!tokenList.isEmpty()) {
//			    	        try {
//			    	             send_FCM(tokenList, title, content, send);
//			    	             alarmService.updateAlarmSend(data);
//			    	        } catch (Exception e) {
//			    	             System.err.println("FCM 전송 중 오류 발생: " + e.getMessage());
//			    	             e.printStackTrace();
//			    	        }
//			    	    }
//			    	}
			    	
			    	List<Users> datas = userService.sendAlarmList(users); //보낼 알람, 사람 정보
			    	List<String> sendedAlarm = new ArrayList<>(); //보낸 알람 배열
			    	for(Users v: datas) {
			    		title = "알람 발생";
			    		content = v.getComment();
			    		send = v.getA_hogi();
			    		sendedAlarm.add(v.getRegtime());
				    	List<String> tokenList = new ArrayList<>();
			    		if(v.getDevice_token() != null ) {
				    		tokenList.add(v.getDevice_token());
			    		}
			    	    if (!tokenList.isEmpty()) {
		    	        try {
		    	             send_FCM(tokenList, title, content, send);
		    	        } catch (Exception e) {
		    	             System.err.println("FCM 전송 중 오류 발생: " + e.getMessage());
		    	             e.printStackTrace();
		    	        }
		    	    }
			    		
			    	}
			    	
			    	for(String v: sendedAlarm) {
			    		users.setRegtime(v);
			    		//System.out.println("보낸 알람: " + v);
			    		userService.updateAlarmSend(users);
			    	}

			    	}catch(Exception e) {
			            System.err.println("send() 실행 중 오류 발생: " + e.getMessage());
			            e.printStackTrace();
			    	}
			    	
//			    	tokenList.add("fzCJzCQeTryFfjMBTiWQp7:APA91bFPvzl1r1XkZ7xgfPbKydfXuNh7YdBUrlUrl7xa0BbPuMr13yYtWEI41CvsTz1qmJJkcS0b_CdGFHWcWW-hhckiIn6iKSiJ0JLWGoqBnvxwbg-TsjQ");
//			    	tokenList.add("eEtqr47NRsaE83vSqvyTKU:APA91bEigWE3l-hDms-qCpO0mGIGIhJVigLte3EV52RjMsZIWSBpmKPVuq6xIPe1Qrov7wwt03Eg8YOnrpGKOPvW_iYbItsCNNolPZEWHJnj56HLtksb7pQ");
//			    	tokenList.add("dos5pfXKRee2vf0qwvSZAD:APA91bFMJC2pvJY_D786C-bqvfyCSdHq6tHqWO5s9MrvYp6c3xw3P0spaoM-bkqRa9SJ89f7rMn-aNTfF07kh3rTmnOMJ9nXbmoKchH4783tkgJMHt1rBfk");
//					if(tokenList.size() != 0){
//						send_FCM(tokenList, "알람 발생", "알람", "1");		
//						
//					}
			    }
			    
				public void send_FCM(List<String> tokenList, String title, String content, String send){
					//System.out.println("send_FCM 컨트롤러 도착");
					try{
						FileInputStream refreshToken = new FileInputStream("D:/fireBase_key/mibo-test-firebase-adminsdk-fbsvc-6c2a67fbc1.json");

						FirebaseOptions options = FirebaseOptions.builder()
													.setCredentials(GoogleCredentials.fromStream(refreshToken))
													//.setDatabaseUrl("https://samplepush-fe215.firebaseio.com")
													.build();
						
						if(FirebaseApp.getApps().isEmpty()){
							FirebaseApp.initializeApp(options);
						}
						
						
						
						
						MulticastMessage message = MulticastMessage.builder()
								.setAndroidConfig(AndroidConfig.builder()
								.setTtl(3600 * 1000)
								.setPriority(AndroidConfig.Priority.HIGH)
								
										/*
										 * .setNotification(AndroidNotification.builder() .setTag(send) .setTitle(send)
										 * .setBody(title+" : "+content) .setClickAction("") .setSound("default")
										 * .build())
										 */	
									/* default, siren, heartbeat */
								
								.build())
								.putData("title",title)
								.putData("body",content)
								.putData("hogi",send)
								.addAllTokens(tokenList)
								.build();
						//BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
						BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
						//System.out.println(response.getSuccessCount() + " messages were sent successfully");

						
						//안드로이드 토큰 입력
						String registrationToken = "eEtqr47NRsaE83vSqvyTKU:APA91bEigWE3l-hDms-qCpO0mGIGIhJVigLte3EV52RjMsZIWSBpmKPVuq6xIPe1Qrov7wwt03Eg8YOnrpGKOPvW_iYbItsCNNolPZEWHJnj56HLtksb7pQ";
						
						//message 작성
						Message msg = Message.builder()
										.setAndroidConfig(AndroidConfig.builder()
										.setTtl(3600 * 1000)
										.setPriority(AndroidConfig.Priority.HIGH)
		/*								
										
										.setNotification(AndroidNotification.builder()
											.setTitle(title)
											.setBody(content)
											//.setChannelId("default_channel") 
											//.setIcon("stock_ticker_update")
											//.setColor("#f45342")
											.build())
									
										.build())
										.putData("title",title)
										.putData("body", content)
										.setToken(registrationToken)
										.build();*/
				                        .build())
				                        .putData("title", title)
				                        .putData("body", content)
				                        .putData("hogi", send)
				                        .setToken(registrationToken) // 단일 토큰 사용
				                        .build();
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				//그룹 추가
				@RequestMapping(value="/user/insertGroup", method=RequestMethod.POST)
				@ResponseBody
				public boolean insertGroup(@RequestBody Users users) {
					return userService.insertGroup(users);

				}
				//그룹 삭제
				@RequestMapping(value="/user/deleteGroup", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteGroup(@RequestBody Users users) {
					return userService.deleteGroup(users);

				}
				//그룹 알림 스케줄 추가
				@RequestMapping(value="/user/insertGroupSchedule", method=RequestMethod.POST)
				@ResponseBody
				public String updateTime(@RequestBody Users users) {
					userService.insertGroupSchedule(users); 
				    
				    return "SUCCESS"; 
				}
				//그룹별 조회
				@RequestMapping(value="/user/getGroupUser", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupUser(@RequestBody Users users) {
					//System.out.println("그룹별 조회 컨트롤러");
					//System.out.println("users.getGroup_id()" + users.getGroup_id());
					return userService.getGroupUser(users); 
				}
				//그룹 스케줄 조회
				@RequestMapping(value="/user/getGroupScheduleList", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupScheduleList(Users users) {
					return userService.getGroupScheduleList(users); 
				}
				//그룹 수신 알림 조회
				@RequestMapping(value="/user/getGroupList", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupList(Users users) {
					return userService.getGroupList(users); 
				}
				//그룹별 수신 알람 업데이트
				@RequestMapping(value="/user/updateRecieveAlarm", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateRecieveAlarm(@RequestBody Users users) {
					return userService.updateRecieveAlarm(users); 
				}
				//스케줄 삭제
				@RequestMapping(value="/user/deleteSchedule", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteSchedule(@RequestBody Users users) {
					return userService.deleteSchedule(users); 
				}
				//회원정보 업데이트
				@RequestMapping(value="/user/updateUser", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateUser(@RequestBody Users users) {
					 Users duplicateUser = userService.userDuplicateCheck(users);
					 if(duplicateUser != null) {
						 return false;			 
					 }
					return userService.updateUser(users);
				}
				//회원 삭제
				@RequestMapping(value="/user/deleteUser", method=RequestMethod.POST)
				@ResponseBody
				public boolean deleteUser(@RequestBody Users users) {
					return userService.deleteUser(users);
				}
				//스케줄 업데이트
				@RequestMapping(value="/user/updateGroupSchedule", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateGroupSchedule(@RequestBody Users users) {
					return userService.updateGroupSchedule(users);
				}
				//사람 그룹 이름 조회
				@RequestMapping(value="/user/getGroupName", method=RequestMethod.POST)
				@ResponseBody
				public List<Users> getGroupName(Users users) {
					return userService.getGroupName(users);
				}
				//그룹 이름 업데이트
				@RequestMapping(value="/user/updateGroupName", method=RequestMethod.POST)
				@ResponseBody
				public boolean updateGroupName(@RequestBody Users users) {
					return userService.updateGroupName(users);
				}
}


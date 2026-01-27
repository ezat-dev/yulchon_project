//package com.yulchon.async;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.yulchon.controller.MainController;
//import com.yulchon.controller.UserController;
//
//public class CommProcessor {
//	@Autowired
//	UserController userController;
//	
//	//1초주기로 OPC UA 커넥션이 null일경우 연결
//	@Scheduled(fixedRate = 1000)
//	public void handle() {
//		if(MainController.client == null) {
////			MainController.opcStart();
//			MainController.commCheck();
//		}		
//	}
//}

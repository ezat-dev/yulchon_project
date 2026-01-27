package com.yulchon.util;

public class UtilClass {
	   
	//2024-11-30 추가(설비호기 연산하여 1~4는 서버1, 5~7은 서버2로 리턴)
	public static int serverSelectRtn(String deviceCode){
		int device = Integer.parseInt(deviceCode);
		if(device < 5) {
			return 1;
		}else {
			return 2;
		}
	}
}

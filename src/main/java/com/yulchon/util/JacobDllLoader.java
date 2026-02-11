package com.yulchon.util;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class JacobDllLoader {
	
	private static boolean dllLoaded = false;
	@PostConstruct
    public void init() {
		
        if (dllLoaded) {
            System.out.println("========== JACOB DLL 이미 로드됨 ==========");
            return;
        }
        
        try {
            String dllPath = "D:/stspjt/yulchon_project/src/main/java/jacob/jacob-1.18-x64.dll";
            
            // java.library.path에 DLL 경로 추가
            System.setProperty("jacob.dll.path", dllPath);
            System.setProperty("java.library.path", 
                System.getProperty("java.library.path") + ";" + dllPath);
            System.load(dllPath);
            // 또는 직접 로드
            //System.load(dllPath + "jacob-1.18-x64.dll");
            dllLoaded = true;
            System.out.println("========== JACOB DLL 로드 성공! ==========");
            
        } catch (UnsatisfiedLinkError e) {
            if (e.getMessage().contains("already loaded")) {
                System.out.println("========== JACOB DLL 이미 로드됨 (정상) ==========");
                dllLoaded = true;
            } else {
                System.err.println("!!!!!!!! JACOB DLL 로드 실패 !!!!!!!! ");
                e.printStackTrace();
            }
        }
    }
}

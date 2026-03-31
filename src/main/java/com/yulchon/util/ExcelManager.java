package com.yulchon.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcelManager {
	private static ExcelManager instance;
    private GenericObjectPool<ActiveXComponent> pool;
    
    //파일 경로별 락 관리
    private final ConcurrentHashMap<String, ReentrantLock> fileLocks = new ConcurrentHashMap<>();


    private ExcelManager() {
        ExcelInstanceFactory factory = new ExcelInstanceFactory();
        GenericObjectPoolConfig<ActiveXComponent> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(3); // 엑셀 인스턴스 최대 3개
        config.setBlockWhenExhausted(true); // 3개 다 쓰고 있으면 다음 사람 대기
        config.setMaxWaitMillis(10000); // 최대 10초 대기 후 에러 반환
        
        this.pool = new GenericObjectPool<>(factory, config);
    }

    public static synchronized ExcelManager getInstance() {
        if (instance == null) instance = new ExcelManager();
        return instance;
    }

    // 빌려오기
    public ActiveXComponent borrowExcel() throws Exception {
        return pool.borrowObject();
    }

    // 반납하기
    public void returnExcel(ActiveXComponent excel) {
        if (excel != null) pool.returnObject(excel);
    }
    
    // 파일별 락 가져오기 (없으면 새로 생성)
    public ReentrantLock getFileLock(String filePath) {
        return fileLocks.computeIfAbsent(
            filePath.toLowerCase(), 
            k -> new ReentrantLock()
        );
    }
    
    public Dispatch getWorkbook(ActiveXComponent excel, String filePath) {
        Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
        int count = Dispatch.get(workbooks, "Count").toInt();

        // 현재 엑셀 프로세스에 열려 있는 파일들을 다 뒤져서 같은 경로가 있는지 확인
        for (int i = 1; i <= count; i++) {
            Dispatch wb = Dispatch.call(workbooks, "Item", new Variant(i)).toDispatch();
            String openPath = Dispatch.get(wb, "FullName").toString();
            //System.out.println("열려있는 파일 경로: " + openPath);
            //System.out.println("요청 들어온 파일 경로: " + filePath);
            
            if (openPath.replace("\\", "/").equalsIgnoreCase(filePath.replace("\\", "/"))) {
            	//System.out.println("열었던 엑셀 반환");
                return wb; // 이미 열려있으면 그대로 반환 (재사용)
            }
        }
        // 없으면 새로 Open
        //System.out.println("엑셀 새로 열기");
        return Dispatch.call(workbooks, "Open", filePath).toDispatch();
    }
}

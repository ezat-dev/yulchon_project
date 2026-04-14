package com.yulchon.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcelManager {
    private static ExcelManager instance;
    private final List<ActiveXComponent> excelPool = new ArrayList<>();
    private final ConcurrentHashMap<String, ReentrantLock> fileLocks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ActiveXComponent> fileToExcelCache = new ConcurrentHashMap<>();
    private static final int POOL_SIZE = 3;

    private ExcelManager() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                ActiveXComponent excel = new ActiveXComponent("Excel.Application");
                excel.setProperty("Visible", false);
                excel.setProperty("DisplayAlerts", false);
                excelPool.add(excel);
                System.out.println("엑셀 인스턴스 생성 완료: " + (i+1) + "번");
            } catch (Exception e) {
                System.out.println("엑셀 인스턴스 생성 실패: " + (i+1) + "번 - " + e.getMessage());
            }
        }
        System.out.println("최종 풀 크기: " + excelPool.size());
    }

    public static synchronized ExcelManager getInstance() {
        if (instance == null) instance = new ExcelManager();
        return instance;
    }

    public synchronized ActiveXComponent borrowExcelForFile(String filePath) {
        String normalPath = filePath.replace("\\", "/").toLowerCase();

        // 1. 캐시에서 먼저 찾기 (COM 호출 없음)
        ActiveXComponent cached = fileToExcelCache.get(normalPath);
        if (cached != null) {
            System.out.println("캐시 재사용: " + filePath);
            return cached;
        }

        // 2. 캐시 없으면 파일이 가장 적게 열려있는 인스턴스 반환
        ActiveXComponent least = excelPool.get(0);
        int minCount = Integer.MAX_VALUE;
        for (ActiveXComponent excel : excelPool) {
            try {
                int count = Dispatch.get(excel.getProperty("Workbooks").toDispatch(), "Count").toInt();
                if (count < minCount) {
                    minCount = count;
                    least = excel;
                }
            } catch (Exception e) { /* skip */ }
        }

        // 3. 캐시에 등록
        fileToExcelCache.put(normalPath, least);
        System.out.println("새 인스턴스에 파일 열기: " + filePath);
        return least;
    }

    public ReentrantLock getFileLock(String filePath) {
        return fileLocks.computeIfAbsent(filePath.toLowerCase(), k -> new ReentrantLock());
    }

    public Dispatch getWorkbook(ActiveXComponent excel, String filePath) {
        Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
        int count = Dispatch.get(workbooks, "Count").toInt();

        for (int i = 1; i <= count; i++) {
            Dispatch wb = Dispatch.call(workbooks, "Item", new Variant(i)).toDispatch();
            String openPath = Dispatch.get(wb, "FullName").toString();
            if (openPath.replace("\\", "/").equalsIgnoreCase(filePath.replace("\\", "/"))) {
                return wb;
            }
        }

        return Dispatch.call(workbooks, "Open", filePath).toDispatch();
    }
}

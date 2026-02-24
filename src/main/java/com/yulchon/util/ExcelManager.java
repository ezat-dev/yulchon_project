package com.yulchon.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcelManager {
    private static ExcelManager instance;

    private ActiveXComponent excel;
    private Dispatch workbook;   // ← Workbook 상주
    private String currentPath;  // ← 어떤 템플릿 열려있는지 기억

    private ExcelManager() {
        try {
            this.excel = new ActiveXComponent("Excel.Application");
            this.excel.setProperty("Visible", false);
            this.excel.setProperty("DisplayAlerts", false);

            // 성능 옵션
            this.excel.setProperty("ScreenUpdating", false);
            this.excel.setProperty("EnableEvents", false);

            System.out.println("=== Excel 프로세스 상주 시작 ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ExcelManager getInstance() {
        if (instance == null) {
            instance = new ExcelManager();
        }
        return instance;
    }

    public ActiveXComponent getExcel() {
        return excel;
    }

    /**
     * Workbook 재사용
     */
    public synchronized Dispatch getWorkbook(String path) {

        try {
            // 이미 같은 템플릿 열려있으면 재사용
            if (workbook != null && path.equals(currentPath)) {
                return workbook;
            }

            // 다른 템플릿 열려있으면 닫기
            if (workbook != null) {
                Dispatch.call(workbook, "Close", false);
                workbook = null;
            }

            Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
            workbook = Dispatch.call(workbooks, "Open", path).toDispatch();
            
            Dispatch.put(excel, "Calculation", new Variant(-4135));

            
            currentPath = path;

            return workbook;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 서버 종료 시 Excel 종료
     */
    public void quit() {
        try {
            if (workbook != null) {
                Dispatch.call(workbook, "Close", false);
            }
            if (excel != null) {
                excel.invoke("Quit");
            }
        } catch (Exception ignore) {
        }
    }
}

package com.yulchon.controller;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

@Controller
public class PrintController {

    //private static final String DLL_PATH = "D:/stspjt/yulchon/src/main/java/jacob/jacob-1.18-x64.dll"; //VM arguments에서 실행함
    private static final String FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_테스트_양식.xlsx";
    
	/*
	 * @PostConstruct public void loadJacobDll() {
	 * System.out.println("jacob.dll 로드"); //접근 테스트 File f = new File(DLL_PATH);
	 * System.out.println("DLL exists=" + f.exists() + ", path=" +
	 * f.getAbsolutePath()); System.out.println(System.getProperty("java.version"));
	 * System.out.println(System.getProperty("os.arch"));
	 * System.out.println(System.getProperty("java.vm.name"));
	 * System.out.println(System.getProperty("java.library.path"));
	 * 
	 * System.load(DLL_PATH); // 서버 시작 시 1회만 }
	 */
	//인쇄 테스트
	@RequestMapping(value="/test/autoPrint", method=RequestMethod.POST)
	@ResponseBody
    public boolean autoPrint() {
		System.out.println("인쇄 함수");
        ActiveXComponent excel = null;
        Dispatch workbooks = null;
        Dispatch workbook = null;
        Dispatch sheet = null;

        try {
            excel = new ActiveXComponent("Excel.Application");
            excel.setProperty("Visible", false);
            

            workbooks = excel.getProperty("Workbooks").toDispatch();
            workbook = Dispatch.call(workbooks, "Open", FILE_PATH).toDispatch();

            //첫 번째 셀
            sheet = Dispatch.call(workbook, "Worksheets", 1).toDispatch();

            //값 넣기
            Dispatch cell = Dispatch.call(sheet, "Range", "C4").toDispatch();
            Dispatch.put(cell, "Value", new Variant("test spec"));
            cell = Dispatch.call(sheet, "Range", "C5").toDispatch();
            Dispatch.put(cell, "Value", new Variant("test size"));
            cell = Dispatch.call(sheet, "Range", "C6").toDispatch();
            Dispatch.put(cell, "Value", new Variant("test length"));
            cell = Dispatch.call(sheet, "Range", "E6").toDispatch();
            Dispatch.put(cell, "Value", new Variant("test Lot No"));
            cell = Dispatch.call(sheet, "Range", "E7").toDispatch();
            Dispatch.put(cell, "Value", new Variant("test date"));
            
            Dispatch.call(workbook, "PrintOut");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (workbook != null) {
                    Dispatch.call(workbook, "Close", false);
                }
            } catch (Exception ignore) {
            }
            try {
                if (excel != null) {
                    excel.invoke("Quit");
                }
            } catch (Exception ignore) {
            }
        }
    }
}

package com.yulchon.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yulchon.domain.Management;
import com.yulchon.service.ManagementService;
import com.yulchon.util.PrintExcel;

@Controller
public class ManagementController {

	@Autowired
	private ManagementService managementService;

	private static final String FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_테스트_양식.xlsx";
	private static final String KAB_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KAB_양식.xlsx";
	private static final String KCB_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KCB_양식.xlsx";
	private static final String KKB_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KKB_양식.xlsx";
	private static final String SANKIN_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_SANKIN_양식.xlsx";
	private static final String KKM_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KKM_양식.xlsx";
	private static final String KOB_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KOB_양식.xlsx";
	private static final String CASH_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_CASH_양식.xlsx";
	private static final String ELM2_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_ELM2_양식.xlsx";
	private static final String KEEPRO_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KEEPRO_양식.xlsx";
	private static final String MBI_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_MBI_양식.xlsx";
	private static final String MMP_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_MMP_양식.xlsx";
	private static final String NOK_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_NOK_양식.xlsx";
	private static final String NST_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_NST_양식.xlsx";
	private static final String PROFENDER_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_PROFENDER_양식.xlsx";
	private static final String DKK_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_DKK_양식.xlsx";
	private static final String KTH_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KTH_양식.xlsx";

	private String getShippingMarkFilePath(String companyName) {
		if(companyName.contains("KEEPRO")) {
			return KEEPRO_FILE_PATH;
		}else if(companyName.contains("KAB")) {
			return KAB_FILE_PATH;
		}
		return "";
	}

	// 패킹리스트/재고현황 페이지 이동
	@RequestMapping(value = "/management/inventoryPackingList", method = RequestMethod.GET)
	public String inventoryPackingListPage() {
		return "/management/inventoryPackingList.jsp";
	}

	// 재고 리스트 조회
	@RequestMapping(value = "/management/getInventoryList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getInventoryList(Management management) {
		return managementService.getInventoryList(management);
	}

	// 인보이스 리스트 조회
	@RequestMapping(value = "/management/getInvoiceList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getInvoiceList(Management management) {
		List<Management> datas = managementService.getInvoiceList(management);
		//System.out.println("인보이스 리스트: " + datas);
		return datas;
	}

	// 인보이스 생성
	@RequestMapping(value = "/management/insertInvoiceName", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean insertInvoiceName(Management management) {
		return managementService.insertInvoiceName(management);
	}

	// 재고처리 페이지 이동
	@RequestMapping(value = "/management/inventoryProcessing", method = RequestMethod.GET)
	public String inventoryProcessingPage() {
		return "/management/inventoryProcessing.jsp";
	}	 

	// 패킹리스트/재고현황 페이지 이동
	@RequestMapping(value = "/management/inventoryHistory", method = RequestMethod.GET)
	public String inventoryHistoryPage() {
		return "/management/inventoryHistory.jsp";
	}	

	// 스캔 페이지 이동
	@RequestMapping(value = "/management/mobile/scan", method = RequestMethod.GET)
	public String scanPage() {
		return "/management/scan.jsp";
	}

	// 쉬핑마크 출력 페이지 이동
	@RequestMapping(value = "/management/mobile/shippingMarkPrint", method = RequestMethod.GET)
	public String shippingMarkPrintPage(
			@RequestParam(value="lbl_lot_no", required=true) String lbl_lot_no, 
			@RequestParam(value="selectedInvoiceNo", required=true) String selectedInvoiceNo,
			@RequestParam(value="selectedInvoiceName", required=true) String selectedInvoiceName,
			Model model, Management management, HttpServletResponse response) throws Exception {

		System.out.println("selectedInvoiceNo: " + selectedInvoiceNo);
		System.out.println("selectedInvoiceName: " + selectedInvoiceName);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		management.setLbl_lot_no(lbl_lot_no);

		Management data = managementService.mobileGetShippingMarkPrintInventory(management);
		System.out.println("출력 조회 데이터: " + data);

		if (data == null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('품목을 찾을 수 없습니다. 다시 스캔해주세요.'); history.back();</script>");
			out.flush();
			return null; // 스크립트를 직접 실행하므로 뷰 이름을 반환하지 않음
		}

		model.addAttribute("data", data);
		model.addAttribute("selectedInvoiceNo", selectedInvoiceNo);
		model.addAttribute("selectedInvoiceName", selectedInvoiceName);
		return "/management/shippingMarkPrint.jsp";
	}

	// 출하 취소 페이지 이동
	@RequestMapping(value = "/management/mobile/shippingCancel", method = RequestMethod.GET)
	public String shippingCancelPage(@RequestParam(value="lbl_lot_no", required=false) String lbl_lot_no, 
			Model model, Management management, HttpServletResponse response) throws Exception {
		management.setLbl_lot_no(lbl_lot_no);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		Management data = managementService.getShippingMarkPrintInventory(management);
		System.out.println("출력 조회 데이터: " + data);
		if (data == null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('인보이스 부여된 품목이 아닙니다.'); history.back();</script>");
			out.flush();
			return null; // 스크립트를 직접 실행하므로 뷰 이름을 반환하지 않음
		}
		model.addAttribute("data", data);
		return "/management/shippingCancel.jsp";
	}

	// 제품확인 페이지 이동
	@RequestMapping(value = "/management/mobile/productConfirm", method = RequestMethod.GET)
	public String productConfirmPage(@RequestParam(value="lbl_lot_no", required=false) String lbl_lot_no, 
			Model model, Management management, HttpServletResponse response) throws Exception {
		management.setLbl_lot_no(lbl_lot_no);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		Management data = managementService.getShippingMarkPrintInventory(management);
		System.out.println("출력 조회 데이터: " + data);
		if (data == null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('인보이스 부여된 품목이 아닙니다.'); history.back();</script>");
			out.flush();
			return null; // 스크립트를 직접 실행하므로 뷰 이름을 반환하지 않음
		}
		model.addAttribute("data", data);
		return "/management/productConfirm.jsp";
	}

	// 쉬핑마크 관리 페이지 이동
	@RequestMapping(value = "/management/shippingMarkManage", method = RequestMethod.GET)
	public String shippingMarkManagePage() {
		return "/management/shippingMarkManage.jsp";
	}

	//가장 최근에 생긴 인보이스 조회
	@RequestMapping(value = "/management/getRecentInvoice", method = RequestMethod.POST) 
	@ResponseBody 
	public Management getRecentInvoice(Management management) {
		return managementService.getRecentInvoice(management);
	}

	//재고 로트번호 동기화 프로시저
	@RequestMapping(value = "/management/syncEzInventoryLotExt", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean syncEzInventoryLotExt(Management management) {
		try {
			managementService.syncEzInventoryLotExt(management);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	//인보이스 클릭시 해당하는 품목 조회
	@RequestMapping(value = "/management/getInvoiceInventoryList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getInvoiceInventoryList(Management management) {
		//미처리 된건 미처리 테이블에서 조회
		if("R".equals(management.getInvoice_is_shipped())) {
			System.out.println("리셋 데이터 조회 시작");
			List<Management> datas = managementService.getResetDatas(management);
			System.out.println("리셋 데이터 개수: " + datas.size());
			return datas;
		}
		return managementService.getInvoiceInventoryList(management);
	}

	//인보이스에 품목 추가
	@RequestMapping(value = {"/management/insertInvoiceInventory", 
	"/management/mobile/insertInvoiceInventory"}, method = RequestMethod.POST) 
	@ResponseBody 
	public boolean insertInvoiceInventory(@RequestBody Management management) {
		return managementService.insertInvoiceInventory(management);
	}

	//인보이스에 품목 삭제
	@RequestMapping(value = "/management/deleteInvoiceInventory", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean deleteInvoiceInventory(@RequestBody Management management) {
		return managementService.deleteInvoiceInventory(management);
	}

	//쉬핑마크 출력
	@RequestMapping(value="/management/mobile/printShippingMark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> autoPrint(@RequestBody Management management) {
		Map<String, Object> resultMap = new HashMap<>();
		System.out.println("인쇄 함수");
		Management data = managementService.getShippingMarkPrintInventory(management);
		//String file_path = getShippingMarkFilePath(data.getNm_customer());
		PrintExcel printExcel = new PrintExcel();
		if (data == null) {
			resultMap.put("result", false);
			resultMap.put("message", "출력할 데이터를 찾을 수 없습니다.\n다시 스캔해주세요.");
			return resultMap;
		}
		String customerName = data.getNm_customer();
		String file_path = getShippingMarkFilePath(customerName);
		
		//테스트용으로 해놓음
		customerName = "KTH";
		
		Map<String, Object> printResult = new HashMap<>();
		if(customerName.contains("KAB")) {
			printResult = printExcel.printKoideKab(data, KAB_FILE_PATH);
		}else if(customerName.contains("KCB")) {
			printResult = printExcel.printKoideKcb(data, KCB_FILE_PATH);
		}else if(customerName.contains("KKB")) {
			printResult = printExcel.printKoideKkb(data, KKB_FILE_PATH);
		}else if(customerName.contains("SANKIN")) {
			printResult = printExcel.printKoideSankin(data, SANKIN_FILE_PATH);
		}else if(customerName.contains("KKM")) {
			printResult = printExcel.printKkm(data, KKM_FILE_PATH);
		}else if(customerName.contains("KOB")) {
			printResult = printExcel.printKob(data, KOB_FILE_PATH);
		}else if(customerName.contains("CASH")) {
			printResult = printExcel.printCash(data, CASH_FILE_PATH);
		}else if(customerName.contains("ELM2")) {
			printResult = printExcel.printElm2(data, ELM2_FILE_PATH);
		}else if(customerName.contains("KEEPRO")) {
			printResult = printExcel.printKeepro(data, KEEPRO_FILE_PATH);
		}else if(customerName.contains("MBI")) {
			printResult = printExcel.printMbi(data, MBI_FILE_PATH);
		}else if(customerName.contains("MMP")) {
			printResult = printExcel.printMmp(data, MMP_FILE_PATH);
		}else if(customerName.contains("NOK")) {
			printResult = printExcel.printNok(data, NOK_FILE_PATH);
		}else if(customerName.contains("NST")) {
			printResult = printExcel.printNst(data, NST_FILE_PATH);
		}else if(customerName.contains("PROFENDER")) {
			printResult = printExcel.printProfender(data, PROFENDER_FILE_PATH);
		}else if(customerName.contains("DKK")) {
			printResult = printExcel.printDkk(data, DKK_FILE_PATH);
		}else if(customerName.contains("KTH")) {
			printResult = printExcel.printKth(data, KTH_FILE_PATH);
		}else {
			resultMap.put("result", false);
			resultMap.put("message", "고객사 양식이 없습니다.\n다시 확인해주세요");
		}
		
		if ((boolean) printResult.get("result")) {
	        boolean isInserted = managementService.insertShippingList(data);
	        
	        if(isInserted) {
	            resultMap.put("result", true);
	            resultMap.put("message", "출력 요청 및 출하목록 추가 완료");
	        } else {
	            resultMap.put("result", false);
	            resultMap.put("message", "출력 요청 완료\n잠시만 기다려 주세요.");
	        }
	    } else {
	        // 인쇄 자체가 실패한 경우
	        resultMap = printResult;
	    }



		return resultMap;
	}


	//쉬핑마크 출력 후 출하목록에 추가
	@RequestMapping(value = "/management/mobile/insertShippingList", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean insertShippingList(@RequestBody Management management) {
		return managementService.insertShippingList(management);
	}

	//모바일 출하취소(출하목록에서만 삭제)
	@RequestMapping(value = "/management/mobile/deleteShippingList", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean mobileDeleteShippingList(@RequestBody Management management) {
		return managementService.deleteShippingList(management);
	}

	//인보이스 클릭시 해당하는 출하목록 조회
	@RequestMapping(value = "/management/getShippingList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getShippingList(Management management) {
		return managementService.getShippingList(management);
	}

	//웹 출하목록 품목 삭제
	@RequestMapping(value = "/management/deleteShippingListInventory", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean deleteShippingListInventory(@RequestBody Management management) {
		try {
			boolean flag1 = managementService.deleteInvoiceInventory(management);
			boolean flag2 = managementService.deleteShippingListInventory(management);
			System.out.println("flag1: " + flag1);
			System.out.println("flag2: " + flag2);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//웹 출하목록 출하취소
	@RequestMapping(value = "/management/cancelShippingList", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean cancelShippingList(@RequestBody Management management) {
		try {
			//취소테이블 저장
			boolean flag0 = managementService.insertShippingCancel(management);
			//출하목록에서 삭제
			boolean flag1 =  managementService.cancelShippingList(management);
			//인보이스 "N" 업데이트
			boolean flag2 = managementService.cancelInvoiceList(management);
			//매핑테이블 삭제
			boolean flag3 = managementService.cancelInvoiceInventory(management);

			System.out.println("취소테이블 저장: " + flag0);
			System.out.println("취소테이블 저장: " + flag1);
			System.out.println("취소테이블 저장: " + flag2);
			System.out.println("취소테이블 저장: " + flag3);

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 출하처리 안된 인보이스 리스트 조회
	@RequestMapping(value = "/management/getNoUpdatedInvoiceList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getUpdatedInvoiceList(Management management) {
		return managementService.getNoUpdatedInvoiceList(management);
	}

	//인보이스에 품목에 고객사 부여 품번 업데이트
	@RequestMapping(value = "/management/updateCustomerProductCodeNumber", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean updateCustomerProductCodeNumber(@RequestBody Management management) {
		return managementService.updateCustomerProductCodeNumber(management);
	}

	//출하완료
	@RequestMapping(value = "/management/shippingComplete", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean shippingComplete(@RequestBody Management management) {
		//스캔한 품목들만 출하완료 테이블에 저장
		boolean flag1 =   managementService.insertShippingResult(management);

		//출하완료 'Y'로 업데이트
		boolean flag2 = managementService.updateCompleteInvoiceList(management);

		//인보이스/품목 매핑 테이블에서 삭제
		boolean flag3 = managementService.deleteNoScanInventory(management);

		System.out.println("출하완료 테이블 저장: " + flag1);
		System.out.println("인보이스 출하완료로 업데이트: " + flag2);
		System.out.println("인보이스,품목 매핑 테이블에서 삭제: " + flag3);

		if(flag1 && flag2 && flag3) {
			return true;
		}
		return false;
	}

	//출하완료된 품목 인보이스별 조회
	@RequestMapping(value = "/management/getCompleteInventoryList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getCompleteInventoryList(Management management) {
		if("Y".equals(management.getInvoice_is_shipped())) {
			return managementService.getCompleteInventoryList(management);
		}else if("N".equals(management.getInvoice_is_shipped())) {
			return managementService.getCancelInventoryList(management);
		}else if("R".equals(management.getInvoice_is_shipped())) {
			return managementService.getResetInventoryList(management);
		}
		//빈 배열 반환
		return new java.util.ArrayList<Management>();
	}

	// 출하처리 안된 인보이스 리스트 조회
	@RequestMapping(value = "/management/mobile/getNoUpdatedInvoiceList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> mobileGetUpdatedInvoiceList(Management management) {
		return managementService.getNoUpdatedInvoiceList(management);
	}

	//초기화 되었거나 처리 안된 인보이스 조회
	@RequestMapping(value = "/management/getNoUpdatedOrResetInvoiceList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getNoUpdatedOrResetInvoiceList(Management management) {
		return managementService.getNoUpdatedOrResetInvoiceList(management);
	}
	
	//미처리 된 인보이스 중 데이터 이관당한거 칼럼 업데이트
	@RequestMapping(value = "/management/updateInvoiceIsMoved", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean updateInvoiceIsMoved(@RequestBody Management management) {
		return managementService.updateInvoiceIsMoved(management);
	}
	
	//고객사 조회
	@RequestMapping(value = "/management/getCustomerList", method = RequestMethod.POST) 
	@ResponseBody 
	public List<Management> getCustomerList(Management management) {
		return managementService.getCustomerList(management);
	}
	
	//쉬핑마크 양식 삭제 및 업로드
	@RequestMapping(value = "/management/deleteAndUploadShippingMark", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean deleteAndUploadShippingMark(Management management,
			@RequestParam("file") MultipartFile file,
			HttpSession session) {
		String basePath = "D:/율촌_쉬핑마크_양식/";
		String oldFileName = management.getOld_file_name();
		
		try {
	        // 1. 기존 파일 삭제 (값이 있을 때만)
	        if (oldFileName != null && !oldFileName.isEmpty()) {
	            File oldFile = new File(basePath + oldFileName);
	            if (oldFile.exists()) {
	                oldFile.delete(); 
	            }
	        }

	        // 2. 신규 파일 저장
	        String newFileName = file.getOriginalFilename();
	        File targetFile = new File(basePath + newFileName);
	        file.transferTo(targetFile);
	        
	        management.setCustomer_shippingmark_file_name(newFileName);
	        management.setUpdate_user_id((String)session.getAttribute("loginUserId"));
	        
	        return managementService.updateShippingMarkFile(management);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
	}
	
	//고객사 비고 업데이트
	@RequestMapping(value = "/management/updateCustomerRemark", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean updateCustomerRemark(@RequestBody Management management) {
		return managementService.updateCustomerRemark(management);
	}
	
	//고객사 추가
	@RequestMapping(value = "/management/insertCustomer", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean insertCustomer(Management management,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		String basePath = "D:/율촌_쉬핑마크_양식/";
		String newFileName = "";
		try {

	        // 신규 파일 저장
			if(file != null) {
	        newFileName = file.getOriginalFilename();
	        File targetFile = new File(basePath + newFileName);
	        file.transferTo(targetFile);
			}
	        management.setCustomer_shippingmark_file_name(newFileName);
	        management.setUpdate_user_id((String)session.getAttribute("loginUserId"));
	        
	        return managementService.insertCustomer(management);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
	}
	
	//쉬핑마크 양식 클릭해서 다운로드
	@RequestMapping(value = "/management/downloadShippingMark", method = RequestMethod.GET) 
	@ResponseBody 
	public void downloadFile(@RequestParam("fileName") String fileName,
			HttpServletResponse response) {
	    String filePath = "D:/율촌_쉬핑마크_양식/" + fileName;
	    File file = new File(filePath);

	    if (!file.exists()) {
	        return;
	    }

	    try {
	        // 1. 파일명을 브라우저가 인식할 수 있게 인코딩 (한글 깨짐 방지)
	        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
	        
	        // 2. 응답 헤더 설정
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
	        response.setContentLength((int) file.length());

	        // 3. 파일을 읽어서 클라이언트에 전송
	        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
	            
	            byte[] buffer = new byte[8192];
	            int bytesRead;
	            while ((bytesRead = bis.read(buffer)) != -1) {
	                bos.write(buffer, 0, bytesRead);
	            }
	            bos.flush();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}


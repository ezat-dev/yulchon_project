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
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.yulchon.util.LogAspect;
import com.yulchon.util.PreviewExcel;
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
	private static final String KPS_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KPS_양식.xlsx";
	private static final String KMEX_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_KMEX_양식.xlsx";
	private static final String THAIAUTO_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_THAIAUTO_양식.xlsx";
	private static final String PIONEER_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_PIONEER_양식.xlsx";
	private static final String PCT_FILE_PATH = "D:/율촌_쉬핑마크_양식/쉬핑마크_PCT_양식.xlsx";

	private static final Logger logger = Logger.getLogger(LogAspect.class);
	
	private String getShippingMarkFilePath(String companyName) {
		if(companyName.contains("KEEPRO")) {
			return KEEPRO_FILE_PATH;
		}else if(companyName.contains("KAB")) {
			return KAB_FILE_PATH;
		}
		return "";
	}
	
	//고객사 db조회하려고 추가
	private static final String BASE_FILE_PATH = "D:/율촌_쉬핑마크_양식/";

	private final PrintExcel printExcel = new PrintExcel();
	private final PreviewExcel previewExcel = new PreviewExcel();

	// customer_key와 다른 키워드로 매칭해야 하는 예외 케이스만 관리
	private static final Map<String, String> KEYWORD_OVERRIDE_MAP = new HashMap<>();
	static {
	    KEYWORD_OVERRIDE_MAP.put("아이엠에스", "NST");
	    KEYWORD_OVERRIDE_MAP.put("PROFENDER CORPORATION", "PCT");
	}

	private Map<String, BiFunction<Management, String, Map<String, Object>>> PRINT_METHOD_MAP;
	private Map<String, BiFunction<Management, String, byte[]>> PREVIEW_METHOD_MAP;

	@PostConstruct
	public void initMethodMaps() {
	    PRINT_METHOD_MAP = new HashMap<>();
	    PRINT_METHOD_MAP.put("KAB",       (data, path) -> printExcel.printKoideKab(data, path));
	    PRINT_METHOD_MAP.put("KCB",       (data, path) -> printExcel.printKoideKcb(data, path));
	    PRINT_METHOD_MAP.put("KKB",       (data, path) -> printExcel.printKoideKkb(data, path));
	    PRINT_METHOD_MAP.put("SANKIN",    (data, path) -> printExcel.printKoideSankin(data, path));
	    PRINT_METHOD_MAP.put("KKM",       (data, path) -> printExcel.printKkm(data, path));
	    PRINT_METHOD_MAP.put("KOB",       (data, path) -> printExcel.printKob(data, path));
	    PRINT_METHOD_MAP.put("CASH",      (data, path) -> printExcel.printCash(data, path));
	    PRINT_METHOD_MAP.put("ELM2",      (data, path) -> printExcel.printElm2(data, path));
	    PRINT_METHOD_MAP.put("KEEPRO",    (data, path) -> printExcel.printKeepro(data, path));
	    PRINT_METHOD_MAP.put("MBI",       (data, path) -> printExcel.printMbi(data, path));
	    PRINT_METHOD_MAP.put("MMP",       (data, path) -> printExcel.printMmp(data, path));
	    PRINT_METHOD_MAP.put("NOK",       (data, path) -> printExcel.printNok(data, path));
	    PRINT_METHOD_MAP.put("NST",       (data, path) -> printExcel.printNst(data, path));
	    PRINT_METHOD_MAP.put("PROFENDER", (data, path) -> printExcel.printProfender(data, path));
	    PRINT_METHOD_MAP.put("DKK",       (data, path) -> printExcel.printDkk(data, path));
	    PRINT_METHOD_MAP.put("KTH",       (data, path) -> printExcel.printKth(data, path));
	    PRINT_METHOD_MAP.put("KPS",       (data, path) -> printExcel.printKps(data, path));
	    PRINT_METHOD_MAP.put("KMEX",      (data, path) -> printExcel.printKmex(data, path));
	    PRINT_METHOD_MAP.put("타이오토",  (data, path) -> printExcel.printThaiAuto(data, path));
	    PRINT_METHOD_MAP.put("PIONEER",   (data, path) -> printExcel.printPioneer(data, path));
	    PRINT_METHOD_MAP.put("PCT",       (data, path) -> printExcel.printProfenderPct(data, path));
	    
	    // PREVIEW_METHOD_MAP 초기화
	    PREVIEW_METHOD_MAP = new HashMap<>();
	    PREVIEW_METHOD_MAP.put("KAB",       (data, path) -> previewExcel.previewKoideKab(data, path));
	    PREVIEW_METHOD_MAP.put("KCB",       (data, path) -> previewExcel.previewKoideKcb(data, path));
	    PREVIEW_METHOD_MAP.put("KKB",       (data, path) -> previewExcel.previewKoideKkb(data, path));
	    PREVIEW_METHOD_MAP.put("SANKIN",    (data, path) -> previewExcel.previewKoideSankin(data, path));
	    PREVIEW_METHOD_MAP.put("KKM",       (data, path) -> previewExcel.previewKkm(data, path));
	    PREVIEW_METHOD_MAP.put("KOB",       (data, path) -> previewExcel.previewKob(data, path));
	    PREVIEW_METHOD_MAP.put("CASH",      (data, path) -> previewExcel.previewCash(data, path));
	    PREVIEW_METHOD_MAP.put("ELM2",      (data, path) -> previewExcel.previewElm2(data, path));
	    PREVIEW_METHOD_MAP.put("KEEPRO",    (data, path) -> previewExcel.previewKeepro(data, path));
	    PREVIEW_METHOD_MAP.put("MBI",       (data, path) -> previewExcel.previewMbi(data, path));
	    PREVIEW_METHOD_MAP.put("MMP",       (data, path) -> previewExcel.previewMmp(data, path));
	    PREVIEW_METHOD_MAP.put("NOK",       (data, path) -> previewExcel.previewNok(data, path));
	    PREVIEW_METHOD_MAP.put("NST",       (data, path) -> previewExcel.previewNst(data, path));
	    PREVIEW_METHOD_MAP.put("PROFENDER", (data, path) -> previewExcel.previewProfender(data, path));
	    PREVIEW_METHOD_MAP.put("DKK",       (data, path) -> previewExcel.previewDkk(data, path));
	    PREVIEW_METHOD_MAP.put("KTH",       (data, path) -> previewExcel.previewKth(data, path));
	    PREVIEW_METHOD_MAP.put("KPS",       (data, path) -> previewExcel.previewKps(data, path));
	    PREVIEW_METHOD_MAP.put("KMEX",      (data, path) -> previewExcel.previewKmex(data, path));
	    PREVIEW_METHOD_MAP.put("타이오토",  (data, path) -> previewExcel.previewThaiAuto(data, path));
	    PREVIEW_METHOD_MAP.put("PIONEER",   (data, path) -> previewExcel.previewPioneer(data, path));
	    PREVIEW_METHOD_MAP.put("PCT",       (data, path) -> previewExcel.previewProfenderPct(data, path));
	    // 양식 없는 고객사는 넣지 않음 - null 체크로 400 반환
	}

	// 양식 없는 고객사 공통 결과
	private Map<String, Object> noFormResult() {
	    Map<String, Object> r = new HashMap<>();
	    r.put("result", true);
	    r.put("noForm", true);
	    return r;
	}
	
	private Management findMatchedCustomer(String customerName, List<Management> customerList) {
	    String overrideKey = KEYWORD_OVERRIDE_MAP.entrySet().stream()
	        .filter(entry -> customerName.contains(entry.getKey()))
	        .map(Map.Entry::getValue)
	        .findFirst()
	        .orElse(null);

	    if (overrideKey != null) {
	        return customerList.stream()
	            .filter(c -> overrideKey.equals(c.getCustomer_key()))
	            .findFirst()
	            .orElse(null);
	    }

	    return customerList.stream()
	        .filter(c -> c.getCustomer_key() != null
	                  && customerName.contains(c.getCustomer_key()))
	        .findFirst()
	        .orElse(null);
	}

	//-----------------------------------------------------------------------------------//
	
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
			Model model, Management management, HttpServletResponse response,
			HttpServletRequest request) throws Exception {

		//System.out.println("selectedInvoiceNo: " + selectedInvoiceNo);
		//System.out.println("selectedInvoiceName: " + selectedInvoiceName);
		//System.out.println("조회 Lot No: " + lbl_lot_no);
		management.setLbl_lot_no(lbl_lot_no);

		Management data = managementService.mobileGetShippingMarkPrintInventory(management);
		//System.out.println("출력 조회 데이터: " + data);

		if (data == null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

		    String mode = request.getParameter("mode");
		    if ("pda".equals(mode)) {
		        out.println("<script>" +
		                "location.href='/yulchon/management/mobile/scan" +
		                "?mode=pda" +
		                "&invoiceNo=" + selectedInvoiceNo +
		                "&invoiceName=" + java.net.URLEncoder.encode(selectedInvoiceName, "UTF-8") +
		                "&errorMsg=" + java.net.URLEncoder.encode("품목을 찾을 수 없습니다. 다시 스캔해주세요.", "UTF-8") +
		                "';</script>");
		    } else {
		        out.println("<script>alert('품목을 찾을 수 없습니다. 다시 스캔해주세요.'); history.back();</script>");
		    }
		    
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
		//System.out.println("조회 Lot No: " + lbl_lot_no);
		Management data = managementService.getShippingMarkPrintInventory(management);
		//System.out.println("출력 조회 데이터: " + data);
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
		//System.out.println("조회 Lot No: " + lbl_lot_no);
		
		Management data = managementService.getProductConfirm(management);
		//System.out.println("출력 조회 데이터: " + data);
		
		/*
		 * if (data == null) { response.setContentType("text/html; charset=UTF-8");
		 * PrintWriter out = response.getWriter(); out.
		 * println("<script>alert('품목을 조회하지 못했습니다.\n다시 스캔해주세요.'); history.back();</script>"
		 * ); out.flush(); return null; // 스크립트를 직접 실행하므로 뷰 이름을 반환하지 않음 }
		 */
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
			//System.out.println("리셋 데이터 조회 시작");
			List<Management> datas = managementService.getResetDatas(management);
			//System.out.println("리셋 데이터 개수: " + datas.size());
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
		List<Management> datas = managementService.getShippingDatas(management);
		//System.out.println("스캔한거 있는지: " + datas.size());
		if(datas.size() > 0) {
			return false;
		}
		return managementService.deleteInvoiceInventory(management);
	}
/*
	//쉬핑마크 출력
	@RequestMapping(value="/management/mobile/printShippingMark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> autoPrint(@RequestBody Management management) {
		long start = System.nanoTime();

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
		//customerName = "CASH";

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
		}else if(customerName.contains("NST") || customerName.contains("아이엠에스")) {
			printResult = printExcel.printNst(data, NST_FILE_PATH);
		}else if(customerName.contains("PROFENDER")) {
			printResult = printExcel.printProfender(data, PROFENDER_FILE_PATH);
		}else if(customerName.contains("DKK")) {
			printResult = printExcel.printDkk(data, DKK_FILE_PATH);
		}else if(customerName.contains("KTH")) {
			printResult = printExcel.printKth(data, KTH_FILE_PATH);
		}else if(customerName.contains("KPS")) {
			printResult = printExcel.printKps(data, KPS_FILE_PATH);
		}else if(customerName.contains("KMEX")) {
			printResult = printExcel.printKmex(data, KMEX_FILE_PATH);
		}else if(customerName.contains("THAI AUTO")) {
			printResult = printExcel.printThaiAuto(data, THAIAUTO_FILE_PATH);
		}else if(customerName.contains("PIONEER")) {
			printResult = printExcel.printPioneer(data, PIONEER_FILE_PATH);
		}else if(customerName.contains("Profender")) {
			printResult = printExcel.printProfenderPct(data, PCT_FILE_PATH);
		} else if(customerName.contains("ROCS") || customerName.contains("ASTEMO") 
				|| customerName.contains("SCHIAVELLO")) {
		    // 출력 없이 출하목록만 추가
			System.out.println("양식 없는 고객사 출력요청");
		    printResult.put("result", true);
		    printResult.put("noForm", true);
		}else {
			resultMap.put("result", false);
			resultMap.put("message", "등록되지 않은 고객사의 품목입니다.");
			return resultMap;
		}

		if ((boolean) printResult.get("result")) {
			boolean isInserted = managementService.insertShippingList(data);
			boolean noForm = printResult.get("noForm") != null && (boolean) printResult.get("noForm");
			if(isInserted) {
				resultMap.put("result", true);
				resultMap.put("message", noForm ? "출하목록 추가 완료" : "출력 요청 및 출하목록 추가 완료");
			} else {
				resultMap.put("result", false);
				resultMap.put("message", noForm ? "출하목록에 이미 추가된 품목입니다." : "이미 출력된 품목입니다.\n재출력 요청이 완료되었습니다.");
			}
		} else {
			// 인쇄 자체가 실패한 경우
			resultMap = printResult;
		}


		long end = System.nanoTime();   // 끝 시간
		System.out.println("⏱ 실행시간(ms): " + (end - start)/1000000);

		return resultMap;
	}
*/
	@RequestMapping(value="/management/mobile/printShippingMark", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> autoPrint(@RequestBody Management management) {
	    long start = System.nanoTime();
	    Map<String, Object> resultMap = new HashMap<>();

	    Management data = managementService.getShippingMarkPrintInventory(management);
	    if (data == null) {
	        resultMap.put("result", false);
	        resultMap.put("message", "출력할 데이터를 찾을 수 없습니다.\n다시 스캔해주세요.");
	        return resultMap;
	    }

	    String customerName = data.getNm_customer();
	    //System.out.println("고객사명: " + customerName);
	    List<Management> customerList = managementService.getCustomerList(management);
	    Management matched = findMatchedCustomer(customerName, customerList);
	    //System.out.println("matched: " + (matched != null ? matched.getCustomer_key() : "null"));

	    if (matched == null) {
	        resultMap.put("result", false);
	        resultMap.put("message", "등록되지 않은 고객사의 품목입니다.");
	        return resultMap;
	    }

	    // 파일명 없으면 자동으로 양식 없는 고객사 처리
	    if (matched.getCustomer_shippingmark_file_name() == null || matched.getCustomer_shippingmark_file_name().trim().isEmpty()) {
	        boolean isInserted = managementService.insertShippingList(data);
	        resultMap.put("result", true);
	        resultMap.put("message", isInserted ? "출하목록 추가 완료" : "출하목록에 이미 추가된 품목입니다.");
	        return resultMap;
	    }

	    String customerKey = matched.getCustomer_key();
	    String filePath = BASE_FILE_PATH + matched.getCustomer_shippingmark_file_name();

	    BiFunction<Management, String, Map<String, Object>> printMethod = PRINT_METHOD_MAP.get(customerKey);
	    if (printMethod == null) {
	        resultMap.put("result", false);
	        resultMap.put("message", "등록되지 않은 고객사의 품목입니다.");
	        return resultMap;
	    }

	    Map<String, Object> printResult = printMethod.apply(data, filePath);

	    if ((boolean) printResult.get("result")) {
	        boolean isInserted = managementService.insertShippingList(data);
	        boolean noForm = printResult.get("noForm") != null && (boolean) printResult.get("noForm");
	        if (isInserted) {
	            resultMap.put("result", true);
	            resultMap.put("message", noForm ? "출하목록 추가 완료" : "출력 요청 및 출하목록 추가 완료");
	        } else {
	            resultMap.put("result", false);
	            resultMap.put("message", noForm ? "출하목록에 이미 추가된 품목입니다." : "이미 출력된 품목입니다.\n재출력 요청이 완료되었습니다.");
	        }
	    } else {
	        resultMap = printResult;
	    }

	    long end = System.nanoTime();
	    System.out.println("⏱ 실행시간(ms): " + (end - start) / 1000000);
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
			//System.out.println("flag1: " + flag1);
			//System.out.println("flag2: " + flag2);
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

			//System.out.println("취소테이블 저장: " + flag0);
			//System.out.println("취소테이블 저장: " + flag1);
			//System.out.println("취소테이블 저장: " + flag2);
			//System.out.println("취소테이블 저장: " + flag3);

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
	/*
	 * @RequestMapping(value = "/management/shippingComplete", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public boolean shippingComplete(@RequestBody Management
	 * management, HttpSession session) { String loginUserID =
	 * (String)session.getAttribute("loginUserId"); //스캔한 품목들만 출하완료 테이블에 저장 boolean
	 * flag1 = managementService.insertShippingResult(management);
	 * 
	 * //출하완료 'Y'로 업데이트 boolean flag2 =
	 * managementService.updateCompleteInvoiceList(management);
	 * 
	 * //인보이스/품목 매핑 테이블에서 삭제 boolean flag3 =
	 * managementService.deleteNoScanInventory(management);
	 * 
	 * //실제 재고 차감 시작!!!!!!!!!!!!!!!!!!!!!! //차감할 데이터 조회 List<Management> datas1 =
	 * managementService.getRealDeductInventoryList(management); for(Management v:
	 * datas1) { v.setUser_id(loginUserID); } System.out.println("차감할 데이터 조회: " +
	 * datas1);
	 * 
	 * //S_SALES_REQUEST_PROCESS 업데이트 boolean flag4 =
	 * managementService.updateS_SALES_REQUEST_PROCESS(datas1);
	 * System.out.println("첫 번째 업데이트(S_SALES_REQUEST_PROCESS): " + flag4);
	 * 
	 * //출하요청등록번호에 순번의 수량 조회해서 그 배열만큼 업데이트 for(Management v: datas1) { Management
	 * data = managementService.getSeqSalesRequestInventoryList(v);
	 * data.setUser_id(loginUserID); boolean flag5 =
	 * managementService.updateS_SALES_REQUEST_DETAIL(data);
	 * System.out.println("총 재고에 따가 R, P, F 업데이트: " + flag5); }
	 * 
	 * //로트번호별로 F(아마 finish?)업데이트 boolean flag6 =
	 * managementService.updateS_SALES_REQUEST_LOT(datas1);
	 * System.out.println("로트번호별로 F로 업데이트: " + flag6);
	 * 
	 * //재고차감(I_ONHAND_INVENTORY 테이블 업데이트) for(Management v: datas1) { Management
	 * data = managementService.getI_ONHAND_INVENTORY(v);
	 * data.setUser_id(loginUserID); boolean flag7 =
	 * managementService.updateI_ONHAND_INVENTORY(data); boolean flag8 =
	 * managementService.updateI_WH_ONHAND_INVENTORY(data); boolean flag9 =
	 * managementService.updateI_MONTHLY_INVENTORY(data); boolean flag10 =
	 * managementService.updateI_WH_MONTHLY_INVENTORY(data);
	 * System.out.println("I_ONHAND_INVENTORY 업데이트: " + flag7);
	 * System.out.println("I_WH_ONHAND_INVENTORY 업데이트: " + flag8);
	 * System.out.println("I_MONTHLY_INVENTORY 업데이트: " + flag9);
	 * System.out.println("I_WH_MONTHLY_INVENTORY 업데이트: " + flag10);
	 * 
	 * //월, 로트별 재고 조회? data = managementService.getI_WH_MONTHY_INVENTORY(v);
	 * data.setUser_id(loginUserID); boolean flag11 =
	 * managementService.updateI_WH_MONTHLY_INVENTORY2(data);
	 * 
	 * //월별재고 업데이트? data = managementService.getI_MONTHY_INVENTORY(v);
	 * data.setUser_id(loginUserID); boolean flag12 =
	 * managementService.updateI_MONTHLY_INVENTORY2(data); boolean flag13 =
	 * managementService.updateI_MONTHLY_INVENTORY3(data); }
	 * 
	 * //I_TRANSACTION_DETAIL 테이블 INSERT/UPDATE, I_TRANSACTION_SALES 테이블 INSERT 안함
	 * 
	 * System.out.println("출하완료 테이블 저장: " + flag1);
	 * System.out.println("인보이스 출하완료로 업데이트: " + flag2);
	 * System.out.println("인보이스/품목 매핑 테이블에서 삭제: " + flag3);
	 * 
	 * if(flag1 && flag2 && flag3) { return true; } return false; }
	 */
	@RequestMapping(value = "/management/shippingComplete", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean shippingComplete(@RequestBody Management management, HttpSession session) {
		String loginUserID = (String)session.getAttribute("loginUserId");
	    
	    try {
	        // 모든 로직이 묶인 서비스 호출
	        return managementService.processShippingComplete(management, loginUserID);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 에러 발생 시 서비스에서 롤백을 수행하므로 데이터는 안전함
	        logger.error("[출하완료 컨트롤러 에러]: " + e.getMessage(), e);
	        return false;
	    }
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
	
	//인보이스 이름 업데이트
	@RequestMapping(value = "/management/updateInvoiceName", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean updateInvoiceName(@RequestBody Management management) {
		return managementService.updateInvoiceName(management);
	}
	/*
	//쉬핑마크 미리보기
	@RequestMapping(value="/management/previewShippingMark", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> previewShippingMark(@RequestBody Management management) {
		System.out.println("미리보기 함수");
		
		long start = System.nanoTime();
		
		Management data = managementService.getShippingMarkPrintInventory(management);
	    if (data == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
		String customerName = data.getNm_customer();
		PreviewExcel previewExcel = new PreviewExcel();
		byte[] imageBytes = null;

		//테스트용
		//customerName = "THAI AUTO";
		
		if(customerName.contains("KAB")) {
			imageBytes = previewExcel.previewKoideKab(data, KAB_FILE_PATH);
		}else if(customerName.contains("KCB")) {
			imageBytes = previewExcel.previewKoideKcb(data, KCB_FILE_PATH);
		}else if(customerName.contains("KKB")) {
			imageBytes = previewExcel.previewKoideKkb(data, KKB_FILE_PATH);
		}else if(customerName.contains("SANKIN")) {
			imageBytes = previewExcel.previewKoideSankin(data, SANKIN_FILE_PATH);
		}else if(customerName.contains("KKM")) {
			imageBytes = previewExcel.previewKkm(data, KKM_FILE_PATH);
		}else if(customerName.contains("KOB")) {
			imageBytes = previewExcel.previewKob(data, KOB_FILE_PATH);
		}else if(customerName.contains("CASH")) {
			imageBytes = previewExcel.previewCash(data, CASH_FILE_PATH);
		}else if(customerName.contains("ELM2")) {
			imageBytes = previewExcel.previewElm2(data, ELM2_FILE_PATH);
		}else if(customerName.contains("KEEPRO")) {
			imageBytes = previewExcel.previewKeepro(data, KEEPRO_FILE_PATH);
		}else if(customerName.contains("MBI")) {
			imageBytes = previewExcel.previewMbi(data, MBI_FILE_PATH);
		}else if(customerName.contains("MMP")) {
			imageBytes = previewExcel.previewMmp(data, MMP_FILE_PATH);
		}else if(customerName.contains("NOK")) {
			imageBytes = previewExcel.previewNok(data, NOK_FILE_PATH);
		}else if(customerName.contains("NST") || customerName.contains("아이엠에스")) {
			imageBytes = previewExcel.previewNst(data, NST_FILE_PATH);
		}else if(customerName.contains("PROFENDER")) {
			imageBytes = previewExcel.previewProfender(data, PROFENDER_FILE_PATH);
		}else if(customerName.contains("DKK")) {
			imageBytes = previewExcel.previewDkk(data, DKK_FILE_PATH);
		}else if(customerName.contains("KTH")) {
			imageBytes = previewExcel.previewKth(data, KTH_FILE_PATH);
		}else if(customerName.contains("KPS")) {
			imageBytes = previewExcel.previewKps(data, KPS_FILE_PATH);
		}else if(customerName.contains("KMEX")) {
			imageBytes = previewExcel.previewKmex(data, KMEX_FILE_PATH);
		}else if(customerName.contains("THAI AUTO")) {
			imageBytes = previewExcel.previewThaiAuto(data, THAIAUTO_FILE_PATH);
		}else if(customerName.contains("PIONEER")) {
			imageBytes = previewExcel.previewPioneer(data, PIONEER_FILE_PATH);
		}else if(customerName.contains("Profender")) {
			imageBytes = previewExcel.previewProfenderPct(data, PCT_FILE_PATH);
		}else if(customerName.contains("ROCS") || customerName.contains("ASTEMO") 
				|| customerName.contains("SCHIAVELLO")) {
			System.out.println("양식 없는 고객사 미리보기");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

	    if (imageBytes == null) {
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
	    }

		long end = System.nanoTime();   // 끝 시간
		System.out.println("⏱ 실행시간(ms): " + (end - start)/1000000);
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}
	*/
	
	//쉬핑마크 미리보기
	@RequestMapping(value="/management/previewShippingMark", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> previewShippingMark(@RequestBody Management management) {
	    System.out.println("미리보기 함수");
	    long start = System.nanoTime();

	    Management data = managementService.getShippingMarkPrintInventory(management);
	    if (data == null) {
	    	System.out.println("인보이스 품목 못찾음");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    String customerName = data.getNm_customer();
	    List<Management> customerList = managementService.getCustomerList(management);
	    Management matched = findMatchedCustomer(customerName, customerList);

	    if (matched == null) {
	    	System.out.println("맞는 양식 없음");
	        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
	    }

	    // 파일명 없으면 자동으로 양식 없는 고객사 처리
	    if (matched.getCustomer_shippingmark_file_name() == null || matched.getCustomer_shippingmark_file_name().trim().isEmpty()) {
	        System.out.println("양식 없는 고객사 미리보기");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }

	    String customerKey = matched.getCustomer_key();
	    String filePath = BASE_FILE_PATH + matched.getCustomer_shippingmark_file_name();

	    BiFunction<Management, String, byte[]> previewMethod = PREVIEW_METHOD_MAP.get(customerKey);
	    if (previewMethod == null) {
	    	System.out.println("양식에 데이터 넣는 함수 없음");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }

	    byte[] imageBytes = previewMethod.apply(data, filePath);
	    if (imageBytes == null) {
	    	System.out.println("imageBytes null - previewMethod 실패: " + customerKey);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
	    }

	    long end = System.nanoTime();
	    System.out.println("⏱ 실행시간(ms): " + (end - start) / 1000000);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}
	
	//인보이스 생성 및 조회 페이지 칼럼 조회
	@RequestMapping(value="/management/getColumnSettingList", method=RequestMethod.POST)
	@ResponseBody
	public List<Management> getColumnSetting(@RequestBody Management management, HttpSession session) {
	    String userId = (String) session.getAttribute("loginUserId");
	    management.setUser_id(userId);
	    return managementService.getColumnSettingList(management);
	}
	
	//dataTable 칼럼 숨기기
	@RequestMapping(value = "/management/insertColumnSetting", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean insertColumnSetting(@RequestBody Management management, HttpSession session) {
		String userId = (String) session.getAttribute("loginUserId");
	    management.setUser_id(userId);
		return managementService.insertColumnSetting(management);
	}
	
	//dataTable 칼럼 보이게
	@RequestMapping(value = "/management/deleteColumnSetting", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean deleteColumnSetting(@RequestBody Management management, HttpSession session) {
		String userId = (String) session.getAttribute("loginUserId");
	    management.setUser_id(userId);
		return managementService.deleteColumnSetting(management);
	}
	
	@RequestMapping(value = "/management/mobile/deleteAndInsertInvoiceInventory", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean mobileDeleteAndInsertInvoiceInventory(@RequestBody Management management) {
		return managementService.mobileDeleteAndInsertInvoiceInventory(management);
	}
	
	//W/O, 수량 같은 품목 있나 조회
	@RequestMapping(value = "/management/mobile/getSameWoQtyInventory", method = RequestMethod.POST) 
	@ResponseBody
	public Management findMatchingUnprintedItem(@RequestBody Management management) {
	    return managementService.getSameWoQtyInventory(management);
	}

	//로트번호 스왑
	@RequestMapping(value = "/management/mobile/swapLotNo", method = RequestMethod.POST) 
	@ResponseBody
	public boolean swapLotNo(@RequestBody Management management) {
	    return managementService.swapLotNo(management);
	}
	
	//인보이스 부여 안된거 스캔 후 w/o, 수량 같은거 있으면 업데이트
	//모바일 출하취소(출하목록에서만 삭제)
	@RequestMapping(value = "/management/mobile/updateLotNo", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean updateLotNo(@RequestBody Management management) {
		System.out.println("인보이스 교체 함수");
		return managementService.updateLotNo(management);
	}
	
	//스캔한 품목이 출력한 품목인지 조회
	//W/O, 수량 같은 품목 있나 조회
	@RequestMapping(value = "/management/mobile/getIsShippingList", method = RequestMethod.POST) 
	@ResponseBody
	public Management getIsShippingList(@RequestBody Management management) {
	    return managementService.getIsShippingList(management);
	}
	
	//양식 없는 고객사 조회
	@RequestMapping(value = "/management/mobile/getNoShippingMarkCustomerList", method = RequestMethod.GET) 
	@ResponseBody
	public List<Management> getNoShippingMarkCustomerList(Management management) {
	    return managementService.getNoShippingMarkCustomerList(management);
	}
	
	//출하취소!!!!!!!!!!!!!!!!
	@RequestMapping(value = "/management/processShippingCancel", method = RequestMethod.POST) 
	@ResponseBody 
	public boolean processShippingCancel(@RequestBody Management management, HttpSession session) {
		String loginUserID = (String)session.getAttribute("loginUserId");
		
		//출하완료 취소 개발 이전 인보이스는 출하취소 불가(ez_invoice_inventory_mapping에서 삭제하고 있었음)
		Management mappingCount = managementService.getMappingInvoiceCount(management);
		if(mappingCount.getMapping_count() == 0) {
			return false;
		}
	    
	    try {
	        // 모든 로직이 묶인 서비스 호출
	        return managementService.processShippingCancel(management, loginUserID);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 에러 발생 시 서비스에서 롤백을 수행하므로 데이터는 안전함
	        logger.error("[출하완료 컨트롤러 에러]: " + e.getMessage(), e);
	        throw e;
	    }
	}
	
	//쉬핑마크 삭제(db, 경로의 실제 파일 둘 다)
	@RequestMapping(value = "/management/deleteShippingMark", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteShippingMark(Management management, HttpSession session) {
	    String basePath = "D:/율촌_쉬핑마크_양식/";
	    String oldFileName = management.getOld_file_name();
	    try {
	        if (oldFileName != null && !oldFileName.isEmpty()) {
	            File oldFile = new File(basePath + oldFileName);
	            if (oldFile.exists()) oldFile.delete();
	        }
	        management.setCustomer_shippingmark_file_name(null); // DB도 null로
	        management.setUpdate_user_id((String)session.getAttribute("loginUserId"));
	        return managementService.updateShippingMarkFile(management);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}


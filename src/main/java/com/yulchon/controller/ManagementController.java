package com.yulchon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yulchon.domain.Management;
import com.yulchon.service.ManagementService;

@Controller
public class ManagementController {
	
	@Autowired
	private ManagementService managementService;

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
		 return managementService.getInvoiceList(management);
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
	public String shippingMarkPrintPage(@RequestParam(value="lbl_lot_no", required=false) String lbl_lot_no, 
			Model model, Management management) {
		management.setLbl_lot_no(lbl_lot_no);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		 Management data = managementService.getShippingMarkPrintInventory(management);
		 System.out.println("출력 조회 데이터: " + data);
		 model.addAttribute("data", data);
		return "/management/shippingMarkPrint.jsp";
	}
	
	// 출하 취소 페이지 이동
	@RequestMapping(value = "/management/mobile/shippingCancel", method = RequestMethod.GET)
	public String shippingCancelPage(@RequestParam(value="lbl_lot_no", required=false) String lbl_lot_no, 
			Model model, Management management) {
		management.setLbl_lot_no(lbl_lot_no);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		 Management data = managementService.getShippingMarkPrintInventory(management);
		 System.out.println("출력 조회 데이터: " + data);
		 model.addAttribute("data", data);
		return "/management/shippingCancel.jsp";
	}
	
	// 제품확인 페이지 이동
	@RequestMapping(value = "/management/mobile/productConfirm", method = RequestMethod.GET)
	public String productConfirmPage(@RequestParam(value="lbl_lot_no", required=false) String lbl_lot_no, 
			Model model, Management management) {
		management.setLbl_lot_no(lbl_lot_no);
		System.out.println("조회 Lot No: " + lbl_lot_no);
		 Management data = managementService.getShippingMarkPrintInventory(management);
		 System.out.println("출력 조회 데이터: " + data);
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
		 return managementService.getInvoiceInventoryList(management);
	 }
	 
	 //인보이스에 품목 추가
	 @RequestMapping(value = "/management/insertInvoiceInventory", method = RequestMethod.POST) 
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
	 
	 //쉬핑마크 출력 후 출하목록에 추가
	 @RequestMapping(value = "/management/mobile/insertShippingList", method = RequestMethod.POST) 
	 @ResponseBody 
	 public boolean insertShippingList(@RequestBody Management management) {
		 return managementService.insertShippingList(management);
	 }
	 
	 //모바일 출하목록 삭제
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
	 
	 //웹 출하목록 리스트 삭제
	 @RequestMapping(value = "/management/deleteShippingListInventory", method = RequestMethod.POST) 
	 @ResponseBody 
	 public boolean deleteShippingListInventory(@RequestBody Management management) {
		 return managementService.deleteShippingListInventory(management);
	 }

}


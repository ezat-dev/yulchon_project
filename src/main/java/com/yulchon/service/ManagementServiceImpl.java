package com.yulchon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yulchon.dao.ManagementDao;
import com.yulchon.domain.Management;

@Service
public class ManagementServiceImpl implements ManagementService{
	
	@Autowired
	private ManagementDao managementDao;
	
	@Override
	public List<Management> getInventoryList(Management management) {
		return managementDao.getInventoryList(management);
	}
	
	@Override
	public List<Management> getInvoiceList(Management management) {
		return managementDao.getInvoiceList(management);
	}
	
	@Override
	public boolean insertInvoiceName(Management management) {
		return managementDao.insertInvoiceName(management);
	}
	
	@Override
	public Management getRecentInvoice(Management management) {
		return managementDao.getRecentInvoice(management);
	}
	
	@Override
	public void syncEzInventoryLotExt(Management management) {
		managementDao.syncEzInventoryLotExt(management);
	}
	
	@Override
	public List<Management> getInvoiceInventoryList(Management management) {
		return managementDao.getInvoiceInventoryList(management);
	}
	
	@Override
	public boolean insertInvoiceInventory(Management management) {
		return managementDao.insertInvoiceInventory(management);
	}
	
	@Override
	public boolean deleteInvoiceInventory(Management management) {
		return managementDao.deleteInvoiceInventory(management);
	}
	
	@Override
	public Management getShippingMarkPrintInventory(Management management) {
		return managementDao.getShippingMarkPrintInventory(management);
	}
	
	@Override
	public boolean insertShippingList(Management management) {
		return managementDao.insertShippingList(management);
	}
	
	@Override
	public boolean deleteShippingList(Management management) {
		return managementDao.deleteShippingList(management);
	}
	
	@Override
	public List<Management> getShippingList(Management management) {
		return managementDao.getShippingList(management);
	}
	
	@Override
	public boolean deleteShippingListInventory(Management management) {
		return managementDao.deleteShippingListInventory(management);
	}
	
	@Override
	public boolean cancelShippingList(Management management) {
		return managementDao.cancelShippingList(management);
	}
	
	@Override
	public boolean cancelInvoiceList(Management management) {
		return managementDao.cancelInvoiceList(management);
	}
	
	@Override
	public boolean cancelInvoiceInventory(Management management) {
		return managementDao.cancelInvoiceInventory(management);
	}
	
	@Override
	public List<Management> getNoUpdatedInvoiceList(Management management) {
		return managementDao.getNoUpdatedInvoiceList(management);
	}
	
	@Override
	public boolean updateCustomerProductCodeNumber(Management management) {
		return managementDao.updateCustomerProductCodeNumber(management);
	}
	
	@Override
	public boolean insertShippingResult(Management management) {
		return managementDao.insertShippingResult(management);
	}
	
	@Override
	public boolean updateCompleteInvoiceList(Management management) {
		return managementDao.updateCompleteInvoiceList(management);
	}
	
	@Override
	public List<Management> getCompleteInventoryList(Management management) {
		return managementDao.getCompleteInventoryList(management);
	}
	
	@Override
	public Management mobileGetShippingMarkPrintInventory(Management management) {
		return managementDao.mobileGetShippingMarkPrintInventory(management);
	}
	
	@Override
	public boolean deleteNoScanInventory(Management management) {
		return managementDao.deleteNoScanInventory(management);
	}
	
	@Override
	public List<Management> getNoUpdatedOrResetInvoiceList(Management management) {
		return managementDao.getNoUpdatedOrResetInvoiceList(management);
	}
	
	@Override
	public List<Management> getResetDatas(Management management) {
		return managementDao.getResetDatas(management);
	}
	
	@Override
	public boolean insertShippingCancel(Management management) {
		return managementDao.insertShippingCancel(management);
	}
	
	@Override
	public List<Management> getCancelInventoryList(Management management) {
		return managementDao.getCancelInventoryList(management);
	}
	
	@Override
	public List<Management> getResetInventoryList(Management management) {
		return managementDao.getResetInventoryList(management);
	}

	@Override
	public boolean updateInvoiceIsMoved(Management management) {
		return managementDao.updateInvoiceIsMoved(management);
	}
	
	@Override
	public List<Management> getCustomerList(Management management) {
		return managementDao.getCustomerList(management);
	}
	
	@Override
	public boolean updateShippingMarkFile(Management management) {
		return managementDao.updateShippingMarkFile(management);
	}
	
	@Override
	public boolean updateCustomerRemark(Management management) {
		return managementDao.updateCustomerRemark(management);
	}
	
	@Override
	public boolean insertCustomer(Management management) {
		return managementDao.insertCustomer(management);
	}
	
	@Override
	public List<Management> getShippingDatas(Management management) {
		return managementDao.getShippingDatas(management);
	}
	
	@Transactional(rollbackFor = Exception.class) // 에러 발생 시 전체 롤백
    public boolean processShippingComplete(Management management, String loginUserID) {
		try {
	        System.out.println(">>> 출하 완료 프로세스 시작 (사용자: " + loginUserID + ")");

	        // 1. 기초 데이터 처리
	        managementDao.insertShippingResult(management);
	        managementDao.updateCompleteInvoiceList(management);
	        managementDao.deleteNoScanInventory(management);
	        System.out.println("1단계: 기초 데이터 처리 완료");

	        // 2. 차감 데이터 조회
	        List<Management> datas1 = managementDao.getRealDeductInventoryList(management);
	        if (datas1 == null || datas1.isEmpty()) {
	            System.out.println("알림: 차감할 데이터가 없습니다.");
	            throw new RuntimeException("차감할 재고 데이터가 존재하지 않아 출하 완료를 취소합니다.");
	        }
	        for(Management v : datas1) { v.setUser_id(loginUserID); }
	        System.out.println("2단계: 차감 대상 조회 완료 (건수: " + datas1.size() + ")");

	        // 3. S_SALES_REQUEST 업데이트
	        managementDao.updateS_SALES_REQUEST_PROCESS(datas1);
	        for(Management v : datas1) {
	            Management data = managementDao.getSeqSalesRequestInventoryList(v);
	            if(data != null) {
	                data.setUser_id(loginUserID);
	                managementDao.updateS_SALES_REQUEST_DETAIL(data);
	            }
	        }
	        managementDao.updateS_SALES_REQUEST_LOT(datas1);
	        System.out.println("3단계: S_SALES_REQUEST 관련 업데이트 완료");

	        // 4. 재고(Inventory) 루프 업데이트
	        int count = 0;
	        for(Management v : datas1) {
	            Management data = managementDao.getI_ONHAND_INVENTORY(v);
	            if(data != null) {
	                data.setUser_id(loginUserID);
	                managementDao.updateI_ONHAND_INVENTORY(data);
	                managementDao.updateI_WH_ONHAND_INVENTORY(data);
	                managementDao.updateI_MONTHLY_INVENTORY(data);
	                managementDao.updateI_WH_MONTHLY_INVENTORY(data);
	            }
	            
	            Management whData = managementDao.getI_WH_MONTHY_INVENTORY(v);
	            if(whData != null) {
	                whData.setUser_id(loginUserID);
	                managementDao.updateI_WH_MONTHLY_INVENTORY2(whData);
	            }
	            
	            Management monData = managementDao.getI_MONTHY_INVENTORY(v);
	            if(monData != null) {
	                monData.setUser_id(loginUserID);
	                managementDao.updateI_MONTHLY_INVENTORY2(monData);
	                managementDao.updateI_MONTHLY_INVENTORY3(monData);
	            }
	            count++;
	        }
	        System.out.println("4단계: 상세 재고 차감 루프 완료 (처리건수: " + count + ")");

	        System.out.println(">>> 모든 프로세스 성공적으로 완료 (COMMIT)");
	        return true;

	    } catch (Exception e) {
	        // 여기서 로그를 찍어야 어디서 에러가 났는지 알 수 있음
	        System.err.println("!!! 출하 프로세스 중 에러 발생 (ROLLBACK) !!!");
	        System.err.println("에러 메시지: " + e.getMessage());
	        e.printStackTrace(); // 전체 에러 스택 확인용
	        
	        // 중요: 에러를 다시 던져야 @Transactional이 인식하고 롤백을 수행함
	        throw e; 
	    }
	}
}

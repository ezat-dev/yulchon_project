package com.yulchon.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
			String invoice_no = management.getInvoiceList().get(0);
			//System.out.println("인보이스 번호: " + invoice_no);
			management.setInvoice_no(invoice_no);

			//기초데이터(뒤쪽에서 하면 조회 안되서 insert 안됨. 먼저 실행해야함)
			managementDao.insertShippingResult(management);
			
			
			//pda 스캔 로직 추가(S_SALES_REQUEST_LOT 테이블에 추가)
			List<Management> datas0 = managementDao.getRealDeductInventoryList(management);
			for(Management v: datas0) {
				Management seq_request_serial_data = managementDao.selectSEQ_REQUEST_SERIAL(v);
				String no_sales_request_serial = seq_request_serial_data.getNo_sales_request_serial();
				//Integer seq_sales_request = seq_request_serial_data.getSeq_sales_request();
				//System.out.println("수주번호: " + no_sales_request_serial);
				String lbl_lot_no = v.getLbl_lot_no();
				seq_request_serial_data.setLbl_lot_no(lbl_lot_no);
				boolean insertS_SALES_REQUEST_LOT = managementDao.insertS_SALES_REQUEST_LOT(seq_request_serial_data);
				//System.out.println("insertS_SALES_REQUEST_LOT 성공여부: " + insertS_SALES_REQUEST_LOT);
			}


			// 2. 차감 데이터 조회
			List<Management> datas1 = managementDao.getRealDeductInventoryList(management);
			System.out.println("차감할 데이터 개수: " + datas1.size());

			if(datas1 == null || datas1.size() == 0 || datas1.isEmpty()) {
				//System.out.println("알림: 차감할 데이터가 없습니다.");
				throw new RuntimeException("차감할 재고 데이터가 존재하지 않아 출하 완료를 취소합니다.");
			}
			String no_sales_request_serial = datas1.get(0).getNo_sales_request_serial();
			//System.out.println("no_sales_request_serial: " + no_sales_request_serial);

			if (datas1 == null || datas1.isEmpty()) {
				//System.out.println("알림: 차감할 데이터가 없습니다.");
				throw new RuntimeException("차감할 재고 데이터가 존재하지 않아 출하 완료를 취소합니다.");
			}

			for(Management v : datas1) { v.setUser_id(loginUserID); }
			//System.out.println("2단계: 차감 대상 조회 완료 (건수: " + datas1.size() + ")");

			// 3. S_SALES_REQUEST 업데이트
			managementDao.updateS_SALES_REQUEST_PROCESS(datas1);

			for(Management v : datas1) {
				Integer seq_sales_request = v.getSeq_sales_request();
				Management data = managementDao.getSeqSalesRequestInventoryList(v);
				if(data != null) {
					data.setUser_id(loginUserID);
					data.setNo_sales_request_serial(no_sales_request_serial);
					data.setSeq_sales_request(seq_sales_request);
					//System.out.println(data);
					managementDao.updateS_SALES_REQUEST_DETAIL(data);
				}
			}
			managementDao.updateS_SALES_REQUEST_LOT(datas1);
			//System.out.println("3단계: S_SALES_REQUEST 관련 업데이트 완료");

			// 4. 재고(Inventory) 루프 업데이트
			int count = 0;
			for(Management v : datas1) {
				String lbl_lot_no = v.getLbl_lot_no();
				//System.out.println("로트번호: " + lbl_lot_no);
				String inventoryCount = v.getQty_inventory();
				//System.out.println("inventoryCount: " + inventoryCount);
				String invoice_name = v.getInvoice_name();
				//System.out.println("invoice_name: " + invoice_name);
				Management data = managementDao.getI_ONHAND_INVENTORY(v);
				if(data != null) {
					//System.out.println("getI_ONHAND_INVENTORY 데이터 존재!");
					data.setUser_id(loginUserID);
					data.setQty_inventory(inventoryCount);
					data.setNo_lot(lbl_lot_no);
					managementDao.updateI_ONHAND_INVENTORY(data);
					managementDao.updateI_WH_ONHAND_INVENTORY(data);
					managementDao.updateI_MONTHLY_INVENTORY(data);
					managementDao.updateI_WH_MONTHLY_INVENTORY(data);
				}

				//I_TRANSACTION 테이블에 추가
				//수불번호 만들기
				String[] parts = invoice_name.split("-");
				String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // "20260226" >> 여기 오늘 날짜	**수정함
				//int sequencePart = Integer.parseInt(parts[2]); // 1 (숫자로 변환) >> I_TRANSACTION 오늘날짜로 조회해서 행 개수 + 1	**수정함
				int sequencePart = Integer.parseInt(managementDao.selectI_TRANSACTION_NextSeq(management).getNext_seq());

				String noTransaction = String.format("ISS%s%05d", datePart, sequencePart);
				//System.out.println("만든 수불번호: " + noTransaction);
				v.setNo_transaction(noTransaction);
				String invoice_name_date = "";
				String invoiceNameDatePart = v.getInvoice_name().split("-")[1];
				if (invoiceNameDatePart.length() == 6) {
				    // YYMMDD 형식인 경우 앞에 20을 붙임
				    invoice_name_date = "20" + invoiceNameDatePart;
				} else if (invoiceNameDatePart.length() == 8) {
				    // YYYYMMDD 형식인 경우 그대로 사용
				    invoice_name_date = invoiceNameDatePart;
				}
				v.setInvoice_name_date(invoice_name_date);
				boolean insertI_TRANSACTION = managementDao.insertI_TRANSACTION(v);
				//System.out.println("I_TRANSACTION 데이터 추가 성공 여부: " + insertI_TRANSACTION);

				//I_TRANSACTION_DETAIL 테이블에 추가
				UUID uuid = UUID.randomUUID();
				String finalUuid = uuid.toString().replace("-", "").toUpperCase();
				v.setUuid(finalUuid);
				managementDao.insertI_TRANSACTION_DETAIL(v);

				Management whData = managementDao.getI_WH_MONTHLY_INVENTORY(v);
				if(whData != null) {
					whData.setUser_id(loginUserID);
					whData.setLbl_lot_no(lbl_lot_no);
					managementDao.updateI_WH_MONTHLY_INVENTORY2(whData);
				}

				List<Management> monData = managementDao.getI_MONTHLY_INVENTORY(v);

				String seq_transaction = "";
				//I_TRANSACTION에서 SEQ_TRANSACTION 조회
				//System.out.println("SEQ_TRANSACTION 조회할 로트번호: " + v.getLbl_lot_no());
				Management seqTransactionData = managementDao.getI_TRANSACTION_DETAIL(v);
				seq_transaction = seqTransactionData.getSeq_transaction();

				if(monData != null) {
					for(Management monDataaa: monData) {
						monDataaa.setLbl_lot_no(lbl_lot_no);
						
						monDataaa.setUser_id(loginUserID);
						monDataaa.setInvoice_name(invoice_name);
					managementDao.updateI_MONTHLY_INVENTORY2(monDataaa);
					managementDao.updateI_MONTHLY_INVENTORY3(monDataaa);

					monDataaa.setSeq_transaction(seq_transaction);
					managementDao.updateI_TRANSACTION_DETAIL(monDataaa);
					}
				}

				v.setSeq_transaction(seq_transaction);
				managementDao.insertI_TRANSACTION_SALES(v);
				count++;
			}
			System.out.println("4단계: 상세 재고 차감 루프 완료 (처리건수: " + count + ")");

			// 기초 데이터 처리
			managementDao.updateCompleteInvoiceList(management);
			managementDao.deleteNoScanInventory(management);

			//System.out.println("기초 데이터 처리 완료(update, delete 뒤로 뺌)");

			System.out.println(">>> 모든 프로세스 성공적으로 완료");
			//System.out.println("일부러 롤백 시작");
			//if(true) throw new RuntimeException("테스트를 위한 강제 롤백!");
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

	@Override
	public Management getProductConfirm(Management management) {
		return managementDao.getProductConfirm(management);
	}

	@Override
	public boolean updateInvoiceName(Management management) {
		return managementDao.updateInvoiceName(management);
	}

	@Override
	public List<Management> getColumnSettingList(Management management) {
		return managementDao.getColumnSettingList(management);
	}

	@Override
	public boolean insertColumnSetting(Management management) {
		return managementDao.insertColumnSetting(management);
	}

	@Override
	public boolean deleteColumnSetting(Management management) {
		return managementDao.deleteColumnSetting(management);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean mobileDeleteAndInsertInvoiceInventory(Management management) {
		try {
			boolean flag1 = managementDao.mobileDeleteInvoiceInventory(management);
			boolean flag2 = managementDao.mobileInsertInvoiceInventory(management);
			if(flag1 && flag2) {
				return true;
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	            return false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        return false;
		}
	}
	
	@Override
	public Management getSameWoQtyInventory(Management management) {
		return managementDao.getSameWoQtyInventory(management);
	}

	@Transactional
	@Override
	public boolean swapLotNo(Management management) {
		return managementDao.swapLotNo(management);
	}
	
	@Override
	public boolean updateLotNo(Management management) {
		return managementDao.updateLotNo(management);
	}
	
	@Override
	public Management getIsShippingList(Management management) {
		return managementDao.getIsShippingList(management);
	}
	
	@Override
	public List<Management> getNoShippingMarkCustomerList(Management management) {
		return managementDao.getNoShippingMarkCustomerList(management);
	}
	
	//출하취소------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean processShippingCancel(Management management, String loginUserID) {
	    try {
	        //System.out.println(">>> 출하 취소 프로세스 시작 (사용자: " + loginUserID + ")");
	        
	        String invoice_no = management.getInvoice_no();
	        //System.out.println(">>> [1] invoice_no: " + invoice_no);
	        management.setInvoice_no(invoice_no);

	        List<Management> datas = managementDao.getCancelShippingList(management);
	        //System.out.println(">>> [2] 복구할 데이터 개수: " + (datas != null ? datas.size() : "null"));

	        if (datas == null || datas.isEmpty()) {
	            throw new RuntimeException("복구할 데이터가 존재하지 않아 출하 취소를 중단합니다.");
	        }

	        String no_sales_request_serial = datas.get(0).getNo_sales_request_serial();
	        //System.out.println(">>> [3] no_sales_request_serial: " + no_sales_request_serial);

	        for (Management v : datas) { v.setUser_id(loginUserID); }

	        //System.out.println(">>> [4] cancelS_SALES_REQUEST_PROCESS 시작");
	        managementDao.cancelS_SALES_REQUEST_PROCESS(datas);
	        //System.out.println(">>> [4] cancelS_SALES_REQUEST_PROCESS 완료");

	        //System.out.println(">>> [5] S_SALES_REQUEST_DETAIL 상태 재계산 시작 (총 " + datas.size() + "건)");
	        for (Management v : datas) {
	            Integer seq_sales_request = v.getSeq_sales_request();
	            //System.out.println("    [5] seq_sales_request: " + seq_sales_request + ", lot_no: " + v.getLbl_lot_no());
	            Management data = managementDao.getSeqSalesRequestInventoryList(v);
	            //System.out.println("    [5] getSeqSalesRequestInventoryList 결과: " + (data != null ? "있음" : "null"));
	            if (data != null) {
	                data.setUser_id(loginUserID);
	                data.setNo_sales_request_serial(no_sales_request_serial);
	                data.setSeq_sales_request(seq_sales_request);
	                managementDao.updateS_SALES_REQUEST_DETAIL(data);
	                //System.out.println("    [5] updateS_SALES_REQUEST_DETAIL 완료");
	            }
	        }
	        //System.out.println(">>> [5] S_SALES_REQUEST_DETAIL 재계산 완료");

	        //System.out.println(">>> [6] cancelS_SALES_REQUEST_LOT 시작");
	        managementDao.cancelS_SALES_REQUEST_LOT(datas);
	        //System.out.println(">>> [6] cancelS_SALES_REQUEST_LOT 완료");

	        //System.out.println(">>> [7] 재고 복구 루프 시작 (총 " + datas.size() + "건)");
	        for (Management v : datas) {
	            String lbl_lot_no = v.getLbl_lot_no();
	            String inventoryCount = v.getQty_inventory();
	            //System.out.println("    [7] lot_no: " + lbl_lot_no + ", qty: " + inventoryCount);

	            Management data = managementDao.getI_ONHAND_INVENTORY(v);
	            //System.out.println("    [7] getI_ONHAND_INVENTORY 결과: " + (data != null ? "있음" : "null"));

	            if (data != null) {
	                data.setUser_id(loginUserID);
	                data.setNo_lot(lbl_lot_no);
	                data.setQty_inventory(inventoryCount);

	                managementDao.cancelI_ONHAND_INVENTORY(data);
	                //System.out.println("    [7] cancelI_ONHAND_INVENTORY 완료");
	                managementDao.cancelI_WH_ONHAND_INVENTORY(data);
	                //System.out.println("    [7] cancelI_WH_ONHAND_INVENTORY 완료");
	                managementDao.cancelI_MONTHLY_INVENTORY(data);
	                //System.out.println("    [7] cancelI_MONTHLY_INVENTORY 완료");
	                managementDao.cancelI_WH_MONTHLY_INVENTORY(data);
	                //System.out.println("    [7] cancelI_WH_MONTHLY_INVENTORY 완료");
	            }

	            managementDao.deleteI_TRANSACTION_SALES(v);
	            //System.out.println("    [7] deleteI_TRANSACTION_SALES 완료");
	            managementDao.deleteI_TRANSACTION_DETAIL(v);
	            //System.out.println("    [7] deleteI_TRANSACTION_DETAIL 완료");
	            managementDao.deleteI_TRANSACTION(v);
	            //System.out.println("    [7] deleteI_TRANSACTION 완료");
	        }
	        //System.out.println(">>> [7] 재고 복구 및 트랜잭션 삭제 완료");

	        //System.out.println(">>> [8] cancelCompleteInvoiceList 시작");
	        managementDao.cancelCompleteInvoiceList(management);
	        //System.out.println(">>> [8] cancelCompleteInvoiceList 완료");

	        //System.out.println(">>> [9] deleteShippingResult 시작");
	        managementDao.deleteShippingResult(management);
	        //System.out.println(">>> [9] deleteShippingResult 완료");

	        //System.out.println(">>> 출하 취소 프로세스 완료");
	        //System.out.println("일부러 롤백 시작");
	        //if(true) throw new RuntimeException("테스트를 위한 강제 롤백!");
	        return true;

	    } catch (Exception e) {
	        System.err.println("!!! 출하 취소 프로세스 중 에러 발생 (ROLLBACK) !!!");
	        System.err.println("에러 메시지: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	@Override
	public Management getMappingInvoiceCount(Management management) {
		return managementDao.getMappingInvoiceCount(management);
	}
}

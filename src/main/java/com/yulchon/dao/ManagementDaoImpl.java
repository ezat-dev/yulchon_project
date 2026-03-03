package com.yulchon.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.yulchon.domain.Management;

@Repository
public class ManagementDaoImpl implements ManagementDao {
	
	@Resource(name="session")
	private SqlSession sqlSession;

	@Override
	public List<Management> getInventoryList(Management management) {
		return sqlSession.selectList("management.getInventoryList", management);
	}
	
	@Override
	public List<Management> getInvoiceList(Management management) {
		return sqlSession.selectList("management.getInvoiceList", management);
	}
	
	@Override
	public boolean insertInvoiceName(Management management) {
		int result =  sqlSession.insert("management.insertInvoiceName", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public Management getRecentInvoice(Management management) {
		return sqlSession.selectOne("management.getRecentInvoice", management);
	}
	
	@Override
	public void syncEzInventoryLotExt(Management management) {
		sqlSession.selectOne("management.syncEzInventoryLotExt", management);
	}
	
	@Override
	public List<Management> getInvoiceInventoryList(Management management) {
		return sqlSession.selectList("management.getInvoiceInventoryList", management);
	}
	
	@Override
	public boolean insertInvoiceInventory(Management management) {
		int result =  sqlSession.update("management.insertInvoiceInventory", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteInvoiceInventory(Management management) {
		int result =  sqlSession.delete("management.deleteInvoiceInventory", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public Management getShippingMarkPrintInventory(Management management) {
		return sqlSession.selectOne("management.getShippingMarkPrintInventory", management);
	}
	
	@Override
	public boolean insertShippingList(Management management) {
		int result =  sqlSession.insert("management.insertShippingList", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteShippingList(Management management) {
		int result =  sqlSession.delete("management.deleteShippingList", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getShippingList(Management management) {
		return sqlSession.selectList("management.getShippingList", management);
	}
	
	@Override
	public boolean deleteShippingListInventory(Management management) {
		int result =  sqlSession.delete("management.deleteShippingListInventory", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean cancelShippingList(Management management) {
		int result =  sqlSession.delete("management.cancelShippingList", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean cancelInvoiceList(Management management) {
		int result =  sqlSession.update("management.cancelInvoiceList", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean cancelInvoiceInventory(Management management) {
		int result =  sqlSession.delete("management.cancelInvoiceInventory", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getNoUpdatedInvoiceList(Management management) {
		return sqlSession.selectList("management.getNoUpdatedInvoiceList", management);
	}
	
	@Override
	public boolean updateCustomerProductCodeNumber(Management management) {
		int result =  sqlSession.update("management.updateCustomerProductCodeNumber", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertShippingResult(Management management) {
		int result =  sqlSession.insert("management.insertShippingResult", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateCompleteInvoiceList(Management management) {
		int result =  sqlSession.update("management.updateCompleteInvoiceList", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getCompleteInventoryList(Management management) {
		return sqlSession.selectList("management.getCompleteInventoryList", management);
	}
	
	@Override
	public Management mobileGetShippingMarkPrintInventory(Management management) {
		return sqlSession.selectOne("management.mobileGetShippingMarkPrintInventory", management);
	}
	
	@Override
	public boolean deleteNoScanInventory(Management management) {
		int result =  sqlSession.delete("management.deleteNoScanInventory", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getNoUpdatedOrResetInvoiceList(Management management) {
		return sqlSession.selectList("management.getNoUpdatedOrResetInvoiceList", management);
	}
	
	@Override
	public List<Management> getResetDatas(Management management) {
		return sqlSession.selectList("management.getResetDatas", management);
	}
	
	@Override
	public boolean insertShippingCancel(Management management) {
		int result =  sqlSession.insert("management.insertShippingCancel", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getCancelInventoryList(Management management) {
		return sqlSession.selectList("management.getCancelInventoryList", management);
	}
	
	@Override
	public List<Management> getResetInventoryList(Management management) {
		return sqlSession.selectList("management.getResetInventoryList", management);
	}
	
	@Override
	public boolean updateInvoiceIsMoved(Management management) {
		int result =  sqlSession.update("management.updateInvoiceIsMoved", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getCustomerList(Management management) {
		return sqlSession.selectList("management.getCustomerList", management);
	}
	
	@Override
	public boolean updateShippingMarkFile(Management management) {
		int result =  sqlSession.insert("management.updateShippingMarkFile", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateCustomerRemark(Management management) {
		int result =  sqlSession.update("management.updateCustomerRemark", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertCustomer(Management management) {
		int result =  sqlSession.insert("management.insertCustomer", management);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Management> getShippingDatas(Management management) {
		return sqlSession.selectList("management.getShippingDatas", management);
	}

	@Override
	public List<Management> getRealDeductInventoryList(Management management) {
		return sqlSession.selectList("management.getRealDeductInventoryList", management);
	}

	@Override
	public boolean updateS_SALES_REQUEST_PROCESS(List<Management> datas) {
		int result =  sqlSession.update("management.updateS_SALES_REQUEST_PROCESS", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public Management getSeqSalesRequestInventoryList(Management management) {
		return sqlSession.selectOne("management.getSeqSalesRequestInventoryList", management);
	}

	@Override
	public boolean updateS_SALES_REQUEST_DETAIL(Management datas) {
		int result =  sqlSession.update("management.updateS_SALES_REQUEST_DETAIL", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateS_SALES_REQUEST_LOT(List<Management> datas) {
		int result =  sqlSession.update("management.updateS_SALES_REQUEST_LOT", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public Management getI_ONHAND_INVENTORY(Management management) {
		return sqlSession.selectOne("management.getI_ONHAND_INVENTORY", management);
	}

	@Override
	public boolean updateI_ONHAND_INVENTORY(Management datas) {
		int result =  sqlSession.update("management.updateI_ONHAND_INVENTORY", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateI_WH_ONHAND_INVENTORY(Management datas) {
		int result =  sqlSession.update("management.updateI_WH_ONHAND_INVENTORY", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateI_MONTHLY_INVENTORY(Management datas) {
		int result =  sqlSession.update("management.updateI_MONTHLY_INVENTORY", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateI_WH_MONTHLY_INVENTORY(Management datas) {
		int result =  sqlSession.update("management.updateI_WH_MONTHLY_INVENTORY", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public Management getI_WH_MONTHLY_INVENTORY(Management management) {
		return sqlSession.selectOne("management.getI_WH_MONTHLY_INVENTORY", management);
	}

	@Override
	public boolean updateI_WH_MONTHLY_INVENTORY2(Management datas) {
		int result =  sqlSession.update("management.updateI_WH_MONTHLY_INVENTORY2", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public Management getI_MONTHLY_INVENTORY(Management management) {
		return sqlSession.selectOne("management.getI_MONTHLY_INVENTORY", management);
	}

	@Override
	public boolean updateI_MONTHLY_INVENTORY2(Management datas) {
		int result =  sqlSession.update("management.updateI_MONTHLY_INVENTORY2", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateI_MONTHLY_INVENTORY3(Management datas) {
		int result =  sqlSession.update("management.updateI_MONTHLY_INVENTORY3", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertI_TRANSACTION(Management datas) {
		int result =  sqlSession.insert("management.insertI_TRANSACTION", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertI_TRANSACTION_DETAIL(Management datas) {
		int result =  sqlSession.insert("management.insertI_TRANSACTION_DETAIL", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public Management getI_TRANSACTION_DETAIL(Management management) {
		return sqlSession.selectOne("management.getI_TRANSACTION_DETAIL", management);
	}

	@Override
	public boolean updateI_TRANSACTION_DETAIL(Management datas) {
		int result =  sqlSession.update("management.updateI_TRANSACTION_DETAIL", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertI_TRANSACTION_SALES(Management datas) {
		int result =  sqlSession.insert("management.insertI_TRANSACTION_SALES", datas);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public Management getProductConfirm(Management management) {
		return sqlSession.selectOne("management.getProductConfirm", management);
	}

}


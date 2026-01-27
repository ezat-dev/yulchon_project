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
		if(result < 0) {
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
		if(result < 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteInvoiceInventory(Management management) {
		int result =  sqlSession.delete("management.deleteInvoiceInventory", management);
		if(result < 0) {
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
		if(result < 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteShippingList(Management management) {
		int result =  sqlSession.delete("management.deleteShippingList", management);
		if(result < 0) {
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
		if(result < 0) {
			return false;
		}
		return true;
	}

}


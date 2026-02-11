package com.yulchon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

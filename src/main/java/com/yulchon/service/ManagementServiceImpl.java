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
}

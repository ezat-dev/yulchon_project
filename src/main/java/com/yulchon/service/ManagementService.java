package com.yulchon.service;

import java.util.List;

import com.yulchon.domain.Management;

public interface ManagementService {
	List<Management> getInventoryList(Management management);
	List<Management> getInvoiceList(Management management);
	boolean insertInvoiceName(Management management);
	Management getRecentInvoice(Management management);
	void syncEzInventoryLotExt(Management management);
	List<Management> getInvoiceInventoryList(Management management);
	boolean insertInvoiceInventory(Management management);
	boolean deleteInvoiceInventory(Management management);
	Management getShippingMarkPrintInventory(Management management);
	boolean insertShippingList(Management management);
	boolean deleteShippingList(Management management);
	List<Management> getShippingList(Management management);
	boolean deleteShippingListInventory(Management management);
}

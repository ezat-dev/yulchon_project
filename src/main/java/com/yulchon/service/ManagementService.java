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
	boolean cancelShippingList(Management management);
	boolean cancelInvoiceList(Management management);
	boolean cancelInvoiceInventory(Management management);
	List<Management> getNoUpdatedInvoiceList(Management management);
	boolean updateCustomerProductCodeNumber(Management management);
	boolean insertShippingResult(Management management);
	boolean updateCompleteInvoiceList(Management management);
	List<Management> getCompleteInventoryList(Management management);
	Management mobileGetShippingMarkPrintInventory(Management management);
	boolean deleteNoScanInventory(Management management);
	List<Management> getNoUpdatedOrResetInvoiceList(Management management);
	List<Management> getResetDatas(Management management);
	boolean insertShippingCancel(Management management);
	List<Management> getCancelInventoryList(Management management);
	List<Management> getResetInventoryList(Management management);
	boolean updateInvoiceIsMoved(Management management);
	List<Management> getCustomerList(Management management);
	boolean updateShippingMarkFile(Management management);
	boolean updateCustomerRemark(Management management);
	boolean insertCustomer(Management management);
}

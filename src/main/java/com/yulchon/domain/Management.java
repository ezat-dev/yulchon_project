package com.yulchon.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Management {

	private String cd_item;	//품목코드
	private String nm_item;	//품목명
	private String spec_item; //규격
	private String kgm_weight; //단중
	private String lbl_real_length; //실길이
	private String lbl_lot_no; //Lot No
	private String no_mfg_order_serial; //W/O No
	private String qty_inventory; //재고수량
	private String wgt_inventory; //재고중량
	private String cd_wh; //창고코드
	private String nm_wh; //창고명
	private String lbl_date; //발행일자
	private String nm_customer; //고객명
	private String po_customer; //고객PO
	private String no_receipt; //입하NO
	private String remarks; //비고
	private String nm_location; //위치
	
	private String invoice_no; //인보이스 pk
	private String invoice_name; //인보이스
	private String invoice_date; //인보이스 날짜
	private String invoice_name_base;
	private List<String> lotList;
	private String is_shipped;
	private String shipping_list_no;
	private List<String> invoiceList;
	private String invoice_is_shipped;
	private String out_diameter; //외경
	private String in_daimeter; //내경
	private String thickness; //두께
	private String cd_materail; //재질
	private String customer_product_code_number; //고객사에서 지정한 품번
	private List<Management> addList;
	private Integer item_count; //인보이스 같은 품목별 번호
	private Integer invoice_inventory_no;
	private String insert_date;
	private String start_date;
	private String end_date;
	private String move_invoice_name;
	private String invoice_is_moved;
	private String extra_invoice_no;
	private String extra_packing_inspection;
	private String targetField;
	private String newValue;
	private String extra_order_no;
	private String extra_part_no;
	private String extra_spec;
	private String item_seq_total;
	private String customer_id;
	private String customer_name;
	private String customer_shippingmark_file_name;
	private String regtime;
	private String update_user_id;
	private String remark;
	private String old_file_name;
	private String user_id;
	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getOld_file_name() {
		return old_file_name;
	}
	public void setOld_file_name(String old_file_name) {
		this.old_file_name = old_file_name;
	}
	public String getCustomer_shippingmark_file_name() {
		return customer_shippingmark_file_name;
	}
	public void setCustomer_shippingmark_file_name(String customer_shippingmark_file_name) {
		this.customer_shippingmark_file_name = customer_shippingmark_file_name;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public String getUpdate_user_id() {
		return update_user_id;
	}
	public void setUpdate_user_id(String update_user_id) {
		this.update_user_id = update_user_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItem_seq_total() {
		return item_seq_total;
	}
	public void setItem_seq_total(String item_seq_total) {
		this.item_seq_total = item_seq_total;
	}
	public String getExtra_spec() {
		return extra_spec;
	}
	public void setExtra_spec(String extra_spec) {
		this.extra_spec = extra_spec;
	}
	public String getExtra_part_no() {
		return extra_part_no;
	}
	public void setExtra_part_no(String extra_part_no) {
		this.extra_part_no = extra_part_no;
	}
	public String getExtra_order_no() {
		return extra_order_no;
	}
	public void setExtra_order_no(String extra_order_no) {
		this.extra_order_no = extra_order_no;
	}
	public String getTargetField() {
		return targetField;
	}
	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getExtra_invoice_no() {
		return extra_invoice_no;
	}
	public void setExtra_invoice_no(String extra_invoice_no) {
		this.extra_invoice_no = extra_invoice_no;
	}
	public String getExtra_packing_inspection() {
		return extra_packing_inspection;
	}
	public void setExtra_packing_inspection(String extra_packing_inspection) {
		this.extra_packing_inspection = extra_packing_inspection;
	}
	public String getInvoice_is_moved() {
		return invoice_is_moved;
	}
	public void setInvoice_is_moved(String invoice_is_moved) {
		this.invoice_is_moved = invoice_is_moved;
	}
	public String getMove_invoice_name() {
		return move_invoice_name;
	}
	public void setMove_invoice_name(String move_invoice_name) {
		this.move_invoice_name = move_invoice_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}
	public String getIn_daimeter() {
		return in_daimeter;
	}
	public void setIn_daimeter(String in_daimeter) {
		this.in_daimeter = in_daimeter;
	}
	public Integer getInvoice_inventory_no() {
		return invoice_inventory_no;
	}
	public void setInvoice_inventory_no(Integer invoice_inventory_no) {
		this.invoice_inventory_no = invoice_inventory_no;
	}
	public Integer getItem_count() {
		return item_count;
	}
	public void setItem_count(Integer item_count) {
		this.item_count = item_count;
	}
	public List<String> getLotList() {
		return lotList;
	}
	public void setLotList(List<String> lotList) {
		this.lotList = lotList;
	}
	public List<Management> getAddList() {
		return addList;
	}
	public void setAddList(List<Management> addList) {
		this.addList = addList;
	}
	public String getCustomer_product_code_number() {
		return customer_product_code_number;
	}
	public void setCustomer_product_code_number(String customer_product_code_number) {
		this.customer_product_code_number = customer_product_code_number;
	}
	public String getOut_diameter() {
		return out_diameter;
	}
	public void setOut_diameter(String out_diameter) {
		this.out_diameter = out_diameter;
	}
	public String getThickness() {
		return thickness;
	}
	public void setThickness(String thickness) {
		this.thickness = thickness;
	}
	public String getCd_materail() {
		return cd_materail;
	}
	public void setCd_materail(String cd_materail) {
		this.cd_materail = cd_materail;
	}
	public String getInvoice_is_shipped() {
		return invoice_is_shipped;
	}
	public void setInvoice_is_shipped(String invoice_is_shipped) {
		this.invoice_is_shipped = invoice_is_shipped;
	}
	public List<String> getInvoiceList() {
		return invoiceList;
	}
	public void setInvoiceList(List<String> invoiceList) {
		this.invoiceList = invoiceList;
	}
	public String getShipping_list_no() {
		return shipping_list_no;
	}
	public void setShipping_list_no(String shipping_list_no) {
		this.shipping_list_no = shipping_list_no;
	}
	public String getIs_shipped() {
		return is_shipped;
	}
	public void setIs_shipped(String is_shipped) {
		this.is_shipped = is_shipped;
	}
	public String getInvoice_name_base() {
		return invoice_name_base;
	}
	public void setInvoice_name_base(String invoice_name_base) {
		this.invoice_name_base = invoice_name_base;
	}
	public String getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(String invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public String getInvoice_name() {
		return invoice_name;
	}
	public void setInvoice_name(String invoice_name) {
		this.invoice_name = invoice_name;
	}
	public String getCd_item() {
		return cd_item;
	}
	public void setCd_item(String cd_item) {
		this.cd_item = cd_item;
	}
	public String getNm_item() {
		return nm_item;
	}
	public void setNm_item(String nm_item) {
		this.nm_item = nm_item;
	}
	public String getSpec_item() {
		return spec_item;
	}
	public void setSpec_item(String spec_item) {
		this.spec_item = spec_item;
	}
	public String getKgm_weight() {
		return kgm_weight;
	}
	public void setKgm_weight(String kgm_weight) {
		this.kgm_weight = kgm_weight;
	}
	public String getCd_wh() {
		return cd_wh;
	}
	public void setCd_wh(String cd_wh) {
		this.cd_wh = cd_wh;
	}
	public String getNm_wh() {
		return nm_wh;
	}
	public void setNm_wh(String nm_wh) {
		this.nm_wh = nm_wh;
	}
	public String getNm_customer() {
		return nm_customer;
	}
	public void setNm_customer(String nm_customer) {
		this.nm_customer = nm_customer;
	}
	public String getNm_location() {
		return nm_location;
	}
	public void setNm_location(String nm_location) {
		this.nm_location = nm_location;
	}
	public String getLbl_real_length() {
		return lbl_real_length;
	}
	public void setLbl_real_length(String lbl_real_length) {
		this.lbl_real_length = lbl_real_length;
	}
	public String getNo_mfg_order_serial() {
		return no_mfg_order_serial;
	}
	public void setNo_mfg_order_serial(String no_mfg_order_serial) {
		this.no_mfg_order_serial = no_mfg_order_serial;
	}
	public String getQty_inventory() {
		return qty_inventory;
	}
	public void setQty_inventory(String qty_inventory) {
		this.qty_inventory = qty_inventory;
	}
	public String getWgt_inventory() {
		return wgt_inventory;
	}
	public void setWgt_inventory(String wgt_inventory) {
		this.wgt_inventory = wgt_inventory;
	}
	public String getLbl_date() {
		return lbl_date;
	}
	public void setLbl_date(String lbl_date) {
		this.lbl_date = lbl_date;
	}
	public String getPo_customer() {
		return po_customer;
	}
	public void setPo_customer(String po_customer) {
		this.po_customer = po_customer;
	}
	public String getNo_receipt() {
		return no_receipt;
	}
	public void setNo_receipt(String no_receipt) {
		this.no_receipt = no_receipt;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getLbl_lot_no() {
		return lbl_lot_no;
	}
	public void setLbl_lot_no(String lbl_lot_no) {
		this.lbl_lot_no = lbl_lot_no;
	}
	@Override
	public String toString() {
		return "Management [cd_item=" + cd_item + ", nm_item=" + nm_item + ", spec_item=" + spec_item + ", kgm_weight="
				+ kgm_weight + ", lbl_real_length=" + lbl_real_length + ", lbl_lot_no=" + lbl_lot_no
				+ ", no_mfg_order_serial=" + no_mfg_order_serial + ", qty_inventory=" + qty_inventory
				+ ", wgt_inventory=" + wgt_inventory + ", cd_wh=" + cd_wh + ", nm_wh=" + nm_wh + ", lbl_date="
				+ lbl_date + ", nm_customer=" + nm_customer + ", po_customer=" + po_customer + ", no_receipt="
				+ no_receipt + ", remarks=" + remarks + ", nm_location=" + nm_location + ", invoice_no=" + invoice_no
				+ ", invoice_name=" + invoice_name + ", invoice_date=" + invoice_date + ", invoice_name_base="
				+ invoice_name_base + ", lotList=" + lotList + ", is_shipped=" + is_shipped + ", shipping_list_no="
				+ shipping_list_no + ", invoiceList=" + invoiceList + ", invoice_is_shipped=" + invoice_is_shipped
				+ ", out_diameter=" + out_diameter + ", in_daimeter=" + in_daimeter + ", thickness=" + thickness
				+ ", cd_materail=" + cd_materail + ", customer_product_code_number=" + customer_product_code_number
				+ ", addList=" + addList + ", item_count=" + item_count + ", invoice_inventory_no="
				+ invoice_inventory_no + ", insert_date=" + insert_date + "]";
	}

	
}

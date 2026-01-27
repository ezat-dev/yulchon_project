package com.yulchon.domain;

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
	

	
	public String getIs_shipped() {
		return is_shipped;
	}
	public void setIs_shipped(String is_shipped) {
		this.is_shipped = is_shipped;
	}
	public List<String> getLotList() {
		return lotList;
	}
	public void setLotList(List<String> lotList) {
		this.lotList = lotList;
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
				+ invoice_name_base + ", lotList=" + lotList + ", getLotList()=" + getLotList()
				+ ", getInvoice_name_base()=" + getInvoice_name_base() + ", getInvoice_date()=" + getInvoice_date()
				+ ", getInvoice_no()=" + getInvoice_no() + ", getInvoice_name()=" + getInvoice_name()
				+ ", getCd_item()=" + getCd_item() + ", getNm_item()=" + getNm_item() + ", getSpec_item()="
				+ getSpec_item() + ", getKgm_weight()=" + getKgm_weight() + ", getCd_wh()=" + getCd_wh()
				+ ", getNm_wh()=" + getNm_wh() + ", getNm_customer()=" + getNm_customer() + ", getNm_location()="
				+ getNm_location() + ", getLbl_real_length()=" + getLbl_real_length() + ", getNo_mfg_order_serial()="
				+ getNo_mfg_order_serial() + ", getQty_inventory()=" + getQty_inventory() + ", getWgt_inventory()="
				+ getWgt_inventory() + ", getLbl_date()=" + getLbl_date() + ", getPo_customer()=" + getPo_customer()
				+ ", getNo_receipt()=" + getNo_receipt() + ", getRemarks()=" + getRemarks() + ", getLbl_lot_no()="
				+ getLbl_lot_no() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>인보이스 생성 및 조회</title>
    <%-- <%@ include file="../include/sideBar.jsp" %> --%>
    <link rel="stylesheet" href="/yulchon/css/tabBar/tabBar.css">
    <%@include file="../include/pluginpage.jsp" %>
    <link rel="stylesheet" href="/yulchon/css/management/userinsert2.css">
    <script type="text/javascript" src="https://oss.sheetjs.com/sheetjs/xlsx.full.min.js"></script>
    <style>
/* =========================
   Global / Theme
========================= */
:root {
  --bg: #f6f8fb;
  --panel: #ffffff;
  --border: #e6eaf2;
  --text: #1f2937;
  --muted: #6b7280;
  --primary: #2563eb;
  --primary-weak: #e8efff;
  --shadow: 0 10px 24px rgba(16, 24, 40, 0.08);
  --radius: 14px;
}

* {
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  overflow-x: hidden; /* 가로 스크롤 '깔짝' 방지 */
}

body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Noto Sans KR",
    Arial, sans-serif;
  color: var(--text);
  background: var(--bg);
}

/* =========================
   Page Layout
========================= */
.main {
  width: min(1800px, 96vw);
  margin: 22px auto;
  padding: 14px;
}

/* 상단 바(날짜 + 버튼 영역) */
.tab {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px;
  background: var(--panel);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.button-container {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  margin-left: 0px;
}

/* =========================
   Inputs / Buttons
========================= */
.daySet {
  height: 40px;
  padding: 0 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
  background: #fff;
  color: var(--text);
  outline: none;
}

.daySet::placeholder {
  color: var(--muted);
}

.daySet:focus {
  border-color: rgba(37, 99, 235, 0.55);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

/* 날짜 input (inline style 덮어쓰기) */
#invoice_date {
  width: clamp(140px, 18vw, 250px);
  min-width: 140px;
  height: 40px;
  margin-bottom: 0 !important; /* HTML inline margin-bottom 무력화 */
  font-size: 15px !important;  /* HTML inline font-size 무력화 */
}

/* 기본 버튼 공통 */
button,
.insert-button {
  height: 40px;
  border-radius: 10px;
  border: 1px solid var(--border);
  background: #fff;
  color: var(--text);
  cursor: pointer;
  padding: 0 14px;
  font-weight: 700;
}

/* 인보이스 생성 버튼 */
#insert-invoice-button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border-color: rgba(37, 99, 235, 0.35);
  background: var(--primary);
  color: #fff;
}

#insert-invoice-button .button-image {
  width: 18px;
  height: 18px;
  filter: brightness(0) invert(1);
}

#insert-invoice-button:hover {
  background: #1d4ed8;
}

button:hover {
  background: #f3f6ff;
  border-color: rgba(37, 99, 235, 0.35);
}

/* =========================
   Main View (Two Tables)
   - 높이 통일 + 스크롤 깔짝 제거 + invoiceTable 내용 안 사라짐
========================= */

/* 두 테이블의 높이 기준: view가 잡고, 내부는 100% 따라감 */
.view {
  display: flex;
  gap: 14px;
  width: 100%;
  margin-top: 14px;

  /* 핵심: 상단(tab) 포함한 전체 레이아웃 고려한 높이 */
  height: calc(100vh - 160px);
  min-height: 520px;

  /* '깔짝' 스크롤 원인 차단 */
  overflow: hidden;
}

/* 테이블 카드 공통 */
#invoiceTable,
#dataTable {
  background: var(--panel);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);

  /* 중요: flex 안에서 가로 삐져나옴 방지 */
  min-width: 0;

  /* 중요: 테이블 라이브러리 absolute 레이아웃 대응 */
  position: relative;

  /* view 높이 따라감 */
  height: 100%;

  /* 0 높이로 떨어지는 문제 방지 */
  min-height: 300px;
}

/* 좌측 인보이스 */
#invoiceTable {
  flex: 0 0 382px;
  min-width: 320px;

  /* 여기서 overflow:hidden 하면 라이브러리 내부 스크롤이 꼬여서
     내용이 안 보이는 경우가 있어서 안전하게 auto/visible 처리 */
  overflow: hidden;
}

/* 우측 데이터 */
#dataTable {
  flex: 1 1 auto;
  overflow: hidden;
}

/* 만약 너가 쓰는 테이블이 Tabulator라면:
   Tabulator 컨테이너/holder에만 높이를 지정해야 안정적 */
#invoiceTable .tabulator,
#dataTable .tabulator {
  height: 100%;
}

/* 헤더 제외한 스크롤 영역 높이 (Tabulator 기본 헤더 높이 감안) */
#invoiceTable .tabulator-tableholder,
#dataTable .tabulator-tableholder {
  height: calc(100% - 40px);
}

/* 혹시 DataTables(scrollY) 같은 경우 내부 wrapper가 이런 구조면 대응 */
#invoiceTable .dataTables_wrapper,
#dataTable .dataTables_wrapper {
  height: 100%;
}

#invoiceTable .dataTables_scroll,
#dataTable .dataTables_scroll {
  height: 100%;
}

#invoiceTable .dataTables_scrollBody,
#dataTable .dataTables_scrollBody {
  height: calc(100% - 40px);
}

/* 기존 span margin 같은 잔여 스타일 제거 */
#dataTable span {
  margin-left: 15%;
}

/* 선택 행 강조 */
.row_select {
  background-color: #fff7d6 !important;
}

/* =========================
   Modal
========================= */
.modal,
.addDeleteModal {
  display: none;
  position: fixed;
  z-index: 999999;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.55);
  overflow: hidden;
}

.modal.show,
.addDeleteModal.show {
  display: block !important;
}



.arrow-box,
.arrow-box2 {
    margin-top: 220px;          /* arrow-box 기본값 */
    font-size: 45px;

    /* 박스 */
    width: 70px;
    height: 70px;
    border-radius: 14px;
    border: 2px solid #1e88e5;  /* 푸른색 테두리 */
    background-color: #f5faff;  /* 아주 연한 하늘색 */

    /* 가운데 정렬 */
    display: flex;
    justify-content: center;
    align-items: center;

    /* 아이콘 색 */
    color: #1e88e5;             /* 진한 블루 */

    /* UX */
    cursor: pointer;
    user-select: none;
}

/* 두 번째 화살표는 간격만 다르게 */
.arrow-box2 {
    margin-top: 20px;
}

/* hover 효과 – 하늘색 느낌 */
.arrow-box:hover,
.arrow-box2:hover {
    background-color: #dbeeff;  /* 하늘색 */
    border-color: #1565c0;      /* 조금 더 진한 블루 */
    color: #1565c0;
}

/* 클릭 시 살짝 눌린 느낌 (애니메이션 없이) */
.arrow-box:active,
.arrow-box2:active {
    background-color: #cfe6ff;
}


.modal-content,
.addDelete-modal-content {
  background: var(--panel);
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: min(1600px, 94vw);
  max-height: 95vh;
  overflow: auto;
  padding: 18px;
  border-radius: 16px;
  border: 1px solid var(--border);
  box-shadow: 0 18px 50px rgba(0, 0, 0, 0.25);
}

/* 모달 타이틀 */
.addDeleteInvoiceModalText {
  display: block;
  font-size: 20px;
  font-weight: 800;
  margin-bottom: 12px;
}

.invoice-number {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: #f8fafc;
  border: 1px solid var(--border);
  border-radius: 12px;
  margin-bottom: 14px;
}

#invoice_name_span {
  font-weight: 900;
  color: var(--primary);
}

/* =========================
   Modal Tables (Left/Right)
========================= */
.tables-container {
  display: flex;
  gap: 12px;
  align-items: stretch;
  margin-bottom: 14px;
  width: 100%;
}

.table-section {
  flex: 1 1 0;
  min-width: 0;
  background: #fbfcff;
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 10px;
}

#leftTable,
#rightTable {
  width: 100% !important;
}




.tabulator .tabulator-tableHolder {
    position: relative;
    width: 100%;
    white-space: nowrap;
    overflow: auto;
    -webkit-overflow-scrolling: touch;
}
#closeAddDeleteInvoiceProduct{
    margin-left: 46%;
    width: 120px;
    background-color: #93c4f5;
}
.legend-container {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-left: 10px; /* 버튼과의 간격 */
    padding: 0 10px;
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 6px;
}

.dot {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    display: inline-block;
    border: 1px solid var(--border); /* 흰색 동그라미를 위해 경계선 추가 */
}

.scan-complete {
    background-color: #beddf8;
    border-color: #000000; 
}
.scan-cancel {
    background-color: #f8c1be;
    border-color: #000000; 
}
.scan-reset {
    background-color: #e6e6e6;
    border-color: #000000; 
}
.scan-wait {
    background-color: #ffffff;
    border-color: #000000; 
}
.legend-text {
    font-size: 14px;
    font-weight: 600;
    color: var(--muted);
}

/* 모바일 대응: 화면이 좁아지면 범례도 자연스럽게 줄바꿈 */
@media (max-width: 1200px) {
    .legend-container {
        margin-left: 0;
        margin-top: 5px;
        width: 100%;
    }
}
/* 모달 내 좌/우 테이블도 내부 라이브러리 높이 안정화 */
.table-sectio

    </style>
</head>

<body>

    <main class="main">
        <div class="tab">
                    
                
            <div class="button-container">
            
				<input type="text"autocomplete="off" class="daySet" id="invoice_date" style="font-size: 16px; margin-bottom:10px;" placeholder="날짜 선택"> 
                
                <button class="insert-button" id="insert-invoice-button">
                    <img src="/yulchon/css/image/insert-icon.png" alt="insert" class="button-image">인보이스 생성
                </button>
                <span class="info-text">
      			  * 미처리된 인보이스를 더블클릭하면 새로운 인보이스에 추가할 수 있습니다.
    			</span>
               	<div class="legend-container">
				<div class="legend-item">
				        <span class="dot scan-reset"></span>
				        <span class="legend-text">미처리</span>
				</div>
				<div class="legend-item">
				        <span class="dot scan-wait"></span>
				        <span class="legend-text">대기</span>
				</div>
				</div>
            </div>
        </div>

        <div class="view">
        	<div id="invoiceTable"></div>
            <div id="dataTable"></div>
        </div>
    </main>

	
	<!-- 인보이스 품목 추가/삭제 모달 -->
	<div id="addDeleteInvoiceProduct" class="addDeleteModal">
	    <div class="addDelete-modal-content">
	<!--         <span class="close">&times;</span> -->
	        <label class="addDeleteInvoiceModalText">품목 추가/삭제</label>
	        
        <div class="invoice-number">
            <label>인보이스 No. : </label>
            <span id="invoice_name_span">YP-20260101-001</span>
            <span id="invoice_no_span" style="display: none"></span>
        </div>
                <div class="tables-container">
            <div class="table-section">
                <div id="leftTable"></div>
            </div>
            
            <div class="arrow-container">
                <div class="arrow-box">
                    <div class="arrow" id="insertArrow">⬅</div>
                </div>
                <div class="arrow-box2">
                    <div class="arrow" id="deleteArrow">➡</div>
                </div>
            </div>
            
            <div class="table-section">
                <div id="rightTable"></div>
            </div>
        </div>
	            
	            <button type="button" id="closeAddDeleteInvoiceProduct">닫기</button>
	    </div>
	</div>


<script>
let now_page_code = "h03";
var dataTable;
var selectedRowData = null;
var invoiceTable;
var leftTable;
var rightTable;
var selectedLeftRows = [];
var selectedRightRows = [];
var clickedInvoiceNo = "";
var clickedInvoiceIsReset = "";

// 재고현황 조회
function initDataTable(){
dataTable = new Tabulator('#dataTable', {
  height: "100%",
  layout: "fitColumns",
  headerHozAlign: "center",
  ajaxConfig: { method: 'POST' },
  ajaxLoader: false,
  ajaxURL: "/yulchon/management/getInvoiceInventoryList",
  ajaxParams: {},
  placeholder: "좌측 인보이스를 클릭하면 인보이스에 해당하는 품목이 조회됩니다.",
  ajaxResponse: function(url, params, response) {
   // console.log("서버 응답 데이터 확인:", response);
    return response;
  },
  columns: [
	{ title: "No", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false, frozen: true },
    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input"},
    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
    { title: "고객사 부여 품번", field: "customer_product_code_number", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "출력용 인보이스", field: "extra_invoice_no", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "출력용 Packinglist No/Inspection No", field: "extra_packing_inspection", width: 300, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "출력용 Order No", field: "extra_order_no", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "출력용 Part No", field: "extra_part_no", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "출력용 Spec", field: "extra_spec", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "재고수량", field: "qty_inventory", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input"},
    { title: "재고중량", field: "wgt_inventory", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "창고코드", field: "cd_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "창고명", field: "nm_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "발행일자", field: "lbl_date", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "고객명", field: "nm_customer", sorter: "string", width: 200, hozAlign: "center", headerFilter: "input" },
    { title: "고객PO", field: "po_customer", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "입하No", field: "no_receipt", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "비고", field: "remarks", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "위치", field: "nm_location", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "invoice_inventory_no", field: "invoice_inventory_no", visible: false }
  ],
  cellEdited: function(cell) {
      // cell : 수정된 셀 객체
      var rowData = cell.getRow().getData(); // 수정된 행 전체 데이터
      var field = cell.getField();           // 수정된 필드명 (customer_product_code_number)
      var newValue = cell.getValue();        // 바뀐 값
      var lbl_lot_no = rowData.lbl_lot_no;
      var invoice_inventory_no = rowData.invoice_inventory_no;
      
      console.log("수정할 필드: ", field);
      console.log("새로운 데이터: ", newValue);
      console.log("수정할 데이터: ", rowData);
      console.log("수정할 lot_no: ", lbl_lot_no);

      var updateData = {
    		  "targetField": field,
    	        "newValue": newValue,
    	        "lbl_lot_no": lbl_lot_no,
    	        "invoice_inventory_no": invoice_inventory_no
    	      }

      $.ajax({
          url: "/yulchon/management/updateCustomerProductCodeNumber",
          method: "POST",
          data: JSON.stringify(updateData),
          contentType: "application/json",
          success: function(res) {
			if(res === true || res === "true"){
				console.log("고객사 부여 품번 수정 완료");
				}else{
					console.log("고객사 부여 품번 수정 실패")
					}
  			},
		      error: function() {
		    	  console.log("고객사 부여 품번 수정 중 에러 발생");
		      }
      });
      
  },
});
}

//인보이스 조회
function initInvoiceTable(){
	invoiceTable = new Tabulator('#invoiceTable', {
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getNoUpdatedOrResetInvoiceList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  	  rowFormatter: function(row) {
	      var data = row.getData();
	      var status = (data.invoice_is_shipped || "").toString().trim().toUpperCase();
	      if(status === "R"){
	    	  row.getElement().style.backgroundColor = "#e6e6e6";
		      }
	  },
	  columns: [
		  { title: "NO", formatter: "rownum", hozAlign: "center", width: 80, frozen: true },
		  {title: "인보이스", 
	        field: "invoice_name", 
	        hozAlign: "center", 
	        width: 300, 
	        headerFilter: "input",
	        formatter: function(cell, formatterParams, onRendered) {
	            const rowData = cell.getData(); // 현재 행 데이터
	            const name = cell.getValue();   // invoice_name 값
	            
	            // invoice_is_shipped 값이 'R' 인 경우 뒤에 (미처리) 추가
	            if (rowData.invoice_is_shipped && rowData.invoice_is_shipped.trim() === "R") {
	                return name + "(미처리)";
	            }
	            return name; 
	        }
		  },
		  { title: "invoice_no", formatter: "invoice_no", hozAlign: "center", width: 80, visible:false},
		  { title: "invoice_is_shipped", field: "invoice_is_shipped", hozAlign: "center", width: 80, visible:false}
	  ],
	  rowClick: function(e, row) {
	    $('#invoiceTable .tabulator-row').removeClass('row_select');
	    row.getElement().classList.add('row_select');
	    selectedRowData = row.getData();
	    console.log("인보이스 pk: ", selectedRowData.invoice_no);
		  clickedInvoiceNo = selectedRowData.invoice_no;
		  clickedInvoiceIsReset = selectedRowData.invoice_is_shipped?.trim();
		  console.log("인보이스 리셋 여부: [", clickedInvoiceIsReset, "]");

	    //다시 로딩
	    dataTable.setData("/yulchon/management/getInvoiceInventoryList", 
	    	    { invoice_no: selectedRowData.invoice_no,
    	    invoice_is_shipped: clickedInvoiceIsReset });
	  },
	  rowDblClick: function(e, row) {
		syncEzInventoryLotExt();
	    var d = row.getData();
	    selectedRowData = d;
	    console.log("selectedRowData.invoice_name: ", selectedRowData.invoice_name);
	    console.log("미처리 여부: ", selectedRowData.invoice_is_shipped);

	    $('#invoice_name_span').text(selectedRowData.invoice_name);
	    $('#invoice_no_span').text(selectedRowData.invoice_no);
	    
	    if(selectedRowData.invoice_is_shipped && selectedRowData.invoice_is_shipped.trim() == "R"){
	    	if (confirm(selectedRowData.invoice_name + "의 미처리 품목들을\n새로운 인보이스에 추가하시겠습니까?")) {
	            
	            // 현재 dataTable 테이터 가져오기
	            var allItems = dataTable.getData(); 
	            
	            console.log("새 인보이스에 추가할 품목 배열: ", allItems);
	            console.log("추출된 품목 개수: ", allItems.length);

	            if (allItems.length === 0) {
	                alert("추가할 품목 데이터가 없습니다.");
	                return;
	            }

	            //일단 새로운 인보이스 생성
			const date = $('#invoice_date').val(); 
		    const formattedDate = date.replace(/-/g, '');
		    const invoice_name_base = 'YC-' + formattedDate;
		    console.log("invoice_name_base: ", invoice_name_base); // 20260101
		    $.ajax({
		      url: "/yulchon/management/insertInvoiceName",
		      type: "POST",
		      data: {invoice_name_base: invoice_name_base},
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		        console.log(result);
		        if (result === true || result === "true") {
			        console.log("인보이스 생성 완료");
		              
		            getRecentInvoice(function(){
		    		    //새로운 인보이스에 미처리된 품목 추가
			            insertInvoiceInventoryItems(allItems);

			            //미처리 인보이스 칼럼 업데이트
			            updateInvoiceIsMoved(selectedRowData.invoice_no);
			            
			            invoiceTable.replaceData("/yulchon/management/getNoUpdatedOrResetInvoiceList")
			            .then(() => {
				              const firstRow = invoiceTable.getRows()[0];
				              if (!firstRow) {
				                return;
				              }

				              $('#invoiceTable .tabulator-row').removeClass('row_select');
				              firstRow.getElement().classList.add('row_select');
			            
			            selectedRowData = null;
			            });
			            
			         });

		        } else {
		            alert("새로운 인보이스 생성 실패: " + result); 
		        }
		      },
		      error: function() {
		        alert('새로운 인보이스 생성 중 오류가 발생했습니다.');
		      }
		    });
		    return;
	        } else {
	            console.log("추가 취소됨");
	            return; // 취소 누르면 중단
	        }
		    }
	    
	    leftTable.setData("/yulchon/management/getInvoiceInventoryList", 
	    	    { invoice_no: selectedRowData.invoice_no });
	    
	    rightTable.setData("/yulchon/management/getInventoryList", {});
	    
	    $('#addDeleteInvoiceProduct').show().addClass('show');
	  }
	});
	}

//품목 추가/삭제 왼쪽 포함된 품목 조회
function initLeftTable(){
	leftTable = new Tabulator('#leftTable', {
	  height: "555px",
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getInvoiceInventoryList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  selectable: true,
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  columns: [
			{ title: "<div style='margin-bottom: 3px;'>No</div>", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
		    { 
		        title: "선택", 
		        field: "selected", 
		        hozAlign: "center", 
		        width: 80,
		        formatter: "rowSelection", 
		        titleFormatter: "rowSelection", 
		        //필터링된 데이터들만 선택 되도록
		        titleFormatterParams:{
			        rowRange: "active"
			        },
		        headerSort: false,
		        cellClick: function(e, cell) {
		            cell.getRow().toggleSelect();
		        }
		    },
		    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input" },
		    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
		    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "재고수량", field: "qty_inventory", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input"},
		    { title: "재고중량", field: "wgt_inventory", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "창고코드", field: "cd_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "창고명", field: "nm_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "발행일자", field: "lbl_date", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "고객명", field: "nm_customer", sorter: "string", width: 200, hozAlign: "center", headerFilter: "input" },
		    { title: "고객PO", field: "po_customer", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "입하No", field: "no_receipt", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "비고", field: "remarks", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "위치", field: "nm_location", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" }
	  ],
	  rowClick: function(e, row) {
/* 	    $('#leftTable .tabulator-row').removeClass('row_select');
	    row.getElement().classList.add('row_select'); */
	    selectedRowData = row.getData();
	  },
	  rowDblClick: function(e, row) {
	    var d = row.getData();
	    selectedRowData = d;
	    
	    $('#addDeleteInvoiceProduct').show().addClass('show');
	  },
	    //체크박스 선택/해제 시
	  rowSelectionChanged: function(data, rows){
		  selectedLeftRows = data;
		  console.log("선택된 삭제할 데이터:", selectedLeftRows, "개수:", selectedLeftRows.length);
		}
		  
	});
	}

//품목 추가/삭제 우측 인보이스 없는 품목 조회
function initRightTable(){
	rightTable = new Tabulator('#rightTable', {
	  height: "555px",
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getInventoryList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  selectable: true,
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  columns: [
			{ title: "<div style='margin-bottom: 3px;'>No</div>", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
		    { 
		        title: "선택", 
		        field: "selected", 
		        hozAlign: "center", 
		        width: 80,
		        formatter: "rowSelection", 
		        titleFormatter: "rowSelection", 
		        //필터링된 데이터들만 선택 되도록
		        titleFormatterParams:{
			        rowRange: "active"
			        },
		        headerSort: false,
		        cellClick: function(e, cell) {
		            cell.getRow().toggleSelect();
		        }
		    },
		    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input" },
		    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
		    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "재고수량", field: "qty_inventory", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input"},
		    { title: "재고중량", field: "wgt_inventory", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "창고코드", field: "cd_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "창고명", field: "nm_wh", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "발행일자", field: "lbl_date", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "고객명", field: "nm_customer", sorter: "string", width: 200, hozAlign: "center", headerFilter: "input" },
		    { title: "고객PO", field: "po_customer", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "입하No", field: "no_receipt", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "비고", field: "remarks", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "위치", field: "nm_location", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" }
	  ],
	  rowClick: function(e, row) {
/* 	    $('#dataTable .tabulator-row').removeClass('row_select');
	    row.getElement().classList.add('row_select'); */
	    selectedRowData = row.getData();
	  },
	  rowDblClick: function(e, row) {
	    var d = row.getData();
	    selectedRowData = d;
	    
	    $('#addDeleteInvoiceProduct').show().addClass('show');
	  },
	    //체크박스 선택/해제 시
	  rowSelectionChanged: function(data, rows){
		  selectedRightRows = data;
		  console.log("선택된 추가할 데이터:", selectedRightRows, "개수:", selectedRightRows.length);
		}
	});
	}

//재고 로트번호 동기화 프로시저
function syncEzInventoryLotExt(){
    $.ajax({
      url: "/yulchon/management/syncEzInventoryLotExt",
      type: "POST",
      contentType: 'application/json',
      data:{},
      //processData: false,
      //contentType: false,
      success: function(result) {
          if(result === true){
        	  console.log("재고 동기화 성공");
          }else{
	          console.log("재고 동기화 실패");
              }
        },
      error: function() {
    	  console.log("에러 발생");
      }
    });
  }

//가장 최근에 생긴 인보이스 가져오기
function getRecentInvoice(callback){
	    $.ajax({
	      url: "/yulchon/management/getRecentInvoice",
	      type: "POST",
	      contentType: 'application/json',
	      data:{},
	      //processData: false,
	      //contentType: false,
	      success: function(result) {
	          if(result && result.invoice_name){
		          console.log("새로 만들어진 인보이스: ", result.invoice_name, result.invoice_no);
	        	  $('#invoice_name_span').text(result.invoice_name);
	        	  $('#invoice_no_span').text(result.invoice_no);

	        	  if (typeof callback === "function") {
	                    callback(); 
	                }
	          }else{
		          alert("생성한 인보이스 조회 실패");
	              }
	        },
	      error: function() {
	        alert('저장 중 오류가 발생했습니다.');
	      }
	    });
	  }
$(function() {
	initDataTable();
	initInvoiceTable();
	initLeftTable();
	initRightTable();
	
	//오늘 날짜
	const today = todayDate();
	$('#startDate').val(today);
	$('#endDate').val(today);
	$('#invoice_date').val(today);

  // 인보이스 생성 모달 닫기
  $('.close, #closeModal').click(function() {
    $('#modalContainer').removeClass('show').hide();
  });
  //인보이스 품목 추가/삭제 모달 닫기
  $('#closeAddDeleteInvoiceProduct').click(function() {
	    $('#addDeleteInvoiceProduct').removeClass('show').hide();
	  });
  
  // 인보이스 생성 클릭 시
  $('.insert-button').click(function(event) {
    event.preventDefault();
    const date = $('#invoice_date').val(); 
    const formattedDate = date.replace(/-/g, '');
    const invoice_name_base = 'YC-' + formattedDate;
    console.log("invoice_name_base: ", invoice_name_base); // 20260101

    $.ajax({
      url: "/yulchon/management/insertInvoiceName",
      type: "POST",
      data: {invoice_name_base: invoice_name_base},
      //processData: false,
      //contentType: false,
      success: function(result) {
        console.log(result);
        if (result === true) {
            //인보이스 추가 모달 숨기고
            //인보이스에 품목 추가/삭제 모달 나오게
            $('#modalContainer').hide();
            //initInvoiceTable();

            invoiceTable.replaceData("/yulchon/management/getNoUpdatedOrResetInvoiceList")
            .then(() => {
              const firstRow = invoiceTable.getRows()[0];
              if (!firstRow) {
                return;
              }

              $('#invoiceTable .tabulator-row').removeClass('row_select');
              firstRow.getElement().classList.add('row_select');
              
            getRecentInvoice();
    	    initLeftTable();
    	    initRightTable();
    	    $('#addDeleteInvoiceProduct').show().addClass('show');
            selectedRowData = null;
            });
        } else {
            alert("오류: " + result); 
        }
      },
      error: function() {
        alert('저장 중 오류가 발생했습니다.');
      }
    });
  });

	//추가 화살표 클릭시
$('#insertArrow').click(function() {
	  //console.log("추가할 데이터 개수: ", selectedRightRows.length);
	  const invoice_no = $('#invoice_no_span').text();
	  //console.log("추가할 인보이스 pk: ", invoice_no);
	  
	  if (!selectedRightRows || selectedRightRows.length === 0) {
		    alert("선택된 품목이 없습니다.");
		    return;
		  }

	    const invalidRow = selectedRightRows.find(row => row.cd_wh !== 'YC_FG3');

	    if (invalidRow) {
	        alert("선택된 품목 중 제품 창고가 아닌 항목이 있습니다.");
	        return;
	    }

	  //전송 데이터
	    const payload = {
    invoice_no: invoice_no,
    addList: selectedRightRows.map(row => ({
        lbl_lot_no: row.lbl_lot_no,
        cd_item: row.cd_item
    }))
  };
	    console.log("전송 데이타: ", payload);
	    $.ajax({
		      url: "/yulchon/management/insertInvoiceInventory",
		      type: "POST",
		      contentType: 'application/json',
		      data: JSON.stringify(payload),
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		          if(result === true || result === "true"){
		        	  console.log("화살표로 품목 추가 성공");
		    	    
		    	    leftTable.setData("/yulchon/management/getInvoiceInventoryList", 
		    	    	    { invoice_no: invoice_no });
    	    	    rightTable.setData("/yulchon/management/getInventoryList", {});
    	    	    dataTable.setData("/yulchon/management/getInvoiceInventoryList", 
    	    	    	    { invoice_no: invoice_no });
		          }else{
			          console.log("추가 실패");
		              }
		        },
		      error: function() {
		        alert('저장 중 오류가 발생했습니다.');
		      }
		    });
});

//삭제 화살표 클릭시
$('#deleteArrow').click(function() {
	  //console.log("추가할 데이터 개수: ", selectedRightRows.length);
	  const invoice_no = $('#invoice_no_span').text();
	  //console.log("추가할 인보이스 pk: ", invoice_no);
	  
	  if (!selectedLeftRows || selectedLeftRows.length === 0) {
		    alert("선택된 품목이 없습니다.");
		    return;
		  }

	  //전송 데이터
	    const payload = {
  invoice_no: invoice_no,
  lotList: selectedLeftRows.map(row => row.lbl_lot_no)
};
	    console.log("전송 데이터: ", payload);
	    $.ajax({
		      url: "/yulchon/management/deleteInvoiceInventory",
		      type: "POST",
		      contentType: 'application/json',
		      data: JSON.stringify(payload),
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		          if(result === true || result === "true"){
		        	  console.log("삭제 성공");
		    	    
		    	    leftTable.setData("/yulchon/management/getInvoiceInventoryList", 
		    	    	    { invoice_no: invoice_no });
		    	    rightTable.setData("/yulchon/management/getInventoryList", {});
	    	    dataTable.setData("/yulchon/management/getInvoiceInventoryList", 
	    	    	    { invoice_no: invoice_no });
		          }else{
			          console.log("삭제 실패");
		              }
		        },
		      error: function() {
		        alert('삭제 중 오류가 발생했습니다.');
		      }
		    });
});
});


//인보이스에 품목 추가 함수(미처리 인보이스 더블릭해서 새로 추가할 때 사용)
function insertInvoiceInventoryItems(items) {
  if (!items || items.length === 0) {
      alert("추가할 품목이 없습니다.");
      return;
  }
  const invoiceNo = $('#invoice_no_span').text();
  const invoiceName = $('#invoice_name_span').text();
  console.log("추가할 인보이스 이름: ", invoiceName, "추가할 인보이스 번호: ", invoiceNo);
  
  // 전송 데이터 구조 생성
  const payload = {
      invoice_no: invoiceNo,
      addList: items.map(row => ({
          lbl_lot_no: row.lbl_lot_no,
          cd_item: row.cd_item
      }))
  };

  console.log("새로운 인보이스에 품목 추가 시 전송 데이터: ", payload);

  $.ajax({
      url: "/yulchon/management/insertInvoiceInventory",
      type: "POST",
      contentType: 'application/json',
      data: JSON.stringify(payload),
      success: function(result) {
          if (result === true || result === "true") {
              console.log("인보이스에 품목 추가 성공");
              alert("성공적으로 추가되었습니다.");

              // 테이블들 새로고침
              if (typeof leftTable !== "undefined") {
                  leftTable.setData("/yulchon/management/getInvoiceInventoryList", { invoice_no: invoiceNo });
              }
              if (typeof rightTable !== "undefined") {
                  rightTable.setData("/yulchon/management/getInventoryList", {});
              }
              if (typeof dataTable !== "undefined") {
                  dataTable.setData("/yulchon/management/getInvoiceInventoryList", { invoice_no: invoiceNo });
              }
          } else {
              console.log("추가 실패");
              alert("추가에 실패했습니다. 데이터를 확인해주세요.");
          }
      },
      error: function(xhr, status, error) {
          console.error("에러 상세:", error);
          alert('저장 중 서버 오류가 발생했습니다.');
      }
  });
}

//미처리 된 인보이스 중 데이터 이동하면 인보이스 칼럼 2개 변경(invoice_is_moved, move_invoice_name)
function updateInvoiceIsMoved(invoiceNo) {
  if (!invoiceNo || invoiceNo.length === 0) {
      console.log("인보이스 is_moved 업데이트 중 데이터 안 들어옴");
      return;
  }
  const moveInvoiceName = $('#invoice_name_span').text();
  // 전송 데이터 구조 생성
  const payload = {
      invoice_no: invoiceNo,
      move_invoice_name: moveInvoiceName
  };

  console.log("새로운 인보이스에 품목 추가 시 전송 데이터: ", payload);

  $.ajax({
      url: "/yulchon/management/updateInvoiceIsMoved",
      type: "POST",
      contentType: 'application/json',
      data: JSON.stringify(payload),
      success: function(result) {
          if (result === true || result === "true") {
              console.log("미처리 인보이스 칼럼 업데이트 성공");
          } else {
              console.log("미처리 인보이스 칼럼 업데이트 실패");
          }
      },
      error: function(xhr, status, error) {
          console.error("에러 상세:", error);
          alert('저장 중 서버 오류가 발생했습니다.');
      }
  });
}
</script>


</body>
</html>
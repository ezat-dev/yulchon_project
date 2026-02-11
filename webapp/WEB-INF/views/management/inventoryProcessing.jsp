<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>출하 완료 처리</title>
    <%-- <%@ include file="../include/sideBar.jsp" %> --%>
    <link rel="stylesheet" href="/yulchon/css/tabBar/tabBar.css">
    <%@include file="../include/pluginpage.jsp" %>
    <link rel="stylesheet" href="/yulchon/css/management/userinsert2.css">
    <script type="text/javascript" src="https://oss.sheetjs.com/sheetjs/xlsx.full.min.js"></script>
    <style>
/* =========================
   Global / Theme
========================= */
:root{
  --bg:#f6f8fb;
  --panel:#ffffff;
  --border:#e6eaf2;
  --text:#1f2937;
  --muted:#6b7280;
  --primary:#2563eb;
  --primary-weak:#e8efff;
  --shadow:0 10px 24px rgba(16,24,40,.08);
  --radius:14px;
}

*{ box-sizing:border-box; }
html,body{ height:100%; overflow-x:hidden; }

body{
  margin:0;
  padding:0;
  font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Noto Sans KR",Arial,sans-serif;
  background:var(--bg);
  color:var(--text);
}

/* =========================
   Layout
========================= */
.main{
  width:min(1800px, 96vw);
  margin:22px auto;
  padding:14px;
}

/* 상단 바 */
.tab{
  width:100%;
  padding:14px;
  background:var(--panel);
  border:1px solid var(--border);
  border-radius:var(--radius);
  box-shadow:var(--shadow);
}

/* 버튼/필터 라인 */
.button-container{
  display:flex;
  align-items:center;
  gap:10px;
  flex-wrap:wrap;
  margin:0;           /* 기존 margin-top/left 제거 */
}

/* 오른쪽 날짜 영역(필터) */
.box1{
  display:flex;
  align-items:center;
  gap:10px;
  margin-left:auto;   /* 핵심: 오른쪽으로 밀기 */
  width:auto;         /* 고정 895px 제거 */
}

/* 텍스트 */
.daylabel{
  margin:0;
  font-size:15px;
  font-weight:700;
  color:var(--muted);
  white-space:nowrap;
}

.mid{
  font-size:18px;
  font-weight:900;
  color:var(--muted);
  height:auto;
  margin:0;
  padding:0 2px;
}

/* =========================
   Inputs
========================= */
.daySet{
  height:40px;
  padding:0 12px;
  border:1px solid var(--border);
  border-radius:10px;
  background:#fff;
  color:var(--text);
  outline:none;
  text-align:left;         /* 날짜는 좌측 정렬이 더 자연스러움 */
  width:clamp(150px, 16vw, 220px);
  margin-bottom:0 !important; /* inline style 무력화 */
  font-size:15px !important;
}

.daySet::placeholder{ color:var(--muted); }

.daySet:focus{
  border-color:rgba(37,99,235,.55);
  box-shadow:0 0 0 3px rgba(37,99,235,.12);
}

/* =========================
   Buttons
========================= */
button{
  height:40px;
  border-radius:10px;
  border:1px solid var(--border);
  background:#fff;
  color:var(--text);
  cursor:pointer;
  padding:0 14px;
  font-weight:800;
  display:inline-flex;
  align-items:center;
  gap:8px;
}

.button-image{
  width:18px;
  height:18px;
}

/* 공통 hover */
button:hover{
  background:#f3f6ff;
  border-color:rgba(37,99,235,.35);
}

/* 주요 버튼(출하 완료) */
#insert-invoice-button{
  background:var(--primary);
  color:#fff;
  border-color:rgba(37,99,235,.35);
}
#insert-invoice-button .button-image{
  filter:brightness(0) invert(1);
}
#insert-invoice-button:hover{
  background:#1d4ed8;
}

/* 취소 버튼(출하 취소) */
#delete-invoice-button{
  background:#fff;
  border-color:rgba(220,38,38,.35);
  color:#b91c1c;
}
#delete-invoice-button:hover{
  background:#fff5f5;
  border-color:rgba(220,38,38,.55);
}

/* 조회 버튼 */
.select-button{
  background:var(--primary-weak);
  border-color:rgba(37,99,235,.25);
  color:#1d4ed8;
}
.select-button:hover{
  background:#dbeeff;
  border-color:rgba(37,99,235,.35);
}

/* 품목 삭제 버튼 */
#delete-product-button{
  background:#fff;
  border-color:rgba(148,163,184,.7);
  color:#334155;
}
#delete-product-button:hover{
  background:#f8fafc;
  border-color:rgba(37,99,235,.25);
}

/* 고정 폭 제거(원하면 유지 가능) */
#insert-invoice-button,
#delete-invoice-button{
  width:auto;
}

/* =========================
   Main View (Tables)
========================= */
.view{
  display:flex;
  gap:14px;
  width:100%;
  margin-top:14px;

  height:calc(100vh - 160px); /* 상단바+여백 감안 */
  min-height:520px;

  overflow:hidden; /* '깔짝' 스크롤 차단 */
}

#invoiceTable,
#dataTable{
  background:var(--panel);
  border:1px solid var(--border);
  border-radius:var(--radius);
  box-shadow:var(--shadow);
  min-width:0;
  position:relative;
  height:100%;
  overflow:hidden;
}

#invoiceTable{
  flex:0 0 380px;
  min-width:320px;
}

#dataTable{
  flex:1 1 auto;
}

/* Tabulator 사용 시 높이 안정화 */
#invoiceTable .tabulator,
#dataTable .tabulator{
  height:100%;
  width:100%;
}
.tabulator .tabulator-tableHolder {
    position: relative;
    width: 101%;
    white-space: nowrap;
    overflow: auto;
    -webkit-overflow-scrolling: touch;
}
#invoiceTable .tabulator-tableholder,
#dataTable .tabulator-tableholder{
  height:calc(100% - 42px);
  overflow-y:auto;
  overflow-x:hidden;
}

/* 기존 span 마진(필요하면 유지) */
#dataTable span{
  margin-left:15%;
}

/* 선택 행 */
.row_select{
  background-color:#fff7d6 !important;
}

/* =========================
   Modal (기존 유지용, 필요시)
========================= */
.modal{
  display:none;
  position:fixed;
  inset:0;
  background:rgba(15,23,42,.55);
  overflow:hidden;
  z-index:999999;
}
.modal.show{ display:block; }

.modal-content{
  background:#fff;
  position:absolute;
  top:50%;
  left:50%;
  transform:translate(-50%,-50%);
  width:min(500px, 92vw);
  max-height:85vh;
  overflow:auto;
  padding:18px;
  border-radius:16px;
  border:1px solid var(--border);
  box-shadow:0 18px 50px rgba(0,0,0,.25);
}
/* =========================
   Responsive
========================= */
@media (max-width: 1200px){
  .box1{
    width:100%;
    margin-left:0;
    justify-content:flex-start;
    flex-wrap:wrap;
  }

  .view{
    flex-direction:column;
    height:auto;
    overflow:visible;
  }

  #invoiceTable,
  #dataTable{
    height:70vh;
  }

  #invoiceTable{
    flex:none;
    width:100%;
    min-width:0;
  }
}
/* =========================
   Legend (범례)
========================= */
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

/* 스캔 완료: 요청하신 #E8F5E9 색상 */
.scan-complete {
    background-color: #E8F5E9;
    border-color: #000000; 
}

/* 스캔 대기: 흰색 */
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
    </style>
</head>

<body>

    <main class="main">
        <div class="tab">
                    
                
            <div class="button-container">

                <button class="insert-button" id="insert-invoice-button">
                    <img src="/yulchon/css/image/insert-icon.png" alt="insert" class="button-image">출하 완료
                </button>
               <button class="insert-button" id="delete-invoice-button">
                    <img src="/yulchon/css/image/delete-icon.png" alt="insert" class="button-image">출하 취소
                </button>
                
               <div class="box1">
	           <p class="tabP" style="font-size: 20px; margin-left: 40px; color: white; font-weight: 800;"></p>
	           <label class="daylabel">인보이스 생성날짜 :</label>
				<input type="text" autocomplete="off" class="daySet" id="startDate" style="font-size: 16px; margin-bottom:10px;" placeholder="시작 날짜 선택">
				
				 <span class="mid"  style="font-size: 20px; font-weight: bold; margin-botomm:10px;"> ~ </span>
	 	<input type="text"autocomplete="off" class="daySet" id="endDate" style="font-size: 16px; margin-bottom:10px;" placeholder="종료 날짜 선택"> 
	</div>

	           
		
                <button class="select-button">
                    <img src="/yulchon/css/image/search-icon.png" alt="select" class="button-image">조회
                </button>
                
                <button class="insert-button" id="delete-product-button">
                    <img src="/yulchon/css/image/delete-icon.png" alt="insert" class="button-image">품목 삭제
                </button>
                <div class="legend-container">
				    <div class="legend-item">
				        <span class="dot scan-complete"></span>
				        <span class="legend-text">스캔 완료</span>
				    </div>
				    <div class="legend-item">
				        <span class="dot scan-wait"></span>
				        <span class="legend-text">스캔 대기</span>
				    </div>
				</div>
<!--                 <button class="insert-button">
                    <img src="/yulchon/css/image/insert-icon.png" alt="insert" class="button-image">추가
                </button>
                 <button class="delete-button">
				    <img src="/yulchon/css/image/delete-icon.png" alt="delete" class="button-image"> 삭제
				</button> -->
                
            <!--     
                <button class="excel-button">
                    <img src="/geomet/css/tabBar/excel-icon.png" alt="excel" class="button-image">엑셀
                </button> -->
                
            </div>
        </div>

        <div class="view">
        	<div id="invoiceTable"></div>
            <div id="dataTable"></div>
        </div>
    </main>


<script>
let now_page_code = "h03";
var dataTable;
var selectedRowData = null;
var invoiceTable;
var leftTable;
var rightTable;
var selectedProductRows = [];
var selectedInvoiceRows = [];

// 재고현황 조회
function initDataTable(){
dataTable = new Tabulator('#dataTable', {
  layout: "fitColumns",
  headerHozAlign: "center",
  ajaxConfig: { method: 'POST' },
  ajaxLoader: false,
  ajaxURL: "/yulchon/management/getShippingList",
  ajaxParams: {},
  placeholder: "좌측 인보이스를 클릭하면 인보이스에 해당하는 품목이 조회됩니다.",
  ajaxResponse: function(url, params, response) {
   // console.log("서버 응답 데이터 확인:", response);
    return response;
  },
  rowFormatter: function(row) {
      var data = row.getData();
      // shipping_list_no가 존재하고(null이나 undefined가 아님) 빈 문자열이 아닐 때
      if (data.shipping_list_no !== null && data.shipping_list_no !== undefined && data.shipping_list_no !== "") {
          row.getElement().style.backgroundColor = "#E8F5E9"; 
          // 글자색도 변경하고 싶다면 아래 주석 해제
          // row.getElement().style.color = "#000"; 
      }
  },
  columns: [
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
		{ title: "No", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
	    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input"},
	    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
	    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
	    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
	    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
	    { title: "고객사 부여 품번", field: "customer_product_code_number", width: 170, hozAlign: "center", headerFilter: "input", editor: "input" },
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
	    { title: "번호", field: "shipping_list_no", visible: false}
  ],
  //체크박스 선택/해제 시
  rowSelectionChanged: function(data, rows){
	  selectedProductRows = data;
	  console.log("선택된 삭제할 데이터:", selectedProductRows, "개수:", selectedProductRows.length);
	}
});
}

//출하처리 안된 인보이스 조회
function initInvoiceTable(){
	invoiceTable = new Tabulator('#invoiceTable', {
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getNoUpdatedInvoiceList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  columns: [
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
		  { title: "invoice_no", formatter: "invoice_no", hozAlign: "center", width: 80, visible:false},
		  { title: "NO", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false },
	    { title: "인보이스", field: "invoice_name", hozAlign: "center", width: 240, headerFilter: "input" }
	  ],
	  rowClick: function(e, row) {
		    $('#invoiceTable .tabulator-row').removeClass('row_select');
		    row.getElement().classList.add('row_select');
		    selectedRowData = row.getData();
		    console.log("인보이스 pk: ", selectedRowData.invoice_no);
			  clickedInvoiceNo = selectedRowData.invoice_no;

		    //다시 로딩
		    dataTable.setData("/yulchon/management/getShippingList", 
		    	    { invoice_no: selectedRowData.invoice_no });
		  },

		  //체크박스 선택/해제 시
		  rowSelectionChanged: function(data, rows){
			  selectedInvoiceRows = data;
			  console.log("선택된 삭제할 데이터:", selectedInvoiceRows, "개수:", selectedInvoiceRows.length);
			}
	});
	}
$(function() {
	var today = new Date();
    
    var yesterday = new Date();
    yesterday.setDate(today.getDate() - 1);
    
    var tomorrow = new Date();
    tomorrow.setDate(today.getDate() + 1);

    // 날짜를 ISO 형식(YYYY-MM-DD)으로 변환하여 input에 세팅
    $('#startDate').val(yesterday.toISOString().substring(0, 10));
    $('#endDate').val(tomorrow.toISOString().substring(0, 10));
    
	initDataTable();
	initInvoiceTable();

  // 출하완료 클릭 시(스캔한 품목들만 출하완료)
  $('#insert-invoice-button').click(function() {
	  if (!selectedInvoiceRows || selectedInvoiceRows.length === 0) {
		    alert("선택된 인보이스가 없습니다.");
		    return;
		  }
	  
	  //전송 데이터
	    const payload = {
  invoiceList: selectedInvoiceRows.map(row => row.invoice_no)
};
	    console.log("전송 데이타: ", payload);
	    $.ajax({
		      url: "/yulchon/management/shippingComplete",
		      type: "POST",
		      contentType: 'application/json',
		      data: JSON.stringify(payload),
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		          if(result === true || result === "true"){
		        	  console.log("출하완료 성공");
		        	  alert("출하 완료 성공");
			    	    invoiceTable.setData("/yulchon/management/getNoUpdatedInvoiceList", 
			    	    	    {});
			    	    dataTable.setData("/yulchon/management/getShippingList", 
			    	    	    {});
		          }else{
			          console.log("출하완료 실패");
		              }
		        },
		      error: function() {
		        alert('출하완료 중 오류가 발생했습니다.');
		      }
		    });
  });

  // 품목삭제 버튼 클릭 시
  $('#delete-product-button').click(function() {
	  //console.log("추가할 데이터 개수: ", selectedRightRows.length);
	  //console.log("추가할 인보이스 pk: ", invoice_no);
	  
	  if (!selectedProductRows || selectedProductRows.length === 0) {
		    alert("선택된 품목이 없습니다.");
		    return;
		  }

	  const invoice_no = selectedProductRows[0].invoice_no;
	  //전송 데이터
	    const payload = {
    invoice_no: invoice_no,
    lotList: selectedProductRows.map(row => row.lbl_lot_no)
  };
	    console.log("전송 데이타: ", payload);
	    $.ajax({
		      url: "/yulchon/management/deleteShippingListInventory",
		      type: "POST",
		      contentType: 'application/json',
		      data: JSON.stringify(payload),
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		          if(result === true || result === "true"){
		        	  console.log("출하목록 삭제 성공");
			    	    dataTable.setData("/yulchon/management/getShippingList", 
			    	    	    { invoice_no: invoice_no });
		          }else{
			          console.log("삭제 실패");
		              }
		        },
		      error: function() {
		        alert('저장 중 오류가 발생했습니다.');
		      }
		    });
  });

  // 출하취소 버튼 클릭 시
  $('#delete-invoice-button').click(function() {
	  
	  if (!selectedInvoiceRows || selectedInvoiceRows.length === 0) {
		    alert("선택된 인보이스가 없습니다.");
		    return;
		  }

	  //전송 데이터
	    const payload = {
    invoiceList: selectedInvoiceRows.map(row => row.invoice_no)
  };
	    console.log("전송 데이타: ", payload);
	    $.ajax({
		      url: "/yulchon/management/cancelShippingList",
		      type: "POST",
		      contentType: 'application/json',
		      data: JSON.stringify(payload),
		      //processData: false,
		      //contentType: false,
		      success: function(result) {
		          if(result === true || result === "true"){
		        	  console.log("출하취소 성공");
		        	  alert("출하 취소되었습니다.");
			    	    invoiceTable.setData("/yulchon/management/getNoUpdatedInvoiceList", 
			    	    	    {});
			    	    dataTable.setData("/yulchon/management/getShippingList", 
			    	    	    {});
		          }else{
			          console.log("출하취소 실패");
		        	  alert("출하 취소 실패했습니다.")
		              }
		        },
		      error: function() {
		        alert('취소 중 오류가 발생했습니다.');
		      }
		    });
  });

  // 인보이스 생성 모달 닫기
  $('.close, #closeModal').click(function() {
    $('#modalContainer').removeClass('show').hide();
  });
  //인보이스 품목 추가/삭제 모달 닫기
  $('#closeAddDeleteInvoiceProduct').click(function() {
	    $('#addDeleteInvoiceProduct').removeClass('show').hide();
	  });

  // 저장 버튼 클릭 시
  $('#saveCorrStatus').click(function(event) {
    event.preventDefault();
    const invoice_datee = $('#invoice_date').val(); 
    const invoice_date = invoice_datee.replace(/-/g, '');
    console.log("invoice_date", invoice_date); // 20260101

    $.ajax({
      url: "/yulchon/management/insertInvoiceName",
      type: "POST",
      data: formData,
      //processData: false,
      //contentType: false,
      success: function(result) {
        console.log(result);
        if (result === true) {
            $('#modalContainer').hide();
            dataTable.setData("/yulchon/management/getInventoryList", {});
            selectedRowData = null;
        } else {
            alert("오류: " + result.data); 
        }
      },
      error: function() {
        alert('저장 중 오류가 발생했습니다.');
      }
    });
  });
});
</script>


</body>
</html>
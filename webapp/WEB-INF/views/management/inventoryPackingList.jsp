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
 제공해주신 코드에서 가장 문제가 되는 **고정 픽셀(1008px, 1200px 등)**과 **부모를 벗어나는 너비(113%)**를 제거하고, 어떤 모니터 해상도에서도 화면에 꽉 차게 들어오는 Flexbox 기반 반응형 레이아웃으로 수정했습니다.

이 코드를 복사해서 기존 <style> 태그 내용을 교체하시면 바로 적용됩니다.

CSS

<style>
/* 1. 기본 설정 - 박스 모델 고정 */
* {
    box-sizing: border-box;
}

body {
    margin: 0;
    padding: 0;
    font-family: sans-serif;
}

/* 2. 메인 컨테이너 - 고정 마진 제거 및 중앙 정렬 */
.container {
    display: flex;
    flex-direction: column; /* 세로 배치로 변경하여 유연성 확보 */
    padding: 20px;
    margin: 40px auto; /* 중앙 정렬 */
    width: 98%;      /* 모니터 너비에 맞춰 꽉 참 */
    max-width: 1800px; 
}

/* 상단 날짜 선택/버튼 영역 */
.box1 {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    margin-bottom: 20px;
    gap: 10px;
}

/* 3. 테이블 뷰 영역 - 화면에 꽉 차게 배치 */
.view {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 20px;       /* 테이블 간 간격 */
    width: 100%;     /* 113% 제거 */
    overflow-x: hidden; 
}

/* 왼쪽 인보이스 테이블 (작은 테이블) */
#invoiceTable {
    flex: 0 0 380px; /* 고정 너비 유지하되 유연함 부여 */
    min-width: 300px;
}

/* 오른쪽 데이터 테이블 (큰 테이블) */
#dataTable {
height: 85vh !important;
}
/* 모달 내 좌우 테이블 배치 */
.tables-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
    margin-bottom: 25px;
    width: 100%;
}

.table-section {
    flex: 1;         /* 좌우 테이블 5:5 비율 */
    min-width: 0;    /* Flex 자식 찌그러짐 방지 */
}

#leftTable, #rightTable {
    width: 100% !important; /* 고정 688px 제거 */
}

/* 화살표 박스 */
.arrow-container {
    flex: 0 0 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}

/* 5. 공통 컴포넌트 수치 조정 */
.daySet {
    height: 35px;
    padding: 5px 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.mid {
    font-size: 18px;
    font-weight: bold;
    padding: 0 10px;
}

.form-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
}
/* 1. 모달 전체 배경 */
.modal, .addDeleteModal {
    display: none;
    position: fixed;    /* 화면에 고정 */
    z-index: 999999;    /* 최상단으로 올림 */
    left: 0;
    top: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.6); /* 배경을 약간 더 어둡게 */
    overflow: hidden;   /* 모달 떴을 때 배경 스크롤 방지용 */
}

/* 2. 모달 콘텐츠 (중앙 정렬 핵심) */
.modal-content, .addDelete-modal-content {
    background: white;
    position: absolute; /* 부모(.modal) 기준 */
    top: 50%;           /* 세로 중앙 */
    left: 50%;          /* 가로 중앙 */
    transform: translate(-50%, -50%) scale(0.9); /* 중앙 보정 및 애니메이션 준비 */
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0px 10px 40px rgba(0, 0, 0, 0.4);
    transition: all 0.3s ease-in-out;
    opacity: 0;
    margin: 0;          /* 기존 margin 제거 */
}

/* 3. 일반 등록 모달 크기 */
.modal-content {
    width: 400px;       /* 고정 너비 혹은 비율 */
    max-height: 90vh;
    overflow-y: auto;
}

/* 4. 추가/삭제 전용 큰 모달 크기 */
.addDelete-modal-content {
    width: 95%;
    max-width: 1600px;
    height: auto;
    max-height: 95vh;
    overflow-y: auto;
}

/* 5. 모달이 열렸을 때 (show 클래스) */
.modal.show, .addDeleteModal.show {
    display: block !important;
}

.modal.show .modal-content, 
.addDeleteModal.show .addDelete-modal-content {
    transform: translate(-50%, -50%) scale(1); /* 원래 크기로 */
    opacity: 1;
}
/* 6. 모니터 해상도가 작아질 때 (1200px 이하) 대응 */
@media (max-width: 1200px) {
    .view {
        flex-direction: column; /* 테이블을 위아래로 배치 */
        align-items: center;
    }
    #invoiceTable, #dataTable {
        width: 100%;
        flex: none;
    }
    .tables-container {
        flex-direction: column;
    }
    .arrow-container {
        flex-direction: row;
        margin: 20px 0;
    }
}
.addDeleteInvoiceModalText{
font-size: 30px;
}
#invoice_name_span{
font-weight: bold;
}
/* 애니메이션 */
@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
.arrow {
    /* 기존 45px에서 대폭 상향 */
    font-size: 50px; 
    
    /* 진한 하늘색 (Deep Sky Blue 계열) */
    color: #007bff; 
    
    /* 볼드 처리 (화살표 자체 두께) */
    font-weight: 900; 
    
    /* 텍스트 줄간격으로 인한 여백 제거 */
    line-height: 1;
    
    /* 마우스 커서 포인터 유지 */
    cursor: pointer;
    
    /* 선택 방지 (더블 클릭 시 텍스트 블록 지정 방지) */
    user-select: none;
    
    transition: transform 0.2s ease;
}

/* 화살표 박스 크기도 화살표에 맞춰 자동 조절되도록 수정 */
.arrow-box {
    border: 3px solid #007bff; /* 테두리도 진한 하늘색으로 맞춤 */
    border-radius: 12px;
    padding: 5px; /* 내부 여백 확보 */
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
.arrow-box:hover {
    background-color: #c5e2fb;
}
#closeAddDeleteInvoiceProduct {
    display: block;
    margin: 20px auto 0;
    padding: 10px 40px;
    font-size: 14px;
    font-weight: bold;
    border: 2px solid #4a90e2;
    background-color: white;
    color: #4a90e2;
    cursor: pointer;
    border-radius: 4px;
    transition: all 0.3s;
}
#closeAddDeleteInvoiceProduct:hover {
    background-color: #4a90e2;
    color: white;
}
.button-container {
    display: flex;
    gap: 10px;
    margin-left: 1%;
    margin-right: 10px;
}
#invoice_date{
/* 기본적으로 부모 너비의 100%를 시도하되 */
    width: 25%; 
    
    /* 모니터에서 너무 길어지면 보기 흉하므로 최대폭 제한 */
    max-width: 250px; 
    
    /* 너무 좁아져서 날짜가 잘리지 않도록 최소폭 제한 */
    min-width: 88px; 
    height: 29px;
}
#dataTable span{
margin-left: 15%;
}
.row_select {
background-color: #ffeeba !important;
}
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
                
<!--                <div class="box1">
	           <p class="tabP" style="font-size: 20px; margin-left: 40px; color: white; font-weight: 800;"></p>
	           <label class="daylabel">발행일자 :</label>
				<input type="text" autocomplete="off" class="daySet" id="startDate" style="font-size:16px; margin-bottom:10px; text-align:center; border-radius:6px; border:1px solid #ccc;" placeholder="시작 날짜 선택">
				
				 <span class="mid"  style="font-size: 20px; font-weight: bold; margin-botomm:10px;"> ~ </span>
	 	<input type="text"autocomplete="off" class="daySet" id="endDate" style="font-size: 16px; margin-bottom:10px;" placeholder="종료 날짜 선택"> 
	</div>

	           
		
                <button class="select-button">
                    <img src="/yulchon/css/image/search-icon.png" alt="select" class="button-image">조회
                </button> -->
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
	
	<!-- 인보이스 부여 모달 -->
<!-- 	<div id="modalContainer" class="modal">
	    <div class="modal-content">
	        <span class="close">&times;</span>
	        <h2>인보이스 생성</h2>
	        <form id="corrForm"autocomplete="off">
		 	<input type="text"autocomplete="off" class="daySet" id="invoice_date" style="font-size: 16px; margin-bottom:10px;" placeholder="날짜 선택"> 
	            
	            <button type="submit" id="saveCorrStatus">저장</button>
	            <button type="button" id="closeModal">닫기</button>
	        </form>
	    </div>
	</div> -->
	
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
                <div class="arrow-box">
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
    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
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
    $('#dataTable .tabulator-row').removeClass('row_select');
    row.getElement().classList.add('row_select');
    selectedRowData = row.getData();
  },
  rowDblClick: function(e, row) {
    var d = row.getData();
    selectedRowData = d;
    $('#corrForm')[0].reset();
    $('input[name="no"]').val(d.idx);
    $('input[name="user_code"]').val(d.user_code);
    $('input[name="user_id"]').val(d.user_id);
    $('input[name="user_pw"]').val(d.user_pw);
    $('input[name="st_day"]').val(d.st_day);
    $('input[name="user_phone"]').val(d.user_phone);
    $('input[name="user_name"]').val(d.user_name);
    $('select[name="user_level"]').val(d.user_level);
    $('input[name="user_busu"]').val(d.user_busu);
    $('input[name="user_jick"]').val(d.user_jick);
  }
});
}

//인보이스 조회
function initInvoiceTable(){
	invoiceTable = new Tabulator('#invoiceTable', {
	  height: "705px",
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getInvoiceList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  columns: [
		  { title: "NO", formatter: "rownum", hozAlign: "center", width: 80, frozen: true },
	    { title: "인보이스", field: "invoice_name", hozAlign: "center", width: 300, headerFilter: "input" },
		  { title: "invoice_no", formatter: "invoice_no", hozAlign: "center", width: 80, visible:false}
	  ],
	  rowClick: function(e, row) {
	    $('#invoiceTable .tabulator-row').removeClass('row_select');
	    row.getElement().classList.add('row_select');
	    selectedRowData = row.getData();
	    console.log("인보이스 pk: ", selectedRowData.invoice_no);
		  clickedInvoiceNo = selectedRowData.invoice_no;

	    //다시 로딩
	    dataTable.setData("/yulchon/management/getInvoiceInventoryList", 
	    	    { invoice_no: selectedRowData.invoice_no });
	  },
	  rowDblClick: function(e, row) {
		syncEzInventoryLotExt();
	    var d = row.getData();
	    selectedRowData = d;
	    console.log("selectedRowData.invoice_name: ", selectedRowData.invoice_name);

	    $('#invoice_name_span').text(selectedRowData.invoice_name);
	    $('#invoice_no_span').text(selectedRowData.invoice_no);
	    
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
			{ title: "<div style='margin-bottom: 15px;'>No</div>", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
		    { 
		        title: "<div style='margin-bottom: 15px;'>선택</div>", 
		        field: "selected", 
		        hozAlign: "center", 
		        width: 80,
		        formatter: "rowSelection", 
		        //titleFormatter: "rowSelection", 
		        headerSort: false,
		        cellClick: function(e, cell) {
		            cell.getRow().toggleSelect();
		        }
		    },
		    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input" },
		    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
		    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
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
			{ title: "<div style='margin-bottom: 15px;'>No</div>", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
		    { 
		        title: "<div style='margin-bottom: 15px;'>선택</div>", 
		        field: "selected", 
		        hozAlign: "center", 
		        width: 80,
		        formatter: "rowSelection", 
		        //titleFormatter: "rowSelection", 
		        headerSort: false,
		        cellClick: function(e, cell) {
		            cell.getRow().toggleSelect();
		        }
		    },
		    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120, headerFilter: "input" },
		    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center", headerFilter: "input" },
		    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input"},
		    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center", headerFilter: "input" },
		    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center", headerFilter: "input" },
		    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center", headerFilter: "input" },
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

            invoiceTable.replaceData("/yulchon/management/getInvoiceList")
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

  //가장 최근에 생긴 인보이스 가져오기
function getRecentInvoice(){
	    $.ajax({
	      url: "/yulchon/management/getRecentInvoice",
	      type: "POST",
	      contentType: 'application/json',
	      data:{},
	      //processData: false,
	      //contentType: false,
	      success: function(result) {
	          if(result && result.invoice_name){
	        	  $('#invoice_name_span').text(result.invoice_name);
	        	  $('#invoice_no_span').text(result.invoice_no);
	          }else{
		          alert("생성한 인보이스 조회 실패");
	              }
	        },
	      error: function() {
	        alert('저장 중 오류가 발생했습니다.');
	      }
	    });
	  }

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
    lotList: selectedRightRows.map(row => row.lbl_lot_no)
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
		        	  console.log("추가 성공");
		    	    
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
		        	  console.log("추가 성공");
		    	    
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
	
});
</script>


</body>
</html>
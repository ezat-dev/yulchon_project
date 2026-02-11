<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>출하 이력 조회 및 관리</title>
    <%-- <%@ include file="../include/sideBar.jsp" %> --%>
    <link rel="stylesheet" href="/yulchon/css/tabBar/tabBar.css">
    <%@include file="../include/pluginpage.jsp" %>
    <link rel="stylesheet" href="/yulchon/css/management/userinsert2.css">
    <script type="text/javascript" src="https://oss.sheetjs.com/sheetjs/xlsx.full.min.js"></script>
    <style>
:root {
  --bg: #f7f8fa;
  --card: #ffffff;
  --text: #111827;
  --muted: #6b7280;
  --border: #e5e7eb;
  --shadow: 0 8px 24px rgba(0, 0, 0, 0.08);

  --radius: 12px;
  --radius-sm: 10px;

  --gap-1: 8px;
  --gap-2: 12px;
  --gap-3: 16px;
  --gap-4: 20px;

  --control-h: 40px;
  --control-font: 15px;
}

* { box-sizing: border-box; }
html, body { height: 100%; }

body {
  margin: 0;
  color: var(--text);
  background: var(--bg);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Noto Sans KR",
               "Apple SD Gothic Neo", "Malgun Gothic", Arial, sans-serif;
}

/* =========================
   Layout
========================= */
.main {
  width: min(1800px, calc(100% - 32px));
  margin: 24px auto;
}

/* 상단 바(탭) */
.tab {
  width: 100%;
  min-height: 64px;
  margin: 0 0 16px 0;
  padding: 14px 16px;

  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--gap-3);

  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

/* 혹시 남아있던 예전 container 스타일 무력화 */
.container {
  display: flex;
  justify-content: space-between;
  padding: 0;
  margin: 0;
}

/* =========================
   Top Filter Row (유형/일자/조회)
========================= */

/* button-container: 한 줄 기준 정렬 */
.button-container {
  width: 100% !important;                 /* 51% 같은 값 무시 */
  display: flex;
  align-items: center;                    /* 높이 맞추는 핵심 */
  justify-content: flex-start;            /* space-between 금지 */
  gap: var(--gap-2);
  flex-wrap: wrap;
}

/* box1: 강제 이동 제거 + 수평정렬 */
.box1 {
  display: flex;
  align-items: center;                    /* 높이 맞추는 핵심 */
  gap: var(--gap-2);
  flex-wrap: wrap;

  width: auto;
  margin: 0 !important;                   /* margin-left:-94% 무시 */
}

/* 레이블 공통 */
.daylabel,
.category,
.button-container label {
  font-size: 15px;
  font-weight: 700;
  color: var(--text);
  margin: 0;
  white-space: nowrap;
  line-height: var(--control-h);          /* 레이블도 40px 기준 */
}

/* ~ 표시 */
.mid {
  font-size: 18px;
  font-weight: 800;
  color: var(--muted);
  margin: 0 2px;
  height: var(--control-h);
  display: inline-flex;
  align-items: center;
}

/* =========================
   Form Controls
========================= */

/* input/select 공통 */
input[type="text"],
select,
.modal-content input,
.modal-content textarea {
  height: var(--control-h);
  font-size: var(--control-font);

  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 0 12px;

  outline: none;
  background: #fff;
  color: var(--text);

  margin-bottom: 0 !important;            /* 인라인 margin-bottom:10px 무시 */
}

/* placeholder */
input[type="text"]::placeholder { color: #9ca3af; }

/* focus */
input[type="text"]:focus,
select:focus,
.modal-content input:focus,
.modal-content textarea:focus {
  border-color: #c7d2fe;
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.15);
}

/* select 화살표 커스텀 */
select {
  appearance: none;
  display: inline-block;                  /* inline-flex 충돌 방지 */
  padding-right: 34px;

  background-image:
    linear-gradient(45deg, transparent 50%, #6b7280 50%),
    linear-gradient(135deg, #6b7280 50%, transparent 50%);
  background-position:
    calc(100% - 18px) 16px,
    calc(100% - 12px) 16px;
  background-size: 6px 6px;
  background-repeat: no-repeat;
}

/* 컨트롤 폭 */
#category { min-width: 140px; }
.daySet { width: 200px; text-align: left; }  /* 날짜 input */
#startDate, #endDate { width: 200px; }       /* id로도 보강 */

/* =========================
   Buttons
========================= */
.select-button,
.insert-button,
.delete-button,
.modal-content button {
  height: var(--control-h);
  border-radius: 10px;
  border: 1px solid var(--border);
  background: #fff;
  color: var(--text);
  cursor: pointer;

  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  padding: 0 14px;
  font-size: 14px;
  font-weight: 800;

  transition: transform 0.06s ease, background-color 0.2s ease, border-color 0.2s ease;

  margin: 0 !important;                   /* margin-right:94% 같은 값 무시 */
}

/* hover/active */
.select-button:hover,
.insert-button:hover,
.delete-button:hover,
.modal-content button:hover {
  background: #f3f4f6;
  border-color: #d1d5db;
}

.select-button:active,
.insert-button:active,
.delete-button:active,
.modal-content button:active {
  transform: translateY(1px);
}

/* 버튼 이미지 */
.button-image {
  width: 16px;
  height: 16px;
  display: inline-block;
}

/* 삭제 버튼 톤(혹시 쓸 때 대비) */
.delete-button {
  border-color: #ef4444;
  color: #b91c1c;
}
.delete-button:hover {
  background: rgba(239, 68, 68, 0.08);
  border-color: #ef4444;
}

/* =========================
   View (아래 테이블 영역)
========================= */
.view {
    display: flex;
    gap: 14px;
    width: 100%;
    margin-top: 14px;
    height: calc(100vh - 160px);
    min-height: 520px;
    overflow: hidden;
}

/* 카드 래핑 */
#invoiceTable,
#dataTable {
  width: 100%;
  min-width: 0;

  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);

  padding: 12px;
}
#invoiceTable{
  flex:0 0 630px;
  min-width:320px;
}

#dataTable{
  flex:1 1 auto;
}
/* span 밀어내던 거 최소화(필요하면 값 조절) */
#dataTable span { margin-left: 8% !important; }

/* =========================
   Modal
========================= */
.modal {
  display: none;
  position: fixed;
  inset: 0;

  background-color: rgba(0, 0, 0, 0.45);
  transition: opacity 0.2s ease-in-out;
  padding: 16px;
  z-index: 9999;
}

.modal-content {
  background: white;
  width: min(520px, 100%);
  max-width: 520px;

  max-height: 80vh;
  overflow-y: auto;

  margin: 8vh auto 0;
  padding: 18px 18px;

  border-radius: var(--radius);
  box-shadow: var(--shadow);

  position: relative;

  transform: translateY(10px) scale(0.98);
  opacity: 0;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.modal.show { display: block; opacity: 1; }
.modal.show .modal-content { transform: translateY(0) scale(1); opacity: 1; }

.close {
  background: transparent;
  border: none;

  position: absolute;
  right: 12px;
  top: 10px;

  width: 36px;
  height: 36px;
  border-radius: 10px;

  font-size: 22px;
  font-weight: 900;
  color: var(--muted);
  cursor: pointer;

  display: grid;
  place-items: center;
}
.close:hover { background: #f3f4f6; color: var(--text); }

.modal-content form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.modal-content label {
  font-weight: 800;
  margin: 10px 0 4px;
}

.modal-content textarea {
  height: auto;
  min-height: 110px;
  resize: vertical;
  padding: 10px 12px;
}

/* =========================
   Row highlight
========================= */
.row_select { background-color: #fff7ed !important; }

/* =========================
   Utility
========================= */
.form-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

/* =========================
   Responsive
========================= */
@media (max-width: 1100px) {
  .view { grid-template-columns: 1fr; }
}

@media (max-width: 720px) {
  .main {
    width: calc(100% - 20px);
    margin: 16px auto;
  }

  .tab { padding: 12px 12px; }

  .button-container {
    justify-content: flex-start;
  }

  .box1 { width: 100%; }

  /* 모바일에서는 날짜/유형을 줄바꿈해도 보기 좋게 */
  #category,
  #startDate,
  #endDate,
  .daySet {
    width: 100% !important;
  }

  .select-button,
  .insert-button,
  .delete-button {
    width: 100%;
    justify-content: center;
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
    </style>
</head>

<body>

    <main class="main">
        <div class="tab">
                    
                
            <div class="button-container">
                
               <div class="box1">
               	
				<label class="category" style="font-size: 18px; margin-right: 0;">유형: </label>
				<select id="category" name="category" style="font-size: 16px;">
				    <option value="">전체</option> 
				    <option value="Y">출하 완료</option>
				    <option value="N">출하 취소</option>
				    <option value="R">미처리</option>
				</select>
				
	           <label class="daylabel">일자 :</label>
				<input type="text" autocomplete="off" class="daySet" id="startDate" style="font-size: 16px; margin-bottom:10px;" placeholder="시작 날짜 선택">
				
				 <span class="mid"  style="font-size: 20px; font-weight: bold; margin-botomm:10px;"> ~ </span>
	 			<input type="text"autocomplete="off" class="daySet" id="endDate" style="font-size: 16px; margin-bottom:10px;" placeholder="종료 날짜 선택"> 
	</div>

	           
		
                <button class="select-button">
                    <img src="/yulchon/css/image/search-icon.png" alt="select" class="button-image">조회
                </button>
               	<div class="legend-container">
				    <div class="legend-item">
				        <span class="dot scan-complete"></span>
				        <span class="legend-text">출하 완료</span>
				    </div>
				    <div class="legend-item">
				        <span class="dot scan-cancel"></span>
				        <span class="legend-text">출하 취소</span>
				</div>
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
<script>
let now_page_code = "h03";
var dataTable;
var selectedRowData = null;
var invoiceTable;

// 출하이력 인보이스별 품목 조회
function initDataTable(){
dataTable = new Tabulator('#dataTable', {
  layout: "fitColumns",
  headerHozAlign: "center",
  ajaxConfig: { method: 'POST' },
  ajaxLoader: false,
  ajaxURL: "/yulchon/management/getCompleteInventoryList",
  ajaxParams: {},
  placeholder: "출하 완료 처리 된 인보이스를 클릭하면 데이터가 조회됩니다.",
  ajaxResponse: function(url, params, response) {
   // console.log("서버 응답 데이터 확인:", response);
    return response;
  },
  columns: [
	{ title: "인보이스", field: "invoice_name", hozAlign: "center", width: 120 },
	{ title: "No", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false },
    { title: "품목코드", field: "cd_item", hozAlign: "center", width: 120 },
    { title: "품목명", field: "nm_item", sorter: "string", width: 120, hozAlign: "center"},
    { title: "규격", field: "spec_item", sorter: "string", width: 100, hozAlign: "center"},
    { title: "단중", field: "kgm_weight", sorter: "string", width: 120, hozAlign: "center"},
    { title: "실길이", field: "lbl_real_length", sorter: "string", width: 120, hozAlign: "center"},
    { title: "Lot No.", field: "lbl_lot_no", width: 170, hozAlign: "center"},
    { title: "W/O No", field: "no_mfg_order_serial", width: 170, hozAlign: "center"},
    { title: "재고수량", field: "qty_inventory", sorter: "string", width: 100, hozAlign: "center"},
    { title: "재고중량", field: "wgt_inventory", sorter: "string", width: 120, hozAlign: "center"},
    { title: "창고코드", field: "cd_wh", sorter: "string", width: 120, hozAlign: "center"},
    { title: "창고명", field: "nm_wh", sorter: "string", width: 120, hozAlign: "center"},
    { title: "발행일자", field: "lbl_date", sorter: "string", width: 120, hozAlign: "center"},
    { title: "고객명", field: "nm_customer", sorter: "string", width: 200, hozAlign: "center"},
    { title: "고객PO", field: "po_customer", sorter: "string", width: 120, hozAlign: "center"},
    { title: "입하No", field: "no_receipt", sorter: "string", width: 120, hozAlign: "center"},
    { title: "비고", field: "remarks", sorter: "string", width: 120, hozAlign: "center"},
    { title: "위치", field: "nm_location", sorter: "string", width: 120, hozAlign: "center"}
  ]
});
}

//인보이스 조회
function initInvoiceTable(){
	invoiceTable = new Tabulator('#invoiceTable', {
	  layout: "fitColumns",
	  headerHozAlign: "center",
	  ajaxConfig: { method: 'POST' },
	  ajaxLoader: false,
	  ajaxURL: "/yulchon/management/getInvoiceList",
	  ajaxParams: {},
	  placeholder: "조회된 데이터가 없습니다.",
	  variableHeight: true,
	  ajaxResponse: function(url, params, response) {
	    return response;
	  },
	  rowFormatter: function(row) {
	      var data = row.getData();
	      var status = (data.invoice_is_shipped || "").toString().trim().toUpperCase();
	      if (status === "Y") {
	          row.getElement().style.backgroundColor = "#beddf8";
	      }else if(status === "N"){
	    	  row.getElement().style.backgroundColor = "#f8c1be";
		      }else if(status === "R"){
	    	  row.getElement().style.backgroundColor = "#e6e6e6";
		      }
	  },
	  columns: [
		  { title: "invoice_no", formatter: "invoice_no", hozAlign: "center", width: 80, visible:false},
		  { title: "NO", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false },
	    { title: "인보이스", field: "invoice_name", hozAlign: "center", width: 170 },
	    { title: "유형", field: "invoice_is_shipped", hozAlign: "center", width: 170,
	    	  formatter: function(cell) {
	    	      const value = (cell.getValue() || "").toString().trim();
	    	      const rowData = cell.getData(); // 행 전체 데이터
	    	      const moveName = rowData.move_invoice_name || ""; //옮긴 인보이스 이름
	    	      if (value === "Y") {
	    	          return "출하 완료";
	    	      }
	    	      if (value === "N") {
	    	          return "출하 취소";
	    	      }
	    	      if (value === "R") {
	    	    	  let displayText = "미처리";
	    	            if (moveName) {
	    	                displayText += "<br><span>( ➔ "+ moveName + ")</span>";
	    	            }
	    	            return displayText;
	    	      }
	    	      return "대기"; 
	    	  }
  	   },
  	 { title: "다른 인보이스로 옮겼는지", field: "invoice_is_moved", hozAlign: "center", width: 210, visible: false },
  	{ title: "옮긴 인보이스 이름", field: "move_invoice_name", hozAlign: "center", width: 210, visible: false },
  	 { title: "처리 날짜", field: "insert_date", hozAlign: "center", width: 200 }
	  ],
	  rowClick: function(e, row) {
	    //$('#invoiceTable .tabulator-row').removeClass('row_select');
	    //row.getElement().classList.add('row_select');
	    selectedRowData = row.getData();
	    dataTable.setData("/yulchon/management/getCompleteInventoryList", 
	    	    { invoice_no: selectedRowData.invoice_no,
    	    		invoice_is_shipped: selectedRowData.invoice_is_shipped.trim() });
	  },
	  rowDblClick: function(e, row) {
	    var d = row.getData();
	    selectedRowData = d;
	    initLeftTable();
	    initRightTable();
	    $('#addDeleteInvoiceProduct').show().addClass('show');
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

  // 조회 버튼 클릭 시
  $('.select-button').click(function() {
	  const st_day = $('#startDate').val(); 
	  const user_name = $('#user_name').val();  
	  console.log("날짜:", st_day + ", 이름:", user_name);

	    const data = {};
	    
	    if (st_day) {
	        data.st_day = st_day; 
	    }
	    if (user_name) {
	        data.user_name = user_name;
	    }
	    
	  dataTable.setData("/ezPublic/user/selectList", data);
  });

  //유형 선택 시
  $('#category').on('change', function() {
	    var selectedValue = $(this).val(); // Y, N 또는 ""
	    
	    // 테이블의 데이터를 다시 불러옴 (ajaxURL로 파라미터와 함께 요청)
	    invoiceTable.setData("/yulchon/management/getInvoiceList", { 
	        invoice_is_shipped: selectedValue 
	    });
	    dataTable.setData("/yulchon/management/getCompleteInventoryList", { 
	    });
	});
	
});
</script>


</body>
</html>
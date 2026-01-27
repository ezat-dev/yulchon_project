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
    
        .container {
            display: flex;
            justify-content: space-between;
            padding: 20px;
            margin-left: 1008px;
            margin-top: 200px;
        }
        .view {
            display: flex;
            justify-content: center;
            margin-top: 1%;
            gap: 3%;
			overflow-x: auto; /* 내용이 넘치면 가로 스크롤 생성 */
		    width: 113%;      /* 부모 너비 지정 */
        }
        .tab {
            width: 100%;
            margin-bottom: 37px;
            margin-top: 5px;
            height: 45px;
            border-radius: 6px 6px 0px 0px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .modal {
            display: none;
            position: fixed;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            transition: opacity 0.3s ease-in-out;
        }
	    .modal-content {
	        background: white;
	        width: 24%;
	        max-width: 500px;
	        height: 32vh; 
	        overflow-y: auto; 
	        margin: 8% auto 0;
	        padding: 20px;
	        border-radius: 10px;
	        position: relative;
	        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
	        transform: scale(0.8);
	        transition: transform 0.3s ease-in-out, opacity 0.3s ease-in-out;
	        opacity: 0;
	    }
        .modal.show {
            display: block;
            opacity: 1;
        }
        .modal.show .modal-content {
            transform: scale(1);
            opacity: 1;
        }
        .close {
            background-color:white;
            position: absolute;
            right: 15px;
            top: 10px;
            font-size: 24px;
            font-weight: bold;
            cursor: pointer;
        }
        .modal-content form {
            display: flex;
            flex-direction: column;
        }
        .modal-content label {
            font-weight: bold;
            margin: 10px 0 5px;
        }
        .modal-content input, .modal-content textarea {
            width: 97%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .modal-content button {
            background-color: #d3d3d3;
            color: black;
            padding: 10px;
            border: none;
            border-radius: 5px;
            margin-top: 10px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .modal-content button:hover {
            background-color: #a9a9a9;
        }
        .button-container {
    		display: flex;
		    gap: 10px;
		    margin-left: 2%;
		    margin-top: 40px;
		}
		.box1 {
		    display: flex;
		    justify-content: right;
		    align-items: center;
		    width: 895px;
		    margin-right: 20px;
		    margin-top:4px;
		}
        .dayselect {
            width: 20%;
            text-align: center;
            font-size: 15px;
        }
        .daySet {
        	width: 20%;
      		text-align: center;
            height: 16px;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
        }
        .daylabel {
            margin-right: 10px;
            margin-bottom: 13px;
            font-size: 18px;
            margin-left: 20px;
        }
        button-container.button{
        height: 16px;
        }
         .mid{
        margin-right: 9px;
	    font-size: 20px;
	    font-weight: bold;
	
	    height: 42px;
	    margin-left: 9px;
        }
        .row_select {
	    background-color: #ffeeba !important;
	    }
	    
	    .form-row {
  display: flex;
  align-items: center;
  gap: 16px;           /* 레이블–인풋 간격 */
  flex-wrap: wrap;     /* 화면 좁아지면 줄 바꿈 */
  margin-bottom: 12px; /* 각 행 간 간격 */
}
	.delete-button {
	    height: 40px; /* tab보다 조금 작게 설정 */
	    padding: 0 11px; /* 좌우 패딩 */
	    border: 1px solid rgb(53, 53, 53);
	    border-radius: 4px; /* 모서리 둥글게 */
	    background-color: #ffffff; /* 배경색 */
	    cursor: pointer; /* 포인터 커서 */
	    display: flex; /* 내부 요소를 플렉스 박스로 설정 */
	    align-items: center; /* 버튼 안에서 세로 가운데 정렬 */
	}
#invoiceTable {
    flex: 0 0 380px; /* 고정 너비 유지하되 유연함 부여 */
    min-width: 300px;
}
#dataTable {
height: 85vh !important;
}
#insert-invoice-button{
width: 100px;
}
#delete-invoice-button{
width: 100px;
}
#dataTable span{
margin-left: 15%;
}
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
	           <label class="daylabel">발행일자 :</label>
				<input type="text" autocomplete="off" class="yearSet" id="startDate" style="font-size:16px; height:30px; width:220px; margin-bottom:10px; text-align:center; border-radius:6px; border:1px solid #ccc;" placeholder="시작 날짜 선택">
				
				 <span class="mid"  style="font-size: 20px; font-weight: bold; margin-botomm:10px;"> ~ </span>
	 	<input type="text"autocomplete="off" class="daySet" id="endDate" style="font-size: 16px; margin-bottom:10px;" placeholder="종료 날짜 선택"> 
	</div>

	           
		
                <button class="select-button">
                    <img src="/yulchon/css/image/search-icon.png" alt="select" class="button-image">조회
                </button>
                
                <button class="insert-button" id="delete-product-button">
                    <img src="/yulchon/css/image/delete-icon.png" alt="insert" class="button-image">품목 삭제
                </button>
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
  height: "705px",
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
  columns: [
	    { 
	        title: "선택", 
	        field: "selected", 
	        hozAlign: "center", 
	        width: 80,
	        formatter: "rowSelection", 
	        titleFormatter: "rowSelection", 
	        headerSort: false,
	        cellClick: function(e, cell) {
	            cell.getRow().toggleSelect();
	        }
	    },
		{ title: "No", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false},
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
  },
  //체크박스 선택/해제 시
  rowSelectionChanged: function(data, rows){
	  selectedProductRows = data;
	  console.log("선택된 삭제할 데이터:", selectedProductRows, "개수:", selectedProductRows.length);
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
		    { 
		        title: "선택", 
		        field: "selected", 
		        hozAlign: "center", 
		        width: 80,
		        formatter: "rowSelection", 
		        titleFormatter: "rowSelection", 
		        headerSort: false,
		        cellClick: function(e, cell) {
		            cell.getRow().toggleSelect();
		        }
		    },
		  { title: "invoice_no", formatter: "invoice_no", hozAlign: "center", width: 80, visible:false},
		  { title: "NO", formatter: "rownum", hozAlign: "center", width: 60, headerSort: false },
	    { title: "인보이스", field: "invoice_name", hozAlign: "center", width: 240 }
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
	initDataTable();
	initInvoiceTable();

  // 조회 버튼 클릭 시
  $('#insert-invoice-button').click(function() {
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

  // 품목삭제 버튼 클릭 시
  $('#delete-product-button').click(function() {
	  //console.log("추가할 데이터 개수: ", selectedRightRows.length);
	  const invoice_no = selectedProductRows[0].invoice_no;
	  //console.log("추가할 인보이스 pk: ", invoice_no);
	  
	  if (!selectedProductRows || selectedProductRows.length === 0) {
		    alert("선택된 품목이 없습니다.");
		    return;
		  }

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
			          console.log("추가 실패");
		              }
		        },
		      error: function() {
		        alert('저장 중 오류가 발생했습니다.');
		      }
		    });
  });

  // 삭제 버튼 클릭 시
  $('.delete-button').click(function() {
    if (!selectedRowData) {
      alert('삭제할 행을 먼저 클릭해 주세요.');
      return;
    }
    if (!selectedRowData) {
    	  alert('삭제할 행을 먼저 클릭해 주세요.');
    	  return;
    	}
    	if (!confirm('선택된 항목을 정말 삭제하시겠습니까?')) return;

    	const deleteData = { user_code: selectedRowData.user_code };
    	console.log("삭제 요청 데이터:", deleteData); // 추가된 로그

    	$.ajax({
    	  url: "/ezPublic/user/deleteUser",
    	  type: "POST",
    	  contentType: "application/json",
    	  data: JSON.stringify(deleteData),
    	  success: function(res) {
    	    alert('삭제되었습니다.');
    	
    	    dataTable.setData("/ezPublic/user/selectList", {});
    	    selectedRowData = null;
    	  },
    	  error: function() {
    	    alert('삭제 중 오류가 발생했습니다.');
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
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
		    margin-top: 40px;
		    margin-left: 1200px;
		}
		.box1 {
		    /*display: flex;*/
		    justify-content: right;
		    align-items: center;
		    width: 580px;
		    margin-left: 441px;
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
.button-container select{
width: 105px;
}
.button-container label{
width: 40px;
margin-top: 6px;
}
    </style>
</head>

<body>

    <main class="main">
        <div class="tab">
                    
                
            <div class="button-container">
                

	           
		
                <button class="select-button">
                    <img src="/yulchon/css/image/search-icon.png" alt="select" class="button-image">조회
                </button>
                 <button class="insert-button">
                    <img src="/yulchon/css/image/insert-icon.png" alt="insert" class="button-image">업체 추가
                </button>
<!--                 <button class="delete-button">
				    <img src="/yulchon/css/image/delete-icon.png" alt="delete" class="button-image"> 삭제
				</button> -->
                
            <!--     
                <button class="excel-button">
                    <img src="/geomet/css/tabBar/excel-icon.png" alt="excel" class="button-image">엑셀
                </button> -->
                
            </div>
        </div>

        <div class="view">
            <div id="dataTable"></div>
        </div>
    </main>
<script>
let now_page_code = "h03";
var dataTable;
var selectedRowData = null;

// 재고현황 조회
function initDataTable(){
dataTable = new Tabulator('#dataTable', {
  height: "705px",
  layout: "fitColumns",
  headerHozAlign: "center",
  ajaxConfig: { method: 'POST' },
  ajaxLoader: false,
  ajaxURL: "/yulchon/management/getInventoryList",
  ajaxParams: {},
  placeholder: "조회된 데이터가 없습니다.",
  ajaxResponse: function(url, params, response) {
   // console.log("서버 응답 데이터 확인:", response);
    return response;
  },
  columns: [
    { title: "업체명", field: "nm_customer", sorter: "string", width: 200, hozAlign: "center" },
    { title: "비고", field: "txt_remark", sorter: "string", width: 120, hozAlign: "center" },
    { title: "최종수정일", field: "update_date", sorter: "string", width: 120, hozAlign: "center" },
    { title: "최정수정자", field: "update_user", sorter: "string", width: 120, hozAlign: "center" }
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

$(function() {
	initDataTable();

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


});
</script>


</body>
</html>
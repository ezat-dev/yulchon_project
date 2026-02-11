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
            width: 105%;
            margin-bottom: 37px;
            margin-top: 5px;
            height: 45px;
            border-radius: 6px 6px 0px 0px;
            display: flex;
            align-items: center;
            justify-content: space-between;
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
        select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .button-container {
    		display: flex;
		    gap: 10px;
		    margin-top: 40px;
		    margin-left: 1320px;
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
/* 모달 배경 */
.modal {
    position: fixed; z-index: 1000; left: 0; top: 0;
    width: 100%; height: 100%; background-color: rgba(0,0,0,0.5);
}
/* 모달 박스 */
.modal-content {
    background-color: #fefefe; margin: 10% auto; padding: 20px;
    border: 1px solid #888; width: 400px; border-radius: 8px;
}
.modal-header { border-bottom: 1px solid #ddd; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }
.modal-body { padding: 20px 0; }
.input-group { margin-bottom: 15px; }
.input-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.input-group input { width: 100%; padding: 8px; box-sizing: border-box; }
.modal-footer { text-align: right; border-top: 1px solid #ddd; padding-top: 10px; }
.save-btn { background: #1890ff; color: white; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer; }
.cancel-btn { background: #f0f0f0; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer; margin-left: 5px; }
.close-modal { cursor: pointer; font-size: 24px; }
.select-button {
    height: 40px;
    padding: 0 11px;
    border: 1px solid rgb(53, 53, 53);
    border-radius: 10px;
    background-color: #ffffff;
    cursor: pointer;
    display: flex;
    align-items: center;
}
.insert-button {
    height: 40px;
    padding: 0 11px;
    border: 1px solid rgb(53, 53, 53);
    border-radius: 10px;
    background-color: #ffffff;
    cursor: pointer;
    display: flex;
    align-items: center;
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
                    <img src="/yulchon/css/image/insert-icon.png" alt="insert" class="button-image">고객사 추가
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
        
        <div id="insertModal" class="modal" style="display:none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>신규 업체 등록</h3>
            <span class="close-modal">&times;</span>
        </div>
        <div class="modal-body">
            <div class="input-group">
                <label for="new_customer_name">고객사 이름:</label>
                <input type="text" id="new_customer_name" placeholder="고객사명을 입력하세요">
            </div>
            <div class="input-group">
                <label for="modal_file_input">양식 파일:</label>
                <input type="file" id="modal_file_input" accept=".xlsx, .xls">
            </div>
        </div>
        <div class="modal-footer">
            <button id="btnSaveCustomer" class="save-btn">저장</button>
            <button id="btnCloseModal" class="cancel-btn">취소</button>
        </div>
    </div>
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
  ajaxURL: "/yulchon/management/getCustomerList",
  ajaxParams: {},
  placeholder: "조회된 데이터가 없습니다.",
  ajaxResponse: function(url, params, response) {
   // console.log("서버 응답 데이터 확인:", response);
    return response;
  },
  columns: [
	  { title: "No", formatter: "rownum", sorter: false, width: 80, hozAlign: "center" },
    { title: "고객명", field: "customer_name", sorter: "string", width: 370, hozAlign: "center", headerFilter: "input" },
    {
        title: "양식 파일명", 
        field: "customer_shippingmark_file_name", 
        hozAlign: "center",
        width: 300,
        formatter: function(cell) {
            const fileName = cell.getValue();
            if (fileName) {
            	return "<a href='javascript:void(0);' onclick='downloadFile(\"" + fileName + "\")' " +
                "style='color: #1890ff; text-decoration: underline;'>" + fileName + "</a>";
            } else {
                return "<span style='color: #ccc;'>양식 없음</span>";
            }
        }
    },
    {
        title: "관리", 
        width: 150, 
        hozAlign: "center", 
        headerSort: false,
        formatter: function(cell) {
            const fileName = cell.getData().customer_shippingmark_file_name;
            const upBtn = `<button class='up-btn' style='border-radius:4px; cursor:pointer; margin-left:5px;'>업로드</button>`;
            return upBtn;
        },
        cellClick: function(e, cell) {
            const data = cell.getData();
            console.log("데이터: ", data);
            if (e.target.classList.contains("up-btn")) {
                // 1. 숨겨진 파일 선택창 생성
                const fileInput = document.createElement("input");
                fileInput.type = "file";
                fileInput.accept = ".xlsx"; // 엑셀만 허용

                fileInput.onchange = function() {
                    const file = fileInput.files[0];
                    if (!file) return;

                    // 2. FormData에 데이터 담기
                    const formData = new FormData();
                    formData.append("file", file);
                    formData.append("customer_id", data.customer_id); 
                    formData.append("old_file_name", data.customer_shippingmark_file_name); // 삭제할 기존 파일명

                    // 3. 서버로 전송
                    $.ajax({
                        url: "/yulchon/management/deleteAndUploadShippingMark",
                        type: "POST",
                        data: formData,
                        processData: false, // 파일 전송 시 필수: 데이터를 쿼리 스트링으로 변환하지 않음
                        contentType: false, // 파일 전송 시 필수: 브라우저가 자동으로 boundary를 설정하게 함
                        success: function(isSuccess) {
                            // 컨트롤러에서 boolean을 리턴하므로 isSuccess는 true/false임
                            if(isSuccess) {
                                alert("양식이 교체되었습니다.");
                                dataTable.setData("/yulchon/management/getCustomerList", {});
                            } else {
                                alert("❌ 양식 교체에 실패했습니다.");
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error("에러 발생:", error);
                            alert("서버와 통신 중 오류가 발생했습니다.");
                        }
                    });
                };
                fileInput.click(); // 파일 탐색기 열기
            }
        }
    },
    { title: "최종수정일", field: "regtime", sorter: "string", width: 160, hozAlign: "center" },
    { title: "최정수정자", field: "update_user_id", sorter: "string", width: 160, hozAlign: "center" },
    { title: "비고", field: "remark", sorter: "string", width: 150, hozAlign: "center", editor: "input" }
  ],
  cellEdited: function(cell) {
      // cell : 수정된 셀 객체
      var rowData = cell.getRow().getData(); // 수정된 행 전체 데이터
      var field = cell.getField();           // 수정된 필드명 (customer_product_code_number)
      var newValue = cell.getValue();        // 바뀐 값
      var customer_id = rowData.customer_id;
      
      console.log("수정할 필드: ", field);
      console.log("새로운 데이터: ", newValue);
      console.log("수정할 데이터: ", rowData);

      var updateData = {
    		  "targetField": field,
    	        "newValue": newValue,
    	        "customer_id": customer_id
    	      }

      $.ajax({
          url: "/yulchon/management/updateCustomerRemark",
          method: "POST",
          data: JSON.stringify(updateData),
          contentType: "application/json",
          success: function(res) {
			if(res === true || res === "true"){
				console.log("비고 업데이트 완료");
				}else{
					console.log("비고 업데이트 실패")
					}
  			},
		      error: function() {
		    	  console.log("비고 수정 중 에러 발생");
		      }
      });
      
  }
});
}

//파일 다운로드 요청
function downloadFile(fileName) {
  // 서버로 파일명을 보내 다운로드 요청
  location.href = "/yulchon/management/downloadShippingMark?fileName=" + encodeURIComponent(fileName);
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
	    
	  dataTable.setData("/yulchon/management/getCustomerList", data);
  });

  //고객사 추가 버튼 클릭시
  $(".insert-button").click(function() {
      $("#new_customer_id, #new_customer_name, #modal_file_input").val(""); // 초기화
      $("#insertModal").show();
  });

  // 모달 닫기
  $(".close-modal, #btnCloseModal").click(function() {
      $("#insertModal").hide();
  });

  //저장 버튼 클릭시
  $("#btnSaveCustomer").click(function() {
      const name = $("#new_customer_name").val();
      const file = $("#modal_file_input")[0].files[0];

      if(!name) {
          alert("고객사를 입력해주세요.");
          return;
      }

      const formData = new FormData();
      formData.append("customer_name", name);
      if(file) formData.append("file", file);

      $.ajax({
          url: "/yulchon/management/insertCustomer", 
          type: "POST",
          data: formData,
          processData: false,
          contentType: false,
          success: function(result) {
              if(result) {
                  alert("성공적으로 등록되었습니다.");
                  $("#insertModal").hide();
                  dataTable.setData("/yulchon/management/getCustomerList", {});
              } else {
                  alert("등록 실패");
              }
          }
      });
  });

});
</script>


</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>권한 설정</title>
    <%-- <%@ include file="../include/sideBar.jsp" %> --%>
    <link rel="stylesheet" href="/ezPublic/css/tabBar/tabBar.css">
    <%@include file="../include/pluginpage.jsp" %>
    <link rel="stylesheet" href="/ezPublic/css/management/userinsert2.css">
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
        }
        .tab {
            width: 95%;
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
	        height: 80vh; 
	        overflow-y: auto; 
	        margin: 6% auto 0;
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
		    margin-left: auto;
		    margin-right: 10px;
		    margin-top: 40px;
		}
		.box1 {
		    display: flex;
		    justify-content: right;
		    align-items: center;
		    width: 1000px;
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

    </style>
</head>

<body>

    <main class="main">
        <div class="tab">
        
            <div class="button-container">
            
               <div class="box1">
	           <p class="tabP" style="font-size: 20px; margin-left: 40px; color: white; font-weight: 800;"></p>
	           <label class="daylabel">입사 연도 :</label>
				<input type="text" autocomplete="off" class="yearSet" id="startDate" style="font-size:16px; height:30px; width:220px; margin-bottom:10px; text-align:center; border-radius:6px; border:1px solid #ccc;" placeholder="시작 날짜 선택">
				
				<!-- <span class="mid"  style="font-size: 20px; font-weight: bold; margin-botomm:10px;"> ~ </span> -->
	
			<!-- 	<input type="text"autocomplete="off" class="daySet" id="endDate" style="font-size: 16px; margin-bottom:10px;" placeholder="종료 날짜 선택"> 
 -->
	
			  <label class="daylabel">성명 :</label>
			 <input type="text" id="user_name" style="font-size:16px; height:30px; width:220px; margin-bottom:10px; text-align:center; border-radius:6px; border:1px solid #ccc;" placeholder="이름 입력">

	</div>

	           
		
                <button class="select-button">
                    <img src="/ezPublic/css/image/search-icon.png" alt="select" class="button-image">조회
                </button>
                <button class="insert-button">
                    <img src="/ezPublic/css/image/insert-icon.png" alt="insert" class="button-image">추가
                </button>
                 <button class="delete-button">
				    <img src="/ezPublic/css/image/delete-icon.png" alt="delete" class="button-image"> 삭제
				</button>
                
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
	
	   <div id="modalContainer" class="modal">
	    <div class="modal-content">
	<!--         <span class="close">&times;</span> -->
	        <h2>사용자 등록</h2>
	        <form id="corrForm"autocomplete="off">
	          
		 	<input type="text" name="user_code" style="display:none;">
 
	
	            <label>ID</label>
				<input type="text" name="user_id">
	
	           <label>PassWord</label>
	           <input type="text" name="user_pw">

	
	            <label>성명</label>
	            <input type="text" name="user_name">
	            
	            <label>입사일</label>
	            <input type="text" name="st_day" class="daySet" style="text-align: left;">

	            
	              <label>전화번호</label>
	            <input type="text" name="user_phone">
	            
	            
	            
	        <select name="user_level" style="display: none;">
			  <option value="3">기본</option>
			</select>
			
			<select name="user_yn" style="display: none;">
			  <option value="Y">기본</option>
			</select>
			           	            	
	            <label>부서</label>
	             <input type="text" name="user_busu">
	           
	            <label>직책</label>
	             <input type="text" name="user_jick">
	            
	            <button type="submit" id="saveCorrStatus">저장</button>
	            <button type="submit" id="updateCorrStatus" style="display: none;">수정</button>
	            <button type="button" id="closeModal">닫기</button>
	        </form>
	    </div>
	</div>


<script>
let now_page_code = "h03";
var dataTable;
var selectedRowData = null;
let title = "만든 제목";

$(function() {
  // Initialize the dataTable
  dataTable = new Tabulator('#dataTable', {
    height: "705px",
    layout: "fitColumns",
    headerHozAlign: "center",
    ajaxConfig: { method: 'POST' },
    ajaxLoader: false,
    ajaxURL: "/ezPublic/user/selectList",
    ajaxParams: {},
    placeholder: "조회된 데이터가 없습니다.",
    ajaxResponse: function(url, params, response) {
     // console.log("서버 응답 데이터 확인:", response);
      return response;
    },
    columns: [
      { title: "NO", formatter: "rownum", hozAlign: "center", width: 120 },
      { title: "user_code", field: "user_code", sorter: "string", width: 240, hozAlign: "center", visible: false },

      { title: "ID", field: "user_id", sorter: "string", width: 100, hozAlign: "center" },
      { title: "비밀번호", field: "user_pw", sorter: "string", width: 240, hozAlign: "center", visible: false },
      { title: "성명", field: "user_name", sorter: "string", width: 240, hozAlign: "center" },
      { title: "입사일", field: "st_day", width: 170, hozAlign: "center" },
      { title: "전화번호", field: "user_phone", width: 200, hozAlign: "center" },
      { title: "등급", field: "user_level", sorter: "string", width: 240, hozAlign: "center", visible: false },

      { title: "부서", field: "user_busu", sorter: "string", width: 120, hozAlign: "center" },
      { title: "직책", field: "user_jick", sorter: "string", width: 120, hozAlign: "center" }
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

      // 저장 숨기고 수정 보이게
      $('#saveCorrStatus').hide();
      $('#updateCorrStatus').show();
      
      $('#modalContainer').show().addClass('show');
    }
  });

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

  // 삽입 버튼 클릭 시
  $('.insert-button').click(function() {
      // 수정 숨기고 저장 보이게
      $('#saveCorrStatus').show();
      $('#updateCorrStatus').hide();
    selectedRowData = null;
    $('#corrForm')[0].reset();
    $('#modalContainer').show().addClass('show');
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

  // 모달 닫기
  $('.close, #closeModal').click(function() {
    $('#modalContainer').removeClass('show').hide();
  });

  // 저장 버튼 클릭 시
  $('#saveCorrStatus').click(function(event) {
    event.preventDefault();
    //var formData = new FormData($('#corrForm')[0]);
    var formData = $('#corrForm').serialize();

    // 사용자 저장 시 user_code 삭제
    formData = formData.replace(/&user_code=[^&]*/g, ''); 
    formData = formData.replace(/^user_code=[^&]*&/g, ''); 
    formData = formData.replace(/^user_code=[^&]*/g, '');
    console.log("formData: ", formData);
    //formData.
/*     if (selectedRowData && selectedRowData.user_code) {
      formData.append('user_code', selectedRowData.user_code);  // 수정 시 user_code 추가
    } */
/*     for (var pair of formData.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      } */
        

    $.ajax({
      url: "/ezPublic/user/insert",
      type: "POST",
      data: formData,
      //processData: false,
      //contentType: false,
      success: function(result) {
        console.log(result);
        if (result.status === "OK") {
            alert(result.message); // "사용자 정보가 성공적으로 저장되었습니다."
            $('#modalContainer').hide();
            dataTable.setData("/ezPublic/user/selectList", {});
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

  //수정 버튼 클릭 시
  $('#updateCorrStatus').click(function(event) {
    event.preventDefault();
    const data = {};
    const formDataArray = $('#corrForm').serializeArray();
    $.each(formDataArray, function() {
        data[this.name] = this.value;
    });

    // 결과 확인 (콘솔 출력)
    console.log("폼 데이터 추출 결과:", data);
/*     if (selectedRowData && selectedRowData.user_code) {
    	$('input[name="user_code"]').val(selectedRowData.user_code);  // 수정 시 user_code 추가
    }
    var formData = $('#corrForm').serialize(); */
        
    $.ajax({
      url: "/ezPublic/user/updateUser",
      type: "POST",
      contentType: 'application/json',
      data: JSON.stringify(data),
      //processData: false,
      //contentType: false,
      success: function(result) {
          if(result == true){
        alert("수정되었습니다!");
        $('#modalContainer').hide();
    
        dataTable.setData("/ezPublic/user/selectList", {});
          }else{
			alert('아이디를 확인해주세요');
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
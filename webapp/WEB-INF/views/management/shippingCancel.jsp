<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="description" content="태경열처리 관리 시스템">
  <meta name="author" content="태경열처리">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="/yulchon/css/login/style.css">
  <script src="https://cdn.jsdelivr.net/npm/ionicons@latest/dist/ionicons/ionicons.js"></script>
  
<%@include file="../include/pluginpage.jsp" %>  
  <title>율촌</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Malgun Gothic', sans-serif;
      background-color: #f5f5f5;
    }

    .scan-page {
      padding: 20px;
      max-width: 1400px;
      margin: 0 auto;
    }

    .scan-title {
      font-size: 28px;
      font-weight: bold;
      margin-bottom: 30px;
      color: #333;
    }

    .info-table-container {
      background: white;
      border-radius: 8px;
      padding: 30px;
      margin-bottom: 30px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .info-table {
      width: 100%;
      border-collapse: collapse;
    }

    .info-table th,
    .info-table td {
      padding: 15px;
      border: 1px solid #ddd;
      text-align: left;
    }

    .info-table th {
      background-color: #4a90e2;
      color: white;
      font-weight: bold;
      width: 150px;
    }

    .info-table td {
      background-color: #f9f9f9;
    }

    .info-table tr:hover td {
      background-color: #f0f0f0;
    }

    .button-container {
      display: flex;
      justify-content: center;
      gap: 15px;
      margin-top: 20px;
    }

    .btn {
      padding: 12px 40px;
      font-size: 16px;
      font-weight: bold;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s;
    }

    .btn-print {
      background-color: #4a90e2;
      color: white;
    }

    .btn-print:hover {
      background-color: #357abd;
    }

    .btn-back {
      background-color: #6c757d;
      color: white;
    }

    .btn-back:hover {
      background-color: #5a6268;
    }

    /* 태블릿 */
    @media (max-width: 1024px) {
      .scan-title {
        font-size: 24px;
      }

      .info-table th,
      .info-table td {
        padding: 12px;
      }

      .info-table th {
        width: 130px;
      }
    }

    /* 모바일 */
    @media (max-width: 768px) {
      .scan-page {
        padding: 15px;
      }

      .scan-title {
        font-size: 22px;
        margin-bottom: 20px;
      }

      .info-table-container {
        padding: 15px;
      }

      .info-table {
        display: block;
      }

      .info-table tbody {
        display: block;
      }

      .info-table tr {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
        border: 1px solid #ddd;
        border-radius: 6px;
        overflow: hidden;
      }

      .info-table th,
      .info-table td {
        display: block;
        width: 100%;
        border: none;
        padding: 12px 15px;
      }

      .info-table th {
        width: 100%;
        text-align: left;
      }

      .info-table td {
        border-top: 1px solid #ddd;
      }

      .btn {
        padding: 15px 20px;
        flex: 1;
      }
    }

    /* 작은 모바일 */
    @media (max-width: 480px) {
      .scan-title {
        font-size: 20px;
      }

      .info-table th,
      .info-table td {
        padding: 10px 12px;
        font-size: 14px;
      }
    }
  </style>
</head>
<body>
  <div class="scan-page">
    <div class="scan-title">쉬핑마크 출력</div>
    
    <div class="info-table-container">
      <table class="info-table">
        <tbody>
          <tr>
            <th>품목코드</th>
            <td id="cd_item"></td>
          </tr>
          <tr>
            <th>Lot No.</th>
            <td id="lbl_lot_no"></td>
          </tr>
          <tr>
            <th>W/O No</th>
            <td id="no_mfg_order_serial"></td>
          </tr>
          <tr>
            <th>재고수량</th>
            <td id="qty_inventory"></td>
          </tr>
          <tr>
            <th>발행일자</th>
            <td id="lbl_date"></td>
          </tr>
          <tr>
            <th>고객명</th>
            <td id="nm_customer"></td>
          </tr>
          <tr style="display: none">
			<th>인보이스</th>
			<td id="invoice_no"></td>
		  </tr>
        </tbody>
      </table>
    </div>

    <div class="button-container">
      <button class="btn btn-print" onclick="shippingCalcel()">
        출하 취소
      </button>
      <button class="btn btn-back" onclick="handleBack()">
        뒤로가기
      </button>
    </div>
  </div>

  <script>
  $(document).ready(function() {
	  	$('#cd_item').text("${data.cd_item}");
	    $('#lbl_lot_no').text("${data.lbl_lot_no}");
	    $('#no_mfg_order_serial').text("${data.no_mfg_order_serial}");
	    $('#qty_inventory').text("${data.qty_inventory}");
	    $('#lbl_date').text("${data.lbl_date}");
	    $('#nm_customer').text("${data.nm_customer}");
	    $('#invoice_no').text("${data.invoice_no}");
});

//일단 출하목록에서 삭제
  function shippingCalcel() {
    const lot_no = $('#lbl_lot_no').text().trim();
    const invoice_no = $('#invoice_no').text().trim();
    console.log("lot_no: ", lot_no, "invoice_no: ", invoice_no);
    
    if(!lot_no){
        alert("Lot No가 없습니다. 다시 스캔해주세요.");
        return;
        }

	$.ajax({
	  url: "/yulchon/management/mobile/deleteShippingList",
	  type: "POST",
	  contentType: "application/json",
	  data: JSON.stringify({lbl_lot_no: lot_no, invoice_no: invoice_no}),
	  success: function(result) {
	  	  if(result === true || result === "true"){
				alert("출하목록 삭제 완료");
				history.back();
	  	  	  }else{
				alert("출하목록 삭제 실패");
	  	  	  	  }
	  },
	  error: function() {
	    alert('출하목록 삭제 중 오류가 발생했습니다.');
	  }
	});
  }
  function handleBack() {
    history.back();
  }
  </script>
</body>
</html>
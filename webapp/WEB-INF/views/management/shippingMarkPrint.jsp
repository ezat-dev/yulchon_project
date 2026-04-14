<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="description" content="태경열처리 관리 시스템">
<meta name="author" content="태경열처리">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/yulchon/css/login/style.css">
<script
	src="https://cdn.jsdelivr.net/npm/ionicons@latest/dist/ionicons/ionicons.js"></script>
<script src="https://unpkg.com/@zxing/library@latest"></script>
<script src="https://cdn.jsdelivr.net/npm/tesseract.js@5/dist/tesseract.min.js"></script>
<%@include file="../include/pluginpage.jsp"%>
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

.info-table th, .info-table td {
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
@media ( max-width : 1024px) {
	.scan-title {
		font-size: 24px;
	}
	.info-table th, .info-table td {
		padding: 12px;
	}
	.info-table th {
		width: 130px;
	}
}

/* 모바일 */
@media ( max-width : 768px) {
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
	.info-table th, .info-table td {
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
@media ( max-width : 480px) {
	.scan-title {
		font-size: 20px;
	}
	.info-table th, .info-table td {
		padding: 10px 12px;
		font-size: 14px;
	}
}
@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
#loadingOverlay{
display: none; 
    position: fixed; 
    top: 0; 
    left: 0; 
    width: 100%; 
    height: 100%; 
    background: rgba(0,0,0,0.7); /* 배경을 좀 더 어둡게 해서 가독성 높임 */
    
    /* 중요: 버튼의 999999보다 더 높은 숫자를 부여 */
    z-index: 9999999 !important; 
    
    flex-direction: column; 
    align-items: center; 
    justify-content: center; 
    color: white;
}
</style>
</head>
<body>
	<div class="scan-page">
		<div class="scan-title">쉬핑마크 출력</div>

		<div class="info-table-container">
			<table class="info-table">
				<tbody>
<!-- 					<tr>
						<th>품목명</th>
						<td id="nm_item"></td>
					</tr> -->
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
			<button class="btn btn-print" onclick="handlePrint()">출력</button>
			<button class="btn btn-back" onclick="handleBack()">뒤로가기</button>
		</div>
	</div>
<div id="loadingOverlay" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5); z-index:999999; flex-direction:column; align-items:center; justify-content:center; color:white;">
    <div class="spinner" style="width:50px; height:50px; border:5px solid #f3f3f3; border-top:5px solid #3498db; border-radius:50%; animation:spin 1s linear infinite;"></div>
    <p style="margin-top:15px; font-weight:bold;">쉬핑마크 출력 준비 중입니다. 잠시만 기다려 주세요...</p>
</div>
	<script>
	let lotValue = "";
	let scannedText = null;
	let scannedFormat = null;
	let selectedInvoiceNo = "";
	let selectedInvoiceName = "";
	let invoice_no = "";
	let lot_no = "";
	let customer_product_code_number = "";
	let cd_item = "";
	let extra_invoice_no = "";
	let extra_packing_inspection = "";
	let extra_order_no = "";
	let extra_part_no = "";
	let extra_spec = "";
	let extra_bundle_no = "";
	let extra_weight = "";
	let no_mfg_order_serial = "";
	let qty_inventory = "";
	
	const showLoading = () => $('#loadingOverlay').css('display', 'flex');
	const hideLoading = () => $('#loadingOverlay').hide();

	//===== 설정 값 =====
	const ZXING_FORMATS = [
	    ZXing.BarcodeFormat.QR_CODE,
	    ZXing.BarcodeFormat.CODE_128,
	    ZXing.BarcodeFormat.CODE_39,
	    ZXing.BarcodeFormat.EAN_13,
	    ZXing.BarcodeFormat.ITF
	];

	// 가운데 촬영 전제에서 중앙 크롭 여러 단계 시도 구성
	const CROP_RATIOS = [1.0, 0.85, 0.72, 0.60];

	// 너무 큰 원본은 실패/느림 가능성이 올라가므로 적당히 축소 구성
	const MAX_LONG_EDGE = 1600;

	// 대비는 과하면 오히려 깨질 수 있어 1.15~1.35 범위 권장 구성
	const PREPROCESS_FILTER = "grayscale(1) contrast(1.25)";

	const formats = [
		  ZXing.BarcodeFormat.QR_CODE,
		  ZXing.BarcodeFormat.CODE_128,
		  ZXing.BarcodeFormat.CODE_39,
		  ZXing.BarcodeFormat.EAN_13,
		  ZXing.BarcodeFormat.ITF,
		];
	const hints = new Map();
	hints.set(ZXing.DecodeHintType.POSSIBLE_FORMATS, formats);
	hints.set(ZXing.DecodeHintType.TRY_HARDER, true);

	// timeBetweenScans는 단일 디코딩이면 큰 의미 없지만, 생성자 시그니처 맞추기 용도도 있음
	const codeReader = new ZXing.BrowserMultiFormatReader(hints, 200);
	
	$(document).ready(function() {
	    invoice_no = "${data.invoice_no}";
	    selectedInvoiceNo = "${selectedInvoiceNo}";
	    selectedInvoiceName = "${selectedInvoiceName}";
	    
	    lot_no = "${data.lbl_lot_no}";
	    customer_product_code_number = "${data.customer_product_code_number}";
	    cd_item = "${data.cd_item}";
	    no_mfg_order_serial = "${data.no_mfg_order_serial}";
	    qty_inventory = "${data.qty_inventory}";
	    customerName = "${data.nm_customer}";

	    $('#nm_item').text("${data.nm_item}");
	    $('#lbl_lot_no').text("${data.lbl_lot_no}");
	    $('#no_mfg_order_serial').text("${data.no_mfg_order_serial}");
	    $('#qty_inventory').text("${data.qty_inventory}");
	    $('#nm_customer').text("${data.nm_customer}");
	    $('#invoice_no').text(invoice_no);
	    
		updateButtonLabel();
		
	    //스캔한 품목 인보이스 없을 때 - w/o, 수량 같은거로 로트번호 바꿈
	    if (invoice_no == null || invoice_no == "") {
	        var msg = "인보이스 부여되지 않은 품목입니다.\n" + selectedInvoiceName + "의  W/O, 수량이 같은 품목과 교체하시겠습니까?";
	        if (confirm(msg)) {
	            insertInvoiceInventory(); 
	        } else {
	            history.back(); 
	        }
	    } //선택한 인보이스와 스캔한 품목의 인보이스 다를 때
	    else if (invoice_no != selectedInvoiceNo) {
	        console.log("선택한 인보이스 번호:", selectedInvoiceNo);
	        console.log("스캔한 품목의 인보이스 번호: ", invoice_no);
	        console.log("스캔한 품목의 로트넘버: ", lot_no);

	        //이미 스캔한 품목인지 확인 - 스캔 한건 인보이스 교체 불가
	        $.ajax({
	            url: "/yulchon/management/mobile/getIsShippingList",
	            type: "POST",
	            contentType: "application/json",
	            data: JSON.stringify({
	                invoice_no: invoice_no,
	                lot_no: lot_no
	            }),
	            success: function (data) {
		            console.log("출력한 품목인지 조회 데이터: ", data);
					if(data && Object.keys(data).length > 0){
						alert("인보이스를 잘못 선택하셨습니다.");
						history.back();
						return;
						}

			        //w/o, 수량 같은거 있는지 조회
			        $.ajax({
			            url: "/yulchon/management/mobile/getSameWoQtyInventory",
			            type: "POST",
			            contentType: "application/json",
			            data: JSON.stringify({
			                invoice_no: selectedInvoiceNo,
			                no_mfg_order_serial: no_mfg_order_serial,
			                qty_inventory: qty_inventory
			            }),
			            success: function (matchedItem) {
			                if (matchedItem && matchedItem.lbl_lot_no) {
			                    var matchedLotNo = matchedItem.lbl_lot_no;

			                    if (!confirm("[" + selectedInvoiceName + "] 인보이스에 동일 W/O·수량의 미출력 품목(LOT: " + matchedLotNo + ")이 있습니다.\n로트번호를 서로 교체하시겠습니까?")) {
			                        history.back();
			                        return;
			                    }

			                    $.ajax({
			                        url: "/yulchon/management/mobile/swapLotNo",
			                        type: "POST",
			                        contentType: "application/json",
			                        data: JSON.stringify({
			                            scan_lot_no: lot_no,
			                            scan_invoice_no: invoice_no,
			                            target_lot_no: matchedLotNo,
			                            target_invoice_no: selectedInvoiceNo
			                        }),
			                        success: function (result) {
			                            if (result === true || result === "true") {
			                                alert("로트번호 교체가 완료되었습니다.");
			                                var url = "/yulchon/management/mobile/shippingMarkPrint?lbl_lot_no=" + lot_no
			                                        + "&selectedInvoiceNo=" + selectedInvoiceNo
			                                        + "&selectedInvoiceName=" + encodeURIComponent(selectedInvoiceName);
			                                window.location.href = url;
			                            } else {
			                                alert("로트번호 교체 실패");
			                                history.back();
			                            }
			                        },
			                        error: function () {
			                            alert("로트번호 교체 중 오류가 발생했습니다.");
			                        }
			                    });
			                } else {
			                    alert("매칭되는 교체 대상이 없습니다.");
			                    history.back();
			                }
			            },
			            error: function() {
			                alert("조회 중 오류가 발생했습니다.");
			            }
			        });
	            },
	            error: function() {
	                alert("조회 중 오류가 발생했습니다.");
	            }
	        });

	    } 

	    // 스캔한 품목 인보이스 없을 때 - w/o, 수량 같은거로 로트번호 바꿈
	    function insertInvoiceInventory() {  
	        var addList = [{
	            lbl_lot_no: "${data.lbl_lot_no}",
	            cd_item: "${data.cd_item}",
	            no_mfg_order_serial: "${data.no_mfg_order_serial}",
	            qty_inventory: "${data.qty_inventory}"
	        }];
	        console.log("스캔한거 인보이스 부여 안되어 있을 때: ", addList);
	        const payload = {
	            invoice_no: selectedInvoiceNo,
	            addList: addList
	        };

	        $.ajax({
	            url: "/yulchon/management/mobile/getSameWoQtyInventory",
	            type: "POST",
	            contentType: "application/json",
	            data: JSON.stringify({
	                invoice_no: selectedInvoiceNo,
	                no_mfg_order_serial: "${data.no_mfg_order_serial}",
	                qty_inventory: "${data.qty_inventory}"
	            }),
	            success: function(matchedItem) {

	                // 매칭된 미출력 품목 있으면 → lot_no만 스캔한걸로 UPDATE
	                if (matchedItem && matchedItem.lbl_lot_no) {
	                    var matchedLotNo = matchedItem.lbl_lot_no;

	                    $.ajax({
	                        url: "/yulchon/management/mobile/updateLotNo",
	                        type: "POST",
	                        contentType: "application/json",
	                        data: JSON.stringify({
	                            old_lot_no: matchedLotNo,          // 기존 매칭된 lot
	                            new_lot_no: "${data.lbl_lot_no}",  // 스캔한 lot으로 교체
	                            invoice_no: selectedInvoiceNo
	                        }),
	                        success: function(result) {
	                            if (result === true || result === "true") {
	                                console.log("교체 성공했습니다.");
	                                var url = "/yulchon/management/mobile/shippingMarkPrint?lbl_lot_no=" + lot_no
	                                + "&selectedInvoiceNo=" + selectedInvoiceNo
	                                + "&selectedInvoiceName=" + encodeURIComponent(selectedInvoiceName);
	                        window.location.href = url;
	                            } else {
	                            	console.log("교체 실패했습니다.");
	                                history.back();
	                            }
	                        },
	                        error: function() {
	                            alert("lot 교체 중 오류가 발생했습니다.");
	                            history.back();
	                        }
	                    });
	                // w/o, 수량 같은거 없을 때
	                }else {
	                	alert("W/O, 수량이 같은 품목이 없습니다.");
	                	history.back();
	                }
	            },
	            error: function() {
	                alert("미출력 품목 조회 중 오류가 발생했습니다.");
	                history.back();
	            }
	        });
	    }
	});
			    

   //쉬핑마크 출력 및 출하목록 저장
    function handlePrint() {
      const lot_no = $('#lbl_lot_no').text().trim();
      const invoice_no = $('#invoice_no').text().trim();
      console.log("lot_no: ", lot_no, "invoice_no: ", invoice_no);
      
      if(!lot_no){
          alert("Lot No가 없습니다. 다시 스캔해주세요.");
          return;
          }
    	$.ajax({
    	  	  //url: "/yulchon/management/mobile/insertShippingList",
    	  	  url: "/yulchon/management/mobile/printShippingMark",
    	  	  type: "POST",
    	  	  contentType: "application/json",
    	  	  data: JSON.stringify({lbl_lot_no: lot_no, invoice_no: invoice_no}),
    	  	  beforeSend: function(){
    	  		$('#loadingOverlay').css('display', 'flex');
        	  	  },
    	  	  success: function(result) {
    	  	  	  if(result.result === true || result.result === "true"){
    					alert(result.message);
    	  	  	  	  }else{
    					alert(result.message);
    	  	  	  	  	  }
    	  	  },
    	  	  error: function() {
    	  	    alert('쉬핑마크 출력 중 오류가 발생했습니다.');
    	  	  },
    	  	  complete: function(){
    	  		$('#loadingOverlay').hide();
        	  	  }
    	  	});
    }

    function handleBack() {
    	openCamera();
    }
    function openCamera() {
        const input = document.createElement("input");
        input.type = "file";
        input.accept = "image/*";
        input.capture = "environment";

        input.onchange = async function (e) {
            const file = e.target.files && e.target.files[0];
            if (!file) return;

            showLoading();
            try {
                const decoded = await decodeBarcodeOrQrFromFile(file);
                if (decoded) {
                    // 인보이스 번호와 QR/바코드 텍스트를 함께 전달
                	var url = "/yulchon/management/mobile/shippingMarkPrint?lbl_lot_no=" 
                        + encodeURIComponent(decoded.text) 
                        + "&selectedInvoiceNo=" 
                        + encodeURIComponent(selectedInvoiceNo)
                        + "&selectedInvoiceName=" 
                        + encodeURIComponent(selectedInvoiceName);
              window.location.href = url;
                } else {
                    //alert("인식 실패. 선명하게 다시 촬영해주세요.");
                	// 2. 스캔 실패 시: 직접 입력창 띄우고 입력값으로 이동
                    hideLoading(); // 입력창을 띄우기 위해 로딩바 숨김
                    
                    const userInput = prompt("바코드나 QR을 인식하지 못했습니다.\nLOT 번호를 직접 입력해주세요.");
                    
                    // 취소를 눌렀거나 아무것도 입력 안 했을 때 처리
                    if (userInput === null) return; 
                    if (userInput.trim() === "") {
                        alert("번호를 입력해야 합니다.");
                        return;
                    }

                    console.log("직접 입력 완료:", userInput);

                    var url = "/yulchon/management/mobile/shippingMarkPrint?lbl_lot_no=" 
                            + encodeURIComponent(userInput.trim()) 
                            + "&selectedInvoiceNo=" 
                            + encodeURIComponent(selectedInvoiceNo)
                            + "&selectedInvoiceName=" 
                            + encodeURIComponent(selectedInvoiceName);
                    
                    window.location.href = url;
                }
            } catch (err) {
                alert("스캔 중 오류 발생");
            } finally {
                hideLoading();
            }
        };
        input.click();
    }

    //고객사 ROCS면 버튼 텍스트 바꿈
    function updateButtonLabel() {
    const customerName = $('#nm_customer').text().trim();
    console.log("고객사: ", customerName);
    if (customerName.includes('ROCS')) {
        $('.btn-print').text('출하 등록');
    } else {
        $('.btn-print').text('출력');
    }
}
    
  //===== 메인 디코딩 함수 =====
    async function decodeBarcodeOrQrFromFile(file) {
        const bitmap = await createImageBitmap(file, { imageOrientation: "from-image" });

        // 1) 다운스케일 캔버스 생성
        const baseCanvas = drawBitmapToScaledCanvas(bitmap, MAX_LONG_EDGE);

        // 2) 전처리 캔버스 생성
        const preCanvas = applyPreprocessFilter(baseCanvas, PREPROCESS_FILTER);

        // 3) ZXing Reader 생성
        const codeReader = createZxingReaderWithHints();

        // 4) 전처리 캔버스 기준 크롭 여러 단계 시도
        for (let i = 0; i < CROP_RATIOS.length; i++) {
            const ratio = CROP_RATIOS[i];

            const candidateCanvas = (ratio === 1.0)
                ? preCanvas
                : cropCenter(preCanvas, ratio);

            const result = await tryDecodeReaderFromCanvasOrImage(codeReader, candidateCanvas);
            if (result) {
                return result;
            }
        }

        // 5) 전처리가 오히려 방해인 케이스 대비, 원본(base)도 크롭 여러 단계 시도
        for (let i = 0; i < CROP_RATIOS.length; i++) {
            const ratio = CROP_RATIOS[i];

            const candidateCanvas = (ratio === 1.0)
                ? baseCanvas
                : cropCenter(baseCanvas, ratio);

            const result = await tryDecodeReaderFromCanvasOrImage(codeReader, candidateCanvas);
            if (result) {
                return result;
            }
        }

        return null;
    }

    // ===== ZXing 리더 생성 =====
    function createZxingReaderWithHints() {
        const hints = new Map();
        hints.set(ZXing.DecodeHintType.POSSIBLE_FORMATS, ZXING_FORMATS);
        hints.set(ZXing.DecodeHintType.TRY_HARDER, true);

        // 번들에 따라 생성자 hints 지원 여부가 다를 수 있음
        let reader = null;

        try {
            reader = new ZXing.BrowserMultiFormatReader(hints);
        } catch (e) {
            console.warn("[ZXing] BrowserMultiFormatReader(hints) 실패, 기본 생성자 사용", e);
            reader = new ZXing.BrowserMultiFormatReader();
        }

        return reader;
    }

    // ===== 호환형 디코딩: 캔버스 디코딩이 없으면 ImageElement로 디코딩 =====
    async function tryDecodeReaderFromCanvasOrImage(codeReader, canvas) {
        // 1) decodeFromCanvas가 있는 환경이면 캔버스로 디코딩 시도
        if (codeReader && typeof codeReader.decodeFromCanvas === "function") {
            try {
                const result = await codeReader.decodeFromCanvas(canvas);
                return normalizeZxingResult(result);
            } catch (e) {
                if (e && e.name === "NotFoundException") {
                    return null;
                }
                console.error("[ZXing decodeFromCanvas error]", e);
                return null;
            }
        }

        // 2) decodeFromImageElement가 있는 환경이면 캔버스를 이미지로 변환 후 디코딩
        if (codeReader && typeof codeReader.decodeFromImageElement === "function") {
            try {
                const img = await canvasToImageElement(canvas);
                const result = await codeReader.decodeFromImageElement(img);
                return normalizeZxingResult(result);
            } catch (e) {
                if (e && e.name === "NotFoundException") {
                    return null;
                }
                console.error("[ZXing decodeFromImageElement error]", e);
                return null;
            }
        }

        // 3) 최후 수단: decodeFromImageUrl이 있으면 dataURL로 디코딩
        if (codeReader && typeof codeReader.decodeFromImageUrl === "function") {
            try {
                const dataUrl = canvas.toDataURL("image/png");
                const result = await codeReader.decodeFromImageUrl(dataUrl);
                return normalizeZxingResult(result);
            } catch (e) {
                if (e && e.name === "NotFoundException") {
                    return null;
                }
                console.error("[ZXing decodeFromImageUrl error]", e);
                return null;
            }
        }

        console.error("[ZXing] 사용 가능한 decode 메서드가 없는 상태", codeReader);
        return null;
    }

    // ===== ZXing 결과 정규화 =====
    function normalizeZxingResult(result) {
        if (!result) {
            return null;
        }

        const text = result.text || (result.getText ? result.getText() : null);
        const format = result.getBarcodeFormat ? result.getBarcodeFormat() : null;

        if (!text) {
            return null;
        }

        return { text: text, format: format };
    }

    // ===== 캔버스를 ImageElement로 변환 =====
    function canvasToImageElement(canvas) {
        return new Promise(function (resolve, reject) {
            try {
                const img = new Image();
                img.onload = function () {
                    resolve(img);
                };
                img.onerror = function (e) {
                    reject(e);
                };
                img.src = canvas.toDataURL("image/png");
            } catch (e) {
                reject(e);
            }
        });
    }

    // ===== bitmap을 스케일하여 canvas에 그림 =====
    function drawBitmapToScaledCanvas(bitmap, maxLongEdge) {
        const w = bitmap.width;
        const h = bitmap.height;

        const longEdge = Math.max(w, h);
        const scale = Math.min(maxLongEdge / longEdge, 1);

        const cw = Math.max(1, Math.round(w * scale));
        const ch = Math.max(1, Math.round(h * scale));

        const canvas = document.createElement("canvas");
        canvas.width = cw;
        canvas.height = ch;

        const ctx = canvas.getContext("2d", { willReadFrequently: true });

        ctx.fillStyle = "#FFFFFF";
        ctx.fillRect(0, 0, cw, ch);

        ctx.drawImage(bitmap, 0, 0, cw, ch);

        return canvas;
    }

    // ===== 전처리 필터 적용 =====
    function applyPreprocessFilter(srcCanvas, filterStr) {
        const canvas = document.createElement("canvas");
        canvas.width = srcCanvas.width;
        canvas.height = srcCanvas.height;

        const ctx = canvas.getContext("2d", { willReadFrequently: true });

        if (ctx.filter !== undefined) {
            ctx.filter = filterStr;
        }

        ctx.drawImage(srcCanvas, 0, 0);

        if (ctx.filter !== undefined) {
            ctx.filter = "none";
        }

        return canvas;
    }

    // ===== 중앙 크롭 =====
    function cropCenter(srcCanvas, ratio) {
        const sw = srcCanvas.width;
        const sh = srcCanvas.height;

        const cw = Math.max(1, Math.round(sw * ratio));
        const ch = Math.max(1, Math.round(sh * ratio));

        const sx = Math.max(0, Math.round((sw - cw) / 2));
        const sy = Math.max(0, Math.round((sh - ch) / 2));

        const canvas = document.createElement("canvas");
        canvas.width = cw;
        canvas.height = ch;

        const ctx = canvas.getContext("2d", { willReadFrequently: true });

        ctx.fillStyle = "#FFFFFF";
        ctx.fillRect(0, 0, cw, ch);

        ctx.drawImage(srcCanvas, sx, sy, cw, ch, 0, 0, cw, ch);

        return canvas;
    }

    // ===== URL 유효성 검사 =====
    function isValidUrl(str) {
        try {
            const url = new URL(str);
            return (url.protocol === "http:" || url.protocol === "https:");
        } catch (e) {
            return false;
        }
    }

    // ===== 로그 =====
    function log(msg, obj) {
        const line = obj ? (msg + " " + JSON.stringify(obj)) : msg;
        console.log(line);

        const el = document.getElementById("scanLog");
        if (el) {
            el.textContent += line + "\n";
        }
    }

    // ===== 결과 처리 =====
    function processResult(text, format) {
        if (isValidUrl(text)) {
            log("[스캔 결과]", { text: text, format: format });
            alert("스캔 결과: " + text);
        } else {
            log("[처리] 바코드 데이터", { text: text, format: format });
            alert("바코드 데이터: " + text);
        }
    }
  </script>
</body>
</html>
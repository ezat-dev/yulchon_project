<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="description" content="태경열처리 관리 시스템">
  <meta name="author" content="태경열처리">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="/yulchon/css/login/style.css">

	<script src="https://cdn.jsdelivr.net/npm/ionicons@latest/dist/ionicons/ionicons.js"></script>
<!-- 	<script src="https://cdn.jsdelivr.net/npm/jsqr@1.4.0/dist/jsQR.js"></script>  -->
<script src="https://unpkg.com/@zxing/library@latest"></script>
<script src="https://cdn.jsdelivr.net/npm/tesseract.js@5/dist/tesseract.min.js"></script>
<%@include file="../include/pluginpage.jsp" %>  
  <title>율촌</title>
  <style>
  .scan-page {
    padding: 20px;
}

/* 왼쪽 위 제목 */
.scan-title {
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 60px;
}

/* 버튼 영역 */
.scan-button-wrap {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 110px;
}

/* 공통 버튼 */
.scan-btn {
    height: 140px;
    font-size: 22px;
    font-weight: bold;
    border-radius: 12px;
    border: none;
    cursor: pointer;
    color: #fff;
}

/* 버튼별 색상 */
.btn-print {
    background-color: #4f8fd3;
}

.btn-cancel {
    background-color: #d9534f;
}

.btn-check {
    background-color: #5cb85c;
}

/* 🔥 태블릿 */
@media (max-width: 1024px) {
    .scan-button-wrap {
        grid-template-columns: 1fr;
    }

    .scan-btn {
        height: 120px;
        font-size: 20px;
    }
}

/* 🔥 모바일 */
@media (max-width: 600px) {
    .scan-title {
        font-size: 40px;
    }

    .scan-btn {
        height: 100px;
        font-size: 25px;
    }
}
  .scan-page,
.scan-button-wrap,
.scan-btn {
  position: relative !important;
  z-index: 999999 !important;
}
@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
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
    <div class="scan-title">스캔</div>

    <div class="scan-button-wrap">
        <button class="scan-btn btn-print">쉬핑마크 출력</button>
        <button type="button" class="scan-btn btn-cancel">출하 취소</button>
        <button class="scan-btn btn-check">제품 확인</button>
    </div>
    <input type="file" id="qrInput" accept="image/*" capture="environment" style="display:none;">
</div>

<div id="loadingOverlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 9999; flex-direction: column; align-items: center; justify-content: center; color: white;">
    <div class="spinner" style="width: 50px; height: 50px; border: 5px solid #f3f3f3; border-top: 5px solid #3498db; border-radius: 50%; animation: spin 1s linear infinite;"></div>
    <p style="margin-top: 15px;">이미지를 분석 중입니다...</p>
</div>

<!-- 로그 -->
<pre id="scanLog"
     style="white-space:pre-wrap; border:1px solid #ccc; padding:10px; margin-top:20px;">
</pre>
<script>
let lotValue = "";
let scannedText = null;
let scannedFormat = null;
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

//로딩 표시
const showLoading = () => $('#loadingOverlay').css('display', 'flex');
const hideLoading = () => $('#loadingOverlay').hide();

$(function() {
	hideLoading();
	  console.log('jQuery 버전:', $.fn.jquery);
	  console.log('jsQR 타입:', typeof jsQR);
	  console.log('.btn-print 개수:', $('.btn-print').length);
	  console.log('.btn-cancel 개수:', $('.btn-cancel').length);
	  console.log('.btn-check 개수:', $('.btn-check').length);

	//쉬핑마크 출력 클릭시
$(document).on("click", ".btn-print", function () {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.capture = "environment";

    input.onchange = async function (e) {
        const file = e.target.files && e.target.files[0];
        if (!file) { 
            return; 
        }

        showLoading();

        try {
            const decoded = await decodeBarcodeOrQrFromFile(file);

            console.log("[decoded]", decoded);

            if (decoded) {
                window.location.href = "/yulchon/management/mobile/shippingMarkPrint?lbl_lot_no=" + encodeURIComponent(decoded.text);
            } else {
                alert("바코드나 QR코드를 인식하지 못했습니다. 선명하게 다시 촬영해주세요.");
            }
        } catch (err) {
            console.error("[scan fatal error]", err);
            alert("스캔 중 오류가 발생했습니다.");
        } finally {
            hideLoading();
        }
    };

    input.click();
});

//출하 취소 클릭시
$(document).on('click', '.btn-cancel', function(e) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.capture = "environment";

    input.onchange = async function (e) {
        const file = e.target.files && e.target.files[0];
        if (!file) { 
            return; 
        }

        showLoading();

        try {
            const decoded = await decodeBarcodeOrQrFromFile(file);

            console.log("[decoded]", decoded);

            if (decoded) {
                window.location.href = "/yulchon/management/mobile/shippingCancel?lbl_lot_no=" + encodeURIComponent(decoded.text);
            } else {
                alert("바코드나 QR코드를 인식하지 못했습니다. 선명하게 다시 촬영해주세요.");
            }
        } catch (err) {
            console.error("[scan fatal error]", err);
            alert("스캔 중 오류가 발생했습니다.");
        } finally {
            hideLoading();
        }
    };

    input.click();
});

//제품확인 클릭시
$(document).on('click', '.btn-check', function(e) {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";
    input.capture = "environment";

    input.onchange = async function (e) {
        const file = e.target.files && e.target.files[0];
        if (!file) { 
            return; 
        }

        showLoading();

        try {
            const decoded = await decodeBarcodeOrQrFromFile(file);

            console.log("[decoded]", decoded);

            if (decoded) {
                window.location.href = "/yulchon/management/mobile/productConfirm?lbl_lot_no=" + encodeURIComponent(decoded.text);
            } else {
                alert("바코드나 QR코드를 인식하지 못했습니다. 선명하게 다시 촬영해주세요.");
            }
        } catch (err) {
            console.error("[scan fatal error]", err);
            alert("스캔 중 오류가 발생했습니다.");
        } finally {
            hideLoading();
        }
    };

    input.click();
});

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
});


</script>
</body>
</html>
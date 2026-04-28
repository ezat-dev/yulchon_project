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
  <script src="https://unpkg.com/@zxing/library@latest"></script>
  <script src="https://cdn.jsdelivr.net/npm/tesseract.js@5/dist/tesseract.min.js"></script>
  <%@include file="../include/pluginpage.jsp" %>
  <title>율촌</title>
  <style>
.scan-page {
    padding: 20px;
}

.scan-title {
    font-size: 28px;
    font-weight: bold;
    margin-bottom: 60px;
    display: flex;
    justify-content: center;
}

.scan-button-wrap {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 110px;
}

.scan-btn {
    height: 140px;
    font-size: 22px;
    font-weight: bold;
    border-radius: 12px;
    border: none;
    cursor: pointer;
    color: #fff;
}

.btn-print  { background-color: #4f8fd3; }
.btn-cancel { background-color: #d9534f; }
.btn-check  { background-color: #5cb85c; }

.scan-btn.btn-active {
    outline: 4px solid #fff;
    box-shadow: 0 0 0 6px rgba(0,0,0,0.35);
    transform: scale(1.03);
}

@media (max-width: 1024px) {
    .scan-button-wrap {
        grid-template-columns: 1fr;
        gap: 20px;
    }
    .scan-btn {
        height: 120px;
        font-size: 20px;
    }
}

@media (max-width: 600px) {
    .scan-title { font-size: 40px; }
    .scan-btn   { height: 100px; font-size: 25px; }
}

@keyframes spin {
    0%   { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

#loadingOverlay {
    display: none;
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(0,0,0,0.7);
    z-index: 9999999 !important;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: white;
}

.modal-overlay {
    position: fixed; top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(0,0,0,0.6);
    display: flex; align-items: center; justify-content: center;
    z-index: 1000;
}
.modal-content {
    width: 85%; max-width: 400px;
    background: #fff; border-radius: 8px;
    overflow: hidden; display: flex;
    flex-direction: column; max-height: 70vh;
}
.modal-header { padding: 15px; background: #222; color: #fff; text-align: center; font-weight: bold; }
.modal-body   { flex: 1; overflow-y: auto; padding: 10px; min-height: 100px;}
.modal-footer { border-top: 1px solid #eee; padding: 10px; text-align: center; }

.invoice-list { list-style: none; padding: 0; margin: 0; }
.invoice-item {
    padding: 15px; border-bottom: 1px solid #f0f0f0;
    font-size: 16px; cursor: pointer;
    display: flex; justify-content: space-between; align-items: center;
}
.invoice-item:active { background-color: #f8f8f8; }
.invoice-item:after  { content: '❯'; color: #ccc; font-size: 12px; }

.btn-close-modal {
    width: 100%; padding: 10px;
    border: none; background: #eee; border-radius: 4px;
    font-size: 16px; cursor: pointer;
}

/* PDA 상태 안내 */
#pdaStatus {
    display: none;
    margin-top: 30px;
    padding: 18px 20px;
    background: #e8f0fe;
    border-left: 5px solid #4f8fd3;
    border-radius: 8px;
    font-size: 20px;
    font-weight: bold;
    color: #1a3c6e;
    text-align: center;
    line-height: 1.6;
}

/* PDA 입력창 - 실제 input */
#pdaInputDisplay {
    display: none;
    margin-top: 12px;
    padding: 14px 18px;
    width: 100%;
    background: #fff;
    border: 2px solid #4f8fd3;
    border-radius: 8px;
    font-size: 26px;
    font-weight: bold;
    color: #1a3c6e;
    letter-spacing: 0.05em;
    text-align: center;
    box-sizing: border-box;
    outline: none;
}
#pdaInputDisplay:focus {
    border-color: #1a3c6e;
    box-shadow: 0 0 0 3px rgba(79,143,211,0.3);
}
  </style>
</head>
<body>

<div class="scan-page">
    <div class="scan-title">
        <a href="#" class="nav__logo">
            <img class="yulchonLogo" src="/yulchon/css/sideBar/yulchon_logo.png">
        </a>
    </div>

    <div class="scan-button-wrap">
        <button type="button" class="scan-btn btn-cancel">출하 취소</button>
        <button type="button" class="scan-btn btn-print">쉬핑마크 출력</button>
        <button type="button" class="scan-btn btn-check">품목 확인</button>
    </div>

    <!-- PDA 상태 안내 -->
    <div id="pdaStatus"></div>

    <!-- PDA 입력창 (보이는 실제 input) -->
    <input type="text" id="pdaInputDisplay"
           autocomplete="off" autocorrect="off"
           autocapitalize="off" spellcheck="false"
           inputmode="url"
           lang="en"
           placeholder="스캔 또는 직접 입력 후 Enter">
</div>

<!-- 로딩 오버레이 -->
<div id="loadingOverlay">
    <div class="spinner" style="width:50px;height:50px;border:5px solid #f3f3f3;border-top:5px solid #3498db;border-radius:50%;animation:spin 1s linear infinite;"></div>
    <p style="margin-top:15px;">이미지를 분석 중입니다...</p>
</div>

<!-- 인보이스 선택 모달 -->
<div id="invoiceModal" class="modal-overlay" style="display:none;">
    <div class="modal-content">
        <div class="modal-header">인보이스 선택</div>
        <div class="modal-body">
            <ul id="invoiceList" class="invoice-list"></ul>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-close-modal">닫기</button>
        </div>
    </div>
</div>

<script>
// =====================================================
// 공통 변수
// =====================================================
var selectedInvoiceNo   = "";
var selectedInvoiceName = "";

var isPDA = new URLSearchParams(window.location.search).get("mode") === "pda";

// =====================================================
// 로딩 (웹모바일 전용)
// =====================================================
var showLoading = function() { $('#loadingOverlay').css('display', 'flex'); };
var hideLoading = function() { $('#loadingOverlay').hide(); };

// =====================================================
// ZXing 설정 (웹모바일 전용)
// =====================================================
var ZXING_FORMATS = [
    ZXing.BarcodeFormat.QR_CODE,
    ZXing.BarcodeFormat.CODE_128,
    ZXing.BarcodeFormat.CODE_39,
    ZXing.BarcodeFormat.EAN_13,
    ZXing.BarcodeFormat.ITF
];
var CROP_RATIOS   = [1.0, 0.85, 0.72, 0.60];
var MAX_LONG_EDGE = 1600;
var PREPROCESS_FILTER = [
    "grayscale(1) contrast(1.25)",
    "grayscale(1) contrast(1.5) brightness(0.9)",
    "grayscale(1) contrast(1.8) brightness(0.8)",
    "grayscale(1) contrast(2.0) brightness(0.75)"
];

// =====================================================
// 공통: 인보이스 리스트 렌더링
// =====================================================
function renderInvoiceList(list) {
    var $listContainer = $("#invoiceList");
    $listContainer.empty();
    if (!list || list.length === 0) {
        $listContainer.append('<li class="invoice-item" style="justify-content:center;">목록이 없습니다.</li>');
    } else {
        list.forEach(function(item) {
            var html =
                '<li class="invoice-item"' +
                ' data-invoice-name="' + item.invoice_name + '"' +
                ' data-invoice-no="'   + item.invoice_no   + '">' +
                item.invoice_name + '</li>';
            $listContainer.append(html);
        });
    }
    $("#invoiceModal").fadeIn(200);
}

// =====================================================
// jQuery ready
// =====================================================
$(function() {
    hideLoading();
    if (isPDA) {
        initPDAMode();
    } else {
        initWebMode();
    }
});


// #####################################################
// PDA 모드
// #####################################################
function initPDAMode() {

    var $pdaStatus    = $('#pdaStatus');
    var $inputDisplay = $('#pdaInputDisplay'); // 이게 곧 스캔 input

    $pdaStatus.show();

    // 상태 안내 + 입력창 표시 제어
    function showPDAStatus(msg, showInput) {
        $pdaStatus.html(msg);
        if (showInput) {
            $inputDisplay.show().val('');
        } else {
            $inputDisplay.hide().val('');
        }
    }
    showPDAStatus('버튼 선택 후 바코드를 스캔하세요.', false);

    // 현재 대기 액션: null / 'cancel' / 'check' / 'print'
    var pendingAction = null;

    // 버튼 활성 강조
    function setActiveBtn(btnClass) {
        $('.scan-btn').removeClass('btn-active');
        if (btnClass) $('.' + btnClass).addClass('btn-active');
    }

    //품목 찾을 수 없을 때
    var errorMsg = new URLSearchParams(window.location.search).get("errorMsg");
    if (errorMsg) {
        // 이미 출력한 품목 → 모달 띄우기
        $('<style>').text(`
        @keyframes blink {
            0%, 100% { opacity: 1; }
            50% { opacity: 0; }
        }
    `).appendTo('head');

    const $overlay = $('<div>').css({
        position: 'fixed', top: 0, left: 0,
        width: '100%', height: '100%',
        background: 'rgba(0,0,0,0.4)',
        zIndex: 9998
    });
    const $modal = $('<div>').addClass('blink-modal').css({
        position: 'fixed', top: '50%', left: '50%',
        transform: 'translate(-50%, -50%)',
        background: '#fff', padding: '20px 30px',
        border: '1px solid #ccc', borderRadius: '4px',
        zIndex: 9999, textAlign: 'center',
        boxShadow: '0 2px 10px rgba(0,0,0,0.3)',
        minWidth: '350px',
        fontSize: '18px'
    }).append(
        $('<p>').text(errorMsg).css('color', 'red'),
        $('<button>').text('확인').css({
            marginTop: '10px',
            padding: '10px 40px',
            fontSize: '16px',
            cursor: 'pointer'
        }).on('click', function() {
            $overlay.remove();
            $modal.remove();
            $('#pdaInputDisplay').on('focus', function() {
                if ($(this).val() === '') {
                    $(this).val('D');
                }
            });
            $('#pdaInputDisplay').focus();
        })
    );
    $overlay.appendTo('body');
    $modal.appendTo('body');
    }

    // -----------------------------------------------
    // 버튼 클릭 핸들러
    // -----------------------------------------------

    // 출하 취소
    $(document).on('click', '.btn-cancel', function() {
        pendingAction = 'cancel';
        setActiveBtn('btn-cancel');
        showPDAStatus('출하 취소<br>바코드를 스캔하거나 LOT번호를 입력 후 Enter', true);
     // 클릭 이벤트 컨텍스트 안이라 포커스 허용됨
        $('#pdaInputDisplay').on('focus', function() {
            if ($(this).val() === '') {
                $(this).val('D');
            }
        });
        $('#pdaInputDisplay').focus();
    });

    // 품목 확인
    $(document).on('click', '.btn-check', function() {
        pendingAction = 'check';
        setActiveBtn('btn-check');
        showPDAStatus('품목 확인<br>바코드를 스캔하거나 LOT번호를 입력 후 Enter', true);
     // 클릭 이벤트 컨텍스트 안이라 포커스 허용됨
        $('#pdaInputDisplay').on('focus', function() {
            if ($(this).val() === '') {
                $(this).val('D');
            }
        });
        $('#pdaInputDisplay').focus();
    });

    // 쉬핑마크 출력 → 인보이스 선택 먼저
    $(document).on('click', '.btn-print', function() {
        setActiveBtn('btn-print');
        showPDAStatus('쉬핑마크 출력<br>인보이스를 선택하세요.', false);

        $.ajax({
            url:      "/yulchon/management/mobile/getNoUpdatedInvoiceList?mode=pda",
            method:   "POST",
            dataType: "json",
            success: function(data) {
                renderInvoiceList(data);
            },
            error: function() {
                alert("인보이스 목록 조회 중 오류가 발생했습니다.");
                pendingAction = null;
                setActiveBtn(null);
                showPDAStatus('버튼 선택 후 바코드를 스캔하세요.', false);
            }
        });
    });

    // 인보이스 선택 → 그때부터 스캔 대기
    $(document).on('click', '.invoice-item', function() {
        selectedInvoiceNo   = $(this).data("invoice-no");
        selectedInvoiceName = $(this).data("invoice-name");
        if (!selectedInvoiceNo) return;

        // fadeOut 콜백 제거 → 클릭 이벤트 안에서 바로 처리
        $("#invoiceModal").hide();
        pendingAction = 'print';
        showPDAStatus(
            '쉬핑마크 출력<br>' + selectedInvoiceName +
            '<br>바코드를 스캔하거나 LOT번호를 입력 후 Enter',
            true
        );
        // 클릭 이벤트 컨텍스트 안이라 포커스 허용됨
        $('#pdaInputDisplay').on('focus', function() {
            if ($(this).val() === '') {
                $(this).val('D');
            }
        });
        $('#pdaInputDisplay').focus();
    });

    // 모달 닫기
    $(document).on('click', '.btn-close-modal', function() {
        $("#invoiceModal").fadeOut(200, function() {
            pendingAction = null;
            setActiveBtn(null);
            showPDAStatus('버튼 선택 후 바코드를 스캔하세요.', false);
        });
    });

    // -----------------------------------------------
    // 입력 감지
    // -----------------------------------------------
    // 한글 → 영문 변환
	$inputDisplay.on('input', function() {
	    var val = $(this).val();

	    // DD로 시작하면 앞의 D 하나 제거
	    if (val.startsWith('DD')) {
	        $(this).val(val.substring(1));
	        return;
	    }
	    
	    var korToEng = {
	        'ㅂ':'Q','ㅈ':'W','ㄷ':'E','ㄱ':'R','ㅅ':'T','ㅛ':'Y','ㅕ':'U','ㅑ':'I','ㅐ':'O','ㅔ':'P',
	        'ㅁ':'A','ㄴ':'S','ㅇ':'D','ㄹ':'F','ㅎ':'G','ㅗ':'H','ㅓ':'J','ㅏ':'K','ㅣ':'L',
	        'ㅋ':'Z','ㅌ':'X','ㅊ':'C','ㅍ':'V','ㅠ':'B','ㅜ':'N','ㅡ':'M'
	    };
	    var converted = val.replace(/[\u3131-\u314E]/g, function(ch) {
	        return korToEng[ch] || ch;
	    });
	    converted = converted.replace(/[\uAC00-\uD7A3]/g, '');
	    if (converted !== val) {
	        $(this).val(converted);
	    }
	});
    
    $inputDisplay.on('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            var value = $inputDisplay.val().trim();
            $inputDisplay.val('');
            if (!value) return;
            handlePDAScan(value);
        }
    });

    // -----------------------------------------------
    // 스캔 처리 → URL 이동
    // -----------------------------------------------
    function handlePDAScan(value) {
        if (!pendingAction) {
            alert('버튼을 먼저 선택해주세요.');
            return;
        }

        var action = pendingAction;
        pendingAction = null;

        switch (action) {
            case 'cancel':
                window.location.href =
                    "/yulchon/management/mobile/shippingCancel?lbl_lot_no=" +
                    encodeURIComponent(value) + "?mode=pda";
                break;

            case 'check':
                window.location.href =
                    "/yulchon/management/mobile/productConfirm?lbl_lot_no=" +
                    encodeURIComponent(value) + "?mode=pda";
                break;

            case 'print':
                window.location.href =
                    "/yulchon/management/mobile/shippingMarkPrint" +
                    "?lbl_lot_no="          + encodeURIComponent(value) +
                    "&selectedInvoiceNo="   + encodeURIComponent(selectedInvoiceNo) +
                    "&selectedInvoiceName=" + encodeURIComponent(selectedInvoiceName) +
                    "&mode=pda";
                break;

            default:
                alert('알 수 없는 동작입니다.');
        }
    }

    // postMessage 수신 (pdaTest.jsp 시뮬레이터 연동용)
    window.addEventListener('message', function(e) {
        if (e.data && e.data.type === 'PDA_SCAN' && e.data.lotValue) {
            handlePDAScan(e.data.lotValue);
        }
    });

    // 복귀 시 인보이스 자동 복원
    var params = new URLSearchParams(window.location.search);
    var returnInvoiceNo   = params.get("invoiceNo");
    var returnInvoiceName = params.get("invoiceName");

    if (returnInvoiceNo && returnInvoiceName) {
        selectedInvoiceNo   = returnInvoiceNo;
        selectedInvoiceName = decodeURIComponent(returnInvoiceName);
        pendingAction = 'print';
        setActiveBtn('btn-print');
        showPDAStatus(
            '쉬핑마크 출력 &nbsp;|&nbsp; ' + selectedInvoiceName +
            '<br>바코드를 스캔하거나 LOT번호를 입력 후 Enter',
            true
        );
        $('#pdaInputDisplay').on('focus', function() {
            if ($(this).val() === '') {
                $(this).val('D');
            }
        });
        $('#pdaInputDisplay').focus();
    }
}


// #####################################################
// 웹모바일 모드 (기존 로직 그대로)
// #####################################################
function initWebMode() {

    $(document).on("click", ".btn-print", function() {
        $.ajax({
            url:      "/yulchon/management/mobile/getNoUpdatedInvoiceList",
            method:   "POST",
            dataType: "json",
            success: function(data) {
                hideLoading();
                renderInvoiceList(data);
            },
            error: function() {
                hideLoading();
                alert("에러 발생");
            }
        });
    });

    $(document).on("click", ".invoice-item", function() {
        selectedInvoiceNo   = $(this).data("invoice-no");
        selectedInvoiceName = $(this).data("invoice-name");
        if (!selectedInvoiceNo) return;
        $("#invoiceModal").fadeOut(200);
        openCameraForPrint();
    });

    $(document).on("click", ".btn-close-modal", function() {
        $("#invoiceModal").fadeOut(200);
    });

    function openCameraForPrint() {
        var input     = document.createElement("input");
        input.type    = "file";
        input.accept  = "image/*";
        input.capture = "environment";

        input.onchange = async function(e) {
            var file = e.target.files && e.target.files[0];
            if (!file) return;
            showLoading();
            try {
                var decoded = await decodeBarcodeOrQrFromFile(file);
                if (decoded) {
                    window.location.href =
                        "/yulchon/management/mobile/shippingMarkPrint" +
                        "?lbl_lot_no="          + encodeURIComponent(decoded.text) +
                        "&selectedInvoiceNo="   + encodeURIComponent(selectedInvoiceNo) +
                        "&selectedInvoiceName=" + encodeURIComponent(selectedInvoiceName);
                } else {
                    hideLoading();
                    var userInput = prompt("바코드나 QR을 인식하지 못했습니다.\nLOT 번호를 직접 입력해주세요.", "D");
                    if (userInput === null) return;
                    if (userInput.trim() === "") { alert("번호를 입력해야 합니다."); return; }
                    window.location.href =
                        "/yulchon/management/mobile/shippingMarkPrint" +
                        "?lbl_lot_no="          + encodeURIComponent(userInput.trim()) +
                        "&selectedInvoiceNo="   + encodeURIComponent(selectedInvoiceNo) +
                        "&selectedInvoiceName=" + encodeURIComponent(selectedInvoiceName);
                }
            } catch(err) {
                alert("스캔 중 오류 발생");
            } finally {
                hideLoading();
            }
        };
        input.click();
    }

    $(document).on('click', '.btn-cancel', function() {
        var input     = document.createElement("input");
        input.type    = "file";
        input.accept  = "image/*";
        input.capture = "environment";

        input.onchange = async function(e) {
            var file = e.target.files && e.target.files[0];
            if (!file) return;
            showLoading();
            try {
                var decoded = await decodeBarcodeOrQrFromFile(file);
                if (decoded) {
                    window.location.href =
                        "/yulchon/management/mobile/shippingCancel?lbl_lot_no=" +
                        encodeURIComponent(decoded.text);
                } else {
                    hideLoading();
                    var userInput = prompt("바코드나 QR을 인식하지 못했습니다.\nLOT 번호를 직접 입력해주세요.");
                    if (userInput === null) return;
                    if (userInput.trim() === "") { alert("번호를 입력해야 합니다."); return; }
                    window.location.href =
                        "/yulchon/management/mobile/shippingCancel?lbl_lot_no=" +
                        encodeURIComponent(userInput.trim());
                }
            } catch(err) {
                alert("스캔 중 오류가 발생했습니다.");
            } finally {
                hideLoading();
            }
        };
        input.click();
    });

    $(document).on('click', '.btn-check', function() {
        var input     = document.createElement("input");
        input.type    = "file";
        input.accept  = "image/*";
        input.capture = "environment";

        input.onchange = async function(e) {
            var file = e.target.files && e.target.files[0];
            if (!file) return;
            showLoading();
            try {
                var decoded = await decodeBarcodeOrQrFromFile(file);
                if (decoded) {
                    window.location.href =
                        "/yulchon/management/mobile/productConfirm?lbl_lot_no=" +
                        encodeURIComponent(decoded.text);
                } else {
                    hideLoading();
                    var userInput = prompt("바코드나 QR을 인식하지 못했습니다.\nLOT 번호를 직접 입력해주세요.");
                    if (userInput === null) return;
                    if (userInput.trim() === "") { alert("번호를 입력해야 합니다."); return; }
                    window.location.href =
                        "/yulchon/management/mobile/productConfirm?lbl_lot_no=" +
                        encodeURIComponent(userInput.trim());
                }
            } catch(err) {
                alert("스캔 중 오류가 발생했습니다.");
            } finally {
                hideLoading();
            }
        };
        input.click();
    });

    // -----------------------------------------------
    // 디코딩 함수들 (기존 그대로)
    // -----------------------------------------------

    async function decodeBarcodeOrQrFromFile(file) {
        var bitmap     = await createImageBitmap(file, { imageOrientation: "from-image" });
        var baseCanvas = drawBitmapToScaledCanvas(bitmap, MAX_LONG_EDGE);
        var codeReader = createZxingReaderWithHints();
        var gammaValues = [1.5, 2.0, 2.5];

        for (var gi = 0; gi < gammaValues.length; gi++) {
            var g           = gammaValues[gi];
            var gammaCanvas = applyGammaCorrection(baseCanvas, g);
            var pipelines   = [
                applyAdaptiveThreshold(gammaCanvas, 15, 10),
                applyAdaptiveThreshold(applyBlur(gammaCanvas), 21, 7)
            ];
            for (var fi = 0; fi < PREPROCESS_FILTER.length; fi++) {
                pipelines.push(applyPreprocessFilter(gammaCanvas, PREPROCESS_FILTER[fi]));
            }
            for (var pi = 0; pi < pipelines.length; pi++) {
                for (var ci = 0; ci < CROP_RATIOS.length; ci++) {
                    var ratio  = CROP_RATIOS[ci];
                    var c      = (ratio === 1.0) ? pipelines[pi] : cropCenter(pipelines[pi], ratio);
                    var result = await tryDecodeReaderFromCanvasOrImage(codeReader, c);
                    if (result) return result;
                }
            }
        }
        return null;
    }

    function applyBlur(srcCanvas) {
        var canvas = document.createElement("canvas");
        canvas.width  = srcCanvas.width;
        canvas.height = srcCanvas.height;
        var ctx = canvas.getContext("2d");
        ctx.filter = "blur(1px)";
        ctx.drawImage(srcCanvas, 0, 0);
        return canvas;
    }

    function applyGammaCorrection(srcCanvas, gamma) {
        gamma = gamma || 1.8;
        var canvas = document.createElement("canvas");
        canvas.width  = srcCanvas.width;
        canvas.height = srcCanvas.height;
        var ctx = canvas.getContext("2d", { willReadFrequently: true });
        ctx.drawImage(srcCanvas, 0, 0);
        var imgData = ctx.getImageData(0, 0, canvas.width, canvas.height);
        var data    = imgData.data;
        var lut     = new Uint8Array(256);
        for (var i = 0; i < 256; i++) {
            lut[i] = Math.round(255 * Math.pow(i / 255, gamma));
        }
        for (var j = 0; j < data.length; j += 4) {
            data[j]   = lut[data[j]];
            data[j+1] = lut[data[j+1]];
            data[j+2] = lut[data[j+2]];
        }
        ctx.putImageData(imgData, 0, 0);
        return canvas;
    }

    function applyAdaptiveThreshold(srcCanvas, blockSize, C) {
        blockSize = blockSize || 15;
        C         = C         || 10;
        var canvas = document.createElement("canvas");
        canvas.width  = srcCanvas.width;
        canvas.height = srcCanvas.height;
        var ctx = canvas.getContext("2d", { willReadFrequently: true });
        ctx.drawImage(srcCanvas, 0, 0);
        var imgData  = ctx.getImageData(0, 0, canvas.width, canvas.height);
        var data     = imgData.data;
        var w        = canvas.width;
        var h        = canvas.height;
        var half     = Math.floor(blockSize / 2);
        var gray     = new Uint8Array(w * h);
        for (var i = 0; i < w * h; i++) {
            gray[i] = 0.299 * data[i*4] + 0.587 * data[i*4+1] + 0.114 * data[i*4+2];
        }
        var integral = new Float64Array((w+1) * (h+1));
        for (var y = 1; y <= h; y++) {
            for (var x = 1; x <= w; x++) {
                integral[y*(w+1)+x] =
                    gray[(y-1)*w+(x-1)]
                    + integral[(y-1)*(w+1)+x]
                    + integral[y*(w+1)+(x-1)]
                    - integral[(y-1)*(w+1)+(x-1)];
            }
        }
        for (var ry = 0; ry < h; ry++) {
            for (var rx = 0; rx < w; rx++) {
                var x1    = Math.max(0, rx - half);
                var y1    = Math.max(0, ry - half);
                var x2    = Math.min(w-1, rx + half);
                var y2    = Math.min(h-1, ry + half);
                var count = (x2-x1+1) * (y2-y1+1);
                var sum   =
                    integral[(y2+1)*(w+1)+(x2+1)]
                    - integral[y1*(w+1)+(x2+1)]
                    - integral[(y2+1)*(w+1)+x1]
                    + integral[y1*(w+1)+x1];
                var threshold = (sum / count) - C;
                var val       = gray[ry*w+rx] < threshold ? 0 : 255;
                var idx       = (ry*w+rx)*4;
                data[idx] = data[idx+1] = data[idx+2] = val;
                data[idx+3] = 255;
            }
        }
        ctx.putImageData(imgData, 0, 0);
        return canvas;
    }

    function createZxingReaderWithHints() {
        var hints = new Map();
        hints.set(ZXing.DecodeHintType.POSSIBLE_FORMATS, ZXING_FORMATS);
        hints.set(ZXing.DecodeHintType.TRY_HARDER, true);
        var reader;
        try      { reader = new ZXing.BrowserMultiFormatReader(hints); }
        catch(e) { reader = new ZXing.BrowserMultiFormatReader(); }
        return reader;
    }

    async function tryDecodeReaderFromCanvasOrImage(codeReader, canvas) {
        if (codeReader && typeof codeReader.decodeFromCanvas === "function") {
            try {
                var result = await codeReader.decodeFromCanvas(canvas);
                return normalizeZxingResult(result);
            } catch(e) { return null; }
        }
        if (codeReader && typeof codeReader.decodeFromImageElement === "function") {
            try {
                var img    = await canvasToImageElement(canvas);
                var result = await codeReader.decodeFromImageElement(img);
                return normalizeZxingResult(result);
            } catch(e) { return null; }
        }
        if (codeReader && typeof codeReader.decodeFromImageUrl === "function") {
            try {
                var dataUrl = canvas.toDataURL("image/png");
                var result  = await codeReader.decodeFromImageUrl(dataUrl);
                return normalizeZxingResult(result);
            } catch(e) { return null; }
        }
        return null;
    }

    function normalizeZxingResult(result) {
        if (!result) return null;
        var text = result.text || (result.getText ? result.getText() : null);
        if (!text) return null;
        return { text: text, format: result.getBarcodeFormat ? result.getBarcodeFormat() : null };
    }

    function canvasToImageElement(canvas) {
        return new Promise(function(resolve, reject) {
            var img     = new Image();
            img.onload  = function() { resolve(img); };
            img.onerror = function(e) { reject(e); };
            img.src     = canvas.toDataURL("image/png");
        });
    }

    function drawBitmapToScaledCanvas(bitmap, maxLongEdge) {
        var w     = bitmap.width;
        var h     = bitmap.height;
        var scale = Math.min(maxLongEdge / Math.max(w, h), 1);
        var cw    = Math.max(1, Math.round(w * scale));
        var ch    = Math.max(1, Math.round(h * scale));
        var canvas = document.createElement("canvas");
        canvas.width  = cw;
        canvas.height = ch;
        var ctx = canvas.getContext("2d", { willReadFrequently: true });
        ctx.fillStyle = "#FFFFFF";
        ctx.fillRect(0, 0, cw, ch);
        ctx.drawImage(bitmap, 0, 0, cw, ch);
        return canvas;
    }

    function applyPreprocessFilter(srcCanvas, filterStr) {
        var canvas = document.createElement("canvas");
        canvas.width  = srcCanvas.width;
        canvas.height = srcCanvas.height;
        var ctx = canvas.getContext("2d", { willReadFrequently: true });
        if (ctx.filter !== undefined) ctx.filter = filterStr;
        ctx.drawImage(srcCanvas, 0, 0);
        if (ctx.filter !== undefined) ctx.filter = "none";
        return canvas;
    }

    function cropCenter(srcCanvas, ratio) {
        var sw = srcCanvas.width;
        var sh = srcCanvas.height;
        var cw = Math.max(1, Math.round(sw * ratio));
        var ch = Math.max(1, Math.round(sh * ratio));
        var sx = Math.max(0, Math.round((sw - cw) / 2));
        var sy = Math.max(0, Math.round((sh - ch) / 2));
        var canvas = document.createElement("canvas");
        canvas.width  = cw;
        canvas.height = ch;
        var ctx = canvas.getContext("2d", { willReadFrequently: true });
        ctx.fillStyle = "#FFFFFF";
        ctx.fillRect(0, 0, cw, ch);
        ctx.drawImage(srcCanvas, sx, sy, cw, ch, 0, 0, cw, ch);
        return canvas;
    }
}
</script>
</body>
</html>

package com.yulchon.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.yulchon.domain.Management;
import com.yulchon.service.ManagementService;

public class PrintExcel {
	@Autowired
	ManagementService managementService;
	
	private void putCellValue(Dispatch sheet, String cellAddress, Object value) {
		if (value == null) value = ""; // Null 방어

		// 'Range'를 가져올 때는 Dispatch.call을 사용해야 합니다.
		Dispatch range = Dispatch.call(sheet, "Range", cellAddress).toDispatch();

		// 값을 넣을 때는 "Value" 또는 "Value2" 속성을 설정합니다.
		Dispatch.put(range, "Value", value);
	}
	
	//고이데 KAB
	public Map<String, Object> printKoideKab(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "F4", data.getItem_count());
			putCellValue(sheet, "E5", data.getCustomer_product_code_number());
			putCellValue(sheet, "F6", data.getOut_diameter());
			putCellValue(sheet, "I6", data.getIn_daimeter());
			putCellValue(sheet, "K6", data.getLbl_real_length());
			putCellValue(sheet, "J5", data.getCd_materail());
			putCellValue(sheet, "G7", data.getExtra_packing_inspection());
			putCellValue(sheet, "L7", data.getNo_mfg_order_serial());
			putCellValue(sheet, "F8", data.getExtra_invoice_no());
			putCellValue(sheet, "L8", getTodayFormatted());
			putCellValue(sheet, "E9", data.getWgt_inventory());
			putCellValue(sheet, "K9", data.getQty_inventory());

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//고이데 KCB
	public Map<String, Object> printKoideKcb(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "H5", data.getItem_count());
			putCellValue(sheet, "E6", data.getCustomer_product_code_number());
			putCellValue(sheet, "J6", data.getCd_materail());
			putCellValue(sheet, "F7", data.getOut_diameter());
			putCellValue(sheet, "I7", data.getIn_daimeter());
			putCellValue(sheet, "K7", data.getLbl_real_length());
			putCellValue(sheet, "G8", data.getExtra_packing_inspection());
			putCellValue(sheet, "L8", data.getNo_mfg_order_serial());
			putCellValue(sheet, "F9", data.getExtra_invoice_no());
			putCellValue(sheet, "L9", getTodayFormatted());
			putCellValue(sheet, "E10", data.getWgt_inventory());
			putCellValue(sheet, "K10", data.getQty_inventory());
			

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//고이데 KKB
	public Map<String, Object> printKoideKkb(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "H5", data.getItem_count());
			putCellValue(sheet, "E6", data.getCustomer_product_code_number());
			putCellValue(sheet, "J6", data.getCd_materail());
			putCellValue(sheet, "F7", data.getOut_diameter());
			putCellValue(sheet, "I7", data.getIn_daimeter());
			putCellValue(sheet, "K7", data.getLbl_real_length());
			putCellValue(sheet, "G8", data.getExtra_packing_inspection());
			putCellValue(sheet, "L8", data.getNo_mfg_order_serial());
			putCellValue(sheet, "F9", data.getExtra_invoice_no());
			putCellValue(sheet, "L9", getTodayFormatted());
			putCellValue(sheet, "E10", data.getWgt_inventory());
			putCellValue(sheet, "K10", data.getQty_inventory());
			

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//SANKIN
	public Map<String, Object> printKoideSankin(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "F4", data.getItem_count());
			putCellValue(sheet, "E5", data.getCd_materail());
			putCellValue(sheet, "F6", data.getOut_diameter());
			putCellValue(sheet, "I6", data.getIn_daimeter());
			putCellValue(sheet, "K6", data.getLbl_real_length());
			putCellValue(sheet, "G7", data.getExtra_packing_inspection());
			putCellValue(sheet, "K7", data.getNo_mfg_order_serial());
			putCellValue(sheet, "F8", data.getExtra_order_no());
			putCellValue(sheet, "L8", getTodayFormatted());
			putCellValue(sheet, "E9", data.getWgt_inventory());
			putCellValue(sheet, "K9", data.getQty_inventory());
			

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch cellD1 = Dispatch.call(sheet, "Range", "M1").toDispatch();
			double left = Dispatch.get(cellD1, "Left").toDouble();
			double top = Dispatch.get(cellD1, "Top").toDouble();

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			// AddPicture 파라미터: 경로, LinkToFile(false), SaveWithDocument(true), x, y, width, height
			Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left+30, top-10, 90, 90);

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//KKM
	public Map<String, Object> printKkm(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B3", data.getInvoice_name());
			putCellValue(sheet, "B4", data.getExtra_part_no());
			putCellValue(sheet, "B5", data.getCd_materail());
			putCellValue(sheet, "B6", data.getOut_diameter());
			putCellValue(sheet, "E6", data.getIn_daimeter());
			putCellValue(sheet, "B7", data.getLbl_real_length());
			putCellValue(sheet, "B8", data.getQty_inventory());
			putCellValue(sheet, "B9", data.getWgt_inventory());
			putCellValue(sheet, "B10", data.getExtra_packing_inspection());
			putCellValue(sheet, "B12", data.getItem_count());	

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//KOB
	public Map<String, Object> printKob(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B3", data.getInvoice_name());
			putCellValue(sheet, "B4", data.getExtra_part_no());
			putCellValue(sheet, "B5", data.getCd_materail());
			putCellValue(sheet, "B6", data.getOut_diameter() + " OD X " + data.getIn_daimeter() + " ID");
			putCellValue(sheet, "B7", data.getLbl_real_length() + " mm");
			putCellValue(sheet, "B8", data.getQty_inventory() + " PCS");
			putCellValue(sheet, "B9", data.getWgt_inventory() + " KG");
			putCellValue(sheet, "B10", data.getExtra_packing_inspection());
			putCellValue(sheet, "B12", data.getItem_count());
			

			// [3] QR 이미지 삽입 (D1 셀 위치)
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//CASH
	public Map<String, Object> printCash(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			String rawDate = data.getLbl_date();
			String formattedDate = "";
			if (rawDate != null && rawDate.length() == 8) {
			    formattedDate = rawDate.substring(0, 4) + "-" + 
			                           rawDate.substring(4, 6) + "-" + 
			                           rawDate.substring(6, 8);
			}else {
				formattedDate = rawDate;
			}
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "C4", data.getSpec_item());
			putCellValue(sheet, "C5", "OD " + data.getOut_diameter() + " x ID " + data.getIn_daimeter()  + " x WT " +data.getThickness());
			putCellValue(sheet, "C6", data.getLbl_real_length() + "mm");
			putCellValue(sheet, "E6", data.getNo_mfg_order_serial());
			putCellValue(sheet, "C7", data.getQty_inventory());
			putCellValue(sheet, "E7", formattedDate);
			putCellValue(sheet, "C8", data.getWgt_inventory() + " kg");
			putCellValue(sheet, "E8", data.getLbl_real_length());
			putCellValue(sheet, "E10", data.getItem_count());

			// [3] QR 이미지 삽입 (D1 셀 위치)
			/*
			 * Dispatch cellD1 = Dispatch.call(sheet, "Range", "E9").toDispatch(); double
			 * left = Dispatch.get(cellD1, "Left").toDouble(); double top =
			 * Dispatch.get(cellD1, "Top").toDouble();
			 * 
			 * Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch(); // AddPicture
			 * 파라미터: 경로, LinkToFile(false), SaveWithDocument(true), x, y, width, height
			 * Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left+40, top-10,
			 * 45, 45);
			 */
			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//ELM2
	public Map<String, Object> printElm2(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";
			
			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			String rawDate = data.getLbl_date();
			String formattedDate = "";
			if (rawDate != null && rawDate.length() == 8) {
			    formattedDate = rawDate.substring(0, 4) + "-" + 
			                           rawDate.substring(4, 6) + "-" + 
			                           rawDate.substring(6, 8);
			}else {
				formattedDate = rawDate;
			}
			int realLength = Integer.parseInt(data.getLbl_real_length());
			int count = Integer.parseInt(data.getQty_inventory());
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "A4", data.getExtra_invoice_no());
			putCellValue(sheet, "A6", "*N" + data.getExtra_invoice_no() + "*");
			putCellValue(sheet, "G7", data.getWgt_inventory());
			putCellValue(sheet, "I7", data.getWgt_inventory() + 1);
			putCellValue(sheet, "K7", data.getQty_inventory());
			putCellValue(sheet, "A9", data.getExtra_part_no());
			putCellValue(sheet, "A10", "*P" + data.getExtra_part_no() + "*");
			putCellValue(sheet, "A12", realLength/1000*count);
			putCellValue(sheet, "A13", "*Q" + realLength/1000*count + "*");
			putCellValue(sheet, "G12", data.getOut_diameter() + " OD x " + data.getIn_daimeter()  + " ID " +data.getLbl_real_length() + "L");
			putCellValue(sheet, "A19", data.getItem_count());
			putCellValue(sheet, "A20", "*" + data.getItem_count() + "*");

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//KEEPRO
	public Map<String, Object> printKeepro(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B4", data.getExtra_packing_inspection());
			putCellValue(sheet, "D4", data.getInvoice_name());
			putCellValue(sheet, "B5", data.getCustomer_product_code_number());
			putCellValue(sheet, "B6", data.getCd_materail() + ": " + data.getOut_diameter() + " OD X  " + data.getIn_daimeter() + " ID");
			putCellValue(sheet, "B7", data.getLbl_real_length());
			putCellValue(sheet, "B8", data.getQty_inventory());
			putCellValue(sheet, "B9", data.getWgt_inventory());
			putCellValue(sheet, "B11", data.getItem_count());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//MBI
	public Map<String, Object> printMbi(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			int totalLength = Integer.parseInt(data.getLbl_real_length())/1000*(Integer.parseInt(data.getQty_inventory()));
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "C4", data.getCd_materail());
			putCellValue(sheet, "B6", "OD " + data.getOut_diameter() + " x ID " + data.getIn_daimeter() + " x WT " + data.getThickness());
			putCellValue(sheet, "E5", data.getExtra_part_no());
			putCellValue(sheet, "C6", data.getLbl_real_length() + " mm");
			putCellValue(sheet, "E6", data.getNo_mfg_order_serial());
			putCellValue(sheet, "C7", data.getQty_inventory());
			putCellValue(sheet, "E7", getTodayDate());
			putCellValue(sheet, "C8", data.getWgt_inventory());
			putCellValue(sheet, "E8", totalLength + " m");
			putCellValue(sheet, "E10", data.getItem_count());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//MMP
	public Map<String, Object> printMmp(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			System.out.println("item_seq_total: " + data.getItem_seq_total());
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "A3", "S/O NO. " + data.getCustomer_product_code_number());
			putCellValue(sheet, "B4", data.getCd_materail());
			putCellValue(sheet, "B5", data.getOut_diameter() + "OD X " + data.getIn_daimeter() + " ID X " + data.getLbl_real_length() + " L");
			putCellValue(sheet, "B7", data.getItem_seq_total()); //<- 여기에 같은 품목 개수 조회해서 넣어야 함(1/30)
			putCellValue(sheet, "A9", "NET WEIGHT : " + data.getWgt_inventory() + " KG / PCS : " + data.getQty_inventory() + "PCS");

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//NOK
	public Map<String, Object> printNok(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			int totalLength = Integer.parseInt(data.getLbl_real_length())/1000*(Integer.parseInt(data.getQty_inventory()));
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B3", data.getExtra_invoice_no());
			putCellValue(sheet, "B4", data.getCd_materail());
			putCellValue(sheet, "B5", data.getOut_diameter() + "OD X " + data.getIn_daimeter() + " ID");
			putCellValue(sheet, "B6", data.getLbl_real_length() + " L");
			putCellValue(sheet, "B7", data.getQty_inventory() + " PCS");
			putCellValue(sheet, "B8", totalLength + " MTR");
			putCellValue(sheet, "B9", data.getWgt_inventory() + " KG");
			putCellValue(sheet, "B12", data.getItem_count());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//NST
	public Map<String, Object> printNst(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();

			String[] part = data.getCd_materail().split(" ");
			String material1 = part[2];
			String material2 = part[0] + " " + part[1];
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B3", data.getExtra_part_no());
			putCellValue(sheet, "B4", data.getItem_count());
			putCellValue(sheet, "B7", material1);
			putCellValue(sheet, "B8", material2);
			putCellValue(sheet, "B9", data.getOut_diameter() + " OD X " + data.getIn_daimeter() + " ID X " + data.getLbl_real_length() + "mm");
			putCellValue(sheet, "B10", data.getWgt_inventory() + " KG");
			putCellValue(sheet, "B11", data.getQty_inventory() + " PCS");
			putCellValue(sheet, "B2", data.getExtra_invoice_no());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//PROFENDER
	public Map<String, Object> printProfender(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "B9", data.getItem_count());
			putCellValue(sheet, "D4", "P/O No.: " + data.getCustomer_product_code_number());
			putCellValue(sheet, "E6", data.getWgt_inventory() + " KG");
			putCellValue(sheet, "E8", data.getQty_inventory());
			putCellValue(sheet, "E10", data.getExtra_invoice_no());
			putCellValue(sheet, "E12", data.getItem_count());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//DKK
	public Map<String, Object> printDkk(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "H4", data.getItem_count());
			putCellValue(sheet, "E5", data.getCustomer_product_code_number());
			putCellValue(sheet, "L5", data.getCd_materail());
			putCellValue(sheet, "F6", data.getOut_diameter());
			putCellValue(sheet, "I6", data.getIn_daimeter());
			putCellValue(sheet, "K6", data.getLbl_real_length());
			putCellValue(sheet, "M6", "(LOT:" + data.getNo_mfg_order_serial() + ")");
			putCellValue(sheet, "G7", data.getExtra_packing_inspection());
			putCellValue(sheet, "F8", data.getExtra_invoice_no());
			putCellValue(sheet, "L8", getTodayFormattedDkk());
			putCellValue(sheet, "E9", data.getWgt_inventory());
			putCellValue(sheet, "K9", data.getQty_inventory());

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//KTH
	public Map<String, Object> printKth(Management data, String file_path) {
		Map<String, Object> resultMap = new HashMap<>();
		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;

		//QR 임시 저장 경로
		String qrTempPath = "D:\\\\율촌_쉬핑마크_양식\\\\QR임시저장경로\\\\qr_temp.png"; 
		
		try {
			// QR 이미지 생성 부분
			String qrContent = data.getLbl_lot_no(); 
			if (qrContent == null || qrContent.isEmpty()) qrContent = "No Data";

			//여백 줄이기
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.MARGIN, 0); // 여백을 0으로 설정 (기본값은 보통 4)
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 한글 깨짐 방지용 (선택사항)

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
			Path path = FileSystems.getDefault().getPath(qrTempPath);
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);


			// 엑셀 실행
			excel = new ActiveXComponent("Excel.Application");

			excel.setProperty("Visible", false);
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.call(workbooks, "Open", file_path).toDispatch();

			// 시트 가져오기
			Dispatch worksheets = Dispatch.get(workbook, "Worksheets").toDispatch();
			sheet = Dispatch.call(worksheets, "Item", new Variant(1)).toDispatch();
			
			// [2] 값 넣기 (Null 방어 로직 추가)
			putCellValue(sheet, "H4", data.getItem_count());
			putCellValue(sheet, "E5", data.getCustomer_product_code_number());
			putCellValue(sheet, "J5", data.getCd_materail());
			putCellValue(sheet, "F6", data.getOut_diameter());
			putCellValue(sheet, "I6", data.getIn_daimeter());
			putCellValue(sheet, "K6", data.getLbl_real_length());
			putCellValue(sheet, "G7", data.getExtra_packing_inspection());
			putCellValue(sheet, "F8", data.getExtra_invoice_no());
			putCellValue(sheet, "L8", getTodayFormattedDkk());
			putCellValue(sheet, "E9", data.getWgt_inventory());
			putCellValue(sheet, "K9", data.getQty_inventory());
			putCellValue(sheet, "K7", data.getRemarks()); //비고

			Dispatch shapes = Dispatch.get(sheet, "Shapes").toDispatch();
			int shapeCount = Dispatch.get(shapes, "Count").toInt();
			Dispatch qrHolder = null;

			// [2] 이름이 "QR_HOLDER"인 도형 찾기
			for (int i = 1; i <= shapeCount; i++) {
			    Dispatch shape = Dispatch.call(shapes, "Item", new Variant(i)).toDispatch();
			    String shapeName = Dispatch.get(shape, "Name").toString();
			    
			    if ("QR_HOLDER".equals(shapeName)) {
			        qrHolder = shape;
			        break;
			    }
			}

			if (qrHolder != null) {
			    // [3] 찾은 도형의 위치와 크기 정보를 그대로 가져옴
			    double left = Dispatch.get(qrHolder, "Left").toDouble();
			    double top = Dispatch.get(qrHolder, "Top").toDouble();
			    double width = Dispatch.get(qrHolder, "Width").toDouble();
			    double height = Dispatch.get(qrHolder, "Height").toDouble();

			    // [4] 그 위치 그대로 QR 이미지 삽입 (좌표 계산 필요 없음!)
			    Dispatch.call(shapes, "AddPicture", qrTempPath, false, true, left, top, width, height);
			    
			    // (선택) 원본 홀더 도형은 삭제하거나 보이지 않게 처리
			    // Dispatch.call(qrHolder, "Delete");
			} else {
			    System.err.println("엑셀 양식에 'QR_HOLDER' 이름의 도형이 없습니다!");
			}

			// 인쇄
			Dispatch.call(workbook, "PrintOut");

			//결과만 반환하고 서비스는 컨트롤러에서
			resultMap.put("result", true);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "인쇄 중 오류가 발생했습니다. " + e.getMessage());
		}finally {
			try {
				if (workbook != null) {
					Dispatch.call(workbook, "Close", false);
				}
			} catch (Exception ignore) {
			}
			try {
				if (excel != null) {
					excel.invoke("Quit");
				}
			} catch (Exception ignore) {
			}
		}
		return resultMap;
	}
	
	//고이데 양식에서 오늘 날짜(OCT.16,2025 형식)
	public static String getTodayFormatted() {
        // 1. 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 2. 패턴 설정 (MMM: 월 약어, dd: 일, yyyy: 년도)
        // Locale.US를 설정해야 월 이름이 영어(OCT)로 나옵니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM.dd,yyyy", Locale.US);

        // 3. 포맷 적용 및 대문자 변환 (.toUpperCase())
        return today.format(formatter).toUpperCase();
    }
	
	//MBI 오늘 날짜(2026-02-09 형식)
	public static String getTodayDate() {
        // 1. 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        
        // 2. 포맷 지정하기 (yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 3. 포맷에 맞춰 문자열로 반환
        return now.format(formatter);
    }
	
	//DKK 양식에서 오늘 날짜(OCT.16.2025 이 형식)
	public static String getTodayFormattedDkk() {
        // 1. 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 2. 패턴 설정 (MMM: 월 약어, dd: 일, yyyy: 년도)
        // Locale.US를 설정해야 월 이름이 영어(OCT)로 나옵니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM.dd.yyyy", Locale.US);

        // 3. 포맷 적용 및 대문자 변환 (.toUpperCase())
        return today.format(formatter).toUpperCase();
    }
}

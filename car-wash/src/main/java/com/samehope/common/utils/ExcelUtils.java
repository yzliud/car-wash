package com.samehope.common.utils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.log.Log;

/**
 * Excel工具类
 */
public class ExcelUtils {

	private static final Log log = Log.getLog(ExcelUtils.class);

	/**
	 * 
	 * @param fileName 文件名
	 * @param sheets sheet
	 * @param headers 表头
	 * @param datas 数据
	 * @param response 输出
	 */
	public static void ExpExs(String fileName, String sheets, List<String> headers, List<Map<String,String>> datas, HttpServletResponse response) {
		try {
			if (StringUtils.isBlank(sheets)) {
				sheets = "sheet";
			}

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(sheets); // +workbook.getNumberOfSheets()

			HSSFRow row;
			HSSFCell cell;

			// 设置这些样式
			HSSFFont font = workbook.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);// 字体
			font.setFontHeightInPoints((short) 12);// 字号
			//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			// font.setColor(HSSFColor.BLUE.index);//颜色

			HSSFCellStyle cellStyle = workbook.createCellStyle(); // 设置单元格样式
			//cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setFont(font);

			// 产生表格标题行
			row = sheet.createRow(0);
			row.setHeightInPoints(20);
			for (int i = 0; i < headers.size(); i++) {
				HSSFRichTextString text = new HSSFRichTextString(headers.get(i).toString());
				cell = row.createCell(i);
				cell.setCellValue(text);
				cell.setCellStyle(cellStyle);
			}

			cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setDataFormat((short) 0x31);// 设置显示格式，避免点击后变成科学计数法了
			// cellStyle.setWrapText(true);//设置自动换行
			Map<String,String> map;
			// 遍历集合数据，产生数据行
			for (int i = 0; i < datas.size(); i++) {
				row = sheet.createRow((i + 1));
				row.setHeightInPoints(20);
				map = datas.get(i);

				Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
				int j = 0;
				while (iterator.hasNext()) {  
		            Map.Entry<String, String> entry = iterator.next();  
		            cell = row.createCell(j);
					cell.setCellStyle(cellStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					if (!StringUtils.isBlank(entry.getValue())) {
						cell.setCellValue(new HSSFRichTextString(entry.getValue()));
					} else {
						cell.setCellValue(new HSSFRichTextString(""));
					} 
					j++;
		        }  
			}

			for (int i = 0; i < headers.size(); i++) {
				sheet.setColumnWidth(i,4500);
			}

			response.reset();
			response.setContentType("application/vnd.ms-excel; charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
			response.setCharacterEncoding("utf-8");
			// 文件流输出到rs里
			workbook.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			System.out.println("#Error [" + e.getMessage() + "] ");
		}
		log.info("[" + sheets + "] 创建成功...");
	}
}
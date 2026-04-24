package com.digitalpark.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel 导出工具类
 *
 * @author digitalpark
 */
public class ExcelUtil {

    private ExcelUtil() {
    }

    /**
     * 导出 Excel 文件
     *
     * @param response  HTTP 响应对象
     * @param fileName  文件名（支持中文）
     * @param sheetName 工作表名称
     * @param headers   表头列表
     * @param dataList  数据列表（每行为一个 List<Object>）
     */
    public static void export(HttpServletResponse response, String fileName, String sheetName,
                              List<String> headers, List<List<Object>> dataList) throws IOException {
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);

            // 创建数据样式
            CellStyle dataStyle = createDataStyle(workbook);

            // 写入表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // 写入数据
            if (dataList != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    List<Object> rowData = dataList.get(i);
                    if (rowData != null) {
                        for (int j = 0; j < rowData.size(); j++) {
                            Cell cell = row.createCell(j);
                            Object value = rowData.get(j);
                            setCellValue(cell, value);
                            cell.setCellStyle(dataStyle);
                        }
                    }
                }
            }

            // 自动调整列宽
            autoSizeColumns(sheet, headers.size());

            // 设置响应头
            setResponseHeaders(response, fileName);

            // 写入响应流
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 创建表头样式（加粗、背景色）
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 设置背景色（浅蓝色）
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 设置居中对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体（加粗）
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Microsoft YaHei");
        style.setFont(font);

        return style;
    }

    /**
     * 创建数据单元格样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Microsoft YaHei");
        style.setFont(font);

        return style;
    }

    /**
     * 设置单元格值，根据值类型自动选择
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.time.LocalDateTime) {
            cell.setCellValue(((java.time.LocalDateTime) value).toString());
        } else if (value instanceof java.time.LocalDate) {
            cell.setCellValue(((java.time.LocalDate) value).toString());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 自动调整列宽（设置最大宽度限制）
     */
    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // 限制最大列宽为50个字符
            int columnWidth = sheet.getColumnWidth(i);
            int maxWidth = 50 * 256;
            if (columnWidth > maxWidth) {
                sheet.setColumnWidth(i, maxWidth);
            }
            // 设置最小列宽
            int minWidth = 10 * 256;
            if (columnWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            }
        }
    }

    /**
     * 设置 HTTP 响应头，支持中文文件名下载
     */
    private static void setResponseHeaders(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        // URL 编码文件名以支持中文
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + encodedFileName + ";filename*=UTF-8''" + encodedFileName);

        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
    }
}

package com.kunbu.common.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 基于 Apache poi 的excel工具类
 *
 * @author scosyf
 * @time 2019年5月9日
 *
 * 2019.8.22 新增
 *
 * 2019.11.18 新增模板导出
 */
public class ExcelExportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtil.class);

    private static final int DEFAULT_TEMPLATE_ROW = 30;

    /**
     * 导出excel模板（实时生成，也可以预先放在服务器上）
     *
     * @param headers
     * @param out
     * @author kunbu
     * @time 2019/11/8 11:07
     * @return
     **/
    public static boolean exportExcelTemplate(List<String> headers, OutputStream out) {
        try {
            // 默认用xlsx的方式导出
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("sheet");
            sheet.setDefaultColumnWidth(30);
            // 设置单元格为文本格式（@==TEXT）
            CellStyle style = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            style.setDataFormat(format.getFormat("@"));
            style.setAlignment(CellStyle.ALIGN_LEFT);
            // 先设置头行
            setHeader(sheet, headers, style);
            // 在头行之后的行设置单元格为文本（默认填充30行，在导入时需要对每行做空检查，因为此时的行是有数据的，即空字符串）
            int headerSize = headers.size();
            for (int column = 1; column <= DEFAULT_TEMPLATE_ROW; column++) {
                //从第二行开始
                Row row = sheet.createRow(column);
                for (int i = 0; i < headerSize; i++) {
                    Cell cell = row.createCell(i);
                    //style和type需要同时设置才能生效
                    cell.setCellStyle(style);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
            }
            try {
                workbook.write(out);
                out.flush();
                out.close();
                return true;
            } catch (IOException e) {
                LOGGER.error(">>> exportExcelTemplate, 资源处理异常", e);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(">>> exportExcelTemplate, 导出模板失败", e);
            return false;
        }
    }

    /**
     * 大数据导出,不能使用普通的导出,会OOM（使用SXSSF速度更快）
     *
     * https://blog.csdn.net/daiyutage/article/details/53010491
     * http://poi.apache.org/spreadsheet/how-to.html#sxssf
     *
     * @param headers
     * @param keys
     * @param dataList
     * @param out
     * @return
     * @author kunbu
     * @time 2019/8/22 11:08
     **/
    public static boolean exportExcelSimpleBigData(List<String> headers, List<String> keys, List<Map<String, Object>> dataList, OutputStream out) {
        try {
            // keep 100 rows in memory, exceeding rows will be flushed to disk
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            //单工作簿
            Sheet sheet = workbook.createSheet("sheet");
            //设置样式
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);
            //设置默认width
            sheet.setDefaultColumnWidth(30);
            //设置头行
            setHeader(sheet, headers, style);
            //填充数据
            setData(sheet, 1, style, keys, dataList);
            try {
                workbook.write(out);
                out.flush();
                out.close();
                // dispose of temporary files backing this workbook on disk
                workbook.dispose();
                return true;
            } catch (IOException e) {
                LOGGER.error(">>> exportExcelSimpleBigData, 资源处理异常", e);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(">>> exportExcelSimpleBigData, 导出失败", e);
            return false;
        }

    }

    /**
     * 简单导出excel 2003
     *
     * @param headers   标头行
     * @param keys      标头key
     * @param dataList   数据：Map<标头key, 数据value>
     * @param out       输出流
     * @return
     */
    public static boolean exportExcelSimple2003(List<String> headers, List<String> keys, List<Map<String, Object>> dataList, OutputStream out) {
        Workbook workbook = new HSSFWorkbook();
        return exportExcelSimple(workbook, headers, keys, dataList, out);
    }

    /**
     * 简单导出excel 2007+
     *
     * @param headers   标头行
     * @param keys      标头key
     * @param dataList   数据：Map<标头key, 数据value>
     * @param out     输出流
     * @return
     */
    public static boolean exportExcelSimple2007(List<String> headers, List<String> keys, List<Map<String, Object>> dataList, OutputStream out) {
        Workbook workbook = new XSSFWorkbook();
        return exportExcelSimple(workbook, headers, keys, dataList, out);
    }

    /**
     * 简单导出excel(只有一个sheet)
     *
     * @param workbook
     * @param headers
     * @param dataList
     * @param out
     * @return
     */
    private static boolean exportExcelSimple(Workbook workbook, List<String> headers, List<String> keys, List<Map<String, Object>> dataList, OutputStream out) {
        try {
            //单工作簿
            Sheet sheet = workbook.createSheet("sheet");
            //设置样式
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);
            //设置默认width
            sheet.setDefaultColumnWidth(20);
            //设置头行
            setHeader(sheet, headers, style);
            //填充数据
            setData(sheet, 1, style, keys, dataList);
            try {
                workbook.write(out);
                out.flush();
                out.close();
                return true;
            } catch (IOException e) {
                LOGGER.error(">>> exportExcelSimple, 资源处理异常", e);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(">>> exportExcelSimple, 导出失败", e);
            return false;
        }
    }

    /**
     * 设置头行
     *
     * @param sheet
     * @param headers
     * @param style
     */
    private static void setHeader(Sheet sheet, List<String> headers, CellStyle style) {
        int headerSize = headers.size();
        Row header = sheet.createRow(0);
        for (int i = 0; i < headerSize; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers.get(i));
            if (style != null) {
                headerCell.setCellStyle(style);
            }
        }
    }

    /**
     * 填充数据
     *
     * @param sheet
     * @param rowNum 数据起始行
     * @param style
     * @param keys
     * @param dataList
     */
    private static void setData(Sheet sheet, int rowNum, CellStyle style, List<String> keys, List<Map<String, Object>> dataList) {
        int headerSize = keys.size();
        for (Map<String, Object> dataMap : dataList) {
            Row data = sheet.createRow(rowNum);
            for (int colume = 0; colume < headerSize; colume++) {
                Cell dataCell = data.createCell(colume);
                String key = keys.get(colume);
                setCellValue(dataCell, dataMap.get(key));
                if (style != null) {
                    dataCell.setCellStyle(style);
                }
            }
            rowNum++;
        }
    }

    /**
     * 设置value
     *
     * @param cell
     * @param obj
     **/
    private static void setCellValue(Cell cell, Object obj) {
        if (obj != null) {
            cell.setCellValue(obj.toString());
        } else {
            cell.setCellValue("");
        }
    }


}

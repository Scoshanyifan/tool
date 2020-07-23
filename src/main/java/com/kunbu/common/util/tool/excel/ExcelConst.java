package com.kunbu.common.util.tool.excel;

/**
 * @author: KunBu
 * @time: 2019/8/22 10:46
 * @description:
 */
public interface ExcelConst {

    /**
     * 2003 最大行数65536，最大列数256
     */
    String EXCEL_XLS_2003 = "xls";
    /**
     * 2007+ 最大行1048576，最大列16384
     */
    String EXCEL_XLSX_2007 = "xlsx";

    String FILE_TYPE_HTTP = "http";


    String EXCEL_TEMPLATE_FILE_NAME = "订单导入模板.xlsx";

    /** 实时生成模板的文本单元格行数 */
    int DEFAULT_TEMPLATE_STRING_CELL_ROW = 30;


    String EXCEL_EXPORT_FILE_NAME_PREFIX = "数据导出列表";
}

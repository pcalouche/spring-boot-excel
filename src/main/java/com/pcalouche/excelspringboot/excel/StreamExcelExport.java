package com.pcalouche.excelspringboot.excel;

public class StreamExcelExport extends ExcelExport {

    public StreamExcelExport(int columns, int rows) {
        super(true);
        build(columns, rows);
    }

    private void build(int columns, int rows) {
        addDataToWorkbook(getSXSSFWorkbook(), columns, rows);
        addRichTextCell(getSXSSFWorkbook());
    }

    @Override
    public String getFilename() {
        return "streaming.xlsx";
    }
}

package dev.pcalouche.springboot.excel.excel;

import dev.pcalouche.springboot.excel.util.DownloadableFile;

public class NonStreamingExcelExport extends ExcelExport implements DownloadableFile {

	public NonStreamingExcelExport(int columns, int rows) {
		super(false);
		build(columns, rows);
	}

	private void build(int columns, int rows) {
		addDataToWorkbook(getXSSFWorkbook(), columns, rows);
		addRichTextCell(getXSSFWorkbook());
	}

	@Override
	public String getFilename() {
		return "non-streaming.xlsx";
	}

}
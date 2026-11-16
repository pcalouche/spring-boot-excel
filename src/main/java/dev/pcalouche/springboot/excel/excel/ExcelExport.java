package dev.pcalouche.springboot.excel.excel;

import dev.pcalouche.springboot.excel.util.DownloadableFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.DefaultTempFileCreationStrategy;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;

@Slf4j
public abstract class ExcelExport implements DownloadableFile {

	private static final Path EXCEL_TEMP_FILE_PATH;

	// In the static block of this class, set the Apache POI temp files path and delete an
	// old files that may have been there.
	// Because this is a static block it should only run once when any instance of
	// ExcelExport is created
	static {
		EXCEL_TEMP_FILE_PATH = Paths.get(System.getProperty("user.dir")).resolve("excelStaging");
		log.info("Excel temp file staging path set to->{}", EXCEL_TEMP_FILE_PATH.toAbsolutePath());
		if (!Files.exists(EXCEL_TEMP_FILE_PATH)) {
			try {
				Files.createDirectories(EXCEL_TEMP_FILE_PATH);
			}
			catch (IOException e) {
				log.error("Could not create temp page", e);
			}
		}
		TempFile.setTempFileCreationStrategy(new DefaultTempFileCreationStrategy(EXCEL_TEMP_FILE_PATH.toFile()));
		// Deleting an old files at startup
		try (Stream<Path> streamPath = Files.list(EXCEL_TEMP_FILE_PATH)) {
			streamPath.forEach(path -> {
				log.info("Deleting old Excel temp file->{}", path.toAbsolutePath());
				try {
					Files.deleteIfExists(path);
				}
				catch (IOException e) {
					log.error("Exception occurred during Excel temp file deletion", e);
				}
			});
		}
		catch (IOException e) {
			log.error("Exception occurred during listing of Excel temp files", e);
		}
	}

	protected final Font boldFont;

	protected final XSSFCellStyle boldStyle;

	private final Workbook workbook;

	public ExcelExport(boolean useStreaming) {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		boldFont = xssfWorkbook.createFont();
		boldFont.setBold(true);
		boldStyle = xssfWorkbook.createCellStyle();
		boldStyle.setFont(boldFont);

		// If streaming is true use the streaming version of Apache POI.
		if (useStreaming) {
			workbook = new SXSSFWorkbook(xssfWorkbook, 100, false, true);
		}
		else {
			workbook = xssfWorkbook;
		}
	}

	@Override
	public byte[] getBytes() {
		Long startTime = System.currentTimeMillis();
		log.info("start getBytes for Excel");
		byte[] bytes;
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			workbook.write(byteArrayOutputStream);
			bytes = byteArrayOutputStream.toByteArray();
			// If streaming Apache POI was used then dispose of temp files
			if (workbook instanceof SXSSFWorkbook) {
				workbook.close();
			}
		}
		catch (IOException e) {
			log.error("Failed to convert Excel to byte array", e);
			throw new RuntimeException("Failed to convert Excel to byte array: " + e.getMessage());
		}
		Long endTime = System.currentTimeMillis();
		log.info("end getBytes for Excel->{} seconds.", (endTime - startTime) / 1000);
		return bytes;
	}

	@Override
	public MediaType getContentType() {
		return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	protected XSSFWorkbook getXSSFWorkbook() {
		return (XSSFWorkbook) workbook;
	}

	protected SXSSFWorkbook getSXSSFWorkbook() {
		return (SXSSFWorkbook) workbook;
	}

	protected void addDataToWorkbook(Workbook workbook, int columns, int rows) {
		Long sheetStartTime = System.currentTimeMillis();
		log.info("start sheet generation");
		Sheet dataSheet = workbook.createSheet("Data");
		for (int i = 0; i < rows; i++) {
			Row row = dataSheet.createRow(i);
			for (int j = 0; j < columns; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(new CellReference(cell).formatAsString());
			}
			// Uncomment to see status information every few rows
			// if (i >= 10 && i % 10 == 0) {
			// log.info("done with row->" + i);
			// }
		}
		Long endSheetTime = System.currentTimeMillis();
		log.info("Total sheet generation time {} seconds.", (endSheetTime - sheetStartTime) / 1000);
	}

	protected void addRichTextCell(Workbook workbook) {
		Sheet richTextSheet = workbook.createSheet("Rich Text Sheet");
		XSSFRichTextString richTextString = new XSSFRichTextString(
				"Label: The label section should be bold.  This doesn't work when Streaming Excel is used though.");
		richTextString.applyFont(0, "Label:".length(), boldFont);
		Cell cell = richTextSheet.createRow(0).createCell(0);
		cell.setCellValue(richTextString);
	}

}
package com.pcalouche.excelspringboot.excel;

import com.pcalouche.excelspringboot.util.DownloadableFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.DefaultTempFileCreationStrategy;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public abstract class ExcelExport implements DownloadableFile {
    // Currently Apache POI doesn't use the new Java Time for its methods, so falling back to old Java classes
    private static final Logger logger = LoggerFactory.getLogger(ExcelExport.class);
    private static final Path EXCEL_TEMP_FILE_PATH;
    protected final Font boldFont;
    protected final XSSFCellStyle boldStyle;

    // In the static block of this class, set the Apache POI temp files path and delete an old files that may have been there.
    // Because this is a static block it should only run once when any instance of ExcelExport is created
    static {
        EXCEL_TEMP_FILE_PATH = Paths.get(System.getProperty("user.dir")).resolve("excelStaging");
        logger.info("Excel temp file staging path set to->" + EXCEL_TEMP_FILE_PATH.toAbsolutePath());
        if (!Files.exists(EXCEL_TEMP_FILE_PATH)) {
            try {
                Files.createDirectories(EXCEL_TEMP_FILE_PATH);
            } catch (IOException e) {
                logger.error("Could not create temp page", e);
            }
        }
        TempFile.setTempFileCreationStrategy(new DefaultTempFileCreationStrategy(EXCEL_TEMP_FILE_PATH.toFile()));
        // Deleting an old files at startup
        try {
            for (Path path : Files.list(EXCEL_TEMP_FILE_PATH).collect(Collectors.toList())) {
                logger.info("Deleting old Excel temp file->" + path.toAbsolutePath());
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            logger.error("Exception occurred during Excel temp file deletion", e);
        }
    }

    private Workbook workbook;

    public ExcelExport(boolean useStreaming) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        boldFont = xssfWorkbook.createFont();
        boldFont.setBold(true);
        boldStyle = xssfWorkbook.createCellStyle();
        boldStyle.setFont(boldFont);

        // If streaming is true use the streaming version of Apache Poi.
        if (useStreaming) {
            workbook = new SXSSFWorkbook(xssfWorkbook, 100);
        } else {
            workbook = xssfWorkbook;
        }
    }

    @Override
    public byte[] getBytes() {
        Long startTime = System.currentTimeMillis();
        logger.info("start getBytes for Excel");
        byte[] bytes;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            // If streaming Apache Poi was used then dispose of temp files
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (IOException e) {
            logger.error("Failed to convert Excel to byte array", e);
            throw new RuntimeException("Failed to convert Excel to byte array: " + e.getMessage());
        }
        Long endTime = System.currentTimeMillis();
        logger.info("end getBytes for Excel->" + (endTime - startTime) / 1000 + " seconds.");
        return bytes;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    protected XSSFWorkbook getXSSFWorkbook() {
        return (XSSFWorkbook) workbook;
    }

    protected SXSSFWorkbook getSXSSFWorkbook() {
        return (SXSSFWorkbook) workbook;
    }

    protected void addDataToWorkbook(Workbook workbook, int columns, int rows) {
        Long sheetStartTime = System.currentTimeMillis();
        logger.info("start sheet generation");
        Sheet dataSheet = workbook.createSheet("Data");
        for (int i = 0; i < rows; i++) {
            Row row = dataSheet.createRow(i);
            for (int j = 0; j < columns; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(new CellReference(cell).formatAsString());
            }
            // Uncomment to see status information every few rows
            //            if (i >= 10 && i % 10 == 0) {
            //                logger.info("done with row->" + i);
            //            }
        }
        Long endSheetTime = System.currentTimeMillis();
        logger.info("Total sheet generation time " + (endSheetTime - sheetStartTime) / 1000 + " seconds.");
    }

    protected void addRichTextCell(Workbook workbook) {
        Sheet richTextSheet = workbook.createSheet("Rich Text Sheet");
        XSSFRichTextString richTextString = new XSSFRichTextString("Label: The label section should be bold.  This doesn't work when Streaming Excel is used though.");
        richTextString.applyFont(0, "Label:".length(), boldFont);
        Cell cell = richTextSheet.createRow(0).createCell(0);
        cell.setCellValue(richTextString);
    }
}
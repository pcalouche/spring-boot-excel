package com.pcalouche.excelspringboot.controller;

import com.pcalouche.excelspringboot.excel.NonStreamingExcelExport;
import com.pcalouche.excelspringboot.excel.StreamExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @GetMapping("non-streaming-excel")
    public ResponseEntity<byte[]> nonStreamingExcel(@RequestParam int columns, @RequestParam int rows, HttpServletResponse response) throws IOException {
        logger.info("columns->" + columns + " rows->" + rows);

        Long startTime = System.currentTimeMillis();
        logger.info("start of Non Streaming Excel request");

        NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows);

        Long endTime = System.currentTimeMillis();
        logger.info("end of Non Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");

        return nonStreamingExcelExport.getResponseEntity();
    }

    @GetMapping("streaming-excel")
    public ResponseEntity<byte[]> streamingExcel(@RequestParam int columns, @RequestParam int rows, HttpServletResponse response) throws IOException {
        logger.info("columns->" + columns + " rows->" + rows);

        Long startTime = System.currentTimeMillis();
        logger.info("start of Streaming Excel request");

        StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows);

        Long endTime = System.currentTimeMillis();
        logger.info("end of Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
        return streamExcelExport.getResponseEntity();
    }
}

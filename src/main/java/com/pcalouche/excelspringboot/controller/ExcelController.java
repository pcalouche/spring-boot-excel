package com.pcalouche.excelspringboot.controller;

import com.pcalouche.excelspringboot.excel.NonStreamingExcelExport;
import com.pcalouche.excelspringboot.excel.StreamExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @GetMapping("non-streaming-excel")
    public void nonStreamingExcel(@RequestParam int columns, @RequestParam int rows, HttpServletResponse response) throws IOException {
        logger.info("columns->" + columns + " rows->" + rows);

        Long startTime = System.currentTimeMillis();
        logger.info("start of Non Streaming Excel request");

        NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows);
        nonStreamingExcelExport.writeToHttpResponse(response);

        Long endTime = System.currentTimeMillis();
        logger.info("end of Non Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
    }

    @GetMapping("streaming-excel")
    public void streamingExcel(@RequestParam int columns, @RequestParam int rows, HttpServletResponse response) throws IOException {
        logger.info("columns->" + columns + " rows->" + rows);

        Long startTime = System.currentTimeMillis();
        logger.info("start of Streaming Excel request");

        StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows);
        streamExcelExport.writeToHttpResponse(response);

        Long endTime = System.currentTimeMillis();
        logger.info("end of Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
    }
}

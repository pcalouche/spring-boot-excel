package dev.pcalouche.springboot.excel.controller;

import dev.pcalouche.springboot.excel.excel.NonStreamingExcelExport;
import dev.pcalouche.springboot.excel.excel.StreamExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
public class ExcelController {

	private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

	@GetMapping("non-streaming-excel")
	public ResponseEntity<byte[]> nonStreamingExcel(@RequestParam int columns, @RequestParam int rows) {
		logger.info("columns->{} rows->{}", columns, rows);

		Long startTime = System.currentTimeMillis();
		logger.info("start of Non Streaming Excel request");

		NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows);

		Long endTime = System.currentTimeMillis();
		logger.info("end of Non Streaming Excel request->{} seconds.", (endTime - startTime) / 1000);

		return nonStreamingExcelExport.getResponseEntity();
	}

	@GetMapping("streaming-excel")
	public ResponseEntity<byte[]> streamingExcel(@RequestParam int columns, @RequestParam int rows) {
		logger.info("columns->{} rows->{}", columns, rows);

		Long startTime = System.currentTimeMillis();
		logger.info("start of Streaming Excel request");

		StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows);

		Long endTime = System.currentTimeMillis();
		logger.info("end of Streaming Excel request->{} seconds.", (endTime - startTime) / 1000);
		return streamExcelExport.getResponseEntity();
	}

}

package dev.pcalouche.springboot.excel.controller;

import dev.pcalouche.springboot.excel.excel.NonStreamingExcelExport;
import dev.pcalouche.springboot.excel.excel.StreamExcelExport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {

	@GetMapping("non-streaming-excel")
	public ResponseEntity<byte[]> nonStreamingExcel(@RequestParam int columns, @RequestParam int rows) {
		log.info("columns->{} rows->{}", columns, rows);

		Long startTime = System.currentTimeMillis();
		log.info("start of Non Streaming Excel request");

		NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows);

		Long endTime = System.currentTimeMillis();
		log.info("end of Non Streaming Excel request->{} seconds.", (endTime - startTime) / 1000);

		return nonStreamingExcelExport.getResponseEntity();
	}

	@GetMapping("streaming-excel")
	public ResponseEntity<byte[]> streamingExcel(@RequestParam int columns, @RequestParam int rows) {
		log.info("columns->{} rows->{}", columns, rows);

		Long startTime = System.currentTimeMillis();
		log.info("start of Streaming Excel request");

		StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows);

		Long endTime = System.currentTimeMillis();
		log.info("end of Streaming Excel request->{} seconds.", (endTime - startTime) / 1000);
		return streamExcelExport.getResponseEntity();
	}

}

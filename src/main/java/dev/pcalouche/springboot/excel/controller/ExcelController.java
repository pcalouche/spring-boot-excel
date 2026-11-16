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
		logInput(columns, rows);

		long startTime = System.currentTimeMillis();
		log.info("Starting non-streaming Excel request");

		NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows);

		long endTime = System.currentTimeMillis();
		logTotalTime("non-streaming", startTime, endTime);

		return nonStreamingExcelExport.getResponseEntity();
	}

	@GetMapping("streaming-excel")
	public ResponseEntity<byte[]> streamingExcel(@RequestParam int columns, @RequestParam int rows) {
		logInput(columns, rows);

		long startTime = System.currentTimeMillis();
		log.info("Starting streaming Excel request");

		StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows);

		long endTime = System.currentTimeMillis();
		logTotalTime("streaming", startTime, endTime);
		return streamExcelExport.getResponseEntity();
	}

	private void logInput(int columns, int rows) {
		log.info("columns->{} rows->{}", columns, rows);
	}

	private void logTotalTime(String type, long startTimeInMillis, long endTimeInMillis) {
		log.info("Completed {} Excel request in {} seconds", type, (endTimeInMillis - startTimeInMillis) / 1000);
	}

}

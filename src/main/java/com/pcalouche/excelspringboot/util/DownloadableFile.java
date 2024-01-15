package com.pcalouche.excelspringboot.util;

import org.springframework.http.*;

public interface DownloadableFile {

	byte[] getBytes();

	MediaType getContentType();

	String getFilename();

	default ResponseEntity<byte[]> getResponseEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(getContentType());
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(getFilename()).build());
		return new ResponseEntity<>(getBytes(), headers, HttpStatus.OK);
	}

}

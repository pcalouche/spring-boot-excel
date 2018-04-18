package com.pcalouche.excelspringboot.util;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadableFile {
    byte[] getBytes();

    String getContentType();

    default String getContentDisposition() {
        return String.format("attachment; filename=%s", getFilename());
    }

    String getFilename();

    default void writeToHttpResponse(HttpServletResponse response) throws IOException {
        response.setContentType(getContentType());
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition());
        byte[] bytes = getBytes();
        if (bytes == null) {
            response.getOutputStream().write(new byte[0]);
        } else {
            response.getOutputStream().write(bytes);
        }
    }
}

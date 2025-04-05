package com.xk.utils;

import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Response 工具类，用于将 ResponseEntity<byte[]> 转为 InputStream。
 */
public class ResponseStreamUtil {

    /**
     * 将 ResponseEntity<byte[]> 中的 byte[] 转换为 InputStream。
     *
     * @param response ResponseEntity<byte[]> 对象，必须不为 null 且 body 不为 null
     * @return 包装 byte[] 的 ByteArrayInputStream 对象
     * @throws IllegalArgumentException 如果 response 或 response body 为 null
     */
    public static InputStream convertResponseToStream(ResponseEntity<byte[]> response) throws ServletException {
        if (response == null || response.getBody() == null) {
            throw new ServletException("Response或其body不能为空");
        }
        return new ByteArrayInputStream(response.getBody());
    }

    /**
     * 直接将 byte[] 转换为 InputStream。
     *
     * @param bytes 文件的字节数组
     * @return ByteArrayInputStream 对象
     * @throws IllegalArgumentException 如果 byte[] 为 null
     */
    public static InputStream convertBytesToStream(byte[] bytes) throws ServletException {
        if (bytes == null) {
            throw new ServletException("bytes不能为null");
        }
        return new ByteArrayInputStream(bytes);
    }
}

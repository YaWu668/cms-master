package com.xk.utils;

import com.cms.common.core.exception.ServiceException;

public class FileNameExtractorByPattern {

    public static String extractFileName(String path) {
        if (path == null || path.isEmpty()) {
            throw new ServiceException("路径不能为空");
        }

        // 正则匹配固定格式 /cms-file/yyyy/MM/dd/ 后接文件名
        String regex = "^/cms-file/\\d{4}/\\d{2}/\\d{2}/(.+)$";
        if (path.matches(regex)) {
            // 提取文件名部分
            return path.replaceFirst("^/cms-file/\\d{4}/\\d{2}/\\d{2}/", "");
        } else {
            throw new ServiceException("文件路径格式非法，应为 /cms-file/yyyy/MM/dd/filename");
        }
    }

    public static void main(String[] args) {
        String original = "/cms-file/2025/04/12/XueXiTongAutoFlush-master_20250412115158A009.zip";
        String result = extractFileName(original);
        System.out.println(result);
    }
}

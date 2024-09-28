package com.cms.file.utils;

import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.utils.file.FileTypeUtils;
import com.cms.common.core.utils.uuid.Seq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 *
 * @author 邓志军
 * @date 2024年8月28日16:42:47
 */
public class FileUploadUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 编码文件名
     *
     * @param file 上传的文件
     * @return 文件名称
     */
    public static String extractFileName(MultipartFile file) {
        return StringUtils.format("{}/{}_{}.{}",
                DateUtils.datePath(), // 获取日期路径
                FilenameUtils.getBaseName(file.getOriginalFilename()), // 提取文件名称
                Seq.getId(Seq.uploadSeqType),  // 生成序列化
                FileTypeUtils.getExtension(file) // 获取问价后缀
        );
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension        上传文件类型
     * @param allowedExtension 允许上传文件类型
     * @return true/false
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
package com.cms.file.service;


import com.cms.file.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SysFileService {

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    String uploadFile(MultipartFile file);

    boolean download(HttpServletResponse response, SysFile file);

    /**
     * 判断文件是否在桶内
     */
    boolean checkFileIsExist(String fileId);

    /**
     * 上传图片
     * @param file
     * @return 访问URL(不包含服务器请求地址)
     */
    String uploadImg(MultipartFile file);

    boolean viewXmFile(HttpServletRequest request, HttpServletResponse response);

    void viewXmFile(String filePath, HttpServletResponse response);
    /**
     * 下载文件内容，返回文件的字节数组
     *
     * @param objectName Minio存储文件的对象名称
     * @return 文件数据的字节数组，下载失败时可抛出异常或返回 null
     */
    byte[] downloadFile(String objectName);
}

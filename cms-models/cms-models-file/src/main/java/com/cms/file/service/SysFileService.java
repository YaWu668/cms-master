package com.cms.file.service;


import com.cms.file.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

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
}

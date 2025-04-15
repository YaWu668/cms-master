package com.xk.client;

import com.cms.common.core.web.domain.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cms-file", path = "", contextId = "sysFileClient")
public interface SysFileClient {
    /**
     * 通过 URL 访问图片（Spring Boot 代理
     * @param filePath  文件路径
     * @return
     */
    @GetMapping(value = "/viewXmPath", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> viewXmByPath(@RequestParam("filePath") String filePath);

    /**
     * 判断文件是否存在
     * @param fileName 文件名
     * @return
     */
    @GetMapping("/fileIsNull")
     Response fileIsNull(@RequestParam("fileName") String fileName);


    /**
     * 根据文件对象名称下载文件，返回 Response 包装的文件二进制数据
     * 注意：接口 URL、请求方式根据文件模块中 Controller 的实现自行调整
     */
    @GetMapping("/download/Client")
    Response<byte[]> downloadFileClient(@RequestParam("objectName") String objectName);
}

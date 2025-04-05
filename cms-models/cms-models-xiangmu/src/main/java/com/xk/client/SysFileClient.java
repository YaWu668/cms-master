package com.xk.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cms-file", path = "", contextId = "sysFileClient")
public interface SysFileClient {

    @GetMapping(value = "/viewXmPath", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadFile(@RequestParam("filePath") String filePath);
}

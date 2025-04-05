package com.cms.file.controller;

import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.utils.file.FileUtils;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.Response;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.file.domain.SysFile;
import com.cms.file.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统文件控制器
 *
 * @author 邓志军
 * @date 2024年8月28日10:18:46
 */
@Api(tags = {"系统文件控制器"})
@RestController
public class SysFileController extends BaseController {

    @Resource
    private SysFileService sysFileService;

    /**
     * 文件上传
     *
     * @param file 上传的文件对象
     * @return 文件对象
     */
    @ApiOperation(value = "文件上传", notes = "文件上传", httpMethod = "POST")
    @PostMapping("/upload")
    @Log(title = "文件上传", businessType = BusinessType.UPLOAD)
    public Response<SysFile> upload(@RequestBody MultipartFile file) {
        if (StringUtils.isNull(file)) {
            throw new ServiceException("上传文件不允许为空!");
        }
        // 1、存储文件
        String url = this.sysFileService.uploadFile(file);
        // 2、包装返回对象
        SysFile sysFile = new SysFile(FileUtils.getName(url), url, file.getSize());
        // 3、返回数据
        return this.success(sysFile);
    }

    /**
     * 下载文件
     *
     * @param file 文件对象
     */
    @ApiOperation(value = "下载文件", notes = "下载文件", httpMethod = "POST")
    @PostMapping("/download")
    public Response<?> download(HttpServletResponse response, @RequestBody SysFile file) {
        return this.success(this.sysFileService.download(response, file));
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @ApiOperation(value = "上传图片", notes = "上传图片", httpMethod = "POST")
    @PostMapping("/uploadImg")
    @Log(title = "上传图片", businessType = BusinessType.UPLOAD)
    public Response<String> uploadImg(@RequestParam("file") MultipartFile file) {
        return this.success(this.sysFileService.uploadImg(file));
    }


    @ApiOperation(value = "判断文件是否存在", notes = "判断文件是否存在", httpMethod = "GET")
    @GetMapping("/fileIsNull")
    public Response fileIsNull(@RequestParam("fileName") String fileName) {
        if (this.sysFileService.checkFileIsExist(fileName)){
            return this.success("文件存在于桶内");
        }else {
            return this.error("文件不存在");
        }
    }

    /**
     * 通过 URL 访问图片（Spring Boot 代理）
     */
    @GetMapping("/viewXm/**")
    public Response<?> viewXmFile(HttpServletRequest request, HttpServletResponse response) {
        return this.success(sysFileService.viewXmFile(request, response));
    }

    /**
     * 通过 URL 访问图片（Spring Boot 代理）
     * @param filePath 文件路径
     * @param response
     */
    @GetMapping("/viewXmPath")
    public void viewXmByPath(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        sysFileService.viewXmFile(filePath, response);
    }


}

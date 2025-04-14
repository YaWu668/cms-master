package com.cms.file.service.impl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.core.io.IoUtil;
import java.net.URLConnection;
import java.io.InputStream;
import cn.hutool.core.io.IoUtil;
import com.alibaba.nacos.common.utils.IoUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.file.FileTypeUtils;
import com.cms.common.core.utils.uuid.Seq;
import com.cms.file.config.MinioConfig;
import com.cms.file.domain.SysFile;
import com.cms.file.service.SysFileService;
import com.cms.file.utils.FileUploadUtils;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService implements SysFileService {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // 1、更改文件名称
            String newFileName = FileUploadUtils.extractFileName(file);

            // 2、获取输入流
            InputStream inputStream = file.getInputStream();

            // 3、向 Minio 上传文件
            this.minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(this.minioConfig.getFileBucketName())
                    .object(newFileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            // 4、关闭网络流
            IoUtils.closeQuietly(inputStream);

            // 5、返回访问路径
            return "/" + this.minioConfig.getFileBucketName() + "/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("上传文件失败!");
        }
    }

    @Override
    public boolean download(HttpServletResponse response, SysFile file) {
        try {
            String fileName = file.getUrl();

            if (StringUtils.isEmpty(fileName)) {
                throw new ServiceException("下载文件失败!");
            }

            // 提取桶部分
            int indexOfFirstSlash = fileName.indexOf('/', 1);
            String bucket = fileName.substring(1, indexOfFirstSlash);

            // 提取文件后面的路径
            String filePath = fileName.substring(bucket.length() + 2);

            GetObjectResponse in = this.minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(filePath)
                    .build()
            );
            ServletOutputStream os = response.getOutputStream();
            IoUtil.copy(in, os);
            in.close();
            os.close();
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean checkFileIsExist(String objectName) {
        String prefix = "/cms-file";
        if (objectName.startsWith(prefix)) {
            objectName = objectName.substring(prefix.length());
        }

        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(this.minioConfig.getFileBucketName())
                            .object(objectName)
                            .build()
            );
            return true;
        }  catch (Exception e) {
//            throw new ServiceException("检查文件是否存在异常:" + e.getMessage());
            return  false;
        }
    }

    @Override
    public String uploadImg(MultipartFile file) {
        try {
            String fileName = getTimeUUIDFileName(file.getOriginalFilename());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.minioConfig.getFileBucketName())
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return "/file/viewXm/"+fileName;
        } catch (Exception e) {
            throw new ServiceException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean viewXmFile(HttpServletRequest request, HttpServletResponse response) {
        // 参数校验，防止路径穿越攻击
        // 获取请求路径（去除前缀）
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();


        // 计算文件路径（去掉 "/viewXm/" 前缀）
        String filePathName = requestUri.replaceFirst(contextPath + "/viewXm/", "");


        // 使用 Hutool 进行 URL 解码
        filePathName = URLUtil.decode(filePathName, StandardCharsets.UTF_8);

        if (filePathName == null || filePathName.isEmpty() || filePathName.contains("..")) {
            throw new IllegalArgumentException("文件名无效");
        }

        // 根据文件扩展名动态设置 Content-Type
        String contentType = URLConnection.guessContentTypeFromName(filePathName);
        if (contentType == null) {
            contentType = "application/octet-stream"; // 未知类型时默认
        }
        response.setContentType(contentType);

        try (
                // 从MinIO服务器获取对象并输出到HTTP响应
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(minioConfig.getFileBucketName()) // 设置存储桶名称
                                .object(filePathName) // 设置对象路径和名称
                                .build());
                ServletOutputStream outputStream = response.getOutputStream() // 获取HTTP响应的输出流
        ) {
            IoUtil.copy(inputStream, outputStream);
            response.flushBuffer();
            return true;
        } catch (Exception e) {
            throw new ServiceException("文件下载失败:"+e.getMessage()); // 保留完整异常信息
        }
    }

    @Override
    public void viewXmFile(String filePaths, HttpServletResponse response) {
        // 去除文件路径（去掉 "/viewXm/" 前缀）
        String filePathName = filePaths.replaceFirst(  "/file/viewXm/", "");

        if (filePathName == null || filePathName.isEmpty() || filePathName.contains("..")) {
            throw new IllegalArgumentException("文件名无效");
        }

        String contentType = URLConnection.guessContentTypeFromName(filePathName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);

        try (
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(minioConfig.getFileBucketName())
                                .object(filePathName)
                                .build());
                ServletOutputStream outputStream = response.getOutputStream()
        ) {
            IoUtil.copy(inputStream, outputStream);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ServiceException("文件下载失败: " + e.getMessage());
        }
    }


    /**
     * 时间戳+UUID+原文件名
     * @param originalName
     * @return
     */
    private String getTimeUUIDFileName(String originalName) {
        //        return UUID.randomUUID() + "-" + originalName;
        return extractFileName(System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "") + originalName);
    }

    /**
     * 构建文件名
     * @param fileName
     * @return
     */
    private  String extractFileName(String fileName) {
        return com.cms.common.core.utils.StringUtils.format("{}/{}.{}",
                DateUtils.datePath(), // 获取日期路径
                FilenameUtils.getBaseName(fileName), // 提取文件名称
                FilenameUtils.getExtension(fileName) // 获取文件后缀
        );
    }
}

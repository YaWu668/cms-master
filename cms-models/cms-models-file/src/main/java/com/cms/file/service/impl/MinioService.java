package com.cms.file.service.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.nacos.common.utils.IoUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.cms.common.core.exception.ServiceException;
import com.cms.file.config.MinioConfig;
import com.cms.file.domain.SysFile;
import com.cms.file.service.SysFileService;
import com.cms.file.utils.FileUploadUtils;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
                throw new ServiceException("文件上传失败!");
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

    public boolean checkFileIsExist(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs
                            .builder().
                            bucket(this.minioConfig.
                                    getFileBucketName()
                            )
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

package com.cms.file.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    /**
     * 服务地址
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 文件桶
     */
    private String fileBucketName;

    @Bean
    public MinioClient getMinioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
        this.createBucket(minioClient);
        return minioClient;
    }

    private void createBucket(MinioClient minioClient) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(fileBucketName).build())) {
                // 创建桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(fileBucketName).build());
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
    }
}

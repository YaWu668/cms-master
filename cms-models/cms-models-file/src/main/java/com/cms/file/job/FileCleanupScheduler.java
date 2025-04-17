package com.cms.file.job;
//
//import com.xk.entity.Project;
//import com.xk.service.ProjectService;
//import io.minio.MinioClient;
//import io.minio.RemoveObjectArgs;
//import io.minio.Result;
//import io.minio.messages.Item;
//import io.minio.errors.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
////https://chatgpt.com/c/67fb821c-e784-800a-94d5-67bf0c0425e2
//
//@Component
//public class FileCleanupScheduler {
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Autowired
//    private ProjectService projectService;
//
//    @Value("${minio.fileBucketName}")
//    private String fileBucketName;
//
//
//    /**
//     * 定时任务：每天凌晨2点扫描、比对并删除孤立文件
//     */
//    @Scheduled(cron = "0 0 2 * * ?")
//    public void cleanOrphanFiles() {
//        try {
//            // 1. 获取minio中所有的文件key
//            Set<String> minioFileKeys = listMinioFileKeys();
//            // 2. 从业务数据库中收集所有引用的文件key
//            Set<String> referencedFileKeys = getReferencedFileKeysFromProjects();
//            // 3. 计算孤立文件（minio中存在，但不在引用中的）
//            if (!CollectionUtils.isEmpty(minioFileKeys)) {
//                for (String fileKey : minioFileKeys) {
//                    if (!referencedFileKeys.contains(fileKey)) {
//                        // 调用删除接口
//                        minioClient.removeObject(
//                                RemoveObjectArgs.builder()
//                                        .bucket(fileBucketName)
//                                        .object(fileKey)
//                                        .build()
//                        );
//                        System.out.println("删除孤立文件: " + fileKey);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // 处理 minio 异常及数据库异常
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 获取minio中所有文件的key集合
//     */
//
//    private Set<String> listMinioFileKeys() throws Exception {
//        Set<String> fileKeys = new HashSet<>();
//        Iterable<Result<Item>> results = minioClient.listObjects(
//                io.minio.ListObjectsArgs.builder().bucket(fileBucketName).recursive(true).build()
//        );
//        for (Result<Item> result : results) {
//            Item item = result.get();
//            fileKeys.add(item.objectName());
//        }
//        return fileKeys;
//    }
//
//
//    /**
//     * 从xm_project业务表中获取所有引用的文件 key（附件、结题文件及富文本中的图片）
//     */
//
//    private Set<String> getReferencedFileKeysFromProjects() {
//        Set<String> fileKeys = new HashSet<>();
//        List<Project> projectList = projectService.list();
//        if (!CollectionUtils.isEmpty(projectList)) {
//            for (Project project : projectList) {
//                // 附件和结题文件字段
//                addIfNotEmpty(fileKeys, project.getMaterialsUrl());
//                addIfNotEmpty(fileKeys, project.getConcludeUrl());
//
//                // 假设富文本字段可能存在于某些字段中，比如teacher_experience
//                addImageKeysFromRichText(fileKeys, project.getTeacherExperience());
//                // 如有其它富文本字段，也可以同理处理
//            }
//        }
//        return fileKeys;
//    }
//
//
///**
//     * 如果内容不为空，添加到集合中
//     */
//
//    private void addIfNotEmpty(Set<String> set, String content) {
//        if (content != null && !content.trim().isEmpty()) {
//            // 假设存储的直接是文件Key，如不然可进行转换：例如截取 URL部分
//            set.add(content.trim());
//        }
//    }
//
//
///**
//     * 从富文本内容中提取图片url，并转换为minio文件key后加入集合中
//     */
//
//    private void addImageKeysFromRichText(Set<String> fileKeys, String richText) {
//        if (richText != null && !richText.trim().isEmpty()) {
//            // 正则提取<img ... src="...">中的URL
//            Pattern pattern = Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*>");
//            Matcher matcher = pattern.matcher(richText);
//            while (matcher.find()) {
//                String imgUrl = matcher.group(1);
//                // 根据实际情况处理URL，假设URL包含桶名后面的部分就是文件key
//                // 例如 http://minio-server/minio-bucket/your-file-key
//                String fileKey = extractFileKeyFromUrl(imgUrl);
//                if (fileKey != null) {
//                    fileKeys.add(fileKey);
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 根据图片 URL 转换为 minio 文件key
//     * 这里根据自己URL的格式进行处理，示例中假设URL中包含 {bucketName}/{fileKey}
//     */
//
//    private String extractFileKeyFromUrl(String url) {
//        if (url != null && !url.trim().isEmpty()) {
//            // 示例：假设 URL 格式为：http://域名/{bucketName}/{fileKey}
//            int index = url.indexOf(fileBucketName);
//            if (index != -1) {
//                // 加上桶名长度和斜杠
//                int start = index + fileBucketName.length() + 1;
//                return url.substring(start);
//            }
//        }
//        return null;
//    }
//}
//

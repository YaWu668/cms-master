package com.xk.utils;

import com.cms.common.core.exception.ServiceException;

import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {



    /**
     * 将多个文件（以 Map 形式存放文件路径与内容）压缩成 zip 文件
     *
     * @param filesMap key: zip包内的相对路径（包含文件夹），value: 文件内容字节数组
     * @return 压缩包对应的字节数组
     */
    public static byte[] compress(Map<String, byte[]> filesMap) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(baos))) {

            for (Map.Entry<String, byte[]> entry : filesMap.entrySet()) {
                String entryName = entry.getKey();
                byte[] data = entry.getValue();
                ZipEntry zipEntry = new ZipEntry(entryName);

                // 如果文件后缀是 .zip ，则使用 STORED 模式写入，避免二次压缩破坏文件结构
                if (entryName.toLowerCase().endsWith(".zip")) {
                    zipEntry.setMethod(ZipEntry.STORED);
                    zipEntry.setSize(data.length);
                    CRC32 crc32 = new CRC32();
                    crc32.update(data);
                    zipEntry.setCrc(crc32.getValue());
                }
                // 否则使用默认压缩模式 DEFLATED

                zos.putNextEntry(zipEntry);
                zos.write(data);
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("压缩文件失败:" + e.getMessage());
        }
    }
}

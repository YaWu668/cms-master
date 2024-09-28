package com.cms.common.core.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.cms.common.core.annotation.excel.CommentCellWriteHandler;
import com.cms.common.core.constant.Constants;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel 工具类，用于处理 Excel 文件的读写操作。
 */
public class ExcelUtils {

    /**
     * 将列表数据写入 Excel 文件，并响应给前端浏览器下载。
     *
     * @param response  响应对象，用于设置 HTTP 响应头并获取输出流。
     * @param filename  下载的文件名，不包括扩展名。
     * @param sheetName Excel 工作表的名称。
     * @param head      数据模型类的 Class 对象，用于定义 Excel 表头。
     * @param data      数据列表，列表中的每一项将作为 Excel 表格中的一行。
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        ExcelWriterBuilder writerBuilder;
        // 使用 try-with-resources 确保输出流在使用后自动关闭
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 设置 HTTP 响应头，指定下载的文件名和内容类型
            response.setHeader("Content-disposition", "attachment;filename=" + buildFileName(filename) + ".xlsx");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // 初始化 Excel 写入构建器
            writerBuilder = EasyExcel.write(outputStream, head);

            writerBuilder
                    // 使用内存中的数据
                    .inMemory(true)
                    // 不要自动关闭流，交给 Servlet 自己处理
                    .autoCloseStream(false)
                    // 注册列宽自动适配策略，以确保列宽适应内容
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 注册自定义的批注适配器，解析注解的内容
                    .registerWriteHandler(new CommentCellWriteHandler(head))
                    // 设置工作表名称
                    .sheet(sheetName)
                    // 写入数据
                    .doWrite(data);
        }
    }

    /**
     * 从上传的 Excel 文件中读取数据，并将其解析为指定类型的对象列表。
     *
     * @param file 上传的 Excel 文件。
     * @param head 数据模型类的 Class 对象，用于定义如何解析 Excel 表头。
     * @return 解析得到的数据对象列表。
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // 从 Excel 文件中读取数据并同步返回
            return EasyExcel.read(inputStream, head, null)
                    // 不要自动关闭流，交给 Servlet 自己处理
                    .autoCloseStream(false)
                    // 同步读取所有数据
                    .doReadAllSync();
        }
    }

    /**
     * 构建文件名，包含前缀和当前时间戳，并进行 URL 编码以确保文件名在 URL 中合法。
     *
     * @param prefix 文件名前缀。
     * @return URL 编码后的完整文件名。
     */
    public static String buildFileName(String prefix) {
        try {
            // 创建时间格式化器，按照 YYYYMMDDHHMMSS 格式化当前时间
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.YYYYMMDDHHMMSS);
            // 构建文件名，并对其进行 URL 编码
            return URLEncoder.encode(prefix + "-" + dateTimeFormatter.format(LocalDateTime.now()), Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // 打印编码异常的栈跟踪
        }
        // 在编码失败时返回原始前缀
        return prefix;
    }
}

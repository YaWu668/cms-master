package com.cms.common.core.annotation.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.cms.common.core.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Excel导出批注处理类
 * 负责处理Excel导出时单元格的批注
 *
 * @author 邓志军
 * @date 2024年9月5日11:01:23
 */
public class CommentCellWriteHandler implements CellWriteHandler {

    /**
     * 存储批注信息的Map
     */
    private final Map<Integer, ExcelComment> notationMap;

    /**
     * 构造方法
     *
     * @param clazz 需要处理批注的类
     */
    public CommentCellWriteHandler(Class<?> clazz) {
        this.notationMap = this.getNotationMap(clazz);
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 判断是否为表头，只有表头才需要添加批注
        if (!isHead) {
            return;
        }
        Sheet sheet = writeSheetHolder.getSheet();
        Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
        if (!StringUtils.isEmpty(notationMap) && notationMap.containsKey(cell.getColumnIndex())) {
            // 获取该列的批注内容
            ExcelComment excelComment = notationMap.get(cell.getColumnIndex());
            if (Objects.nonNull(excelComment)) {
                // 创建批注
                Comment comment = drawingPatriarch.createCellComment(new XSSFClientAnchor(
                        0, // 从单元格左边缘到批注左边缘的水平偏移量（单位是EMU）。0表示没有水平偏移。
                        0, // 从单元格上边缘到批注上边缘的垂直偏移量（单位是EMU）。0表示没有垂直偏移。
                        0, // 批注右边缘的水平偏移量（单位是EMU）。0表示没有水平偏移。
                        0, // 批注下边缘的垂直偏移量（单位是EMU）。0表示没有垂直偏移。
                        (short) cell.getColumnIndex(), // 批注的起始列索引。批注将出现在该列。
                        0, // 批注的起始行索引。批注将出现在该行。
                        (short) excelComment.getRemarkColumnWide(), // 批注的结束列索引。表示批注在该列宽度处结束。
                        1  // 批注的结束行索引。批注在该行结束。
                ));
                comment.setString(new XSSFRichTextString(excelComment.getRemarkValue()));
                cell.setCellComment(comment);
            }
        }
    }

    /**
     * 从类中提取批注信息
     *
     * @param clazz 需要提取批注的类
     * @return 批注信息的Map，其中键是字段的索引，值是与字段关联的批注信息
     */
    public Map<Integer, ExcelComment> getNotationMap(Class<?> clazz) {
        // 创建一个用于存储批注信息的 Map，其中键是字段的索引，值是 ExcelComment 对象
        Map<Integer, ExcelComment> notationMap = new HashMap<>();
        // 获取类中所有声明的字段
        Field[] fields = clazz.getDeclaredFields();

        // 字段的索引初始值设为-1
        int index = -1;
        // 遍历所有字段
        for (Field field : fields) {
            // 索引自增
            ++index;
            // 检查字段是否有 ExcelNotation 注解
            if (!field.isAnnotationPresent(ExcelNotation.class)) {
                // 如果字段有 ExcelIgnore 注解，说明该字段应被忽略
                if (field.isAnnotationPresent(ExcelIgnore.class)) {
                    // 索引减一，因为该字段不会被处理
                    --index;
                }
                // 继续处理下一个字段
                continue;
            }

            // 创建一个新的 ExcelComment 对象用于存储批注信息
            ExcelComment excelComment = new ExcelComment();
            // 获取字段上的 ExcelNotation 注解
            ExcelNotation excelNotation = field.getAnnotation(ExcelNotation.class);
            // 设置批注的值
            excelComment.setRemarkValue(excelNotation.value());
            // 设置批注的列宽
            excelComment.setRemarkColumnWide(excelNotation.remarkColumnWide());
            // 将字段的索引和批注信息存入 Map
            notationMap.put(index, excelComment);
        }

        // 返回包含批注信息的 Map
        return notationMap;
    }

}

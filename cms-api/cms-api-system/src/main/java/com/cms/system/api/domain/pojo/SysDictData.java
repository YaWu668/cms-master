package com.cms.system.api.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 字典数据表 sys_dict_data
 *
 * @author 邓志军
 * @date 2024年6月1日15:52:15
 */
@ApiModel(description = "系统字典数据")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictData extends BaseEntity {
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "修改时字典编码必须指定", groups = {ValidationGroups.Update.class})
    @Null(message = "添加时字典编码不允许指定", groups = {ValidationGroups.Insert.class})
    @ApiModelProperty(value = "字典编码", position = 2)
    private Long dictCode;

    /**
     * 字典排序
     */
    @ApiModelProperty(value = "字典排序", position = 3)
    private Long dictSort;

    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签", position = 4, required = true)
    @NotBlank(message = "字典标签不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @Size(max = 100, message = "字典标签长度不能超过100个字符", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String dictLabel;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值", position = 5, required = true)
    @NotBlank(message = "字典键值不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @Size(max = 100, message = "字典键值长度不能超过100个字符", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String dictValue;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型", position = 6, required = true)
    @NotBlank(message = "字典类型不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @ApiModelProperty(value = "样式属性（其他样式扩展）", position = 7, required = true)
    @Size(max = 100, message = "样式属性长度不能超过100个字符", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String cssClass;

    /**
     * 表格字典样式
     */
    @ApiModelProperty(value = "表格字典样式", position = 8)
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @ApiModelProperty(value = "是否默认（Y是 N否）", position = 9)
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）", position = 10)
    private String status;
}

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

import javax.validation.constraints.*;


/**
 * 字典类型表 sys_dict_type
 *
 * @author 邓志军
 * @date 2024年6月1日15:52:15
 */
@ApiModel(description = "系统字典类型")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "修改时字典id必须指定", groups = {ValidationGroups.Update.class})
    @Null(message = "添加时字典id不允许指定", groups = {ValidationGroups.Insert.class})
    @ApiModelProperty(value = "字典主键", position = 2)
    private Long dictId;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称", position = 3, required = true)
    @NotBlank(message = "字典名称不能为空", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Size(max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型", position = 4, required = true)
    @NotBlank(message = "字典类型不能为空", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Size(max = 100, message = "字典类型类型长度不能超过100个字符", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @NotNull(message = "状态不允许为空!!!", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Pattern(regexp = "[01]", message = "状态参数值错误!", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @ApiModelProperty(value = "状态（0正常 1停用）", position = 5)
    private String status;
}

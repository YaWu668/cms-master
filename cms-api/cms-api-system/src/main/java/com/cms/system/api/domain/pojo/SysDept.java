package com.cms.system.api.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cms.common.core.constant.VerifyConstants;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 部门数据模型
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@ApiModel(description = "部门数据模型")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysDept extends BaseEntity implements Serializable {
    /**
     * 部门id
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "修改时部门id必须指定!!!", groups = {ValidationGroups.Update.class})
    @Null(message = "添加时部门id不允许指定!!!", groups = {ValidationGroups.Insert.class})
    @ApiModelProperty(value = "部门id", position = 1)
    private Long deptId;

    /**
     * 父部门id
     */
    @NotNull(message = "父部门id不能为空!!!", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @Positive(message = "父部门id异常!!!", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @ApiModelProperty(value = "父部门id", position = 2)
    private Long parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表", position = 3)
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", position = 4)
    private String deptName;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序", position = 5)
    private Integer orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人", position = 6)
    private String leader;

    /**
     * 联系电话
     */
    @Pattern(regexp = VerifyConstants.PHONE_NUMBER_REGEX, message = "联系电话格式不正确!!!", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @ApiModelProperty(value = "联系电话", position = 7)
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", position = 8)
    @Email(message = "邮箱格式不正确!!!", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    @ApiModelProperty(value = "部门状态（0正常 1停用）", position = 9)
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）", position = 10)
    private String delFlag;

    /**
     * 部门类型(0集团 1分公司 2 部门 3二级部门)
     */
    @ApiModelProperty(value = "部门类型(0集团 1分公司 2 部门 3二级部门)", position = 11)
    private String deptType;
}
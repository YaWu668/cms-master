package com.cms.system.domain.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

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

/**
 * 岗位表 sys_post
 *
 * @author 邓志军
 * @date 2024年6月1日13:05:33
 */
@ApiModel(description = "岗位表 sys_post")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位id
     */
    @NotNull(message = "修改时岗位id必须指定", groups = {ValidationGroups.Update.class})
    @Null(message = "添加时岗位id不允许指定", groups = {ValidationGroups.Insert.class})
    @ApiModelProperty(value = "岗位id", position = 2)
    @TableId(type = IdType.AUTO)
    private Long postId;

    /**
     * 岗位编码
     */
    @ApiModelProperty(value = "岗位编码", position = 3, required = true)
    @NotBlank(message = "岗位编码不能为空", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Size(max = 64, message = "岗位编码长度不能超过64个字符", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    private String postCode;

    /**
     * 岗位类别
     */
    @ApiModelProperty(value = "岗位类别", position = 4)
    private String postType;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称", position = 5, required = true)
    @NotBlank(message = "岗位名称不能为空", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    @Size(max = 50, message = "岗位名称长度不能超过50个字符", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    private String postName;

    /**
     * 岗位排序
     */
    @ApiModelProperty(value = "岗位排序", position = 6, required = true)
    private Integer postSort;

    /**
     * 岗位职责
     */
    @ApiModelProperty(value = "岗位职责", position = 7)
    @Size(max = 300, message = "岗位职责长度不能超过300个字符", groups = {ValidationGroups.Update.class, ValidationGroups.Insert.class})
    private String responsibility;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）", position = 8)
    private String status;
}

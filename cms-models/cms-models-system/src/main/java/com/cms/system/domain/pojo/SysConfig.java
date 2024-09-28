package com.cms.system.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cms.common.core.validation.ValidationGroups;
import com.cms.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * 系统参数模型
 *
 * @author 邓志军
 * @date 2024年8月15日14:54:44
 */
@ApiModel(description = "系统参数模型")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @ApiModelProperty(value = "参数主键", position = 1, required = true)
    @TableId(type = IdType.AUTO)
    @NotNull(message = "修改时系统配置id必须指定!!!", groups = {ValidationGroups.Update.class})
    @Null(message = "添加时系统配置id不允许指定!!!", groups = {ValidationGroups.Insert.class})
    private Long configId;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称", position = 2, required = true)
    @NotNull(message = "系统配置参数名称不允许为空!",groups = {ValidationGroups.Update.class,ValidationGroups.Insert.class})
    private String configName;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名", position = 3, required = true)
    @NotNull(message = "系统配置参数键名不允许为空!",groups = {ValidationGroups.Update.class,ValidationGroups.Insert.class})
    private String configKey;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值", position = 4, required = true)
    @NotNull(message = "系统配置参数键值不允许为空!",groups = {ValidationGroups.Update.class,ValidationGroups.Insert.class})
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @ApiModelProperty(value = "系统内置（Y是 N否）", position = 5, required = true)
    @Pattern(regexp = "^[YN]$",message = "出现意外的值，系统内置值只允许是'Y' 或 'N'",groups = {ValidationGroups.Update.class,ValidationGroups.Insert.class})
    private String configType;
}

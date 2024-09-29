package com.cms.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cms.system.domain.pojo.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单信息
 */
@ApiModel(description = "菜单信息")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuDto extends SysMenu implements Serializable {

    /**
     * 子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "子菜单", position = 1)
    private List<SysMenuDto> children = new ArrayList<>();
}

package com.cms.system.domain.vo;

import com.cms.system.api.domain.dto.SysDeptDto;
import com.cms.system.api.domain.pojo.SysRole;
import com.cms.system.domain.dto.SysMenuDto;
import com.cms.system.api.domain.pojo.SysUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect 树结构实体类
 *
 * @author 邓志军
 * @date 2024年5月29日21:36:10
 */
@ApiModel(description = "树结构")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelect implements Serializable {
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @ApiModelProperty(value = "节点ID", position = 2)
    private Long id;

    /**
     * 节点值
     */
    @ApiModelProperty(value = "节点值", position = 3)
    private Long value;

    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称", position = 3)
    private String label;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点", position = 4)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    /**
     * 组装前端部门树
     *
     * @param dept 部门数据
     */
    public TreeSelect(SysDeptDto dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 组装前端菜单树
     *
     * @param menu 菜单数据
     */
    public TreeSelect(SysMenuDto menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 组织前端用户树
     */
    public TreeSelect(SysUser user) {
        this.value = user.getUserId();
        this.label = user.getUserName() + " - " + user.getNickName();
    }

    /**
     * 组装前端角色树
     */
    public TreeSelect(SysRole role) {
        this.value = role.getRoleId();
        this.label = role.getRoleName();
    }
}

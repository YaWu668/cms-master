package com.cms.system.domain.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cms.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 菜单权限表
 * 
 * @author 邓志军
 * @date 2024-05-29
 */ 
@ApiModel(description = "菜单权限表")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu extends BaseEntity implements Serializable {
	/**
	 * 菜单ID
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "菜单ID", position = 1)
	private Long menuId;

	/**
	 * 菜单名称
	 */ 
	@ApiModelProperty(value = "菜单名称", position = 2)
	private String menuName;

	/**
	 * 父菜单ID
	 */ 
	@ApiModelProperty(value = "父菜单ID", position = 3)
	private Long parentId;

	/**
	 * 显示顺序
	 */ 
	@ApiModelProperty(value = "显示顺序", position = 4)
	private Integer orderNum;

	/**
	 * 路由地址
	 */ 
	@ApiModelProperty(value = "路由地址", position = 5)
	private String path;

	/**
	 * 组件路径
	 */ 
	@ApiModelProperty(value = "组件路径", position = 6)
	private String component;

	/**
	 * 路由参数
	 */ 
	@ApiModelProperty(value = "路由参数", position = 7)
	private String query;

	/**
	 * 是否为外链（0是 1否）
	 */ 
	@ApiModelProperty(value = "是否为外链（0是 1否）", position = 8)
	private Integer isFrame;

	/**
	 * 是否缓存（0缓存 1不缓存）
	 */ 
	@ApiModelProperty(value = "是否缓存（0缓存 1不缓存）", position = 9)
	private Integer isCache;

	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */ 
	@ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）", position = 10)
	private String menuType;

	/**
	 * 菜单状态（0显示 1隐藏）
	 */ 
	@ApiModelProperty(value = "菜单状态（0显示 1隐藏）", position = 11)
	private String visible;

	/**
	 * 菜单状态（0正常 1停用）
	 */ 
	@ApiModelProperty(value = "菜单状态（0正常 1停用）", position = 12)
	private String status;

	/**
	 * 权限标识
	 */ 
	@ApiModelProperty(value = "权限标识", position = 13)
	private String perms;

	/**
	 * 菜单图标
	 */ 
	@ApiModelProperty(value = "菜单图标", position = 14)
	private String icon;
}
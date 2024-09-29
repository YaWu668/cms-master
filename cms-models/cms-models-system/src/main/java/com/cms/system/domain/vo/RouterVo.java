package com.cms.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author 邓志军
 * @date 2024年5月29日21:36:53
 */
@ApiModel(description = "路由配置信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    /**
     * 路由名字
     */
    @ApiModelProperty(value = "路由名字", position = 1)
    private String name;

    // 组件id
    @ApiModelProperty(value = "组件id", position = 2)
    private Long id;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址", position = 3)
    private String path;

    /**
     * 菜单类型
     */
    @ApiModelProperty(value = "菜单类型", position = 4)
    private String MenuType;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    @ApiModelProperty(value = "是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现", position = 5)
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    @ApiModelProperty(value = "重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击", position = 6)
    private String redirect;

    /**
     * 组件地址
     */
    @ApiModelProperty(value = "组件地址", position = 7)
    private String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    @ApiModelProperty(value = "}", position = 8)
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    @ApiModelProperty(value = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面", position = 9)
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    @ApiModelProperty(value = "其他元素", position = 10)
    private MetaVo meta;

    /**
     * 子路由
     */
    @ApiModelProperty(value = "子路由", position = 11)
    private List<RouterVo> children;
}

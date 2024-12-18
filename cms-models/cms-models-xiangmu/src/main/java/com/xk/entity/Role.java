package com.xk.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 角色信息表(Role)表实体类
 *
 * @author YaWu
 * @since 2024-12-18 00:29:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class Role  {
    //角色ID
    @TableId
    private Long roleId;

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    private String dataScope;
    //菜单树选择项是否关联显示
    private Integer menuCheckStrictly;
    //部门树选择项是否关联显示
    private Integer deptCheckStrictly;
    //角色状态（0正常 1停用）
    private String status;
    //删除标志（0代表存在 2代表删除）
    @TableLogic(value = "0", delval = "2")
    private String delFlag;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;
    //备注
    private String remark;



}

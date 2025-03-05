package com.cms.system.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 系统操作日志查询模型
 *
 * @author 邓志军
 * @date 2024年6月2日17:30:19
 */
@ApiModel(description = "系统操作日志查询模型")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysOperLogQuery {



    /**
     * 模块标题
     */
    @ApiModelProperty(value = "模块标题", position = 1)
    private String title;

    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员", position = 2)
    private String operName;

    /**
     * 操作角色
     */
    @ApiModelProperty(value = "操作角色", position = 3)
    private Long roleId;

    /**
     * 请求URL
     */
    @ApiModelProperty(value = "请求URL", position = 4)
    private String operUrl;

    /**
     * 操作状态（0正常 1异常）
     */
    @ApiModelProperty(value = "操作状态（0正常 1异常）", position = 5)
    private Integer status;

    /**
     * 访问时间开始时间
     */
    @ApiModelProperty(value = "访问时间开始时间", position = 6)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 访问时间结束时间
     */
    @ApiModelProperty(value = "访问时间结束时间", position = 7)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 操作ip地址
     */
    @ApiModelProperty(value = "操作ip地址", position = 8)
    private String operIp;

    /**
     *业务类型（0其它 1新增 2修改 3删除
     */
    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除", position = 9)
    @Pattern(regexp = "^[0-9]*$", message = "业务类型只能是数字字符串")
    private String businessType;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", position = 10)
    @Pattern(regexp = "^[0-9]*$", message = "用户ID只能是数字字符串")
    private String userId;



}

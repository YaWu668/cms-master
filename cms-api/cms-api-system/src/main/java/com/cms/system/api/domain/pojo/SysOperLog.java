package com.cms.system.api.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@ApiModel(description = "操作日志记录")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysOperLog implements Serializable {
	/**
	 * 日志主键
	 */
	@ApiModelProperty(value = "日志主键", position = 1)
	@TableId(type = IdType.AUTO)
	private Long operId;

	/**
	 * 模块标题
	 */
	@ApiModelProperty(value = "模块标题", position = 2)
	private String title;

	/**
	 * 业务类型
	 */
	@ApiModelProperty(value = "业务类型", position = 3)
	private Integer businessType;

	/**
	 * 方法名称
	 */
	@ApiModelProperty(value = "方法名称", position = 4)
	private String method;

	/**
	 * 请求方式
	 */
	@ApiModelProperty(value = "请求方式", position = 5)
	private String requestMethod;

	/**
	 * 操作类别
	 */
	@ApiModelProperty(value = "操作类别", position = 6)
	private Integer operatorType;

	/**
	 * 操作人员
	 */
	@ApiModelProperty(value = "操作人员", position = 7)
	private String operName;

	/**
	 * 请求URL
	 */
	@ApiModelProperty(value = "请求URL", position = 8)
	private String operUrl;

	/**
	 * 主机地址
	 */
	@ApiModelProperty(value = "主机地址", position = 9)
	private String operIp;

	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数", position = 10)
	private String operParam;

	/**
	 * 返回参数
	 */
	@ApiModelProperty(value = "返回参数", position = 11)
	private String jsonResult;

	/**
	 * 操作状态（0正常 1异常）
	 */
	@ApiModelProperty(value = "操作状态（0正常 1异常）", position = 12)
	private Integer status;

	/**
	 * 错误消息
	 */
	@ApiModelProperty(value = "错误消息", position = 13)
	private String errorMsg;

	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间", position = 14)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date operTime;

	/**
	 * 消耗时间
	 */
	@ApiModelProperty(value = "消耗时间", position = 15)
	private Long costTime;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id", position = 16)
	private Long userId;

}

package com.cms.system.domain.query;

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
 * 系统访问记录查询模型
 * 
 * @author 邓志军
 * @date 2024-05-29
 */ 
@ApiModel(description = "系统访问记录查询模型")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysLogininforQuery implements Serializable {
	/**
	 * 用户账号
	 */ 
	@ApiModelProperty(value = "用户账号", position = 1)
	private String userName;

	/**
	 * 登录状态（0成功 1失败）
	 */ 
	@ApiModelProperty(value = "登录状态（0成功 1失败）", position = 2)
	private String status;

	/**
	 * 访问时间开始时间
	 */ 
	@ApiModelProperty(value = "访问时间开始时间", position = 3)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date beginTime;

	/**
	 * 访问时间结束时间
	 */
	@ApiModelProperty(value = "访问时间结束时间", position = 4)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
}
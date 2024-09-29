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
 * 系统访问记录
 * 
 * @author 邓志军
 * @date 2024-05-29
 */ 
@ApiModel(description = "系统访问记录")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysLogininfor implements Serializable {
	/**
	 * 访问ID
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "访问ID", position = 1)
	private Long infoId;

	/**
	 * 用户账号
	 */ 
	@ApiModelProperty(value = "用户账号", position = 2)
	private String userName;

	/**
	 * 登录IP地址
	 */ 
	@ApiModelProperty(value = "登录IP地址", position = 3)
	private String ipaddr;

	/**
	 * 登录状态（0成功 1失败）
	 */ 
	@ApiModelProperty(value = "登录状态（0成功 1失败）", position = 4)
	private String status;

	/**
	 * 操作类型（0登录 1退出）
	 */
	@ApiModelProperty(value = "登录状态（0成功 1失败）", position = 4)
	private Integer type;

	/**
	 * 提示信息
	 */ 
	@ApiModelProperty(value = "提示信息", position = 5)
	private String msg;

	/**
	 * 访问时间
	 */ 
	@ApiModelProperty(value = "访问时间", position = 6)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date accessTime;
}
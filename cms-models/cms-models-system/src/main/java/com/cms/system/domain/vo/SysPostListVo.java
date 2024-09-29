package com.cms.system.domain.vo;

import com.cms.system.domain.pojo.SysPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统岗位列表模型对象
 *
 * @author 邓志军
 * @date 2024年6月7日15:14:29
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "系统岗位列表模型对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPostListVo extends SysPost {
    /**
     * 人员数
     */
    @ApiModelProperty(value = "人员数", position = 1)
    private Integer userCount;
}

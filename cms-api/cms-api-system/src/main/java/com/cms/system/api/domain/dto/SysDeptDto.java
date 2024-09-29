package com.cms.system.api.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cms.system.api.domain.pojo.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 部门表 sys_dept
 *
 * @author 邓志军
 * @date 2024年5月28日23:25:32
 */
@ApiModel(description = "部门信息")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysDeptDto extends SysDept implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 子部门
     */
    @ApiModelProperty(value = "子部门", position = 13)
    private List<SysDeptDto> children = new ArrayList<>();

    /**
     * 员工数
     */
    @ApiModelProperty(value = "员工数", position = 14)
    private Integer userCount;
}

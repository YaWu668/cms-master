package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 学院组表修改实体类
 *
 * @author yauw
 * @since 2024-12-05 21:21:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateCollegeGroup {
    /**
     * 学院组的id
     */
    @NotNull(message = "学院组id不能为空")
    @Min(value = 1, message = "学院组id不能小于1")
    private Long collegeGroupId;
    /**
     * 学院组名称
     */
    @NotNull(message = "学院组名称不能为空")
    @Length(min= 3,max = 50, message = "学院组名称长度3~50以内")
    private String name;
    /**
     * 状态(0正常 1停用)
     */
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态不能小于0")
    @Max(value = 1, message = "状态不能大于1")
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}

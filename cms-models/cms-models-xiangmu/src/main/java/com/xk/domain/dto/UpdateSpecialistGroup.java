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
 * 修改专家组表SpecialistGroup实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateSpecialistGroup{
    /**
     * 专家组的id
     */
    @NotNull(message = "专家组的id不能为空")
    @Min(value = 1, message = "专家组的id不能小于1")
    private Long specialistGroupId;
    /**
     * 专家组的名称
     */
    @NotNull
    @Length(min =3,max = 50, message = "专家组的名称长度范围是3~50")
    private String name;
    /**
     * 状态(0正常 1停用)
     */
    @NotNull
    @Max(value = 1, message = "状态只能是0或1")
    @Min(value = 0, message = "状态只能是0或1")
    private Long status;

    /**
     * 备注
     */
    private String remark;

}

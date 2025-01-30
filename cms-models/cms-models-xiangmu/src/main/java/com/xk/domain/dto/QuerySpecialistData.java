package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xk.domain.query.PageQuery;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *根据专家组的id,分页查询专家组人员实体类
 *
 * @author YaWu
 * @since 2024-12-19 01:42:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuerySpecialistData extends PageQuery {
    /**
     * 专家组的id
     */
    @NotNull(message = "专家组的id不能为空")
    @Min(value = 1, message = "专家组的id不能小于1")
    private Long specialistGroupId;
}

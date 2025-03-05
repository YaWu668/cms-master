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
 * 根据学院id查询学院人员
 *
 * @author yauw
 * @since 2024-12-05 21:21:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class selectByIdCollegeDateDto extends PageQuery {
    /**
     * 学院组的id
     */
    @NotNull(message = "学院组的id不允许为空")
    @Min(value = 1, message = "学院组的id不允许小于1")
    private Long collegeGroupId;

}

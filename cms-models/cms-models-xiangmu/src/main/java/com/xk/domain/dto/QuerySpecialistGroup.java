package com.xk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xk.domain.query.PageQuery;
import com.xk.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 查询实体类专家组表SpecialistGroup实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuerySpecialistGroup extends PageQuery {
    /**
     * 专家组的名称
     */
    private String name;
}

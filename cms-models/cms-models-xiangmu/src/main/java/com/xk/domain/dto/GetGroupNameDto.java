package com.xk.domain.dto;

import lombok.Data;

@Data
public class GetGroupNameDto {
    Long specialistId;// 专家组 id

    Long yearId; //年度组id

    Long collegeId; // 学院组
}

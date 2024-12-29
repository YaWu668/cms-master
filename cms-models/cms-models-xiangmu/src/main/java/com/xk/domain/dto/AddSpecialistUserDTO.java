package com.xk.domain.dto;


import com.xk.entity.User;
import lombok.Data;

import java.util.List;

/**
 *添加专家组
 */
@Data
public class AddSpecialistUserDTO {
    private Long specialistGroupId;
    private List<User> userIdList;
}





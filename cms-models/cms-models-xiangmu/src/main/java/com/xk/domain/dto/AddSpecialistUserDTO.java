package com.xk.domain.dto;


import com.xk.entity.User;
import lombok.Data;

import java.util.List;

/**
 *添加专家组
 */
@Data
public class AddSpecialistUserDTO {
    /**
     * 专家组的id
     */
    private Long specialistGroupId;
    /**
     * 用户的id
     */
    private List<User> userIdList;
}





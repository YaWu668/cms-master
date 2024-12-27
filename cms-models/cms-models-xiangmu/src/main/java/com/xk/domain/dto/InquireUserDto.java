package com.xk.domain.dto;


import com.xk.domain.query.PageQuery;
import lombok.Data;

/**
 * @author 根据用户账号或者姓名查询的Dto
 * @date 2022/12/28 16:01
 */
@Data
public class InquireUserDto  extends PageQuery {

    //用户账号, (学生就学号, 老师就是工号)
    private String userName;
    //用户姓名,学生姓名或者教师姓名
    private String nickName;
}

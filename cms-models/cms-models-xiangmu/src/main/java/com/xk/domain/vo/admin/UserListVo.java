package com.xk.domain.vo.admin;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Lombok;

/**
 * 返回用户特定信息Vo
 */
@Data
public class UserListVo {
    //用户ID
    @TableId
    private Long userId;
    //用户账号, (学生就学号, 老师就是工号)
    private String userName;
    //用户姓名,学生姓名或者教师姓名
    private String nickName;
    //用户性别（0男 1女 2未知）
    private String sex;
}

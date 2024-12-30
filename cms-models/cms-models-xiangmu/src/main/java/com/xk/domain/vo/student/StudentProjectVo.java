package com.xk.domain.vo.student;

import lombok.Data;

import java.util.Date;

/**
 * 学生页面项目列表显示
 */
@Data
public class StudentProjectVo {
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 负责人的id, 确定负责人
     */
    private Long userId;
    /**
     * 负责人的名字
     */
    private String nickName;
    /**
     * 年度组的id
     */
    private Long yearGroupId;
    /**
     * 状态序号 项目状态的值 0:未通过审核, 1:审核中 ,2:项目进行中 ,3:待结题 4:通过结题 ,5不通结题
     */
    private Long state;
    /**
     * 项目类型, (字典)示例: 1为创新训练项目, 2为创业训练项目, 3为创业实践
     */
    private Long type;
}

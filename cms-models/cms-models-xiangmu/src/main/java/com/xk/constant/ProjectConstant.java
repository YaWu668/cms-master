package com.xk.constant;

/**
 * 项目常量类
 */
public class ProjectConstant {
    /**
     * 项目审核通过状态
     */
    public static final long PROJECT_STATUS_PASS = 0;

    /**
     * 项目审核未通过状态
     */
    public static final long PROJECT_STATUS_NOT_PASS = 1;

    /**
     * 项目审核进度状态值: 1为初始化(后续执行都单调梯子),0代表审核结束
     */
    public static final long PROJECT_STATUS_PROGRESS = 0;
    public static final long PROJECT_STATUS_PROGRESS_INIT = 1;

    /**
     *  项目状态的值  0:未通过审核, 1:审核中 ,2:项目进行中 ,3:待结题 4:通过结题 ,5不通结题
     */
    public static final long PROJECT_STATUS_NOT_PASS_VALUE = 0;
    public static final long PROJECT_STATUS_AUDIT_VALUE = 1;
    public static final long PROJECT_STATUS_IN_PROGRESS_VALUE = 2;
    public static final long PROJECT_STATUS_PENDING_VALUE = 3;
    public static final long PROJECT_STATUS_PASS_VALUE = 4;
    public static final long PROJECT_STATUS_NOT_PASS_VALUE_2 = 5;

    /**
     * 角色标识符 1:学生(xm-student) , 2:教师(xm-teacher), 3:学院审核人(xm-college), 4:专家(xm-specialist), 5:管理员(xm-admin)
     */
    public static final String ROLE_STUDENT = "xm-student";
    public static final String ROLE_TEACHER = "xm-teacher";
    public static final String ROLE_COLLEGE = "xm-college";
    public static final String ROLE_SPECIALIST = "xm-specialist";
    public static final String ROLE_ADMIN = "xm-admin";

    /**
     * 项目类型: 1:创新训练项目 2:创业训练项目 3:创业实践项目
     */
    public static final Long PROJECT_TYPE_INNOVATION_TRAINING = 1L;
    public static final Long PROJECT_TYPE_STARTUP_TRAINING = 2L;
    public static final Long PROJECT_TYPE_STARTUP_PRACTICE = 3L;

    /**
     * 学院和专家组是否启动,状态(0正常 1停用)
     */
    public static final Long COLLEGE_GROUP_STATUS_NORMAL = 0L;
    public static final Long COLLEGE_GROUP_STATUS_DISABLE = 1L;
}

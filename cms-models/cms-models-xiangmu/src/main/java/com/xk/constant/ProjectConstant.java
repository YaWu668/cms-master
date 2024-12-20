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
}

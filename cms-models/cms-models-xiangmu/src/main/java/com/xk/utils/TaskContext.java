package com.xk.utils;

/**
 * 上下文工具类，用于判断是否为定时任务
 */
public class TaskContext {
    private static final ThreadLocal<Boolean> IS_TASK_CONTEXT = new ThreadLocal<>();

    // 设置为定时任务
    public static void setTaskContext(boolean isTask) {
        IS_TASK_CONTEXT.set(isTask);
    }

    // 判断是否为定时任务
    public static boolean isTaskContext() {
        return Boolean.TRUE.equals(IS_TASK_CONTEXT.get());
    }

    // 清理上下文
    public static void clear() {
        IS_TASK_CONTEXT.remove();
    }
}

package com.xk.job;

import com.xk.utils.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 使用定时任务案例
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MyScheduledTask {
//    private final YearTypeService yearTypeService;

    /**
     * 测试使用自动填充,来区分定时任务
     */
    @Async
    @Scheduled(fixedRate = 50000)
    public void executeAsyncTask() {
        try {
            TaskContext.setTaskContext(true); // 设置为定时任务上下文
            // 执行任务逻辑
//            YearType byId = yearTypeService.getById(1871121232058060805L);
//            yearTypeService.updateById(byId);
            log.info("定时任务执行成功");
        } finally {
            TaskContext.clear(); // 清理上下文
            log.info("定时任务执行结束");
        }
    }
}

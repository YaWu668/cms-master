package com.xk.job;

import com.cms.common.core.exception.ServiceException;
import com.xk.config.ProjectScheduleConfig;
import com.xk.constant.ProjectConstant;
import com.xk.entity.Project;
import com.xk.mapper.ProjectMapper;
import com.xk.service.ProjectScheduleService;
import com.xk.service.ProjectService;
import com.xk.utils.TaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerError;
import java.rmi.ServerException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用定时任务案例
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MyScheduledTask {
//    private final YearTypeService yearTypeService;
    /**
     * 项目mapper
     */
    private final ProjectMapper projectMapper;
    /**
     * 项目Service
     */
    private final ProjectService projectService;
    /**
     * 项目进度Service
     */
    private final ProjectScheduleService projectScheduleService;


    /**
     * 测试使用自动填充,来区分定时任务
     */
//    @Async
//    @Scheduled(fixedRate = 50000)
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

    /**
     * 项目启动时候也一直一次
     */
    @Async
    @Scheduled(cron = "0 * * * * *")
    //测试 5分钟执行一次
//    @Scheduled(cron ="0 * * * * *")
    @Transactional
    public void executeAsyncTask2() {
        log.info("开始执行定时任务");
        try {
            TaskContext.setTaskContext(true); // 设置为定时任务上下文
            //1.获取项目待结题的项目 项目结束时间<当前日期
            List<Project> projectList = projectMapper.getToBeCompletedProjectList();
            //2.判断列表是否为空
            if (projectList.isEmpty()){
                log.info("当前没有待结题项目");
                return;
            }
            //3.更新状态
            projectList.stream()
                            .forEach(e->{
                                //更新为待结题
                                e.setState(ProjectConstant.PROJECT_STATUS_PENDING_VALUE);
                            });
            //4.更新项目
            projectService.updateBatchById(projectList);
            //5.添加项目结题记录
            List<Long> ids = projectList.stream()
                    .map(e -> e.getProjectId())
                    .collect(Collectors.toList());
            boolean b = projectScheduleService.updateBatchByProjectId(ids);
            if (!b){
                throw new ServiceException("添加项目结题记录失败");
            }
            log.info("定时任务执行成功");
        } finally {
            TaskContext.clear(); // 清理上下文
            log.info("定时任务执行结束");
        }
    }
}


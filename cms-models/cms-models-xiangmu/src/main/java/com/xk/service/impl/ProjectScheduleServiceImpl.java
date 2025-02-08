package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.xk.config.ProjectScheduleConfig;
import com.xk.entity.ProjectSchedule;
import com.xk.mapper.ProjectScheduleMapper;
import com.xk.service.ProjectScheduleService;
import com.xk.utils.ProjectScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目进度记录表(ProjectSchedule)表服务实现类
 *
 * @author makejava
 * @since 2024-12-07 01:10:18
 */
@Service
@RequiredArgsConstructor
public class ProjectScheduleServiceImpl extends ServiceImpl<ProjectScheduleMapper, ProjectSchedule> implements ProjectScheduleService {
    /**
     * 项目进度记录表(ProjectSchedule)表服务实现类
     */
    private final ProjectScheduleConfig projectScheduleConfig;
    @Override
    public ProjectSchedule getLastByProjectId(Long projectId) {
        //1.根项目id进行获
        List<ProjectSchedule> projectSchedules = listByProjectId(projectId);
        //2.根据链表递归获取最后一条记录
        return ProjectScheduleUtils.getLastByProjectId(projectId, projectSchedules);
    }


    @Override
    public List<ProjectSchedule> listByProjectId(Long projectId) {
        //1.根项目id进行获
        LambdaQueryWrapper<ProjectSchedule> queryWrapper = new LambdaQueryWrapper<ProjectSchedule>()
                .eq(ProjectSchedule::getProjectId, projectId);
        List<ProjectSchedule> list = this.list(queryWrapper);
        if(list == null || list.size() <= 0){
            throw new ServiceException("项目id:"+projectId+"的项目进度记录表(ProjectSchedule)表不存在,表示当前项目不存在,先初始化项目再初始化进度记录表",400);
        }
        return list;
    }

    @Override
    public boolean updateBatchByProjectId(List<Long> ids) {
        ArrayList<ProjectSchedule> list = new ArrayList<>();
        ids.stream().forEach(e->{
            //1.获取最后一条记录
            ProjectSchedule lastByProjectId = this.getLastByProjectId(e);
            //2.更新最后一条记录
            ProjectSchedule projectSchedule = new ProjectSchedule()
                    .setProjectId(e)
                    .setRootId(lastByProjectId.getProjectScheduleId())
                    .setUserId(-1L)
                    .setContent(projectScheduleConfig.getSolveProblems());
            list.add(projectSchedule);
        });
        //3.批量插入
        boolean saveBatch = this.saveBatch(list);
        return saveBatch;
    }
}


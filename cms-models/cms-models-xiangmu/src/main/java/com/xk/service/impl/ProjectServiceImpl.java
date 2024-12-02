package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.Project;
import com.xk.mapper.ProjectMapper;
import com.xk.service.ProjectService;
import org.springframework.stereotype.Service;

/**
 * 项目表(Project)表服务实现类
 *
 * @author yawu
 */
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

}


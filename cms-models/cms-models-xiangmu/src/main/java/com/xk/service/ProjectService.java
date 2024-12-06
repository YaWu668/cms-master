package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.entity.Project;


/**
 * 项目表(Project)表服务接口
 *
 * @author yawu
 */
public interface ProjectService extends IService<Project> {
    /**
     * 申请项目
     * @param applyForDTO
     * @return
     */
    Response addProject(ApplyForDTO applyForDTO);
}


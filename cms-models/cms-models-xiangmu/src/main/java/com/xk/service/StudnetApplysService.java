package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.ApplyForDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.dto.TeacherSearchStuentDto;
import com.xk.domain.vo.student.StudentVo;
import com.xk.entity.StudnetApplys;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 学生报名表(StudnetApplys)表服务接口
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
public interface StudnetApplysService extends IService<StudnetApplys> {
    /**
     * 获取用户自己报名的项目id
     * @return
     */
    List<Long> getUserApplyListId();

    /*PageDTO<StudentVo> getBindStudent(TeacherSearchStuentDto teacherSearchStuentDto);*/

}


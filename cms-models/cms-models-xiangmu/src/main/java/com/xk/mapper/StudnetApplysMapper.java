package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.StudnetApplys;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 学生报名表(StudnetApplys)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
public interface StudnetApplysMapper extends BaseMapper<StudnetApplys> {

    @Select("SELECT DISTINCT member_id_value " +
            "FROM xm_project " +
            "JOIN JSON_TABLE( " +
            "       member_id, " +
            "       '$[*]' COLUMNS ( " +
            "         member_id_value BIGINT PATH '$' " +
            "       ) " +
            "     ) AS member_ids " +
            "WHERE JSON_CONTAINS(teacher_id, CAST(#{TeacherId} AS JSON), '$') and del_flag = 0;")
    List<Long> getMyBindStudentId(Long TeacherId);

}


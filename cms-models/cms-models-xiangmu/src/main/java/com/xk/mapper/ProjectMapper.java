package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.domain.vo.Execl.ProjectStatistics;
import com.xk.entity.Project;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 项目表(Project)表数据库访问层
 *
 * @author yawu
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 获取可以结题的项目列表,要求项目状态为项目进行中,并且项目结束时间<当前日期
     */
    @Select("SELECT * FROM xm_project WHERE state = 2 AND end_time < CURDATE()")
    List<Project> getToBeCompletedProjectList();

    /**
     * 根据年度id查询聚合数据
     * @param yearGroupId
     * @return
     */
    @Select("SELECT\n" +
            "    (SELECT `name`\n" +
            "     FROM xm_year_group\n" +
            "     WHERE year_group_id = #{yearGroupId}) AS yearly,\n" +
            "\n" +
            "    (SELECT SUM(total_money)\n" +
            "     FROM xm_project\n" +
            "     WHERE year_group_id = #{yearGroupId}\n" +
            "       AND state >= 2\n" +
            "       AND del_flag = 0) AS totalMoneyAll,\n" +
            "\n" +
            "    (SELECT COUNT(DISTINCT xsa.user_id)\n" +
            "     FROM xm_project xp\n" +
            "     JOIN xm_studnet_applys xsa ON xp.project_id = xsa.project_id\n" +
            "     WHERE xp.year_group_id = #{yearGroupId}\n" +
            "       AND xp.state >= 2\n" +
            "       AND xp.del_flag = 0) AS participantsAll,\n" +
            "\n" +
            "    (SELECT COUNT(*)\n" +
            "     FROM xm_project\n" +
            "     WHERE year_group_id = #{yearGroupId}\n" +
            "       AND state >= 2\n" +
            "       AND del_flag = 0) AS validProjectsAll,\n" +
            "\n" +
            "    (SELECT COUNT(*)\n" +
            "     FROM xm_project\n" +
            "     WHERE year_group_id = #{yearGroupId}\n" +
            "       AND state >= 2\n" +
            "       AND type = 1\n" +
            "       AND del_flag = 0) AS validProjectsInnovation,\n" +
            "\n" +
            "    (SELECT COUNT(DISTINCT xsa.user_id)\n" +
            "     FROM xm_project xp\n" +
            "     JOIN xm_studnet_applys xsa ON xp.project_id = xsa.project_id\n" +
            "     WHERE xp.year_group_id = #{yearGroupId}\n" +
            "       AND xp.state >= 2\n" +
            "       AND xp.type = 1\n" +
            "       AND xp.del_flag = 0) AS participantsInnovation,\n" +
            "\n" +
            "    (SELECT COUNT(*)\n" +
            "     FROM xm_project\n" +
            "     WHERE year_group_id = #{yearGroupId}\n" +
            "       AND state >= 2\n" +
            "       AND type != 1\n" +
            "       AND del_flag = 0) AS validProjectsEntrepreneurship,\n" +
            "\n" +
            "    (SELECT COUNT(DISTINCT xsa.user_id)\n" +
            "     FROM xm_project xp\n" +
            "     JOIN xm_studnet_applys xsa ON xp.project_id = xsa.project_id\n" +
            "     WHERE xp.year_group_id = #{yearGroupId}\n" +
            "       AND xp.state >= 2\n" +
            "       AND xp.type != 1\n" +
            "       AND xp.del_flag = 0) AS participantsEntrepreneurship,\n" +
            "\n" +
            "    (SELECT SUM(total_money)\n" +
            "     FROM xm_project\n" +
            "     WHERE year_group_id = #{yearGroupId}\n" +
            "       AND state >= 2\n" +
            "       AND type != 1\n" +
            "       AND del_flag = 0) AS totalMoneyEntrepreneurship")
    ProjectStatistics getProjectStatistics(@Param("yearGroupId") Long yearGroupId);
}


package com.xk.entity;

import cn.hutool.json.JSONUtil;
import com.cms.common.core.exception.ServiceException;
import com.xk.domain.dto.ApplyForStudent;
import com.xk.domain.dto.ApplyForTeacher;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用查询出来老师和学生进行封装的Pojo类
 */
@Data
@Accessors(chain = true)
public class StudentsAndTeacherListPojo {
    /**
     * 学生列表
     */
    private List<StudnetApplys> students;
    /**
     * 老师列表
     */
    private List<TeacherApplys> teachers;

    /**
     * 获取负责人（isPrincipal==1）的 userId，负责人只能有一个且必须排在 students 列表第一位
     *
     * @return 负责人 userId
     * @throws ServiceException 如果列表为空、负责人不唯一或第一个成员不是负责人
     */
    public Long getPrincipalUserId() {
        if (students == null || students.isEmpty()) {
            throw new ServiceException("学生列表不能为空，且至少要有一名负责人");
        }
        // 统计负责人数量
        long principalCount = students.stream()
                .filter(s -> Long.valueOf(1L).equals(s.getIsPrincipal()))
                .count();
        if (principalCount != 1) {
            throw new ServiceException("学生列表中必须且只能有一名负责人，当前发现 " + principalCount + " 人");
        }
        //校验排序
        StudnetApplys first = students.get(0);
        if (!Long.valueOf(1L).equals(first.getIsPrincipal())) {
            throw new ServiceException("负责人必须排在 students 列表的第一个位置");
        }
        return first.getUserId();
    }

    //-------企业指导老师----------

    /**
     * 提取所有企业指导老师用户ID数组。
     * @return List<Long> 包含所有 userId 的 Long 数组；若没有企业指导老师则返回长度为 0 的数组
     */
    public List<Long> getFirmTeacher(){
        if (teachers == null || teachers.isEmpty()) {
            return Collections.emptyList();
        }
        return teachers.stream()
                .filter(e->e.getIsTeacher().intValue()==1)
                .map(TeacherApplys::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 将企业指导老师用户ID列表序列化为 JSON 数组字符串
     * @return JSON 格式的字符串，例如 "[1001,1002,1003]"
     */
    public String getFirmTeacherJsonArray(){
        return JSONUtil.toJsonStr(getFirmTeacher());
    }

    //--------指导老师-------

    /**
     * 提取所有指导老师用户ID数组。
     * @exception ServiceException 如果列表为空,就抛出异常
     * @return List<Long> 包含所有 userId 的 Long 数组；
     */
    public List<Long> getTeacherUserIdList() {
        if (teachers == null || teachers.isEmpty()) {
            throw new ServiceException("指导老师列表不能为空");
        }
        return teachers.stream()
                .filter(e->e.getIsTeacher().intValue()==0)
                .map(TeacherApplys::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 将指导老师用户ID列表序列化为 JSON 数组字符串
     * @return JSON 格式的字符串，例如 "[1001,1002,1003]"
     */
    public String getTeacherUserIdJsonArray(){
        return JSONUtil.toJsonStr(getTeacherUserIdList());
    }


    //-------学生-------

    /**
     * 提取所有学生的用户ID数组。
     * @exception ServiceException 如果列表为空,就抛出异常
     * @return List<Long> 包含所有 userId 的 Long 数组；
     */
    public List<Long> getStudentUserIdList() {
        if (students == null || students.isEmpty()) {
            throw new ServiceException("学生列表不能为空");
        }
        return students.stream()
                .map(StudnetApplys::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 将学生用户ID列表序列化为 JSON 数组字符串。
     *
     * @return JSON 格式的字符串，例如 "[1001,1002,1003]"
     */
    public String getStudentUserIdJsonArray() {
        // 直接把 List<Long> 序列化成 "[1,2,3]" 这种格式
        return JSONUtil.toJsonStr(getStudentUserIdList());
    }
}

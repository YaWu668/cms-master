package com.xk.utils;

import com.cms.common.core.exception.ServiceException;
import com.xk.entity.ProjectSchedule;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目进度表工具类
 */
public class ProjectScheduleUtils {

    public static void main(String[] args) {
        // 创建测试数据
        List<ProjectSchedule> projectSchedules = new ArrayList<>();
        projectSchedules.add(new ProjectSchedule(1L, 101L, -1L, 1L, "初始状态"));
        projectSchedules.add(new ProjectSchedule(2L, 101L, 1L, 2L, "审核中"));
        projectSchedules.add(new ProjectSchedule(3L, 101L, 2L, 3L, "已完成"));

        // 测试获取最后一个节点的方法
//        ProjectSchedule lastSchedule = ProjectScheduleUtils.getLastByProjectId(101L, projectSchedules);
//
//        // 打印结果
//        if (lastSchedule != null) {
//            System.out.println("最后一个节点的内容: " + lastSchedule.getContent());
//        } else {
//            System.out.println("未找到最后一个节点");
//        }
        // 测试获取指定节点的方法
        ProjectSchedule node = getNodeByIndex(101L, projectSchedules, 2L);

        // 打印结果
        if (node != null) {
            System.out.println("指定节点的内容: " + node.getContent());
        } else {
            System.out.println("未找到指定节点");
        }


    }


    /**
     * 根据索引获取指定的节点
     * @param projectId 项目id
     * @param projectSchedules 项目进度记录表
     * @param index 要获取的节点索引（1表示第一个节点）
     * @return 指定的节点
     */
    public static ProjectSchedule getNodeByIndex(Long projectId, List<ProjectSchedule> projectSchedules, Long index) {
        // 过滤出指定项目的所有进度记录
        List<ProjectSchedule> filteredSchedules = projectSchedules.stream()
                .filter(schedule -> schedule.getProjectId().equals(projectId))
                .collect(Collectors.toList());

        if (filteredSchedules.isEmpty()) {
            throw new ServiceException("项目id:" + projectId + " 的项目进度记录表不存在，请先初始化项目",500);
        }

        // 构建以 project_schedule_id 为键的映射表
        Map<Long, ProjectSchedule> scheduleMap = filteredSchedules.stream()//key为project_schedule_id, value为ProjectSchedule对象
                .collect(Collectors.toMap(ProjectSchedule::getProjectScheduleId, schedule -> schedule));

        // 找到根节点
        ProjectSchedule current = filteredSchedules.stream()
                .filter(schedule -> schedule.getRootId() == -1)
                .findFirst()
                .orElseThrow(() -> new ServiceException("项目id:" + projectId + " 的根节点未找到",500));

        // 遍历链表找到指定索引的节点
        Long currentIndex = 1L;
        while (current != null && scheduleMap.containsKey(current.getProjectScheduleId())) {
            if (currentIndex.equals(index)) {
                return current;
            }
            Long nextId = current.getProjectScheduleId();
            ProjectSchedule next = filteredSchedules.stream()
                    .filter(schedule -> schedule.getRootId().equals(nextId))
                    .findFirst()
                    .orElse(null);

            current = next;
            currentIndex++;
        }
        // 如果遍历结束未找到索引对应的节点
        throw new ServiceException("项目id:" + projectId + " 的索引为" + index + "的节点未找到",500);
    }

    /**
     * 根据项目id获取最后一个节点
     * @param projectId 项目id
     * @param projectSchedules 项目进度记录表
     * @return 最后一个节点
     */
    public static ProjectSchedule getLastByProjectId(Long projectId, List<ProjectSchedule> projectSchedules) {
        // 过滤出指定项目的所有进度记录
        List<ProjectSchedule> filteredSchedules = projectSchedules.stream()
                .filter(schedule -> schedule.getProjectId().equals(projectId))
                .collect(Collectors.toList());

        if (filteredSchedules.isEmpty()) {
            throw new ServiceException("项目id:" + projectId + " 的项目进度记录表不存在，表示当前项目不存在，请先初始化项目",500);
        }

        // 构建以 project_schedule_id 为键的映射表
        Map<Long, ProjectSchedule> scheduleMap = filteredSchedules.stream()
                .collect(Collectors.toMap(ProjectSchedule::getProjectScheduleId, schedule -> schedule));

        // 找到根节点
        ProjectSchedule current = filteredSchedules.stream()
                .filter(schedule -> schedule.getRootId() == -1)
                .findFirst()
                .orElseThrow(() -> new ServiceException("项目id:" + projectId + " 的根节点未找到",500));

        // 循环根据父节点找到下一个节点, 直到没有下一个节点为止
        while (current != null && scheduleMap.containsKey(current.getProjectScheduleId())) {//循环条件: 当前节点存在且映射表中存在当前节点

            Long nextId = current.getProjectScheduleId();//父节点的id
            ProjectSchedule next = filteredSchedules.stream()
                    .filter(schedule -> schedule.getRootId().equals(nextId))//父节点的下一个节点
                    .findFirst()// 匹配首先出现的下一个节点
                    .orElse(null);// 没有下一个节点则返回null

            if (next == null) {//没有下一个节点则返回当前节点
                break;
            }
            current = next;//更新当前节点
        }
        return current;
    }
}

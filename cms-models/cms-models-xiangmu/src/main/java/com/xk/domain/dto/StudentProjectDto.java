package com.xk.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * 学生页面项目列表显示
 */
@Data
public class StudentProjectDto {
    //项目ID
    private Long projectId;
    //项目名称
    private String name;
    //负责人的id, 确定负责人
    private Long userId;
    //成员的id 会存储所有成员的id,存储是用户的id,不是成员表的 示例:[1, 2]
    private String memberId;
    //第一负责指导老师的id
//    private Long oneTeacherId;
    //指导老师的id 示例:[1, 2]
    private String teacherId;
    //企业老师的id 示例:[1, 2]
    private String firmTeacherId;
    //学院组的id
    private Long collegeGroupId;
    //专家组的id, 申请时候没有分配
    private Long specialistGroupId;
    //年度组的id
    private Long yearGroupId;
    //年度数据的id
    private Long yearDataId;
    //活动的开始时间
    private Date beginTime;
    //活动的结束时间
    private Date endTime;
    //状态序号 项目状态的值 0:未通过审核, 1:审核中 ,2:项目进行中 ,3:待结题 4:通过结题 ,5不通结题
    private Long state;
    //项目类型, (字典)示例: 1为创新训练项目, 2为创业训练项目, 3为创业实践
    private Long type;
    //项目级别 (字典)示例 : 1为国家级, 2为区级(省级),3为校级
    private Long projectRank;
    //学科类别 (字典)示例 1为工科 ,  2为文科
    private Long subjectCategory;
    //项目类别 (字典)示例 1为一般项目 2为重点支持领域项目
    private Long category;
    //当前项目审核状态进度, 看nacos配置角色审核项目顺序, 从1开始, 1代表需要序号为角色进行审核, 如果当前序号为0代表审核完毕
    private Long auditStatus;

}

package com.xk.entity;

import java.math.BigDecimal;
import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 项目表(Project)表实体类
 * @author yawu
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("xm_project")
@lombok.experimental.Accessors(chain = true)
public class Project  {
    //项目ID
    @TableId
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
    //状态序号表达式什么待定
    private Long state;
    //项目类型, (字典)示例: 1为创新训练项目, 2为创业训练项目, 3为创业实践
    private Long type;
    //项目级别 (字典)示例 : 1为国家级, 2为区级(省级),3为校级
    private Long projectRank;
    //学科类别 (字典)示例 1为工科 ,  2为文科
    private Long subjectCategory;
    //项目类别 (字典)示例 1为一般项目 2为重点支持领域项目
    private Long category;
    //项目来源 (字典)示例 看前端
    private Long projectSource;
    //项目简介 500字内
    private String projectSynopsis;
    //负责人曾经参与科研的情况
    private String principalExperience;
    //指导老师科研情况
    private String teacherExperience;
    //企业老师科研情况，仅创新训练类型项目可为 NULL
    private String firmTeacherExperience;
    //指导老师和企业老师对项目的支持情况, 仅创新训练类型项目是指导老师支持情况
    private String teacherSupport;
    //学生报名详细信息表的id 示例:[1, 2]
//    private String studentApplyId;
//    //老师报名详细信息表的id 示例:[1, 2]
//    private String teacherApplyId;
    //一.研究目的
    private String aOne;
    //二.研究内容
    private String aTwo;
    //三.国、内外研究现状和发展动态
    private String aThree;
    //四.创新点与项目特色
    private String aFour;
    //五.技术路线、拟解决的问题及预期成果
    private String aFive;
    //六.项目研究进度安排
    private String aSix;
    //七.1:	与本项目有关的研究积累和已取得的成绩
    private String aSeven;
    //七.2:已具备的条件，尚缺少的条件及解决方法
    private String aEight;
    //一.项目来源
    private String bOne;
    //二.行业及市场前景
    private String bTwo;
    //三.创新点与项目特色
    private String bThree;
    //四.生产或运营
    private String bFour;
    //五.投融资方案
    private String bFive;
    //六.管理模式
    private String bSix;
    //七.	风险预测及应对措施
    private String bSeven;
    //八.效益预测
    private String bEight;
    //一.实体运行机构名称或公司注册名称
    private String cOne;
    //二.创业计划书摘要
    private String cTwo;
    //三.项目背景
    private String cThree;
    //四.行业及市场前景
    private String cFour;
    //五.产品技术或服务内容
    private String cFive;
    //六.商业模式（创业过程、机会与商业分析）
    private String cSix;
    //七.团队组建
    private String cSeven;
    //八.财务情况
    private String cEight;
    //九.预期效益分析
    private String cNine;
    //十.投融资计划
    private String cTen;
    //十一.风险防范
    private String cEleven;
    //todo Bean拷贝可能有问题,手动传入 ,财政拨款的金额
    private BigDecimal fiscalAppropriation;
    //todo Bean拷贝可能有问题,手动传入 ,学校预算
    private BigDecimal schoolAllocation;
    //todo Bean拷贝可能有问题,手动传入 ,申请金额合计
    private BigDecimal totalMoney;
    //详细经费预算, 没有设计业务, 前端发什么样子json, 就存储什么样子
    private String budget;
    //指导老师的意见
    private String teacherOpinion;
    //企业老师的意见, 只有附加4没有这个选项
    private String firmTeacherOpinion;
    //学院的意见, 每个附加文档名称不一样
    private String collegeOpinion;
    //双创学院的意见, 每个附加名称都不一样
    private String creativityCollegeOpinion;
    //项目附加的URL地址, 提交只能提交压缩包
    private String materialsUrl;
    //项目结题文件url, 只能提交压缩包 示例: [URL1, URL2]
    private String concludeUrl;
    //当前项目审核状态进度, 看nacos配置角色审核项目顺序, 从1开始, 1代表需要序号为角色进行审核, 如果当前序号为0代表审核完毕
    private Integer auditStatus;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新者
    private String updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;



}

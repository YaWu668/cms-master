package com.xk.config;

import com.cms.common.core.exception.ServiceException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 项目进度状态变化配置类，支持热更新。
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xm.project-schedule-list")
public class ProjectScheduleConfig {

    /**
     * 第一次申请项目记录信息。
     */
    private String applyForProjectApproval;

    /**
     * 项目被所有角色审核通过记录信息。
     */
    private String passTheAudit;

    /**
     * 项目中途任何一个角色审核未通过记录信息。
     */
    private String noPassTheAudit;

    /**
     * 审核未通过，重新修改合规后，再次提交项目申请的记录信息。
     */
    private String newApply;

    /**
     * 定时任务刷新到待结题状态时的记录信息。
     */
    private String solveProblems;

    /**
     * 项目修改结题时间 记录信息
     */
    private String advanced;

    /**
     * 手动延期需要记录。
     */
    private String postpone;

    /**
     * 项目通过解题的记录信息。
     */
    private String pass;

    /**
     * 项目不通过解题的记录信息。
     */
    private String noPass;

    /**
     * 管理员撤回项目状态的记录信息。
     */
    private String withdraw;
    /**
     * 项目审核特殊情况一(项目申请时候,配置最多审核进度值为7,但是中途修改变小,为6时候就会转为审核成功,也会有审核记录和项目进度信息记录):
     */
    private String auditSpecialOne; //审核通过
    //审核不通过
    private  String auditSpecialTwo;


    /**
     * 热配置初始化<br>
     * 执行时机: 项目启动时 和 Nacos 配置变更时<br>
     */
    @PostConstruct
    public void validateConfig() {
        //非空校验
        if (applyForProjectApproval == null) {
            throw new ServiceException("配置项:xm.project-schedule-list.applyForProjectApproval不能为空,请检nacos配置,或联系管理员!",500);
        }
        if (passTheAudit == null) {
            throw new ServiceException("配置项:xm.project-schedule-list.passTheAudit不能为空,请检nacos配置,或联系管理员!",500);
        }
        if (noPassTheAudit == null) {
            throw new ServiceException("配置项:xm.project-schedule-list.noPassTheAudit不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(newApply == null){
            throw new ServiceException("配置项:xm.project-schedule-list.newApply不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(solveProblems == null){
            throw new ServiceException("配置项:xm.project-schedule-list.solveProblems不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(advanced == null){
            throw new ServiceException("配置项:xm.project-schedule-list.advanced不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(postpone == null){
            throw new ServiceException("配置项:xm.project-schedule-list.postpone不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(pass == null){
            throw new ServiceException("配置项:xm.project-schedule-list.pass不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(noPass == null){
            throw new ServiceException("配置项:xm.project-schedule-list.noPass不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(withdraw == null){
            throw new ServiceException("配置项:xm.project-schedule-list.withdraw不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(auditSpecialOne == null){
            throw new ServiceException("配置项:xm.project-schedule-list.auditSpecialOne不能为空,请检nacos配置,或联系管理员!",500);
        }
        if(auditSpecialTwo == null){
            throw new ServiceException("配置项:xm.project-schedule-list.auditSpecialTwo不能为空,请检nacos配置,或联系管理员!",500);
        }
    }
}

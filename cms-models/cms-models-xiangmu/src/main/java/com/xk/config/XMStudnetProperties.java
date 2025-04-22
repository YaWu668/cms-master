package com.xk.config;

import cn.hutool.core.collection.CollUtil;
import com.cms.common.core.exception.ServiceException;
import com.xk.service.DictDataService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 学生热更新配置项
 */
@Data
@Component
@RefreshScope
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "xm.studnet")
public class XMStudnetProperties {
    /**
     * 字典数据服务
     */
    private final DictDataService dictDataService;
    /**
     * 学生最大报名人数 , key(项目类型,字典): value(报名人数),<br>
     * 1表示 创新训练项目 :5 表示报名最大报名人数5
     */
    private Map<Long, Long> maxApplys;
    /**
     *     项目类型 是否需要企业老师的支持  <br>
     *     key(字典类型的): vlaue(0不需要企业老师,1需要企业)
     */
    private Map<Long, Long> teacherBusinessGuidance;

    /**
     * 项目级别的分数设置 1代表国家级 2代表区级 3代表校级
     */
    private Map<Long,String> projectType;


    /**
     * 热配置初始化<br>
     * 执行时机: 项目启动时 和 Nacos 配置变更时<br>
     */
    @PostConstruct
    public void validateConfig() {
        //非空校验
        validateNotNull();

        // 校验 `maxApplys` 和 `teacherBusinessGuidance` 的键集合是否一致
        if (!maxApplys.keySet().equals(teacherBusinessGuidance.keySet())) {
            throw new IllegalArgumentException("配置项: xm.studnet.maxApplys 和 xm.studnet.teacherBusinessGuidance 的键集合必须一致，请检查 Nacos 配置");
        }

        //校验字典:项目类型(字典值)是否存在
        for(Long key:maxApplys.keySet()){
            if (!dictDataService.checkDictData(key,"xm_item_type")){
                throw new ServiceException("配置项:xm.studnet.maxApplys 中项目类型: "+key+" 不存在，请检查 Nacos 配置，请联系管理员",500);
            }
        }

        //校验字典:项目类型(字典值)是否存在
        for(Long key:teacherBusinessGuidance.keySet()){
            if (!dictDataService.checkDictData(key,"xm_item_type")){
                throw new ServiceException("配置项:xm.studnet.teacherBusinessGuidance 中项目类型: "+key+" 不存在，请检查 Nacos 配置，请联系管理员",500);
            }
        }

        //校验字典: 学生最大报名人数的value值是否为>0
        for(Long value:maxApplys.values()){
            if (value<=0){
                throw new ServiceException("配置项:xm.studnet.maxApplys 中学生最大报名人数: "+value+" 必须大于0，请检查 Nacos 配置，请联系管理员",500);
            }
        }
        //校验字典: 企业老师的支持的value值是否为0或1
        for(Long value:teacherBusinessGuidance.values()){
            if (value!=0 && value!=1){
                throw new ServiceException("配置项:xm.studnet.teacherBusinessGuidance 中是否需要企业老师的支持: "+value+" 必须为0或1，请检查 Nacos 配置，请联系管理员",500);
            }
        }

        //校验 项目级别的分数设置 的字典类型存在
        for(Long key:projectType.keySet()){
            if (!dictDataService.checkDictData(key,"xm_item_rank")){
                throw new ServiceException("配置项:xm.studnet.projectType 中项目级别: "+key+" 不存在，请检查 Nacos 配置，请联系管理员",500);
            }
        }

        //todo 项目级别的分数设置,必须是数字

    }

    /**
     * 非空校验
     */
    private void validateNotNull() {
        if(maxApplys == null ) {
            throw new ServiceException("配置项:xm.studnet.maxApplys 不能为空，请检查 Nacos 配置，请联系管理员",500);
        }
        if(teacherBusinessGuidance == null) {
            throw new ServiceException("配置项:xm.studnet.teacherBusinessGuidance 不能为空，请检查 Nacos 配置，请联系管理员", 500);
        }
        if (projectType == null || CollUtil.isEmpty(projectType)) {
            throw new ServiceException("配置项:xm.studnet.projectType 不能为空，请检查 Nacos 配置，请联系管理员", 500);
        }
    }
}

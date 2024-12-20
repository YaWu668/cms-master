package com.xk.config;

import com.cms.common.core.exception.ServiceException;
import com.xk.service.DictDataService;
import com.xk.service.RoleService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 项目热更配置
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xm.auditconfig")
public class XMAuditConfig {
    /**
     * 角色服务
     */
    private final RoleService roleService;
    /**
     * 字典数据服务
     */
    private final DictDataService dictDataService;

    /**
     * 不同类型的项目审核流程配置<br>
     * key: 项目类型（与字典数值对应）<br>
     * value: 审核流程列表
     */
    private Map<Long, List<AuditRole>> projectAuditTypes;

    /**
     *  数组元素对象的属性解释, order属性(审核顺序只能从1开始,并且order值递增为1) <br>
     *  name属性(进行审核角色的标识符,一定要跟角色表标识符一致)
     */
    @Data
    @Accessors(chain = true)
    public static class AuditRole {
        /**
         * 审核顺序,从1开始,并且order值递增为1
         */
        private Long order;

        /**
         * 审核角色名称（与角色表标识符一致）
         */
        private String name;
    }

    /**
     * 热配置初始化<br>
     * 执行时机: 项目启动时 和 Nacos 配置变更时<br>
     */
    @PostConstruct
    public void validateConfig() {
        //非空校验
        validateNotNull();

        //校验projectAuditTypes的key是否与字典表中项目类型的值一致
        projectAuditTypes.forEach((key, value) -> {
            //校验projectAuditTypes的key是否存在于字典表中
            validateKey(key);
            //校验projectAuditTypes的vlaue集合是否为空,value集合个数>2并且每个元素里面的属性不能为空,还是校验角色是否存在
            validateValue(key, value);
            //校验projectAuditTypes的vlaue集合的的order是否从1开始,并且order值递增为1
            validateOrder(key, value);

        });


    }

    /**
     * 校验projectAuditTypes的value集合的的order是否从1开始,并且order值递增为1
     * @param key 项目类型的key
     * @param value 项目类型的审核流程列表
     * @throws ServiceException 项目类型为空时抛出ServiceException
     */
    private void validateOrder(Long key, List<AuditRole> value) {
        value.forEach(auditRole -> {
            if(auditRole.getOrder() == null || auditRole.getOrder() < 1){
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+ key +" 的审核流程的order值必须从1开始，请检查 Nacos 配置，请联系管理员",500);
            }
            if(auditRole.getOrder() != value.indexOf(auditRole) + 1){ // 返回当前元素在列表中的索引值加1,如果不等于order值,则说明order值不符合递增规则
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+ key +" 的审核流程的order值必须从1开始，并且order值递增为1，请检查 Nacos 配置，请联系管理员",500);
            }
        });
    }

    /**
     * 校验projectAuditTypes的value集合是否为空,value集合个数>=1并且每个元素里面的属性不能为空<br>
     * 会校验vlaue集合的所有元素的属性不能为空(order和name属性)<br>
     * 会校验角色是否存在<br>
     * @param key 项目类型的key
     * @param value 项目类型的审核流程列表
     * @throws ServiceException 项目类型为空时抛出ServiceException
     *
     */
    private void validateValue(Long key, List<AuditRole> value) {
        if(value == null || value.isEmpty()){
            throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程不能为空，请检查 Nacos 配置，请联系管理员",500);
        }

        if(value.size()  <2){//这个不可以删审批流程不然会出现bug
            throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程个数必须大于等于2，请检查 Nacos 配置，请联系管理员",500);
        }
        value.forEach(auditRole -> {
            //校验order和name属性是否为空
            if(auditRole.getOrder() == null){
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程的order属性不能为空，请检查 Nacos 配置，请联系管理员",500);
            }
            if(auditRole.getName() == null || auditRole.getName().isEmpty()){
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程的name属性不能为空，请检查 Nacos 配置，请联系管理员",500);
            }
            //校验order属性是否从1开始和name属性是否存在于角色表中
            if(auditRole.getOrder() <1L){
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程的order属性必须从1开始，请检查 Nacos 配置，请联系管理员",500);
            }
            //校验角色是否存在
            if(!roleService.existRole(auditRole.getName())){
                throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 的审核流程的name属性: "+auditRole.getName()+" 不存在，请检查 Nacos 配置，请联系管理员",500);
            }
        });

    }

    /**
     * 项目类型字典表中是否存在该key
     * @param key 项目类型的key
     * @throws ServiceException 项目类型为空时抛出ServiceException
     */
    private void validateKey(Long key) {
        if(!dictDataService.checkDictData(key,"xm_item_type")){
            throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes 中项目类型: "+key+" 不存在，请检查 Nacos 配置，请联系管理员",500);
        }
    }

    /**
     * 校验配置项是否为空
     */
    private void validateNotNull() {
        if(projectAuditTypes ==null){
            throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes不能为空",500);
        }
        if (projectAuditTypes.isEmpty()){
            throw new ServiceException("配置项:xm.auditconfig.projectAuditTypes不能为空",500);
        }
    }
}

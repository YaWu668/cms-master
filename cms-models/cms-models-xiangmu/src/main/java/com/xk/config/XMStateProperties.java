package com.xk.config;

import com.cms.common.core.exception.ServiceException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 项目状态热更新配置类
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xm.state")
public class XMStateProperties {
    /**
     *   0:未通过审核, 1:审核中 ,2:项目进行中 ,3:待结题 4:通过结题 ,5不通结题<br>
     * 项目状态列表,从小到大排序开始
     */
    private List<Long> stateList;

    /**
     * 热配置初始化<br>
     * 执行时机: 项目启动时 和 Nacos 配置变更时<br>
     */
    @PostConstruct
    public void validateConfig() {
        if(stateList == null || stateList.isEmpty()){
            throw new ServiceException("配置项:xm.state.stateList不能为空,请检查配置项,联系管理员",500);
        }
        if(stateList.size() !=6){
            throw new ServiceException("配置项:xm.state.stateList的元素个数必须为6,请检查配置项,联系管理员",500);
        }
        //校验stateList元素必须从0开始,并且连续
        for(int i=0;i<stateList.size();i++){
            if(i!= stateList.get(i).intValue()){
                throw new ServiceException("配置项:xm.state.stateList的元素必须从0开始,并且连续,请检查配置项,联系管理员",500);
            }
        }
    }
}

package com.cms.common.datascope.domain;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限范围参数类
 *
 * @author 邓志军
 * @date 2024年6月3日20:39:27
 */
public class DataScopeEntity {

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

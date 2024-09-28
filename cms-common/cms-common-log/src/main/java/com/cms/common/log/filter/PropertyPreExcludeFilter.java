package com.cms.common.log.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

/**
 * 排除JSON敏感属性的过滤器
 * PropertyPreExcludeFilter 类继承自 SimplePropertyPreFilter，用于排除在 JSON 序列化时需要隐藏的敏感属性。
 *
 * @author 邓志军
 * @date 2024年5月28日23:45:31
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {

    /**
     * 默认构造函数
     */
    public PropertyPreExcludeFilter() {
    }

    /**
     * 添加需要排除的属性名
     *
     * @param filters 需要排除的属性名数组
     * @return 返回当前过滤器对象，方便链式调用
     */
    public PropertyPreExcludeFilter addExcludes(String... filters) {
        for (String filter : filters) {
            this.getExcludes().add(filter);
        }
        return this;
    }
}

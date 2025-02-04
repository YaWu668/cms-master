package com.xk.utils;

import com.cms.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

public class BeanUtils {

    /**
     *
     *
     * 检查一个对象中的所有字段是否都不为 null
     *
     * @param bean 要检查的对象
     * @return 如果所有字段都不为 null 返回 true，否则返回 false
     */
    public static boolean areAllFieldsNotNull(Object bean) {
        if (bean == null) {
            return false; // 如果对象本身为 null，返回 false
        }
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 设置私有字段可以访问
                field.setAccessible(true);
                // 获取字段值
                Object value = field.get(bean);
                if (value == null) {
                    field.setAccessible(false);// 访问完后恢复原值
                    return false; // 如果字段值为null，直接返回false
                }
                field.setAccessible(false);// 访问完后恢复原值
            }
            return true; // 所有字段都不为 null
        } catch (IllegalAccessException e) {
            throw new ServiceException("字段访问失败:"+e.getMessage(), 500);
        }
    }

    @Data
    @AllArgsConstructor
    public static class TestBean {
        private String field1;
        private Integer field2;
        private Object field3;

    }

    public static void main(String[] args) {
        // 测试用例1: 所有字段都不为 null
        TestBean bean1 = new TestBean("value1", 123, new Object());
        boolean result1 = BeanUtils.areAllFieldsNotNull(bean1);
        System.out.println("Test Case 1 - All fields not null: " + result1); // 预期输出: true

        // 测试用例2: 有一个字段为 null
        TestBean bean2 = new TestBean(null, 123, new Object());
        boolean result2 = BeanUtils.areAllFieldsNotNull(bean2);
        System.out.println("Test Case 2 - One field is null: " + result2); // 预期输出: false

        // 测试用例3: 所有字段都为 null
        TestBean bean3 = new TestBean(null, null, null);
        boolean result3 = BeanUtils.areAllFieldsNotNull(bean3);
        System.out.println("Test Case 3 - All fields are null: " + result3); // 预期输出: false

        // 测试用例4: 对象本身为 null
        TestBean bean4 = null;
        boolean result4 = BeanUtils.areAllFieldsNotNull(bean4);
        System.out.println("Test Case 4 - Object is null: " + result4); // 预期输出: false
    }

}



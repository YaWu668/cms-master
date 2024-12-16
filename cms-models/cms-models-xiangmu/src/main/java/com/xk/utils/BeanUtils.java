package com.xk.utils;

import lombok.Data;

import java.lang.reflect.Field;

public class BeanUtils {

    /**
     *
     * todo bug,全部为null才会true,看外面方法就知道了
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
//                    field.setAccessible(false);// 访问完后恢复原值
                    return false; // 如果字段值为null，直接返回false
                }
//                field.setAccessible(false);// 访问完后恢复原值
            }
            return true; // 所有字段都不为 null
        } catch (IllegalAccessException e) {
            throw new RuntimeException("字段访问失败", e);
        }
    }


}



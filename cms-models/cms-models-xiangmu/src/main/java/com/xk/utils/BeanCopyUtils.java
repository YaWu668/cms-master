package com.xk.utils;


import com.cms.common.core.exception.ServiceException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拷贝bean的工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils(){
    }

    /**
     *
     * 将一个JavaBean对象的属性值拷贝到另一个JavaBean对象中
     * @param source 源对象
     * @param targetType 目标对象类型
     * @param <V> 目标对象类型
     * @return 拷贝后的目标对象
     */
    public static <V> V copyBean(Object source,Class<V> targetType){
        //创建 目标对象
        V target= null;
        try {
            target= targetType.newInstance();
            //拷贝bean
            BeanUtils.copyProperties(source,target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回数据
        return target;
    }
    /**
     * 将一个JavaBean对象的属性值拷贝到另一个JavaBean对象中
     * @param source 源对象
     * @param targetBean 目标对象
     * @param <V> 目标对象类型
     *
     */
    public static <V> void copyBean(Object source,V targetBean){
        if (source==null || targetBean==null){
            throw new ServiceException("源对象或目标对象为空",500);
        }
        try {
            //拷贝bean
            BeanUtils.copyProperties(source,targetBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个 List 集合中的对象转化为另一个类型的 List 集合
     * @param sourceLists 源 List 集合
     * @param targetType 目标 List 中对象的类型
     * @param <V> 目标 List 中对象的类型
     * @param <T> 源 List 中对象的类型
     * @return 转化后的 List 集合
     */
    public  static <V,T> List<V> copyBeans(List<T> sourceLists ,Class<V> targetType){
        //获取Stream流
        return sourceLists.stream()
                .map(source->copyBean(source,targetType))//把源对象 转化为 目标对象
                .collect(Collectors.toList());//把流转为 List集合
    }

}

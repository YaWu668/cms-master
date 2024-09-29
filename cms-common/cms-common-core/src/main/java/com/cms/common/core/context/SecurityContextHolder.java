package com.cms.common.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.text.Convert;
import com.cms.common.core.utils.StringUtils;

/**
 * 获取当前线程变量中的 用户id、用户名称、Token等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 * <p>
 * SecurityContextHolder 类用于从当前线程变量中获取用户相关信息，例如用户ID、用户名、Token等。
 * 在网关通过请求头的方法传入用户信息，在 HeaderInterceptor 拦截器中设置值，然后可以通过 SecurityContextHolder 来获取。
 *
 * @author 邓志军
 * @date 2024年5月28日22:52:38
 */
public class SecurityContextHolder {
    // 使用 TransmittableThreadLocal 来存储线程局部变量，确保线程切换时能够正确传递用户信息
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置线程局部变量中指定键的值
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    /**
     * 获取线程局部变量中指定键的值
     *
     * @param key 键
     * @return 指定键的值，转换为字符串类型
     */
    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    /**
     * 获取线程局部变量中指定键的值，并按指定类型进行转换
     *
     * @param key   键
     * @param clazz 目标类型
     * @return 指定键的值，按指定类型进行转换
     */
    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    /**
     * 获取当前线程的局部变量Map对象，如果不存在则创建一个新的并设置到当前线程中
     *
     * @return 当前线程的局部变量Map对象
     */
    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    /**
     * 设置当前线程的局部变量Map对象
     *
     * @param threadLocalMap 要设置的局部变量Map对象
     */
    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    /**
     * 获取当前用户的ID
     *
     * @return 当前用户的ID，转换为Long类型，如果为空则返回0L
     */
    public static Long getUserId() {
        return Convert.toLong(get(SecurityConstants.DETAILS_USER_ID), 0L);
    }

    /**
     * 设置当前用户的ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        set(SecurityConstants.DETAILS_USER_ID, userId);
    }

    /**
     * 获取当前用户的用户名
     *
     * @return 当前用户的用户名
     */
    public static String getUserName() {
        return get(SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 设置当前用户的用户名
     *
     * @param username 用户名
     */
    public static void setUserName(String username) {
        set(SecurityConstants.DETAILS_USERNAME, username);
    }

    /**
     * 获取当前用户的Key
     *
     * @return 当前用户的Key
     */
    public static String getUserKey() {
        return get(SecurityConstants.USER_KEY);
    }

    /**
     * 设置当前用户的Key
     *
     * @param userKey 用户Key
     */
    public static void setUserKey(String userKey) {
        set(SecurityConstants.USER_KEY, userKey);
    }

    /**
     * 获取当前用户的权限信息
     *
     * @return 当前用户的权限信息
     */
    public static String getPermission() {
        return get(SecurityConstants.ROLE_PERMISSION);
    }

    /**
     * 设置当前用户的权限信息
     *
     * @param permissions 权限信息
     */
    public static void setPermission(String permissions) {
        set(SecurityConstants.ROLE_PERMISSION, permissions);
    }

    /**
     * 清除当前线程的局部变量
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}

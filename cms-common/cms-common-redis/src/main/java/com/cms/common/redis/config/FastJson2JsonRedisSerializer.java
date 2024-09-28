package com.cms.common.redis.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.cms.common.core.constant.Constants;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;

/**
 * Redis使用FastJson序列化
 * 使用FastJson2JsonRedisSerializer来序列化和反序列化Redis中的对象
 *
 * @author 邓志军
 * @date 2024年5月29日11:38:29
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 自动类型过滤器，用于防止FastJson反序列化时的自动类型漏洞
    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);

    private final Class<T> clazz;

    /**
     * 构造函数
     *
     * @param clazz 要进行序列化和反序列化的类的类型
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 将对象序列化为字节数组
     *
     * @param t 要序列化的对象
     * @return 序列化后的字节数组
     * @throws SerializationException 如果序列化失败
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 将字节数组反序列化为对象
     *
     * @param bytes 要反序列化的字节数组
     * @return 反序列化后的对象
     * @throws SerializationException 如果反序列化失败
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}

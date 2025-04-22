package com.xk.utils;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;  // renamed from Whitelist in jsoup 1.15.3
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS工具（适配 jsoup 1.15.3，现在使用 Safelist 替代 Whitelist）<br>
 *
 * 富文本中的 img 标签如果使用相对 URL（如 "/file/viewXm/..."），<br>
 * 可以在前端统一拼接完整域名和协议后再渲染。<br>
 * 后端清洗只允许常用标签和属性，并保留相对路径。<br>
 *
 * @author
 * @since
 */
public class XSSUtil {


    public static void main(String[] args) {
        // 原始富文本，包含相对路径的图片和一个恶意 onerror
        String rawHtml = "<p>11111<img src=\"/file/viewXm/2025/04/12/1744448552567de7674715cc4485293824a4444a89b7720240916215438_1.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"/file/viewXm/2025/04/12/17444485715782aa192ff8dd9428b872b0205f0043ee120240906212521_1.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p>";


        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^\"'>]+)[\"']?[^>]*>");
        Matcher matcher = pattern.matcher(rawHtml);
        while (matcher.find()) {
            System.out.println("图片地址：" + matcher.group(1));
        }

    }
}

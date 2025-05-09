package com.xk.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HtmlUtils {

    /**
     * 对于html文本中的src的url地址进行提取
     * @param html html文本
     * @return url集合
     */
    public static List<String> getUrlForSrc(String html) {
        return getUrl(html, CollUtil.toList("src"));
    }

    /**
     * 对于html文本中的href的url地址进行提取
     * @param html html文本
     * @return url集合
     */
    public static List<String> getUrlForHref(String html) {
        return getUrl(html, CollUtil.toList("href"));
    }

    /**
     * 对于html文本中的style的url地址进行提取
     * @param html html文本
     * @return url集合
     */
    public static List<String> getUrlForStyle(String html) {
        return getUrl(html, CollUtil.toList("style"));
    }

    /**
     * 对于html文本中的DataHref的url地址进行提取
     * @param html html文本
     * @return url集合
     */
    public static List<String> getUrlForDataHref(String html) {
        return getUrl(html, CollUtil.toList("data-href"));
    }

    /**
     * 对于html文本中的Poster的url地址进行提取
     * @param html html文本
     * @return url集合
     */
    public static List<String> getUrlForPoster(String html) {
        return getUrl(html, CollUtil.toList("style"));
    }

    /**
     * 对于html文本中其他指定属性标签的url地址进行提取
     * @param html html 文本
     * @param attributes 需要提取 URL 的属性名称列表
     * @return url 集合
     */
    public static List<String> getUrlsForAttributes(String html, List<String>  attributes) {
        return getUrl(html, attributes);
    }

    private static List<String> getUrl(String html, List<String>  attributes){
        if (StrUtil.isEmpty(html)){
            return null;
        }
        List<String> urls = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        for (String attribute : attributes) {
            // 提取所有带有指定属性的标签的 URL
            Elements elements = doc.select("[" + attribute + "]");
            for (Element element : elements) {
                urls.add(element.attr(attribute));
            }
        }
        //过滤空路径
        return urls.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }
}

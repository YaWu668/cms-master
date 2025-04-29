package com.xk.utils;

import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlUtils {
    /**
     * 对于html文本中的href和src属性的url地址进行提取
     * @param html html文本
     * @return url集合
     * @throws IOException IO异常
     */
    public static List<String> getUrls(String html) throws IOException {
        List<String> urls = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        // 提取所有带有 href 属性的标签的 URL
        Elements hrefElements = doc.select("[href]");
        for (Element element : hrefElements) {
            urls.add(element.attr("href"));
        }

        // 提取所有带有 src 属性的标签的 URL
        Elements srcElements = doc.select("[src]");
        for (Element element : srcElements) {
            urls.add(element.attr("src"));
        }

        //过滤空地址
        urls = urls.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        return urls;
    }

    /**
     * 对于html文本中其他指定属性标签的url地址进行提取
     * @param html html 文本
     * @param attributes 需要提取 URL 的属性名称列表
     * @return url 集合
     * @throws IOException IO 异常
     */
    public static List<String> getUrls(String html, List<String> attributes) throws IOException {
        List<String> urls = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        for (String attribute : attributes) {
            // 提取所有带有指定属性的标签的 URL
            Elements elements = doc.select("[" + attribute + "]");
            for (Element element : elements) {
                urls.add(element.attr(attribute));
            }
        }

        // 过滤空地址
        urls = urls.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        return urls;
    }
}

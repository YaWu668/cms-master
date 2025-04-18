package com.xk.check.Validator;

import com.xk.check.annotations.RichText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichTextValidator implements ConstraintValidator<RichText, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            // 如果允许空值，可返回 true；否则返回 false
            return true; // 或根据需求调整为 false
        }
        return isValidRichText(value);
    }


    private static final Set<String> VOID_TAGS = new HashSet<>(Arrays.asList(
            "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta",
            "param", "source", "track", "wbr"
    ));

    public  boolean isValidRichText(String html) {
        List<String> tags = extractTags(html);
        Deque<String> tagStack = new ArrayDeque<>();

        for (String tag : tags) {
            if (isCommentOrDoctype(tag)) {
                continue;
            }

            if (isEndTag(tag)) {
                if (tagStack.isEmpty()) {
                    return false;
                }
                String expectedTag = tagStack.pop();
                String actualTag = parseEndTagName(tag);
                if (!expectedTag.equalsIgnoreCase(actualTag)) {
                    return false;
                }
            } else if (isSelfClosingTag(tag)) {
                String tagName = parseTagName(tag);
                if (!VOID_TAGS.contains(tagName.toLowerCase())) {
                    return false;
                }
            } else {
                String tagName = parseTagName(tag);
                if (!VOID_TAGS.contains(tagName.toLowerCase())) {
                    tagStack.push(tagName);
                }
            }
        }

        return tagStack.isEmpty();
    }

    private static List<String> extractTags(String html) {
        List<String> tags = new ArrayList<>();
        Matcher matcher = Pattern.compile("<(?!/?(?:!--|\\[CDATA\\[|DOCTYPE))([^>]++)>", Pattern.CASE_INSENSITIVE).matcher(html);
        while (matcher.find()) {
            tags.add(matcher.group());
        }
        return tags;
    }

    private  boolean isCommentOrDoctype(String tag) {
        return tag.startsWith("<!--") || tag.startsWith("<![CDATA[") || tag.startsWith("<!DOCTYPE");
    }

    private  boolean isEndTag(String tag) {
        return tag.startsWith("</");
    }

    private  boolean isSelfClosingTag(String tag) {
        return tag.endsWith("/>");
    }

    private  String parseEndTagName(String endTag) {
        return endTag.substring(2, endTag.length() - 1).trim().split("\\s+", 2)[0].toLowerCase();
    }

    private  String parseTagName(String tag) {
        String content = tag.substring(1, tag.length() - 1).trim().split("\\s+", 2)[0];
        return content.replace("/", "").toLowerCase();
    }

}

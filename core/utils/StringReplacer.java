package com.coloryr.allmusic.server.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StringReplacer {
    private Map<String, String> literalMap = new LinkedHashMap<>();
    private Map<Pattern, String> regexMap = new LinkedHashMap<>();

    // 自动判断是否为正则表达式
    public void put(String search, String replacement) {
        if (isRegex(search)) {
            regexMap.put(Pattern.compile(search), replacement);
        } else {
            literalMap.put(search, replacement);
        }
    }

    // 判断字符串是否包含正则表达式特征
    private boolean isRegex(String str) {
        return str.matches(".*[.\\[\\]{}(),*+?|^$\\\\].*");
    }

    // 强制添加为正则表达式
    public void putRegex(String regex, String replacement) {
        regexMap.put(Pattern.compile(regex), replacement);
    }

    // 强制添加为普通字符串
    public void putLiteral(String literal, String replacement) {
        literalMap.put(literal, replacement);
    }

    // 执行替换
    public String replace(String input) {
        String result = input;

        // 先替换普通字符串（更快）
        for (Map.Entry<String, String> entry : literalMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        // 再替换正则表达式
        for (Map.Entry<Pattern, String> entry : regexMap.entrySet()) {
            result = entry.getKey().matcher(result).replaceAll(entry.getValue());
        }

        return result;
    }
}

package com.wuxp.querydsl.core.codegen.util;


import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理java method name
 *
 * @author wuxp
 */
public final class JavaMethodNameUtils {

    private static final String IS_GET_METHOD_NAME = "^get[A-Z]+\\w*";

    private static final String IS_IS_METHOD_NAME = "^is[A-Z]+\\w*";

    private static final Pattern TO_LINE_REGEXP = Pattern.compile("([A-Z]+)");

    public static final String LINE_CHAR = "_";

    /**
     * 用于查询整个对象的关键字
     */
    private static final String[] FIND_ENTITY_KEYWORDS = new String[]{"find", "get", "query", "read"};

    /**
     * 用于查询某些列的关键字
     */
    private static final String[] SELECT_COLUMN_KEYWORDS = new String[]{"select"};

    /**
     * 用于统计的关键字
     */
    private static final String[] COUNT_COLUMN_KEYWORDS = new String[]{"count"};


    private JavaMethodNameUtils() {
    }

    /**
     * 是否为get方法或is 方法
     *
     * @param methodName 方法名称
     * @return 是否为 get or is 方法
     */
    public static boolean isGetMethodOrIsMethod(String methodName) {
        if (methodName == null) {
            return false;
        }
        return Pattern.matches(IS_GET_METHOD_NAME, methodName) || Pattern.matches(IS_IS_METHOD_NAME, methodName);
    }


    /**
     * 驼峰格式的字符串转下划线
     *
     * @param str 驼峰格式内容
     * @return 驼峰格式的字符串转下划线的字符串内容
     * @link http://ifeve.com/google-guava/
     * CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
     */
    public static String humpToLine(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }

        StringBuffer text = new StringBuffer(str);
        Matcher matcher = TO_LINE_REGEXP.matcher(text);
        while (matcher.find()) {
            String replaceText = MessageFormat.format("{0}{1}", LINE_CHAR, text.substring(matcher.start(0), matcher.end(0)));
            text.replace(matcher.start(0), matcher.end(0), replaceText.toLowerCase());
            matcher = TO_LINE_REGEXP.matcher(text);
        }
        String result = text.toString();
        if (result.startsWith(LINE_CHAR)) {
            return result.substring(1);
        }
        return result;
    }


}

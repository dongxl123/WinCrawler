package com.winbaoxian.crawler.constant;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 18:10
 */
public interface WinCrawlerConstant {
    String OPEN_PAREN = "(";
    String CLOSE_PAREN = ")";
    String OPEN_BRACE = "{";
    String CLOSE_BRACE = "}";
    String OPEN_BRACKET = "[";
    String CLOSE_BRACKET = "]";
    String CHAR_DOT = ".";
    String CHAR_COMMA = ",";
    String CHAR_COLON = ":";
    String CHAR_SEMICOLON = ";";
    String CHAR_SPACE = " ";
    String CHAR_RIGHT_ARROW = "→";
    String CHAR_DOUBLE_QUOTA = "\"";
    String CHAR_SINGLE_QUOTA = "'";
    String CHAR_SEPARATOR = "/";
    String CHAR_RIGHT_SLASH = "\\";
    String CHAR_AND = "&";
    String CHAR_EQUAL = "=";
    String FREEMARKER_VARIABLE_PREFIX = "${";
    String FREEMARKER_CONDITION_PREFIX = "<#";
    String LINE_SEPARATOR = System.getProperty("line.separator").intern();
    String LOG_PREFIX_STEP_TPL = "%s<%s>:%s";

    String HTTP_HEADER_COOKIE = "Cookie";
    String LOG_TITLE_START = "start";
    String LOG_TITLE_END = "end";
    String LOG_TITLE_GLOBAL_PARAMS_CONFIG = "获取全局配置";

    String STRING_HTTP = "http";
    String CONSOLE_LOG_END_STRING = "(*^▽^*)";

    int TPL_CACHE_LOCK_TIMEOUT = 60 * 10;

    String SYSTEM_CONFIG_VALUE_TRUE = "1";
    String SYSTEM_CONFIG_VALUE_FALSE = "0";
    String IMAGE_DATA_PREFIX = "data:";
}

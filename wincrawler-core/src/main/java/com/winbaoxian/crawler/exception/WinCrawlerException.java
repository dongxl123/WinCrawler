package com.winbaoxian.crawler.exception;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:54
 */
public class WinCrawlerException extends RuntimeException {

    public WinCrawlerException(String message) {
        super(message);
    }

    public WinCrawlerException(Throwable cause) {
        super(cause);
    }

    public WinCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }
}

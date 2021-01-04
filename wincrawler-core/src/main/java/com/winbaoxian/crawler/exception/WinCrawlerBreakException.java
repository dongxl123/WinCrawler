package com.winbaoxian.crawler.exception;

/**
 * @author dongxuanliang252
 * @date  2019-7-24 10:28:30
 */
public class WinCrawlerBreakException extends RuntimeException {

    public WinCrawlerBreakException(String message) {
        super(message);
    }

    public WinCrawlerBreakException(Throwable cause) {
        super(cause);
    }

    public WinCrawlerBreakException(String message, Throwable cause) {
        super(message, cause);
    }
}

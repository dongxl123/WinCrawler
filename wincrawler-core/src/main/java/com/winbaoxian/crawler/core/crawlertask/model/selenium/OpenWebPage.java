package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenWebPage extends AbstractSeleniumStep {

    private static String DEFAULT_STEP_NAME = "打开网页";
    /**
     * 页面url
     */
    private String url;
    /**
     * 超时时间, 单位：毫秒
     */
    private Long timeOut;

    public OpenWebPage() {
        setName(DEFAULT_STEP_NAME);
    }
}

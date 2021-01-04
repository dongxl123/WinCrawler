package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FetchCookie extends AbstractSeleniumStep {

    private static String DEFAULT_STEP_NAME = "获取Cookie";

    private String alias;

    public FetchCookie() {
        setName(DEFAULT_STEP_NAME);
    }

}

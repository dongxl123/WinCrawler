package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SetCookie extends AbstractSeleniumStep {

    private static String DEFAULT_STEP_NAME = "设置Cookie";

    private String cookies;

    public SetCookie() {
        setName(DEFAULT_STEP_NAME);
    }

}

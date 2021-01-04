package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Click extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "点击元素";
    /**
     * 是否有alter框
     */
    private Boolean hasAlert = Boolean.FALSE;

    public Click() {
        setName(DEFAULT_STEP_NAME);
    }
}

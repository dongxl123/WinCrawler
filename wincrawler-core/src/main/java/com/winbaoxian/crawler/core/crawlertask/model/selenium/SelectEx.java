package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectEx extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "下拉框选择EX";

    private String selectXPath;

    public SelectEx() {
        setName(DEFAULT_STEP_NAME);
    }

}

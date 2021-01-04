package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputText extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "输入文字";
    /**
     * 输入的文本
     */
    private String value;

    public InputText() {
        setName(DEFAULT_STEP_NAME);
    }

}

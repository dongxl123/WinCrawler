package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import com.winbaoxian.crawler.model.core.SelectConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Select extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "下拉框单选";

    /**
     * 选择配置
     */
    private SelectConfig selectConfig;

    public Select() {
        setName(DEFAULT_STEP_NAME);
    }

}

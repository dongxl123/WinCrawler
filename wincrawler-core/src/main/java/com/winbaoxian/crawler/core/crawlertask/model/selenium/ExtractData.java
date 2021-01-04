package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ExtractData extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "提取数据";
    /**
     * 数据提取规则
     */
    private List<ExtractRule> ruleList;

    public ExtractData() {
        setName(DEFAULT_STEP_NAME);
    }

    @Setter
    @Getter
    public static class ExtractRule implements Serializable {
        private String name;
        private String rule;
    }
}

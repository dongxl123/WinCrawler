package com.winbaoxian.crawler.core.crawlertask.model.logic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IfGroup extends AbstractGroupStep {

    private static String DEFAULT_STEP_NAME = "IF判断";
    /**
     * 判断条件
     */
    private String condition;

    public IfGroup() {
        setName(DEFAULT_STEP_NAME);
    }
}

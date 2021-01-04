package com.winbaoxian.crawler.core.crawlertask.model.logic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhileGroup extends AbstractGroupStep {

    private static String DEFAULT_STEP_NAME = "WHILE循环判断";
    /**
     * 判断条件为true时跳出循环
     */
    private String breakCondition;
    /**
     * 执行最大次数
     */
    private Integer maxCount;
    /**
     * 循环内部对象别名
     */
    private String iterAlias;

    public WhileGroup() {
        setName(DEFAULT_STEP_NAME);
    }
}

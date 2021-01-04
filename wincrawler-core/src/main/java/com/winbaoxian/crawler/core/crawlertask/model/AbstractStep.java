package com.winbaoxian.crawler.core.crawlertask.model;

import com.winbaoxian.crawler.enums.StepType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractStep implements IStep {
    /**
     * 步骤类型
     */
    private StepType type;
    /**
     * 步骤名称
     */
    private String name;

}

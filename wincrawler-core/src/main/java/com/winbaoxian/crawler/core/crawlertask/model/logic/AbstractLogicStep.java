package com.winbaoxian.crawler.core.crawlertask.model.logic;

import com.winbaoxian.crawler.core.crawlertask.model.AbstractStep;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLogicStep extends AbstractStep {

    /**
     * 执行后延迟时间，单位：毫秒
     */
    private Long delayTime;

}

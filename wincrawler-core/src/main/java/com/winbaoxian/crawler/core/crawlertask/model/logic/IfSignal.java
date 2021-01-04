package com.winbaoxian.crawler.core.crawlertask.model.logic;

import com.winbaoxian.crawler.enums.StepSignal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IfSignal extends AbstractLogicStep  {

    private static String DEFAULT_STEP_NAME = "IF逻辑判断";

    /**
     * 判断条件
     */
    private String condition;

    /**
     * 信号
     */
    private StepSignal signal;

    /**
     * 信号消息
     */
    private String signalMsg;

    public IfSignal() {
        setName(DEFAULT_STEP_NAME);
    }

}

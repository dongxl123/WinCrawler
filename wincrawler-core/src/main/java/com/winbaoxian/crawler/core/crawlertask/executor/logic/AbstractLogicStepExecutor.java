package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.crawler.core.crawlertask.executor.IStepExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.AbstractLogicStep;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractLogicStepExecutor<T extends AbstractLogicStep> implements IStepExecutor<T> {

    @Override
    public void execute(T step, TaskContext context) throws Exception {
        doExecute(step, context);
        postExecute(step, context);
    }

    public abstract void doExecute(T step, TaskContext context) throws Exception;

    private void postExecute(T step, TaskContext context) throws Exception {
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        if (step.getDelayTime() != null) {
            log.info("[{}]DelayTime, time: {}", stepLogPrefix, step.getDelayTime());
            Thread.sleep(step.getDelayTime());
        }
    }

}

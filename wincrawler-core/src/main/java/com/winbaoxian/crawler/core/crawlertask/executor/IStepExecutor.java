package com.winbaoxian.crawler.core.crawlertask.executor;

import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import com.winbaoxian.crawler.model.core.TaskContext;

public interface IStepExecutor<T extends IStep> {

    void execute(T step, TaskContext context) throws Exception;
}

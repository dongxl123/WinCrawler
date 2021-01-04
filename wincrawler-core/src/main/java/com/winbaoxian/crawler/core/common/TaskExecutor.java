package com.winbaoxian.crawler.core.common;

import com.winbaoxian.crawler.model.core.TaskContext;

public interface TaskExecutor {

    void doTask(TaskContext context) throws Exception;
}

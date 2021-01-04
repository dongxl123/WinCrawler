package com.winbaoxian.crawler.utils;

import com.winbaoxian.crawler.constant.WinCrawlerConstant;
import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import com.winbaoxian.crawler.model.core.TaskContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dongxuanliang252
 * @date 2019-03-08 15:01
 */
public enum WinCrawlerLogUtils {
    INSTANCE;

    public String getStepLogPrefix(IStep step, TaskContext context) {
        String prefix = String.format(WinCrawlerConstant.LOG_PREFIX_STEP_TPL, step.getName(), step.getType(), context.getStepDepth());
        if (context.getStepDepth() > 0) {
            prefix = StringUtils.repeat("*", context.getStepDepth()) + prefix;
        }
        return prefix;
    }

}

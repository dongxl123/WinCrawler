package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.IfSignal;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class IfSignalStepExecutor extends AbstractLogicStepExecutor<IfSignal> {

    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public void doExecute(IfSignal step, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getCondition()) || step.getSignal() == null) {
            return;
        }
        //condition为true时，执行下面步骤
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]{} if condition is {}, signal is {}", stepLogPrefix, step.getCondition(), step.getSignal());
        if (step.getCondition().equalsIgnoreCase(Boolean.TRUE.toString())) {
            context.setStepSignal(step.getSignal());
            if (StringUtils.isNotBlank(step.getSignalMsg())) {
                context.setCurrentStoreMsg(paramsExecutor.render(step.getSignalMsg(), context));
            }
        }
    }

}

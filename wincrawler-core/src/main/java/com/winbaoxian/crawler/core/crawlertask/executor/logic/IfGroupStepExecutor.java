package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.IfGroup;
import com.winbaoxian.crawler.exception.WinCrawlerBreakException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class IfGroupStepExecutor extends AbstractGroupStepExecutor<IfGroup> {

    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public void executeGroup(IfGroup step, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getCondition())) {
            return;
        }
        //condition为true时，执行下面步骤
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]{} if condition is {} {}", stepLogPrefix, StringUtils.repeat("+", 10),
                step.getCondition(), StringUtils.repeat("+", 10));
        if (step.getCondition().equalsIgnoreCase(Boolean.TRUE.toString())) {
            Map<String, Object> currentStoreParams = context.getCurrentStoreParams();
            context.setCurrentStoreParams(new HashMap<>(currentStoreParams));
            context.setStepDepth(context.getStepDepth() + 1);
            String storeKey = null;
            try {
                storeKey = paramsExecutor.render(step.getStoreKey(), context);
                if (checkIfExists(step, context, storeKey)) {
                    return;
                }
                mainStepsExecutor.execute(step.getStepList(), context);
            } catch (Exception e) {
                log.error("[{}]mainStepsExecutor execute error, ", WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context), e);
                if (e instanceof WinCrawlerBreakException) {
                    return;
                } else if (BooleanUtils.isTrue(step.getNeedStore())) {
                    context.setCurrentStoreMsg(e.getMessage());
                } else {
                    throw e;
                }
            } finally {
                storeData(step, context, storeKey);
                context.setCurrentStoreParams(new HashMap<>(currentStoreParams));
                context.setStepDepth(context.getStepDepth() - 1);
            }
        }
    }

}

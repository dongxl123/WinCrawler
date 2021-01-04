package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.WhileGroup;
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
public class WhileGroupStepExecutor extends AbstractGroupStepExecutor<WhileGroup> {

    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public void executeGroup(WhileGroup step, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getBreakCondition())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        Map<String, Object> globalParams = context.getGlobalParams();
        Map<String, Object> currentStoreParams = context.getCurrentStoreParams();
        context.setCurrentStoreParams(new HashMap<>(currentStoreParams));
        int maxCount = step.getMaxCount() == null ? Integer.MAX_VALUE : step.getMaxCount();
        int index = 0;
        boolean breakFlag = false;
        while (!breakFlag && index < maxCount) {
            log.info("[{}]{}while, 第{}次执行{}", stepLogPrefix, StringUtils.repeat("+", 10), index + 1, StringUtils.repeat("+", 10));
            if (BooleanUtils.isTrue(step.getNeedStore())) {
                context.setGlobalParams(new HashMap<>(globalParams));
            }
            if(StringUtils.isNotBlank(step.getIterAlias())) {
                context.getGlobalParams().put(String.format("%s_winindex", step.getIterAlias()), index);
            }
            context.setStepDepth(context.getStepDepth() + 1);
            String storeKey = null;
            try {
                storeKey = paramsExecutor.render(step.getStoreKey(), context);
                if (checkIfExists(step, context, storeKey)) {
                    continue;
                }
                mainStepsExecutor.execute(step.getStepList(), context);
                String ret = paramsExecutor.render(step.getBreakCondition(), context);
                breakFlag = Boolean.TRUE.toString().equalsIgnoreCase(ret);
                log.info("[{}]{}第{}次, while breakCondition is {} {}", stepLogPrefix, StringUtils.repeat("+", 10),
                        index + 1, breakFlag, StringUtils.repeat("+", 10));
            } catch (Exception e) {
                log.error("[{}]第{}次, mainStepsExecutor execute error, ", stepLogPrefix, index + 1, e);
                if (e instanceof WinCrawlerBreakException) {
                    break;
                } else if (BooleanUtils.isTrue(step.getNeedStore())) {
                    context.setCurrentStoreMsg(e.getMessage());
                } else {
                    throw e;
                }
            } finally {
                storeData(step, context, storeKey);
                context.setCurrentStoreParams(new HashMap<>(currentStoreParams));
                context.setStepDepth(context.getStepDepth() - 1);
                index++;
            }
        }
    }

}

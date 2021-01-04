package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.ForGroup;
import com.winbaoxian.crawler.exception.WinCrawlerBreakException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ForGroupStepExecutor extends AbstractGroupStepExecutor<ForGroup> {

    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public void executeGroup(ForGroup step, TaskContext context) throws Exception {
        if ((CollectionUtils.isEmpty(step.getList()) && StringUtils.isBlank(step.getListStr())) || StringUtils.isBlank(step.getIterAlias())) {
            return;
        }
        if (CollectionUtils.isEmpty(step.getList())) {
            step.setList(JSON.parseArray(step.getListStr()));
        }
        if (CollectionUtils.isEmpty(step.getList())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]loop list:{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(step.getList()));
        Map<String, Object> globalParams = context.getGlobalParams();
        Map<String, Object> currentStoreParams = context.getCurrentStoreParams();
        context.setCurrentStoreParams(new HashMap<>(currentStoreParams));
        int index = 0;
        for (Object o : step.getList()) {
            if (BooleanUtils.isNotTrue(step.getGlobalVarsFlag()) && BooleanUtils.isTrue(step.getNeedStore())) {
                context.setGlobalParams(new HashMap<>(globalParams));
            }
            context.getCurrentStoreParams().put(step.getIterAlias(), o);
            context.getGlobalParams().put(String.format("%s_winindex", step.getIterAlias()), index);
            log.info("{}set {}={}{}", StringUtils.repeat("+", 20), step.getIterAlias(), JsonUtils.INSTANCE.toJSONString(o), StringUtils.repeat("+", 20));
            log.info("[{}]current params:{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreParams()));
            context.setStepDepth(context.getStepDepth() + 1);
            String storeKey = null;
            try {
                storeKey = paramsExecutor.render(step.getStoreKey(), context);
                if (checkIfExists(step, context, storeKey)) {
                    continue;
                }
                mainStepsExecutor.execute(step.getStepList(), context);
            } catch (Exception e) {
                log.error("[{}]mainStepsExecutor execute error, ", WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context), e);
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

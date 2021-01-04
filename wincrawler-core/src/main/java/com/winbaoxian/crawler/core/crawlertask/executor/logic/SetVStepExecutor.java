package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.crawler.core.crawlertask.model.logic.SetV;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SetVStepExecutor extends AbstractLogicStepExecutor<SetV> {

    @Override
    public void doExecute(SetV step, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getAlias()) || MapUtils.isEmpty(step.getValues())) {
            return;
        }
        Map<String, String> values = step.getValues();
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]setV into globalParams: {}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(values));
        context.getGlobalParams().put(step.getAlias(), values);
    }

}

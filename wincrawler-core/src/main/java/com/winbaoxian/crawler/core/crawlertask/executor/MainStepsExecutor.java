package com.winbaoxian.crawler.core.crawlertask.executor;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import com.winbaoxian.crawler.enums.StepMode;
import com.winbaoxian.crawler.enums.StepSignal;
import com.winbaoxian.crawler.enums.StepType;
import com.winbaoxian.crawler.enums.SystemConfigKey;
import com.winbaoxian.crawler.exception.WinCrawlerBreakException;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.ConfigParseUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MainStepsExecutor {

    @Resource
    private ParamsExecutor paramsExecutor;

    public void execute(List<IStep> stepList, TaskContext context) throws Exception {
        if (CollectionUtils.isEmpty(stepList)) {
            return;
        }
        log.info("当前globalParams:{}", JsonUtils.INSTANCE.toJSONString(context.getGlobalParams()));
        for (IStep step : stepList) {
            log.info(StringUtils.repeat("-", 50));
            long sleepTime = getStepSleepTime(step, context);
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
            //TPL替换
            JSONObject stepJo = (JSONObject) JsonUtils.INSTANCE.toJSON(step);
            paramsExecutor.renderDeep(stepJo, context);
            //生成新的step对象
            IStep newStep = ConfigParseUtils.INSTANCE.parseOneStep(stepJo);
            String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(newStep, context);
            log.info("[{}]step start,{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(newStep));
            log.info("[{}]current params,{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreParams()));
            try {
                IStepExecutor stepExecutor = determineStepExecutor(newStep.getType());
                if (stepExecutor == null) {
                    log.info("[{}]step 没有找到步骤执行器", stepLogPrefix);
                    throw new WinCrawlerException(String.format("[%s]step 没有找到步骤执行器", stepLogPrefix));
                }
                stepExecutor.execute(newStep, context);
                if (StepSignal.step_continue.equals(context.getStepSignal())) {
                    break;
                } else if (StepSignal.step_throwe.equals(context.getStepSignal())) {
                    throw new WinCrawlerException(String.format("[%s]step 主动抛出异常,msg:%s", stepLogPrefix, context.getCurrentStoreMsg()));
                } else if (StepSignal.step_break.equals(context.getStepSignal())) {
                    throw new WinCrawlerBreakException(String.format("[%s]step break, 跳出循环", stepLogPrefix));
                }
            } catch (Exception e) {
                log.error("[{}]step error,{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(newStep), e);
                throw e;
            } finally {
                context.setStepSignal(StepSignal.step_next);
                log.info("[{}]step end", stepLogPrefix);
            }
        }
    }

    private long getStepSleepTime(IStep step, TaskContext context) {
        Map<String, String> systemConfig = context.getSystemConfig();
        long sleepTime = 0;
        StepMode stepMode = step.getType().getMode();
        if (StepMode.logic.equals(stepMode)) {
            if (systemConfig.containsKey(SystemConfigKey.logic_step_sleep_time.name())) {
                sleepTime = Long.valueOf(systemConfig.get(SystemConfigKey.logic_step_sleep_time.name()));
            }
        } else if (StepMode.selenium.equals(stepMode)) {
            if (systemConfig.containsKey(SystemConfigKey.selenium_step_sleep_time.name())) {
                sleepTime = Long.valueOf(systemConfig.get(SystemConfigKey.selenium_step_sleep_time.name()));
            }
        } else if (StepMode.ajax.equals(stepMode)) {
            if (systemConfig.containsKey(SystemConfigKey.ajax_step_sleep_time.name())) {
                sleepTime = Long.valueOf(systemConfig.get(SystemConfigKey.ajax_step_sleep_time.name()));
            }
        }
        return sleepTime;
    }


    @Resource
    private ApplicationContext applicationContext;

    private IStepExecutor determineStepExecutor(StepType stepType) {
        if (stepType == null) {
            return null;
        }
        String beanName = String.format("%sStepExecutor", stepType);
        return applicationContext.getBean(beanName, IStepExecutor.class);
    }

}
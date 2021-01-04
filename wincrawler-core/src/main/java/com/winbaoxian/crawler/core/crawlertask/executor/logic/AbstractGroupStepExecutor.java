package com.winbaoxian.crawler.core.crawlertask.executor.logic;

import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.executor.MainStepsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.logic.AbstractGroupStep;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.model.dto.CrawlerResultDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.service.WinCrawlerService;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

@Slf4j
public abstract class AbstractGroupStepExecutor<T extends AbstractGroupStep> extends AbstractLogicStepExecutor<T> {

    @Resource
    protected MainStepsExecutor mainStepsExecutor;
    @Resource
    private WinCrawlerService winCrawlerService;
    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public void doExecute(T step, TaskContext context) throws Exception {
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]{}", stepLogPrefix, StringUtils.repeat("<", (40 / (context.getStepDepth() + 1))));
        if (CollectionUtils.isEmpty(step.getStepList())) {
            return;
        }
        executeGroup(step, context);
        log.info("[{}]{}", stepLogPrefix, StringUtils.repeat(">", (40 / (context.getStepDepth() + 1))));
    }

    public abstract void executeGroup(T step, TaskContext context) throws Exception;

    protected boolean checkIfExists(T step, TaskContext context, String storeKey) {
        if (BooleanUtils.isNotTrue(step.getNeedStore()) || StringUtils.isBlank(storeKey)) {
            return false;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        CrawlerTaskDTO taskDTO = context.getTaskDTO();
        Long checkedTaskId = taskDTO.getMergedTaskId() != null ? taskDTO.getMergedTaskId() : taskDTO.getId();
        log.info("[{}]try to check result,checkedTaskId:{},key:{}", stepLogPrefix, checkedTaskId, storeKey);
        CrawlerResultDTO crawlerResult = winCrawlerService.getCrawlerResult(checkedTaskId, storeKey);
        if (crawlerResult != null && (StringUtils.isNotBlank(crawlerResult.getResult()) || StringUtils.isNotBlank(crawlerResult.getMsg()))) {
            if (needUpdate(step, context, crawlerResult)) {
                log.info("[{}]exist result, to be update, checkedTaskId:{},key:{}", stepLogPrefix, checkedTaskId, storeKey);
                return false;
            } else {
                log.info("[{}]exist result, skipped, checkedTaskId:{},key:{}", stepLogPrefix, checkedTaskId, storeKey);
                return true;
            }
        } else {
            CrawlerResultDTO resultDTO = new CrawlerResultDTO();
            resultDTO.setTaskId(checkedTaskId);
            resultDTO.setKeyName(storeKey);
            if (MapUtils.isNotEmpty(context.getCurrentStoreParams())) {
                resultDTO.setParams(JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreParams()));
            }
            winCrawlerService.saveCrawlerResult(resultDTO);
            log.info("[{}]not exist result,checkedTaskId:{},key:{}", stepLogPrefix, checkedTaskId, storeKey);
            return false;
        }
    }

    protected void storeData(T step, TaskContext context, String storeKey) {
        if (BooleanUtils.isNotTrue(step.getNeedStore()) || StringUtils.isBlank(storeKey)) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        try {
            if (CollectionUtils.isEmpty(context.getCurrentStoreResult()) && StringUtils.isBlank(context.getCurrentStoreMsg())) {
                return;
            }
            Long checkedTaskId = context.getTaskDTO().getMergedTaskId() != null ? context.getTaskDTO().getMergedTaskId() : context.getTaskDTO().getId();
            log.info("[{}]try to store data, checkedTaskId:{},key:{},params:{}, result:{}, msg:{}", stepLogPrefix,
                    checkedTaskId, storeKey, JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreParams()),
                    JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreResult()), context.getCurrentStoreMsg());
            CrawlerResultDTO resultDTO = new CrawlerResultDTO();
            resultDTO.setTaskId(checkedTaskId);
            resultDTO.setKeyName(storeKey);
            if (MapUtils.isNotEmpty(context.getCurrentStoreParams())) {
                resultDTO.setParams(JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreParams()));
            }
            if (CollectionUtils.isNotEmpty(context.getCurrentStoreResult())) {
                resultDTO.setResult(JsonUtils.INSTANCE.toJSONString(context.getCurrentStoreResult()));
            }
            if (StringUtils.isNotBlank(context.getCurrentStoreMsg())) {
                resultDTO.setMsg(context.getCurrentStoreMsg());
            }
            winCrawlerService.saveCrawlerResult(resultDTO);
        } catch (Exception e) {
            log.error("[{}]storeData error, key:{}", stepLogPrefix, storeKey, e);
        } finally {
            context.clearCurrentData();
        }
    }

    private boolean needUpdate(T step, TaskContext context, CrawlerResultDTO crawlerResult) {
        context.getGlobalParams().put("persistent", crawlerResult);
        if (BooleanUtils.isTrue(step.getNeedUpdate())) {
            if (StringUtils.isBlank(step.getUpdateCondition())) {
                return true;
            } else {
                String ret = paramsExecutor.render(step.getUpdateCondition(), context);
                if (Boolean.TRUE.toString().equalsIgnoreCase(ret)) {
                    return true;
                }
            }
        }
        return false;
    }

}

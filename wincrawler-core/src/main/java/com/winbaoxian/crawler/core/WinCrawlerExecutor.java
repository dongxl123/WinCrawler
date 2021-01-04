package com.winbaoxian.crawler.core;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.common.TaskExecutor;
import com.winbaoxian.crawler.core.crawlertask.CrawlTaskExecutor;
import com.winbaoxian.crawler.core.exporttask.ExportTaskExecutor;
import com.winbaoxian.crawler.enums.TaskType;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.service.WinCrawlerService;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-6-27 17:58:45
 */

@Service
@Slf4j
public class WinCrawlerExecutor {

    @Resource
    private WinCrawlerService winCrawlerService;
    @Resource
    private ParamsExecutor paramsExecutor;

    public CrawlerTaskDTO[] getTaskList(String sql) {
        return winCrawlerService.getTaskList(sql);
    }


    /**
     * 执行数据抓取任务
     */
    public void executeAlllTask(CrawlerTaskDTO taskDTO) throws Exception {
        executeCrawlTask(taskDTO);
        executeExportTask(taskDTO);
    }


    /**
     * 执行数据抓取任务
     */
    public void executeCrawlTask(CrawlerTaskDTO taskDTO) throws Exception {
        executeTask(taskDTO, TaskType.crawl);
    }

    /**
     * 执行导出数据任务
     */
    public void executeExportTask(CrawlerTaskDTO taskDTO) throws Exception {
        executeTask(taskDTO, TaskType.export);
    }


    /**
     * 执行导出数据任务
     */
    public void executeTask(CrawlerTaskDTO taskDTO, TaskType taskType) throws Exception {
        log.info("▷▷▷▷ executeTask start, type:{}, task: {}", taskType, JsonUtils.INSTANCE.toJSONString(taskDTO));
        try {
            TaskContext context = initTaskContext(taskDTO);
            TaskExecutor taskExecutor = getTaskExecutor(context, taskType);
            if (taskExecutor != null) {
                log.info("找到任务执行器, {}", TaskExecutor.class.getSimpleName());
                taskExecutor.doTask(context);
            } else {
                log.info("没找到任务执行器");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("□□□□ executeTask end");
        }
    }

    private TaskContext initTaskContext(CrawlerTaskDTO taskDTO) {
        TaskContext context = new TaskContext();
        context.setTaskDTO(taskDTO);
        // 设置系统配置
        Map<String, String> systemConfig = winCrawlerService.getSystemConfigMap();
        if (MapUtils.isNotEmpty(systemConfig)) {
            context.setSystemConfig(systemConfig);
        }
        // 设置执行模板
        CrawlerTemplateDTO templateDTO = winCrawlerService.getCrawlerTemplate(context.getTaskDTO().getTemplateId());
        if (templateDTO == null) {
            log.info("找不到模板, id: {}", context.getTaskDTO().getTemplateId());
            throw new WinCrawlerException("找不到模板");
        } else {
            log.info("找到模板配置，{}", JsonUtils.INSTANCE.toJSONString(templateDTO));
        }
        context.setTemplateDTO(templateDTO);
        // 设置任务参数
        if (StringUtils.isNotBlank(taskDTO.getParams())) {
            String paramsStr = paramsExecutor.render(taskDTO.getParams(), context);
            Map<String, Object> params = JSON.parseObject(paramsStr);
            if (MapUtils.isNotEmpty(params)) {
                context.setGlobalParams(params);
            }
        }
        //设置任务变量
        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("id", taskDTO.getId());
        taskParams.put("name", taskDTO.getName());
        context.getGlobalParams().put("task", taskParams);
        return context;
    }

    @Resource
    private CrawlTaskExecutor crawlTaskExecutor;
    @Resource
    private ExportTaskExecutor exportTaskExecutor;

    private TaskExecutor getTaskExecutor(TaskContext context, TaskType taskType) {
        if (context == null || context.getTemplateDTO() == null) {
            return null;
        }
        if (TaskType.crawl.equals(taskType)) {
            return crawlTaskExecutor;
        } else if (TaskType.export.equals(taskType)) {
            return exportTaskExecutor;
        }
        return null;
    }

}

package com.winbaoxian.crawler.core.exporttask.executor;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.exporttask.model.StepTplExportConfig;
import com.winbaoxian.crawler.model.dto.CrawlerResultDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.service.WinCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StepTplExportExecutor implements IExportExecutor<StepTplExportConfig> {

    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private WinCrawlerService winCrawlerService;

    @Override
    public void execute(StepTplExportConfig exportConfig, CrawlerTaskDTO taskDTO) throws Exception {
        if (StringUtils.isBlank(exportConfig.getTitleTpl()) || StringUtils.isBlank(exportConfig.getTpl())) {
            return;
        }
        int page = 0;
        int step = exportConfig.getStep();
        long startId = exportConfig.getStartId();
        BufferedWriter writer = null;
        try {
            Map<String, Object> localModel = new HashMap<>();
            if (StringUtils.isNotBlank(taskDTO.getParams())) {
                localModel.put("global", JSONObject.parseObject(taskDTO.getParams()));
            }
            writer = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(exportConfig.getFileName(), true), "gbk")));
            while (true) {
                List<CrawlerResultDTO> resultList = winCrawlerService.getCrawlerResultList(taskDTO.getId(), startId, page, step);
                localModel.put("list", resultList);
                if (page == 0 && startId == 0) {
                    log.info("开始处理标题");
                    String titleLine = paramsExecutor.render(exportConfig.getTitleTpl(), localModel);
                    writer.write(titleLine);
                    writer.flush();
                }
                log.info("开始处理数据, page:{}", page);
                writer.newLine();
                String content = paramsExecutor.render(exportConfig.getTpl(), localModel);
                writer.write(content);
                writer.flush();
                if (CollectionUtils.isEmpty(resultList) || resultList.size() < step) {
                    break;
                }
                page++;
            }
        } catch (Exception e) {
            log.error("export error, stopped this task, taskId:{}", taskDTO.getId());
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}

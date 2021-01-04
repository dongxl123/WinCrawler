package com.winbaoxian.crawler.core.exporttask.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.exporttask.model.SimpleExportConfig;
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
public class SimpleExportExecutor implements IExportExecutor<SimpleExportConfig> {

    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private WinCrawlerService winCrawlerService;

    @Override
    public void execute(SimpleExportConfig exportConfig, CrawlerTaskDTO taskDTO) throws Exception {
        if (CollectionUtils.isEmpty(exportConfig.getTitles()) || CollectionUtils.isEmpty(exportConfig.getValueTplList())) {
            return;
        }
        List<CrawlerResultDTO> resultList = winCrawlerService.getCrawlerResultList(taskDTO.getId());
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportConfig.getFileName()), "gbk"));
            String lineTpl = StringUtils.join(exportConfig.getValueTplList(), ",");
            String titleLine = StringUtils.join(exportConfig.getTitles(), ",");
            Map<String, Object> titleModel = new HashMap<>();
            if (StringUtils.isNotBlank(taskDTO.getParams())) {
                titleModel.put("global", JSONObject.parseObject(taskDTO.getParams()));
            }
            writer.write(paramsExecutor.render(titleLine, titleModel));
            for (CrawlerResultDTO resultDTO : resultList) {
                if (StringUtils.isBlank(resultDTO.getResult())) {
                    continue;
                }
                JSONArray retArray = JSON.parseArray(resultDTO.getResult());
                for (Object jo : retArray) {
                    if (!(jo instanceof JSONObject) && !(jo instanceof String)) {
                        continue;
                    }
                    Map<String, Object> local = new HashMap<>();
                    local.put("result", jo);
                    if (StringUtils.isNotBlank(resultDTO.getParams())) {
                        local.put("params", JSONObject.parseObject(resultDTO.getParams()));
                    }
                    if (StringUtils.isNotBlank(taskDTO.getParams())) {
                        local.put("global", JSONObject.parseObject(taskDTO.getParams()));
                    }
                    String line = "not found";
                    try {
                        line = paramsExecutor.render(lineTpl, local);
                    } catch (Exception e) {
                        log.warn("export error, taskId:{}, key:{}, lineTpl:{}", resultDTO.getTaskId(), resultDTO.getKeyName(), lineTpl, e);
                    }
                    writer.newLine();
                    writer.write(line);
                }
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

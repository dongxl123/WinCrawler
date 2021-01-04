package com.winbaoxian.crawler.core.exporttask.executor;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.exporttask.model.TplExportConfig;
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
public class TplExportExecutor implements IExportExecutor<TplExportConfig> {

    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private WinCrawlerService winCrawlerService;

    @Override
    public void execute(TplExportConfig exportConfig, CrawlerTaskDTO taskDTO) throws Exception {
        if (StringUtils.isBlank(exportConfig.getTpl())) {
            return;
        }
        List<CrawlerResultDTO> resultList = winCrawlerService.getCrawlerResultList(taskDTO.getId());
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        String content = "error";
        try {
            Map<String, Object> local = new HashMap<>();
            local.put("list", resultList);
            if (StringUtils.isNotBlank(taskDTO.getParams())) {
                local.put("global", JSONObject.parseObject(taskDTO.getParams()));
            }
            log.info("开始生成文件数据");
            content = paramsExecutor.render(exportConfig.getTpl(), local);
        } catch (Exception e) {
            log.warn("export error, taskId:{}, content:{}", taskDTO.getId(), content, e);
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(exportConfig.getFileName()), "gbk")));
            writer.write(content);
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

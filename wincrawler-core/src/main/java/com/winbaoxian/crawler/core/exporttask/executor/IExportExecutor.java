package com.winbaoxian.crawler.core.exporttask.executor;

import com.winbaoxian.crawler.core.exporttask.model.IExportConfig;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;

public interface IExportExecutor<T extends IExportConfig> {

    void execute(T exportConfig, CrawlerTaskDTO taskDTO) throws Exception;
}

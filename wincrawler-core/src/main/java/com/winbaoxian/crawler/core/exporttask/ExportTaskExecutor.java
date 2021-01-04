package com.winbaoxian.crawler.core.exporttask;

import com.winbaoxian.crawler.core.common.TaskExecutor;
import com.winbaoxian.crawler.core.exporttask.executor.IExportExecutor;
import com.winbaoxian.crawler.core.exporttask.model.AbstractExportConfig;
import com.winbaoxian.crawler.core.exporttask.model.IExportConfig;
import com.winbaoxian.crawler.enums.ExportType;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.utils.ConfigParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ExportTaskExecutor implements TaskExecutor {

    /**
     * 导出数据
     *
     * @param context
     * @throws Exception
     */
    @Override
    public void doTask(TaskContext context) throws Exception {
        CrawlerTemplateDTO templateDTO = context.getTemplateDTO();
        if (templateDTO == null) {
            return;
        }
        String exportConfigStr = context.getTaskDTO().getExportConfig();
        if (StringUtils.isBlank(exportConfigStr)) {
            exportConfigStr = templateDTO.getExportConfig();
        }
        if (StringUtils.isBlank(exportConfigStr)) {
            log.info("[doExport] 无配置");
            return;
        }
        log.info("[doExport] 开始执行");
        IExportConfig exportConfig = ConfigParseUtils.INSTANCE.parseExportConfig(exportConfigStr);
        doExport(exportConfig, context.getTaskDTO());
        log.info("[doExport] 完成执行");
    }

    public void doExport(IExportConfig exportConfig, CrawlerTaskDTO taskDTO) throws Exception {
        if (exportConfig == null || taskDTO == null) {
            return;
        }
        if (exportConfig instanceof AbstractExportConfig) {
            AbstractExportConfig realConfig = (AbstractExportConfig) exportConfig;
            if (StringUtils.isBlank(realConfig.getFileName())) {
                realConfig.setFileName(String.format("%s.csv", taskDTO.getName()));
            }
        }
        ExportType exportType = exportConfig.getType();
        IExportExecutor exportExecutor = determineExportExecutor(exportType);
        if (exportExecutor == null) {
            log.info("[export] 没有找到导出执行器, export:{}", exportType);
            return;
        }
        exportExecutor.execute(exportConfig, taskDTO);
    }


    @Resource
    private ApplicationContext applicationContext;

    /**
     * 根据StepType找到对应的步骤执行器
     *
     * @param exportType
     * @return
     */
    private IExportExecutor determineExportExecutor(ExportType exportType) {
        if (exportType == null) {
            return null;
        }
        String beanName = String.format("%sExportExecutor", exportType);
        return applicationContext.getBean(beanName, IExportExecutor.class);
    }

}

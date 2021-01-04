package com.winbaoxian.crawler.core.exporttask.model;

import com.winbaoxian.crawler.enums.ExportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractExportConfig implements IExportConfig {

    /**
     * 导出类型
     */
    private ExportType type;

    /**
     * 导出文件名
     */
    private String fileName;

}

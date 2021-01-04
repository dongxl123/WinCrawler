package com.winbaoxian.crawler.core.exporttask.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StepTplExportConfig extends AbstractExportConfig {

    private static final Long DEFAULT_START_ID = 0L;
    private static final Integer DEFAULT_STEP = 100;

    private Long startId = DEFAULT_START_ID;
    private Integer step = DEFAULT_STEP;

    /**
     * 标题模板
     */
    private String titleTpl;
    /**
     * 内容模板
     */
    private String tpl;

}

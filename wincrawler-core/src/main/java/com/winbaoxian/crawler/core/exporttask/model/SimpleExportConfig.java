package com.winbaoxian.crawler.core.exporttask.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleExportConfig extends AbstractExportConfig {

    /**
     * 标题
     */
    private List<String> titles;
    /**
     * 数据
     */
    private List<String> valueTplList;

}

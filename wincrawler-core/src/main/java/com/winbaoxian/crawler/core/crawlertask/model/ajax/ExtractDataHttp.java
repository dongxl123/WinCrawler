package com.winbaoxian.crawler.core.crawlertask.model.ajax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractDataHttp extends AbstractAjaxStep {

    private static String DEFAULT_STEP_NAME = "提取数据请求";

    /**
     * 响应结果存储数据模板
     */
    private String tpl;

    public ExtractDataHttp() {
        setName(DEFAULT_STEP_NAME);
    }

}

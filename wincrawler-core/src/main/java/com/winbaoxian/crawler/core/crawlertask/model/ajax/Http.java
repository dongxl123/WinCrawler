package com.winbaoxian.crawler.core.crawlertask.model.ajax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Http extends AbstractAjaxStep {

    private static String DEFAULT_STEP_NAME = "Ajax请求";

    /**
     * 响应结果数据模板
     */
    private String tpl;

    public Http() {
        setName(DEFAULT_STEP_NAME);
    }

}

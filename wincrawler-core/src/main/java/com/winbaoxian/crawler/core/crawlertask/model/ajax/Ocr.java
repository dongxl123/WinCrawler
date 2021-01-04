package com.winbaoxian.crawler.core.crawlertask.model.ajax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ocr extends AbstractAjaxStep {

    private static String DEFAULT_STEP_NAME = "获取图片验证码";

    public Ocr() {
        setName(DEFAULT_STEP_NAME);
    }


}

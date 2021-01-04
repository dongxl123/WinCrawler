package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.AbstractStep;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class AbstractSeleniumStep extends AbstractStep {

    /**
     * 等待出现的元素；当无设置时，使用等待时间
     */
    private String waitElementXPath;
    /**
     * 执行前等待时间，单位：毫秒
     */
    private Long waitTime;
    /**
     * 定位iframe中的元素
     */
    private List<String> frameXPathList;


}

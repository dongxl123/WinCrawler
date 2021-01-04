package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractXPathStep extends AbstractSeleniumStep {

    /**
     * 元素匹配路径
     */
    private String matchXPath;


}

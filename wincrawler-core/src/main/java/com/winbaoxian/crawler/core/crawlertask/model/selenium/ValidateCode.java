package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateCode extends AbstractSeleniumStep {

    private static String DEFAULT_STEP_NAME = "识别验证码";
    /**
     * 图片验证码XPath
     */
    private String codeImageXPath;
    /**
     * 验证码输入框XPath
     */
    private String inputTextXPath;
    /**
     * 是否ajax校验验证码
     */
    private Boolean ajaxFlag;
    /**
     * 成功出现元素XPath(优先级高)
     */
    private String presentXPathOnSuccess;
    /**
     * 失败出现元素XPath
     */
    private String presentXPathOnFailure;
    /**
     * 失败重试次数
     */
    private Integer retryTimesOnFailure;
    /**
     * 提交按钮XPath
     */
    private String submitXPath;

    public ValidateCode() {
        setName(DEFAULT_STEP_NAME);
    }
}

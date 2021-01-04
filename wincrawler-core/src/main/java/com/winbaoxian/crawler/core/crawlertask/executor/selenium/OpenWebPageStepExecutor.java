package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.OpenWebPage;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OpenWebPageStepExecutor extends AbstractSeleniumStepExecutor<OpenWebPage> {

    @Override
    public void doExecute(OpenWebPage step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getUrl())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]打开网页, url: {}", stepLogPrefix, step.getUrl());
        webDriver.get(step.getUrl());
    }

}

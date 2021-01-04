package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.ExtractData;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.SeleniumUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ExtractDataStepExecutor extends AbstractSeleniumStepExecutor<ExtractData> {

    @Override
    public void doExecute(ExtractData step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getMatchXPath())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]try to findElements, xpath: {}", stepLogPrefix, step.getMatchXPath());
        List<WebElement> webElements = webDriver.findElements(By.xpath(step.getMatchXPath()));
        if (CollectionUtils.isEmpty(webElements)) {
            return;
        }
        List data;
        if (CollectionUtils.isNotEmpty(step.getRuleList())) {
            data = SeleniumUtils.INSTANCE.getExtraDataList(webElements, step.getRuleList());
        } else {
            data = SeleniumUtils.INSTANCE.getHtmlList(webElements);
        }
        context.getCurrentStoreResult().addAll(data);
    }
}

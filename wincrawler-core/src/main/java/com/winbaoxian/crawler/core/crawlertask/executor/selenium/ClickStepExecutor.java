package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.Click;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.SeleniumUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClickStepExecutor extends AbstractSeleniumStepExecutor<Click> {

    @Override
    public void doExecute(Click step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getMatchXPath())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]try to findElement, xpath: {}", stepLogPrefix, step.getMatchXPath());
        WebElement webElement = webDriver.findElement(By.xpath(step.getMatchXPath()));
        SeleniumUtils.INSTANCE.locateWebElement(webDriver, webElement);
        webElement.click();
        if (BooleanUtils.isTrue(step.getHasAlert())) {
            Thread.sleep(2000L);
            webDriver.switchTo().alert().accept();
        }
    }
}

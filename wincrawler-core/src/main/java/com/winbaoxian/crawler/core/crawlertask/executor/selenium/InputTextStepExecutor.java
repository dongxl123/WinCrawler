package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.InputText;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.SeleniumUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InputTextStepExecutor extends AbstractSeleniumStepExecutor<InputText> {

    @Override
    public void doExecute(InputText step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getValue()) || StringUtils.isBlank(step.getMatchXPath())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]try to findElement, xpath: {}", stepLogPrefix, step.getMatchXPath());
        WebElement webElement = webDriver.findElement(By.xpath(step.getMatchXPath()));
        SeleniumUtils.INSTANCE.locateWebElement(webDriver, webElement);
        log.info("[{}]set inputText, value: {}", stepLogPrefix, step.getValue());
        webElement.sendKeys(step.getValue() + Keys.TAB);
    }
}

package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.MultiSelect;
import com.winbaoxian.crawler.model.core.SelectConfig;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
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
public class MultiSelectStepExecutor extends AbstractSeleniumStepExecutor<MultiSelect> {

    @Override
    public void doExecute(MultiSelect step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getMatchXPath()) || CollectionUtils.isEmpty(step.getSelectConfigList())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        log.info("[{}]try to findElement, xpath: {}", stepLogPrefix, step.getMatchXPath());
        WebElement webElement = webDriver.findElement(By.xpath(step.getMatchXPath()));
        SeleniumUtils.INSTANCE.locateWebElement(webDriver, webElement);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(webElement);
        if (!select.isMultiple()) {
            log.warn("[{}] this select element is not multiple: {}", stepLogPrefix, step.getMatchXPath());
            return;
        }
        List<SelectConfig> selectConfigList = step.getSelectConfigList();
        log.info("[{}]try to select, config:{}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(step.getSelectConfigList()));
        select.deselectAll();
        for (SelectConfig selectConfig : selectConfigList) {
            if (SelectConfig.SelectType.byIndex.equals(selectConfig.getType())) {
                select.selectByIndex(Integer.parseInt(selectConfig.getValue()));
            } else if (SelectConfig.SelectType.byValue.equals(selectConfig.getType())) {
                select.selectByValue(selectConfig.getValue());
            } else if (SelectConfig.SelectType.byText.equals(selectConfig.getType()))
                select.selectByVisibleText(selectConfig.getValue());
        }
    }

}

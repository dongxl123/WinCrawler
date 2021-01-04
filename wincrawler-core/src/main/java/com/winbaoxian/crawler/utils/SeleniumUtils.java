package com.winbaoxian.crawler.utils;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.ExtractData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum SeleniumUtils {

    INSTANCE;

    public List<String> getHtmlList(List<WebElement> webElements) {
        if (CollectionUtils.isEmpty(webElements)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (WebElement webElement : webElements) {
            list.add(getHtml(webElement));
        }
        return list;
    }


    public String getHtml(WebElement webElement) {
        if (webElement == null) {
            return StringUtils.EMPTY;
        }
        return webElement.getAttribute("innerHTML");
    }

    public List getExtraDataList(List<WebElement> webElements, List<ExtractData.ExtractRule> ruleList) {
        if (CollectionUtils.isEmpty(webElements)) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (WebElement webElement : webElements) {
            list.add(getExtraData(webElement, ruleList));
        }
        return list;
    }

    private Map<String, String> getExtraData(WebElement webElement, List<ExtractData.ExtractRule> ruleList) {
        if (webElement == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, String> data = new LinkedHashMap<>();
        for (ExtractData.ExtractRule rule : ruleList) {
            if (StringUtils.isBlank(rule.getName()) || StringUtils.isBlank(rule.getRule())) {
                continue;
            }
            List<WebElement> textElements = webElement.findElements(By.xpath(rule.getRule()));
            String text = CollectionUtils.isEmpty(textElements) ? StringUtils.EMPTY : getHtml(textElements.get(0));
            data.put(rule.getName(), text);
        }
        return data;
    }

    public void locateWebElement(WebDriver webDriver, WebElement webElement) {
        ((ChromeDriver) webDriver).executeScript("window.scrollTo(0," + webElement.getLocation().getY() + ")");
    }
}

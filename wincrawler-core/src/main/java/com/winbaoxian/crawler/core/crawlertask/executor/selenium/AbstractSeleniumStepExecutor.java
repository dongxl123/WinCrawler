package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.constant.WinCrawlerConstant;
import com.winbaoxian.crawler.core.crawlertask.executor.IStepExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.selenium.AbstractSeleniumStep;
import com.winbaoxian.crawler.enums.SystemConfigKey;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.utils.ProxyUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
public abstract class AbstractSeleniumStepExecutor<T extends AbstractSeleniumStep> implements IStepExecutor<T> {

    @Override
    public void execute(T step, TaskContext context) throws Exception {
        WebDriver webDriver = context.getWebDriver();
        if (webDriver == null) {
            webDriver = createWebDriver(context.getSystemConfig(), context.getTemplateDTO());
            context.setWebDriver(webDriver);
        }
        preExecute(step, webDriver, context);
        doExecute(step, webDriver, context);
    }

    private void preExecute(T step, WebDriver webDriver, TaskContext context) throws Exception {
        //webdriver frame复位
        webDriver.switchTo().defaultContent();
        //webdriver switchTo frame
        if (CollectionUtils.isNotEmpty(step.getFrameXPathList())) {
            for (String frameXpath : step.getFrameXPathList()) {
                webDriver.switchTo().frame(webDriver.findElement(By.xpath(frameXpath)));
            }
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        if (StringUtils.isNotBlank(step.getWaitElementXPath())) {
            //timeOut=key: selenium_implicitly_wait, 当超时抛出异常
            log.info("[{}]WaitElementXPath, xpath: {}", stepLogPrefix, step.getWaitElementXPath());
            webDriver.findElement(By.xpath(step.getWaitElementXPath()));
        } else if (step.getWaitTime() != null) {
            log.info("[{}]WaitTime, time: {}", stepLogPrefix, step.getWaitTime());
            Thread.sleep(step.getWaitTime());
        }
    }

    public abstract void doExecute(T step, WebDriver webDriver, TaskContext context) throws Exception;

    private WebDriver createWebDriver(Map<String, String> configMap, CrawlerTemplateDTO templateDTO) {
        ChromeOptions options = new ChromeOptions();
        String headlessKey = SystemConfigKey.selenium_headless.name();
        if (configMap.containsKey(headlessKey) && configMap.get(headlessKey).equals(WinCrawlerConstant.SYSTEM_CONFIG_VALUE_TRUE)) {
            options.addArguments("headless");
        }
        String useProxyKey = SystemConfigKey.selenium_use_proxy.name();
        if (configMap.containsKey(useProxyKey) && configMap.get(useProxyKey).equals(WinCrawlerConstant.SYSTEM_CONFIG_VALUE_TRUE) && templateDTO.getUseProxyMethod() != null && templateDTO.getUseProxyMethod() != 0) {
            String proxyString = ProxyUtils.INSTANCE.getSeleniumProxy(templateDTO.getUseProxyMethod());
            if (StringUtils.isNotBlank(proxyString)) {
                options.addArguments("--proxy-server=http://" + proxyString);
            }
        }
        WebDriver webDriver = new ChromeDriver(options);
        if (configMap.containsKey(SystemConfigKey.selenium_page_load_timeout.name())) {
            webDriver.manage().timeouts().pageLoadTimeout(Long.valueOf(configMap.get(SystemConfigKey.selenium_page_load_timeout.name())), TimeUnit.MILLISECONDS);
        }
        if (configMap.containsKey(SystemConfigKey.selenium_implicitly_wait.name())) {
            webDriver.manage().timeouts().implicitlyWait(Long.valueOf(configMap.get(SystemConfigKey.selenium_implicitly_wait.name())), TimeUnit.MILLISECONDS);
        }
        webDriver.manage().window().maximize();
        return webDriver;
    }

}

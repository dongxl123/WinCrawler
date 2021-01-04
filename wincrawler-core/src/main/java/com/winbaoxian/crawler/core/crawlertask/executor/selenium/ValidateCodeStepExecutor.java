package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.constant.WinCrawlerConstant;
import com.winbaoxian.crawler.core.crawlertask.model.selenium.ValidateCode;
import com.winbaoxian.crawler.enums.SystemConfigKey;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ValidateCodeStepExecutor extends AbstractSeleniumStepExecutor<ValidateCode> {

    @Override
    public void doExecute(ValidateCode step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getCodeImageXPath()) || StringUtils.isBlank(step.getInputTextXPath()) || StringUtils.isBlank(step.getSubmitXPath())) {
            return;
        }
        WebElement codeImageElement = webDriver.findElement(By.xpath(step.getCodeImageXPath()));
        String imgUrl = codeImageElement.getAttribute("src");
        if (StringUtils.isBlank(imgUrl)) {
            return;
        }
        boolean ajaxUrlFlag = true;
        if (StringUtils.startsWithIgnoreCase(imgUrl, WinCrawlerConstant.IMAGE_DATA_PREFIX)) {
            ajaxUrlFlag = false;
        }
        //识别图片验证码
        Map<String, String> systemConfig = context.getSystemConfig();
        if (!systemConfig.containsKey(SystemConfigKey.showapi_app_id.name()) || !systemConfig.containsKey(SystemConfigKey.showapi_app_secret.name())) {
            throw new WinCrawlerException("showApi appId or appSecret can not be null");
        }
        int retryTimes = step.getRetryTimesOnFailure() != null ? step.getRetryTimesOnFailure() : 0;
        int index = 0;
        while (index++ <= retryTimes) {
            byte[] bytes = null;
            if (ajaxUrlFlag) {
                bytes = HttpUtils.INSTANCE.doFileGet(imgUrl, HttpUtils.INSTANCE.toCookieString(webDriver.manage().getCookies()));
            } else {
                bytes = Base64Utils.INSTANCE.toBytes(imgUrl);
            }
            String imgCode = ShowApiValidateCodeUtils.INSTANCE.validateCode(systemConfig.get(SystemConfigKey.showapi_app_id.name()),
                    systemConfig.get(SystemConfigKey.showapi_app_secret.name()), bytes);
            if (StringUtils.isBlank(imgCode)) {
                if (index > retryTimes) {
                    throw new WinCrawlerException("validate code failed");
                } else {
                    continue;
                }
            }
            String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
            log.info("[{}]尝试第{}次识别验证码, code:{}", stepLogPrefix, index, imgCode);
            WebElement inputTextElement = webDriver.findElement(By.xpath(step.getInputTextXPath()));
            SeleniumUtils.INSTANCE.locateWebElement(webDriver, inputTextElement);
            inputTextElement.clear();
            inputTextElement.sendKeys(imgCode);
            boolean validateSuccess = false;
            if (StringUtils.isBlank(step.getPresentXPathOnSuccess()) && StringUtils.isBlank(step.getPresentXPathOnFailure())) {
                validateSuccess = true;
            } else if (StringUtils.isNotBlank(step.getPresentXPathOnSuccess())) {
                List<WebElement> successElementList = webDriver.findElements(By.xpath(step.getPresentXPathOnSuccess()));
                if (CollectionUtils.isNotEmpty(successElementList)) {
                    validateSuccess = true;
                }
            } else {
                List<WebElement> failureElementList = webDriver.findElements(By.xpath(step.getPresentXPathOnFailure()));
                if (CollectionUtils.isEmpty(failureElementList)) {
                    validateSuccess = true;
                }
            }
            if (validateSuccess) {
                log.info("[{}]识别验证码成功", stepLogPrefix);
                WebElement submitElement = webDriver.findElement(By.xpath(step.getSubmitXPath()));
                submitElement.click();
                break;
            } else if (index > retryTimes) {
                throw new WinCrawlerException("validate code failed");
            }
        }
    }
}

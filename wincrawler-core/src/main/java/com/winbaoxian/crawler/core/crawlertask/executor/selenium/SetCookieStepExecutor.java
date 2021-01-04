package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.crawler.core.crawlertask.model.selenium.SetCookie;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SetCookieStepExecutor extends AbstractSeleniumStepExecutor<SetCookie> {

    @Override
    public void doExecute(SetCookie step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getCookies())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        List<Cookie> cookies = JSON.parseArray(step.getCookies(), Cookie.class);
        log.info("[{}]准备设置cookies: {}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(cookies));
        Set<Cookie> existCookies = webDriver.manage().getCookies();
        List<String> existCookieNames = existCookies.stream().map(o -> o.getName()).collect(Collectors.toList());
        for (Cookie cookie : cookies) {
            if (existCookieNames.contains(cookie.getName())) {
                webDriver.manage().deleteCookieNamed(cookie.getName());
            }
            webDriver.manage().addCookie(cookie);
        }
        log.info("[{}]设置cookies成功", stepLogPrefix);
    }

}

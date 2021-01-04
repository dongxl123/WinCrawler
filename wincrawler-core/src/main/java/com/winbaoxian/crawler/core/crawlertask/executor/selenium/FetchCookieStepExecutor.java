package com.winbaoxian.crawler.core.crawlertask.executor.selenium;

import com.winbaoxian.crawler.core.crawlertask.model.selenium.FetchCookie;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FetchCookieStepExecutor extends AbstractSeleniumStepExecutor<FetchCookie> {

    @Override
    public void doExecute(FetchCookie step, WebDriver webDriver, TaskContext context) throws Exception {
        if (StringUtils.isBlank(step.getAlias())) {
            return;
        }
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        Set<Cookie> cookies = webDriver.manage().getCookies();
        log.info("[{}]获取cookies: {}", stepLogPrefix, JsonUtils.INSTANCE.toJSONString(cookies));
        context.getGlobalParams().put(step.getAlias(), cookies);
    }

}

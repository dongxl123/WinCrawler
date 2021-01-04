package com.winbaoxian.crawler.core.crawlertask;

import com.winbaoxian.crawler.core.common.TaskExecutor;
import com.winbaoxian.crawler.core.crawlertask.executor.MainStepsExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.ConfigParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class CrawlTaskExecutor implements TaskExecutor {

    @Resource
    private MainStepsExecutor mainStepsExecutor;

    @Override
    public void doTask(TaskContext context) throws Exception {
        String mainSteps = context.getTemplateDTO().getMainSteps();
        if (StringUtils.isBlank(mainSteps)) {
            log.info("[doMainSteps] 无配置");
            return;
        }
        try {
            List<IStep> stepList = ConfigParseUtils.INSTANCE.parseStepsFromConfig(mainSteps);
            log.info("[doMainSteps] 开始执行");
            mainStepsExecutor.execute(stepList, context);
            log.info("[doMainSteps] 完成执行");
        } catch (Exception e) {
            throw e;
        } finally {
            WebDriver webDriver = context.getWebDriver();
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

}

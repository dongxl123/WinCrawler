package com.winbaoxian.crawler.core.crawlertask.executor.ajax;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.Ocr;
import com.winbaoxian.crawler.enums.StepSignal;
import com.winbaoxian.crawler.enums.SystemConfigKey;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.utils.ShowApiValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class OcrStepExecutor extends AbstractAjaxStepExecutor<Ocr> {

    @Override
    protected Object doSuccessExecute(Ocr step, TaskContext context, JSONObject response) throws Exception {
        if (!response.containsKey("body")) {
            return response;
        }
        byte[] bytes = response.getBytes("body");
        Map<String, String> systemConfig = context.getSystemConfig();
        if (!systemConfig.containsKey(SystemConfigKey.showapi_app_id.name()) || !systemConfig.containsKey(SystemConfigKey.showapi_app_secret.name())) {
            throw new WinCrawlerException("showApi appId or appSecret can not be null");
        }
        int retryTimes = 3;
        while (retryTimes-- > 0) {
            String imgCode = ShowApiValidateCodeUtils.INSTANCE.validateCode(systemConfig.get(SystemConfigKey.showapi_app_id.name()),
                    systemConfig.get(SystemConfigKey.showapi_app_secret.name()), bytes);
            if (StringUtils.isNotBlank(imgCode)) {
                return imgCode;
            }
        }
        context.setStepSignal(StepSignal.step_throwe);
        return null;
    }

}

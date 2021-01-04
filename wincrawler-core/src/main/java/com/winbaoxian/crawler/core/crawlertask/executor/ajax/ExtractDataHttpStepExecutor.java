package com.winbaoxian.crawler.core.crawlertask.executor.ajax;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.ExtractDataHttp;
import com.winbaoxian.crawler.enums.StepSignal;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractDataHttpStepExecutor extends AbstractAjaxStepExecutor<ExtractDataHttp> {

    @Override
    protected Object doSuccessExecute(ExtractDataHttp step, TaskContext context, JSONObject response) throws Exception {
        if (StringUtils.isNotBlank(step.getTpl())) {
            String tplResult = super.renderTpl(step.getTpl(), context, response);
            if (StringUtils.isNotBlank(tplResult)) {
                Object result = JsonUtils.INSTANCE.parseObject(tplResult);
                if (result instanceof JSONArray) {
                    context.getCurrentStoreResult().addAll((JSONArray) result);
                } else {
                    context.getCurrentStoreResult().add(result);
                }
                return result;
            } else {
                String errorMsg = "找不到数据";
                context.setCurrentStoreMsg(errorMsg);
                context.setStepSignal(StepSignal.step_throwe);
                return errorMsg;
            }
        }
        return null;
    }
}

package com.winbaoxian.crawler.core.crawlertask.executor.ajax;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.Http;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HttpStepExecutor extends AbstractAjaxStepExecutor<Http> {

    @Override
    protected Object doSuccessExecute(Http step, TaskContext context, JSONObject response) throws Exception {
        if (StringUtils.isBlank(step.getTpl())) {
            return response;
        }
        String result = super.renderTpl(step.getTpl(), context, response);
        return JsonUtils.INSTANCE.parseObject(result);
    }

}

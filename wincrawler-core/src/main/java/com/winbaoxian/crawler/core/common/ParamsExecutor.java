package com.winbaoxian.crawler.core.common;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.AbstractFreemarkerConfiguration;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.constant.WinCrawlerConstant;
import com.winbaoxian.crawler.enums.StepMode;
import com.winbaoxian.crawler.enums.StepType;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 14:31
 */
@Component
@Slf4j
public class ParamsExecutor extends AbstractFreemarkerConfiguration {

    @Resource
    private FreeMarkerConfigurer freemarkerConfig;

    @PostConstruct
    public void initFunction() {
        try {
            super.settings(freemarkerConfig.getConfiguration());
        } catch (Exception e) {
            log.error("freemarker init error", e);
        }
    }

    public String render(String content, TaskContext context) {
        Map<String, Object> model = new HashMap<>();
        if (context != null) {
            if (MapUtils.isNotEmpty(context.getGlobalParams())) {
                model.putAll(context.getGlobalParams());
            }
            if (MapUtils.isNotEmpty(context.getCurrentStoreParams())) {
                model.putAll(context.getCurrentStoreParams());
            }
        }
        return render(content, model);
    }

    public String render(String content, Map<String, Object> model) {
        if (StringUtils.isBlank(content) || !needRender(content)) {
            return content;
        }
        try {
            //log.info("Before render,{}", content);
            Template template = new Template(null, content, freemarkerConfig.getConfiguration());
            String ret = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            log.info("After render,{}", ret);
            return ret;
        } catch (Exception e) {
            log.error("ParamsExecutor.render error, content:{}, model:{}", content, JsonUtils.INSTANCE.toJSONString(model));
            throw new WinCrawlerException(String.format("ParamsExecutor.render error, content:%s", content), e);
        }
    }

    private boolean needRender(String content) {
        if (content.contains(WinCrawlerConstant.FREEMARKER_VARIABLE_PREFIX) || content.contains(WinCrawlerConstant.FREEMARKER_CONDITION_PREFIX)) {
            return true;
        }
        return false;
    }


    /**
     * 以下情况不执行FTL
     * 当StepMode.ajax，并且key=tpl或者errorMsgTpl或者storeFilePath时;
     * 当StepMode.logic，并且key=storeKey或者updateCondition时;
     * 当value instanceof JSONArray (stepList)时;
     * 当WhileGroup，并且key=breakCondition时;
     * 当ifSignal,并且key=signalMsg时;
     *
     * @param jo
     * @param context
     */
    public void renderDeep(JSONObject jo, TaskContext context) {
        if (jo == null) {
            return;
        }
        String type = jo.getString("type");
        StepType stepType = StepType.getStepType(type);
        for (String key : jo.keySet()) {
            if (stepType != null && ((StepMode.ajax.equals(stepType.getMode()) && (key.equals("tpl") || key.equals("errorMsgTpl") || key.equals("storeFilePath")))
                    || (StepMode.logic.equals(stepType.getMode()) && (key.equals("storeKey") || key.equals("updateCondition")))
                    || (StepType.whileGroup.equals(stepType) && key.equals("breakCondition"))
                    || (StepType.ifSignal.equals(stepType) && key.equals("signalMsg")))) {
                continue;
            }
            Object value = jo.get(key);
            if (value instanceof String) {
                jo.put(key, render((String) value, context));
            } else if (value instanceof JSONObject) {
                JSONObject innerJo = (JSONObject) value;
                renderDeep(innerJo, context);
            }
        }
    }

}

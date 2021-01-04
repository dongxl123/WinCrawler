package com.winbaoxian.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.AbstractAjaxStep;
import com.winbaoxian.crawler.core.crawlertask.model.logic.AbstractGroupStep;
import com.winbaoxian.crawler.core.crawlertask.model.logic.AbstractLogicStep;
import com.winbaoxian.crawler.core.crawlertask.model.selenium.AbstractSeleniumStep;
import com.winbaoxian.crawler.core.exporttask.model.IExportConfig;
import com.winbaoxian.crawler.enums.ExportType;
import com.winbaoxian.crawler.enums.StepMode;
import com.winbaoxian.crawler.enums.StepType;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum ConfigParseUtils {

    INSTANCE;

    private Logger log = LoggerFactory.getLogger(ConfigParseUtils.class);

    /**
     * ----------------------Crawl-------------------
     */
    public List<IStep> parseStepsFromConfig(String config) throws Exception {
        if (StringUtils.isBlank(config)) {
            return null;
        }
        try {
            JSONArray jsonArray = JSON.parseArray(config);
            List<IStep> steps = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jo = (JSONObject) o;
                IStep step = parseOneStep(jo);
                if (step != null) {
                    steps.add(step);
                }
            }
            return steps;
        } catch (Exception e) {
            log.error("parseStepsFromConfig error, data:{}", config);
            throw e;
        }
    }

    public IStep parseOneStep(JSONObject jo) throws Exception {
        if (jo == null) {
            return null;
        }
        if (!jo.containsKey("type")) {
            log.warn("step config missing type, {}", jo.toJSONString());
            throw new WinCrawlerException(String.format("step config missing type:%s", jo.toJSONString()));
        }
        try {
            Class<? extends IStep> stepClass = determineStepClass(jo.getString("type"));
            IStep step = JSON.parseObject(jo.toJSONString(), stepClass);
            if (step instanceof AbstractGroupStep) {
                AbstractGroupStep realStep = (AbstractGroupStep) step;
                if (jo.containsKey("stepList")) {
                    realStep.setStepList(parseStepsFromConfig(jo.get("stepList").toString()));
                }
            }
            return step;
        } catch (Exception e) {
            log.error("parseOneStep error, config:{}", jo.toJSONString());
            throw new WinCrawlerException(String.format("parseOneStep error, config:%s", jo.toJSONString()), e);
        }
    }

    private Class<? extends IStep> determineStepClass(String type) throws Exception {
        StepType stepType = StepType.getStepType(type);
        if (stepType == null) {
            throw new WinCrawlerException(String.format("stepType[%s] is not supported", type));
        }
        String basePackage = null;
        StepMode stepMode = stepType.getMode();
        if (StepMode.logic.equals(stepMode)) {
            basePackage = ClassUtils.getPackageCanonicalName(AbstractLogicStep.class);
        } else if (StepMode.selenium.equals(stepMode)) {
            basePackage = ClassUtils.getPackageCanonicalName(AbstractSeleniumStep.class);
        } else if (StepMode.ajax.equals(stepMode)) {
            basePackage = ClassUtils.getPackageCanonicalName(AbstractAjaxStep.class);
        }
        String className = String.format("%s.%s", basePackage, StringUtils.capitalize(type));
        Class clazz = ClassUtils.getClass(className);
        if (ClassUtils.isAssignable(clazz, IStep.class)) {
            return clazz;
        } else {
            throw new WinCrawlerException(String.format("step class[%s] can not be found", className));
        }

    }

    /**
     * ----------------------Export----------------------
     */
    public IExportConfig parseExportConfig(String exportConfigStr) throws Exception {
        if (StringUtils.isBlank(exportConfigStr)) {
            return null;
        }
        try {
            JSONObject jo = JSON.parseObject(exportConfigStr);
            if (!jo.containsKey("type")) {
                log.warn("export config missing type, {}", jo.toJSONString());
                throw new WinCrawlerException(String.format("export config missing type:%s", jo.toJSONString()));
            }
            Class<? extends IExportConfig> exportConfigClass = determineExportConfigClass(jo.getString("type"));
            IExportConfig exportConfig = JSON.parseObject(jo.toJSONString(), exportConfigClass);
            return exportConfig;
        } catch (Exception e) {
            log.error("parseExportConfig error, data:{}", exportConfigStr, e);
            throw e;
        }
    }

    private Class<? extends IExportConfig> determineExportConfigClass(String type) throws Exception {
        ExportType exportType = ExportType.getExportType(type);
        if (exportType == null) {
            throw new WinCrawlerException(String.format("exportType[%s] is not supported", type));
        }
        String basePackage = ClassUtils.getPackageCanonicalName(IExportConfig.class);
        String className = String.format("%s.%sExportConfig", basePackage, StringUtils.capitalize(type));
        Class clazz = ClassUtils.getClass(className);
        if (ClassUtils.isAssignable(clazz, IExportConfig.class)) {
            return clazz;
        } else {
            throw new WinCrawlerException(String.format("exportConfig class[%s] can not be found", className));
        }
    }


}

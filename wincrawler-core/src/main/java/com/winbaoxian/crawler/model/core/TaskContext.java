package com.winbaoxian.crawler.model.core;

import com.winbaoxian.crawler.enums.StepSignal;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-02-28 16:13
 */
@Getter
@Setter
public class TaskContext implements Serializable {

    /**
     * 系统配置
     */
    private Map<String, String> systemConfig;

    /**
     * 全局数据
     */
    private Map<String, Object> globalParams;

    /**
     * 需要持久化保存的数据
     */
    private Map<String, Object> currentStoreParams;
    private List<Object> currentStoreResult;
    private String currentStoreMsg;

    /**
     * 任务对象
     */
    private CrawlerTaskDTO taskDTO;

    /**
     * 模板对象
     */
    private CrawlerTemplateDTO templateDTO;

    /**
     * 供selenium Step使用
     */
    private WebDriver webDriver;
    /**
     * 步骤深度
     */
    private int stepDepth = 0;
    /**
     * 步骤执行后行为
     */
    private StepSignal stepSignal = StepSignal.step_next;

    public TaskContext() {
        globalParams = new HashMap<>();
        currentStoreParams = new HashMap<>();
        currentStoreResult = new ArrayList<>();
    }

    public void clearCurrentData() {
        currentStoreParams.clear();
        currentStoreResult.clear();
        currentStoreMsg = null;
    }

}

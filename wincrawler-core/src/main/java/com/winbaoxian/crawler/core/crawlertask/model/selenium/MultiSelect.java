package com.winbaoxian.crawler.core.crawlertask.model.selenium;

import com.winbaoxian.crawler.model.core.SelectConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiSelect extends AbstractXPathStep {

    private static String DEFAULT_STEP_NAME = "下拉框多选";

    /**
     * 选择配置
     */
    private List<SelectConfig> selectConfigList;

    public MultiSelect() {
        setName(DEFAULT_STEP_NAME);
    }

}

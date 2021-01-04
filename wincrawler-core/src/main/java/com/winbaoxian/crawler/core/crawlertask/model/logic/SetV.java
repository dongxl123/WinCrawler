package com.winbaoxian.crawler.core.crawlertask.model.logic;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SetV extends AbstractLogicStep  {

    private static String DEFAULT_STEP_NAME = "设置属性";

    /**
     * 设置的属性集合
     */
    private Map<String,String> values;

    /**
     * 别名
     */
    private String alias;

    public SetV() {
        setName(DEFAULT_STEP_NAME);
    }

}

package com.winbaoxian.crawler.core.crawlertask.model.logic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForGroup extends AbstractGroupStep {

    private static String DEFAULT_STEP_NAME = "遍历数据";
    /**
     * 遍历对象列表
     */
    private String listStr;
    /**
     * 遍历对象列表
     */
    private List<Object> list;
    /**
     * 遍历对象列表对象别名
     */
    private String iterAlias;
    /**
     * 全局变量, 默认false，true:将子步骤中返回的数据存储到globalParams
     */
    private Boolean globalVarsFlag;

    public ForGroup() {
        setName(DEFAULT_STEP_NAME);
    }
}

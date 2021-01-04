package com.winbaoxian.crawler.core.crawlertask.model.logic;

import com.winbaoxian.crawler.core.crawlertask.model.IStep;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class AbstractGroupStep extends AbstractLogicStep {

    /**
     * 是否需要存储结果数据
     */
    private Boolean needStore;
    /**
     * 存储结果数据key
     */
    private String storeKey;

    /**
     * 内部包含步骤组
     */
    private List<IStep> stepList;

    /**
     * 是否需要更新结果数据
     */
    private Boolean needUpdate;

    /**
     * 更新结果数据判断条件，匹配则更新数据
     */
    private String updateCondition;
}

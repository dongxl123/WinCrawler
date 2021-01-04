package com.winbaoxian.crawler.core.crawlertask.model;

import com.winbaoxian.crawler.enums.StepType;

import java.io.Serializable;

public interface IStep extends Serializable {

    StepType getType();

    String getName();

}

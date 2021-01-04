package com.winbaoxian.crawler.model.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SelectConfig implements Serializable {

    private SelectType type;

    private String value;

    public enum SelectType {
        byIndex,
        byValue,
        byText;
    }

}

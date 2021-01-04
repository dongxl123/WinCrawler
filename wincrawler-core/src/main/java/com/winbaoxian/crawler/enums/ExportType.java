package com.winbaoxian.crawler.enums;

public enum ExportType {
    simple,
    tpl,
    stepTpl,
    ;

    public static ExportType getExportType(String type) {
        for (ExportType exportType : ExportType.values()) {
            if (exportType.name().equalsIgnoreCase(type)) {
                return exportType;
            }
        }
        return null;
    }

}

package com.winbaoxian.crawler.core.exporttask.model;

import com.winbaoxian.crawler.enums.ExportType;

import java.io.Serializable;

public interface IExportConfig extends Serializable {

    ExportType getType();

}

package com.winbaoxian.crawler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫任务(CRAWLER_TASK)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-06-28
 */
@Setter
@Getter
public class CrawlerTaskDTO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 参数
     */
    private String params;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 数据合并到指定的taskId中
     */
    private Long mergedTaskId;

    /**
     * 不配置时，默认使用crawler_template的export_config
     */
    private String exportConfig;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
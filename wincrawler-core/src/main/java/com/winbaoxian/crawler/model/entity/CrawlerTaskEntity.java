package com.winbaoxian.crawler.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫任务(CRAWLER_TASK)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-06-28
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "CRAWLER_TASK")
public class CrawlerTaskEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 任务名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 参数，json格式
     */
    @Column(name = "PARAMS")
    private String params;

    /**
     * 模板ID
     */
    @Column(name = "TEMPLATE_ID")
    private Long templateId;

    /**
     * 数据合并到指定的taskId中
     */
    @Column(name = "merged_task_id")
    private Long mergedTaskId;
    /**
     * 不配置时，默认使用crawler_template的export_config
     */
    @Column(name = "export_config")
    private String exportConfig;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
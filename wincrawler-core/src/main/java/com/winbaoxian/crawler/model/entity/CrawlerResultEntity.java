package com.winbaoxian.crawler.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫结果(CRAWLER_RESULT)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-07-09
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "CRAWLER_RESULT")
public class CrawlerResultEntity implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 8362681218141887425L;

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
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 任务ID
     */
    @Column(name = "TASK_ID")
    private Long taskId;

    /**
     * 结果key, k1_v1-k2_v2...
     */
    @Column(name = "KEY_NAME")
    private String keyName;

    /**
     * 参数内容, json格式
     */
    @Column(name = "PARAMS")
    private String params;

    /**
     * 结果内容, json格式
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 记录关键信息或异常信息
     */
    @Column(name = "MSG")
    private String msg;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
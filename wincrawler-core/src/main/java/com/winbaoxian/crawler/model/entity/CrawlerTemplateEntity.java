package com.winbaoxian.crawler.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫模板，一个系统对应一个模板(CRAWLER_TEMPLATE)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-06-28
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "CRAWLER_TEMPLATE")
public class CrawlerTemplateEntity implements Serializable {

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
     * 创建人ID
     */
    @CreatedBy
    @Column(name = "CREATOR_UID")
    private Long creatorUid;

    /**
     * 模板名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 网站ID
     */
    @Column(name = "WEBSITE_ID")
    private String websiteId;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 使用代理方式，0:不使用，1:当请求被拦截时使用代理，2:始终使用
     */
    @Column(name = "USE_PROXY_METHOD")
    private Integer useProxyMethod;

    /**
     * 使用代理或切换代理的匹配条件
     */
    @Column(name = "USE_PROXY_CONDITION")
    private String useProxyCondition;

    /**
     * 参数
     */
    @Column(name = "PARAMS")
    private String params;

    /**
     * 参数可选范围，json格式
     */
    @Column(name = "PARAMS_RANGE")
    private String paramsRange;

    /**
     * 步骤配置，json格式
     */
    @Column(name = "MAIN_STEPS")
    private String mainSteps;

    /**
     * 文件导出配置，json格式
     */
    @Column(name = "EXPORT_CONFIG")
    private String exportConfig;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
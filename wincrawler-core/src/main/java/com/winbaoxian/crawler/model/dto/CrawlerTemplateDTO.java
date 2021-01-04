package com.winbaoxian.crawler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫模板，一个系统对应一个模板(CRAWLER_TEMPLATE)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-06-28
 */
@Setter
@Getter
public class CrawlerTemplateDTO implements Serializable {

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
     * 创建人ID
     */
    private Long creatorUid;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 网站ID
     */
    private String websiteId;

    /**
     * 描述
     */
    private String description;

    /**
     * 使用代理方式，0:不使用，1:当请求被拦截时使用代理，2:始终使用
     */
    private Integer useProxyMethod;

    /**
     * 使用代理或切换代理的匹配条件
     */
    private String useProxyCondition;

    /**
     * 参数
     */
    private String params;

    /**
     * 参数可选范围，json格式
     */
    private String paramsRange;

    /**
     * 步骤配置，json格式
     */
    private String mainSteps;

    /**
     * 文件导出配置，json格式
     */
    private String exportConfig;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
package com.winbaoxian.crawler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * SYSTEM_CONFIG
 *
 * @author bianj
 * @version 1.0.0 2019-06-28
 */
@Setter
@Getter
public class SystemConfigDTO implements Serializable {

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
     * 名称
     */
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
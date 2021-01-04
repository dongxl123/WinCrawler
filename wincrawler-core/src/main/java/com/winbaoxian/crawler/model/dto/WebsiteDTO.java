package com.winbaoxian.crawler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * WEBSITE
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-07-18
 */
@Setter
@Getter
public class WebsiteDTO implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 8335934416323221124L;

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
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
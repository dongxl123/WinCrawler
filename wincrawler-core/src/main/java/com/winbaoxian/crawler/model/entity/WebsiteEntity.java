package com.winbaoxian.crawler.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * WEBSITE
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-07-18
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "WEBSITE")
public class WebsiteEntity implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 8335934416323221124L;

    /**
     * 主键
     */
    @Id
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
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 地址
     */
    @Column(name = "URL")
    private String url;

    /**
     * 用户名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
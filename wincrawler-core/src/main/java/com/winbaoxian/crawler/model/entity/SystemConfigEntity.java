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
 * SYSTEM_CONFIG
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-06-28
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "SYSTEM_CONFIG")
public class SystemConfigEntity implements Serializable {

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
     * 名称
     */
    @Column(name = "KEY")
    private String key;

    /**
     * 值
     */
    @Column(name = "VALUE")
    private String value;

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
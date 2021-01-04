package com.winbaoxian.crawler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫结果(CRAWLER_RESULT)
 *
 * @author dongxuanliang252
 * @version 1.0.0 2019-07-09
 */
@Getter
@Setter
public class CrawlerResultDTO implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 8362681218141887425L;

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
     * 任务ID
     */
    private Long taskId;

    /**
     * 结果key, k1_v1-k2_v2...
     */
    private String keyName;

    /**
     * 参数内容, json格式
     */
    private String params;

    /**
     * 结果内容, json格式
     */
    private String result;

    /**
     * 记录关键信息或异常信息
     */
    private String msg;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
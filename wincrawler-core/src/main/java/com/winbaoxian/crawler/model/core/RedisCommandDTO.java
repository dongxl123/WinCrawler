package com.winbaoxian.crawler.model.core;

import com.winbaoxian.crawler.enums.RedisSupportCommandType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 */
@Setter
@Getter
public class RedisCommandDTO {

    private RedisSupportCommandType commandType;
    private String[] args;

}

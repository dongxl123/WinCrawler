package com.winbaoxian.crawler.core.crawlertask.model.ajax;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.winbaoxian.crawler.core.crawlertask.model.AbstractStep;
import com.winbaoxian.crawler.enums.RequestContentType;
import com.winbaoxian.crawler.enums.RequestMethod;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractAjaxStep extends AbstractStep {

    /**
     * 执行后延迟时间，单位：毫秒
     */
    private Long delayTime;

    /**
     * 别名，当alias不为空时，存储数据
     */
    private String alias;

    /**
     * 请求头
     */
    private Map<String, String> requestHeader;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private Map<String, String> requestParams;

    /**
     * 请求方式
     */
    private RequestMethod requestMethod;

    /**
     * 请求数据类型
     */
    @JSONField(serializeUsing = RequestContentTypeSerializer.class, deserializeUsing = RequestContentTypeDeserializer.class)
    private RequestContentType requestContentType;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应编码
     */
    private String responseCharset;

    /**
     * 响应结果错误信息模板
     */
    private String errorMsgTpl;

    /**
     * 返回结果文件保存路径
     */
    private String storeFilePath;

    /**
     * url编码,默认true
     */
    private Boolean urlEncodingEnabled;


    public static class RequestContentTypeSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            if (object != null) {
                RequestContentType value = (RequestContentType) object;
                serializer.write(value.getName());
            } else {
                serializer.writeNull();
            }
        }
    }

    public static class RequestContentTypeDeserializer implements ObjectDeserializer {

        @Override
        public RequestContentType deserialze(DefaultJSONParser parser, Type fieldType, Object fieldName) {
            String value = parser.getLexer().stringVal();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return RequestContentType.getRequestContentType(value);
        }

        @Override
        public int getFastMatchToken() {
            return 0;
        }
    }


}

package com.winbaoxian.crawler.core.crawlertask.executor.ajax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.constant.WinCrawlerConstant;
import com.winbaoxian.crawler.core.common.ParamsExecutor;
import com.winbaoxian.crawler.core.crawlertask.executor.IStepExecutor;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.AbstractAjaxStep;
import com.winbaoxian.crawler.core.crawlertask.model.ajax.Ocr;
import com.winbaoxian.crawler.enums.RequestContentType;
import com.winbaoxian.crawler.enums.RequestMethod;
import com.winbaoxian.crawler.enums.StepSignal;
import com.winbaoxian.crawler.enums.SystemConfigKey;
import com.winbaoxian.crawler.exception.WinCrawlerException;
import com.winbaoxian.crawler.model.core.TaskContext;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.utils.HttpUtils;
import com.winbaoxian.crawler.utils.ProxyUtils;
import com.winbaoxian.crawler.utils.WinCrawlerLogUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.config.DecoderConfig.decoderConfig;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.SSLConfig.sslConfig;


@Slf4j
public abstract class AbstractAjaxStepExecutor<T extends AbstractAjaxStep> implements IStepExecutor<T> {

    @Resource
    private ParamsExecutor paramsExecutor;

    private static final int CONNECTION_TIMEOUT = 1000 * 60 * 2;

    static {
        //默认UTF-8编码、redirect、trust all ssl
        RestAssured.config = RestAssured.config()
                .httpClient(httpClientConfig().setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, CONNECTION_TIMEOUT)
                        .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, (long) CONNECTION_TIMEOUT))
                .encoderConfig(encoderConfig().defaultContentCharset(Charset.defaultCharset()))
                .redirect(redirectConfig().followRedirects(false))
                .sslConfig(sslConfig().relaxedHTTPSValidation());
    }

    @Override
    public void execute(T step, TaskContext context) throws Exception {
        JSONObject response = preExecute(step, context);
        //存储结果
        Object ret = doExecute(step, context, response);
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        if (StringUtils.isNotBlank(step.getAlias())) {
            context.getGlobalParams().put(step.getAlias(), ret);
            log.info("[{}]globalParams,add key:{},values:{}", stepLogPrefix, step.getAlias(), JsonUtils.INSTANCE.toJSONString(ret));
        }
        if (step.getDelayTime() != null) {
            log.info("[{}]DelayTime, time: {}", stepLogPrefix, step.getDelayTime());
            Thread.sleep(step.getDelayTime());
        }
    }

    protected Object doExecute(T step, TaskContext context, JSONObject response) throws Exception {
        if (StringUtils.isNotBlank(step.getErrorMsgTpl())) {
            String errorMsg = renderTpl(step.getErrorMsgTpl(), context, response);
            if (StringUtils.isNotBlank(errorMsg)) {
                context.setCurrentStoreMsg(errorMsg);
                context.setStepSignal(StepSignal.step_throwe);
                return errorMsg;
            }
        }
        return doSuccessExecute(step, context, response);
    }

    protected abstract Object doSuccessExecute(T step, TaskContext context, JSONObject response) throws Exception;

    private JSONObject preExecute(T step, TaskContext context) throws Exception {
        RequestSpecification specification = given().log().everything();
        String stepLogPrefix = WinCrawlerLogUtils.INSTANCE.getStepLogPrefix(step, context);
        //ContentType
        RequestContentType requestContentType = step.getRequestContentType();
        //RequestMethod
        RequestMethod reqMethod = step.getRequestMethod();
        if (requestContentType != null) {
            specification.contentType(requestContentType.getName());
        } else if (!RequestMethod.GET.equals(reqMethod)) {
            specification.contentType(ContentType.TEXT);
        }
        //RequestHeader
        Map<String, String> requestHeader = step.getRequestHeader();
        if (MapUtils.isNotEmpty(requestHeader)) {
            specification.headers(requestHeader);
        }
        //RequestParams
        Map<String, String> requestParams = step.getRequestParams();
        if (MapUtils.isNotEmpty(requestParams)) {
            specification.queryParams(requestParams);
        }
        RestAssuredConfig config = RestAssured.config();
        //RequestBody
        String requestBody = step.getRequestBody();
        if (StringUtils.isNotBlank(requestBody)) {
            if (Arrays.asList(RequestContentType.XML, RequestContentType.JSON, RequestContentType.TEXT).contains(requestContentType)) {
                specification.body(requestBody);
            } else if (RequestContentType.FORM.equals(requestContentType)) {
                specification.formParams(getFormParams(requestBody));
            } else if (RequestContentType.MULTIPART.equals(requestContentType)) {
                JSONObject multiPartParams = JSON.parseObject(requestBody);
                if (multiPartParams != null || multiPartParams.size() == 1) {
                    Set<String> keys = multiPartParams.keySet();
                    String k = (String) CollectionUtils.get(keys, 0);
                    String v = multiPartParams.getString(k);
                    String mimeType = RequestContentType.BINARY.getName();
                    String fileName = v.lastIndexOf("/") >= 0 ? v.substring(v.lastIndexOf("/") + 1) : v;
                    Path path = Paths.get(fileName);
                    try {
                        mimeType = Files.probeContentType(path);
                    } catch (IOException e) {
                    }
                    if (StringUtils.startsWithIgnoreCase(v, WinCrawlerConstant.STRING_HTTP)) {
                        byte[] bytes = HttpUtils.INSTANCE.doFileGet(v);
                        specification.multiPart(k, fileName, bytes, mimeType);
                    }
                }
            } else if (RequestContentType.BINARY.equals(requestContentType)) {
                // specification.
            } else if (RequestContentType.EB.equals(requestContentType)) {
                config = config.encoderConfig(encoderConfig().defaultCharsetForContentType(Charset.defaultCharset(), ContentType.JSON).encodeContentTypeAs(RequestContentType.EB.getName(), ContentType.JSON));
                specification.body(requestBody);
            } else {
                config = config.encoderConfig(encoderConfig().encodeContentTypeAs(requestContentType.getName(), ContentType.TEXT));
                specification.body(requestBody);
            }
        }
        if (StringUtils.isNotBlank(step.getResponseCharset())) {
            config = RestAssured.config().decoderConfig(decoderConfig().defaultContentCharset(step.getResponseCharset()));
        }
        //设置config
        specification.config(config);
        //set urlEncoding disabled
        if (step.getUrlEncodingEnabled() != null) {
            specification.urlEncodingEnabled(step.getUrlEncodingEnabled());
        }
        String requestUrl = step.getRequestUrl();
        Method requestMethod;
        if (RequestMethod.POST.equals(reqMethod)) {
            requestMethod = Method.POST;
        } else if (RequestMethod.GET.equals(reqMethod)) {
            requestMethod = Method.GET;
        } else if (RequestMethod.PUT.equals(reqMethod)) {
            requestMethod = Method.PUT;
        } else if (RequestMethod.DELETE.equals(reqMethod)) {
            requestMethod = Method.DELETE;
        } else {
            requestMethod = Method.GET;
        }
        Response response = null;
        JSONObject ret = null;
        int retryTimes = 3;
        int index = 0;
        //代理开关
        Map<String, String> configMap = context.getSystemConfig();
        CrawlerTemplateDTO templateDTO = context.getTemplateDTO();
        String useProxyKey = SystemConfigKey.ajax_use_proxy.name();
        boolean needUseProxy = configMap.containsKey(useProxyKey) && configMap.get(useProxyKey).equals(WinCrawlerConstant.SYSTEM_CONFIG_VALUE_TRUE)
                && templateDTO.getUseProxyMethod() != null && templateDTO.getUseProxyMethod() != 0;
        boolean proxyFlag = false;
        if (templateDTO.getUseProxyMethod() == 2) {
            proxyFlag = true;
        }
        while (index++ <= retryTimes) {
            //proxy
            try {
                log.info("[{}]尝试第{}次发送请求", stepLogPrefix, index);
                if (needUseProxy && proxyFlag) {
                    String[] proxyValues = ProxyUtils.INSTANCE.getAjaxProxyIpPort();
                    if (ArrayUtils.isNotEmpty(proxyValues)) {
                        specification.proxy(proxyValues[0], Integer.parseInt(proxyValues[1]));
                    }
                }
                response = specification.request(requestMethod, requestUrl);
                //request timeout
                response.then().log().body();
            } catch (Exception e) {
                log.error("[{}]第{}次请求失败", stepLogPrefix, index, e);
                if (e instanceof ConnectException) {
                    if (needUseProxy && proxyFlag) {
                        ProxyUtils.INSTANCE.changeAjaxProxy();
                    } else {
                        Thread.sleep(1000 * 60L);
                    }
                }
            }
            if (response != null) {
                //存储文件处理
                byte[] responseByteArray = null;
                String responseString = null;
                try {
                    ret = new JSONObject();
                    ret.put("statusCode", response.getStatusCode());
                    ret.put("headers", response.getHeaders());
                    ret.put("cookies", response.getCookies());
                    if (StringUtils.isNotBlank(step.getStoreFilePath()) || step instanceof Ocr) {
                        responseByteArray = response.asByteArray();
                    }
                    responseString = response.asString();
                    if (StringUtils.isBlank(step.getStoreFilePath())) {
                        ret.put("body", responseString);
                    }
                } catch (Exception e) {
                    log.error("[{}] response read data error", stepLogPrefix);
                    if (needUseProxy) {
                        ProxyUtils.INSTANCE.changeAjaxProxy();
                        continue;
                    }
                }
                if (needUseProxy) {
                    try {
                        if (ProxyUtils.INSTANCE.isInvalidProxy(responseString)) {
                            ProxyUtils.INSTANCE.changeAjaxProxy();
                            continue;
                        }
                        String conValue = renderTpl(templateDTO.getUseProxyCondition(), context, ret);
                        if (Boolean.TRUE.toString().equalsIgnoreCase(conValue)) {
                            proxyFlag = true;
                            if (index > 2) {
                                ProxyUtils.INSTANCE.changeAjaxProxy();
                            }
                            continue;
                        }
                    } catch (Exception e) {
                        log.error("[{}]UseProxyCondition render error, tpl:{}, model:{}", stepLogPrefix, templateDTO.getUseProxyCondition(), JsonUtils.INSTANCE.toJSONString(ret));
                    }
                }
                if (StringUtils.isNotBlank(step.getStoreFilePath())) {
                    String storeFilePathName = null;
                    try {
                        storeFilePathName = renderTpl(step.getStoreFilePath(), context, ret);
                        File file = new File(storeFilePathName);
                        file.getParentFile().mkdirs();
                        FileUtils.writeByteArrayToFile(file, responseByteArray);
                        break;
                    } catch (Exception e) {
                        log.error("[{}]保存文件错误, fileName: {}", stepLogPrefix, storeFilePathName, e);
                    }
                } else {
                    if (step instanceof Ocr) {
                        ret.put("body", responseByteArray);
                    } else {
                        ret.put("body", JsonUtils.INSTANCE.parseObject(responseString));
                    }
                    break;
                }
            }
        }
        return ret;
    }

    protected String renderTpl(String tpl, TaskContext context, JSONObject response) throws Exception {
        if (StringUtils.isBlank(tpl)) {
            return null;
        }
        Map<String, Object> localModel = new HashMap<>();
        if (MapUtils.isNotEmpty(context.getGlobalParams())) {
            localModel.putAll(context.getGlobalParams());
        }
        if (MapUtils.isNotEmpty(context.getCurrentStoreParams())) {
            localModel.putAll(context.getCurrentStoreParams());
        }
        if (MapUtils.isNotEmpty(response)) {
            localModel.putAll(response);
        }
        return paramsExecutor.render(tpl, localModel);
    }

    private Map<String, String> getFormParams(String requestBody) {
        Object o = JsonUtils.INSTANCE.parseObject(requestBody);
        if (o instanceof JSONObject) {
            Map<String, String> formParams = new HashMap<>();
            JSONObject jo = (JSONObject) o;
            for (String key : jo.keySet()) {
                Object value = jo.get(key);
                if (value instanceof String) {
                    formParams.put(key, (String) value);
                } else {
                    formParams.put(key, JsonUtils.INSTANCE.toJSONString(value));
                }
            }
            return formParams;
        } else if (o instanceof JSONArray) {
            throw new WinCrawlerException("requestBody格式不对,请检查");
        } else if (o instanceof String) {
            String kvStrList = (String) o;
            if (StringUtils.isBlank(kvStrList)) {
                return MapUtils.EMPTY_MAP;
            }
            String[] parameterParts = StringUtils.split(kvStrList, "&");
            if (parameterParts.length < 1) {
                return MapUtils.EMPTY_MAP;
            }
            Map<String, String> formParams = new HashMap<>();
            for (String parameterPart : parameterParts) {
                String[] ps = StringUtils.split(parameterPart, "=");
                if (ps.length < 1) {
                    continue;
                }
                if (ps.length < 2) {
                    formParams.put(StringUtils.trim(ps[0]), StringUtils.EMPTY);
                } else {
                    try {
                        formParams.put(StringUtils.trim(ps[0]), URLDecoder.decode(StringUtils.trim(ps[1]), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        log.error("getFormParams异常", e);
                    }
                }
            }
            return formParams;
        }
        return MapUtils.EMPTY_MAP;
    }

}

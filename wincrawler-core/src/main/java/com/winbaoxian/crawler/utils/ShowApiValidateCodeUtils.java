package com.winbaoxian.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.crawler.third.showapi.ShowApiRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public enum ShowApiValidateCodeUtils {

    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(ShowApiValidateCodeUtils.class);

    public String validateCode(String appId, String appSecret, byte[] bytes) {
        try {
            String imgCode = new ShowApiRequest("http://route.showapi.com/184-5", appId, appSecret)
                    .addTextPara("img_base64", Base64.encodeBase64String(bytes))
                    .addTextPara("typeId", "34")
                    .addTextPara("convert_to_jpg", "0")
                    .addTextPara("needMorePrecise", "0")
                    .post();
            log.info("response:{}", imgCode);
            JSONObject res = JSON.parseObject(imgCode);
            if (res.containsKey("showapi_res_code") && res.getIntValue("showapi_res_code") == 0
                    && res.containsKey("showapi_res_body") && res.getJSONObject("showapi_res_body") != null) {
                return res.getJSONObject("showapi_res_body").getString("Result");
            }
        } catch (Exception e) {
            log.error("ShowApi validateCode failed", e);
        }
        return null;
    }
}

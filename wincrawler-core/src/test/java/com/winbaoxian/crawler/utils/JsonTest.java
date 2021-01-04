package com.winbaoxian.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JsonTest extends BaseTest {

    @Test
    public void testJSON() {
        Map<String, Object> model = new HashMap<>();
        model.put("a", 1);
        model.put("b", "");
        model.put("c", null);
        System.out.println(JSON.toJSONString(model));
        System.out.println(JsonUtils.INSTANCE.toJSONString(model));
    }

}

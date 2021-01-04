package com.winbaoxian.crawler.example;

import com.winbaoxian.crawler.example.function.GenerateIdNumFunction;
import com.winbaoxian.crawler.example.function.MakeDateByDeltaFunction;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-15 12:51
 */
@Component
@Slf4j
public class FunctionLoader {

    @Resource
    private FreeMarkerConfigurer freemarkerConfig;

    @PostConstruct
    public void initFunction() {
        try {
            Configuration configuration = freemarkerConfig.getConfiguration();
            configuration.setSharedVariable("generateIdNum", new GenerateIdNumFunction());
            configuration.setSharedVariable("makeDateByDelta", new MakeDateByDeltaFunction());
        } catch (Exception e) {
            log.error("freemarker init error", e);
        }
    }
}

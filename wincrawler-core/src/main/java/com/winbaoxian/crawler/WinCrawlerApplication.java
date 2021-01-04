package com.winbaoxian.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author dongxuanliang252
 * @date 2019-6-27 17:58:45
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class WinCrawlerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(WinCrawlerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WinCrawlerApplication.class, args);
    }

}

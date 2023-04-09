package com.example.ai_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件路径配置，可以直接访问
 * 映射的路径后面必须加/，否则访问不到
 */
@Configuration
public class FilePathConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的User目录下
        registry.addResourceHandler("/Users/**").addResourceLocations("file:E:/java/AI_demo/Users/");
    }

}
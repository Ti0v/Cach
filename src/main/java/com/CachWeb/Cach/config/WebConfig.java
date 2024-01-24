package com.CachWeb.Cach.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        // ... other configurations
//        engine.addDialect(new LayoutDialect());
//        return engine;
//    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");// Use "HTML" instead of "Html"
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/404").setViewName("error/404");
    }
}




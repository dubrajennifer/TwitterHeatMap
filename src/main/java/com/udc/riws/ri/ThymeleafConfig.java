package com.udc.riws.ri;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;


/**
 * Beans to setting Thymeleaf.
 */
@Configuration
public class ThymeleafConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String VIEWS = "/views/";

    @Value("${thymeleaf.template.cache:false}")
    private String cacheEnabled;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(this.templateEngine());
        resolver.setCharacterEncoding(CHARACTER_ENCODING);
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(this.templateResolver());
        engine.addDialect(new LayoutDialect(new GroupingStrategy()));
        return engine;
    }

    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(this.applicationContext);
        resolver.setPrefix(VIEWS);
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(CHARACTER_ENCODING);
        resolver.setCacheable(Boolean.valueOf(this.cacheEnabled));
        return resolver;
    }



}
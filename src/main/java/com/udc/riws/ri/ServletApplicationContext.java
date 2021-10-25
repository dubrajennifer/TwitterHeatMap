package com.udc.riws.ri;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;


/**
 * Beans to inject in servlet context and settings.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = Application.class)
public class ServletApplicationContext implements WebMvcConfigurer {

    private static final String RESOURCES_LOCATION = "/resources/";
    private static final String RESOURCES_HANDLER = RESOURCES_LOCATION + "**";
    private static final int RESOURCES_CACHE_AGE_DAYS = 1;

    /**
     * Configure a handler to delegate unhandled requests by forwarding to the Servlet container's "default" servlet.
     * 
     * @param configurer servlet
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // Requests for static resources are forwarded to default servlet server.
        configurer.enable();
    }

    /**
     * Provides the URL path patterns for which the handler should be invoked to serve static resources.
     * 
     * @param registry with resources
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION)
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
    }

    /**
     * Resolves ${...} placeholders within bean definition property values and @Value annotations against the current
     * Spring Environment and its set of PropertySources.
     *
     * @return Specialization of {@code PlaceholderConfigurerSupport}
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

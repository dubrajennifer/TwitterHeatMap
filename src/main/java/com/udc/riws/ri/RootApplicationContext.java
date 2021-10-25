package com.udc.riws.ri;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Beans to inject in root context.
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
public class RootApplicationContext {

}

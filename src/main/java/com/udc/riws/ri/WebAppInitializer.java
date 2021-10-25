package com.udc.riws.ri;


import com.udc.riws.ri.service.configuration.ServicesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * {@link DispatcherServlet} configuration (instead of a web.xml file).
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAppInitializer.class);



    /**
     * Identifies the paths that {@link DispatcherServlet} will be mapped to.
     *
     * @return paths to map in {@link DispatcherServlet}
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    /**
     * Specifies the classes that contains the beans to inject in root context.
     *
     * @return classes with beans.
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        LOGGER.info("portal initialization started.");
        LOGGER.info("ApplicationInitializer: Configuring Root Config Classes");
        return new Class<?>[]{ServicesConfiguration.class, RootApplicationContext.class};
    }


    /**
     * Specifies the classes that contains the beans to inject in servlet context.
     *
     * @return classes with beans.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        LOGGER.info("WebAppInitializer: Configuring Spring Web Config Classes");
        return new Class<?>[] { ServletApplicationContext.class, ThymeleafConfig.class };
    }
}
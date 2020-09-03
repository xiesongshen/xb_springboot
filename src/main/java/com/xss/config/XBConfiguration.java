package com.xss.config;

import com.xss.code.GenerateImageCodeServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@Configuration
public class XBConfiguration extends WebMvcConfigurerAdapter {
    

    /**
     * 验证码
     * @return
     */
    @Bean
    public ServletRegistrationBean myServlet(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new GenerateImageCodeServlet(),"/generateCode");
        return registrationBean;
    }


    @Value("${file.realPath}")
    private String realPath; //真实路径

    @Value("${file.path}")
    private String path;     //虚拟路径

    /*
     * @param
     * @return void
     * @desc  映射/uploadImage下的路径取磁盘找
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(path).addResourceLocations("file:" + realPath);
    }
}

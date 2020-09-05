package com.xss.config;

import com.xss.code.GenerateImageCodeServlet;
import com.xss.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

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


    @Autowired
    LoginInterceptor loginInterceptor;

    /*
     * @param
     * @return void
     * @desc  拦截处理
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/checkEmail/**")
                .excludePathPatterns("/user/checkUsername/**")
                .excludePathPatterns("/user/register/**")
                .excludePathPatterns("/user/sendEmail/**")
                .excludePathPatterns("/user/login/**")
                .excludePathPatterns("/user/logout/**")
                .excludePathPatterns("/generateCode/**")
                .excludePathPatterns("/to_wxLogin/**")
                .excludePathPatterns("/wx_login/**")
                .excludePathPatterns("/generateCode/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/gorget.html")
                .excludePathPatterns("/register.html")
                .excludePathPatterns("/bootstrap/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/fonts/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/vendor/**")
                .excludePathPatterns("/assets/**")
                .excludePathPatterns("/upload/**");
    }

    /*
     * @param
     * @return org.springframework.web.socket.server.standard.ServerEndpointExporter
     * @desc  开启WebSocket支持
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}

package com.xss.interceptor;

import com.xss.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XSS
 * @date 2020/9/4
 * @desc
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        if (LoginUserUtil.getLoginUser() == null){

            response.sendRedirect("/index.html");

            return false;
        }

        return true;
    }
}


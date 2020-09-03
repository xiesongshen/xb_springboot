package com.xss.controller;

import com.xss.entity.Result;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@RestController
@RequestMapping("/common")
public class commonController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    HttpSession session;


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 邮箱发送验证码
     */
    @RequestMapping("sendCode")
    public Result sendCode(String email){
        System.out.println("邮箱是: " + email);

        String code = RandomStringUtils.randomNumeric(4);

        redisTemplate.opsForValue().set("updatePassword:code:" + session.getId(), code);
        System.out.println(code);

        return new Result(true, "发送成功", code);
    }

}

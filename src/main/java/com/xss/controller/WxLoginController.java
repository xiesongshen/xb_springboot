package com.xss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xss.entity.Result;
import com.xss.entity.User;
import com.xss.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author XSS
 * @date 2020/9/4
 * @desc
 */
@RestController
public class WxLoginController {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;

    @Value("${wx.redirect_uri}")
    private String redirect_uri;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    HttpSession session;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    /*
     * @param
     * @return void
     * @desc  请求授权
     */
    @RequestMapping("/to_wxLogin")
    public void to_wxLogin() throws Exception {

        //第一步：请求CODE
        String url = "https://open.weixin.qq.com/connect/qrconnect?appid="+appId +
                "&redirect_uri="+ URLEncoder.encode(redirect_uri)+
                "&response_type=code" +
                "&scope=snsapi_login";

        // 重定向到微信登录指定的地址进行微信登录授权,授权成功后返回code
        response.sendRedirect(url);
    }


    /*
     * @param
     * @return void
     * @desc   用户确认授权之后的操作
     */
    @RequestMapping("wx_login")
    public void wx_Login() throws IOException {

        // 用户扫码成功后携带过来的code
        String code = request.getParameter("code");

        //第二步：通过code获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId +
                "&secret="+appSecret+
                "&code="+code+
                "&grant_type=authorization_code";

        // 获取AccessToken、openid等数据
        HashMap info = auth(url);
        System.out.println("info: " + info);

        //第三步：通过access_token调用接口
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + info.get("access_token") +
                "&openid=" + info.get("openid");

        //获得用户信息
        HashMap userInfo = getUserInfo(url);
        System.out.println("userInfo: " + userInfo);

        // 根据微信的openid查询此用户原来有没有使用过微信登录
        User user = userService.findByWxOpenid(info.get("openid").toString());

        // 说明该用户是第一次使用微信登录
        if (user == null) {
            // 获取用户信息
            HashMap jsonObject = getUserInfo(url);

            user = new User();

            // 用户的头像
            user.setPic(jsonObject.get("headimgurl").toString());

            // 性别
            user.setGender(jsonObject.get("sex").toString());
            // 用户的昵称
            user.setRealName(jsonObject.get("nickname").toString());

            // 随机用户名(11位随机字符串)
            user.setUsername(UUID.randomUUID().toString().substring(36 - 15));

            user.setWxOpenid(info.get("openid").toString());
            // 注册一个新的用户
            userService.save(user);

            user = userService.findByWxOpenid(info.get("openid").toString());
        }

        session.setAttribute("userId", user.getId());

        // 修改登录时间
        userService.updateLoginTime(user.getId());
        redisTemplate.opsForValue().set("loginUser:" + user.getId(), user, 30, TimeUnit.MINUTES);

        response.sendRedirect("/html/wx_login_info.html");

    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  获得微信登录用户信息
     */
    @PostMapping("/getWxLoginInfo")
    public Result getWxLoginInfo() throws IOException {

        Object userId = session.getAttribute("userId");

        Map returnMap = new HashMap<>();

        returnMap.put("loginUser", redisTemplate.opsForValue().get("loginUser:" + userId));
        returnMap.put("userId", userId);

        return new Result(true, "获取微信登录信息成功", returnMap);
    }


    /**
     * 认证
     * 获取AccessToken、openid等数据
     * @param url
     * @return
     */
    private HashMap auth(String url) {

        try {
            // 创建一个http Client请求
            CloseableHttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();       // 获取响应数据(json)

            if (entity != null) {
                String result = EntityUtils.toString(entity, Charset.forName("UTF8"));

                return new ObjectMapper().readValue(result, HashMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求微信对外提供的接口获取用户信息
     * @param url
     * @return
     */
    private HashMap getUserInfo(String url) {

        try {
            CloseableHttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity, Charset.forName("UTF8"));

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(result, HashMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}

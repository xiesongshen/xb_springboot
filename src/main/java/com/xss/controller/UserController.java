package com.xss.controller;

import com.xss.entity.PageResult;
import com.xss.entity.Result;
import com.xss.entity.User;
import com.xss.service.DeptService;
import com.xss.service.UserService;
import com.xss.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DeptService deptService;


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  检查邮箱是否被注册
     */
    @RequestMapping("/checkEmail")
    public Result checkEmail(String email) {
        return new Result(true, "用户名已被注册", userService.checkEmail(email));
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  检查用户名是否被注册
     */
    @RequestMapping("/checkUsername")
    public Result checkUsername(String username) {
        return new Result(true, "用户名已被注册", userService.checkUsername(username));
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  新用户注册
     */
    @PostMapping("/doRegister")
    public Result doRegister(@RequestBody User user) {
        userService.save(user);
        return new Result(true, "注册成功");
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 发送邮箱验证码重置密码
     */
    @GetMapping("doReset")
    public Result doReset(String email, String password) {
        userService.updatePassword(email, password);

        if (LoginUserUtil.getLoginUser()!=null){

            redisTemplate.delete("loginUser:"+LoginUserUtil.getLoginUser().getId());

            session.invalidate();
        }

        return new Result(true, "修改成功");
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  登陆
     */
    @PostMapping("login/{checkCode}")
    public Result login(@RequestBody User user, @PathVariable String checkCode) {
        String safeCode = session.getAttribute("safeCode").toString();

        if (user.getUsername() == null || user.getPassword() == null){
            return new Result(false,"请输入用户名或密码");
        }

        if (!safeCode.equals(checkCode) || checkCode == null) {
            return new Result(false, "请输入正确验证码");
        }

        User dbUser = userService.checkUsername(user.getUsername());

        if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) {
            return new Result(false, "用户名或密码错误");
        }


        redisTemplate.opsForValue().set("loginUser:" + dbUser.getId(), dbUser, 30, TimeUnit.MINUTES);


        // 把userId存入session
        session.setAttribute("userId", dbUser.getId());

        //更改登陆时间
        userService.updateLoginTime(dbUser.getId());

        Map returnMap = new HashMap<>();
        returnMap.put("userId", dbUser.getId());

        // 密码置空
        dbUser.setPassword(null);
        returnMap.put("loginUser", dbUser);

        return new Result(true, "登录成功", returnMap);
    }


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 模糊查询+分页
     */
    @RequestMapping("/search/{page}")
    public Result search(String username, @PathVariable Integer page) {

        Page<User> pageData = userService.findPage("%" + username + "%", page);

        int totalPages = pageData.getTotalPages();
        if (totalPages == 0){
            totalPages++;
        }
        PageResult<User> pageResult = new PageResult<>(totalPages, pageData.getContent());


        // 查询当前登录的用户关注过的人
        List<Integer> ids = userService.findFocus(LoginUserUtil.getLoginUser().getId());

        Map returnMap = new HashMap();

        returnMap.put("pageResult", pageResult);
        returnMap.put("userFocus", ids);

        // 搜索条件回显
        returnMap.put("username", username);

        return new Result(true, "查询成功", returnMap);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  根据uid查询用户
     */
    @RequestMapping("findById")
    public Result findById(String uid) {
        return new Result(true, "查询成功", userService.findById(Long.parseLong(uid)));
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 增加被看数
     */
    @PutMapping("addLook")
    public Result addLook(@RequestBody User user) {
        if (user.getLook() == null) {
            user.setLook(1L);
        }
        Long look = user.getLook();
        look++;

        user.setLook(look);
        userService.updateLook(user);

        return new Result(true, "成功");
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  修改用户信息
     */
    @RequestMapping("/doUpdate")
    public Result doUpdate(@RequestBody User user){
        User dbUser = LoginUserUtil.getLoginUser();
        dbUser.setRealName(user.getRealName());
        dbUser.setDeptId(user.getDeptId());
        dbUser.setPhone(user.getPhone());
        dbUser.setAge(user.getAge());
        dbUser.setGender(user.getGender());
        dbUser.setIsSecret(user.getIsSecret());
        dbUser.setDeptName(deptService.findByDeptId(user.getDeptId()).getName());

        userService.update(dbUser);

        // 更新redis中的user信息
        redisTemplate.opsForValue().set("loginUser:" + dbUser.getId(), dbUser, 7, TimeUnit.DAYS);

        return new Result(true, "修改成功", user);
    }


    @Value("${file.realPath}")
    private String realPath; //真实路径

    @Value("${file.path}")
    private String path;     //虚拟路径

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  头像图片上传
     */
    @PostMapping("uploadPic")
    public Result uploadPic(MultipartFile pic) throws IOException {

        //图片上传的名字
        String filename = pic.getOriginalFilename();

        //获取图片类型(从.开始截取)
        String type = filename.substring(filename.lastIndexOf("."));

        //组建新的文件名(防止覆盖图片)
        filename = UUID.randomUUID().toString() + type;

        //文件上传真实路径
        String dirPath = realPath + filename;

        //写出图片
        pic.transferTo(new File(dirPath));

        //虚拟地址
        String url = "http://localhost:8080/uploadImage/" + filename;

        //更改数据库图片路径
        userService.updatePicUrl(LoginUserUtil.getLoginUser().getId(), url);

        LoginUserUtil.getLoginUser().setPic(url);

        // 更改Redis中的用户信息
        redisTemplate.opsForValue().set("loginUser:" + LoginUserUtil.getLoginUser().getId(), LoginUserUtil.getLoginUser(), 30, TimeUnit.MINUTES);

        // 把url带到前端(更改localStorage中的url地址)
        return new Result(true, "上传成功", url);
    }


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  查询用户关注列表
     */
    @RequestMapping("/myUserList/{page}")
    public Result myUserList(Long uid, @PathVariable Integer page){
        Page<User> pageData = userService.findFocusList(uid, page);

        PageResult<User> pageResult = new PageResult<>(pageData.getTotalPages(), pageData.getContent());

        return new Result(true,"成功",pageResult);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  根据部门id查找用户
     */
    @RequestMapping("findByDeptId")
    public Result findByDeptId(Long deptId){

        return new Result(true,"成功",userService.findByDeptId(deptId));
    }

    /*
     * @param
     * @return
     * @desc  登出
     */
    @RequestMapping("loginOut")
    public Result loginOut(){

        redisTemplate.delete("loginUser:"+LoginUserUtil.getLoginUser().getId());

        session.invalidate();

        return new Result(true,"登出成功");
    }
}

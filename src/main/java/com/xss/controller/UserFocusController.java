package com.xss.controller;

import com.xss.config.XbWebSocket;
import com.xss.entity.Result;
import com.xss.service.UserFocusService;
import com.xss.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@RestController
@RequestMapping("/userFocus")
public class UserFocusController {

    @Autowired
    UserFocusService userFocusService;

    @Autowired
    HttpSession session;


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  关注/取消关注
     */
    @RequestMapping("/focus/{focusId}")
    public Result focus(@PathVariable Long focusId) throws Exception {

        Long userId = LoginUserUtil.getLoginUser().getId();

        if (userId == focusId) {
            return new Result(false, "您不能对自己操作");
        }

        // 查询是否已经关注过
        Boolean isFucos = userFocusService.isFocus(userId, focusId);

        if (isFucos) {
            session.setAttribute("time"+focusId,new Date().getTime());

            // 已经关注过(取消关注)
            userFocusService.delFocus(userId, focusId);

            return new Result(true, "取关成功");
        }

        Long oldTime = (Long) session.getAttribute("time" + focusId);

        if (oldTime == null||new Date().getTime() - oldTime > 1*60*1000){

            XbWebSocket.sendMessage(focusId, LoginUserUtil.getLoginUser().getRealName() + "关注了您");
        }


        // 关注
        userFocusService.save(userId, focusId);

        return new Result(true, "关注成功");

    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  查询用户粉丝数
     */
    @RequestMapping("/selectFocusCount")
    public Result selectFocusCount(Long userId){
        return new Result(true,"成功",userFocusService.selectFocusCount(userId));
    }


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  查询用户关注数
     */
    @RequestMapping("/selectFCount")
    public Result selectFCount(Long userId){
        return new Result(true,"成功",userFocusService.selectFCount(userId));
    }


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 取消关注
     */
    @RequestMapping("/delFocus")
    public Result delFocus(Long userId,Long focusId){
        userFocusService.delFocus(userId,focusId);

        return new Result(true,"取关成功");
    }

}

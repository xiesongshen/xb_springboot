package com.xss.controller;

import com.xss.entity.Meeting;
import com.xss.entity.PageResult;
import com.xss.entity.Result;
import com.xss.service.MeetingJoinService;
import com.xss.service.MeetingService;
import com.xss.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/2
 * @desc
 */
@RestController
@RequestMapping("meeting")
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    MeetingJoinService meetingJoinService;

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 模糊查询+分页
     */
    @RequestMapping("/search/{page}")
    public Result search(String title, Long status, @PathVariable Integer page) {


        Page<Meeting> pageData = meetingService.findPage("%" + title + "%",status, page);

        int totalPages = pageData.getTotalPages();
        if (totalPages == 0){
            totalPages++;
        }

        PageResult<Meeting> pageResult = new PageResult<>(totalPages, pageData.getContent());

        Map returnMap = new HashMap();

        returnMap.put("pageResult", pageResult);

        // 搜索条件回显
        returnMap.put("title", title);
        returnMap.put("status",status);

        return new Result(true, "查询成功", returnMap);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  根据会议id查找会议信息
     */
    @RequestMapping("findById")
    public Result findById(Long mid){

        //查询会议信息
        Meeting meeting = meetingService.findById(mid);

        //查询应参加会议的人员及人数 
        String makeUser = meeting.getMakeUser();
        String[] shouldJoin = makeUser.split(",");

        //查询已到的人数
        Integer realCount = meetingJoinService.realJoin(mid);


        //查询自己是否需要参加会议
        Long uid = LoginUserUtil.getLoginUser().getId();
        Boolean isJoin =  meetingJoinService.checkJoin(mid,uid);


        Map returnMap = new HashMap();

        returnMap.put("meeting",meeting);

        //应该参加会议的人数
        returnMap.put("shouldCount",shouldJoin.length);

        returnMap.put("realCount",realCount);

        returnMap.put("isJoin",isJoin);

        returnMap.put("shouldJoin",shouldJoin);

        return new Result(true,"成功",returnMap);
    }


    @RequestMapping("changeJoin")
    public Result changeJoin(Long mid,Long uid){
        //判断自己是否加入会议

        Boolean isJoin =  meetingJoinService.checkJoin(mid,uid);

        if (isJoin){ //已参加会议，点击退出会议
            meetingJoinService.deleteMeetingJoin(mid, uid);
            return new Result(true,"成功退出");
        }

        //不在会议，点击参加会议
        meetingJoinService.save(mid,uid);
        return new Result(true,"成功参加");
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 发布会议
     */
    @PutMapping("save")
    public Result save(@RequestBody Meeting meeting){

        meetingService.save(meeting);
        return new Result(true,"发布成功");
    }
}

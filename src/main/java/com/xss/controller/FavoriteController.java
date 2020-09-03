package com.xss.controller;

import com.xss.entity.Result;
import com.xss.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@RequestMapping("favorite")
@RestController
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 查询文章被收藏数
     */
    @RequestMapping("findCollect")
    public Result findCollect(Long aid){
        return new Result(true,"成功",favoriteService.countFavoriteByAId(aid));
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  判断文章是否被收藏
     */
    @RequestMapping("checkCollect")
    public Result checkCollect(Long aid,Long uid){

        Boolean isCollect = favoriteService.checkCollect(aid, uid);

        return new Result(true,"成功",isCollect);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  收藏/取消收藏
     */
    @RequestMapping("changeCollect")
    public Result changeCollect(Long aid,Long uid){
        Boolean isCollect = favoriteService.checkCollect(aid, uid);

        if (isCollect){  //已收藏,取消收藏
            favoriteService.delCollect(aid,uid);
            return new Result(true,"取消收藏成功");
        }

        //未收藏,添加收藏
        favoriteService.saveCollect(aid, uid);
        return new Result(true,"收藏成功");
    }


}

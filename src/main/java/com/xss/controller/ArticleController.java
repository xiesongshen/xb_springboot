package com.xss.controller;

import com.xss.entity.PageResult;
import com.xss.entity.Result;
import com.xss.entity.Article;
import com.xss.entity.User;
import com.xss.service.ArticleService;
import com.xss.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    FavoriteService favoriteService;


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 模糊查询+分页
     */
    @RequestMapping("/search/{page}")
    public Result search(String title, @PathVariable Integer page) {

        Page<Article> pageData = articleService.findPage("%" + title + "%", page);

        int totalPages = pageData.getTotalPages();
        if (totalPages == 0){
            totalPages++;
        }
        PageResult<Article> pageResult = new PageResult<>(totalPages, pageData.getContent());


        Map returnMap = new HashMap();

        returnMap.put("pageResult", pageResult);

        // 搜索条件回显
        returnMap.put("title", title);

        return new Result(true, "查询成功", returnMap);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  发布新文章
     */
    @RequestMapping("doInsert")
    public Result doInsert(@RequestBody Article article) {
        articleService.save(article);

        return new Result(true, "发布成功");
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 增加浏览数
     */
    @RequestMapping("addBrowseCount")
    public Result addBrowseCount(@RequestBody Article article) {
        Long count = article.getBrowseCount();
        count++;
        article.setBrowseCount(count);

        articleService.updateBrowseCount(article);

        return new Result(true, "成功", article);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  根据文章id获取文章
     */
    @RequestMapping("findById")
    public Result findById(Long aid, Long uid) {
        //根据aid查找文章
        Article article = articleService.findById(aid);

        //查询是否被收藏
        Boolean isCollect = favoriteService.checkCollect(aid, uid);

        //被收藏数
        Integer count = favoriteService.countFavoriteByAId(aid);

        //查询我关注同时也收藏这篇文章的人
        List<User> collectUser = articleService.selectCollectUser(aid, uid);

        Map returnMap = new HashMap();
        returnMap.put("article", article);
        returnMap.put("isCollect", isCollect);
        returnMap.put("count", count);
        returnMap.put("collectUser", collectUser);

        return new Result(true, "成功", returnMap);
    }

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc 收藏文章 模糊查询+分页
     */
    @RequestMapping("/searchCollect/{page}")
    public Result searchCollect(String title, Long uid, @PathVariable Integer page) {

        Page<Article> pageData = articleService.findCollectPage(uid,"%" + title + "%", page);

        int totalPages = pageData.getTotalPages();
        if (totalPages == 0){
            totalPages++;
        }
        PageResult<Article> pageResult = new PageResult<>(totalPages, pageData.getContent());


        Map returnMap = new HashMap();

        returnMap.put("pageResult", pageResult);

        // 搜索条件回显
        returnMap.put("title", title);

        return new Result(true, "查询成功", returnMap);
    }
}

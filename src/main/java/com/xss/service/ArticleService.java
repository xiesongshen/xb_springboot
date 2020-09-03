package com.xss.service;

import com.xss.dao.ArticleDao;
import com.xss.entity.Article;
import com.xss.entity.PageResult;
import com.xss.entity.User;
import com.xss.mapper.ArticleMapper;
import com.xss.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@Service
@Transactional
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleDao articleDao;

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.Article>
     * @desc 模糊查询+分页
     */
    public Page<Article> findPage(String title, Integer page) {

        //实现倒序分页
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "publishDate"));//设置时间倒序
        Sort sort = Sort.by(orders);

        return articleDao.findByTitleLike(title, PageRequest.of(page - 1, PageResult.PAGE_SIZE, sort));
    }

    /*
     * @param
     * @return void
     * @desc  发布新文章
     */
    public void save(Article article) {
        article.setPublishDate(new Date());
        article.setPublishRealName(LoginUserUtil.getLoginUser().getRealName());
        article.setBrowseCount(0L);
        article.setUserId(LoginUserUtil.getLoginUser().getId());

        articleDao.save(article);
    }

    /*
     * @param
     * @return void
     * @desc 增加浏览数
     */
    public void updateBrowseCount(Article article) {
        articleDao.save(article);
    }

    /*
     * @param
     * @return com.xss.entity.Article
     * @desc 根据文章id获取文章
     */
    public Article findById(Long articleId) {

        return articleDao.findById(articleId).get();
    }

    /*
     * @param
     * @return com.xss.entity.User
     * @desc 查询我关注的人是否关注该文章
     */
    public List<User> selectCollectUser(Long aid, Long uid) {
        return articleMapper.selectCollectUser(aid, uid);
    }

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.Article>
     * @desc 查询收藏文章 模糊查询+分页
     */
    public Page<Article> findCollectPage(Long uid, String title, Integer page) {

        //实现倒序分页
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "publishDate"));//设置时间倒序
        Sort sort = Sort.by(orders);

        return articleDao.findCollectByTitleLike(uid,title, PageRequest.of(page - 1, PageResult.PAGE_SIZE, sort));
    }
}

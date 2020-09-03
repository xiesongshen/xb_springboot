package com.xss.dao;

import com.xss.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
public interface ArticleDao extends JpaRepository<Article,Long> {

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.Article>
     * @desc 模糊查询+分页
     */
    Page<Article> findByTitleLike(String title, Pageable of);

    /*
     * @param  
     * @return org.springframework.data.domain.Page<com.xss.entity.Article>
     * @desc  查询收藏文章 模糊查询+分页
     */
    @Query("SELECT " +
            " a  " +
            "FROM " +
            " Article a," +
            " User u," +
            " Favorite f  " +
            "WHERE " +
            " u.id = f.uId  " +
            " AND f.aId = a.id  " +
            " AND u.id = ?1  " +
            " AND a.title LIKE ?2")
    Page<Article> findCollectByTitleLike(Long uid, String title, Pageable of);
}

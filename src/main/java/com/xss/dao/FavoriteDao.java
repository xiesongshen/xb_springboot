package com.xss.dao;

import com.xss.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
public interface FavoriteDao extends JpaRepository<Favorite,Long> {

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询文章收藏数
     */
    Integer countFavoriteByAId(Long aId);

    /*
     * @param
     * @return com.xss.entity.Favorite
     * @desc  查询是否被收藏
     */
    @Query("select count(1) from Favorite f where f.aId=?1 and f.uId=?2")
    Integer findFavoriteByAIdAndUId(Long aid,Long uid);

    /*
     * @param
     * @return void
     * @desc 取消收藏
     */
    @Query(value = "delete from Favorite f where f.aId=?1 and f.uId=?2")
    @Modifying
    void delCollect(Long aid, Long uid);
}

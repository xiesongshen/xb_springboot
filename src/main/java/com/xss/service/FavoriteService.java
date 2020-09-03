package com.xss.service;

import com.xss.dao.FavoriteDao;
import com.xss.entity.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@Service
@Transactional
public class FavoriteService {

    @Autowired
    FavoriteDao favoriteDao;

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询文章被收藏数
     */
    public Integer countFavoriteByAId(Long aid) {
        return favoriteDao.countFavoriteByAId(aid);
    }

    /*
     * @param
     * @return java.lang.Boolean
     * @desc  判断文章是否被收藏
     */
    public Boolean checkCollect(Long aid,Long uid){

        return favoriteDao.findFavoriteByAIdAndUId(aid,uid)>0?true:false;
    }

    /*
     * @param
     * @return void
     * @desc  取消收藏
     */
    public void delCollect(Long aid,Long uid){
        favoriteDao.delCollect(aid,uid);
    }

    /*
     * @param
     * @return void
     * @desc 添加收藏
     */
    public void saveCollect(Long aid, Long uid) {
        Favorite favorite = new Favorite();
        favorite.setaId(aid);
        favorite.setuId(uid);
        favoriteDao.save(favorite);
    }
}

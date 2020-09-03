package com.xss.service;

import com.xss.dao.UserFocusDao;
import com.xss.entity.UserFocus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@Service
public class UserFocusService {

    @Autowired
    UserFocusDao userFocusDao;

    /*
     * @param
     * @return void
     * @desc  新添关注
     */
    @Transactional
    public void save(Long loginUserId,Long uid) {
        UserFocus userFocus = new UserFocus();
        userFocus.setUserId(loginUserId);
        userFocus.setUserFocusId(uid);
        userFocusDao.save(userFocus);
    }

    /*
     * @param
     * @return void
     * @desc  取消关注
     */
    @Transactional
    public void delFocus(Long userId, Long focusId) {
        userFocusDao.deleteFocus(userId, focusId);
    }

    /*
     * @param
     * @return java.lang.Boolean
     * @desc  判断是否关注
     */
    public Boolean isFocus(Long userId, Long focusId) {

        return userFocusDao.countByUserIdAndFocusId(userId, focusId) > 0 ? true : false;
    }

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询粉丝数
     */
    public Integer selectFocusCount(Long focusId){
        return userFocusDao.selectFocusCount(focusId);
    }

    /*
     * @param
     * @return java.lang.Integer
     * @desc 查询关注数
     */
    public Integer selectFCount(Long userId) {
        return userFocusDao.selectFCount(userId);
    }
}

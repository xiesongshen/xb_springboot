package com.xss.dao;

import com.xss.entity.UserFocus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
public interface UserFocusDao extends JpaRepository<UserFocus,Long> {

    /*
     * @param
     * @return void
     * @desc  取消关注
     */
    @Query(value = "delete from UserFocus uf where uf.userId=?1 and uf.userFocusId=?2")
    @Modifying
    void deleteFocus(Long userId, Long focusId);

    /*
     * @param
     * @return java.lang.Integer
     * @desc 判断是否关注
     */
    @Query("select count(1) from UserFocus uf where uf.userId=?1 and uf.userFocusId=?2")
    Integer countByUserIdAndFocusId(Long userId, Long focusId);

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询粉丝数
     */
    @Query("select count(1) from UserFocus uf where uf.userFocusId=?1")
    Integer selectFocusCount(Long focusId);

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询关注数
     */
    @Query("select count(1) from UserFocus uf where uf.userId=?1")
    Integer selectFCount(Long userId);
}

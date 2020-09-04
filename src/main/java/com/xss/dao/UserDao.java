package com.xss.dao;

import com.xss.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
public interface UserDao extends JpaRepository<User,Long> {

    /*
     * @param  
     * @return com.xss.entity.User
     * @desc 根据email查询用户
     */
    User findUserByEmail(String email);

    /*
     * @param
     * @return com.xss.entity.User
     * @desc 根据用户名查询用户
     */
    User findUserByUsername(String username);

    /*
     * @param
     * @return void
     * @desc 根据email重置用户密码
     */
    @Query("update User u set u.password=?2 where u.email=?1")
    @Modifying
    void updatePassword(String email, String password);

    /*
     * @param
     * @return void
     * @desc  更改用户登陆时间
     */
    @Query("update User u set u.loginTime=?2 where u.id=?1")
    @Modifying
    void updateLoginTime(Long id, Date date);

    /*
     * @param
     * @return com.xss.entity.User
     * @desc 根据userId查找用户
     */
    User findUserById(Long userId);

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.User>
     * @desc 模糊查询+分页
     */
    Page<User> findByUsernameLike(String username, Pageable of);

    /*
     * @param
     * @return java.util.List<java.lang.Integer>
     * @desc  根据用户id查询粉丝ids
     */
    @Query("select uf.userFocusId from UserFocus uf where uf.userId=?1")
    List<Integer> findFocusByUserId(Long userId);

    /*
     * @param
     * @return void
     * @desc 更新被看数
     */
    @Query("update User u set u.look=?2 where u.id=?1")
    @Modifying
    void updateLook(Long id, Long look);

    /*
     * @param
     * @return void
     * @desc  根据用户id修改图片路径
     */
    @Query("update User u set u.pic=?2 where u.id=?1")
    @Modifying
    void updatePicUrl(Long id, String path);


    /*
     * @param
     * @return org.springframework.data.domain.Page
     * @desc 查询用户关注列表
     */
    @Query("SELECT u FROM User u LEFT JOIN UserFocus uf on u.id = uf.userFocusId WHERE uf.userId = ?1")
    Page findFocusList(Long userId, Pageable of);


    /*
     * @param
     * @return java.util.List<com.xss.entity.User>
     * @desc 根据部门id查询用户
     */
    List<User> findByDeptId(Long deptId);

    /*
     * @param
     * @return com.xss.entity.User
     * @desc  判断用户是否用微信登陆过
     */
    User findByWxOpenid(String openid);
}

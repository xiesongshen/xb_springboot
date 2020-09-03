package com.xss.service;

import com.mysql.jdbc.StringUtils;
import com.xss.dao.UserDao;
import com.xss.entity.PageResult;
import com.xss.entity.User;
import com.xss.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author XSS
 * @date 2020/8/31
 * @desc
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDao userDao;

    /*
     * @param
     * @return com.xss.entity.User
     * @desc  检查email是否存在
     */
    public User checkEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    /*
     * @param
     * @return com.xss.entity.User
     * @desc  检查用户名是否存在
     */
    public User checkUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    /*
     * @param
     * @return java.lang.Object
     * @desc 注册新用户
     */
    public void save(User user) {
        // 设置默认属性
        user.setIsSecret("0");          // 默认不私密
        user.setLook(0L);               // 默认0的查看数
        user.setRegisterTime(new Date());               // 注册时间

        // 设置默认头像
        if (StringUtils.isNullOrEmpty(user.getPic())) {
            user.setPic("https://www.baidu.com/favicon.ico");
        }
        userDao.save(user);
    }

    /*
     * @param
     * @return void
     * @desc  根据email重置用户密码
     */
    @Transactional
    public void updatePassword(String email, String password) {
        userDao.updatePassword(email, password);
    }

    /*
     * @param
     * @return void
     * @desc 更改登陆时间
     */
    @Transactional
    public void updateLoginTime(Long id) {
        userDao.updateLoginTime(id, new Date());
    }

    /*
     * @param
     * @return com.xss.entity.User
     * @desc 根据userId获取用户
     */
    public User findById(long userId) {
        return userDao.findUserById(userId);
    }

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.User>
     * @desc 模糊查询+分页
     */
    public Page<User> findPage(String username, Integer page) {
        return userDao.findByUsernameLike(username, PageRequest.of(page - 1, PageResult.PAGE_SIZE));
    }


    /*
     * @param
     * @return java.util.List<java.lang.Integer>
     * @desc  查询指定id关注的人的id
     */
    public List<Integer> findFocus(Long userId) {
        return userDao.findFocusByUserId(userId);
    }

    /*
     * @param
     * @return void
     * @desc 添加被看数
     */
    @Transactional
    public void updateLook(User user) {
        userDao.updateLook(user.getId(),user.getLook());
    }

    /*
     * @param
     * @return void
     * @desc 更新用户
     */
    @Transactional
    public void update(User user) {
        userDao.save(user);
    }

    /*
     * @param
     * @return void
     * @desc  根据用户id修改头像图片地址
     */
    @Transactional
    public void updatePicUrl(Long id, String path) {
        userDao.updatePicUrl(id, path);
    }

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.User>
     * @desc  查询关注列表
     */
    public Page<User> findFocusList(Long uid, Integer page) {
        return userDao.findFocusList(uid,PageRequest.of(page - 1, PageResult.PAGE_SIZE));
    }

    /*
     * @param
     * @return java.util.List<com.xss.entity.User>
     * @desc  根据部门id查询用户详细情况
     */
    public List<User> findByDeptId(Long deptId) {
        return userDao.findByDeptId(deptId);
    }
}

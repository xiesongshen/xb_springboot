package com.xss.controller;

import com.xss.entity.Result;
import com.xss.entity.User;
import com.xss.service.DeptService;
import com.xss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DeptService deptService;

    @Autowired
    UserService userService;

    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  查询所有部门信息
     */
    @RequestMapping("/deptList")
    public Result deptList(){
        return new Result(true,"成功",deptService.selectDept());
    }


    /*
     * @param
     * @return com.xss.entity.Result
     * @desc  查询部门信息和人数
     */
    @GetMapping("/findDeptAll")
    public Result findDeptAll() {
        List<Map> deptData = deptService.findDeptAll();

        for (Map dept : deptData) {
            Long deptId = Long.parseLong(dept.get("id").toString());

            List<User> userList=userService.findByDeptId(deptId);
            dept.put("userList",userList);
        }
        return new Result(true, "查询成功", deptData);
    }
}

package com.xss.service;

import com.xss.dao.DeptDao;
import com.xss.entity.Dept;
import com.xss.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@Service
public class DeptService {

    @Autowired
    DeptMapper deptMapper;

    @Autowired
    DeptDao deptDao;

    /*
     * @param
     * @return java.util.List<com.xss.entity.Dept>
     * @desc  查询所有部门
     */
    public List<Dept> selectDept(){
        return deptDao.findAll();
    }

    /*
     * @param
     * @return com.xss.entity.Dept
     * @desc 根据部门id查询部门
     */
    public Dept findByDeptId(Long deptId){
        return deptDao.findById(deptId).get();
    }

    /*
     * @param
     * @return java.util.List<java.util.Map>
     * @desc  查询每个部门的信息  部门信息+部门人数
     */
    public List<Map> findDeptAll() {
        return deptMapper.findDeptAll();
    }
}

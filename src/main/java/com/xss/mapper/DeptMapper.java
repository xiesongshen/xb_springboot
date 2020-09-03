package com.xss.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface DeptMapper {

    /*
     * @param
     * @return java.util.List<java.util.Map>
     * @desc  查询所有部门人数
     */
    List<Map> findDeptAll();
}

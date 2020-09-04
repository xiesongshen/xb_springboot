package com.xss.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/4
 * @desc
 */
@Mapper
public interface HomeMapper {

    /*
     * @param
     * @return java.util.Map
     * @desc  查询当日新增数据
     */
    Map findHomeCount();


    /*
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @desc  查询7日新增数据
     */
    List<Map<String, Object>> findHomeDetail();
}

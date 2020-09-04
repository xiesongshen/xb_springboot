package com.xss.service;

import com.xss.mapper.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author XSS
 * @date 2020/9/4
 * @desc
 */
@Service
@Transactional
public class HomeService {

    @Autowired
    private HomeMapper homeMapper;

    /*
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @desc  查询当日新增信息
     */
    public Map<String, Object> findHomeCount() {
        return homeMapper.findHomeCount();
    }

    /*
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @desc  查询七日信息
     */
    public List<Map<String, Object>> findHomeDetail() {
        return homeMapper.findHomeDetail();
    }

}

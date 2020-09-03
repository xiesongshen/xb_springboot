package com.xss.dao;

import com.xss.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
public interface DeptDao extends JpaRepository<Dept,Long> {
}

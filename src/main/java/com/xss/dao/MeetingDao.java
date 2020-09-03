package com.xss.dao;

import com.xss.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author XSS
 * @date 2020/9/2
 * @desc
 */
public interface MeetingDao extends JpaRepository<Meeting,Long> {

    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.Meeting>
     * @desc 模糊查询+分页
     */
    Page<Meeting> findByTitleLikeAndStatus(String title, Long status, Pageable of);
    Page<Meeting> findByTitleLike(String title, Pageable of);

    /*
     * @param
     * @return java.util.List<com.xss.entity.Meeting>
     * @desc  查询会议状态不为2的会议信息
     */
    List<Meeting> findByStatusNot(Long status);

}

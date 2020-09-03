package com.xss.dao;

import com.xss.entity.MeetingJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author XSS
 * @date 2020/9/2
 * @desc
 */
public interface MeetingJoinDao extends JpaRepository<MeetingJoin,Long> {

    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询已参加会议的人数
     */
    @Query("select count(1) from MeetingJoin  where mId= ?1")
    Integer findCountByMid(Long mid);

    /*
     * @param
     * @return java.util.List<java.lang.Long>
     * @desc   查询已参加会议的人员id
     */
    @Query("select uId from MeetingJoin WHERE mId = ?1")
    List<Long> findUidByMid(Long mid);

    /*
     * @param
     * @return java.lang.Integer
     * @desc 查询是否参加会议
     */
    @Query("select count(1) from MeetingJoin  where mId=?1 and uId=?2")
    Integer countByMidAndUid(Long mid, Long uid);


    /*
     * @param
     * @return void
     * @desc 退出会议
     */
    @Query(value = "delete from MeetingJoin where mId=?1 and uId=?2")
    @Modifying
    void deleteMeetingJoin(Long mid, Long uid);
}

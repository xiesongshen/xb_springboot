package com.xss.service;

import com.xss.dao.MeetingJoinDao;
import com.xss.entity.MeetingJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author XSS
 * @date 2020/9/2
 * @desc
 */
@Service
@Transactional
public class MeetingJoinService {

    @Autowired
    MeetingJoinDao meetingJoinDao;


    /*
     * @param
     * @return java.lang.Integer
     * @desc  查询已参加会议的人数
     */
    public Integer realJoin(Long mid) {

        return meetingJoinDao.findCountByMid(mid);
    }

    /*
     * @param
     * @return java.util.List<java.lang.Long>
     * @desc  查询已参加会议的人员id
     */
    public List<Long> realJoinUser(Long mid) {
        return meetingJoinDao.findUidByMid(mid);
    }

    /*
     * @param
     * @return java.lang.Boolean
     * @desc 判断是否已参加会议
     */
    public Boolean checkJoin(Long mid, Long uid) {

        return meetingJoinDao.countByMidAndUid(mid, uid) > 0 ? true : false;
    }

    /*
     * @param
     * @return void
     * @desc 参加会议
     */
    public void save(Long mid, Long uid) {
        MeetingJoin meetingJoin = new MeetingJoin();
        meetingJoin.setmId(mid);
        meetingJoin.setuId(uid);
        meetingJoinDao.save(meetingJoin);
    }

    /*
     * @param
     * @return
     * @desc  退出会议
     */
    public void deleteMeetingJoin(Long mid, Long uid){
        meetingJoinDao.deleteMeetingJoin(mid,uid);
    }
}

package com.xss.service;

import com.xss.dao.MeetingDao;
import com.xss.entity.Meeting;
import com.xss.entity.PageResult;
import com.xss.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XSS
 * @date 2020/9/2
 * @desc
 */
@Service
@Transactional
public class MeetingService {

    @Autowired
    MeetingDao meetingDao;

    @Autowired
    MeetingMapper meetingMapper;

    @Autowired
    DeptService deptService;


    /*
     * @param
     * @return org.springframework.data.domain.Page<com.xss.entity.Meeting>
     * @desc  模糊查询+分页
     */
    public Page<Meeting> findPage(String title, Long status, Integer page) {

        //实现倒序分页
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "publishDate"));//设置时间倒序
        Sort sort = Sort.by(orders);


        if (status == null){
            return meetingDao.findByTitleLike(title,PageRequest.of(page - 1, PageResult.PAGE_SIZE, sort));
        }

        return meetingDao.findByTitleLikeAndStatus(title,status,PageRequest.of(page - 1, PageResult.PAGE_SIZE, sort));
    }

    /*
     * @param
     * @return com.xss.entity.Meeting
     * @desc  根据会议id查找会议信息
     */
    public Meeting findById(Long mid) {
        return meetingDao.findById(mid).get();
    }

    /*
     * @param
     * @return void
     * @desc  发布会议
     */
    public void save(Meeting meeting) {

        meeting.setDeptName(deptService.findByDeptId(meeting.getDeptId()).getName());
        meeting.setPublishDate(new Date());
        meeting.setStatus(0L);
        meetingDao.save(meeting);
    }

    /*
     * @param
     * @return java.util.List<com.xss.entity.Meeting>
     * @desc 查询会议状态不为2的会议
     */
    public List<Meeting> findByStatusNot(Long sataus) {
        return meetingDao.findByStatusNot(sataus);
    }

    /*
     * @param
     * @return void
     * @desc 更新会议状态
     */
    public void updateStatus(Meeting meeting, long status) {
        meeting.setStatus(status);

        meetingDao.save(meeting);
    }
}

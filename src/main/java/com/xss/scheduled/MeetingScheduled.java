package com.xss.scheduled;

import com.xss.entity.Meeting;
import com.xss.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author XSS
 * @date 2020/9/3
 * @desc
 */
@Component
public class MeetingScheduled {

    @Autowired
    MeetingService meetingService;

    @Scheduled(cron = "* * * * * ?")
    public void meetingScheduled(){
        //查询status不为2的会议
        List<Meeting> meetingList =  meetingService.findByStatusNot(2L);

        for (Meeting meeting : meetingList) {
            if (meeting.getStatus() == 0){ //未开始,判断时间开启
                if(meeting.getStartTime().getTime()<=new Date().getTime()){
                    //更新状态为1

                    meetingService.updateStatus(meeting,1L);
                }
            }

            if (meeting.getStatus() == 1){ //已开始，判断时间结束
                if (meeting.getEndTime().getTime()<=new Date().getTime()){
                    //更新状态为2

                    meetingService.updateStatus(meeting,2L);
                }
            }
        }
    }
}

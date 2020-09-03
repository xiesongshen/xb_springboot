import com.xss.Application;
import com.xss.dao.FavoriteDao;
import com.xss.dao.MeetingJoinDao;
import com.xss.dao.UserDao;
import com.xss.dao.UserFocusDao;
import com.xss.entity.*;
import com.xss.mapper.ArticleMapper;
import com.xss.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author XSS
 * @date 2020/9/1
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestDemo {

    @Autowired
    MeetingService service;

    @Autowired
    MeetingJoinDao dao;

    @Autowired
    ArticleMapper articleMapper;

    @Test
    public void test1(){
        List<Meeting> byNoStatus2 = service.findByStatusNot(2L);
        for (Meeting meeting : byNoStatus2) {

            System.out.println(meeting);
        }
    }

    @Test
    public void test2(){
        List<Long> uidByMid = dao.findUidByMid(2L);
        for (Long aLong : uidByMid) {
            System.out.println(aLong);
        }
    }
}

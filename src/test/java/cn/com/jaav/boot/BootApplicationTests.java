package cn.com.jaav.boot;

import cn.com.jaav.boot.schedule.ScheduledTask;
import cn.com.jaav.boot.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BootApplicationTests
{
    @Autowired
    private ApiService service;

    @Autowired
    private ScheduledTask task;

    @Test
    public void test(){
        service.getNews();
    }

    @Test
    public void sendMail(){
        task.sendMail();
    }

    @Test
    public void getNewsPicture(){
        System.out.println(service.newsPicture());
    }
}
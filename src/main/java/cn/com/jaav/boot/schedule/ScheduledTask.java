package cn.com.jaav.boot.schedule;

import cn.com.jaav.boot.entity.Receiver;
import cn.com.jaav.boot.entity.Weather;
import cn.com.jaav.boot.service.ApiService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
@Service
@Data
@ConfigurationProperties()
public class ScheduledTask
{
    private List<Receiver> list;

    @Value("${spring.mail.username}")
    private String mailSender;

    @Autowired
    private ApiService apiService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RestTemplate restTemplate;

    public void sendTemplateEmail(String to, String subject, Map<String,Object> templateValue) {
        System.out.println("发送html邮件：to：" + to + " " + new Date());
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(new InternetAddress(mailSender, "小助手", "UTF-8"));
            mimeMessageHelper.setSubject(subject);
            Context context = new Context();
            context.setVariables(templateValue);
            String text = templateEngine.process("template",context);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("发送html邮件异常：to：" + to + " " + new Date());
            e.printStackTrace();
        }
    }

    /**
     * 定时任务
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void sendMail()
    {
        for (int i = 0; i < list.size(); i++) {
            String weather = apiService.getWeather(list.get(i).getCity());
            Map<String,Object> map = new HashMap<>();
            //倒计时
            map.put("ddl", computeDate());
            //毒鸡汤
            //map.put("sentence", apiService.getSentence());
            //60S新闻
            map.put("banner", apiService.newsPicture());
            //城市
            map.put("city", JSONPath.read(weather, "$.forecasts[0].city"));
            //今天日期
            map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            //今日壁纸
            //map.put("banner", apiService.getWallpaper());
            List<Weather> weatherList = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) JSONPath.read(weather, "$.forecasts[0].casts");
            weatherList.add(new Weather(
                    "今天",
                    jsonArray.getJSONObject(0).get("dayweather").toString(),
                    jsonArray.getJSONObject(0).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(0).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(0).get("daywind").toString() + "风"));
            weatherList.add(new Weather(
                    "明天",
                    jsonArray.getJSONObject(1).get("dayweather").toString(),
                    jsonArray.getJSONObject(1).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(1).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(1).get("daywind").toString() + "风"));
            weatherList.add(new Weather(
                    "后天",
                    jsonArray.getJSONObject(2).get("dayweather").toString(),
                    jsonArray.getJSONObject(2).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(2).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(2).get("daywind").toString() + "风"));
            //天气详情
            map.put("weatherList", weatherList);
            sendTemplateEmail(list.get(i).getReceiver(),"邮箱小助手", map);
        }
    }

    public String computeDate()
    {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date future = null;
        try {
            future = sdf.parse("2021-12-25 09:0:0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf((future.getTime() - (today.getTime()))/24/60/60/1000);
    }
}
package cn.com.jaav.boot.schedule;

import cn.com.jaav.boot.entity.Receiver;
import cn.com.jaav.boot.entity.Weather;
import cn.com.jaav.boot.service.WeatherService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    private String from;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RestTemplate restTemplate;

    public void sendTemplateEmail(String to, String subject, Map<String,Object> templateValue) {
        System.out.println("发送html邮件：to：" + to + new Date());
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(new InternetAddress(from, "布莱恩特科比酱", "UTF-8"));
            mimeMessageHelper.setSubject(subject);
            Context context = new Context();
            context.setVariables(templateValue);
            String text = templateEngine.process("template",context);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("发送html邮件异常：to：" + to + new Date());
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void sendTemplateMail()
    {
        for (int i = 0; i < list.size(); i++) {
            String weather = weatherService.getWeather(list.get(i).getCity());
            Map<String,Object> map = new HashMap<>();
            //倒计时
            map.put("ddl", computeDate());
            //毒鸡汤
            map.put("sentence", weatherService.getSentence());
            //城市
            map.put("city", JSONPath.read(weather, "$.forecasts[0].city"));
            //今天日期
            map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            //每日一图
            long banner = Math.round(Math.random()*24);
            map.put("banner", "https://matery.oss-cn-hangzhou.aliyuncs.com/pic/banner/" + banner + ".jpg");
            List<Weather> weatherList = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) JSONPath.read(weather, "$.forecasts[0].casts");
            weatherList.add(new Weather(
                    "今天",
                    "https://matery.oss-cn-hangzhou.aliyuncs.com/pic/mail/2.png",
                    jsonArray.getJSONObject(0).get("dayweather").toString(),
                    jsonArray.getJSONObject(0).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(0).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(0).get("daywind").toString() + "风"));
            weatherList.add(new Weather(
                    "明天",
                    "https://matery.oss-cn-hangzhou.aliyuncs.com/pic/mail/2.png",
                    jsonArray.getJSONObject(1).get("dayweather").toString(),
                    jsonArray.getJSONObject(1).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(1).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(1).get("daywind").toString() + "风"));
            weatherList.add(new Weather(
                    "后天",
                    "https://matery.oss-cn-hangzhou.aliyuncs.com/pic/mail/2.png",
                    jsonArray.getJSONObject(2).get("dayweather").toString(),
                    jsonArray.getJSONObject(2).get("nighttemp").toString() + "° / " + jsonArray.getJSONObject(2).get("daytemp").toString() + "°",
                    jsonArray.getJSONObject(2).get("daywind").toString() + "风"));
            //天气详情
            map.put("weatherList", weatherList);
            sendTemplateEmail(list.get(i).getReceiver(),"考研倒计时小助手", map);
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
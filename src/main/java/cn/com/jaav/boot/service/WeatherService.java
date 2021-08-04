package cn.com.jaav.boot.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class WeatherService
{
    @Value("${amap.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    public String getWeather(String city){
        return restTemplate.getForObject("http://restapi.amap.com/v3/weather/weatherInfo?extensions=all&key=" + key + "&city=" + city, String.class);
    }

    public String getSentence() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String res = restTemplate.exchange("https://soul-soup.fe.workers.dev/", HttpMethod.GET, entity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(res);
        return json.getString("title");
    }
}
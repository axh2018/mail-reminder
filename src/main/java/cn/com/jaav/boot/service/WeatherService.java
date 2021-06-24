package cn.com.jaav.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
}
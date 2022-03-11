package cn.com.jaav.boot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ApiService
{
    @Value("${amap.key}")
    private String amapKey;

    @Value("${alapi.key}")
    private String alapiKey;

    @Autowired
    private RestTemplate restTemplate;

    /**
    天气获取
     */
    public String getWeather(String city){
        return restTemplate.getForObject("http://restapi.amap.com/v3/weather/weatherInfo?extensions=all&key=" + amapKey + "&city=" + city, String.class);
    }

    /**
     * 60秒读懂世界图片
     */
    public String newsPicture(){
        JSONObject jsonObject = JSONObject.parseObject(restTemplate.getForObject("https://api.iyk0.com/60s", String.class));
        return jsonObject.getObject("imageUrl", String.class);
    }
    /**
    每日新闻获取
     */
    public ArrayList getNews(){
        String result =  restTemplate.getForObject("https://v2.alapi.cn/api/zaobao?token=" + alapiKey + "&format=json", String.class);
        JSONArray news = (JSONArray) JSONPath.read(result, "$.data.news");
        ArrayList<String> newsList = new ArrayList<>();
        newsList = (ArrayList<String>) news.toJavaList(String.class);
        for (int i = 0; i < newsList.size(); i++) {
            //除去前面的数字和顿号和末尾分号
            if (i < 9){
                newsList.set(i, newsList.get(i).substring(2, newsList.get(i).length() - 1));
            }else {
                newsList.set(i, newsList.get(i).substring(3, newsList.get(i).length() - 1));
            }
            //最前面加上点号
            StringBuilder builder = new StringBuilder(newsList.get(i));
            builder.insert(0, "·");
            newsList.set(i, builder.toString());
        }
        return newsList;
    }

    /**
     * 每日壁纸获取
     */
    public String getWallpaper(){
        String  wallpaperUrl = restTemplate.getForObject("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1", String.class);
        String url = (String) JSONPath.read(wallpaperUrl, "$.images[0].url");
        return "https://cn.bing.com" + url;
    }

    /**
    毒鸡汤获取
     */
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
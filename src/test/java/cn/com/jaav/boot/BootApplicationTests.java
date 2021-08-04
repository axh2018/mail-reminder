package cn.com.jaav.boot;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Action;
import java.util.Collections;

@SpringBootTest
class BootApplicationTests
{
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getSentence() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String res = restTemplate.exchange("https://soul-soup.fe.workers.dev/", HttpMethod.GET, entity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(res);
        System.out.println(json.getString("title"));
    }
}
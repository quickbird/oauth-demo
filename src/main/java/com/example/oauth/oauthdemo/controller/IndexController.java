package com.example.oauth.oauthdemo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/redirect")
    public String redirect(String code, ModelMap modelMap){
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> list = new ArrayList();
        list.add(MediaType.APPLICATION_JSON);
        headers.setAccept(list);
        JSONObject param = new JSONObject();
        param.put("client_id", "xxx");
        param.put("client_secret", "xxx");
        param.put("code", code);
        HttpEntity httpEntity = new HttpEntity(param, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity("https://github.com/login/oauth/access_token?client_id=xxx&" +
                "client_secret=xxx&code=" + code, httpEntity, JSONObject.class);
        headers.setBearerAuth(responseEntity.getBody().getString("access_token"));
        httpEntity = new HttpEntity(null, headers);
        ResponseEntity<JSONObject> resEntity = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, JSONObject.class);
        modelMap.put("userName", resEntity.getBody().getString("login"));
        return "welcome";
    }
}

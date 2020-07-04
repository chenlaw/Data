package com.example.connection.utils;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {
    public static void postRequest(String url, MultiValueMap<String,Object> param){
        String url0 = url;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String ss = restTemplate.postForObject(url0, param,String.class);
        System.out.println(ss);
    }

}

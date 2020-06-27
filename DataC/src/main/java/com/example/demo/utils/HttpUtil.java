package com.example.demo.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class HttpUtil {
    public static boolean sendFile(String url,String filePath) {
        String url0 = url;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        File file = new File(filePath);
        // 文件必须封装成FileSystemResource这个类型后端才能收到附件
        FileSystemResource resource = new FileSystemResource(file);
        // 然后所有参数要封装到MultiValueMap里面
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //将文件封装进去
        param.add("file", resource);
        String ss = restTemplate.postForObject(url0, param, String.class);
        System.out.println(ss);
        if(ss!=null)
            return true;
        return false;
    }
    }


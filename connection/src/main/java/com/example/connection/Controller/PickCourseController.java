package com.example.connection.Controller;

import com.example.connection.utils.FileUtil;
import com.example.connection.utils.HttpUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/course")
public class PickCourseController {
    String urlA="http://127.0.0.1:8081";
    String urlB="http://127.0.0.1:8082";
    String urlC="http://127.0.0.1:8083";
    @PostMapping("/pick")
    public void pickCourse(@RequestParam("Cno") String cno,@RequestParam("Sno")String sno, @RequestParam("to") String to, @RequestParam("student")MultipartFile file){
            File file1= FileUtil.MultipartFileToFile(file);
        FileSystemResource resource = new FileSystemResource(file1);
        MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
        String t=to;
        param.add("Sno",sno);
        param.add("Cno",cno);
        param.add("Student",resource);
        String url=getUrl(to)+"/user/syspick";
        HttpUtil.postRequest(url,param);
    }
    public String getUrl(String t){
        switch (t){
            case "A":return urlA;
            case "B":return urlB;
            case "C":return urlC;
        }
        return "";
    }
}

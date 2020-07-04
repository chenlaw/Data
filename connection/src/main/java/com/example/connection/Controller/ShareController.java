package com.example.connection.Controller;

import com.example.connection.utils.FileUtil;
import com.example.connection.utils.HttpUtil;
import org.dom4j.DocumentException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static com.example.connection.utils.XMLUtil.checkXML;
import static com.example.connection.utils.XMLUtil.transformerXML;
@RestController
@RequestMapping("/course")
public class ShareController {
    String urlA="http://127.0.0.1:8081";
    String urlB="http://127.0.0.1:8082";
    String urlC="http://127.0.0.1:8083";
    @PostMapping("/share")
    public void share(@RequestParam("type") String type){
        MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
        param.add("to",type);
        String url0;
      //  if(!type.equals("A"))
           // HttpUtil.postRequest(urlA+"/course/share",param);
        if(!type.equals("B"))
        HttpUtil.postRequest(urlB+"/course/share",param);
        if(!type.equals("C"))
            HttpUtil.postRequest(urlC+"/course/share",param);
    }
    @PostMapping("/receiveShare")
    public void reiceiveAndPost(@RequestParam("from") String from,@RequestParam("to") String to, @RequestParam("class.xml")MultipartFile multipartFile) throws DocumentException, TransformerException, IOException {
        File file= FileUtil.MultipartFileToFile(multipartFile);
        String checkxsd="src/main/resources/static/xsds/class"+from+".xsd";
        String checkresult=checkXML(checkxsd,file);
        System.out.println(checkresult);
        Random random=new Random();
        Integer integer=random.nextInt(123423534);
       if(checkresult=="success"){
            String transforxsl1="src/main/resources/static/xsls/formatClass.xsl";
            String transforxsl2="src/main/resources/static/xsls/classTo"+to+".xsl";
            String templefile1="src/main/resources/static/TemplateFiles/temple"+integer.toString()+".xml";
            integer=integer+1;
            String templefile2="src/main/resources/static/TemplateFiles/temple"+integer.toString()+".xml";
            transformerXML(templefile1,file,transforxsl1);
            transformerXML(templefile2,templefile1,transforxsl2);
            String urlk=getUrl(to)+"/course/shared";
            MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
            File file1=new File(templefile2);
            FileSystemResource resource = new FileSystemResource(file1);
            param.add("file",resource);
            param.add("from",from);
            HttpUtil.postRequest(urlk,param);
        }
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

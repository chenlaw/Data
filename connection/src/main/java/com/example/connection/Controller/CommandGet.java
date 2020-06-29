package com.example.connection.Controller;
//src/main/resources/static/TemplateFiles/formatstudentA.xml
//        src/main/resources/static/xsls/formatStudent.xsl
//        src/main/resources/static/TemplateFiles/studentA.xml
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.*;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.*;
import org.dom4j.util.XMLErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@RestController
@RequestMapping("/course")
public class CommandGet {
    //介绍：服务端调用这个方法是未来发来一个学生xml 然后集成服务器转发到目标系统，这个方法不涉及选课结果，选课结果需要被选课
    //系统主动调用集成服务器的sendchooseResult方法
    //调用的url格式是：String url="http://127.0.0.1:8081/course/kik?fileName=123.xml&type1=A&type2=B";
    @RequestMapping(value="/getStudent")
    public static String chooseClass(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url1="http://127.0.0.1:8081";
        String url2="http://127.0.0.1:8081";
        String url3="http://127.0.0.1:8081";
        String urlFrom="";
        String urlTo="";
        String typeFrom=request.getParameter("type1");//源系统的类型
        String typeTo=request.getParameter("type2");//目标系统的类型
        if(typeFrom=="A"){
            if(typeTo=="B"){
                urlFrom=url1;
                urlTo=url2;
            }
            else{
                urlFrom=url1;
                urlTo=url3;
            }
        }
        else if(typeFrom=="B"){
            urlFrom=url2;
            if(typeTo=="A"){
                urlTo=url1;
            }
            else{
                urlTo=url3;
            }
        }
        else{
            urlFrom=url3;
            if(typeTo=="A"){
                urlTo=url1;
            }
            else{
                urlTo=url2;
            }
        }
        String fileFullPath = "src/main/resources/static/TemplateFiles/"+request.getParameter("fileName");
        InputStream input = null;
        FileOutputStream fos = null;
        try {
            input = request.getInputStream();
            File file = new File("src/main/resources/static/Dxml");
            if(!file.exists()){
                file.mkdirs();
            }
            fos = new FileOutputStream(fileFullPath);
            int size = 0;
            byte[] buffer = new byte[1024];
            while ((size = input.read(buffer,0,1024)) != -1) {
                fos.write(buffer, 0, size);
            }
        } catch (IOException e) {
            //响应信息 json字符串格式
            Map<String,Object> responseMap = new HashMap<String,Object>();
            responseMap.put("flag", false);
            responseMap.put("errorMsg", e.getMessage());
            String jsonResponse = JSONObject.toJSONString(responseMap);
            sendResponse(jsonResponse,response);
        } finally{
            if(input != null){
                input.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        String checkxsd="src/main/resources/static/xsds/student"+typeFrom+".xsd";
        String checkresult=checkXML(checkxsd,fileFullPath);
        System.out.println("check: "+checkresult);
        String fileFormat="src/main/resources/static/TemplateFiles/format"+request.getParameter("fileName");
        String formatxsl="src/main/resources/static/xsls/formatStudent.xsl";
//        System.out.println(fileFormat);
//        System.out.println(formatxsl);
//        System.out.println(fileFullPath);
        transformerXML(fileFormat,fileFullPath,formatxsl);
        String fileType2="src/main/resources/static/TemplateFiles/type2"+request.getParameter("fileName");
        String xslToType2="src/main/resources/static/xsls/studentTo"+typeTo+".xsl";
        transformerXML(fileType2,fileFormat,xslToType2);
        String chooseurl=url2+"/course/receiveAstudentAndReponse";
        postFileto2(chooseurl,fileType2);
        return null;
    }
    //这个方法是被选课的系统收到集成服务器发去的学生列表后生成选课结果返回给源系统选课结果时用的，选课
    //结果直接放在url的result里面，然后源系统知道结果后反馈到前端。
    @RequestMapping(value="/sendchooseResult")
    public  static  String chooseResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url1="http://127.0.0.1:8081";
        String url2="http://127.0.0.1:8082";
        String url3="http://127.0.0.1:8083";
        String chooseresult=request.getParameter("result");
        String system=request.getParameter("type");
        String url="";
        if(system=="A"){
            url=url1;
        }
        else if(system=="B"){
            url=url2;
        }
        else{
            url=url3;
        }
        url=url+"/returnchooseresult"+"?result="+chooseresult;
        //这边是在调用服务器端接口，再url返回选课结果，让服务器端知道学生选课结果
        sendChooseResult(url);
        return null;
    }
    //这是服务器端请求共享的方法，服务器端在url里放上 ？type=A 表明自己系统类别
    @RequestMapping(value="share")
    public static String share(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlA="http://127.0.0.1:8081";
        String urlB="http://127.0.0.1:8082";
        String urlC="http://127.0.0.1:8083";
        String type1=request.getParameter("type");
        String type2="";
        String type3="";
        String url2="";
        String url3="";
        if(type1=="A"){
            type2="B";
            type3="C";
            url2=urlB;
            url3=urlC;
        }
        else if(type1=="B"){
            type2="A";
            type3="C";
            url2=urlA;
            url3=urlC;
        }
        else{
            type2="A";
            type3="B";
            url2=urlA;
            url3=urlB;
        }
        //设置type1参数是为了告诉被请求分享的系统往哪个系统返回
        url2=url2+"/course/shareClass"+"?type1="+type1;
        url3=url3+"/course/shareClass"+"?type1="+type1;
        requestforshare(url2);
        requestforshare(url3);
        return null;
    }
    public static void requestforshare(String urlS) throws IOException {
        URL url = new URL(urlS);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.connect();
    }
    //这个方法是B系统调用的，B系统post一个自己课程分享文件到这个方法里面，并且在url的fileName参数里
    //注明自己的文件名,在type1参数里注明自己系统的类型，type2参数注明接受服务器的类型 集成服务器接收到这个xml后进行验证转化，调用A系统的方法接受转化后的xml
    @RequestMapping(value="receiveShare")
    public static void receiveShareFileandSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String urlA="http://127.0.0.1:8081";
        String urlB="http://127.0.0.1:8082";
        String urlC="http://127.0.0.1:8083";
        String urlk="";
        String typeFrom=request.getParameter("type1");
        String typeTo=request.getParameter("type2");
        if(typeTo=="A"){
            urlk=urlA;
        }
        else if(typeTo=="B"){
            urlk=urlB;
        }
        else{
            urlk=urlC;
        }
        String fileFullPath = "src/main/resources/static/TemplateFiles/"+request.getParameter("fileName");
        InputStream input = null;
        FileOutputStream fos = null;
        try {
            input = request.getInputStream();
            File file = new File("src/main/resources/static/Dxml");
            if(!file.exists()){
                file.mkdirs();
            }
            fos = new FileOutputStream(fileFullPath);
            int size = 0;
            byte[] buffer = new byte[1024];
            while ((size = input.read(buffer,0,1024)) != -1) {
                fos.write(buffer, 0, size);
            }
        } catch (IOException e) {
            //响应信息 json字符串格式
            Map<String,Object> responseMap = new HashMap<String,Object>();
            responseMap.put("flag", false);
            responseMap.put("errorMsg", e.getMessage());
            String jsonResponse = JSONObject.toJSONString(responseMap);
            sendResponse(jsonResponse,response);
        } finally{
            if(input != null){
                input.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        String checkxsd="src/main/resources/static/xsds/class"+typeFrom+".xsd";
        String checkresult=checkXML(checkxsd,fileFullPath);
        System.out.println(checkresult);
        Random random=new Random();
        Integer integer=random.nextInt(123423534);
        if(checkresult=="success"){
            String transforxsl1="src/main/resources/static/xsls/formatClass.xsl";
            String transforxsl2="src/main/resources/static/xsls/classTo"+typeTo+".xsl";
            String templefile1="src/main/resources/static/TemplateFiles/temple"+integer.toString()+".xml";
            integer=integer+1;
            String templefile2="src/main/resources/static/TemplateFiles/temple"+integer.toString()+".xml";
            transformerXML(templefile1,fileFullPath,transforxsl1);
            transformerXML(templefile2,templefile1,transforxsl2);
            urlk=urlk+"/course/receiveshareFile";
            postFileto2(urlk,templefile2);
            System.out.println("asdf982735asd");
        }
    }
    public static void sendChooseResult(String urlS) throws IOException {
        URL url = new URL(urlS);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();

    }
    public static void postFileto2(String urlS,String filename ) throws IOException {
        DataInputStream in = null;
        OutputStream out = null;
        HttpURLConnection conn = null;
        JSONObject resposeTxt = null;
        InputStream ins = null;
        ByteArrayOutputStream outStream = null;
        try {
            //"http://localhost:8081/mes-boot-doc/test/fileupload?fileName=shafei.xls"
            URL url = new URL(urlS);

            conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.connect();
            conn.setConnectTimeout(10000);
            out = conn.getOutputStream();
            conn.getResponseMessage();
            File file = new File(filename);
            in = new DataInputStream(new FileInputStream(file));

            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }
            out.flush();

            // 返回流
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ins = conn.getInputStream();
                outStream = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int count = -1;
                while ((count = ins.read(data, 0, 1024)) != -1) {
                    outStream.write(data, 0, count);
                }
                data = null;
                resposeTxt = JSONObject.parseObject(new String(outStream
                        .toByteArray(), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (ins != null) {
                ins.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    private static void sendResponse(String responseString, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(responseString);
            pw.flush();
        } finally {
            IOUtils.closeQuietly(pw);
        }
    }
    public static String checkXML(String xsdPath0, String xmlPath0) {
        String xsdPath = xsdPath0;
        String xmlPath = xmlPath0;
        try {
            //错误消息处理类
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            //获得解析器工厂类
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //在解析XML是进行验证
            factory.setValidating(true);
            //支持命名空间
            factory.setNamespaceAware(true);
            //获得解析器
            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + xsdPath);
            SAXReader reader = new SAXReader();
            //读取XML文件
            Document document = reader.read(new File(xmlPath));


            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            // 发生错误时得到相关信息
            validator.setErrorHandler(errorHandler);
            // 进行校验
            validator.validate(document);
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            //通过是否有错误信息判断校验是否匹配
            if (errorHandler.getErrors().hasContent()) {
                System.out.println("XML文件通过XSD文件校验失败！");
                writer.write(errorHandler.getErrors());
                return "has error";
            } else {
                System.out.println("XML文件通过XSD文件校验成功！");
                return "success";
            }
        } catch (Exception e) {
            System.out.println("asdf");
        }
        return "has error";
    }
    public static void transformerXML(String resultPath,String sourcePath,String xslPath) throws IOException, DocumentException, TransformerException {
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(new FileReader(new File(sourcePath)));
        TransformerFactory factory=TransformerFactory.newInstance();
        Transformer transformer=factory.newTransformer(new StreamSource(xslPath));
        DocumentSource source=new DocumentSource(document);
        DocumentResult result=new DocumentResult();
        transformer.transform(source,result);
        Document transformedDoc=result.getDocument();
        Writer w=new FileWriter(resultPath);
        OutputFormat opf=OutputFormat.createPrettyPrint();
        opf.setEncoding("GB2312");
        XMLWriter xw=new XMLWriter(w,opf);
        xw.write(transformedDoc);
        xw.close();
        w.close();
    }
}

package com.example.connection.Controller;
//src/main/resources/static/TemplateFiles/formatstudentA.xml
//        src/main/resources/static/xsls/formatStudent.xsl
//        src/main/resources/static/TemplateFiles/studentA.xml
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
    //介绍：服务端调用这个方法是未来发来一个学生xml 然后集成服务器转发到目标系统，并且返回插入学生选课结果
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

//            //响应信息 json字符串格式
//            Map<String,Object> responseMap = new HashMap<String,Object>();
//            responseMap.put("flag", true);
//
//            //生成响应的json字符串
//            String jsonResponse = JSONObject.toJSONString(responseMap);
//            sendResponse(jsonResponse,response);
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
        postFileto2(chooseurl,fileType2,response);
        String result="";
        System.out.println("asdfasdfgasdjdghakjsdg");
        System.out.println("asdfasd:"+response.getHeader("result"));
        return result;
    }
    @RequestMapping(value = "/sharep")
    public static void postFileto2(String urlS,String filename,HttpServletResponse response) throws IOException {
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
        response.addHeader("Result","successsdfhgsdfgad");
    }
    public static void postaFile(String urlS,String filename) throws IOException {
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
    public static String downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("type");
//        System.out.println(fileName);
//        String fileName ="shafei.xls";
//        String fileFullPath = "C:/Users/Dell/Desktop/print/test/" + fileName;
        String fileFullPath = "src/main/resources/static/TemplateFiles/Template.xml";

        InputStream input = null;
        FileOutputStream fos = null;
        try {
            input = request.getInputStream();
            File file = new File("src/main/resources/static/Axml");
            if(!file.exists()){
                file.mkdirs();
            }
            fos = new FileOutputStream(fileFullPath);
            int size = 0;
            byte[] buffer = new byte[1024];
            while ((size = input.read(buffer,0,1024)) != -1) {
                fos.write(buffer, 0, size);
            }

            //响应信息 json字符串格式
            Map<String,Object> responseMap = new HashMap<String,Object>();
            responseMap.put("flag", true);

            //生成响应的json字符串
            String jsonResponse = JSONObject.toJSONString(responseMap);
            sendResponse(jsonResponse,response);
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

        return null;
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
    //#######################################疑难
    //该方法应该是用户端发送请求其他系统课程列表，然后我这里接收其他两个系统的课程文件进行转换后发给原来请求的系统
//    public void getshareClasses(String url1,String url2,String url3,String type1) throws DocumentException, TransformerException, IOException {
//        //url1是请求分析的系统地址
//        //url2和url3是其他两个系统
//        //type1是请求分享系统的类别
//        String type2;
//        String type3;
//        if (type1=="A") {
//            type2="B";
//            type3="C";
//        }
//        else if(type1=="B"){
//            type2="A";
//            type3="C";
//        }
//        else{
//            type2="A";
//            type3="B";
//        }
//        Random random=new Random();
//        //我是想获得其他两个系统的课程file文件到本地，但是不知道怎么获得，写了一个空的getfile方法
//        String file2=getFile(url2);
//        String file3=getFile(url3);
//        //下面我对获得的两个文件进行了验证和转化
//        String xsd2="src\\main\\resources\\static\\xsds\\class"+type2;
//        String xsd3="src\\main\\resources\\static\\xsds\\\\class"+type3;
//        String xslformat="src\\main\\resources\\static\\xsls\\formatClass.xsl";
//        String xsl1="src\\main\\resources\\static\\xsls\\classTo"+type1+".xsl";
//        checkXML(xsd2,file2);
//        checkXML(xsd3,file3);
//        Integer integer=new Integer(random.nextInt(1000000000));
//        String file2format=integer.toString()+"format2";
//        String file3format=integer.toString()+"format3";
//        transformerXML(file2format,file2,xslformat);
//        transformerXML(file3format,file3,xslformat);
//        String file21=integer.toString()+"21";
//        String file31=integer.toString()+"31";
//        transformerXML(file21,file2format,xsl1);
//        transformerXML(file31,file3format,xsl1);
//        //转化结果是file21，和file31，下面我想把这两个文件传回到请求课程分析的系统，也不知道怎传回去。
//        uploadFile(url1,file21);
//        uploadFile(url1,file31);
//    }
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

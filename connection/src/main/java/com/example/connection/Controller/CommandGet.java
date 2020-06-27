package com.example.connection.Controller;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.*;
import org.dom4j.util.XMLErrorHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@RestController
@RequestMapping("/course")
public class CommandGet {
    //#####################################疑难
    public String chooseClasses(String url1,String url2,String type1,String type2) throws DocumentException, TransformerException, IOException {
        //type1请求选课的系统类型 type2是被选课的系统类型
        //url1是请求选课的系统地址，url2是被选课的系统地址
        Random random=new Random();
        Integer integer=new Integer(random.nextInt(1000000000));
        String file0=integer.toString()+"file0";
        //file0是选课系统发来的文件，但我不知道怎么接受，post请求我看不懂，你能不能在这个方法里添加一下，我搞了两个下午也没搞懂。
        //下面的代码对file0进行了验证，并且转化为标准格式后再转化为目标系统格式
        String xsd1="src\\main\\resources\\static\\xsds\\student"+type1;
        checkXML(xsd1,file0);
        String formatxsl="src\\main\\resources\\static\\xsls\\formatStudent.xsl";
        String file1=integer.toString()+"file1";
        transformerXML(file1,file0,formatxsl);
        String xsl2="src\\main\\resources\\static\\xsls\\studentTo"+type2+".xsl";
        String file2=integer.toString()+"file2";
        transformerXML(file2,file1,xsl2);
        //转化为目标系统格式后为file2，下面我就不知道怎么传到被选课系统了，还有如何收到被选课系统的反馈。
        uploadFile(url2,file2);
        return "success";
    }
    //#######################################疑难
    public void getshareClasses(String url1,String url2,String url3,String type1) throws DocumentException, TransformerException, IOException {
        //url1是请求分析的系统地址
        //url2和url3是其他两个系统
        //type1是请求分享系统的类别
        String type2;
        String type3;
        if (type1=="A") {
            type2="B";
            type3="C";
        }
        else if(type1=="B"){
            type2="A";
            type3="C";
        }
        else{
            type2="A";
            type3="B";
        }
        Random random=new Random();
        //我是想获得其他两个系统的课程file文件到本地，但是不知道怎么获得，写了一个空的getfile方法
        String file2=getFile(url2);
        String file3=getFile(url3);
        //下面我对获得的两个文件进行了验证和转化
        String xsd2="src\\main\\resources\\static\\xsds\\class"+type2;
        String xsd3="src\\main\\resources\\static\\xsds\\\\class"+type3;
        String xslformat="src\\main\\resources\\static\\xsls\\formatClass.xsl";
        String xsl1="src\\main\\resources\\static\\xsls\\classTo"+type1+".xsl";
        checkXML(xsd2,file2);
        checkXML(xsd3,file3);
        Integer integer=new Integer(random.nextInt(1000000000));
        String file2format=integer.toString()+"format2";
        String file3format=integer.toString()+"format3";
        transformerXML(file2format,file2,xslformat);
        transformerXML(file3format,file3,xslformat);
        String file21=integer.toString()+"21";
        String file31=integer.toString()+"31";
        transformerXML(file21,file2format,xsl1);
        transformerXML(file31,file3format,xsl1);
        //转化结果是file21，和file31，下面我想把这两个文件传回到请求课程分析的系统，也不知道怎传回去。
        uploadFile(url1,file21);
        uploadFile(url1,file31);
    }
    public static String getFile(String url){
        return "";
    }
    public static void uploadFile(String urlS,String fileName) {
        try {

            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // 上传文件
            File file = new File(fileName);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"photo\";filename=\"" + fileName
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());

            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(
                    file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    conn.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }
    public static void checkXML(String xsdPath0, String xmlPath0) {
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
            } else {
                System.out.println("XML文件通过XSD文件校验成功！");
            }
        } catch (Exception e) {
            System.out.println("asdf");
        }

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

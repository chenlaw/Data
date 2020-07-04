package com.example.connection.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.*;
import org.dom4j.util.XMLErrorHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XMLUtil {
    public static void transformerXML(String resultPath,String sourcePath,String xslPath) throws IOException, DocumentException, TransformerException {
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(new BufferedInputStream(new FileInputStream(sourcePath)));
        TransformerFactory factory=TransformerFactory.newInstance();
        Transformer transformer=factory.newTransformer(new StreamSource(xslPath));
        DocumentSource source=new DocumentSource(document);
        DocumentResult result=new DocumentResult();
        transformer.transform(source,result);
        Document transformedDoc=result.getDocument();
        Writer w=new FileWriter(resultPath);
        OutputFormat opf=OutputFormat.createPrettyPrint();
        opf.setEncoding("utf-8");
        XMLWriter xw=new XMLWriter(w,opf);
        xw.write(transformedDoc);
        xw.close();
        w.close();
    }
    public static void transformerXML(String resultPath,File sourcePath,String xslPath) throws IOException, DocumentException, TransformerException {
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read(new FileReader(sourcePath));
        TransformerFactory factory=TransformerFactory.newInstance();
        Transformer transformer=factory.newTransformer(new StreamSource(xslPath));
        DocumentSource source=new DocumentSource(document);
        DocumentResult result=new DocumentResult();
        transformer.transform(source,result);
        Document transformedDoc=result.getDocument();
        Writer w=new FileWriter(resultPath);
        OutputFormat opf=OutputFormat.createPrettyPrint();
        opf.setEncoding("utf-8");
        XMLWriter xw=new XMLWriter(w,opf);
        xw.write(transformedDoc);
        xw.close();
        w.close();
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
    public static String checkXML(String xsdPath0, File xmlPath) {
        String xsdPath = xsdPath0;
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
            Document document = reader.read(xmlPath);


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
}

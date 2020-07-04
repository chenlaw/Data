package com.example.demo.utils;

import com.example.demo.Dao.CourseMapper;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/31 14:56
 */
@Service
public class XMLUtil {

    @Autowired
    CourseMapper mapper;
    public static boolean validate(File file,String xsdFileName) throws SAXException, IOException, DocumentException, ParserConfigurationException {
        SAXReader saxReader=new SAXReader();
        saxReader.setValidation(true);
        saxReader.setFeature("http://xml.org/sax/feature/validation",true);
        saxReader.setFeature("http://apache.org/xml/features/validation/schema",true);
        saxReader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation","*.xsd");
        XMLErrorHandler errorHandler=new XMLErrorHandler();
        saxReader.setErrorHandler(errorHandler);
        SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
        SAXParser saxParser= saxParserFactory.newSAXParser();
        Document document=saxReader.read((File) file);
        saxParser.setProperty(
                "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                "http://www.w3.org/2001/XMLSchema");
        saxParser.setProperty(
                "http://java.sun.com/xml/jaxp/properties/schemaSource",
                "file:" + xsdFileName);
        SAXValidator validator = new SAXValidator(saxParser.getXMLReader());
        //设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
        validator.setErrorHandler(errorHandler);
        //校验
        validator.validate(document);
        if(errorHandler.getErrors().hasContent())
            return false;
        else
            return true;
    }


    //todo:考虑专门化插入对应课程。可能除了xml文件，附加的参数不同。
    public void analytical(File file,String share) throws FileNotFoundException, DocumentException {
       SAXReader saxReader=new SAXReader();
       Document document=saxReader.read(new FileReader(file));
        Element root=document.getRootElement();
        for(Iterator i=root.elementIterator();i.hasNext();){
            Element foo= (Element) i.next();
            Vector<String> course=new Vector<>();
            for(Iterator j=foo.elementIterator();j.hasNext();){
                Element temp=(Element) j.next();
                course.add(temp.getStringValue());
            }
            String[] data=course.toArray(new String[course.size()+1]);
            data[data.length-1]=share;
            mapper.insertCurriculum( data);
        }
    }
}

package com.example.demo.Controller;

import com.example.demo.Dao.CourseMapper;
import com.example.demo.Service.CourseService;
import com.example.demo.po.Curriculum;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.XMLUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.spi.XmlWriter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/29 19:52
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseMapper mapper;
    @Autowired
    XMLUtil util;
    @GetMapping("/share")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return courseService.share();
    }
    @PostMapping("/shared")
     public String upload(@RequestParam("file") MultipartFile mfile) throws SAXException, IOException, DocumentException, ParserConfigurationException {
       File file= FileUtil.MultipartFileToFile(mfile);
        //XMLUtil.validate( file,"s");
        util.analytical( file);
        return "true";
    }
    @GetMapping("/getCourses")
    public List<Curriculum> getCourses(){
        return courseService.getCourses();
    }
    @GetMapping("/test")
    public String test(){
        String[] v= new String[]{"1", "2","4","4","4","4","4"};
        mapper.insertCurriculum(v);
        return "1";
    }
}

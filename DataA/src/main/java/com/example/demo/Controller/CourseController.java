package com.example.demo.Controller;

import com.example.demo.Dao.CourseMapper;
import com.example.demo.Service.CourseService;
import com.example.demo.po.Curriculum;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.XMLUtil;
import com.example.demo.vo.ResponseVO;
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
import java.io.*;
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
    //分享课程第一步
    //内部调用 服务器/course/share
    @GetMapping("wantToShare")
    public ResponseVO getShare(){
        return courseService.getShare("A");
    }
    //此接口将本地课程数据传给中间服务器
    //接口 /course/share?to=?
    //内部调用：服务器的/course/receiveShare?class.xml={file}&to=?
    @GetMapping("/share")
    public ResponseVO downloadFile(@RequestParam("to") String to) throws IOException {
        return courseService.share(to);
    }
    //分享的文件发向这里
    //接口 /course/shared?file=file&from=?
    @PostMapping("/shared")
     public ResponseVO upload(@RequestParam("file") MultipartFile mfile,@RequestParam("from") String share) throws SAXException, IOException, DocumentException, ParserConfigurationException {
       File file= FileUtil.MultipartFileToFile(mfile);
        //XMLUtil.validate( file,"s");
        util.analytical( file,share);
        return ResponseVO.buildSuccess();
    }
    //供给前端选择课程.
    @GetMapping("/getCourses")
    public ResponseVO getCourses(){
        return courseService.getCourses();
    }
    @GetMapping("/test")
    public String test(){
        String[] v= new String[]{"1", "2","4","4","4","A"};
        mapper.insertCurriculum(v);
        return "1";
    }
}

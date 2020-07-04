package com.example.demo.Service;

import com.example.demo.Dao.CourseMapper;
import com.example.demo.po.Curriculum;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.HttpUtil;
import com.example.demo.vo.ResponseVO;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSetInternal;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.XmlWriter;
import java.io.*;
import org.dom4j.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.SQLException;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/29 19:56
 */
@Service
public class CourseService {
    @Autowired
    CourseMapper mapper;
    public ResponseVO share(String to) throws IOException {
        List<Curriculum> curricula=mapper.getCurriculums();
        Document  doc=DocumentHelper.createDocument();
        Element root=doc.addElement("Classess");
        for(int i=0;i<curricula.size();i++){
            Element emp=root.addElement("class");
            Element c=emp.addElement("课程编号");
            c.setText(curricula.get(i).getCno());
            Element cnm=emp.addElement("课程名");
            cnm.setText(curricula.get(i).getCnm());
            Element ctm=emp.addElement("学分");
            cnm.setText(String.valueOf(curricula.get(i).getCtm()));
            Element tec=emp.addElement("授课老师");
            tec.setText(curricula.get(i).getTec());
            Element pla=emp.addElement("授课地点");
            pla.setText(curricula.get(i).getPla());
            Element share=emp.addElement("共享");
            share.setText(String.valueOf(curricula.get(i).getShare()));
        }
        String filepath="src\\main\\resources\\temp\\shareClass.xml";
        Writer w=new FileWriter(filepath);
        OutputFormat opf=OutputFormat.createPrettyPrint();
        opf.setEncoding("GB2312");
        XMLWriter xw= new XMLWriter(w, opf);
        xw.write(doc);
        xw.close();
        w.close();
        String urls="http://localhost:8080/course/receiveShare";
        MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
        File file = new File(filepath);
        // 文件必须封装成FileSystemResource这个类型后端才能收到附件
        FileSystemResource resource = new FileSystemResource(file);
        param.add("class.xml",resource);
        param.add("to",to);
        boolean res= HttpUtil.sendFile(urls,param);
        if(res)
            return ResponseVO.buildSuccess();
        else
            return ResponseVO.buildFailure("通讯失败");
    }

    public ResponseVO getCourses() {
        return ResponseVO.buildSuccess(mapper.getCurriculums());
    }

    public ResponseVO getShare(String c) {
        String urls="http://localhost:8080/course/share";
        MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
        param.add("type",c);
        HttpUtil.sendFile(urls,param);
        return ResponseVO.buildSuccess();

    }
}

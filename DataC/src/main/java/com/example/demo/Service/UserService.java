package com.example.demo.Service;

import com.example.demo.Dao.CourseMapper;
import com.example.demo.Dao.SelectionMapper;
import com.example.demo.Dao.UserMapper;
import com.example.demo.po.Curriculum;
import com.example.demo.po.Student;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.HttpUtil;
import com.example.demo.vo.LogForm;
import com.example.demo.vo.ResponseVO;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 21:32
 */
@Service
public class UserService {
    @Autowired
    UserMapper mapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    SelectionMapper selectionMapper;
    public ResponseVO login(LogForm form) {
        Student student=mapper.getStudentFromSno(form.getPassWd());
        if(student==null)
            return ResponseVO.buildFailure("用户名或密码错误");
        else if(student.getPwd()!=form.getPassWd()){
            return ResponseVO.buildFailure("用户名或密码错误");
        }else
            return ResponseVO.buildSuccess();
    }

    public ResponseVO pickCourse(String sno, String cno) throws IOException, JAXBException {
        Student s=mapper.getStudentFromSno(sno);
        Curriculum c=courseMapper.getCurriculumFromSno(cno);
        if(c.getShare()=='C'){
            selectionMapper.insertData(cno,sno);
        return ResponseVO.buildSuccess();}
        else{
            String ht="http://localhost:8080/course/pick";
            String path=getUserXml(s);
            MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
            File file = new File(path);
            // 文件必须封装成FileSystemResource这个类型后端才能收到附件
            FileSystemResource resource = new FileSystemResource(file);
            param.add("student",resource);
            param.add("Cno",cno);
            param.add("Sno",sno);
            param.add("to",String.valueOf(c.getShare()));
            boolean res=HttpUtil.sendFile(ht,param);
            if(res) {
                selectionMapper.insertData(cno, sno);
                return ResponseVO.buildSuccess();
            }
            else
                return ResponseVO.buildFailure("选课失败");
        }
    }
    public String getUserXml(Student student) throws IOException, JAXBException {

        Document doc= DocumentHelper.createDocument();
        Element root=doc.addElement("Students");

            Element emp=root.addElement("student");
            Element c=emp.addElement("Sno");
            c.setText(student.getSno());
            Element cnm=emp.addElement("Snm");
            cnm.setText(student.getSnm());
            Element ctm=emp.addElement("CSex");
            ctm.setText(student.getSex());
            Element cpt=emp.addElement("Sde");
            cpt.setText(student.getSde());

        String filepath="src\\main\\resources\\temp\\student.xml";
        Writer w=new FileWriter(filepath);
        OutputFormat opf=OutputFormat.createPrettyPrint();
        opf.setEncoding("GB2312");
        XMLWriter xw= new XMLWriter(w, opf);
        xw.write(doc);
        xw.close();
        w.close();
        return filepath;
    }

    public ResponseVO getDecidedCourse(String sno) {
        List<String> curris=selectionMapper.getDataBySno(sno);
        List<Curriculum> curricula=courseMapper.getCurriculumsfromCnos(curris);
        return ResponseVO.buildSuccess(curricula);
    }

    public ResponseVO dropCourse(String sno, String cno) throws IOException, JAXBException {
        Student s=mapper.getStudentFromSno(sno);
        Curriculum c=courseMapper.getCurriculumFromSno(sno);
        if(c.getShare()=='C')
            selectionMapper.deleteData(cno,sno);
        else {
            String ht="http://localhost:8080/course/drop";
            String path=getUserXml(s);
            MultiValueMap<String, Object> param=new LinkedMultiValueMap<>();
            File file = new File(path);
            // 文件必须封装成FileSystemResource这个类型后端才能收到附件
            FileSystemResource resource = new FileSystemResource(file);
            param.add("student",resource);
            param.add("class",cno);
           boolean res=HttpUtil.sendFile(ht,param);
           if(res){
               selectionMapper.deleteData(cno,sno);

           }

        }
        return ResponseVO.buildSuccess();
    }
}

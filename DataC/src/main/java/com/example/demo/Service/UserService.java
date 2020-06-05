package com.example.demo.Service;

import com.example.demo.Dao.CourseMapper;
import com.example.demo.Dao.SelectionMapper;
import com.example.demo.Dao.UserMapper;
import com.example.demo.po.Curriculum;
import com.example.demo.po.Student;
import com.example.demo.utils.FileUtil;
import com.example.demo.vo.LogForm;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    public String login(LogForm form) {
        return "a";
    }

    public String pickCourse(String sno, String cno) throws IOException, JAXBException {
        Student s=mapper.getStudentFromSno(sno);
        Curriculum c=courseMapper.getCurriculumFromSno(sno);
        if(c.getShare()=='C')
            selectionMapper.insertData(cno,sno);
        else{
            String ht=String.valueOf(c.getShare());
            String path=getUserXml(s);
            FileUtil.uploadFile(ht,path);
        }


        return "yes";
    }
    public String getUserXml(Student student) throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(Student.class);
        // 创建 Marshaller 实例
        Marshaller marshaller = context.createMarshaller();
        // 设置转换参数 -> 这里举例是告诉序列化器是否格式化输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // 构建输出环境 -> 这里使用标准输出，输出到控制台Console

        // 将所需对象序列化 -> 该方法没有返回值
        String filepath="F:\\"+student.getSno()+".xml";
        Writer w=new FileWriter(filepath);
        marshaller.marshal(student,w);
        w.close();
        return filepath;
    }

    public List<Curriculum> getDecidedCourse(String sno) {
        List<String> curris=selectionMapper.getDataBySno(sno);
        List<Curriculum> curricula=courseMapper.getCurriculumsfromCnos(curris);
        return curricula;
    }

    public String dropCourse(String sno, String cno) throws IOException, JAXBException {
        Student s=mapper.getStudentFromSno(sno);
        Curriculum c=courseMapper.getCurriculumFromSno(sno);
        if(c.getShare()=='C')
            selectionMapper.deleteData(cno,sno);
        else {
            String ht=String.valueOf(c.getShare());
            String path=getUserXml(s);
            FileUtil.uploadFile(ht,path);
        }
        return "yes";
    }
}

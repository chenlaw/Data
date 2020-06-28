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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
            return ResponseVO.buildSuccess()
    }

    public ResponseVO pickCourse(String sno, String cno) throws IOException, JAXBException {
        Student s=mapper.getStudentFromSno(sno);
        Curriculum c=courseMapper.getCurriculumFromSno(sno);
        if(c.getShare()=='C'){
            selectionMapper.insertData(cno,sno);
        return ResponseVO.buildSuccess();}
        else{
            String ht=String.valueOf(c.getShare());
            String path=getUserXml(s);
            boolean res=HttpUtil.sendFile(ht,path);
            if(res) {
                selectionMapper.insertData(cno, sno);
                return ResponseVO.buildSuccess();
            }
            else
                return ResponseVO.buildFailure("选课失败");
        }
    }
    public String getUserXml(Student student) throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(Student.class);
        // 创建 Marshaller 实例
        Marshaller marshaller = context.createMarshaller();
        // 设置转换参数 -> 这里举例是告诉序列化器是否格式化输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // 构建输出环境 -> 这里使用标准输出，输出到控制台Console

        // 将所需对象序列化 -> 该方法没有返回值
        String filepath="src\\main\\resources\\temp\\"+student.getSno()+".xml";
        Writer w=new FileWriter(filepath);
        marshaller.marshal(student,w);
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
            String ht=String.valueOf(c.getShare());
            String path=getUserXml(s);
           boolean res=HttpUtil.sendFile(ht,path);
           if(res){
               selectionMapper.deleteData(cno,sno);

           }

        }
        return ResponseVO.buildSuccess();
    }
}

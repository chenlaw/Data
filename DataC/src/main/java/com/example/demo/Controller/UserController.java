package com.example.demo.Controller;

import com.example.demo.Dao.UserMapper;
import com.example.demo.Service.UserService;
import com.example.demo.po.Curriculum;
import com.example.demo.po.Student;
import com.example.demo.vo.LogForm;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 21:31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/log")
    @ResponseBody
    public ResponseVO log(@RequestBody LogForm form){
        return userService.login(form);
    }
    @GetMapping("/pick")
    public ResponseVO pickCourse(@RequestParam("Sno") String sno,@RequestParam("Cno") String cno) throws IOException, JAXBException {
        return userService.pickCourse(sno,cno);
    }
    @GetMapping("/course")
    public ResponseVO getDecidedCourse(@RequestParam("Sno") String sno){
        return userService.getDecidedCourse(sno);
    }
    @GetMapping("/dropCourse")
    public String dropCourse(@RequestParam("Sno") String sno,@RequestParam("Cno") String cno) throws IOException, JAXBException {
        return userService.dropCourse(sno,cno);
    }
}

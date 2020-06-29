package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.vo.LogForm;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;

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
    //选课
    //当课程的share与服务器相同，则直接插入数据库。
    //调用服务器：/course/choose?student={文件}&class=?
    @GetMapping("/pick")
    public ResponseVO pickCourse(@RequestParam("Sno") String sno,@RequestParam("Cno") String cno) throws IOException, JAXBException {
        return userService.pickCourse(sno,cno);
    }
    @GetMapping("/course")
    public ResponseVO getDecidedCourse(@RequestParam("Sno") String sno){
        return userService.getDecidedCourse(sno);
    }
    //退课
    //类似选课
    @GetMapping("/dropCourse")
    public ResponseVO dropCourse(@RequestParam("Sno") String sno,@RequestParam("Cno") String cno) throws IOException, JAXBException {
        return userService.dropCourse(sno,cno);
    }
}

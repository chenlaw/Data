package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 23:55
 */
@Controller
public class HtmlController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}

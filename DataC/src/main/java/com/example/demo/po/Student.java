package com.example.demo.po;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 22:14
 */
@Data
@XmlRootElement
public class Student {
    String Sno;
    String Snm;
    String Sex;
    String Sde;//院系，这里作为分辩来源
    String Pwd;
}

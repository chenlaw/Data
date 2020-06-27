package com.example.connection.po;

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
    String Sde;
    String Pwd;
}

package com.example.demo.Dao;

import com.example.demo.po.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/27 21:32
 */
@Mapper
public interface UserMapper {

    Student getStudentFromSno(String sno);
}

package com.example.demo.Dao;

import com.example.demo.po.Curriculum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Vector;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/29 21:14
 */
@Mapper
public interface CourseMapper {

    List<Curriculum> getCurriculums();

    Curriculum getCurriculumFromSno(String sno);
    int insertCurriculum(String[] v);
    List<Curriculum> getCurriculumsfromCnos(List<String> curris);
}

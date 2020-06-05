package com.example.demo.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @date 2020/5/30 22:50
 */
@Mapper
public interface SelectionMapper {

    int insertData(String cno, String sno);

    List<String> getDataBySno(String sno);

    void deleteData(String cno, String sno);
}

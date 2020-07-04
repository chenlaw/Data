package com.example.demo.Dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    String getPassword(String account);
}

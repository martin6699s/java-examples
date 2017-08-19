package com.example.transactionTest.mapper;

import com.example.transactionTest.pojo.RUser;

import java.util.List;

public interface RUserMapper {

    int insert(RUser record);

    int insertSelective(RUser record);

    List<RUser> selectByAge(Integer age);
}
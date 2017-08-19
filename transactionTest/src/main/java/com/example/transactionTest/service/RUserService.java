package com.example.transactionTest.service;

import com.example.transactionTest.mapper.RUserMapper;
import com.example.transactionTest.pojo.RUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by martin on 2017/8/17.
 */
@Service
public class RUserService {

    @Autowired
    private RUserMapper rUserMapper;

    @Transactional
    public List<RUser> insert(RUser user) throws Exception {

        Integer isSuccess = rUserMapper.insert(user);

//        if(isSuccess > 0) {
//          List<RUser> rUserList = rUserMapper.selectByAge(10);
//
//          throw new RespException("4001","不合适");
//
//        }

        return rUserMapper.selectByAge(10);
    }
}

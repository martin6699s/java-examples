package com.martin6699s.springBootExample2021.controller;

import com.martin6699s.springBootExample2021.service.RedisService;
import com.martin6699s.springBootExample2021.vo.resp.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final static Logger log = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisService redisService;



    @PostMapping("/setnxex")
    public BaseResponse setNxEx() {


        return redisService.setNxEx();

    }


    @PostMapping("/setCommonRedisVo")
    public BaseResponse setCommonRedisVoNxEx() {


        return redisService.setCommonRedisVoNxEx();

    }


    @PostMapping("/lockAndUnlock")
    public BaseResponse lockAndUnlock() {

        return redisService.lockAndUnlock();
    }
}

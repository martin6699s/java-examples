package com.martin6699s.springBootExample2021.service;

import com.martin6699s.springBootExample2021.utils.JacksonUtil;
import com.martin6699s.springBootExample2021.utils.RedisUtil;
import com.martin6699s.springBootExample2021.vo.CommonRedisVo;
import com.martin6699s.springBootExample2021.vo.resp.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisService {

    private final static Logger log = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisUtil<String> stringRedisUtil;

    @Autowired
    private RedisUtil<CommonRedisVo> commonRedisVoRedisUtil;

    public BaseResponse setNxEx() {

        Boolean isSuccess = stringRedisUtil.setIfAbsent2Expire("helloworld", "100", 100);
        log.info("是否成功：" + isSuccess);

        return new BaseResponse().setSuccess();
    }

    public BaseResponse setCommonRedisVoNxEx() {

        CommonRedisVo commonRedisVo = new CommonRedisVo();
        commonRedisVo.setId("td123456");
        commonRedisVo.setValue("hello spring!");

        /**
         *  redis会记录对象类型
         * "[\"com.martin6699s.springBootExample2021.vo.CommonRedisVo\",{\"id\":\"td123456\
         * ",\"value\":\"hello spring!\"}]"
         */
        Boolean isSuccess = commonRedisVoRedisUtil.setIfAbsent2Expire("common1", commonRedisVo, 100);
        log.info("是否成功：" + isSuccess);
        CommonRedisVo redisVo = commonRedisVoRedisUtil.getValue("common1");
        log.info("redisVo" + JacksonUtil.serialize(redisVo));

        return new BaseResponse().setSuccess();
    }

    public BaseResponse lockAndUnlock() {

        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        log.info("uuid=" + uuid);
        if(stringRedisUtil.lock("uniquelock", uuid, 60000)) {
            try {
                // 获取锁成功，执行业务代码...
                log.info("获取锁成功，执行业务代码");
                Thread.sleep(100);
                return new BaseResponse().setSuccess();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Boolean isSuccess = stringRedisUtil.unlock("uniquelock", uuid);
                log.info("是否解锁成功：" + isSuccess);
            }
        }

        return new BaseResponse().setError(-1, "获取锁失败");
    }
}

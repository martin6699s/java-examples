package com.martin6699s.springBootExample2021.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<T> {

    private final static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private RedisTemplate<String, T> redisTemplate;
    private ValueOperations<String, T> valueOperations;
    private ListOperations<String, T> listOperations;
    private SetOperations<String, T> setOperations;
    private HashOperations<String, Integer, T> hashOperations;

    private HashMap<String, DefaultRedisScript<List>> scriptHashMap ;

    @Autowired
    public RedisUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.listOperations = redisTemplate.opsForList();
        this.setOperations = redisTemplate.opsForSet();
        this.hashOperations = redisTemplate.opsForHash();
        this.scriptHashMap = new HashMap<>(10);
        initScriptMap();
    }


    private void initScriptMap(){
        DefaultRedisScript<List> script = new DefaultRedisScript<List>();
        script.setResultType(List.class);
        script.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("luascript/setnxex.lua")));
        this.scriptHashMap.put("setnxex", script);

        DefaultRedisScript<List> lockScript = new DefaultRedisScript<List>();
        lockScript.setResultType(List.class);
        lockScript.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("luascript/lock.lua")));
        this.scriptHashMap.put("lock", lockScript);

        DefaultRedisScript<List> unlockScript = new DefaultRedisScript<List>();
        unlockScript.setResultType(List.class);
        unlockScript.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("luascript/unlock.lua")));
        this.scriptHashMap.put("unlock", unlockScript);
    }

    //region String
    public void putValue(String key, T value) {
        valueOperations.set(key, value);
    }

    public T getValue(String key) {
        return valueOperations.get(key);
    }

    /**
     * 设置过期时间 毫秒
     * @param key
     * @param timeout
     */
    public void setPExpire(String key, long timeout) {
        setExpire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }
    //endregion


    //region List
    public void addList(String key, T value) {
        listOperations.leftPush(key, value);
    }

    public List<T> getListMembers(String key) {
        return listOperations.range(key, 0, -1);
    }

    public Long getListSize(String key) {
        return listOperations.size(key);
    }
    //endregion


    //region Set
    public void addToSet(String key, T... values) {
        setOperations.add(key, values);
    }

    public Set<T> getSetMembers(String key) {
        return setOperations.members(key);
    }
    //endregion


    //region Hash
    public void saveHash(String key, Integer id, T value) {
        hashOperations.put(key, id, value);
    }

    public T findInHash(String key, int id) {
        return hashOperations.get(key, id);
    }

    public void deleteHash(String key, int id) {
        hashOperations.delete(key, id);
    }
    //endregion


    public Boolean setIfAbsent(String key, T value) {

        return this.valueOperations.setIfAbsent(key, value);
    }

    /**
     * 如果key不存在，则保存并设置过期时间
     * 使用lua脚本保证多命令执行的原子性
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public Boolean setIfAbsent2Expire(String key, T value, Integer expire) {
        DefaultRedisScript<List> script = this.scriptHashMap.get("setnxex");
        List<String> keys = new ArrayList<>();
        keys.add(key);

        List<T> resultList = redisTemplate.execute(script, keys, value, expire);
        log.info("setIfAbsent2Expire返回值：" + JacksonUtil.serialize(resultList));

        return resultList.get(0).equals(1L);
    }

    /**
     * 加锁，value作为该线程锁标识 防止锁误解除
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public synchronized Boolean lock(String key, T value, Integer expire) {
        DefaultRedisScript<List> script = this.scriptHashMap.get("lock");
        List<String> keys = new ArrayList<>();
        keys.add(key);

        List<T> resultList = redisTemplate.execute(script, keys, value, expire);
        log.info("【加锁】 返回数据：{}", JacksonUtil.serialize(resultList));
        if(resultList == null || resultList.isEmpty()) return false;

        return resultList.get(0).equals(1L);
    }


    /**
     * 删除锁 防止锁误解除
     * @param key
     * @param value
     * @return
     */
    public synchronized Boolean unlock(String key, T value) {
        DefaultRedisScript<List> script = this.scriptHashMap.get("unlock");
        List<String> keys = new ArrayList<>();
        keys.add(key);

        List<T> resultList = redisTemplate.execute(script, keys, value);
        log.info("【解锁】 返回数据：{}", JacksonUtil.serialize(resultList));
        if(resultList == null || resultList.isEmpty()) return false;

        return resultList.get(0).equals(1L);
    }


}

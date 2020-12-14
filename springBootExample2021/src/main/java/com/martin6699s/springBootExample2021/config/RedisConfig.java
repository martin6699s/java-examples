package com.martin6699s.springBootExample2021.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final static Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    RedisProperties redisProperties;
    //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    private final static Jackson2JsonRedisSerializer JACKSON_SERIALIZER = new Jackson2JsonRedisSerializer(Object.class);
    private final static StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    /**
     * GenericObjectPoolConfig 连接池配置
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        log.info("redis缓存测试 获取最大保活连接数：{}", redisProperties.getLettuce().getPool().getMaxActive());
        return genericObjectPoolConfig;
    }

    /**
     * 工厂创建了连接 但是一个单实例
     **/
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(),redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(redisProperties.getTimeout())
                .shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout())
                .poolConfig(genericObjectPoolConfig)
                .build();
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,clientConfig);
        return lettuceConnectionFactory;
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper mapper = JsonMapper.builder().build();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        /**
         *  mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)已被弃用，
         *  使用activateDefaultTyping方法代替
         *  作用是指定序列化输入的类型，将数据类型一并存储到redis缓存中。原因是存储到redis里的数据如果是没有类型的纯json，
         * 我们调用redis API获取到数据后，java解析将是一个LinkHashMap类型的key-value的数据结构，
         * 我们需要使用的话就要自行解析，这样增加了编程的复杂度。
         */
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        JACKSON_SERIALIZER.setObjectMapper(mapper);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        // key采用String的序列化方式
        template.setKeySerializer(STRING_SERIALIZER);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(STRING_SERIALIZER);
        // value采用jackson序列化方式
        template.setValueSerializer(JACKSON_SERIALIZER);
        // hash的value采用jackson序列化方式
        template.setHashValueSerializer(JACKSON_SERIALIZER);

        template.afterPropertiesSet();

        return template;
    }


    public static Jackson2JsonRedisSerializer getJacksonSerializer() {
        return JACKSON_SERIALIZER;
    }

    public static StringRedisSerializer getStringSerializer() {
        return STRING_SERIALIZER;
    }

}

package com.example.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    public static final int REDIS_TIMEOUT = 10;
    @Autowired
    private HttpMessageConverters fastJsonMessageConverters;
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer); //设置key的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);//设置hash类型的数据的key的序列化方式
//
//
//        ObjectMapper om = new ObjectMapper();
//        //一种方便的方法，允许更改底层 VisibilityCheckers 的配置，以更改自动检测的属性类型的详细信息。
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//        //LocalDatetime序列化
//        JavaTimeModule timeModule = new JavaTimeModule();
//        timeModule.addDeserializer(LocalDate.class,
//                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        timeModule.addDeserializer(LocalDateTime.class,
//                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        timeModule.addSerializer(LocalDate.class,
//                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        timeModule.addSerializer(LocalDateTime.class,
//                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        timeModule.addSerializer(Long.class, ToStringSerializer.instance);
//        timeModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
//
//        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        om.registerModule(timeModule);

        //GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer=new GenericFastJsonRedisSerializer();
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);//非final类型的数据才会被序列化
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

//        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);//设置value的序列化方式为json
//        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);//设置value的序列化方式为json
        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}

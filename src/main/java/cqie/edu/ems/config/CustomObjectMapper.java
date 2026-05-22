package cqie.edu.ems.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        // 注册Java 8日期时间模块
        registerModule(new JavaTimeModule());
        // 禁用将日期序列化为时间戳的功能
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
package com.example.storyworkshop.common.util;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.JSON_PROCESS_ERROR, "对象转换 JSON 失败");
        }
    }

    public static <T> T fromJson(String json, Class<T> targetClass) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, targetClass);
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.JSON_PROCESS_ERROR, "JSON 转换对象失败");
        }
    }

    public static <T> List<T> fromJsonList(String json, Class<T> elementClass) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        JavaType javaType = OBJECT_MAPPER
                .getTypeFactory()
                .constructCollectionType(List.class, elementClass);

        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.JSON_PROCESS_ERROR, "JSON 转换列表失败");
        }
    }
}
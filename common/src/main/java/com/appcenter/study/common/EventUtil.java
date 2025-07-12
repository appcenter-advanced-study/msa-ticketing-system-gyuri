package com.appcenter.study.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventUtil {
    public static <T> T mappingMessageToClass(String message, Class<T> classType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(message, classType);
        } catch (JsonProcessingException e) {
            log.error("Kafka 메시지 역직렬화 실패", e);
            return null;
        }
    }
}

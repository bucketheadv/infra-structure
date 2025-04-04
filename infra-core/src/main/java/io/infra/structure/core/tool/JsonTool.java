package io.infra.structure.core.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author sven
 * Created on 2025/1/12 14:34
 */
@Slf4j
@Configuration("jsonTool")
public class JsonTool implements ApplicationContextAware {
    private static ObjectMapper om = new ObjectMapper();

    static {
        init(om);
    }

    private static void setDefaultObjectMapper(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            om = objectMapper;
        }
    }

    private static void init(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            // 时间序列化为时间戳
            objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 未知属性时，不拋出异常
            objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String str, Class<T> clazz) {
        try {
            return om.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析 T<R> 格式的泛型类
     * @param jsonString
     * @param c1
     * @param c2
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> T parseGeneric(String jsonString, Class<T> c1, Class<R> c2) {
        try {
            return om.readValue(jsonString, om.getTypeFactory().constructParametricType(c1, c2));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseList(String str, Class<T> clazz) {
        try {
            return om.readValue(str, om.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K, V> Map<K, V> parseMap(String str, Class<K> kClass, Class<V> vClass) {
        try {
            return om.readValue(str, om.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convert2Value(Object value, Class<T> clazz) {
        return om.convertValue(value, clazz);
    }

    public static <T> List<T> convert2List(Object value, Class<T> clazz) {
        return om.convertValue(value, om.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        setDefaultObjectMapper(objectMapper);
        init(objectMapper);
    }
}

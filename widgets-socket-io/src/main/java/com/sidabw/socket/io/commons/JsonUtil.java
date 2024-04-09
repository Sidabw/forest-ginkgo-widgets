package com.sidabw.socket.io.commons;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * https://www.baeldung.com/jackson-object-mapper-tutorial
 * @author liyk
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 反序列化忽略未知字段
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JsonUtil() {
    }

    /**
     * json
     */
    public static JsonNode deserialize(String s) {

        try {
            return MAPPER.readTree(s);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 序列化
     */
    public static String serialize(Object obj) {

        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 反序列化 obj
     */
    public static <T> T deserialize(String str, Class<T> valueType) {

        try {
            return MAPPER.readValue(str, valueType);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 反序列化
     */
    public static <T> T deserialize(TreeNode node, Class<T> valueType) {

        if (Objects.isNull(node)) {
            return null;
        }
        try {
            return MAPPER.treeToValue(node, valueType);
        } catch (Exception e) {
            log.error("invalid tree node {}", node, e);
        }
        return null;
    }

    /**
     * 反序列化识别泛型obj.  eg.list、map eg： List<ReceiveMsgNew> list = JsonUtil.deserialize(result1, new
     * TypeReference<List<ReceiveMsgNew>>() {}); Map<String,Object> map = JsonUtil.deserialize(result1, new
     * TypeReference<Map<String,Object>>() {});
     */
    public static <T> T deserialize(String str, TypeReference<T> valueTypeRef) {
        try {
            return MAPPER.readValue(str, valueTypeRef);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * https://www.baeldung.com/jackson-collection-array
     */
    public static <T> List<T> deserializeToList(String str, Class<T> clazz) {
        try {
            CollectionType javaType = MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, clazz);
            return MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

    @SuppressWarnings({"rawtypes"})
    public static <T> T convertMapToPojo(Map aMap, Class<T> clazz) {
        try {
            return MAPPER
                    .convertValue(aMap, MAPPER.getTypeFactory().constructType(clazz));
        } catch (Exception e) {
            log.error("converting failed! aMap: {}, class: {}", aMap, clazz.getSimpleName(), e);
        }
        return null;
    }

    public static <T> Map<String, String> convertPojoToStringMap(T t) {
        try {
            return MAPPER.convertValue(t, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            log.error("converting failed! pojo: {}", t, e);
        }

        return null;
    }

    public static <T> Map<String, Object> convertPojoToMap(T t) {
        try {
            return MAPPER.convertValue(t, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("converting failed! pojo: {}", t, e);
        }
        return null;
    }

}

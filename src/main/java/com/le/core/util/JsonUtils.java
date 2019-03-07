/**
 * Copyright (c) 2018 苍穹跃龙
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.le.core.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 严秋旺
 * @date 2018-08-30
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * json字符串转对象
     *
     * @param json
     * @param valueType
     * @param           <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        T result = null;

        try {
            result = mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * json字符串转对象列表
     *
     * @param json
     * @param valueType
     * @param            <T>
     * @return
     */
    public static <T> List<T> toObjectList(String json, Class<T> valueType) {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        List<T> result;

        try {
            result = mapper.readValue(json, typeFactory.constructCollectionType(ArrayList.class, valueType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * json字符串转Map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> result = null;

        try {
            result = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 对象转JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            String json = mapper.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

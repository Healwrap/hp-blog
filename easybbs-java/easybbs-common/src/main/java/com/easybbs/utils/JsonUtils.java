package com.easybbs.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @ClassName JsonUtils
 * @Description json工具类
 * @Date 2023/4/12 12:00
 * @author pepedd
 */
public class JsonUtils {
  /**
   * 对象转json
   *
   * @param object 对象
   * @return json
   */
  public static String convertObject2Json(Object object) {
    return JSON.toJSONString(object);
  }

  /**
   * json转对象
   *
   * @param json  json
   * @param clazz 对象类型
   * @param <T>   对象类型
   * @return 对象
   */
  public static <T> T convertJson2Object(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

  /**
   * json转list
   *
   * @param json  json
   * @param clazz 对象类型
   * @param <T>   对象类型
   * @return list
   */
  public static <T> List<T> convertJsonArray2List(String json, Class<T> clazz) {
    return JSON.parseArray(json, clazz);
  }
}

package com.healwrap.generator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.logging.Logger;

/**
 * &#064;ClassName  JsonUtils
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 21:52
 * &#064;Created  by admin
 */
public class JsonUtils {
  public static String convertObject2Json(Object obj) {
    if (obj == null) {
      return null;
    }
    return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
  }
}

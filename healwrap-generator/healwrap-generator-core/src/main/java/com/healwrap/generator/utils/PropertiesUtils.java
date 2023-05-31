package com.healwrap.generator.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * &#064;ClassName  PropertiesUtils
 * &#064;Description  TODO
 * &#064;Date  2023/4/9 17:30
 * &#064;Created  by admin
 */
public class PropertiesUtils {
  public static Object getProperty(String property) {
    InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.yaml");
    Yaml yaml = new Yaml();
    Map<String, Object> obj = yaml.load(inputStream);
    String[] keys = property.split("\\.");
    Object value = obj;
    for (String key : keys) {
      if (value instanceof Map) {
        value = ((Map) value).get(key);
      }
    }
    return value;
  }

  public static void main(String[] args) {
    String property = "database.url";
    Object value = getProperty(property);
    System.out.println(value);
  }
}

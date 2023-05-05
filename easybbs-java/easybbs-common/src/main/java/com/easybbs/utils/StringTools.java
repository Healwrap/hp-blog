package com.easybbs.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @ClassName StringUtils
 * @Description 字符串工具类
 * @Date 2023/4/11 13:20
 * @Created by admin
 */
public class StringTools {
  /**
   * 判断字符串是否为空
   *
   * @param str 字符串
   * @return true:空 false:非空
   */
  public static Boolean isEmpty(String str) {
    return null == str || "".equals(str.trim()) || "null".equals(str);
  }

  /**
   * 获取随机字符串
   *
   * @param count 字符串长度
   * @return 随机字符串
   */
  public static String getRandomString(Integer count) {
    return RandomStringUtils.random(count, true, true);
  }

  /**
   * 获取随机数字
   *
   * @param count 数字长度
   * @return 随机数字
   */
  public static String getRandomNumber(Integer count) {
    return RandomStringUtils.random(count, false, true);
  }

  /**
   * MD5加密
   *
   * @param str 要加密的字符串
   * @return 加密后的字符串
   */
  public static String encodeMd5(String str) {
    return StringTools.isEmpty(str) ? "" : DigestUtils.md5Hex(str);
  }

  /**
   * 获取文件后缀
   * @param fileName 文件名
   * @return
   */
  public static String getFileSuffix(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }
}

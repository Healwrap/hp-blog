package com.easybbs.entity.constants;

/**
 * @ClassName Constants
 * @Description 一些常量
 * @Date 2023/4/11 12:04
 * @Created by admin
 */
public class Constants {
  /**
   * http请求
   */
  public static final String HTTP = "http://";

  /**
   * https请求
   */
  public static final String HTTPS = "https://";
  /**
   * 数值常量
   */
  public static final Integer INTEGRAL_5 = 5;
  public static final Integer ONE = 1;
  public static final Integer ZERO = 0;
  public static final Integer LENGTH_5 = 5;
  public static final Integer LENGTH_10 = 10;
  public static final Integer LENGTH_15 = 15;
  public static final Integer LENGTH_30 = 30;
  public static final Integer LENGTH_200 = 200;
  /**
   * 验证码
   */
  public static final String CHECK_CODE_KEY = "check_code_key";
  /**
   * 邮箱验证码
   */
  public static final String CHECK_CODE_KEY_EMAIL = "check_code_key_email";
  /**
   * 未知地址
   */
  public static final String NO_ADDRESS = "未知";
  /**
   * session key
   */
  public static final String SESSIONS_KEY = "session_key";
  /**
   * 文件夹
   */
  public static final String FILE_FOLDER_FILE = "/file/";
  /**
   * 临时文件夹
   */
  public static final String FILE_FOLDER_TEMP = "/temp/";
  /**
   * 图片
   */
  public static final String FILE_FOLDER_IMAGE = "/image/";
  /**
   * 头像
   */
  public static final String FILE_FOLDER_AVATAR_NAME = "/avatar/";
  /**
   * 附件
   */
  public static final String FILE_FOLDER_ATTACHMENT = "/attachment/";
  /**
   * 头像后缀
   */
  public static final String AVATAR_SUFFIX = ".jpg";
  /**
   * 默认头像文件名
   */
  public static final String AVATAR_DEFAULT = "default_avatar.jpg";
  /**
   * 允许的图片后缀
   */
  public static final String[] IMAGE_SUFFIX = new String[]{".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};
  /**
   * 允许的文件后缀
   */
  public static final String[] FILE_SUFFIX = new String[]{".zip", ".ZIP", ".rar", ".RAR"};
}

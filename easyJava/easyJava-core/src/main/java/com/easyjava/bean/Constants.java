package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;

/**
 * &#064;ClassName  Constants
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 18:03
 * &#064;Created  by admin
 */
public class Constants {
  public static Boolean IGNORE_TABLE_PREFIX;

  public static String SUFFIX_BEAN_PARAM;
  private static String PATH_JAVA = "java";
  private static String PATH_RESOURCES = "resources";
  public static String PATH_BASE;
  public static String PATH_PO;
  public static String PACKAGE_BASE;
  public static String PACKAGE_PO;


  static {
    IGNORE_TABLE_PREFIX = (Boolean) PropertiesUtils.getProperty("ignore.table.prefix");
    SUFFIX_BEAN_PARAM = (String) PropertiesUtils.getProperty("suffix.bean.param");

    PACKAGE_BASE = (String) PropertiesUtils.getProperty("package.base");
    PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.po");

    PATH_BASE = (String) PropertiesUtils.getProperty("path.base");
    PATH_BASE = PATH_BASE + "/" + PATH_JAVA;

    PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");

  }

  public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
  public final static String[] SQL_DATE_TYPES = new String[]{"date"};
  public static final String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};
  public static final String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
  // integer
  public static final String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};
  // long
  public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};

  public static void main(String[] args) {
    System.out.println(PACKAGE_PO);
    System.out.println(PATH_PO);
  }
}

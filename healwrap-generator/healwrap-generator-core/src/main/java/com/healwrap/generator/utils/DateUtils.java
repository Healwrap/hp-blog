package com.healwrap.generator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * &#064; ClassName Dateutils
 * &#064; Description TODO
 * &#064; Date 2023/4/11 8:48
 * &#064; Created by admin
 */
public class DateUtils {
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String _YYYY_MM_DD = "yyyy/MM/dd";


  public static String format(Date date, String patten) {
    return new SimpleDateFormat(patten).format(date);
  }

  public static String parse(String date, String patten) {
    try {
      return String.valueOf(new SimpleDateFormat(patten).parse(date));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
}

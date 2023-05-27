package com.easyjava.builder;

import com.easyjava.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

/**
 * &#064; ClassName BuildComment
 * &#064; Description TODO
 * &#064; Date 2023/4/11 8:38
 * &#064; Created by admin
 */
public class BuildComment {
  public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {

    bw.write("/**");
    bw.newLine();
    bw.write(" * &#064; Description  " + classComment);
    bw.newLine();
    bw.write(" * &#064; Date " + DateUtils.format(new Date(), DateUtils._YYYY_MM_DD));
    bw.newLine();
    bw.write(" */");
    bw.newLine();
  }

  public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
    bw.write("\t/**");
    bw.newLine();
    bw.write("\t * " + fieldComment);
    bw.newLine();
    bw.write("\t */");
    bw.newLine();
  }

  public static void createMethodComment() {

  }
}

package com.easyjava.builder;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * &#064;ClassName  BuildPo
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 23:19
 * &#064;Created  by admin
 */
public class BuildPo {
  private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

  public static void execute(TableInfo tableInfo) {

    File folder = new File(Constants.PATH_PO);
    System.out.println(folder);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File file = new File(Constants.PATH_PO + "/" + tableInfo.getBeanName() + ".java");
    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;
    try {
      out = new FileOutputStream(file);
      outw = new OutputStreamWriter(out, "UTF-8");
      bw = new BufferedWriter(outw);
      bw.write("package " + Constants.PACKAGE_PO + ";");
      bw.newLine();
      bw.newLine();

      bw.write("import java.io.Serializable;");
      bw.newLine();
      if (tableInfo.isHaveDate() || tableInfo.isHaveDateTime()) {
        bw.write("import java.util.Date;");
        bw.newLine();
      }

      if (tableInfo.isHaveBigDecimal()) {
        bw.write("import java.math.BigDecimal;");
        bw.newLine();
      }

      bw.newLine();
      // 生成类注释
      BuildComment.createClassComment(bw, tableInfo.getComment());

      bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
      bw.newLine();

      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        BuildComment.createFieldComment(bw, fieldInfo.getCommnet());
        bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getFieldName() + ";");
        bw.newLine();
        bw.newLine();
      }

      bw.write("}");
      bw.flush();
    } catch (Exception e) {
      logger.error("创建po失败", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          logger.error("关闭流失败", e);
        }
      }
      if (outw != null) {
        try {
          outw.close();
        } catch (IOException e) {
          logger.error("关闭流失败", e);
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          logger.error("关闭流失败", e);
        }
      }
    }
  }
}

package com.healwrap.generator.builder;

import com.healwrap.generator.bean.Constants;
import com.healwrap.generator.bean.FieldInfo;
import com.healwrap.generator.bean.TableInfo;
import com.healwrap.generator.utils.PropertiesUtils;
import com.healwrap.generator.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * &#064;ClassName  BuildTable
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 13:32
 * &#064;Created  by admin
 */
public class BuildTable {
  private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
  private static Connection conn = null;

  private static final String SQL_SHOW_TABLE_STATUS = "show table status";
  private static final String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
  private static final String SQL_SHOW_TABLE_INDEX = "show index from %s";


  static {
    String driverName = PropertiesUtils.getProperty("db.driver.name").toString();
    String url = PropertiesUtils.getProperty("db.url").toString();
    String username = PropertiesUtils.getProperty("db.username").toString();
    String password = PropertiesUtils.getProperty("db.password").toString();
    try {
      Class.forName(driverName);
      conn = DriverManager.getConnection(url, username, password);
    } catch (Exception e) {
      logger.error("数据库连接失败", e);
    }

  }

  public static List<TableInfo> getTables() {
    PreparedStatement ps = null;
    ResultSet tableResult = null;

    List<TableInfo> tableInfoList = new ArrayList<>();

    try {
      ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
      tableResult = ps.executeQuery();
      while (tableResult.next()) {
        String tableName = tableResult.getString("Name");
        String tableComment = tableResult.getString("Comment");

        String beanName = tableName;
        if (Constants.IGNORE_TABLE_PREFIX) {
          beanName = tableName.substring(beanName.indexOf("_") + 1);
        }

        beanName = processField(beanName, true);

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setBeanName(beanName);
        tableInfo.setComment(tableComment);
        tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);
        readFieldInfo(tableInfo);
        getKeyIndexInfo(tableInfo);
        tableInfoList.add(tableInfo);
      }
    } catch (Exception e) {
      logger.error("获取表信息失败", e);
    } finally {
      if (tableResult != null) {
        try {
          tableResult.close();
        } catch (Exception e) {
          logger.error("关闭结果集失败", e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (Exception e) {
          logger.error("关闭PreparedStatement失败", e);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
          logger.error("关闭数据库连接失败", e);
        }
      }
    }
    return tableInfoList;
  }

  private static void readFieldInfo(TableInfo tableInfo) {
    PreparedStatement ps = null;
    ResultSet fieldResult = null;

    List<FieldInfo> fieldInfoList = new ArrayList<>();

    try {
      ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
      fieldResult = ps.executeQuery();

      boolean haveDateTime = false;
      boolean havaDate = false;
      boolean havaHaveBigDecimal = false;

      while (fieldResult.next()) {
        String field = fieldResult.getString("field");
        String type = fieldResult.getString("type");
        String extra = fieldResult.getString("extra");
        String comment = fieldResult.getString("comment");
        if (type.indexOf("(") > 0) {
          type = type.substring(0, type.indexOf("("));
        }

        String propertyName = processField(field, false);

        FieldInfo fieldInfo = new FieldInfo();
        fieldInfoList.add(fieldInfo);
        fieldInfo.setFieldName(field);
        fieldInfo.setCommnet(comment);
        fieldInfo.setSqlType(type);
        fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
        fieldInfo.setPropertyName(propertyName);
        fieldInfo.setJavaType(processJavaType(type));

        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
          haveDateTime = true;
        }
        if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
          havaDate = true;
        }
        if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
          havaHaveBigDecimal = true;
        }
      }
      tableInfo.setHaveDate(havaDate);
      tableInfo.setHaveDateTime(haveDateTime);
      tableInfo.setHaveBigDecimal(havaHaveBigDecimal);
      tableInfo.setFieldList(fieldInfoList);
    } catch (Exception e) {
      logger.error("获取表信息失败", e);
    } finally {
      if (fieldResult != null) {
        try {
          fieldResult.close();
        } catch (Exception e) {
          logger.error("关闭结果集失败", e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (Exception e) {
          logger.error("关闭PreparedStatement失败", e);
        }
      }
    }
  }

  private static void getKeyIndexInfo(TableInfo tableInfo) {
    PreparedStatement ps = null;
    ResultSet fieldResult = null;

    List<FieldInfo> fieldInfoList = new ArrayList<>();

    try {

      Map<String, FieldInfo> tempMap = new HashMap<>();
      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        tempMap.put(fieldInfo.getFieldName(), fieldInfo);
      }

      ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
      fieldResult = ps.executeQuery();
      while (fieldResult.next()) {
        String keyName = fieldResult.getString("key_name");
        int nonUnique = fieldResult.getInt("non_unique");
        String columnName = fieldResult.getString("column_name");
        if (nonUnique == 1) {
          continue;
        }
        List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
        if (null == keyFieldList) {
          keyFieldList = new ArrayList<>();
          tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
        }
        keyFieldList.add(tempMap.get(columnName));
      }
    } catch (Exception e) {
      logger.error("获取索引失败", e);
    } finally {
      if (fieldResult != null) {
        try {
          fieldResult.close();
        } catch (Exception e) {
          logger.error("关闭结果集失败", e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (Exception e) {
          logger.error("关闭PreparedStatement失败", e);
        }
      }
    }
  }

  private static String processField(String field, Boolean upCaseFistLetter) {
    StringBuilder sb = new StringBuilder();
    String[] fields = field.split("_");
    sb.append(upCaseFistLetter ? StringUtils.upperCaseFirstLetter(fields[0]) : fields[0]);
    for (int i = 1, len = fields.length; i < len; i++) {
      sb.append(StringUtils.upperCaseFirstLetter(fields[i]));
    }
    return sb.toString();
  }

  private static String processJavaType(String type) {
    if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
      return "Integer";
    } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
      return "Long";
    } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
      return "String";
    } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
      return "Date";
    } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
      return "BigDecimal";
    } else {
      throw new RuntimeException("未知的数据类型" + type);
    }
  }
}

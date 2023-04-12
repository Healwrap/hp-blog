package com.easyjava;

import com.easyjava.bean.TableInfo;
import com.easyjava.builder.BuildPo;
import com.easyjava.builder.BuildTable;
import com.easyjava.utils.JsonUtils;

import java.util.List;

/**
 * &#064;ClassName  RunApplication
 * &#064;Description  TODO
 * &#064;Date  2023/4/10 17:22
 * &#064;Created  by admin
 */
public class RunApplication {
  public static void main(String[] args) {
    List<TableInfo> tables = BuildTable.getTables();
    for (TableInfo table : tables) {
      BuildPo.execute(table);
    }
  }
}

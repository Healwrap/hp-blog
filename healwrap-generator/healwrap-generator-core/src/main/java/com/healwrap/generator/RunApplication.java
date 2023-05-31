package com.healwrap.generator;

import com.healwrap.generator.bean.TableInfo;
import com.healwrap.generator.builder.BuildPo;
import com.healwrap.generator.builder.BuildTable;

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

package com.healwrap.utils;

import com.healwrap.entity.dto.SysSettingDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pepedd
 * @ClassName SysCacheUtils
 * @Description 系统缓存工具类
 * @Date 2023/4/12 14:09
 */
public class SysCacheUtils {
  private static final String KEY_SYS = "sys_setting";
  private static final Map<String, SysSettingDto> CACHE_DATA = new ConcurrentHashMap<>();

  public static SysSettingDto getSysSetting() {
    return CACHE_DATA.get(KEY_SYS);
  }

  public static void refresh(SysSettingDto sysSettingDto) {
    CACHE_DATA.put(KEY_SYS, sysSettingDto);
  }
}

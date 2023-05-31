package com.healwrap.service;

import com.healwrap.entity.dto.SysSettingDto;
import com.healwrap.entity.po.SysSetting;
import com.healwrap.entity.query.SysSettingQuery;
import com.healwrap.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 系统设置信息 业务接口
 */
public interface SysSettingService {

  /**
   * 根据条件查询列表
   */
  List<SysSetting> findListByParam(SysSettingQuery param);

  /**
   * 根据条件查询列表
   */
  Integer findCountByParam(SysSettingQuery param);

  /**
   * 分页查询
   */
  PaginationResultVO<SysSetting> findListByPage(SysSettingQuery param);

  /**
   * 新增
   */
  Integer add(SysSetting bean);

  /**
   * 批量新增
   */
  Integer addBatch(List<SysSetting> listBean);

  /**
   * 批量新增/修改
   */
  Integer addOrUpdateBatch(List<SysSetting> listBean);

  /**
   * 根据Code查询对象
   */
  SysSetting getSysSettingByCode(String code);


  /**
   * 根据Code修改
   */
  Integer updateSysSettingByCode(SysSetting bean, String code);


  /**
   * 根据Code删除
   */
  Integer deleteSysSettingByCode(String code);

  /**
   * 刷新系统设置缓存
   */
  SysSettingDto refreshCache();

  /**
   * 保存系统设置
   */
  void saveSetting(SysSettingDto sysSettingDto);
}

package com.healwrap.service.impl;

import com.healwrap.entity.dto.SysSettingDto;
import com.healwrap.entity.enums.PageSize;
import com.healwrap.entity.enums.SysSettingCodeEnum;
import com.healwrap.entity.po.SysSetting;
import com.healwrap.entity.query.SimplePage;
import com.healwrap.entity.query.SysSettingQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.mappers.SysSettingMapper;
import com.healwrap.service.SysSettingService;
import com.healwrap.utils.JsonUtils;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 系统设置信息 业务接口实现
 */
@Service("sysSettingService")
public class SysSettingServiceImpl implements SysSettingService {
  private static final Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);
  @Resource
  private SysSettingMapper<SysSetting, SysSettingQuery> sysSettingMapper;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<SysSetting> findListByParam(SysSettingQuery param) {
    return this.sysSettingMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(SysSettingQuery param) {
    return this.sysSettingMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<SysSetting> findListByPage(SysSettingQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<SysSetting> list = this.findListByParam(param);
    PaginationResultVO<SysSetting> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(SysSetting bean) {
    return this.sysSettingMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<SysSetting> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.sysSettingMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<SysSetting> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.sysSettingMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据Code获取对象
   */
  @Override
  public SysSetting getSysSettingByCode(String code) {
    return this.sysSettingMapper.selectByCode(code);
  }

  /**
   * 根据Code修改
   */
  @Override
  public Integer updateSysSettingByCode(SysSetting bean, String code) {
    return this.sysSettingMapper.updateByCode(bean, code);
  }

  /**
   * 根据Code删除
   */
  @Override
  public Integer deleteSysSettingByCode(String code) {
    return this.sysSettingMapper.deleteByCode(code);
  }

  /**
   * 刷新缓存，获取所有的系统设置信息
   */
  @Override
  public SysSettingDto refreshCache() {
    try {
      SysSettingDto sysSettingDto = new SysSettingDto();
      List<SysSetting> sysSettingList = this.sysSettingMapper.selectList(new SysSettingQuery());
      for (SysSetting sysSetting : sysSettingList) {
        String jsonContent = sysSetting.getJsonContent();
        if (StringTools.isEmpty(jsonContent)) {
          continue;
        }
        String code = sysSetting.getCode();
        SysSettingCodeEnum sysSettingCodeEnum = SysSettingCodeEnum.getByCode(code);
        // 反射
        PropertyDescriptor pd = new PropertyDescriptor(sysSettingCodeEnum.getPropName(), SysSettingDto.class);
        Method method = pd.getWriteMethod();
        Class subClass = Class.forName(sysSettingCodeEnum.getClazz());
        method.invoke(sysSettingDto, JsonUtils.convertJson2Object(jsonContent, subClass));
      }
      SysCacheUtils.refresh(sysSettingDto);
      return sysSettingDto;
    } catch (Exception e) {
      logger.error("刷新缓存失败", e);
      throw new BusinessException("刷新缓存失败");
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveSetting(SysSettingDto sysSettingDto) {
    try {
      Class clazz = SysSettingDto.class;
      for (SysSettingCodeEnum codeEnum : SysSettingCodeEnum.values()) {
        PropertyDescriptor pd = new PropertyDescriptor(codeEnum.getPropName(), clazz);
        Method method = pd.getReadMethod();
        Object obj = method.invoke(sysSettingDto);
        SysSetting sysSetting = new SysSetting();
        sysSetting.setCode(codeEnum.getCode());
        sysSetting.setJsonContent(JsonUtils.convertObject2Json(obj));
        this.sysSettingMapper.insertOrUpdate(sysSetting);
      }
    } catch (Exception e) {
      logger.error("保存系统设置失败", e);
      throw new BusinessException("保存系统设置失败");
    }
  }
}


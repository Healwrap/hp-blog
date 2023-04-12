package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.UserStatusEnum;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.SimplePage;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mappers.UserInfoMapper;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

  @Resource
  private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
  @Resource
  private EmailCodeService emailCodeService;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<UserInfo> findListByParam(UserInfoQuery param) {
    return this.userInfoMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(UserInfoQuery param) {
    return this.userInfoMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<UserInfo> list = this.findListByParam(param);
    PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(UserInfo bean) {
    return this.userInfoMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<UserInfo> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.userInfoMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<UserInfo> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.userInfoMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据UserId获取对象
   */
  @Override
  public UserInfo getUserInfoByUserId(String userId) {
    return this.userInfoMapper.selectByUserId(userId);
  }

  /**
   * 根据UserId修改
   */
  @Override
  public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
    return this.userInfoMapper.updateByUserId(bean, userId);
  }

  /**
   * 根据UserId删除
   */
  @Override
  public Integer deleteUserInfoByUserId(String userId) {
    return this.userInfoMapper.deleteByUserId(userId);
  }

  /**
   * 根据Email获取对象
   */
  @Override
  public UserInfo getUserInfoByEmail(String email) {
    return this.userInfoMapper.selectByEmail(email);
  }

  /**
   * 根据Email修改
   */
  @Override
  public Integer updateUserInfoByEmail(UserInfo bean, String email) {
    return this.userInfoMapper.updateByEmail(bean, email);
  }

  /**
   * 根据Email删除
   */
  @Override
  public Integer deleteUserInfoByEmail(String email) {
    return this.userInfoMapper.deleteByEmail(email);
  }

  /**
   * 根据NickName获取对象
   */
  @Override
  public UserInfo getUserInfoByNickName(String nickName) {
    return this.userInfoMapper.selectByNickName(nickName);
  }

  /**
   * 根据NickName修改
   */
  @Override
  public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
    return this.userInfoMapper.updateByNickName(bean, nickName);
  }

  /**
   * 根据NickName删除
   */
  @Override
  public Integer deleteUserInfoByNickName(String nickName) {
    return this.userInfoMapper.deleteByNickName(nickName);
  }

  @Override
  public void register(String email, String emailCode, String nickName, String password) {
    UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
    if (userInfo != null) {
      throw new BusinessException("邮箱已经被注册");
    }
    userInfo = this.userInfoMapper.selectByNickName(nickName);
    if (userInfo != null) {
      throw new BusinessException("昵称已存在");
    }
    emailCodeService.checkEmailCode(email, emailCode);
    // 创建用户
    String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
    UserInfo insertInfo = new UserInfo();
    insertInfo.setUserId(userId);
    insertInfo.setEmail(email);
    insertInfo.setNickName(nickName);
    insertInfo.setPassword(StringTools.encodeMd5(password));
    insertInfo.setJoinTime(new Date());
    insertInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
    insertInfo.setTotalIntegral(Constants.ZERO);
    insertInfo.setCurrentIntegral(Constants.ZERO);
    this.userInfoMapper.insert(insertInfo);
  }

}

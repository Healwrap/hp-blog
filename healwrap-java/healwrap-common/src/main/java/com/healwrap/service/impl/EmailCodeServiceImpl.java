package com.healwrap.service.impl;

import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.enums.PageSize;
import com.healwrap.entity.po.EmailCode;
import com.healwrap.entity.po.UserInfo;
import com.healwrap.entity.query.EmailCodeQuery;
import com.healwrap.entity.query.SimplePage;
import com.healwrap.entity.query.UserInfoQuery;
import com.healwrap.entity.vo.PaginationResultVO;
import com.healwrap.exception.BusinessException;
import com.healwrap.framework.config.WebConfig;
import com.healwrap.mappers.EmailCodeMapper;
import com.healwrap.mappers.UserInfoMapper;
import com.healwrap.service.EmailCodeService;
import com.healwrap.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;


/**
 * 邮箱验证码 业务接口实现
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

  private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

  @Resource
  private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

  @Resource
  private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
  @Resource
  private JavaMailSender javaMailSender;
  @Resource
  private WebConfig webConfig;

  /**
   * 根据条件查询列表
   */
  @Override
  public List<EmailCode> findListByParam(EmailCodeQuery param) {
    return this.emailCodeMapper.selectList(param);
  }

  /**
   * 根据条件查询列表
   */
  @Override
  public Integer findCountByParam(EmailCodeQuery param) {
    return this.emailCodeMapper.selectCount(param);
  }

  /**
   * 分页查询方法
   */
  @Override
  public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
    int count = this.findCountByParam(param);
    int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

    SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
    param.setSimplePage(page);
    List<EmailCode> list = this.findListByParam(param);
    PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    return result;
  }

  /**
   * 新增
   */
  @Override
  public Integer add(EmailCode bean) {
    return this.emailCodeMapper.insert(bean);
  }

  /**
   * 批量新增
   */
  @Override
  public Integer addBatch(List<EmailCode> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.emailCodeMapper.insertBatch(listBean);
  }

  /**
   * 批量新增或者修改
   */
  @Override
  public Integer addOrUpdateBatch(List<EmailCode> listBean) {
    if (listBean == null || listBean.isEmpty()) {
      return 0;
    }
    return this.emailCodeMapper.insertOrUpdateBatch(listBean);
  }

  /**
   * 根据EmailAndCode获取对象
   */
  @Override
  public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
    return this.emailCodeMapper.selectByEmailAndCode(email, code);
  }

  /**
   * 根据EmailAndCode修改
   */
  @Override
  public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
    return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
  }

  /**
   * 根据EmailAndCode删除
   */
  @Override
  public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
    return this.emailCodeMapper.deleteByEmailAndCode(email, code);
  }

  /**
   * 验证邮箱，发送验证码
   *
   * @param email 邮箱
   * @param type  验证码类型
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void sendEmailCode(String email, Integer type) {
    if (type == 0) {
      UserInfo userInfo = userInfoMapper.selectByEmail(email);
      if (userInfo != null) {
        throw new BusinessException("邮箱已被注册");
      }
    }
    String code = StringTools.getRandomString(Constants.LENGTH_5);
    sendEmailCodeDo(email, code);
    // 将之前的验证码失效
    emailCodeMapper.disableEmailCode(email);
    EmailCode emailCode = new EmailCode();
    emailCode.setCode(code);
    emailCode.setEmail(email);
    emailCode.setStatus(0);
    emailCode.setCreateTime(new Date());
    emailCodeMapper.insert(emailCode);
  }

  /**
   * 验证邮箱验证码
   *
   * @param email     邮箱
   * @param emailCode 验证码
   */
  @Override
  public void checkEmailCode(String email, String emailCode) {
    EmailCode dbInfo = this.emailCodeMapper.selectByEmailAndCode(email, emailCode);
    if (null == dbInfo) {
      throw new BusinessException("邮箱验证码错误");
    }
    if (dbInfo.getStatus() != 0 || System.currentTimeMillis() - dbInfo.getCreateTime().getTime() > 1000 * 60 * Constants.LENGTH_5) {
      throw new BusinessException("验证码已失效");
    }
    emailCodeMapper.disableEmailCode(email);
  }

  /**
   * 发送邮件
   *
   * @param toEmail 邮箱
   * @param code    验证码
   */
  private void sendEmailCodeDo(String toEmail, String code) {

    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      // 邮件发送人
      helper.setFrom(webConfig.getSendUserName());
      // 邮件接收人
      helper.setTo(toEmail);
      helper.setSubject("邮箱验证码");
      helper.setText("您的验证码为：" + code + "，请在5分钟内使用。");
      helper.setSentDate(new Date());
      javaMailSender.send(message);
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
      throw new BusinessException("发送邮件失败");
    }


  }
}

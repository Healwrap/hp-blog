package com.easybbs.entity.annotation;

import com.easybbs.entity.enums.UserOperFrequencyTypeEnum;

import java.lang.annotation.*;

/**
 * @author pepedd
 * @ClassName GloabalIntercepter
 * @Description TODO
 * @Date 2023/4/13 9:58
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GlobalIntercepter {
  /**
   * 是否需要登录
   */
  boolean checkLogin() default false;

  /**
   * 是否需要校验参数
   */
  boolean checkParams() default false;

  /**
   * 校验频次
   */
  UserOperFrequencyTypeEnum frequencyType() default UserOperFrequencyTypeEnum.NO_CHECK;
}

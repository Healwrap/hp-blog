package com.easybbs.entity.annotation;

import java.lang.annotation.*;

/**
 * @ClassName GloabalIntercepter
 * @Description TODO
 * @Date 2023/4/13 9:58
 * @Created by admin
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GlobalIntercepter {
  /**
   * 是否需要登录
   * @return true 需要登录
   */
  boolean checkLogin() default false;

  /**
   * 是否需要校验参数
   * @return true 需要校验参数
   */
  boolean checkParams() default false;

}

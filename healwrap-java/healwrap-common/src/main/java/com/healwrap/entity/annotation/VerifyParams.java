package com.healwrap.entity.annotation;

import com.healwrap.entity.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName VerifyParams
 * @Description TODO
 * @Date 2023/4/13 10:25
 * @author pepedd
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParams {
  boolean required() default false;

  int max() default -1;

  int min() default -1;

  /**
   * 正则表达式
   */
  VerifyRegexEnum regex() default VerifyRegexEnum.NO;
}

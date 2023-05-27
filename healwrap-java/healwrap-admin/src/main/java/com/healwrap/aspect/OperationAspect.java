package com.healwrap.aspect;

import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.enums.ResponseCodeEnum;
import com.healwrap.exception.BusinessException;
import com.healwrap.service.ForumArticleService;
import com.healwrap.service.ForumCommentService;
import com.healwrap.service.LikeRecordService;
import com.healwrap.utils.StringTools;
import com.healwrap.utils.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * @author pepedd
 * @ClassName OperationAspect
 * @Description AOP切面
 * @Date 2023/4/13 10:03
 */

@Component
@Aspect
public class OperationAspect {
  private static final Logger logger = LoggerFactory.getLogger(OperationAspect.class);

  private static final String[] TYPE_BASE = {"java.lang.String", "java.lang.Integer", "java.lang.Long", "java.lang.Double", "java.lang.Float", "java.lang.Boolean", "java.lang.Short", "java.lang.Byte", "java.lang.Character", "java.lang.Number", "java.lang.Object", "java.util.Date", "java.sql.Date", "java.sql.Timestamp", "java.math.BigDecimal", "java.math.BigInteger"};

  @Resource
  private ForumArticleService forumArticleService;
  @Resource
  private ForumCommentService forumCommentService;
  @Resource
  private LikeRecordService likeRecordService;


  /**
   * 校验参数
   */
  @Pointcut("@annotation(com.healwrap.entity.annotation.GlobalIntercepter)")
  private void requestIntercepter() {
  }

  /**
   * 校验参数
   *
   * @param pjp 切点
   * @return Object对象
   */
  @Around("requestIntercepter()")
  public Object requestIntercepterDo(ProceedingJoinPoint pjp) {
    try {
      Object target = pjp.getTarget();
      Object[] args = pjp.getArgs();
      String methodName = pjp.getSignature().getName();
      Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
      Method method = target.getClass().getMethod(methodName, parameterTypes);
      GlobalIntercepter intercepter = method.getAnnotation(GlobalIntercepter.class);
      if (null == intercepter) {
        return null;
      }
      /* 校验参数 */
      if (intercepter.checkParams()) {
        validateParams(method, args);
      }
      Object result = pjp.proceed();
      return result;
    } catch (BusinessException e) {
      logger.error("全局拦截器异常", e);
      throw e;
    } catch (Throwable e) {
      logger.error("全局拦截器异常", e);
      throw new BusinessException(ResponseCodeEnum.CODE_500);
    }
  }

  /**
   * 校验参数
   *
   * @param method 方法
   * @param args   参数
   */
  private void validateParams(Method method, Object[] args) {
    Parameter[] parameters = method.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      Object value = args[i];

      VerifyParams verifyParams = parameter.getAnnotation(VerifyParams.class);
      if (null == verifyParams) {
        continue;
      }
      if (ArrayUtils.contains(TYPE_BASE, parameter.getType().getName())) {
        checkValue(value, verifyParams);
      } else {
        // TODO
      }
    }
  }

  /**
   * 校验参数
   *
   * @param value        参数值
   * @param verifyParams 注解
   */
  private void checkValue(Object value, VerifyParams verifyParams) {
    boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
    int length = value == null ? 0 : value.toString().length();
    /* 校验是否为空 */
    if (isEmpty && verifyParams.required()) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    /* 校验长度 */
    if (verifyParams.max() != -1 && length > verifyParams.max() || verifyParams.min() != -1 && length < verifyParams.min()) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    /* 校验正则 */
    if (!StringTools.isEmpty(verifyParams.regex().getRegex()) && !VerifyUtils.verify(verifyParams.regex(), value.toString())) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
  }

  /**
   * 校验对象
   *
   * @param parameter 参数
   * @param value     参数值
   */
  private void checkObject(Parameter parameter, Object value) {
    try {
      String typeName = parameter.getParameterizedType().getTypeName();
      Class<?> clazz = Class.forName(typeName);
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
        VerifyParams fieldVerifyParams = field.getAnnotation(VerifyParams.class);
        if (null == fieldVerifyParams) {
          continue;
        }
        field.setAccessible(true);
        Object resultValue = field.get(value);
        checkValue(resultValue, fieldVerifyParams);
      }
    } catch (Exception e) {
      logger.error("校验参数异常", e);
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
  }

}

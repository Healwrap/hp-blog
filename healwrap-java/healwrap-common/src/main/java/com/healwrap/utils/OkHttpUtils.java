package com.healwrap.utils;


import com.healwrap.exception.BusinessException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author pepedd
 * @ClassName OkHttpUtils
 * @Description okhttp工具类
 * @Date 2023/4/13 11:48
 */

public class OkHttpUtils {

  /**
   * 请求超时
   */
  private static final int TIME_OUT_SECONDS = 5;

  private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

  private static OkHttpClient.Builder getClientBuilder() {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().followRedirects(false).retryOnConnectionFailure(false);
    clientBuilder.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS).readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS).writeTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS);
    return clientBuilder;
  }

  private static Request.Builder getRequestBuilder(Map<String, String> headers) {
    Request.Builder requestBuilder = new Request.Builder();
    if (headers != null) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        requestBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }
    return requestBuilder;
  }

  private static FormBody.Builder getBuilder(Map<String, String> params) {
    FormBody.Builder builder = new FormBody.Builder();
    if (params != null) {
      for (Map.Entry<String, String> entry : params.entrySet()) {
        builder.add(entry.getKey(), entry.getValue());
      }
    }
    return builder;
  }

  public static String getRequest(String url) throws BusinessException {
    ResponseBody responseBody = null;
    try {
      OkHttpClient.Builder clientBuilder = getClientBuilder();
      Request.Builder requestBuilder = getRequestBuilder(null);
      OkHttpClient client = clientBuilder.build();
      Request request = requestBuilder.url(url).build();
      Response response = client.newCall(request).execute();
      responseBody = response.body();
      return responseBody.string();
    } catch (SocketTimeoutException | ConnectException e) {
      logger.error("请求超时", e);
      throw new BusinessException("请求超时");
    } catch (Exception e) {
      logger.error("请求异常", e);
      throw new BusinessException("请求异常");
    } finally {
      if (responseBody != null) {
        responseBody.close();
      }
    }
  }
}


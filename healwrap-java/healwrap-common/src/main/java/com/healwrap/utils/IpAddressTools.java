package com.healwrap.utils;

import com.healwrap.entity.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

/**
 * @author pepedd
 * @ClassName IpAddressTools
 * @Description TODO
 * @Date 2023/5/27 10:07
 */
public class IpAddressTools {
  private static final Logger logger = LoggerFactory.getLogger(IpAddressTools.class);

  /**
   * 获取位置信息
   *
   * @param ip ip地址
   * @return 位置信息
   */
  public static String getIpAddress(String ip) {
    // 使用正则判断是否是本地IP
    if (ip.matches(Constants.LOCAL_IP_REGEX)) {
      return Constants.LOCAL_ADDRESS;
    }
    try {
      String url = "http://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
      String responseJson = OkHttpUtils.getRequest(url);
      if (null == responseJson) {
        return Constants.NO_ADDRESS;
      }
      Map<String, String> addressInfo = JsonUtils.convertJson2Object(responseJson, Map.class);
      return addressInfo.get("addr");
    } catch (Exception e) {
      logger.error("获取ip地址异常");
    }
    return Constants.NO_ADDRESS;
  }
}

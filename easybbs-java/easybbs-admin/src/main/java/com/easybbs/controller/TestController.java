package com.easybbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Date 2023/4/9 10:58
 * @Created by admin
 */
@RestController
public class TestController {
  @RequestMapping("/test")
  public String test() {
    return "test admin";
  }
}

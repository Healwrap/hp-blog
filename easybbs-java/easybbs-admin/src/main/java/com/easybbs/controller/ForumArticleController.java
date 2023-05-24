package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pepedd
 * @ClassName ForumArticleController
 * @Description TODO
 * @Date 2023/5/25 7:06
 */
@RestController
@RequestMapping("forum")
public class ForumArticleController extends ABaseController {
  private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);

  @PostMapping("loadArticleList")
  public ResponseVO loadArticleList(ForumArticleQuery articleQuery) {
    return getSuccessResponseVO(null);
  }
}

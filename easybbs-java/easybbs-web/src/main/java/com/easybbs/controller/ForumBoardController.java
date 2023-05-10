package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.service.ForumBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName ForumBoardController
 * @Description 论坛板块控制器
 * @Date 2023/4/23 9:12
 * @author pepedd
 */
@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {
  @Resource
  private ForumBoardService forumBoardService;

  /**
   * 加载板块树
   * @return 板块树
   */
  @GetMapping("loadBoard")
  public ResponseVO loadBoard() {
    return getSuccessResponseVO(forumBoardService.getBoardTree(null));
  }
}

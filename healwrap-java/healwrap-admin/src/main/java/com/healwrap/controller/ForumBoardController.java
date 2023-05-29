package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.ForumBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pepedd
 * @ClassName ForumBoardController
 * @Description TODO
 * @Date 2023/5/25 10:01
 */
@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {
  @Resource
  private ForumBoardService forumBoardService;

  @GetMapping("/loadBoard")
  public ResponseVO loadBoard() {
    return getSuccessResponseVO(forumBoardService.getBoardTree(null));
  }
}

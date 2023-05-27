package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.dto.SessionWebUserDto;
import com.healwrap.entity.po.ForumBoard;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.ForumBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author pepedd
 * @ClassName ForumBoardController
 * @Description 论坛板块控制器
 * @Date 2023/4/23 9:12
 */
@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {
  @Resource
  private ForumBoardService forumBoardService;

  /**
   * 加载板块树
   *
   * @return 板块树
   */
  @GetMapping("loadBoard")
  public ResponseVO loadBoard() {
    return getSuccessResponseVO(forumBoardService.getBoardTree(null));
  }

  /**
   * 发布文章时加载板块
   *
   * @param session 会话
   * @return
   */

  @GetMapping("/loadBoard4Post")
  @GlobalIntercepter(checkLogin = true)
  public ResponseVO loadBoard4Post(HttpSession session) {
    SessionWebUserDto webUserDto = getUserInfoFromSession(session);
    Integer postType = null;
    if (!webUserDto.getIsAdmin()) {
      postType = 1;
    }
    List<ForumBoard> list = forumBoardService.getBoardTree(postType);
    return getSuccessResponseVO(list);
  }
}

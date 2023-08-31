package com.healwrap.controller;

import com.healwrap.controller.base.ABaseController;
import com.healwrap.entity.annotation.GlobalIntercepter;
import com.healwrap.entity.annotation.VerifyParams;
import com.healwrap.entity.constants.Constants;
import com.healwrap.entity.dto.FileUploadDto;
import com.healwrap.entity.enums.file.FileUploadTypeEnum;
import com.healwrap.entity.po.ForumBoard;
import com.healwrap.entity.vo.ResponseVO;
import com.healwrap.service.ForumBoardService;
import com.healwrap.utils.file.FileUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  @Resource
  private FileUtils fileUtils;

  /**
   * 加载板块树
   *
   * @return ResponseVO
   */
  @GetMapping("/loadBoard")
  public ResponseVO loadBoard() {
    return getSuccessResponseVO(forumBoardService.getBoardTree(null));
  }

  /**
   * 保存板块
   *
   * @param boardId   板块id
   * @param pBoardId  父板块id
   * @param boardName 板块名称
   * @param boardDesc 板块描述
   * @param postType  发帖类型
   * @param cover     板块封面
   * @return ResponseVO
   */
  @PatchMapping("/saveBoard")
  @CacheEvict(value = {"boardTree", "boardTree4Post"}, allEntries = true)
  @GlobalIntercepter(checkParams = true)
  public ResponseVO saveBoard(Integer boardId,
                              @VerifyParams(required = true) Integer pBoardId,
                              @VerifyParams(required = true) String boardName,
                              String boardDesc,
                              Integer postType,
                              MultipartFile cover) {
    ForumBoard forumBoard = new ForumBoard();
    forumBoard.setBoardId(boardId);
    forumBoard.setPBoardId(pBoardId);
    forumBoard.setBoardName(boardName);
    forumBoard.setBoardDesc(boardDesc);
    forumBoard.setPostType(postType);
    if (cover != null) {
      FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
      forumBoard.setCover(fileUploadDto.getLocalPath());
    }
    forumBoardService.saveBoard(forumBoard);
    return getSuccessResponseVO(null);
  }

  /**
   * 删除板块
   *
   * @param boardId 板块id
   * @return ResponseVO
   */
  @DeleteMapping("/deleteBoard")
  @CacheEvict(value = {"boardTree", "boardTree4Post"}, allEntries = true)
  @GlobalIntercepter(checkParams = true)
  public ResponseVO deleteBoard(@VerifyParams(required = true) Integer boardId) {
    forumBoardService.deleteForumBoardByBoardId(boardId);
    return getSuccessResponseVO(null);
  }

  /**
   * 修改板块排序
   *
   * @param boardIds 板块id数组
   * @return
   */
  @PatchMapping("/changeBoardSort")
  @CacheEvict(value = {"boardTree", "boardTree4Post"}, allEntries = true)
  @GlobalIntercepter(checkParams = true)
  public ResponseVO changeBoardSort(@VerifyParams(required = true) String boardIds) {
    forumBoardService.changeBoardSort(boardIds);
    return getSuccessResponseVO(null);
  }
}

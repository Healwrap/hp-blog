package com.easybbs.entity.query;

import lombok.Data;

import java.util.List;

/**
 * 评论参数
 */
@Data
public class ForumCommentQuery extends BaseParam {


  /**
   * 评论ID
   */
  private Integer commentId;

  /**
   * 父级评论ID
   */
  private Integer pCommentId;

  /**
   * 文章ID
   */
  private String articleId;

  private String articleIdFuzzy;

  /**
   * 回复内容
   */
  private String content;

  private String contentFuzzy;

  /**
   * 图片
   */
  private String imgPath;

  private String imgPathFuzzy;

  /**
   * 用户ID
   */
  private String userId;

  private String userIdFuzzy;

  /**
   * 昵称
   */
  private String nickName;

  private String nickNameFuzzy;

  /**
   * 用户ip地址
   */
  private String userIpAddress;

  private String userIpAddressFuzzy;

  /**
   * 回复人ID
   */
  private String replyUserId;

  private String replyUserIdFuzzy;

  /**
   * 回复人昵称
   */
  private String replyNickName;

  private String replyNickNameFuzzy;

  /**
   * 0:未置顶  1:置顶
   */
  private Integer topType;

  /**
   * 发布时间
   */
  private String postTime;

  private String postTimeStart;

  private String postTimeEnd;

  /**
   * good数量
   */
  private Integer goodCount;

  /**
   * 0:待审核  1:已审核
   */
  private Integer status;
  private Boolean loadChildren;
  private String currentUserId;
  private Boolean queryLikeType;
  private List<Integer> pCommentIdList;
}

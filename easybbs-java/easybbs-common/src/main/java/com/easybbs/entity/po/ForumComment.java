package com.easybbs.entity.po;

import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 评论
 */
@Data
public class ForumComment implements Serializable {


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

  /**
   * 回复内容
   */
  private String content;

  /**
   * 图片
   */
  private String imgPath;

  /**
   * 用户ID
   */
  private String userId;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 用户ip地址
   */
  private String userIpAddress;

  /**
   * 回复人ID
   */
  private String replyUserId;

  /**
   * 回复人昵称
   */
  private String replyNickName;

  /**
   * 0:未置顶  1:置顶
   */
  private Integer topType;

  /**
   * 发布时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date postTime;

  /**
   * good数量
   */
  private Integer goodCount;

  /**
   * 0:待审核  1:已审核
   */
  private Integer status;
  /**
   * 点赞类型 0:未点赞 1:已点赞
   */
  private Integer likeType;
  private List<ForumComment> children;
}

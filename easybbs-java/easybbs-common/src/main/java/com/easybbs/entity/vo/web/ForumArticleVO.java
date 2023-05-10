package com.easybbs.entity.vo.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ForumArticleVO
 * @Description 文章信息
 * @Date 2023/4/25 0:35
 * @author pepedd
 */
@Data
public class ForumArticleVO implements Serializable {
  /**
   * 文章ID
   */
  private String articleId;
  /**
   * 板块ID
   */
  private Integer boardId;
  /**
   * 板块名称
   */
  private String boardName;
  /**
   * 父级板块ID
   */
  private Integer pBoardId;
  /**
   * 父级板块名称
   */
  private String pBoardName;
  /**
   * 用户ID
   */
  private String userId;
  /**
   * 昵称
   */
  private String nickName;
  /**
   * 最后登录的IP地址
   */
  private String userIpAddress;
  /**
   * 标题
   */
  private String title;
  /**
   * 封面
   */
  private String cover;
  /**
   * 内容
   */
  private String content;
  /**
   * 摘要
   */
  private String summary;
  /**
   * 发布时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date postTime;
  /**
   * 阅读数量
   */
  private Integer readCount;
  /**
   * 点赞量
   */
  private Integer goodCount;
  /**
   * 评论数
   */
  private Integer commentCount;
  /**
   * 0:未置顶 1:置顶
   */
  private Integer topType;
  /**
   * 0:没有附件 1:有附件
   */
  private Integer attachmentType;
  /**
   * -1:已删除 0:待审核 1:审核通过
   */
  private Integer status;

}

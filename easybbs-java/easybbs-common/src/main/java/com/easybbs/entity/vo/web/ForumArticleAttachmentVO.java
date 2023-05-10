package com.easybbs.entity.vo.web;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ForumArticleAttachmentVO
 * @Description TODO
 * @Date 2023/4/27 23:04
 * @author pepedd
 */
@Data
public class ForumArticleAttachmentVO implements Serializable {
  /**
   * 文件id
   */
  private String fileId;
  /**
   * 文件大小
   */
  private Long fileSize;
  /**
   * 文件名
   */
  private String fileName;
  /**
   * 下载次数
   */
  private Integer downloadCount;
  /**
   * 文件类型
   */
  private Integer fileType;
  /**
   * 下载所需积分
   */
  private Integer integral;

}

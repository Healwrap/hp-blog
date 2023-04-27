package com.easybbs.entity.vo.web;

import java.io.Serializable;

/**
 * @ClassName ForumArticleAttachmentVO
 * @Description TODO
 * @Date 2023/4/27 23:04
 * @Created by admin
 */
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

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(Integer downloadCount) {
    this.downloadCount = downloadCount;
  }

  public Integer getFileType() {
    return fileType;
  }

  public void setFileType(Integer fileType) {
    this.fileType = fileType;
  }

  public Integer getIntegral() {
    return integral;
  }

  public void setIntegral(Integer integral) {
    this.integral = integral;
  }

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

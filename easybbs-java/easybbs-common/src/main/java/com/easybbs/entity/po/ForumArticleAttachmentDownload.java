package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;


/**
 *
 * 用户附件下载
 *
 */
@Data
public class ForumArticleAttachmentDownload implements Serializable {


	/**
	 * 文件ID
	 */
	private String fileId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 文章ID
	 */
	private String articleId;

	/**
	 * 文件下载次数
	 */
	private Integer downloadCount;
}

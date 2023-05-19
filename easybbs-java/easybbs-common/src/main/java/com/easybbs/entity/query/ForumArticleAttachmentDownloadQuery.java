package com.easybbs.entity.query;


import lombok.Data;

/**
 *
 * 用户附件下载参数
 *
 */
@Data
public class ForumArticleAttachmentDownloadQuery extends BaseParam {


	/**
	 * 文件ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
	 * 用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
	 * 文件下载次数
	 */
	private Integer downloadCount;

}

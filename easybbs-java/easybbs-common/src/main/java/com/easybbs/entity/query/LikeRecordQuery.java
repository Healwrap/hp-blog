package com.easybbs.entity.query;

import lombok.Data;

import java.util.Date;


/**
 *
 * 点赞记录参数
 *
 */
@Data
public class LikeRecordQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer opId;

	/**
	 * 操作类型0:文章点赞 1:评论点赞
	 */
	private Integer opType;

	/**
	 * 主体ID
	 */
	private String objectId;

	private String objectIdFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 发布时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 主体作者ID
	 */
	private String authorUserId;

	private String authorUserIdFuzzy;
}

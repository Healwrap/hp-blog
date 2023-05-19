package com.easybbs.entity.po;

import java.util.Date;
import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 *
 * 用户积分记录表
 *
 */
@Data
public class UserIntegralRecord implements Serializable {


	/**
	 * 记录ID
	 */
	private Integer recordId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 操作类型
	 */
	private Integer operType;

	/**
	 * 积分
	 */
	private Integer integral;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String operTypeName;
	public String getOperTypeName() {
		UserIntegralOperTypeEnum operTypeEnum = UserIntegralOperTypeEnum.getByOperType(operType);
		return operTypeEnum == null ? "" : operTypeEnum.getDesc();
	}
}

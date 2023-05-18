package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;


/**
 *
 * 系统设置信息
 *
 */
@Data
public class SysSetting implements Serializable {


	/**
	 * 编号
	 */
	private String code;

	/**
	 * 设置信息
	 */
	private String jsonContent;
}

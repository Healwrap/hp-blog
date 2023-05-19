package com.easybbs.entity.query;


import lombok.Data;

/**
 *
 * 系统设置信息参数
 *
 */
@Data
public class SysSettingQuery extends BaseParam {


	/**
	 * 编号
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * 设置信息
	 */
	private String jsonContent;

	private String jsonContentFuzzy;

}

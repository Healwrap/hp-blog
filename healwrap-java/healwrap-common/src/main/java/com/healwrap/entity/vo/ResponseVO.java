package com.healwrap.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseVO<T> implements Serializable {
  private String status;
  private Integer code;
  private String info;
  private T data;
}

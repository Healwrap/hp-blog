package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @ClassName FileUploadDto
 * @Description TODO
 * @Date 2023/5/9 20:13
 * @Created by pepedd
 */
@Data
public class FileUploadDto {
  private String localPath;
  private String originalFileName;

}

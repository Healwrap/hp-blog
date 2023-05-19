package com.easybbs.entity.dto;

import lombok.Data;

/**
 * @author pepedd
 * @ClassName FileUploadDto
 * @Description TODO
 * @Date 2023/5/9 20:13
 */
@Data
public class FileUploadDto {
  private String localPath;
  private String originalFileName;
}

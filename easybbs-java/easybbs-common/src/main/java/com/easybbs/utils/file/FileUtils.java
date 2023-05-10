package com.easybbs.utils.file;

import com.easybbs.entity.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.FileUploadDto;
import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.entity.enums.file.FileUploadTypeEnum;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.DateUtils;
import com.easybbs.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * @ClassName FileUtils
 * @Description TODO
 * @Date 2023/5/9 20:05
 * @author pepedd
 */
@Component
public class FileUtils {
  private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
  @Resource
  private AppConfig appConfig;
  @Resource
  private ImageUtils imageUtils;

  public FileUploadDto uploadFile2Local(MultipartFile file, String folder, FileUploadTypeEnum uploadTypeEnum) {
    try {
      FileUploadDto fileUploadDto = new FileUploadDto();
      String originalFilename = file.getOriginalFilename();
      assert originalFilename != null;
      String fileSuffix = FileTypeUtils.getFileType(originalFilename);
      if (originalFilename.length() > Constants.LENGTH_200) {
        originalFilename = FileTypeUtils.getFileType(originalFilename).substring(0, Constants.LENGTH_200 - 10) + "." + fileSuffix;
      }
      if (!ArrayUtils.contains(uploadTypeEnum.getSuffixArray(), ".".concat(fileSuffix))) {
        throw new BusinessException("文件类型不正确");
      }
      String month = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
      String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
//      File targetFileFolder = new File(baseFolder + "/" + folder + "/" + month + "/");
      File targetFileFolder = new File(mergePath(baseFolder, folder, month));
      String fileName = StringTools.getRandomString(Constants.LENGTH_15) + fileSuffix;
//      File targetFile = new File(targetFileFolder + "/" + fileName);
      File targetFile = new File(mergePath(targetFileFolder.getPath(), fileName));
      if (!targetFile.exists()) {
        targetFile.mkdirs();
      }
//      String localPath = month + "/" + fileName;
      String localPath = mergePath(month, fileName);
      if (uploadTypeEnum == FileUploadTypeEnum.AVATAR) {
        targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
//        targetFile = new File(targetFileFolder.getPath() + "/" + folder + Constants.AVATAR_SUFFIX);
        targetFile = new File(mergePath(targetFile.getPath(), folder + Constants.AVATAR_SUFFIX));
        localPath = mergePath(folder,Constants.AVATAR_SUFFIX);
      }
      file.transferTo(targetFile);
      // 压缩图片
      if (uploadTypeEnum == FileUploadTypeEnum.COMMENT_IMAGE) {
        String thumbnailName = targetFile.getName().replace(".", "_.");
        File thumbnail = new File(targetFile.getParent(), thumbnailName);
        Boolean thumbnailCreated = imageUtils.createThumbnail(targetFile, thumbnail, Constants.LENGTH_200, Constants.LENGTH_200);
        if (!thumbnailCreated) {
          org.apache.commons.io.FileUtils.copyFile(targetFile, thumbnail);
        }
      } else if (uploadTypeEnum == FileUploadTypeEnum.AVATAR || uploadTypeEnum == FileUploadTypeEnum.ARTICLE_COVER) {
        imageUtils.createThumbnail(targetFile, targetFile, Constants.LENGTH_200, Constants.LENGTH_200);
      }
      fileUploadDto.setLocalPath(localPath);
      fileUploadDto.setOriginalFileName(originalFilename);
      return fileUploadDto;
    } catch (BusinessException e) {
      logger.error("文件上传失败", e);
      throw e;
    } catch (Exception e) {
      logger.error("文件上传失败", e);
      throw new BusinessException("文件上传失败");
    }
  }

  /**
   * 合并路径
   *
   * @param paths 路径
   * @return
   */

  public static String mergePath(String... paths) {
    String joinedPath = "";
    for (String path : paths) {
      if (path == null || path.isEmpty()) {
        continue;
      }
      if (joinedPath.isEmpty()) {
        joinedPath = path;
      } else {
        char lastChar = joinedPath.charAt(joinedPath.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
          joinedPath += "/";
        }
        char firstChar = path.charAt(0);
        if (firstChar == '/' || firstChar == '\\') {
          path = path.substring(1);
        }
        joinedPath += path;
      }
    }
    return joinedPath.replace('\\', '/');
  }
}

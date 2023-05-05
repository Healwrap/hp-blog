package com.easybbs.controller;

import com.easybbs.controller.base.ABaseController;
import com.easybbs.entity.annotation.GlobalIntercepter;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FileController
 * @Description TODO
 * @Date 2023/5/5 20:48
 * @Created by pepedd
 */
@RestController
@RequestMapping("/files")
public class FileController extends ABaseController {
  private static final Logger logger = LoggerFactory.getLogger(FileController.class);
  @Resource
  private WebConfig webConfig;

  @RequestMapping("/uploadImage")
  @GlobalIntercepter(checkLogin = true)
  public ResponseVO uploadImage(MultipartFile file) {
    if (file == null) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    String fileName = file.getOriginalFilename();
    assert fileName != null;
    String fileSuffix = StringTools.getFileSuffix(fileName);
    if (!ArrayUtils.contains(Constants.IMAGE_SUFFIX, fileSuffix)) {
      throw new BusinessException(ResponseCodeEnum.CODE_600);
    }
    String path = copyFile(file);
    Map<String, String> fileMap = new HashMap<>();
    fileMap.put("fileName", path);
    return getSuccessResponseVO(fileMap);
  }

  private String copyFile(MultipartFile file) {
    try {
      String fileName = file.getOriginalFilename();
      assert fileName != null;
      String fileSuffix = StringTools.getFileSuffix(fileName);
      String fileRealName = StringTools.getRandomString(Constants.LENGTH_30) + fileSuffix;
      String folderPath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_TEMP;
      File folder = new File(folderPath);
      if (!folder.exists()) {
        folder.mkdirs();
      }
      File uploadFile = new File(folderPath + "/" + fileRealName);
      file.transferTo(uploadFile);
      return Constants.FILE_FOLDER_TEMP + fileRealName;
    } catch (Exception e) {
      logger.error("上传文件失败", e);
      throw new BusinessException("上传文件失败");
    }
  }

  @GetMapping("/getImage/{imageFolder}/{imageName}")
  public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName) {
    readImage(response, imageFolder, imageName);
  }

  private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
    ServletOutputStream sos = null;
    FileInputStream fis = null;
    ByteArrayOutputStream bos = null;
    try {
      if (StringTools.isEmpty(imageFolder) || StringTools.isEmpty(imageName)) {
        throw new BusinessException(ResponseCodeEnum.CODE_600);
      }
      String imageSuffix = StringTools.getFileSuffix(imageName);
      String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + "/" + imageFolder + "/" + imageName;
      if (Constants.FILE_FOLDER_TEMP.equals(imageFolder) || imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)) {
        filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + "/" + imageName;
      }
      File file = new File(filePath);
      if (!file.exists()) {
        throw new BusinessException(ResponseCodeEnum.CODE_600);
      }
      imageSuffix = imageSuffix.replace(".", "");
      if (!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)) {
        response.setHeader("Cache-Control", "max-age=2592000");
      }
      response.setContentType("image/" + imageSuffix);
      fis = new FileInputStream(file);
      sos = response.getOutputStream();
      bos = new ByteArrayOutputStream();
      int ch = 0;
      while (-1 != (ch = fis.read())) {
        bos.write(ch);
      }
      sos.write(bos.toByteArray());
    } catch (Exception e) {
      logger.error("读取图片失败", e);
      throw new BusinessException("读取图片失败");
    }
  }
}

package com.easybbs.utils.file;

import com.easybbs.entity.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.DateUtils;
import com.easybbs.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pepedd
 * @ClassName ImageUtils
 * @Description 图片工具类
 * @Date 2023/5/9 22:10
 */
@Component
public class ImageUtils {
  private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
  @Resource
  private AppConfig appConfig;

  public static Boolean createThumbnail(File file, File targetFile, int thumbnailWidth, int thumbnailHeight) {
    try {
      BufferedImage src = ImageIO.read(file);
      // thumbnailWidth 缩略图的宽度 thumbnailHeight 缩略图的高度
      int sorceW = src.getWidth();
      int sorceH = src.getHeight();
      // 小于 指定 宽高 不压缩
      if (sorceW <= thumbnailWidth) { // 目标文件宽度大于指定宽度
        return false;
      }
      int height = sorceH; // 目标文件的高度
      if (sorceW > thumbnailWidth) { // 目标文件宽度大于指定宽度
        height = thumbnailWidth * sorceH / sorceW;
      } else { // 目标文件宽度小于指定宽度 那么缩略图大小跟原图一样
        thumbnailWidth = sorceW;
        height = sorceH;
      }
      // 生成宽度为150的缩略图
      BufferedImage dst = new BufferedImage(thumbnailWidth, height, BufferedImage.TYPE_INT_RGB);
      Image scaleImage = src.getScaledInstance(thumbnailWidth, height, Image.SCALE_SMOOTH);
      Graphics2D g = dst.createGraphics();
      g.drawImage(scaleImage, 0, 0, thumbnailWidth, height, null);
      g.dispose();
      int resultH = dst.getHeight();
      // 高度过大 裁剪图片
      if (resultH > thumbnailHeight) {
        resultH = thumbnailHeight;
        dst = dst.getSubimage(0, 0, thumbnailWidth, resultH);
      }
      ImageIO.write(dst, "JPEG", targetFile);
    } catch (Exception e) {
      throw new BusinessException("图片压缩失败");
    }
    return true;
  }

  public String resetImageHtml(String html) {
    String month = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
    List<String> imageList = getImageList(html);
    for (String img : imageList) {
      resetImage(img, month);
    }
    return month;
  }

  private String resetImage(String imagePath, String month) {
    if (StringTools.isEmpty(imagePath) || !imagePath.contains(Constants.FILE_FOLDER_TEMP)) {
      return imagePath;
    }
    imagePath = imagePath.replace(Constants.READ_IMAGE_PATH, "");
    if (StringTools.isEmpty(month)) {
      month = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
    }
    String imageFileName = FileUtils.mergePath(month, imagePath.substring(imagePath.lastIndexOf("/") + 1));
    File targetFile = new File(FileUtils.mergePath(appConfig.getProjectFolder(), Constants.FILE_FOLDER_FILE, Constants.FILE_FOLDER_IMAGE, imageFileName));
    try {
      File tempFile = new File(FileUtils.mergePath(appConfig.getProjectFolder(), Constants.FILE_FOLDER_FILE, imagePath));
      org.apache.commons.io.FileUtils.copyFile(tempFile, targetFile);
      tempFile.delete();
    } catch (IOException e) {
      logger.error("复制出错:", e);
      return imagePath;
    }
    return imageFileName;
  }

  public List<String> getImageList(String html) {
    List<String> imageList = new ArrayList<String>();
    String regExImg = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
    Pattern pImage = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
    Matcher mImage = pImage.matcher(html);
    while (mImage.find()) {
      String img = mImage.group();
      Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
      while (m.find()) {
        String imageUrl = m.group(1);
        imageList.add(imageUrl);
      }
    }
    return imageList;
  }
}

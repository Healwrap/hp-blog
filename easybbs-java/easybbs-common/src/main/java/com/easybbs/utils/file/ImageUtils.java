package com.easybbs.utils.file;

import com.easybbs.exception.BusinessException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName ImageUtils
 * @Description TODO
 * @Date 2023/5/9 22:10
 * @Created by pepedd
 */
public class ImageUtils {
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
}

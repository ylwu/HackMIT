package org.jpedal.examples.handlers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.io.JAIHelper;
import org.jpedal.utils.LogWriter;

public class DefaultImageHelper
{
  public DefaultImageHelper()
  {
    ImageIO.setUseCache(false);
  }

  public static void write(BufferedImage paramBufferedImage, String paramString1, String paramString2)
    throws IOException
  {
    if ((!paramString1.equals("jpg")) && (JAIHelper.isJAIused()))
    {
      JAIHelper.confirmJAIOnClasspath();
      JAIHelper.filestore(paramBufferedImage, paramString2, paramString1);
    }
    else
    {
      if (GenericColorSpace.fasterPNG)
      {
        localObject = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 13);
        Graphics2D localGraphics2D = ((BufferedImage)localObject).createGraphics();
        localGraphics2D.drawImage(paramBufferedImage, 0, 0, null);
        paramBufferedImage = (BufferedImage)localObject;
      }
      Object localObject = new BufferedOutputStream(new FileOutputStream(new File(paramString2)));
      ImageIO.write(paramBufferedImage, paramString1, (OutputStream)localObject);
      ((BufferedOutputStream)localObject).flush();
      ((BufferedOutputStream)localObject).close();
    }
  }

  public static BufferedImage read(String paramString)
  {
    int i = 0;
    BufferedImage localBufferedImage = null;
    if (JAIHelper.isJAIused())
      try
      {
        localBufferedImage = JAIHelper.fileload(paramString);
        i = 1;
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
        i = 0;
      }
      catch (Error localError1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError1.getMessage());
        throw new RuntimeException("Error " + localError1 + " loading " + paramString + " with JAI");
      }
    if (i == 0)
      try
      {
        localBufferedImage = ImageIO.read(new File(paramString));
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
      catch (Error localError2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError2.getMessage());
        throw new RuntimeException("Error " + localError2 + " loading " + paramString + " with ImageIO");
      }
    return localBufferedImage;
  }

  public static Raster readRasterFromJPeg(byte[] paramArrayOfByte)
    throws IOException
  {
    Raster localRaster = null;
    ImageReader localImageReader = null;
    ImageInputStream localImageInputStream = null;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    try
    {
      Iterator localIterator = ImageIO.getImageReadersByFormatName("JPEG");
      while (localIterator.hasNext())
      {
        Object localObject1 = localIterator.next();
        localImageReader = (ImageReader)localObject1;
        if (localImageReader.canReadRaster())
          break;
      }
      ImageIO.setUseCache(false);
      localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      localRaster = localImageReader.readRaster(0, null);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to find JAI jars on classpath");
    }
    finally
    {
      if (localByteArrayInputStream != null)
        localByteArrayInputStream.close();
      if (localImageInputStream != null)
      {
        localImageInputStream.flush();
        localImageInputStream.close();
      }
      if (localImageReader != null)
        localImageReader.dispose();
    }
    return localRaster;
  }

  public static BufferedImage read(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    ImageIO.setUseCache(false);
    return ImageIO.read(localByteArrayInputStream);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.DefaultImageHelper
 * JD-Core Version:    0.6.2
 */
package org.jpedal.render.output.io;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.io.JAIHelper;
import org.jpedal.utils.LogWriter;

public class DefaultIO
  implements CustomIO
{
  public static boolean isTest = false;
  private BufferedWriter output = null;
  Map imagesWritten = new HashMap();

  public static void write(BufferedImage paramBufferedImage, String paramString1, String paramString2)
  {
    try
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(paramString2)));
      ImageIO.write(paramBufferedImage, paramString1, localBufferedOutputStream);
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
  }

  public static void write(BufferedImage paramBufferedImage, String paramString, OutputStream paramOutputStream)
  {
    try
    {
      ImageIO.write(paramBufferedImage, paramString, paramOutputStream);
      paramOutputStream.flush();
      paramOutputStream.close();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
  }

  public static Raster getRasterFromJPEG(byte[] paramArrayOfByte, String paramString)
  {
    ByteArrayInputStream localByteArrayInputStream = null;
    ImageReader localImageReader = null;
    ImageInputStream localImageInputStream = null;
    Raster localRaster = null;
    try
    {
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      Iterator localIterator = ImageIO.getImageReadersByFormatName(paramString);
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        localImageReader = (ImageReader)localObject;
        if (localImageReader.canReadRaster())
          break;
      }
      ImageIO.setUseCache(false);
      localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      localRaster = localImageReader.readRaster(0, null);
      localByteArrayInputStream.close();
      localImageReader.dispose();
      localImageInputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem closing  " + localException);
    }
    return localRaster;
  }

  public void writeFont(String paramString, byte[] paramArrayOfByte)
  {
    try
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString));
      localBufferedOutputStream.write(paramArrayOfByte);
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public void writePlainTextFile(String paramString, StringBuilder paramStringBuilder)
  {
    File localFile = new File(paramString).getParentFile();
    if (!localFile.exists())
      localFile.mkdirs();
    try
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString));
      localBufferedOutputStream.write(paramStringBuilder.toString().getBytes());
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public boolean isOutputOpen()
  {
    return this.output != null;
  }

  public void setupOutput(String paramString1, boolean paramBoolean, String paramString2)
    throws FileNotFoundException, UnsupportedEncodingException
  {
    this.output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramString1, paramBoolean), paramString2));
  }

  public void setupOutput(OutputStream paramOutputStream, boolean paramBoolean, String paramString)
    throws FileNotFoundException, UnsupportedEncodingException
  {
    this.output = new BufferedWriter(new OutputStreamWriter(paramOutputStream, paramString));
  }

  public void flush()
  {
    try
    {
      this.output.flush();
      this.output.close();
      this.imagesWritten.clear();
      this.output = null;
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public void writeString(String paramString)
  {
    try
    {
      this.output.write(paramString);
      this.output.write(10);
      this.output.flush();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public String writeImage(String paramString1, String paramString2, BufferedImage paramBufferedImage)
  {
    String str1 = paramString2 + getImageTypeUsed();
    String str2 = paramString1 + str1;
    File localFile = new File(str2).getParentFile();
    if (!localFile.exists())
      localFile.mkdirs();
    try
    {
      if ((!JAIHelper.isJAIused()) || (isTest))
      {
        ImageIO.write(paramBufferedImage, "PNG", new File(str2));
      }
      else
      {
        JAIHelper.confirmJAIOnClasspath();
        BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(str2)));
        ImageEncoder localImageEncoder = ImageCodec.createImageEncoder("PNG", localBufferedOutputStream, null);
        localImageEncoder.encode(paramBufferedImage);
        localBufferedOutputStream.close();
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    return str1;
  }

  public String getImageTypeUsed()
  {
    return ".png";
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.io.DefaultIO
 * JD-Core Version:    0.6.2
 */
package org.jpedal.color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class SeparationColorSpace extends GenericColorSpace
{
  protected GenericColorSpace altCS;
  static final int Black = 1009857357;
  static final int PANTONE_BLACK = 573970506;
  private static final int Cyan = 323563838;
  private static final int Magenta = 895186280;
  public static final int Yellow = 1010591868;
  protected ColorMapping colorMapper;
  private float[] domain;
  protected int cmykMapping = -1;
  protected static final int NOCMYK = -1;
  protected static final int MYK = 1;
  protected static final int CMY = 2;
  protected static final int CMK = 4;
  protected static final int CY = 5;
  protected static final int MY = 6;
  protected static final int CM = 8;
  protected static final int CMYK = 7;
  protected static final int CMYB = 9;

  public SeparationColorSpace()
  {
  }

  public SeparationColorSpace(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    this.value = -2073385820;
    processColorToken(paramPdfObjectReader, paramPdfObject);
  }

  protected void processColorToken(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(895578984);
    PdfObject localPdfObject2 = paramPdfObject.getDictionary(-1313946392);
    this.domain = null;
    byte[] arrayOfByte1 = null;
    Object localObject = (byte[][])null;
    if (this.value == -2073385820)
    {
      arrayOfByte1 = paramPdfObject.getStringValueAsByte(506543413);
      if (arrayOfByte1 != null)
        localObject = new byte[][] { arrayOfByte1 };
      this.componentCount = 1;
    }
    else
    {
      localObject = paramPdfObject.getStringArray(1920898752);
      this.componentCount = localObject.length;
    }
    this.cmykMapping = -1;
    int[] arrayOfInt = new int[this.componentCount];
    int i;
    if (localObject != null)
      for (i = 0; i < this.componentCount; i++)
        arrayOfInt[i] = PdfDictionary.generateChecksum(1, localObject[i].length - 1, localObject[i]);
    switch (this.componentCount)
    {
    case 1:
      if ((localObject != null) && ((arrayOfInt[0] == 1009857357) || (arrayOfInt[0] == 573970506)))
        this.cmykMapping = 1009857357;
      break;
    case 2:
      if (arrayOfInt[0] == 323563838)
      {
        if (arrayOfInt[1] == 1010591868)
          this.cmykMapping = 5;
        else if (arrayOfInt[1] == 895186280)
          this.cmykMapping = 8;
      }
      else if ((arrayOfInt[0] == 895186280) && (arrayOfInt[1] == 1010591868))
        this.cmykMapping = 6;
      break;
    case 3:
      if ((arrayOfInt[0] == 895186280) && (arrayOfInt[1] == 1010591868) && (arrayOfInt[2] == 1009857357))
        this.cmykMapping = 1;
      else if ((arrayOfInt[0] == 323563838) && (arrayOfInt[1] == 895186280) && (arrayOfInt[2] == 1010591868))
        this.cmykMapping = 2;
      else if ((arrayOfInt[0] == 323563838) && (arrayOfInt[1] == 895186280) && (arrayOfInt[2] == 1009857357))
        this.cmykMapping = 4;
      break;
    case 4:
      if ((arrayOfInt[0] == 323563838) && (arrayOfInt[1] == 895186280) && (arrayOfInt[2] == 1010591868) && (arrayOfInt[3] == 1009857357))
        this.cmykMapping = 9;
      break;
    case 5:
      if ((arrayOfInt[0] == 323563838) && (arrayOfInt[1] == 895186280) && (arrayOfInt[2] == 1010591868) && (arrayOfInt[3] == 1009857357))
        this.cmykMapping = 7;
      break;
    case 6:
      if ((arrayOfInt[0] == 323563838) && (arrayOfInt[1] == 895186280) && (arrayOfInt[2] == 1010591868) && (arrayOfInt[3] == 1009857357))
        this.cmykMapping = 7;
      break;
    }
    if (this.cmykMapping != -1)
    {
      this.altCS = new DeviceCMYKColorSpace();
    }
    else
    {
      paramPdfObject = paramPdfObject.getDictionary(-1247101998);
      this.altCS = ColorspaceFactory.getColorSpaceInstance(paramPdfObjectReader, paramPdfObject);
      if ((this.altCS.getID() == 1247168582) && (paramPdfObject.getParameterConstant(2054519176) == 1498837125))
        this.altCS = new DeviceCMYKColorSpace();
    }
    if (arrayOfByte1 != null)
    {
      i = arrayOfByte1.length;
      int j = 0;
      byte[] arrayOfByte2 = new byte[i];
      for (int n = 0; n < i; n++)
      {
        if (arrayOfByte1[n] == 35)
        {
          n++;
          int k = arrayOfByte1[n];
          if ((k >= 65) && (k <= 70))
            k -= 55;
          else if ((k >= 97) && (k <= 102))
            k -= 87;
          else if ((k >= 48) && (k <= 57))
            k -= 48;
          n++;
          while ((arrayOfByte1[n] == 32) || (arrayOfByte1[n] == 10) || (arrayOfByte1[n] == 13))
            n++;
          int m = arrayOfByte1[n];
          if ((m >= 65) && (m <= 70))
            m -= 55;
          else if ((m >= 97) && (m <= 102))
            m -= 87;
          else if ((m >= 48) && (m <= 57))
            m -= 48;
          arrayOfByte2[j] = ((byte)(m + (k << 4)));
        }
        else
        {
          arrayOfByte2[j] = arrayOfByte1[n];
        }
        j++;
      }
      if (j != i)
      {
        arrayOfByte1 = new byte[j];
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, j);
      }
      this.pantoneName = new String(arrayOfByte1);
    }
    if (localPdfObject2 == null)
      paramPdfObject.getDictionary(-1313946392);
    if ((localPdfObject2 == null) && (localPdfObject1 != null))
      localPdfObject2 = localPdfObject1.getDictionary(-1313946392);
    this.colorMapper = new ColorMapping(paramPdfObjectReader, localPdfObject2);
    this.domain = localPdfObject2.getFloatArray(1026641277);
  }

  private void setColor(float paramFloat)
  {
    try
    {
      if (this.cmykMapping == 1009857357)
      {
        float[] arrayOfFloat1 = { 0.0F, 0.0F, 0.0F, paramFloat };
        this.altCS.setColor(arrayOfFloat1, 1);
      }
      else
      {
        int i = 1;
        if (this.domain != null)
          i = this.domain.length / 2;
        float[] arrayOfFloat2 = new float[i];
        for (int j = 0; j < i; j++)
          arrayOfFloat2[j] = paramFloat;
        float[] arrayOfFloat3 = this.colorMapper.getOperandFloat(arrayOfFloat2);
        this.altCS.setColor(arrayOfFloat3, arrayOfFloat3.length);
      }
    }
    catch (Exception localException)
    {
    }
  }

  public void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    setColor(paramArrayOfFloat[0]);
  }

  public void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[1];
    arrayOfFloat[0] = Float.parseFloat(paramArrayOfString[0]);
    setColor(arrayOfFloat, 1);
  }

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    ByteArrayInputStream localByteArrayInputStream = null;
    ImageReader localImageReader = null;
    ImageInputStream localImageInputStream = null;
    BufferedImage localBufferedImage;
    try
    {
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      Iterator localIterator = ImageIO.getImageReadersByFormatName("JPEG");
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        localImageReader = (ImageReader)localObject;
        if (localImageReader.canReadRaster())
          break;
      }
      ImageIO.setUseCache(false);
      localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      Object localObject = localImageReader.readRaster(0, null);
      localObject = cleanupRaster((Raster)localObject, paramInt3, paramInt4, 1);
      int i = ((Raster)localObject).getWidth();
      int j = ((Raster)localObject).getHeight();
      DataBufferByte localDataBufferByte = (DataBufferByte)((Raster)localObject).getDataBuffer();
      byte[] arrayOfByte = localDataBufferByte.getData();
      if (this.altCS.getID() == 1568372915)
      {
        for (int k = 0; k < arrayOfByte.length; k++)
          arrayOfByte[k] = ((byte)(arrayOfByte[k] ^ 0xFF));
        int[] arrayOfInt = { 0 };
        localBufferedImage = new BufferedImage(i, j, 10);
        WritableRaster localWritableRaster = Raster.createInterleavedRaster(new DataBufferByte(arrayOfByte, arrayOfByte.length), i, j, i, 1, arrayOfInt, null);
        localBufferedImage.setData(localWritableRaster);
      }
      else
      {
        localBufferedImage = createImage(i, j, arrayOfByte, paramBoolean);
      }
    }
    catch (Exception localException1)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException1);
    }
    try
    {
      localByteArrayInputStream.close();
      localImageReader.dispose();
      localImageInputStream.close();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem closing  " + localException2);
    }
    return localBufferedImage;
  }

  public BufferedImage JPEG2000ToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4)
    throws PdfException
  {
    ByteArrayInputStream localByteArrayInputStream;
    ImageReader localImageReader;
    BufferedImage localBufferedImage;
    try
    {
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localImageReader = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG2000").next();
    }
    catch (Exception localException1)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localException1);
      String str = "Exception " + localException1 + " with JPeg 2000 Image from iir = (ImageReader)ImageIO.getImageReadersByFormatName(\"JPEG2000\").next();";
      if (!JAIHelper.isJAIused())
        str = "JPeg 2000 Images and JAI not setup.\nYou need both JAI and imageio.jar on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on";
      throw new PdfException(str);
    }
    if (localImageReader == null)
      return null;
    try
    {
      ImageInputStream localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      try
      {
        localImageReader.setInput(localImageInputStream, true);
        localBufferedImage = localImageReader.read(0);
        localImageReader.dispose();
        localImageInputStream.close();
        localByteArrayInputStream.close();
      }
      catch (Exception localException4)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Problem reading JPEG 2000: " + localException4);
        localException4.printStackTrace();
        return null;
      }
      localBufferedImage = cleanupImage(localBufferedImage, paramInt3, paramInt4);
      paramInt1 = localBufferedImage.getWidth();
      paramInt2 = localBufferedImage.getHeight();
      DataBufferByte localDataBufferByte = (DataBufferByte)localBufferedImage.getRaster().getDataBuffer();
      byte[] arrayOfByte = localDataBufferByte.getData();
      localBufferedImage = createImage(paramInt1, paramInt2, arrayOfByte, false);
    }
    catch (Exception localException2)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException2);
    }
    try
    {
      localByteArrayInputStream.close();
      localImageReader.dispose();
    }
    catch (Exception localException3)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem closing  " + localException3);
    }
    return localBufferedImage;
  }

  public BufferedImage dataToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage;
    try
    {
      localBufferedImage = createImage(paramInt1, paramInt2, paramArrayOfByte, false);
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't convert Separation colorspace data: " + localException);
    }
    return localBufferedImage;
  }

  private BufferedImage createImage(int paramInt1, int paramInt2, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = 3 * paramInt1 * paramInt2;
    byte[] arrayOfByte = new byte[i];
    int j = 0;
    float[][] arrayOfFloat = new float[3][256];
    for (int k = 0; k < 256; k++)
      arrayOfFloat[0][k] = -1.0F;
    for (int i1 : paramArrayOfByte)
    {
      int i2 = i1 & 0xFF;
      if (arrayOfFloat[0][i2] == -1.0F)
      {
        if (paramBoolean)
          setColor(1.0F - i2 / 255.0F);
        else
          setColor(i2 / 255.0F);
        arrayOfFloat[0][i2] = ((Color)getColor()).getRed();
        arrayOfFloat[1][i2] = ((Color)getColor()).getGreen();
        arrayOfFloat[2][i2] = ((Color)getColor()).getBlue();
      }
      for (int i3 = 0; i3 < 3; i3++)
      {
        arrayOfByte[j] = ((byte)(int)arrayOfFloat[i3][i2]);
        j++;
      }
    }
    BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
    ??? = ColorSpaceConvertor.createInterleavedRaster(arrayOfByte, paramInt1, paramInt2);
    localBufferedImage.setData((Raster)???);
    return localBufferedImage;
  }

  public byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    this.isConverted = true;
    byte[] arrayOfByte = new byte[768];
    try
    {
      int i = 0;
      int j = paramArrayOfByte.length;
      float[] arrayOfFloat3 = new float[this.componentCount];
      int k = 0;
      while (k < j)
      {
        Color localColor;
        if ((this.componentCount == 1) && (this.value == -2073385820) && (this.colorMapper == null))
        {
          float[] arrayOfFloat1 = new float[1];
          arrayOfFloat1[1] = (paramArrayOfByte[k] & 0xFF);
          setColor(arrayOfFloat1, 1);
          localColor = (Color)getColor();
        }
        else
        {
          for (int m = 0; m < this.componentCount; m++)
            arrayOfFloat3[m] = ((paramArrayOfByte[(k + m)] & 0xFF) / 255.0F);
          float[] arrayOfFloat2 = this.colorMapper.getOperandFloat(arrayOfFloat3);
          this.altCS.setColor(arrayOfFloat2, arrayOfFloat2.length);
          localColor = (Color)this.altCS.getColor();
        }
        arrayOfByte[i] = ((byte)localColor.getRed());
        i++;
        arrayOfByte[i] = ((byte)localColor.getGreen());
        i++;
        arrayOfByte[i] = ((byte)localColor.getBlue());
        i++;
        k += this.componentCount;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception  " + localException + " converting colorspace");
    }
    return arrayOfByte;
  }

  public PdfPaint getColor()
  {
    return this.altCS.getColor();
  }

  public final Object clone()
  {
    setColorStatus();
    Object localObject;
    try
    {
      localObject = super.clone();
    }
    catch (Exception localException)
    {
      throw new RuntimeException("Unable to clone object");
    }
    return localObject;
  }

  private void setColorStatus()
  {
    int i = this.altCS.currentColor.getRGB();
    this.r = (i >> 16 & 0xFF);
    this.g = (i >> 8 & 0xFF);
    this.b = (i & 0xFF);
  }

  public void restoreColorStatus()
  {
    this.altCS.currentColor = new PdfColor(this.r, this.g, this.b);
    this.altCS.clearCache();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.SeparationColorSpace
 * JD-Core Version:    0.6.2
 */
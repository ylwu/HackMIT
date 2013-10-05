package org.jpedal.io;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import org.jpedal.color.ColorSpaces;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.utils.LogWriter;

public class ColorSpaceConvertor
{
  public static boolean isUsingARGB = false;

  public static BufferedImage convertFromICCCMYK(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    BufferedImage localBufferedImage = null;
    try
    {
      int i = paramInt1 * paramInt2 * 4;
      if (paramArrayOfByte.length < i)
      {
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
        paramArrayOfByte = arrayOfByte;
      }
      return profileConvertCMYKImageToRGB(paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception  " + localException + " converting from ICC colorspace");
      localException.printStackTrace();
    }
    return localBufferedImage;
  }

  public static BufferedImage convertToRGB(BufferedImage paramBufferedImage)
  {
    if (paramBufferedImage.getType() != 1)
      try
      {
        BufferedImage localBufferedImage = paramBufferedImage;
        paramBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 1);
        new ColorConvertOp(ColorSpaces.hints).filter(localBufferedImage, paramBufferedImage);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException.toString() + " converting to RGB");
      }
      catch (Error localError)
      {
        localError.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error " + localError.toString() + " converting to RGB");
        paramBufferedImage = null;
      }
    return paramBufferedImage;
  }

  public static BufferedImage convertToARGB(BufferedImage paramBufferedImage)
  {
    if (paramBufferedImage.getType() != 2)
      try
      {
        BufferedImage localBufferedImage = paramBufferedImage;
        paramBufferedImage = new BufferedImage(localBufferedImage.getWidth(), localBufferedImage.getHeight(), 2);
        ColorConvertOp localColorConvertOp = new ColorConvertOp(null);
        localColorConvertOp.filter(localBufferedImage, paramBufferedImage);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " creating argb image");
      }
    isUsingARGB = true;
    return paramBufferedImage;
  }

  public static BufferedImage algorithmicConvertCMYKImageToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = null;
    byte[] arrayOfByte = new byte[paramInt1 * paramInt2 * 3];
    int i = paramInt1 * paramInt2 * 4;
    double d1 = -1.0D;
    double d2 = -1.12D;
    double d3 = -1.12D;
    double d4 = -1.21D;
    double d5 = 255.0D;
    double d16 = 0.0D;
    double d17 = 0.0D;
    double d18 = 0.0D;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      double d19 = (paramArrayOfByte[k] & 0xFF) / d5;
      double d20 = (paramArrayOfByte[(k + 1)] & 0xFF) / d5;
      double d21 = (paramArrayOfByte[(k + 2)] & 0xFF) / d5;
      double d22 = (paramArrayOfByte[(k + 3)] & 0xFF) / d5;
      if ((d1 != d19) || (d2 != d20) || (d3 != d21) || (d4 != d22))
      {
        double d23 = 1.0D;
        double d6 = clip01(d19 + d22);
        double d7 = clip01(d20 + d22);
        double d8 = clip01(d21 + d22);
        double d9 = (d23 - d6) * (d23 - d7) * (d23 - d8);
        double d10 = d6 * (d23 - d7) * (d23 - d8);
        double d11 = (d23 - d6) * d7 * (d23 - d8);
        double d12 = (d23 - d6) * (d23 - d7) * d8;
        double d13 = (d23 - d6) * d7 * d8;
        double d14 = d6 * (d23 - d7) * d8;
        double d15 = d6 * d7 * (d23 - d8);
        d16 = d5 * clip01(d9 + 0.9137D * d11 + 0.9961D * d12 + 0.9882D * d13);
        d17 = d5 * clip01(d9 + 0.6196D * d10 + d12 + 0.5176D * d14);
        d18 = d5 * clip01(d9 + 0.7804D * d10 + 0.5412D * d11 + 0.0667D * d13 + 0.2118D * d14 + 0.4863D * d15);
        d1 = d19;
        d2 = d20;
        d3 = d21;
        d4 = d22;
      }
      arrayOfByte[(j++)] = ((byte)(int)d16);
      arrayOfByte[(j++)] = ((byte)(int)d17);
      arrayOfByte[(j++)] = ((byte)(int)d18);
      k += 4;
    }
    try
    {
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      Raster localRaster = createInterleavedRaster(arrayOfByte, paramInt1, paramInt2);
      localBufferedImage.setData(localRaster);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " with 24 bit RGB image");
    }
    return localBufferedImage;
  }

  public static BufferedImage profileConvertCMYKImageToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    ColorSpace localColorSpace = DeviceCMYKColorSpace.getColorSpaceInstance();
    BufferedImage localBufferedImage = null;
    byte[] arrayOfByte = new byte[paramInt1 * paramInt2 * 3];
    int i = paramInt1 * paramInt2 * 4;
    float f1 = -1.0F;
    float f2 = -1.0F;
    float f3 = -1.0F;
    float f4 = -1.0F;
    float[] arrayOfFloat = new float[3];
    int j = 0;
    int k = 0;
    while (k < i)
    {
      float f5 = (paramArrayOfByte[k] & 0xFF) / 255.0F;
      float f6 = (paramArrayOfByte[(k + 1)] & 0xFF) / 255.0F;
      float f7 = (paramArrayOfByte[(k + 2)] & 0xFF) / 255.0F;
      float f8 = (paramArrayOfByte[(k + 3)] & 0xFF) / 255.0F;
      if ((f1 != f5) || (f2 != f6) || (f3 != f7) || (f4 != f8))
      {
        arrayOfFloat = localColorSpace.toRGB(new float[] { f5, f6, f7, f8 });
        f1 = f5;
        f2 = f6;
        f3 = f7;
        f4 = f8;
      }
      arrayOfByte[(j++)] = ((byte)(int)(arrayOfFloat[0] * 255.0F));
      arrayOfByte[(j++)] = ((byte)(int)(arrayOfFloat[1] * 255.0F));
      arrayOfByte[(j++)] = ((byte)(int)(arrayOfFloat[2] * 255.0F));
      k += 4;
    }
    try
    {
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      Raster localRaster = createInterleavedRaster(arrayOfByte, paramInt1, paramInt2);
      localBufferedImage.setData(localRaster);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " with 24 bit RGB image");
    }
    return localBufferedImage;
  }

  public static BufferedImage algorithmicConvertYCbCrToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = null;
    byte[] arrayOfByte = new byte[paramInt1 * paramInt2 * 3];
    int i = paramInt1 * paramInt2 * 3;
    if (i > paramArrayOfByte.length)
      i = paramArrayOfByte.length;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = -1;
    int i1 = -1;
    int i2 = -1;
    int i3 = 0;
    int i4 = 0;
    while (i4 < i)
    {
      int i5 = paramArrayOfByte[i4] & 0xFF;
      int i6 = paramArrayOfByte[(1 + i4)] & 0xFF;
      int i7 = paramArrayOfByte[(2 + i4)] & 0xFF;
      if ((n != i5) || (i1 != i6) || (i2 != i7))
      {
        float f = 298.082F * i5;
        j = (int)((f + 408.58301F * i7) / 256.0F - 222.92099999999999D);
        if (j < 0)
          j = 0;
        if (j > 255)
          j = 255;
        k = (int)((f - 100.291F * i6 - 208.12F * i7) / 256.0F + 135.576F);
        if (k < 0)
          k = 0;
        if (k > 255)
          k = 255;
        m = (int)((f + 516.41199F * i6) / 256.0F - 276.836F);
        if (m < 0)
          m = 0;
        if (m > 255)
          m = 255;
        n = i5;
        i1 = i6;
        i2 = i7;
      }
      arrayOfByte[(i3++)] = ((byte)j);
      arrayOfByte[(i3++)] = ((byte)k);
      arrayOfByte[(i3++)] = ((byte)m);
      i4 += 3;
    }
    try
    {
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      Raster localRaster = createInterleavedRaster(arrayOfByte, paramInt1, paramInt2);
      localBufferedImage.setData(localRaster);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " with 24 bit RGB image");
    }
    return localBufferedImage;
  }

  public static BufferedImage convertIndexedToFlat(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean1, boolean paramBoolean2)
  {
    int[] arrayOfInt1 = { 0, 1, 2 };
    int[] arrayOfInt2 = { 0, 1, 2, 3 };
    int i = 3;
    int j = 0;
    if (paramArrayOfByte2 != null)
      j = paramArrayOfByte2.length;
    int[] arrayOfInt3;
    if (paramBoolean1)
    {
      arrayOfInt3 = arrayOfInt2;
      i = 4;
    }
    else
    {
      arrayOfInt3 = arrayOfInt1;
    }
    int k = paramInt2 * paramInt3 * i;
    byte[] arrayOfByte = new byte[k];
    int m = 0;
    float f = 0.0F;
    switch (paramInt1)
    {
    case 8:
      int n = 0;
      for (int i1 = 0; i1 < paramArrayOfByte1.length - 1; i1++)
      {
        if (paramBoolean2)
          f = (paramArrayOfByte1[i1] & 0xFF) / 255.0F;
        else
          m = (paramArrayOfByte1[i1] & 0xFF) * 3;
        if (n >= k)
          break;
        if (paramBoolean2)
        {
          if (f > 0.0F)
          {
            arrayOfByte[(n++)] = ((byte)(int)((255 - paramArrayOfByte2[0]) * f));
            arrayOfByte[(n++)] = ((byte)(int)((255 - paramArrayOfByte2[1]) * f));
            arrayOfByte[(n++)] = ((byte)(int)((255 - paramArrayOfByte2[2]) * f));
          }
          else
          {
            n += 3;
          }
        }
        else if (m < j)
        {
          arrayOfByte[(n++)] = paramArrayOfByte2[m];
          arrayOfByte[(n++)] = paramArrayOfByte2[(m + 1)];
          arrayOfByte[(n++)] = paramArrayOfByte2[(m + 2)];
        }
        if (paramBoolean1)
          if ((m == 0) && (f == 0.0F))
            arrayOfByte[(n++)] = -1;
          else
            arrayOfByte[(n++)] = 0;
      }
      break;
    case 4:
      flatten4bpc(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramBoolean1, k, arrayOfByte);
      break;
    case 2:
      flatten2bpc(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramBoolean1, k, arrayOfByte);
      break;
    case 1:
      flatten1bpc(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramBoolean1, k, 255, arrayOfByte);
    case 3:
    case 5:
    case 6:
    case 7:
    }
    BufferedImage localBufferedImage;
    if (paramBoolean1)
      localBufferedImage = new BufferedImage(paramInt2, paramInt3, 2);
    else
      localBufferedImage = new BufferedImage(paramInt2, paramInt3, 1);
    DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
    WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, paramInt2, paramInt3, paramInt2 * i, i, arrayOfInt3, null);
    localBufferedImage.setData(localWritableRaster);
    return localBufferedImage;
  }

  private static void flatten4bpc(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean, int paramInt2, byte[] paramArrayOfByte3)
  {
    int j = 0;
    int[] arrayOfInt = { 4, 0 };
    int k = 0;
    for (int i1 : paramArrayOfByte1)
      for (int i2 = 0; i2 < 2; i2++)
      {
        int i = (i1 >> arrayOfInt[i2] & 0xF) * 3;
        if (j >= paramInt2)
          break;
        paramArrayOfByte3[(j++)] = paramArrayOfByte2[i];
        paramArrayOfByte3[(j++)] = paramArrayOfByte2[(i + 1)];
        paramArrayOfByte3[(j++)] = paramArrayOfByte2[(i + 2)];
        if (paramBoolean)
          if (i == 0)
            paramArrayOfByte3[(j++)] = 0;
          else
            paramArrayOfByte3[(j++)] = 0;
        k++;
        if (k == paramInt1)
        {
          k = 0;
          i2 = 8;
        }
      }
  }

  public static void flatten1bpc(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean, int paramInt2, int paramInt3, byte[] paramArrayOfByte3)
  {
    int i = 0;
    int k = 0;
    for (int i1 : paramArrayOfByte1)
      for (int i2 = 0; i2 < 8; i2++)
      {
        int j = (i1 >> 7 - i2 & 0x1) * 3;
        if (i >= paramInt2)
          break;
        if (paramBoolean)
        {
          if (j == 0)
          {
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[j];
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 1)];
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 2)];
            paramArrayOfByte3[(i++)] = ((byte)paramInt3);
          }
          else
          {
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[j];
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 1)];
            paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 2)];
            paramArrayOfByte3[(i++)] = 0;
          }
        }
        else
        {
          paramArrayOfByte3[(i++)] = paramArrayOfByte2[j];
          paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 1)];
          paramArrayOfByte3[(i++)] = paramArrayOfByte2[(j + 2)];
        }
        k++;
        if (k == paramInt1)
        {
          k = 0;
          i2 = 8;
        }
      }
  }

  public static void flatten2bpc(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean, int paramInt2, byte[] paramArrayOfByte3)
  {
    int j = 0;
    int[] arrayOfInt = { 6, 4, 2, 0 };
    int k = 0;
    for (int i1 : paramArrayOfByte1)
      for (int i2 = 0; (i2 < 4) && (j < paramInt2); i2++)
      {
        int i;
        if (paramArrayOfByte2 == null)
        {
          i = i1 << arrayOfInt[(3 - i2)] & 0xC0;
          if (i == 192)
            i = 255;
          paramArrayOfByte3[(j++)] = ((byte)i);
        }
        else
        {
          i = (i1 >> arrayOfInt[i2] & 0x3) * 3;
          paramArrayOfByte3[(j++)] = paramArrayOfByte2[i];
          paramArrayOfByte3[(j++)] = paramArrayOfByte2[(i + 1)];
          paramArrayOfByte3[(j++)] = paramArrayOfByte2[(i + 2)];
          if (paramBoolean)
            if (i == 0)
              paramArrayOfByte3[(j++)] = 0;
            else
              paramArrayOfByte3[(j++)] = 0;
        }
        k++;
        if (k == paramInt1)
        {
          k = 0;
          i2 = 8;
        }
      }
  }

  public static BufferedImage iccConvertCMYKImageToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    ColorSpace localColorSpace1 = DeviceCMYKColorSpace.getColorSpaceInstance();
    ColorSpace localColorSpace2 = GenericColorSpace.getColorSpaceInstance();
    ComponentColorModel localComponentColorModel = new ComponentColorModel(localColorSpace2, new int[] { 8, 8, 8 }, false, false, 1, 0);
    ColorConvertOp localColorConvertOp = new ColorConvertOp(localColorSpace1, localColorSpace2, ColorSpaces.hints);
    int i = paramInt1 * paramInt2 * 4;
    int i1 = -1;
    int i2 = -1;
    int i3 = -1;
    int i4 = -1;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    while (i8 < i)
    {
      int j = paramArrayOfByte[i8] & 0xFF;
      int k = paramArrayOfByte[(i8 + 1)] & 0xFF;
      int m = paramArrayOfByte[(i8 + 2)] & 0xFF;
      int n = paramArrayOfByte[(i8 + 3)] & 0xFF;
      if ((j != i1) || (k != i2) || (m != i3) || (n != i4))
      {
        double d1 = j + 1.402D * m - 179.45599999999999D;
        if (d1 < 0.0D)
          d1 = 0.0D;
        else if (d1 > 255.0D)
          d1 = 255.0D;
        double d2 = j - 0.34414D * k - 0.71414D * m + 135.45984000000001D;
        if (d2 < 0.0D)
          d2 = 0.0D;
        else if (d2 > 255.0D)
          d2 = 255.0D;
        double d3 = j + 1.772D * k - 226.816D;
        if (d3 < 0.0D)
          d3 = 0.0D;
        else if (d3 > 255.0D)
          d3 = 255.0D;
        i5 = 255 - (int)d1;
        i6 = 255 - (int)d2;
        i7 = 255 - (int)d3;
        i1 = j;
        i2 = k;
        i3 = m;
        i4 = n;
      }
      paramArrayOfByte[i8] = ((byte)i5);
      paramArrayOfByte[(i8 + 1)] = ((byte)i6);
      paramArrayOfByte[(i8 + 2)] = ((byte)i7);
      i8 += 4;
    }
    WritableRaster localWritableRaster1 = Raster.createInterleavedRaster(new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length), paramInt1, paramInt2, paramInt1 * 4, 4, new int[] { 0, 1, 2, 3 }, null);
    WritableRaster localWritableRaster2 = localComponentColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
    localColorConvertOp.filter(localWritableRaster1, localWritableRaster2);
    BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
    localBufferedImage.setData(localWritableRaster2);
    return localBufferedImage;
  }

  public static BufferedImage convertColorspace(BufferedImage paramBufferedImage, int paramInt)
  {
    try
    {
      BufferedImage localBufferedImage = paramBufferedImage;
      paramBufferedImage = new BufferedImage(localBufferedImage.getWidth(), localBufferedImage.getHeight(), paramInt);
      ColorConvertOp localColorConvertOp = new ColorConvertOp(null);
      localColorConvertOp.filter(localBufferedImage, paramBufferedImage);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " converting image");
    }
    return paramBufferedImage;
  }

  private static double clip01(double paramDouble)
  {
    if (paramDouble < 0.0D)
      paramDouble = 0.0D;
    if (paramDouble > 1.0D)
      paramDouble = 1.0D;
    return paramDouble;
  }

  public static WritableRaster createCompatibleWritableRaaster(ColorModel paramColorModel, int paramInt1, int paramInt2)
  {
    return paramColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
  }

  public static Raster createInterleavedRaster(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    DataBufferByte localDataBufferByte = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
    int[] arrayOfInt = { 0, 1, 2 };
    return Raster.createInterleavedRaster(localDataBufferByte, paramInt1, paramInt2, paramInt1 * 3, 3, arrayOfInt, null);
  }

  public static void drawImage(Graphics2D paramGraphics2D, BufferedImage paramBufferedImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
  {
    paramGraphics2D.drawImage(paramBufferedImage, paramAffineTransform, paramImageObserver);
  }

  public static void flatten4bpc(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    int k = 0;
    int m = 0;
    int n = (paramInt1 & 0x1) == 1 ? 1 : 0;
    for (int i1 = 0; i1 < i; i1++)
    {
      int j = paramArrayOfByte1[i1];
      m += 2;
      paramArrayOfByte2[k] = ((byte)(j & 0xF0));
      if (paramArrayOfByte2[k] == -16)
        paramArrayOfByte2[k] = -1;
      k++;
      if ((n != 0) && (m > paramInt1))
      {
        m = 0;
      }
      else
      {
        paramArrayOfByte2[k] = ((byte)((j & 0xF) << 4));
        if (paramArrayOfByte2[k] == -16)
          paramArrayOfByte2[k] = -1;
        k++;
      }
      if (k == paramInt2)
        i1 = i;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ColorSpaceConvertor
 * JD-Core Version:    0.6.2
 */
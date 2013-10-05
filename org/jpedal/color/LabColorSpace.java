package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class LabColorSpace extends GenericColorSpace
{
  private int r;
  private int g;
  private int b;
  private float lastL = -1.0F;
  private float lastA = 65536.0F;
  private float lastBstar;
  private Map cache = new HashMap();
  private static final float C1 = 0.1284186F;
  private static final float C2 = 0.137931F;
  private static final float C3 = 0.2068966F;
  private static final float C4 = 0.3921569F;
  private static final float C5 = 128.0F;

  public LabColorSpace(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.value = 1847602;
    setCIEValues(paramArrayOfFloat1, paramArrayOfFloat2, null, null);
  }

  public byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    this.isConverted = true;
    int i = paramArrayOfByte.length;
    int j = 0;
    while (j < i)
    {
      float f1 = (paramArrayOfByte[j] & 0xFF) * 0.3921569F;
      float f2 = (paramArrayOfByte[(j + 1)] & 0xFF) - 128.0F;
      float f3 = (paramArrayOfByte[(j + 2)] & 0xFF) - 128.0F;
      convertToRGB(f1, f2, f3);
      paramArrayOfByte[j] = ((byte)this.r);
      paramArrayOfByte[(j + 1)] = ((byte)this.g);
      paramArrayOfByte[(j + 2)] = ((byte)this.b);
      j += 3;
    }
    return paramArrayOfByte;
  }

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    BufferedImage localBufferedImage;
    try
    {
      Raster localRaster = DefaultImageHelper.readRasterFromJPeg(paramArrayOfByte);
      localRaster = cleanupRaster(localRaster, paramInt3, paramInt4, 3);
      int i = localRaster.getWidth();
      int j = localRaster.getHeight();
      int k = i * j;
      byte[] arrayOfByte = ((DataBufferByte)localRaster.getDataBuffer()).getData();
      int m = 0;
      while (m < k * 3)
      {
        float f1 = (arrayOfByte[m] & 0xFF) * 0.3921569F;
        float f2 = (arrayOfByte[(m + 1)] & 0xFF) - 128.0F;
        float f3 = (arrayOfByte[(m + 2)] & 0xFF) - 128.0F;
        convertToRGB(f1, f2, f3);
        arrayOfByte[m] = ((byte)this.r);
        arrayOfByte[(m + 1)] = ((byte)this.g);
        arrayOfByte[(m + 2)] = ((byte)this.b);
        m += 3;
      }
      localBufferedImage = new BufferedImage(i, j, 1);
      DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
      int[] arrayOfInt = { 0, 1, 2 };
      WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, i, j, i * 3, 3, arrayOfInt, null);
      localBufferedImage.setData(localWritableRaster);
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException);
    }
    return localBufferedImage;
  }

  public BufferedImage dataToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1 * paramInt2;
    BufferedImage localBufferedImage;
    try
    {
      int j = 0;
      while (j < i * 3)
      {
        float f1 = (paramArrayOfByte[j] & 0xFF) * 0.3921569F;
        float f2 = (paramArrayOfByte[(j + 1)] & 0xFF) - 128.0F;
        float f3 = (paramArrayOfByte[(j + 2)] & 0xFF) - 128.0F;
        convertToRGB(f1, f2, f3);
        paramArrayOfByte[j] = ((byte)this.r);
        paramArrayOfByte[(j + 1)] = ((byte)this.g);
        paramArrayOfByte[(j + 2)] = ((byte)this.b);
        j += 3;
      }
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      Raster localRaster = ColorSpaceConvertor.createInterleavedRaster(paramArrayOfByte, paramInt1, paramInt2);
      localBufferedImage.setData(localRaster);
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException);
    }
    return localBufferedImage;
  }

  private void convertToRGB(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < 0.0F)
      paramFloat1 = 0.0F;
    else if (paramFloat1 > 100.0F)
      paramFloat1 = 100.0F;
    if (paramFloat2 < this.R[0])
      paramFloat2 = this.R[0];
    else if (paramFloat2 > this.R[1])
      paramFloat2 = this.R[1];
    if (paramFloat3 < this.R[2])
      paramFloat3 = this.R[2];
    else if (paramFloat3 > this.R[3])
      paramFloat3 = this.R[3];
    if ((this.lastL != paramFloat1) || (this.lastA != paramFloat2) || (this.lastBstar != paramFloat3))
    {
      int i = (int)paramFloat1;
      int j = (int)(paramFloat2 - this.R[0]);
      int k = (int)(paramFloat3 - this.R[2]);
      Integer localInteger = Integer.valueOf((i << 16) + (j << 8) + k);
      Object localObject = this.cache.get(localInteger);
      if (localObject != null)
      {
        int m = ((Integer)localObject).intValue();
        this.r = (m >> 16 & 0xFF);
        this.g = (m >> 8 & 0xFF);
        this.b = (m & 0xFF);
      }
      else
      {
        double d = (paramFloat1 + 16.0D) / 116.0D;
        double[] arrayOfDouble = new double[3];
        arrayOfDouble[0] = (d + paramFloat2 / 500.0D);
        arrayOfDouble[1] = d;
        arrayOfDouble[2] = (d - paramFloat3 / 200.0D);
        float[] arrayOfFloat = new float[3];
        for (int n = 0; n < 3; n++)
        {
          if (arrayOfDouble[n] >= 0.2068965584039688D)
            arrayOfFloat[n] = ((float)(this.W[n] * arrayOfDouble[n] * arrayOfDouble[n] * arrayOfDouble[n]));
          else
            arrayOfFloat[n] = ((float)(this.W[n] * 0.1284186F * (arrayOfDouble[n] - 0.1379310339689255D)));
          if (arrayOfFloat[n] < 0.0F)
            arrayOfFloat[n] = 0.0F;
        }
        arrayOfFloat = this.cs.toRGB(arrayOfFloat);
        this.r = ((int)(arrayOfFloat[0] * 255.0F));
        this.g = ((int)(arrayOfFloat[1] * 255.0F));
        this.b = ((int)(arrayOfFloat[2] * 255.0F));
        if (this.r < 0)
          this.r = 0;
        if (this.g < 0)
          this.g = 0;
        if (this.b < 0)
          this.b = 0;
        if (this.r > 255)
          this.r = 255;
        if (this.g > 255)
          this.g = 255;
        if (this.b > 255)
          this.b = 255;
        n = (this.r << 16) + (this.g << 8) + this.b;
        this.cache.put(localInteger, Integer.valueOf(n));
      }
      this.lastL = paramFloat1;
      this.lastA = paramFloat2;
      this.lastBstar = paramFloat3;
    }
  }

  public final void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = Float.parseFloat(paramArrayOfString[i]);
    setColor(arrayOfFloat, paramInt);
  }

  public final void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    float f1 = paramArrayOfFloat[0];
    float f2 = paramArrayOfFloat[1];
    float f3 = paramArrayOfFloat[2];
    convertToRGB(f1, f2, f3);
    this.currentColor = new PdfColor(this.r, this.g, this.b);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.LabColorSpace
 * JD-Core Version:    0.6.2
 */
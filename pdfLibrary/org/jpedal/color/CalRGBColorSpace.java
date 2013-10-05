package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jpedal.utils.LogWriter;

public class CalRGBColorSpace extends GenericColorSpace
{
  private static final long serialVersionUID = 4569336292751894930L;
  private int r;
  private int g;
  private int b;
  private static final double[][] xyzrgb = { { 3.240449D, -1.537136D, -0.498531D }, { -0.969265D, 1.876011D, 0.041556D }, { 0.055643D, -0.204026D, 1.057229D } };
  private float lastC = -255.0F;
  private float lastI = -255.0F;
  private float lastE = -255.0F;

  public CalRGBColorSpace(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    this.cs = ColorSpace.getInstance(1001);
    setCIEValues(paramArrayOfFloat1, null, paramArrayOfFloat2, paramArrayOfFloat3);
    this.value = 1008872003;
  }

  public final BufferedImage dataToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    DataBufferByte localDataBufferByte = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
    int i = paramInt1 * paramInt2;
    BufferedImage localBufferedImage;
    try
    {
      int j = 0;
      while (j < i * 3)
      {
        float f1 = paramArrayOfByte[j] & 0xFF;
        float f2 = paramArrayOfByte[(j + 1)] & 0xFF;
        float f3 = paramArrayOfByte[(j + 2)] & 0xFF;
        convertToRGB(f1, f2, f3);
        localDataBufferByte.setElem(j, this.r);
        localDataBufferByte.setElem(j + 1, this.g);
        localDataBufferByte.setElem(j + 2, this.b);
        j += 3;
      }
      int[] arrayOfInt = { 0, 1, 2 };
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, paramInt1, paramInt2, paramInt1 * 3, 3, arrayOfInt, null);
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

  public final void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = Float.parseFloat(paramArrayOfString[i]);
    setColor(arrayOfFloat, paramInt);
  }

  public final void reset()
  {
    this.lastC = -255.0F;
    this.lastI = -255.0F;
    this.lastE = -255.0F;
    this.r = 0;
    this.g = 0;
    this.b = 0;
    this.currentColor = new PdfColor(0, 0, 0);
  }

  public final void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    float[] arrayOfFloat = { 1.0F, 1.0F, 1.0F };
    int i;
    if (paramInt == 3)
    {
      for (i = 0; i < paramInt; i++)
      {
        arrayOfFloat[i] = paramArrayOfFloat[i];
        if (arrayOfFloat[i] > 1.0F)
          return;
      }
      convertToRGB(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
      this.currentColor = new PdfColor(this.r, this.g, this.b);
    }
    else if ((paramInt == 1) && (getIndexedMap() != null))
    {
      i = (int)(paramArrayOfFloat[0] * 3.0F);
      byte[] arrayOfByte = getIndexedMap();
      arrayOfFloat[0] = ((arrayOfByte[i] & 0xFF) / 255.0F);
      arrayOfFloat[1] = ((arrayOfByte[(i + 1)] & 0xFF) / 255.0F);
      arrayOfFloat[2] = ((arrayOfByte[(i + 2)] & 0xFF) / 255.0F);
      convertToRGB(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
      this.currentColor = new PdfColor(this.r, this.g, this.b);
    }
  }

  private final void convertToRGB(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if ((this.lastC != paramFloat1) || (this.lastI != paramFloat2) || (this.lastE != paramFloat3))
    {
      double d1 = Math.pow(paramFloat1, this.G[0]);
      double d2 = Math.pow(paramFloat2, this.G[1]);
      double d3 = Math.pow(paramFloat3, this.G[2]);
      double d4 = this.Ma[0] * d1 + this.Ma[3] * d2 + this.Ma[6] * d3;
      double d5 = this.Ma[1] * d1 + this.Ma[4] * d2 + this.Ma[7] * d3;
      double d6 = this.Ma[2] * d1 + this.Ma[5] * d2 + this.Ma[8] * d3;
      double d7 = xyzrgb[0][0] * d4 + xyzrgb[0][1] * d5 + xyzrgb[0][2] * d6;
      double d8 = xyzrgb[1][0] * d4 + xyzrgb[1][1] * d5 + xyzrgb[1][2] * d6;
      double d9 = xyzrgb[2][0] * d4 + xyzrgb[2][1] * d5 + xyzrgb[2][2] * d6;
      double d10 = 1.0D / (xyzrgb[0][0] * this.W[0] + xyzrgb[0][1] * this.W[1] + xyzrgb[0][2] * this.W[2]);
      double d11 = 1.0D / (xyzrgb[1][0] * this.W[0] + xyzrgb[1][1] * this.W[1] + xyzrgb[1][2] * this.W[2]);
      double d12 = 1.0D / (xyzrgb[2][0] * this.W[0] + xyzrgb[2][1] * this.W[1] + xyzrgb[2][2] * this.W[2]);
      this.r = ((int)(255.0D * Math.pow(clip(d7 * d10), 0.5D)));
      this.g = ((int)(255.0D * Math.pow(clip(d8 * d11), 0.5D)));
      this.b = ((int)(255.0D * Math.pow(clip(d9 * d12), 0.5D)));
      this.lastC = paramFloat1;
      this.lastI = paramFloat2;
      this.lastE = paramFloat3;
    }
  }

  private static double clip(double paramDouble)
  {
    if (paramDouble > 1.0D)
      return 1.0D;
    return paramDouble;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.CalRGBColorSpace
 * JD-Core Version:    0.6.2
 */
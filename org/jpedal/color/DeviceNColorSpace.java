package org.jpedal.color;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.utils.LogWriter;

public class DeviceNColorSpace extends SeparationColorSpace
{
  private static final long serialVersionUID = -1372268945371555187L;
  private Map cache = new HashMap();

  public DeviceNColorSpace(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    this.value = 960981604;
    processColorToken(paramPdfObjectReader, paramPdfObject);
  }

  public void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = Float.parseFloat(paramArrayOfString[i]);
    setColor(arrayOfFloat, paramInt);
  }

  public void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    int[] arrayOfInt = new int[3];
    int i = paramArrayOfFloat.length;
    if (i > 3)
      i = 3;
    for (int j = 0; j < i; j++)
      arrayOfInt[j] = ((int)(paramArrayOfFloat[j] * 255.0F));
    j = 0;
    Object localObject;
    if ((this.cmykMapping == 1009857357) && (paramInt == 1))
    {
      localObject = new float[] { 0.0F, 0.0F, 0.0F, paramArrayOfFloat[0] };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((paramInt < 4) && (this.cache.get(Integer.valueOf((arrayOfInt[0] << 16) + (arrayOfInt[1] << 8) + arrayOfInt[2])) != null))
    {
      j = 1;
      localObject = this.cache.get(Integer.valueOf((arrayOfInt[0] << 16) + (arrayOfInt[1] << 8) + arrayOfInt[2]));
      int m = ((Integer)localObject).intValue();
      int n = m >> 16 & 0xFF;
      int i1 = m >> 8 & 0xFF;
      int i2 = m & 0xFF;
      this.altCS.currentColor = new PdfColor(n, i1, i2);
    }
    else if ((this.cmykMapping == 9) && (paramInt == 4))
    {
      localObject = new float[] { paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2], paramArrayOfFloat[3] };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 7) && (paramInt == 6))
    {
      localObject = new float[] { paramArrayOfFloat[5], paramArrayOfFloat[4], paramArrayOfFloat[3], paramArrayOfFloat[2] };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 1) && (paramInt == 3))
    {
      localObject = new float[] { 0.0F, paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2] };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 2) && (paramInt == 3))
    {
      localObject = new float[] { paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2], 0.0F };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 4) && (paramInt == 3))
    {
      localObject = new float[] { paramArrayOfFloat[0], paramArrayOfFloat[1], 0.0F, paramArrayOfFloat[2] };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 5) && (paramInt == 2))
    {
      localObject = new float[] { paramArrayOfFloat[0], 0.0F, paramArrayOfFloat[1], 0.0F };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 8) && (paramInt == 2))
    {
      localObject = new float[] { paramArrayOfFloat[0], paramArrayOfFloat[1], 0.0F, 0.0F };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else if ((this.cmykMapping == 6) && (paramInt == 2))
    {
      localObject = new float[] { 0.0F, paramArrayOfFloat[0], paramArrayOfFloat[1], 0.0F };
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    else
    {
      localObject = this.colorMapper.getOperandFloat(paramArrayOfFloat);
      this.altCS.setColor((float[])localObject, localObject.length);
    }
    if (j == 0)
    {
      this.altCS.getColor().getRGB();
      int k = this.altCS.getColor().getRGB();
      this.cache.put(Integer.valueOf((arrayOfInt[0] << 16) + (arrayOfInt[1] << 8) + arrayOfInt[2]), Integer.valueOf(k));
    }
  }

  public BufferedImage dataToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage;
    try
    {
      localBufferedImage = createImage(paramInt1, paramInt2, paramArrayOfByte);
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't convert DeviceN colorspace data: " + localException);
    }
    return localBufferedImage;
  }

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    BufferedImage localBufferedImage = null;
    try
    {
      Raster localRaster = DefaultIO.getRasterFromJPEG(paramArrayOfByte, "JPEG");
      if (localRaster != null)
      {
        localRaster = cleanupRaster(localRaster, paramInt3, paramInt4, this.componentCount);
        int i = localRaster.getWidth();
        int j = localRaster.getHeight();
        DataBufferByte localDataBufferByte = (DataBufferByte)localRaster.getDataBuffer();
        localBufferedImage = createImage(i, j, localDataBufferByte.getData());
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException);
      localException.printStackTrace();
    }
    return localBufferedImage;
  }

  private BufferedImage createImage(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramInt1 * paramInt2 * 3];
    int i = paramArrayOfByte.length;
    int j = paramArrayOfByte.length / this.componentCount;
    float[] arrayOfFloat = new float[this.componentCount];
    int k = 0;
    int m = 0;
    for (int n = 0; (n < j) && (k < i); n++)
    {
      for (int i1 = 0; i1 < this.componentCount; i1++)
      {
        arrayOfFloat[i1] = ((paramArrayOfByte[k] & 0xFF) / 255.0F);
        k++;
      }
      setColor(arrayOfFloat, this.componentCount);
      i1 = this.altCS.currentColor.getRGB();
      arrayOfByte[m] = ((byte)(i1 >> 16 & 0xFF));
      arrayOfByte[(m + 1)] = ((byte)(i1 >> 8 & 0xFF));
      arrayOfByte[(m + 2)] = ((byte)(i1 & 0xFF));
      m += 3;
    }
    int[] arrayOfInt = { 0, 1, 2 };
    BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
    DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
    WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, paramInt1, paramInt2, paramInt1 * 3, 3, arrayOfInt, null);
    localBufferedImage.setData(localWritableRaster);
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.DeviceNColorSpace
 * JD-Core Version:    0.6.2
 */
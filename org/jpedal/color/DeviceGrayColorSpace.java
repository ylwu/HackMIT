package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jpedal.objects.raw.MaskObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.utils.LogWriter;

public class DeviceGrayColorSpace extends GenericColorSpace
{
  private static final long serialVersionUID = -8160089076145994695L;

  public DeviceGrayColorSpace()
  {
    this.value = 1568372915;
    this.cs = ColorSpace.getInstance(1003);
  }

  public final void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[1];
    arrayOfFloat[0] = Float.parseFloat(paramArrayOfString[0]);
    setColor(arrayOfFloat, 1);
  }

  public final void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    float f = paramArrayOfFloat[0];
    int i;
    if (f <= 1.0F)
      i = (int)(255.0F * f);
    else
      i = (int)f;
    if (i < 0)
      i = 0;
    this.currentColor = new PdfColor(i, i, i);
  }

  public byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    this.isConverted = true;
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[i * 3];
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfByte[j];
      for (int m = 0; m < 3; m++)
        arrayOfByte[(j * 3 + m)] = k;
    }
    return arrayOfByte;
  }

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    if ((paramPdfObject instanceof MaskObject))
      return super.JPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4, paramBoolean, paramPdfObject);
    BufferedImage localBufferedImage = null;
    try
    {
      Raster localRaster = DefaultIO.getRasterFromJPEG(paramArrayOfByte, "JPEG");
      if (localRaster != null)
      {
        localRaster = cleanupRaster(localRaster, paramInt3, paramInt4, 1);
        int i = localRaster.getWidth();
        int j = localRaster.getHeight();
        DataBufferByte localDataBufferByte = (DataBufferByte)localRaster.getDataBuffer();
        byte[] arrayOfByte1 = localDataBufferByte.getData();
        int k = arrayOfByte1.length;
        byte[] arrayOfByte2 = new byte[k * 3];
        int m = 0;
        for (int n = 0; n < k; n++)
        {
          if (paramBoolean)
            arrayOfByte1[n] = ((byte)(arrayOfByte1[n] ^ 0xFF));
          arrayOfByte2[m] = arrayOfByte1[n];
          m++;
          arrayOfByte2[m] = arrayOfByte1[n];
          m++;
          arrayOfByte2[m] = arrayOfByte1[n];
          m++;
        }
        int[] arrayOfInt = { 0, 1, 2 };
        localBufferedImage = new BufferedImage(i, j, 1);
        WritableRaster localWritableRaster = Raster.createInterleavedRaster(new DataBufferByte(arrayOfByte2, arrayOfByte2.length), i, j, i * 3, 3, arrayOfInt, null);
        localBufferedImage.setData(localWritableRaster);
      }
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException);
    }
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.DeviceGrayColorSpace
 * JD-Core Version:    0.6.2
 */
package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class ICCColorSpace extends GenericColorSpace
{
  private int[] a1 = new int[256];
  private int[] b1 = new int[256];
  private int[] c1 = new int[256];
  private Map cache = new HashMap();

  public void reset()
  {
    super.reset();
    this.isConverted = false;
  }

  public ICCColorSpace(PdfObject paramPdfObject)
  {
    for (int i = 0; i < 256; i++)
    {
      this.a1[i] = -1;
      this.b1[i] = -1;
      this.c1[i] = -1;
    }
    this.value = 1247168582;
    this.cs = ColorSpace.getInstance(1000);
    byte[] arrayOfByte = paramPdfObject.getDecodedStream();
    if (arrayOfByte == null)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Error in ICC data");
    }
    else
      try
      {
        this.cs = new ICC_ColorSpace(ICC_Profile.getInstance(arrayOfByte));
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("[PDF] Problem " + localException.getMessage() + " with ICC data ");
        this.failed = true;
      }
    this.componentCount = this.cs.getNumComponents();
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
    Object localObject1 = new float[paramInt];
    int[] arrayOfInt = new int[paramInt];
    this.rawValues = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
    {
      float f = paramArrayOfFloat[i];
      this.rawValues[i] = f;
      localObject1[i] = f;
      if (f > 1.0F)
        arrayOfInt[i] = ((int)f);
      else
        arrayOfInt[i] = ((int)(f * 255.0F));
    }
    if ((paramInt == 3) && (this.a1[arrayOfInt[0]] != -1) && (this.b1[arrayOfInt[1]] != -1) && (this.c1[arrayOfInt[2]] != -1))
    {
      this.currentColor = new PdfColor(this.a1[arrayOfInt[0]], this.b1[arrayOfInt[1]], this.c1[arrayOfInt[2]]);
    }
    else if ((paramInt == 4) && (this.cache.get(Integer.valueOf((arrayOfInt[0] << 24) + (arrayOfInt[1] << 16) + (arrayOfInt[2] << 8) + arrayOfInt[3])) != null))
    {
      Object localObject2 = this.cache.get(Integer.valueOf((arrayOfInt[0] << 24) + (arrayOfInt[1] << 16) + (arrayOfInt[2] << 8) + arrayOfInt[3]));
      int k = ((Integer)localObject2).intValue();
      int m = k >> 16 & 0xFF;
      int n = k >> 8 & 0xFF;
      int i1 = k & 0xFF;
      this.currentColor = new PdfColor(m, n, i1);
    }
    else
    {
      try
      {
        localObject1 = this.cs.toRGB((float[])localObject1);
      }
      catch (Exception localException)
      {
        float[] arrayOfFloat = { localObject1[0], localObject1[0], localObject1[0] };
        localObject1 = arrayOfFloat;
      }
      this.currentColor = new PdfColor(localObject1[0], localObject1[1], localObject1[2]);
      if (paramInt == 3)
      {
        this.a1[arrayOfInt[0]] = ((int)(localObject1[0] * 255.0F));
        this.b1[arrayOfInt[1]] = ((int)(localObject1[1] * 255.0F));
        this.c1[arrayOfInt[2]] = ((int)(localObject1[2] * 255.0F));
      }
      else if (paramInt == 4)
      {
        int j = ((int)(localObject1[0] * 255.0F) << 16) + ((int)(localObject1[1] * 255.0F) << 8) + (int)(localObject1[2] * 255.0F);
        this.cache.put(Integer.valueOf((arrayOfInt[0] << 24) + (arrayOfInt[1] << 16) + (arrayOfInt[2] << 8) + arrayOfInt[3]), Integer.valueOf(j));
      }
    }
  }

  public byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    this.isConverted = true;
    if (this.componentCount == 4)
      return convert4Index(paramArrayOfByte);
    return paramArrayOfByte;
  }

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    if ((paramArrayOfByte.length > 9) && (paramArrayOfByte[6] == 74) && (paramArrayOfByte[7] == 70) && (paramArrayOfByte[8] == 73) && (paramArrayOfByte[9] == 70))
      return nonRGBJPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, null, paramInt3, paramInt4);
    return algorithmicICCToRGB(paramArrayOfByte, paramInt1, paramInt2, false, paramInt3, paramInt4, paramArrayOfFloat);
  }

  public BufferedImage JPEG2000ToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4)
    throws PdfException
  {
    byte[] arrayOfByte = getIndexedMap();
    if ((this.cs.getNumComponents() == 3) || (arrayOfByte != null))
      return super.JPEG2000ToRGBImage(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4);
    BufferedImage localBufferedImage;
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      ImageReader localImageReader = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG2000").next();
      ImageInputStream localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      Object localObject2;
      Object localObject1;
      if (arrayOfByte != null)
      {
        if (!isIndexConverted())
          arrayOfByte = convertIndexToRGB(arrayOfByte);
        localObject2 = localImageReader.readAsRenderedImage(0, localImageReader.getDefaultReadParam());
        localObject1 = ((RenderedImage)localObject2).getData();
        IndexColorModel localIndexColorModel = new IndexColorModel(8, arrayOfByte.length / 3, arrayOfByte, 0, false);
        localBufferedImage = new BufferedImage(localIndexColorModel, ((Raster)localObject1).createCompatibleWritableRaster(), false, null);
        localBufferedImage = cleanupImage(localBufferedImage, paramInt3, paramInt4);
      }
      else
      {
        localBufferedImage = localImageReader.read(0);
        localObject1 = localBufferedImage.getRaster();
        if (paramArrayOfFloat != null)
          if (((paramArrayOfFloat.length == 6) && (paramArrayOfFloat[0] == 1.0F) && (paramArrayOfFloat[1] == 0.0F) && (paramArrayOfFloat[2] == 1.0F) && (paramArrayOfFloat[3] == 0.0F) && (paramArrayOfFloat[4] == 1.0F) && (paramArrayOfFloat[5] == 0.0F)) || ((paramArrayOfFloat.length > 2) && (paramArrayOfFloat[0] == 1.0F) && (paramArrayOfFloat[1] == 0.0F)))
          {
            localObject2 = ((Raster)localObject1).getDataBuffer();
            int i = ((DataBuffer)localObject2).getSize();
            for (int j = 0; j < i; j++)
              ((DataBuffer)localObject2).setElem(j, 255 - ((DataBuffer)localObject2).getElem(j));
          }
          else if (((paramArrayOfFloat.length == 6) && (paramArrayOfFloat[0] == 0.0F) && (paramArrayOfFloat[1] == 1.0F) && (paramArrayOfFloat[2] == 0.0F) && (paramArrayOfFloat[3] == 1.0F) && (paramArrayOfFloat[4] == 0.0F) && (paramArrayOfFloat[5] == 1.0F)) || (paramArrayOfFloat == null) || (paramArrayOfFloat.length <= 0));
        localObject1 = cleanupRaster((Raster)localObject1, paramInt3, paramInt4, 4);
        paramInt1 = ((Raster)localObject1).getWidth();
        paramInt2 = ((Raster)localObject1).getHeight();
        if (localBufferedImage.getType() == 13)
        {
          localObject2 = ColorSpaceConvertor.createCompatibleWritableRaaster(localBufferedImage.getColorModel(), paramInt1, paramInt2);
          CSToRGB = new ColorConvertOp(this.cs, localBufferedImage.getColorModel().getColorSpace(), ColorSpaces.hints);
          localBufferedImage = new BufferedImage(paramInt1, paramInt2, localBufferedImage.getType());
        }
        else if (localBufferedImage.getType() != 10)
        {
          if (CSToRGB == null)
            initCMYKColorspace();
          localObject2 = ColorSpaceConvertor.createCompatibleWritableRaaster(rgbModel, paramInt1, paramInt2);
          CSToRGB = new ColorConvertOp(this.cs, rgbCS, ColorSpaces.hints);
          CSToRGB.filter((Raster)localObject1, (WritableRaster)localObject2);
          localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
        }
        else
        {
          localObject2 = null;
        }
        if (localObject2 != null)
          localBufferedImage.setData((Raster)localObject2);
      }
      localImageReader.dispose();
      localImageInputStream.close();
      localByteArrayInputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localException);
      localException.printStackTrace();
      throw new PdfException("Exception " + localException + " with JPEG2000 image - please ensure imageio.jar (see http://www.idrsolutions.com/additional-jars/) on classpath");
    }
    catch (Error localError)
    {
      localError.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000 with error " + localError);
      throw new PdfException("Error with JPEG2000 image - please ensure imageio.jar (see http://www.idrsolutions.com/additional-jars/) on classpath");
    }
    return localBufferedImage;
  }

  private BufferedImage algorithmicICCToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4, float[] paramArrayOfFloat)
  {
    BufferedImage localBufferedImage = null;
    ImageReader localImageReader = null;
    ImageInputStream localImageInputStream = null;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    try
    {
      Iterator localIterator = ImageIO.getImageReadersByFormatName("JPEG");
      while (localIterator.hasNext())
      {
        localObject1 = localIterator.next();
        localImageReader = (ImageReader)localObject1;
        if (localImageReader.canReadRaster())
          break;
      }
      ImageIO.setUseCache(false);
      localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      Object localObject1 = localImageReader.readRaster(0, null);
      if ((localImageReader.getRawImageType(0) == null) || (this.alternative == -1))
      {
        localObject2 = nonRGBJPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4);
        return localObject2;
      }
      localObject1 = cleanupRaster((Raster)localObject1, paramInt3, paramInt4, this.componentCount);
      paramInt1 = ((Raster)localObject1).getWidth();
      paramInt2 = ((Raster)localObject1).getHeight();
      Object localObject2 = new byte[paramInt1 * paramInt2 * 3];
      paramArrayOfByte = ((DataBufferByte)((Raster)localObject1).getDataBuffer()).getData();
      int i = paramInt1 * paramInt2 * 3;
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3 = 0.0F;
      int j = 0;
      float f4 = -1.0F;
      float f5 = -1.0F;
      float f6 = -1.0F;
      int k = 0;
      while (k < i)
      {
        float f7 = (paramArrayOfByte[k] & 0xFF) / 255.0F;
        float f8 = (paramArrayOfByte[(1 + k)] & 0xFF) / 255.0F;
        float f9 = (paramArrayOfByte[(2 + k)] & 0xFF) / 255.0F;
        if ((f4 != f7) || (f5 != f8) || (f6 != f9))
        {
          if (paramBoolean)
            System.out.println(f7 + " " + f8 + ' ' + f9);
          float[] arrayOfFloat2 = { f7, f8, f9 };
          float[] arrayOfFloat1 = this.cs.toRGB(arrayOfFloat2);
          f1 = arrayOfFloat1[0] * 255.0F;
          f2 = arrayOfFloat1[1] * 255.0F;
          f3 = arrayOfFloat1[2] * 255.0F;
          f4 = f7;
          f5 = f8;
          f6 = f9;
        }
        localObject2[(j++)] = ((byte)(int)f1);
        localObject2[(j++)] = ((byte)(int)f2);
        localObject2[(j++)] = ((byte)(int)f3);
        k += 3;
      }
      int[] arrayOfInt = { 0, 1, 2 };
      DataBufferByte localDataBufferByte = new DataBufferByte((byte[])localObject2, localObject2.length);
      localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
      WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, paramInt1, paramInt2, paramInt1 * 3, 3, arrayOfInt, null);
      localBufferedImage.setData(localWritableRaster);
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem with color conversion");
    }
    finally
    {
      try
      {
        localByteArrayInputStream.close();
        localImageReader.dispose();
        localImageInputStream.close();
      }
      catch (Exception localException5)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Problem closing  " + localException5);
      }
    }
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.ICCColorSpace
 * JD-Core Version:    0.6.2
 */
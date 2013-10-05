package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class DeviceCMYKColorSpace extends GenericColorSpace
{
  private static final long serialVersionUID = 4054062852632000027L;
  private float lastC = -1.0F;
  private float lastM = -1.0F;
  private float lastY = -1.0F;
  private float lastK = -1.0F;
  public static ColorSpace CMYK = null;

  public void clearCache()
  {
    this.lastC = -1.0F;
  }

  private void initColorspace()
  {
    Object localObject1 = null;
    try
    {
      String str = System.getProperty("org.jpedal.CMYKprofile");
      if (str == null)
        localObject1 = getClass().getResourceAsStream("/org/jpedal/res/cmm/cmyk.icm");
      else
        try
        {
          localObject1 = new FileInputStream(str);
        }
        catch (Exception localException2)
        {
          throw new PdfException("PdfException attempting to use user profile " + str + " Message=" + localException2);
        }
      ICC_Profile localICC_Profile = ICC_Profile.getInstance((InputStream)localObject1);
      CMYK = new ICC_ColorSpace(localICC_Profile);
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException1);
      throw new RuntimeException("Problem setting CMYK Colorspace with message " + localException1 + " Possible cause file cmyk.icm corrupted");
    }
    finally
    {
      if (localObject1 != null)
        try
        {
          ((InputStream)localObject1).close();
        }
        catch (IOException localIOException2)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception " + localIOException2);
        }
    }
  }

  public DeviceCMYKColorSpace()
  {
    this.componentCount = 4;
    if (CMYK == null)
      initColorspace();
    this.cs = CMYK;
    this.value = 1498837125;
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
    int i = 1;
    this.c = 1.0F;
    this.y = 1.0F;
    this.m = 1.0F;
    this.k = 1.0F;
    if (paramInt > 3)
    {
      this.c = paramArrayOfFloat[0];
      this.m = paramArrayOfFloat[1];
      this.y = paramArrayOfFloat[2];
      this.k = paramArrayOfFloat[3];
    }
    else
    {
      if (paramInt > 0)
        this.c = paramArrayOfFloat[0];
      if (paramInt > 1)
        this.m = paramArrayOfFloat[1];
      if (paramInt > 2)
        this.y = paramArrayOfFloat[2];
      if (paramInt > 3)
        this.k = paramArrayOfFloat[3];
    }
    if ((this.lastC != this.c) || (this.lastM != this.m) || (this.lastY != this.y) || (this.lastK != this.k))
    {
      this.rawValues = new float[4];
      this.rawValues[0] = this.c;
      this.rawValues[1] = this.m;
      this.rawValues[2] = this.y;
      this.rawValues[3] = this.k;
      if (i == 0)
      {
        float f1 = this.c + this.k;
        if (f1 > 1.0F)
          f1 = 1.0F;
        float f2 = this.m + this.k;
        if (f2 > 1.0F)
          f2 = 1.0F;
        float f3 = this.y + this.k;
        if (f3 > 1.0F)
          f3 = 1.0F;
        this.currentColor = new PdfColor((int)(255.0F * (1.0F - f1)), (int)(255.0F * (1.0F - f2)), (int)(255.0F * (1.0F - f3)));
      }
      else if ((this.c == 0.0F) && (this.y == 0.0F) && (this.m == 0.0F) && (this.k == 0.0F))
      {
        this.currentColor = new PdfColor(1.0F, 1.0F, 1.0F);
      }
      else
      {
        if (this.c > 0.99D)
          this.c = 1.0F;
        else if (this.c < 0.01D)
          this.c = 0.0F;
        if (this.m > 0.99D)
          this.m = 1.0F;
        else if (this.m < 0.01D)
          this.m = 0.0F;
        if (this.y > 0.99D)
          this.y = 1.0F;
        else if (this.y < 0.01D)
          this.y = 0.0F;
        if (this.k > 0.99D)
          this.k = 1.0F;
        else if (this.k < 0.01D)
          this.k = 0.0F;
        float[] arrayOfFloat1 = null;
        if (arrayOfFloat1 == null)
        {
          float[] arrayOfFloat2 = { this.c, this.m, this.y, this.k };
          arrayOfFloat1 = CMYK.toRGB(arrayOfFloat2);
          for (int j = 0; j < 3; j++)
            if (arrayOfFloat1[j] > 0.99D)
              arrayOfFloat1[j] = 1.0F;
            else if (arrayOfFloat1[j] < 0.01D)
              arrayOfFloat1[j] = 0.0F;
        }
        this.currentColor = new PdfColor(arrayOfFloat1[0], arrayOfFloat1[1], arrayOfFloat1[2]);
      }
      this.lastC = this.c;
      this.lastM = this.m;
      this.lastY = this.y;
      this.lastK = this.k;
    }
  }

  public final BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    return nonRGBJPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramInt4);
  }

  public BufferedImage JPEG2000ToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4)
    throws PdfException
  {
    BufferedImage localBufferedImage;
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      ImageReader localImageReader = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG2000").next();
      ImageInputStream localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      byte[] arrayOfByte = getIndexedMap();
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
        localBufferedImage.setData((Raster)localObject1);
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
        else
        {
          if (CSToRGB == null)
            initCMYKColorspace();
          localObject2 = ColorSpaceConvertor.createCompatibleWritableRaaster(rgbModel, paramInt1, paramInt2);
          CSToRGB = new ColorConvertOp(this.cs, rgbCS, ColorSpaces.hints);
          CSToRGB.filter((Raster)localObject1, (WritableRaster)localObject2);
          localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
        }
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

  public final byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    this.isConverted = true;
    return convert4Index(paramArrayOfByte);
  }

  public static ColorSpace getColorSpaceInstance()
  {
    Object localObject = new DeviceCMYKColorSpace().getColorSpace();
    String str = System.getProperty("org.jpedal.CMYKprofile");
    if (str != null)
      try
      {
        localObject = new ICC_ColorSpace(ICC_Profile.getInstance(new FileInputStream(str)));
      }
      catch (Exception localException)
      {
        throw new RuntimeException("Unable to create CMYK colorspace with  " + str + "\nPlease check Path and file valid or use built-in");
      }
    return localObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.DeviceCMYKColorSpace
 * JD-Core Version:    0.6.2
 */
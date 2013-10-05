package org.jpedal.color;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.NodeList;

public class GenericColorSpace
  implements Cloneable, Serializable
{
  boolean isConverted = false;
  private String intent = null;
  float[] rawValues;
  Map patterns;
  float[][] CTM;
  private int size = 0;
  float c = -1.0F;
  float y = -1.0F;
  float m = -1.0F;
  float k = -1.0F;
  float[] W;
  float[] G;
  float[] Ma;
  float[] R;
  public static ColorSpace rgbCS;
  public static final String cb = "<color ";
  public static final String ce = "</color>";
  int value = 1785221209;
  static ColorConvertOp CSToRGB = null;
  ColorSpace cs = rgbCS;
  PdfPaint currentColor = new PdfColor(0, 0, 0);
  static ColorModel rgbModel = null;
  public static ICC_Profile ICCProfileForRGB = null;
  public static boolean fasterPNG = false;
  boolean failed = false;
  int alternative = -1;
  private PdfObject decodeParms = null;
  private boolean hasYCCKimages = false;
  private Object[] cache;
  boolean isPrinting = false;
  int r;
  int g;
  int b;
  byte[] IndexedColorMap = null;
  String pantoneName = null;
  int componentCount = 3;
  GraphicsState gs;
  int pageWidth;
  int pageHeight;
  private static final int multiplier = 100000;

  public void setPrinting(boolean paramBoolean)
  {
    this.isPrinting = paramBoolean;
  }

  protected static void initCMYKColorspace()
    throws PdfException
  {
    try
    {
      if (ICCProfileForRGB == null)
      {
        rgbModel = new ComponentColorModel(rgbCS, new int[] { 8, 8, 8 }, false, false, 1, 0);
      }
      else
      {
        int i = rgbCS.getNumComponents();
        localObject = new int[i];
        for (int j = 0; j < i; j++)
          localObject[j] = 8;
        rgbModel = new ComponentColorModel(rgbCS, (int[])localObject, false, false, 1, 0);
      }
      ICC_Profile localICC_Profile = ICC_Profile.getInstance(GenericColorSpace.class.getResourceAsStream("/org/jpedal/res/cmm/cmyk.icm"));
      Object localObject = new ICC_ColorSpace(localICC_Profile);
      CSToRGB = new ColorConvertOp((ColorSpace)localObject, rgbCS, ColorSpaces.hints);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException.getMessage() + " initialising color components");
      throw new PdfException("[PDF] Unable to create CMYK colorspace. Check cmyk.icm in jar file");
    }
  }

  public void reset()
  {
    this.currentColor = new PdfColor(0, 0, 0);
  }

  public boolean isInvalid()
  {
    return this.failed;
  }

  public int getIndexSize()
  {
    return this.size;
  }

  public PdfPaint getColor()
  {
    return this.currentColor;
  }

  public ColorSpace getColorSpace()
  {
    return this.cs;
  }

  protected void setAlternateColorSpace(int paramInt)
  {
    this.alternative = paramInt;
  }

  public int getAlternateColorSpace()
  {
    return this.alternative;
  }

  public void restoreColorStatus()
  {
    this.currentColor = new PdfColor(this.r, this.g, this.b);
  }

  public Object clone()
  {
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

  protected final BufferedImage nonRGBJPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4)
  {
    int i = 0;
    BufferedImage localBufferedImage = null;
    ByteArrayInputStream localByteArrayInputStream = null;
    ImageReader localImageReader = null;
    ImageInputStream localImageInputStream = null;
    try
    {
      if (CSToRGB == null)
        initCMYKColorspace();
      CSToRGB = new ColorConvertOp(this.cs, rgbCS, ColorSpaces.hints);
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      int j = getJPEGTransform(paramArrayOfByte);
      Object localObject;
      try
      {
        Iterator localIterator = ImageIO.getImageReadersByFormatName("JPEG");
        while (localIterator.hasNext())
        {
          localObject = localIterator.next();
          localImageReader = (ImageReader)localObject;
          if (localImageReader.canReadRaster())
            break;
        }
      }
      catch (Exception localException3)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Unable to find JAI jars on classpath");
        return null;
      }
      ImageIO.setUseCache(false);
      localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      localImageReader.setInput(localImageInputStream, true);
      Raster localRaster = localImageReader.readRaster(0, null);
      int n;
      int i1;
      if (paramArrayOfFloat != null)
        if (((paramArrayOfFloat.length == 6) && (paramArrayOfFloat[0] == 1.0F) && (paramArrayOfFloat[1] == 0.0F) && (paramArrayOfFloat[2] == 1.0F) && (paramArrayOfFloat[3] == 0.0F) && (paramArrayOfFloat[4] == 1.0F) && (paramArrayOfFloat[5] == 0.0F)) || ((paramArrayOfFloat.length > 2) && (paramArrayOfFloat[0] == 1.0F) && (paramArrayOfFloat[1] == 0.0F)))
        {
          localObject = localRaster.getDataBuffer();
          n = ((DataBuffer)localObject).getSize();
          for (i1 = 0; i1 < n; i1++)
            ((DataBuffer)localObject).setElem(i1, 255 - ((DataBuffer)localObject).getElem(i1));
        }
        else if (((paramArrayOfFloat.length == 6) && (paramArrayOfFloat[0] == 0.0F) && (paramArrayOfFloat[1] == 1.0F) && (paramArrayOfFloat[2] == 0.0F) && (paramArrayOfFloat[3] == 1.0F) && (paramArrayOfFloat[4] == 0.0F) && (paramArrayOfFloat[5] == 1.0F)) || (paramArrayOfFloat == null) || (paramArrayOfFloat.length <= 0));
      if (this.cs.getNumComponents() == 4)
      {
        i = 1;
        try
        {
          if (j == 2)
          {
            this.hasYCCKimages = true;
            localBufferedImage = ColorSpaceConvertor.iccConvertCMYKImageToRGB(((DataBufferByte)localRaster.getDataBuffer()).getData(), paramInt1, paramInt2);
          }
          else
          {
            localRaster = cleanupRaster(localRaster, paramInt3, paramInt4, 4);
            paramInt1 = localRaster.getWidth();
            paramInt2 = localRaster.getHeight();
            localObject = rgbModel.createCompatibleWritableRaster(paramInt1, paramInt2);
            CSToRGB.filter(localRaster, (WritableRaster)localObject);
            localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
            localBufferedImage.setData((Raster)localObject);
          }
        }
        catch (Exception localException4)
        {
          localException4.printStackTrace();
          if (LogWriter.isOutput())
            LogWriter.writeLog("Problem with JPEG conversion");
        }
      }
      else if (j != 0)
      {
        localBufferedImage = localImageReader.read(0);
        localBufferedImage = cleanupImage(localBufferedImage, paramInt3, paramInt4);
        i = 1;
      }
      if (i == 0)
        if (j == 4)
        {
          localRaster = cleanupRaster(localRaster, paramInt3, paramInt4, 4);
          n = localRaster.getWidth();
          i1 = localRaster.getHeight();
          WritableRaster localWritableRaster = rgbModel.createCompatibleWritableRaster(n, i1);
          CSToRGB.filter(localRaster, localWritableRaster);
          localBufferedImage = new BufferedImage(n, i1, 1);
          localBufferedImage.setData(localWritableRaster);
        }
        else
        {
          n = 0;
          try
          {
            IIOMetadata localIIOMetadata = localImageReader.getImageMetadata(0);
            String str = localIIOMetadata.getNativeMetadataFormatName();
            IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)localIIOMetadata.getAsTree(str);
            NodeList localNodeList = localIIOMetadataNode.getElementsByTagName("app14Adobe");
            if (localNodeList.getLength() > 0)
              n = 1;
          }
          catch (Exception localException5)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("[PDF] Unable to read metadata on Jpeg " + localException5);
          }
          if (LogWriter.isOutput())
            LogWriter.writeLog("COLOR_ID_YCbCr image");
          if (n != 0)
            localBufferedImage = DefaultImageHelper.read(paramArrayOfByte);
          else
            localBufferedImage = ColorSpaceConvertor.algorithmicConvertYCbCrToRGB(((DataBufferByte)localRaster.getDataBuffer()).getData(), paramInt1, paramInt2);
          localBufferedImage = cleanupImage(localBufferedImage, paramInt3, paramInt4);
          localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
        }
    }
    catch (Exception localException1)
    {
      localBufferedImage = null;
      localException1.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Couldn't read JPEG, not even raster: " + localException1);
    }
    catch (Error localError)
    {
      if (localImageReader != null)
        localImageReader.dispose();
      if (localImageInputStream != null)
        try
        {
          localImageInputStream.flush();
        }
        catch (IOException localIOException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localIOException.getMessage());
        }
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

  protected static BufferedImage cleanupImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2)
  {
    try
    {
      int i = paramBufferedImage.getType();
      if ((getSampling(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), paramInt1, paramInt2) <= 1) || (i == 0))
        return paramBufferedImage;
      if (i == 5)
        return cleanupBGRImage(paramBufferedImage, paramInt1, paramInt2);
      if (i == 5)
        paramBufferedImage = ColorSpaceConvertor.convertToRGB(paramBufferedImage);
      Raster localRaster = cleanupRaster(paramBufferedImage.getData(), paramInt1, paramInt2, paramBufferedImage.getColorModel().getNumColorComponents());
      paramBufferedImage = new BufferedImage(localRaster.getWidth(), localRaster.getHeight(), paramBufferedImage.getType());
      paramBufferedImage.setData(localRaster);
      return paramBufferedImage;
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("[PDF] Error in cleanupImage " + localError);
    }
    return paramBufferedImage;
  }

  private static int getSampling(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 1;
    int j = paramInt1;
    int n = paramInt2;
    if ((paramInt3 > 0) && (paramInt4 > 0))
    {
      int i1 = paramInt4 << 2;
      int i2 = paramInt3 << 2;
      while ((j > i2) && (n > i1))
      {
        i <<= 1;
        j >>= 1;
        n >>= 1;
      }
      int i3 = paramInt1 / paramInt3;
      if (i3 < 1)
        i3 = 1;
      int i4 = paramInt2 / paramInt4;
      if (i4 < 1)
        i4 = 1;
      i = i3;
      if (i > i4)
        i = i4;
    }
    return i;
  }

  protected static Raster cleanupRaster(Raster paramRaster, int paramInt1, int paramInt2, int paramInt3)
  {
    String str = System.getProperty("org.jpedal.avoidCleanupRaster");
    if ((str != null) && (str.toLowerCase().contains("true")))
      return paramRaster;
    byte[] arrayOfByte1 = null;
    int[] arrayOfInt1 = null;
    DataBuffer localDataBuffer = paramRaster.getDataBuffer();
    int i;
    if ((localDataBuffer instanceof DataBufferInt))
      i = 1;
    else
      i = 0;
    int i4;
    int i5;
    if (i == 1)
    {
      arrayOfInt1 = ((DataBufferInt)localDataBuffer).getData();
    }
    else
    {
      j = paramRaster.getNumBands();
      if (j == paramInt3)
      {
        arrayOfByte1 = ((DataBufferByte)localDataBuffer).getData();
      }
      else if (j == 1)
      {
        byte[] arrayOfByte2 = ((DataBufferByte)paramRaster.getDataBuffer()).getData();
        i1 = arrayOfByte2.length;
        i2 = i1 * paramInt3;
        i3 = 0;
        i4 = 0;
        arrayOfByte1 = new byte[i2];
        while (true)
        {
          for (i5 = 0; i5 < paramInt3; i5++)
          {
            arrayOfByte1[i3] = arrayOfByte2[i4];
            i3++;
          }
          i4++;
          if (i4 >= i1)
            break;
        }
      }
    }
    int j = 1;
    int n = paramRaster.getWidth();
    int i1 = paramRaster.getHeight();
    int i2 = n;
    int i3 = i1;
    int i6;
    int i7;
    if ((paramInt1 > 0) && (paramInt2 > 0))
    {
      i4 = paramInt2 << 2;
      i5 = paramInt1 << 2;
      while ((i2 > i5) && (i3 > i4))
      {
        j <<= 1;
        i2 >>= 1;
        i3 >>= 1;
      }
      i6 = n / paramInt1;
      if (i6 < 1)
        i6 = 1;
      i7 = i1 / paramInt2;
      if (i7 < 1)
        i7 = 1;
      j = i6;
      if (j > i7)
        j = i7;
    }
    if (j > 1)
    {
      i2 = n / j;
      i3 = i1 / j;
      int i9 = n;
      try
      {
        byte[] arrayOfByte3 = new byte[i2 * i3 * paramInt3];
        if (i == 0)
          i9 = n * paramInt3;
        for (i5 = 0; i5 < i3; i5++)
          for (i4 = 0; i4 < i2; i4++)
          {
            int i10 = j;
            i11 = j;
            int i12 = n - i4;
            int i13 = i1 - i5;
            if (i10 > i12)
              i10 = i12;
            if (i11 > i13)
              i11 = i13;
            for (int i8 = 0; i8 < paramInt3; i8++)
            {
              int i14 = 0;
              int i15 = 0;
              for (i7 = 0; i7 < i11; i7++)
                for (i6 = 0; i6 < i10; i6++)
                {
                  if (i == 0)
                    i14 += (arrayOfByte1[((i7 + i5 * j) * i9 + (i4 * j * paramInt3 + i6 * paramInt3 + i8))] & 0xFF);
                  else
                    i14 += (arrayOfInt1[((i7 + i5 * j) * i9 + i4 * j + i6)] >> 8 * (2 - i8) & 0xFF);
                  i15++;
                }
              if (i15 > 0)
                arrayOfByte3[(i8 + i4 * paramInt3 + i2 * i5 * paramInt3)] = ((byte)(i14 / i15));
            }
          }
        int[] arrayOfInt2 = new int[paramInt3];
        for (int i11 = 0; i11 < paramInt3; i11++)
          arrayOfInt2[i11] = i11;
        paramRaster = Raster.createInterleavedRaster(new DataBufferByte(arrayOfByte3, arrayOfByte3.length), i2, i3, i2 * paramInt3, paramInt3, arrayOfInt2, null);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("Problem with Image");
      }
    }
    return paramRaster;
  }

  private static BufferedImage cleanupBGRImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2)
  {
    if (System.getProperty("java.version").startsWith("1.5"))
      return paramBufferedImage;
    Raster localRaster = paramBufferedImage.getData();
    int i = paramBufferedImage.getColorModel().getNumColorComponents();
    byte[] arrayOfByte1 = null;
    int[] arrayOfInt1 = null;
    DataBuffer localDataBuffer = localRaster.getDataBuffer();
    int j;
    if ((localDataBuffer instanceof DataBufferInt))
      j = 1;
    else
      j = 0;
    int i5;
    int i6;
    if (j == 1)
    {
      arrayOfInt1 = ((DataBufferInt)localDataBuffer).getData();
    }
    else
    {
      n = localRaster.getNumBands();
      if (n == i)
      {
        arrayOfByte1 = ((DataBufferByte)localDataBuffer).getData();
      }
      else if (n == 1)
      {
        byte[] arrayOfByte2 = ((DataBufferByte)localRaster.getDataBuffer()).getData();
        i2 = arrayOfByte2.length;
        i3 = i2 * i;
        i4 = 0;
        i5 = 0;
        arrayOfByte1 = new byte[i3];
        while (true)
        {
          for (i6 = 0; i6 < i; i6++)
          {
            arrayOfByte1[i4] = arrayOfByte2[i5];
            i4++;
          }
          i5++;
          if (i5 >= i2)
            break;
        }
      }
    }
    int n = 1;
    int i1 = localRaster.getWidth();
    int i2 = localRaster.getHeight();
    int i3 = i1;
    int i4 = i2;
    int i7;
    int i8;
    if ((paramInt1 > 0) && (paramInt2 > 0))
    {
      i5 = paramInt2 << 2;
      i6 = paramInt1 << 2;
      while ((i3 > i6) && (i4 > i5))
      {
        n <<= 1;
        i3 >>= 1;
        i4 >>= 1;
      }
      i7 = i1 / paramInt1;
      if (i7 < 1)
        i7 = 1;
      i8 = i2 / paramInt2;
      if (i8 < 1)
        i8 = 1;
      n = i7;
      if (n > i8)
        n = i8;
    }
    if (n > 1)
    {
      WritableRaster localWritableRaster = (WritableRaster)localRaster;
      i3 = i1 / n;
      i4 = i2 / n;
      int i11 = i1;
      try
      {
        int[] arrayOfInt2 = new int[i];
        if (j == 0)
          i11 = i1 * i;
        for (i7 = 0; i7 < i4; i7++)
          for (i6 = 0; i6 < i3; i6++)
          {
            int i12 = n;
            int i13 = n;
            int i14 = i1 - i6;
            int i15 = i2 - i7;
            if (i12 > i14)
              i12 = i14;
            if (i13 > i15)
              i13 = i15;
            for (int i10 = 0; i10 < i; i10++)
            {
              int i16 = 0;
              int i17 = 0;
              for (int i9 = 0; i9 < i13; i9++)
                for (i8 = 0; i8 < i12; i8++)
                {
                  if (j == 0)
                    i16 += (arrayOfByte1[((i9 + i7 * n) * i11 + (i6 * n * i + i8 * i + i10))] & 0xFF);
                  else
                    i16 += (arrayOfInt1[((i9 + i7 * n) * i11 + i6 * n + i8)] >> 8 * (2 - i10) & 0xFF);
                  i17++;
                }
              if (i17 > 0)
                if (i10 == 0)
                  arrayOfInt2[2] = (i16 / i17);
                else if (i10 == 2)
                  arrayOfInt2[0] = (i16 / i17);
                else
                  arrayOfInt2[i10] = (i16 / i17);
            }
            localWritableRaster.setPixels(i6, i7, 1, 1, arrayOfInt2);
          }
        paramBufferedImage = new BufferedImage(i3, i4, paramBufferedImage.getType());
        paramBufferedImage.setData(localWritableRaster);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("Problem with Image");
      }
    }
    return paramBufferedImage;
  }

  private static int getJPEGTransform(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int n = paramArrayOfByte.length - 2;
    while (j < n)
    {
      int i1 = paramArrayOfByte[(j + 1)] & 0xFF;
      j += 2;
      if ((i1 != 1) && ((208 > i1) || (i1 > 218)))
      {
        if (i1 == 218)
        {
          j = j + ((paramArrayOfByte[j] & 0xFF) << 8) + (paramArrayOfByte[(j + 1)] & 0xFF);
          while (true)
            if ((j < n) && (((paramArrayOfByte[j] & 0xFF) != 255) || (paramArrayOfByte[(j + 1)] == 0)))
            {
              j++;
            }
            else
            {
              int i2 = paramArrayOfByte[(j + 1)] & 0xFF;
              if ((208 > i2) || (i2 > 215))
                break;
              j += 2;
            }
        }
        if ((i1 == 238) && (paramArrayOfByte[(j + 2)] == 65) && (paramArrayOfByte[(j + 3)] == 100) && (paramArrayOfByte[(j + 4)] == 111) && (paramArrayOfByte[(j + 5)] == 98) && (paramArrayOfByte[(j + 6)] == 101))
        {
          i = paramArrayOfByte[(j + 13)] & 0xFF;
          break;
        }
        j = j + ((paramArrayOfByte[j] & 0xFF) << 8) + (paramArrayOfByte[(j + 1)] & 0xFF);
      }
    }
    return i;
  }

  public void setIndex(byte[] paramArrayOfByte, int paramInt)
  {
    this.IndexedColorMap = paramArrayOfByte;
    this.size = paramInt;
  }

  protected int getIndexedColorComponent(int paramInt)
  {
    int i = 255;
    if (this.IndexedColorMap != null)
    {
      i = this.IndexedColorMap[paramInt];
      if (i < 0)
        i = 256 + i;
    }
    return i;
  }

  public byte[] getIndexedMap()
  {
    if (this.IndexedColorMap == null)
      return null;
    int i = this.IndexedColorMap.length;
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(this.IndexedColorMap, 0, arrayOfByte, 0, i);
    return arrayOfByte;
  }

  public void setColor(String[] paramArrayOfString, int paramInt)
  {
  }

  public void setColor(float[] paramArrayOfFloat, int paramInt)
  {
  }

  private BufferedImage JPEGToRGBImageFromLUV(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage;
    try
    {
      Raster localRaster = DefaultImageHelper.readRasterFromJPeg(paramArrayOfByte);
      if (localRaster == null)
        return null;
      localRaster = cleanupRaster(localRaster, paramInt1, paramInt2, 3);
      int i = localRaster.getWidth();
      int j = localRaster.getHeight();
      int n = i * j;
      byte[] arrayOfByte = ((DataBufferByte)localRaster.getDataBuffer()).getData();
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f6 = 0.0F;
      float f7 = -1.0F;
      float f8 = -1.0F;
      float f9 = -1.0F;
      int i1 = 0;
      while (i1 < n * 3)
      {
        float f1 = arrayOfByte[i1] & 0xFF;
        float f2 = arrayOfByte[(i1 + 1)] & 0xFF;
        float f3 = arrayOfByte[(i1 + 2)] & 0xFF;
        if ((f1 != f7) || (f2 != f8) || (f3 != f9))
        {
          f4 = f1 + 1.402F * (f3 - 128.0F);
          if (f4 < 0.0F)
            f4 = 0.0F;
          else if (f4 > 255.0F)
            f4 = 255.0F;
          f5 = f1 - 0.344F * (f2 - 128.0F) - 0.714F * (f3 - 128.0F);
          if (f5 < 0.0F)
            f5 = 0.0F;
          else if (f5 > 255.0F)
            f5 = 255.0F;
          f6 = f1 + 1.772F * (f2 - 128.0F);
          if (f6 < 0.0F)
            f6 = 0.0F;
          else if (f6 > 255.0F)
            f6 = 255.0F;
          f7 = f1;
          f8 = f2;
          f9 = f3;
        }
        arrayOfByte[i1] = ((byte)(int)f4);
        arrayOfByte[(i1 + 1)] = ((byte)(int)f5);
        arrayOfByte[(i1 + 2)] = ((byte)(int)f6);
        i1 += 3;
      }
      DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
      int[] arrayOfInt = { 0, 1, 2 };
      localBufferedImage = new BufferedImage(i, j, 1);
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

  public BufferedImage JPEGToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4, boolean paramBoolean, PdfObject paramPdfObject)
  {
    if ((this.decodeParms != null) && (this.decodeParms.getInt(-1263544861) == 1) && (this.value != 1568372915))
      return JPEGToRGBImageFromLUV(paramArrayOfByte, paramInt3, paramInt4);
    Object localObject = null;
    BufferedImage localBufferedImage;
    try
    {
      localBufferedImage = DefaultImageHelper.read(paramArrayOfByte);
      if ((localBufferedImage != null) && (!fasterPNG))
      {
        if (this.value != 1568372915)
          localBufferedImage = cleanupImage(localBufferedImage, paramInt3, paramInt4);
        if (this.value != 1568372915)
          localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
      }
    }
    catch (Exception localException)
    {
      localBufferedImage = null;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG: " + localException);
      localException.printStackTrace();
    }
    if (localObject != null)
      try
      {
        localObject.close();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
    if ((paramBoolean) && (this.value == 1568372915))
    {
      DataBufferByte localDataBufferByte = (DataBufferByte)localBufferedImage.getRaster().getDataBuffer();
      byte[] arrayOfByte = localDataBufferByte.getData();
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfByte[i] = ((byte)(arrayOfByte[i] ^ 0xFF));
      localBufferedImage.setData(Raster.createRaster(localBufferedImage.getSampleModel(), new DataBufferByte(arrayOfByte, arrayOfByte.length), null));
    }
    return localBufferedImage;
  }

  public BufferedImage JPEG2000ToRGBImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4)
    throws PdfException
  {
    int i = 0;
    if (((System.getProperty("isJpegOnServer") != null) && (System.getProperty("isJpegOnServer").equals("true"))) || ((System.getProperty("org.jpedal.isJpegOnServer") != null) && (System.getProperty("org.jpedal.isJpegOnServer").equals("true"))))
      i = 1;
    BufferedImage localBufferedImage1;
    if (i != 0)
      localBufferedImage1 = JAIHelper.getJPEG2000OnServer(paramArrayOfByte);
    else
      localBufferedImage1 = JAIHelper.getJPEG2000(paramArrayOfByte);
    if (localBufferedImage1 != null)
    {
      byte[] arrayOfByte = getIndexedMap();
      if ((arrayOfByte != null) && (this.value == 1785221209))
      {
        paramArrayOfByte = ((DataBufferByte)localBufferedImage1.getRaster().getDataBuffer()).getData();
        localBufferedImage1 = ColorSpaceConvertor.convertIndexedToFlat(8, paramInt1, paramInt2, paramArrayOfByte, arrayOfByte, false, false);
      }
      localBufferedImage1 = cleanupImage(localBufferedImage1, paramInt3, paramInt4);
      if (localBufferedImage1.getType() == 13)
      {
        BufferedImage localBufferedImage2 = localBufferedImage1;
        int j = localBufferedImage1.getWidth();
        int n = localBufferedImage1.getHeight();
        localBufferedImage1 = new BufferedImage(j, n, 1);
        Graphics2D localGraphics2D = (Graphics2D)localBufferedImage1.getGraphics();
        localGraphics2D.setPaint(Color.WHITE);
        localGraphics2D.fillRect(0, 0, j, n);
        localGraphics2D.drawImage(localBufferedImage2, 0, 0, null);
      }
      localBufferedImage1 = ColorSpaceConvertor.convertToRGB(localBufferedImage1);
    }
    return localBufferedImage1;
  }

  public BufferedImage dataToRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
    Raster localRaster = ColorSpaceConvertor.createInterleavedRaster(paramArrayOfByte, paramInt1, paramInt2);
    localBufferedImage.setData(localRaster);
    return localBufferedImage;
  }

  public int getID()
  {
    return this.value;
  }

  public final void setCIEValues(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
  {
    this.cs = ColorSpace.getInstance(1001);
    this.G = paramArrayOfFloat4;
    this.Ma = paramArrayOfFloat3;
    this.W = paramArrayOfFloat1;
    this.R = paramArrayOfFloat2;
  }

  protected final byte[] convert4Index(byte[] paramArrayOfByte)
  {
    return convertIndex(paramArrayOfByte, 4);
  }

  private byte[] convertIndex(byte[] paramArrayOfByte, int paramInt)
  {
    int i;
    Object localObject2;
    if ((paramInt == 4) && (this.value == 1498837125))
    {
      i = paramArrayOfByte.length;
      byte[] arrayOfByte = new byte[i * 3 / 4];
      int n = 0;
      int i1 = 0;
      while (i1 < i)
      {
        localObject2 = new float[4];
        for (int i2 = 0; i2 < 4; i2++)
          localObject2[i2] = ((paramArrayOfByte[(i1 + i2)] & 0xFF) / 255.0F);
        setColor((float[])localObject2, 4);
        i2 = this.currentColor.getRGB();
        arrayOfByte[n] = ((byte)(i2 >> 16 & 0xFF));
        arrayOfByte[(n + 1)] = ((byte)(i2 >> 8 & 0xFF));
        arrayOfByte[(n + 2)] = ((byte)(i2 & 0xFF));
        n += 3;
        if (i - 4 - i1 < 4)
          i1 = i;
        i1 += 4;
      }
      return arrayOfByte;
    }
    try
    {
      i = paramArrayOfByte.length / paramInt;
      int j = 1;
      DataBufferByte localDataBufferByte = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
      int[] arrayOfInt1 = { 0, 1, 2, 3 };
      int[] arrayOfInt2 = { 0, 1, 2 };
      Object localObject1;
      if (paramInt == 4)
        localObject1 = arrayOfInt1;
      else
        localObject1 = arrayOfInt2;
      localObject2 = Raster.createInterleavedRaster(localDataBufferByte, i, j, i * paramInt, paramInt, localObject1, null);
      if (CSToRGB == null)
        initCMYKColorspace();
      CSToRGB = new ColorConvertOp(this.cs, rgbCS, ColorSpaces.hints);
      WritableRaster localWritableRaster = rgbModel.createCompatibleWritableRaster(i, j);
      CSToRGB.filter((Raster)localObject2, localWritableRaster);
      DataBuffer localDataBuffer = localWritableRaster.getDataBuffer();
      int i3 = i * j * 3;
      paramArrayOfByte = new byte[i3];
      for (int i4 = 0; i4 < i3; i4++)
        paramArrayOfByte[i4] = ((byte)localDataBuffer.getElem(i4));
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception  " + localException + " converting colorspace");
    }
    return paramArrayOfByte;
  }

  public byte[] convertIndexToRGB(byte[] paramArrayOfByte)
  {
    return paramArrayOfByte;
  }

  public String getXMLColorToken()
  {
    String str;
    if (this.c == -1.0F)
    {
      if ((this.currentColor instanceof Color))
      {
        Color localColor = (Color)this.currentColor;
        float f1 = (255 - localColor.getRed()) / 255.0F;
        float f2 = (255 - localColor.getGreen()) / 255.0F;
        float f3 = (255 - localColor.getBlue()) / 255.0F;
        float f4 = f1;
        if (f4 < f2)
          f4 = f2;
        if (f4 < f3)
          f4 = f3;
        if (this.pantoneName == null)
          str = "<color C='" + f1 + "' M='" + f2 + "' Y='" + f3 + "' K='" + f4 + "' >";
        else
          str = "<color C='" + f1 + "' M='" + f2 + "' Y='" + f3 + "' K='" + f4 + "' pantoneName='" + this.pantoneName + "' >";
      }
      else
      {
        str = "<color type='shading'>";
      }
    }
    else if (this.pantoneName == null)
      str = "<color C='" + this.c + "' M='" + this.m + "' Y='" + this.y + "' K='" + this.k + "' >";
    else
      str = "<color C='" + this.c + "' M='" + this.m + "' Y='" + this.y + "' K='" + this.k + "' pantoneName='" + this.pantoneName + "' >";
    return str;
  }

  public void setPattern(Map paramMap, int paramInt1, int paramInt2, float[][] paramArrayOfFloat)
  {
    this.patterns = paramMap;
    this.pageWidth = paramInt1;
    this.pageHeight = paramInt2;
    this.CTM = paramArrayOfFloat;
  }

  public void setColor(PdfPaint paramPdfPaint)
  {
    this.currentColor = paramPdfPaint;
  }

  public int getColorComponentCount()
  {
    return this.componentCount;
  }

  public void setGS(GraphicsState paramGraphicsState)
  {
    this.gs = paramGraphicsState;
  }

  public void setIntent(String paramString)
  {
    this.intent = paramString;
  }

  public String getIntent()
  {
    return this.intent;
  }

  public float[] getRawValues()
  {
    return this.rawValues;
  }

  public boolean isImageYCCK()
  {
    return this.hasYCCKimages;
  }

  public void setDecodeParms(PdfObject paramPdfObject)
  {
    this.decodeParms = paramPdfObject;
  }

  public boolean isIndexConverted()
  {
    return this.isConverted;
  }

  public Color getCachedShadingColor(float paramFloat)
  {
    if (this.cache == null)
      return null;
    return (Color)this.cache[((int)(paramFloat * 100000.0F))];
  }

  public void setShadedColor(float paramFloat, Color paramColor)
  {
    if (this.cache == null)
      this.cache = new Object[100001];
    this.cache[((int)(paramFloat * 100000.0F))] = paramColor;
  }

  public void clearCache()
  {
  }

  public static ColorSpace getColorSpaceInstance()
  {
    Object localObject = ColorSpace.getInstance(1000);
    String str = System.getProperty("org.jpedal.RGBprofile");
    if (str != null)
      try
      {
        localObject = new ICC_ColorSpace(ICC_Profile.getInstance(new FileInputStream(str)));
        System.out.println("use " + str);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("[PDF] Problem " + localException.getMessage() + " with ICC data ");
      }
    return localObject;
  }

  static
  {
    String str1 = System.getProperty("org.jpedal.fasterPNG");
    fasterPNG = (str1 != null) && (str1.toLowerCase().equals("true"));
    String str2 = System.getProperty("org.jpedal.RGBprofile");
    if (str2 != null)
      try
      {
        ICCProfileForRGB = ICC_Profile.getInstance(new FileInputStream(str2));
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("[PDF] Problem " + localException.getMessage() + " with ICC data ");
        if (ICCProfileForRGB == null)
          throw new RuntimeException("Problem wth RGB profile " + str2 + ' ' + localException.getMessage());
      }
    if (ICCProfileForRGB != null)
      rgbCS = new ICC_ColorSpace(ICCProfileForRGB);
    else
      rgbCS = ColorSpace.getInstance(1000);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.GenericColorSpace
 * JD-Core Version:    0.6.2
 */
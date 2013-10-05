package org.jpedal.parser;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.function.FunctionFactory;
import org.jpedal.function.PDFFunction;
import org.jpedal.images.ImageOps;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.DecryptionFactory;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.FunctionObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;

public class ImageCommands
{
  public static final int ID = 0;
  public static final int XOBJECT = 2;
  static boolean sharpenDownsampledImages = false;
  static boolean JAImessageShow = false;
  public static boolean trackImages = false;
  public static boolean rejectSuperimposedImages = true;

  static BufferedImage addMaskObject(GenericColorSpace paramGenericColorSpace, int paramInt1, boolean paramBoolean1, boolean paramBoolean2, BufferedImage paramBufferedImage, int paramInt2, byte[] paramArrayOfByte1, PdfObject paramPdfObject, int paramInt3, PdfObjectReader paramPdfObjectReader, byte[] paramArrayOfByte2)
  {
    int[] arrayOfInt = paramPdfObject.getIntArray(489767739);
    if ((arrayOfInt != null) && (paramBufferedImage.getWidth() == 1) && (paramBufferedImage.getHeight() == 1) && (arrayOfInt[0] == arrayOfInt[1]))
      return null;
    Object localObject1;
    WritableRaster localWritableRaster2;
    Object localObject2;
    int j;
    WritableRaster localWritableRaster3;
    if (arrayOfInt != null)
    {
      if ((paramBufferedImage.getType() == 10) && (arrayOfInt.length == 6) && (arrayOfInt[0] == arrayOfInt[1]) && (arrayOfInt[2] == arrayOfInt[3]) && (arrayOfInt[4] == arrayOfInt[5]))
      {
        localObject1 = (DataBufferByte)paramBufferedImage.getRaster().getDataBuffer();
        byte[] arrayOfByte1 = ((DataBufferByte)localObject1).getData();
        localWritableRaster2 = arrayOfByte1.length;
        for (int i = 0; i < localWritableRaster2; i++)
          arrayOfByte1[i] = ((byte)(arrayOfByte1[i] ^ 0xFF));
        localObject2 = new int[] { 0 };
        j = paramBufferedImage.getWidth();
        int k = paramBufferedImage.getHeight();
        localWritableRaster3 = Raster.createInterleavedRaster(new DataBufferByte(arrayOfByte1, localWritableRaster2), j, k, j, 1, (int[])localObject2, null);
        paramBufferedImage.setData(localWritableRaster3);
      }
      else if (paramArrayOfByte1 != null)
      {
        switch (paramInt1)
        {
        case 1:
          paramBufferedImage = convertIndexedPixelsToTransparent1bpc(paramBufferedImage, arrayOfInt, paramArrayOfByte2);
          break;
        case 2:
          paramBufferedImage = convertIndexedPixelsToTransparent2bpc(paramBufferedImage, arrayOfInt, paramArrayOfByte2);
          break;
        default:
          paramBufferedImage = convertIndexedPixelsToTransparent8bpc(paramBufferedImage, arrayOfInt, paramArrayOfByte2);
          break;
        }
      }
      else
      {
        paramBufferedImage = convertPixelsToTransparent(paramBufferedImage, arrayOfInt);
      }
    }
    else
    {
      localObject1 = paramPdfObjectReader.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(paramPdfObjectReader.getObjectReader()));
      WritableRaster localWritableRaster1 = paramPdfObject.getInt(959726687);
      localWritableRaster2 = paramPdfObject.getInt(959926393);
      localObject2 = paramPdfObject.getFloatArray(859785322);
      if ((localObject2 != null) && ((paramInt2 == 1785221209) || (paramInt2 == -2073385820)))
        applyDecodeArray((byte[])localObject1, localObject2.length / 2, (float[])localObject2, paramInt2);
      j = 3;
      if ((paramInt3 & j) == j)
      {
        byte[] arrayOfByte2 = ImageOps.rotateImage((byte[])localObject1, localWritableRaster1, localWritableRaster2, 1, 1, null);
        if (arrayOfByte2 != null)
        {
          localWritableRaster3 = localWritableRaster2;
          localWritableRaster2 = localWritableRaster1;
          localWritableRaster1 = localWritableRaster3;
          arrayOfByte2 = ImageOps.rotateImage(arrayOfByte2, localWritableRaster1, localWritableRaster2, paramInt1, 1, null);
          if (arrayOfByte2 != null)
          {
            localWritableRaster3 = localWritableRaster2;
            localWritableRaster2 = localWritableRaster1;
            localWritableRaster1 = localWritableRaster3;
          }
        }
        localObject1 = arrayOfByte2;
      }
      else if ((paramInt3 & 0x1) == 1)
      {
        localObject1 = ImageOps.invertImage((byte[])localObject1, localWritableRaster1, localWritableRaster2, 1, 1, null);
      }
      if ((paramInt3 & 0x2) == 2)
        localObject1 = ImageOps.rotateImage((byte[])localObject1, localWritableRaster1, localWritableRaster2, 1, 1, null);
      if (localObject1 != null)
      {
        boolean bool = (paramGenericColorSpace != null) && ((paramGenericColorSpace.getID() == 1568372915) || (paramGenericColorSpace.getID() == 1008872003)) && (!paramBoolean2);
        int m = (paramGenericColorSpace != null) && (paramGenericColorSpace.getID() == 1785221209) ? 1 : 0;
        if (((bool) && ((paramGenericColorSpace.getID() == 1568372915) || (paramGenericColorSpace.getID() == 1008872003))) || ((!bool) && (m == 0) && (paramBoolean1)))
        {
          PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(1011108731);
          int n = 0;
          if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
            while (localPdfArrayIterator.hasMoreTokens())
            {
              int i1 = localPdfArrayIterator.getNextValueAsConstant(true);
              n = (i1 == 2108391315) || (i1 == 1247500931) ? 1 : 0;
            }
          if (paramGenericColorSpace.getID() == 1008872003)
            n = n == 0 ? 1 : 0;
          if (n == 0)
            bool = false;
          else if ((bool) && (paramGenericColorSpace.getID() == 1568372915))
            bool = false;
          if ((localObject2 != null) && (paramGenericColorSpace.getID() == 1568372915) && (localObject2[0] == 1.0F) && (localObject2[1] == 0.0F))
            bool = !bool;
        }
        paramBufferedImage = overlayImage(paramBufferedImage, (byte[])localObject1, paramPdfObject, bool, paramInt3);
      }
    }
    return paramBufferedImage;
  }

  static BufferedImage makeBlackandWhiteTransparent(BufferedImage paramBufferedImage)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = localWritableRaster.getWidth();
    int j = localWritableRaster.getHeight();
    BufferedImage localBufferedImage = new BufferedImage(i, j, 2);
    int k = 0;
    int[] arrayOfInt1 = new int[3];
    int[] arrayOfInt2 = { 255, 0, 0, 0 };
    for (int i1 = 0; i1 < j; i1++)
      for (int i2 = 0; i2 < i; i2++)
      {
        localWritableRaster.getPixels(i2, i1, 1, 1, arrayOfInt1);
        int m = (arrayOfInt1[0] > 245) && (arrayOfInt1[1] > 245) && (arrayOfInt1[2] > 245) ? 1 : 0;
        int n = (arrayOfInt1[0] < 10) && (arrayOfInt1[1] < 10) && (arrayOfInt1[2] < 10) ? 1 : 0;
        if ((m != 0) || (n != 0))
        {
          localBufferedImage.getRaster().setPixels(i2, i1, 1, 1, arrayOfInt2);
        }
        else
        {
          k = 1;
          int[] arrayOfInt3 = new int[4];
          arrayOfInt3[3] = 255;
          arrayOfInt3[0] = arrayOfInt1[0];
          arrayOfInt3[1] = arrayOfInt1[1];
          arrayOfInt3[2] = arrayOfInt1[2];
          localBufferedImage.getRaster().setPixels(i2, i1, 1, 1, arrayOfInt3);
        }
      }
    if (k != 0)
      return localBufferedImage;
    return null;
  }

  private static BufferedImage overlayImage(BufferedImage paramBufferedImage, byte[] paramArrayOfByte, PdfObject paramPdfObject, boolean paramBoolean, int paramInt)
  {
    paramBufferedImage = ColorSpaceConvertor.convertToRGB(paramBufferedImage);
    WritableRaster localWritableRaster1 = paramBufferedImage.getRaster();
    int i = paramPdfObject.getInt(959726687);
    int j = paramPdfObject.getInt(959926393);
    if (paramInt == 2)
    {
      j = paramPdfObject.getInt(959726687);
      i = paramPdfObject.getInt(959926393);
    }
    int k = paramBufferedImage.getWidth();
    int m = paramBufferedImage.getHeight();
    int n = (k != i) || (m != j) ? 1 : 0;
    float f1 = 0.0F;
    if (n != 0)
    {
      float f2 = k / i;
      float f3 = m / j;
      if (f2 > f3)
        f1 = f2;
      else
        f1 = f3;
    }
    BufferedImage localBufferedImage = new BufferedImage(i, j, 2);
    WritableRaster localWritableRaster2 = localBufferedImage.getRaster();
    int i1 = i;
    if ((i1 & 0x7) != 0)
      i1 += 8;
    i1 >>= 3;
    int i2 = 0;
    int[] arrayOfInt1 = { 128, 64, 32, 16, 8, 4, 2, 1 };
    for (int i5 = 0; i5 < j; i5++)
    {
      int i4;
      if (n != 0)
      {
        i4 = (int)(f1 * i5);
        if (i4 > m)
          i4 = m;
      }
      else
      {
        i4 = i5;
      }
      for (int i9 = 0; i9 < i; i9++)
      {
        int i3;
        if (n != 0)
        {
          i3 = (int)(f1 * i9);
          if (i3 > k)
            i3 = m;
        }
        else
        {
          i3 = i9;
        }
        int i7 = i9 >> 3;
        int i8 = paramArrayOfByte[(i2 + i7)];
        int i6;
        if (paramBoolean)
          i6 = (i8 & arrayOfInt1[(i9 & 0x7)]) == 0 ? 1 : 0;
        else
          i6 = (i8 & arrayOfInt1[(i9 & 0x7)]) != 0 ? 1 : 0;
        if ((i6 == 0) && (i3 < k) && (i4 < m))
        {
          int[] arrayOfInt2 = new int[3];
          arrayOfInt2 = localWritableRaster1.getPixel(i3, i4, arrayOfInt2);
          localWritableRaster2.setPixel(i9, i5, new int[] { arrayOfInt2[0], arrayOfInt2[1], arrayOfInt2[2], 255 });
        }
      }
      i2 += i1;
    }
    return localBufferedImage;
  }

  private static BufferedImage convertIndexedPixelsToTransparent2bpc(BufferedImage paramBufferedImage, int[] paramArrayOfInt, byte[] paramArrayOfByte)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = localWritableRaster.getNumBands();
    int j = 0;
    int k = paramArrayOfByte.length;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = paramBufferedImage.getWidth();
    int[] arrayOfInt1 = { 6, 4, 2, 0 };
    int i4 = 0;
    paramBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 2);
    for (int i7 : paramArrayOfByte)
      for (int i8 = 0; (i8 < 4) && (n < k); i8++)
      {
        j = i7 << arrayOfInt1[(3 - i8)] & 0xC0;
        int m = (paramArrayOfInt[0] <= j) && (j <= paramArrayOfInt[1]) ? 1 : 0;
        if (m == 0)
        {
          int[] arrayOfInt2 = new int[i];
          localWritableRaster.getPixel(i1, i2, arrayOfInt2);
          if (i == 1)
            paramBufferedImage.getRaster().setPixel(i1, i2, new int[] { arrayOfInt2[0], arrayOfInt2[0], arrayOfInt2[0], 255 });
          else
            paramBufferedImage.getRaster().setPixel(i1, i2, new int[] { arrayOfInt2[0], arrayOfInt2[1], arrayOfInt2[2], 255 });
        }
        i4++;
        if (i4 == i3)
        {
          i4 = 0;
          i8 = 8;
          i2++;
          i1 = 0;
        }
        else
        {
          i1++;
        }
      }
    return paramBufferedImage;
  }

  private static BufferedImage convertIndexedPixelsToTransparent1bpc(BufferedImage paramBufferedImage, int[] paramArrayOfInt, byte[] paramArrayOfByte)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = localWritableRaster.getNumBands();
    int j = 0;
    int k = paramArrayOfByte.length;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = paramBufferedImage.getWidth();
    int[] arrayOfInt1 = { 7, 6, 5, 4, 3, 2, 1, 0 };
    int i4 = 0;
    paramBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 2);
    for (int i7 : paramArrayOfByte)
      for (int i8 = 0; (i8 < 8) && (n < k); i8++)
      {
        j = i7 << arrayOfInt1[i8] & 0x1;
        int m = (paramArrayOfInt[0] <= j) && (j <= paramArrayOfInt[1]) ? 1 : 0;
        if (m == 0)
        {
          int[] arrayOfInt2 = new int[i];
          localWritableRaster.getPixel(i1, i2, arrayOfInt2);
          if (i == 1)
            paramBufferedImage.getRaster().setPixel(i1, i2, new int[] { arrayOfInt2[0], arrayOfInt2[0], arrayOfInt2[0], 255 });
          else if ((arrayOfInt2[0] > 0) && (arrayOfInt2[1] > 0) && (arrayOfInt2[2] > 0))
            paramBufferedImage.getRaster().setPixel(i1, i2, new int[] { arrayOfInt2[0], arrayOfInt2[1], arrayOfInt2[2], 255 });
        }
        i4++;
        if (i4 == i3)
        {
          i4 = 0;
          i8 = 8;
          i2++;
          i1 = 0;
        }
        else
        {
          i1++;
        }
      }
    return paramBufferedImage;
  }

  private static BufferedImage convertIndexedPixelsToTransparent8bpc(BufferedImage paramBufferedImage, int[] paramArrayOfInt, byte[] paramArrayOfByte)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = localWritableRaster.getNumBands();
    int j = 0;
    int k = 0;
    int m = paramArrayOfByte.length;
    paramBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 2);
    for (int i1 = 0; i1 < paramBufferedImage.getHeight(); i1++)
      for (int i2 = 0; i2 < paramBufferedImage.getWidth(); i2++)
      {
        k = i1 * paramBufferedImage.getWidth() + i2;
        if (k < m)
          j = paramArrayOfByte[k] & 0xFF;
        else
          j = 0;
        int n = (paramArrayOfInt[0] <= j) && (j <= paramArrayOfInt[1]) ? 1 : 0;
        if (n == 0)
        {
          int[] arrayOfInt = new int[i];
          localWritableRaster.getPixel(i2, i1, arrayOfInt);
          if (i == 1)
            paramBufferedImage.getRaster().setPixel(i2, i1, new int[] { arrayOfInt[0], arrayOfInt[0], arrayOfInt[0], 255 });
          else
            paramBufferedImage.getRaster().setPixel(i2, i1, new int[] { arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], 255 });
        }
      }
    return paramBufferedImage;
  }

  private static BufferedImage convertPixelsToTransparent(BufferedImage paramBufferedImage, int[] paramArrayOfInt)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = localWritableRaster.getNumBands();
    paramBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 2);
    for (int j = 0; j < paramBufferedImage.getHeight(); j++)
      for (int k = 0; k < paramBufferedImage.getWidth(); k++)
      {
        int[] arrayOfInt = new int[i];
        localWritableRaster.getPixel(k, j, arrayOfInt);
        int m = 1;
        for (int n = 0; n < i; n++)
          if ((paramArrayOfInt[(2 * n)] > arrayOfInt[n]) || (arrayOfInt[n] > paramArrayOfInt[(2 * n + 1)]))
          {
            m = 0;
            n = i;
          }
        if (m == 0)
          if (i == 1)
            paramBufferedImage.getRaster().setPixel(k, j, new int[] { arrayOfInt[0], arrayOfInt[0], arrayOfInt[0], 255 });
          else
            paramBufferedImage.getRaster().setPixel(k, j, new int[] { arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], 255 });
      }
    return paramBufferedImage;
  }

  static BufferedImage simulateOP(BufferedImage paramBufferedImage, boolean paramBoolean)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    paramBufferedImage = ColorSpaceConvertor.convertToARGB(paramBufferedImage);
    int i = paramBufferedImage.getWidth();
    int j = paramBufferedImage.getHeight();
    int k = 0;
    int[] arrayOfInt1 = { 255, 0, 0, 0 };
    int[] arrayOfInt2 = new int[4];
    for (int n = 0; n < j; n++)
      for (int i1 = 0; i1 < i; i1++)
      {
        localWritableRaster.getPixel(i1, n, arrayOfInt2);
        int m;
        if (paramBoolean)
          m = (arrayOfInt2[0] > 243) && (arrayOfInt2[1] > 243) && (arrayOfInt2[2] > 243) ? 1 : 0;
        else
          m = (arrayOfInt2[1] < 3) && (arrayOfInt2[2] < 3) && (arrayOfInt2[3] < 3) ? 1 : 0;
        if (m != 0)
          paramBufferedImage.getRaster().setPixel(i1, n, arrayOfInt1);
        else
          k = 1;
      }
    if (k != 0)
      return paramBufferedImage;
    return null;
  }

  static void getMaskColor(byte[] paramArrayOfByte, GraphicsState paramGraphicsState)
  {
    int i = paramGraphicsState.nonstrokeColorSpace.getColor().getRGB();
    paramArrayOfByte[0] = ((byte)(i >> 16 & 0xFF));
    paramArrayOfByte[1] = ((byte)(i >> 8 & 0xFF));
    paramArrayOfByte[2] = ((byte)(i & 0xFF));
  }

  static boolean isRepeatingLine(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length % paramInt != 0)
      return false;
    int i = paramArrayOfByte.length / paramInt;
    for (int j = 0; j < paramArrayOfByte.length / paramInt - 1; j++)
    {
      int k = i;
      while (k < paramArrayOfByte.length - 1)
      {
        if (paramArrayOfByte[j] != paramArrayOfByte[k])
          return false;
        k += i;
      }
    }
    return true;
  }

  public static BufferedImage applySmask(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, PdfObject paramPdfObject1, boolean paramBoolean1, boolean paramBoolean2, PdfObject paramPdfObject2, PdfObject paramPdfObject3, GraphicsState paramGraphicsState)
  {
    PdfArrayIterator localPdfArrayIterator = paramPdfObject3.getMixedArray(1011108731);
    int i = 0;
    int j;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
      while (localPdfArrayIterator.hasMoreTokens())
      {
        j = localPdfArrayIterator.getNextValueAsConstant(true);
        i = j == 1180911742 ? 1 : 0;
      }
    int k = (i == 0) && (paramGraphicsState.CTM[1][1] > 0.0F) && (paramGraphicsState.CTM[0][0] < 0.0F) ? 1 : 0;
    int m = paramBufferedImage1.getType();
    int[] arrayOfInt1 = { 0 };
    int[] arrayOfInt2 = { 0, 0, 0, 0 };
    int[] arrayOfInt3 = { 0, 0, 0, 0 };
    localPdfArrayIterator = paramPdfObject1.getMixedArray(1011108731);
    int n = 0;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
      while (localPdfArrayIterator.hasMoreTokens())
      {
        j = localPdfArrayIterator.getNextValueAsConstant(true);
        n = j == 1180911742 ? 1 : 0;
      }
    int i1 = (paramPdfObject2 != null) && (paramPdfObject2.getParameterConstant(2087749783) == 1568372915) ? 1 : 0;
    int i2 = (n != 0) && (i1 != 0) ? 1 : 0;
    int i3 = -1;
    if (paramPdfObject2 != null)
      i3 = paramPdfObject2.getParameterConstant(2087749783);
    i2 = (i2 == 0) && (paramBoolean1) && (paramPdfObject2 != null) && ((i3 == 1498837125) || (i3 == 1247168582)) ? 1 : 0;
    if (i2 != 0)
    {
      paramBufferedImage2 = ColorSpaceConvertor.convertColorspace(paramBufferedImage2, 10);
      arrayOfInt2 = arrayOfInt1;
    }
    if (paramBufferedImage2 == null)
      return paramBufferedImage1;
    WritableRaster localWritableRaster1 = paramBufferedImage2.getRaster();
    WritableRaster localWritableRaster2 = null;
    int i4 = 0;
    int i5 = paramBufferedImage1.getWidth();
    int i6 = paramBufferedImage1.getHeight();
    int i7 = paramBufferedImage2.getWidth();
    int i8 = paramBufferedImage2.getHeight();
    float f1 = 0.0F;
    float f2 = 0.0F;
    if ((i5 != i7) || (i6 != i8))
    {
      f1 = i5 / i7;
      f2 = i6 / i8;
      if ((paramBoolean2) && (f1 == 0.5D) && (f2 == 0.5D) && (n == 0))
      {
        BufferedImage localBufferedImage = new BufferedImage(i7, i8, paramBufferedImage1.getType());
        localObject = localBufferedImage.createGraphics();
        ((Graphics2D)localObject).dispose();
        ((Graphics2D)localObject).setComposite(AlphaComposite.Src);
        ((Graphics2D)localObject).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D)localObject).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D)localObject).drawImage(paramBufferedImage1, 0, 0, i7, i8, null);
        paramBufferedImage1 = localBufferedImage;
        i5 = i7;
        i6 = i8;
        f1 = 1.0F;
        f2 = 1.0F;
      }
    }
    int i9 = paramBufferedImage2.getColorModel().getNumComponents();
    Object localObject = new int[i9];
    int[] arrayOfInt4 = new int[4];
    int i11 = localWritableRaster1.getHeight();
    for (int i12 = 0; i12 < i6; i12++)
      for (int i13 = 0; i13 < i5; i13++)
      {
        int i10;
        if ((k != 0) && (i11 >= i6))
          i10 = i11 - i12 - 1;
        else
          i10 = i12;
        if (f1 == 0.0F)
          localWritableRaster1.getPixels(i13, i10, 1, 1, (int[])localObject);
        else
          localWritableRaster1.getPixels((int)(i13 / f1), (int)(i10 / f2), 1, 1, (int[])localObject);
        int i14 = 0;
        if (i9 == 1)
        {
          if ((localObject[0] > 127) && (n != 0))
            i14 = 1;
        }
        else
          for (int i15 = 0; i15 < i9; i15++)
            if (localObject[i15] != arrayOfInt2[i15])
            {
              i15 = i9;
              i14 = 1;
            }
        if (i14 == 0)
        {
          if (i4 == 0)
          {
            paramBufferedImage1 = ColorSpaceConvertor.convertToARGB(paramBufferedImage1);
            localWritableRaster2 = paramBufferedImage1.getRaster();
            i4 = 1;
          }
          if (i9 == 1)
          {
            localWritableRaster2.getPixels(i13, i12, 1, 1, arrayOfInt4);
            if (localObject[0] == arrayOfInt4[0])
            {
              if ((arrayOfInt4[0] == 255) && ((m == 1) || (m == 5) || (m == 10)) && (i1 != 0))
                localWritableRaster2.setPixels(i13, i12, 1, 1, new int[] { arrayOfInt4[0], arrayOfInt4[1], arrayOfInt4[2], localObject[0] });
              else
                localWritableRaster2.setPixels(i13, i12, 1, 1, arrayOfInt3);
            }
            else
              localWritableRaster2.setPixels(i13, i12, 1, 1, new int[] { arrayOfInt4[0], arrayOfInt4[1], arrayOfInt4[2], localObject[0] });
          }
          else
          {
            localWritableRaster2.setPixels(i13, i12, 1, 1, arrayOfInt3);
          }
        }
      }
    return paramBufferedImage1;
  }

  static BufferedImage simulateOverprint(GenericColorSpace paramGenericColorSpace, byte[] paramArrayOfByte, boolean paramBoolean1, boolean paramBoolean2, BufferedImage paramBufferedImage, int paramInt, PdfObject paramPdfObject1, PdfObject paramPdfObject2, DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
    if (((paramInt == 1498837125) || (paramInt == 1247168582)) && (paramGraphicsState.getOPM() == 1.0F))
    {
      int i = 0;
      byte[] arrayOfByte = paramGenericColorSpace.getIndexedMap();
      if ((arrayOfByte == null) && (paramDynamicVectorRenderer.hasObjectsBehind(paramGraphicsState.CTM)))
      {
        i = 1;
        for (int j = 0; j < paramArrayOfByte.length; j++)
          if (paramArrayOfByte[j] != 0)
          {
            j = paramArrayOfByte.length;
            i = 0;
          }
      }
      if (i != 0)
      {
        paramBufferedImage.flush();
        paramBufferedImage = null;
      }
      else if (((paramBoolean1) || (paramBoolean2)) && (paramGraphicsState.getNonStrokeOP()))
      {
        paramBufferedImage = simulateOP(paramBufferedImage, paramBoolean2);
      }
      else if ((paramBoolean1) && (paramPdfObject2 == null) && (paramPdfObject1 == null) && ((paramGraphicsState.nonstrokeColorSpace.getColor().getRGB() == -16777216) || (paramGraphicsState.nonstrokeColorSpace.getColor().getRGB() == -1)) && (paramGenericColorSpace.isImageYCCK()) && (paramGenericColorSpace.getIntent() != null) && (paramGenericColorSpace.getIntent().equals("RelativeColorimetric")))
      {
        paramBufferedImage = simulateOP(paramBufferedImage, true);
      }
      else if (paramGraphicsState.getNonStrokeOP())
      {
        if (paramInt == 1498837125)
          paramBufferedImage = simulateOP(paramBufferedImage, false);
        else
          paramBufferedImage = simulateOP(paramBufferedImage, paramBufferedImage.getType() == 1);
      }
    }
    return paramBufferedImage;
  }

  static BufferedImage addBackgroundToMask(BufferedImage paramBufferedImage, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int i = paramBufferedImage.getWidth();
      int j = paramBufferedImage.getHeight();
      BufferedImage localBufferedImage = new BufferedImage(i, j, 1);
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.setColor(Color.white);
      localGraphics2D.fillRect(0, 0, i, j);
      localGraphics2D.drawImage(paramBufferedImage, 0, 0, null);
      paramBufferedImage = localBufferedImage;
    }
    return paramBufferedImage;
  }

  static BufferedImage applyTR(BufferedImage paramBufferedImage, PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    PDFFunction[] arrayOfPDFFunction = new PDFFunction[4];
    int i = 0;
    byte[][] arrayOfByte = paramPdfObject.getKeyArray(9250);
    if (arrayOfByte != null)
      i = arrayOfByte.length;
    for (int j = 0; j < i; j++)
      if (arrayOfByte[j] != null)
      {
        localObject = new String(arrayOfByte[j]);
        FunctionObject localFunctionObject = new FunctionObject((String)localObject);
        byte[] arrayOfByte1 = arrayOfByte[j];
        if ((arrayOfByte1 != null) && (arrayOfByte1.length > 4) && (arrayOfByte1[0] == 47) && (arrayOfByte1[1] == 73) && (arrayOfByte1[2] == 100) && (arrayOfByte1[3] == 101))
          localFunctionObject = null;
        else
          paramPdfObjectReader.readObject(localFunctionObject);
        if (localFunctionObject != null)
          arrayOfPDFFunction[j] = FunctionFactory.getFunction(localFunctionObject, paramPdfObjectReader);
      }
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    Object localObject = new int[4];
    for (int k = 0; k < paramBufferedImage.getHeight(); k++)
      for (int m = 0; m < paramBufferedImage.getWidth(); m++)
      {
        localWritableRaster.getPixels(m, k, 1, 1, (int[])localObject);
        for (int n = 0; n < 3; n++)
        {
          float[] arrayOfFloat1 = { localObject[n] / 255.0F };
          if (arrayOfPDFFunction[n] != null)
          {
            float[] arrayOfFloat2 = arrayOfPDFFunction[n].compute(arrayOfFloat1);
            localObject[n] = ((int)(255.0F * arrayOfFloat2[0]));
          }
        }
        paramBufferedImage.getRaster().setPixels(m, k, 1, 1, (int[])localObject);
      }
    return paramBufferedImage;
  }

  static void applyDecodeArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    int i = paramArrayOfFloat.length;
    int j = 0;
    for (float f : paramArrayOfFloat)
      if (j < f)
        j = (int)f;
    int k = 1;
    ??? = paramArrayOfFloat.length;
    ??? = 0;
    while (??? < ???)
    {
      if ((paramArrayOfFloat[???] != 0.0F) || ((paramArrayOfFloat[(??? + 1)] != 1.0F) && (paramArrayOfFloat[(??? + 1)] != 255.0F)))
      {
        k = 0;
        ??? = ???;
      }
      ??? += 2;
    }
    if (k != 0)
      return;
    int i1;
    if (paramInt1 == 1)
    {
      if (paramArrayOfFloat[0] > paramArrayOfFloat[1])
      {
        ??? = paramArrayOfByte.length;
        for (i1 = 0; i1 < ???; i1++)
          paramArrayOfByte[i1] = ((byte)(paramArrayOfByte[i1] ^ 0xFFFFFFFF));
      }
    }
    else
    {
      int i2;
      if ((paramInt1 == 8) && (j > 1) && ((paramInt2 == 1785221209) || (paramInt2 == 1008872003) || (paramInt2 == 1498837125)))
      {
        ??? = 0;
        for (i1 = 0; i1 < paramArrayOfByte.length; i1++)
        {
          i2 = paramArrayOfByte[i1] & 0xFF;
          if (i2 < paramArrayOfFloat[???])
            i2 = (int)paramArrayOfFloat[???];
          else if (i2 > paramArrayOfFloat[(??? + 1)])
            i2 = (int)paramArrayOfFloat[(??? + 1)];
          ??? += 2;
          if (??? == paramArrayOfFloat.length)
            ??? = 0;
          paramArrayOfByte[i1] = ((byte)i2);
        }
      }
      else
      {
        j = paramInt1 << 1;
        ??? = j - 1;
        for (i1 = 0; i1 < paramArrayOfByte.length; i1++)
        {
          i2 = paramArrayOfByte[i1];
          int i3 = 0;
          int i4 = 0;
          int i5 = 0;
          int i6 = 1;
          for (int i7 = 7; i7 > -1; i7--)
          {
            int i8 = i2 >> i7 & 0x1;
            i8 = (int)(paramArrayOfFloat[i5] + i8 * ((paramArrayOfFloat[i6] - paramArrayOfFloat[i5]) / ???));
            if (i8 > j)
              i8 = j;
            if (i8 < 0)
              i8 = 0;
            i8 = (i8 & 0x1) << i7;
            i4 += i8;
            i3 += 2;
            if (i3 == i)
            {
              i3 = 0;
              i5 = 0;
              i6 = 1;
            }
            else
            {
              i5 += 2;
              i6 += 2;
            }
          }
          paramArrayOfByte[i1] = ((byte)i4);
        }
      }
    }
  }

  static boolean isExtractionAllowed(PdfObjectReader paramPdfObjectReader)
  {
    PdfFileReader localPdfFileReader = paramPdfObjectReader.getObjectReader();
    DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
    return (localDecryptionFactory == null) || (localDecryptionFactory.getBooleanValue(103));
  }

  public static BufferedImage handle1bitSMask(BufferedImage paramBufferedImage, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = paramBufferedImage;
    paramBufferedImage = new BufferedImage(paramInt1, paramInt2, 2);
    WritableRaster localWritableRaster1 = localBufferedImage.getRaster();
    WritableRaster localWritableRaster2 = paramBufferedImage.getRaster();
    float f1 = localBufferedImage.getWidth() / paramBufferedImage.getWidth();
    float f2 = localBufferedImage.getHeight() / paramBufferedImage.getHeight();
    int i = 0;
    int k = 0;
    int m = 0;
    int n = paramArrayOfByte.length;
    int[] arrayOfInt = new int[3];
    for (int i3 : paramArrayOfByte)
      for (int i4 = 0; i4 < 8; i4++)
      {
        int j = (i3 >> 7 - i4 & 0x1) * 3;
        if (i >= n)
          break;
        if (j == 0)
        {
          localWritableRaster1.getPixels((int)(m * f1), (int)(k * f2), 1, 1, arrayOfInt);
          localWritableRaster2.setPixels(m, k, 1, 1, new int[] { arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], 255 });
        }
        m++;
        if (m == paramInt1)
        {
          m = 0;
          i4 = 8;
          k++;
        }
      }
    return paramBufferedImage;
  }

  static
  {
    String str1 = System.getProperty("org.jpedal.rejectsuperimposedimages");
    if (str1 != null)
      rejectSuperimposedImages = (str1 != null) && (str1.toLowerCase().contains("true"));
    String str2 = System.getProperty("org.jpedal.trackImages");
    if (str2 != null)
      trackImages = (str2 != null) && (str2.toLowerCase().contains("true"));
    String str3 = System.getProperty("org.jpedal.sharpendownsampledimages");
    if (str3 != null)
      sharpenDownsampledImages = str3.toLowerCase().contains("true");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.ImageCommands
 * JD-Core Version:    0.6.2
 */
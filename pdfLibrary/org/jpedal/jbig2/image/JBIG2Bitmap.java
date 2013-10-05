package org.jpedal.jbig2.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.DecodeIntResult;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.decoders.MMRDecoder;

public final class JBIG2Bitmap
{
  private int height;
  private final int width;
  private final int line;
  private int bitmapNumber;
  private byte[] data;
  private final ArithmeticDecoder arithmeticDecoder;
  private final HuffmanDecoder huffmanDecoder;
  private final MMRDecoder mmrDecoder;

  public JBIG2Bitmap(int paramInt1, int paramInt2, ArithmeticDecoder paramArithmeticDecoder, HuffmanDecoder paramHuffmanDecoder, MMRDecoder paramMMRDecoder)
  {
    this.width = paramInt1;
    this.height = paramInt2;
    this.arithmeticDecoder = paramArithmeticDecoder;
    this.huffmanDecoder = paramHuffmanDecoder;
    this.mmrDecoder = paramMMRDecoder;
    this.line = (paramInt1 + 7 >> 3);
    this.data = new byte[this.line * (paramInt2 + 1)];
  }

  public void readBitmap(boolean paramBoolean1, int paramInt1, boolean paramBoolean2, boolean paramBoolean3, JBIG2Bitmap paramJBIG2Bitmap, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int paramInt2)
  {
    Object localObject1;
    Object localObject2;
    int i2;
    if (paramBoolean1)
    {
      this.mmrDecoder.reset();
      localObject1 = new int[this.width + 2];
      localObject2 = new int[this.width + 2];
      localObject2[0] = this.width;
      localObject2[1] = this.width;
      for (int i = 0; i < this.height; i++)
      {
        for (int j = 0; localObject2[j] < this.width; j++)
          localObject1[j] = localObject2[j];
        localObject1[j] = this.width;
        localObject1[(j + 1)] = this.width;
        int k = 0;
        int m = 0;
        int n = 0;
        do
        {
          i1 = this.mmrDecoder.get2DCode();
          switch (i1)
          {
          case 0:
            if (localObject1[k] < this.width)
            {
              n = localObject1[(k + 1)];
              k += 2;
            }
            break;
          case 1:
            int i3;
            if ((m & 0x1) != 0)
            {
              i1 = 0;
              do
              {
                i3 = this.mmrDecoder.getBlackCode();
                i1 += i3;
              }
              while (i3 >= 64);
              i2 = 0;
              do
              {
                i3 = this.mmrDecoder.getWhiteCode();
                i2 += i3;
              }
              while (i3 >= 64);
            }
            else
            {
              i1 = 0;
              do
              {
                i3 = this.mmrDecoder.getWhiteCode();
                i1 += i3;
              }
              while (i3 >= 64);
              i2 = 0;
              do
              {
                i3 = this.mmrDecoder.getBlackCode();
                i2 += i3;
              }
              while (i3 >= 64);
            }
            if ((i1 > 0) || (i2 > 0))
            {
              n += i1;
              localObject2[m] = n;
              m++;
              n += i2;
              localObject2[m] = n;
              m++;
            }
            break;
          case 2:
          case 3:
          case 5:
          case 7:
          case 4:
          case 6:
          case 8:
            while ((localObject1[k] <= n) && (localObject1[k] < this.width))
            {
              k += 2;
              continue;
              n = localObject1[k];
              localObject2[m] = n;
              m++;
              if (localObject1[k] < this.width)
              {
                k++;
                break;
                n = localObject1[k] + 1;
                localObject2[m] = n;
                m++;
                if (localObject1[k] < this.width)
                {
                  k++;
                  while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                  {
                    k += 2;
                    continue;
                    n = localObject1[k] + 2;
                    localObject2[m] = n;
                    m++;
                    if (localObject1[k] < this.width)
                    {
                      k++;
                      while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                      {
                        k += 2;
                        continue;
                        n = localObject1[k] + 3;
                        localObject2[m] = n;
                        m++;
                        if (localObject1[k] < this.width)
                        {
                          k++;
                          while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                          {
                            k += 2;
                            continue;
                            n = localObject1[k] - 1;
                            localObject2[m] = n;
                            m++;
                            if (k > 0)
                              k--;
                            else
                              k++;
                            while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                            {
                              k += 2;
                              continue;
                              n = localObject1[k] - 2;
                              localObject2[m] = n;
                              m++;
                              if (k > 0)
                                k--;
                              else
                                k++;
                              while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                              {
                                k += 2;
                                continue;
                                n = localObject1[k] - 3;
                                localObject2[m] = n;
                                m++;
                                if (k > 0)
                                  k--;
                                else
                                  k++;
                                while ((localObject1[k] <= n) && (localObject1[k] < this.width))
                                  k += 2;
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        while (n < this.width);
        localObject2[m] = this.width;
        for (int i1 = 0; localObject2[i1] < this.width; i1 += 2)
          for (i2 = localObject2[i1]; i2 < localObject2[(i1 + 1)]; i2++)
            setPixel(i2, i, 1);
      }
      if (paramInt2 >= 0)
        this.mmrDecoder.skipTo(paramInt2);
      else if (this.mmrDecoder.get24Bits() == 4097L);
    }
    else
    {
      localObject1 = new BitmapPointer(this);
      localObject2 = new BitmapPointer(this);
      BitmapPointer localBitmapPointer1 = new BitmapPointer(this);
      BitmapPointer localBitmapPointer2 = new BitmapPointer(this);
      BitmapPointer localBitmapPointer3 = new BitmapPointer(this);
      BitmapPointer localBitmapPointer4 = new BitmapPointer(this);
      long l1 = 0L;
      if (paramBoolean2)
        switch (paramInt1)
        {
        case 0:
          l1 = 14675L;
          break;
        case 1:
          l1 = 1946L;
          break;
        case 2:
          l1 = 227L;
          break;
        case 3:
          l1 = 394L;
        }
      i2 = 0;
      byte[] arrayOfByte;
      int i4;
      if (paramBoolean3)
      {
        arrayOfByte = paramJBIG2Bitmap.data;
        i4 = paramJBIG2Bitmap.line;
      }
      else
      {
        arrayOfByte = null;
        i4 = 0;
      }
      for (int i5 = 0; i5 < this.height; i5++)
      {
        int i6;
        if (paramBoolean2)
        {
          i6 = this.arithmeticDecoder.decodeBit(l1, this.arithmeticDecoder.genericRegionStats);
          if (i6 != 0)
            i2 = i2 == 0 ? 1 : 0;
          if (i2 != 0)
          {
            duplicateRow(i5, i5 - 1);
            continue;
          }
        }
        long l3;
        long l4;
        long l5;
        int i7;
        long l2;
        switch (paramInt1)
        {
        case 0:
          ((BitmapPointer)localObject1).setPointer(0, i5 - 2);
          l3 = ((BitmapPointer)localObject1).nextPixel();
          l3 = l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel();
          ((BitmapPointer)localObject2).setPointer(0, i5 - 1);
          l4 = ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l5 = 0L;
          localBitmapPointer1.setPointer(paramArrayOfShort1[0], i5 + paramArrayOfShort2[0]);
          localBitmapPointer2.setPointer(paramArrayOfShort1[1], i5 + paramArrayOfShort2[1]);
          localBitmapPointer3.setPointer(paramArrayOfShort1[2], i5 + paramArrayOfShort2[2]);
          localBitmapPointer4.setPointer(paramArrayOfShort1[3], i5 + paramArrayOfShort2[3]);
          for (i7 = 0; i7 < this.width; i7++)
          {
            l2 = l3 << 13 & 0xFFFFFFFF | l4 << 8 & 0xFFFFFFFF | l5 << 4 & 0xFFFFFFFF | localBitmapPointer1.nextPixel() << 3 | localBitmapPointer2.nextPixel() << 2 | localBitmapPointer3.nextPixel() << 1 | localBitmapPointer4.nextPixel();
            if ((paramBoolean3) && (getPixel(i7, i5, i4, arrayOfByte) != 0))
            {
              i6 = 0;
            }
            else
            {
              i6 = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.genericRegionStats);
              if (i6 != 0)
                setPixel(i7, i5, 1);
            }
            l3 = (l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel()) & 0x7;
            l4 = (l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel()) & 0x1F;
            l5 = (l5 << 1 & 0xFFFFFFFF | i6) & 0xF;
          }
          break;
        case 1:
          ((BitmapPointer)localObject1).setPointer(0, i5 - 2);
          l3 = ((BitmapPointer)localObject1).nextPixel();
          l3 = l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel();
          l3 = l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel();
          ((BitmapPointer)localObject2).setPointer(0, i5 - 1);
          l4 = ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l5 = 0L;
          localBitmapPointer1.setPointer(paramArrayOfShort1[0], i5 + paramArrayOfShort2[0]);
          for (i7 = 0; i7 < this.width; i7++)
          {
            l2 = l3 << 9 & 0xFFFFFFFF | l4 << 4 & 0xFFFFFFFF | l5 << 1 & 0xFFFFFFFF | localBitmapPointer1.nextPixel();
            if ((paramBoolean3) && (getPixel(i7, i5, i4, arrayOfByte) != 0))
            {
              i6 = 0;
            }
            else
            {
              i6 = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.genericRegionStats);
              if (i6 != 0)
                setPixel(i7, i5, 1);
            }
            l3 = (l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel()) & 0xF;
            l4 = (l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel()) & 0x1F;
            l5 = (l5 << 1 & 0xFFFFFFFF | i6) & 0x7;
          }
          break;
        case 2:
          ((BitmapPointer)localObject1).setPointer(0, i5 - 2);
          l3 = ((BitmapPointer)localObject1).nextPixel();
          l3 = l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel();
          ((BitmapPointer)localObject2).setPointer(0, i5 - 1);
          l4 = ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l5 = 0L;
          localBitmapPointer1.setPointer(paramArrayOfShort1[0], i5 + paramArrayOfShort2[0]);
          for (i7 = 0; i7 < this.width; i7++)
          {
            l2 = l3 << 7 & 0xFFFFFFFF | l4 << 3 & 0xFFFFFFFF | l5 << 1 & 0xFFFFFFFF | localBitmapPointer1.nextPixel();
            if ((paramBoolean3) && (getPixel(i7, i5, i4, arrayOfByte) != 0))
            {
              i6 = 0;
            }
            else
            {
              i6 = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.genericRegionStats);
              if (i6 != 0)
                setPixel(i7, i5, 1);
            }
            l3 = (l3 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject1).nextPixel()) & 0x7;
            l4 = (l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel()) & 0xF;
            l5 = (l5 << 1 & 0xFFFFFFFF | i6) & 0x3;
          }
          break;
        case 3:
          ((BitmapPointer)localObject2).setPointer(0, i5 - 1);
          l4 = ((BitmapPointer)localObject2).nextPixel();
          l4 = l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel();
          l5 = 0L;
          localBitmapPointer1.setPointer(paramArrayOfShort1[0], i5 + paramArrayOfShort2[0]);
          for (i7 = 0; i7 < this.width; i7++)
          {
            l2 = l4 << 5 & 0xFFFFFFFF | l5 << 1 & 0xFFFFFFFF | localBitmapPointer1.nextPixel();
            if ((paramBoolean3) && (getPixel(i7, i5, i4, arrayOfByte) != 0))
            {
              i6 = 0;
            }
            else
            {
              i6 = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.genericRegionStats);
              if (i6 != 0)
                setPixel(i7, i5, 1);
            }
            l4 = (l4 << 1 & 0xFFFFFFFF | ((BitmapPointer)localObject2).nextPixel()) & 0x1F;
            l5 = (l5 << 1 & 0xFFFFFFFF | i6) & 0xF;
          }
        }
      }
    }
  }

  public void readGenericRefinementRegion(int paramInt1, boolean paramBoolean, JBIG2Bitmap paramJBIG2Bitmap, int paramInt2, int paramInt3, short[] paramArrayOfShort1, short[] paramArrayOfShort2)
  {
    long l1;
    BitmapPointer localBitmapPointer1;
    BitmapPointer localBitmapPointer2;
    BitmapPointer localBitmapPointer3;
    BitmapPointer localBitmapPointer4;
    BitmapPointer localBitmapPointer5;
    BitmapPointer localBitmapPointer6;
    BitmapPointer localBitmapPointer7;
    BitmapPointer localBitmapPointer8;
    BitmapPointer localBitmapPointer9;
    BitmapPointer localBitmapPointer10;
    if (paramInt1 != 0)
    {
      l1 = 8L;
      localBitmapPointer1 = new BitmapPointer(this);
      localBitmapPointer2 = new BitmapPointer(this);
      localBitmapPointer3 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer4 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer5 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer6 = new BitmapPointer(this);
      localBitmapPointer7 = new BitmapPointer(this);
      localBitmapPointer8 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer9 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer10 = new BitmapPointer(paramJBIG2Bitmap);
    }
    else
    {
      l1 = 16L;
      localBitmapPointer1 = new BitmapPointer(this);
      localBitmapPointer2 = new BitmapPointer(this);
      localBitmapPointer3 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer4 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer5 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer6 = new BitmapPointer(this);
      localBitmapPointer7 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer8 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer9 = new BitmapPointer(paramJBIG2Bitmap);
      localBitmapPointer10 = new BitmapPointer(paramJBIG2Bitmap);
    }
    int i = 0;
    for (int j = 0; j < this.height; j++)
    {
      long l3;
      long l5;
      long l6;
      long l7;
      long l8;
      long l9;
      int k;
      int m;
      long l2;
      if (paramInt1 != 0)
      {
        localBitmapPointer1.setPointer(0, j - 1);
        l3 = localBitmapPointer1.nextPixel();
        localBitmapPointer2.setPointer(-1, j);
        localBitmapPointer3.setPointer(-paramInt2, j - 1 - paramInt3);
        localBitmapPointer4.setPointer(-1 - paramInt2, j - paramInt3);
        l5 = localBitmapPointer4.nextPixel();
        l5 = l5 << 1 & 0xFFFFFFFF | localBitmapPointer4.nextPixel();
        localBitmapPointer5.setPointer(-paramInt2, j + 1 - paramInt3);
        l6 = localBitmapPointer5.nextPixel();
        l7 = 0L;
        l8 = 0L;
        l9 = 0L;
        if (paramBoolean)
        {
          localBitmapPointer8.setPointer(-1 - paramInt2, j - 1 - paramInt3);
          l7 = localBitmapPointer8.nextPixel();
          l7 = l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel();
          l7 = l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel();
          localBitmapPointer9.setPointer(-1 - paramInt2, j - paramInt3);
          l8 = localBitmapPointer9.nextPixel();
          l8 = l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel();
          l8 = l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel();
          localBitmapPointer10.setPointer(-1 - paramInt2, j + 1 - paramInt3);
          l9 = localBitmapPointer10.nextPixel();
          l9 = l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel();
          l9 = l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel();
        }
        for (k = 0; k < this.width; k++)
        {
          l3 = (l3 << 1 & 0xFFFFFFFF | localBitmapPointer1.nextPixel()) & 0x7;
          l5 = (l5 << 1 & 0xFFFFFFFF | localBitmapPointer4.nextPixel()) & 0x7;
          l6 = (l6 << 1 & 0xFFFFFFFF | localBitmapPointer5.nextPixel()) & 0x3;
          if (paramBoolean)
          {
            l7 = (l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel()) & 0x7;
            l8 = (l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel()) & 0x7;
            l9 = (l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel()) & 0x7;
            m = this.arithmeticDecoder.decodeBit(l1, this.arithmeticDecoder.refinementRegionStats);
            if (m != 0)
              i = i == 0 ? 1 : 0;
            if ((l7 == 0L) && (l8 == 0L) && (l9 == 0L))
            {
              setPixel(k, j, 0);
              continue;
            }
            if ((l7 == 7L) && (l8 == 7L) && (l9 == 7L))
            {
              setPixel(k, j, 1);
              continue;
            }
          }
          l2 = l3 << 7 & 0xFFFFFFFF | localBitmapPointer2.nextPixel() << 6 | localBitmapPointer3.nextPixel() << 5 | l5 << 2 & 0xFFFFFFFF | l6;
          m = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.refinementRegionStats);
          if (m == 1)
            setPixel(k, j, 1);
        }
      }
      else
      {
        localBitmapPointer1.setPointer(0, j - 1);
        l3 = localBitmapPointer1.nextPixel();
        localBitmapPointer2.setPointer(-1, j);
        localBitmapPointer3.setPointer(-paramInt2, j - 1 - paramInt3);
        long l4 = localBitmapPointer3.nextPixel();
        localBitmapPointer4.setPointer(-1 - paramInt2, j - paramInt3);
        l5 = localBitmapPointer4.nextPixel();
        l5 = l5 << 1 & 0xFFFFFFFF | localBitmapPointer4.nextPixel();
        localBitmapPointer5.setPointer(-1 - paramInt2, j + 1 - paramInt3);
        l6 = localBitmapPointer5.nextPixel();
        l6 = l6 << 1 & 0xFFFFFFFF | localBitmapPointer5.nextPixel();
        localBitmapPointer6.setPointer(paramArrayOfShort1[0], j + paramArrayOfShort2[0]);
        localBitmapPointer7.setPointer(paramArrayOfShort1[1] - paramInt2, j + paramArrayOfShort2[1] - paramInt3);
        l7 = 0L;
        l8 = 0L;
        l9 = 0L;
        if (paramBoolean)
        {
          localBitmapPointer8.setPointer(-1 - paramInt2, j - 1 - paramInt3);
          l7 = localBitmapPointer8.nextPixel();
          l7 = l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel();
          l7 = l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel();
          localBitmapPointer9.setPointer(-1 - paramInt2, j - paramInt3);
          l8 = localBitmapPointer9.nextPixel();
          l8 = l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel();
          l8 = l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel();
          localBitmapPointer10.setPointer(-1 - paramInt2, j + 1 - paramInt3);
          l9 = localBitmapPointer10.nextPixel();
          l9 = l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel();
          l9 = l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel();
        }
        for (k = 0; k < this.width; k++)
        {
          l3 = (l3 << 1 & 0xFFFFFFFF | localBitmapPointer1.nextPixel()) & 0x3;
          l4 = (l4 << 1 & 0xFFFFFFFF | localBitmapPointer3.nextPixel()) & 0x3;
          l5 = (l5 << 1 & 0xFFFFFFFF | localBitmapPointer4.nextPixel()) & 0x7;
          l6 = (l6 << 1 & 0xFFFFFFFF | localBitmapPointer5.nextPixel()) & 0x7;
          if (paramBoolean)
          {
            l7 = (l7 << 1 & 0xFFFFFFFF | localBitmapPointer8.nextPixel()) & 0x7;
            l8 = (l8 << 1 & 0xFFFFFFFF | localBitmapPointer9.nextPixel()) & 0x7;
            l9 = (l9 << 1 & 0xFFFFFFFF | localBitmapPointer10.nextPixel()) & 0x7;
            m = this.arithmeticDecoder.decodeBit(l1, this.arithmeticDecoder.refinementRegionStats);
            if (m == 1)
              i = i == 0 ? 1 : 0;
            if ((l7 == 0L) && (l8 == 0L) && (l9 == 0L))
            {
              setPixel(k, j, 0);
              continue;
            }
            if ((l7 == 7L) && (l8 == 7L) && (l9 == 7L))
            {
              setPixel(k, j, 1);
              continue;
            }
          }
          l2 = l3 << 11 & 0xFFFFFFFF | localBitmapPointer2.nextPixel() << 10 | l4 << 8 & 0xFFFFFFFF | l5 << 5 & 0xFFFFFFFF | l6 << 2 & 0xFFFFFFFF | localBitmapPointer6.nextPixel() << 1 | localBitmapPointer7.nextPixel();
          m = this.arithmeticDecoder.decodeBit(l2, this.arithmeticDecoder.refinementRegionStats);
          if (m == 1)
            setPixel(k, j, 1);
        }
      }
    }
  }

  public void readTextRegion(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, int paramInt3, int[][] paramArrayOfInt1, int paramInt4, JBIG2Bitmap[] paramArrayOfJBIG2Bitmap, int paramInt5, int paramInt6, boolean paramBoolean3, int paramInt7, int paramInt8, int[][] paramArrayOfInt2, int[][] paramArrayOfInt3, int[][] paramArrayOfInt4, int[][] paramArrayOfInt5, int[][] paramArrayOfInt6, int[][] paramArrayOfInt7, int[][] paramArrayOfInt8, int paramInt9, short[] paramArrayOfShort1, short[] paramArrayOfShort2, JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    int i = 1 << paramInt2;
    clear(paramInt5);
    int j;
    if (paramBoolean1)
      j = this.huffmanDecoder.decodeInt(paramArrayOfInt4).intResult();
    else
      j = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadtStats).intResult();
    j *= -i;
    int k = 0;
    int m = 0;
    if (k < paramInt1)
    {
      int n;
      if (paramBoolean1)
        n = this.huffmanDecoder.decodeInt(paramArrayOfInt4).intResult();
      else
        n = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadtStats).intResult();
      j += n * i;
      int i2;
      if (paramBoolean1)
        i2 = this.huffmanDecoder.decodeInt(paramArrayOfInt2).intResult();
      else
        i2 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iafsStats).intResult();
      m += i2;
      int i3 = m;
      while (true)
      {
        if (i == 1)
          n = 0;
        else if (paramBoolean1)
          n = paramJBIG2StreamDecoder.readBits(paramInt2);
        else
          n = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaitStats).intResult();
        int i1 = j + n;
        long l;
        if (paramBoolean1)
        {
          if (paramArrayOfInt1 != null)
            l = this.huffmanDecoder.decodeInt(paramArrayOfInt1).intResult();
          else
            l = paramJBIG2StreamDecoder.readBits(paramInt4);
        }
        else
          l = this.arithmeticDecoder.decodeIAID(paramInt4, this.arithmeticDecoder.iaidStats);
        if (l < paramInt3)
        {
          int i4;
          if (paramBoolean2)
          {
            if (paramBoolean1)
              i4 = paramJBIG2StreamDecoder.readBit();
            else
              i4 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iariStats).intResult();
          }
          else
            i4 = 0;
          JBIG2Bitmap localJBIG2Bitmap;
          if (i4 != 0)
          {
            if (paramBoolean1)
            {
              i5 = this.huffmanDecoder.decodeInt(paramArrayOfInt5).intResult();
              i6 = this.huffmanDecoder.decodeInt(paramArrayOfInt6).intResult();
              i7 = this.huffmanDecoder.decodeInt(paramArrayOfInt7).intResult();
              i8 = this.huffmanDecoder.decodeInt(paramArrayOfInt8).intResult();
              paramJBIG2StreamDecoder.consumeRemainingBits();
              this.arithmeticDecoder.start();
            }
            else
            {
              i5 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardwStats).intResult();
              i6 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardhStats).intResult();
              i7 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardxStats).intResult();
              i8 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardyStats).intResult();
            }
            int i7 = (i5 >= 0 ? i5 : i5 - 1) / 2 + i7;
            int i8 = (i6 >= 0 ? i6 : i6 - 1) / 2 + i8;
            localJBIG2Bitmap = new JBIG2Bitmap(i5 + paramArrayOfJBIG2Bitmap[((int)l)].width, i6 + paramArrayOfJBIG2Bitmap[((int)l)].height, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
            localJBIG2Bitmap.readGenericRefinementRegion(paramInt9, false, paramArrayOfJBIG2Bitmap[((int)l)], i7, i8, paramArrayOfShort1, paramArrayOfShort2);
          }
          else
          {
            localJBIG2Bitmap = paramArrayOfJBIG2Bitmap[((int)l)];
          }
          int i5 = localJBIG2Bitmap.width - 1;
          int i6 = localJBIG2Bitmap.height - 1;
          if (paramBoolean3)
          {
            switch (paramInt7)
            {
            case 0:
              combine(localJBIG2Bitmap, i1, i3, paramInt6);
              break;
            case 1:
              combine(localJBIG2Bitmap, i1, i3, paramInt6);
              break;
            case 2:
              combine(localJBIG2Bitmap, i1 - i5, i3, paramInt6);
              break;
            case 3:
              combine(localJBIG2Bitmap, i1 - i5, i3, paramInt6);
            }
            i3 += i6;
          }
          else
          {
            switch (paramInt7)
            {
            case 0:
              combine(localJBIG2Bitmap, i3, i1 - i6, paramInt6);
              break;
            case 1:
              combine(localJBIG2Bitmap, i3, i1, paramInt6);
              break;
            case 2:
              combine(localJBIG2Bitmap, i3, i1 - i6, paramInt6);
              break;
            case 3:
              combine(localJBIG2Bitmap, i3, i1, paramInt6);
            }
            i3 += i5;
          }
        }
        k++;
        DecodeIntResult localDecodeIntResult;
        if (paramBoolean1)
          localDecodeIntResult = this.huffmanDecoder.decodeInt(paramArrayOfInt3);
        else
          localDecodeIntResult = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadsStats);
        if (!localDecodeIntResult.booleanResult())
          break;
        i2 = localDecodeIntResult.intResult();
        i3 += paramInt8 + i2;
      }
    }
  }

  public void clear(int paramInt)
  {
    this.data = new byte[this.data.length];
    int i = paramInt == 0 ? 0 : -128;
    for (int j = 0; j < this.data.length; j++)
      this.data[j] = i;
  }

  public void combine(JBIG2Bitmap paramJBIG2Bitmap, int paramInt1, int paramInt2, long paramLong)
  {
    int i = paramJBIG2Bitmap.width;
    int j = paramJBIG2Bitmap.height;
    int k = paramJBIG2Bitmap.line;
    int m = 0;
    int n = 0;
    int i1 = (int)paramLong;
    byte[] arrayOfByte = paramJBIG2Bitmap.data;
    int i2;
    int i3;
    int i4;
    if (paramInt1 % 8 == 0)
    {
      i2 = (paramInt1 + i) / 8 * 8;
      int i5;
      for (i3 = paramInt2; i3 < paramInt2 + j; i3++)
      {
        i4 = m * k;
        for (i5 = paramInt1 / 8; i5 < i2 / 8; i5++)
        {
          int i6 = i3 * this.line + i5;
          if ((i6 < this.data.length) && (i6 >= 0))
            switch (i1)
            {
            case 0:
              int tmp158_156 = i6;
              byte[] tmp158_153 = this.data;
              tmp158_153[tmp158_156] = ((byte)(tmp158_153[tmp158_156] | arrayOfByte[(i4 + n)]));
              break;
            case 1:
              int tmp180_178 = i6;
              byte[] tmp180_175 = this.data;
              tmp180_175[tmp180_178] = ((byte)(tmp180_175[tmp180_178] & arrayOfByte[(i4 + n)]));
              break;
            case 2:
              int tmp202_200 = i6;
              byte[] tmp202_197 = this.data;
              tmp202_197[tmp202_200] = ((byte)(tmp202_197[tmp202_200] ^ arrayOfByte[(i4 + n)]));
              break;
            case 3:
              int tmp224_222 = i6;
              byte[] tmp224_219 = this.data;
              tmp224_219[tmp224_222] = ((byte)(tmp224_219[tmp224_222] ^ arrayOfByte[(i4 + n)]));
              int tmp243_241 = tmp224_222;
              byte[] tmp243_238 = this.data;
              tmp243_238[tmp243_241] = ((byte)(tmp243_238[tmp243_241] ^ 0xFF));
              break;
            case 4:
              this.data[tmp224_222] = arrayOfByte[(i4 + n)];
            }
          n++;
        }
        n = 0;
        m++;
      }
      m = 0;
      n = i2 - paramInt1;
      for (i3 = paramInt2; i3 < paramInt2 + j; i3++)
      {
        for (i4 = i2; i4 < paramInt1 + i; i4++)
        {
          i5 = getPixel(n, m, k, arrayOfByte);
          switch (i1)
          {
          case 0:
            setPixel(i4, i3, getPixel(i4, i3, this.line, this.data) | i5);
            break;
          case 1:
            setPixel(i4, i3, getPixel(i4, i3, this.line, this.data) & i5);
            break;
          case 2:
            setPixel(i4, i3, getPixel(i4, i3, this.line, this.data) ^ i5);
            break;
          case 3:
            if (((getPixel(i4, i3, this.line, this.data) == 1) && (i5 == 1)) || ((getPixel(i4, i3, this.line, this.data) == 0) && (i5 == 0)))
              setPixel(i4, i3, 1);
            else
              setPixel(i4, i3, 0);
            break;
          case 4:
            setPixel(i4, i3, i5);
          }
          n++;
        }
        n = i2 - paramInt1;
        m++;
      }
    }
    else
    {
      for (i2 = paramInt2; i2 < paramInt2 + j; i2++)
      {
        for (i3 = paramInt1; i3 < paramInt1 + i; i3++)
        {
          i4 = getPixel(n, m, k, arrayOfByte);
          switch (i1)
          {
          case 0:
            setPixel(i3, i2, getPixel(i3, i2, this.line, this.data) | i4);
            break;
          case 1:
            setPixel(i3, i2, getPixel(i3, i2, this.line, this.data) & i4);
            break;
          case 2:
            setPixel(i3, i2, getPixel(i3, i2, this.line, this.data) ^ i4);
            break;
          case 3:
            if (((getPixel(i3, i2, this.line, this.data) == 1) && (i4 == 1)) || ((getPixel(i3, i2, this.line, this.data) == 0) && (i4 == 0)))
              setPixel(i3, i2, 1);
            else
              setPixel(i3, i2, 0);
            break;
          case 4:
            setPixel(i3, i2, i4);
          }
          n++;
        }
        n = 0;
        m++;
      }
    }
  }

  private void duplicateRow(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < this.width; i++)
      setPixel(i, paramInt1, getPixel(i, paramInt2, this.line, this.data));
  }

  public int getWidth()
  {
    return this.width;
  }

  public int getHeight()
  {
    return this.height;
  }

  public int getLine()
  {
    return this.line;
  }

  public byte[] getData()
  {
    return this.data;
  }

  public byte[] getWriteSafeData(boolean paramBoolean)
  {
    byte[] arrayOfByte = new byte[this.data.length];
    System.arraycopy(this.data, 0, arrayOfByte, 0, this.data.length);
    if (paramBoolean)
      for (int i = 0; i < arrayOfByte.length; i++)
      {
        int tmp37_36 = i;
        byte[] tmp37_35 = arrayOfByte;
        tmp37_35[tmp37_36] = ((byte)(tmp37_35[tmp37_36] ^ 0xFF));
      }
    return arrayOfByte;
  }

  public JBIG2Bitmap getSlice(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    JBIG2Bitmap localJBIG2Bitmap = new JBIG2Bitmap(paramInt3, paramInt4, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    int i = 0;
    int j = 0;
    for (int k = paramInt2; k < paramInt4; k++)
    {
      for (int m = paramInt1; m < paramInt1 + paramInt3; m++)
      {
        localJBIG2Bitmap.setPixel(j, i, getPixel(m, k, this.line, this.data));
        j++;
      }
      j = 0;
      i++;
    }
    return localJBIG2Bitmap;
  }

  public void setPixel(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt2 * this.line + paramInt1 / 8;
    if ((i >= this.data.length) || (i < 0))
      return;
    int j = this.data[i];
    if (paramInt3 == 0)
      j &= (1 << 7 - paramInt1 % 8 ^ 0xFFFFFFFF);
    else
      j |= 1 << 7 - paramInt1 % 8;
    this.data[i] = ((byte)j);
  }

  private static int getPixel(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = paramInt2 * paramInt3 + (paramInt1 >> 3);
    if ((i < paramArrayOfByte.length) && (i >= 0))
      return (paramArrayOfByte[i] & 1 << 7 - (paramInt1 & 0x7)) != 0 ? 1 : 0;
    return 0;
  }

  public int getPixel(int paramInt1, int paramInt2)
  {
    return (this.data[(paramInt2 * this.line + (paramInt1 >> 3))] & 1 << 7 - (paramInt1 & 0x7)) != 0 ? 1 : 0;
  }

  public void expand(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt * this.line];
    System.arraycopy(this.data, 0, arrayOfByte, 0, this.height * this.line);
    this.height = paramInt;
    this.data = arrayOfByte;
  }

  public void setBitmapNumber(int paramInt)
  {
    this.bitmapNumber = paramInt;
  }

  public int getBitmapNumber()
  {
    return this.bitmapNumber;
  }

  public BufferedImage getBufferedImage()
  {
    byte[] arrayOfByte = getWriteSafeData(true);
    if (arrayOfByte == null)
      return null;
    BufferedImage localBufferedImage = new BufferedImage(this.width, this.height, 12);
    DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
    WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferByte, this.width, this.height, 1, null);
    localBufferedImage.setData(localWritableRaster);
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.image.JBIG2Bitmap
 * JD-Core Version:    0.6.2
 */
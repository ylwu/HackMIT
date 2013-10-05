package org.jpedal.io.filter.ccitt;

import java.util.BitSet;
import org.jpedal.objects.raw.PdfObject;

public class CCITT1D
  implements CCITTDecoder
{
  boolean BlackIs1 = false;
  boolean isByteAligned = false;
  int columns = 1728;
  byte[] data;
  int bitReached;
  private static final int EOL = -1;
  private static final boolean debug = false;
  boolean isWhite = true;
  private boolean isTerminating = false;
  private boolean isEndOfLine = false;
  private boolean EOS = false;
  private int cRTC = 0;
  int width = 0;
  int height = 0;
  private int line = 0;
  BitSet out;
  private BitSet inputBits;
  int outPtr = 0;
  private int bytesNeeded = 0;
  int scanlineStride;
  private int inputBitCount = 0;
  private static final int[][][] b = { { { 3, 2, 0 }, { 2, 3, 0 } }, { { 2, 1, 0 }, { 3, 4, 0 } }, { { 3, 5, 0 }, { 2, 6, 0 } }, { { 3, 7, 0 } }, { { 5, 8, 0 }, { 4, 9, 0 } }, { { 4, 10, 0 }, { 5, 11, 0 }, { 7, 12, 0 } }, { { 4, 13, 0 }, { 7, 14, 0 } }, { { 24, 15, 0 } }, { { 55, 0, 0 }, { 23, 16, 0 }, { 24, 17, 0 }, { 8, 18, 0 }, { 15, 64, 1 } }, { { 103, 19, 0 }, { 104, 20, 0 }, { 108, 21, 0 }, { 55, 22, 0 }, { 40, 23, 0 }, { 23, 24, 0 }, { 24, 25, 0 }, { 8, 1792, 1 }, { 12, 1856, 1 }, { 13, 1920, 1 } }, { { 202, 26, 0 }, { 203, 27, 0 }, { 204, 28, 0 }, { 205, 29, 0 }, { 104, 30, 0 }, { 105, 31, 0 }, { 106, 32, 0 }, { 107, 33, 0 }, { 210, 34, 0 }, { 211, 35, 0 }, { 212, 36, 0 }, { 213, 37, 0 }, { 214, 38, 0 }, { 215, 39, 0 }, { 108, 40, 0 }, { 109, 41, 0 }, { 218, 42, 0 }, { 219, 43, 0 }, { 84, 44, 0 }, { 85, 45, 0 }, { 86, 46, 0 }, { 87, 47, 0 }, { 100, 48, 0 }, { 101, 49, 0 }, { 82, 50, 0 }, { 83, 51, 0 }, { 36, 52, 0 }, { 55, 53, 0 }, { 56, 54, 0 }, { 39, 55, 0 }, { 40, 56, 0 }, { 88, 57, 0 }, { 89, 58, 0 }, { 43, 59, 0 }, { 44, 60, 0 }, { 90, 61, 0 }, { 102, 62, 0 }, { 103, 63, 0 }, { 200, 128, 1 }, { 201, 192, 1 }, { 91, 256, 1 }, { 51, 320, 1 }, { 52, 384, 1 }, { 53, 448, 1 }, { 1, -1, 1 }, { 18, 1984, 1 }, { 19, 2048, 1 }, { 20, 2112, 1 }, { 21, 2176, 1 }, { 22, 2240, 1 }, { 23, 2304, 1 }, { 28, 2368, 1 }, { 29, 2432, 1 }, { 30, 2496, 1 }, { 31, 2560, 1 } }, { { 108, 512, 1 }, { 109, 576, 1 }, { 74, 640, 1 }, { 75, 704, 1 }, { 76, 768, 1 }, { 77, 832, 1 }, { 114, 896, 1 }, { 115, 960, 1 }, { 116, 1024, 1 }, { 117, 1088, 1 }, { 118, 1152, 1 }, { 119, 1216, 1 }, { 82, 1280, 1 }, { 83, 1344, 1 }, { 84, 1408, 1 }, { 85, 1472, 1 }, { 90, 1536, 1 }, { 91, 1600, 1 }, { 100, 1664, 1 }, { 101, 1728, 1 } } };
  private static final int[][][] w = { { { 7, 2, 0 }, { 8, 3, 0 }, { 11, 4, 0 }, { 12, 5, 0 }, { 14, 6, 0 }, { 15, 7, 0 } }, { { 19, 8, 0 }, { 20, 9, 0 }, { 7, 10, 0 }, { 8, 11, 0 }, { 27, 64, 1 }, { 18, 128, 1 } }, { { 7, 1, 0 }, { 8, 12, 0 }, { 3, 13, 0 }, { 52, 14, 0 }, { 53, 15, 0 }, { 42, 16, 0 }, { 43, 17, 0 }, { 23, 192, 1 }, { 24, 1664, 1 } }, { { 39, 18, 0 }, { 12, 19, 0 }, { 8, 20, 0 }, { 23, 21, 0 }, { 3, 22, 0 }, { 4, 23, 0 }, { 40, 24, 0 }, { 43, 25, 0 }, { 19, 26, 0 }, { 36, 27, 0 }, { 24, 28, 0 }, { 55, 256, 1 } }, { { 53, 0, 0 }, { 2, 29, 0 }, { 3, 30, 0 }, { 26, 31, 0 }, { 27, 32, 0 }, { 18, 33, 0 }, { 19, 34, 0 }, { 20, 35, 0 }, { 21, 36, 0 }, { 22, 37, 0 }, { 23, 38, 0 }, { 40, 39, 0 }, { 41, 40, 0 }, { 42, 41, 0 }, { 43, 42, 0 }, { 44, 43, 0 }, { 45, 44, 0 }, { 4, 45, 0 }, { 5, 46, 0 }, { 10, 47, 0 }, { 11, 48, 0 }, { 82, 49, 0 }, { 83, 50, 0 }, { 84, 51, 0 }, { 85, 52, 0 }, { 36, 53, 0 }, { 37, 54, 0 }, { 88, 55, 0 }, { 89, 56, 0 }, { 90, 57, 0 }, { 91, 58, 0 }, { 74, 59, 0 }, { 75, 60, 0 }, { 50, 61, 0 }, { 51, 62, 0 }, { 52, 63, 0 }, { 54, 320, 1 }, { 55, 384, 1 }, { 100, 448, 1 }, { 101, 512, 1 }, { 104, 576, 1 }, { 103, 640, 1 } }, { { 204, 704, 1 }, { 205, 768, 1 }, { 210, 832, 1 }, { 211, 896, 1 }, { 212, 960, 1 }, { 213, 1024, 1 }, { 214, 1088, 1 }, { 215, 1152, 1 }, { 216, 1216, 1 }, { 217, 1280, 1 }, { 218, 1344, 1 }, { 219, 1408, 1 }, { 152, 1472, 1 }, { 153, 1536, 1 }, { 154, 1600, 1 }, { 155, 1728, 1 } }, new int[0][], { { 8, 1792, 1 }, { 12, 1856, 1 }, { 13, 1920, 1 } }, { { 1, -1, 1 }, { 18, 1984, 1 }, { 19, 2048, 1 }, { 20, 2112, 1 }, { 21, 2176, 1 }, { 22, 2240, 1 }, { 23, 2304, 1 }, { 28, 2368, 1 }, { 29, 2432, 1 }, { 30, 2496, 1 }, { 31, 2560, 1 } } };

  public CCITT1D(byte[] paramArrayOfByte, int paramInt1, int paramInt2, PdfObject paramPdfObject)
  {
    this.data = paramArrayOfByte;
    this.bitReached = 0;
    this.columns = paramInt1;
    if (paramPdfObject != null)
    {
      this.BlackIs1 = paramPdfObject.getBoolean(1297445940);
      int i = paramPdfObject.getInt(1162902911);
      if (i != -1)
        this.columns = i;
      int j = paramPdfObject.getInt(574572355);
      if (j > 0)
        paramInt2 = j;
      this.isByteAligned = paramPdfObject.getBoolean(-823077984);
    }
    this.width = this.columns;
    this.height = paramInt2;
    this.scanlineStride = (this.columns + 7 >> 3);
    this.bytesNeeded = (paramInt2 * this.scanlineStride);
    this.out = new BitSet(this.bytesNeeded << 3);
    this.inputBitCount = (this.data.length << 3);
    this.inputBits = fromByteArray(this.data, this.inputBitCount);
  }

  CCITT1D()
  {
  }

  public byte[] decode()
  {
    decode1DRun();
    byte[] arrayOfByte = createOutputFromBitset();
    if (this.BlackIs1)
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfByte[i] = ((byte)(255 - arrayOfByte[i]));
    return arrayOfByte;
  }

  byte[] createOutputFromBitset()
  {
    byte[] arrayOfByte = new byte[this.bytesNeeded];
    int i = 0;
    int j = 7;
    int m = 0;
    for (int n = 0; n < this.outPtr; n++)
    {
      if (this.out.get(n))
      {
        int k = 1 << j;
        m = (byte)(m | k);
        j--;
      }
      else
      {
        j--;
      }
      if (((n + 1) % this.width == 0) && (n != 0))
        j = -1;
      if ((j < 0) && (i < arrayOfByte.length))
      {
        arrayOfByte[i] = m;
        i++;
        j = 7;
        m = 0;
      }
    }
    return arrayOfByte;
  }

  private void decode1DRun()
  {
    while (!this.EOS)
    {
      if (this.isTerminating)
      {
        this.isTerminating = false;
        this.isWhite = (!this.isWhite);
        if (this.isEndOfLine)
        {
          this.isEndOfLine = false;
          this.isWhite = true;
        }
      }
      boolean bool = this.isWhite;
      int i = getCodeWord();
      if (i > 0)
      {
        if (bool)
          this.out.set(this.outPtr, this.outPtr + i, true);
        this.outPtr += i;
      }
    }
  }

  private int getCodeWord()
  {
    int i = 0;
    int j = -1;
    int k = 0;
    int m = 2;
    int n = 14;
    int i1 = 0;
    int i2 = 0;
    if (this.isWhite)
    {
      m = 4;
      n = 13;
    }
    for (int i3 = m; i3 < n; i3++)
    {
      i1 = get1DBits(i3, true) & 0xFF;
      j = checkTables(i1, i3, this.isWhite);
      if (j != -1)
      {
        i2 = i3;
        i3 = n;
      }
      else if (i3 == 8)
      {
        k++;
        this.bitReached += 1;
      }
    }
    if (j != -1)
    {
      this.bitReached = (this.bitReached + i2 - k);
      i = processCodeWord(j, i1, i2);
    }
    else if (this.bitReached > this.inputBitCount)
    {
      this.EOS = true;
    }
    return i;
  }

  private int processCodeWord(int paramInt1, int paramInt2, int paramInt3)
  {
    int i;
    int j;
    if (this.isWhite)
    {
      i = w[(paramInt3 - 4)][paramInt1][1];
      j = w[(paramInt3 - 4)][paramInt1][2] == 0 ? 1 : 0;
    }
    else
    {
      i = b[(paramInt3 - 2)][paramInt1][1];
      j = b[(paramInt3 - 2)][paramInt1][2] == 0 ? 1 : 0;
    }
    if (j != 0)
      this.isTerminating = true;
    if (i == -1)
    {
      if (this.line != 0)
        i = this.width - this.line;
      this.line = 0;
      this.isWhite = true;
      this.isTerminating = false;
    }
    if (i != -1)
    {
      this.line += i;
      if (this.line == this.width)
      {
        if (j != 0)
        {
          this.line = 0;
          this.isEndOfLine = true;
        }
      }
      else if (this.line > this.width)
      {
        this.line = 0;
        this.isEndOfLine = true;
      }
    }
    if ((paramInt3 == 12) && (paramInt2 == 1))
    {
      this.cRTC += 1;
      if (this.cRTC == 6)
        this.EOS = true;
    }
    else
    {
      this.cRTC = 0;
    }
    if ((this.cRTC != 6) && (this.isEndOfLine) && (this.isByteAligned))
    {
      int k = this.bitReached % 8;
      int m = 8 - k;
      if (k > 0)
        this.bitReached += m;
    }
    return i;
  }

  int get1DBits(int paramInt)
  {
    return get1DBits(paramInt, false);
  }

  private int get1DBits(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    if ((paramBoolean) && (paramInt > 8))
      j++;
    for (int m = 0; m < paramInt; m++)
      if (this.inputBits.get(m + this.bitReached))
      {
        int k = 1 << paramInt - m - 1 - j;
        i |= k;
      }
    return i;
  }

  private static int checkTables(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = -1;
    int[][] arrayOfInt;
    if (paramBoolean)
      arrayOfInt = w[(paramInt2 - 4)];
    else
      arrayOfInt = b[(paramInt2 - 2)];
    int j = arrayOfInt.length;
    for (int k = 0; k < j; k++)
      if (paramInt1 == arrayOfInt[k][0])
      {
        i = k;
        k = j;
      }
    return i;
  }

  private static BitSet fromByteArray(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    BitSet localBitSet = new BitSet(paramInt);
    for (int i1 : paramArrayOfByte)
    {
      int k = i1;
      for (int i2 = 7; i2 >= 0; i2--)
      {
        int j = k & 1 << i2;
        if (j >= 1)
          localBitSet.set(i, true);
        i++;
      }
    }
    return localBitSet;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.ccitt.CCITT1D
 * JD-Core Version:    0.6.2
 */
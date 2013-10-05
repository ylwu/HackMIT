package org.jpedal.io.filter.ccitt;

import java.util.BitSet;
import org.jpedal.objects.raw.PdfObject;

public class CCITT2D extends CCITT1D
  implements CCITTDecoder
{
  int changingElemSize = 0;
  boolean is2D = true;
  int bitOffset;
  int currIndex;
  static final int[] initBlack = { 3226, 6412, 200, 168, 38, 38, 134, 134, 100, 100, 100, 100, 68, 68, 68, 68 };
  private static final byte[] code2D = { 80, 88, 23, 71, 30, 30, 62, 62, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41 };
  static final int[] black = { 62, 62, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 588, 588, 588, 588, 588, 588, 588, 588, 1680, 1680, 20499, 22547, 24595, 26643, 1776, 1776, 1808, 1808, -24557, -22509, -20461, -18413, 1904, 1904, 1936, 1936, -16365, -14317, 782, 782, 782, 782, 814, 814, 814, 814, -12269, -10221, 10257, 10257, 12305, 12305, 14353, 14353, 16403, 18451, 1712, 1712, 1744, 1744, 28691, 30739, -32749, -30701, -28653, -26605, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 750, 750, 750, 750, 1616, 1616, 1648, 1648, 1424, 1424, 1456, 1456, 1488, 1488, 1520, 1520, 1840, 1840, 1872, 1872, 1968, 1968, 8209, 8209, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 1552, 1552, 1584, 1584, 2000, 2000, 2032, 2032, 976, 976, 1008, 1008, 1040, 1040, 1072, 1072, 1296, 1296, 1328, 1328, 718, 718, 718, 718, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 4113, 4113, 6161, 6161, 848, 848, 880, 880, 912, 912, 944, 944, 622, 622, 622, 622, 654, 654, 654, 654, 1104, 1104, 1136, 1136, 1168, 1168, 1200, 1200, 1232, 1232, 1264, 1264, 686, 686, 686, 686, 1360, 1360, 1392, 1392, 12, 12, 12, 12, 12, 12, 12, 12, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390 };
  static final int[] additionalMakeup = { 28679, 28679, 31752, 32777, 33801, 34825, 35849, 36873, 29703, 29703, 30727, 30727, 37897, 38921, 39945, 40969 };
  static final int[] white = { 6430, 6400, 6400, 6400, 3225, 3225, 3225, 3225, 944, 944, 944, 944, 976, 976, 976, 976, 1456, 1456, 1456, 1456, 1488, 1488, 1488, 1488, 718, 718, 718, 718, 718, 718, 718, 718, 750, 750, 750, 750, 750, 750, 750, 750, 1520, 1520, 1520, 1520, 1552, 1552, 1552, 1552, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 654, 654, 654, 654, 654, 654, 654, 654, 1072, 1072, 1072, 1072, 1104, 1104, 1104, 1104, 1136, 1136, 1136, 1136, 1168, 1168, 1168, 1168, 1200, 1200, 1200, 1200, 1232, 1232, 1232, 1232, 622, 622, 622, 622, 622, 622, 622, 622, 1008, 1008, 1008, 1008, 1040, 1040, 1040, 1040, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 1712, 1712, 1712, 1712, 1744, 1744, 1744, 1744, 846, 846, 846, 846, 846, 846, 846, 846, 1264, 1264, 1264, 1264, 1296, 1296, 1296, 1296, 1328, 1328, 1328, 1328, 1360, 1360, 1360, 1360, 1392, 1392, 1392, 1392, 1424, 1424, 1424, 1424, 686, 686, 686, 686, 686, 686, 686, 686, 910, 910, 910, 910, 910, 910, 910, 910, 1968, 1968, 1968, 1968, 2000, 2000, 2000, 2000, 2032, 2032, 2032, 2032, 16, 16, 16, 16, 10257, 10257, 10257, 10257, 12305, 12305, 12305, 12305, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 878, 878, 878, 878, 878, 878, 878, 878, 1904, 1904, 1904, 1904, 1936, 1936, 1936, 1936, -18413, -18413, -16365, -16365, -14317, -14317, -10221, -10221, 590, 590, 590, 590, 590, 590, 590, 590, 782, 782, 782, 782, 782, 782, 782, 782, 1584, 1584, 1584, 1584, 1616, 1616, 1616, 1616, 1648, 1648, 1648, 1648, 1680, 1680, 1680, 1680, 814, 814, 814, 814, 814, 814, 814, 814, 1776, 1776, 1776, 1776, 1808, 1808, 1808, 1808, 1840, 1840, 1840, 1840, 1872, 1872, 1872, 1872, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, 14353, 14353, 14353, 14353, 16401, 16401, 16401, 16401, 22547, 22547, 24595, 24595, 20497, 20497, 20497, 20497, 18449, 18449, 18449, 18449, 26643, 26643, 28691, 28691, 30739, 30739, -32749, -32749, -30701, -30701, -28653, -28653, -26605, -26605, -24557, -24557, -22509, -22509, -20461, -20461, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232 };
  static final int[] twoBitBlack = { 292, 260, 226, 226 };

  public CCITT2D(byte[] paramArrayOfByte, int paramInt1, int paramInt2, PdfObject paramPdfObject)
  {
    super(paramArrayOfByte, paramInt1, paramInt2, paramPdfObject);
  }

  CCITT2D()
  {
  }

  public byte[] decode()
  {
    decode2DRun();
    byte[] arrayOfByte = createOutputFromBitset();
    if (!this.BlackIs1)
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfByte[i] = ((byte)(255 - arrayOfByte[i]));
    return arrayOfByte;
  }

  private void decode2DRun()
  {
    Object localObject1 = new int[this.width + 1];
    Object localObject2 = new int[this.width + 1];
    this.changingElemSize = 2;
    localObject2[0] = this.width;
    localObject2[1] = this.width;
    int i = 0;
    int[] arrayOfInt = new int[2];
    for (int j = 0; j < this.height; j++)
    {
      if ((this.isByteAligned) && (this.bitReached > 0))
      {
        int k = this.bitReached % 8;
        int m = 8 - k;
        if (k > 0)
          this.bitReached += m;
      }
      Object localObject3 = localObject1;
      localObject1 = localObject2;
      localObject2 = localObject3;
      set2D((int[])localObject1, (int[])localObject2, this.changingElemSize, arrayOfInt);
      if (localObject2.length != this.currIndex)
        localObject2[(this.currIndex++)] = this.bitOffset;
      this.changingElemSize = this.currIndex;
      i += this.scanlineStride;
    }
  }

  void set2D(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt, int[] paramArrayOfInt3)
  {
    this.isWhite = true;
    this.currIndex = 0;
    this.bitOffset = 0;
    int k = 0;
    int m = -1;
    while (this.bitOffset < this.width)
    {
      getNextChangingElement(m, this.isWhite, paramArrayOfInt3, paramArrayOfInt1, paramInt);
      int n = get1DBits(7);
      this.bitReached += 7;
      int i = code2D[n] & 0xFF;
      int j = (i & 0x78) >>> 3;
      if (!this.is2D)
        k = i & 0x7;
      else if (j != 11)
        updatePointer(7 - (i & 0x7));
      int i1;
      int i3;
      int i4;
      switch (j)
      {
      case 0:
        i1 = paramArrayOfInt3[1] - this.bitOffset;
        if (!this.isWhite)
          this.out.set(this.outPtr, this.outPtr + i1, true);
        this.outPtr += i1;
        this.bitOffset = paramArrayOfInt3[1];
        m = paramArrayOfInt3[1];
        if (!this.is2D)
          this.bitReached -= 7 - k;
        break;
      case 1:
        if (!this.is2D)
          this.bitReached -= 7 - k;
        if (this.isWhite)
        {
          i1 = getWhiteRunCodeWord();
          this.outPtr += i1;
          this.bitOffset += i1;
          paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
          i1 = getBlackRunCodeWord();
          this.out.set(this.outPtr, this.outPtr + i1, true);
          this.outPtr += i1;
        }
        else
        {
          i1 = getBlackRunCodeWord();
          this.out.set(this.outPtr, this.outPtr + i1, true);
          this.outPtr += i1;
          this.bitOffset += i1;
          paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
          i1 = getWhiteRunCodeWord();
          this.outPtr += i1;
        }
        this.bitOffset += i1;
        paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
        m = this.bitOffset;
        break;
      case 11:
        int i2 = get1DBits(3);
        this.bitReached += 3;
        if (i2 != 7)
          throw new RuntimeException("Unexpected value " + i2);
        i3 = 0;
        i4 = 0;
      default:
        while (i4 == 0)
        {
          while (true)
          {
            n = get1DBits(1);
            this.bitReached += 1;
            if (n == 1)
              break;
            i3++;
          }
          if (i3 > 5)
          {
            i3 -= 6;
            if ((!this.isWhite) && (i3 > 0))
              paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
            this.bitOffset += i3;
            if (i3 > 0)
              this.isWhite = true;
            n = get1DBits(1);
            this.bitReached += 1;
            if (n == 0)
            {
              if (!this.isWhite)
                paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
              this.isWhite = true;
            }
            else
            {
              if (this.isWhite)
                paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
              this.isWhite = false;
            }
            i4 = 1;
          }
          if (i3 == 5)
          {
            if (!this.isWhite)
              paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
            this.bitOffset += i3;
            this.isWhite = true;
          }
          else
          {
            this.bitOffset += i3;
            paramArrayOfInt2[(this.currIndex++)] = this.bitOffset;
            this.out.set(this.outPtr, this.outPtr + 1, true);
            this.outPtr += 1;
            this.bitOffset += 1;
            this.isWhite = false;
            continue;
            if (j > 8)
              throw new RuntimeException("CCITT unexpected value");
            paramArrayOfInt2[(this.currIndex++)] = (paramArrayOfInt3[0] + (j - 5));
            i1 = paramArrayOfInt3[0] + (j - 5) - this.bitOffset;
            if (!this.isWhite)
              this.out.set(this.outPtr, this.outPtr + i1, true);
            this.outPtr += i1;
            m = paramArrayOfInt3[0] + (j - 5);
            this.bitOffset = m;
            this.isWhite = (!this.isWhite);
            if (!this.is2D)
              this.bitReached -= 7 - k;
          }
        }
      }
    }
  }

  int getBlackRunCodeWord()
  {
    int m = 0;
    int n = 1;
    while (n != 0)
    {
      int i1 = get1DBits(4);
      this.bitReached += 4;
      int i = initBlack[i1];
      int j = i >>> 1 & 0xF;
      int k = i >>> 5 & 0x7FF;
      switch (k)
      {
      case 100:
        i1 = get1DBits(9);
        this.bitReached += 9;
        i = black[i1];
        j = i >>> 1 & 0xF;
        k = i >>> 5 & 0x7FF;
        if (j == 12)
        {
          updatePointer(5);
          i1 = get1DBits(4);
          this.bitReached += 4;
          i = additionalMakeup[i1];
          j = i >>> 1 & 0x7;
          k = i >>> 4 & 0xFFF;
          m += k;
          updatePointer(4 - j);
        }
        else
        {
          if (j == 15)
            throw new RuntimeException("CCITT unexpected EOL");
          m += k;
          updatePointer(9 - j);
          if ((i & 0x1) == 0)
            n = 0;
        }
        break;
      case 200:
        i1 = get1DBits(2);
        this.bitReached += 2;
        i = twoBitBlack[i1];
        k = i >>> 5 & 0x7FF;
        m += k;
        j = i >>> 1 & 0xF;
        updatePointer(2 - j);
        n = 0;
        break;
      default:
        m += k;
        updatePointer(4 - j);
        n = 0;
      }
    }
    return m;
  }

  int getWhiteRunCodeWord()
  {
    int i1 = 0;
    int i2 = 1;
    while (i2 != 0)
    {
      int i = get1DBits(10);
      this.bitReached += 10;
      int j = white[i];
      int k = j >>> 1 & 0xF;
      int n;
      if (k == 12)
      {
        int m = get1DBits(2);
        this.bitReached += 2;
        i = i << 2 & 0xC | m;
        j = additionalMakeup[i];
        k = j >>> 1 & 0x7;
        n = j >>> 4 & 0xFFF;
        i1 += n;
        updatePointer(4 - k);
      }
      else
      {
        if ((k == 0) || (k == 15))
          throw new RuntimeException("CCITT Error in getWhiteRunCodeWord");
        n = j >>> 5 & 0x7FF;
        i1 += n;
        updatePointer(10 - k);
        if ((j & 0x1) == 0)
          i2 = 0;
      }
    }
    return i1;
  }

  private static void getNextChangingElement(int paramInt1, boolean paramBoolean, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2)
  {
    int i = 0;
    if (paramBoolean)
      i &= -2;
    else
      i |= 1;
    for (int j = i; j < paramInt2; j += 2)
    {
      int k = paramArrayOfInt2[j];
      if (k > paramInt1)
      {
        paramArrayOfInt1[0] = k;
        break;
      }
    }
    if (j + 1 < paramInt2)
      paramArrayOfInt1[1] = paramArrayOfInt2[(j + 1)];
  }

  private void updatePointer(int paramInt)
  {
    this.bitReached -= paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.ccitt.CCITT2D
 * JD-Core Version:    0.6.2
 */
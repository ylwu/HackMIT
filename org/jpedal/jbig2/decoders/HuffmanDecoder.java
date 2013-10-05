package org.jpedal.jbig2.decoders;

import org.jpedal.jbig2.io.StreamReader;

public class HuffmanDecoder
{
  private static final int jbig2HuffmanLOW = -3;
  public static final int jbig2HuffmanOOB = -2;
  public static final int jbig2HuffmanEOT = -1;
  private final StreamReader reader;
  public static final int[][] huffmanTableA = { { 0, 1, 4, 0 }, { 16, 2, 8, 2 }, { 272, 3, 16, 6 }, { 65808, 3, 32, 7 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableB = { { 0, 1, 0, 0 }, { 1, 2, 0, 2 }, { 2, 3, 0, 6 }, { 3, 4, 3, 14 }, { 11, 5, 6, 30 }, { 75, 6, 32, 62 }, { 0, 6, -2, 63 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableC = { { 0, 1, 0, 0 }, { 1, 2, 0, 2 }, { 2, 3, 0, 6 }, { 3, 4, 3, 14 }, { 11, 5, 6, 30 }, { 0, 6, -2, 62 }, { 75, 7, 32, 254 }, { -256, 8, 8, 254 }, { -257, 8, -3, 255 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableD = { { 1, 1, 0, 0 }, { 2, 2, 0, 2 }, { 3, 3, 0, 6 }, { 4, 4, 3, 14 }, { 12, 5, 6, 30 }, { 76, 5, 32, 31 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableE = { { 1, 1, 0, 0 }, { 2, 2, 0, 2 }, { 3, 3, 0, 6 }, { 4, 4, 3, 14 }, { 12, 5, 6, 30 }, { 76, 6, 32, 62 }, { -255, 7, 8, 126 }, { -256, 7, -3, 127 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableF = { { 0, 2, 7, 0 }, { 128, 3, 7, 2 }, { 256, 3, 8, 3 }, { -1024, 4, 9, 8 }, { -512, 4, 8, 9 }, { -256, 4, 7, 10 }, { -32, 4, 5, 11 }, { 512, 4, 9, 12 }, { 1024, 4, 10, 13 }, { -2048, 5, 10, 28 }, { -128, 5, 6, 29 }, { -64, 5, 5, 30 }, { -2049, 6, -3, 62 }, { 2048, 6, 32, 63 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableG = { { -512, 3, 8, 0 }, { 256, 3, 8, 1 }, { 512, 3, 9, 2 }, { 1024, 3, 10, 3 }, { -1024, 4, 9, 8 }, { -256, 4, 7, 9 }, { -32, 4, 5, 10 }, { 0, 4, 5, 11 }, { 128, 4, 7, 12 }, { -128, 5, 6, 26 }, { -64, 5, 5, 27 }, { 32, 5, 5, 28 }, { 64, 5, 6, 29 }, { -1025, 5, -3, 30 }, { 2048, 5, 32, 31 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableH = { { 0, 2, 1, 0 }, { 0, 2, -2, 1 }, { 4, 3, 4, 4 }, { -1, 4, 0, 10 }, { 22, 4, 4, 11 }, { 38, 4, 5, 12 }, { 2, 5, 0, 26 }, { 70, 5, 6, 27 }, { 134, 5, 7, 28 }, { 3, 6, 0, 58 }, { 20, 6, 1, 59 }, { 262, 6, 7, 60 }, { 646, 6, 10, 61 }, { -2, 7, 0, 124 }, { 390, 7, 8, 125 }, { -15, 8, 3, 252 }, { -5, 8, 1, 253 }, { -7, 9, 1, 508 }, { -3, 9, 0, 509 }, { -16, 9, -3, 510 }, { 1670, 9, 32, 511 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableI = { { 0, 2, -2, 0 }, { -1, 3, 1, 2 }, { 1, 3, 1, 3 }, { 7, 3, 5, 4 }, { -3, 4, 1, 10 }, { 43, 4, 5, 11 }, { 75, 4, 6, 12 }, { 3, 5, 1, 26 }, { 139, 5, 7, 27 }, { 267, 5, 8, 28 }, { 5, 6, 1, 58 }, { 39, 6, 2, 59 }, { 523, 6, 8, 60 }, { 1291, 6, 11, 61 }, { -5, 7, 1, 124 }, { 779, 7, 9, 125 }, { -31, 8, 4, 252 }, { -11, 8, 2, 253 }, { -15, 9, 2, 508 }, { -7, 9, 1, 509 }, { -32, 9, -3, 510 }, { 3339, 9, 32, 511 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableJ = { { -2, 2, 2, 0 }, { 6, 2, 6, 1 }, { 0, 2, -2, 2 }, { -3, 5, 0, 24 }, { 2, 5, 0, 25 }, { 70, 5, 5, 26 }, { 3, 6, 0, 54 }, { 102, 6, 5, 55 }, { 134, 6, 6, 56 }, { 198, 6, 7, 57 }, { 326, 6, 8, 58 }, { 582, 6, 9, 59 }, { 1094, 6, 10, 60 }, { -21, 7, 4, 122 }, { -4, 7, 0, 123 }, { 4, 7, 0, 124 }, { 2118, 7, 11, 125 }, { -5, 8, 0, 252 }, { 5, 8, 0, 253 }, { -22, 8, -3, 254 }, { 4166, 8, 32, 255 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableK = { { 1, 1, 0, 0 }, { 2, 2, 1, 2 }, { 4, 4, 0, 12 }, { 5, 4, 1, 13 }, { 7, 5, 1, 28 }, { 9, 5, 2, 29 }, { 13, 6, 2, 60 }, { 17, 7, 2, 122 }, { 21, 7, 3, 123 }, { 29, 7, 4, 124 }, { 45, 7, 5, 125 }, { 77, 7, 6, 126 }, { 141, 7, 32, 127 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableL = { { 1, 1, 0, 0 }, { 2, 2, 0, 2 }, { 3, 3, 1, 6 }, { 5, 5, 0, 28 }, { 6, 5, 1, 29 }, { 8, 6, 1, 60 }, { 10, 7, 0, 122 }, { 11, 7, 1, 123 }, { 13, 7, 2, 124 }, { 17, 7, 3, 125 }, { 25, 7, 4, 126 }, { 41, 8, 5, 254 }, { 73, 8, 32, 255 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableM = { { 1, 1, 0, 0 }, { 2, 3, 0, 4 }, { 7, 3, 3, 5 }, { 3, 4, 0, 12 }, { 5, 4, 1, 13 }, { 4, 5, 0, 28 }, { 15, 6, 1, 58 }, { 17, 6, 2, 59 }, { 21, 6, 3, 60 }, { 29, 6, 4, 61 }, { 45, 6, 5, 62 }, { 77, 7, 6, 126 }, { 141, 7, 32, 127 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableN = { { 0, 1, 0, 0 }, { -2, 3, 0, 4 }, { -1, 3, 0, 5 }, { 1, 3, 0, 6 }, { 2, 3, 0, 7 }, { 0, 0, -1, 0 } };
  public static final int[][] huffmanTableO = { { 0, 1, 0, 0 }, { -1, 3, 0, 4 }, { 1, 3, 0, 5 }, { -2, 4, 0, 12 }, { 2, 4, 0, 13 }, { -4, 5, 1, 28 }, { 3, 5, 1, 29 }, { -8, 6, 2, 60 }, { 5, 6, 2, 61 }, { -24, 7, 4, 124 }, { 9, 7, 4, 125 }, { -25, 7, -3, 126 }, { 25, 7, 32, 127 }, { 0, 0, -1, 0 } };

  public HuffmanDecoder(StreamReader paramStreamReader)
  {
    this.reader = paramStreamReader;
  }

  public DecodeIntResult decodeInt(int[][] paramArrayOfInt)
  {
    int i = 0;
    int j = 0;
    for (int k = 0; paramArrayOfInt[k][2] != -1; k++)
      if (paramArrayOfInt[k][1] != 0)
      {
        int m;
        while (i < paramArrayOfInt[k][1])
        {
          m = this.reader.readBit();
          j = j << 1 | m;
          i++;
        }
        if (j == paramArrayOfInt[k][3])
        {
          if (paramArrayOfInt[k][2] == -2)
            return new DecodeIntResult(-1, false);
          int n;
          if (paramArrayOfInt[k][2] == -3)
          {
            n = this.reader.readBits(32);
            m = paramArrayOfInt[k][0] - n;
          }
          else if (paramArrayOfInt[k][2] > 0)
          {
            n = this.reader.readBits(paramArrayOfInt[k][2]);
            m = paramArrayOfInt[k][0] + n;
          }
          else
          {
            m = paramArrayOfInt[k][0];
          }
          return new DecodeIntResult(m, true);
        }
      }
    return new DecodeIntResult(-1, false);
  }

  public static int[][] buildTable(int[][] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramInt; i++)
    {
      for (int j = i; (j < paramInt) && (paramArrayOfInt[j][1] == 0); j++);
      if (j == paramInt)
        break;
      for (int k = j + 1; k < paramInt; k++)
        if ((paramArrayOfInt[k][1] > 0) && (paramArrayOfInt[k][1] < paramArrayOfInt[j][1]))
          j = k;
      if (j != i)
      {
        int[] arrayOfInt = paramArrayOfInt[j];
        for (k = j; k > i; k--)
          paramArrayOfInt[k] = paramArrayOfInt[(k - 1)];
        paramArrayOfInt[i] = arrayOfInt;
      }
    }
    paramArrayOfInt[i] = paramArrayOfInt[paramInt];
    i = 0;
    int m = 0;
    paramArrayOfInt[(i++)][3] = (m++);
    while (paramArrayOfInt[i][2] != -1)
    {
      m <<= paramArrayOfInt[i][1] - paramArrayOfInt[(i - 1)][1];
      paramArrayOfInt[i][3] = (m++);
      i++;
    }
    return paramArrayOfInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.decoders.HuffmanDecoder
 * JD-Core Version:    0.6.2
 */
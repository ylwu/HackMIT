package org.jpedal.images;

public class ImageOps
{
  private static int[] bitCheck = { 128, 64, 32, 16, 8, 4, 2, 1 };

  private static byte[] invertByteImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length / (paramInt1 * paramInt2);
    if (i != paramInt3)
      paramInt3 = i;
    int j = paramInt1 * paramInt3;
    int k = 0;
    int m = (paramInt2 - 1) * j;
    while (k < m)
    {
      int i1 = 0;
      while (i1 < j)
      {
        for (int i2 = 0; i2 < paramInt3; i2++)
        {
          int n = paramArrayOfByte[(k + i1 + i2)];
          paramArrayOfByte[(k + i1 + i2)] = paramArrayOfByte[(m + i1 + i2)];
          paramArrayOfByte[(m + i1 + i2)] = n;
        }
        i1 += paramInt3;
      }
      k += j;
      m -= j;
    }
    return paramArrayOfByte;
  }

  private static byte[] invertOneBitImage(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = (paramInt1 + 7) / 8;
    int j = 0;
    int k = (paramInt2 - 1) * i;
    while (j < k)
    {
      for (int n = 0; n < i; n++)
      {
        int m = paramArrayOfByte[(j + n)];
        paramArrayOfByte[(j + n)] = paramArrayOfByte[(k + n)];
        paramArrayOfByte[(k + n)] = m;
      }
      j += i;
      k -= i;
    }
    return paramArrayOfByte;
  }

  private static byte[] rotateOneBitImage(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
  {
    int i = (paramInt1 + 7) / 8;
    int j = (paramInt4 + 7) / 8;
    byte[] arrayOfByte = new byte[j * paramInt3];
    for (int k = 0; k < paramInt2; k++)
      for (int m = 0; m < paramInt1; m++)
      {
        int n = (paramInt2 - 1 - k) * i + (m >> 3);
        int i1 = paramArrayOfByte[n];
        if (i1 == 0)
        {
          m += 7;
        }
        else
        {
          int i2 = m & 0x7;
          if ((i1 & bitCheck[i2]) == bitCheck[i2])
          {
            int i3 = m * j + (k >> 3);
            int i4 = arrayOfByte[i3];
            int i5 = k & 0x7;
            i4 = (byte)(i4 | bitCheck[i5]);
            arrayOfByte[i3] = i4;
          }
        }
      }
    return arrayOfByte;
  }

  private static byte[] rotateByteImage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length / (paramInt1 * paramInt2);
    if (i != paramInt3)
      paramInt3 = i;
    int j = paramInt1 * paramInt3;
    int k = paramInt2 * paramInt3;
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int m = 0; m < paramInt2; m++)
    {
      int n = 0;
      while (n < j)
      {
        int i1 = 0;
        int i2 = n / paramInt3 * k + (k - m * paramInt3 - paramInt3);
        while (i1 < paramInt3)
        {
          arrayOfByte[(i2 + i1)] = paramArrayOfByte[(m * j + (n + i1))];
          i1++;
        }
        n += paramInt3;
      }
    }
    return arrayOfByte;
  }

  public static byte[] invertImage(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2)
  {
    if (paramInt3 == 8)
    {
      if (paramArrayOfByte2 != null)
        paramInt4 = 1;
      else
        paramArrayOfByte1 = checkSize(paramArrayOfByte1, paramInt1, paramInt2, paramInt4);
      return invertByteImage(paramInt1, paramInt2, paramInt4, paramArrayOfByte1);
    }
    if (paramInt3 == 1)
      return invertOneBitImage(paramInt1, paramInt2, paramArrayOfByte1);
    return null;
  }

  public static byte[] rotateImage(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2)
  {
    if (paramInt3 == 8)
    {
      if (paramArrayOfByte2 != null)
        paramInt4 = 1;
      return rotateByteImage(paramInt1, paramInt2, paramInt4, paramArrayOfByte1);
    }
    if ((paramInt3 != 4) && (paramInt3 == 1))
      return rotateOneBitImage(paramInt1, paramInt2, paramArrayOfByte1, paramInt1, paramInt2);
    return null;
  }

  public static byte[] checkSize(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 * paramInt2 * paramInt3;
    if (paramArrayOfByte.length < i)
    {
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
      paramArrayOfByte = arrayOfByte;
    }
    return paramArrayOfByte;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.images.ImageOps
 * JD-Core Version:    0.6.2
 */
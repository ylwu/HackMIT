package org.jpedal.fonts.tt.conversion;

public class CFFFixer
{
  private byte[] data;
  private byte[] original;
  private int charstringOffset = -1;
  private int privateOffset = -1;
  private int privateOffsetLocation = -1;
  private int privateOffsetLength = -1;
  private int privateLength = -1;
  private int privateLengthLocation = -1;
  private int privateLengthLength = -1;
  private int fdArrayOffset = -1;
  private int fdArrayOffsetLocation = -1;
  private int fdArrayOffsetLength = -1;
  private boolean possibleDotsection = false;
  private boolean possibleUnknownCommand = false;

  public CFFFixer(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
    this.original = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, this.original, 0, paramArrayOfByte.length);
    scanForProblems();
    if ((this.possibleDotsection) || (this.possibleUnknownCommand))
      fixData();
  }

  private void scanForProblems()
  {
    for (int i = 0; i < this.data.length - 1; i++)
      if ((this.data[i] == 12) && (this.data[(i + 1)] == 0))
        this.possibleDotsection = true;
      else if ((this.data[i] == 12) && (this.data[(i + 1)] == 15))
        this.possibleUnknownCommand = true;
  }

  private void fixData()
  {
    try
    {
      int i = this.data[2];
      int j = FontWriter.getUintFromByteArray(this.data, i, 2);
      int k = this.data[(i + 2)];
      int m = i + 3 + k * j;
      int n = FontWriter.getUintFromByteArray(this.data, m, k);
      int i1 = m + n + (k - 1);
      int i2 = FontWriter.getUintFromByteArray(this.data, i1, 2);
      if (i2 != 1)
      {
        this.data = this.original;
        return;
      }
      int i3 = this.data[(i1 + 2)];
      int i4 = i1 + 3 + i3 * i2;
      int i5 = FontWriter.getUintFromByteArray(this.data, i4, i3);
      int i6 = i4 + i3;
      byte[] arrayOfByte1 = new byte[i5 - 1];
      System.arraycopy(this.data, i6, arrayOfByte1, 0, i5 - 1);
      decodeTopDict(arrayOfByte1);
      int i9;
      int i11;
      int i12;
      int i17;
      int i19;
      if (this.possibleDotsection)
      {
        if (this.charstringOffset == -1)
        {
          this.data = this.original;
          return;
        }
        int i7 = FontWriter.getUintFromByteArray(this.data, this.charstringOffset, 2);
        int i8 = this.data[(this.charstringOffset + 2)];
        i9 = this.charstringOffset + 3;
        int[] arrayOfInt2 = new int[i7 + 1];
        for (i11 = 0; i11 < i7 + 1; i11++)
          arrayOfInt2[i11] = FontWriter.getUintFromByteArray(this.data, i9 + i8 * i11, i8);
        i11 = i9 + (i7 + 1) * i8;
        i12 = 0;
        for (int i13 = 0; i13 < i7; i13++)
        {
          int i15 = i11 + arrayOfInt2[i13] - 1;
          i17 = i11 + arrayOfInt2[(i13 + 1)] - 1;
          int[] arrayOfInt3 = new int[i17 - i15];
          for (i19 = 0; i19 < arrayOfInt3.length; i19++)
          {
            arrayOfInt3[i19] = this.data[(i15 + i19)];
            if (arrayOfInt3[i19] < 0)
              arrayOfInt3[i19] += 256;
          }
          i19 = 0;
          while (i19 < arrayOfInt3.length)
          {
            int i22 = arrayOfInt3[i19];
            int i20;
            int i21;
            if ((i22 >= 32) && (i22 <= 246))
            {
              i20 = 1;
              i21 = 0;
            }
            else if ((i22 >= 247) && (i22 <= 254))
            {
              i20 = 2;
              i21 = 0;
            }
            else if (i22 == 255)
            {
              i20 = 5;
              i21 = 0;
            }
            else if (i22 == 28)
            {
              i20 = 3;
              i21 = 0;
            }
            else if (i22 == 12)
            {
              if (arrayOfInt3[(i19 + 1)] == 0)
              {
                System.arraycopy(this.data, i15 + i19 + 2, this.data, i15 + i19, this.data.length - (i15 + i19 + 2));
                System.arraycopy(arrayOfInt3, i19 + 2, arrayOfInt3, i19, arrayOfInt3.length - (i19 + 2));
                for (int i23 = i13 + 1; i23 < arrayOfInt2.length; i23++)
                  arrayOfInt2[i23] -= 2;
                i12 += 2;
                i21 = 0;
              }
              else
              {
                i21 = 2;
              }
              i20 = 0;
            }
            else
            {
              i21 = 1;
              i20 = 0;
            }
            i19 += i21 + i20;
          }
        }
        if (i12 == 0)
        {
          this.data = this.original;
          return;
        }
        for (i13 = 0; i13 < arrayOfInt2.length; i13++)
        {
          byte[] arrayOfByte4 = FontWriter.setUintAsBytes(arrayOfInt2[i13], i8);
          System.arraycopy(arrayOfByte4, 0, this.data, i9 + i13 * i8, i8);
        }
        if ((this.fdArrayOffset != -1) && (this.fdArrayOffsetLength != -1) && (this.fdArrayOffsetLocation != -1))
        {
          arrayOfByte3 = CFFUtils.storeInteger(this.fdArrayOffset - i12);
          arrayOfByte3 = pad1cNumber(this.fdArrayOffsetLength, arrayOfByte3, this.fdArrayOffset - i12);
          System.arraycopy(arrayOfByte3, 0, this.data, i6 + this.fdArrayOffsetLocation, this.fdArrayOffsetLength);
        }
        if ((this.privateOffset != -1) && (this.privateOffsetLength != -1) && (this.privateOffsetLocation != -1))
        {
          arrayOfByte3 = CFFUtils.storeInteger(this.privateOffset - i12);
          arrayOfByte3 = pad1cNumber(this.privateOffsetLength, arrayOfByte3, this.privateOffset - i12);
          System.arraycopy(arrayOfByte3, 0, this.data, i6 + this.privateOffsetLocation, this.privateOffsetLength);
          this.privateOffset -= i12;
        }
        byte[] arrayOfByte3 = this.data;
        this.data = new byte[this.data.length - i12];
        System.arraycopy(arrayOfByte3, 0, this.data, 0, this.data.length);
      }
      if ((this.possibleUnknownCommand) && (this.privateOffset != -1))
      {
        byte[] arrayOfByte2 = new byte[this.privateLength];
        System.arraycopy(this.data, this.privateOffset, arrayOfByte2, 0, this.privateLength);
        int[] arrayOfInt1 = new int[arrayOfByte2.length];
        for (i9 = 0; i9 < arrayOfByte2.length; i9++)
          arrayOfInt1[i9] = (arrayOfByte2[i9] < 0 ? arrayOfByte2[i9] + 256 : arrayOfByte2[i9]);
        i9 = 0;
        int i10 = 0;
        i12 = -1;
        int i14 = -1;
        int i16 = -1;
        while (i9 < arrayOfInt1.length)
        {
          i17 = arrayOfInt1[i9];
          if ((i17 >= 32) && (i17 <= 246))
          {
            i10 = 1;
            i11 = 0;
          }
          else if ((i17 >= 247) && (i17 <= 250))
          {
            i10 = 2;
            i11 = 0;
          }
          else if ((i17 >= 251) && (i17 <= 254))
          {
            i10 = 2;
            i11 = 0;
          }
          else if (i17 == 28)
          {
            i10 = 3;
            i11 = 0;
          }
          else if (i17 == 29)
          {
            i10 = 5;
            i11 = 0;
          }
          else if (i17 == 30)
          {
            for (i10 = 1; (arrayOfInt1[(i9 + i10)] & 0xF) != 15; i10++);
            i10++;
            i11 = 0;
          }
          else if (i17 == 12)
          {
            if (arrayOfInt1[(i9 + 1)] == 15)
            {
              i12 = i9 - i10;
              i14 = i10;
              i16 = i9;
            }
            i11 = 2;
            i10 = 0;
          }
          else
          {
            i11 = 1;
            i10 = 0;
          }
          i9 += i11 + i10;
        }
        if (i16 != -1)
        {
          i17 = 2 + i14;
          int i18 = this.privateOffset + i12;
          i19 = this.privateOffset + i16 + 2;
          byte[] arrayOfByte5 = this.data;
          this.data = new byte[this.data.length - i17];
          System.arraycopy(arrayOfByte5, 0, this.data, 0, i18);
          System.arraycopy(arrayOfByte5, i19, this.data, i18, this.data.length - i18);
          if ((this.privateLength != -1) && (this.privateLengthLength != -1) && (this.privateLengthLocation != -1))
          {
            byte[] arrayOfByte6 = CFFUtils.storeInteger(this.privateLength - i17);
            arrayOfByte6 = pad1cNumber(this.privateLengthLength, arrayOfByte6, this.privateLength - i17);
            System.arraycopy(arrayOfByte6, 0, this.data, i6 + this.privateLengthLocation, this.privateLengthLength);
          }
        }
      }
    }
    catch (Exception localException)
    {
      this.data = this.original;
    }
  }

  private void decodeTopDict(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++)
      arrayOfInt[i] = (paramArrayOfByte[i] < 0 ? paramArrayOfByte[i] + 256 : paramArrayOfByte[i]);
    i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    while (i < arrayOfInt.length)
    {
      int i2 = arrayOfInt[i];
      int i1;
      if ((i2 >= 32) && (i2 <= 246))
      {
        n = m;
        k = j;
        m = i2 - 139;
        j = 1;
        i1 = 0;
      }
      else if ((i2 >= 247) && (i2 <= 250))
      {
        n = m;
        k = j;
        m = (i2 - 247) * 256 + arrayOfInt[(i + 1)] + 108;
        j = 2;
        i1 = 0;
      }
      else if ((i2 >= 251) && (i2 <= 254))
      {
        n = m;
        k = j;
        m = -((i2 - 251) * 256) - arrayOfInt[(i + 1)] - 108;
        j = 2;
        i1 = 0;
      }
      else if (i2 == 28)
      {
        n = m;
        k = j;
        m = FontWriter.getUintFromIntArray(arrayOfInt, i + 1, 2);
        j = 3;
        i1 = 0;
      }
      else if (i2 == 29)
      {
        n = m;
        k = j;
        m = FontWriter.getUintFromIntArray(arrayOfInt, i + 1, 4);
        j = 5;
        i1 = 0;
      }
      else if (i2 == 30)
      {
        n = m;
        k = j;
        for (j = 1; (arrayOfInt[(i + j)] & 0xF) != 15; j++);
        j++;
        i1 = 0;
      }
      else if (i2 == 12)
      {
        if (arrayOfInt[(i + 1)] == 36)
        {
          this.fdArrayOffsetLocation = (i - j);
          this.fdArrayOffsetLength = j;
          this.fdArrayOffset = m;
        }
        i1 = 2;
        j = 0;
      }
      else if (i2 == 17)
      {
        this.charstringOffset = m;
        i1 = 1;
        j = 0;
      }
      else if (i2 == 18)
      {
        this.privateOffsetLocation = (i - j);
        this.privateOffsetLength = j;
        this.privateOffset = m;
        this.privateLengthLocation = (i - j - k);
        this.privateLengthLength = k;
        this.privateLength = n;
        i1 = 1;
        j = 0;
      }
      else
      {
        i1 = 1;
        j = 0;
      }
      i += i1 + j;
    }
  }

  private static byte[] pad1cNumber(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    if (paramInt1 == paramArrayOfByte.length)
      return paramArrayOfByte;
    if (paramInt1 < paramArrayOfByte.length)
      throw new IllegalArgumentException("Trying to pad a number to a smaller size.");
    if ((paramInt1 != 2) && (paramInt1 != 3) && (paramInt1 != 5))
      throw new IllegalArgumentException("Padding to an incorect number of bytes.");
    byte[] arrayOfByte;
    if (paramInt1 == 2)
      arrayOfByte = new byte[] { -117, paramArrayOfByte[0] };
    else if (paramInt1 == 3)
      arrayOfByte = new byte[] { 28, (byte)(paramInt2 >> 8 & 0xFF), (byte)(paramInt2 & 0xFF) };
    else
      arrayOfByte = new byte[] { 29, (byte)(paramInt2 >> 24 & 0xFF), (byte)(paramInt2 >> 16 & 0xFF), (byte)(paramInt2 >> 8 & 0xFF), (byte)(paramInt2 & 0xFF) };
    return arrayOfByte;
  }

  public byte[] getBytes()
  {
    return this.data;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.CFFFixer
 * JD-Core Version:    0.6.2
 */
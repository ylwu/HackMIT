package org.jpedal.fonts;

import org.jpedal.utils.LogWriter;

public class UnicodeReader
{
  private static final int[] powers = { 1, 16, 256, 4096 };
  int ptr;
  byte[] data;
  boolean hasDoubleBytes = false;

  public UnicodeReader(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
  }

  public String[] readUnicode()
  {
    if (this.data == null)
      return null;
    int i = 0;
    String[] arrayOfString = new String[65536];
    int j = this.data.length;
    int k = 0;
    try
    {
      while (true)
        if ((this.ptr < j) && (this.data[this.ptr] == 9))
        {
          this.ptr += 1;
        }
        else
        {
          if (this.ptr >= j)
            break;
          if ((this.ptr + 4 < j) && (this.data[this.ptr] == 101) && (this.data[(this.ptr + 1)] == 110) && (this.data[(this.ptr + 2)] == 100) && (this.data[(this.ptr + 3)] == 98) && (this.data[(this.ptr + 4)] == 102))
          {
            i = 0;
            k = 0;
          }
          else if (k != 0)
          {
            readLineValue(arrayOfString, i);
          }
          if (this.ptr >= j)
            break;
          if ((this.data[this.ptr] == 98) && (this.data[(this.ptr + 1)] == 101) && (this.data[(this.ptr + 2)] == 103) && (this.data[(this.ptr + 3)] == 105) && (this.data[(this.ptr + 4)] == 110) && (this.data[(this.ptr + 5)] == 98) && (this.data[(this.ptr + 6)] == 102) && (this.data[(this.ptr + 7)] == 99) && (this.data[(this.ptr + 8)] == 104) && (this.data[(this.ptr + 9)] == 97) && (this.data[(this.ptr + 10)] == 114))
          {
            i = 1;
            this.ptr += 10;
            k = 1;
          }
          else if ((this.data[this.ptr] == 98) && (this.data[(this.ptr + 1)] == 101) && (this.data[(this.ptr + 2)] == 103) && (this.data[(this.ptr + 3)] == 105) && (this.data[(this.ptr + 4)] == 110) && (this.data[(this.ptr + 5)] == 98) && (this.data[(this.ptr + 6)] == 102) && (this.data[(this.ptr + 7)] == 114) && (this.data[(this.ptr + 8)] == 97) && (this.data[(this.ptr + 9)] == 110) && (this.data[(this.ptr + 10)] == 103) && (this.data[(this.ptr + 11)] == 101))
          {
            i = 2;
            this.ptr += 11;
            k = 1;
          }
          this.ptr += 1;
        }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception setting up text object " + localException);
    }
    return arrayOfString;
  }

  private void readLineValue(String[] paramArrayOfString, int paramInt)
  {
    int i = paramInt + 1;
    int j = this.data.length;
    int[] arrayOfInt = new int[2000];
    int m = 0;
    for (int n = 0; n < i; n++)
    {
      if (m == 0)
      {
        while ((this.ptr < this.data.length) && (this.data[this.ptr] != 60))
        {
          if ((n == 2) && (i == 3) && (this.data[this.ptr] == 91))
          {
            paramInt = 4;
            for (i1 = this.ptr; this.data[i1] != 93; i1++)
              if (this.data[i1] == 60)
                i++;
            i--;
          }
          this.ptr += 1;
        }
        this.ptr += 1;
      }
      int i1 = 0;
      int i2 = 0;
      while ((this.ptr < j) && (this.data[this.ptr] != 62))
      {
        if ((this.data[this.ptr] != 10) && (this.data[this.ptr] != 13) && (this.data[this.ptr] != 32))
          i2++;
        this.ptr += 1;
        i1++;
        if (i2 == 5)
        {
          i1 = 4;
          this.ptr -= 1;
          i++;
          m = 1;
        }
      }
      int i3 = 0;
      for (int i4 = 0; i4 < i1; i4++)
      {
        int k;
        while (true)
        {
          k = this.data[(this.ptr - 1 - i4)];
          if ((k != 10) && (k != 13) && (k != 32))
            break;
          i4++;
        }
        if ((k >= 65) && (k <= 70))
          k -= 55;
        else if ((k >= 97) && (k <= 102))
          k -= 87;
        else if ((k >= 48) && (k <= 57))
          k -= 48;
        else
          throw new RuntimeException("Unexpected number " + (char)k);
        arrayOfInt[n] += k * powers[i3];
        i3++;
      }
    }
    while ((this.ptr < j) && ((this.data[this.ptr] == 62) || (this.data[this.ptr] == 32) || (this.data[this.ptr] == 10) || (this.data[this.ptr] == 13) || (this.data[this.ptr] == 93)))
      this.ptr += 1;
    this.ptr -= 1;
    fillValues(paramArrayOfString, i, arrayOfInt, paramInt);
  }

  private void fillValues(String[] paramArrayOfString, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    int k;
    int i;
    switch (paramInt2)
    {
    case 1:
      if (paramInt1 == 2)
      {
        if (paramArrayOfInt[paramInt2] > 0)
        {
          paramArrayOfString[paramArrayOfInt[0]] = String.valueOf((char)paramArrayOfInt[paramInt2]);
          if (paramArrayOfInt[0] > 255)
            this.hasDoubleBytes = true;
        }
      }
      else
      {
        char[] arrayOfChar = new char[paramInt1 - 1];
        for (k = 0; k < paramInt1 - 1; k++)
          arrayOfChar[k] = ((char)paramArrayOfInt[(paramInt2 + k)]);
        paramArrayOfString[paramArrayOfInt[0]] = new String(arrayOfChar);
        if (paramArrayOfInt[0] > 255)
          this.hasDoubleBytes = true;
      }
      break;
    case 4:
      int j = 2;
      for (k = paramArrayOfInt[0]; k < paramArrayOfInt[1] + 1; k++)
        if ((paramInt1 > 1) && (paramArrayOfInt[0] == paramArrayOfInt[1]))
        {
          paramArrayOfString[k] = String.valueOf((char)paramArrayOfInt[2]);
          if (k > 255)
            this.hasDoubleBytes = true;
          for (int m = 1; m < paramInt1; m++)
          {
            paramArrayOfString[k] = (paramArrayOfString[k] + String.valueOf((char)paramArrayOfInt[(2 + m)]));
            if (k > 255)
              this.hasDoubleBytes = true;
          }
        }
        else
        {
          i = paramArrayOfInt[j];
          j++;
          if (i > 0)
          {
            paramArrayOfString[k] = String.valueOf((char)i);
            if (k > 255)
              this.hasDoubleBytes = true;
          }
        }
      break;
    default:
      for (k = paramArrayOfInt[0]; k < paramArrayOfInt[1] + 1; k++)
      {
        i = paramArrayOfInt[paramInt2] + k - paramArrayOfInt[0];
        if (i > 0)
        {
          paramArrayOfString[k] = String.valueOf((char)i);
          if (k > 255)
            this.hasDoubleBytes = true;
        }
      }
    }
  }

  public boolean hasDoubleByteValues()
  {
    return this.hasDoubleBytes;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.UnicodeReader
 * JD-Core Version:    0.6.2
 */
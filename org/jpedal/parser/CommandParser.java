package org.jpedal.parser;

public class CommandParser
{
  private byte[] characterStream;
  private int commandID = -1;
  private static final int[] prefixes = { 60, 40 };
  private static final int[] suffixes = { 62, 41 };
  private static final int[][] intValues = { { 0, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000 }, { 0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000 }, { 0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000 }, { 0, 100, 200, 300, 400, 500, 600, 700, 800, 900 }, { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } };
  private static final int MAXOPS = 50;
  private int[] opStart = new int[50];
  private int[] opEnd = new int[50];
  private int operandCount;
  private int currentOp = 0;

  public CommandParser(byte[] paramArrayOfByte)
  {
    this.characterStream = paramArrayOfByte;
  }

  int getCommandValues(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = prefixes.length;
    int j = this.characterStream[paramInt1];
    int m = 0;
    this.commandID = -1;
    int n = this.characterStream.length;
    int i1 = j;
    if ((j == 13) || (j == 10) || (j == 32) || (j == 9))
    {
      paramInt1++;
      while (paramInt1 != paramInt2)
      {
        i1 = this.characterStream[paramInt1];
        if ((i1 != 13) && (i1 != 10) && (i1 != 32) && (i1 != 9))
          break;
        paramInt1++;
      }
    }
    while (i1 == 37)
    {
      paramInt1++;
      while (paramInt1 != paramInt2)
      {
        i1 = this.characterStream[paramInt1];
        if ((i1 == 13) || (i1 == 10))
          while ((paramInt1 + 1 < paramInt2) && (this.characterStream[(paramInt1 + 1)] == 10))
          {
            paramInt1++;
            i1 = this.characterStream[paramInt1];
          }
        paramInt1++;
      }
      paramInt1++;
      if (paramInt1 >= paramInt2)
        break;
      i1 = this.characterStream[paramInt1];
    }
    if (paramInt1 == paramInt2)
      return paramInt1;
    int i2 = 0;
    int i3 = getType(i1, paramInt1);
    int k;
    int i4;
    if (i3 == 3)
    {
      k = paramInt1;
      while (true)
      {
        paramInt1++;
        if (paramInt1 < n)
        {
          i1 = this.characterStream[paramInt1];
          if ((i1 != 13) && (i1 != 10) && (i1 != 32) && (i1 != 40) && (i1 != 47) && (i1 != 91) && (i1 != 9))
            if (i1 == 60)
              break;
        }
      }
      m = paramInt1 - 1;
      if (m >= n)
        return m;
      i4 = this.characterStream[m];
      if ((i4 == 47) || (i4 == 91) || (i4 == 60))
        m--;
      this.commandID = -1;
      int i9;
      if (m - k < 3)
      {
        int i5 = 0;
        int i7 = 0;
        for (i9 = m; i9 > k - 1; i9--)
        {
          i5 += (this.characterStream[i9] << i7);
          i7 += 8;
        }
        this.commandID = Cmd.getCommandID(i5);
      }
      if (this.commandID == -1)
      {
        this.opStart[this.currentOp] = k;
        this.opEnd[this.currentOp] = m;
        this.currentOp += 1;
        if (this.currentOp == 50)
          this.currentOp = 0;
        this.operandCount += 1;
      }
      else
      {
        if (this.operandCount > 0)
        {
          int[] arrayOfInt1 = new int[50];
          int[] arrayOfInt2 = new int[50];
          i9 = 0;
          for (int i10 = this.currentOp - 1; i10 > -1; i10--)
          {
            arrayOfInt1[i9] = this.opStart[i10];
            arrayOfInt2[i9] = this.opEnd[i10];
            if (i9 == this.operandCount)
              i10 = -1;
            i9++;
          }
          if (i9 == this.operandCount)
          {
            this.currentOp -= 1;
            for (i10 = 49; i10 > this.currentOp; i10--)
            {
              arrayOfInt1[i9] = this.opStart[i10];
              arrayOfInt2[i9] = this.opEnd[i10];
              if (i9 == this.operandCount)
                i10 = this.currentOp;
              i9++;
            }
            this.currentOp += 1;
          }
          this.opStart = arrayOfInt1;
          this.opEnd = arrayOfInt2;
        }
        return -paramInt1;
      }
    }
    else if (i3 != 4)
    {
      k = paramInt1;
      int i6;
      int i8;
      if ((i3 == 1) || (i3 == 2))
      {
        i4 = 0;
        i2 = 1;
        i6 = 32;
        while (true)
        {
          if ((i6 == 92) && (i1 == 92))
            i6 = 120;
          else
            i6 = i1;
          paramInt1++;
          if (paramInt1 == n)
            break;
          i1 = this.characterStream[paramInt1];
          if ((i1 == 13) || (i1 == 10) || (i1 == 9))
            i1 = 32;
          i8 = 0;
          if ((i1 == 62) && (i6 == 62) && (i3 == 1))
            i8 = 1;
          if (i3 == 2)
          {
            if ((i1 == 40) && (i6 != 92))
              i4 = 1;
            else if ((i1 == 41) && (i6 != 92))
              i4 = 0;
            if ((i4 == 0) && (i1 == 93) && (i6 != 92))
              i8 = 1;
          }
          if (i8 != 0)
            break;
        }
        m = paramInt1;
      }
      if (i2 == 0)
      {
        i4 = 32;
        for (i6 = 0; i6 < i; i6++)
          if (i1 == prefixes[i6])
          {
            i2 = 1;
            k = paramInt1;
            for (i8 = 0; ; i8--)
            {
              do
              {
                if ((i4 == 92) && (i1 == 92))
                  i4 = 120;
                else
                  i4 = i1;
                paramInt1++;
                if (paramInt1 == n)
                  break;
                i1 = this.characterStream[paramInt1];
                if ((i1 == 13) || (i1 == 10) || (i1 == 9))
                  i1 = 32;
                if ((i1 == prefixes[i6]) && (i4 != 92))
                  i8++;
              }
              while ((i1 != suffixes[i6]) || (i4 == 92));
              if (i8 == 0)
                break;
            }
            i6 = i;
          }
        m = paramInt1;
      }
      if (i2 == 0)
      {
        k = paramInt1;
        i4 = this.characterStream[k];
        while (true)
        {
          paramInt1++;
          if (paramInt1 != n)
          {
            i1 = this.characterStream[paramInt1];
            if ((i1 != 13) && (i1 != 10) && (i1 != 32) && (i1 != 40) && (i1 != 47) && (i1 != 91) && (i1 != 9))
              if ((i4 == 47) && (i1 == 60))
                break;
          }
        }
        m = paramInt1;
      }
      if (m < this.characterStream.length)
      {
        i4 = this.characterStream[m];
        if ((i4 == 47) || (i4 == 91))
          m--;
      }
      this.opStart[this.currentOp] = k;
      this.opEnd[this.currentOp] = m;
      this.currentOp += 1;
      if (this.currentOp == 50)
        this.currentOp = 0;
      this.operandCount += 1;
    }
    if (paramInt1 < paramInt2)
    {
      j = this.characterStream[paramInt1];
      if ((j != 47) && (j != 40) && (j != 91) && (j != 60))
        paramInt1++;
    }
    return paramInt1;
  }

  public int getCommandID()
  {
    return this.commandID;
  }

  private int getType(int paramInt1, int paramInt2)
  {
    int i = 0;
    if ((paramInt1 == 60) && (this.characterStream[(paramInt2 + 1)] == 60))
      i = 1;
    else if (paramInt1 == 32)
      i = 4;
    else if (paramInt1 == 91)
      i = 2;
    else if ((paramInt1 >= 97) && (paramInt1 <= 122))
      i = 3;
    else if ((paramInt1 >= 65) && (paramInt1 <= 90))
      i = 3;
    else if ((paramInt1 == 39) || (paramInt1 == 34))
      i = 3;
    return i;
  }

  String generateOpAsString(int paramInt, boolean paramBoolean)
  {
    byte[] arrayOfByte = this.characterStream;
    int i = this.opStart[paramInt];
    if ((paramBoolean) && (arrayOfByte[i] == 47))
      i++;
    for (int j = this.opEnd[paramInt]; (arrayOfByte[j] == 32) || (arrayOfByte[j] == 13) || (arrayOfByte[j] == 10); j--);
    int k = j - i + 1;
    int m = 0;
    for (int n = 0; n < k; n++)
      if ((n > 0) && ((arrayOfByte[(i + n)] == 32) || (arrayOfByte[(i + n)] == 13) || (arrayOfByte[(i + n)] == 10)) && ((arrayOfByte[(i + n - 1)] == 32) || (arrayOfByte[(i + n - 1)] == 13) || (arrayOfByte[(i + n - 1)] == 10)))
        m++;
    char[] arrayOfChar = new char[k - m];
    int i1 = 0;
    for (int i2 = 0; i2 < k; i2++)
      if ((i2 <= 0) || ((arrayOfByte[(i + i2)] != 32) && (arrayOfByte[(i + i2)] != 13) && (arrayOfByte[(i + i2)] != 10)) || ((arrayOfByte[(i + i2 - 1)] != 32) && (arrayOfByte[(i + i2 - 1)] != 13) && (arrayOfByte[(i + i2 - 1)] != 10)))
      {
        if ((arrayOfByte[(i + i2)] == 10) || (arrayOfByte[(i + i2)] == 13))
          arrayOfChar[i1] = ' ';
        else
          arrayOfChar[i1] = ((char)arrayOfByte[(i + i2)]);
        i1++;
      }
    String str = String.copyValueOf(arrayOfChar);
    return str;
  }

  final float parseFloat(int paramInt)
  {
    byte[] arrayOfByte = this.characterStream;
    int i = this.opStart[paramInt];
    int j = this.opEnd[paramInt] - i;
    int k = j;
    int m = 0;
    int n = 0;
    for (int i1 = j - 1; i1 > -1; i1--)
      if (arrayOfByte[(i + i1)] == 46)
      {
        k = i1;
        break;
      }
    i1 = k;
    if (arrayOfByte[i] == 43)
    {
      i1--;
      m++;
    }
    else if (arrayOfByte[i] == 45)
    {
      m++;
      n = 1;
    }
    int i2 = i1 - m;
    int i3 = j - k;
    float f1;
    if ((i2 > 3) || (i3 > 11))
    {
      n = 0;
      f1 = Float.parseFloat(generateOpAsString(paramInt, false));
    }
    else
    {
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f6 = 0.0F;
      float f7 = 0.0F;
      float f8 = 0.0F;
      float f9 = 0.0F;
      float f10 = 0.0F;
      float f11 = 0.0F;
      int i4;
      if (i2 > 2)
      {
        i4 = arrayOfByte[(i + m)] - 48;
        switch (i4)
        {
        case 1:
          f6 = 100.0F;
          break;
        case 2:
          f6 = 200.0F;
          break;
        case 3:
          f6 = 300.0F;
          break;
        case 4:
          f6 = 400.0F;
          break;
        case 5:
          f6 = 500.0F;
          break;
        case 6:
          f6 = 600.0F;
          break;
        case 7:
          f6 = 700.0F;
          break;
        case 8:
          f6 = 800.0F;
          break;
        case 9:
          f6 = 900.0F;
        }
        m++;
      }
      if (i2 > 1)
      {
        i4 = arrayOfByte[(i + m)] - 48;
        switch (i4)
        {
        case 1:
          f5 = 10.0F;
          break;
        case 2:
          f5 = 20.0F;
          break;
        case 3:
          f5 = 30.0F;
          break;
        case 4:
          f5 = 40.0F;
          break;
        case 5:
          f5 = 50.0F;
          break;
        case 6:
          f5 = 60.0F;
          break;
        case 7:
          f5 = 70.0F;
          break;
        case 8:
          f5 = 80.0F;
          break;
        case 9:
          f5 = 90.0F;
        }
        m++;
      }
      if (i2 > 0)
      {
        i4 = arrayOfByte[(i + m)] - 48;
        switch (i4)
        {
        case 1:
          f4 = 1.0F;
          break;
        case 2:
          f4 = 2.0F;
          break;
        case 3:
          f4 = 3.0F;
          break;
        case 4:
          f4 = 4.0F;
          break;
        case 5:
          f4 = 5.0F;
          break;
        case 6:
          f4 = 6.0F;
          break;
        case 7:
          f4 = 7.0F;
          break;
        case 8:
          f4 = 8.0F;
          break;
        case 9:
          f4 = 9.0F;
        }
      }
      if (i3 > 1)
      {
        k++;
        i4 = arrayOfByte[(i + k)] - 48;
        switch (i4)
        {
        case 1:
          f7 = 0.1F;
          break;
        case 2:
          f7 = 0.2F;
          break;
        case 3:
          f7 = 0.3F;
          break;
        case 4:
          f7 = 0.4F;
          break;
        case 5:
          f7 = 0.5F;
          break;
        case 6:
          f7 = 0.6F;
          break;
        case 7:
          f7 = 0.7F;
          break;
        case 8:
          f7 = 0.8F;
          break;
        case 9:
          f7 = 0.9F;
        }
      }
      if (i3 > 2)
      {
        k++;
        i4 = arrayOfByte[(i + k)] - 48;
        switch (i4)
        {
        case 1:
          f8 = 0.01F;
          break;
        case 2:
          f8 = 0.02F;
          break;
        case 3:
          f8 = 0.03F;
          break;
        case 4:
          f8 = 0.04F;
          break;
        case 5:
          f8 = 0.05F;
          break;
        case 6:
          f8 = 0.06F;
          break;
        case 7:
          f8 = 0.07F;
          break;
        case 8:
          f8 = 0.08F;
          break;
        case 9:
          f8 = 0.09F;
        }
      }
      if (i3 > 3)
      {
        k++;
        i4 = arrayOfByte[(i + k)] - 48;
        switch (i4)
        {
        case 1:
          f9 = 0.001F;
          break;
        case 2:
          f9 = 0.002F;
          break;
        case 3:
          f9 = 0.003F;
          break;
        case 4:
          f9 = 0.004F;
          break;
        case 5:
          f9 = 0.005F;
          break;
        case 6:
          f9 = 0.006F;
          break;
        case 7:
          f9 = 0.007F;
          break;
        case 8:
          f9 = 0.008F;
          break;
        case 9:
          f9 = 0.009F;
        }
      }
      if (i3 > 4)
      {
        k++;
        i4 = arrayOfByte[(i + k)] - 48;
        switch (i4)
        {
        case 1:
          f10 = 1.0E-004F;
          break;
        case 2:
          f10 = 0.0002F;
          break;
        case 3:
          f10 = 0.0003F;
          break;
        case 4:
          f10 = 0.0004F;
          break;
        case 5:
          f10 = 0.0005F;
          break;
        case 6:
          f10 = 0.0006F;
          break;
        case 7:
          f10 = 0.0007F;
          break;
        case 8:
          f10 = 0.0008F;
          break;
        case 9:
          f10 = 0.0009F;
        }
      }
      if (i3 > 5)
      {
        k++;
        i4 = arrayOfByte[(i + k)] - 48;
        switch (i4)
        {
        case 1:
          f11 = 1.0E-005F;
          break;
        case 2:
          f11 = 2.0E-005F;
          break;
        case 3:
          f11 = 3.0E-005F;
          break;
        case 4:
          f11 = 4.0E-005F;
          break;
        case 5:
          f11 = 5.0E-005F;
          break;
        case 6:
          f11 = 6.0E-005F;
          break;
        case 7:
          f11 = 7.E-005F;
          break;
        case 8:
          f11 = 8.0E-005F;
          break;
        case 9:
          f11 = 9.E-005F;
        }
      }
      float f2 = f7 + f8 + f9 + f10 + f11;
      float f3 = f6 + f5 + f4;
      f1 = f3 + f2;
    }
    if (n != 0)
      return -f1;
    return f1;
  }

  float[] getValuesAsFloat()
  {
    float[] arrayOfFloat = new float[this.operandCount];
    for (int i = 0; i < this.operandCount; i++)
      arrayOfFloat[i] = parseFloat(i);
    return arrayOfFloat;
  }

  String[] getValuesAsString()
  {
    String[] arrayOfString = new String[this.operandCount];
    for (int i = 0; i < this.operandCount; i++)
      arrayOfString[i] = generateOpAsString(i, true);
    return arrayOfString;
  }

  final int parseInt(int paramInt)
  {
    int i = this.opStart[paramInt];
    int j = this.opEnd[paramInt];
    byte[] arrayOfByte = this.characterStream;
    int k = 0;
    int m = 0;
    int n = j - i;
    int i1 = 0;
    int i2 = 0;
    int i3 = n;
    if (arrayOfByte[i] == 43)
    {
      i3--;
      i1++;
    }
    else if (arrayOfByte[i] == 45)
    {
      i1++;
      i2 = 1;
    }
    int i4 = i3 - i1;
    if (i4 > 6)
    {
      i2 = 0;
      k = Integer.parseInt(generateOpAsString(m, false));
    }
    else
    {
      for (int i6 = 5; i6 > -1; i6--)
        if (i4 > i6)
        {
          int i5 = arrayOfByte[(i + i1)] - 48;
          k += intValues[(5 - i6)][i5];
          i1++;
        }
    }
    if (i2 != 0)
      return -k;
    return k;
  }

  public void reset()
  {
    this.currentOp = 0;
    this.operandCount = 0;
  }

  public int getOperandCount()
  {
    return this.operandCount;
  }

  public byte[] getStream()
  {
    return this.characterStream;
  }

  public int getcurrentOp()
  {
    return this.currentOp;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.CommandParser
 * JD-Core Version:    0.6.2
 */
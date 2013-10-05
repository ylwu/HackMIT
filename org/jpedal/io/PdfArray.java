package org.jpedal.io;

public class PdfArray
{
  public static float[] convertToFloatArray(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    float[] arrayOfFloat = byteStreamToFloatArray(arrayOfChar);
    return arrayOfFloat;
  }

  private static float[] byteStreamToFloatArray(char[] paramArrayOfChar)
  {
    int i = 0;
    int k = paramArrayOfChar.length;
    int m = 0;
    float[] arrayOfFloat1 = new float[k];
    while ((m < k) && (paramArrayOfChar[m] != '['))
      m++;
    if (m == paramArrayOfChar.length)
      m = 0;
    else
      m++;
    while (m < k)
    {
      while ((m < k) && (paramArrayOfChar[m] == ' '))
        m++;
      int j = m;
      while ((m < k) && (paramArrayOfChar[m] != ' ') && (paramArrayOfChar[m] != ']'))
        m++;
      int n = m - j;
      if (n < 1)
      {
        m = k;
      }
      else
      {
        char[] arrayOfChar = new char[n];
        int i1 = 0;
        while (j < m)
        {
          arrayOfChar[i1] = paramArrayOfChar[j];
          i1++;
          j++;
        }
        arrayOfFloat1[i] = convertToFloat(arrayOfChar);
        i++;
      }
    }
    float[] arrayOfFloat2 = new float[i];
    System.arraycopy(arrayOfFloat1, 0, arrayOfFloat2, 0, i);
    return arrayOfFloat2;
  }

  private static float convertToFloat(char[] paramArrayOfChar)
  {
    int i = 0;
    int j = paramArrayOfChar.length;
    int k = j;
    int m = 0;
    int n = 0;
    for (int i1 = j - 1; i1 > -1; i1--)
      if (paramArrayOfChar[(i + i1)] == '.')
      {
        k = i1;
        break;
      }
    i1 = k;
    if (paramArrayOfChar[i] == '+')
    {
      i1--;
      m++;
    }
    else if (paramArrayOfChar[i] == '-')
    {
      m++;
      n = 1;
    }
    int i2 = i1 - m;
    int i3 = j - k;
    float f1;
    if (i2 > 4)
    {
      n = 0;
      f1 = Float.parseFloat(new String(paramArrayOfChar));
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
      float f12 = 0.0F;
      int i4;
      if (i2 > 3)
      {
        i4 = paramArrayOfChar[(i + m)] - '0';
        switch (i4)
        {
        case 1:
          f4 = 1000.0F;
          break;
        case 2:
          f4 = 2000.0F;
          break;
        case 3:
          f4 = 3000.0F;
          break;
        case 4:
          f4 = 4000.0F;
          break;
        case 5:
          f4 = 5000.0F;
          break;
        case 6:
          f4 = 6000.0F;
          break;
        case 7:
          f4 = 7000.0F;
          break;
        case 8:
          f4 = 8000.0F;
          break;
        case 9:
          f4 = 9000.0F;
        }
        m++;
      }
      if (i2 > 2)
      {
        i4 = paramArrayOfChar[(i + m)] - '0';
        switch (i4)
        {
        case 1:
          f7 = 100.0F;
          break;
        case 2:
          f7 = 200.0F;
          break;
        case 3:
          f7 = 300.0F;
          break;
        case 4:
          f7 = 400.0F;
          break;
        case 5:
          f7 = 500.0F;
          break;
        case 6:
          f7 = 600.0F;
          break;
        case 7:
          f7 = 700.0F;
          break;
        case 8:
          f7 = 800.0F;
          break;
        case 9:
          f7 = 900.0F;
        }
        m++;
      }
      if (i2 > 1)
      {
        i4 = paramArrayOfChar[(i + m)] - '0';
        switch (i4)
        {
        case 1:
          f6 = 10.0F;
          break;
        case 2:
          f6 = 20.0F;
          break;
        case 3:
          f6 = 30.0F;
          break;
        case 4:
          f6 = 40.0F;
          break;
        case 5:
          f6 = 50.0F;
          break;
        case 6:
          f6 = 60.0F;
          break;
        case 7:
          f6 = 70.0F;
          break;
        case 8:
          f6 = 80.0F;
          break;
        case 9:
          f6 = 90.0F;
        }
        m++;
      }
      if (i2 > 0)
      {
        i4 = paramArrayOfChar[(i + m)] - '0';
        switch (i4)
        {
        case 1:
          f5 = 1.0F;
          break;
        case 2:
          f5 = 2.0F;
          break;
        case 3:
          f5 = 3.0F;
          break;
        case 4:
          f5 = 4.0F;
          break;
        case 5:
          f5 = 5.0F;
          break;
        case 6:
          f5 = 6.0F;
          break;
        case 7:
          f5 = 7.0F;
          break;
        case 8:
          f5 = 8.0F;
          break;
        case 9:
          f5 = 9.0F;
        }
      }
      if (i3 > 1)
      {
        k++;
        i4 = paramArrayOfChar[(i + k)] - '0';
        switch (i4)
        {
        case 1:
          f8 = 0.1F;
          break;
        case 2:
          f8 = 0.2F;
          break;
        case 3:
          f8 = 0.3F;
          break;
        case 4:
          f8 = 0.4F;
          break;
        case 5:
          f8 = 0.5F;
          break;
        case 6:
          f8 = 0.6F;
          break;
        case 7:
          f8 = 0.7F;
          break;
        case 8:
          f8 = 0.8F;
          break;
        case 9:
          f8 = 0.9F;
        }
      }
      if (i3 > 2)
      {
        k++;
        i4 = paramArrayOfChar[(i + k)] - '0';
        switch (i4)
        {
        case 1:
          f9 = 0.01F;
          break;
        case 2:
          f9 = 0.02F;
          break;
        case 3:
          f9 = 0.03F;
          break;
        case 4:
          f9 = 0.04F;
          break;
        case 5:
          f9 = 0.05F;
          break;
        case 6:
          f9 = 0.06F;
          break;
        case 7:
          f9 = 0.07F;
          break;
        case 8:
          f9 = 0.08F;
          break;
        case 9:
          f9 = 0.09F;
        }
      }
      if (i3 > 3)
      {
        k++;
        i4 = paramArrayOfChar[(i + k)] - '0';
        switch (i4)
        {
        case 1:
          f10 = 0.001F;
          break;
        case 2:
          f10 = 0.002F;
          break;
        case 3:
          f10 = 0.003F;
          break;
        case 4:
          f10 = 0.004F;
          break;
        case 5:
          f10 = 0.005F;
          break;
        case 6:
          f10 = 0.006F;
          break;
        case 7:
          f10 = 0.007F;
          break;
        case 8:
          f10 = 0.008F;
          break;
        case 9:
          f10 = 0.009F;
        }
      }
      if (i3 > 4)
      {
        k++;
        i4 = paramArrayOfChar[(i + k)] - '0';
        switch (i4)
        {
        case 1:
          f11 = 1.0E-004F;
          break;
        case 2:
          f11 = 0.0002F;
          break;
        case 3:
          f11 = 0.0003F;
          break;
        case 4:
          f11 = 0.0004F;
          break;
        case 5:
          f11 = 0.0005F;
          break;
        case 6:
          f11 = 0.0006F;
          break;
        case 7:
          f11 = 0.0007F;
          break;
        case 8:
          f11 = 0.0008F;
          break;
        case 9:
          f11 = 0.0009F;
        }
      }
      if (i3 > 5)
      {
        k++;
        i4 = paramArrayOfChar[(i + k)] - '0';
        switch (i4)
        {
        case 1:
          f12 = 1.0E-005F;
          break;
        case 2:
          f12 = 2.0E-005F;
          break;
        case 3:
          f12 = 3.0E-005F;
          break;
        case 4:
          f12 = 4.0E-005F;
          break;
        case 5:
          f12 = 5.0E-005F;
          break;
        case 6:
          f12 = 6.0E-005F;
          break;
        case 7:
          f12 = 7.E-005F;
          break;
        case 8:
          f12 = 8.0E-005F;
          break;
        case 9:
          f12 = 9.E-005F;
        }
      }
      float f2 = f8 + f9 + f10 + f11 + f12;
      float f3 = f4 + f7 + f6 + f5;
      f1 = f3 + f2;
    }
    if (n != 0)
      return -f1;
    return f1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.PdfArray
 * JD-Core Version:    0.6.2
 */
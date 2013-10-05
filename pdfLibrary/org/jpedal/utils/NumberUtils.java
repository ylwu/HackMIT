package org.jpedal.utils;

public class NumberUtils
{
  public static final int[] powers = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };

  public static int parseInt(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    paramInt1--;
    for (int m = paramInt2 - 1; m > paramInt1; m--)
      if (paramArrayOfByte[m] == 45)
      {
        k = 1;
      }
      else
      {
        if (paramArrayOfByte[m] != 48)
          i += (paramArrayOfByte[m] - 48) * powers[j];
        j++;
      }
    if (k != 0)
      return -i;
    return i;
  }

  public static double parseDouble(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = paramInt2;
    int j = paramInt1;
    int k = 0;
    for (int m = paramInt2 - 1; m > paramInt1 - 1; m--)
      if (paramArrayOfByte[m] == 46)
      {
        i = m;
        break;
      }
    m = i;
    int n = i;
    if (paramArrayOfByte[paramInt1] == 43)
    {
      m--;
      j++;
    }
    else if (paramArrayOfByte[paramInt1] == 45)
    {
      j++;
      k = 1;
    }
    int i1 = m - j;
    int i2 = paramInt2 - i;
    double d1;
    if (i1 > 4)
    {
      k = 0;
      int i3 = paramInt2 - paramInt1;
      byte[] arrayOfByte = new byte[i3];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, i3);
      d1 = Double.parseDouble(new String(arrayOfByte));
    }
    else
    {
      double d4 = 0.0D;
      double d5 = 0.0D;
      double d6 = 0.0D;
      double d7 = 0.0D;
      double d8 = 0.0D;
      double d9 = 0.0D;
      double d10 = 0.0D;
      double d11 = 0.0D;
      double d12 = 0.0D;
      double d13 = 0.0D;
      int i4;
      if (i1 > 3)
      {
        i4 = paramArrayOfByte[j] - 48;
        switch (i4)
        {
        case 1:
          d4 = 1000.0D;
          break;
        case 2:
          d4 = 2000.0D;
          break;
        case 3:
          d4 = 3000.0D;
          break;
        case 4:
          d4 = 4000.0D;
          break;
        case 5:
          d4 = 5000.0D;
          break;
        case 6:
          d4 = 6000.0D;
          break;
        case 7:
          d4 = 7000.0D;
          break;
        case 8:
          d4 = 8000.0D;
          break;
        case 9:
          d4 = 9000.0D;
        }
        j++;
      }
      if (i1 > 2)
      {
        i4 = paramArrayOfByte[j] - 48;
        switch (i4)
        {
        case 1:
          d7 = 100.0D;
          break;
        case 2:
          d7 = 200.0D;
          break;
        case 3:
          d7 = 300.0D;
          break;
        case 4:
          d7 = 400.0D;
          break;
        case 5:
          d7 = 500.0D;
          break;
        case 6:
          d7 = 600.0D;
          break;
        case 7:
          d7 = 700.0D;
          break;
        case 8:
          d7 = 800.0D;
          break;
        case 9:
          d7 = 900.0D;
        }
        j++;
      }
      if (i1 > 1)
      {
        i4 = paramArrayOfByte[j] - 48;
        switch (i4)
        {
        case 1:
          d6 = 10.0D;
          break;
        case 2:
          d6 = 20.0D;
          break;
        case 3:
          d6 = 30.0D;
          break;
        case 4:
          d6 = 40.0D;
          break;
        case 5:
          d6 = 50.0D;
          break;
        case 6:
          d6 = 60.0D;
          break;
        case 7:
          d6 = 70.0D;
          break;
        case 8:
          d6 = 80.0D;
          break;
        case 9:
          d6 = 90.0D;
        }
        j++;
      }
      if (i1 > 0)
      {
        i4 = paramArrayOfByte[j] - 48;
        switch (i4)
        {
        case 1:
          d5 = 1.0D;
          break;
        case 2:
          d5 = 2.0D;
          break;
        case 3:
          d5 = 3.0D;
          break;
        case 4:
          d5 = 4.0D;
          break;
        case 5:
          d5 = 5.0D;
          break;
        case 6:
          d5 = 6.0D;
          break;
        case 7:
          d5 = 7.0D;
          break;
        case 8:
          d5 = 8.0D;
          break;
        case 9:
          d5 = 9.0D;
        }
      }
      if (i2 > 1)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d8 = 0.1000000014901161D;
          break;
        case 2:
          d8 = 0.2000000029802322D;
          break;
        case 3:
          d8 = 0.300000011920929D;
          break;
        case 4:
          d8 = 0.4000000059604645D;
          break;
        case 5:
          d8 = 0.5D;
          break;
        case 6:
          d8 = 0.6000000238418579D;
          break;
        case 7:
          d8 = 0.699999988079071D;
          break;
        case 8:
          d8 = 0.800000011920929D;
          break;
        case 9:
          d8 = 0.8999999761581421D;
        }
      }
      if (i2 > 2)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d9 = 0.009999999776482582D;
          break;
        case 2:
          d9 = 0.01999999955296516D;
          break;
        case 3:
          d9 = 0.02999999932944775D;
          break;
        case 4:
          d9 = 0.03999999910593033D;
          break;
        case 5:
          d9 = 0.0500000007450581D;
          break;
        case 6:
          d9 = 0.05999999865889549D;
          break;
        case 7:
          d9 = 0.07000000029802322D;
          break;
        case 8:
          d9 = 0.07999999821186066D;
          break;
        case 9:
          d9 = 0.09000000357627869D;
        }
      }
      if (i2 > 3)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d10 = 0.001000000047497451D;
          break;
        case 2:
          d10 = 0.002000000094994903D;
          break;
        case 3:
          d10 = 0.003000000026077032D;
          break;
        case 4:
          d10 = 0.004000000189989805D;
          break;
        case 5:
          d10 = 0.00499999988824129D;
          break;
        case 6:
          d10 = 0.006000000052154064D;
          break;
        case 7:
          d10 = 0.007000000216066837D;
          break;
        case 8:
          d10 = 0.00800000037997961D;
          break;
        case 9:
          d10 = 0.00899999961256981D;
        }
      }
      if (i2 > 4)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d11 = 9.999999747378752E-005D;
          break;
        case 2:
          d11 = 0.000199999994947575D;
          break;
        case 3:
          d11 = 0.0003000000142492354D;
          break;
        case 4:
          d11 = 0.0003999999898951501D;
          break;
        case 5:
          d11 = 0.0005000000237487257D;
          break;
        case 6:
          d11 = 0.0006000000284984708D;
          break;
        case 7:
          d11 = 0.00069999997504056D;
          break;
        case 8:
          d11 = 0.0007999999797903001D;
          break;
        case 9:
          d11 = 0.0008999999845400453D;
        }
      }
      if (i2 > 5)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d12 = 9.999999747378752E-006D;
          break;
        case 2:
          d12 = 1.99999994947575E-005D;
          break;
        case 3:
          d12 = 2.999999924213626E-005D;
          break;
        case 4:
          d12 = 3.999999898951501E-005D;
          break;
        case 5:
          d12 = 4.999999873689376E-005D;
          break;
        case 6:
          d12 = 5.999999848427251E-005D;
          break;
        case 7:
          d12 = 7.000000186963007E-005D;
          break;
        case 8:
          d12 = 7.999999797903001E-005D;
          break;
        case 9:
          d12 = 9.000000136438757E-005D;
        }
      }
      if (i2 > 6)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          d13 = 9.999999974752427E-007D;
          break;
        case 2:
          d13 = 1.999999994950485E-006D;
          break;
        case 3:
          d13 = 3.000000106112566E-006D;
          break;
        case 4:
          d13 = 3.999999989900971E-006D;
          break;
        case 5:
          d13 = 4.999999873689376E-006D;
          break;
        case 6:
          d13 = 6.000000212225132E-006D;
          break;
        case 7:
          d13 = 7.000000096013537E-006D;
          break;
        case 8:
          d13 = 7.999999979801942E-006D;
          break;
        case 9:
          d13 = 9.000000318337698E-006D;
        }
      }
      double d2 = d8 + d9 + d10 + d11 + d12 + d13;
      double d3 = d4 + d7 + d6 + d5;
      d1 = d3 + d2;
    }
    if (k != 0)
      return -d1;
    return d1;
  }

  public static float parseFloat(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = paramInt2;
    int j = paramInt1;
    int k = 0;
    for (int m = paramInt2 - 1; m > paramInt1 - 1; m--)
      if (paramArrayOfByte[m] == 46)
      {
        i = m;
        break;
      }
    m = i;
    int n = i;
    if (paramArrayOfByte[paramInt1] == 43)
    {
      m--;
      j++;
    }
    else if (paramArrayOfByte[paramInt1] == 45)
    {
      j++;
      k = 1;
    }
    int i1 = m - j;
    int i2 = paramInt2 - i;
    float f1;
    if (i1 > 4)
    {
      k = 0;
      int i3 = paramInt2 - paramInt1;
      byte[] arrayOfByte = new byte[i3];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, i3);
      f1 = Float.parseFloat(new String(arrayOfByte));
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
      float f13 = 0.0F;
      int i4;
      if (i1 > 3)
      {
        i4 = paramArrayOfByte[j] - 48;
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
        j++;
      }
      if (i1 > 2)
      {
        i4 = paramArrayOfByte[j] - 48;
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
        j++;
      }
      if (i1 > 1)
      {
        i4 = paramArrayOfByte[j] - 48;
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
        j++;
      }
      if (i1 > 0)
      {
        i4 = paramArrayOfByte[j] - 48;
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
      if (i2 > 1)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
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
      if (i2 > 2)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
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
      if (i2 > 3)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
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
      if (i2 > 4)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
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
      if (i2 > 5)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
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
      if (i2 > 6)
      {
        n++;
        i4 = paramArrayOfByte[n] - 48;
        switch (i4)
        {
        case 1:
          f13 = 1.0E-006F;
          break;
        case 2:
          f13 = 2.0E-006F;
          break;
        case 3:
          f13 = 3.E-006F;
          break;
        case 4:
          f13 = 4.0E-006F;
          break;
        case 5:
          f13 = 5.0E-006F;
          break;
        case 6:
          f13 = 6.E-006F;
          break;
        case 7:
          f13 = 7.E-006F;
          break;
        case 8:
          f13 = 8.0E-006F;
          break;
        case 9:
          f13 = 9.E-006F;
        }
      }
      float f2 = f8 + f9 + f10 + f11 + f12 + f13;
      float f3 = f4 + f7 + f6 + f5;
      f1 = f3 + f2;
    }
    if (k != 0)
      return -f1;
    return f1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.NumberUtils
 * JD-Core Version:    0.6.2
 */
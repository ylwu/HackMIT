package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CFFUtils
{
  public static byte[] createIndex(byte[][] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte == null)
      return new byte[] { 0, 0 };
    int i = paramArrayOfByte.length;
    if (i == 0)
      return new byte[] { 0, 0 };
    int[] arrayOfInt = new int[i + 1];
    arrayOfInt[0] = 1;
    for (int j = 1; j < i + 1; j++)
    {
      byte[] arrayOfByte1 = paramArrayOfByte[(j - 1)];
      if (arrayOfByte1 != null)
        arrayOfInt[j] = (arrayOfInt[(j - 1)] + arrayOfByte1.length);
      else
        arrayOfInt[j] = arrayOfInt[(j - 1)];
    }
    j = getOffsizeForMaxVal(arrayOfInt[i]);
    int k = 3 + j * arrayOfInt.length + arrayOfInt[i];
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(k);
    localByteArrayOutputStream.write(FontWriter.setNextUint16(i));
    localByteArrayOutputStream.write(FontWriter.setNextUint8(j));
    for (int i1 : arrayOfInt)
      localByteArrayOutputStream.write(FontWriter.setUintAsBytes(i1, j));
    for (byte[] arrayOfByte2 : paramArrayOfByte)
      if (arrayOfByte2 != null)
        localByteArrayOutputStream.write(arrayOfByte2);
    localByteArrayOutputStream.close();
    localByteArrayOutputStream.flush();
    return localByteArrayOutputStream.toByteArray();
  }

  private static byte getOffsizeForMaxVal(int paramInt)
  {
    byte b = 1;
    while (paramInt > 256)
    {
      b = (byte)(b + 1);
      paramInt /= 256;
    }
    return b;
  }

  static byte[] storeInteger(int paramInt)
  {
    byte[] arrayOfByte;
    if ((paramInt >= -107) && (paramInt <= 107))
    {
      arrayOfByte = new byte[] { (byte)(paramInt + 139) };
    }
    else if ((paramInt >= 108) && (paramInt <= 1131))
    {
      paramInt -= 108;
      arrayOfByte = new byte[] { (byte)(247 + paramInt / 256), (byte)(paramInt & 0xFF) };
    }
    else if ((paramInt >= -1131) && (paramInt <= -108))
    {
      paramInt += 108;
      arrayOfByte = new byte[] { (byte)(251 + paramInt / -256), (byte)(-paramInt & 0xFF) };
    }
    else if ((paramInt >= -32768) && (paramInt <= 32767))
    {
      arrayOfByte = new byte[] { 28, (byte)(paramInt / 256 & 0xFF), (byte)(paramInt & 0xFF) };
    }
    else
    {
      arrayOfByte = new byte[] { 29, (byte)(paramInt / 256 / 256 / 256 & 0xFF), (byte)(paramInt / 256 / 256 & 0xFF), (byte)(paramInt / 256 & 0xFF), (byte)(paramInt & 0xFF) };
    }
    return arrayOfByte;
  }

  static byte[] storeReal(double paramDouble)
  {
    String str = Double.toString(paramDouble);
    if (str.length() > 10)
      if (str.contains("E"))
      {
        String[] arrayOfString = str.split("E");
        str = str.substring(0, 10 - (arrayOfString[1].length() + 1)) + 'E' + arrayOfString[1];
      }
      else
      {
        str = str.substring(0, 10);
      }
    str = str + 'f';
    int i = str.length();
    if (i % 2 == 1)
      i++;
    i /= 2;
    byte[] arrayOfByte = new byte[1 + i];
    arrayOfByte[0] = 30;
    for (int j = 0; j < i; j++)
    {
      int k = j * 2;
      char c1 = str.charAt(k);
      char c2;
      if (k + 1 < str.length())
        c2 = str.charAt(k + 1);
      else
        c2 = c1;
      int m = getNibble(c1);
      int n = getNibble(c2);
      arrayOfByte[(j + 1)] = ((byte)((m & 0xF) << 4 | n & 0xF));
    }
    return arrayOfByte;
  }

  static byte[] storeCharstringType2Integer(int paramInt)
  {
    byte[] arrayOfByte;
    if ((paramInt >= -107) && (paramInt <= 107))
    {
      arrayOfByte = new byte[] { (byte)(paramInt + 139) };
    }
    else if ((paramInt >= 108) && (paramInt <= 1131))
    {
      paramInt -= 108;
      arrayOfByte = new byte[] { (byte)(247 + paramInt / 256), (byte)(paramInt & 0xFF) };
    }
    else if ((paramInt >= -1131) && (paramInt <= -108))
    {
      paramInt += 108;
      arrayOfByte = new byte[] { (byte)(251 + paramInt / -256), (byte)(-paramInt & 0xFF) };
    }
    else if (paramInt >= 0)
    {
      arrayOfByte = new byte[] { -1, (byte)(paramInt / 256 & 0xFF), (byte)(paramInt & 0xFF), 0, 0 };
    }
    else
    {
      int i = paramInt + 32768;
      arrayOfByte = new byte[] { -1, (byte)(0x80 | i / 256 & 0x7F), (byte)(i & 0xFF), 0, 0 };
    }
    return arrayOfByte;
  }

  private static byte getNibble(char paramChar)
  {
    switch (paramChar)
    {
    case '.':
      return 10;
    case 'E':
      return 11;
    case '-':
      return 14;
    case 'f':
      return 15;
    }
    return (byte)Integer.parseInt(String.valueOf(paramChar));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.CFFUtils
 * JD-Core Version:    0.6.2
 */
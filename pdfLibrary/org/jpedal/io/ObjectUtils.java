package org.jpedal.io;

import java.io.PrintStream;
import org.jpedal.objects.raw.DecodeParmsObject;
import org.jpedal.objects.raw.PdfObject;

public class ObjectUtils
{
  static byte[] checkEndObject(byte[] paramArrayOfByte)
  {
    long l1 = -1L;
    long l2 = -1L;
    int i = 0;
    for (int j = 0; j < paramArrayOfByte.length - 8; j++)
    {
      if ((i < 2) && (paramArrayOfByte[j] == 32) && (paramArrayOfByte[(j + 1)] == 111) && (paramArrayOfByte[(j + 2)] == 98) && (paramArrayOfByte[(j + 3)] == 106))
      {
        i++;
        l1 = j;
      }
      if ((i < 2) && (paramArrayOfByte[j] == 101) && (paramArrayOfByte[(j + 1)] == 110) && (paramArrayOfByte[(j + 2)] == 100) && (paramArrayOfByte[(j + 3)] == 115) && (paramArrayOfByte[(j + 4)] == 116) && (paramArrayOfByte[(j + 5)] == 114) && (paramArrayOfByte[(j + 6)] == 101) && (paramArrayOfByte[(j + 7)] == 97) && (paramArrayOfByte[(j + 8)] == 109))
        l2 = j + 9;
    }
    if ((l2 > 0L) && (l1 > l2))
    {
      byte[] arrayOfByte = new byte[(int)l2];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, (int)l2);
      paramArrayOfByte = arrayOfByte;
    }
    return paramArrayOfByte;
  }

  static byte[] readEscapedValue(int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    int i = 0;
    for (int j = paramInt2; j < paramInt1; j++)
      if ((paramArrayOfByte[j] == 92) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13))
      {
        i = 1;
        j = paramInt1;
      }
    byte[] arrayOfByte1;
    if (i == 0)
    {
      j = paramInt1 - paramInt2;
      if (j < 1)
        return new byte[0];
      arrayOfByte1 = new byte[j];
      System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte1, 0, j);
    }
    else
    {
      j = 0;
      int k = paramInt1 - paramInt2;
      arrayOfByte1 = new byte[k];
      for (int m = paramInt2; m < paramInt1; m++)
        if (paramArrayOfByte[m] == 92)
        {
          m++;
          if ((paramArrayOfByte[m] == 13) && (paramArrayOfByte[(m + 1)] != 92) && (arrayOfByte1[0] == -2) && (arrayOfByte1[1] == -1))
            m++;
          int n = paramArrayOfByte[m];
          if (n == 98)
          {
            arrayOfByte1[j] = 8;
          }
          else if (n == 110)
          {
            arrayOfByte1[j] = 10;
          }
          else if (n == 116)
          {
            arrayOfByte1[j] = 9;
          }
          else if (n == 114)
          {
            arrayOfByte1[j] = 13;
          }
          else if (n == 102)
          {
            arrayOfByte1[j] = 12;
          }
          else if (n == 92)
          {
            arrayOfByte1[j] = 92;
          }
          else if ((n > 47) && (n < 58))
          {
            StringBuilder localStringBuilder = new StringBuilder(3);
            int i1 = 0;
            for (int i2 = 0; i2 < 3; i2++)
              if ((paramArrayOfByte[m] == 92) || (paramArrayOfByte[m] == 41) || (paramArrayOfByte[m] < 48) || (paramArrayOfByte[m] > 57))
              {
                i2 = 3;
              }
              else
              {
                localStringBuilder.append((char)paramArrayOfByte[m]);
                if (paramArrayOfByte[m] > 55)
                  i1 = 1;
                m++;
              }
            m--;
            if (i1 != 0)
              arrayOfByte1[j] = ((byte)Integer.parseInt(localStringBuilder.toString()));
            else
              arrayOfByte1[j] = ((byte)Integer.parseInt(localStringBuilder.toString(), 8));
          }
          else if ((n == 13) || (n == 10))
          {
            j--;
          }
          else
          {
            arrayOfByte1[j] = n;
          }
          j++;
        }
        else if ((!paramBoolean) && ((paramArrayOfByte[m] == 13) || (paramArrayOfByte[m] == 10)))
        {
          arrayOfByte1[j] = 32;
          j++;
        }
        else
        {
          arrayOfByte1[j] = paramArrayOfByte[m];
          j++;
        }
      byte[] arrayOfByte2 = arrayOfByte1;
      arrayOfByte1 = new byte[j];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, j);
    }
    return arrayOfByte1;
  }

  public static PdfObject[] setupDecodeParms(PdfObject paramPdfObject, PdfFileReader paramPdfFileReader)
  {
    Object[] arrayOfObject = paramPdfObject.getObjectArray(1888135062);
    PdfObject[] arrayOfPdfObject;
    if (arrayOfObject != null)
    {
      int i = arrayOfObject.length;
      arrayOfPdfObject = new PdfObject[i];
      for (int j = 0; j < i; j++)
      {
        byte[] arrayOfByte = (byte[])arrayOfObject[j];
        if (arrayOfByte != null)
        {
          String str = new String(arrayOfByte);
          DecodeParmsObject localDecodeParmsObject = new DecodeParmsObject(str);
          if (arrayOfByte[0] == 60)
            localDecodeParmsObject.setStatus(2);
          else
            localDecodeParmsObject.setStatus(1);
          localDecodeParmsObject.setUnresolvedData(arrayOfByte, 1888135062);
          ObjectDecoder localObjectDecoder = new ObjectDecoder(paramPdfFileReader);
          localObjectDecoder.checkResolved(localDecodeParmsObject);
          arrayOfPdfObject[j] = localDecodeParmsObject;
        }
        else
        {
          arrayOfPdfObject[j] = null;
        }
      }
    }
    else
    {
      arrayOfPdfObject = new PdfObject[1];
      arrayOfPdfObject[0] = paramPdfObject.getDictionary(1888135062);
    }
    return arrayOfPdfObject;
  }

  static byte[] convertReturnsToSpaces(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    int i = 0;
    int j = paramArrayOfByte.length;
    for (int k = 0; k < j; k++)
      if ((paramArrayOfByte[k] == 13) && (paramArrayOfByte[(k + 1)] == 10))
      {
        k++;
        i++;
      }
    if (i > 0)
    {
      k = j - i;
      int m = 0;
      byte[] arrayOfByte = paramArrayOfByte;
      paramArrayOfByte = new byte[k];
      for (int n = 0; n < j; n++)
      {
        if ((arrayOfByte[n] == 13) && (n < j - 1) && (arrayOfByte[(n + 1)] == 10))
        {
          paramArrayOfByte[m] = 32;
          n++;
        }
        else
        {
          paramArrayOfByte[m] = arrayOfByte[n];
        }
        m++;
      }
    }
    return paramArrayOfByte;
  }

  static int handleUnknownType(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 - 1;
    for (int j = paramInt1; j < i; j++)
      if ((paramArrayOfByte[j] == 82) && (paramArrayOfByte[(j - 2)] == 48))
      {
        paramInt1 = j;
        j = i;
      }
      else if ((paramArrayOfByte[j] == 60) && (paramArrayOfByte[(j + 1)] == 60))
      {
        int k = 0;
        while (true)
        {
          if ((paramArrayOfByte[j] == 60) && (paramArrayOfByte[(j + 1)] == 60))
          {
            k++;
          }
          else if ((paramArrayOfByte[j] == 62) && (paramArrayOfByte[(j + 1)] == 62))
          {
            k--;
            if (paramArrayOfByte[(j + 2)] == 62)
              j++;
          }
          j++;
          if (k != 0)
            if (j >= i)
              break;
        }
        paramInt1 = j;
        j = i;
      }
      else if (paramArrayOfByte[j] == 47)
      {
        j = i;
      }
      else if ((paramArrayOfByte[j] == 62) && (paramArrayOfByte[(j + 1)] == 62))
      {
        paramInt1 = j - 1;
        j = i;
      }
      else if (paramArrayOfByte[j] == 40)
      {
        while ((j < i) && ((paramArrayOfByte[j] != 41) || (isEscaped(paramArrayOfByte, j))))
          j++;
        paramInt1 = j;
        j = i;
      }
    return paramInt1;
  }

  static boolean isEscaped(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt - 1;
    for (int j = 0; (i > -1) && (paramArrayOfByte[i] == 92); j++)
      i--;
    return (j & 0x1) == 1;
  }

  static int setDirectValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramInt1++;
    int i = paramInt1;
    while ((paramInt1 < paramArrayOfByte.length) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13))
      paramInt1++;
    paramPdfObject.setConstant(paramInt2, i, paramInt1 - i, paramArrayOfByte);
    return paramInt1;
  }

  static void showData(PdfObject paramPdfObject, int paramInt1, int paramInt2, byte[] paramArrayOfByte, String paramString)
  {
    System.out.println(new StringBuilder().append("\n\n").append(paramString).append(" ------------readDictionaryAsObject ref=").append(paramPdfObject.getObjectRefAsString()).append(" into ").append(paramPdfObject).append("-----------------\ni=").append(paramInt1).append("\nData=>>>>").toString());
    System.out.print(paramString);
    for (int i = paramInt1; i < paramInt2; i++)
    {
      System.out.print((char)paramArrayOfByte[i]);
      if (paramArrayOfByte[i] == 37)
      {
        while ((i < paramInt2) && (paramArrayOfByte[i] != 10) && (paramArrayOfByte[i] != 13))
          i++;
        while ((i < paramInt2) && ((paramArrayOfByte[i] == 9) || (paramArrayOfByte[i] == 10) || (paramArrayOfByte[i] == 13) || (paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 60)))
          i++;
      }
      if ((i > 5) && (paramArrayOfByte[(i - 5)] == 115) && (paramArrayOfByte[(i - 4)] == 116) && (paramArrayOfByte[(i - 3)] == 114) && (paramArrayOfByte[(i - 2)] == 101) && (paramArrayOfByte[(i - 1)] == 97) && (paramArrayOfByte[i] == 109))
        i = paramInt2;
      if ((i > 2) && (paramArrayOfByte[(i - 2)] == 66) && (paramArrayOfByte[(i - 1)] == 68) && (paramArrayOfByte[i] == 67))
        i = paramInt2;
    }
    System.out.println(new StringBuilder().append(paramString).append("\n<<<<-----------------------------------------------------\n").toString());
  }

  static String showMixedValuesAsString(Object[] paramArrayOfObject, String paramString)
  {
    if (paramArrayOfObject == null)
      return "null";
    paramString = new StringBuilder().append(paramString).append('[').toString();
    int i = paramArrayOfObject.length;
    for (int j = 0; j < i; j++)
      if (paramArrayOfObject[j] == null)
      {
        paramString = new StringBuilder().append(paramString).append("null ").toString();
      }
      else if ((paramArrayOfObject[j] instanceof byte[]))
      {
        paramString = new StringBuilder().append(paramString).append(new String((byte[])paramArrayOfObject[j])).toString();
        if (i - j > 1)
          paramString = new StringBuilder().append(paramString).append(" , ").toString();
      }
      else
      {
        paramString = new StringBuilder().append(showMixedValuesAsString((Object[])paramArrayOfObject[j], paramString)).append(']').toString();
        if (i - j > 1)
          paramString = new StringBuilder().append(paramString).append(" ,").toString();
      }
    return paramString;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ObjectUtils
 * JD-Core Version:    0.6.2
 */
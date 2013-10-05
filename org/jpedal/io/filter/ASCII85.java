package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class ASCII85 extends BaseFilter
  implements PdfFilter
{
  private static final long[] hex_indices = { 16777216L, 65536L, 256L, 1L };
  private static final long[] base_85_indices = { 52200625L, 614125L, 7225L, 85L, 1L };

  public ASCII85(PdfObject paramPdfObject)
  {
    super(paramPdfObject);
  }

  public byte[] decode(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int k = paramArrayOfByte.length;
    for (int m = 0; m < k; m++)
      if (paramArrayOfByte[m] == 122)
        i++;
      else if (paramArrayOfByte[m] == 10)
        j++;
    if ((j == 5) && (i == 0) && ((k - j) % 5 == 4))
      k++;
    m = 0;
    byte[] arrayOfByte1 = new byte[k - j + 1 + i * 3];
    for (int i2 = 0; i2 < k; i2++)
    {
      long l = 0L;
      int i1 = paramArrayOfByte[i2];
      while ((i1 == 10) || (i1 == 13))
      {
        i2++;
        if (i2 == k)
          i1 = 0;
        else
          i1 = paramArrayOfByte[i2];
      }
      int i3;
      if (i1 == 122)
      {
        for (i3 = 0; i3 < 4; i3++)
        {
          arrayOfByte1[m] = 0;
          m++;
        }
      }
      else if ((k - i2 > 4) && (i1 > 32) && (i1 < 118))
      {
        for (int n = 0; n < 5; n++)
        {
          if (i2 < paramArrayOfByte.length)
            i1 = paramArrayOfByte[i2];
          while ((i1 == 10) || (i1 == 13))
          {
            i2++;
            if (i2 == k)
              i1 = 0;
            else
              i1 = paramArrayOfByte[i2];
          }
          i2++;
          if (((i1 > 32) && (i1 < 118)) || (i1 == 126))
            l += (i1 - 33) * base_85_indices[n];
        }
        for (i3 = 0; i3 < 4; i3++)
        {
          arrayOfByte1[m] = ((byte)(int)(l / hex_indices[i3] & 0xFF));
          m++;
        }
        i2--;
      }
    }
    byte[] arrayOfByte2 = new byte[m];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, m);
    return arrayOfByte2;
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
  {
    this.bis = paramBufferedInputStream;
    this.streamCache = paramBufferedOutputStream;
    this.cachedObjects = paramMap;
    try
    {
      int j = 0;
      while (paramBufferedInputStream.available() > 0)
      {
        long l = 0L;
        int i = read(paramBufferedInputStream);
        int k;
        if (i == 122)
        {
          for (k = 0; k < 4; k++)
            paramBufferedOutputStream.write(0);
        }
        else if ((paramBufferedInputStream.available() >= 4) && (i > 32) && (i < 118))
        {
          l += (i - 33) * base_85_indices[0];
          for (k = 1; k < 5; k++)
          {
            i = read(paramBufferedInputStream);
            if (i == -1)
              i = 0;
            if (i == -1)
              j = 1;
            if (((i > 32) && (i < 118)) || (i == 126))
              l += (i - 33) * base_85_indices[k];
          }
          if (j == 0)
            for (k = 0; k < 4; k++)
            {
              int m = (byte)(int)(l / hex_indices[k] & 0xFF);
              paramBufferedOutputStream.write(m);
            }
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing Ascii85Decode filter ");
    }
  }

  private static int read(BufferedInputStream paramBufferedInputStream)
    throws IOException
  {
    for (int i = paramBufferedInputStream.read(); (i == 13) || (i == 10); i = paramBufferedInputStream.read());
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.ASCII85
 * JD-Core Version:    0.6.2
 */
package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class RunLength extends BaseFilter
  implements PdfFilter
{
  public RunLength(PdfObject paramPdfObject)
  {
    super(paramPdfObject);
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws Exception
  {
    int i = paramArrayOfByte.length;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(i);
    for (int m = 0; m < i; m++)
    {
      int j = paramArrayOfByte[m];
      if (j < 0)
        j = 256 + j;
      if (j == 128)
      {
        m = i;
      }
      else
      {
        int k;
        int n;
        if (j > 128)
        {
          m++;
          j = 257 - j;
          k = paramArrayOfByte[m];
          for (n = 0; n < j; n++)
            localByteArrayOutputStream.write(k);
        }
        else
        {
          m++;
          j++;
          for (n = 0; n < j; n++)
          {
            k = paramArrayOfByte[(m + n)];
            localByteArrayOutputStream.write(k);
          }
          m = m + j - 1;
        }
      }
    }
    localByteArrayOutputStream.close();
    paramArrayOfByte = localByteArrayOutputStream.toByteArray();
    return paramArrayOfByte;
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
  {
    this.bis = paramBufferedInputStream;
    this.streamCache = paramBufferedOutputStream;
    this.cachedObjects = paramMap;
    try
    {
      int i = paramBufferedInputStream.available();
      for (int n = 0; n < i; n++)
      {
        int k = paramBufferedInputStream.read();
        if (k >= 128)
          k -= 256;
        int j = k;
        if (j < 0)
          j = 256 + j;
        if (j == 128)
        {
          n = i;
        }
        else
        {
          int m;
          int i1;
          if (j > 128)
          {
            n++;
            j = 257 - j;
            m = paramBufferedInputStream.read();
            if (m >= 128)
              m -= 256;
            for (i1 = 0; i1 < j; i1++)
              paramBufferedOutputStream.write(m);
          }
          else
          {
            n++;
            j++;
            for (i1 = 0; i1 < j; i1++)
            {
              m = paramBufferedInputStream.read();
              if (m >= 128)
                m -= 256;
              paramBufferedOutputStream.write(m);
            }
            n = n + j - 1;
          }
        }
      }
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("IO exception in RunLength " + localIOException);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.RunLength
 * JD-Core Version:    0.6.2
 */
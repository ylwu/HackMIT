package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class ASCIIHex extends BaseFilter
  implements PdfFilter
{
  public ASCIIHex(PdfObject paramPdfObject)
  {
    super(paramPdfObject);
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws IOException
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    BufferedReader localBufferedReader = null;
    ByteArrayInputStream localByteArrayInputStream = null;
    try
    {
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localBufferedReader = new BufferedReader(new InputStreamReader(localByteArrayInputStream));
      if (localBufferedReader != null)
        while (true)
        {
          String str = localBufferedReader.readLine();
          if (str == null)
            break;
          localStringBuilder2.append(str);
        }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading ASCII stream ").toString());
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
        localByteArrayInputStream.close();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
      }
    int i = localStringBuilder2.length();
    int j = 0;
    int k = 0;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(paramArrayOfByte.length);
    while (true)
    {
      char c = localStringBuilder2.charAt(j);
      if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')))
      {
        localStringBuilder1.append(c);
        if (k == 1)
        {
          localByteArrayOutputStream.write(Integer.valueOf(localStringBuilder1.toString(), 16).intValue());
          k = 0;
          localStringBuilder1 = new StringBuilder();
        }
        else
        {
          k++;
        }
      }
      if (c != '>')
      {
        j++;
        if (j == i)
          break;
      }
    }
    if (k == 1)
    {
      localStringBuilder1.append('0');
      localByteArrayOutputStream.write(Integer.valueOf(localStringBuilder1.toString(), 16).intValue());
    }
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
  {
    this.bis = paramBufferedInputStream;
    this.streamCache = paramBufferedOutputStream;
    this.cachedObjects = paramMap;
    try
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i = paramBufferedInputStream.available();
      for (int j = 0; j < i; j++)
      {
        for (char c = (char)paramBufferedInputStream.read(); c == '\n'; c = (char)paramBufferedInputStream.read());
        if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')))
        {
          localStringBuffer.append(c);
          if (i == 1)
          {
            paramBufferedOutputStream.write(Integer.valueOf(localStringBuffer.toString(), 16).intValue());
            i = 0;
            localStringBuffer = new StringBuffer();
          }
          else
          {
            i++;
          }
        }
        if (c == '>')
          break;
      }
      if (i == 1)
      {
        localStringBuffer.append('0');
        paramBufferedOutputStream.write(Integer.valueOf(localStringBuffer.toString(), 16).intValue());
      }
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("IO exception in RunLength ").append(localIOException).toString());
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.ASCIIHex
 * JD-Core Version:    0.6.2
 */
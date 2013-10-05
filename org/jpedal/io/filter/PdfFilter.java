package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Map;

public abstract interface PdfFilter
{
  public abstract byte[] decode(byte[] paramArrayOfByte)
    throws Exception;

  public abstract void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
    throws Exception;

  public abstract boolean hasError();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.PdfFilter
 * JD-Core Version:    0.6.2
 */
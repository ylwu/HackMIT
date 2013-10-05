package org.jpedal.fonts.objects;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.jpedal.utils.LogWriter;

public class FontData
{
  private byte[] fontData;
  private boolean isInMemory = false;
  private int fullLength = 0;
  private int offset = 0;
  public static int maxSizeAllowedInMemory = -1;
  private int blockSize = 8192;
  private RandomAccessFile fontFile = null;

  public FontData(String paramString)
  {
    try
    {
      this.fontFile = new RandomAccessFile(paramString, "r");
      this.fullLength = ((int)this.fontFile.length());
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    if (this.fullLength < maxSizeAllowedInMemory)
    {
      this.blockSize = maxSizeAllowedInMemory;
      adjustForCache(0);
      this.isInMemory = true;
    }
  }

  public byte getByte(int paramInt)
  {
    if (!this.isInMemory)
      paramInt = adjustForCache(paramInt);
    if (paramInt >= this.fontData.length)
      return 0;
    return this.fontData[paramInt];
  }

  private int adjustForCache(int paramInt)
  {
    if ((this.fontData == null) || (paramInt < this.offset) || (paramInt >= this.offset + this.blockSize - 1))
    {
      try
      {
        this.fontFile.seek(paramInt);
        this.fontData = new byte[this.blockSize];
        this.fontFile.read(this.fontData);
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
      this.offset = paramInt;
    }
    return paramInt - this.offset;
  }

  private int adjustForCache(int paramInt1, int paramInt2)
  {
    try
    {
      this.fontFile.seek(paramInt1);
      this.fontData = new byte[paramInt2];
      this.fontFile.read(this.fontData);
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
    this.offset = paramInt1;
    return paramInt1 - this.offset;
  }

  public byte[] getBytes(int paramInt1, int paramInt2)
  {
    if (!this.isInMemory)
      paramInt1 = adjustForCache(paramInt1, paramInt2 + 1);
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(this.fontData, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }

  public int length()
  {
    if (this.isInMemory)
      return this.fontData.length;
    return this.fullLength;
  }

  public void close()
  {
    if (this.fontFile != null)
      try
      {
        this.fontFile.close();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.objects.FontData
 * JD-Core Version:    0.6.2
 */
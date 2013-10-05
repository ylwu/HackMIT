package org.jpedal.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.jpedal.utils.LogWriter;

public class RandomAccessFileChannelBuffer
  implements RandomAccessBuffer
{
  private long pointer;
  private int length = 0;
  private ByteBuffer mb;

  public RandomAccessFileChannelBuffer(InputStream paramInputStream)
  {
    try
    {
      this.length = paramInputStream.available();
      this.mb = ByteBuffer.allocate(this.length);
      byte[] arrayOfByte = new byte[4096];
      int i;
      while ((i = paramInputStream.read(arrayOfByte)) != -1)
        if (i > 0)
          for (int j = 0; j < i; j++)
            this.mb.put(arrayOfByte[j]);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
  }

  public long getFilePointer()
    throws IOException
  {
    return this.pointer;
  }

  public void seek(long paramLong)
    throws IOException
  {
    if (checkPos(paramLong))
      this.pointer = paramLong;
    else
      throw new IOException("Position out of bounds");
  }

  public void close()
    throws IOException
  {
    if (this.mb != null)
      this.mb = null;
    this.pointer = -1L;
  }

  public void finalize()
  {
    try
    {
      super.finalize();
    }
    catch (Throwable localThrowable)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localThrowable.getMessage()).toString());
    }
    try
    {
      close();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
  }

  public long length()
    throws IOException
  {
    if (this.mb != null)
      return this.length;
    throw new IOException("Data buffer not initialized.");
  }

  public int read()
    throws IOException
  {
    if (checkPos(this.pointer))
    {
      this.mb.position((int)this.pointer);
      this.pointer += 1L;
      return this.mb.get();
    }
    return -1;
  }

  private int peek()
    throws IOException
  {
    if (checkPos(this.pointer))
    {
      this.mb.position((int)this.pointer);
      return this.mb.get();
    }
    return -1;
  }

  public String readLine()
    throws IOException
  {
    if (this.pointer >= this.length - 1)
      return null;
    StringBuilder localStringBuilder = new StringBuilder();
    int i;
    while ((i = read()) >= 0)
    {
      if ((i == 10) || (i == 13))
      {
        if (((peek() != 10) && (peek() != 13)) || (peek() == i))
          break;
        read();
        break;
      }
      localStringBuilder.append((char)i);
    }
    return localStringBuilder.toString();
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.mb == null)
      throw new IOException("Data buffer not initialized.");
    if ((this.pointer < 0L) || (this.pointer >= this.length))
      return -1;
    int i = this.length - (int)this.pointer;
    if (i > paramArrayOfByte.length)
      i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      this.mb.position((int)this.pointer);
      this.pointer += 1L;
      paramArrayOfByte[j] = this.mb.get();
    }
    return i;
  }

  private boolean checkPos(long paramLong)
    throws IOException
  {
    return (paramLong >= 0L) && (paramLong < length());
  }

  public byte[] getPdfBuffer()
  {
    byte[] arrayOfByte = new byte[this.length];
    this.mb.position(0);
    this.mb.get(arrayOfByte);
    return arrayOfByte;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.RandomAccessFileChannelBuffer
 * JD-Core Version:    0.6.2
 */
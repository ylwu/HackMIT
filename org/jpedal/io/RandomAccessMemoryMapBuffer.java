package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import org.jpedal.utils.LogWriter;

public class RandomAccessMemoryMapBuffer
  implements RandomAccessBuffer
{
  private long pointer = -1L;
  private int length = 0;
  private File file;
  private RandomAccessFile buf;

  public RandomAccessMemoryMapBuffer(InputStream paramInputStream)
  {
    FileOutputStream localFileOutputStream = null;
    BufferedInputStream localBufferedInputStream = null;
    try
    {
      this.file = File.createTempFile("page", ".bin", new File(ObjectStore.temp_dir));
      localFileOutputStream = new FileOutputStream(this.file);
      localBufferedInputStream = new BufferedInputStream(paramInputStream);
      byte[] arrayOfByte = new byte[65535];
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte)) != -1)
      {
        localFileOutputStream.write(arrayOfByte, 0, i);
        this.length += i;
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
    }
    try
    {
      if (localFileOutputStream != null)
        localFileOutputStream.close();
      if (localBufferedInputStream != null)
        localBufferedInputStream.close();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" closing files").toString());
    }
    try
    {
      init();
    }
    catch (Exception localException3)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException3.getMessage()).toString());
    }
  }

  private void init()
    throws Exception
  {
    this.buf = new RandomAccessFile(this.file, "r");
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
    if (this.buf != null)
    {
      this.buf.close();
      this.buf = null;
    }
    this.pointer = -1L;
    if ((this.file != null) && (this.file.exists()))
      this.file.delete();
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
    if (this.buf != null)
      return this.length;
    throw new IOException("Data buffer not initialized.");
  }

  public int read()
    throws IOException
  {
    if (checkPos(this.pointer))
    {
      this.buf.seek(this.pointer++);
      return b2i(this.buf.readByte());
    }
    return -1;
  }

  private int peek()
    throws IOException
  {
    if (checkPos(this.pointer))
    {
      this.buf.seek(this.pointer++);
      return b2i(this.buf.readByte());
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
    if (this.buf == null)
      throw new IOException("Data buffer not initialized.");
    if ((this.pointer < 0L) || (this.pointer >= this.length))
      return -1;
    int i = this.length - (int)this.pointer;
    if (i > paramArrayOfByte.length)
      i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      this.buf.seek(this.pointer++);
      paramArrayOfByte[j] = this.buf.readByte();
    }
    return i;
  }

  private static int b2i(byte paramByte)
  {
    if (paramByte >= 0)
      return paramByte;
    return 256 + paramByte;
  }

  private boolean checkPos(long paramLong)
    throws IOException
  {
    return (paramLong >= 0L) && (paramLong < length());
  }

  public byte[] getPdfBuffer()
  {
    byte[] arrayOfByte = new byte[this.length];
    try
    {
      this.buf.seek(0L);
      this.buf.read(arrayOfByte);
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
    return arrayOfByte;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.RandomAccessMemoryMapBuffer
 * JD-Core Version:    0.6.2
 */
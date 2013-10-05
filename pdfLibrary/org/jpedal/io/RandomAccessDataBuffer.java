package org.jpedal.io;

import java.io.IOException;

public class RandomAccessDataBuffer
  implements RandomAccessBuffer
{
  private byte[] data;
  private long pointer;

  public RandomAccessDataBuffer(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
    this.pointer = -1L;
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
    this.data = null;
    this.pointer = -1L;
  }

  public long length()
    throws IOException
  {
    if (this.data != null)
      return this.data.length;
    throw new IOException("Data buffer not initialized.");
  }

  public int read()
    throws IOException
  {
    if (checkPos(this.pointer))
      return b2i(this.data[((int)this.pointer++)]);
    return -1;
  }

  private int peek()
    throws IOException
  {
    if (checkPos(this.pointer))
      return b2i(this.data[((int)this.pointer)]);
    return -1;
  }

  public String readLine()
    throws IOException
  {
    if (this.pointer >= this.data.length - 1)
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
    if (this.data == null)
      throw new IOException("Data buffer not initialized.");
    if ((this.pointer < 0L) || (this.pointer >= this.data.length))
      return -1;
    int i = Math.min(paramArrayOfByte.length, this.data.length - (int)this.pointer);
    for (int j = 0; j < i; j++)
      paramArrayOfByte[j] = this.data[((int)this.pointer++)];
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
    return this.data;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.RandomAccessDataBuffer
 * JD-Core Version:    0.6.2
 */
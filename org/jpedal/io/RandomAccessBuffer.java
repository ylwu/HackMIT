package org.jpedal.io;

import java.io.IOException;

public abstract interface RandomAccessBuffer
{
  public abstract long getFilePointer()
    throws IOException;

  public abstract void seek(long paramLong)
    throws IOException;

  public abstract int read()
    throws IOException;

  public abstract String readLine()
    throws IOException;

  public abstract long length()
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract int read(byte[] paramArrayOfByte)
    throws IOException;

  public abstract byte[] getPdfBuffer();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.RandomAccessBuffer
 * JD-Core Version:    0.6.2
 */
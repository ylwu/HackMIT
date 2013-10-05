package org.jpedal.jbig2.io;

public class StreamReader
{
  private final byte[] data;
  private int bitPointer = 7;
  private int bytePointer = 0;

  public StreamReader(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
  }

  public short readByte()
  {
    short s = 0;
    if (this.data.length > this.bytePointer)
      s = (short)(this.data[(this.bytePointer++)] & 0xFF);
    return s;
  }

  public void readByte(short[] paramArrayOfShort)
  {
    for (int i = 0; i < paramArrayOfShort.length; i++)
      paramArrayOfShort[i] = ((short)(this.data[(this.bytePointer++)] & 0xFF));
  }

  public int readBit()
  {
    int i = readByte();
    int j = (short)(1 << this.bitPointer);
    int k = (i & j) >> this.bitPointer;
    this.bitPointer -= 1;
    if (this.bitPointer == -1)
      this.bitPointer = 7;
    else
      movePointer(-1);
    return k;
  }

  public int readBits(int paramInt)
  {
    int i = 0;
    for (int j = 0; j < paramInt; j++)
      i = i << 1 | readBit();
    return i;
  }

  public void movePointer(int paramInt)
  {
    this.bytePointer += paramInt;
  }

  public void consumeRemainingBits()
  {
    if (this.bitPointer != 7)
      readBits(this.bitPointer + 1);
  }

  public boolean bytesRemaining()
  {
    return this.bytePointer != this.data.length;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.io.StreamReader
 * JD-Core Version:    0.6.2
 */
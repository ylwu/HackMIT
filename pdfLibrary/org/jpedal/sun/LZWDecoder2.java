package org.jpedal.sun;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.jpedal.utils.LogWriter;

public class LZWDecoder2
{
  BufferedInputStream bis = null;
  byte[][] stringTable;
  byte[] data = null;
  OutputStream uncompData;
  int tableIndex;
  int bitsToGet = 9;
  int bytePointer;
  int nextData = 0;
  int nextBits = 0;
  int[] andTable = { 511, 1023, 2047, 4095 };

  public void decode(byte[] paramArrayOfByte, BufferedOutputStream paramBufferedOutputStream, BufferedInputStream paramBufferedInputStream)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte[0] == 0) && (paramArrayOfByte[1] == 1))
      throw new RuntimeException("LZW flavour not supported.");
    initializeStringTable();
    this.bis = paramBufferedInputStream;
    this.data = paramArrayOfByte;
    this.uncompData = paramBufferedOutputStream;
    this.bytePointer = 0;
    this.nextData = 0;
    this.nextBits = 0;
    int j = 0;
    int i;
    while ((i = getNextCode()) != 257)
      if (i == 256)
      {
        initializeStringTable();
        i = getNextCode();
        if (i == 257)
          break;
        writeString(this.stringTable[i]);
        j = i;
      }
      else
      {
        byte[] arrayOfByte;
        if (i < this.tableIndex)
        {
          arrayOfByte = this.stringTable[i];
          writeString(arrayOfByte);
          addStringToTable(this.stringTable[j], arrayOfByte[0]);
          j = i;
        }
        else
        {
          arrayOfByte = this.stringTable[j];
          arrayOfByte = composeString(arrayOfByte, arrayOfByte[0]);
          writeString(arrayOfByte);
          addStringToTable(arrayOfByte);
          j = i;
        }
      }
  }

  public void initializeStringTable()
  {
    this.stringTable = new byte[8192][];
    for (int i = 0; i < 256; i++)
    {
      this.stringTable[i] = new byte[1];
      this.stringTable[i][0] = ((byte)i);
    }
    this.tableIndex = 258;
    this.bitsToGet = 9;
  }

  public void writeString(byte[] paramArrayOfByte)
  {
    try
    {
      this.uncompData.write(paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      LogWriter.writeLog("Exception " + localIOException + " with LZW decoder");
    }
  }

  public void addStringToTable(byte[] paramArrayOfByte, byte paramByte)
  {
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[i + 1];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, i);
    arrayOfByte[i] = paramByte;
    this.stringTable[(this.tableIndex++)] = arrayOfByte;
    if (this.tableIndex == 511)
      this.bitsToGet = 10;
    else if (this.tableIndex == 1023)
      this.bitsToGet = 11;
    else if (this.tableIndex == 2047)
      this.bitsToGet = 12;
  }

  public void addStringToTable(byte[] paramArrayOfByte)
  {
    this.stringTable[(this.tableIndex++)] = paramArrayOfByte;
    if (this.tableIndex == 511)
      this.bitsToGet = 10;
    else if (this.tableIndex == 1023)
      this.bitsToGet = 11;
    else if (this.tableIndex == 2047)
      this.bitsToGet = 12;
  }

  public static byte[] composeString(byte[] paramArrayOfByte, byte paramByte)
  {
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[i + 1];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, i);
    arrayOfByte[i] = paramByte;
    return arrayOfByte;
  }

  public int getNextCode()
  {
    try
    {
      int i = this.bis.read();
      if (this.data == null)
        this.bytePointer += 1;
      this.nextData = (this.nextData << 8 | i);
      this.nextBits += 8;
      if (this.nextBits < this.bitsToGet)
      {
        i = this.bis.read();
        if (this.data == null)
          this.bytePointer += 1;
        this.nextData = (this.nextData << 8 | i);
        this.nextBits += 8;
      }
      int j = this.nextData >> this.nextBits - this.bitsToGet & this.andTable[(this.bitsToGet - 9)];
      this.nextBits -= this.bitsToGet;
      return j;
    }
    catch (Exception localException)
    {
    }
    return 257;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.sun.LZWDecoder2
 * JD-Core Version:    0.6.2
 */
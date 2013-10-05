package org.jpedal.sun;

public class TIFFLZWDecoder
{
  int[] andTable = { 511, 1023, 2047, 4095 };
  int nextBits = 0;
  int nextData = 0;
  int tableIndex;
  int bitsToGet = 9;
  int bytePointer;
  int dstIndex;
  byte[][] stringTable;
  byte[] data = null;
  byte[] uncompData;

  public int getNextCode()
  {
    try
    {
      this.nextData = (this.nextData << 8 | this.data[(this.bytePointer++)] & 0xFF);
      this.nextBits += 8;
      if (this.nextBits < this.bitsToGet)
      {
        this.nextData = (this.nextData << 8 | this.data[(this.bytePointer++)] & 0xFF);
        this.nextBits += 8;
      }
      int i = this.nextData >> this.nextBits - this.bitsToGet & this.andTable[(this.bitsToGet - 9)];
      this.nextBits -= this.bitsToGet;
      return i;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
    return 257;
  }

  public static byte[] composeString(byte[] paramArrayOfByte, byte paramByte)
  {
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[i + 1];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, i);
    arrayOfByte[i] = paramByte;
    return arrayOfByte;
  }

  public TIFFLZWDecoder(int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public byte[] decode(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    if ((paramArrayOfByte1[0] == 0) && (paramArrayOfByte1[1] == 1))
      throw new UnsupportedOperationException(JaiI18N.getString("TIFFLZWDecoder0"));
    initializeStringTable();
    this.data = paramArrayOfByte1;
    this.uncompData = paramArrayOfByte2;
    this.bytePointer = 0;
    this.dstIndex = 0;
    this.nextData = 0;
    this.nextBits = 0;
    int j = 0;
    int i;
    while (((i = getNextCode()) != 257) && (this.dstIndex < paramArrayOfByte2.length))
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
    return paramArrayOfByte2;
  }

  public void initializeStringTable()
  {
    this.stringTable = new byte[4096][];
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
    for (int k : paramArrayOfByte)
      this.uncompData[(this.dstIndex++)] = k;
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
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.sun.TIFFLZWDecoder
 * JD-Core Version:    0.6.2
 */
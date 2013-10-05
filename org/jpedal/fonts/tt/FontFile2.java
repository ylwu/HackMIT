package org.jpedal.fonts.tt;

import java.io.Serializable;
import java.util.ArrayList;
import org.jpedal.fonts.objects.FontData;

public class FontFile2
  implements Serializable
{
  private static final long serialVersionUID = -3097990864237320960L;
  public static final int HEAD = 0;
  public static final int MAXP = 1;
  public static final int CMAP = 2;
  public static final int LOCA = 3;
  public static final int GLYF = 4;
  public static final int HHEA = 5;
  public static final int HMTX = 6;
  public static final int NAME = 7;
  public static final int POST = 8;
  public static final int CVT = 9;
  public static final int FPGM = 10;
  public static final int HDMX = 11;
  public static final int KERN = 12;
  public static final int OS2 = 13;
  public static final int PREP = 14;
  public static final int DSIG = 15;
  public static final int CFF = 16;
  public static final int GSUB = 17;
  public static final int BASE = 18;
  public static final int EBDT = 19;
  public static final int EBLC = 20;
  public static final int GASP = 21;
  public static final int VHEA = 22;
  public static final int VMTX = 23;
  public static final int GDEF = 24;
  public static final int JSTF = 25;
  public static final int LTSH = 26;
  public static final int PCLT = 27;
  public static final int VDMX = 28;
  public static final int BSLN = 29;
  public static final int MORT = 30;
  public static final int FDSC = 31;
  public static final int FFTM = 32;
  public static final int GPOS = 33;
  public static final int FEAT = 34;
  public static final int JUST = 35;
  public static final int PROP = 36;
  public static final int LCCL = 37;
  public static final int Zapf = 38;
  protected int tableCount = 39;
  protected int[][] checksums;
  protected int[][] tables;
  protected int[][] tableLength;
  private FontData fontDataAsObject = null;
  private byte[] fontDataAsArray = null;
  private boolean useArray = true;
  protected ArrayList tableList = new ArrayList(32);
  private int pointer = 0;
  public static final int OPENTYPE = 1;
  public static final int TRUETYPE = 2;
  public static final int TTC = 3;
  public static final int PS = 10;
  public static final int TTF = 11;
  protected int subType = 10;
  protected int type = 2;
  public int currentFontID = 0;
  private int fontCount = 1;
  protected int numTables = 11;
  protected int searchRange = 128;
  protected int entrySelector = 3;
  protected int rangeShift = 48;

  public FontFile2(FontData paramFontData)
  {
    this.useArray = false;
    this.fontDataAsObject = paramFontData;
    readHeader();
  }

  public FontFile2(byte[] paramArrayOfByte)
  {
    this.useArray = true;
    this.fontDataAsArray = paramArrayOfByte;
    readHeader();
  }

  public FontFile2(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    this.useArray = true;
    this.fontDataAsArray = paramArrayOfByte;
    if (!paramBoolean)
      readHeader();
  }

  public FontFile2()
  {
  }

  public void setSelectedFontIndex(int paramInt)
  {
    if (paramInt < this.fontCount)
      this.currentFontID = paramInt;
  }

  private final void readHeader()
  {
    int i = getNextUint32();
    if (i == 1330926671)
      this.type = 1;
    else if (i == 1953784678)
      this.type = 3;
    if (this.type == 3)
    {
      getNextUint32();
      this.fontCount = getNextUint32();
      this.checksums = new int[this.tableCount][this.fontCount];
      this.tables = new int[this.tableCount][this.fontCount];
      this.tableLength = new int[this.tableCount][this.fontCount];
      int[] arrayOfInt = new int[this.fontCount];
      for (int j = 0; j < this.fontCount; j++)
      {
        this.currentFontID = j;
        int k = getNextUint32();
        arrayOfInt[j] = k;
      }
      for (j = 0; j < this.fontCount; j++)
      {
        this.currentFontID = j;
        this.pointer = arrayOfInt[j];
        getNextUint32();
        readTablesForFont();
      }
      this.currentFontID = 0;
    }
    else
    {
      this.checksums = new int[this.tableCount][1];
      this.tables = new int[this.tableCount][1];
      this.tableLength = new int[this.tableCount][1];
      readTablesForFont();
    }
  }

  private void readTablesForFont()
  {
    this.numTables = getNextUint16();
    this.searchRange = getNextUint16();
    this.entrySelector = getNextUint16();
    this.rangeShift = getNextUint16();
    for (int n = 0; n < this.numTables; n++)
    {
      String str = getNextUint32AsTag();
      int i = getNextUint32();
      int j = getNextUint32();
      int k = getNextUint32();
      this.tableList.add(str);
      int m = getTableID(str);
      if (m != -1)
      {
        this.checksums[m][this.currentFontID] = i;
        this.tables[m][this.currentFontID] = j;
        this.tableLength[m][this.currentFontID] = k;
      }
    }
  }

  protected static int getTableID(String paramString)
  {
    int i = -1;
    if (paramString.equals("maxp"))
      i = 1;
    else if (paramString.equals("head"))
      i = 0;
    else if (paramString.equals("cmap"))
      i = 2;
    else if (paramString.equals("loca"))
      i = 3;
    else if (paramString.equals("glyf"))
      i = 4;
    else if (paramString.equals("hhea"))
      i = 5;
    else if (paramString.equals("hmtx"))
      i = 6;
    else if (paramString.equals("name"))
      i = 7;
    else if (paramString.equals("post"))
      i = 8;
    else if (paramString.equals("cvt "))
      i = 9;
    else if (paramString.equals("fpgm"))
      i = 10;
    else if (paramString.equals("hdmx"))
      i = 11;
    else if (paramString.equals("kern"))
      i = 12;
    else if (paramString.equals("OS/2"))
      i = 13;
    else if (paramString.equals("prep"))
      i = 14;
    else if (paramString.equals("DSIG"))
      i = 15;
    else if (paramString.equals("BASE"))
      i = 18;
    else if (paramString.equals("CFF "))
      i = 16;
    else if (paramString.equals("GSUB"))
      i = 17;
    else if (paramString.equals("EBDT"))
      i = 19;
    else if (paramString.equals("EBLC"))
      i = 20;
    else if (paramString.equals("gasp"))
      i = 21;
    else if (paramString.equals("vhea"))
      i = 22;
    else if (paramString.equals("vmtx"))
      i = 23;
    else if (paramString.equals("GDEF"))
      i = 24;
    else if (paramString.equals("JSTF"))
      i = 25;
    else if (paramString.equals("LTSH"))
      i = 26;
    else if (paramString.equals("PCLT"))
      i = 27;
    else if (paramString.equals("VDMX"))
      i = 28;
    else if (paramString.equals("mort"))
      i = 30;
    else if (paramString.equals("bsln"))
      i = 29;
    else if (paramString.equals("fdsc"))
      i = 31;
    else if (paramString.equals("FFTM"))
      i = 32;
    else if (paramString.equals("GPOS"))
      i = 33;
    else if (paramString.equals("feat"))
      i = 34;
    else if (paramString.equals("just"))
      i = 35;
    else if (paramString.equals("prop"))
      i = 36;
    else if (paramString.equals("LCCL"))
      i = 37;
    else if (paramString.equals("Zapf"))
      i = 38;
    return i;
  }

  public int selectTable(int paramInt)
  {
    this.pointer = this.tables[paramInt][this.currentFontID];
    return this.pointer;
  }

  public int getTableSize(int paramInt)
  {
    return this.tableLength[paramInt][this.currentFontID];
  }

  public int getTableStart(int paramInt)
  {
    return this.tables[paramInt][this.currentFontID];
  }

  public final int getNextUint32()
  {
    int i = 0;
    for (int k = 0; k < 4; k++)
    {
      int j;
      if (this.useArray)
      {
        if (this.pointer < this.fontDataAsArray.length)
          j = this.fontDataAsArray[this.pointer] & 0xFF;
        else
          j = 0;
      }
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (3 - k));
      this.pointer += 1;
    }
    return i;
  }

  public final int getNextUint64()
  {
    int i = 0;
    for (int k = 0; k < 8; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer];
      else
        j = this.fontDataAsObject.getByte(this.pointer);
      if (j < 0)
        j = 256 + j;
      i += (j << 8 * (7 - k));
      this.pointer += 1;
    }
    return i;
  }

  public final void setPointer(int paramInt)
  {
    this.pointer = paramInt;
  }

  public final int getOffset(int paramInt)
  {
    return this.tableLength[paramInt][this.currentFontID];
  }

  public final int getTable(int paramInt)
  {
    return this.tables[paramInt][this.currentFontID];
  }

  public final int getPointer()
  {
    return this.pointer;
  }

  public final String getNextUint32AsTag()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < 4; i++)
    {
      char c;
      if (this.useArray)
        c = (char)this.fontDataAsArray[this.pointer];
      else
        c = (char)this.fontDataAsObject.getByte(this.pointer);
      localStringBuilder.append(c);
      this.pointer += 1;
    }
    return localStringBuilder.toString();
  }

  public final int getNextUint16()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return i;
  }

  public final short getShort()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer];
      else
        j = this.fontDataAsObject.getByte(this.pointer);
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return (short)i;
  }

  public final int getNextUint8()
  {
    int i;
    if (this.useArray)
      i = this.fontDataAsArray[this.pointer] & 0xFF;
    else
      i = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
    this.pointer += 1;
    return i;
  }

  public final int getNextint8()
  {
    int i;
    if (this.useArray)
      i = this.fontDataAsArray[this.pointer];
    else
      i = this.fontDataAsObject.getByte(this.pointer);
    this.pointer += 1;
    return i;
  }

  public void skip(int paramInt)
  {
    this.pointer += paramInt;
  }

  public short getFWord()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return (short)i;
  }

  public short getNextInt16()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return (short)i;
  }

  public short getNextSignedInt16()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return (short)i;
  }

  public short readUFWord()
  {
    int i = 0;
    for (int k = 0; k < 2; k++)
    {
      int j;
      if (this.useArray)
        j = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        j = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      i += (j << 8 * (1 - k));
      this.pointer += 1;
    }
    return (short)i;
  }

  public float getFixed()
  {
    int i;
    if (this.useArray)
      i = (this.fontDataAsArray[this.pointer] & 0xFF) * 256 + (this.fontDataAsArray[(this.pointer + 1)] & 0xFF);
    else
      i = (this.fontDataAsObject.getByte(this.pointer) & 0xFF) * 256 + (this.fontDataAsObject.getByte(this.pointer + 1) & 0xFF);
    if (i > 32768)
      i -= 65536;
    this.pointer += 2;
    int j;
    if (this.useArray)
      j = (this.fontDataAsArray[this.pointer] & 0xFF) * 256 + (this.fontDataAsArray[(this.pointer + 1)] & 0xFF);
    else
      j = (this.fontDataAsObject.getByte(this.pointer) & 0xFF) * 256 + (this.fontDataAsObject.getByte(this.pointer + 1) & 0xFF);
    this.pointer += 2;
    return i + j / 65536.0F;
  }

  public String getString()
  {
    if ((this.useArray) && (this.pointer == this.fontDataAsArray.length))
      return "";
    int i;
    if (this.useArray)
      i = this.fontDataAsArray[this.pointer] & 0xFF;
    else
      i = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
    char[] arrayOfChar = new char[i];
    this.pointer += 1;
    for (int j = 0; j < i; j++)
    {
      int k;
      if (this.useArray)
        k = this.fontDataAsArray[this.pointer] & 0xFF;
      else
        k = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
      this.pointer += 1;
      arrayOfChar[j] = ((char)k);
      if ((this.useArray) && (this.pointer >= this.fontDataAsArray.length))
        j = i;
    }
    return String.copyValueOf(arrayOfChar);
  }

  public byte[] getStringBytes()
  {
    if ((this.useArray) && (this.pointer == this.fontDataAsArray.length))
      return new byte[1];
    int i;
    if (this.useArray)
      i = this.fontDataAsArray[this.pointer] & 0xFF;
    else
      i = this.fontDataAsObject.getByte(this.pointer) & 0xFF;
    byte[] arrayOfByte = new byte[i];
    this.pointer += 1;
    for (int j = 0; j < i; j++)
    {
      int k;
      if (this.useArray)
        k = this.fontDataAsArray[this.pointer];
      else
        k = this.fontDataAsObject.getByte(this.pointer);
      this.pointer += 1;
      arrayOfByte[j] = k;
      if ((this.useArray) && (this.pointer >= this.fontDataAsArray.length))
        j = i;
    }
    return arrayOfByte;
  }

  public float getF2Dot14()
  {
    int i;
    if (this.useArray)
      i = ((this.fontDataAsArray[this.pointer] & 0xFF) << 8) + (this.fontDataAsArray[(this.pointer + 1)] & 0xFF);
    else
      i = ((this.fontDataAsObject.getByte(this.pointer) & 0xFF) << 8) + (this.fontDataAsObject.getByte(this.pointer + 1) & 0xFF);
    this.pointer += 2;
    if (i == 49152)
      return -1.0F;
    if (i == 16384)
      return 1.0F;
    return (i - 2 * (i & 0x8000)) / 16384.0F;
  }

  public byte[] readBytes(int paramInt1, int paramInt2)
  {
    if (this.useArray)
    {
      byte[] arrayOfByte = new byte[paramInt2];
      System.arraycopy(this.fontDataAsArray, paramInt1, arrayOfByte, 0, paramInt2);
      return arrayOfByte;
    }
    return this.fontDataAsObject.getBytes(paramInt1, paramInt2);
  }

  public byte[] getTableBytes(int paramInt)
  {
    int i = this.tables[paramInt][this.currentFontID];
    int j = this.tableLength[paramInt][this.currentFontID];
    if (this.useArray)
    {
      byte[] arrayOfByte = new byte[j];
      System.arraycopy(this.fontDataAsArray, i, arrayOfByte, 0, j);
      return arrayOfByte;
    }
    return this.fontDataAsObject.getBytes(i, j);
  }

  public int getFontCount()
  {
    return this.fontCount;
  }

  public boolean hasValuesLeft()
  {
    int i;
    if (this.useArray)
      i = this.fontDataAsArray.length;
    else
      i = this.fontDataAsObject.length();
    return this.pointer < i;
  }

  public int getBytesLeft()
  {
    int i;
    if (this.useArray)
      i = this.fontDataAsArray.length;
    else
      i = this.fontDataAsObject.length();
    return i - this.pointer;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.FontFile2
 * JD-Core Version:    0.6.2
 */
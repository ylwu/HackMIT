package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Deflater;
import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.utils.Sorts;
import org.jpedal.utils.StringUtils;

public class FontWriter extends FontFile2
{
  String name;
  int glyphCount;
  int headCheckSumPos = -1;
  Map IDtoTable = new HashMap();
  private static boolean compressWoff = true;
  ArrayList<TTFDirectory> ttfList = new ArrayList();
  static final String[] TTFTableOrder = { "OS/2", "cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name", "post", "prep" };
  private HashMap<Integer, byte[]> tableStore = new HashMap();
  protected String styleName;
  boolean debug = false;

  public FontWriter(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  public FontWriter()
  {
  }

  static int getUintFromByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = (paramInt2 - 1) * 8;
    int j = 0;
    int k = 0;
    while (i >= 0)
    {
      int m = paramArrayOfByte[(paramInt1 + k)];
      if (m < 0)
        m += 256;
      j |= m << i;
      k++;
      i -= 8;
    }
    return j;
  }

  static int getUintFromIntArray(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = (paramInt2 - 1) * 8;
    int j = 0;
    int k = 0;
    while (i >= 0)
    {
      j |= (paramArrayOfInt[(paramInt1 + k)] & 0xFF) << i;
      k++;
      i -= 8;
    }
    return j;
  }

  public static byte[] setUintAsBytes(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    for (int i = paramInt2; i > 0; i--)
    {
      int j = paramInt1;
      for (int k = 1; k < i; k++)
        j >>= 8;
      arrayOfByte[(paramInt2 - i)] = ((byte)j);
    }
    return arrayOfByte;
  }

  static int createChecksum(byte[] paramArrayOfByte)
  {
    int i = 0;
    FontFile2 localFontFile2 = new FontFile2(paramArrayOfByte, true);
    int j = paramArrayOfByte.length + 3 >> 2;
    for (int k = 0; k < j; k++)
      i += localFontFile2.getNextUint32();
    return i;
  }

  public static final byte[] setUFWord(int paramInt)
  {
    int i = (short)paramInt;
    byte[] arrayOfByte = new byte[2];
    for (int j = 0; j < 2; j++)
      arrayOfByte[j] = ((byte)(i >> 8 * (1 - j) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setFWord(int paramInt)
  {
    int i = (short)paramInt;
    byte[] arrayOfByte = new byte[2];
    for (int j = 0; j < 2; j++)
      arrayOfByte[j] = ((byte)(i >> 8 * (1 - j) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setNextUint16(int paramInt)
  {
    byte[] arrayOfByte = new byte[2];
    for (int i = 0; i < 2; i++)
      arrayOfByte[i] = ((byte)(paramInt >> 8 * (1 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setNextInt16(int paramInt)
  {
    byte[] arrayOfByte = new byte[2];
    for (int i = 0; i < 2; i++)
      arrayOfByte[i] = ((byte)(paramInt >> 8 * (1 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setNextSignedInt16(short paramShort)
  {
    byte[] arrayOfByte = new byte[2];
    for (int i = 0; i < 2; i++)
      arrayOfByte[i] = ((byte)(paramShort >> 8 * (1 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte setNextUint8(int paramInt)
  {
    return (byte)(paramInt & 0xFF);
  }

  public static final byte[] setNextUint32(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    for (int i = 0; i < 4; i++)
      arrayOfByte[i] = ((byte)(paramInt >> 8 * (3 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setNextUint64(int paramInt)
  {
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < 8; i++)
      arrayOfByte[i] = ((byte)(paramInt >> 8 * (7 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] setNextUint64(long paramLong)
  {
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < 8; i++)
      arrayOfByte[i] = ((byte)(int)(paramLong >> 8 * (7 - i) & 0xFF));
    return arrayOfByte;
  }

  public static final byte[] switchEndian(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int i = 0;
    for (int j = paramArrayOfByte.length - 1; j > -1; j--)
    {
      arrayOfByte[i] = paramArrayOfByte[j];
      i++;
    }
    return arrayOfByte;
  }

  public final byte[] writeFontToStream()
    throws IOException
  {
    readTables();
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (this.type == 1)
    {
      if (this.subType == 10)
        localByteArrayOutputStream.write(setNextUint32(1330926671));
      else
        localByteArrayOutputStream.write(setNextUint32(65536));
    }
    else if (this.type == 3)
      localByteArrayOutputStream.write(setNextUint32(1953784678));
    else
      localByteArrayOutputStream.write(setNextUint32(65536));
    if (this.type == 3)
      System.out.println("TTC write not implemented");
    else
      writeTablesForFont(localByteArrayOutputStream);
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    byte[] arrayOfByte2 = setNextUint32((int)(Long.parseLong("B1B0AFBA", 16) - createChecksum(arrayOfByte1)));
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0 + this.headCheckSumPos, 4);
    return arrayOfByte1;
  }

  void readTables()
  {
  }

  private void writeTablesForFont(ByteArrayOutputStream paramByteArrayOutputStream)
    throws IOException
  {
    int[] arrayOfInt1 = new int[this.numTables];
    int[] arrayOfInt2 = new int[this.numTables];
    int[] arrayOfInt3 = new int[this.numTables];
    int[] arrayOfInt4 = new int[this.numTables];
    paramByteArrayOutputStream.write(setNextUint16(this.numTables));
    paramByteArrayOutputStream.write(setNextUint16(this.searchRange));
    paramByteArrayOutputStream.write(setNextUint16(this.entrySelector));
    paramByteArrayOutputStream.write(setNextUint16(this.rangeShift));
    String str;
    int i;
    for (int j = 0; j < this.numTables; j++)
    {
      str = (String)this.tableList.get(j);
      i = getTableID(str);
      if (this.debug)
        System.out.println("writing out " + str + " id=" + i);
      if (i != -1)
      {
        localObject = getTableBytes(i);
        this.tableStore.put(Integer.valueOf(i), localObject);
        if (i != 0)
          arrayOfInt3[j] = createChecksum((byte[])localObject);
        arrayOfInt4[j] = localObject.length;
        arrayOfInt1[j] = j;
        arrayOfInt2[j] = this.tables[i][this.currentFontID];
      }
    }
    if (this.subType == 10)
      arrayOfInt1 = Sorts.quicksort(arrayOfInt2, arrayOfInt1);
    j = alignOnWordBoundary(paramByteArrayOutputStream.size() + 16 * this.numTables);
    Object localObject = new int[this.numTables];
    for (int m = 0; m < this.numTables; m++)
    {
      int k = arrayOfInt1[m];
      localObject[k] = j;
      arrayOfInt2[k] = j;
      j = alignOnWordBoundary(j + arrayOfInt4[k]);
    }
    int n;
    if (this.subType == 11)
      for (m = 0; m < this.numTables; m++)
      {
        str = TTFTableOrder[m];
        paramByteArrayOutputStream.write(StringUtils.toBytes(str));
        i = getTableID(str);
        n = ((Integer)this.IDtoTable.get(str)).intValue();
        if (i != -1)
        {
          if (i == 0)
            this.headCheckSumPos = paramByteArrayOutputStream.size();
          paramByteArrayOutputStream.write(setNextUint32(arrayOfInt3[n]));
          paramByteArrayOutputStream.write(setNextUint32(localObject[n]));
          paramByteArrayOutputStream.write(setNextUint32(arrayOfInt4[n]));
          this.ttfList.add(new TTFDirectory(str, localObject[n], arrayOfInt3[n], arrayOfInt4[n]));
        }
      }
    else
      for (m = 0; m < this.numTables; m++)
      {
        n = m;
        str = (String)this.tableList.get(n);
        paramByteArrayOutputStream.write(StringUtils.toBytes(str));
        i = getTableID(str);
        if (i != -1)
        {
          if (i == 0)
            this.headCheckSumPos = paramByteArrayOutputStream.size();
          paramByteArrayOutputStream.write(setNextUint32(arrayOfInt3[n]));
          paramByteArrayOutputStream.write(setNextUint32(localObject[n]));
          paramByteArrayOutputStream.write(setNextUint32(arrayOfInt4[n]));
          this.ttfList.add(new TTFDirectory(str, localObject[n], arrayOfInt3[n], arrayOfInt4[n]));
        }
      }
    for (int i1 = 0; i1 < this.numTables; i1++)
    {
      m = arrayOfInt1[i1];
      str = (String)this.tableList.get(m);
      i = getTableID(str);
      byte[] arrayOfByte = (byte[])this.tableStore.get(Integer.valueOf(i));
      while ((paramByteArrayOutputStream.size() & 0x3) != 0)
        paramByteArrayOutputStream.write(0);
      paramByteArrayOutputStream.write(arrayOfByte);
    }
  }

  private static int alignOnWordBoundary(int paramInt)
  {
    int i = paramInt & 0x3;
    if (i != 0)
      paramInt += 4 - i;
    return paramInt;
  }

  public final byte[] writeFontToWoffStream()
    throws IOException
  {
    byte[] arrayOfByte1 = writeFontToStream();
    Iterator localIterator = this.ttfList.iterator();
    while (localIterator.hasNext())
    {
      TTFDirectory localTTFDirectory1 = (TTFDirectory)localIterator.next();
      if (localTTFDirectory1.getTag().equals("head"))
      {
        k = AdjustWoffChecksum(arrayOfByte1, localTTFDirectory1.getOffset(), localTTFDirectory1.getLength());
        localTTFDirectory1.setChecksum(k);
      }
    }
    int i = 12 + 16 * this.numTables;
    int j = 44 + 20 * this.numTables;
    int k = arrayOfByte1.length - i;
    byte[] arrayOfByte2 = new byte[k];
    System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte1.length - i);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4);
    int m = 0;
    Object localObject1 = this.ttfList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      TTFDirectory localTTFDirectory2 = (TTFDirectory)((Iterator)localObject1).next();
      localObject2 = new byte[localTTFDirectory2.getLength()];
      System.arraycopy(arrayOfByte1, localTTFDirectory2.getOffset(), localObject2, 0, localTTFDirectory2.getLength());
      localTTFDirectory2.setOffset(m + j);
      int i1 = localTTFDirectory2.getLength();
      Object localObject3;
      if (compressWoff)
      {
        byte[] arrayOfByte3 = new byte[localTTFDirectory2.getLength()];
        localObject3 = new Deflater();
        ((Deflater)localObject3).setInput((byte[])localObject2);
        ((Deflater)localObject3).finish();
        i1 = ((Deflater)localObject3).deflate(arrayOfByte3);
        if ((i1 < localTTFDirectory2.getLength()) && (((Deflater)localObject3).finished()))
        {
          localByteArrayOutputStream.write(arrayOfByte3, 0, i1);
        }
        else
        {
          localByteArrayOutputStream.write((byte[])localObject2);
          i1 = localTTFDirectory2.getLength();
        }
      }
      else
      {
        localByteArrayOutputStream.write((byte[])localObject2);
      }
      localTTFDirectory2.setCompressLength(i1);
      int i2 = i1 % 4 > 0 ? 4 - i1 % 4 : 0;
      if (i2 > 0)
      {
        localObject3 = new byte[i2];
        localByteArrayOutputStream.write((byte[])localObject3);
      }
      m = m + localTTFDirectory2.getCompressLength() + i2;
    }
    localObject1 = new ByteArrayOutputStream();
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(2001684038));
    if (this.type == 1)
    {
      if (this.subType == 10)
        ((ByteArrayOutputStream)localObject1).write(setNextUint32(1330926671));
      else
        ((ByteArrayOutputStream)localObject1).write(setNextUint32(65536));
    }
    else if (this.type == 3)
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(1953784678));
    else
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(65536));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(j + m));
    ((ByteArrayOutputStream)localObject1).write(setNextUint16(this.numTables));
    ((ByteArrayOutputStream)localObject1).write(setNextUint16(0));
    int n = arrayOfByte1.length % 4 > 0 ? 4 - arrayOfByte1.length % 4 : 0;
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(n > 0 ? arrayOfByte1.length + n : arrayOfByte1.length));
    ((ByteArrayOutputStream)localObject1).write(setNextUint16(1));
    ((ByteArrayOutputStream)localObject1).write(setNextUint16(1));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(0));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(0));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(0));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(0));
    ((ByteArrayOutputStream)localObject1).write(setNextUint32(0));
    Object localObject2 = this.ttfList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      TTFDirectory localTTFDirectory3 = (TTFDirectory)((Iterator)localObject2).next();
      ((ByteArrayOutputStream)localObject1).write(localTTFDirectory3.getTagBytes());
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(localTTFDirectory3.getOffset()));
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(localTTFDirectory3.getCompressLength()));
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(localTTFDirectory3.getLength()));
      ((ByteArrayOutputStream)localObject1).write(setNextUint32(localTTFDirectory3.getChecksum()));
    }
    ((ByteArrayOutputStream)localObject1).write(localByteArrayOutputStream.toByteArray());
    ((ByteArrayOutputStream)localObject1).flush();
    ((ByteArrayOutputStream)localObject1).close();
    return ((ByteArrayOutputStream)localObject1).toByteArray();
  }

  private static int AdjustWoffChecksum(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramArrayOfByte.length > paramInt1) && (paramInt2 >= 4))
    {
      ByteBuffer localByteBuffer1 = ByteBuffer.wrap(paramArrayOfByte);
      byte[] arrayOfByte1 = new byte[4];
      for (int i = 0; i < 4; i++)
        arrayOfByte1[i] = 0;
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt1 + 8, 4);
      i = -1313820742 - createChecksum(arrayOfByte1);
      byte[] arrayOfByte2 = new byte[paramInt2];
      System.arraycopy(localByteBuffer1.array(), paramInt1, arrayOfByte2, 0, paramInt2);
      int j = createChecksum(arrayOfByte2);
      ByteBuffer localByteBuffer2 = ByteBuffer.allocate(4);
      localByteBuffer2.putInt(i);
      byte[] arrayOfByte3 = localByteBuffer2.array();
      System.arraycopy(arrayOfByte3, 0, paramArrayOfByte, paramInt1 + 8, 4);
      return j;
    }
    return 0;
  }

  public final byte[] writeFontToEotStream()
    throws IOException
  {
    byte[] arrayOfByte1 = writeFontToStream();
    byte[] arrayOfByte2 = this.name.replace(',', '-').getBytes("UTF-16LE");
    byte[] arrayOfByte3 = this.styleName.getBytes("UTF-16LE");
    byte[] arrayOfByte4 = "Version 1.0".getBytes("UTF-16LE");
    int i = arrayOfByte1.length;
    int j = 131073;
    int k = 0;
    byte[] arrayOfByte5 = (byte[])OS2Writer.fontPanose.clone();
    int m = 1;
    int n = 0;
    if (this.styleName.equals("Italic"))
      n = 1;
    int i1 = 400;
    if (this.styleName.equals("Bold"))
      i1 = 700;
    int i2 = 8;
    int i3 = 20556;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    int i9 = 0;
    int i10 = 0;
    int i11 = arrayOfByte2.length;
    byte[] arrayOfByte6 = (byte[])arrayOfByte2.clone();
    int i12 = arrayOfByte3.length;
    byte[] arrayOfByte7 = (byte[])arrayOfByte3.clone();
    int i13 = arrayOfByte4.length;
    byte[] arrayOfByte8 = (byte[])arrayOfByte4.clone();
    int i14 = arrayOfByte2.length;
    byte[] arrayOfByte9 = (byte[])arrayOfByte2.clone();
    int i15 = 0;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(switchEndian(setNextUint32(0)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(j)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(k)));
    int i18;
    for (i18 : arrayOfByte5)
      localByteArrayOutputStream.write(setNextUint8(i18));
    localByteArrayOutputStream.write(setNextUint8(m));
    localByteArrayOutputStream.write(setNextUint8(n));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i1)));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i2)));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i3)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i4)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i5)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i6)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i7)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i8)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i9)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(i10)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(0)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(0)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(0)));
    localByteArrayOutputStream.write(switchEndian(setNextUint32(0)));
    localByteArrayOutputStream.write(setNextUint16(0));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i11)));
    for (i18 : arrayOfByte6)
      localByteArrayOutputStream.write(setNextUint8(i18));
    localByteArrayOutputStream.write(setNextUint16(0));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i12)));
    for (i18 : arrayOfByte7)
      localByteArrayOutputStream.write(setNextUint8(i18));
    localByteArrayOutputStream.write(setNextUint16(0));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i13)));
    for (i18 : arrayOfByte8)
      localByteArrayOutputStream.write(setNextUint8(i18));
    localByteArrayOutputStream.write(setNextUint16(0));
    localByteArrayOutputStream.write(switchEndian(setNextUint16(i14)));
    for (i18 : arrayOfByte9)
      localByteArrayOutputStream.write(setNextUint8(i18));
    localByteArrayOutputStream.write(setNextUint16(0));
    localByteArrayOutputStream.write(setNextInt16(i15));
    localByteArrayOutputStream.write(arrayOfByte1);
    ??? = localByteArrayOutputStream.toByteArray();
    byte[] arrayOfByte11 = switchEndian(setNextUint32(???.length));
    for (??? = 0; ??? < 4; ???++)
      ???[???] = arrayOfByte11[???];
    return ???;
  }

  static
  {
    if (DefaultIO.isTest)
      compressWoff = false;
  }

  private static class TTFDirectory
  {
    private int offset2;
    private int checksum2;
    private int length2;
    private int compressLength;
    private String tag2;

    public TTFDirectory(String paramString, int paramInt1, int paramInt2, int paramInt3)
    {
      this.tag2 = paramString;
      this.offset2 = paramInt1;
      this.checksum2 = paramInt2;
      this.length2 = paramInt3;
    }

    public String getTag()
    {
      return this.tag2;
    }

    public byte[] getTagBytes()
    {
      byte[] arrayOfByte = new byte[4];
      for (int i = 0; i < 4; i++)
        arrayOfByte[i] = ((byte)this.tag2.charAt(i));
      return arrayOfByte;
    }

    public int getOffset()
    {
      return this.offset2;
    }

    public int getChecksum()
    {
      return this.checksum2;
    }

    public int getLength()
    {
      return this.length2;
    }

    public int getCompressLength()
    {
      return this.compressLength;
    }

    public void setOffset(int paramInt)
    {
      this.offset2 = paramInt;
    }

    public void setChecksum(int paramInt)
    {
      this.checksum2 = paramInt;
    }

    public void setLength(int paramInt)
    {
      this.length2 = paramInt;
    }

    public void setTag(String paramString)
    {
      this.tag2 = paramString;
    }

    public void setCompressLength(int paramInt)
    {
      this.compressLength = paramInt;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.FontWriter
 * JD-Core Version:    0.6.2
 */
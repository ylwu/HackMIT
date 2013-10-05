package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.FontFile2;

public class Type1CAdjustment
  implements FontTableWriter
{
  private final Type1C font;
  private final PdfJavaGlyphs cffGlyphs;
  private final String name;
  private byte[] header;
  private byte[] nameIndex;
  private byte[] topDictIndex;
  private byte[] globalSubrIndex;
  private byte[] encodings;
  private byte[] charsets;
  private byte[] charStringsIndex;
  private byte[] privateDict;
  private byte[] localSubrIndex;
  private byte[] stringIndex;
  private int padding = 50;
  boolean hasNotDef = false;
  LinkedHashMap<Integer, byte[]> codeByteMap = new LinkedHashMap();

  public Type1CAdjustment(Type1C paramType1C, PdfJavaGlyphs paramPdfJavaGlyphs, String paramString)
  {
    this.font = paramType1C;
    this.cffGlyphs = paramPdfJavaGlyphs;
    this.name = paramString;
    this.codeByteMap = getOrderedGlyphMapForAdobe(paramType1C);
  }

  public byte[] writeTable()
    throws IOException
  {
    this.header = (this.nameIndex = this.topDictIndex = this.globalSubrIndex = this.stringIndex = this.encodings = this.charsets = this.charStringsIndex = this.privateDict = this.localSubrIndex = new byte[0]);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    this.header = createHeadIndex();
    this.nameIndex = createNameIndex(this.name);
    this.stringIndex = CFFUtils.createIndex((byte[][])null);
    this.globalSubrIndex = CFFUtils.createIndex((byte[][])null);
    this.charsets = createCharsets();
    this.charStringsIndex = createCharStrings();
    this.privateDict = createPrivateDict();
    byte[] arrayOfByte1 = fillTopDictionary(this.header.length, this.nameIndex.length, this.stringIndex.length, this.globalSubrIndex.length, this.charsets.length, this.charStringsIndex.length, this.privateDict.length);
    this.topDictIndex = CFFUtils.createIndex(new byte[][] { arrayOfByte1 });
    this.padding -= this.topDictIndex.length - arrayOfByte1.length;
    localByteArrayOutputStream.write(this.header);
    localByteArrayOutputStream.write(this.nameIndex);
    localByteArrayOutputStream.write(this.topDictIndex);
    localByteArrayOutputStream.write(this.stringIndex);
    localByteArrayOutputStream.write(this.globalSubrIndex);
    for (int i = 0; i < this.padding; i++)
    {
      int j = 1;
      localByteArrayOutputStream.write(j);
    }
    localByteArrayOutputStream.write(this.charsets);
    localByteArrayOutputStream.write(this.charStringsIndex);
    localByteArrayOutputStream.write(this.privateDict);
    localByteArrayOutputStream.close();
    localByteArrayOutputStream.flush();
    byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
    return arrayOfByte2;
  }

  private static byte[] createHeadIndex()
  {
    return new byte[] { TTFontWriter.setNextUint8(1), TTFontWriter.setNextUint8(0), TTFontWriter.setNextUint8(4), TTFontWriter.setNextUint8(2) };
  }

  private byte[] fillTopDictionary(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    throws IOException
  {
    int i = paramInt1 + paramInt2 + paramInt3 + paramInt4;
    ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
    byte[] arrayOfByte1 = Type1C.getOperatorBytes(3078);
    byte[] arrayOfByte2 = CFFUtils.storeInteger(2);
    i += arrayOfByte1.length + arrayOfByte2.length;
    localByteArrayOutputStream1.write(arrayOfByte2);
    localByteArrayOutputStream1.write(arrayOfByte1);
    float[] arrayOfFloat = this.font.getFontBounds();
    ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
    for (int j = 0; j < arrayOfFloat.length; j++)
      localByteArrayOutputStream2.write(CFFUtils.storeReal(arrayOfFloat[j]));
    byte[] arrayOfByte3 = Type1C.getOperatorBytes(5);
    byte[] arrayOfByte4 = localByteArrayOutputStream2.toByteArray();
    i += arrayOfByte3.length + arrayOfByte4.length;
    localByteArrayOutputStream1.write(arrayOfByte4);
    localByteArrayOutputStream1.write(arrayOfByte3);
    double[] arrayOfDouble = (double[])this.font.getKeyValue(3079);
    ByteArrayOutputStream localByteArrayOutputStream3 = new ByteArrayOutputStream();
    for (int k = 0; k < arrayOfDouble.length; k++)
      localByteArrayOutputStream3.write(CFFUtils.storeReal(arrayOfDouble[k]));
    localByteArrayOutputStream3.close();
    localByteArrayOutputStream3.flush();
    byte[] arrayOfByte5 = Type1C.getOperatorBytes(3079);
    byte[] arrayOfByte6 = localByteArrayOutputStream3.toByteArray();
    i += arrayOfByte5.length + arrayOfByte6.length;
    localByteArrayOutputStream1.write(arrayOfByte6);
    localByteArrayOutputStream1.write(arrayOfByte5);
    byte[] arrayOfByte7 = Type1C.getOperatorBytes(15);
    byte[] arrayOfByte8 = CFFUtils.storeInteger(i + 50);
    this.padding = (this.padding - arrayOfByte7.length - arrayOfByte8.length);
    localByteArrayOutputStream1.write(arrayOfByte8);
    localByteArrayOutputStream1.write(arrayOfByte7);
    byte[] arrayOfByte9 = Type1C.getOperatorBytes(17);
    byte[] arrayOfByte10 = CFFUtils.storeInteger(i + 50 + paramInt5);
    this.padding = (this.padding - arrayOfByte9.length - arrayOfByte10.length);
    localByteArrayOutputStream1.write(arrayOfByte10);
    localByteArrayOutputStream1.write(arrayOfByte9);
    byte[] arrayOfByte11 = Type1C.getOperatorBytes(18);
    byte[] arrayOfByte12 = CFFUtils.storeInteger(paramInt7);
    byte[] arrayOfByte13 = CFFUtils.storeInteger(i + 50 + paramInt5 + paramInt6);
    this.padding = (this.padding - arrayOfByte11.length - arrayOfByte13.length - arrayOfByte12.length);
    localByteArrayOutputStream1.write(arrayOfByte12);
    localByteArrayOutputStream1.write(arrayOfByte13);
    localByteArrayOutputStream1.write(arrayOfByte11);
    localByteArrayOutputStream1.close();
    localByteArrayOutputStream1.flush();
    byte[] arrayOfByte14 = localByteArrayOutputStream1.toByteArray();
    return arrayOfByte14;
  }

  private byte[] createCharsets()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    int[] arrayOfInt = new int[this.codeByteMap.size()];
    Object[] arrayOfObject = this.codeByteMap.keySet().toArray();
    for (int j = 0; j < arrayOfInt.length; j++)
      arrayOfInt[j] = ((Integer)arrayOfObject[j]).intValue();
    Arrays.sort(arrayOfInt);
    localByteArrayOutputStream.write(FontWriter.setNextUint8(i));
    for (j = 0; j < arrayOfInt.length; j++)
      if (arrayOfInt[j] != 0)
        if (i == 0)
          localByteArrayOutputStream.write(FontWriter.setNextUint16(arrayOfInt[j]));
        else if (i == 1)
          localByteArrayOutputStream.write(FontWriter.setNextUint32(arrayOfInt[j]));
    localByteArrayOutputStream.close();
    localByteArrayOutputStream.flush();
    return localByteArrayOutputStream.toByteArray();
  }

  private byte[] createCharStrings()
    throws IOException
  {
    int[] arrayOfInt = new int[this.codeByteMap.size()];
    Object[] arrayOfObject = this.codeByteMap.keySet().toArray();
    for (int i = 0; i < arrayOfInt.length; i++)
    {
      arrayOfInt[i] = ((Integer)arrayOfObject[i]).intValue();
      if (arrayOfInt[i] == 0)
        this.hasNotDef = true;
    }
    Arrays.sort(arrayOfInt);
    ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
    if (this.hasNotDef)
      localByteArrayOutputStream1.write(FontWriter.setNextInt16(arrayOfInt.length));
    else
      localByteArrayOutputStream1.write(FontWriter.setNextInt16(arrayOfInt.length + 1));
    localByteArrayOutputStream1.write(FontWriter.setNextUint8(2));
    ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
    int j = 1;
    byte[] arrayOfByte1 = { 1, 14 };
    if (!this.hasNotDef)
    {
      localByteArrayOutputStream1.write(FontWriter.setNextUint16(j));
      localByteArrayOutputStream2.write(arrayOfByte1);
      j += arrayOfByte1.length;
    }
    for (int k = 0; k < arrayOfInt.length; k++)
    {
      byte[] arrayOfByte2 = (byte[])this.codeByteMap.get(Integer.valueOf(arrayOfInt[k]));
      localByteArrayOutputStream1.write(FontWriter.setNextUint16(j));
      localByteArrayOutputStream2.write(arrayOfByte2);
      j += arrayOfByte2.length;
    }
    localByteArrayOutputStream1.write(FontWriter.setNextUint16(j));
    localByteArrayOutputStream2.close();
    localByteArrayOutputStream2.flush();
    localByteArrayOutputStream1.write(localByteArrayOutputStream2.toByteArray());
    localByteArrayOutputStream1.close();
    localByteArrayOutputStream1.flush();
    return localByteArrayOutputStream1.toByteArray();
  }

  private static byte[] createNameIndex(String paramString)
    throws IOException
  {
    return CFFUtils.createIndex(new byte[][] { paramString.getBytes() });
  }

  private byte[] createPrivateDict()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = ((Integer)this.font.getKeyValue(20)).intValue();
    localByteArrayOutputStream.write(CFFUtils.storeInteger(i));
    localByteArrayOutputStream.write(20);
    int j = ((Integer)this.font.getKeyValue(21)).intValue();
    localByteArrayOutputStream.write(CFFUtils.storeInteger(j));
    localByteArrayOutputStream.write(21);
    int[] arrayOfInt1 = (int[])this.font.getKeyValue(6);
    if (arrayOfInt1 != null)
    {
      for (int k = 0; k < arrayOfInt1.length; k++)
        localByteArrayOutputStream.write(CFFUtils.storeReal(arrayOfInt1[k]));
      localByteArrayOutputStream.write(6);
    }
    int[] arrayOfInt2 = (int[])this.font.getKeyValue(7);
    if (arrayOfInt2 != null)
    {
      for (int m = 0; m < arrayOfInt2.length; m++)
        localByteArrayOutputStream.write(CFFUtils.storeReal(arrayOfInt2[m]));
      localByteArrayOutputStream.write(7);
    }
    int[] arrayOfInt3 = (int[])this.font.getKeyValue(8);
    if (arrayOfInt3 != null)
    {
      for (int n = 0; n < arrayOfInt3.length; n++)
        localByteArrayOutputStream.write(CFFUtils.storeReal(arrayOfInt3[n]));
      localByteArrayOutputStream.write(8);
    }
    int[] arrayOfInt4 = (int[])this.font.getKeyValue(9);
    if (arrayOfInt4 != null)
    {
      for (int i1 = 0; i1 < arrayOfInt4.length; i1++)
        localByteArrayOutputStream.write(CFFUtils.storeReal(arrayOfInt4[i1]));
      localByteArrayOutputStream.write(9);
    }
    localByteArrayOutputStream.write(CFFUtils.storeInteger(((Integer)this.font.getKeyValue(10)).intValue()));
    localByteArrayOutputStream.write(10);
    localByteArrayOutputStream.write(CFFUtils.storeInteger(((Integer)this.font.getKeyValue(11)).intValue()));
    localByteArrayOutputStream.write(11);
    localByteArrayOutputStream.close();
    localByteArrayOutputStream.flush();
    return localByteArrayOutputStream.toByteArray();
  }

  public static LinkedHashMap<Integer, String> getUniOrderedGlyphNames(PdfFont paramPdfFont)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    Object[] arrayOfObject = paramPdfFont.glyphs.getCharStrings().keySet().toArray();
    HashMap localHashMap1 = new HashMap();
    HashMap localHashMap2 = new HashMap();
    for (int i = 0; i < arrayOfObject.length; i++)
    {
      String str = StandardFonts.getUnicodeName((String)arrayOfObject[i]);
      if (str != null)
      {
        int j = getAdjustedUniValue(str);
        if (j > 0)
          localHashMap1.put(Integer.valueOf(j), (String)arrayOfObject[i]);
      }
      else if (localHashMap1.isEmpty())
      {
        localHashMap2.put(Integer.valueOf(localHashMap2.size()), (String)arrayOfObject[i]);
      }
      else
      {
        localHashMap2.clear();
      }
    }
    arrayOfObject = localHashMap1.keySet().toArray();
    if (arrayOfObject.length > 0)
    {
      Arrays.sort(arrayOfObject);
      for (i = 0; i < arrayOfObject.length; i++)
        localLinkedHashMap.put((Integer)arrayOfObject[i], localHashMap1.get(arrayOfObject[i]));
    }
    else
    {
      arrayOfObject = localHashMap2.keySet().toArray();
      Arrays.sort(arrayOfObject);
      for (i = 0; i < arrayOfObject.length; i++)
        localLinkedHashMap.put((Integer)arrayOfObject[i], localHashMap2.get(arrayOfObject[i]));
    }
    return localLinkedHashMap;
  }

  public static LinkedHashMap<Integer, byte[]> getOrderedGlyphMapForAdobe(PdfFont paramPdfFont)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    Object[] arrayOfObject1 = getUniOrderedGlyphNames(paramPdfFont).values().toArray();
    Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
    for (int i = 0; i < arrayOfObject1.length; i++)
      arrayOfObject2[i] = paramPdfFont.glyphs.getCharStrings().get(arrayOfObject1[i]);
    HashMap localHashMap1 = new HashMap();
    HashMap localHashMap2 = new HashMap();
    for (int j = 0; j < arrayOfObject1.length; j++)
    {
      String str = (String)arrayOfObject1[j];
      int k = getSIDForString(str);
      if (k > 0)
        localHashMap1.put(Integer.valueOf(k), (byte[])arrayOfObject2[j]);
      else if ((k == -1) && (localHashMap1.isEmpty()))
        localHashMap2.put(Integer.valueOf(localHashMap2.size() + 1), (byte[])arrayOfObject2[j]);
    }
    arrayOfObject1 = localHashMap1.keySet().toArray();
    if (arrayOfObject1.length > 0)
    {
      Arrays.sort(arrayOfObject1);
      for (j = 0; j < arrayOfObject1.length; j++)
        localLinkedHashMap.put((Integer)arrayOfObject1[j], (byte[])localHashMap1.get(arrayOfObject1[j]));
    }
    else
    {
      arrayOfObject1 = localHashMap2.keySet().toArray();
      Arrays.sort(arrayOfObject1);
      for (j = 0; j < arrayOfObject1.length; j++)
        localLinkedHashMap.put((Integer)arrayOfObject1[j], (byte[])localHashMap2.get(arrayOfObject1[j]));
    }
    return localLinkedHashMap;
  }

  public static int calculateWidth(byte[] paramArrayOfByte)
  {
    FontFile2 localFontFile2 = new FontFile2(paramArrayOfByte, true);
    ArrayList localArrayList1 = new ArrayList();
    while (localFontFile2.hasValuesLeft())
    {
      ArrayList localArrayList2 = readGlyphTopDictItem(localFontFile2);
      localArrayList1.add(localArrayList2);
      if (((Integer)localArrayList2.get(localArrayList2.size() - 1)).intValue() == 14)
        break;
    }
    return 100;
  }

  public static int getSIDForString(String paramString)
  {
    for (int i = 0; i < Type1C.type1CStdStrings.length; i++)
      if (paramString.equals(Type1C.type1CStdStrings[i]))
        return i;
    return -1;
  }

  private static int getAdjustedUniValue(String paramString)
  {
    if (paramString.length() == 1)
      return paramString.charAt(0);
    if (paramString.equals("ff"))
      return 64256;
    if (paramString.equals("fi"))
      return 64257;
    if (paramString.equals("fl"))
      return 64258;
    if (paramString.equals("ffi"))
      return 64259;
    if (paramString.equals("ffl"))
      return 64260;
    return -1;
  }

  public static ArrayList<Object> readGlyphTopDictItem(FontFile2 paramFontFile2)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; (paramFontFile2.getBytesLeft() > 0) && (i == 0); i = 1)
    {
      StringBuilder localStringBuilder1 = paramFontFile2.getNextUint8();
      int k;
      int n;
      int j;
      StringBuilder localStringBuilder2;
      if (localStringBuilder1 == 28)
      {
        k = paramFontFile2.getNextUint8();
        n = paramFontFile2.getNextUint8();
        j = k << 8 | n;
        localArrayList.add(Integer.valueOf(j));
      }
      else if ((localStringBuilder1 >= 32) && (localStringBuilder1 <= 246))
      {
        j = localStringBuilder1 - 139;
        localArrayList.add(Integer.valueOf(j));
      }
      else if ((localStringBuilder1 >= 247) && (localStringBuilder1 <= 250))
      {
        k = paramFontFile2.getNextUint8();
        j = (localStringBuilder1 - 247) * 256 + k + 108;
        localArrayList.add(Integer.valueOf(j));
      }
      else if ((localStringBuilder1 >= 251) && (localStringBuilder1 <= 254))
      {
        k = paramFontFile2.getNextUint8();
        j = -((localStringBuilder1 - 251) * 256) - k - 108;
        localArrayList.add(Integer.valueOf(j));
      }
      else if (localStringBuilder1 == 30)
      {
        localStringBuilder2 = new StringBuilder();
        n = 0;
        while (n == 0)
        {
          int i1 = paramFontFile2.getNextUint8();
          int[] arrayOfInt1 = { i1 / 16, i1 % 16 };
          for (int i5 : arrayOfInt1)
            switch (i5)
            {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
              localStringBuilder2.append(i5);
              break;
            case 10:
              localStringBuilder2.append(".");
              break;
            case 11:
              localStringBuilder2.append("E");
              break;
            case 12:
              localStringBuilder2.append("E-");
              break;
            case 13:
              break;
            case 14:
              localStringBuilder2.append("-");
              break;
            case 15:
              n = 1;
            }
        }
        double d = Double.valueOf(localStringBuilder2.toString()).doubleValue();
        int i2 = (int)d;
        localArrayList.add(Integer.valueOf(i2));
      }
      if ((localStringBuilder1 <= 31) && (localStringBuilder1 != 28))
      {
        int m;
        if (localStringBuilder1 != 12)
          localStringBuilder2 = localStringBuilder1;
        else
          m = 3072 + paramFontFile2.getNextUint8();
        localArrayList.add(Integer.valueOf(m));
      }
    }
    return localArrayList;
  }

  public int getIntValue(int paramInt)
  {
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.Type1CAdjustment
 * JD-Core Version:    0.6.2
 */
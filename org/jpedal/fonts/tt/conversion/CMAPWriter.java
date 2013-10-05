package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.CMAP;
import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.fonts.tt.Hmtx;
import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.utils.Sorts;

public class CMAPWriter extends CMAP
  implements FontTableWriter
{
  int minCharCode = 65536;
  int maxCharCode = 0;
  String fontName;
  private PdfFont originalFont;

  public CMAPWriter(FontFile2 paramFontFile2, int paramInt)
  {
    super(paramFontFile2, paramInt);
  }

  private void createFormat0MapForTT(PdfJavaGlyphs paramPdfJavaGlyphs)
  {
    TTGlyphs localTTGlyphs = (TTGlyphs)paramPdfJavaGlyphs;
    CMAP localCMAP = (CMAP)localTTGlyphs.getTable(2);
    Object localObject;
    if (localCMAP == null)
    {
      localObject = new HashMap();
      if (this.originalFont.getToUnicode() == null)
      {
        i = paramPdfJavaGlyphs.getGlyphCount();
        for (int j = 0; j < i; j++)
          ((Map)localObject).put(Integer.valueOf(j), Integer.valueOf(j));
      }
      else
      {
        for (i = 0; i < 65536; i++)
          if (this.originalFont.getUnicodeMapping(i) != null)
          {
            String str = this.originalFont.getUnicodeMapping(i);
            int k = getAdjustedUniValue(str);
            if (k >= 0)
              ((Map)localObject).put(Integer.valueOf(k), Integer.valueOf(i));
          }
      }
    }
    else
    {
      localObject = localCMAP.buildCharStringTable();
    }
    for (int i = 0; i < 256; i++)
      this.glyphIndexToChar[1][i] = (((Map)localObject).get(Integer.valueOf(i)) != null ? ((Integer)((Map)localObject).get(Integer.valueOf(i))).intValue() : 0);
  }

  private int createFormat4MapForTT(PdfJavaGlyphs paramPdfJavaGlyphs)
  {
    TTGlyphs localTTGlyphs = (TTGlyphs)paramPdfJavaGlyphs;
    CMAP localCMAP = (CMAP)localTTGlyphs.getTable(2);
    Object localObject1;
    Object localObject5;
    if (localCMAP == null)
    {
      localObject1 = new HashMap();
      int i;
      if (this.originalFont.getToUnicode() == null)
      {
        i = paramPdfJavaGlyphs.getGlyphCount();
        for (int k = 0; k < i; k++)
          ((Map)localObject1).put(Integer.valueOf(k), Integer.valueOf(k));
      }
      else
      {
        int m;
        for (i = 0; i < 65536; i++)
          if (this.originalFont.getUnicodeMapping(i) != null)
          {
            localObject2 = this.originalFont.getUnicodeMapping(i);
            m = getAdjustedUniValue((String)localObject2);
            if (m >= 0)
              ((Map)localObject1).put(Integer.valueOf(m), Integer.valueOf(i));
          }
        if (((Map)localObject1).get(Integer.valueOf(32)) == null)
        {
          Object[] arrayOfObject = localTTGlyphs.getCharStrings().keySet().toArray();
          localObject2 = (Hmtx)localTTGlyphs.getTable(6);
          m = 0;
          for (localObject5 : arrayOfObject)
          {
            while ((m < this.originalFont.getGlyphData().getGlyphCount()) && (((Hmtx)localObject2).getUnscaledWidth(m) == 0.0F))
              m++;
            if (m == ((Integer)localObject5).intValue())
              m++;
          }
          ((Map)localObject1).put(Integer.valueOf(32), Integer.valueOf(m));
        }
      }
    }
    else
    {
      localObject1 = localCMAP.buildCharStringTable();
    }
    Object localObject2 = new HashMap();
    HashMap localHashMap = new HashMap();
    ??? = new HashMap();
    ArrayList localArrayList = new ArrayList();
    Object localObject4 = ((Map)localObject1).keySet().iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject5 = ((Iterator)localObject4).next();
      i3 = ((Integer)localObject5).intValue();
      i4 = ((Integer)((Map)localObject1).get(Integer.valueOf(i3))).intValue();
      if ((localCMAP == null) || (localCMAP.hasFormat4()))
        i5 = i3;
      else
        i5 = this.originalFont.getUnicodeMapping(i3) != null ? this.originalFont.getUnicodeMapping(i3).charAt(0) : i3;
      ((HashMap)localObject2).put(Integer.valueOf(i5), Integer.valueOf(i3));
      localHashMap.put(Integer.valueOf(i5), Integer.valueOf(i4));
    }
    localObject4 = ((HashMap)localObject2).keySet().toArray();
    Arrays.sort((Object[])localObject4);
    int j = ((HashMap)localObject2).size() + 1;
    this.endCode = new int[j];
    this.endCode[(j - 1)] = 65535;
    for (int i2 = 0; i2 < j - 1; i2++)
      this.endCode[i2] = ((Integer)localObject4[i2]).intValue();
    this.startCode = new int[j];
    this.startCode[(j - 1)] = 65535;
    for (i2 = 0; i2 < j - 1; i2++)
      this.startCode[i2] = ((Integer)localObject4[i2]).intValue();
    this.idDelta = new int[j];
    this.idDelta[(j - 1)] = 1;
    for (i2 = 0; i2 < j - 1; i2++)
    {
      this.idDelta[i2] = (((Integer)localHashMap.get(Integer.valueOf(this.startCode[i2]))).intValue() - this.startCode[i2]);
      if (this.idDelta[i2] >= 0)
      {
        this.idDelta[i2] = 0;
        ((HashMap)???).put(Integer.valueOf(i2), Integer.valueOf(2 * (j - i2) + 2 * localArrayList.size()));
        localArrayList.add(localHashMap.get(Integer.valueOf(this.startCode[i2])));
      }
    }
    this.idRangeOffset = new int[j];
    this.idRangeOffset[(j - 1)] = 0;
    for (i2 = 0; i2 < j - 1; i2++)
      this.idRangeOffset[i2] = (((HashMap)???).get(Integer.valueOf(i2)) != null ? ((Integer)((HashMap)???).get(Integer.valueOf(i2))).intValue() : 0);
    i2 = j * 2;
    int i3 = (int)(2.0D * Math.pow(2.0D, Math.floor(Math.log(j) / Math.log(2.0D))));
    int i4 = (int)(Math.log(i3 / 2) / Math.log(2.0D));
    int i5 = 2 * j - i3;
    this.CMAPreserved = new int[] { 0, 0, 0 };
    this.CMAPsegCount = new int[] { i2, 0, i2 };
    this.CMAPsearchRange = new int[] { i3, 0, i3 };
    this.CMAPentrySelector = new int[] { i4, 0, i4 };
    this.CMAPrangeShift = new int[] { i5, 0, i5 };
    if (!localArrayList.isEmpty())
    {
      this.glyphIdArray = new int[localArrayList.size()];
      for (int i6 = 0; i6 < this.glyphIdArray.length; i6++)
        this.glyphIdArray[i6] = ((Integer)localArrayList.get(i6)).intValue();
    }
    return 16 + j * 8 + localArrayList.size() * 2;
  }

  private int createFormat4Map(int[] paramArrayOfInt)
  {
    int[] arrayOfInt1 = new int[40000];
    int[] arrayOfInt2 = new int[40000];
    int i = 0;
    int j = 0;
    for (int k = 0; k < 65536; k++)
      if ((j != 0) && (paramArrayOfInt[k] == 2147483647))
      {
        j = 0;
        arrayOfInt2[i] = (k - 1);
        i++;
      }
      else if ((j == 0) && (paramArrayOfInt[k] != 2147483647))
      {
        j = 1;
        arrayOfInt1[i] = k;
      }
    if (paramArrayOfInt[65533] == 2147483647)
    {
      arrayOfInt1[i] = 65533;
      arrayOfInt2[i] = 65533;
      paramArrayOfInt[65533] = 1;
      i++;
    }
    if (paramArrayOfInt[65535] == 2147483647)
    {
      arrayOfInt1[i] = 65535;
      arrayOfInt2[i] = 65535;
      paramArrayOfInt[65535] = 1;
      i++;
    }
    k = i * 2;
    this.CMAPsegCount = new int[] { k, 0, k };
    int m = 1;
    while (m * 2 <= i)
      m *= 2;
    m *= 2;
    this.CMAPsearchRange = new int[] { m, 0, m };
    int n = 0;
    int i1 = m / 2;
    while (i1 > 1)
    {
      i1 /= 2;
      n++;
    }
    this.CMAPentrySelector = new int[] { n, 0, n };
    int i2 = k - m;
    this.CMAPrangeShift = new int[] { i2, 0, i2 };
    this.endCode = arrayOfInt2;
    this.CMAPreserved = new int[] { 0, 0, 0 };
    this.startCode = arrayOfInt1;
    this.idRangeOffset = new int[i];
    int i3 = 0;
    for (int i4 = 0; i4 < i; i4++)
    {
      i5 = paramArrayOfInt[arrayOfInt1[i4]] - arrayOfInt1[i4];
      for (i6 = arrayOfInt1[i4] + 1; i6 <= arrayOfInt2[i4]; i6++)
        if (paramArrayOfInt[i6] - i6 != i5)
        {
          this.idRangeOffset[i4] = -1;
          i3 += arrayOfInt2[i4] + 1 - arrayOfInt1[i4];
          break;
        }
    }
    this.glyphIdArray = new int[i3];
    this.idDelta = new int[i];
    i4 = 16 + i * 8;
    int i5 = 0;
    for (int i6 = 0; i6 < this.idRangeOffset.length; i6++)
      if (this.idRangeOffset[i6] == 0)
      {
        this.idDelta[i6] = (paramArrayOfInt[arrayOfInt1[i6]] - arrayOfInt1[i6] - 1);
      }
      else
      {
        this.idRangeOffset[i6] = (i4 - (16 + i * 6 + i6 * 2));
        for (int i7 = arrayOfInt1[i6]; i7 <= arrayOfInt2[i6]; i7++)
        {
          this.glyphIdArray[i5] = (paramArrayOfInt[i7] - 1);
          i4 += 2;
          i5++;
        }
      }
    return 16 + i * 8 + i3 * 2;
  }

  private void getNonTTGlyphData(PdfJavaGlyphs paramPdfJavaGlyphs, boolean paramBoolean, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    int i = 0;
    for (int j = 0; j < paramPdfJavaGlyphs.getGlyphCount() + 1; j++)
    {
      String str = null;
      if ((this.originalFont != null) && (this.originalFont.getGlyphData().isIdentity()) && (this.originalFont.hasToUnicode()) && (j > 1))
      {
        for (str = this.originalFont.getUnicodeMapping(i); (str == null) && (i < 55296); str = this.originalFont.getUnicodeMapping(i))
          i++;
        if (str != null)
          paramArrayOfInt[str.charAt(0)] = j;
        i++;
      }
      else
      {
        if ((str == null) && (paramBoolean))
          str = paramPdfJavaGlyphs.getIndexForCharString(j);
        else if ((str == null) && (j < paramArrayOfString.length))
          str = paramArrayOfString[j];
        if (str != null)
        {
          int k = StandardFonts.getIDForGlyphName(this.fontName, str);
          if ((k >= 0) && (k < paramArrayOfInt.length))
            if (paramBoolean)
              paramArrayOfInt[k] = j;
            else
              paramArrayOfInt[k] = (j + 1);
        }
      }
    }
  }

  public CMAPWriter(String paramString, PdfFont paramPdfFont1, PdfFont paramPdfFont2, PdfJavaGlyphs paramPdfJavaGlyphs, String[] paramArrayOfString, Collection<GlyphMapping> paramCollection)
  {
    this.fontName = paramString;
    this.originalFont = paramPdfFont2;
    this.numberSubtables = 3;
    this.CMAPformats = new int[] { 4, 0, 4 };
    this.glyphIndexToChar = new int[3][256];
    this.platformID = new int[] { 0, 1, 3 };
    this.platformSpecificID = new int[] { 3, 0, 1 };
    this.CMAPlang = new int[] { 0, 0, 0 };
    int i;
    if (paramPdfFont2.getFontType() == 1217103210)
    {
      i = createFormat4MapForTT(paramPdfJavaGlyphs);
      createFormat0MapForTT(paramPdfJavaGlyphs);
    }
    else
    {
      int[] arrayOfInt = new int[65536];
      for (int j = 0; j < arrayOfInt.length; j++)
        arrayOfInt[j] = 2147483647;
      if (paramPdfFont2.getFontType() == 1217103210)
      {
        j = paramArrayOfString.length;
        for (int i1 = 0; i1 < j; i1++)
          if (paramArrayOfString[i1] != null)
          {
            int m = Integer.parseInt(paramArrayOfString[i1]);
            if (m > 0)
              arrayOfInt[i1] = m;
          }
      }
      else
      {
        getNonTTGlyphData(paramPdfJavaGlyphs, paramPdfFont1.is1C(), paramArrayOfString, arrayOfInt);
      }
      if (paramCollection != null)
      {
        Iterator localIterator1 = paramCollection.iterator();
        while (localIterator1.hasNext())
        {
          GlyphMapping localGlyphMapping = (GlyphMapping)localIterator1.next();
          arrayOfInt[localGlyphMapping.getOutputChar().codePointAt(0)] = localGlyphMapping.getGlyphNumber();
        }
      }
      i = createFormat4Map(arrayOfInt);
      int k = 0;
      StandardFonts.checkLoaded(k);
      Object localObject;
      int i3;
      for (int n = 0; n < paramPdfJavaGlyphs.getGlyphCount() + 1; n++)
      {
        localObject = null;
        if (paramPdfFont1.is1C())
          localObject = paramPdfJavaGlyphs.getIndexForCharString(n);
        else if (n < paramArrayOfString.length)
          localObject = paramArrayOfString[n];
        if (localObject != null)
        {
          int i2 = StandardFonts.lookupCharacterIndex((String)localObject, 0);
          i3 = n;
          if (paramPdfFont1.is1C())
          {
            i3--;
            if (n == 1)
              i3 = 1;
          }
          if ((i2 >= 0) && (i2 < 256))
            this.glyphIndexToChar[1][i2] = i3;
        }
      }
      if (paramCollection != null)
      {
        Iterator localIterator2 = paramCollection.iterator();
        while (localIterator2.hasNext())
        {
          localObject = (GlyphMapping)localIterator2.next();
          String str = StandardFonts.getNameFromUnicode(((GlyphMapping)localObject).getOutputChar());
          i3 = StandardFonts.lookupCharacterIndex(str, 0);
          if ((i3 >= 0) && (i3 < 256))
            this.glyphIndexToChar[1][i3] = (((GlyphMapping)localObject).getGlyphNumber() - 1);
        }
      }
    }
    this.CMAPlength = new int[] { i, 262, i };
    this.CMAPsubtables = new int[] { 28, 28 + i * 2, 28 + i };
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = this.numberSubtables;
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++)
      localArrayList.add(Integer.valueOf(j));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.id));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(i));
    for (j = 0; j < i; j++)
    {
      int k = ((Integer)localArrayList.get(j)).intValue();
      m = k < 0 ? 1 : 0;
      if (k < 0)
        k = -k;
      if (m != 0)
      {
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(3));
      }
      else
      {
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.platformID[k]));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.platformSpecificID[k]));
      }
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(this.CMAPsubtables[k]));
    }
    int[] arrayOfInt1 = new int[i];
    int[] arrayOfInt2 = new int[i];
    int n;
    for (int m = 0; m < i; m++)
    {
      n = ((Integer)localArrayList.get(m)).intValue();
      if (n < 0)
        n = -n;
      arrayOfInt1[n] = this.CMAPsubtables[n];
      arrayOfInt2[m] = n;
    }
    arrayOfInt2 = Sorts.quicksort(arrayOfInt1, arrayOfInt2);
    for (m = 0; m < i; m++)
    {
      n = arrayOfInt2[m];
      while (localByteArrayOutputStream.size() < this.CMAPsubtables[n])
        localByteArrayOutputStream.write(0);
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPformats[n]));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPlength[n]));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPlang[n]));
      int i1;
      if ((this.CMAPformats[n] == 0) && (this.CMAPlength[n] == 262))
      {
        for (i1 = 0; i1 < 256; i1++)
          localByteArrayOutputStream.write(TTFontWriter.setNextUint8(this.glyphIndexToChar[n][i1]));
      }
      else if (this.CMAPformats[n] == 4)
      {
        i1 = this.CMAPsegCount[n] / 2;
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPsegCount[n]));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPsearchRange[n]));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPentrySelector[n]));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPrangeShift[n]));
        for (int i2 = 0; i2 < i1; i2++)
          localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.endCode[i2]));
        localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.CMAPreserved[n]));
        for (i2 = 0; i2 < i1; i2++)
          localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.startCode[i2]));
        for (i2 = 0; i2 < i1; i2++)
          localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.idDelta[i2]));
        for (i2 = 0; i2 < i1; i2++)
          localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.idRangeOffset[i2]));
        if (this.glyphIdArray != null)
          for (int i5 : this.glyphIdArray)
            localByteArrayOutputStream.write(TTFontWriter.setNextUint16(i5));
      }
    }
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    case 0:
      i = this.minCharCode;
      break;
    case 1:
      i = this.maxCharCode;
    }
    return i;
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

  public int getMaxCharCode()
  {
    return this.maxCharCode;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.CMAPWriter
 * JD-Core Version:    0.6.2
 */
package org.jpedal.fonts.tt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.utils.LogWriter;

public class CMAP extends Table
{
  protected int[][] glyphIndexToChar;
  private int[] glyphToIndex;
  private boolean hasSix = false;
  private boolean hasFormat4 = false;
  private boolean hasFormat6 = false;
  private int lastFormat4Found = -1;
  private int firstCode = -1;
  private int entryCount = -1;
  private int winScore = 0;
  private int macScore = 0;
  private int segCount = 0;
  private int fontMapping = 0;
  protected int[] endCode;
  protected int[] startCode;
  protected int[] idDelta;
  protected int[] idRangeOffset;
  protected int[] glyphIdArray;
  private int[] f6glyphIdArray;
  private int[] offset;
  int nGroups;
  private int[] startCharCode;
  private int[] endCharCode;
  private int[] startGlyphCode;
  protected int[] CMAPformats;
  protected int[] CMAPlength;
  protected int[] CMAPlang;
  protected int[] CMAPsegCount;
  protected int[] CMAPsearchRange;
  protected int[] CMAPentrySelector;
  protected int[] CMAPrangeShift;
  protected int[] CMAPreserved;
  private boolean maybeWinEncoded = false;
  protected int[] platformID;
  private static Map exceptions = new HashMap();
  private int formatToUse;
  private int encodingToUse = 0;
  private static boolean WINchecked;
  protected int id;
  protected int numberSubtables;
  protected int[] CMAPsubtables;
  protected int[] platformSpecificID;

  public CMAP(FontFile2 paramFontFile2, int paramInt)
  {
    if (paramInt == 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No CMAP table found");
    }
    else
    {
      this.id = paramFontFile2.getNextUint16();
      this.numberSubtables = paramFontFile2.getNextUint16();
      this.CMAPsubtables = new int[this.numberSubtables];
      this.platformID = new int[this.numberSubtables];
      this.platformSpecificID = new int[this.numberSubtables];
      this.CMAPformats = new int[this.numberSubtables];
      this.CMAPsearchRange = new int[this.numberSubtables];
      this.CMAPentrySelector = new int[this.numberSubtables];
      this.CMAPrangeShift = new int[this.numberSubtables];
      this.CMAPreserved = new int[this.numberSubtables];
      this.CMAPsegCount = new int[this.numberSubtables];
      this.CMAPlength = new int[this.numberSubtables];
      this.CMAPlang = new int[this.numberSubtables];
      this.glyphIndexToChar = new int[this.numberSubtables][256];
      this.glyphToIndex = new int[256];
      for (int i = 0; i < this.numberSubtables; i++)
      {
        this.platformID[i] = paramFontFile2.getNextUint16();
        this.platformSpecificID[i] = paramFontFile2.getNextUint16();
        this.CMAPsubtables[i] = paramFontFile2.getNextUint32();
      }
      for (i = 0; i < this.numberSubtables; i++)
      {
        paramFontFile2.selectTable(2);
        paramFontFile2.skip(this.CMAPsubtables[i]);
        this.CMAPformats[i] = paramFontFile2.getNextUint16();
        this.CMAPlength[i] = paramFontFile2.getNextUint16();
        this.CMAPlang[i] = paramFontFile2.getNextUint16();
        if (this.CMAPformats[i] == 6)
          this.hasSix = true;
        int i1;
        int i2;
        int j;
        if ((this.CMAPformats[i] == 0) && (this.CMAPlength[i] == 262))
        {
          StandardFonts.checkLoaded(2);
          StandardFonts.checkLoaded(0);
          for (i1 = 0; i1 < 256; i1++)
          {
            i2 = paramFontFile2.getNextUint8();
            this.glyphIndexToChar[i][i1] = i2;
            this.glyphToIndex[i2] = i1;
            if (i2 > 0)
            {
              j = StandardFonts.isValidMacEncoding(i1);
              int m = StandardFonts.isValidWinEncoding(i1);
              if (j != m)
                this.maybeWinEncoded = true;
              if (j != 0)
                this.macScore += 1;
              if (m != 0)
                this.winScore += 1;
            }
          }
        }
        else
        {
          int k;
          if (this.CMAPformats[i] == 4)
          {
            this.CMAPsegCount[i] = paramFontFile2.getNextUint16();
            this.segCount = (this.CMAPsegCount[i] / 2);
            this.CMAPsearchRange[i] = paramFontFile2.getNextUint16();
            this.CMAPentrySelector[i] = paramFontFile2.getNextUint16();
            this.CMAPrangeShift[i] = paramFontFile2.getNextUint16();
            if (this.hasFormat4)
            {
              if (this.CMAPlength[this.lastFormat4Found] > this.CMAPlength[i])
              {
                this.CMAPlength[i] = this.CMAPlength[this.lastFormat4Found];
                this.CMAPsegCount[i] = this.CMAPsegCount[this.lastFormat4Found];
                this.CMAPsearchRange[i] = this.CMAPsearchRange[this.lastFormat4Found];
                this.CMAPentrySelector[i] = this.CMAPentrySelector[this.lastFormat4Found];
                this.CMAPrangeShift[i] = this.CMAPrangeShift[this.lastFormat4Found];
                continue;
              }
              if (this.CMAPlength[this.lastFormat4Found] < this.CMAPlength[i])
              {
                this.CMAPlength[this.lastFormat4Found] = this.CMAPlength[i];
                this.CMAPsegCount[this.lastFormat4Found] = this.CMAPsegCount[i];
                this.CMAPsearchRange[this.lastFormat4Found] = this.CMAPsearchRange[i];
                this.CMAPentrySelector[this.lastFormat4Found] = this.CMAPentrySelector[i];
                this.CMAPrangeShift[this.lastFormat4Found] = this.CMAPrangeShift[i];
              }
            }
            this.lastFormat4Found = i;
            this.hasFormat4 = true;
            this.endCode = new int[this.segCount];
            for (j = 0; j < this.segCount; j++)
              this.endCode[j] = paramFontFile2.getNextUint16();
            this.CMAPreserved[i] = paramFontFile2.getNextUint16();
            this.startCode = new int[this.segCount];
            for (k = 0; k < this.segCount; k++)
              this.startCode[k] = paramFontFile2.getNextUint16();
            this.idDelta = new int[this.segCount];
            for (k = 0; k < this.segCount; k++)
              this.idDelta[k] = paramFontFile2.getNextUint16();
            this.idRangeOffset = new int[this.segCount];
            for (k = 0; k < this.segCount; k++)
              this.idRangeOffset[k] = paramFontFile2.getNextUint16();
            this.offset = new int[this.segCount];
            int n = 0;
            for (i1 = 0; i1 < this.segCount; i1++)
              if (this.idDelta[i1] == 0)
              {
                this.offset[i1] = n;
                k = 1 + this.endCode[i1] - this.startCode[i1];
                if ((this.startCode[i1] == this.endCode[i1]) && (this.idRangeOffset[i1] == 0))
                  k = 0;
                n += k;
              }
            i1 = (this.CMAPlength[i] - 16 - this.segCount * 8) / 2;
            this.glyphIdArray = new int[i1];
            for (i2 = 0; i2 < i1; i2++)
              this.glyphIdArray[i2] = paramFontFile2.getNextUint16();
          }
          else if (this.CMAPformats[i] == 6)
          {
            this.hasFormat6 = true;
            this.firstCode = paramFontFile2.getNextUint16();
            this.entryCount = paramFontFile2.getNextUint16();
            this.f6glyphIdArray = new int[this.firstCode + this.entryCount];
            for (k = 0; k < this.entryCount; k++)
              this.f6glyphIdArray[(k + this.firstCode)] = paramFontFile2.getNextUint16();
          }
          else if (this.CMAPformats[i] == 12)
          {
            paramFontFile2.getNextUint16();
            paramFontFile2.getNextUint32();
            this.nGroups = paramFontFile2.getNextUint32();
            this.startCharCode = new int[this.nGroups];
            this.endCharCode = new int[this.nGroups];
            this.startGlyphCode = new int[this.nGroups];
            for (k = 0; k < this.nGroups; k++)
            {
              this.startCharCode[k] = paramFontFile2.getNextUint32();
              this.endCharCode[k] = paramFontFile2.getNextUint32();
              this.startGlyphCode[k] = paramFontFile2.getNextUint32();
            }
          }
          else
          {
            this.CMAPformats[i] = -1;
          }
        }
      }
    }
  }

  public CMAP()
  {
  }

  public int convertIndexToCharacterCode(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString)
  {
    int i = -1;
    int j = paramInt;
    int k = this.CMAPformats[this.formatToUse];
    if (((this.fontMapping == 1) || ((!paramBoolean1) && (this.fontMapping == 4))) && (paramString != null) && (!"notdef".equals(paramString)))
    {
      paramInt = StandardFonts.getAdobeMap(paramString);
    }
    else if ((this.fontMapping == 3) && (paramArrayOfString != null) && (paramArrayOfString[paramInt] != null))
    {
      paramInt = StandardFonts.getAdobeMap(paramString);
      if (paramInt < 0)
        return -1;
      if (paramInt > 255)
        k = 4;
    }
    else if (this.fontMapping == 2)
    {
      StandardFonts.checkLoaded(this.encodingToUse);
      if (this.encodingToUse == 0)
      {
        Object localObject = null;
        if (paramString != null)
          localObject = exceptions.get(paramString);
        if (localObject == null)
        {
          if ((paramString != null) && (!paramBoolean2))
            paramInt = StandardFonts.lookupCharacterIndex(paramString, this.encodingToUse);
        }
        else if ((paramArrayOfString == null) || (paramArrayOfString[paramInt] == null))
          try
          {
            paramInt = Integer.parseInt((String)localObject);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
        if (paramString != null)
        {
          if (!WINchecked)
          {
            StandardFonts.checkLoaded(2);
            WINchecked = true;
          }
          i = StandardFonts.lookupCharacterIndex(paramString, 2);
        }
      }
      else if (paramString != null)
      {
        paramInt = StandardFonts.lookupCharacterIndex(paramString, this.encodingToUse);
      }
    }
    int m = -1;
    if ((paramBoolean1) && (k > 0) && (k != 6))
      paramInt += 61440;
    if (k == 0)
    {
      if (paramInt > 255)
        paramInt = 0;
      m = this.glyphIndexToChar[this.formatToUse][paramInt];
      if ((m == 0) && (i != -1))
        m = this.glyphIndexToChar[this.formatToUse][i];
      if ((j == 128) && (this.endCode != null) && ("Euro".equals(paramString)))
        m = getFormat4Value(8364, false, m);
    }
    else if (k == 4)
    {
      m = getFormat4Value(paramInt, false, m);
      if (m == -1)
        if (paramInt > 61440)
          m = getFormat4Value(paramInt - 61440, false, m);
        else
          m = getFormat4Value(paramInt + 61440, false, m);
    }
    else if (k == 12)
    {
      m = getFormat12Value(paramInt, false, m);
    }
    if ((m == -1) && (this.hasSix))
    {
      paramInt = j;
      k = 6;
    }
    if (k == 6)
    {
      if (!paramBoolean1)
        paramInt = StandardFonts.lookupCharacterIndex(paramString, this.encodingToUse);
      if (paramInt >= this.f6glyphIdArray.length)
        m = 0;
      else
        m = this.f6glyphIdArray[paramInt];
    }
    return m;
  }

  private int getFormat12Value(int paramInt1, boolean paramBoolean, int paramInt2)
  {
    for (int i = 0; i < this.nGroups; i++)
    {
      if (paramBoolean)
        System.out.println("table=" + i + " start=" + this.startCharCode[i] + ' ' + paramInt1 + " end=" + this.endCharCode[i] + " glypgStartCode[i]=" + this.startGlyphCode[i]);
      if ((this.endCharCode[i] >= paramInt1) && (this.startCharCode[i] <= paramInt1))
      {
        paramInt2 = this.startGlyphCode[i] + paramInt1 - this.startCharCode[i];
        i = this.nGroups;
      }
    }
    return paramInt2;
  }

  private int getFormat4Value(int paramInt1, boolean paramBoolean, int paramInt2)
  {
    for (int i = 0; i < this.segCount; i++)
    {
      if (paramBoolean)
        System.out.println("Segtable=" + i + " start=" + this.startCode[i] + ' ' + paramInt1 + " end=" + this.endCode[i] + " idRangeOffset[i]=" + this.idRangeOffset[i] + " offset[i]=" + this.offset[i] + " idRangeOffset[i]=" + this.idRangeOffset[i] + " idDelta[i]=" + this.idDelta[i]);
      if ((this.endCode[i] >= paramInt1) && (this.startCode[i] <= paramInt1))
        if (this.idRangeOffset[i] == 0)
        {
          if (paramBoolean)
            System.out.println("xxx=" + (this.idDelta[i] + paramInt1));
          paramInt2 = (this.idDelta[i] + paramInt1) % 65536;
          i = this.segCount;
        }
        else
        {
          int j = this.offset[i] + (paramInt1 - this.startCode[i]);
          paramInt2 = this.glyphIdArray[j];
          if (paramBoolean)
            System.out.println("value=" + paramInt2 + " idx=" + j + " glyphIdArrays=" + this.glyphIdArray[0] + ' ' + this.glyphIdArray[1] + ' ' + this.glyphIdArray[2] + " offset[i]=" + this.offset[i] + " index=" + paramInt1 + " startCode[" + i + "]=" + this.startCode[i] + " i=" + i);
          i = this.segCount;
        }
    }
    return paramInt2;
  }

  public void setEncodingToUse(boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.formatToUse = -1;
    int i = this.platformID.length;
    if ((!paramBoolean2) && (this.macScore < 207) && ((this.glyphToIndex == null) || (this.macScore <= 90) || (this.maybeWinEncoded)) && ((this.glyphToIndex == null) || (this.macScore <= 205) || (this.glyphToIndex[''] == 0) || (this.glyphToIndex['ä'] != 0)))
    {
      if ((i > 0) && (this.winScore > this.macScore))
        this.encodingToUse = 2;
      if ((this.macScore > 80) && (paramBoolean1) && (paramInt == 2) && (this.winScore >= this.macScore))
        this.encodingToUse = 2;
    }
    for (int j = 0; j < i; j++)
      if ((this.platformID[j] == 3) && ((this.CMAPformats[j] == 1) || (this.CMAPformats[j] == 0)))
      {
        this.formatToUse = j;
        this.fontMapping = 1;
        j = i;
      }
    j = 0;
    if ((this.formatToUse == -1) && (this.CMAPformats[0] != 12) && (((this.macScore > 0) && (this.winScore > 0)) || ((this.CMAPformats.length == 1) && (!paramBoolean3) && ((!paramBoolean2) || ((this.CMAPformats.length == 1) && (this.CMAPformats[0] == 0))))))
      for (k = 0; k < i; k++)
        if ((this.platformID[k] == 1) && (this.CMAPformats[k] == 0))
        {
          this.formatToUse = k;
          if ((paramBoolean1) || (paramInt == 2))
            this.fontMapping = 2;
          else
            this.fontMapping = 3;
          k = i;
          j = 1;
        }
    int k = 0;
    int m;
    if (this.formatToUse == -1)
      for (m = 0; m < i; m++)
        if (this.CMAPformats[m] == 6)
        {
          this.formatToUse = m;
          if ((!paramBoolean1) && (paramInt == 2))
          {
            this.fontMapping = 2;
            StandardFonts.checkLoaded(0);
          }
          else
          {
            this.fontMapping = 6;
          }
          k = 1;
          m = i;
        }
    if ((this.formatToUse == -1) || (k != 0) || ((j != 0) && ((this.glyphIndexToChar[this.formatToUse]['ß'] == 0) || (getFormat4Value(223, false, 0) != 0))))
      for (m = 0; m < i; m++)
        if (this.CMAPformats[m] == 4)
        {
          this.formatToUse = m;
          this.fontMapping = 4;
          m = i;
        }
    if (this.formatToUse == -1)
      for (m = 0; m < i; m++)
        if (this.CMAPformats[m] == 12)
        {
          this.formatToUse = m;
          if ((!paramBoolean1) && (paramInt == 2))
          {
            this.fontMapping = 2;
            StandardFonts.checkLoaded(0);
          }
          else
          {
            this.fontMapping = 12;
          }
          m = i;
        }
    if (paramInt == 5)
      this.fontMapping = 2;
    if ((this.encodingToUse == 2) && (this.macScore == this.winScore) && (this.glyphIndexToChar[this.formatToUse][''] == 0) && (this.glyphIndexToChar[this.formatToUse]['Õ'] != 0))
      this.encodingToUse = 0;
    if ((this.encodingToUse == 2) && (this.macScore == this.winScore) && (this.glyphIndexToChar[this.formatToUse]['ä'] == 0) && (this.glyphIndexToChar[this.formatToUse][''] != 0))
      this.encodingToUse = 0;
  }

  public Map buildCharStringTable()
  {
    HashMap localHashMap = new HashMap();
    if (this.hasFormat4)
    {
      ArrayList localArrayList = new ArrayList();
      for (int j = 0; j < this.segCount; j++)
      {
        int k = this.endCode[j] - this.startCode[j] + 1;
        for (int m = 0; m < k; m++)
          localArrayList.add(Integer.valueOf(this.startCode[j] + m));
      }
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        Integer localInteger = (Integer)localIterator.next();
        localHashMap.put(localInteger, Integer.valueOf(getFormat4Value(localInteger.intValue(), false, 0)));
      }
    }
    else
    {
      int i;
      if (this.hasFormat6)
        for (i = 0; i < this.entryCount; i++)
          localHashMap.put(Integer.valueOf(this.firstCode + i), Integer.valueOf(this.f6glyphIdArray[(this.firstCode + i)]));
      else
        for (i = 0; i < this.glyphToIndex.length; i++)
          if (this.glyphToIndex[i] > 0)
            localHashMap.put(Integer.valueOf(this.glyphToIndex[i]), Integer.valueOf(i));
    }
    return localHashMap;
  }

  public boolean hasFormat4()
  {
    return this.hasFormat4;
  }

  static
  {
    String[] arrayOfString = { "notequal", "173", "infinity", "176", "lessequal", "178", "greaterequal", "179", "partialdiff", "182", "summation", "183", "product", "184", "pi", "185", "integral", "186", "Omega", "189", "radical", "195", "approxequal", "197", "Delta", "198", "lozenge", "215", "Euro", "219", "apple", "240" };
    int i = 0;
    while (i < arrayOfString.length)
    {
      exceptions.put(arrayOfString[i], arrayOfString[(i + 1)]);
      i += 2;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.CMAP
 * JD-Core Version:    0.6.2
 */
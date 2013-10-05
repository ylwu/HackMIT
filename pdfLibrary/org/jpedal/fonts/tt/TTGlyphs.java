package org.jpedal.fonts.tt;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.fonts.tt.hinting.TTVM;
import org.jpedal.utils.LogWriter;

public class TTGlyphs extends PdfJavaGlyphs
{
  protected boolean hasGIDtoCID;
  protected int[] CIDToGIDMap;
  float[] FontBBox = { 0.0F, 0.0F, 1000.0F, 1000.0F };
  private String[] diffTable = null;
  boolean isCorrupted = false;
  private CMAP currentCMAP;
  private Post currentPost;
  private Glyf currentGlyf;
  private Hmtx currentHmtx;
  private Hhea currentHhea;
  private FontFile2 fontTable;
  private Loca currentLoca;
  private TTVM vm;
  private CFF currentCFF;
  int type = 1217103210;
  private int unitsPerEm;
  private boolean hasCFF;
  private boolean isCID;

  public PdfGlyph getEmbeddedGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3)
  {
    int i = paramInt;
    if (this.hasGIDtoCID)
      paramInt = this.CIDToGIDMap[paramInt];
    if (((this.lastTrm[0][0] != paramArrayOfFloat[0][0] ? 1 : 0) | (this.lastTrm[1][0] != paramArrayOfFloat[1][0] ? 1 : 0) | (this.lastTrm[0][1] != paramArrayOfFloat[0][1] ? 1 : 0) | (this.lastTrm[1][1] != paramArrayOfFloat[1][1] ? 1 : 0)) != 0)
    {
      this.lastTrm = paramArrayOfFloat;
      flush();
    }
    PdfGlyph localPdfGlyph = getEmbeddedCachedShape(i);
    if (localPdfGlyph == null)
    {
      int j = paramInt;
      if (((!this.isCID) || (!isIdentity())) && (this.currentCMAP != null))
        j = this.currentCMAP.convertIndexToCharacterCode(paramString1, paramInt, this.remapFont, this.isSubsetted, this.diffTable);
      if (j < 1)
        j = this.currentPost.convertGlyphToCharacterCode(paramString1);
      try
      {
        if (this.hasCFF)
        {
          localPdfGlyph = this.currentCFF.getCFFGlyph(paramGlyphFactory, paramString1, paramArrayOfFloat, j, paramString2, paramFloat, paramString3);
          if (localPdfGlyph != null)
            localPdfGlyph.setWidth(getUnscaledWidth(paramString1, paramInt, false));
          if (localPdfGlyph != null)
            localPdfGlyph.setID(paramInt);
        }
        else
        {
          localPdfGlyph = getTTGlyph(j, paramString1, paramInt, paramString2, paramGlyphFactory);
        }
      }
      catch (Exception localException)
      {
        localPdfGlyph = null;
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
      setEmbeddedCachedShape(i, localPdfGlyph);
    }
    return localPdfGlyph;
  }

  private PdfGlyph getTTGlyph(int paramInt1, String paramString1, int paramInt2, String paramString2, GlyphFactory paramGlyphFactory)
  {
    if (this.isCorrupted)
      paramInt1 = paramInt2;
    Object localObject = null;
    try
    {
      BaseTTGlyph.debug = false;
      if (paramInt1 != -1)
      {
        int i = this.currentGlyf.getCharString(paramInt1);
        if (i != -1)
        {
          if (paramGlyphFactory.useFX())
          {
            if (TTGlyph.useHinting)
              localObject = new TTGlyphFX(paramString1, this.currentGlyf, this.fontTable, this.currentHmtx, paramInt1, this.unitsPerEm / 1000.0F, this.vm);
            else
              localObject = new TTGlyphFX(paramString1, this.currentGlyf, this.fontTable, this.currentHmtx, paramInt1, this.unitsPerEm / 1000.0F, this.baseFontName);
          }
          else if (TTGlyph.useHinting)
            localObject = new TTGlyph(paramString1, this.currentGlyf, this.fontTable, this.currentHmtx, paramInt1, this.unitsPerEm / 1000.0F, this.vm);
          else
            localObject = new TTGlyph(paramString1, this.currentGlyf, this.fontTable, this.currentHmtx, paramInt1, this.unitsPerEm / 1000.0F, this.baseFontName);
          if (BaseTTGlyph.debug)
            System.out.println(">>" + i + ' ' + paramInt2 + ' ' + paramString2 + ' ' + this.baseFontName);
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace(System.out);
    }
    return localObject;
  }

  public void setDiffValues(String[] paramArrayOfString)
  {
    this.diffTable = paramArrayOfString;
  }

  public void setEncodingToUse(boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (this.currentCMAP != null)
      if (this.isCorrupted)
        this.currentCMAP.setEncodingToUse(paramBoolean1, paramInt, true, paramBoolean3);
      else
        this.currentCMAP.setEncodingToUse(paramBoolean1, paramInt, paramBoolean2, paramBoolean3);
  }

  public int getConvertedGlyph(int paramInt)
  {
    if (this.currentCMAP == null)
      return paramInt;
    return this.currentCMAP.convertIndexToCharacterCode(null, paramInt, false, false, this.diffTable);
  }

  public Map getCharStrings()
  {
    if (this.currentCMAP != null)
      return this.currentCMAP.buildCharStringTable();
    return this.currentGlyf.buildCharStringTable();
  }

  public float getTTWidth(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
  {
    int i = paramInt;
    float f = 0.0F;
    try
    {
      if (!paramBoolean)
        i = this.currentCMAP.convertIndexToCharacterCode(paramString1, paramInt, this.remapFont, this.isSubsetted, this.diffTable);
      if (i < 1)
        i = this.currentPost.convertGlyphToCharacterCode(paramString1);
      f = this.currentHmtx.getWidth(i);
    }
    catch (Exception localException)
    {
    }
    return f;
  }

  private float getUnscaledWidth(String paramString, int paramInt, boolean paramBoolean)
  {
    int i = paramInt;
    float f = 0.0F;
    try
    {
      if (!paramBoolean)
        i = this.currentCMAP.convertIndexToCharacterCode(paramString, paramInt, this.remapFont, this.isSubsetted, this.diffTable);
      if (i < 1)
        i = this.currentPost.convertGlyphToCharacterCode(paramString);
      f = this.currentHmtx.getUnscaledWidth(i);
    }
    catch (Exception localException)
    {
    }
    return f;
  }

  public void setGIDtoCID(int[] paramArrayOfInt)
  {
    this.hasGIDtoCID = true;
    this.CIDToGIDMap = paramArrayOfInt;
  }

  public static String[] readFontNames(FontData paramFontData, int paramInt)
  {
    FontFile2 localFontFile2 = new FontFile2(paramFontData);
    int i = localFontFile2.getFontCount();
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
    {
      localFontFile2.setSelectedFontIndex(j);
      Name localName = new Name(localFontFile2);
      String str;
      if (paramInt == 2)
        str = localName.getString(Name.POSTSCRIPT_NAME);
      else if (paramInt == 3)
        str = localName.getString(Name.FONT_FAMILY_NAME);
      else if (paramInt == 4)
        str = localName.getString(Name.FULL_FONT_NAME);
      else
        throw new RuntimeException("Unsupported mode " + paramInt + ". Unable to resolve font names");
      if (str == null)
        arrayOfString[j] = null;
      else
        arrayOfString[j] = str.toLowerCase();
    }
    if (paramFontData != null)
      paramFontData.close();
    return arrayOfString;
  }

  public static void addStringValues(FontData paramFontData, Map paramMap)
  {
    FontFile2 localFontFile2 = new FontFile2(paramFontData);
    int i = localFontFile2.getFontCount();
    for (int j = 0; j < i; j++)
    {
      localFontFile2.setSelectedFontIndex(j);
      Name localName = new Name(localFontFile2);
      Map localMap = localName.getStrings();
      if (localMap != null)
      {
        Iterator localIterator = localMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          Integer localInteger = (Integer)localObject;
          int k = localInteger.intValue();
          if (k < Name.stringNames.length)
            paramMap.put(Name.stringNames[localInteger.intValue()], localMap.get(localInteger));
        }
      }
    }
    if (paramFontData != null)
      paramFontData.close();
  }

  public int readEmbeddedFont(boolean paramBoolean, byte[] paramArrayOfByte, FontData paramFontData)
  {
    this.isCID = paramBoolean;
    FontFile2 localFontFile2;
    if (paramArrayOfByte != null)
      localFontFile2 = new FontFile2(paramArrayOfByte);
    else
      localFontFile2 = new FontFile2(paramFontData);
    if (FontMappings.fontSubstitutionFontID == null)
    {
      localFontFile2.setPointer(0);
    }
    else
    {
      localObject = (Integer)FontMappings.fontSubstitutionFontID.get(this.fontName.toLowerCase());
      if (localObject != null)
        localFontFile2.setPointer(((Integer)localObject).intValue());
      else
        localFontFile2.setPointer(0);
    }
    Object localObject = new Head(localFontFile2);
    this.currentPost = new Post(localFontFile2);
    Maxp localMaxp = new Maxp(localFontFile2);
    this.glyphCount = localMaxp.getGlyphCount();
    this.currentLoca = new Loca(localFontFile2, this.glyphCount, ((Head)localObject).getFormat());
    this.isCorrupted = this.currentLoca.isCorrupted();
    this.currentGlyf = new Glyf(localFontFile2, this.glyphCount, this.currentLoca.getIndices());
    this.currentCFF = new CFF(localFontFile2, this.isCID);
    this.hasCFF = this.currentCFF.hasCFFData();
    if (this.hasCFF)
      this.type = 6;
    if (TTGlyph.useHinting)
      this.vm = new TTVM(localFontFile2, localMaxp);
    this.currentHhea = new Hhea(localFontFile2);
    this.FontBBox = ((Head)localObject).getFontBBox();
    this.currentHmtx = new Hmtx(localFontFile2, this.glyphCount, this.currentHhea.getNumberOfHMetrics(), (int)this.FontBBox[3]);
    int i = localFontFile2.selectTable(2);
    if (i != 0)
      this.currentCMAP = new CMAP(localFontFile2, i);
    this.unitsPerEm = ((Head)localObject).getUnitsPerEm();
    this.fontTable = new FontFile2(this.currentGlyf.getTableData(), true);
    if (paramFontData != null)
      paramFontData.close();
    return this.type;
  }

  public float[] getFontBoundingBox()
  {
    return this.FontBBox;
  }

  public int getType()
  {
    return this.type;
  }

  public boolean isCorrupted()
  {
    return this.isCorrupted;
  }

  public void setCorrupted(boolean paramBoolean)
  {
    this.isCorrupted = paramBoolean;
  }

  public Table getTable(int paramInt)
  {
    Object localObject;
    switch (paramInt)
    {
    case 3:
      localObject = this.currentLoca;
      break;
    case 2:
      localObject = this.currentCMAP;
      break;
    case 5:
      localObject = this.currentHhea;
      break;
    case 6:
      localObject = this.currentHmtx;
      break;
    case 4:
    default:
      throw new RuntimeException("table not yet added to getTable)");
    }
    return localObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.TTGlyphs
 * JD-Core Version:    0.6.2
 */
package org.jpedal.fonts.tt.conversion;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.TrueType;
import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1GlyphFactory;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.fonts.tt.Hhea;
import org.jpedal.fonts.tt.Hmtx;
import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.utils.LogWriter;

public class PS2OTFFontWriter extends FontWriter
{
  byte[] cff;
  PdfFont pdfFont;
  PdfFont originalFont;
  PdfJavaGlyphs glyphs;
  int minCharCode;
  int maxCharCode;
  private int xAvgCharWidth = 0;
  private double xMaxExtent = 4.9E-324D;
  private double minRightSideBearing = 1.7976931348623157E+308D;
  private double minLeftSideBearing = 1.7976931348623157E+308D;
  private double advanceWidthMax = 4.9E-324D;
  private double lowestDescender = -1.0D;
  private double highestAscender = 1.0D;
  private float[] fontBBox = new float[4];
  private double emSquareSize = 1000.0D;
  private int[] advanceWidths;
  private int[] lsbs = null;
  private HashMap<String, Integer> widthMap;
  private Collection<GlyphMapping> mappings;
  private String[] glyphList = null;
  private double headEmSquare = 1024.0D;
  FontFile2 orginTTTables = null;

  public PS2OTFFontWriter(PdfFont paramPdfFont, byte[] paramArrayOfByte, String paramString, HashMap<String, Integer> paramHashMap, Collection<GlyphMapping> paramCollection)
    throws Exception
  {
    boolean bool = paramString.equals("cff");
    this.name = paramPdfFont.getBaseFontName();
    this.widthMap = paramHashMap;
    this.mappings = paramCollection;
    String[] arrayOfString = { "CFF ", "OS/2", "cmap", "head", "hhea", "hmtx", "maxp", "name", "post" };
    if (paramString.equals("ttf"))
    {
      this.subType = 11;
      this.glyphs = paramPdfFont.getGlyphData();
      this.pdfFont = new TrueType();
      this.orginTTTables = new FontFile2(paramArrayOfByte);
      arrayOfString = new String[] { "OS/2", "cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name", "post", "prep" };
      for (i = 0; i < arrayOfString.length; i++)
        this.IDtoTable.put(arrayOfString[i], Integer.valueOf(i));
    }
    else
    {
      this.glyphs = new T1Glyphs(false, bool);
      this.pdfFont = new Type1C(paramArrayOfByte, this.glyphs, bool);
    }
    this.glyphCount = this.glyphs.getGlyphCount();
    this.originalFont = paramPdfFont;
    this.cff = paramArrayOfByte;
    this.tableList = new ArrayList();
    this.numTables = arrayOfString.length;
    int i = 1;
    while (i * 2 <= this.numTables)
      i *= 2;
    this.searchRange = (i * 16);
    for (this.entrySelector = 0; Math.pow(2.0D, this.entrySelector) < i; this.entrySelector += 1);
    this.rangeShift = (this.numTables * 16 - this.searchRange);
    this.tableList.addAll(Arrays.asList(arrayOfString).subList(0, this.numTables));
    this.checksums = new int[this.tableCount][1];
    this.tables = new int[this.tableCount][1];
    this.tableLength = new int[this.tableCount][1];
    this.type = 1;
  }

  void readTables()
  {
    int i = 0;
    this.advanceWidths = new int[this.glyphCount];
    if (this.widthMap != null)
      for (int j = 0; j < this.glyphCount; j++)
      {
        Integer localInteger1;
        if (this.pdfFont.isCIDFont())
          localInteger1 = (Integer)this.widthMap.get(this.glyphs.getCharGlyph(Integer.valueOf(j + 1)));
        else
          localInteger1 = (Integer)this.widthMap.get(this.glyphs.getIndexForCharString(j + 1));
        if (localInteger1 != null)
        {
          this.advanceWidths[j] = localInteger1.intValue();
        }
        else if (this.pdfFont.is1C())
        {
          k = 0;
          if (((Type1C)this.pdfFont).getFDSelect() != null)
            k = ((Type1C)this.pdfFont).getFDSelect()[j];
          Integer localInteger2 = (Integer)this.widthMap.get("JPedalDefaultWidth" + k);
          if (localInteger2 != null)
            this.advanceWidths[j] = localInteger2.intValue();
        }
        this.advanceWidthMax = (this.advanceWidthMax > this.advanceWidths[j] ? this.advanceWidthMax : this.advanceWidths[j]);
        i += this.advanceWidths[j];
      }
    if (this.glyphCount > 0)
      this.xAvgCharWidth = ((int)(i / this.glyphCount));
    double d1 = 4.9E-324D;
    int k = this.glyphCount;
    if ((this.originalFont.getCIDToGIDMap() != null) && (this.originalFont.getCIDToGIDMap().length < k))
      k = this.originalFont.getCIDToGIDMap().length;
    for (int m = 0; (m < k) && (m < 256); m++)
    {
      PdfGlyph localPdfGlyph = this.glyphs.getEmbeddedGlyph(new T1GlyphFactory(false), this.pdfFont.getMappedChar(m, false), new float[][] { { 1.0F, 0.0F }, { 0.0F, 1.0F } }, m, this.pdfFont.getGlyphValue(m), this.pdfFont.getWidth(m), this.pdfFont.getMappedChar(m, false));
      if ((localPdfGlyph != null) && (localPdfGlyph.getShape() != null))
      {
        Rectangle2D localRectangle2D = localPdfGlyph.getShape().getBounds2D();
        double d2 = localRectangle2D.getMinX();
        double d3 = this.advanceWidths[m] - localRectangle2D.getMaxX();
        double d4 = localRectangle2D.getMinX() + localRectangle2D.getWidth();
        this.minLeftSideBearing = (this.minLeftSideBearing < d2 ? this.minLeftSideBearing : d2);
        this.minRightSideBearing = (this.minRightSideBearing < d3 ? this.minRightSideBearing : d3);
        this.xMaxExtent = (this.xMaxExtent > d4 ? this.xMaxExtent : d4);
        this.lowestDescender = (this.lowestDescender < localRectangle2D.getMinY() ? this.lowestDescender : localRectangle2D.getMinY());
        this.highestAscender = (this.highestAscender > localRectangle2D.getMaxY() ? this.highestAscender : localRectangle2D.getMaxY());
        d1 = d1 > localRectangle2D.getMaxX() ? d1 : localRectangle2D.getMaxX();
      }
    }
    if (this.originalFont.is1C())
    {
      this.fontBBox = this.pdfFont.FontBBox;
      if (this.fontBBox[2] > 2048.0F)
        this.headEmSquare = 2048.0D;
    }
    else
    {
      this.fontBBox = this.originalFont.getFontBounds();
    }
    this.minLeftSideBearing = (this.minLeftSideBearing < this.fontBBox[0] ? this.minLeftSideBearing : this.fontBBox[0]);
    this.lowestDescender = (this.lowestDescender < this.fontBBox[1] ? this.lowestDescender : this.fontBBox[1]);
    d1 = d1 > this.fontBBox[2] ? d1 : this.fontBBox[2];
    this.highestAscender = (this.highestAscender > this.fontBBox[3] ? this.highestAscender : this.fontBBox[3]);
    this.fontBBox = new float[] { (float)this.minLeftSideBearing, (float)this.lowestDescender, (float)d1, (float)this.highestAscender };
  }

  public byte[] getTableBytes(int paramInt)
  {
    Object localObject1 = new byte[0];
    Object localObject2 = null;
    Object localObject3;
    switch (paramInt)
    {
    case 16:
      if (this.pdfFont.is1C())
      {
        localObject1 = new CFFFixer(this.cff).getBytes();
      }
      else
      {
        localObject2 = new CFFWriter(this.glyphs, this.name);
        localObject3 = (CFFWriter)localObject2;
        this.glyphList = ((CFFWriter)localObject3).getGlyphList();
        this.advanceWidths = ((CFFWriter)localObject3).getWidths();
        this.lsbs = ((CFFWriter)localObject3).getBearings();
        this.fontBBox = ((CFFWriter)localObject3).getBBox();
        this.emSquareSize = ((CFFWriter)localObject3).getEmSquareSize();
        this.highestAscender = this.fontBBox[3];
        this.lowestDescender = this.fontBBox[1];
        this.advanceWidthMax = 0.0D;
        int i = 0;
        for (int j = 0; j < this.advanceWidths.length; j++)
        {
          this.advanceWidthMax = (this.advanceWidthMax > this.advanceWidths[j] ? this.advanceWidthMax : this.advanceWidths[j]);
          i += this.advanceWidths[j];
          this.minLeftSideBearing = (this.minLeftSideBearing < this.lsbs[j] ? this.minLeftSideBearing : this.lsbs[j]);
        }
        if (this.glyphCount > 0)
          this.xAvgCharWidth = ((int)(i / this.glyphCount));
      }
      break;
    case 0:
      if (this.subType == 11)
        localObject1 = this.orginTTTables.getTableBytes(0);
      else
        localObject2 = new HeadWriter(this.fontBBox, this.headEmSquare);
      break;
    case 2:
      localObject2 = new CMAPWriter(this.name, this.pdfFont, this.originalFont, this.glyphs, this.glyphList, this.mappings);
      if (this.subType != 11)
      {
        this.minCharCode = ((FontTableWriter)localObject2).getIntValue(0);
        this.maxCharCode = ((FontTableWriter)localObject2).getIntValue(1);
      }
      break;
    case 4:
      localObject1 = this.orginTTTables.getTableBytes(4);
      break;
    case 5:
      if (this.subType == 11)
      {
        localObject3 = (TTGlyphs)this.glyphs;
        Hmtx localHmtx = (Hmtx)((TTGlyphs)localObject3).getTable(6);
        Hhea localHhea = (Hhea)((TTGlyphs)localObject3).getTable(5);
        int k = 0;
        int m = localHhea.getIntValue(4);
        if (m == 0)
        {
          for (int n = 0; n < this.glyphs.getGlyphCount(); n++)
          {
            int i1 = (int)localHmtx.getUnscaledWidth(n);
            m = i1 > m ? i1 : m;
          }
          if (m != localHhea.getIntValue(4))
            k = 1;
        }
        if (k != 0)
          localObject2 = new HheaWriter(this.glyphs, localHhea.getIntValue(7), localHhea.getIntValue(6), localHhea.getIntValue(6), m, localHhea.getIntValue(2), localHhea.getIntValue(1));
        else
          localObject1 = this.orginTTTables.getTableBytes(5);
      }
      else
      {
        localObject2 = new HheaWriter(this.glyphs, this.xMaxExtent, this.minRightSideBearing, this.minLeftSideBearing, this.advanceWidthMax, this.lowestDescender, this.highestAscender);
      }
      break;
    case 6:
      if (this.subType == 11)
        localObject1 = this.orginTTTables.getTableBytes(6);
      else
        localObject2 = new HmtxWriter(this.glyphs, this.advanceWidths, this.lsbs);
      break;
    case 3:
      if (this.subType == 11)
        localObject1 = this.orginTTTables.getTableBytes(3);
      else
        localObject2 = new LocaWriter(this.originalFont);
      break;
    case 13:
      if (this.subType == 11)
      {
        localObject3 = this.orginTTTables.getTableBytes(13);
        if (localObject3.length > 0)
          localObject1 = localObject3;
        else
          localObject2 = new OS2Writer(this.originalFont, this.glyphs, this.xAvgCharWidth, this.minCharCode, this.maxCharCode, this.fontBBox, this.emSquareSize);
      }
      else
      {
        localObject2 = new OS2Writer(this.originalFont, this.glyphs, this.xAvgCharWidth, this.minCharCode, this.maxCharCode, this.fontBBox, this.emSquareSize);
      }
      break;
    case 1:
      if (this.subType == 11)
        localObject1 = this.orginTTTables.getTableBytes(1);
      else
        localObject2 = new MAXPWriter(this.glyphs);
      break;
    case 7:
      if (this.subType == 11)
        localObject2 = new NameWriter(this.pdfFont, this.glyphs, this.name);
      else
        localObject2 = new NameWriter(this.pdfFont, this.glyphs, this.name);
      this.styleName = ((NameWriter)localObject2).getString(2);
      break;
    case 8:
      if (this.subType == 11)
      {
        localObject3 = this.orginTTTables.getTableBytes(8);
        if (localObject3.length > 0)
          localObject1 = localObject3;
        else
          localObject2 = new PostWriter();
      }
      else
      {
        localObject2 = new PostWriter();
      }
      break;
    case 14:
      localObject1 = this.orginTTTables.getTableBytes(14);
      if (localObject1.length == 0)
        localObject1 = new byte[] { FontWriter.setNextUint8(0) };
      break;
    case 9:
      localObject1 = this.orginTTTables.getTableBytes(9);
      if (localObject1.length == 0)
        localObject1 = FontWriter.setNextUint16(0);
      break;
    case 10:
      localObject1 = this.orginTTTables.getTableBytes(10);
      if (localObject1.length == 0)
        localObject1 = new byte[] { FontWriter.setNextUint8(0) };
      break;
    case 11:
    case 12:
    case 15:
    }
    if (localObject2 != null)
      try
      {
        localObject1 = ((FontTableWriter)localObject2).writeTable();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return localObject1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.PS2OTFFontWriter
 * JD-Core Version:    0.6.2
 */
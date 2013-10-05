package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Hhea;

public class HheaWriter extends Hhea
  implements FontTableWriter
{
  private int glyphCount;
  private double xMaxExtent;
  private double minRightSideBearing;
  private double minLeftSideBearing;
  private double advanceWidthMax;
  private double lowestDescender;
  private double highestAscender;

  public HheaWriter(PdfJavaGlyphs paramPdfJavaGlyphs, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
  {
    this.glyphCount = paramPdfJavaGlyphs.getGlyphCount();
    this.xMaxExtent = paramDouble1;
    this.minRightSideBearing = paramDouble2;
    this.minLeftSideBearing = paramDouble3;
    this.advanceWidthMax = paramDouble4;
    this.lowestDescender = paramDouble5;
    this.highestAscender = paramDouble6;
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(65536));
    localByteArrayOutputStream.write(TTFontWriter.setFWord((int)this.highestAscender));
    localByteArrayOutputStream.write(TTFontWriter.setFWord((int)this.lowestDescender));
    localByteArrayOutputStream.write(TTFontWriter.setFWord(0));
    localByteArrayOutputStream.write(TTFontWriter.setUFWord((int)this.advanceWidthMax));
    localByteArrayOutputStream.write(TTFontWriter.setFWord((int)this.minLeftSideBearing));
    localByteArrayOutputStream.write(TTFontWriter.setFWord((int)this.minRightSideBearing));
    localByteArrayOutputStream.write(TTFontWriter.setFWord((int)this.xMaxExtent));
    localByteArrayOutputStream.write(TTFontWriter.setNextInt16(1));
    localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
    localByteArrayOutputStream.write(TTFontWriter.setFWord(0));
    for (int i = 0; i < 4; i++)
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.glyphCount));
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.HheaWriter
 * JD-Core Version:    0.6.2
 */
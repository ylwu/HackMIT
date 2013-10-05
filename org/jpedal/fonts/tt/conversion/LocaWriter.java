package org.jpedal.fonts.tt.conversion;

import java.io.IOException;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Loca;

class LocaWriter extends Loca
  implements FontTableWriter
{
  private PdfFont originalFont;

  LocaWriter(PdfFont paramPdfFont)
  {
    super(null, 0, 0);
    this.originalFont = paramPdfFont;
  }

  public byte[] writeTable()
    throws IOException
  {
    Loca localLoca = (Loca)this.originalFont.getGlyphData().getTable(3);
    this.format = localLoca.getFormat();
    this.glyphCount = localLoca.getGlyphCount();
    this.glyfTableLength = localLoca.getGlyfTableLength();
    return null;
  }

  public int getIntValue(int paramInt)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.LocaWriter
 * JD-Core Version:    0.6.2
 */
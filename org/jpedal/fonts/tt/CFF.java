package org.jpedal.fonts.tt;

import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.utils.LogWriter;

public class CFF extends Table
{
  PdfJavaGlyphs glyphs;
  boolean hasCFFdata = false;

  public CFF(FontFile2 paramFontFile2, boolean paramBoolean)
  {
    this.glyphs = new T1Glyphs(paramBoolean);
    if (paramBoolean)
      this.glyphs.init(65536, true);
    int i = paramFontFile2.selectTable(16);
    if (i != 0)
      try
      {
        int j = paramFontFile2.getTableSize(16);
        byte[] arrayOfByte = paramFontFile2.readBytes(i, j);
        new Type1C(arrayOfByte, null, this.glyphs);
        this.hasCFFdata = true;
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
  }

  public boolean hasCFFData()
  {
    return this.hasCFFdata;
  }

  public PdfGlyph getCFFGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3)
  {
    return this.glyphs.getEmbeddedGlyph(paramGlyphFactory, paramString1, paramArrayOfFloat, paramInt, paramString2, paramFloat, paramString3);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.CFF
 * JD-Core Version:    0.6.2
 */
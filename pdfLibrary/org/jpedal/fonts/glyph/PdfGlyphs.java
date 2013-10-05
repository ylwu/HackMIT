package org.jpedal.fonts.glyph;

import java.awt.geom.Area;

public abstract interface PdfGlyphs
{
  public abstract Area getStandardGlyph(float[][] paramArrayOfFloat, int paramInt, String paramString, float paramFloat, boolean paramBoolean);

  public abstract PdfGlyph getEmbeddedGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3);

  public abstract String getBaseFontName();

  public abstract String getDisplayValue(Integer paramInteger);

  public abstract String getCharGlyph(Integer paramInteger);

  public abstract String getEmbeddedEnc(Integer paramInteger);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.PdfGlyphs
 * JD-Core Version:    0.6.2
 */
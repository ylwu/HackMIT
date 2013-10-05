package org.jpedal.fonts.glyph;

public class T3Glyphs extends PdfJavaGlyphs
{
  private PdfGlyph[] charProcs = new PdfGlyph[256];

  public PdfGlyph getEmbeddedGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3)
  {
    if (((this.lastTrm[0][0] != paramArrayOfFloat[0][0] ? 1 : 0) | (this.lastTrm[1][0] != paramArrayOfFloat[1][0] ? 1 : 0) | (this.lastTrm[0][1] != paramArrayOfFloat[0][1] ? 1 : 0) | (this.lastTrm[1][1] != paramArrayOfFloat[1][1] ? 1 : 0)) != 0)
    {
      this.lastTrm = paramArrayOfFloat;
      flush();
    }
    PdfGlyph localPdfGlyph = getEmbeddedCachedShape(paramInt);
    if (localPdfGlyph == null)
    {
      localPdfGlyph = this.charProcs[paramInt];
      setEmbeddedCachedShape(paramInt, localPdfGlyph);
    }
    return localPdfGlyph;
  }

  public void setT3Glyph(int paramInt1, int paramInt2, PdfGlyph paramPdfGlyph)
  {
    this.charProcs[paramInt1] = paramPdfGlyph;
    if ((paramInt2 != -1) && (this.charProcs[paramInt2] == null))
      this.charProcs[paramInt2] = paramPdfGlyph;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.T3Glyphs
 * JD-Core Version:    0.6.2
 */
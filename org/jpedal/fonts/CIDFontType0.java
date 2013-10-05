package org.jpedal.fonts;

import java.util.Map;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;

public class CIDFontType0 extends Type1C
{
  private CIDFontType2 subFont = null;

  public CIDFontType0(PdfObjectReader paramPdfObjectReader, String paramString)
  {
    this.glyphs = new T1Glyphs(true);
    this.isCID = true;
    this.isCIDFont = true;
    this.TTstreamisCID = true;
    init(paramPdfObjectReader);
    this.currentPdfFile = paramPdfObjectReader;
    this.substituteFontFile = paramString;
  }

  public void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    this.fontTypes = -1684566726;
    this.fontID = paramString;
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(-1547306032);
    PdfObject localPdfObject2 = localPdfObject1.getDictionary(-1044665361);
    createCIDFont(paramPdfObject, localPdfObject1);
    if (localPdfObject2 != null)
    {
      float[] arrayOfFloat = localPdfObject2.getFloatArray(676429196);
      if (arrayOfFloat != null)
        this.FontBBox = arrayOfFloat;
      float f = localPdfObject2.getFloatNumber(859131783);
      if (f != 0.0F)
        this.ascent = f;
      f = localPdfObject2.getFloatNumber(860451719);
      if (f != 0.0F)
        this.descent = f;
      readEmbeddedFont(localPdfObject2);
    }
    if ((paramBoolean) && (!this.isFontEmbedded) && (this.substituteFontFile != null))
    {
      this.isFontSubstituted = true;
      this.subFont = new CIDFontType2(this.currentPdfFile, this.TTstreamisCID);
      this.subFont.substituteFontUsed(this.substituteFontFile);
      this.isFontEmbedded = true;
      this.glyphs.setFontEmbedded(true);
    }
    if (!this.isFontEmbedded)
      selectDefaultFont();
    if (paramBoolean)
      setFont(getBaseFontName(), 1);
  }

  public PdfJavaGlyphs getGlyphData()
  {
    if (this.subFont != null)
      return this.subFont.getGlyphData();
    return this.glyphs;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.CIDFontType0
 * JD-Core Version:    0.6.2
 */
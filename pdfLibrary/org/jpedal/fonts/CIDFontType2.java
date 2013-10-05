package org.jpedal.fonts;

import java.util.Map;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfFontFactory;

public class CIDFontType2 extends TrueType
{
  public CIDFontType2(PdfObjectReader paramPdfObjectReader, String paramString)
  {
    this.isCIDFont = true;
    this.TTstreamisCID = true;
    this.glyphs = new TTGlyphs();
    init(paramPdfObjectReader);
    this.substituteFontFile = paramString;
  }

  public CIDFontType2(PdfObjectReader paramPdfObjectReader, boolean paramBoolean)
  {
    this.isCIDFont = true;
    this.TTstreamisCID = paramBoolean;
    this.glyphs = new TTGlyphs();
    init(paramPdfObjectReader);
  }

  public void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    this.fontTypes = -1684566724;
    this.fontID = paramString;
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(-1547306032);
    PdfObject localPdfObject2 = localPdfObject1.getDictionary(-1044665361);
    createCIDFont(paramPdfObject, localPdfObject1);
    Object localObject;
    if (localPdfObject2 != null)
    {
      localObject = localPdfObject2.getDictionary(2021292334);
      if (localObject != null)
      {
        byte[] arrayOfByte = this.currentPdfFile.readStream((PdfObject)localObject, true, true, false, false, false, ((PdfObject)localObject).getCacheName(this.currentPdfFile.getObjectReader()));
        if (arrayOfByte != null)
          readEmbeddedFont(arrayOfByte, null, this.hasEncoding, false);
      }
    }
    boolean bool = this.glyphs.isCorrupted();
    if (this.glyphs.isCorrupted())
    {
      localObject = new PdfFontFactory(this.currentPdfFile);
      ((PdfFontFactory)localObject).getFontSub(getBaseFontName());
      this.isFontEmbedded = false;
      this.substituteFontFile = ((PdfFontFactory)localObject).getMapFont();
    }
    if ((paramBoolean) && (!this.isFontEmbedded) && (this.substituteFontFile != null))
    {
      substituteFontUsed(this.substituteFontFile);
      this.isFontSubstituted = true;
      this.isFontEmbedded = true;
      this.glyphs.setFontEmbedded(true);
    }
    if (paramBoolean)
      setFont(getBaseFontName(), 1);
    if (!this.isFontEmbedded)
      selectDefaultFont();
    this.glyphs.setCorrupted(bool);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.CIDFontType2
 * JD-Core Version:    0.6.2
 */
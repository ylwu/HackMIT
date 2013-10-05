package org.jpedal.parser;

import org.jpedal.fonts.CIDFontType0;
import org.jpedal.fonts.CIDFontType2;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.TrueType;
import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.Type3;
import org.jpedal.io.PdfObjectReader;

public class FontFactory
{
  public static PdfFont createFont(int paramInt, PdfObjectReader paramPdfObjectReader, String paramString, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 1228944677:
      return new Type1C(paramPdfObjectReader, paramString);
    case 1217103210:
      return new TrueType(paramPdfObjectReader, paramString);
    case 1228944679:
      return new Type3(paramPdfObjectReader, paramBoolean);
    case -1684566726:
      return new CIDFontType0(paramPdfObjectReader, paramString);
    case -1684566724:
      return new CIDFontType2(paramPdfObjectReader, paramString);
    }
    return new PdfFont(paramPdfObjectReader);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.FontFactory
 * JD-Core Version:    0.6.2
 */
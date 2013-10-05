package org.jpedal.fonts;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import org.jpedal.fonts.tt.conversion.GlyphMapping;
import org.jpedal.fonts.tt.conversion.PS2OTFFontWriter;
import org.jpedal.fonts.tt.conversion.TTFontWriter;

public class HTMLFontUtils
{
  public static byte[] convertTTForHTML(byte[] paramArrayOfByte)
    throws IOException
  {
    return new TTFontWriter(paramArrayOfByte).writeFontToStream();
  }

  public static byte[] convertPSForHTMLOTF(PdfFont paramPdfFont, byte[] paramArrayOfByte, String paramString, HashMap<String, Integer> paramHashMap, Collection<GlyphMapping> paramCollection)
    throws Exception
  {
    return new PS2OTFFontWriter(paramPdfFont, paramArrayOfByte, paramString, paramHashMap, paramCollection).writeFontToStream();
  }

  public static byte[] convertPSForHTMLWOFF(PdfFont paramPdfFont, byte[] paramArrayOfByte, String paramString, HashMap<String, Integer> paramHashMap, Collection<GlyphMapping> paramCollection)
    throws Exception
  {
    return new PS2OTFFontWriter(paramPdfFont, paramArrayOfByte, paramString, paramHashMap, paramCollection).writeFontToWoffStream();
  }

  public static byte[] convertPSForHTMLEOT(PdfFont paramPdfFont, byte[] paramArrayOfByte, String paramString, HashMap<String, Integer> paramHashMap, Collection<GlyphMapping> paramCollection)
    throws Exception
  {
    return new PS2OTFFontWriter(paramPdfFont, paramArrayOfByte, paramString, paramHashMap, paramCollection).writeFontToEotStream();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.HTMLFontUtils
 * JD-Core Version:    0.6.2
 */
package org.jpedal.parser;

import org.jpedal.exception.PdfException;
import org.jpedal.fonts.glyph.T3Size;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;

public class T3StreamDecoder extends PdfStreamDecoder
{
  public T3StreamDecoder(PdfObjectReader paramPdfObjectReader, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramPdfObjectReader, paramBoolean1, null);
    this.isType3Font = true;
    this.isPrinting = paramBoolean2;
  }

  public T3Size decodePageContent(PdfObject paramPdfObject, GraphicsState paramGraphicsState)
    throws PdfException
  {
    this.newGS = paramGraphicsState;
    return decodePageContent(paramPdfObject);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.T3StreamDecoder
 * JD-Core Version:    0.6.2
 */
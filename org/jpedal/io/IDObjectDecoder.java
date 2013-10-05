package org.jpedal.io;

public class IDObjectDecoder extends ObjectDecoder
{
  public IDObjectDecoder(PdfFileReader paramPdfFileReader)
  {
    super(paramPdfFileReader);
    this.isInlineImage = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.IDObjectDecoder
 * JD-Core Version:    0.6.2
 */
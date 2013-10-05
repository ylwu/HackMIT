package org.jpedal.external;

import java.awt.Graphics2D;
import org.jpedal.PdfDecoder;

public abstract interface CustomPrintHintingHandler
{
  public abstract boolean preprint(Graphics2D paramGraphics2D, PdfDecoder paramPdfDecoder);

  public abstract boolean postprint(Graphics2D paramGraphics2D, PdfDecoder paramPdfDecoder);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.CustomPrintHintingHandler
 * JD-Core Version:    0.6.2
 */
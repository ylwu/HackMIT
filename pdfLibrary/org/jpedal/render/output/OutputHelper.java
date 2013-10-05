package org.jpedal.render.output;

import org.jpedal.fonts.PdfFont;

public abstract interface OutputHelper
{
  public abstract String tidyText(String paramString);

  public abstract String mapNonstandardGlyfName(String paramString, PdfFont paramPdfFont);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.OutputHelper
 * JD-Core Version:    0.6.2
 */
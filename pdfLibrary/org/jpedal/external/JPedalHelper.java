package org.jpedal.external;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jpedal.color.PdfPaint;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;

public abstract interface JPedalHelper
{
  public abstract Font setFont(PdfJavaGlyphs paramPdfJavaGlyphs, String paramString, int paramInt);

  public abstract Font getJavaFontX(PdfFont paramPdfFont, int paramInt);

  public abstract void setPaint(Graphics2D paramGraphics2D, PdfPaint paramPdfPaint, int paramInt, boolean paramBoolean);

  public abstract BufferedImage processImage(BufferedImage paramBufferedImage, int paramInt, boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.JPedalHelper
 * JD-Core Version:    0.6.2
 */
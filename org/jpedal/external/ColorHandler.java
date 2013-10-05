package org.jpedal.external;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jpedal.color.PdfPaint;

public abstract interface ColorHandler
{
  public abstract void setPaint(Graphics2D paramGraphics2D, PdfPaint paramPdfPaint, int paramInt, boolean paramBoolean);

  public abstract BufferedImage processImage(BufferedImage paramBufferedImage, int paramInt, boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.ColorHandler
 * JD-Core Version:    0.6.2
 */
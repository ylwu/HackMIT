package org.jpedal.color;

import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class PdfTexturePaint extends TexturePaint
  implements PdfPaint
{
  public PdfTexturePaint(BufferedImage paramBufferedImage, Rectangle2D paramRectangle2D)
  {
    super(paramBufferedImage, paramRectangle2D);
  }

  public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
  {
    return super.createContext(paramColorModel, paramRectangle, paramRectangle2D, paramAffineTransform, paramRenderingHints);
  }

  public void setScaling(double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
  }

  public boolean isPattern()
  {
    return false;
  }

  public void setPattern(int paramInt)
  {
  }

  public int getRGB()
  {
    return 0;
  }

  public void setRenderingType(int paramInt)
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.PdfTexturePaint
 * JD-Core Version:    0.6.2
 */
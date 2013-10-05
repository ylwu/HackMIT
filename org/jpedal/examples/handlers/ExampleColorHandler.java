package org.jpedal.examples.handlers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.jpedal.color.PdfPaint;
import org.jpedal.external.ColorHandler;

public class ExampleColorHandler
  implements ColorHandler
{
  public void setPaint(Graphics2D paramGraphics2D, PdfPaint paramPdfPaint, int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int i = paramPdfPaint.getRGB();
      if (i > -8388608)
        paramGraphics2D.setPaint(Color.WHITE);
      else
        paramGraphics2D.setPaint(Color.BLACK);
    }
    else
    {
      paramGraphics2D.setPaint(paramPdfPaint);
    }
  }

  public BufferedImage processImage(BufferedImage paramBufferedImage, int paramInt, boolean paramBoolean)
  {
    BufferedImage localBufferedImage = null;
    if (paramBoolean)
    {
      localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 12);
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.setPaint(Color.WHITE);
      localGraphics2D.fillRect(0, 0, paramBufferedImage.getWidth(), paramBufferedImage.getHeight());
      localGraphics2D.drawImage(paramBufferedImage, 0, 0, null);
    }
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleColorHandler
 * JD-Core Version:    0.6.2
 */
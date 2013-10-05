package org.jpedal.examples.handlers;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.jpedal.external.ImageHandler;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;

public class ExampleImageHandler
  implements ImageHandler
{
  public boolean alwaysIgnoreGenericHandler()
  {
    return true;
  }

  public boolean imageHasBeenScaled()
  {
    return true;
  }

  public boolean drawImageOnscreen(BufferedImage paramBufferedImage, int paramInt, AffineTransform paramAffineTransform, String paramString, Graphics2D paramGraphics2D, boolean paramBoolean1, ObjectStore paramObjectStore, boolean paramBoolean2)
  {
    return false;
  }

  public BufferedImage processImageData(GraphicsState paramGraphicsState, PdfObject paramPdfObject)
  {
    int i = (int)paramGraphicsState.CTM[0][0];
    int j = (int)paramGraphicsState.CTM[1][1];
    if (i < 0)
      i = -i;
    if (j < 0)
      j = -j;
    BufferedImage localBufferedImage = new BufferedImage(i, j, 2);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    AffineTransform localAffineTransform = new AffineTransform();
    localAffineTransform.translate(0.0D, -j);
    localAffineTransform.scale(1.0D, -1.0D);
    localGraphics2D.setTransform(localAffineTransform);
    String str = "Image removed";
    int k = i / str.length();
    Font localFont = new Font("serif", 0, k);
    Rectangle2D localRectangle2D = localFont.getStringBounds(str, 0, str.length(), localGraphics2D.getFontRenderContext());
    localGraphics2D.setFont(localFont);
    localGraphics2D.drawString(str, (int)((i - localRectangle2D.getWidth()) / 2.0D), -j - j / 2);
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleImageHandler
 * JD-Core Version:    0.6.2
 */
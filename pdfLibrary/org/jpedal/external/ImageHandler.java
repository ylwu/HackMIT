package org.jpedal.external;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;

public abstract interface ImageHandler
{
  public abstract boolean alwaysIgnoreGenericHandler();

  public abstract BufferedImage processImageData(GraphicsState paramGraphicsState, PdfObject paramPdfObject);

  public abstract boolean imageHasBeenScaled();

  public abstract boolean drawImageOnscreen(BufferedImage paramBufferedImage, int paramInt, AffineTransform paramAffineTransform, String paramString, Graphics2D paramGraphics2D, boolean paramBoolean1, ObjectStore paramObjectStore, boolean paramBoolean2);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.ImageHandler
 * JD-Core Version:    0.6.2
 */
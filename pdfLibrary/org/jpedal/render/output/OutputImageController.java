package org.jpedal.render.output;

import java.awt.image.BufferedImage;
import org.jpedal.objects.GraphicsState;

public abstract interface OutputImageController
{
  public abstract BufferedImage processImage(BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, String paramString);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.OutputImageController
 * JD-Core Version:    0.6.2
 */
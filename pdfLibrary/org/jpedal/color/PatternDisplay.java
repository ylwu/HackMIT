package org.jpedal.color;

import java.awt.image.BufferedImage;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.render.T3Display;
import org.jpedal.render.T3Renderer;

public class PatternDisplay extends T3Display
  implements T3Renderer
{
  private BufferedImage lastImg;
  private int imageCount = 0;

  public PatternDisplay(int paramInt1, boolean paramBoolean, int paramInt2, ObjectStore paramObjectStore)
  {
    super(paramInt1, paramBoolean, paramInt2, paramObjectStore);
    this.type = 3;
  }

  public int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    this.lastImg = paramBufferedImage;
    this.imageCount += 1;
    return super.drawImage(paramInt1, paramBufferedImage, paramGraphicsState, paramBoolean, paramString, paramInt2, paramInt3);
  }

  public BufferedImage getSingleImagePattern()
  {
    if (this.imageCount != 1)
      return null;
    return this.lastImg;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.PatternDisplay
 * JD-Core Version:    0.6.2
 */
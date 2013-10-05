package org.jpedal.examples.viewer.utils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

public class IconiseImage
  implements Icon
{
  private BufferedImage current_image;
  private int w;
  private int h;

  public IconiseImage(BufferedImage paramBufferedImage)
  {
    this.current_image = paramBufferedImage;
    this.w = paramBufferedImage.getWidth();
    this.h = paramBufferedImage.getHeight();
  }

  public final void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.drawImage(this.current_image, null, 0, 0);
  }

  public final int getIconWidth()
  {
    return this.w;
  }

  public final int getIconHeight()
  {
    return this.h;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.IconiseImage
 * JD-Core Version:    0.6.2
 */
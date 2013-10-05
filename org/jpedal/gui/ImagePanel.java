package org.jpedal.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
  BufferedImage image;

  public ImagePanel(BufferedImage paramBufferedImage)
  {
    if (paramBufferedImage != null)
      this.image = paramBufferedImage;
    setPreferredSize(new Dimension(paramBufferedImage.getWidth(), paramBufferedImage.getHeight()));
  }

  public final void paintComponent(Graphics paramGraphics)
  {
    super.paintComponent(paramGraphics);
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    localGraphics2D.drawImage(this.image, 0, 0, this);
  }

  public BufferedImage getImage()
  {
    return this.image;
  }

  public void setImage(BufferedImage paramBufferedImage)
  {
    if (paramBufferedImage != null)
    {
      this.image = paramBufferedImage;
      setPreferredSize(new Dimension(paramBufferedImage.getWidth(), paramBufferedImage.getHeight()));
      repaint();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.gui.ImagePanel
 * JD-Core Version:    0.6.2
 */
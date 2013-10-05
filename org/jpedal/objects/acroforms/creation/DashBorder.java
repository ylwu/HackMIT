package org.jpedal.objects.acroforms.creation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.border.LineBorder;

class DashBorder extends LineBorder
{
  Stroke stroke = new BasicStroke(1.0F, 0, 2, 1.0F, new float[] { 5.0F, 5.0F }, 10.0F);

  public DashBorder(Stroke paramStroke, Color paramColor)
  {
    super(paramColor);
    this.stroke = paramStroke;
  }

  public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
    localGraphics2D.setStroke(this.stroke);
    super.paintBorder(paramComponent, localGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
    localGraphics2D.dispose();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.DashBorder
 * JD-Core Version:    0.6.2
 */
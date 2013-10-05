package org.jpedal.external;

import java.awt.Graphics2D;

public abstract interface JPedalCustomDrawObject
{
  public static final Integer ALLPAGES = Integer.valueOf(1);

  public abstract void paint(Graphics2D paramGraphics2D);

  public abstract void print(Graphics2D paramGraphics2D, int paramInt);

  public abstract void setVisible(boolean paramBoolean);

  public abstract void setMedX(int paramInt);

  public abstract void setMedY(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.JPedalCustomDrawObject
 * JD-Core Version:    0.6.2
 */
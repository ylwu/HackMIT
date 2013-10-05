package org.jpedal.color;

import java.awt.Paint;

public abstract interface PdfPaint extends Paint
{
  public abstract void setScaling(double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2, float paramFloat3);

  public abstract boolean isPattern();

  public abstract int getRGB();

  public abstract void setRenderingType(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.PdfPaint
 * JD-Core Version:    0.6.2
 */
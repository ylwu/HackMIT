package org.jpedal.color;

import java.awt.Color;
import java.awt.Paint;

public class PdfColor extends Color
  implements PdfPaint, Paint
{
  private static final long serialVersionUID = 1L;
  protected boolean isPattern = false;

  public PdfColor(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    super(paramFloat1, paramFloat2, paramFloat3);
  }

  public PdfColor(int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramInt1, paramInt2, paramInt3);
  }

  public void setScaling(double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
  }

  public boolean isPattern()
  {
    return this.isPattern;
  }

  public void setPattern(int paramInt)
  {
    this.isPattern = true;
  }

  public void setRenderingType(int paramInt)
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.PdfColor
 * JD-Core Version:    0.6.2
 */
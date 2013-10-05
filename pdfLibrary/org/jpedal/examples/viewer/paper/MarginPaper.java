package org.jpedal.examples.viewer.paper;

import java.awt.print.Paper;

public class MarginPaper extends Paper
{
  double minX = 0.0D;
  double minY = 0.0D;
  double maxRX = 0.0D;
  double maxBY = 0.0D;

  public void setMinImageableArea(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    this.minX = paramDouble1;
    this.minY = paramDouble2;
    this.maxRX = (paramDouble1 + paramDouble3);
    this.maxBY = (paramDouble2 + paramDouble4);
    super.setImageableArea(this.minX, this.minY, this.maxRX, this.maxBY);
  }

  public void setImageableArea(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if (paramDouble1 < this.minX)
      paramDouble1 = this.minX;
    if (paramDouble2 < this.minY)
      paramDouble2 = this.minY;
    if (paramDouble1 + paramDouble3 > this.maxRX)
      paramDouble3 = this.maxRX - paramDouble1;
    if (paramDouble2 + paramDouble4 > this.maxBY)
      paramDouble4 = this.maxBY - paramDouble2;
    super.setImageableArea(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
  }

  public double getMinX()
  {
    return this.minX;
  }

  public double getMinY()
  {
    return this.minY;
  }

  public double getMaxRX()
  {
    return this.maxRX;
  }

  public double getMaxBY()
  {
    return this.maxBY;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.paper.MarginPaper
 * JD-Core Version:    0.6.2
 */
package org.jpedal.utils;

public class DPIFactory
{
  private float dpi = 72.0F;

  public float adjustScaling(float paramFloat)
  {
    return paramFloat * (this.dpi / 72.0F);
  }

  public float removeScaling(float paramFloat)
  {
    return paramFloat / (this.dpi / 72.0F);
  }

  public float getDpi()
  {
    return this.dpi;
  }

  public void setDpi(float paramFloat)
  {
    this.dpi = paramFloat;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.DPIFactory
 * JD-Core Version:    0.6.2
 */
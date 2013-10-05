package org.jpedal.color;

import java.awt.color.ColorSpace;

public class CalGrayColorSpace extends GenericColorSpace
{
  private static final long serialVersionUID = -6459433440483127497L;

  public CalGrayColorSpace(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.componentCount = 1;
    setCIEValues(paramArrayOfFloat1, null, null, paramArrayOfFloat2);
    this.value = 391471749;
  }

  public final void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[1];
    arrayOfFloat[0] = Float.parseFloat(paramArrayOfString[0]);
    setColor(arrayOfFloat, 1);
  }

  public final void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    float f1 = paramArrayOfFloat[0];
    float[] arrayOfFloat = new float[3];
    float f2 = (float)Math.pow(f1, this.G[0]);
    arrayOfFloat[0] = (this.W[0] * f2);
    arrayOfFloat[1] = (this.W[1] * f2);
    arrayOfFloat[2] = (this.W[2] * f2);
    arrayOfFloat = this.cs.toRGB(arrayOfFloat);
    this.currentColor = new PdfColor(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.CalGrayColorSpace
 * JD-Core Version:    0.6.2
 */
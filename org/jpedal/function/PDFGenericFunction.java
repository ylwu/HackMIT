package org.jpedal.function;

import java.io.Serializable;

public class PDFGenericFunction
  implements Serializable
{
  protected float[] domain;
  protected float[] encode;
  protected float[] decode;
  protected float[] range;

  public PDFGenericFunction(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.range = paramArrayOfFloat2;
    this.domain = paramArrayOfFloat1;
  }

  static float interpolate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return (paramFloat1 - paramFloat2) * (paramFloat5 - paramFloat4) / (paramFloat3 - paramFloat2) + paramFloat4;
  }

  static float min(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 > paramFloat2)
      return paramFloat2;
    return paramFloat1;
  }

  static float max(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 < paramFloat2)
      return paramFloat2;
    return paramFloat1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFGenericFunction
 * JD-Core Version:    0.6.2
 */
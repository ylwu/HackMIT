package org.jpedal.function;

public abstract interface PDFFunction
{
  public abstract float[] compute(float[] paramArrayOfFloat);

  public abstract float[] computeStitch(float[] paramArrayOfFloat);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFFunction
 * JD-Core Version:    0.6.2
 */
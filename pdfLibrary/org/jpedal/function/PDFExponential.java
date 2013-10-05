package org.jpedal.function;

public class PDFExponential extends PDFGenericFunction
  implements PDFFunction
{
  private float[] C0 = { 0.0F };
  private float[] C1 = { 1.0F };
  private float N;
  int returnValues;

  public PDFExponential(float paramFloat, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
  {
    super(paramArrayOfFloat3, paramArrayOfFloat4);
    this.N = paramFloat;
    if (paramArrayOfFloat1 != null)
      this.C0 = paramArrayOfFloat1;
    if (paramArrayOfFloat2 != null)
      this.C1 = paramArrayOfFloat2;
    this.returnValues = this.C0.length;
  }

  public float[] computeStitch(float[] paramArrayOfFloat)
  {
    return compute(paramArrayOfFloat);
  }

  public float[] compute(float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat1 = new float[this.returnValues];
    float[] arrayOfFloat2 = new float[this.returnValues];
    float f = min(max(paramArrayOfFloat[0], this.domain[0]), this.domain[1]);
    int i;
    if (this.N == 1.0F)
      for (i = 0; i < this.C0.length; i++)
      {
        arrayOfFloat1[i] = (this.C0[i] + f * (this.C1[i] - this.C0[i]));
        if (this.range != null)
          arrayOfFloat1[i] = min(max(arrayOfFloat1[i], this.range[(i * 2)]), this.range[(i * 2 + 1)]);
        arrayOfFloat2[i] = arrayOfFloat1[i];
      }
    else
      for (i = 0; i < this.C0.length; i++)
      {
        arrayOfFloat1[i] = (this.C0[i] + (float)Math.pow(f, this.N) * (this.C1[i] - this.C0[i]));
        if (this.range != null)
          arrayOfFloat1[i] = min(max(arrayOfFloat1[i], this.range[(i * 2)]), this.range[(i * 2 + 1)]);
        arrayOfFloat2[i] = arrayOfFloat1[i];
      }
    return arrayOfFloat2;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFExponential
 * JD-Core Version:    0.6.2
 */
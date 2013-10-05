package org.jpedal.function;

public class PDFStitching extends PDFGenericFunction
  implements PDFFunction
{
  private PDFFunction[] functions;
  private float[] bounds;

  public PDFStitching(PDFFunction[] paramArrayOfPDFFunction, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
  {
    super(paramArrayOfFloat3, paramArrayOfFloat4);
    if (paramArrayOfFloat2 != null)
      this.bounds = paramArrayOfFloat2;
    if (paramArrayOfFloat1 != null)
      this.encode = paramArrayOfFloat1;
    if (paramArrayOfPDFFunction != null)
      this.functions = paramArrayOfPDFFunction;
  }

  public float[] compute(float[] paramArrayOfFloat)
  {
    float f1 = min(max(paramArrayOfFloat[0], this.domain[0]), this.domain[1]);
    if (this.bounds == null)
      this.bounds = new float[0];
    for (int i = this.bounds.length - 1; (i >= 0) && (f1 < this.bounds[i]); i--);
    i++;
    float[] arrayOfFloat1 = new float[1];
    float f2 = this.domain[0];
    float f3 = this.domain[1];
    if (i > 0)
      f2 = this.bounds[(i - 1)];
    if (i < this.bounds.length)
      f3 = this.bounds[i];
    float f4 = this.encode[(i * 2)];
    float f5 = this.encode[(i * 2 + 1)];
    arrayOfFloat1[0] = interpolate(f1, f2, f3, f4, f5);
    float[] arrayOfFloat2 = this.functions[i].computeStitch(arrayOfFloat1);
    float[] arrayOfFloat3 = new float[arrayOfFloat2.length];
    int j;
    if (this.range != null)
      for (j = 0; j != this.range.length / 2; j++)
        arrayOfFloat3[j] = min(max(arrayOfFloat2[j], this.range[0]), this.range[1]);
    else
      for (j = 0; j != arrayOfFloat2.length; j++)
        arrayOfFloat3[j] = arrayOfFloat2[j];
    return arrayOfFloat3;
  }

  public float[] computeStitch(float[] paramArrayOfFloat)
  {
    float f1 = min(max(paramArrayOfFloat[0], this.domain[0]), this.domain[1]);
    for (int i = this.bounds.length - 1; (i >= 0) && (f1 < this.bounds[i]); i--);
    i++;
    float[] arrayOfFloat1 = new float[1];
    float f2 = this.domain[0];
    float f3 = this.domain[1];
    if (i > 0)
      f2 = this.bounds[(i - 1)];
    if (i < this.bounds.length)
      f3 = this.bounds[i];
    float f4 = this.encode[(i * 2)];
    float f5 = this.encode[(i * 2 + 1)];
    arrayOfFloat1[0] = interpolate(f1, f2, f3, f4, f5);
    float[] arrayOfFloat2 = this.functions[i].compute(arrayOfFloat1);
    float[] arrayOfFloat3 = new float[arrayOfFloat2.length];
    for (int j = 0; j != this.range.length / 2; j++)
      if (this.range != null)
        arrayOfFloat3[j] = min(max(arrayOfFloat2[j], this.range[0]), this.range[1]);
      else
        arrayOfFloat3[j] = arrayOfFloat2[j];
    return arrayOfFloat3;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFStitching
 * JD-Core Version:    0.6.2
 */
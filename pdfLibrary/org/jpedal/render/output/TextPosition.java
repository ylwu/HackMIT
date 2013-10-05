package org.jpedal.render.output;

public class TextPosition
{
  float[] rawAffine;
  double[] coords;

  public TextPosition(double[] paramArrayOfDouble, float[] paramArrayOfFloat)
  {
    this.coords = paramArrayOfDouble;
    this.rawAffine = paramArrayOfFloat;
  }

  public double[] getCoords()
  {
    return this.coords;
  }

  public float[] getRawAffine()
  {
    return new float[] { this.rawAffine[0], this.rawAffine[1], this.rawAffine[2], this.rawAffine[3], this.rawAffine[4], this.rawAffine[5] };
  }

  public float[][] getTrm()
  {
    return new float[][] { { this.rawAffine[0], this.rawAffine[1], 0.0F }, { this.rawAffine[2], this.rawAffine[3], 0.0F }, { this.rawAffine[4], this.rawAffine[5], 1.0F } };
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.TextPosition
 * JD-Core Version:    0.6.2
 */
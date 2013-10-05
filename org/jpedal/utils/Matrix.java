package org.jpedal.utils;

public class Matrix
{
  public static final float[][] multiply(float[][] paramArrayOfFloat1, float[][] paramArrayOfFloat2)
  {
    float[][] arrayOfFloat = new float[3][3];
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        arrayOfFloat[j][i] = (paramArrayOfFloat1[j][0] * paramArrayOfFloat2[0][i] + paramArrayOfFloat1[j][1] * paramArrayOfFloat2[1][i] + paramArrayOfFloat1[j][2] * paramArrayOfFloat2[2][i]);
    return arrayOfFloat;
  }

  public static final void show(float[][] paramArrayOfFloat)
  {
    for (int i = 0; i < 3; i++)
      LogWriter.writeLog(i + "((" + paramArrayOfFloat[i][0] + " , " + paramArrayOfFloat[i][1] + " , " + paramArrayOfFloat[i][2] + " ))");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.Matrix
 * JD-Core Version:    0.6.2
 */
package org.jpedal.function;

import org.jpedal.utils.LogWriter;

public class PDFCalculator extends PDFGenericFunction
  implements PDFFunction
{
  int returnValues;
  byte[] stream;

  public PDFCalculator(byte[] paramArrayOfByte, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    super(paramArrayOfFloat1, paramArrayOfFloat2);
    this.returnValues = (paramArrayOfFloat2.length / 2);
    this.stream = paramArrayOfByte;
  }

  public float[] computeStitch(float[] paramArrayOfFloat)
  {
    return compute(paramArrayOfFloat);
  }

  public float[] compute(float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat1 = new float[this.returnValues];
    float[] arrayOfFloat2 = new float[this.returnValues];
    try
    {
      PostscriptFactory localPostscriptFactory = new PostscriptFactory(this.stream);
      localPostscriptFactory.resetStacks(paramArrayOfFloat);
      double[] arrayOfDouble = localPostscriptFactory.executePostscript();
      int i;
      int j;
      if (this.domain.length / 2 == 1)
      {
        i = 0;
        j = this.range.length / 2;
        while (i < j)
        {
          arrayOfFloat1[i] = ((float)arrayOfDouble[i]);
          arrayOfFloat2[i] = min(max(arrayOfFloat1[i], this.range[(i * 2)]), this.range[(i * 2 + 1)]);
          i++;
        }
      }
      else
      {
        i = 0;
        j = this.range.length / 2;
        while (i < j)
        {
          arrayOfFloat1[i] = ((float)arrayOfDouble[i]);
          arrayOfFloat2[i] = min(max(arrayOfFloat1[i], this.range[(i * 2)]), this.range[(i * 2 + 1)]);
          i++;
        }
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    return arrayOfFloat2;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFCalculator
 * JD-Core Version:    0.6.2
 */
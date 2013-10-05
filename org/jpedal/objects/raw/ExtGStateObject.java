package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class ExtGStateObject extends PdfObject
{
  private float[] Matrix;
  float CA = -1.0F;
  float ca = -1.0F;
  float LW = -1.0F;
  float OPM = -1.0F;
  boolean AIS = false;
  boolean op = false;
  boolean OP = false;
  PdfObject TR;
  private byte[][] BM;

  public ExtGStateObject(String paramString)
  {
    super(paramString);
  }

  public ExtGStateObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    case 4881:
      return this.CA;
    case 13105:
      return this.ca;
    case 7207:
      return this.LW;
    case 2039837:
      return this.OPM;
    }
    return super.getFloatNumber(paramInt);
  }

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 4881:
      this.CA = paramFloat;
      break;
    case 13105:
      this.ca = paramFloat;
      break;
    case 7207:
      this.LW = paramFloat;
      break;
    case 2039837:
      this.OPM = paramFloat;
      break;
    default:
      super.setFloatNumber(paramInt, paramFloat);
    }
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case 1120547:
      return this.AIS;
    case 16192:
      return this.op;
    case 7968:
      return this.OP;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 1120547:
      this.AIS = paramBoolean;
      break;
    case 7968:
      this.OP = paramBoolean;
      break;
    case 16192:
      this.op = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 9250:
      return this.TR;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    }
    super.setIntNumber(paramInt1, paramInt2);
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 9250:
      this.TR = paramPdfObject;
      break;
    default:
      super.setDictionary(paramInt, paramPdfObject);
    }
  }

  public int setConstant(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = -1;
    int j = 0;
    int k = 0;
    try
    {
      for (int n = paramInt3 - 1; n > -1; n--)
      {
        int m = paramArrayOfByte[(paramInt2 + n)];
        m -= 48;
        j += (m << k);
        k += 8;
      }
      switch (j)
      {
      case 1026635598:
        i = 1026635598;
        break;
      case 373244477:
        i = 373244477;
        break;
      default:
        i = super.setConstant(paramInt1, j);
        if ((i == -1) && (debug))
        {
          byte[] arrayOfByte = new byte[paramInt3];
          System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
          System.out.println("key=" + new String(arrayOfByte) + ' ' + j + " not implemented in setConstant in " + this);
          System.out.println("final public static int " + new String(arrayOfByte) + '=' + j + ';');
        }
        break;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    return i;
  }

  public int getParameterConstant(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getParameterConstant(paramInt);
  }

  public PdfArrayIterator getMixedArray(int paramInt)
  {
    switch (paramInt)
    {
    case 4637:
      return new PdfArrayIterator(this.BM);
    }
    return super.getMixedArray(paramInt);
  }

  public double[] getDoubleArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getDoubleArray(paramInt);
  }

  public void setDoubleArray(int paramInt, double[] paramArrayOfDouble)
  {
    switch (paramInt)
    {
    }
    super.setDoubleArray(paramInt, paramArrayOfDouble);
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 4637:
      this.BM = paramArrayOfByte;
      break;
    default:
      super.setMixedArray(paramInt, paramArrayOfByte);
    }
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1145198201:
      return deepCopy(this.Matrix);
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 1145198201:
      this.Matrix = paramArrayOfFloat;
      break;
    default:
      super.setFloatArray(paramInt, paramArrayOfFloat);
    }
  }

  public void setName(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    }
    super.setName(paramInt, paramArrayOfByte);
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    }
    super.setTextStreamValue(paramInt, paramArrayOfByte);
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getTextStreamValue(paramInt);
  }

  public String getStringValue(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = null;
    switch (paramInt2)
    {
    case 0:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 1:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 2:
      if (arrayOfByte1 != null)
      {
        int i = arrayOfByte1.length;
        if ((i > 6) && (arrayOfByte1[6] == 43))
        {
          int j = i - 7;
          byte[] arrayOfByte2 = new byte[j];
          System.arraycopy(arrayOfByte1, 7, arrayOfByte2, 0, j);
          return new String(arrayOfByte2);
        }
        return new String(arrayOfByte1);
      }
      return null;
    }
    throw new RuntimeException("Value not defined in getName(int,mode) in " + this);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.ExtGStateObject
 * JD-Core Version:    0.6.2
 */
package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class FunctionObject extends PdfObject
{
  byte[][] Functions;
  float[] Bounds;
  float[] C0;
  float[] C1;
  float[] Encode;
  int[] Size;
  int BitsPerSample = -1;
  int FunctionType = -1;
  float N = -1.0F;

  public FunctionObject(String paramString)
  {
    super(paramString);
  }

  public FunctionObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public int[] getIntArray(int paramInt)
  {
    switch (paramInt)
    {
    case 590957109:
      return deepCopy(this.Size);
    }
    return super.getIntArray(paramInt);
  }

  public void setIntArray(int paramInt, int[] paramArrayOfInt)
  {
    switch (paramInt)
    {
    case 590957109:
      this.Size = paramArrayOfInt;
      break;
    default:
      super.setIntArray(paramInt, paramArrayOfInt);
    }
  }

  public FunctionObject(int paramInt)
  {
    super(paramInt);
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getBoolean(paramInt);
  }

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 2122150301:
      return deepCopy(this.Functions);
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 2122150301:
      this.Functions = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    }
    super.setBoolean(paramInt, paramBoolean);
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case -1413045608:
      this.BitsPerSample = paramInt2;
      break;
    case 2127019430:
      this.FunctionType = paramInt2;
      break;
    case 30:
      this.N = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case -1413045608:
      return this.BitsPerSample;
    case 2127019430:
      return this.FunctionType;
    }
    return super.getInt(paramInt);
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    case 30:
      return this.N;
    }
    return super.getFloatNumber(paramInt);
  }

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 30:
      this.N = paramFloat;
      break;
    default:
      super.setFloatNumber(paramInt, paramFloat);
    }
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    }
    super.setDictionary(paramInt, paramPdfObject);
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
      }
      i = super.setConstant(paramInt1, j);
      if ((i == -1) && (debug))
      {
        byte[] arrayOfByte = new byte[paramInt3];
        System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
        System.out.println("key=" + new String(arrayOfByte) + ' ' + j + " not implemented in setConstant in " + this);
        System.out.println("final public static int " + new String(arrayOfByte) + '=' + j + ';');
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
    }
    super.setMixedArray(paramInt, paramArrayOfByte);
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1161709186:
      return deepCopy(this.Bounds);
    case 4864:
      return deepCopy(this.C0);
    case 4865:
      return deepCopy(this.C1);
    case 859785587:
      return deepCopy(this.Encode);
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 1161709186:
      this.Bounds = paramArrayOfFloat;
      break;
    case 4864:
      this.C0 = paramArrayOfFloat;
      break;
    case 4865:
      this.C1 = paramArrayOfFloat;
      break;
    case 859785587:
      this.Encode = paramArrayOfFloat;
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

  public boolean decompressStreamWhenRead()
  {
    return true;
  }

  public int getObjectType()
  {
    return 1518239089;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.FunctionObject
 * JD-Core Version:    0.6.2
 */
package org.jpedal.objects.raw;

import java.io.PrintStream;
import java.io.Serializable;
import org.jpedal.utils.LogWriter;

public class ShadingObject extends PdfObject
  implements Serializable
{
  int ShadingType = -1;
  boolean AntiAlias;
  float[] Array;
  float[] Background;
  float[] Coords;
  byte[][] Functions;
  boolean[] Extend;

  public ShadingObject(String paramString)
  {
    super(paramString);
  }

  public ShadingObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1518239089:
      return deepCopy(this.Functions);
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1518239089:
      this.Functions = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case 2055039589:
      return this.AntiAlias;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 2055039589:
      this.AntiAlias = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
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
    case 1487255197:
      this.ShadingType = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 1487255197:
      return this.ShadingType;
    }
    return super.getInt(paramInt);
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

  public boolean[] getBooleanArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1144345468:
      return deepCopy(this.Extend);
    }
    return super.getBooleanArray(paramInt);
  }

  public void setBooleanArray(int paramInt, boolean[] paramArrayOfBoolean)
  {
    switch (paramInt)
    {
    case 1144345468:
      this.Extend = paramArrayOfBoolean;
      break;
    default:
      super.setBooleanArray(paramInt, paramArrayOfBoolean);
    }
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1111634266:
      return this.Array;
    case 1921025959:
      return this.Background;
    case 1061308290:
      return this.Coords;
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 1111634266:
      this.Array = paramArrayOfFloat;
      break;
    case 1921025959:
      this.Background = paramArrayOfFloat;
      break;
    case 1061308290:
      this.Coords = paramArrayOfFloat;
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
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.ShadingObject
 * JD-Core Version:    0.6.2
 */
package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class NamesObject extends PdfObject
{
  byte[] rawJS;
  String JSString;
  private PdfObject Dests = null;
  private PdfObject JavaScript = null;
  private PdfObject JS = null;
  private byte[][] Kids;
  private byte[][] Names = (byte[][])null;

  public NamesObject(String paramString)
  {
    super(paramString);
  }

  public NamesObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getBoolean(paramInt);
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
    case 893600855:
      return this.Dests;
    case -2006286978:
      return this.JavaScript;
    case 6691:
      return this.JS;
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
    case 893600855:
      this.Dests = paramPdfObject;
      break;
    case -2006286978:
      this.JavaScript = paramPdfObject;
      break;
    case 6691:
      this.JS = paramPdfObject;
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
    switch (paramInt1)
    {
    }
    super.setConstant(paramInt1, j);
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
    case 826094945:
      return new PdfArrayIterator(this.Names);
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

  public int[] getIntArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getIntArray(paramInt);
  }

  public void setIntArray(int paramInt, int[] paramArrayOfInt)
  {
    switch (paramInt)
    {
    }
    super.setIntArray(paramInt, paramArrayOfInt);
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 826094945:
      this.Names = paramArrayOfByte;
      break;
    default:
      super.setMixedArray(paramInt, paramArrayOfByte);
    }
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    }
    super.setFloatArray(paramInt, paramArrayOfFloat);
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
    case 6691:
      this.rawJS = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
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
    case 6691:
      if ((this.JSString == null) && (this.rawJS != null))
        this.JSString = new String(this.rawJS);
      return this.JSString;
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

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 456733763:
      return deepCopy(this.Kids);
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 456733763:
      this.Kids = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }

  public int getObjectType()
  {
    return 826094945;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.NamesObject
 * JD-Core Version:    0.6.2
 */
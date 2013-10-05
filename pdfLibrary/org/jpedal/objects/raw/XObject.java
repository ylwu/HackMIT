package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class XObject extends PdfObject
{
  byte[] rawIntent = null;
  byte[] rawOC;
  String Intent = null;
  String OC;
  boolean ImageMask = false;
  boolean K = false;
  int Height = 1;
  int Width = 1;
  private PdfObject Group = null;
  private PdfObject Mask = null;
  private PdfObject OCObject = null;
  private PdfObject OPI = null;
  private PdfObject XObject = null;

  public XObject(String paramString)
  {
    super(paramString);
  }

  public XObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public XObject(int paramInt)
  {
    super(paramInt);
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case 1516403337:
      return this.ImageMask;
    case 27:
      return this.K;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 1516403337:
      this.ImageMask = paramBoolean;
      break;
    case 27:
      this.K = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 1111442775:
      return this.Group;
    case 489767739:
      return this.Mask;
    case 7955:
      return this.OCObject;
    case 2039833:
      return this.OPI;
    case 979194486:
      return this.XObject;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 959926393:
      this.Height = paramInt2;
      break;
    case 959726687:
      this.Width = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 959926393:
      return this.Height;
    case 959726687:
      return this.Width;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 1111442775:
      this.Group = paramPdfObject;
      break;
    case 489767739:
      this.Mask = paramPdfObject;
      break;
    case 7955:
      this.OCObject = paramPdfObject;
      break;
    case 2039833:
      this.OPI = paramPdfObject;
      break;
    case 979194486:
      this.XObject = paramPdfObject;
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
    }
    super.setMixedArray(paramInt, paramArrayOfByte);
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
    case 1144346498:
      this.rawIntent = paramArrayOfByte;
      break;
    case 7955:
      this.rawOC = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
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
    case 1144346498:
      if ((this.Intent == null) && (this.rawIntent != null))
        this.Intent = new String(this.rawIntent);
      return this.Intent;
    case 7955:
      if ((this.OC == null) && (this.rawOC != null))
        this.OC = new String(this.rawOC);
      return this.OC;
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

  public int getObjectType()
  {
    return 979194486;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.XObject
 * JD-Core Version:    0.6.2
 */
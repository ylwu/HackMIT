package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class OCObject extends PdfObject
{
  float max;
  float min;
  int Event = -1;
  private byte[] rawBaseState;
  private byte[] rawListMode;
  String BaseState;
  String ListMode;
  private PdfObject D = null;
  private PdfObject Layer = null;
  private PdfObject Usage = null;
  private PdfObject Zoom = null;
  private Object[] Order;
  private byte[][] AS;
  private byte[][] Category;
  private byte[][] Locked;
  private byte[][] ON;
  private byte[][] OFF;
  private byte[][] OCGs;
  private byte[][] Configs;
  private byte[][] RBGroups;

  public OCObject(String paramString)
  {
    super(paramString);
  }

  public OCObject(int paramInt1, int paramInt2)
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
    case 20:
      return this.D;
    case 826881374:
      return this.Layer;
    case 1127298906:
      return this.Usage;
    case 708788029:
      return this.Zoom;
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

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 4010312:
      this.max = paramFloat;
      break;
    case 4012350:
      this.min = paramFloat;
      break;
    default:
      super.setFloatNumber(paramInt, paramFloat);
    }
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    case 4010312:
      return this.max;
    case 4012350:
      return this.min;
    }
    return super.getFloatNumber(paramInt);
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
    case 20:
      this.D = paramPdfObject;
      break;
    case 826881374:
      this.Layer = paramPdfObject;
      break;
    case 1127298906:
      this.Usage = paramPdfObject;
      break;
    case 708788029:
      this.Zoom = paramPdfObject;
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
    case 1177894489:
      this.Event = i;
      break;
    default:
      super.setConstant(paramInt1, j);
    }
    return i;
  }

  public int getParameterConstant(int paramInt)
  {
    switch (paramInt)
    {
    case 1177894489:
      return this.Event;
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
    case 964196217:
      this.rawListMode = paramArrayOfByte;
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

  public int getNameAsConstant(int paramInt)
  {
    byte[] arrayOfByte;
    switch (paramInt)
    {
    case 1970567530:
      arrayOfByte = this.rawBaseState;
      break;
    case 964196217:
      arrayOfByte = this.rawListMode;
      break;
    default:
      return super.getNameAsConstant(paramInt);
    }
    if (arrayOfByte == null)
      return super.getNameAsConstant(paramInt);
    return PdfDictionary.generateChecksum(0, arrayOfByte.length, arrayOfByte);
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 1970567530:
      if ((this.BaseState == null) && (this.rawBaseState != null))
        this.BaseState = new String(this.rawBaseState);
      return this.BaseState;
    case 964196217:
      if ((this.ListMode == null) && (this.rawListMode != null))
        this.ListMode = new String(this.rawListMode);
      return this.ListMode;
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

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 4387:
      return this.AS;
    case 1248888446:
      return this.Category;
    case 910980737:
      return this.Configs;
    case 859525491:
      return this.Locked;
    case 521344835:
      return this.OCGs;
    case 2037270:
      return this.OFF;
    case 7966:
      return this.ON;
    case 1633113989:
      return this.RBGroups;
    }
    return super.getKeyArray(paramInt);
  }

  public void setObjectArray(int paramInt, Object[] paramArrayOfObject)
  {
    switch (paramInt)
    {
    case 1110717793:
      this.Order = paramArrayOfObject;
      break;
    default:
      super.setObjectArray(paramInt, paramArrayOfObject);
    }
  }

  public Object[] getObjectArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1110717793:
      return deepCopy(this.Order);
    }
    return super.getObjectArray(paramInt);
  }

  protected static Object[] deepCopy(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject == null)
      return null;
    int i = paramArrayOfObject.length;
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j < i; j++)
      if ((paramArrayOfObject[j] instanceof byte[]))
      {
        byte[] arrayOfByte1 = (byte[])paramArrayOfObject[j];
        int k = arrayOfByte1.length;
        byte[] arrayOfByte2 = new byte[k];
        arrayOfObject[j] = arrayOfByte2;
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, k);
      }
      else
      {
        arrayOfObject[j] = deepCopy((Object[])(Object[])paramArrayOfObject[j]);
      }
    return arrayOfObject;
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 4387:
      this.AS = paramArrayOfByte;
      break;
    case 1248888446:
      this.Category = paramArrayOfByte;
      break;
    case 910980737:
      this.Configs = paramArrayOfByte;
      break;
    case 859525491:
      this.Locked = paramArrayOfByte;
      break;
    case 521344835:
      this.OCGs = paramArrayOfByte;
      break;
    case 2037270:
      this.OFF = paramArrayOfByte;
      break;
    case 7966:
      this.ON = paramArrayOfByte;
      break;
    case 1633113989:
      this.RBGroups = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public boolean decompressStreamWhenRead()
  {
    return false;
  }

  public int getObjectType()
  {
    return -1567847737;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.OCObject
 * JD-Core Version:    0.6.2
 */
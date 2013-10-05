package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class ColorSpaceObject extends PdfObject
{
  String Name = null;
  byte[] rawName = null;
  PdfObject alternateSpace;
  PdfObject IndexedColorSpace;
  PdfObject Lookup;
  PdfObject Process;
  PdfObject tintTransform;
  int Alternate = -1;
  int colorType = -1;
  private byte[][] rawComponents;
  private float[] BlackPoint;
  private float[] Gamma;
  private float[] WhitePoint;
  float GammaInCalGrey = -1.0F;
  float N = -1.0F;
  private int hival = -1;

  public ColorSpaceObject(String paramString)
  {
    super(paramString);
  }

  public ColorSpaceObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public ColorSpaceObject(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    super(paramInt1, paramInt2);
  }

  public ColorSpaceObject(byte[] paramArrayOfByte)
  {
    this.colorType = PdfDictionary.getIntKey(0, paramArrayOfByte.length, paramArrayOfByte);
  }

  public byte[][] getStringArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1920898752:
      return deepCopy(this.rawComponents);
    }
    return super.getStringArray(paramInt);
  }

  public void setStringArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1920898752:
      this.rawComponents = paramArrayOfByte;
      break;
    default:
      super.setStringArray(paramInt, paramArrayOfByte);
    }
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
    case -1247101998:
      return this.alternateSpace;
    case 895578984:
      return this.IndexedColorSpace;
    case 1060856191:
      return this.Lookup;
    case 861242754:
      return this.Process;
    case -1313946392:
      return this.tintTransform;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 960901492:
      this.hival = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 960901492:
      return this.hival;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case -1247101998:
      this.alternateSpace = paramPdfObject;
      break;
    case 895578984:
      this.IndexedColorSpace = paramPdfObject;
      break;
    case 1060856191:
      this.Lookup = paramPdfObject;
      break;
    case -1313946392:
      this.tintTransform = paramPdfObject;
      break;
    case 861242754:
      this.Process = paramPdfObject;
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
      case 23:
        i = 1568372915;
        break;
      case 2234130:
        i = 1785221209;
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
    switch (paramInt1)
    {
    case 2054519176:
      this.Alternate = i;
      break;
    case 2087749783:
      this.colorType = i;
    }
    return i;
  }

  public int getParameterConstant(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    case 2054519176:
      return this.Alternate;
    case 2087749783:
      return this.colorType;
    }
    super.getParameterConstant(paramInt);
    return i;
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
    case 1886161824:
      return deepCopy(this.BlackPoint);
    case 826096968:
      return deepCopy(this.Gamma);
    case 2021497500:
      return deepCopy(this.WhitePoint);
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 826096968:
      this.GammaInCalGrey = paramFloat;
      break;
    case 30:
      this.N = paramFloat;
      break;
    default:
      super.setFloatNumber(paramInt, paramFloat);
    }
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    case 826096968:
      return this.GammaInCalGrey;
    case 30:
      return this.N;
    }
    return super.getFloatNumber(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 1886161824:
      this.BlackPoint = paramArrayOfFloat;
      break;
    case 826096968:
      this.Gamma = paramArrayOfFloat;
      break;
    case 2021497500:
      this.WhitePoint = paramArrayOfFloat;
      break;
    default:
      super.setFloatArray(paramInt, paramArrayOfFloat);
    }
  }

  public void setName(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 506543413:
      this.rawName = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public byte[] getStringValueAsByte(int paramInt)
  {
    switch (paramInt)
    {
    case 506543413:
      return this.rawName;
    }
    return super.getStringValueAsByte(paramInt);
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
    case 506543413:
      if ((this.Name == null) && (this.rawName != null))
        this.Name = new String(this.rawName);
      return this.Name;
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
    return 2087749783;
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.ColorSpaceObject
 * JD-Core Version:    0.6.2
 */
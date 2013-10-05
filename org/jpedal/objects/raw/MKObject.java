package org.jpedal.objects.raw;

import org.jpedal.utils.StringUtils;

public class MKObject extends FormObject
{
  private float[] BC;
  private float[] BG = null;
  protected String AC;
  protected String CA;
  protected String RC;
  protected byte[] rawAC;
  protected byte[] rawCA;
  protected byte[] rawRC;
  private int TP = -1;
  int R = 0;
  private PdfObject I = null;

  public PdfObject duplicate()
  {
    MKObject localMKObject = new MKObject();
    int i = getInt(9248);
    if (i != -1)
      localMKObject.setIntNumber(9248, i);
    int j = getInt(34);
    localMKObject.setIntNumber(34, j);
    int[] arrayOfInt1 = { 4371, 4881, 8723 };
    int n;
    for (n : arrayOfInt1)
    {
      byte[] arrayOfByte = getTextStreamValueAsByte(n);
      if (arrayOfByte != null)
        localMKObject.setTextStreamValue(n, arrayOfByte);
    }
    ??? = new int[] { 4627, 4631 };
    for (int i1 : ???)
    {
      float[] arrayOfFloat = getFloatArray(i1);
      if (arrayOfFloat != null)
        localMKObject.setFloatArray(i1, arrayOfFloat);
    }
    if (this.I != null)
      localMKObject.I = this.I.duplicate();
    return localMKObject;
  }

  public MKObject(String paramString)
  {
    super(paramString);
  }

  public MKObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public MKObject()
  {
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
    case 25:
      return this.I;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 34:
      this.R = paramInt2;
      break;
    case 9248:
      this.TP = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 34:
      return this.R;
    case 9248:
      return this.TP;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 25:
      this.I = paramPdfObject;
      break;
    default:
      super.setDictionary(paramInt, paramPdfObject);
    }
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
    case 4627:
      return this.BC;
    case 4631:
      return this.BG;
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 4627:
      this.BC = paramArrayOfFloat;
      break;
    case 4631:
      this.BG = paramArrayOfFloat;
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

  public byte[] getTextStreamValueAsByte(int paramInt)
  {
    switch (paramInt)
    {
    case 4371:
      return this.rawAC;
    case 4881:
      return this.rawCA;
    case 8723:
      return this.rawRC;
    }
    return super.getTextStreamValueAsByte(paramInt);
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getName(paramInt);
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 4371:
      this.rawAC = paramArrayOfByte;
      break;
    case 4881:
      this.rawCA = paramArrayOfByte;
      break;
    case 8723:
      this.rawRC = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 4371:
      if ((this.AC == null) && (this.rawAC != null))
        this.AC = StringUtils.getTextString(this.rawAC, false);
      return this.AC;
    case 4881:
      if ((this.CA == null) && (this.rawCA != null))
        this.CA = StringUtils.getTextString(this.rawCA, false);
      return this.CA;
    case 8723:
      if ((this.RC == null) && (this.rawRC != null))
        this.RC = StringUtils.getTextString(this.rawRC, false);
      return this.RC;
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
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    }
    super.setKeyArray(paramInt, paramArrayOfByte);
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }

  public int getObjectType()
  {
    return 7451;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.MKObject
 * JD-Core Version:    0.6.2
 */
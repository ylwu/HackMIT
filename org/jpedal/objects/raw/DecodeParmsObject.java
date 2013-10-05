package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class DecodeParmsObject extends PdfObject
{
  boolean EncodedByteAlign = false;
  boolean EndOfBlock = true;
  boolean EndOfLine = false;
  boolean BlackIs1 = false;
  boolean Uncompressed = false;
  PdfObject JBIG2Globals;
  int Blend = -1;
  int Colors = -1;
  int ColorTransform = 1;
  int Columns = -1;
  int DamagedRowsBeforeError = 0;
  int EarlyChange = 1;
  int K = 0;
  int Predictor = 1;
  int QFactor = -1;
  int Rows = -1;

  public DecodeParmsObject(String paramString)
  {
    super(paramString);
  }

  public DecodeParmsObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case 1297445940:
      return this.BlackIs1;
    case -823077984:
      return this.EncodedByteAlign;
    case 1885240971:
      return this.EndOfBlock;
    case 1517116800:
      return this.EndOfLine;
    case -1514034520:
      return this.Uncompressed;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 1297445940:
      this.BlackIs1 = paramBoolean;
      break;
    case -823077984:
      this.EncodedByteAlign = paramBoolean;
      break;
    case 1885240971:
      this.EndOfBlock = paramBoolean;
      break;
    case 1517116800:
      this.EndOfLine = paramBoolean;
      break;
    case -1514034520:
      this.Uncompressed = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 1314558361:
      return this.JBIG2Globals;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 1010122310:
      this.Blend = paramInt2;
      break;
    case 1010783618:
      this.Colors = paramInt2;
      break;
    case -1263544861:
      this.ColorTransform = paramInt2;
      break;
    case 1162902911:
      this.Columns = paramInt2;
      break;
    case 904541242:
      this.DamagedRowsBeforeError = paramInt2;
      break;
    case 1838971823:
      this.EarlyChange = paramInt2;
      break;
    case 27:
      this.K = paramInt2;
      break;
    case 1970893723:
      this.Predictor = paramInt2;
      break;
    case 862279027:
      this.QFactor = paramInt2;
      break;
    case 574572355:
      this.Rows = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 1010122310:
      return this.Blend;
    case 1010783618:
      return this.Colors;
    case -1263544861:
      return this.ColorTransform;
    case 1162902911:
      return this.Columns;
    case 904541242:
      return this.DamagedRowsBeforeError;
    case 1838971823:
      return this.EarlyChange;
    case 27:
      return this.K;
    case 1970893723:
      return this.Predictor;
    case 862279027:
      return this.QFactor;
    case 574572355:
      return this.Rows;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 1314558361:
      this.JBIG2Globals = paramPdfObject;
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
    int i = super.getParameterConstant(paramInt);
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
    }
    super.setTextStreamValue(paramInt, paramArrayOfByte);
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 678461817:
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
    Object localObject = null;
    switch (paramInt2)
    {
    }
    throw new RuntimeException("Value not defined in getName(int,mode)");
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.DecodeParmsObject
 * JD-Core Version:    0.6.2
 */
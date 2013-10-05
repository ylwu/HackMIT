package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class EncryptionObject extends PdfObject
{
  boolean EncryptMetadata = true;
  int V = 1;
  int R = -1;
  int P = -1;
  byte[] rawPerms;
  byte[] rawU;
  byte[] rawUE;
  byte[] rawO;
  byte[] rawOE;
  byte[] rawCFM;
  byte[] rawEFF;
  byte[] rawStrF;
  byte[] rawStmF;
  String U;
  String UE;
  String O;
  String OE;
  String EFF;
  String CFM;
  String StrF = null;
  String StmF = null;
  private PdfObject CF = null;
  private byte[][] Recipients = (byte[][])null;

  public EncryptionObject(String paramString)
  {
    super(paramString);
  }

  public EncryptionObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case -1815804199:
      return this.EncryptMetadata;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case -1815804199:
      this.EncryptMetadata = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 4886:
      return this.CF;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 32:
      this.P = paramInt2;
      break;
    case 34:
      this.R = paramInt2;
      break;
    case 38:
      this.V = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 32:
      return this.P;
    case 34:
      return this.R;
    case 38:
      return this.V;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 4886:
      this.CF = paramPdfObject;
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
    case 1250845:
      this.rawCFM = paramArrayOfByte;
      break;
    case 1381910:
      this.rawEFF = paramArrayOfByte;
      break;
    case 591674646:
      this.rawStmF = paramArrayOfByte;
      break;
    case 591675926:
      this.rawStrF = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 31:
      this.rawO = paramArrayOfByte;
      break;
    case 7957:
      this.rawOE = paramArrayOfByte;
      break;
    case 893533539:
      this.rawPerms = paramArrayOfByte;
      break;
    case 37:
      this.rawU = paramArrayOfByte;
      break;
    case 9493:
      this.rawUE = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 1250845:
      if ((this.CFM == null) && (this.rawCFM != null))
        this.CFM = new String(this.rawCFM);
      return this.CFM;
    case 1381910:
      if ((this.EFF == null) && (this.rawEFF != null))
        this.EFF = new String(this.rawEFF);
      return this.EFF;
    case 591674646:
      if ((this.StmF == null) && (this.rawStmF != null))
        this.StmF = new String(this.rawStmF);
      return this.StmF;
    case 591675926:
      if ((this.StrF == null) && (this.rawStrF != null))
        this.StrF = new String(this.rawStrF);
      return this.StrF;
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 31:
      if ((this.O == null) && (this.rawO != null))
        this.O = new String(this.rawO);
      return this.O;
    case 7957:
      if ((this.OE == null) && (this.rawOE != null))
        this.OE = new String(this.rawOE);
      return this.OE;
    case 37:
      if ((this.U == null) && (this.rawU != null))
        this.U = new String(this.rawU);
      return this.U;
    case 9493:
      if ((this.UE == null) && (this.rawUE != null))
        this.UE = new String(this.rawUE);
      return this.UE;
    }
    return super.getTextStreamValue(paramInt);
  }

  public byte[] getTextStreamValueAsByte(int paramInt)
  {
    switch (paramInt)
    {
    case 31:
      return this.rawO;
    case 7957:
      return this.rawOE;
    case 893533539:
      return this.rawPerms;
    case 37:
      if ((this.U == null) && (this.rawU != null))
        this.U = new String(this.rawU);
      return this.rawU;
    case 9493:
      if ((this.UE == null) && (this.rawUE != null))
        this.UE = new String(this.rawUE);
      return this.rawUE;
    }
    return super.getTextStreamValueAsByte(paramInt);
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
    throw new RuntimeException("Value not defined in getStringValue(int,mode) in " + this);
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

  public byte[][] getStringArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1752671921:
      return deepCopy(this.Recipients);
    }
    return super.getStringArray(paramInt);
  }

  public void setStringArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1752671921:
      this.Recipients = paramArrayOfByte;
      break;
    default:
      super.setStringArray(paramInt, paramArrayOfByte);
    }
  }

  public boolean decompressStreamWhenRead()
  {
    return false;
  }

  public int getObjectType()
  {
    return 1113489015;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.EncryptionObject
 * JD-Core Version:    0.6.2
 */
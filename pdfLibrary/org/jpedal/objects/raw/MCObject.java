package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class MCObject extends PdfObject
{
  private String ID;
  private String OC;
  private String Root;
  private String S;
  private byte[] rawID;
  private byte[] rawOC;
  private byte[] rawRoot;
  private byte[] rawS;
  int Kint = -1;
  int MCID = -1;
  private PdfObject A;
  private PdfObject ClassMap;
  private PdfObject K;
  private PdfObject Layer = null;
  private PdfObject ParentTree = null;
  private PdfObject Pg;
  private PdfObject RoleMap;
  private byte[] rawActualText;
  private byte[] rawLang;
  private byte[] rawIDTree;
  private byte[] rawT;
  private byte[][] Karray;
  private byte[][] Nums;
  private String ActualText;
  private String IDTree;
  private String Lang = null;
  private String T;

  public MCObject(String paramString)
  {
    super(paramString);
    this.objType = 487790868;
  }

  public MCObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
    this.objType = 487790868;
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
    case 17:
      return this.A;
    case 1448698499:
      return this.ClassMap;
    case 27:
      return this.K;
    case 826881374:
      return this.Layer;
    case 1719112618:
      return this.ParentTree;
    case 8247:
      return this.Pg;
    case 893350012:
      return this.RoleMap;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 27:
      this.Kint = paramInt2;
      break;
    case 487790868:
      this.MCID = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 27:
      return this.Kint;
    case 487790868:
      return this.MCID;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 17:
      this.A = paramPdfObject;
      break;
    case 1448698499:
      this.ClassMap = paramPdfObject;
      break;
    case 27:
      this.K = paramPdfObject;
      break;
    case 826881374:
      this.Layer = paramPdfObject;
      break;
    case 1719112618:
      this.ParentTree = paramPdfObject;
      break;
    case 8247:
      this.Pg = paramPdfObject;
      break;
    case 893350012:
      this.RoleMap = paramPdfObject;
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
    case 7955:
      this.rawOC = paramArrayOfByte;
      break;
    case 574570308:
      this.rawRoot = paramArrayOfByte;
      break;
    case 35:
      this.rawS = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1752861363:
      this.rawActualText = paramArrayOfByte;
      break;
    case 6420:
      this.rawID = paramArrayOfByte;
      break;
    case 608325193:
      this.rawIDTree = paramArrayOfByte;
      break;
    case 472989239:
      this.rawLang = paramArrayOfByte;
      break;
    case 36:
      this.rawT = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 7955:
      if ((this.OC == null) && (this.rawOC != null))
        this.OC = new String(this.rawOC);
      return this.OC;
    case 574570308:
      if ((this.Root == null) && (this.rawRoot != null))
        this.Root = new String(this.rawRoot);
      return this.Root;
    case 35:
      if ((this.S == null) && (this.rawS != null))
        this.S = new String(this.rawS);
      return this.S;
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 1752861363:
      if ((this.ActualText == null) && (this.rawActualText != null))
        this.ActualText = StringUtils.getTextString(this.rawActualText, false);
      return this.ActualText;
    case 6420:
      if ((this.ID == null) && (this.rawID != null))
        this.ID = new String(this.rawID);
      return this.ID;
    case 608325193:
      if ((this.IDTree == null) && (this.rawIDTree != null))
        this.IDTree = new String(this.rawIDTree);
      return this.IDTree;
    case 472989239:
      if ((this.Lang == null) && (this.rawLang != null))
        this.Lang = new String(this.rawLang);
      return this.Lang;
    case 36:
      if ((this.T == null) && (this.rawT != null))
        this.T = new String(this.rawT);
      return this.T;
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
    case 507854147:
      return this.Nums;
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 507854147:
      this.Nums = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public byte[][] getStringArray(int paramInt)
  {
    switch (paramInt)
    {
    case 27:
      return deepCopy(this.Karray);
    }
    return super.getStringArray(paramInt);
  }

  public void setStringArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 27:
      this.Karray = paramArrayOfByte;
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
    return this.objType;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.MCObject
 * JD-Core Version:    0.6.2
 */
package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class InfoObject extends PdfObject
{
  private String Author;
  private String CreationDate;
  private String Creator;
  private String ModDate;
  private String Producer;
  private String Keywords;
  private String Subject;
  private String Title;
  private String Trapped;
  private byte[] rawAuthor;
  private byte[] rawCreationDate;
  private byte[] rawCreator;
  private byte[] rawModDate;
  private byte[] rawProducer;
  private byte[] rawKeywords;
  private byte[] rawSubject;
  private byte[] rawTitle;
  private byte[] rawTrapped;

  public InfoObject(String paramString)
  {
    super(paramString);
  }

  public InfoObject(int paramInt1, int paramInt2)
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
    case 1080325989:
      this.rawTrapped = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1144541319:
      this.rawAuthor = paramArrayOfByte;
      break;
    case 1806481572:
      this.rawCreationDate = paramArrayOfByte;
      break;
    case 827818359:
      this.rawCreator = paramArrayOfByte;
      break;
    case 1517780362:
      this.rawKeywords = paramArrayOfByte;
      break;
    case 340689769:
      this.rawModDate = paramArrayOfByte;
      break;
    case 1702196342:
      this.rawProducer = paramArrayOfByte;
      break;
    case 978876534:
      this.rawSubject = paramArrayOfByte;
      break;
    case 960773209:
      this.rawTitle = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public byte[] getTextStreamValueAsByte(int paramInt)
  {
    switch (paramInt)
    {
    case 1144541319:
      return this.rawAuthor;
    case 1806481572:
      return this.rawCreationDate;
    case 827818359:
      return this.rawCreator;
    case 1517780362:
      return this.rawKeywords;
    case 340689769:
      return this.rawModDate;
    case 1702196342:
      return this.rawProducer;
    case 978876534:
      return this.rawSubject;
    case 960773209:
      return this.rawTitle;
    }
    super.getTextStreamValueAsByte(paramInt);
    return null;
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 1080325989:
      if ((this.Trapped == null) && (this.rawTrapped != null))
        this.Trapped = new String(this.rawTrapped);
      return this.Trapped;
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 1144541319:
      if ((this.Author == null) && (this.rawAuthor != null))
        this.Author = new String(this.rawAuthor);
      return this.Author;
    case 1806481572:
      if ((this.CreationDate == null) && (this.rawCreationDate != null))
        this.CreationDate = new String(this.rawCreationDate);
      return this.CreationDate;
    case 827818359:
      if ((this.Creator == null) && (this.rawCreator != null))
        this.Creator = new String(this.rawCreator);
      return this.Creator;
    case 1517780362:
      if ((this.Keywords == null) && (this.rawKeywords != null))
        this.Keywords = new String(this.rawKeywords);
      return this.Keywords;
    case 340689769:
      if ((this.ModDate == null) && (this.rawModDate != null))
        this.ModDate = new String(this.rawModDate);
      return this.ModDate;
    case 1702196342:
      if ((this.Producer == null) && (this.rawProducer != null))
        this.Producer = new String(this.rawProducer);
      return this.Producer;
    case 978876534:
      if ((this.Subject == null) && (this.rawSubject != null))
        this.Subject = new String(this.rawSubject);
      return this.Subject;
    case 960773209:
      if ((this.Title == null) && (this.rawTitle != null))
        this.Title = new String(this.rawTitle);
      return this.Title;
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
    return false;
  }

  public int getObjectType()
  {
    return 1365674082;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.InfoObject
 * JD-Core Version:    0.6.2
 */
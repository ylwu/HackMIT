package org.jpedal.objects.raw;

import java.io.PrintStream;

public class PageObject extends PdfObject
{
  private byte[][] Annots;
  private byte[][] Contents;
  private byte[][] Kids;
  private byte[][] OpenAction;
  PdfObject AA;
  PdfObject AcroForm;
  PdfObject Group;
  PdfObject OCProperties;
  PdfObject O;
  PdfObject OpenActionDict;
  PdfObject PO;
  PdfObject Properties;
  PdfObject PV;
  PdfObject Metadata;
  PdfObject Outlines;
  PdfObject Pages;
  PdfObject MarkInfo;
  PdfObject Names;
  PdfObject StructTreeRoot;
  private int StructParents = -1;
  private int pageMode = -1;

  public PageObject(String paramString)
  {
    super(paramString);
  }

  public PageObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public int getObjectType()
  {
    return 540096309;
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
    case 4369:
      return this.AA;
    case 661816444:
      return this.AcroForm;
    case 1111442775:
      return this.Group;
    case 913275002:
      return this.MarkInfo;
    case 1365674082:
      return this.Metadata;
    case 31:
      return this.O;
    case 2037870513:
      return this.OpenActionDict;
    case -1567847737:
      return this.OCProperties;
    case 1485011327:
      return this.Outlines;
    case 825701731:
      return this.Pages;
    case 8223:
      return this.PO;
    case -2089186617:
      return this.Properties;
    case 8230:
      return this.PV;
    case 826094945:
      return this.Names;
    case -2000237823:
      return this.StructTreeRoot;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case -1113539877:
      this.StructParents = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case -1113539877:
      return this.StructParents;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    switch (paramInt)
    {
    case 4369:
      this.AA = paramPdfObject;
      break;
    case 661816444:
      this.AcroForm = paramPdfObject;
      break;
    case 1111442775:
      this.Group = paramPdfObject;
      break;
    case -1567847737:
      this.OCProperties = paramPdfObject;
      break;
    case 913275002:
      this.MarkInfo = paramPdfObject;
      break;
    case 1365674082:
      this.Metadata = paramPdfObject;
      break;
    case 31:
      this.O = paramPdfObject;
      break;
    case 2037870513:
      this.OpenActionDict = paramPdfObject;
      break;
    case 1485011327:
      this.Outlines = paramPdfObject;
      break;
    case 825701731:
      this.Pages = paramPdfObject;
      break;
    case 8223:
      this.PO = paramPdfObject;
      break;
    case -2089186617:
      this.Properties = paramPdfObject;
      break;
    case 8230:
      this.PV = paramPdfObject;
      break;
    case 826094945:
      this.Names = paramPdfObject;
      break;
    case -2000237823:
      this.StructTreeRoot = paramPdfObject;
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
    for (int n = paramInt3 - 1; n > -1; n--)
    {
      int m = paramArrayOfByte[(paramInt2 + n)];
      m -= 48;
      j += (m << k);
      k += 8;
    }
    switch (j)
    {
    case 540096309:
      return super.setConstant(paramInt1, 540096309);
    case 825701731:
      return super.setConstant(paramInt1, 825701731);
    case 1030777706:
      this.pageMode = j;
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
    return i;
  }

  public int getParameterConstant(int paramInt)
  {
    switch (paramInt)
    {
    case 1030777706:
      return this.pageMode;
    }
    return super.getParameterConstant(paramInt);
  }

  public PdfArrayIterator getMixedArray(int paramInt)
  {
    switch (paramInt)
    {
    case 2037870513:
      return new PdfArrayIterator(this.OpenAction);
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

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1044338049:
      return deepCopy(this.Annots);
    case 1216184967:
      return deepCopy(this.Contents);
    case 456733763:
      return deepCopy(this.Kids);
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1044338049:
      this.Annots = paramArrayOfByte;
      break;
    case 456733763:
      this.Kids = paramArrayOfByte;
      break;
    case 1216184967:
      this.Contents = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 2037870513:
      this.OpenAction = paramArrayOfByte;
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
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.PageObject
 * JD-Core Version:    0.6.2
 */
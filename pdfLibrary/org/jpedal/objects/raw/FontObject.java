package org.jpedal.objects.raw;

import java.io.PrintStream;
import org.jpedal.utils.LogWriter;

public class FontObject extends PdfObject
{
  private static final int MMType1 = 1230852645;
  private static final int Type1C = 1077224796;
  private static final int ZaDb = 707859506;
  private static final int ZapfDingbats = 1889256112;
  private static final int Symbol = 1026712197;
  private PdfObject CharProcs = null;
  private PdfObject CIDSet = null;
  private PdfObject CIDSystemInfo = null;
  private PdfObject CIDToGIDMap = null;
  private PdfObject DescendantFonts = null;
  private PdfObject FontDescriptor = null;
  private PdfObject FontFile;
  private PdfObject FontFile2;
  private PdfObject FontFile3;
  private PdfObject ToUnicode;
  int BaseEncoding = -1;
  int CIDToGIDMapAsConstant = -1;
  int FirstChar = 1;
  int LastChar = 255;
  int Flags = 0;
  int MissingWidth = 0;
  int DW = -1;
  int StemV = 0;
  int Supplement = 0;
  float Ascent = 0.0F;
  float Descent = 0.0F;
  float[] Widths = null;
  float[] FontBBox = null;
  double[] FontMatrix = null;
  byte[][] Differences = (byte[][])null;
  private byte[] rawBaseFont = null;
  private byte[] rawCharSet = null;
  private byte[] rawCMapName = null;
  private byte[] rawFontName = null;
  private byte[] rawFontStretch = null;
  private byte[] rawOrdering = null;
  private byte[] rawRegistry = null;
  private byte[] rawW = null;
  private byte[] rawW2 = null;
  private String BaseFont = null;
  private String CharSet = null;
  private String CMapName = null;
  private String FontName = null;
  private String FontStretch = null;
  private String Ordering = null;
  private String Registry = null;
  private String W = null;
  private String W2 = null;

  public FontObject(String paramString)
  {
    super(paramString);
  }

  public FontObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  public FontObject(int paramInt)
  {
    super(paramInt);
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 2054190454:
      return this.CharProcs;
    case 337856605:
      return this.CIDSet;
    case 1972801240:
      return this.CIDSystemInfo;
    case 946823533:
      return this.CIDToGIDMap;
    case -1547306032:
      return this.DescendantFonts;
    case 1232564598:
      return this.Encoding;
    case -1044665361:
      return this.FontDescriptor;
    case 746093177:
      return this.FontFile;
    case 2021292334:
      return this.FontFile2;
    case 2021292335:
      return this.FontFile3;
    case 1919185554:
      return this.ToUnicode;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 859131783:
      this.Ascent = paramInt2;
      break;
    case 860451719:
      this.Descent = paramInt2;
      break;
    case 5159:
      this.DW = paramInt2;
      break;
    case 1283093660:
      this.FirstChar = paramInt2;
      break;
    case 1009858393:
      this.Flags = paramInt2;
      break;
    case 795440262:
      this.LastChar = paramInt2;
      break;
    case -1884569950:
      this.MissingWidth = paramInt2;
      break;
    case 1144339785:
      this.StemV = paramInt2;
      break;
    case 2104860094:
      this.Supplement = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 5159:
      return this.DW;
    case 1283093660:
      return this.FirstChar;
    case 1009858393:
      return this.Flags;
    case 795440262:
      return this.LastChar;
    case -1884569950:
      return this.MissingWidth;
    case 1144339785:
      return this.StemV;
    case 2104860094:
      return this.Supplement;
    }
    return super.getInt(paramInt);
  }

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 859131783:
      this.Ascent = paramFloat;
      break;
    case 860451719:
      this.Descent = paramFloat;
      break;
    default:
      super.setFloatNumber(paramInt, paramFloat);
    }
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    case 859131783:
      return this.Ascent;
    case 860451719:
      return this.Descent;
    }
    return super.getFloatNumber(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    if ((paramInt == 746093177) || (paramInt == 2021292334) || (paramInt == 2021292335))
      this.hasStream = true;
    switch (paramInt)
    {
    case 2054190454:
      this.CharProcs = paramPdfObject;
      break;
    case 337856605:
      this.CIDSet = paramPdfObject;
      break;
    case 1972801240:
      this.CIDSystemInfo = paramPdfObject;
      break;
    case 946823533:
      this.CIDToGIDMap = paramPdfObject;
      break;
    case -1547306032:
      this.DescendantFonts = paramPdfObject;
      break;
    case 1232564598:
      this.Encoding = paramPdfObject;
      break;
    case -1044665361:
      this.FontDescriptor = paramPdfObject;
      break;
    case 746093177:
      this.FontFile = paramPdfObject;
      break;
    case 2021292334:
      this.FontFile2 = paramPdfObject;
      break;
    case 2021292335:
      this.FontFile3 = paramPdfObject;
      break;
    case 1919185554:
      this.ToUnicode = paramPdfObject;
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
      case -1684566726:
        i = -1684566726;
        break;
      case -1752352082:
        i = -1752352082;
        break;
      case -1684566724:
        i = -1684566724;
        break;
      case 320680256:
        i = 320680256;
        break;
      case 1232564598:
        i = 1232564598;
        break;
      case 2038913669:
        i = 2038913669;
        break;
      case 2038913683:
        i = 2038913683;
        break;
      case -1159739105:
        i = 3;
        break;
      case -1511664170:
        i = 0;
        break;
      case 1602998461:
        i = 6;
        break;
      case 1230852645:
        i = 1228944677;
        break;
      case -1595087640:
        i = 1;
        break;
      case 1228944676:
        i = 1228944676;
        break;
      case 1228944677:
        i = 1228944677;
        break;
      case 1077224796:
        i = 1228944677;
        break;
      case 1228944679:
        i = 1228944679;
        break;
      case 1217103210:
        i = 1217103210;
        break;
      case 1524428269:
        i = 2;
        break;
      default:
        byte[] arrayOfByte;
        if (paramInt1 == 1232564598)
        {
          i = CIDEncodings.getConstant(j);
          if ((debug) && (i == -1))
          {
            System.out.println("Value not in PdfCIDEncodings");
            arrayOfByte = new byte[paramInt3];
            System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
            System.out.println("Add to CIDEncodings and as String");
            System.out.println("key=" + new String(arrayOfByte) + ' ' + j + " not implemented in setConstant in PdfFont Object");
            System.out.println("final public static int CMAP_" + new String(arrayOfByte) + '=' + j + ';');
          }
        }
        else
        {
          i = super.setConstant(paramInt1, j);
        }
        if ((i == -1) && (debug))
        {
          arrayOfByte = new byte[paramInt3];
          System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
          System.out.println("key=" + new String(arrayOfByte) + ' ' + j + " not implemented in setConstant in PdfFont Object");
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
    case 1537782955:
      this.BaseEncoding = i;
      break;
    case 946823533:
      this.CIDToGIDMapAsConstant = i;
      break;
    case 1232564598:
      this.generalType = i;
      break;
    case 1919185554:
      this.generalType = i;
      break;
    default:
      super.setConstant(paramInt1, i);
    }
    return i;
  }

  public int getParameterConstant(int paramInt)
  {
    switch (paramInt)
    {
    case 1537782955:
      if ((paramInt == 1537782955) && (this.Encoding != null) && (this.Encoding.isZapfDingbats))
        return 5;
      if ((paramInt == 1537782955) && (this.Encoding != null) && (this.Encoding.isSymbol))
        return 4;
      return this.BaseEncoding;
    case 946823533:
      return this.CIDToGIDMapAsConstant;
    }
    int i = super.getParameterConstant(paramInt);
    return i;
  }

  public PdfArrayIterator getMixedArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1954328750:
      return new PdfArrayIterator(this.Differences);
    }
    return super.getMixedArray(paramInt);
  }

  public byte[][] getByteArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1954328750:
      return this.Differences;
    }
    return super.getByteArray(paramInt);
  }

  public double[] getDoubleArray(int paramInt)
  {
    switch (paramInt)
    {
    case -2105119560:
      return deepCopy(this.FontMatrix);
    }
    return super.getDoubleArray(paramInt);
  }

  public void setDoubleArray(int paramInt, double[] paramArrayOfDouble)
  {
    switch (paramInt)
    {
    case -2105119560:
      this.FontMatrix = paramArrayOfDouble;
      break;
    default:
      super.setDoubleArray(paramInt, paramArrayOfDouble);
    }
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1954328750:
      this.Differences = paramArrayOfByte;
      break;
    default:
      super.setMixedArray(paramInt, paramArrayOfByte);
    }
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    case 676429196:
      return deepCopy(this.FontBBox);
    case 876896124:
      return deepCopy(this.Widths);
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 676429196:
      this.FontBBox = paramArrayOfFloat;
      break;
    case 876896124:
      this.Widths = paramArrayOfFloat;
      break;
    default:
      super.setFloatArray(paramInt, paramArrayOfFloat);
    }
  }

  public void setName(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 678461817:
      this.rawBaseFont = paramArrayOfByte;
      int i = PdfDictionary.generateChecksum(0, paramArrayOfByte.length, paramArrayOfByte);
      this.isZapfDingbats = ((i == 1889256112) || (i == 707859506));
      this.isSymbol = (i == 1026712197);
      if (this.Encoding != null)
      {
        this.Encoding.isZapfDingbats = this.isZapfDingbats;
        this.Encoding.isSymbol = this.isSymbol;
      }
      break;
    case 827223669:
      this.rawCMapName = paramArrayOfByte;
      break;
    case 879786873:
      this.rawFontName = paramArrayOfByte;
      break;
    case 2038281912:
      this.rawFontStretch = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1110863221:
      this.rawCharSet = paramArrayOfByte;
      break;
    case 1635480172:
      this.rawOrdering = paramArrayOfByte;
      break;
    case 1702459778:
      this.rawRegistry = paramArrayOfByte;
      break;
    case 39:
      this.rawW = paramArrayOfByte;
      break;
    case 9986:
      this.rawW2 = paramArrayOfByte;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 678461817:
      if ((this.BaseFont == null) && (this.rawBaseFont != null))
        this.BaseFont = new String(this.rawBaseFont);
      return this.BaseFont;
    case 827223669:
      if ((this.CMapName == null) && (this.rawCMapName != null))
        this.CMapName = new String(this.rawCMapName);
      return this.CMapName;
    case 879786873:
      if ((this.FontName == null) && (this.rawFontName != null))
        this.FontName = new String(this.rawFontName);
      return this.FontName;
    case 2038281912:
      if ((this.FontStretch == null) && (this.rawFontStretch != null))
        this.FontStretch = new String(this.rawFontStretch);
      return this.FontStretch;
    case 39:
      if ((this.W == null) && (this.rawW != null))
        this.W = new String(this.rawW);
      return this.W;
    case 9986:
      if ((this.W2 == null) && (this.rawW2 != null))
        this.W2 = new String(this.rawW2);
      return this.W2;
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 1110863221:
      if ((this.CharSet == null) && (this.rawCharSet != null))
        this.CharSet = new String(this.rawCharSet);
      return this.CharSet;
    case 1635480172:
      if ((this.Ordering == null) && (this.rawOrdering != null))
        this.Ordering = new String(this.rawOrdering);
      return this.Ordering;
    case 1702459778:
      if ((this.Registry == null) && (this.rawRegistry != null))
        this.Registry = new String(this.rawRegistry);
      return this.Registry;
    case 39:
      if ((this.W == null) && (this.rawW != null))
        this.W = new String(this.rawW);
      return this.W;
    case 9986:
      if ((this.W2 == null) && (this.rawW2 != null))
        this.W2 = new String(this.rawW2);
      return this.W2;
    }
    return super.getTextStreamValue(paramInt);
  }

  public String getStringValue(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = null;
    switch (paramInt1)
    {
    case 678461817:
      arrayOfByte1 = this.rawBaseFont;
      break;
    case 827223669:
      arrayOfByte1 = this.rawCMapName;
      break;
    case 879786873:
      arrayOfByte1 = this.rawFontName;
      break;
    case 2038281912:
      arrayOfByte1 = this.rawFontStretch;
    }
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
        int j;
        byte[] arrayOfByte2;
        if ((i > 6) && (arrayOfByte1[6] == 43))
        {
          j = i - 7;
          arrayOfByte2 = new byte[j];
          System.arraycopy(arrayOfByte1, 7, arrayOfByte2, 0, j);
          return new String(arrayOfByte2);
        }
        if ((i > 7) && (arrayOfByte1[(i - 7)] == 43))
        {
          j = i - 7;
          arrayOfByte2 = new byte[j];
          System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, j);
          return new String(arrayOfByte2);
        }
        return new String(arrayOfByte1);
      }
      return null;
    }
    throw new RuntimeException("Value not defined in getName(int,mode)");
  }

  public int getObjectType()
  {
    return 373243460;
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.FontObject
 * JD-Core Version:    0.6.2
 */
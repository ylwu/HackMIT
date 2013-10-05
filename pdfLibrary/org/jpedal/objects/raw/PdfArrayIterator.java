package org.jpedal.objects.raw;

import org.jpedal.fonts.StandardFonts;
import org.jpedal.utils.NumberUtils;

public class PdfArrayIterator
{
  public static final int TYPE_KEY_INTEGER = 1;
  public static final int TYPE_VALUE_INTEGER = 2;
  byte[][] rawData = (byte[][])null;
  boolean hasHexChars = false;
  int tokenCount = 0;
  int currentToken = 0;
  int spaceChar = -1;

  public PdfArrayIterator(byte[][] paramArrayOfByte)
  {
    this.rawData = paramArrayOfByte;
    if (paramArrayOfByte != null)
      this.tokenCount = paramArrayOfByte.length;
  }

  public boolean hasMoreTokens()
  {
    return this.currentToken < this.tokenCount;
  }

  public int getNextValueType()
  {
    if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
      return -1;
    int j = this.rawData[this.currentToken].length;
    int k = 1;
    for (int m = 0; m < j; m++)
    {
      int i = this.rawData[this.currentToken][m];
      if ((i < 47) || (i >= 58))
      {
        m = j;
        k = 0;
      }
    }
    if (k != 0)
    {
      if (this.rawData[this.currentToken][0] != 47)
        return 1;
      return 2;
    }
    return -1;
  }

  public String getNextValueAsFontChar(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str;
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      int i = this.rawData[this.currentToken].length - 1;
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(this.rawData[this.currentToken], 1, arrayOfByte, 0, i);
      str = new String(arrayOfByte);
      str = StandardFonts.convertNumberToGlyph(str, paramBoolean1, paramBoolean2);
      int j = str.charAt(0);
      if ((j == 66) || (j == 99) || (j == 67) || (j == 71))
      {
        int k = 1;
        int m = str.length();
        while ((!this.hasHexChars) && (k < m))
          this.hasHexChars = Character.isLetter(str.charAt(k++));
      }
      if ((arrayOfByte.length == 5) && (arrayOfByte[0] == 115) && (arrayOfByte[1] == 112) && (arrayOfByte[2] == 97) && (arrayOfByte[3] == 99) && (arrayOfByte[4] == 101))
        this.spaceChar = paramInt;
      this.currentToken += 1;
    }
    else
    {
      throw new RuntimeException("Out of range exception with PdfArrayIterator");
    }
    return str;
  }

  public float getNextValueAsFloat()
  {
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      byte[] arrayOfByte = this.rawData[this.currentToken];
      this.currentToken += 1;
      if ((arrayOfByte[0] == 110) && (arrayOfByte[1] == 117) && (arrayOfByte[2] == 108) && (arrayOfByte[2] == 108))
        return 0.0F;
      return NumberUtils.parseFloat(0, arrayOfByte.length, arrayOfByte);
    }
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public int getNextValueAsInteger()
  {
    return getNextValueAsInteger(true);
  }

  public int getNextValueAsInteger(boolean paramBoolean)
  {
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      byte[] arrayOfByte = this.rawData[this.currentToken];
      if (paramBoolean)
        this.currentToken += 1;
      int i = 0;
      if (arrayOfByte[0] == 47)
        i++;
      return NumberUtils.parseInt(i, arrayOfByte.length, arrayOfByte);
    }
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public float[] getNextValueAsFloatArray()
  {
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      byte[] arrayOfByte = this.rawData[this.currentToken];
      this.currentToken += 1;
      if ((arrayOfByte[0] == 110) && (arrayOfByte[1] == 117) && (arrayOfByte[2] == 108) && (arrayOfByte[2] == 108))
        return new float[1];
      int i = arrayOfByte.length;
      int j = 1;
      int k = 0;
      for (int m = 1; m < i; m++)
        if (((arrayOfByte[m] == 32) || (arrayOfByte[m] == 10) || (arrayOfByte[m] == 13)) && (arrayOfByte[(m - 1)] != 32) && (arrayOfByte[(m - 1)] == 10) && (arrayOfByte[(m - 1)] == 13))
          j++;
      float[] arrayOfFloat = new float[j];
      for (int i1 = 0; i1 < i; i1++)
      {
        while ((i1 < i) && ((arrayOfByte[i1] == 32) || (arrayOfByte[i1] == 10) || (arrayOfByte[i1] == 13)))
          i1++;
        int n = i1;
        while ((i1 < i) && (arrayOfByte[i1] != 32) && (arrayOfByte[i1] != 10) && (arrayOfByte[i1] != 13))
          i1++;
        arrayOfFloat[k] = NumberUtils.parseFloat(n, i1, arrayOfByte);
        k++;
      }
      return arrayOfFloat;
    }
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public int getNextValueAsConstant(boolean paramBoolean)
  {
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      byte[] arrayOfByte = this.rawData[this.currentToken];
      if (paramBoolean)
        this.currentToken += 1;
      return PdfDictionary.getIntKey(1, arrayOfByte.length - 1, arrayOfByte);
    }
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public int getSpaceChar()
  {
    return this.spaceChar;
  }

  public boolean hasHexChars()
  {
    return this.hasHexChars;
  }

  public int getTokenCount()
  {
    return this.tokenCount;
  }

  public String getNextValueAsString(boolean paramBoolean)
  {
    String str = "";
    if (this.currentToken < this.tokenCount)
    {
      if (this.rawData == null)
        throw new RuntimeException("Null Value exception with PdfArrayIterator rawData=" + this.rawData);
      byte[] arrayOfByte = this.rawData[this.currentToken];
      if (arrayOfByte != null)
        str = new String(arrayOfByte);
      if (paramBoolean)
        this.currentToken += 1;
    }
    else
    {
      throw new RuntimeException("Out of range exception with PdfArrayIterator");
    }
    return str;
  }

  public byte[] getNextValueAsByte(boolean paramBoolean)
  {
    byte[] arrayOfByte1 = null;
    if (this.currentToken < this.tokenCount)
    {
      if (this.rawData == null)
        throw new RuntimeException("Null Value exception with PdfArrayIterator rawData=" + this.rawData);
      byte[] arrayOfByte2 = this.rawData[this.currentToken];
      if (arrayOfByte2 != null)
      {
        int i = arrayOfByte2.length;
        arrayOfByte1 = new byte[i];
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
      }
      if (paramBoolean)
        this.currentToken += 1;
    }
    else
    {
      throw new RuntimeException("Out of range exception with PdfArrayIterator");
    }
    return arrayOfByte1;
  }

  public int getNextValueAsKey()
  {
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        throw new RuntimeException("NullValue exception with PdfArrayIterator");
      byte[] arrayOfByte = this.rawData[this.currentToken];
      this.currentToken += 1;
      return PdfDictionary.getIntKey(0, arrayOfByte.length, arrayOfByte);
    }
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public boolean isNextValueRef()
  {
    boolean bool;
    if (this.currentToken < this.tokenCount)
    {
      if ((this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0))
        return false;
      byte[] arrayOfByte = this.rawData[this.currentToken];
      bool = arrayOfByte[(arrayOfByte.length - 1)] == 82;
    }
    else
    {
      throw new RuntimeException("Out of range exception with PdfArrayIterator");
    }
    return bool;
  }

  public boolean isNextValueNull()
  {
    if (this.currentToken < this.tokenCount)
      return (this.rawData == null) || (this.rawData[this.currentToken] == null) || (this.rawData[this.currentToken].length == 0);
    throw new RuntimeException("Out of range exception with PdfArrayIterator");
  }

  public void resetToStart()
  {
    if (this.rawData != null)
      this.tokenCount = this.rawData.length;
    this.currentToken = 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.PdfArrayIterator
 * JD-Core Version:    0.6.2
 */
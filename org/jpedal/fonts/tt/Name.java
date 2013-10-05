package org.jpedal.fonts.tt;

import java.util.HashMap;
import java.util.Map;

public class Name extends Table
{
  private int encoding = 0;
  private Map strings = new HashMap();
  public static final Integer COPYRIGHT_NOTICE = Integer.valueOf(0);
  public static final Integer FONT_FAMILY_NAME = Integer.valueOf(1);
  public static final Integer FONT_SUBFAMILY_NAME = Integer.valueOf(2);
  public static final Integer UNIQUE_FONT_IDENTIFIER = Integer.valueOf(3);
  public static final Integer FULL_FONT_NAME = Integer.valueOf(4);
  public static final Integer VERSION_STRING = Integer.valueOf(5);
  public static final Integer POSTSCRIPT_NAME = Integer.valueOf(6);
  public static final Integer TRADEMARK = Integer.valueOf(7);
  public static final Integer MANUFACTURER = Integer.valueOf(8);
  public static final Integer DESIGNER = Integer.valueOf(9);
  public static final Integer DESCRIPTION = Integer.valueOf(10);
  public static final Integer VENDOR_URL = Integer.valueOf(11);
  public static final Integer DESIGNER_URL = Integer.valueOf(12);
  public static final Integer LICENSE = Integer.valueOf(13);
  public static final Integer LICENSE_URL = Integer.valueOf(14);
  public static final Integer PREFERRED_FAMILY = Integer.valueOf(16);
  public static final Integer PREFERRED_SUBFAMILY = Integer.valueOf(17);
  public static final Integer COMPATIBLE = Integer.valueOf(18);
  public static final Integer SAMPLE_TEXT = Integer.valueOf(19);
  public static final String[] stringNames = { "COPYRIGHT_NOTICE", "FONT_FAMILY_NAME", "FONT_SUBFAMILY_NAME", "UNIQUE_FONT_IDENTIFIER", "FULL_FONT_NAME", "VERSION_STRING", "POSTSCRIPT_NAME", "TRADEMARK", "MANUFACTURER", "DESIGNER", "DESCRIPTION", "VENDOR_URL", "DESIGNER_URL", "LICENSE", "LICENSE_URL", "PREFERRED_FAMILY", "PREFERRED_SUBFAMILY", "COMPATIBLE", "SAMPLE_TEXT" };

  public Name(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(7);
    if (i != 0)
    {
      paramFontFile2.getNextUint16();
      int j = paramFontFile2.getNextUint16();
      int k = paramFontFile2.getNextUint16();
      for (int m = 0; m < j; m++)
      {
        int n = paramFontFile2.getNextUint16();
        int i1 = paramFontFile2.getNextUint16();
        int i2 = paramFontFile2.getNextUint16();
        int i3 = paramFontFile2.getNextUint16();
        int i4 = paramFontFile2.getNextUint16();
        int i5 = paramFontFile2.getNextUint16();
        if (((n == 1) && (i1 == 0) && (i2 == 0)) || ((n == 3) && (i1 == 0) && (i2 == 1033)) || ((n == 3) && (i1 == 1) && (i2 == 1033)))
        {
          int i6 = paramFontFile2.getPointer();
          paramFontFile2.setPointer(i + k + i5);
          if ((n == 0) || (n == 3))
            i4 /= 2;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.setLength(i4);
          for (int i8 = 0; i8 < i4; i8++)
          {
            int i7;
            if ((n == 0) || (n == 3))
              i7 = paramFontFile2.getNextUint16();
            else
              i7 = paramFontFile2.getNextUint8();
            localStringBuilder.setCharAt(i8, (char)i7);
          }
          String str = localStringBuilder.toString();
          if (str != null)
            this.strings.put(Integer.valueOf(i3), str);
          paramFontFile2.setPointer(i6);
        }
      }
    }
  }

  public Name()
  {
  }

  public Map getStrings()
  {
    return this.strings;
  }

  public int getEncoding()
  {
    return this.encoding;
  }

  public String getString(Integer paramInteger)
  {
    return (String)this.strings.get(paramInteger);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Name
 * JD-Core Version:    0.6.2
 */
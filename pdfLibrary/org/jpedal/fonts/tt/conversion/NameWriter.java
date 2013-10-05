package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Name;

public class NameWriter extends Name
  implements FontTableWriter
{
  String[] strings = new String[7];

  public NameWriter(PdfFont paramPdfFont, PdfJavaGlyphs paramPdfJavaGlyphs, String paramString)
  {
    paramString = paramString.replaceAll("[.,<>*#]", "-");
    if (paramPdfFont.getCopyright() != null)
      this.strings[0] = paramPdfFont.getCopyright();
    else
      this.strings[0] = "No copyright information found.";
    this.strings[1] = paramString;
    switch (paramPdfJavaGlyphs.style)
    {
    case 0:
      this.strings[2] = "Roman";
      break;
    case 1:
      this.strings[2] = "Bold";
      break;
    case 2:
      this.strings[2] = "Italic";
      break;
    default:
      this.strings[2] = "Roman";
    }
    this.strings[3] = ("JPedal PDF2HTML " + paramString + ' ' + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
    this.strings[4] = paramString;
    this.strings[5] = "Version 1.0";
    this.strings[6] = paramString;
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(14));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(174));
    int i = 0;
    for (int j = 0; j < 7; j++)
    {
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(1));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(j));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.strings[j].length()));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(i));
      i += this.strings[j].length();
    }
    for (j = 0; j < 7; j++)
    {
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(3));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(1));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(1033));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(j));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.strings[j].length() * 2));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(i));
      i += this.strings[j].length() * 2;
    }
    byte[] arrayOfByte1;
    int n;
    for (j = 0; j < 7; j++)
    {
      arrayOfByte1 = this.strings[j].getBytes("US-ASCII");
      for (n : arrayOfByte1)
        localByteArrayOutputStream.write(TTFontWriter.setNextUint8(n));
    }
    for (j = 0; j < 7; j++)
    {
      arrayOfByte1 = this.strings[j].getBytes("UTF-16BE");
      for (n : arrayOfByte1)
        localByteArrayOutputStream.write(TTFontWriter.setNextUint8(n));
    }
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    }
    return i;
  }

  public String getString(int paramInt)
  {
    return this.strings[paramInt];
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.NameWriter
 * JD-Core Version:    0.6.2
 */
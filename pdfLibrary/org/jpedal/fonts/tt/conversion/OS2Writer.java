package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Hhea;
import org.jpedal.fonts.tt.OS2;

public class OS2Writer extends OS2
  implements FontTableWriter
{
  public static final byte[] fontPanose = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
  int xAvgCharWidth = 0;
  int minCharCode;
  int maxCharCode;
  float[] bounds;
  double scaling = 1.0D;
  PdfFont originalFont;
  PdfJavaGlyphs glyphs;

  public OS2Writer(PdfFont paramPdfFont, PdfJavaGlyphs paramPdfJavaGlyphs, int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat, double paramDouble)
  {
    this.originalFont = paramPdfFont;
    this.glyphs = paramPdfJavaGlyphs;
    this.minCharCode = paramInt2;
    this.maxCharCode = paramInt3;
    this.bounds = paramArrayOfFloat;
    this.xAvgCharWidth = paramInt1;
    if (paramDouble != 1000.0D)
      this.scaling = (1.0D / (paramDouble / 1000.0D));
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (this.originalFont.getFontType() == 1217103210)
    {
      Hhea localHhea = (Hhea)this.glyphs.getTable(5);
      int i = localHhea.getIntValue(1);
      int j = localHhea.getIntValue(2);
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(3));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.xAvgCharWidth));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(400));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(5));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(i * 0.3D)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(fontPanose);
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(new byte[4]);
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(64));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.minCharCode));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.maxCharCode));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(i + j));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(i));
      j = Math.abs(j);
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(j));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
    }
    else
    {
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(3));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.xAvgCharWidth));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(400));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(5));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(102));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(fontPanose);
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(new byte[4]);
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(64));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.minCharCode));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.maxCharCode));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(1000));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16((int)(this.bounds[3] * this.scaling)));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(-(int)(this.bounds[1] * this.scaling)));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(0));
    }
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.OS2Writer
 * JD-Core Version:    0.6.2
 */
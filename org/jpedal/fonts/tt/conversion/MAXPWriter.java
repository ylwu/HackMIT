package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Maxp;

public class MAXPWriter extends Maxp
  implements FontTableWriter
{
  int glyphCount = 0;

  public MAXPWriter(PdfJavaGlyphs paramPdfJavaGlyphs)
  {
    this.glyphCount = paramPdfJavaGlyphs.getGlyphCount();
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(20480));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.glyphCount));
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
 * Qualified Name:     org.jpedal.fonts.tt.conversion.MAXPWriter
 * JD-Core Version:    0.6.2
 */
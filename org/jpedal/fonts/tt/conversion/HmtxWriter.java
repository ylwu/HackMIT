package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.tt.Hmtx;

public class HmtxWriter extends Hmtx
  implements FontTableWriter
{
  int glyphCount;
  int[] advanceWidth;
  int[] leftSideBearing;

  public HmtxWriter(PdfJavaGlyphs paramPdfJavaGlyphs, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.glyphCount = paramPdfJavaGlyphs.getGlyphCount();
    this.advanceWidth = paramArrayOfInt1;
    if (paramArrayOfInt2 == null)
      this.leftSideBearing = new int[65535];
    else
      this.leftSideBearing = paramArrayOfInt2;
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i < this.glyphCount; i++)
    {
      localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.advanceWidth[i]));
      localByteArrayOutputStream.write(TTFontWriter.setNextInt16(this.leftSideBearing[i]));
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
 * Qualified Name:     org.jpedal.fonts.tt.conversion.HmtxWriter
 * JD-Core Version:    0.6.2
 */
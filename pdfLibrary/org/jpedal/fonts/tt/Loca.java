package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;

public class Loca extends Table
{
  int[] glyphIndexStart;
  boolean isCorrupted = false;
  public int format;
  public int glyphCount;
  public int glyfTableLength;

  public Loca(FontFile2 paramFontFile2, int paramInt1, int paramInt2)
  {
    if (paramFontFile2 == null)
      return;
    this.format = paramInt2;
    this.glyphCount = paramInt1;
    this.glyfTableLength = paramFontFile2.getOffset(4);
    int i = paramFontFile2.selectTable(3);
    int k = paramFontFile2.getOffset(3);
    this.glyphIndexStart = new int[paramInt1 + 1];
    if (i != 0)
    {
      this.glyphIndexStart[0] = 0;
      int j;
      if (paramInt2 == 1)
      {
        if ((LogWriter.isOutput()) && (k / 4 != paramInt1 + 1))
          LogWriter.writeLog("Incorrect length");
        for (j = 0; j < paramInt1; j++)
          this.glyphIndexStart[j] = paramFontFile2.getNextUint32();
      }
      if (k / 2 != paramInt1 + 1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Incorrect length");
        this.isCorrupted = true;
      }
      else
      {
        for (j = 0; j < paramInt1; j++)
          this.glyphIndexStart[j] = (paramFontFile2.getNextUint16() * 2);
      }
      this.glyphIndexStart[paramInt1] = this.glyfTableLength;
    }
  }

  public int[] getIndices()
  {
    return this.glyphIndexStart;
  }

  public boolean isCorrupted()
  {
    return this.isCorrupted;
  }

  public int getFormat()
  {
    return this.format;
  }

  public int getGlyphCount()
  {
    return this.glyphCount;
  }

  public int getGlyfTableLength()
  {
    return this.glyfTableLength;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Loca
 * JD-Core Version:    0.6.2
 */
package org.jpedal.fonts.tt.conversion;

import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.fonts.tt.Maxp;
import org.jpedal.utils.LogWriter;

public class TTFontWriter extends FontWriter
{
  byte[] rawFontData;
  byte[] cmap = null;

  public TTFontWriter(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
    Maxp localMaxp = new Maxp(new FontFile2(paramArrayOfByte));
    this.glyphCount = localMaxp.getGlyphCount();
    this.rawFontData = paramArrayOfByte;
  }

  void readTables()
  {
    this.cmap = null;
    TTFontWriter localTTFontWriter = new TTFontWriter(this.rawFontData);
    CMAPWriter localCMAPWriter = new CMAPWriter(localTTFontWriter, localTTFontWriter.selectTable(2));
    try
    {
      this.cmap = localCMAPWriter.writeTable();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public byte[] getTableBytes(int paramInt)
  {
    if (paramInt == 2)
      return this.cmap;
    return super.getTableBytes(paramInt);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.TTFontWriter
 * JD-Core Version:    0.6.2
 */
package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;

public class Head extends Table
{
  protected int format = 0;
  protected float[] FontBBox = new float[4];
  protected int flags = 0;
  protected int unitsPerEm = 1024;

  public Head(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(0);
    if (i == 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No head table found");
    }
    else
    {
      paramFontFile2.getNextUint32();
      for (int j = 0; j < 3; j++)
        paramFontFile2.getNextUint32();
      this.flags = paramFontFile2.getNextUint16();
      this.unitsPerEm = paramFontFile2.getNextUint16();
      for (j = 0; j < 2; j++)
        paramFontFile2.getNextUint64();
      for (j = 0; j < 4; j++)
        this.FontBBox[j] = paramFontFile2.getNextSignedInt16();
      for (j = 0; j < 3; j++)
        paramFontFile2.getNextUint16();
      this.format = paramFontFile2.getNextUint16();
    }
  }

  public Head()
  {
  }

  public int getFormat()
  {
    return this.format;
  }

  public float[] getFontBBox()
  {
    return this.FontBBox;
  }

  public int getUnitsPerEm()
  {
    return this.unitsPerEm;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Head
 * JD-Core Version:    0.6.2
 */
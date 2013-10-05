package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;

public class Hmtx extends Table
{
  private int[] hMetrics;
  private short[] leftSideBearing;
  private float scaling = 0.001F;

  public Hmtx(FontFile2 paramFontFile2, int paramInt1, int paramInt2, int paramInt3)
  {
    this.scaling = paramInt3;
    if (paramInt2 < 0)
      paramInt2 = -paramInt2;
    int i = paramFontFile2.selectTable(6);
    int j = paramInt1 - paramInt2;
    this.hMetrics = new int[paramInt1];
    this.leftSideBearing = new short[paramInt1];
    int k = 0;
    if (i == 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No Htmx table found");
    }
    else if (j < 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Invalid Htmx table found");
    }
    else
    {
      for (int m = 0; m < paramInt2; m++)
      {
        k = paramFontFile2.getNextUint16();
        this.hMetrics[m] = k;
        this.leftSideBearing[m] = paramFontFile2.getNextInt16();
      }
      int n = paramFontFile2.getTableSize(6);
      int i1 = n - m * 4;
      j = i1 / 2;
      for (int i2 = m; i2 < j; i2++)
      {
        this.hMetrics[i2] = k;
        this.leftSideBearing[i2] = paramFontFile2.getFWord();
      }
    }
  }

  public Hmtx()
  {
  }

  public short getRAWLSB(int paramInt)
  {
    if ((this.leftSideBearing == null) || (paramInt >= this.leftSideBearing.length))
      return 0;
    return this.leftSideBearing[paramInt];
  }

  public short getLeftSideBearing(int paramInt)
  {
    if (paramInt < this.hMetrics.length)
      return (short)(this.hMetrics[paramInt] & 0xFFFF);
    if (this.leftSideBearing == null)
      return 0;
    try
    {
      return this.leftSideBearing[(paramInt - this.hMetrics.length)];
    }
    catch (Exception localException)
    {
    }
    return 0;
  }

  public float getAdvanceWidth(int paramInt)
  {
    return (this.hMetrics[paramInt] - getLeftSideBearing(paramInt)) / this.scaling;
  }

  public float getWidth(int paramInt)
  {
    float f = this.hMetrics[paramInt];
    return f / this.scaling;
  }

  public float getUnscaledWidth(int paramInt)
  {
    return this.hMetrics[paramInt];
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Hmtx
 * JD-Core Version:    0.6.2
 */
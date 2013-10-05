package org.jpedal.fonts.tt.hinting;

import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.fonts.tt.Table;

public class Cvt extends Table
{
  short[] unscaledCvt;
  int[] cvt;
  double scale;
  private static boolean messageDisplayed = false;

  public Cvt(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(9);
    if (i != 0)
    {
      int j = paramFontFile2.getOffset(9) / 2;
      this.unscaledCvt = new short[j];
      this.cvt = new int[j];
      for (int k = 0; k < j; k++)
        this.unscaledCvt[k] = paramFontFile2.getFWord();
    }
    else
    {
      this.unscaledCvt = new short[0];
      this.cvt = new int[0];
    }
  }

  public void scale(double paramDouble)
  {
    this.scale = paramDouble;
    for (int i = 0; i < this.unscaledCvt.length; i++)
      this.cvt[i] = ((int)(paramDouble * this.unscaledCvt[i] + 0.5D));
  }

  public void putInPixels(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < this.cvt.length))
      this.cvt[paramInt1] = paramInt2;
  }

  public void putInFUnits(int paramInt1, int paramInt2)
  {
    paramInt2 = (int)(paramInt2 * this.scale + 0.5D);
    if ((paramInt1 >= 0) && (paramInt1 < this.cvt.length))
      this.cvt[paramInt1] = paramInt2;
  }

  public int get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < this.cvt.length))
      return this.cvt[paramInt];
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.hinting.Cvt
 * JD-Core Version:    0.6.2
 */
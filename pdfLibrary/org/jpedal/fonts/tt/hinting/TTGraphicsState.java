package org.jpedal.fonts.tt.hinting;

import java.io.Serializable;

public class TTGraphicsState
  implements Cloneable, Serializable
{
  public static final int x_axis = 1073741824;
  public static final int y_axis = 16384;
  public static final int hg = 104;
  public static final int g = 72;
  public static final int dg = 8;
  public static final int dtg = 68;
  public static final int utg = 64;
  public static final int off = -1;
  public boolean autoFlip = true;
  public int controlValueTableCutIn = 68;
  public int deltaBase = 9;
  public int deltaShift = 3;
  public int freedomVector = 1073741824;
  public int projectionVector = 1073741824;
  public int dualProjectionVector = 1073741824;
  public int instructControl = 0;
  public int loop = 1;
  public int minimumDistance = 1;
  public int roundState = 72;
  public double gridPeriod = 1.0D;
  public int rp0 = 0;
  public int rp1 = 0;
  public int rp2 = 0;
  public int singleWidthCutIn = 0;
  public int singleWidthValue = 0;
  public int zp0 = 1;
  public int zp1 = 1;
  public int zp2 = 1;

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }

  public double round(double paramDouble)
  {
    if (this.roundState == -1)
      return paramDouble;
    int i = paramDouble > 0.0D ? 1 : 0;
    int j = this.roundState >> 6 & 0x3;
    double d1;
    if (j == 0)
      d1 = this.gridPeriod / 2.0D;
    else if (j == 1)
      d1 = this.gridPeriod;
    else
      d1 = this.gridPeriod * 2.0D;
    j = this.roundState >> 4 & 0x3;
    double d2;
    if (j == 0)
      d2 = 0.0D;
    else if (j == 1)
      d2 = d1 / 4.0D;
    else if (j == 2)
      d2 = d1 / 2.0D;
    else
      d2 = 3.0D * d1 / 4.0D;
    j = this.roundState & 0xF;
    if (j == 0)
    {
      for (d3 = d2; d3 < paramDouble; d3 += d1);
      return d3;
    }
    double d3 = (j - 4) * d1 / 8.0D;
    paramDouble -= d2;
    double d4 = 0.0D;
    if (paramDouble > 0.0D)
    {
      paramDouble += d3;
      while (d4 + d1 <= paramDouble)
        d4 += d1;
    }
    paramDouble -= d3;
    while (d4 - d1 >= paramDouble)
      d4 -= d1;
    paramDouble = d4;
    paramDouble += d2;
    if ((i != 0) && (paramDouble < 0.0D))
      paramDouble = d2 % d1;
    if ((i == 0) && (paramDouble > 0.0D))
      paramDouble = (d2 - 10.0D * d1) % d1;
    return paramDouble;
  }

  public int round(int paramInt)
  {
    double d = TTVM.getDoubleFromF26Dot6(paramInt);
    d = round(d);
    return TTVM.storeDoubleAsF26Dot6(d);
  }

  public int[] getFVMoveforPVDistance(int paramInt)
  {
    if (paramInt == 0)
      return new int[] { 0, 0 };
    int[] arrayOfInt = getVectorComponents(this.freedomVector);
    arrayOfInt[0] = TTVM.storeDoubleAsF26Dot6(TTVM.getDoubleFromF2Dot14(arrayOfInt[0]));
    arrayOfInt[1] = TTVM.storeDoubleAsF26Dot6(TTVM.getDoubleFromF2Dot14(arrayOfInt[1]));
    double d1 = TTVM.getDoubleFromF26Dot6(getCoordsOnVector(this.projectionVector, arrayOfInt[0], arrayOfInt[1]));
    if (d1 != 0.0D)
    {
      double d2 = TTVM.getDoubleFromF26Dot6(paramInt);
      arrayOfInt[0] = ((int)(arrayOfInt[0] * d2 / d1));
      arrayOfInt[1] = ((int)(arrayOfInt[1] * d2 / d1));
    }
    else
    {
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
    }
    return arrayOfInt;
  }

  static int[] getVectorComponents(int paramInt)
  {
    return new int[] { paramInt >> 16, paramInt << 16 >> 16 };
  }

  static int createVector(int paramInt1, int paramInt2)
  {
    return ((paramInt1 & 0xFFFF) << 16) + (paramInt2 & 0xFFFF);
  }

  static int getCoordsOnVector(int paramInt1, int paramInt2, int paramInt3)
  {
    int[] arrayOfInt = getVectorComponents(paramInt1);
    long l1 = arrayOfInt[0] * paramInt2;
    long l2 = arrayOfInt[1] * paramInt3;
    long l3 = l1 + l2;
    int i = (l3 & 0x3FFF) >= 127L ? 1 : 0;
    l3 >>= 14;
    if (i != 0)
      l3 += 1L;
    return (int)l3;
  }

  public void resetForGlyph()
  {
    this.zp0 = 1;
    this.zp1 = 1;
    this.zp2 = 1;
    this.projectionVector = 1073741824;
    this.dualProjectionVector = 1073741824;
    this.freedomVector = 1073741824;
    this.roundState = 72;
    this.loop = 1;
    this.controlValueTableCutIn = 68;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.hinting.TTGraphicsState
 * JD-Core Version:    0.6.2
 */
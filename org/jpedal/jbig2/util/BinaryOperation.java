package org.jpedal.jbig2.util;

public class BinaryOperation
{
  public static int getInt32(short[] paramArrayOfShort)
  {
    return paramArrayOfShort[0] << 24 | paramArrayOfShort[1] << 16 | paramArrayOfShort[2] << 8 | paramArrayOfShort[3];
  }

  public static int getInt16(short[] paramArrayOfShort)
  {
    return paramArrayOfShort[0] << 8 | paramArrayOfShort[1];
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.util.BinaryOperation
 * JD-Core Version:    0.6.2
 */
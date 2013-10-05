package org.jpedal.jbig2.decoders;

public class DecodeIntResult
{
  private final int intResult;
  private final boolean booleanResult;

  public DecodeIntResult(int paramInt, boolean paramBoolean)
  {
    this.intResult = paramInt;
    this.booleanResult = paramBoolean;
  }

  public int intResult()
  {
    return this.intResult;
  }

  public boolean booleanResult()
  {
    return this.booleanResult;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.decoders.DecodeIntResult
 * JD-Core Version:    0.6.2
 */
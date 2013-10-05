package org.jpedal.jbig2.decoders;

public class ArithmeticDecoderStats
{
  private final int contextSize;
  private final int[] codingContextTable;

  public ArithmeticDecoderStats(int paramInt)
  {
    this.contextSize = paramInt;
    this.codingContextTable = new int[paramInt];
  }

  public void reset()
  {
    for (int i = 0; i < this.contextSize; i++)
      this.codingContextTable[i] = 0;
  }

  public int getContextCodingTableValue(int paramInt)
  {
    return this.codingContextTable[paramInt];
  }

  public void setContextCodingTableValue(int paramInt1, int paramInt2)
  {
    this.codingContextTable[paramInt1] = paramInt2;
  }

  public int getContextSize()
  {
    return this.contextSize;
  }

  public void overwrite(ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    System.arraycopy(paramArithmeticDecoderStats.codingContextTable, 0, this.codingContextTable, 0, this.contextSize);
  }

  public ArithmeticDecoderStats copy()
  {
    ArithmeticDecoderStats localArithmeticDecoderStats = new ArithmeticDecoderStats(this.contextSize);
    System.arraycopy(this.codingContextTable, 0, localArithmeticDecoderStats.codingContextTable, 0, this.contextSize);
    return localArithmeticDecoderStats;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.decoders.ArithmeticDecoderStats
 * JD-Core Version:    0.6.2
 */
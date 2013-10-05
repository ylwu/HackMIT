package org.jpedal.objects.raw;

import org.jpedal.utils.NumberUtils;

public class PdfKeyPairsIterator
{
  private byte[][] keys;
  private byte[][] values;
  private PdfObject[] objs;
  int maxCount;
  int current = 0;

  public PdfKeyPairsIterator(byte[][] paramArrayOfByte1, byte[][] paramArrayOfByte2, PdfObject[] paramArrayOfPdfObject)
  {
    this.keys = paramArrayOfByte1;
    this.values = paramArrayOfByte2;
    this.objs = paramArrayOfPdfObject;
    if (paramArrayOfByte1 != null)
      this.maxCount = paramArrayOfByte1.length;
    this.current = 0;
  }

  public int getTokenCount()
  {
    return this.maxCount;
  }

  public void nextPair()
  {
    if (this.current < this.maxCount)
      this.current += 1;
    else
      throw new RuntimeException("No keys left in PdfKeyPairsIterator");
  }

  public String getNextKeyAsString()
  {
    return new String(this.keys[this.current]);
  }

  public int getNextKeyAsNumber()
  {
    int i = this.keys[this.current].length;
    boolean bool = isNextKeyANumber();
    if (!bool)
    {
      if (this.keys[this.current].length != 1)
        throw new RuntimeException("Unexpected value in getNextKeyAsNumber >" + new String(this.keys[this.current]) + '<');
      return this.keys[this.current][0] & 0xFF;
    }
    return NumberUtils.parseInt(0, i, this.keys[this.current]);
  }

  public boolean isNextKeyANumber()
  {
    int i = this.keys[this.current].length;
    boolean bool = true;
    for (int j = 0; j < i; j++)
    {
      int k = this.keys[this.current][j];
      if ((k < 48) || (k > 57))
      {
        bool = false;
        j = i;
      }
    }
    return bool;
  }

  public boolean hasMorePairs()
  {
    return this.current < this.maxCount;
  }

  public String getNextValueAsString()
  {
    if (this.values[this.current] == null)
      return null;
    return new String(this.values[this.current]);
  }

  public PdfObject getNextValueAsDictionary()
  {
    return this.objs[this.current];
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.PdfKeyPairsIterator
 * JD-Core Version:    0.6.2
 */
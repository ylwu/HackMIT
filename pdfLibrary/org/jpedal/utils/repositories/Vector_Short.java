package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_Short
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private short[] items = new short[this.max_size];

  protected static int incrementSize(int paramInt)
  {
    if (paramInt < 8000)
      paramInt *= 4;
    else if (paramInt < 16000)
      paramInt *= 2;
    else
      paramInt += 2000;
    return paramInt;
  }

  public Vector_Short(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new short[this.max_size];
  }

  public final short[] get()
  {
    return this.items;
  }

  public final void addElement(short paramShort)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramShort;
    this.current_item += 1;
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      short[] arrayOfShort = this.items;
      this.items = new short[this.max_size];
      System.arraycopy(arrayOfShort, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Short
 * JD-Core Version:    0.6.2
 */
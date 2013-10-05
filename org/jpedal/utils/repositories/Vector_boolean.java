package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_boolean
  implements Serializable
{
  int increment_size = 1000;
  int max_size = 250;
  private boolean[] items = new boolean[this.max_size];

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

  public Vector_boolean(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new boolean[this.max_size];
  }

  public final boolean[] get()
  {
    return this.items;
  }

  public final void setElementAt(boolean paramBoolean, int paramInt)
  {
    if (paramInt >= this.max_size)
      checkSize(paramInt);
    this.items[paramInt] = paramBoolean;
  }

  public final boolean elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return false;
    return this.items[paramInt];
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      boolean[] arrayOfBoolean = this.items;
      this.items = new boolean[this.max_size];
      System.arraycopy(arrayOfBoolean, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_boolean
 * JD-Core Version:    0.6.2
 */
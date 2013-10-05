package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_Int
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  protected int[] items = new int[this.max_size];
  protected int defaultValue = 0;
  private int checkPoint = -1;

  public Vector_Int()
  {
  }

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

  public Vector_Int(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new int[this.max_size];
  }

  public final synchronized int elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return 0;
    return this.items[paramInt];
  }

  public final int[] get()
  {
    return this.items;
  }

  public final void setElementAt(int paramInt1, int paramInt2)
  {
    if (paramInt2 >= this.max_size)
      checkSize(paramInt2);
    this.items[paramInt2] = paramInt1;
  }

  public final void set(int[] paramArrayOfInt)
  {
    this.items = paramArrayOfInt;
  }

  public final void keep_larger(int paramInt1, int paramInt2)
  {
    if (this.items[paramInt1] < this.items[paramInt2])
      this.items[paramInt1] = this.items[paramInt2];
  }

  public final void keep_smaller(int paramInt1, int paramInt2)
  {
    if (this.items[paramInt1] > this.items[paramInt2])
      this.items[paramInt1] = this.items[paramInt2];
  }

  public final void clear()
  {
    this.checkPoint = -1;
    this.items = null;
    this.items = new int[this.max_size];
    int i;
    if (this.defaultValue != 0)
      for (i = 0; i < this.max_size; i++)
        this.items[i] = this.defaultValue;
    else if (this.current_item > 0)
      for (i = 0; i < this.current_item; i++)
        this.items[i] = 0;
    else
      for (i = 0; i < this.max_size; i++)
        this.items[i] = 0;
    this.current_item = 0;
  }

  public final synchronized int size()
  {
    return this.current_item + 1;
  }

  public final synchronized int getCapacity()
  {
    return this.items.length;
  }

  public final void removeElementAt(int paramInt)
  {
    if (paramInt >= 0)
    {
      System.arraycopy(this.items, paramInt + 1, this.items, paramInt, this.current_item - 1 - paramInt);
      this.items[(this.current_item - 1)] = 0;
    }
    else
    {
      this.items[0] = 0;
    }
    this.current_item -= 1;
  }

  public final synchronized void deleteElementWithValue(int paramInt)
  {
    int i = this.items.length;
    int[] arrayOfInt1 = new int[i - 1];
    int j = 0;
    for (int n : this.items)
      if (n != paramInt)
      {
        arrayOfInt1[j] = n;
        j++;
      }
    this.items = arrayOfInt1;
    this.current_item -= 1;
  }

  public String toString()
  {
    String str = "{";
    for (int k : this.items)
      str = str + ' ' + k;
    return str + "} " + this.current_item;
  }

  public final boolean contains(int paramInt)
  {
    boolean bool = false;
    for (int i = 0; i < this.current_item; i++)
      if (this.items[i] == paramInt)
      {
        i = this.current_item + 1;
        bool = true;
      }
    return bool;
  }

  public final int pull()
  {
    if (this.current_item > 0)
      this.current_item -= 1;
    return this.items[this.current_item];
  }

  public final void push(int paramInt)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramInt;
    this.current_item += 1;
    checkSize(this.current_item);
  }

  public final void addElement(int paramInt)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramInt;
    this.current_item += 1;
    checkSize(this.current_item);
  }

  public final void add_together(int paramInt1, int paramInt2)
  {
    this.items[paramInt1] += this.items[paramInt2];
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      int[] arrayOfInt = this.items;
      this.items = new int[this.max_size];
      if (this.defaultValue != 0)
        for (int j = i; j < this.max_size; j++)
          this.items[j] = this.defaultValue;
      System.arraycopy(arrayOfInt, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public final void reuse()
  {
    this.current_item = 0;
  }

  public void trim()
  {
    int[] arrayOfInt = new int[this.current_item];
    System.arraycopy(this.items, 0, arrayOfInt, 0, this.current_item);
    this.items = arrayOfInt;
    this.max_size = this.current_item;
  }

  public void resetToCheckpoint()
  {
    if (this.checkPoint != -1)
      this.current_item = this.checkPoint;
    this.checkPoint = -1;
  }

  public void setCheckpoint()
  {
    if (this.checkPoint == -1)
      this.checkPoint = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Int
 * JD-Core Version:    0.6.2
 */
package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_Double
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private double[] items = new double[this.max_size];
  private int checkPoint = -1;

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

  public Vector_Double(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new double[this.max_size];
  }

  public final double[] get()
  {
    return this.items;
  }

  public final void addElement(double paramDouble)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramDouble;
    this.current_item += 1;
  }

  public final double elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return 0.0D;
    return this.items[paramInt];
  }

  public final void clear()
  {
    this.checkPoint = -1;
    int i;
    if (this.current_item > 0)
      for (i = 0; i < this.current_item; i++)
        this.items[i] = 0.0D;
    else
      for (i = 0; i < this.max_size; i++)
        this.items[i] = 0.0D;
    this.current_item = 0;
  }

  public final double pull()
  {
    if (this.current_item > 0)
      this.current_item -= 1;
    return this.items[this.current_item];
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      double[] arrayOfDouble = this.items;
      this.items = new double[this.max_size];
      System.arraycopy(arrayOfDouble, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public void trim()
  {
    double[] arrayOfDouble = new double[this.current_item];
    System.arraycopy(this.items, 0, arrayOfDouble, 0, this.current_item);
    this.items = arrayOfDouble;
    this.max_size = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Double
 * JD-Core Version:    0.6.2
 */
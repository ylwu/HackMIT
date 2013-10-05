package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_Float
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private float[] items = new float[this.max_size];
  private int checkPoint = -1;

  public Vector_Float()
  {
  }

  public Vector_Float(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new float[this.max_size];
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

  public final float[] get()
  {
    return this.items;
  }

  public final void addElement(float paramFloat)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramFloat;
    this.current_item += 1;
  }

  public final float elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return 0.0F;
    return this.items[paramInt];
  }

  public final void clear()
  {
    int i;
    if (this.current_item > 0)
      for (i = 0; i < this.current_item; i++)
        this.items[i] = 0.0F;
    else
      for (i = 0; i < this.max_size; i++)
        this.items[i] = 0.0F;
    this.current_item = 0;
  }

  public final void reuse()
  {
    this.current_item = 0;
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      float[] arrayOfFloat = this.items;
      this.items = new float[this.max_size];
      System.arraycopy(arrayOfFloat, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public void trim()
  {
    float[] arrayOfFloat = new float[this.current_item];
    System.arraycopy(this.items, 0, arrayOfFloat, 0, this.current_item);
    this.items = arrayOfFloat;
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
    if ((this.checkPoint == -1) && (this.current_item >= 0))
      this.checkPoint = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Float
 * JD-Core Version:    0.6.2
 */
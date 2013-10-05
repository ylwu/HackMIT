package org.jpedal.utils.repositories;

import java.awt.geom.GeneralPath;
import java.io.Serializable;

public class Vector_Path
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private GeneralPath[] items = new GeneralPath[this.max_size];

  public Vector_Path()
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

  public Vector_Path(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new GeneralPath[this.max_size];
  }

  public final GeneralPath[] get()
  {
    return this.items;
  }

  public final void clear()
  {
    int i;
    if (this.current_item > 0)
      for (i = 0; i < this.current_item; i++)
        this.items[i] = null;
    else
      for (i = 0; i < this.max_size; i++)
        this.items[i] = null;
    this.current_item = 0;
  }

  public final void set(GeneralPath[] paramArrayOfGeneralPath)
  {
    this.items = paramArrayOfGeneralPath;
  }

  public final GeneralPath elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return null;
    return this.items[paramInt];
  }

  public final void addElement(GeneralPath paramGeneralPath)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramGeneralPath;
    this.current_item += 1;
  }

  public final int size()
  {
    return this.current_item + 1;
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      GeneralPath[] arrayOfGeneralPath = this.items;
      this.items = new GeneralPath[this.max_size];
      System.arraycopy(arrayOfGeneralPath, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public void setCurrent_item(int paramInt)
  {
    this.current_item = paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Path
 * JD-Core Version:    0.6.2
 */
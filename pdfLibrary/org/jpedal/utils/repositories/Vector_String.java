package org.jpedal.utils.repositories;

import java.io.Serializable;

public class Vector_String
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private String[] items = new String[this.max_size];

  public Vector_String(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new String[this.max_size];
  }

  public Vector_String()
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

  public final String[] get()
  {
    return this.items;
  }

  public final String elementAt(int paramInt)
  {
    String str = null;
    if (paramInt < this.max_size)
      str = this.items[paramInt];
    if (str == null)
      str = "";
    return str;
  }

  public final boolean contains(String paramString)
  {
    boolean bool = false;
    for (int i = 0; i < this.current_item; i++)
      if (this.items[i].equals(paramString))
      {
        i = this.current_item + 1;
        bool = true;
      }
    return bool;
  }

  public final void merge(int paramInt1, int paramInt2, String paramString)
  {
    this.items[paramInt1] = (this.items[paramInt1] + paramString + this.items[paramInt2]);
    this.items[paramInt2] = null;
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

  public final void removeElementAt(int paramInt)
  {
    if (paramInt >= 0)
    {
      System.arraycopy(this.items, paramInt + 1, this.items, paramInt, this.current_item - 1 - paramInt);
      this.items[(this.current_item - 1)] = "";
    }
    else
    {
      this.items[0] = "";
    }
    this.current_item -= 1;
  }

  public final void set(String[] paramArrayOfString)
  {
    this.items = paramArrayOfString;
  }

  public final void addElement(String paramString)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramString;
    this.current_item += 1;
  }

  public final int size()
  {
    return this.current_item + 1;
  }

  public final void setElementAt(String paramString, int paramInt)
  {
    if (paramInt >= this.max_size)
      checkSize(paramInt);
    this.items[paramInt] = paramString;
  }

  private final void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      String[] arrayOfString = this.items;
      this.items = new String[this.max_size];
      System.arraycopy(arrayOfString, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public void trim()
  {
    String[] arrayOfString = new String[this.current_item];
    System.arraycopy(this.items, 0, arrayOfString, 0, this.current_item);
    this.items = arrayOfString;
    this.max_size = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_String
 * JD-Core Version:    0.6.2
 */
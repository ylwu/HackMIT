package org.jpedal.utils.repositories;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jpedal.io.PathSerializer;

public class Vector_Shape
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private Area[] items = new Area[this.max_size];
  private int checkPoint = -1;

  public Vector_Shape()
  {
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

  public Vector_Shape(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new Area[this.max_size];
  }

  public final Area[] get()
  {
    return this.items;
  }

  public final void clear()
  {
    this.checkPoint = -1;
    int i;
    if (this.current_item > 0)
      for (i = 0; i < this.current_item; i++)
        this.items[i] = null;
    else
      for (i = 0; i < this.max_size; i++)
        this.items[i] = null;
    this.current_item = 0;
  }

  public final void addElement(Area paramArea)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramArea;
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
      Area[] arrayOfArea = this.items;
      this.items = new Area[this.max_size];
      System.arraycopy(arrayOfArea, 0, this.items, 0, i);
      this.increment_size = incrementSize(this.increment_size);
    }
  }

  public void writeToStream(ByteArrayOutputStream paramByteArrayOutputStream)
    throws IOException
  {
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(paramByteArrayOutputStream);
    localObjectOutputStream.writeObject(Integer.valueOf(this.max_size));
    for (int i = 0; i < this.max_size; i++)
    {
      Area localArea = this.items[i];
      if (localArea == null)
      {
        localObjectOutputStream.writeObject(null);
      }
      else
      {
        PathIterator localPathIterator = localArea.getPathIterator(new AffineTransform());
        PathSerializer.serializePath(localObjectOutputStream, localPathIterator);
      }
    }
  }

  public void restoreFromStream(ByteArrayInputStream paramByteArrayInputStream)
    throws IOException, ClassNotFoundException
  {
    ObjectInputStream localObjectInputStream = new ObjectInputStream(paramByteArrayInputStream);
    int i = ((Integer)localObjectInputStream.readObject()).intValue();
    this.max_size = i;
    this.items = new Area[i];
    for (int j = 0; j < i; j++)
    {
      GeneralPath localGeneralPath = PathSerializer.deserializePath(localObjectInputStream);
      if (localGeneralPath == null)
        this.items[j] = null;
      else
        this.items[j] = new Area(localGeneralPath);
    }
  }

  public void trim()
  {
    Area[] arrayOfArea = new Area[this.current_item];
    System.arraycopy(this.items, 0, arrayOfArea, 0, this.current_item);
    this.items = arrayOfArea;
    this.max_size = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Shape
 * JD-Core Version:    0.6.2
 */
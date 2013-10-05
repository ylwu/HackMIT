package org.jpedal.utils.repositories;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;
import org.jpedal.color.PdfTexturePaint;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.fonts.glyph.T1Glyph;
import org.jpedal.fonts.glyph.T3Glyph;
import org.jpedal.fonts.tt.TTGlyph;
import org.jpedal.io.PathSerializer;
import org.jpedal.utils.LogWriter;

public class Vector_Object
  implements Serializable
{
  int increment_size = 1000;
  protected int current_item = 0;
  int max_size = 250;
  private static final Integer GENERIC = Integer.valueOf(1);
  private static final Integer BASICSTROKE = Integer.valueOf(2);
  private static final Integer BUFFERED_IMAGE = Integer.valueOf(3);
  private static final Integer GENERAL_PATH = Integer.valueOf(4);
  private static final Integer T1GLYPH = Integer.valueOf(5);
  private static final Integer TTGLYPH = Integer.valueOf(6);
  private static final Integer AREA = Integer.valueOf(7);
  private static final Integer RECT = Integer.valueOf(8);
  private static final Integer T3GLYPH = Integer.valueOf(9);
  private static final Integer TEXTUREDPAINT = Integer.valueOf(10);
  private Object[] items = new Object[this.max_size];
  private int checkPoint = -1;

  public Vector_Object()
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

  public Vector_Object(int paramInt)
  {
    this.max_size = paramInt;
    this.items = new Object[this.max_size];
  }

  public final Object[] get()
  {
    return this.items;
  }

  public final Object pull()
  {
    if (this.current_item > 0)
      this.current_item -= 1;
    return this.items[this.current_item];
  }

  public final void push(Object paramObject)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramObject;
    this.current_item += 1;
  }

  public final boolean contains(Object paramObject)
  {
    boolean bool = false;
    for (int i = 0; i < this.current_item; i++)
      if (this.items[i].equals(paramObject))
      {
        i = this.current_item + 1;
        bool = true;
      }
    return bool;
  }

  public final void addElement(Object paramObject)
  {
    checkSize(this.current_item);
    this.items[this.current_item] = paramObject;
    this.current_item += 1;
  }

  public final void setElementAt(Object paramObject, int paramInt)
  {
    if (paramInt >= this.max_size)
      checkSize(paramInt);
    this.items[paramInt] = paramObject;
  }

  public final Object elementAt(int paramInt)
  {
    if (paramInt >= this.max_size)
      return null;
    return this.items[paramInt];
  }

  public final void set(Object[] paramArrayOfObject)
  {
    this.items = paramArrayOfObject;
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

  public final int size()
  {
    return this.current_item + 1;
  }

  public final void removeElementAt(int paramInt)
  {
    if (paramInt >= 0)
    {
      System.arraycopy(this.items, paramInt + 1, this.items, paramInt, this.current_item - 1 - paramInt);
      this.items[(this.current_item - 1)] = null;
    }
    else
    {
      this.items[0] = null;
    }
    this.current_item -= 1;
  }

  private void checkSize(int paramInt)
  {
    if (paramInt >= this.max_size)
    {
      int i = this.max_size;
      this.max_size += this.increment_size;
      if (this.max_size <= paramInt)
        this.max_size = (paramInt + this.increment_size + 2);
      Object[] arrayOfObject = this.items;
      this.items = new Object[this.max_size];
      System.arraycopy(arrayOfObject, 0, this.items, 0, i);
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
      Object localObject1 = this.items[i];
      Object localObject2;
      if ((localObject1 instanceof BasicStroke))
      {
        localObjectOutputStream.writeObject(BASICSTROKE);
        localObject2 = (BasicStroke)this.items[i];
        localObjectOutputStream.writeFloat(((BasicStroke)localObject2).getLineWidth());
        localObjectOutputStream.writeInt(((BasicStroke)localObject2).getEndCap());
        localObjectOutputStream.writeInt(((BasicStroke)localObject2).getLineJoin());
        localObjectOutputStream.writeFloat(((BasicStroke)localObject2).getMiterLimit());
        localObjectOutputStream.writeObject(((BasicStroke)localObject2).getDashArray());
        localObjectOutputStream.writeFloat(((BasicStroke)localObject2).getDashPhase());
      }
      else if ((localObject1 instanceof Rectangle2D))
      {
        localObjectOutputStream.writeObject(RECT);
        localObject2 = (Rectangle2D)this.items[i];
        localObjectOutputStream.writeDouble(((Rectangle2D)localObject2).getBounds2D().getX());
        localObjectOutputStream.writeDouble(((Rectangle2D)localObject2).getBounds2D().getY());
        localObjectOutputStream.writeDouble(((Rectangle2D)localObject2).getBounds2D().getWidth());
        localObjectOutputStream.writeDouble(((Rectangle2D)localObject2).getBounds2D().getHeight());
      }
      else if ((localObject1 instanceof BufferedImage))
      {
        localObjectOutputStream.writeObject(BUFFERED_IMAGE);
        localObject2 = new ByteArrayOutputStream();
        ImageIO.write((BufferedImage)localObject1, "png", (OutputStream)localObject2);
        localObjectOutputStream.writeObject(((ByteArrayOutputStream)localObject2).toByteArray());
      }
      else if ((localObject1 instanceof GeneralPath))
      {
        localObjectOutputStream.writeObject(GENERAL_PATH);
        PathSerializer.serializePath(localObjectOutputStream, ((GeneralPath)this.items[i]).getPathIterator(new AffineTransform()));
      }
      else if ((localObject1 instanceof T1Glyph))
      {
        localObjectOutputStream.writeObject(T1GLYPH);
        ((T1Glyph)localObject1).flushArea();
        localObjectOutputStream.writeObject(localObject1);
        ((T1Glyph)localObject1).writePathsToStream(localObjectOutputStream);
      }
      else if ((localObject1 instanceof TTGlyph))
      {
        localObjectOutputStream.writeObject(TTGLYPH);
        ((TTGlyph)localObject1).flushArea();
        localObjectOutputStream.writeObject(localObject1);
        ((TTGlyph)localObject1).writePathsToStream(localObjectOutputStream);
      }
      else if ((localObject1 instanceof T3Glyph))
      {
        localObjectOutputStream.writeObject(T3GLYPH);
        ((T3Glyph)localObject1).writePathsToStream(localObjectOutputStream);
      }
      else
      {
        Object localObject3;
        if ((localObject1 instanceof PdfTexturePaint))
        {
          localObjectOutputStream.writeObject(TEXTUREDPAINT);
          localObject2 = new ByteArrayOutputStream();
          ImageIO.write(((PdfTexturePaint)localObject1).getImage(), "png", (OutputStream)localObject2);
          localObjectOutputStream.writeObject(((ByteArrayOutputStream)localObject2).toByteArray());
          localObject3 = ((PdfTexturePaint)localObject1).getAnchorRect();
          localObjectOutputStream.writeDouble(((Rectangle2D)localObject3).getBounds2D().getX());
          localObjectOutputStream.writeDouble(((Rectangle2D)localObject3).getBounds2D().getY());
          localObjectOutputStream.writeDouble(((Rectangle2D)localObject3).getBounds2D().getWidth());
          localObjectOutputStream.writeDouble(((Rectangle2D)localObject3).getBounds2D().getHeight());
        }
        else if ((localObject1 instanceof Area))
        {
          localObjectOutputStream.writeObject(AREA);
          localObject2 = (Area)this.items[i];
          localObject3 = ((Area)localObject2).getPathIterator(new AffineTransform());
          PathSerializer.serializePath(localObjectOutputStream, (PathIterator)localObject3);
        }
        else
        {
          try
          {
            localObjectOutputStream.writeObject(GENERIC);
            localObjectOutputStream.writeObject(localObject1);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
        }
      }
    }
    localObjectOutputStream.close();
  }

  public void restoreFromStream(ByteArrayInputStream paramByteArrayInputStream)
    throws IOException, ClassNotFoundException
  {
    ObjectInputStream localObjectInputStream = new ObjectInputStream(paramByteArrayInputStream);
    int i = ((Integer)localObjectInputStream.readObject()).intValue();
    this.max_size = i;
    this.items = new Object[i];
    for (int j = 0; j < i; j++)
    {
      Integer localInteger = (Integer)localObjectInputStream.readObject();
      float f1;
      float f5;
      float f8;
      Object localObject1;
      if (localInteger.compareTo(BASICSTROKE) == 0)
      {
        f1 = localObjectInputStream.readFloat();
        int k = localObjectInputStream.readInt();
        int n = localObjectInputStream.readInt();
        f5 = localObjectInputStream.readFloat();
        float[] arrayOfFloat = (float[])localObjectInputStream.readObject();
        f8 = localObjectInputStream.readFloat();
        localObject1 = new BasicStroke(f1, k, n, f5, arrayOfFloat, f8);
      }
      else if (localInteger.compareTo(RECT) == 0)
      {
        f1 = localObjectInputStream.readFloat();
        float f2 = localObjectInputStream.readFloat();
        float f3 = localObjectInputStream.readFloat();
        f5 = localObjectInputStream.readFloat();
        localObject1 = new Rectangle2D.Float(f1, f2, f3, f5);
      }
      else
      {
        Object localObject2;
        if (localInteger.compareTo(BUFFERED_IMAGE) == 0)
        {
          localObject2 = (byte[])localObjectInputStream.readObject();
          localObject1 = DefaultImageHelper.read((byte[])localObject2);
        }
        else if (localInteger.compareTo(GENERAL_PATH) == 0)
        {
          localObject1 = PathSerializer.deserializePath(localObjectInputStream);
        }
        else
        {
          int m;
          GeneralPath[] arrayOfGeneralPath;
          if (localInteger.compareTo(T1GLYPH) == 0)
          {
            localObject2 = (T1Glyph)localObjectInputStream.readObject();
            m = ((Integer)localObjectInputStream.readObject()).intValue();
            arrayOfGeneralPath = new GeneralPath[m];
            for (int i1 = 0; i1 < m; i1++)
              arrayOfGeneralPath[i1] = PathSerializer.deserializePath(localObjectInputStream);
            Vector_Path localVector_Path1 = new Vector_Path();
            localVector_Path1.set(arrayOfGeneralPath);
            localVector_Path1.setCurrent_item(arrayOfGeneralPath.length);
            ((T1Glyph)localObject2).setPaths(localVector_Path1);
            localObject1 = localObject2;
          }
          else if (localInteger.compareTo(TTGLYPH) == 0)
          {
            localObject2 = (TTGlyph)localObjectInputStream.readObject();
            m = ((Integer)localObjectInputStream.readObject()).intValue();
            arrayOfGeneralPath = new GeneralPath[m];
            for (int i2 = 0; i2 < m; i2++)
              arrayOfGeneralPath[i2] = PathSerializer.deserializePath(localObjectInputStream);
            Vector_Path localVector_Path2 = new Vector_Path();
            localVector_Path2.set(arrayOfGeneralPath);
            localVector_Path2.setCurrent_item(arrayOfGeneralPath.length);
            ((TTGlyph)localObject2).setPaths(localVector_Path2);
            localObject1 = localObject2;
          }
          else if (localInteger.compareTo(T3GLYPH) == 0)
          {
            localObject1 = new T3Glyph(localObjectInputStream);
          }
          else if (localInteger.compareTo(TEXTUREDPAINT) == 0)
          {
            localObject2 = (byte[])localObjectInputStream.readObject();
            BufferedImage localBufferedImage = DefaultImageHelper.read((byte[])localObject2);
            float f4 = localObjectInputStream.readFloat();
            float f6 = localObjectInputStream.readFloat();
            float f7 = localObjectInputStream.readFloat();
            f8 = localObjectInputStream.readFloat();
            Rectangle2D.Float localFloat = new Rectangle2D.Float(f4, f6, f7, f8);
            localObject1 = new PdfTexturePaint(localBufferedImage, localFloat);
          }
          else if (localInteger.compareTo(AREA) == 0)
          {
            localObject2 = PathSerializer.deserializePath(localObjectInputStream);
            localObject1 = new Area((Shape)localObject2);
          }
          else
          {
            localObject1 = localObjectInputStream.readObject();
          }
        }
      }
      this.items[j] = localObject1;
    }
  }

  public void trim()
  {
    Object[] arrayOfObject = new Object[this.current_item];
    System.arraycopy(this.items, 0, arrayOfObject, 0, this.current_item);
    this.items = arrayOfObject;
    this.max_size = this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.repositories.Vector_Object
 * JD-Core Version:    0.6.2
 */
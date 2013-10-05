package org.jpedal.render;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.jpedal.color.ColorSpaces;
import org.jpedal.io.JAIHelper;
import org.jpedal.utils.LogWriter;

public class RenderUtils
{
  public static boolean isInverted(float[][] paramArrayOfFloat)
  {
    return (paramArrayOfFloat[0][0] < 0.0F) || (paramArrayOfFloat[1][1] < 0.0F);
  }

  public static boolean isRotated(float[][] paramArrayOfFloat)
  {
    return ((paramArrayOfFloat[0][0] == 0.0F) && (paramArrayOfFloat[1][1] == 0.0F)) || ((paramArrayOfFloat[0][1] > 0.0F) && (paramArrayOfFloat[1][0] < 0.0F)) || ((paramArrayOfFloat[0][1] < 0.0F) && (paramArrayOfFloat[1][0] > 0.0F));
  }

  static BufferedImage invertImage(BufferedImage paramBufferedImage)
  {
    AffineTransform localAffineTransform = new AffineTransform();
    localAffineTransform.scale(1.0D, -1.0D);
    localAffineTransform.translate(0.0D, -paramBufferedImage.getHeight());
    AffineTransformOp localAffineTransformOp = new AffineTransformOp(localAffineTransform, ColorSpaces.hints);
    int i = 0;
    if (JAIHelper.isJAIused())
    {
      i = 1;
      try
      {
        paramBufferedImage = JAIHelper.affine(paramBufferedImage, localAffineTransform);
      }
      catch (Exception localException1)
      {
        i = 0;
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException1.getMessage());
      }
      catch (Error localError)
      {
        i = 0;
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError.getMessage());
      }
      if ((i == 0) && (LogWriter.isOutput()))
        LogWriter.writeLog("Unable to use JAI for image inversion");
    }
    if (i == 0)
      if (paramBufferedImage.getType() == 12)
      {
        BufferedImage localBufferedImage = paramBufferedImage;
        paramBufferedImage = new BufferedImage(localBufferedImage.getWidth(), localBufferedImage.getHeight(), localBufferedImage.getType());
        localAffineTransformOp.filter(localBufferedImage, paramBufferedImage);
      }
      else
      {
        int j = 0;
        try
        {
          paramBufferedImage = localAffineTransformOp.filter(paramBufferedImage, null);
        }
        catch (Exception localException2)
        {
          j = 1;
        }
        if (j != 0)
          try
          {
            localAffineTransformOp = new AffineTransformOp(localAffineTransform, null);
            paramBufferedImage = localAffineTransformOp.filter(paramBufferedImage, null);
          }
          catch (Exception localException3)
          {
          }
      }
    return paramBufferedImage;
  }

  static BufferedImage invertImageBeforeSave(BufferedImage paramBufferedImage, boolean paramBoolean)
  {
    AffineTransform localAffineTransform = new AffineTransform();
    if (paramBoolean)
    {
      localAffineTransform.scale(-1.0D, 1.0D);
      localAffineTransform.translate(-paramBufferedImage.getWidth(), 0.0D);
    }
    else
    {
      localAffineTransform.scale(1.0D, -1.0D);
      localAffineTransform.translate(0.0D, -paramBufferedImage.getHeight());
    }
    AffineTransformOp localAffineTransformOp = new AffineTransformOp(localAffineTransform, ColorSpaces.hints);
    int i = 0;
    if (JAIHelper.isJAIused())
    {
      i = 1;
      try
      {
        paramBufferedImage = JAIHelper.affine(paramBufferedImage, localAffineTransform);
      }
      catch (Exception localException)
      {
        i = 0;
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
      catch (Error localError)
      {
        i = 0;
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError.getMessage());
      }
      if ((i == 0) && (LogWriter.isOutput()))
        LogWriter.writeLog("Unable to use JAI for image inversion");
    }
    if (i == 0)
      if (paramBufferedImage.getType() == 12)
      {
        BufferedImage localBufferedImage = paramBufferedImage;
        paramBufferedImage = new BufferedImage(localBufferedImage.getWidth(), localBufferedImage.getHeight(), localBufferedImage.getType());
        localAffineTransformOp.filter(localBufferedImage, paramBufferedImage);
      }
      else
      {
        paramBufferedImage = localAffineTransformOp.filter(paramBufferedImage, null);
      }
    return paramBufferedImage;
  }

  static float[] checkSize(float[] paramArrayOfFloat, int paramInt)
  {
    int i = paramArrayOfFloat.length;
    if (i <= paramInt)
    {
      int j = i * 2;
      float[] arrayOfFloat = new float[j];
      System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, i);
      paramArrayOfFloat = arrayOfFloat;
    }
    return paramArrayOfFloat;
  }

  public static void renderClip(Area paramArea, Rectangle paramRectangle, Shape paramShape, Graphics2D paramGraphics2D)
  {
    if (paramArea != null)
    {
      paramGraphics2D.setClip(paramArea);
      if (paramRectangle != null)
        paramGraphics2D.clip(paramRectangle);
    }
    else
    {
      paramGraphics2D.setClip(paramShape);
    }
  }

  static Rectangle getAreaForGlyph(float[][] paramArrayOfFloat)
  {
    int i = (int)Math.sqrt(paramArrayOfFloat[0][0] * paramArrayOfFloat[0][0] + paramArrayOfFloat[1][0] * paramArrayOfFloat[1][0]);
    int j = (int)Math.sqrt(paramArrayOfFloat[1][1] * paramArrayOfFloat[1][1] + paramArrayOfFloat[0][1] * paramArrayOfFloat[0][1]);
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (paramArrayOfFloat[0][0] < 0.0F)
      f1 = paramArrayOfFloat[0][0];
    else if (paramArrayOfFloat[1][0] < 0.0F)
      f1 = paramArrayOfFloat[1][0];
    if (paramArrayOfFloat[1][1] < 0.0F)
      f2 = paramArrayOfFloat[1][1];
    else if (paramArrayOfFloat[0][1] < 0.0F)
      f2 = paramArrayOfFloat[0][1];
    return new Rectangle((int)(paramArrayOfFloat[2][0] + f1), (int)(paramArrayOfFloat[2][1] + f2), i, j);
  }

  public static boolean rectangleContains(Rectangle paramRectangle, int paramInt1, int paramInt2)
  {
    int i = paramRectangle.x;
    int j = paramRectangle.x + paramRectangle.width;
    int k = paramRectangle.y;
    int m = paramRectangle.y + paramRectangle.height;
    boolean bool = false;
    int n;
    if (i > j)
    {
      n = i;
      i = j;
      j = n;
    }
    if (k > m)
    {
      n = k;
      k = m;
      m = n;
    }
    if ((k < paramInt2) && (paramInt2 < m) && (i < paramInt1) && (paramInt1 < j))
      bool = true;
    return bool;
  }

  public static Object restoreFromStream(ByteArrayInputStream paramByteArrayInputStream)
    throws IOException, ClassNotFoundException
  {
    ObjectInputStream localObjectInputStream = new ObjectInputStream(paramByteArrayInputStream);
    return localObjectInputStream.readObject();
  }

  public static void writeToStream(ByteArrayOutputStream paramByteArrayOutputStream, Object paramObject)
    throws IOException
  {
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(paramByteArrayOutputStream);
    localObjectOutputStream.writeObject(paramObject);
    localObjectOutputStream.close();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.RenderUtils
 * JD-Core Version:    0.6.2
 */
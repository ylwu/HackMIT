package org.jpedal.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jpedal.color.ColorSpaces;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.GraphicsState;
import org.jpedal.utils.LogWriter;

public class ImageTransformer
{
  private BufferedImage current_image;
  private float[][] Trm;
  private float[][] CTM;
  private int i_x = 0;
  private int i_y = 0;
  private int i_w = 0;
  private int i_h = 0;

  public ImageTransformer(GraphicsState paramGraphicsState, BufferedImage paramBufferedImage, boolean paramBoolean)
  {
    this.current_image = paramBufferedImage;
    int i = this.current_image.getWidth();
    int j = this.current_image.getHeight();
    this.CTM = paramGraphicsState.CTM;
    this.Trm = new float[3][3];
    this.Trm[0][0] = (this.CTM[0][0] / i);
    this.Trm[0][1] = (-(this.CTM[0][1] / i));
    this.Trm[0][2] = 0.0F;
    this.Trm[1][0] = (-(this.CTM[1][0] / j));
    this.Trm[1][1] = (this.CTM[1][1] / j);
    this.Trm[1][2] = 0.0F;
    this.Trm[2][0] = this.CTM[2][0];
    this.Trm[2][1] = this.CTM[2][1];
    this.Trm[2][2] = 1.0F;
    for (int k = 0; k < 3; k++)
      for (int m = 0; m < 3; m++)
        if (((this.Trm[m][k] > 0.99D ? 1 : 0) & (this.Trm[m][k] < 1.0F ? 1 : 0)) != 0)
          this.Trm[m][k] = 1.0F;
    scale(paramBoolean, i, j);
    completeImage();
  }

  private void scale(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    if ((this.Trm[0][0] != 1.0D) || (this.Trm[1][1] != 1.0D) || (this.Trm[0][1] != 0.0D) || (this.Trm[1][0] != 0.0D))
    {
      AffineTransform localAffineTransform = new AffineTransform(this.Trm[0][0], this.Trm[0][1], this.Trm[1][0], this.Trm[1][1], 0.0F, 0.0F);
      Area localArea = new Area(new Rectangle(0, 0, paramInt1, paramInt2));
      localArea.transform(localAffineTransform);
      double d1 = localArea.getBounds2D().getY();
      double d2 = localArea.getBounds2D().getX();
      float f1 = this.Trm[0][0];
      float f2 = this.Trm[0][1];
      float f3 = this.Trm[1][0];
      float f4 = this.Trm[1][1];
      localAffineTransform = new AffineTransform(f1, f2, f3, f4, -d2, -d1);
      if (f1 < 0.0F)
        f1 = -f1;
      if (f2 < 0.0F)
        f2 = -f2;
      if (f3 < 0.0F)
        f3 = -f3;
      if (f4 < 0.0F)
        f4 = -f4;
      if ((f1 > 5.0F) || (f2 > 5.0F) || (f3 > 5.0F) || (f4 > 5.0F))
        return;
      AffineTransformOp localAffineTransformOp;
      int i;
      if ((paramInt1 > 1) && (paramInt2 > 1))
      {
        if ((this.CTM[0][0] == 0.0F) && (this.CTM[1][1] == 0.0F) && (this.CTM[0][1] > 0.0F) && (this.CTM[1][0] < 0.0F))
        {
          localAffineTransform.scale(-1.0D, 1.0D);
          localAffineTransform.translate(-this.current_image.getWidth(), 0.0D);
        }
        localAffineTransformOp = new AffineTransformOp(localAffineTransform, ColorSpaces.hints);
      }
      else
      {
        i = 1;
        if (paramInt2 == 1)
        {
          WritableRaster localWritableRaster = this.current_image.getRaster();
          int j = localWritableRaster.getNumBands();
          int k = localWritableRaster.getWidth();
          int[] arrayOfInt = new int[k * j + 1];
          localWritableRaster.getPixels(0, 0, k, 1, arrayOfInt);
          for (int m = 0; m < j; m++)
          {
            int n = arrayOfInt[0];
            for (int i1 = 1; i1 < k; i1++)
              if (arrayOfInt[(i1 * m)] != n)
              {
                i = 0;
                i1 = k;
                m = j;
              }
          }
        }
        if (i != 0)
          localAffineTransformOp = new AffineTransformOp(localAffineTransform, null);
        else
          localAffineTransformOp = new AffineTransformOp(localAffineTransform, ColorSpaces.hints);
      }
      if ((this.CTM[1][0] != 0.0F) || (this.CTM[0][1] != 0.0F))
        this.current_image = ColorSpaceConvertor.convertToARGB(this.current_image);
      if ((paramBoolean) && (paramInt2 > 1))
      {
        i = 0;
        try
        {
          this.current_image = localAffineTransformOp.filter(this.current_image, null);
        }
        catch (Exception localException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException1.getMessage());
          i = 1;
        }
        if (i != 0)
          try
          {
            localAffineTransformOp = new AffineTransformOp(localAffineTransform, null);
            this.current_image = localAffineTransformOp.filter(this.current_image, null);
          }
          catch (Exception localException2)
          {
          }
      }
    }
  }

  private void completeImage()
  {
    calcCoordinates();
  }

  private final void calcCoordinates()
  {
    if ((this.CTM[1][0] == 0.0F) && (this.CTM[0][1] == 0.0F))
    {
      this.i_x = ((int)this.CTM[2][0]);
      this.i_y = ((int)this.CTM[2][1]);
      this.i_w = ((int)this.CTM[0][0]);
      this.i_h = ((int)this.CTM[1][1]);
      if (this.i_w < 0)
        this.i_w = (-this.i_w);
      if (this.i_h < 0)
        this.i_h = (-this.i_h);
    }
    else
    {
      this.i_w = ((int)Math.sqrt(this.CTM[0][0] * this.CTM[0][0] + this.CTM[0][1] * this.CTM[0][1]));
      this.i_h = ((int)Math.sqrt(this.CTM[1][1] * this.CTM[1][1] + this.CTM[1][0] * this.CTM[1][0]));
      if ((this.CTM[1][0] > 0.0F) && (this.CTM[0][1] < 0.0F))
      {
        this.i_x = ((int)this.CTM[2][0]);
        this.i_y = ((int)(this.CTM[2][1] + this.CTM[0][1]));
      }
      else if ((this.CTM[1][0] < 0.0F) && (this.CTM[0][1] > 0.0F))
      {
        this.i_x = ((int)(this.CTM[2][0] + this.CTM[1][0]));
        this.i_y = ((int)this.CTM[2][1]);
      }
      else if ((this.CTM[1][0] > 0.0F) && (this.CTM[0][1] > 0.0F))
      {
        this.i_x = ((int)this.CTM[2][0]);
        this.i_y = ((int)this.CTM[2][1]);
      }
      else
      {
        this.i_x = ((int)this.CTM[2][0]);
        this.i_y = ((int)this.CTM[2][1]);
      }
    }
    if (this.CTM[1][1] < 0.0F)
      this.i_y -= this.i_h;
    if (this.CTM[0][0] < 0.0F)
      this.i_x -= this.i_w;
  }

  public final int getImageY()
  {
    return this.i_y;
  }

  public final BufferedImage getImage()
  {
    return this.current_image;
  }

  public final int getImageW()
  {
    return this.i_w;
  }

  public final int getImageH()
  {
    return this.i_h;
  }

  public final int getImageX()
  {
    return this.i_x;
  }

  public final void clipImage(Area paramArea)
  {
    Area localArea = (Area)paramArea.clone();
    int i = this.current_image.getWidth();
    int j = this.current_image.getHeight();
    double d1 = localArea.getBounds2D().getX();
    double d2 = localArea.getBounds2D().getY();
    double d3 = localArea.getBounds2D().getHeight();
    double d4 = j - d3;
    AffineTransform localAffineTransform1 = new AffineTransform();
    localAffineTransform1.translate(-d1, -d2);
    localAffineTransform1.scale(1.0D, -1.0D);
    localAffineTransform1.translate(d1, -(d2 + d3));
    localArea.transform(localAffineTransform1);
    AffineTransform localAffineTransform2 = new AffineTransform();
    localAffineTransform2.translate(-this.i_x, this.i_y + d4);
    localArea.transform(localAffineTransform2);
    double d5 = localArea.getBounds2D().getX();
    double d6 = localArea.getBounds2D().getY();
    double d7 = localArea.getBounds2D().getWidth();
    double d8 = localArea.getBounds2D().getHeight();
    int k = this.current_image.getType();
    if (k == 0)
      k = 2;
    else if (k == 1)
      k = 2;
    BufferedImage localBufferedImage = new BufferedImage(i, j, k);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    if (!localBufferedImage.getColorModel().hasAlpha())
    {
      localGraphics2D.setBackground(Color.white);
      localGraphics2D.fill(new Rectangle(0, 0, i, j));
    }
    localGraphics2D.setClip(localArea);
    try
    {
      localGraphics2D.drawImage(this.current_image, 0, 0, null);
    }
    catch (Exception localException1)
    {
      LogWriter.writeLog("Exception " + localException1 + " plotting clipping image");
    }
    if (d6 < 0.0D)
    {
      d8 += d6;
      d6 = 0.0D;
    }
    if (d5 < 0.0D)
    {
      d7 += d5;
      d5 = 0.0D;
    }
    if (d7 > i)
      d7 = i;
    if (d8 > j)
      d8 = j;
    if (d6 + d8 > j)
      d8 = j - d6;
    if (d5 + d7 > i)
      d7 = i - d5;
    try
    {
      this.current_image = localBufferedImage.getSubimage((int)d5, (int)d6, (int)d7, (int)d8);
    }
    catch (Exception localException2)
    {
      LogWriter.writeLog("Exception " + localException2 + " extracting clipped image with values x=" + d5 + " y=" + d6 + " w=" + d7 + " h=" + d8 + " from image ");
    }
    double d9;
    if (this.i_x > d1)
      d9 = this.i_x;
    else
      d9 = d1;
    double d10;
    if (this.i_y > d2)
      d10 = this.i_y;
    else
      d10 = d2;
    this.i_x = ((int)d9);
    this.i_y = ((int)d10);
    this.i_w = ((int)d7);
    this.i_h = ((int)d8);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.images.ImageTransformer
 * JD-Core Version:    0.6.2
 */
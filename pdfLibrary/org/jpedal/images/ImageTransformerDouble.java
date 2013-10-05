package org.jpedal.images;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import org.jpedal.color.ColorSpaces;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.GraphicsState;
import org.jpedal.render.BaseDisplay;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;

public class ImageTransformerDouble
{
  double ny = 0.0D;
  double nx = 0.0D;
  private Area clip = null;
  private BufferedImage current_image;
  private float[][] Trm;
  private float[][] Trm1;
  private float[][] Trm2;
  private float[][] CTM;
  private int i_x = 0;
  private int i_y = 0;
  private int i_w = 0;
  private int i_h = 0;
  private boolean scaleImage;
  private boolean hasClip = false;

  public ImageTransformerDouble(GraphicsState paramGraphicsState, BufferedImage paramBufferedImage, boolean paramBoolean)
  {
    this.current_image = paramBufferedImage;
    this.scaleImage = paramBoolean;
    this.CTM = paramGraphicsState.CTM;
    createMatrices();
    if (paramGraphicsState.getClippingShape() != null)
      this.clip = ((Area)paramGraphicsState.getClippingShape().clone());
    calcCoordinates();
  }

  public final void doubleScaleTransformShear()
  {
    scale(this.Trm1);
    if (this.clip != null)
    {
      Area localArea1 = (Area)this.clip.clone();
      Area localArea2 = getUnscaledClip((Area)this.clip.clone());
      int i = BaseDisplay.isRectangle(localArea1);
      clipImage(localArea2, localArea1, i);
      this.i_x = ((int)this.clip.getBounds2D().getMinX());
      this.i_y = ((int)this.clip.getBounds2D().getMinY());
      this.i_w = ((int)(this.clip.getBounds2D().getMaxX() - this.i_x));
      this.i_h = ((int)(this.clip.getBounds2D().getMaxY() - this.i_y));
    }
    else if (this.current_image.getType() != 10)
    {
      this.current_image = ColorSpaceConvertor.convertToARGB(this.current_image);
    }
  }

  public final void doubleScaleTransformScale()
  {
    if (((this.CTM[0][0] != 0.0D ? 1 : 0) & (this.CTM[1][1] != 0.0D ? 1 : 0)) != 0)
      scale(this.Trm2);
  }

  public final void completeImage()
  {
    if (this.hasClip)
    {
      this.i_x = ((int)this.clip.getBounds2D().getMinX());
      this.i_y = ((int)this.clip.getBounds2D().getMinY());
      this.i_w = this.current_image.getWidth();
      this.i_h = this.current_image.getHeight();
    }
  }

  private void scale(float[][] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat[0][0] != 1.0D) || (paramArrayOfFloat[1][1] != 1.0D) || (paramArrayOfFloat[0][1] != 0.0D) || (paramArrayOfFloat[1][0] != 0.0D))
    {
      int i = this.current_image.getWidth();
      int j = this.current_image.getHeight();
      AffineTransform localAffineTransform = new AffineTransform(paramArrayOfFloat[0][0], -paramArrayOfFloat[0][1], -paramArrayOfFloat[1][0], paramArrayOfFloat[1][1], 0.0F, 0.0F);
      Area localArea = new Area(new Rectangle(0, 0, i, j));
      localArea.transform(localAffineTransform);
      this.ny = localArea.getBounds2D().getY();
      this.nx = localArea.getBounds2D().getX();
      localAffineTransform = new AffineTransform(paramArrayOfFloat[0][0], -paramArrayOfFloat[0][1], -paramArrayOfFloat[1][0], paramArrayOfFloat[1][1], -this.nx, -this.ny);
      AffineTransformOp localAffineTransformOp;
      if (((i > 10 ? 1 : 0) & (j > 10 ? 1 : 0)) != 0)
        localAffineTransformOp = new AffineTransformOp(localAffineTransform, ColorSpaces.hints);
      else
        localAffineTransformOp = new AffineTransformOp(localAffineTransform, null);
      if (this.scaleImage)
        this.current_image = localAffineTransformOp.filter(this.current_image, null);
    }
  }

  private void createMatrices()
  {
    int i = this.current_image.getWidth();
    int j = this.current_image.getHeight();
    this.Trm = new float[3][3];
    this.Trm[0][0] = (this.CTM[0][0] / i);
    this.Trm[0][1] = (this.CTM[0][1] / i);
    this.Trm[0][2] = 0.0F;
    this.Trm[1][0] = (this.CTM[1][0] / j);
    this.Trm[1][1] = (this.CTM[1][1] / j);
    this.Trm[1][2] = 0.0F;
    this.Trm[2][0] = this.CTM[2][0];
    this.Trm[2][1] = this.CTM[2][1];
    this.Trm[2][2] = 1.0F;
    for (int k = 0; k < 3; k++)
      for (int m = 0; m < 3; m++)
        if (((this.Trm[m][k] > 0.99D ? 1 : 0) & (this.Trm[m][k] < 1.0F ? 1 : 0)) != 0)
          this.Trm[m][k] = 1.0F;
    this.Trm1 = new float[3][3];
    this.Trm2 = new float[3][3];
    float f1 = this.CTM[0][0];
    if (f1 < 0.0F)
      f1 = -f1;
    float f2 = this.CTM[0][1];
    if (f2 < 0.0F)
      f2 = -f2;
    float f3 = this.CTM[1][1];
    if (f3 < 0.0F)
      f3 = -f3;
    float f4 = this.CTM[1][0];
    if (f4 < 0.0F)
      f4 = -f4;
    int i1;
    if ((this.CTM[0][0] == 0.0D) || (this.CTM[1][1] == 0.0D))
    {
      this.Trm1 = this.Trm;
    }
    else if ((this.CTM[0][1] == 0.0D) && (this.CTM[1][0] == 0.0D))
    {
      this.Trm1[0][0] = (i / this.CTM[0][0]);
      this.Trm1[0][1] = 0.0F;
      this.Trm1[0][2] = 0.0F;
      this.Trm1[1][0] = 0.0F;
      this.Trm1[1][1] = (j / this.CTM[1][1]);
      this.Trm1[1][2] = 0.0F;
      this.Trm1[2][0] = 0.0F;
      this.Trm1[2][1] = 0.0F;
      this.Trm1[2][2] = 1.0F;
      this.Trm1 = Matrix.multiply(this.Trm, this.Trm1);
      for (n = 0; n < 3; n++)
        for (i1 = 0; i1 < 3; i1++)
          if (((this.Trm1[i1][n] > 0.99D ? 1 : 0) & (this.Trm1[i1][n] < 1.0F ? 1 : 0)) != 0)
            this.Trm1[i1][n] = 1.0F;
      if ((this.Trm1[2][0] < 0.0F) && (this.Trm1[0][0] > 0.0F) && (this.CTM[0][0] < 0.0F))
      {
        this.Trm1[2][0] = 0.0F;
        this.Trm1[0][0] = -1.0F;
      }
      if ((this.Trm1[2][1] < 0.0F) && (this.Trm1[1][1] > 0.0F) && (this.CTM[1][1] < 0.0F) && (this.CTM[0][0] < 0.0F))
      {
        this.Trm1[2][1] = 0.0F;
        this.Trm1[1][1] = -1.0F;
      }
    }
    else
    {
      if (f1 > f2)
        this.Trm1[0][0] = (i / this.CTM[0][0]);
      else
        this.Trm1[0][0] = (i / this.CTM[0][1]);
      if (this.Trm1[0][0] < 0.0F)
        this.Trm1[0][0] = (-this.Trm1[0][0]);
      this.Trm1[0][1] = 0.0F;
      this.Trm1[0][2] = 0.0F;
      this.Trm1[1][0] = 0.0F;
      if (f3 > f4)
        this.Trm1[1][1] = (j / this.CTM[1][1]);
      else
        this.Trm1[1][1] = (j / this.CTM[1][0]);
      if (this.Trm1[1][1] < 0.0F)
        this.Trm1[1][1] = (-this.Trm1[1][1]);
      this.Trm1[1][2] = 0.0F;
      this.Trm1[2][0] = 0.0F;
      this.Trm1[2][1] = 0.0F;
      this.Trm1[2][2] = 1.0F;
      this.Trm1 = Matrix.multiply(this.Trm, this.Trm1);
      for (n = 0; n < 3; n++)
        for (i1 = 0; i1 < 3; i1++)
          if (((this.Trm1[i1][n] > 0.99D ? 1 : 0) & (this.Trm1[i1][n] < 1.0F ? 1 : 0)) != 0)
            this.Trm1[i1][n] = 1.0F;
    }
    if (f1 > f2)
      this.Trm2[0][0] = (this.CTM[0][0] / i);
    else
      this.Trm2[0][0] = (this.CTM[0][1] / i);
    if (this.Trm2[0][0] < 0.0F)
      this.Trm2[0][0] = (-this.Trm2[0][0]);
    this.Trm2[0][1] = 0.0F;
    this.Trm2[0][2] = 0.0F;
    this.Trm2[1][0] = 0.0F;
    if (f3 > f4)
      this.Trm2[1][1] = (this.CTM[1][1] / j);
    else
      this.Trm2[1][1] = (this.CTM[1][0] / j);
    if (this.Trm2[1][1] < 0.0F)
      this.Trm2[1][1] = (-this.Trm2[1][1]);
    this.Trm2[1][2] = 0.0F;
    this.Trm2[2][0] = 0.0F;
    this.Trm2[2][1] = 0.0F;
    this.Trm2[2][2] = 1.0F;
    for (int n = 0; n < 3; n++)
      for (i1 = 0; i1 < 3; i1++)
        if (((this.Trm2[i1][n] > 0.99D ? 1 : 0) & (this.Trm2[i1][n] < 1.0F ? 1 : 0)) != 0)
          this.Trm2[i1][n] = 1.0F;
  }

  private final void calcCoordinates()
  {
    if (((this.CTM[1][0] == 0.0F ? 1 : 0) & (this.CTM[0][1] == 0.0F ? 1 : 0)) != 0)
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
      if (((this.CTM[1][0] > 0.0F ? 1 : 0) & (this.CTM[0][1] < 0.0F ? 1 : 0)) != 0)
      {
        this.i_x = ((int)this.CTM[2][0]);
        this.i_y = ((int)(this.CTM[2][1] + this.CTM[0][1]));
      }
      else if (((this.CTM[1][0] < 0.0F ? 1 : 0) & (this.CTM[0][1] > 0.0F ? 1 : 0)) != 0)
      {
        this.i_x = ((int)(this.CTM[2][0] + this.CTM[1][0]));
        this.i_y = ((int)this.CTM[2][1]);
      }
      else if (((this.CTM[1][0] > 0.0F ? 1 : 0) & (this.CTM[0][1] > 0.0F ? 1 : 0)) != 0)
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

  private final void clipImage(Area paramArea1, Area paramArea2, int paramInt)
  {
    double d1 = paramArea2.getBounds2D().getX();
    double d2 = paramArea2.getBounds2D().getY();
    int i = this.current_image.getWidth();
    int j = this.current_image.getHeight();
    int k = (int)paramArea1.getBounds().getX();
    int m = (int)paramArea1.getBounds().getY();
    int n = (int)paramArea1.getBounds().getWidth();
    int i1 = (int)paramArea1.getBounds().getHeight();
    if (paramInt > 5)
    {
      AffineTransform localAffineTransform1 = new AffineTransform();
      localAffineTransform1.scale(1.0D, -1.0D);
      localAffineTransform1.translate(0.0D, -this.current_image.getHeight());
      AffineTransformOp localAffineTransformOp1 = new AffineTransformOp(localAffineTransform1, ColorSpaces.hints);
      this.current_image = localAffineTransformOp1.filter(this.current_image, null);
      Area localArea = new Area(new Rectangle(0, 0, i, j));
      localArea.exclusiveOr(paramArea1);
      this.current_image = ColorSpaceConvertor.convertToARGB(this.current_image);
      Graphics2D localGraphics2D = this.current_image.createGraphics();
      localGraphics2D.setComposite(AlphaComposite.Clear);
      localGraphics2D.fill(localArea);
      AffineTransform localAffineTransform2 = new AffineTransform();
      localAffineTransform2.scale(1.0D, -1.0D);
      localAffineTransform2.translate(0.0D, -this.current_image.getHeight());
      AffineTransformOp localAffineTransformOp2 = new AffineTransformOp(localAffineTransform2, ColorSpaces.hints);
      this.current_image = localAffineTransformOp2.filter(this.current_image, null);
    }
    if (m < 0)
    {
      i1 -= m;
      m = 0;
    }
    else
    {
      m = j - i1 - m;
      if (m < 0)
        m = 0;
    }
    if (k < 0)
    {
      n -= k;
      k = 0;
    }
    if (n > i)
      n = i;
    if (i1 > j)
      i1 = j;
    if (m + i1 > j)
      i1 = j - m;
    if (k + n > i)
      n = i - k;
    if ((i1 >= 1) && (n >= 1) && ((k != 0) || (m != 0) || (n != this.current_image.getWidth()) || (i1 != this.current_image.getHeight())))
      try
      {
        this.current_image = this.current_image.getSubimage(k, m, n, i1);
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception " + localException + " extracting clipped image with values x=" + k + " y=" + m + " w=" + n + " h=" + i1 + " from image " + this.current_image);
      }
      catch (Error localError)
      {
      }
    double d3;
    if (this.i_x > d1)
      d3 = this.i_x;
    else
      d3 = d1;
    double d4;
    if (this.i_y > d2)
      d4 = this.i_y;
    else
      d4 = d2;
    this.i_x = ((int)d3);
    this.i_y = ((int)d4);
    this.i_w = n;
    this.i_h = i1;
  }

  private Area getUnscaledClip(Area paramArea)
  {
    double d1 = -this.CTM[2][0];
    double d2 = -this.CTM[2][1];
    if (this.CTM[1][0] < 0.0F)
      d1 -= this.CTM[1][0];
    if ((this.CTM[0][0] < 0.0F) && (this.CTM[1][0] >= 0.0F))
      d1 -= this.CTM[1][0];
    if (this.CTM[0][1] < 0.0F)
      d2 -= this.CTM[0][1];
    if (this.CTM[1][1] < 0.0F)
      if (this.CTM[0][1] > 0.0F)
        d2 -= this.CTM[0][1];
      else if (this.CTM[1][1] < 0.0F)
        d2 -= this.CTM[1][1];
    AffineTransform localAffineTransform1 = new AffineTransform();
    localAffineTransform1.translate(d1, d2);
    paramArea.transform(localAffineTransform1);
    AffineTransform localAffineTransform2 = new AffineTransform(1.0F / this.Trm2[0][0], 0.0F, 0.0F, 1.0F / this.Trm2[1][1], 0.0F, 0.0F);
    paramArea.transform(localAffineTransform2);
    int i = (int)paramArea.getBounds().getX();
    if (i < 0)
      paramArea.transform(AffineTransform.getTranslateInstance(-i, 0.0D));
    return paramArea;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.images.ImageTransformerDouble
 * JD-Core Version:    0.6.2
 */
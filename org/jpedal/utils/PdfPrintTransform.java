package org.jpedal.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;

public class PdfPrintTransform
{
  public static final boolean DEBUG_PRINT = false;
  private boolean autoRotate;
  private boolean chooseSourceByPDFSize;
  private int scalingMode;
  private double scalingFactor;
  private boolean centerOnScaling;
  private static final boolean useOldTechniques = false;
  private Rectangle crop = null;
  private PageFormat format = null;

  protected PdfPrintTransform()
  {
  }

  public PdfPrintTransform(boolean paramBoolean1, boolean paramBoolean2, int paramInt, boolean paramBoolean3)
  {
    this.autoRotate = paramBoolean1;
    this.centerOnScaling = paramBoolean2;
    this.chooseSourceByPDFSize = paramBoolean3;
    setPageScaling(paramInt);
    this.scalingFactor = 1.0D;
  }

  public AffineTransform getPageTransform(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, PageFormat paramPageFormat)
  {
    return getPageTransform(new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4), paramInt5, paramPageFormat);
  }

  private AffineTransform performTranslation(PageFormat paramPageFormat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
  {
    AffineTransform localAffineTransform = new AffineTransform();
    Point localPoint = new Point(paramInt3 / 2 + paramInt1, paramInt4 / 2 + paramInt2);
    localAffineTransform.translate(-localPoint.x, -localPoint.y);
    localAffineTransform.translate(paramInt3 / 2, paramInt4 / 2);
    if (this.centerOnScaling)
    {
      if ((!this.autoRotate) && ((this.scalingMode == 0) || (this.scalingMode == 2)))
      {
        if ((paramInt5 == 90) || (paramBoolean))
          localAffineTransform.translate(-paramInt3, paramPageFormat.getWidth() - paramInt4);
        else
          localAffineTransform.translate(0.0D, 0.0D);
      }
      else if (!paramBoolean)
      {
        localAffineTransform.translate(paramPageFormat.getImageableX() / this.scalingFactor, paramPageFormat.getImageableY() / this.scalingFactor);
        localAffineTransform.translate(-(paramInt3 / 2), -(paramInt4 / 2));
        localAffineTransform.translate(paramPageFormat.getImageableWidth() / 2.0D / this.scalingFactor, paramPageFormat.getImageableHeight() / 2.0D / this.scalingFactor);
      }
      else
      {
        localAffineTransform.translate(-paramInt3, 0.0D);
        localAffineTransform.translate(-paramPageFormat.getImageableY() / this.scalingFactor, paramPageFormat.getImageableX() / this.scalingFactor);
        localAffineTransform.translate(-paramPageFormat.getImageableHeight() / 2.0D / this.scalingFactor, paramPageFormat.getImageableWidth() / 2.0D / this.scalingFactor);
        localAffineTransform.translate(paramInt3 / 2, -paramInt4 / 2);
      }
    }
    else
    {
      double d1;
      double d2;
      switch (this.scalingMode)
      {
      case 0:
        if (!this.autoRotate)
        {
          if ((paramInt5 != 0) || (paramBoolean))
            localAffineTransform.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY() + paramPageFormat.getWidth() - paramInt4);
          else
            localAffineTransform.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
        }
        else if (!paramBoolean)
        {
          d1 = paramPageFormat.getImageableY() + (paramPageFormat.getImageableHeight() - paramInt4 * this.scalingFactor) / 2.0D;
          d2 = paramPageFormat.getImageableX() + (paramPageFormat.getImageableWidth() - paramInt3 * this.scalingFactor) / 2.0D;
          localAffineTransform.translate(d2, d1);
        }
        else
        {
          d1 = -(paramPageFormat.getImageableY() + paramPageFormat.getImageableHeight() - (paramPageFormat.getImageableHeight() - paramInt3 * this.scalingFactor) / 2.0D);
          d2 = paramPageFormat.getImageableX() + (paramPageFormat.getImageableWidth() - paramInt4 * this.scalingFactor) / 2.0D;
          localAffineTransform.translate(d1, d2);
        }
        break;
      case 1:
        if (((paramInt5 == 90) || (paramInt5 == 270)) && (paramBoolean))
        {
          d1 = -(paramPageFormat.getImageableY() + paramPageFormat.getImageableHeight() - (paramPageFormat.getImageableY() + paramPageFormat.getImageableHeight() - (paramInt1 + paramInt3) * this.scalingFactor));
          d2 = -(paramPageFormat.getImageableX() + paramPageFormat.getImageableWidth() - paramInt4 * this.scalingFactor);
          localAffineTransform.translate(d1, d2);
        }
        else if (paramBoolean)
        {
          localAffineTransform.translate(-(paramPageFormat.getImageableY() + paramPageFormat.getImageableHeight() - (paramInt1 + paramInt4) * this.scalingFactor), -(paramPageFormat.getImageableX() + paramPageFormat.getImageableWidth() - paramInt4 * this.scalingFactor));
        }
        else
        {
          d1 = paramPageFormat.getImageableY() + (paramPageFormat.getImageableHeight() - paramInt4 * this.scalingFactor) / 2.0D;
          d2 = paramPageFormat.getImageableX() + (paramPageFormat.getImageableWidth() - paramInt3 * this.scalingFactor) / 2.0D;
          if (d2 < d1)
            d2 = paramPageFormat.getImageableX();
          else
            d1 = paramPageFormat.getImageableY();
          localAffineTransform.translate(d2, d1);
        }
        break;
      case 2:
        if (!this.autoRotate)
        {
          if ((paramInt5 == 90) || (paramBoolean))
            localAffineTransform.translate(-paramInt3, paramPageFormat.getWidth() - paramInt4);
          else
            localAffineTransform.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
        }
        else if (!paramBoolean)
        {
          d1 = paramPageFormat.getImageableY() + (paramPageFormat.getImageableHeight() - paramInt4 * this.scalingFactor) / 2.0D;
          d2 = paramPageFormat.getImageableX() + (paramPageFormat.getImageableWidth() - paramInt3 * this.scalingFactor) / 2.0D;
          localAffineTransform.translate(d2, d1);
        }
        else
        {
          localAffineTransform.translate(-paramInt3, paramPageFormat.getWidth() - paramInt4 * this.scalingFactor);
        }
        break;
      }
    }
    return localAffineTransform;
  }

  private AffineTransform performScaling(int paramInt1, int paramInt2, PageFormat paramPageFormat, boolean paramBoolean)
  {
    AffineTransform localAffineTransform = new AffineTransform();
    double d1 = !paramBoolean ? paramInt1 : paramInt2;
    double d2 = !paramBoolean ? paramInt2 : paramInt1;
    double d3 = (paramPageFormat.getImageableWidth() - 1.0D) / d1;
    double d4 = (paramPageFormat.getImageableHeight() - 1.0D) / d2;
    if (((d3 >= 1.0D) && (d4 >= 1.0D)) || ((this.scalingMode == 2) || (this.scalingMode == 1)))
    {
      this.scalingFactor = (d3 < d4 ? d3 : d4);
      localAffineTransform.scale(this.scalingFactor, this.scalingFactor);
    }
    return localAffineTransform;
  }

  public void setAutoRotateAndCenter(boolean paramBoolean)
  {
    this.autoRotate = paramBoolean;
    this.centerOnScaling = paramBoolean;
  }

  public void setAutoRotateAndCenter(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.autoRotate = paramBoolean1;
    this.centerOnScaling = paramBoolean2;
  }

  public void setPageScaling(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
    case 1:
    case 2:
      this.scalingMode = paramInt;
      break;
    default:
      throw new IllegalArgumentException("Unknown Scaling mode type: " + paramInt);
    }
  }

  private static boolean isRotated(AffineTransform paramAffineTransform)
  {
    return ((paramAffineTransform.getShearX() > 0.0D) && (paramAffineTransform.getShearY() < 0.0D)) || ((paramAffineTransform.getShearX() < 0.0D) && (paramAffineTransform.getShearY() > 0.0D));
  }

  public static void main(String[] paramArrayOfString)
  {
  }

  public AffineTransform getPageTransform(Rectangle paramRectangle, int paramInt, PageFormat paramPageFormat)
  {
    this.crop = paramRectangle;
    this.format = paramPageFormat;
    AffineTransform localAffineTransform = new AffineTransform();
    this.scalingFactor = 1.0D;
    int i = 0;
    if ((this.autoRotate) || (this.chooseSourceByPDFSize))
    {
      int j = paramPageFormat.getImageableWidth() > paramPageFormat.getImageableHeight() ? 1 : 0;
      int k = this.crop.width > this.crop.height ? 1 : 0;
      if ((paramInt == 90) || (paramInt == 270))
        k = this.crop.width < this.crop.height ? 1 : 0;
      i = j != k ? 1 : 0;
    }
    if (i == 0)
    {
      paramInt += 180;
      if (paramInt > 360)
        paramInt -= 360;
    }
    else
    {
      paramInt += 90;
      if (paramInt > 360)
        paramInt -= 360;
    }
    double d1 = paramInt % 180 == 0 ? this.crop.width : this.crop.height;
    double d2 = paramInt % 180 == 0 ? this.crop.height : this.crop.width;
    double d3 = (paramPageFormat.getImageableWidth() - 1.0D) / d1;
    double d4 = (paramPageFormat.getImageableHeight() - 1.0D) / d2;
    if (((d3 >= 1.0D) && (d4 >= 1.0D)) || ((this.scalingMode == 2) || (this.scalingMode == 1)))
      this.scalingFactor = (d3 < d4 ? d3 : d4);
    if ((paramInt != 0) || (i != 0))
      applyRotation(localAffineTransform, paramInt);
    if ((this.crop.x != 0) || (this.crop.y != 0))
      applyCrop(localAffineTransform, paramInt);
    applyOffset(localAffineTransform, paramInt);
    if (this.centerOnScaling)
      applyCentering(localAffineTransform, paramInt);
    if (this.scalingMode != 0)
      localAffineTransform.scale(this.scalingFactor, this.scalingFactor);
    localAffineTransform.scale(-1.0D, 1.0D);
    localAffineTransform.translate(-this.crop.width, 0.0D);
    this.crop = null;
    this.format = null;
    return localAffineTransform;
  }

  private void applyRotation(AffineTransform paramAffineTransform, int paramInt)
  {
    int i = paramInt / 90;
    paramAffineTransform.rotate(i * 3.141592653589793D / 2.0D);
    switch (i)
    {
    case 1:
      paramAffineTransform.translate(0.0D, -(this.crop.height * this.scalingFactor));
      break;
    case 2:
      paramAffineTransform.translate(-(this.crop.width * this.scalingFactor), -(this.crop.height * this.scalingFactor));
      break;
    case 3:
      paramAffineTransform.translate(-(this.crop.width * this.scalingFactor), 0.0D);
    }
  }

  private void applyCrop(AffineTransform paramAffineTransform, int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      paramAffineTransform.translate(this.crop.x * this.scalingFactor, -this.crop.y * this.scalingFactor);
      break;
    case 90:
      paramAffineTransform.translate(this.crop.x * this.scalingFactor, -this.crop.y * this.scalingFactor);
      break;
    case 180:
      paramAffineTransform.translate(this.crop.x * this.scalingFactor, -this.crop.y * this.scalingFactor);
      break;
    case 270:
      paramAffineTransform.translate(this.crop.x * this.scalingFactor, -this.crop.y * this.scalingFactor);
    }
  }

  private void applyOffset(AffineTransform paramAffineTransform, int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      paramAffineTransform.translate(this.format.getImageableX(), this.format.getImageableY());
      break;
    case 90:
      paramAffineTransform.translate(this.format.getImageableX(), -this.format.getImageableY());
      break;
    case 180:
      paramAffineTransform.translate(-this.format.getImageableX(), -this.format.getImageableY());
      break;
    case 270:
      paramAffineTransform.translate(-this.format.getImageableX(), this.format.getImageableY());
    }
  }

  private void applyCentering(AffineTransform paramAffineTransform, int paramInt)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    switch (paramInt)
    {
    case 0:
      d1 = (this.format.getImageableWidth() - this.crop.width * this.scalingFactor) / 2.0D;
      d2 = (this.format.getImageableHeight() - this.crop.height * this.scalingFactor) / 2.0D;
      break;
    case 90:
      d1 = (this.format.getImageableHeight() - this.crop.width * this.scalingFactor) / 2.0D;
      d2 = -((this.format.getImageableWidth() - this.crop.height * this.scalingFactor) / 2.0D);
      break;
    case 180:
      d1 = -(this.format.getImageableWidth() - this.crop.width * this.scalingFactor) / 2.0D;
      d2 = -(this.format.getImageableHeight() - this.crop.height * this.scalingFactor) / 2.0D;
      break;
    case 270:
      d1 = -(this.format.getImageableHeight() - this.crop.width * this.scalingFactor) / 2.0D;
      d2 = (this.format.getImageableWidth() - this.crop.height * this.scalingFactor) / 2.0D;
    }
    paramAffineTransform.translate(d1, d2);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.PdfPrintTransform
 * JD-Core Version:    0.6.2
 */
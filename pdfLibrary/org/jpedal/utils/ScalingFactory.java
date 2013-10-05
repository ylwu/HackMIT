package org.jpedal.utils;

import java.awt.geom.AffineTransform;
import org.jpedal.objects.PageOrigins;
import org.jpedal.objects.PdfPageData;

public class ScalingFactory
{
  public static double[] getScalingForImage(int paramInt1, int paramInt2, float paramFloat, PdfPageData paramPdfPageData)
  {
    double d1 = paramPdfPageData.getMediaBoxX(paramInt1) * paramFloat;
    double d2 = paramPdfPageData.getMediaBoxY(paramInt1) * paramFloat;
    double d3 = paramPdfPageData.getMediaBoxHeight(paramInt1) * paramFloat;
    double d4 = paramPdfPageData.getCropBoxWidth(paramInt1) * paramFloat;
    double d5 = paramPdfPageData.getCropBoxHeight(paramInt1) * paramFloat;
    double d6 = paramPdfPageData.getCropBoxX(paramInt1) * paramFloat;
    double d7 = paramPdfPageData.getCropBoxY(paramInt1) * paramFloat;
    AffineTransform localAffineTransform = new AffineTransform();
    double[] arrayOfDouble = new double[6];
    int i = (int)(d4 + (d6 - d1));
    int j = (int)(d5 + (d7 - d2));
    if (paramInt2 == 270)
    {
      localAffineTransform.rotate(-1.570796326794897D, i / 2, j / 2);
      if (paramPdfPageData.getOrigin() == PageOrigins.BOTTOM_LEFT)
      {
        double d8 = localAffineTransform.getTranslateX();
        double d9 = localAffineTransform.getTranslateY();
        localAffineTransform.translate(j - d9, -d8);
        localAffineTransform.translate(0.0D, j);
        localAffineTransform.scale(1.0D, -1.0D);
        localAffineTransform.translate(-(d6 + d1), -(d3 - d5 - (d7 - d2)));
      }
    }
    else if (paramInt2 == 180)
    {
      localAffineTransform.rotate(3.141592653589793D, i / 2, j / 2);
      if (paramPdfPageData.getOrigin() == PageOrigins.BOTTOM_LEFT)
      {
        localAffineTransform.translate(-(d6 + d1), j + (d7 + d2) - (d3 - d5 - (d7 - d2)));
        localAffineTransform.scale(1.0D, -1.0D);
      }
    }
    else if (paramInt2 == 90)
    {
      localAffineTransform.rotate(1.570796326794897D);
      if (paramPdfPageData.getOrigin() == PageOrigins.BOTTOM_LEFT)
      {
        localAffineTransform.translate(0.0D, d7 + d2 - (d3 - d5 - (d7 - d2)));
        localAffineTransform.scale(1.0D, -1.0D);
      }
    }
    else if (paramPdfPageData.getOrigin() == PageOrigins.BOTTOM_LEFT)
    {
      localAffineTransform.translate(0.0D, j);
      localAffineTransform.scale(1.0D, -1.0D);
      localAffineTransform.translate(0.0D, -(d3 - d5 - (d7 - d2)));
    }
    localAffineTransform.scale(paramFloat, paramFloat);
    localAffineTransform.getMatrix(arrayOfDouble);
    return arrayOfDouble;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.ScalingFactory
 * JD-Core Version:    0.6.2
 */
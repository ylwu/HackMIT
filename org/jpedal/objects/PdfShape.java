package org.jpedal.objects;

import java.awt.Shape;
import javafx.scene.shape.Path;

public abstract interface PdfShape
{
  public abstract void setEVENODDWindingRule();

  public abstract void setNONZEROWindingRule();

  public abstract void closeShape();

  public abstract Shape generateShapeFromPath(float[][] paramArrayOfFloat, float paramFloat, int paramInt1, int paramInt2);

  public abstract void setClip(boolean paramBoolean);

  public abstract void resetPath();

  public abstract boolean isClip();

  public abstract int getSegmentCount();

  public abstract int getComplexClipCount();

  public abstract void lineTo(float paramFloat1, float paramFloat2);

  public abstract void moveTo(float paramFloat1, float paramFloat2);

  public abstract void appendRectangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public abstract void addBezierCurveC(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

  public abstract void addBezierCurveV(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public abstract void addBezierCurveY(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public abstract Path getPath();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfShape
 * JD-Core Version:    0.6.2
 */
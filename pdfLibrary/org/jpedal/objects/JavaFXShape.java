package org.jpedal.objects;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import javafx.collections.ObservableList;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;

public class JavaFXShape
  implements Serializable, PdfShape
{
  int complexClipCount = 0;
  private boolean isClip = false;
  private Path path = new Path();
  private FillRule windingRule;
  private float[] currentPos = new float[2];
  private static boolean DEBUG = false;

  public final void closeShape()
  {
    this.path.getElements().add(new ClosePath());
  }

  public final void addBezierCurveC(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    this.path.getElements().add(new CubicCurveTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6));
    this.currentPos[0] = paramFloat5;
    this.currentPos[1] = paramFloat6;
  }

  public final void setNONZEROWindingRule()
  {
    setWindingRule(FillRule.NON_ZERO);
  }

  public final void lineTo(float paramFloat1, float paramFloat2)
  {
    this.path.getElements().add(new LineTo(paramFloat1, paramFloat2));
    this.currentPos[0] = paramFloat1;
    this.currentPos[1] = paramFloat2;
  }

  public final void addBezierCurveV(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.path.getElements().add(new CubicCurveTo(this.currentPos[0], this.currentPos[1], paramFloat1, paramFloat2, paramFloat3, paramFloat4));
    this.currentPos[0] = paramFloat3;
    this.currentPos[1] = paramFloat4;
  }

  public final Shape generateShapeFromPath(float[][] paramArrayOfFloat, float paramFloat, int paramInt1, int paramInt2)
  {
    return new GeneralPath();
  }

  public final void appendRectangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    moveTo(paramFloat1, paramFloat2);
    lineTo(paramFloat1 + paramFloat3, paramFloat2);
    lineTo(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
    lineTo(paramFloat1, paramFloat2 + paramFloat4);
    lineTo(paramFloat1, paramFloat2);
    closeShape();
  }

  public final void moveTo(float paramFloat1, float paramFloat2)
  {
    this.path.getElements().add(new MoveTo(paramFloat1, paramFloat2));
    this.currentPos[0] = paramFloat1;
    this.currentPos[1] = paramFloat2;
  }

  public final void addBezierCurveY(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.path.getElements().add(new QuadCurveTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4));
    this.currentPos[0] = paramFloat3;
    this.currentPos[1] = paramFloat4;
  }

  public final void resetPath()
  {
    this.path = new Path();
    this.windingRule = FillRule.NON_ZERO;
  }

  public final void setEVENODDWindingRule()
  {
    setWindingRule(FillRule.EVEN_ODD);
  }

  public final void setWindingRule(FillRule paramFillRule)
  {
    this.windingRule = paramFillRule;
    this.path.setFillRule(this.windingRule);
  }

  public int getSegmentCount()
  {
    if (this.path == null)
      return 0;
    return this.path.getElements().size();
  }

  public void setClip(boolean paramBoolean)
  {
    this.isClip = paramBoolean;
  }

  public boolean isClip()
  {
    return this.isClip;
  }

  public int getComplexClipCount()
  {
    return this.complexClipCount;
  }

  public Path getPath()
  {
    return this.path;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.JavaFXShape
 * JD-Core Version:    0.6.2
 */
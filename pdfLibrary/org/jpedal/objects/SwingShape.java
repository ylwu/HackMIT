package org.jpedal.objects;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javafx.scene.shape.Path;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;

public class SwingShape
  implements Serializable, PdfShape
{
  int complexClipCount = 0;
  private Vector_Float shape_primitive_x2 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y = new Vector_Float(1000);
  private Vector_Int shape_primitives = new Vector_Int(1000);
  private int winding_rule = 1;
  private Vector_Float shape_primitive_x3 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y3 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y2 = new Vector_Float(1000);
  private Vector_Float shape_primitive_x = new Vector_Float(1000);
  private static final int H = 3;
  private static final int L = 2;
  private static final int V = 6;
  private static final int M = 1;
  private static final int Y = 4;
  private static final int C = 5;
  private boolean isClip = false;

  public final void closeShape()
  {
    this.shape_primitives.addElement(3);
    this.shape_primitive_x.addElement(0.0F);
    this.shape_primitive_y.addElement(0.0F);
    this.shape_primitive_x2.addElement(0.0F);
    this.shape_primitive_y2.addElement(0.0F);
    this.shape_primitive_x3.addElement(0.0F);
    this.shape_primitive_y3.addElement(0.0F);
  }

  public final void addBezierCurveC(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    this.shape_primitives.addElement(5);
    this.shape_primitive_x.addElement(paramFloat1);
    this.shape_primitive_y.addElement(paramFloat2);
    this.shape_primitive_x2.addElement(paramFloat3);
    this.shape_primitive_y2.addElement(paramFloat4);
    this.shape_primitive_x3.addElement(paramFloat5);
    this.shape_primitive_y3.addElement(paramFloat6);
  }

  public final void setNONZEROWindingRule()
  {
    this.winding_rule = 1;
  }

  public final void lineTo(float paramFloat1, float paramFloat2)
  {
    this.shape_primitives.addElement(2);
    this.shape_primitive_x.addElement(paramFloat1);
    this.shape_primitive_y.addElement(paramFloat2);
    this.shape_primitive_x2.addElement(0.0F);
    this.shape_primitive_y2.addElement(0.0F);
    this.shape_primitive_x3.addElement(0.0F);
    this.shape_primitive_y3.addElement(0.0F);
  }

  public final void addBezierCurveV(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.shape_primitives.addElement(6);
    this.shape_primitive_x.addElement(200.0F);
    this.shape_primitive_y.addElement(200.0F);
    this.shape_primitive_x2.addElement(paramFloat1);
    this.shape_primitive_y2.addElement(paramFloat2);
    this.shape_primitive_x3.addElement(paramFloat3);
    this.shape_primitive_y3.addElement(paramFloat4);
  }

  public final Shape generateShapeFromPath(float[][] paramArrayOfFloat, float paramFloat, int paramInt1, int paramInt2)
  {
    boolean bool = this.isClip;
    if (paramInt1 == 110)
      bool = false;
    GeneralPath localGeneralPath = null;
    Area localArea = null;
    float[] arrayOfFloat1 = this.shape_primitive_x.get();
    float[] arrayOfFloat2 = this.shape_primitive_y.get();
    float[] arrayOfFloat3 = this.shape_primitive_x2.get();
    float[] arrayOfFloat4 = this.shape_primitive_y2.get();
    float[] arrayOfFloat5 = this.shape_primitive_x3.get();
    float[] arrayOfFloat6 = this.shape_primitive_y3.get();
    int[] arrayOfInt = this.shape_primitives.get();
    int i = this.shape_primitives.size() - 1;
    if ((i == 6) && (paramInt1 == 66) && (paramFloat >= 0.9F))
      for (int j = 0; j < 8; j++)
      {
        float f1 = arrayOfFloat1[j] - (int)arrayOfFloat1[j];
        if (f1 > 0.5F)
          arrayOfFloat1[j] = ((int)arrayOfFloat1[j] - 1.0F);
      }
    for (int k = 0; k < i; k++)
    {
      if (localGeneralPath == null)
      {
        localGeneralPath = new GeneralPath(this.winding_rule);
        localGeneralPath.moveTo(arrayOfFloat1[k], arrayOfFloat2[k]);
      }
      if (arrayOfInt[k] == 3)
      {
        localGeneralPath.closePath();
        if (bool)
        {
          if (localArea == null)
          {
            localArea = new Area(localGeneralPath);
            if ((localArea.getBounds2D().getWidth() <= 0.0D) || (localArea.getBounds2D().getHeight() <= 0.0D))
              localArea = new Area(localGeneralPath.getBounds2D());
          }
          else
          {
            localArea.add(new Area(localGeneralPath));
          }
          localGeneralPath = null;
        }
      }
      if (arrayOfInt[k] == 2)
      {
        localGeneralPath.lineTo(arrayOfFloat1[k], arrayOfFloat2[k]);
      }
      else if (arrayOfInt[k] == 1)
      {
        localGeneralPath.moveTo(arrayOfFloat1[k], arrayOfFloat2[k]);
      }
      else if (arrayOfInt[k] == 4)
      {
        localGeneralPath.curveTo(arrayOfFloat1[k], arrayOfFloat2[k], arrayOfFloat5[k], arrayOfFloat6[k], arrayOfFloat5[k], arrayOfFloat6[k]);
      }
      else if (arrayOfInt[k] == 5)
      {
        localGeneralPath.curveTo(arrayOfFloat1[k], arrayOfFloat2[k], arrayOfFloat3[k], arrayOfFloat4[k], arrayOfFloat5[k], arrayOfFloat6[k]);
      }
      else if (arrayOfInt[k] == 6)
      {
        float f2 = (float)localGeneralPath.getCurrentPoint().getX();
        float f3 = (float)localGeneralPath.getCurrentPoint().getY();
        localGeneralPath.curveTo(f2, f3, arrayOfFloat3[k], arrayOfFloat4[k], arrayOfFloat5[k], arrayOfFloat6[k]);
      }
    }
    if ((localGeneralPath != null) && ((localGeneralPath.getBounds().getHeight() == 0.0D) || ((paramFloat > 0.8D) && (paramFloat < 0.9D) && (localGeneralPath.getBounds2D().getHeight() < 0.1000000014901161D))) && ((localGeneralPath.getBounds2D().getWidth() != 0.0D) || (localGeneralPath.getBounds2D().getHeight() != 0.0D)))
      if ((paramFloat > 1.0F) && (localGeneralPath.getBounds2D().getWidth() < 1.0D))
      {
        localGeneralPath.moveTo(0.0F, -paramFloat / 2.0F);
        localGeneralPath.lineTo(0.0F, paramFloat / 2.0F);
      }
      else
      {
        localGeneralPath.moveTo(0.0F, 1.0F);
      }
    if ((localGeneralPath != null) && (localGeneralPath.getBounds().getWidth() == 0.0D))
      localGeneralPath.moveTo(1.0F, 0.0F);
    Object localObject2;
    if ((paramArrayOfFloat[0][0] != 1.0F) || (paramArrayOfFloat[1][0] != 0.0F) || (paramArrayOfFloat[2][0] != 0.0F) || (paramArrayOfFloat[0][1] != 0.0F) || (paramArrayOfFloat[1][1] != 1.0F) || (paramArrayOfFloat[2][1] != 0.0F) || (paramArrayOfFloat[0][2] != 0.0F) || (paramArrayOfFloat[1][2] != 0.0F) || (paramArrayOfFloat[2][2] != 1.0F))
    {
      localObject2 = new AffineTransform(paramArrayOfFloat[0][0], paramArrayOfFloat[0][1], paramArrayOfFloat[1][0], paramArrayOfFloat[1][1], paramArrayOfFloat[2][0], paramArrayOfFloat[2][1]);
      if (localGeneralPath != null)
        localGeneralPath.transform((AffineTransform)localObject2);
      else if (localArea != null)
        localArea.transform((AffineTransform)localObject2);
    }
    if ((localGeneralPath != null) && (paramArrayOfFloat[0][0] == 1.0F) && (paramArrayOfFloat[1][1] == -1.0F) && (localGeneralPath.getBounds().height == 1) && (paramFloat > 10.0F))
    {
      localObject2 = localGeneralPath.getBounds();
      localGeneralPath = new GeneralPath(this.winding_rule);
      localGeneralPath.moveTo(((Rectangle)localObject2).x, ((Rectangle)localObject2).y - paramFloat / 2.0F);
      localGeneralPath.lineTo(((Rectangle)localObject2).x, ((Rectangle)localObject2).y + paramFloat / 2.0F);
      localGeneralPath.closePath();
    }
    Object localObject1;
    if (!bool)
    {
      if (localArea == null)
        localObject1 = localGeneralPath;
      else
        localObject1 = localArea;
    }
    else
      localObject1 = localArea;
    if ((paramInt1 == 110) && (getSegmentCount() > 2500))
      this.complexClipCount += 1;
    return localObject1;
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

  public final void appendCircle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f1 = paramFloat3 / 2.0F;
    float f2 = paramFloat4 / 2.0F;
    addBezierCurveC(paramFloat1 + f1, paramFloat2, paramFloat1 + paramFloat3, paramFloat2, paramFloat1 + paramFloat3, paramFloat2 + f2);
    addBezierCurveC(paramFloat1 + paramFloat3, paramFloat2 + f2, paramFloat1 + paramFloat3, paramFloat2 + paramFloat4, paramFloat1 + f1, paramFloat2 + paramFloat4);
    addBezierCurveC(paramFloat1 + f1, paramFloat2 + paramFloat4, paramFloat1, paramFloat2 + paramFloat4, paramFloat1, paramFloat2 + f2);
    addBezierCurveC(paramFloat1, paramFloat2 + f2, paramFloat1, paramFloat2, paramFloat1 + f1, paramFloat2);
    closeShape();
  }

  public final void moveTo(float paramFloat1, float paramFloat2)
  {
    this.shape_primitives.addElement(1);
    this.shape_primitive_x.addElement(paramFloat1);
    this.shape_primitive_y.addElement(paramFloat2);
    this.shape_primitive_x2.addElement(0.0F);
    this.shape_primitive_y2.addElement(0.0F);
    this.shape_primitive_x3.addElement(0.0F);
    this.shape_primitive_y3.addElement(0.0F);
  }

  public final void addBezierCurveY(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.shape_primitives.addElement(4);
    this.shape_primitive_x.addElement(paramFloat1);
    this.shape_primitive_y.addElement(paramFloat2);
    this.shape_primitive_x2.addElement(0.0F);
    this.shape_primitive_y2.addElement(0.0F);
    this.shape_primitive_x3.addElement(paramFloat3);
    this.shape_primitive_y3.addElement(paramFloat4);
  }

  public final void resetPath()
  {
    this.shape_primitives.clear();
    this.shape_primitive_x.clear();
    this.shape_primitive_y.clear();
    this.shape_primitive_x2.clear();
    this.shape_primitive_y2.clear();
    this.shape_primitive_x3.clear();
    this.shape_primitive_y3.clear();
    this.winding_rule = 1;
  }

  public final void setEVENODDWindingRule()
  {
    this.winding_rule = 0;
  }

  public int getSegmentCount()
  {
    if (this.shape_primitives == null)
      return 0;
    return this.shape_primitives.size() - 1;
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
    return null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.SwingShape
 * JD-Core Version:    0.6.2
 */
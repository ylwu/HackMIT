package org.jpedal.fonts.glyph;

import java.awt.geom.GeneralPath;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Path;

public class T1GlyphFactory
  implements GlyphFactory
{
  private static final boolean debug = false;
  boolean useFX = false;
  private static final float zero = 0.0F;
  private Vector_Float shape_primitive_x2 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y = new Vector_Float(1000);
  private Vector_Int shape_primitives = new Vector_Int(1000);
  private Vector_Float shape_primitive_x3 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y3 = new Vector_Float(1000);
  private Vector_Float shape_primitive_y2 = new Vector_Float(1000);
  private Vector_Float shape_primitive_x = new Vector_Float(1000);
  protected static final int H = 3;
  protected static final int L = 2;
  protected static final int M = 1;
  protected static final int C = 5;
  private float ymin = 0.0F;
  private int leftSideBearing = 0;

  private T1GlyphFactory()
  {
  }

  public T1GlyphFactory(boolean paramBoolean)
  {
    this.useFX = paramBoolean;
  }

  public PdfGlyph getGlyph()
  {
    if (this.useFX)
      return getFXGlyph();
    return getSwingGlyph();
  }

  private PdfGlyph getSwingGlyph()
  {
    Vector_Path localVector_Path = new Vector_Path(100);
    GeneralPath localGeneralPath = new GeneralPath(1);
    localGeneralPath.moveTo(0.0F, 0.0F);
    float[] arrayOfFloat1 = this.shape_primitive_x.get();
    float[] arrayOfFloat2 = this.shape_primitive_y.get();
    float[] arrayOfFloat3 = this.shape_primitive_x2.get();
    float[] arrayOfFloat4 = this.shape_primitive_y2.get();
    float[] arrayOfFloat5 = this.shape_primitive_x3.get();
    float[] arrayOfFloat6 = this.shape_primitive_y3.get();
    int i = 0;
    int j = this.shape_primitives.size() - 1;
    int[] arrayOfInt = this.shape_primitives.get();
    while (i < j)
    {
      if (arrayOfInt[i] == 2)
      {
        localGeneralPath.lineTo(arrayOfFloat1[i], arrayOfFloat2[i] - this.ymin);
      }
      else if (arrayOfInt[i] == 3)
      {
        localGeneralPath.closePath();
        localVector_Path.addElement(localGeneralPath);
        localGeneralPath = new GeneralPath(1);
        localGeneralPath.moveTo(0.0F, 0.0F);
      }
      else if (arrayOfInt[i] == 1)
      {
        localGeneralPath.moveTo(arrayOfFloat1[i], arrayOfFloat2[i] - this.ymin);
      }
      else if (arrayOfInt[i] == 5)
      {
        localGeneralPath.curveTo(arrayOfFloat1[i], arrayOfFloat2[i] - this.ymin, arrayOfFloat3[i], arrayOfFloat4[i] - this.ymin, arrayOfFloat5[i], arrayOfFloat6[i] - this.ymin);
      }
      i++;
    }
    this.shape_primitive_x2.reuse();
    this.shape_primitive_y.reuse();
    this.shape_primitives.reuse();
    this.shape_primitive_x3.reuse();
    this.shape_primitive_y3.reuse();
    this.shape_primitive_y2.reuse();
    this.shape_primitive_x.reuse();
    return new T1Glyph(localVector_Path);
  }

  private PdfGlyph getFXGlyph()
  {
    float[] arrayOfFloat1 = this.shape_primitive_x.get();
    float[] arrayOfFloat2 = this.shape_primitive_y.get();
    float[] arrayOfFloat3 = this.shape_primitive_x2.get();
    float[] arrayOfFloat4 = this.shape_primitive_y2.get();
    float[] arrayOfFloat5 = this.shape_primitive_x3.get();
    float[] arrayOfFloat6 = this.shape_primitive_y3.get();
    int i = this.shape_primitives.size() - 1;
    int[] arrayOfInt = this.shape_primitives.get();
    this.shape_primitive_x2 = new Vector_Float(1000);
    this.shape_primitive_y = new Vector_Float(1000);
    this.shape_primitives = new Vector_Int(1000);
    this.shape_primitive_x3 = new Vector_Float(1000);
    this.shape_primitive_y3 = new Vector_Float(1000);
    this.shape_primitive_y2 = new Vector_Float(1000);
    this.shape_primitive_x = new Vector_Float(1000);
    return new T1GlyphFX(arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, arrayOfFloat5, arrayOfFloat6, this.ymin, i, arrayOfInt);
  }

  public final void closePath()
  {
    this.shape_primitives.addElement(3);
    this.shape_primitive_x.addElement(0.0F);
    this.shape_primitive_y.addElement(0.0F);
    this.shape_primitive_x2.addElement(0.0F);
    this.shape_primitive_y2.addElement(0.0F);
    this.shape_primitive_x3.addElement(0.0F);
    this.shape_primitive_y3.addElement(0.0F);
  }

  public final void curveTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    this.shape_primitives.addElement(5);
    this.shape_primitive_x.addElement(paramFloat1);
    this.shape_primitive_y.addElement(paramFloat2);
    this.shape_primitive_x2.addElement(paramFloat3);
    this.shape_primitive_y2.addElement(paramFloat4);
    this.shape_primitive_x3.addElement(paramFloat5);
    this.shape_primitive_y3.addElement(paramFloat6);
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

  public void setYMin(float paramFloat)
  {
    this.ymin = paramFloat;
  }

  public int getLSB()
  {
    return this.leftSideBearing;
  }

  public boolean useFX()
  {
    return this.useFX;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.T1GlyphFactory
 * JD-Core Version:    0.6.2
 */
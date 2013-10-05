package org.jpedal.objects;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Area;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import org.jpedal.color.DeviceRGBColorSpace;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfColor;
import org.jpedal.color.PdfPaint;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;

public class GraphicsState
{
  public float x;
  public float y;
  TextState currentTextState = new TextState();
  private float strokeAlpha = 1.0F;
  private float nonstrokeAlpha = 1.0F;
  private float maxStrokeAlpha = 1.0F;
  private float maxNonstrokeAlpha = 1.0F;
  public float[][] lastCTM = new float[3][3];
  private PdfObject TR;
  public PdfObject SMask;
  public GenericColorSpace strokeColorSpace = new DeviceRGBColorSpace();
  public GenericColorSpace nonstrokeColorSpace = new DeviceRGBColorSpace();
  private boolean hasClipChanged = false;
  private boolean op = false;
  private boolean OP = false;
  private float OPM = 0.0F;
  private PdfPaint nonstrokeColor = new PdfColor(0, 0, 0);
  private PdfPaint strokeColor = new PdfColor(0, 0, 0);
  private Area current_clipping_shape = null;
  private PdfClip current_clip = null;
  private static final boolean debugClip = false;
  public float[][] CTM = new float[3][3];
  private int current_line_dash_phase = 0;
  private Area TRmask = null;
  private int fill_type;
  private int mitre_limit = 0;
  private float[] current_line_dash_array = new float[0];
  private int current_line_cap_style = 0;
  private float current_line_width = 1.0F;
  private int output_line_width = -1;
  private int current_line_join_style = 0;
  private int text_render_type = 2;
  private int minX = 0;
  private int minY = 0;
  public static final int STROKE = 1;
  public static final int FILL = 2;
  public static final int FILLSTROKE = 3;
  public static final int INVISIBLE = 4;
  public static final int CLIPTEXT = 7;
  private PdfArrayIterator BM;
  boolean useJavaFX = false;

  private GraphicsState()
  {
  }

  public GraphicsState(boolean paramBoolean)
  {
    this.useJavaFX = paramBoolean;
    resetCTM();
  }

  public GraphicsState(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    this.useJavaFX = paramBoolean;
    this.minX = (-paramInt1);
    this.minY = (-paramInt2);
    resetCTM();
  }

  public void setMaxAlpha(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 1:
      this.maxStrokeAlpha = paramFloat;
      break;
    case 2:
      this.maxNonstrokeAlpha = paramFloat;
    }
  }

  public void setAlpha(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 1:
      if (paramFloat > this.maxStrokeAlpha)
        paramFloat = this.maxStrokeAlpha;
      this.strokeAlpha = paramFloat;
      break;
    case 2:
      if (paramFloat > this.maxNonstrokeAlpha)
        paramFloat = this.maxNonstrokeAlpha;
      this.nonstrokeAlpha = paramFloat;
    }
  }

  public float getAlpha(int paramInt)
  {
    float f = 1.0F;
    switch (paramInt)
    {
    case 1:
      if (this.strokeAlpha > this.maxStrokeAlpha)
        f = this.maxStrokeAlpha;
      else
        f = this.strokeAlpha;
      break;
    case 2:
      if (this.nonstrokeAlpha > this.maxNonstrokeAlpha)
        f = this.maxNonstrokeAlpha;
      else
        f = this.nonstrokeAlpha;
      break;
    }
    return f;
  }

  public float getAlphaMax(int paramInt)
  {
    float f = 1.0F;
    switch (paramInt)
    {
    case 1:
      f = this.maxStrokeAlpha;
      break;
    case 2:
      f = this.maxNonstrokeAlpha;
    }
    return f;
  }

  public boolean getNonStrokeOP()
  {
    return this.op;
  }

  public float getOPM()
  {
    return this.OPM;
  }

  public PdfObject getTR()
  {
    return this.TR;
  }

  public final void setTextRenderType(int paramInt)
  {
    this.text_render_type = paramInt;
    this.TRmask = null;
  }

  public final int getTextRenderType()
  {
    return this.text_render_type;
  }

  public final void setMitreLimit(int paramInt)
  {
    this.mitre_limit = paramInt;
  }

  public final float getLineWidth()
  {
    return this.current_line_width;
  }

  public final int getOutputLineWidth()
  {
    return this.output_line_width;
  }

  public final void setFillType(int paramInt)
  {
    this.fill_type = paramInt;
  }

  public final void updateClip(Area paramArea)
  {
    if ((this.current_clipping_shape == null) || (paramArea == null))
    {
      this.current_clipping_shape = paramArea;
      this.hasClipChanged = true;
    }
    else
    {
      this.current_clipping_shape.intersect(paramArea);
      this.hasClipChanged = true;
    }
  }

  public final void addClip(Path paramPath)
  {
  }

  public final void addClip(Area paramArea)
  {
    if (this.TRmask == null)
      this.TRmask = paramArea;
    else
      this.TRmask.add(paramArea);
  }

  public final Stroke getStroke()
  {
    float f = this.current_line_width;
    if (this.CTM[0][0] != 0.0F)
      f *= this.CTM[0][0];
    else if (this.CTM[0][1] != 0.0F)
      f *= this.CTM[0][1];
    if (f < 0.0F)
      f = -f;
    if (this.mitre_limit < 1)
      this.mitre_limit = 1;
    int i = this.current_line_dash_array.length;
    BasicStroke localBasicStroke;
    if (i > 0)
    {
      float[] arrayOfFloat = new float[i];
      for (int j = 0; j < i; j++)
      {
        if (this.CTM[0][0] != 0.0F)
          arrayOfFloat[j] = (this.current_line_dash_array[j] * this.CTM[0][0]);
        else
          arrayOfFloat[j] = (this.current_line_dash_array[j] * this.CTM[0][1]);
        if (arrayOfFloat[j] < 0.0F)
          arrayOfFloat[j] = (-arrayOfFloat[j]);
        if (arrayOfFloat[j] < 0.05F)
          arrayOfFloat[j] = 0.05F;
      }
      localBasicStroke = new BasicStroke(f, this.current_line_cap_style, this.current_line_join_style, this.mitre_limit, arrayOfFloat, Math.abs(this.current_line_dash_phase));
    }
    else
    {
      localBasicStroke = new BasicStroke(f, this.current_line_cap_style, this.current_line_join_style, this.mitre_limit);
    }
    return localBasicStroke;
  }

  public float getCTMAdjustedLineWidth()
  {
    float f = this.current_line_width;
    if (this.CTM[0][0] != 0.0F)
      f *= this.CTM[0][0];
    else if (this.CTM[0][1] != 0.0F)
      f *= this.CTM[0][1];
    if (f < 0.0F)
      f = -f;
    return f;
  }

  public final void setLineWidth(float paramFloat)
  {
    this.current_line_width = paramFloat;
  }

  public void setCTMAdjustedLineWidth(float paramFloat)
  {
    if (this.CTM[0][0] != 0.0F)
      paramFloat /= this.CTM[0][0];
    else if (this.CTM[0][1] != 0.0F)
      paramFloat /= this.CTM[0][1];
    this.current_line_width = paramFloat;
  }

  public final void setOutputLineWidth(int paramInt)
  {
    this.output_line_width = paramInt;
  }

  public final Area getClippingShape()
  {
    if ((this.TRmask != null) && (this.current_clipping_shape == null))
      return this.TRmask;
    if (this.TRmask != null)
    {
      this.TRmask.intersect(this.current_clipping_shape);
      return this.TRmask;
    }
    return this.current_clipping_shape;
  }

  public void setMode(PdfObject paramPdfObject)
  {
    this.nonstrokeAlpha = 1.0F;
    this.op = false;
    this.OP = false;
    if (paramPdfObject == null)
      return;
    float f1 = paramPdfObject.getFloatNumber(7207);
    if (f1 != -1.0F)
      this.current_line_width = f1;
    boolean bool = paramPdfObject.getBoolean(1120547);
    this.SMask = paramPdfObject.getDictionary(489767774);
    int i = (this.SMask == null) || (bool) || (this.SMask.getGeneralType(489767774) == 507461173) ? 1 : 0;
    if (i != 0)
    {
      f2 = paramPdfObject.getFloatNumber(4881);
      float f3 = paramPdfObject.getFloatNumber(13105);
      if (f2 != -1.0F)
        setAlpha(1, f2);
      if (f3 != -1.0F)
        setAlpha(2, f3);
    }
    this.OP = paramPdfObject.getBoolean(7968);
    this.op = paramPdfObject.getBoolean(16192);
    float f2 = paramPdfObject.getFloatNumber(2039837);
    if (f2 != -1.0F)
      this.OPM = f2;
    else
      this.OPM = 0.0F;
    this.TR = paramPdfObject.getDictionary(9250);
    if (this.TR != null)
    {
      int j = 0;
      if (this.TR.getGeneralType(9250) == 1567455623)
      {
        j = 1;
      }
      else
      {
        byte[][] arrayOfByte1 = this.TR.getKeyArray(9250);
        if (arrayOfByte1 != null)
        {
          int k = arrayOfByte1.length;
          if (k > 0)
          {
            j = 1;
            for (byte[] arrayOfByte : arrayOfByte1)
            {
              int i1 = PdfDictionary.getIntKey(1, arrayOfByte.length - 1, arrayOfByte);
              if (i1 != 1567455623)
              {
                j = 0;
                break;
              }
            }
          }
        }
      }
      if (j != 0)
        this.TR = null;
    }
    this.BM = paramPdfObject.getMixedArray(4637);
  }

  public final void setCapStyle(int paramInt)
  {
    this.current_line_cap_style = paramInt;
  }

  public final void setJoinStyle(int paramInt)
  {
    this.current_line_join_style = paramInt;
  }

  public final void checkWholePageClip(int paramInt)
  {
    if ((this.current_clipping_shape != null) && (this.current_clipping_shape.getBounds().getHeight() > paramInt + 2) && (this.current_clipping_shape.getBounds().y >= 0))
    {
      this.current_clipping_shape = null;
      this.hasClipChanged = true;
    }
  }

  public final void setDashArray(float[] paramArrayOfFloat)
  {
    this.current_line_dash_array = paramArrayOfFloat;
  }

  public final Object clone()
  {
    GraphicsState localGraphicsState = new GraphicsState();
    localGraphicsState.x = this.x;
    localGraphicsState.y = this.y;
    if (this.TR != null)
      localGraphicsState.TR = ((PdfObject)this.TR.clone());
    localGraphicsState.maxNonstrokeAlpha = this.maxNonstrokeAlpha;
    localGraphicsState.maxStrokeAlpha = this.maxStrokeAlpha;
    localGraphicsState.strokeAlpha = this.strokeAlpha;
    localGraphicsState.nonstrokeAlpha = this.nonstrokeAlpha;
    localGraphicsState.op = this.op;
    localGraphicsState.OP = this.OP;
    localGraphicsState.OPM = this.OPM;
    localGraphicsState.nonstrokeColor = this.nonstrokeColor;
    localGraphicsState.strokeColor = this.strokeColor;
    if (this.current_clipping_shape != null)
      localGraphicsState.current_clipping_shape = ((Area)this.current_clipping_shape.clone());
    int i;
    if (this.CTM != null)
      for (i = 0; i < 3; i++)
        System.arraycopy(this.CTM[i], 0, localGraphicsState.CTM[i], 0, 3);
    localGraphicsState.hasClipChanged = this.hasClipChanged;
    localGraphicsState.current_line_dash_phase = this.current_line_dash_phase;
    if (this.TRmask != null)
      localGraphicsState.TRmask = ((Area)this.TRmask.clone());
    localGraphicsState.fill_type = this.fill_type;
    localGraphicsState.mitre_limit = this.mitre_limit;
    if (this.current_line_dash_array != null)
    {
      i = this.current_line_dash_array.length;
      localGraphicsState.current_line_dash_array = new float[i];
      System.arraycopy(this.current_line_dash_array, 0, localGraphicsState.current_line_dash_array, 0, i);
    }
    localGraphicsState.current_line_cap_style = this.current_line_cap_style;
    localGraphicsState.current_line_width = this.current_line_width;
    localGraphicsState.current_line_join_style = this.current_line_join_style;
    localGraphicsState.text_render_type = this.text_render_type;
    localGraphicsState.minX = this.minX;
    localGraphicsState.minY = this.minY;
    return localGraphicsState;
  }

  private void resetCTM()
  {
    this.CTM[0][0] = 1.0F;
    this.CTM[1][0] = 0.0F;
    this.CTM[2][0] = this.minX;
    this.CTM[0][1] = 0.0F;
    this.CTM[1][1] = 1.0F;
    this.CTM[2][1] = this.minY;
    this.CTM[0][2] = 0.0F;
    this.CTM[1][2] = 0.0F;
    this.CTM[2][2] = 1.0F;
  }

  public final void setDashPhase(int paramInt)
  {
    this.current_line_dash_phase = paramInt;
  }

  public final int getFillType()
  {
    return this.fill_type;
  }

  public final void setClippingShape(Area paramArea)
  {
    this.current_clipping_shape = paramArea;
    this.hasClipChanged = true;
  }

  public PdfPaint getNonstrokeColor()
  {
    return this.nonstrokeColor;
  }

  public void setNonstrokeColor(PdfPaint paramPdfPaint)
  {
    this.nonstrokeColor = paramPdfPaint;
  }

  public PdfPaint getStrokeColor()
  {
    return this.strokeColor;
  }

  public void setStrokeColor(PdfPaint paramPdfPaint)
  {
    this.strokeColor = paramPdfPaint;
  }

  public PdfArrayIterator getBM()
  {
    return this.BM;
  }

  public boolean hasClipChanged()
  {
    boolean bool = this.hasClipChanged;
    this.hasClipChanged = false;
    return bool;
  }

  public void setTextState(TextState paramTextState)
  {
    this.currentTextState = paramTextState;
  }

  public TextState getTextState()
  {
    return this.currentTextState;
  }

  public void updateClip(Path paramPath)
  {
    if (this.current_clip == null)
      this.current_clip = new FXClip();
    this.hasClipChanged = this.current_clip.updateClip(paramPath);
  }

  public Shape getFXClippingShape()
  {
    if (this.current_clip == null)
      return null;
    return (Shape)this.current_clip.getClippingShape();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.GraphicsState
 * JD-Core Version:    0.6.2
 */
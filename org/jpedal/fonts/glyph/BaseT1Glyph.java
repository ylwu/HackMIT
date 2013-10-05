package org.jpedal.fonts.glyph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;

public class BaseT1Glyph
  implements Serializable
{
  protected float glyfwidth = 1000.0F;
  protected boolean isStroked = false;
  protected Map strokedPositions = new HashMap();
  protected int glyphNumber = -1;
  int id = 0;

  public int getmaxHeight()
  {
    return 0;
  }

  public String getGlyphName()
  {
    return "";
  }

  public void setStrokedOnly(boolean paramBoolean)
  {
    this.isStroked = paramBoolean;
  }

  public boolean containsBrokenData()
  {
    return false;
  }

  public Path getPath()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setWidth(float paramFloat)
  {
    this.glyfwidth = paramFloat;
  }

  public void setID(int paramInt)
  {
    this.id = paramInt;
  }

  public int getGlyphNumber()
  {
    return this.glyphNumber;
  }

  public void setGlyphNumber(int paramInt)
  {
    this.glyphNumber = paramInt;
  }

  public int getID()
  {
    return this.id;
  }

  public float getmaxWidth()
  {
    return this.glyfwidth;
  }

  public void setT3Colors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean)
  {
  }

  public boolean ignoreColors()
  {
    return false;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.BaseT1Glyph
 * JD-Core Version:    0.6.2
 */
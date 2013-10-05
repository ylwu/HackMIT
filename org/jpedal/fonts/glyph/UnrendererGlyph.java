package org.jpedal.fonts.glyph;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.Serializable;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;

public class UnrendererGlyph
  implements PdfGlyph, Serializable
{
  public float x;
  public float y;
  public int rawInt;
  public float currentWidth;

  public UnrendererGlyph(float paramFloat1, float paramFloat2, int paramInt, float paramFloat3)
  {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.rawInt = paramInt;
    this.currentWidth = paramFloat3;
  }

  public int getID()
  {
    return -1;
  }

  public void setID(int paramInt)
  {
  }

  public void setGlyphNumber(int paramInt)
  {
  }

  public int getGlyphNumber()
  {
    return -1;
  }

  public void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean)
  {
  }

  public float getmaxWidth()
  {
    return 0.0F;
  }

  public void setT3Colors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean)
  {
  }

  public boolean ignoreColors()
  {
    return false;
  }

  public Area getShape()
  {
    return null;
  }

  public void setWidth(float paramFloat)
  {
  }

  public int getFontBB(int paramInt)
  {
    return 0;
  }

  public void setStrokedOnly(boolean paramBoolean)
  {
  }

  public boolean containsBrokenData()
  {
    return false;
  }

  public Path getPath()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.UnrendererGlyph
 * JD-Core Version:    0.6.2
 */
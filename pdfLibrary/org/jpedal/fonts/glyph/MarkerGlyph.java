package org.jpedal.fonts.glyph;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.Serializable;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;

public class MarkerGlyph
  implements PdfGlyph, Serializable
{
  public float a;
  public float b;
  public float c;
  public float d;
  public String fontName;

  public MarkerGlyph(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, String paramString)
  {
    this.a = paramFloat1;
    this.b = paramFloat2;
    this.c = paramFloat3;
    this.d = paramFloat4;
    this.fontName = paramString;
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
 * Qualified Name:     org.jpedal.fonts.glyph.MarkerGlyph
 * JD-Core Version:    0.6.2
 */
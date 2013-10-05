package org.jpedal.fonts.glyph;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;

public abstract interface PdfGlyph
{
  public static final int FontBB_X = 1;
  public static final int FontBB_Y = 2;
  public static final int FontBB_WIDTH = 3;
  public static final int FontBB_HEIGHT = 4;

  public abstract int getID();

  public abstract void setID(int paramInt);

  public abstract int getGlyphNumber();

  public abstract void setGlyphNumber(int paramInt);

  public abstract void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean);

  public abstract float getmaxWidth();

  public abstract void setT3Colors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean);

  public abstract boolean ignoreColors();

  public abstract Area getShape();

  public abstract void setWidth(float paramFloat);

  public abstract int getFontBB(int paramInt);

  public abstract void setStrokedOnly(boolean paramBoolean);

  public abstract boolean containsBrokenData();

  public abstract Path getPath();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.PdfGlyph
 * JD-Core Version:    0.6.2
 */
package org.jpedal.fonts.glyph;

public abstract interface GlyphFactory
{
  public abstract PdfGlyph getGlyph();

  public abstract void curveTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

  public abstract void closePath();

  public abstract void moveTo(float paramFloat1, float paramFloat2);

  public abstract void lineTo(float paramFloat1, float paramFloat2);

  public abstract void setYMin(float paramFloat);

  public abstract int getLSB();

  public abstract boolean useFX();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.GlyphFactory
 * JD-Core Version:    0.6.2
 */
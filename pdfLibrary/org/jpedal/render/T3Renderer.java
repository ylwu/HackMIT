package org.jpedal.render;

import org.jpedal.color.PdfPaint;

public abstract interface T3Renderer extends DynamicVectorRenderer
{
  public abstract void setType3Glyph(String paramString);

  public abstract void lockColors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean);

  public abstract void setOptimisedRotation(boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.T3Renderer
 * JD-Core Version:    0.6.2
 */
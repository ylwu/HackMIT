package org.jpedal.render;

import java.awt.Color;
import java.util.Map;
import org.jpedal.color.PdfColor;
import org.jpedal.color.PdfPaint;
import org.jpedal.io.ObjectStore;

public class T3Display extends SwingDisplay
  implements T3Renderer
{
  public T3Display(int paramInt1, boolean paramBoolean, int paramInt2, ObjectStore paramObjectStore)
  {
    this.rawPageNumber = paramInt1;
    this.objectStoreRef = paramObjectStore;
    this.addBackground = paramBoolean;
    setupArrays(paramInt2);
  }

  public T3Display(byte[] paramArrayOfByte, Map paramMap)
  {
    super(paramArrayOfByte, paramMap);
  }

  public void setOptimisedRotation(boolean paramBoolean)
  {
    this.optimisedTurnCode = paramBoolean;
  }

  public void setType3Glyph(String paramString)
  {
    this.rawKey = paramString;
    this.isType3Font = true;
  }

  public void lockColors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean)
  {
    this.colorsLocked = paramBoolean;
    Color localColor1 = Color.white;
    Color localColor2 = Color.white;
    if ((paramPdfPaint1 != null) && (!paramPdfPaint1.isPattern()))
      localColor1 = (Color)paramPdfPaint1;
    this.strokeCol = new PdfColor(localColor1.getRed(), localColor1.getGreen(), localColor1.getBlue());
    if (!paramPdfPaint2.isPattern())
      localColor2 = (Color)paramPdfPaint2;
    this.fillCol = new PdfColor(localColor2.getRed(), localColor2.getGreen(), localColor2.getBlue());
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.T3Display
 * JD-Core Version:    0.6.2
 */
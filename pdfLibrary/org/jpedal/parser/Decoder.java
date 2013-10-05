package org.jpedal.parser;

import org.jpedal.fonts.PdfFont;
import org.jpedal.objects.TextState;

public abstract interface Decoder
{
  public abstract Object getObjectValue(int paramInt);

  public abstract void setReturnText(boolean paramBoolean);

  public abstract String getLastTextValue();

  public abstract void reset();

  public abstract void setParameters(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4);

  public abstract void setFont(PdfFont paramPdfFont);

  public abstract int processToken(TextState paramTextState, int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean isTTHintingRequired();

  public abstract void setIntValue(int paramInt1, int paramInt2);

  public abstract void setBooleanValue(int paramInt, boolean paramBoolean);

  public abstract void setTextState(TextState paramTextState);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.Decoder
 * JD-Core Version:    0.6.2
 */
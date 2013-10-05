package org.jpedal.examples.viewer.gui.generic;

import java.awt.Component;
import java.util.Map;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.swing.SearchList;

public abstract interface GUISearchWindow
{
  public abstract void find(PdfDecoder paramPdfDecoder, Values paramValues);

  public abstract void findWithoutWindow(PdfDecoder paramPdfDecoder, Values paramValues, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString);

  public abstract void grabFocusInInput();

  public abstract boolean isSearchVisible();

  public abstract void init(PdfDecoder paramPdfDecoder, Values paramValues);

  public abstract void removeSearchWindow(boolean paramBoolean);

  public abstract void resetSearchWindow();

  public abstract SearchList getResults();

  public abstract SearchList getResults(int paramInt);

  public abstract Map getTextRectangles();

  public abstract Component getContentPanel();

  public abstract int getStyle();

  public abstract void setStyle(int paramInt);

  public abstract int getFirstPageWithResults();

  public abstract void setWholeWords(boolean paramBoolean);

  public abstract void setCaseSensitive(boolean paramBoolean);

  public abstract void setMultiLine(boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.generic.GUISearchWindow
 * JD-Core Version:    0.6.2
 */
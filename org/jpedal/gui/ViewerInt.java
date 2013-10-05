package org.jpedal.gui;

import java.util.Map;
import org.jpedal.objects.PdfPageData;

public abstract interface ViewerInt
{
  public abstract void resetPrintData();

  public abstract byte[] getPrintData();

  public abstract PdfPageData getPageData();

  public abstract Map getFontList();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.gui.ViewerInt
 * JD-Core Version:    0.6.2
 */
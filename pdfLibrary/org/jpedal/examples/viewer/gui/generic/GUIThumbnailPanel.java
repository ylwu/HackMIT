package org.jpedal.examples.viewer.gui.generic;

import java.awt.Font;
import java.awt.image.BufferedImage;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.repositories.Vector_Object;

public abstract interface GUIThumbnailPanel
{
  public abstract boolean isShownOnscreen();

  public abstract void terminateDrawing();

  public abstract void setIsDisplayedOnscreen(boolean paramBoolean);

  public abstract Object[] getButtons();

  public abstract void addComponentListener();

  public abstract void generateOtherVisibleThumbnails(int paramInt);

  public abstract void setupThumbnails(int paramInt, Font paramFont, String paramString, PdfPageData paramPdfPageData);

  public abstract void removeAll();

  public abstract void setupThumbnails(int paramInt1, int[] paramArrayOfInt, int paramInt2);

  public abstract void generateOtherThumbnails(String[] paramArrayOfString, Vector_Object paramVector_Object);

  public abstract void resetHighlightedThumbnail(int paramInt);

  public abstract void resetToDefault();

  public abstract void removeAllListeners();

  public abstract void setThumbnailsEnabled(boolean paramBoolean);

  public abstract void refreshDisplay();

  public abstract void dispose();

  public abstract BufferedImage getImage(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel
 * JD-Core Version:    0.6.2
 */
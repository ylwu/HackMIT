package org.jpedal.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Map;
import javafx.scene.layout.Pane;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;

public abstract interface Display
{
  public static final int BORDER_SHOW = 1;
  public static final int BORDER_HIDE = 0;
  public static final int NODISPLAY = 0;
  public static final int SINGLE_PAGE = 1;
  public static final int CONTINUOUS = 2;
  public static final int FACING = 3;
  public static final int CONTINUOUS_FACING = 4;
  public static final int PAGEFLOW = 5;
  public static final int DISPLAY_LEFT_ALIGNED = 1;
  public static final int DISPLAY_CENTERED = 2;
  public static final boolean debugLayout = false;

  public abstract double getIndent();

  public abstract Rectangle getCursorBoxOnScreen();

  public abstract void setCursorBoxOnScreen(Rectangle paramRectangle, boolean paramBoolean);

  public abstract void forceRedraw();

  public abstract void setPageRotation(int paramInt);

  public abstract void resetMultiPageForms(int paramInt);

  public abstract void resetViewableArea();

  public abstract void paintPage(Pane paramPane, AcroRenderer paramAcroRenderer, TextLines paramTextLines);

  public abstract void paintPage(Graphics2D paramGraphics2D, AcroRenderer paramAcroRenderer, TextLines paramTextLines);

  public abstract void updateCursorBoxOnScreen(Rectangle paramRectangle, Color paramColor, int paramInt1, int paramInt2, int paramInt3);

  public abstract void drawCursor(Graphics paramGraphics, float paramFloat);

  public abstract AffineTransform setViewableArea(Rectangle paramRectangle)
    throws PdfException;

  public abstract void drawFacing(Rectangle paramRectangle);

  public abstract int[] getPageSize(int paramInt);

  public abstract void initRenderer(Map paramMap, Graphics2D paramGraphics2D);

  public abstract void decodeOtherPages(int paramInt1, int paramInt2);

  public abstract void stopGeneratingPage();

  public abstract void refreshDisplay();

  public abstract void disableScreen();

  public abstract void flushPageCaches();

  public abstract void resetCachedValues();

  public abstract void init(float paramFloat, int paramInt1, int paramInt2, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean);

  public abstract void drawBorder();

  public abstract void setup(boolean paramBoolean, PageOffsets paramPageOffsets);

  public abstract void resetToDefaultClip();

  public abstract int getYCordForPage(int paramInt);

  public abstract int getYCordForPage(int paramInt, float paramFloat);

  public abstract int getStartPage();

  public abstract int getEndPage();

  public abstract int getXCordForPage(int paramInt);

  public abstract void setThumbnailPanel(GUIThumbnailPanel paramGUIThumbnailPanel);

  public abstract void setScaling(float paramFloat);

  public abstract void setPageOffsets(int paramInt);

  public abstract void dispose();

  public abstract void setAcceleration(boolean paramBoolean);

  public abstract void setObjectValue(int paramInt, Object paramObject);

  public abstract int[] getHighlightedImage();

  public abstract void setHighlightedImage(int[] paramArrayOfInt);

  public abstract float getOldScaling();

  public abstract boolean getBoolean(BoolValue paramBoolValue);

  public abstract void setBoolean(BoolValue paramBoolValue, boolean paramBoolean);

  public static enum BoolValue
  {
    TURNOVER_ON, SEPARATE_COVER;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.Display
 * JD-Core Version:    0.6.2
 */
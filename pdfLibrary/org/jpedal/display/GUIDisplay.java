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
import org.jpedal.external.RenderChangeListener;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.ScalingFactory;

public class GUIDisplay
  implements Display
{
  protected int lastFormPage = -1;
  protected int lastStart = -1;
  protected int lastEnd = -1;
  protected int pageUsedForTransform;
  protected int lastIndent = -1;
  protected double indent = 0.0D;
  public RenderChangeListener customRenderChangeListener;
  protected DisplayOffsets displayOffsets;
  protected DecoderOptions options;
  public static boolean allowChangeCursor = true;
  public static boolean default_turnoverOn = true;
  public boolean turnoverOn = default_turnoverOn;
  public static boolean default_separateCover = true;
  public boolean separateCover = default_separateCover;
  public DynamicVectorRenderer currentDisplay;
  public boolean isInitialised;
  public boolean overRideAcceleration = false;
  public boolean useAcceleration = true;
  public PageOffsets currentOffset;
  public int startViewPage = 0;
  public int endViewPage = 0;
  private int[] highlightedImage = null;
  protected GUIThumbnailPanel thumbnails = null;
  public int[] xReached;
  public int[] yReached;
  public int[] pageW;
  public int[] pageH;
  public int[] pageOffsetH;
  public int[] pageOffsetW;
  public boolean[] isRotated;
  public int topW;
  public int topH;
  public double cropX;
  public double cropY;
  public double cropW;
  public double cropH;
  public int crx;
  public int cry;
  public int crw;
  public int crh;
  public int displayRotation;
  public int displayView = 1;
  public int lastDisplayRotation = 0;
  public int insetW;
  public int insetH;
  public float scaling;
  public float lastScaling;
  public int pageNumber;
  public float oldScaling = -1.0F;
  public float oldRotation = -1.0F;
  public PdfPageData pageData = null;
  public boolean running = false;
  public boolean isGeneratingOtherPages = false;
  public static int CURRENT_BORDER_STYLE = 1;
  protected double[] displayScalingDbl;

  public static void setBorderStyle(int paramInt)
  {
    CURRENT_BORDER_STYLE = paramInt;
  }

  public static int getBorderStyle()
  {
    return CURRENT_BORDER_STYLE;
  }

  public int getXCordForPage(int paramInt, float paramFloat)
  {
    if ((paramFloat == -2.0F) || ((paramFloat != -1.0F) && (paramFloat != this.oldScaling)))
    {
      this.oldScaling = paramFloat;
      setPageOffsets(paramInt);
    }
    return getXCordForPage(paramInt);
  }

  public int getYCordForPage(int paramInt, float paramFloat)
  {
    if ((paramFloat == -2.0F) || ((paramFloat != -1.0F) && (paramFloat != this.oldScaling)))
    {
      this.oldScaling = paramFloat;
      setPageOffsets(paramInt);
    }
    return getYCordForPage(paramInt);
  }

  public boolean getBoolean(Display.BoolValue paramBoolValue)
  {
    switch (1.$SwitchMap$org$jpedal$display$Display$BoolValue[paramBoolValue.ordinal()])
    {
    case 1:
      return this.separateCover;
    case 2:
      return this.turnoverOn;
    }
    return false;
  }

  public void setBoolean(Display.BoolValue paramBoolValue, boolean paramBoolean)
  {
    switch (1.$SwitchMap$org$jpedal$display$Display$BoolValue[paramBoolValue.ordinal()])
    {
    case 1:
      this.separateCover = paramBoolean;
      return;
    case 2:
      this.turnoverOn = paramBoolean;
      return;
    }
  }

  public void setObjectValue(int paramInt, Object paramObject)
  {
  }

  public void setCursorBoxOnScreen(Rectangle paramRectangle, boolean paramBoolean)
  {
  }

  public int[] getPageSize(int paramInt)
  {
    int[] arrayOfInt = new int[2];
    int i = 0;
    int j = 0;
    if ((paramInt == 3) && (this.pageW != null))
    {
      if (this.separateCover)
      {
        k = this.pageNumber;
        if ((k & 0x1) == 1)
          k--;
        m = k + 1;
      }
      else
      {
        k = this.pageNumber;
        if ((k & 0x1) == 0)
          k--;
        m = k + 1;
      }
      if (k == 0)
      {
        i = this.pageH[m];
        j = this.pageW[m] * 2;
      }
      else
      {
        i = this.pageH[k];
        if (m < this.pageH.length)
        {
          if (i < this.pageH[m])
            i = this.pageH[m];
          j = this.pageW[k] + this.pageW[m];
        }
        else
        {
          j = this.pageW[k] * 2;
        }
      }
    }
    int k = this.currentOffset.gaps;
    int m = this.currentOffset.doubleGaps;
    switch (paramInt)
    {
    case 3:
      arrayOfInt[0] = (j + this.insetW + this.insetW);
      arrayOfInt[1] = (i + this.insetH + this.insetH);
      break;
    case 2:
      if (((this.displayRotation == 90 ? 1 : 0) | (this.displayRotation == 270 ? 1 : 0)) != 0)
      {
        arrayOfInt[0] = ((int)(this.currentOffset.biggestHeight * this.scaling) + this.insetW + this.insetW);
        arrayOfInt[1] = ((int)(this.currentOffset.totalSingleWidth * this.scaling) + k + this.insetH + this.insetH);
      }
      else
      {
        arrayOfInt[0] = ((int)(this.currentOffset.biggestWidth * this.scaling) + this.insetW + this.insetW);
        arrayOfInt[1] = ((int)(this.currentOffset.totalSingleHeight * this.scaling) + k + this.insetH + this.insetH);
      }
      break;
    case 4:
      int n = this.pageData.getPageCount();
      if (((this.displayRotation == 90 ? 1 : 0) | (this.displayRotation == 270 ? 1 : 0)) != 0)
      {
        if (n == 2)
        {
          arrayOfInt[0] = ((int)(this.currentOffset.doublePageHeight * this.scaling) + this.insetW + this.insetW);
          arrayOfInt[1] = ((int)(this.currentOffset.biggestWidth * this.scaling) + k + this.insetH + this.insetH);
        }
        else
        {
          arrayOfInt[0] = ((int)(this.currentOffset.doublePageHeight * this.scaling) + this.insetW + this.insetW);
          arrayOfInt[1] = ((int)(this.currentOffset.totalDoubleWidth * this.scaling) + m + this.insetH + this.insetH);
        }
      }
      else if (n == 2)
      {
        arrayOfInt[0] = ((int)(this.currentOffset.doublePageWidth * this.scaling) + this.insetW + this.insetW);
        arrayOfInt[1] = ((int)(this.currentOffset.biggestHeight * this.scaling) + k + this.insetH + this.insetH);
      }
      else
      {
        arrayOfInt[0] = ((int)(this.currentOffset.doublePageWidth * this.scaling) + this.insetW + this.insetW);
        arrayOfInt[1] = ((int)(this.currentOffset.totalDoubleHeight * this.scaling) + m + this.insetH + this.insetH);
      }
      break;
    }
    return arrayOfInt;
  }

  public void setup(boolean paramBoolean, PageOffsets paramPageOffsets)
  {
    this.useAcceleration = paramBoolean;
    this.currentOffset = paramPageOffsets;
    this.overRideAcceleration = false;
  }

  public void setAcceleration(boolean paramBoolean)
  {
    this.useAcceleration = paramBoolean;
  }

  public void setPageSize(int paramInt, float paramFloat)
  {
    this.pageData.setScalingValue(paramFloat);
    this.topW = this.pageData.getScaledCropBoxWidth(paramInt);
    this.topH = this.pageData.getScaledCropBoxHeight(paramInt);
    double d = this.pageData.getScaledMediaBoxHeight(paramInt);
    this.cropX = this.pageData.getScaledCropBoxX(paramInt);
    this.cropY = this.pageData.getScaledCropBoxY(paramInt);
    this.cropW = this.topW;
    this.cropH = this.topH;
    if (this.displayView == 1)
    {
      this.crx = ((int)(this.insetW + this.cropX));
      this.cry = ((int)(this.insetH - this.cropY));
    }
    else
    {
      this.crx = this.insetW;
      this.cry = this.insetH;
    }
    int i = (int)(d - this.cropH);
    if ((this.displayRotation == 90) || (this.displayRotation == 270))
    {
      this.crw = ((int)this.cropH);
      this.crh = ((int)this.cropW);
      int j = this.crx;
      this.crx = this.cry;
      this.cry = j;
      this.crx += i;
    }
    else
    {
      this.crw = ((int)this.cropW);
      this.crh = ((int)this.cropH);
      this.cry += i;
    }
  }

  public void decodeOtherPages(int paramInt1, int paramInt2)
  {
  }

  public void disableScreen()
  {
    this.isInitialised = false;
    this.oldScaling = -1.0F;
  }

  public float getOldScaling()
  {
    return this.oldScaling;
  }

  public void refreshDisplay()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void flushPageCaches()
  {
  }

  public void init(float paramFloat, int paramInt1, int paramInt2, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean)
  {
    if (paramInt2 < 1)
      paramInt2 = 1;
    this.currentDisplay = paramDynamicVectorRenderer;
    this.scaling = paramFloat;
    this.displayRotation = paramInt1;
    this.pageNumber = paramInt2;
    this.insetW = this.options.getInsetW();
    this.insetH = this.options.getInsetW();
    paramDynamicVectorRenderer.setInset(this.insetW, this.insetH);
    this.pageData.setScalingValue(paramFloat);
    if (paramBoolean)
    {
      setPageOffsets(this.pageNumber);
      this.isInitialised = true;
    }
    this.lastScaling = paramFloat;
  }

  public void setThumbnailPanel(GUIThumbnailPanel paramGUIThumbnailPanel)
  {
    this.thumbnails = paramGUIThumbnailPanel;
  }

  public void resetCachedValues()
  {
  }

  public void stopGeneratingPage()
  {
    this.isGeneratingOtherPages = false;
    while (this.running)
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException localInterruptedException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localInterruptedException.getMessage());
      }
  }

  public int getYCordForPage(int paramInt)
  {
    if (this.yReached != null)
      return this.yReached[paramInt] + this.insetH;
    return this.insetH;
  }

  public int getStartPage()
  {
    return this.startViewPage;
  }

  public int getEndPage()
  {
    return this.endViewPage;
  }

  public void setScaling(float paramFloat)
  {
    this.scaling = paramFloat;
    if (this.pageData != null)
      this.pageData.setScalingValue(paramFloat);
  }

  public void setHighlightedImage(int[] paramArrayOfInt)
  {
    this.highlightedImage = paramArrayOfInt;
  }

  public int[] getHighlightedImage()
  {
    return this.highlightedImage;
  }

  public void drawBorder()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void resetToDefaultClip()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public int getXCordForPage(int paramInt)
  {
    if (this.xReached != null)
      return this.xReached[paramInt] + this.insetW;
    return this.insetW;
  }

  public void setPageOffsets(int paramInt)
  {
    int i = this.pageData.getPageCount();
    this.xReached = new int[i + 1];
    this.yReached = new int[i + 1];
    this.pageW = new int[i + 1];
    this.pageH = new int[i + 1];
    this.pageOffsetW = new int[i + 1];
    this.pageOffsetH = new int[i + 1];
    this.isRotated = new boolean[i + 1];
    int m = 10;
    if ((this.turnoverOn) && (i != 2) && (!this.pageData.hasMultipleSizes()) && (this.displayView == 3))
      m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i5;
    for (int i4 = 1; i4 < i + 1; i4++)
    {
      this.pageW[i4] = this.pageData.getScaledCropBoxWidth(i4);
      this.pageH[i4] = this.pageData.getScaledCropBoxHeight(i4);
      int k = this.pageData.getRotation(i4) + this.displayRotation;
      if (k >= 360)
        k -= 360;
      if ((k == 90) || (k == 270))
      {
        i5 = this.pageW[i4];
        this.pageW[i4] = this.pageH[i4];
        this.pageH[i4] = i5;
        this.isRotated[i4] = true;
      }
      if ((i4 & 0x1) == 1)
      {
        if (this.pageW[i4] > i2)
          i2 = this.pageW[i4];
        if (this.pageH[i4] > i3)
          i3 = this.pageH[i4];
      }
      else
      {
        if (this.pageW[i4] > n)
          n = this.pageW[i4];
        if (this.pageH[i4] > i1)
          i1 = this.pageH[i4];
      }
    }
    for (i4 = 1; i4 < i + 1; i4++)
    {
      int j = 0;
      if (((i == 2) && ((this.displayView == 3) || (this.displayView == 4))) || ((this.displayView == 3) && (!this.separateCover)))
      {
        if ((i4 & 0x1) == 1)
        {
          this.xReached[i4] = 0;
          this.yReached[i4] = 0;
        }
        else
        {
          this.xReached[i4] = (this.xReached[(i4 - 1)] + this.pageW[(i4 - 1)] + m);
          this.yReached[i4] = 0;
          if ((i4 != 2) || (this.pageData.getRotation(1) != 270))
          {
            this.pageOffsetW[2] = (this.pageW[2] - this.pageW[1] + this.pageOffsetW[1]);
            this.pageOffsetH[2] = (this.pageH[2] - this.pageH[1] + this.pageOffsetH[1]);
          }
        }
      }
      else if (i4 == 1)
      {
        if (this.displayView == 2)
        {
          this.xReached[1] = 0;
          this.yReached[1] = 0;
          this.pageOffsetW[1] = 0;
          this.pageOffsetH[1] = 0;
          this.pageOffsetW[0] = m;
          this.pageOffsetH[0] = m;
        }
        else if (this.displayView == 4)
        {
          this.pageOffsetW[0] = m;
          this.pageOffsetH[0] = m;
          this.pageOffsetW[1] = 0;
          this.pageOffsetH[1] = 0;
          this.xReached[1] = (n + m);
          this.yReached[1] = 0;
        }
        else if (this.displayView == 3)
        {
          this.xReached[1] = (this.pageW[1] + m);
          this.yReached[1] = 0;
        }
      }
      else if (this.displayView == 4)
      {
        if ((i4 < 2) || (((this.pageData.getRotation(i4) != 270) && (this.pageData.getRotation(i4) != 90)) || ((this.pageData.getRotation(i4 - 1) == 270) && (this.pageData.getRotation(i4 - 1) == 90) && (((this.pageData.getRotation(i4 - 1) != 270) && (this.pageData.getRotation(i4 - 1) != 90)) || ((this.pageData.getRotation(i4) == 270) && (this.pageData.getRotation(i4) == 90))))))
        {
          this.pageOffsetW[i4] = (this.pageW[i4] - this.pageW[(i4 - 1)] + this.pageOffsetW[(i4 - 1)]);
          this.pageOffsetH[i4] = (this.pageH[i4] - this.pageH[(i4 - 1)] + this.pageOffsetH[(i4 - 1)]);
        }
        if ((i4 & 0x1) == 0)
        {
          if (i4 < i)
            j = (this.pageH[(i4 + 1)] - this.pageH[i4]) / 2;
          if (j < 0)
            j = 0;
          if (i4 > 3)
          {
            i5 = (this.pageH[(i4 - 2)] - this.pageH[(i4 - 1)]) / 2;
            if (i5 > 0)
              j += i5;
          }
          this.yReached[i4] = (this.yReached[(i4 - 1)] + this.pageH[(i4 - 1)] + m + j);
        }
        else
        {
          j = (this.pageH[(i4 - 1)] - this.pageH[i4]) / 2;
          this.yReached[i4] = (this.yReached[(i4 - 1)] + j);
        }
        if ((i4 & 0x1) == 0)
          this.xReached[i4] += n - this.pageW[i4];
        else
          this.xReached[i4] = (this.xReached[(i4 - 1)] + this.pageW[(i4 - 1)] + m);
      }
      else if (this.displayView == 2)
      {
        this.yReached[i4] = (this.yReached[(i4 - 1)] + this.pageH[(i4 - 1)] + m);
        if ((i4 < 2) || (((this.pageData.getRotation(i4) != 270) && (this.pageData.getRotation(i4) != 90)) || ((this.pageData.getRotation(i4 - 1) == 270) && (this.pageData.getRotation(i4 - 1) == 90) && (((this.pageData.getRotation(i4 - 1) != 270) && (this.pageData.getRotation(i4 - 1) != 90)) || ((this.pageData.getRotation(i4) == 270) && (this.pageData.getRotation(i4) == 90))))))
        {
          this.pageOffsetW[i4] = (this.pageW[i4] - this.pageW[(i4 - 1)] + this.pageOffsetW[(i4 - 1)]);
          this.pageOffsetH[i4] = (this.pageH[i4] - this.pageH[(i4 - 1)] + this.pageOffsetH[(i4 - 1)]);
        }
      }
      else if (this.displayView == 3)
      {
        if ((i4 & 0x1) == 1)
        {
          this.xReached[i4] = (this.xReached[(i4 - 1)] + this.pageW[(i4 - 1)] + m);
          if (this.pageH[i4] < this.pageH[(i4 - 1)])
            this.yReached[i4] += (this.pageH[(i4 - 1)] - this.pageH[i4]) / 2;
        }
        else
        {
          this.xReached[i4] = 0;
          if ((i4 < i) && (this.pageH[i4] < this.pageH[(i4 + 1)]))
            this.yReached[i4] += (this.pageH[(i4 + 1)] - this.pageH[i4]) / 2;
        }
      }
    }
  }

  public void dispose()
  {
    this.currentOffset = null;
    this.pageH = null;
    this.pageW = null;
    this.isRotated = null;
  }

  public void initRenderer(Map paramMap, Graphics2D paramGraphics2D)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Rectangle getCursorBoxOnScreen()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public double getIndent()
  {
    return this.indent;
  }

  public void forceRedraw()
  {
    this.lastFormPage = -1;
    this.lastEnd = -1;
    this.lastStart = -1;
  }

  public void setPageRotation(int paramInt)
  {
    if (this.displayView != 1)
      paramInt = 0;
    this.pageUsedForTransform = this.pageNumber;
    if ((this.displayView != 1) && (this.displayView != 3))
      this.displayScalingDbl = ScalingFactory.getScalingForImage(1, 0, this.scaling, this.pageData);
    else
      this.displayScalingDbl = ScalingFactory.getScalingForImage(this.pageNumber, paramInt, this.scaling, this.pageData);
    int i = this.options.getInsetW();
    int j = this.options.getInsetH();
    if (paramInt == 90)
    {
      this.displayScalingDbl[4] += i / this.scaling * this.displayScalingDbl[1];
      this.displayScalingDbl[5] += j / this.scaling * this.displayScalingDbl[2];
    }
    else if (paramInt == 270)
    {
      this.displayScalingDbl[4] += -i / this.scaling * this.displayScalingDbl[1];
      this.displayScalingDbl[5] += -j / this.scaling * this.displayScalingDbl[2];
    }
    else if (paramInt == 180)
    {
      this.displayScalingDbl[4] += -i / this.scaling * this.displayScalingDbl[0];
      this.displayScalingDbl[5] += j / this.scaling * this.displayScalingDbl[3];
    }
    else
    {
      this.displayScalingDbl[4] += i / this.scaling * this.displayScalingDbl[0];
      this.displayScalingDbl[5] += -j / this.scaling * this.displayScalingDbl[3];
    }
    refreshDisplay();
  }

  public void resetMultiPageForms(int paramInt)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void resetViewableArea()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void paintPage(Graphics2D paramGraphics2D, AcroRenderer paramAcroRenderer, TextLines paramTextLines)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void updateCursorBoxOnScreen(Rectangle paramRectangle, Color paramColor, int paramInt1, int paramInt2, int paramInt3)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void drawCursor(Graphics paramGraphics, float paramFloat)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public AffineTransform setViewableArea(Rectangle paramRectangle)
    throws PdfException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void drawFacing(Rectangle paramRectangle)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void paintPage(Pane paramPane, AcroRenderer paramAcroRenderer, TextLines paramTextLines)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.GUIDisplay
 * JD-Core Version:    0.6.2
 */
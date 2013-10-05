package org.jpedal.display.swing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.RepaintManager;
import javax.swing.border.Border;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.DisplayOffsets;
import org.jpedal.display.GUIDisplay;
import org.jpedal.display.PageOffsets;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.SwingDisplay;
import org.jpedal.text.TextLines;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.ScalingFactory;

public class SingleDisplay extends GUIDisplay
{
  private AffineTransform displayScaling;
  public Rectangle viewableArea = null;
  private Rectangle cursorBoxOnScreen = null;
  private Rectangle lastCursorBoxOnScreen = null;
  private Color outlineColor;
  private AffineTransform cursorAf;
  private AffineTransform viewScaling = null;
  BufferedImage[] facingDragCachedImages = new BufferedImage[4];
  BufferedImage facingDragTempLeftImg;
  BufferedImage facingDragTempRightImg;
  int facingDragTempLeftNo;
  int facingDragTempRightNo;
  AffineTransform rawAf;
  Shape rawClip;
  protected int volatileWidth;
  protected int volatileHeight;
  Map pagesDrawn = new HashMap();
  boolean screenNeedsRedrawing;
  PdfDecoder pdf;
  float oldVolatileWidth = -1.0F;
  float oldVolatileHeight = -1.0F;
  Map accleratedPagesAlreadyDrawn = new HashMap();
  Graphics2D g2;
  Map areas;
  private int lastAreasPainted = -1;
  int rx = 0;
  int ry = 0;
  int rw = 0;
  int rh = 0;
  AffineTransform current2 = null;
  Shape currentClip = null;
  VolatileImage backBuffer = null;

  public SingleDisplay(int paramInt, DynamicVectorRenderer paramDynamicVectorRenderer, PdfDecoder paramPdfDecoder, DecoderOptions paramDecoderOptions)
  {
    if (paramInt < 1)
      paramInt = 1;
    this.pageNumber = paramInt;
    this.currentDisplay = paramDynamicVectorRenderer;
    this.pdf = paramPdfDecoder;
    this.options = paramDecoderOptions;
    this.displayOffsets = paramPdfDecoder.getDisplayOffsets();
    this.pageData = paramPdfDecoder.getPdfPageData();
  }

  public SingleDisplay(PdfDecoder paramPdfDecoder, DecoderOptions paramDecoderOptions)
  {
    this.pdf = paramPdfDecoder;
    this.options = paramDecoderOptions;
    this.displayOffsets = paramPdfDecoder.getDisplayOffsets();
    this.pageData = paramPdfDecoder.getPdfPageData();
  }

  public void dispose()
  {
    super.dispose();
    if (this.backBuffer != null)
      this.backBuffer.flush();
    this.areas = null;
    this.backBuffer = null;
    this.accleratedPagesAlreadyDrawn = null;
  }

  protected void createBackBuffer()
  {
    if (this.backBuffer != null)
    {
      this.backBuffer.flush();
      this.backBuffer = null;
    }
    int i = 0;
    int j = 0;
    if (this.displayView == 1)
    {
      if ((this.displayRotation == 90) || (this.displayRotation == 270))
      {
        i = this.volatileHeight;
        j = this.volatileWidth;
      }
      else
      {
        i = this.volatileWidth;
        j = this.volatileHeight;
      }
    }
    else if (this.currentOffset != null)
    {
      int k = 0;
      if ((this.displayView == 3) && (this.pageW != null))
      {
        if (this.separateCover)
        {
          m = this.pageNumber;
          if ((m & 0x1) == 1)
            m--;
          n = m + 1;
        }
        else
        {
          m = this.pageNumber;
          if ((m & 0x1) == 0)
            m--;
          n = m + 1;
        }
        k = this.pageH[m];
        if ((n < this.pageH.length) && (k < this.pageH[n]))
          k = this.pageH[n];
      }
      int m = this.currentOffset.gaps;
      int n = this.currentOffset.doubleGaps;
      switch (this.displayView)
      {
      case 3:
        int i1;
        if ((this.displayRotation + this.pageData.getRotation(this.pageNumber)) % 180 == 90)
          i1 = this.pageData.getCropBoxHeight(this.pageNumber);
        else
          i1 = this.pageData.getCropBoxWidth(this.pageNumber);
        int i3 = this.pageData.getPageCount();
        int i2;
        if ((this.pageNumber + 1 > i3) || ((this.pageNumber == 1) && (i3 != 2)))
          i2 = i1;
        else if ((this.displayRotation + this.pageData.getRotation(this.pageNumber + 1)) % 180 == 90)
          i2 = this.pageData.getCropBoxHeight(this.pageNumber + 1);
        else
          i2 = this.pageData.getCropBoxWidth(this.pageNumber + 1);
        int i4 = i1 + i2;
        i = (int)(i4 * this.scaling) + 10;
        j = k;
        break;
      case 2:
        if (((this.displayRotation == 90 ? 1 : 0) | (this.displayRotation == 270 ? 1 : 0)) != 0)
        {
          i = (int)(this.currentOffset.biggestHeight * this.scaling);
          j = (int)(this.currentOffset.totalSingleWidth * this.scaling) + m + this.insetH;
        }
        else
        {
          i = (int)(this.currentOffset.biggestWidth * this.scaling);
          j = (int)(this.currentOffset.totalSingleHeight * this.scaling) + m + this.insetH;
        }
        break;
      case 4:
        if (((this.displayRotation == 90 ? 1 : 0) | (this.displayRotation == 270 ? 1 : 0)) != 0)
        {
          i = (int)(this.currentOffset.doublePageHeight * this.scaling) + this.insetW * 2 + n;
          j = (int)(this.currentOffset.totalDoubleWidth * this.scaling) + n + this.insetH;
        }
        else
        {
          i = (int)(this.currentOffset.doublePageWidth * this.scaling) + this.insetW * 2;
          j = (int)(this.currentOffset.totalDoubleHeight * this.scaling) + n + this.insetH;
        }
        break;
      }
    }
    try
    {
      if (j > 15000)
      {
        this.volatileHeight = 0;
        j = 0;
        this.overRideAcceleration = true;
      }
      if ((i > 0) && (j > 0))
      {
        this.backBuffer = this.pdf.createVolatileImage(i, j);
        this.oldVolatileWidth = this.volatileWidth;
        this.oldVolatileHeight = this.volatileHeight;
        Graphics2D localGraphics2D = (Graphics2D)this.backBuffer.getGraphics();
        localGraphics2D.setPaint(this.pdf.getBackground());
        localGraphics2D.fillRect(0, 0, i, j);
      }
    }
    catch (Error localError)
    {
      this.overRideAcceleration = true;
      this.backBuffer = null;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " is display mode");
    }
    this.currentDisplay.setOptimsePainting(true);
  }

  protected boolean isAccelerated()
  {
    return (this.useAcceleration) && (!this.overRideAcceleration);
  }

  protected boolean testAcceleratedRendering()
  {
    boolean bool = false;
    if ((this.oldScaling != this.scaling) || (this.oldRotation != this.displayRotation) || (this.oldVolatileWidth != this.volatileWidth) || (this.oldVolatileHeight != this.volatileHeight))
    {
      this.backBuffer = null;
      this.overRideAcceleration = false;
    }
    if ((!this.overRideAcceleration) && (this.backBuffer == null))
    {
      createBackBuffer();
      this.accleratedPagesAlreadyDrawn.clear();
    }
    if (this.backBuffer != null)
      do
      {
        int i = 2;
        if (this.backBuffer != null)
          i = this.backBuffer.validate(this.pdf.getGraphicsConfiguration());
        if ((i != 1) && (i == 2) && (!this.overRideAcceleration))
          createBackBuffer();
        if (this.backBuffer != null)
          bool = true;
      }
      while ((this.backBuffer == null) || (this.backBuffer.contentsLost()));
    return bool;
  }

  public Rectangle getDisplayedRectangle()
  {
    Rectangle localRectangle = this.pdf.getVisibleRect();
    this.rx = localRectangle.x;
    this.ry = localRectangle.y;
    this.rw = localRectangle.width;
    this.rh = localRectangle.height;
    if (((this.pdf.isShowing()) && (localRectangle.width == 0)) || (localRectangle.height == 0))
    {
      this.rx = 0;
      this.ry = 0;
      this.rw = this.pageData.getScaledCropBoxWidth(this.pageNumber);
      this.rh = this.pageData.getScaledCropBoxHeight(this.pageNumber);
      if (this.pageData.getRotation(this.pageNumber) % 180 != 0)
      {
        this.rh = this.pageData.getScaledCropBoxWidth(this.pageNumber);
        this.rw = this.pageData.getScaledCropBoxHeight(this.pageNumber);
      }
    }
    return localRectangle;
  }

  public void drawBorder()
  {
    if (CURRENT_BORDER_STYLE == 1)
    {
      Border localBorder = this.pdf.getPDFBorder();
      if ((this.crw > 0) && (this.crh > 0) && (localBorder != null))
        localBorder.paintBorder(this.pdf, this.g2, this.crx - localBorder.getBorderInsets(this.pdf).left, this.cry - localBorder.getBorderInsets(this.pdf).bottom, this.crw + localBorder.getBorderInsets(this.pdf).left + localBorder.getBorderInsets(this.pdf).right, this.crh + localBorder.getBorderInsets(this.pdf).bottom + localBorder.getBorderInsets(this.pdf).top);
    }
  }

  protected void setDisplacementOnG2(Graphics2D paramGraphics2D)
  {
    float f1 = this.crx / this.scaling;
    float f2 = this.cry / this.scaling;
    if ((this.displayRotation == 0) || (this.displayView != 1))
      paramGraphics2D.translate(-f1, f2);
    else if (this.displayRotation == 90)
      paramGraphics2D.translate(-f2, -f1);
    else if (this.displayRotation == 180)
      paramGraphics2D.translate(f1, -f2);
    else if (this.displayRotation == 270)
      paramGraphics2D.translate(f2, f1);
  }

  public void refreshDisplay()
  {
    this.screenNeedsRedrawing = true;
    this.accleratedPagesAlreadyDrawn.clear();
    this.overRideAcceleration = false;
  }

  public void init(float paramFloat, int paramInt1, int paramInt2, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean)
  {
    this.pageData = this.pdf.getPdfPageData();
    super.init(paramFloat, paramInt1, paramInt2, paramDynamicVectorRenderer, paramBoolean);
    this.volatileWidth = this.pageData.getScaledCropBoxWidth(paramInt2);
    this.volatileHeight = this.pageData.getScaledCropBoxHeight(paramInt2);
    if ((this.displayView == 3) && ((paramBoolean) || (this.lastDisplayRotation != paramInt1) || (this.lastScaling != paramFloat)))
    {
      for (int i = 0; i < 4; i++)
        this.facingDragCachedImages[i] = null;
      this.pdf.setUserOffsets(0, 0, 995);
    }
  }

  public void setPageOffsets(int paramInt)
  {
    int i = this.pageData.getPageCount();
    AcroRenderer localAcroRenderer = this.pdf.getFormRenderer();
    if (this.displayView == 1)
    {
      this.xReached = null;
      this.yReached = null;
      if (localAcroRenderer != null)
        localAcroRenderer.getCompData().setPageDisplacements(this.xReached, this.yReached);
      return;
    }
    super.setPageOffsets(paramInt);
    if (localAcroRenderer != null)
      localAcroRenderer.getCompData().setPageDisplacements(this.xReached, this.yReached);
  }

  public void resetToDefaultClip()
  {
    if (this.current2 != null)
      this.g2.setTransform(this.current2);
    if (this.currentClip != null)
      this.g2.setClip(this.currentClip);
  }

  public void initRenderer(Map paramMap, Graphics2D paramGraphics2D)
  {
    this.rawAf = paramGraphics2D.getTransform();
    this.rawClip = paramGraphics2D.getClip();
    if (paramMap != null)
      this.lastAreasPainted = -2;
    this.areas = paramMap;
    this.g2 = paramGraphics2D;
    this.pagesDrawn.clear();
    setPageSize(this.pageNumber, this.scaling);
    paramGraphics2D.translate(this.insetW - this.crx, this.insetH - this.cry);
    this.current2 = paramGraphics2D.getTransform();
    this.currentClip = paramGraphics2D.getClip();
    paramGraphics2D.clip(new Rectangle(this.crx, this.cry, this.crw, this.crh));
  }

  protected Rectangle calcVisibleArea(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    getDisplayedRectangle();
    if ((this.displayRotation != 270) && (this.displayRotation != 180) && (this.rx > this.insetW))
      i = (int)((this.rx - this.insetW) / this.scaling);
    int m = (int)((this.rw + this.insetW) / this.scaling);
    int k = paramInt1;
    if ((this.displayRotation == 0) || (this.displayView != 1))
    {
      i = 0;
      if (this.rx >= this.insetW)
        i = (int)(this.rx / this.scaling) - (int)(this.insetW / this.scaling);
      k = (int)((this.rh + this.insetH) / this.scaling);
      m = (int)((this.rw - this.insetW) / this.scaling) + (int)(this.insetW / this.scaling);
      j = (int)(paramInt1 - (this.ry + this.rh) / this.scaling);
    }
    else if (this.displayRotation == 90)
    {
      j = (int)((this.rx - this.insetW) / this.scaling);
      k = (int)((this.rw + this.insetW) / this.scaling);
      if (this.ry > this.insetW)
        i = (int)((this.ry - this.insetW) / this.scaling);
      else
        i = 0;
      m = (int)(this.rh / this.scaling);
    }
    else if (this.displayRotation == 270)
    {
      m = (int)((this.rh + this.insetW) / this.scaling);
      i = paramInt2 - (int)((this.ry + this.rh) / this.scaling);
      k = (int)((this.rw + this.insetH) / this.scaling);
      j = paramInt1 - (int)((this.rx + this.rw) / this.scaling);
      if (i < this.insetH)
      {
        i = 0;
        m += this.insetH;
      }
      if (j < this.insetW)
      {
        j = 0;
        k += this.insetW;
      }
    }
    else if (this.displayRotation == 180)
    {
      k = (int)((this.rh + this.insetH) / this.scaling) + this.insetH;
      j = (int)((this.ry - this.insetH) / this.scaling);
      m = (int)((this.rw + this.insetW) / this.scaling);
      i = paramInt2 - (int)((this.rx - this.insetW) / this.scaling);
      i -= m;
      if ((i < 0) || (i < this.insetH))
      {
        i = 0;
        m += this.insetH;
      }
      if (j < this.insetW)
      {
        j = 0;
        k += this.insetW;
      }
    }
    Rectangle localRectangle;
    if ((isAccelerated()) || (this.scaling >= 2.0F))
    {
      int i1 = this.pageData.getScaledCropBoxX(this.pageNumber);
      int i2 = this.pageData.getScaledCropBoxY(this.pageNumber);
      int n = 2;
      if ((i1 != 0) || (i2 != 0))
        localRectangle = new Rectangle(i + (int)(i1 / this.scaling), j + (int)(i2 / this.scaling), m, k);
      else
        localRectangle = new Rectangle(i, j, m + (n + n), k + (n + n));
    }
    else
    {
      localRectangle = null;
    }
    return localRectangle;
  }

  Rectangle drawPage(AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, int paramInt)
  {
    Rectangle localRectangle1 = null;
    if ((paramAffineTransform2 != null) && (this.currentDisplay != null))
    {
      if ((this.scaling < 2.0F) && (this.oldScaling > 2.0F))
        this.useAcceleration = true;
      if ((this.useAcceleration) || (this.areas != null))
      {
        i = -1;
        if (this.areas != null)
          i = this.areas.size();
        if (((this.lastAreasPainted != -2) || (i != -1)) && (i != this.lastAreasPainted))
        {
          this.screenNeedsRedrawing = true;
          this.lastAreasPainted = i;
        }
      }
      int i = 0;
      boolean bool;
      if ((this.useAcceleration) && (!this.overRideAcceleration) && (this.scaling < 2.0F))
        bool = testAcceleratedRendering();
      else
        this.useAcceleration = false;
      Rectangle localRectangle2 = null;
      if (!isAccelerated())
        localRectangle2 = calcVisibleArea((int)(this.topH / this.scaling), (int)(this.topW / this.scaling));
      this.currentDisplay.setScalingValues(this.cropX, this.cropH + this.cropY, this.scaling);
      this.g2.transform(paramAffineTransform2);
      if (bool)
      {
        Graphics2D localGraphics2D = (Graphics2D)this.backBuffer.getGraphics();
        if (this.screenNeedsRedrawing)
        {
          this.currentDisplay.setG2(localGraphics2D);
          this.currentDisplay.paintBackground(new Rectangle(0, 0, this.backBuffer.getWidth(), this.backBuffer.getHeight()));
          this.currentDisplay.setOptimsePainting(true);
          localGraphics2D.setTransform(paramAffineTransform2);
          setDisplacementOnG2(localGraphics2D);
          this.currentDisplay.setG2(localGraphics2D);
          if (this.areas != null)
            localRectangle1 = this.currentDisplay.paint((Rectangle[])this.areas.get(Integer.valueOf(this.pageNumber)), paramAffineTransform1, localRectangle2);
          else
            localRectangle1 = this.currentDisplay.paint(null, paramAffineTransform1, localRectangle2);
          this.screenNeedsRedrawing = false;
        }
        localGraphics2D.dispose();
        if (this.backBuffer != null)
        {
          AffineTransform localAffineTransform = this.g2.getTransform();
          this.g2.setTransform(this.rawAf);
          this.g2.drawImage(this.backBuffer, this.insetW, this.insetH, this.pdf);
          this.g2.setTransform(localAffineTransform);
        }
      }
      else
      {
        this.currentDisplay.setOptimsePainting(false);
        this.currentDisplay.setG2(this.g2);
        if (this.areas != null)
          localRectangle1 = this.currentDisplay.paint((Rectangle[])this.areas.get(Integer.valueOf(this.pageNumber)), paramAffineTransform1, localRectangle2);
        else
          localRectangle1 = this.currentDisplay.paint(null, paramAffineTransform1, localRectangle2);
      }
      this.oldScaling = this.scaling;
      this.oldRotation = this.displayRotation;
    }
    return localRectangle1;
  }

  public void paintPage(Graphics2D paramGraphics2D, AcroRenderer paramAcroRenderer, TextLines paramTextLines)
  {
    if (this.displayScaling == null)
      return;
    int i = this.options.getInsetW();
    int j = this.options.getInsetH();
    int k = this.pageData.getPageCount();
    paramGraphics2D.translate(this.displayOffsets.getUserOffsetX(), this.displayOffsets.getUserOffsetY());
    int m = this.pageNumber;
    int n = this.pageNumber;
    if (this.displayView != 1)
    {
      m = getStartPage();
      n = getEndPage();
      if ((m == 0) || (n == 0) || (this.lastEnd != n) || (this.lastStart != m))
        this.lastFormPage = -1;
      this.lastEnd = n;
      this.lastStart = m;
    }
    if ((this.lastFormPage != this.pageNumber) && (paramAcroRenderer != null))
    {
      paramAcroRenderer.displayComponentsOnscreen(m, n);
      if (paramAcroRenderer.getCompData().hasformsOnPageDecoded(this.pageNumber))
        this.lastFormPage = this.pageNumber;
    }
    if (this.pdf.getPageAlignment() == 2)
    {
      double d = this.pdf.getBounds().getWidth();
      int i1 = this.pdf.getPDFWidth();
      if (this.displayView != 1)
        i1 = getPageSize(this.displayView)[0];
      if (this.displayView == 3)
      {
        int i2 = this.pageNumber;
        if ((getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i2 & 0x1) == 1))
          i2--;
        else if ((!getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i2 & 0x1) == 0))
          i2--;
        int i3;
        if ((this.displayRotation + this.pageData.getRotation(i2)) % 180 == 90)
          i3 = this.pageData.getCropBoxHeight(i2);
        else
          i3 = this.pageData.getCropBoxWidth(i2);
        int i4;
        if (i2 + 1 > k)
          i4 = i3;
        else if ((this.displayRotation + this.pageData.getRotation(i2 + 1)) % 180 == 90)
          i4 = this.pageData.getCropBoxHeight(i2 + 1);
        else
          i4 = this.pageData.getCropBoxWidth(i2 + 1);
        int i5 = 0;
        if ((!getBoolean(Display.BoolValue.TURNOVER_ON)) || (this.pageData.hasMultipleSizes()) || (k == 2))
          i5 = 5;
        this.indent = ((d - (i3 + i4) * this.scaling) / 2.0D - i5 - i);
      }
      else
      {
        this.indent = ((d - i1) / 2.0D);
      }
      if (this.displayView == 1)
      {
        this.lastIndent = ((int)this.indent);
      }
      else if ((this.displayView == 2) && (this.lastIndent != -1))
      {
        this.indent = this.lastIndent;
        this.lastIndent = -1;
      }
      else
      {
        this.lastIndent = -1;
      }
      paramGraphics2D.translate(this.indent, 0.0D);
      if ((paramAcroRenderer != null) && (this.currentOffset != null))
      {
        paramAcroRenderer.getCompData().setPageValues(this.scaling, this.displayRotation, (int)this.indent, this.displayOffsets.getUserOffsetX(), this.displayOffsets.getUserOffsetY(), this.displayView, this.currentOffset.widestPageNR, this.currentOffset.widestPageR);
        paramAcroRenderer.getCompData().resetScaledLocation(this.scaling, this.displayRotation, (int)this.indent);
      }
    }
    else if ((paramAcroRenderer != null) && (this.currentOffset != null))
    {
      this.lastIndent = -1;
      paramAcroRenderer.getCompData().setPageValues(this.scaling, this.displayRotation, (int)this.indent, this.displayOffsets.getUserOffsetX(), this.displayOffsets.getUserOffsetY(), this.displayView, this.currentOffset.widestPageNR, this.currentOffset.widestPageR);
      paramAcroRenderer.getCompData().resetScaledLocation(this.scaling, this.displayRotation, (int)this.indent);
    }
    initRenderer(paramTextLines.areas, paramGraphics2D);
    drawPage(this.viewScaling, this.displayScaling, this.pageUsedForTransform);
    if (this.displayView == 1)
    {
      if (this.viewScaling != null)
        paramGraphics2D.transform(this.viewScaling);
      if (this.cursorBoxOnScreen != null)
        this.cursorAf = paramGraphics2D.getTransform();
      resetToDefaultClip();
    }
    if (this.displayView == 1)
      drawHighlightsForImage(paramGraphics2D, getHighlightedImage(), this.scaling, this.displayRotation, i, j, this.pageData.getMediaBoxWidth(this.pageNumber), this.pageData.getMediaBoxHeight(this.pageNumber));
    else
      setHighlightedImage(null);
  }

  public void setCursorBoxOnScreen(Rectangle paramRectangle, boolean paramBoolean)
  {
    this.cursorBoxOnScreen = paramRectangle;
    AcroRenderer localAcroRenderer = this.pdf.getFormRenderer();
    if ((!paramBoolean) && (localAcroRenderer != null))
    {
      localAcroRenderer.removeDisplayComponentsFromScreen();
      this.lastFormPage = -1;
    }
  }

  public Rectangle getCursorBoxOnScreen()
  {
    return this.cursorBoxOnScreen;
  }

  public void drawCursor(Graphics paramGraphics, float paramFloat)
  {
    if (this.cursorBoxOnScreen != null)
    {
      Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
      AffineTransform localAffineTransform = localGraphics2D.getTransform();
      if (this.cursorAf != null)
      {
        localGraphics2D.setTransform(this.cursorAf);
        Shape localShape = localGraphics2D.getClip();
        if ((this.options.getPageAlignment() == 2) && (localShape != null))
          localGraphics2D.setClip(null);
        if ((this.cursorBoxOnScreen != null) && (DecoderOptions.showMouseBox))
          paintRectangle(localGraphics2D, this.cursorBoxOnScreen, paramFloat, this.outlineColor);
        this.lastCursorBoxOnScreen = this.cursorBoxOnScreen;
        localGraphics2D.setClip(localShape);
        localGraphics2D.setTransform(localAffineTransform);
      }
    }
  }

  public final void updateCursorBoxOnScreen(Rectangle paramRectangle, Color paramColor, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((this.displayView != 1) && (getPageSize(this.displayView)[0] == 0) && (getPageSize(this.displayView)[1] == 0))
      return;
    int k;
    int m;
    if (paramRectangle != null)
    {
      i = paramRectangle.x;
      j = paramRectangle.y;
      k = paramRectangle.width;
      m = paramRectangle.height;
      int n = this.pageData.getCropBoxX(paramInt1);
      int i1 = this.pageData.getCropBoxY(paramInt1);
      int i2 = this.pageData.getCropBoxWidth(paramInt1);
      int i3 = this.pageData.getCropBoxHeight(paramInt1);
      if ((j > 0) && (j < i1))
        j += i1;
      int i4;
      if (i < n)
      {
        i4 = n - i;
        k -= i4;
        i = n;
      }
      if (j < i1)
      {
        i4 = i1 - j;
        m -= i4;
        j += i4;
      }
      if (i + k > i2 + n)
        k = n + i2 - i;
      if (j + m > i1 + i3)
        m = i1 + i3 - j;
      this.cursorBoxOnScreen = new Rectangle(i, j, k, m);
    }
    else
    {
      this.cursorBoxOnScreen = null;
    }
    this.outlineColor = paramColor;
    int i = 30;
    int j = 0;
    if (this.pdf.getPageAlignment() == 2)
    {
      k = this.pdf.getBounds().width;
      m = this.pdf.getPDFWidth();
      if (this.displayView != 1)
        m = getPageSize(this.displayView)[0];
      j = (k - m) / 2;
    }
    RepaintManager localRepaintManager = RepaintManager.currentManager(this.pdf);
    if (this.lastCursorBoxOnScreen != null)
    {
      if ((this.pdf.getDisplayRotation() == 0) || (this.pdf.getDisplayRotation() == 180))
        localRepaintManager.addDirtyRegion(this.pdf, this.insetW + j, this.insetH, paramInt2 + 5, paramInt3);
      else
        localRepaintManager.addDirtyRegion(this.pdf, this.insetH + j, this.insetW, paramInt3 + 5, paramInt2);
      this.lastCursorBoxOnScreen = null;
    }
    if (this.cursorBoxOnScreen != null)
      localRepaintManager.addDirtyRegion(this.pdf, (int)(this.cursorBoxOnScreen.x * this.scaling) - i, (int)((this.pageData.getMediaBoxHeight(paramInt1) - this.cursorBoxOnScreen.y - this.cursorBoxOnScreen.height) * this.scaling) - i, (int)(this.cursorBoxOnScreen.width * this.scaling) + i + i, (int)(this.cursorBoxOnScreen.height * this.scaling) + i + i);
    if (this.viewScaling != null)
      localRepaintManager.markCompletelyDirty(this.pdf);
    this.pdf.repaint();
  }

  public void setPageRotation(int paramInt)
  {
    super.setPageRotation(paramInt);
    this.displayScaling = new AffineTransform(this.displayScalingDbl);
    if (this.viewableArea != null)
    {
      this.viewScaling = new AffineTransform();
      double d1 = this.viewableArea.width / this.pageData.getCropBoxWidth(this.pageNumber);
      double d2 = this.viewableArea.height / this.pageData.getCropBoxHeight(this.pageNumber);
      double d3 = d1;
      if (d2 < d1)
        d3 = d2;
      double d4 = this.viewableArea.x;
      double d5 = this.viewableArea.y + (this.viewableArea.height - this.pageData.getCropBoxHeight(this.pageNumber) * d3);
      this.viewScaling.translate(d4, d5);
      this.viewScaling.scale(d3, d3);
    }
    else
    {
      this.viewScaling = null;
    }
  }

  public void resetViewableArea()
  {
    if (this.viewableArea != null)
    {
      this.viewableArea = null;
      setPageRotation(this.pdf.getDisplayRotation());
      this.pdf.repaint();
    }
  }

  public void resetMultiPageForms(int paramInt)
  {
    AcroRenderer localAcroRenderer = this.pdf.getFormRenderer();
    localAcroRenderer.removeDisplayComponentsFromScreen();
    this.lastFormPage = -1;
    localAcroRenderer.displayComponentsOnscreen(paramInt, paramInt);
    if (localAcroRenderer.getCompData().hasformsOnPageDecoded(paramInt))
      this.lastFormPage = paramInt;
  }

  private static void drawHighlightsForImage(Graphics2D paramGraphics2D, int[] paramArrayOfInt, float paramFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramArrayOfInt != null)
    {
      int i = (int)(paramArrayOfInt[0] * paramFloat);
      int j = (int)(paramArrayOfInt[1] * paramFloat);
      int k = (int)(paramArrayOfInt[2] * paramFloat);
      int m = (int)(paramArrayOfInt[3] * paramFloat);
      if (k < 0)
      {
        k = -k;
        i -= k;
      }
      if (m < 0)
      {
        m = -m;
        j -= m;
      }
      int i2 = k;
      int i3 = m;
      int n;
      int i1;
      if (paramInt1 == 90)
      {
        i3 = k;
        i2 = m;
        n = paramInt2 + j;
        i1 = paramInt3 + i;
      }
      else if (paramInt1 == 180)
      {
        n = (int)(paramInt4 * paramFloat - i - k) + paramInt2;
        i1 = paramInt3 + j;
      }
      else if (paramInt1 == 270)
      {
        i3 = k;
        i2 = m;
        i1 = (int)(paramInt4 * paramFloat - i - k) + paramInt2;
        n = (int)(paramInt5 * paramFloat - j - m) + paramInt3;
      }
      else
      {
        n = paramInt2 + i;
        i1 = (int)(paramInt5 * paramFloat - j - m) + paramInt3;
      }
      Color localColor = paramGraphics2D.getColor();
      Composite localComposite = paramGraphics2D.getComposite();
      Stroke localStroke = paramGraphics2D.getStroke();
      paramGraphics2D.setStroke(new BasicStroke(2.0F));
      paramGraphics2D.setComposite(AlphaComposite.getInstance(3, DecoderOptions.highlightComposite));
      if (SwingDisplay.invertHighlight)
      {
        paramGraphics2D.setColor(Color.WHITE);
        paramGraphics2D.setXORMode(Color.BLACK);
      }
      else
      {
        paramGraphics2D.setColor(DecoderOptions.highlightColor);
        paramGraphics2D.drawRect(n, i1, i2, i3);
      }
      paramGraphics2D.fillRect(n, i1, i2, i3);
      paramGraphics2D.setColor(localColor);
      paramGraphics2D.setComposite(localComposite);
      paramGraphics2D.setStroke(localStroke);
    }
  }

  private static void paintRectangle(Graphics2D paramGraphics2D, Rectangle paramRectangle, float paramFloat, Color paramColor)
  {
    Stroke localStroke = paramGraphics2D.getStroke();
    BasicStroke localBasicStroke;
    if (paramFloat < 0.0F)
      localBasicStroke = new BasicStroke(1.0F / -paramFloat);
    else
      localBasicStroke = new BasicStroke(1.0F / paramFloat);
    paramGraphics2D.setStroke(localBasicStroke);
    paramGraphics2D.setColor(paramColor);
    paramGraphics2D.draw(paramRectangle);
    paramGraphics2D.setStroke(localStroke);
  }

  public static BufferedImage getSelectedRectangleOnscreen(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, int paramInt, PdfPageData paramPdfPageData, AcroRenderer paramAcroRenderer, DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    int i = paramPdfPageData.getMediaBoxHeight(paramInt);
    int j = paramPdfPageData.getCropBoxWidth(paramInt);
    int k = paramPdfPageData.getCropBoxHeight(paramInt);
    int m = paramPdfPageData.getCropBoxX(paramInt);
    int n = paramPdfPageData.getCropBoxY(paramInt);
    if (paramFloat4 < n)
      paramFloat4 = n;
    if (paramFloat1 < m)
      paramFloat1 = m;
    if (paramFloat2 > k + n)
      paramFloat2 = k + n;
    if (paramFloat3 > m + j)
      paramFloat3 = m + j;
    if ((paramFloat3 - paramFloat1 < 1.0F) || (paramFloat2 - paramFloat4 < 1.0F))
      return null;
    float f1 = paramFloat5 / 100.0F;
    float f2 = paramFloat3 - paramFloat1;
    float f3 = paramFloat2 - paramFloat4;
    BufferedImage localBufferedImage = new BufferedImage((int)(f2 * f1), (int)(f3 * f1), 1);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    if (n > 0)
      n = i - k - n;
    AffineTransform localAffineTransform1 = new AffineTransform(ScalingFactory.getScalingForImage(paramInt, 0, f1, paramPdfPageData));
    int i1 = -m;
    int i2 = -n;
    localAffineTransform1.translate(i1, -i2);
    localAffineTransform1.translate(-(paramFloat1 - m), i - paramFloat2 - n);
    AffineTransform localAffineTransform2 = localGraphics2D.getTransform();
    localGraphics2D.transform(localAffineTransform1);
    paramDynamicVectorRenderer.setG2(localGraphics2D);
    paramDynamicVectorRenderer.paintBackground(new Rectangle(m, n, j, k));
    paramDynamicVectorRenderer.setOptimsePainting(true);
    paramDynamicVectorRenderer.paint(null, null, null);
    if ((paramAcroRenderer != null) && (paramAcroRenderer.hasFormsOnPage(paramInt)) && (!paramAcroRenderer.getCompData().formsRasterizedForDisplay()))
      paramAcroRenderer.getCompData().renderFormsOntoG2(localGraphics2D, paramInt, 0, 0, null, null, paramPdfPageData.getMediaBoxHeight(paramInt));
    localGraphics2D.setTransform(localAffineTransform2);
    localGraphics2D.dispose();
    return localBufferedImage;
  }

  public void drawFacing(Rectangle paramRectangle)
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.swing.SingleDisplay
 * JD-Core Version:    0.6.2
 */
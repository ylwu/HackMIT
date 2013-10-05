package org.jpedal.display.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.jpedal.FileAccess;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.GUIModes;
import org.jpedal.display.PageOffsets;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.swing.SwingMouseSelector;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.external.RenderChangeListener;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.SwingDisplay;
import org.jpedal.text.TextLines;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class MultiDisplay extends SingleDisplay
  implements Display
{
  private Thread worker = null;
  private int pageUsedForTransform;
  Map cachedPageViews = new WeakHashMap();
  Map currentPageViews = new HashMap();
  FileAccess fileAccess;
  private boolean debugFacingDrag = false;

  public MultiDisplay(int paramInt1, DynamicVectorRenderer paramDynamicVectorRenderer, int paramInt2, Object paramObject, PdfDecoder paramPdfDecoder, DecoderOptions paramDecoderOptions, FileAccess paramFileAccess)
  {
    super(paramInt1, paramDynamicVectorRenderer, paramPdfDecoder, paramDecoderOptions);
    this.displayView = paramInt2;
    this.fileAccess = paramFileAccess;
    if (paramDynamicVectorRenderer != null)
      this.currentPageViews.put(Integer.valueOf(paramInt1), paramDynamicVectorRenderer);
  }

  public void decodeOtherPages(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 1)
      paramInt1 = 1;
    int i = this.pageNumber;
    this.pageNumber = paramInt1;
    if (!this.isInitialised)
      return;
    if ((this.displayView == 3) && (this.turnoverOn))
    {
      SwingGUI localSwingGUI = (SwingGUI)this.pdf.getExternalHandler(11);
      int j;
      if (this.separateCover)
        j = i / 2 * 2;
      else
        j = i - (1 - (i & 0x1));
      if (localSwingGUI.getDragLeft())
      {
        this.facingDragTempLeftImg = this.facingDragCachedImages[0];
        this.facingDragTempLeftNo = (j - 2);
        this.facingDragTempRightImg = this.facingDragCachedImages[1];
        this.facingDragTempRightNo = (j - 1);
      }
      else
      {
        this.facingDragTempLeftImg = this.facingDragCachedImages[2];
        this.facingDragTempLeftNo = (j + 2);
        this.facingDragTempRightImg = this.facingDragCachedImages[3];
        this.facingDragTempRightNo = (j + 3);
      }
    }
    this.facingDragCachedImages[0] = null;
    this.facingDragCachedImages[1] = null;
    this.facingDragCachedImages[2] = null;
    this.facingDragCachedImages[3] = null;
    setPageOffsets(this.pageNumber);
    calcDisplayedRange();
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
    if ((this.worker == null) || (!this.running))
    {
      this.running = true;
      this.worker = new Thread()
      {
        public void run()
        {
          try
          {
            MultiDisplay.this.decodeOtherPages();
            MultiDisplay.this.running = false;
            if (MultiDisplay.this.customRenderChangeListener != null)
              MultiDisplay.this.customRenderChangeListener.renderingWorkerFinished();
          }
          catch (Exception localException)
          {
            MultiDisplay.this.running = false;
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
          catch (Error localError)
          {
            MultiDisplay.this.running = false;
            if (LogWriter.isOutput())
              LogWriter.writeLog("Error: " + localError.getMessage());
          }
        }
      };
      this.worker.setDaemon(true);
      this.worker.start();
    }
  }

  public void flushPageCaches()
  {
    this.currentPageViews.clear();
    this.cachedPageViews.clear();
  }

  void decodeOtherPages()
  {
    int i = this.pageData.getPageCount();
    this.isGeneratingOtherPages = true;
    int j = this.startViewPage;
    int k = this.startViewPage;
    int m = this.endViewPage + 1;
    int n = 1;
    int i1 = 1;
    if ((this.turnoverOn) && (this.displayView == 3))
    {
      n = k - 2;
      i1 = n + 6;
      if (n < 1)
        n = 1;
      if (i1 > i + 1)
        i1 = i + 1;
    }
    int i2 = i1 - n;
    resetPageCaches(this.startViewPage, this.endViewPage + 1);
    while (this.isGeneratingOtherPages)
    {
      if ((k != this.startViewPage) && (m != this.endViewPage))
      {
        j = this.startViewPage;
        m = this.endViewPage + 1;
        if (j == 0)
          j++;
        k = j;
        if ((this.turnoverOn) && (this.displayView == 3))
        {
          n = k - 2;
          i1 = n + 6;
          if (n < 1)
            n = 1;
          if (i1 > i + 1)
            i1 = i + 1;
          i2 = i1 - n;
        }
        resetPageCaches(k, m);
      }
      if (((this.turnoverOn) && (this.displayView == 3) && (i2 == 0)) || (((!this.turnoverOn) || (this.displayView != 3)) && (j == m)))
        break;
      Object localObject3;
      if ((j > 0) && (j < this.pdf.getPageCount() + 1))
      {
        if (this.currentPageViews.get(Integer.valueOf(j)) == null)
        {
          Object localObject2;
          if ((this.displayView == 2) || (this.displayView == 4))
          {
            localObject1 = (PdfStreamDecoder)this.currentPageViews.get(Integer.valueOf(j));
            if (this.currentDisplay.getType() != 6)
            {
              localObject2 = this.pdf.getFormRenderer();
              if (this.currentOffset != null)
                ((AcroRenderer)localObject2).getCompData().setPageValues(this.pdf.scaling, this.displayRotation, (int)getIndent(), 0, 0, 1, this.currentOffset.widestPageNR, this.currentOffset.widestPageR);
              ((AcroRenderer)localObject2).createDisplayComponentsForPage(j, (PdfStreamDecoder)localObject1);
            }
          }
          localObject1 = this.pdf.getIO().getReferenceforPage(j);
          if (localObject1 != null)
          {
            localObject2 = Integer.valueOf(j);
            localObject3 = this.currentPageViews.get(localObject2);
            if ((localObject3 == null) && (this.isGeneratingOtherPages) && (this.isGeneratingOtherPages))
            {
              this.currentDisplay = getPageView((String)localObject1, j);
              this.currentPageViews.put(localObject2, this.currentDisplay);
            }
          }
        }
        Object localObject1 = new Runnable()
        {
          public void run()
          {
            MultiDisplay.this.pdf.repaint();
          }
        };
        SwingUtilities.invokeLater((Runnable)localObject1);
      }
      if ((this.displayView == 3) && (this.turnoverOn))
      {
        int i3 = this.pageNumber;
        if ((this.separateCover) && ((i3 & 0x1) == 1))
          i3--;
        if ((!this.separateCover) && ((i3 & 0x1) == 0))
          i3--;
        int i4 = j - i3 + 2;
        if ((i4 <= 1) || (i4 >= 4))
        {
          if (i4 > 1)
            i4 -= 2;
          if ((i4 < 4) && (i4 > -1) && (this.facingDragCachedImages[i4] == null))
          {
            localObject3 = new BufferedImage(this.pageW[j], this.pageH[j], 2);
            Graphics2D localGraphics2D = (Graphics2D)((BufferedImage)localObject3).getGraphics();
            localGraphics2D.rotate(this.displayRotation * 3.141592653589793D / 180.0D);
            try
            {
              if (this.displayRotation == 90)
              {
                localGraphics2D.translate(0, -this.pageW[j]);
                localGraphics2D.drawImage(this.pdf.getPageAsImage(j), 0, 0, this.pageH[j] + 1, this.pageW[j] + 1, null);
              }
              else if (this.displayRotation == 180)
              {
                localGraphics2D.translate(-this.pageW[j], -this.pageH[j]);
                localGraphics2D.drawImage(this.pdf.getPageAsImage(j), 0, 0, this.pageW[j] + 1, this.pageH[j] + 1, null);
              }
              else if (this.displayRotation == 270)
              {
                localGraphics2D.translate(-this.pageH[j], 0);
                localGraphics2D.drawImage(this.pdf.getPageAsImage(j), 0, 0, this.pageH[j] + 1, this.pageW[j] + 1, null);
              }
              else
              {
                localGraphics2D.drawImage(this.pdf.getPageAsImage(j), 0, 0, this.pageW[j] + 1, this.pageH[j] + 1, null);
              }
            }
            catch (Exception localException)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException.getMessage());
            }
            this.facingDragCachedImages[i4] = localObject3;
          }
        }
      }
      i2--;
      j++;
      if ((this.turnoverOn) && (this.displayView == 3) && (j == i1))
        j = n;
    }
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        if ((MultiDisplay.this.thumbnails != null) && (MultiDisplay.this.thumbnails.isShownOnscreen()))
          MultiDisplay.this.thumbnails.generateOtherVisibleThumbnails(MultiDisplay.this.pdf.getPageNumber());
      }
    };
    SwingUtilities.invokeLater(local3);
  }

  private DynamicVectorRenderer getPageView(String paramString, int paramInt)
  {
    PageObject localPageObject = new PageObject(paramString);
    PdfObjectReader localPdfObjectReader = this.pdf.getIO();
    AcroRenderer localAcroRenderer = this.pdf.getFormRenderer();
    localPdfObjectReader.readObject(localPageObject);
    forceRedraw();
    PdfObject localPdfObject = localPageObject.getDictionary(2004251818);
    SwingDisplay localSwingDisplay = new SwingDisplay(paramInt, this.pdf.getObjectStore(), false);
    localSwingDisplay.setHiResImageForDisplayMode(this.pdf.isHiResScreenDisplay());
    int i = 0;
    if (SwingMouseSelector.activateMultipageHighlight)
      i = 1;
    PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(localPdfObjectReader, this.pdf.isHiResScreenDisplay(), this.fileAccess.getRes().getPdfLayerList());
    if ((localAcroRenderer != null) && (this.pdf.isForm()))
    {
      localAcroRenderer.getCompData().setPageValues(this.scaling, this.displayRotation, 0, 0, 0, 1, this.currentOffset.widestPageNR, this.currentOffset.widestPageR);
      localAcroRenderer.createDisplayComponentsForPage(paramInt, localPdfStreamDecoder);
    }
    localPdfStreamDecoder.setParameters(true, true, 7, i, this.pdf.getExternalHandler().getMode().equals(GUIModes.JAVAFX));
    localPdfStreamDecoder.setXMLExtraction(this.pdf.isXMLExtraction());
    this.pdf.getExternalHandler().addHandlers(localPdfStreamDecoder);
    localPdfStreamDecoder.setObjectValue(-9, this.fileAccess.getFilename());
    localPdfStreamDecoder.setObjectValue(-8, this.pdf.getObjectStore());
    localPdfStreamDecoder.setObjectValue(-18, this.pageData);
    localPdfStreamDecoder.setIntValue(-10, paramInt);
    localPdfStreamDecoder.setObjectValue(23, localSwingDisplay);
    try
    {
      localSwingDisplay.init(this.pageData.getMediaBoxWidth(paramInt), this.pageData.getMediaBoxHeight(paramInt), this.pageData.getRotation(paramInt), this.options.getPageColor());
      localSwingDisplay.setValue(1, this.options.getPageColor().getRGB());
      if (this.options.getTextColor() != null)
      {
        localSwingDisplay.setValue(2, this.options.getTextColor().getRGB());
        if (this.options.getChangeTextAndLine())
          localSwingDisplay.setValue(3, 1);
        else
          localSwingDisplay.setValue(3, 0);
      }
      this.fileAccess.getRes().setupResources(localPdfStreamDecoder, false, localPdfObject, paramInt, localPdfObjectReader);
      localPdfStreamDecoder.decodePageContent(localPageObject);
      TextLines localTextLines = this.pdf.getTextLines();
      if (localTextLines != null)
      {
        Vector_Rectangle localVector_Rectangle = (Vector_Rectangle)localPdfStreamDecoder.getObjectValue(-21);
        localVector_Rectangle.trim();
        Rectangle[] arrayOfRectangle = localVector_Rectangle.get();
        Vector_Int localVector_Int = (Vector_Int)localPdfStreamDecoder.getObjectValue(22);
        localVector_Int.trim();
        int[] arrayOfInt = localVector_Int.get();
        for (int j = 0; j != arrayOfRectangle.length; j++)
          localTextLines.addToLineAreas(arrayOfRectangle[j], arrayOfInt[j], paramInt);
      }
      localSwingDisplay.flagDecodingFinished();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localError.getMessage());
    }
    return localSwingDisplay;
  }

  public final void initRenderer(Map paramMap, Graphics2D paramGraphics2D)
  {
    this.rawAf = paramGraphics2D.getTransform();
    this.rawClip = paramGraphics2D.getClip();
    this.areas = paramMap;
    this.g2 = paramGraphics2D;
    this.pagesDrawn.clear();
    setPageOffsets(this.pageNumber);
    setPageSize(this.pageUsedForTransform, this.scaling);
    paramGraphics2D.translate(this.insetW - this.crx, this.insetH - this.cry);
    this.current2 = paramGraphics2D.getTransform();
    this.currentClip = paramGraphics2D.getClip();
    paramGraphics2D.clip(new Rectangle(this.crx, this.cry, this.crw, this.crh));
  }

  private void resetPageCaches(int paramInt1, int paramInt2)
  {
    Iterator localIterator = this.cachedPageViews.keySet().iterator();
    Integer localInteger;
    int i;
    Object localObject2;
    while (localIterator.hasNext())
    {
      localObject1 = localIterator.next();
      localInteger = (Integer)localObject1;
      i = localInteger.intValue();
      if ((i >= paramInt1) && (i <= paramInt2))
      {
        localObject2 = this.cachedPageViews.get(localInteger);
        if (localObject2 != null)
          this.currentPageViews.put(localInteger, localObject2);
      }
    }
    localIterator = this.currentPageViews.keySet().iterator();
    Object localObject1 = new HashMap();
    while (localIterator.hasNext())
    {
      localInteger = (Integer)localIterator.next();
      i = localInteger.intValue();
      if ((i < paramInt1) || (i > paramInt2))
      {
        localObject2 = this.currentPageViews.get(localInteger);
        if (localObject2 != null)
          this.cachedPageViews.put(localInteger, localObject2);
        ((Map)localObject1).put(localInteger, "x");
      }
    }
    localIterator = ((Map)localObject1).keySet().iterator();
    while (localIterator.hasNext())
      this.currentPageViews.remove(localIterator.next());
  }

  private synchronized void calcDisplayedRange()
  {
    int i = this.pageData.getPageCount();
    if (this.displayView == 1)
      return;
    getDisplayedRectangle();
    if (this.displayView == 3)
    {
      if (this.separateCover)
      {
        if (i == 2)
        {
          this.startViewPage = 1;
          this.endViewPage = 2;
        }
        else
        {
          this.startViewPage = this.pageNumber;
          if (this.startViewPage == 1)
          {
            this.endViewPage = 1;
          }
          else if ((this.startViewPage & 0x1) != 1)
          {
            this.startViewPage = this.pageNumber;
            this.endViewPage = (this.pageNumber + 1);
          }
          else
          {
            this.startViewPage = (this.pageNumber - 1);
            this.endViewPage = this.pageNumber;
          }
        }
      }
      else
      {
        this.startViewPage = (this.pageNumber - (1 - (this.pageNumber & 0x1)));
        this.endViewPage = (this.startViewPage + 1);
      }
    }
    else
    {
      int j = updatePageDisplayed();
      this.fileAccess.setPageNumber(j);
      if (j != -1)
        this.pdf.updatePageNumberDisplayed(j);
      int k;
      int m;
      for (int n = 1; n < i + 1; n++)
      {
        k = (this.rx < this.xReached[n] + this.pageW[n]) && (this.rw > this.xReached[n]) ? 1 : 0;
        m = (this.ry < this.yReached[n] + this.pageH[n]) && (this.rh + this.ry > this.yReached[n]) ? 1 : 0;
        if ((k != 0) && (m != 0))
        {
          this.startViewPage = n;
          n = i;
        }
      }
      this.endViewPage = i;
      for (n = this.startViewPage; n < i + 1; n++)
      {
        k = (this.rx < this.xReached[n] + this.pageW[n]) && (this.rw > this.xReached[n]) ? 1 : 0;
        m = (this.ry < this.yReached[n] + this.pageH[n]) && (this.rh + this.ry + this.insetH + 10 > this.yReached[n]) ? 1 : 0;
        if (((this.displayView == 4) || (this.displayView == 2) || (k == 0)) && (m == 0))
        {
          this.endViewPage = (n - 1);
          n = i;
        }
        else
        {
          k = (this.rx < this.xReached[n] + this.pageW[n]) && (this.rw + this.rx + this.insetW + 10 > this.xReached[n]) ? 1 : 0;
          m = (this.ry < this.yReached[n] + this.pageH[n]) && (this.rh > this.yReached[n]) ? 1 : 0;
          if ((m == 0) && (k == 0))
          {
            this.endViewPage = (n - 1);
            n = i;
          }
        }
      }
    }
    if ((this.displayView != 3) && (this.startViewPage > 1))
      this.startViewPage -= 1;
    if (this.displayView != 3)
      refreshDisplay();
  }

  public void dispose()
  {
    this.cachedPageViews = null;
    super.dispose();
  }

  private int updatePageDisplayed()
  {
    int i = -1;
    int j = this.pageData.getPageCount();
    int k;
    int m;
    int n;
    if (this.displayView == 2)
      for (k = 1; k < j; k++)
        if (this.yReached[k] + this.insetH - this.ry + this.pageH[k] >= 0)
        {
          if (this.ry > this.yReached[k] + this.insetH)
          {
            m = this.yReached[k] + this.insetH - this.ry + this.pageH[k];
            n = this.ry + this.rh - this.yReached[(k + 1)];
            if (n > this.pageH[(k + 1)])
              n = this.pageH[(k + 1)];
            if (m < 0)
              m = -m;
            if (n < 0)
              n = -n;
            if (m >= n)
            {
              i = k;
              break;
            }
            i = k + 1;
            break;
          }
          i = k;
          break;
        }
    else if (this.displayView == 4)
      if (this.yReached[0] + this.insetH - this.ry + this.pageH[0] >= 0)
      {
        if (j > 1)
        {
          if (this.ry > this.yReached[1] + this.insetH)
          {
            k = this.yReached[0] + this.insetH - this.ry + this.pageH[0];
            m = this.rh - this.yReached[1];
            if (m > this.pageH[0])
              m = this.pageH[0];
            if (k < 0)
              k = -k;
            if (m < 0)
              m = -m;
            if (k >= m)
              i = 1;
            else
              i = 2;
          }
          else
          {
            i = 1;
          }
        }
        else
          i = 1;
      }
      else
        for (k = 1; k < j; k++)
          if (this.yReached[k] + this.insetH - this.ry + this.pageH[k] >= 0)
          {
            if (this.ry > this.yReached[k])
            {
              m = this.yReached[k] + this.insetH - this.ry + this.pageH[k];
              if (k + 2 >= j)
                n = 0;
              else
                n = this.rh - this.yReached[(k + 2)];
              if (n > this.pageH[k])
                n = this.pageH[k];
              if (m < 0)
                m = -m;
              if (n < 0)
                n = -n;
              if (m >= n)
              {
                i = k;
                break;
              }
              i = k + 2;
              break;
            }
            i = k;
            break;
          }
    return i;
  }

  private void drawOtherPages(AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, Graphics2D paramGraphics2D, Rectangle paramRectangle, float paramFloat, boolean paramBoolean)
  {
    int k = this.pageData.getPageCount();
    int i = this.startViewPage;
    int j = this.endViewPage;
    if (i > j)
      return;
    if ((this.displayView != 2) && (k == 2))
      j++;
    AffineTransform localAffineTransform1 = paramGraphics2D.getTransform();
    Shape localShape = paramGraphics2D.getClip();
    for (int m = i; m < j + 1; m++)
    {
      int n = m;
      if (n > k)
        break;
      if ((!paramBoolean) || (this.accleratedPagesAlreadyDrawn.get(Integer.valueOf(n)) == null))
      {
        DynamicVectorRenderer localDynamicVectorRenderer = (DynamicVectorRenderer)this.currentPageViews.get(Integer.valueOf(n));
        int i1 = (int)(this.pageData.getScaledCropBoxX(n) / paramFloat);
        int i2 = (int)(this.pageData.getScaledCropBoxY(n) / paramFloat);
        if ((this.pageData.getRotation(n) + this.pdf.getDisplayRotation() == 90) || (this.pageData.getRotation(n) + this.pdf.getDisplayRotation() == 270))
        {
          int i3 = i1;
          i1 = i2;
          i2 = i3;
        }
        double d1 = i1;
        double d2 = i2;
        double d3 = this.pageData.getCropBoxHeight(n);
        if (localDynamicVectorRenderer != null)
        {
          paramGraphics2D.setTransform(paramAffineTransform1);
          if (!paramBoolean)
            paramGraphics2D.setClip(this.pdf.getVisibleRect());
          paramGraphics2D.setTransform(localAffineTransform1);
          int i4 = (int)(this.xReached[n] / paramFloat);
          int i5 = (int)(this.yReached[n] / paramFloat);
          paramGraphics2D.translate(i4 - i1, -i2 - i5);
          int i6 = 0;
          int i7 = this.pageData.getRotation(n);
          if ((this.displayView == 2) || (this.displayView == 4))
            if ((this.pdf.getDisplayRotation() == 0) || (this.pdf.getDisplayRotation() == 180))
            {
              if (this.displayView == 2)
                i6 = (int)((this.currentOffset.widestPageNR - this.pageW[n] / paramFloat) / 2.0F);
              if ((i7 == 0) || (i7 == 180))
                paramGraphics2D.translate(0.0D, -(this.pageOffsetH[m] / paramFloat));
              else if (i7 != 90);
            }
            else
            {
              if (this.displayView == 2)
                i6 = (int)((this.currentOffset.widestPageR - this.pageW[n] / paramFloat) / 2.0F);
              if ((i7 == 0) || (i7 == 180))
                paramGraphics2D.translate(0.0D, -(this.pageOffsetW[m] / paramFloat));
              else if (i7 != 90);
            }
          paramGraphics2D.translate(i6, 0);
          int i8 = this.displayRotation + i7;
          if (i8 >= 360)
            i8 -= 360;
          double d4 = this.pageH[n] / paramFloat;
          double d5 = this.pageW[n] / paramFloat;
          AffineTransform localAffineTransform2 = paramGraphics2D.getTransform();
          double d6 = localAffineTransform2.getTranslateX() / paramFloat;
          double d7 = localAffineTransform2.getTranslateY() / paramFloat;
          if (i8 != 0)
            paramGraphics2D.translate(-d6, -d7);
          if (i8 == 180)
          {
            paramGraphics2D.rotate(3.141592653589793D);
            paramGraphics2D.translate(-d6 - d5, -d7 - d4);
            paramGraphics2D.translate(-2 * i1, -2 * i2);
          }
          else if (i8 == 270)
          {
            paramGraphics2D.rotate(1.570796326794897D);
            paramGraphics2D.translate(d7 + d5 - d4, -d6 - d5);
            paramGraphics2D.translate(0, -2 * i1);
          }
          else if (i8 == 90)
          {
            paramGraphics2D.rotate(-1.570796326794897D);
            paramGraphics2D.translate(-d7 - d5, d6);
            paramGraphics2D.translate(-2 * i2, 0);
          }
          localDynamicVectorRenderer.setScalingValues(d1, d3 + d2, paramFloat);
          int i9 = (int)(this.pageW[n] / paramFloat);
          int i10 = (int)(this.pageH[n] / paramFloat);
          if ((i8 == 90) || (i8 == 270))
          {
            int i11 = i9;
            i9 = i10;
            i10 = i11;
            int i12 = i1;
            i1 = i2;
            i2 = i12;
          }
          paramGraphics2D.clip(new Rectangle(i1, i2, i9, i10));
          localDynamicVectorRenderer.setG2(paramGraphics2D);
          if (this.areas != null)
            localDynamicVectorRenderer.paint((Rectangle[])this.areas.get(Integer.valueOf(n)), paramAffineTransform2, paramRectangle);
          else
            localDynamicVectorRenderer.paint(null, paramAffineTransform2, paramRectangle);
          this.pagesDrawn.put(Integer.valueOf(n), "x");
          if (paramBoolean)
            this.accleratedPagesAlreadyDrawn.put(Integer.valueOf(n), "x");
          paramGraphics2D.setTransform(localAffineTransform1);
          paramGraphics2D.setClip(localShape);
        }
      }
    }
  }

  public final void drawBorder()
  {
    Border localBorder = this.pdf.getPDFBorder();
    int i = this.startViewPage;
    int j = this.endViewPage;
    int k = this.pageData.getPageCount();
    if (i == 0)
      return;
    if ((this.displayView == 2) || (this.displayView == 4))
    {
      i = 1;
      j = k;
    }
    if (this.rawAf != null)
      this.g2.setTransform(this.rawAf);
    this.g2.setClip(this.rawClip);
    if ((this.displayView != 3) && (i > 1))
    {
      i--;
      if (k == 2)
        j++;
    }
    int m = 1;
    if ((this.displayView != 2) && ((m & 0x1) == 1))
      m--;
    this.g2.translate(-this.xReached[m] + this.insetW, -this.yReached[m] + this.insetH);
    for (int n = i; (n < j + 1) && (n != this.xReached.length); n++)
    {
      AffineTransform localAffineTransform = null;
      if (this.displayView == 2)
      {
        localAffineTransform = this.g2.getTransform();
        int i1;
        if ((this.displayRotation == 0) || (this.displayRotation == 180))
          i1 = (int)((this.currentOffset.widestPageNR * this.scaling - this.pageW[n]) / 2.0F);
        else
          i1 = (int)((this.currentOffset.widestPageR * this.scaling - this.pageW[n]) / 2.0F);
        this.g2.translate(i1, 0);
      }
      else if (((this.displayView == 4) || (this.displayView == 3)) && (k > 1))
      {
        localAffineTransform = this.g2.getTransform();
      }
      if ((this.pagesDrawn.get(Integer.valueOf(n)) == null) && (this.accleratedPagesAlreadyDrawn.get(Integer.valueOf(n)) == null))
        try
        {
          if ((this.displayView == 3) && (this.turnoverOn) && (this.facingDragTempLeftImg != null))
          {
            if (this.facingDragTempRightNo == n)
            {
              this.g2.drawImage(this.facingDragTempRightImg, this.xReached[n], this.yReached[n], this.pageW[n], this.pageH[n], null);
            }
            else if (this.facingDragTempLeftNo == n)
            {
              this.g2.drawImage(this.facingDragTempLeftImg, this.xReached[n], this.yReached[n], this.pageW[n], this.pageH[n], null);
            }
            else
            {
              this.g2.setPaint(this.options.getNonDrawnPageColor());
              this.g2.fillRect(this.xReached[n], this.yReached[n], this.pageW[n], this.pageH[n]);
            }
          }
          else
          {
            this.g2.setPaint(this.options.getNonDrawnPageColor());
            this.g2.fillRect(this.xReached[n], this.yReached[n], this.pageW[n], this.pageH[n]);
          }
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
      if ((SingleDisplay.CURRENT_BORDER_STYLE == 1) && (this.pageW[n] > 0) && (this.pageH[n] > 0) && (localBorder != null))
        if ((this.displayView != 3) || (!this.turnoverOn) || (this.pdf.getPdfPageData().hasMultipleSizes()) || (k == 2))
        {
          localBorder.paintBorder(this.pdf, this.g2, this.xReached[n] - localBorder.getBorderInsets(this.pdf).left, this.yReached[n] - localBorder.getBorderInsets(this.pdf).bottom, this.pageW[n] + localBorder.getBorderInsets(this.pdf).left + localBorder.getBorderInsets(this.pdf).right, this.pageH[n] + localBorder.getBorderInsets(this.pdf).bottom + localBorder.getBorderInsets(this.pdf).top);
        }
        else if ((this.turnoverOn) && (this.displayView == 3))
        {
          int i2 = this.pageW[n] * 2;
          int i3;
          if (this.separateCover)
            i3 = n / 2 * 2;
          else
            i3 = n - (1 - (n & 0x1));
          if (i3 == 0)
          {
            i3 = 1;
            if (k != 2)
              i2 = this.pageW[i3];
          }
          if ((n == this.pdf.getPageCount()) && (((this.separateCover) && ((n & 0x1) != 1)) || ((!this.separateCover) && ((n & 0x1) != 0))))
            i2 = this.pageW[n];
          if ((i3 == n) && ((n != 2) || (k != 2)))
            localBorder.paintBorder(this.pdf, this.g2, this.xReached[i3] - localBorder.getBorderInsets(this.pdf).left, this.yReached[i3] - localBorder.getBorderInsets(this.pdf).bottom, i2 + localBorder.getBorderInsets(this.pdf).left + localBorder.getBorderInsets(this.pdf).right, this.pageH[i3] + localBorder.getBorderInsets(this.pdf).bottom + localBorder.getBorderInsets(this.pdf).top);
          this.g2.setPaint(Color.BLACK);
          this.g2.drawLine(this.xReached[i3] + this.pageW[i3], this.yReached[i3], this.xReached[i3] + this.pageW[i3], this.yReached[i3] + this.pageH[i3] - 1);
        }
      if (localAffineTransform != null)
        this.g2.setTransform(localAffineTransform);
    }
  }

  Rectangle drawPage(AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, int paramInt)
  {
    if (paramAffineTransform2 != null)
    {
      if (this.displayRotation != this.oldRotation)
      {
        setPageOffsets(this.pageNumber);
        this.useAcceleration = true;
      }
      this.pageUsedForTransform = paramInt;
      Rectangle localRectangle = null;
      if (!isAccelerated())
        calcVisibleArea(this.topH, this.topW);
      this.g2.transform(paramAffineTransform2);
      boolean bool = false;
      if ((this.useAcceleration) && (!this.overRideAcceleration) && (this.scaling < 2.0F))
        bool = testAcceleratedRendering();
      if (bool)
      {
        Graphics2D localGraphics2D = (Graphics2D)this.backBuffer.getGraphics();
        if (this.screenNeedsRedrawing)
        {
          localGraphics2D.setColor(this.pdf.getBackground());
          localGraphics2D.fill(new Rectangle(0, 0, this.backBuffer.getWidth(), this.backBuffer.getHeight()));
          this.screenNeedsRedrawing = false;
        }
        if ((this.isInitialised) && (this.xReached != null))
        {
          localGraphics2D.setTransform(paramAffineTransform2);
          setDisplacementOnG2(localGraphics2D);
          drawOtherPages(this.rawAf, paramAffineTransform1, localGraphics2D, localRectangle, this.scaling, true);
        }
        localGraphics2D.dispose();
        if (this.backBuffer != null)
        {
          AffineTransform localAffineTransform = this.g2.getTransform();
          this.g2.setTransform(this.rawAf);
          this.g2.setClip(this.rawClip);
          this.g2.setTransform(this.rawAf);
          this.g2.drawImage(this.backBuffer, this.insetW, this.insetH, this.pdf);
          this.g2.setTransform(localAffineTransform);
        }
      }
      else
      {
        this.accleratedPagesAlreadyDrawn.clear();
        this.currentDisplay.setOptimsePainting(false);
        drawOtherPages(this.rawAf, paramAffineTransform1, this.g2, localRectangle, this.scaling, false);
      }
      this.oldScaling = this.scaling;
      this.oldRotation = this.displayRotation;
    }
    return null;
  }

  public void drawFacing(Rectangle paramRectangle)
  {
    SwingGUI localSwingGUI = (SwingGUI)this.pdf.getExternalHandler().getExternalHandler(11);
    int i = (localSwingGUI != null) && (localSwingGUI.getPageTurnScalingAppropriate()) ? 1 : 0;
    if ((this.displayView == 3) && (getBoolean(Display.BoolValue.TURNOVER_ON)) && (i != 0) && (!this.pageData.hasMultipleSizes()))
      drawFacingDrag(this.g2, paramRectangle);
  }

  protected void drawFacingDrag(Graphics2D paramGraphics2D, Rectangle paramRectangle)
  {
    SwingGUI localSwingGUI = (SwingGUI)this.pdf.getExternalHandler(11);
    float f1 = this.pageW[1];
    float f2 = this.pageH[1];
    int i = this.pageData.getPageCount();
    boolean bool1 = localSwingGUI.getDragLeft();
    boolean bool2 = localSwingGUI.getDragTop();
    Point localPoint1 = new Point();
    if (bool1)
      localPoint1.x = ((int)(paramRectangle.getWidth() / 2.0D - f1));
    else
      localPoint1.x = ((int)(paramRectangle.getWidth() / 2.0D + f1));
    localPoint1.y = this.pdf.getInsetH();
    if (!bool2)
    {
      Point tmp124_122 = localPoint1;
      tmp124_122.y = ((int)(tmp124_122.y + f2));
    }
    if (this.debugFacingDrag)
    {
      paramGraphics2D.setPaint(Color.YELLOW);
      paramGraphics2D.drawLine((int)paramRectangle.getWidth() / 2, 0, (int)paramRectangle.getWidth() / 2, 1000);
      paramGraphics2D.drawLine(localPoint1.x, 0, localPoint1.x, 1000);
    }
    Point localPoint2 = this.pdf.getUserOffsets(999);
    if (((bool1) && (localPoint2.x < localPoint1.x)) || ((!bool1) && (localPoint2.x > localPoint1.x) && (((!bool2) && (localPoint2.y > localPoint1.y)) || ((bool2) && (localPoint2.y < localPoint1.y)))))
      return;
    if ((!bool2) && (localPoint2.y >= localPoint1.y - 2))
      localPoint1.y -= 3;
    if ((bool2) && (localPoint2.y <= localPoint1.y + 2))
      localPoint1.y += 3;
    if ((bool1) && (localPoint2.x <= localPoint1.x))
      localPoint1.x += 1;
    if ((!bool1) && (localPoint2.x > localPoint1.x))
      localPoint2.x = localPoint1.x;
    int j = 0;
    double d1;
    if (bool1)
      d1 = f1;
    else
      d1 = -f1;
    int k = (int)Math.hypot(localPoint2.x - (localPoint1.x + d1), localPoint2.y - localPoint1.y);
    if (k > f1)
    {
      localPoint2.x = ((int)(localPoint2.x - (localPoint1.x + d1)));
      localPoint2.y -= localPoint1.y;
      localPoint2.x = ((int)(localPoint2.x * (f1 / k)));
      localPoint2.y = ((int)(localPoint2.y * (f1 / k)));
      localPoint2.x = ((int)(localPoint2.x + (localPoint1.x + d1)));
      localPoint2.y += localPoint1.y;
      j = 1;
    }
    Point localPoint3 = new Point();
    if (bool1)
      localPoint3.x = ((localPoint2.x - localPoint1.x) / 2);
    else
      localPoint3.x = ((localPoint1.x - localPoint2.x) / 2);
    localPoint3.y = ((localPoint1.y - localPoint2.y) / 2);
    if (localPoint1.y - localPoint2.y == 0)
      localPoint2.y += 1;
    double d2 = (localPoint1.x - localPoint2.x) / (localPoint1.y - localPoint2.y);
    Point localPoint4 = new Point();
    if (bool1)
      localPoint4.x = ((int)(localPoint1.x + (localPoint3.x + 1.0D / d2 * -localPoint3.y)));
    else
      localPoint4.x = ((int)(localPoint1.x - (localPoint3.x + 1.0D / d2 * localPoint3.y)));
    localPoint4.y = localPoint1.y;
    if (j != 0)
      localPoint1.x += (int)d1;
    Point localPoint5 = new Point();
    Point localPoint6 = new Point();
    int m;
    if (bool1)
    {
      if (localPoint4.x > localPoint1.x + f1)
        localPoint4.x = ((int)(localPoint1.x + f1));
      m = (int)(localPoint1.y - (localPoint3.y + -d2 * localPoint3.x));
    }
    else
    {
      if (localPoint4.x < localPoint1.x - f1)
        localPoint4.x = ((int)(localPoint1.x - f1));
      m = (int)(localPoint1.y - (localPoint3.y + d2 * localPoint3.x));
    }
    int n = ((!bool2) && (m < localPoint1.y - f2)) || ((bool2) && (m > localPoint1.y + f2)) ? 1 : 0;
    if (n != 0)
    {
      if (bool2)
      {
        localPoint5.y = ((int)(localPoint1.y + f2));
        localPoint5.x = ((int)(localPoint1.x + -1.0D / d2 * (localPoint1.y + f2 - m)));
      }
      else
      {
        localPoint5.y = ((int)(localPoint1.y - f2));
        localPoint5.x = ((int)(localPoint1.x + -1.0D / d2 * (localPoint1.y - f2 - m)));
      }
      double d3 = (localPoint4.x - localPoint2.x) / (localPoint4.y - localPoint2.y);
      double d4 = (localPoint1.x - localPoint5.x) / Math.hypot(localPoint4.x - localPoint2.x, localPoint4.y - localPoint2.y);
      if (bool1)
      {
        localPoint6.y = ((int)(localPoint5.y + 1.0D / d3 * (localPoint4.x - localPoint2.x) * d4));
        localPoint6.x = ((int)(localPoint5.x + d3 * (localPoint4.y - localPoint2.y) * d4));
      }
      else
      {
        localPoint6.y = ((int)(localPoint5.y - 1.0D / d3 * (localPoint4.x - localPoint2.x) * d4));
        localPoint6.x = ((int)(localPoint5.x - d3 * (localPoint4.y - localPoint2.y) * d4));
      }
    }
    else
    {
      localPoint5.y = m;
      localPoint5.x = localPoint1.x;
      localPoint6.y = m;
      localPoint6.x = localPoint1.x;
    }
    GeneralPath localGeneralPath1 = new GeneralPath();
    localGeneralPath1.moveTo(localPoint2.x, localPoint2.y);
    localGeneralPath1.lineTo(localPoint4.x, localPoint4.y);
    localGeneralPath1.lineTo(localPoint5.x, localPoint5.y);
    localGeneralPath1.lineTo(localPoint6.x, localPoint6.y);
    localGeneralPath1.closePath();
    GeneralPath localGeneralPath2 = new GeneralPath();
    localGeneralPath2.moveTo(localPoint5.x, localPoint5.y);
    localGeneralPath2.lineTo(localPoint1.x, localPoint5.y);
    localGeneralPath2.lineTo(localPoint1.x, localPoint1.y);
    localGeneralPath2.lineTo(localPoint4.x, localPoint4.y);
    localGeneralPath2.closePath();
    Rectangle localRectangle = paramRectangle;
    AffineTransform localAffineTransform1 = new AffineTransform();
    paramGraphics2D.setTransform(localAffineTransform1);
    paramGraphics2D.setClip(localRectangle);
    paramGraphics2D.clip(localGeneralPath2);
    int i1;
    if (bool1)
    {
      if (bool2)
        paramGraphics2D.translate(localPoint1.x, localPoint1.y);
      else
        paramGraphics2D.translate(localPoint1.x, localPoint1.y - f2);
      i1 = this.pageNumber - 2 > 0 ? 1 : 0;
    }
    else
    {
      if (bool2)
        paramGraphics2D.translate(localPoint1.x - f1, localPoint1.y);
      else
        paramGraphics2D.translate(localPoint1.x - f1, localPoint1.y - f2);
      int i2 = this.pageNumber + 2;
      if ((this.separateCover) && (i2 % 2 == 0))
        i2++;
      else if ((!this.separateCover) && (i2 % 2 == 1))
        i2++;
      i1 = i2 <= this.pdf.getPageCount() ? 1 : 0;
    }
    Object localObject;
    double d8;
    if (i1 != 0)
    {
      try
      {
        if ((bool1) && (this.facingDragCachedImages[0] != null))
        {
          paramGraphics2D.drawImage(this.facingDragCachedImages[0], 0, 0, (int)f1 - 1, (int)f2 - 1, null);
        }
        else if ((!bool1) && (this.facingDragCachedImages[3] != null))
        {
          paramGraphics2D.drawImage(this.facingDragCachedImages[3], 0, 0, (int)f1 - 1, (int)f2 - 1, null);
        }
        else
        {
          paramGraphics2D.setPaint(Color.WHITE);
          paramGraphics2D.fillRect(0, 0, (int)f1, (int)f2);
        }
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException1.getMessage());
      }
    }
    else
    {
      float f3;
      if (bool1)
        f3 = 0.0F;
      else
        f3 = (float)localRectangle.getWidth();
      float f4;
      float f5;
      if (bool2)
      {
        f4 = 0.0F;
        f5 = (float)localRectangle.getHeight();
      }
      else
      {
        f4 = (float)localRectangle.getHeight();
        f5 = 0.0F;
      }
      GeneralPath localGeneralPath3 = new GeneralPath();
      localGeneralPath3.moveTo(localPoint5.x, localPoint5.y);
      localGeneralPath3.lineTo(localPoint4.x, localPoint4.y);
      localGeneralPath3.lineTo(localPoint4.x, f4);
      localGeneralPath3.lineTo(f3, f4);
      if (n != 0)
      {
        localGeneralPath3.lineTo(f3, f5);
        localGeneralPath3.lineTo(localPoint5.x, f5);
      }
      else
      {
        localGeneralPath3.lineTo(f3, localPoint5.y);
      }
      localGeneralPath3.closePath();
      paramGraphics2D.setTransform(localAffineTransform1);
      paramGraphics2D.setClip(localRectangle);
      paramGraphics2D.clip(localGeneralPath3);
      paramGraphics2D.setPaint(this.pdf.getBackground());
      paramGraphics2D.fill(localGeneralPath3);
      if (this.debugFacingDrag)
      {
        paramGraphics2D.setPaint(new Color(0.0F, 1.0F, 0.0F, 0.5F));
        paramGraphics2D.fill(localGeneralPath3);
      }
      if ((this.pdf.useNewGraphicsMode) && (SingleDisplay.CURRENT_BORDER_STYLE == 1) && (localSwingGUI != null))
      {
        double d7 = Math.hypot(localPoint4.getX() - localPoint5.getX(), localPoint4.getY() - localPoint5.getY());
        localObject = new AffineTransform();
        ((AffineTransform)localObject).translate(localPoint5.getX(), localPoint5.getY());
        if (bool1)
        {
          ((AffineTransform)localObject).rotate(-Math.atan2(localPoint4.getX() - localPoint5.getX(), localPoint4.getY() - localPoint5.getY()));
          if (!bool2)
          {
            ((AffineTransform)localObject).rotate(3.141592653589793D);
            ((AffineTransform)localObject).translate(0.0D, -d7);
          }
        }
        else
        {
          ((AffineTransform)localObject).rotate(-Math.atan2(localPoint4.getX() - localPoint5.getX(), localPoint4.getY() - localPoint5.getY()));
          if (bool2)
          {
            ((AffineTransform)localObject).rotate(3.141592653589793D);
            ((AffineTransform)localObject).translate(0.0D, -d7);
          }
        }
        paramGraphics2D.setTransform((AffineTransform)localObject);
        paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, localSwingGUI.glowInnerColor, localSwingGUI.glowThickness, 0.0F, localSwingGUI.glowOuterColor));
        paramGraphics2D.fillRect(0, 0, localSwingGUI.glowThickness, (int)d7);
        double d9;
        if (bool1)
        {
          d9 = -localSwingGUI.glowThickness;
          d8 = (Math.atan2(localPoint4.getX() - localPoint5.getX(), localPoint4.getY() - localPoint5.getY()) / 3.141592653589793D - 0.5D) * -2.0D;
        }
        else
        {
          d9 = localSwingGUI.glowThickness;
          d8 = (-Math.atan2(localPoint4.getX() - localPoint5.getX(), localPoint4.getY() - localPoint5.getY()) / 3.141592653589793D - 0.5D) * -2.0D;
        }
        double d11;
        if (bool2)
        {
          d8 = -d8;
          d11 = -localSwingGUI.glowThickness;
          if (d8 < 0.0D)
            d8 = 1.0D;
        }
        else
        {
          d11 = localSwingGUI.glowThickness;
        }
        Point localPoint7 = new Point((int)(localPoint5.getX() + d9), (int)localPoint5.getY());
        Point localPoint8 = new Point((int)((int)localPoint5.getX() + d8 * d9), (int)(localPoint5.getY() + (1.0D - d8) * d11));
        paintCornerGlow(paramGraphics2D, 1.0F, localPoint5, localPoint7, localPoint8);
        if (n != 0)
          paintCornerGlow(paramGraphics2D, 1.0F, localPoint5, new Point((int)localPoint5.getX(), (int)(localPoint5.getY() - d11)), localPoint7);
        localPoint8.x = ((int)(localPoint4.getX() + (localPoint8.x - localPoint5.getX())));
        localPoint8.y = ((int)(localPoint4.getY() + (localPoint8.y - localPoint5.getY())));
        localPoint7.x = ((int)localPoint4.getX());
        localPoint7.y = ((int)(localPoint4.getY() + d11));
        paintCornerGlow(paramGraphics2D, 1.0F, localPoint4, localPoint7, localPoint8);
      }
    }
    paramGraphics2D.setTransform(localAffineTransform1);
    paramGraphics2D.setClip(localRectangle);
    paramGraphics2D.setPaint(Color.BLACK);
    if (i1 != 0)
      paramGraphics2D.draw(localGeneralPath2);
    paramGraphics2D.clip(localGeneralPath1);
    paramGraphics2D.translate(localPoint2.x, localPoint2.y);
    if (bool1)
    {
      paramGraphics2D.rotate(Math.atan2(localPoint2.y - localPoint4.y, localPoint2.x - localPoint4.x));
      if (bool2)
        paramGraphics2D.translate(-f1, 0.0D);
      else
        paramGraphics2D.translate(-f1, -f2);
    }
    else
    {
      paramGraphics2D.rotate(Math.atan2(localPoint2.y - localPoint4.y, localPoint2.x - localPoint4.x) + 3.141592653589793D);
      if (!bool2)
        paramGraphics2D.translate(0.0D, -f2);
    }
    try
    {
      if ((bool1) && (this.facingDragCachedImages[1] != null))
      {
        paramGraphics2D.drawImage(this.facingDragCachedImages[1], 0, 0, (int)f1 - 1, (int)f2 - 1, null);
      }
      else if ((!bool1) && (this.facingDragCachedImages[2] != null))
      {
        paramGraphics2D.drawImage(this.facingDragCachedImages[2], 0, 0, (int)f1 - 1, (int)f2 - 1, null);
      }
      else
      {
        paramGraphics2D.setPaint(Color.WHITE);
        paramGraphics2D.fillRect(0, 0, (int)f1, (int)f2);
      }
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException2.getMessage());
    }
    double d5 = Math.atan2(localPoint5.y - localPoint4.y, localPoint5.x - localPoint4.x) / 3.141592653589793D + 0.5D;
    if (bool2)
      d5 = 1.0D - d5;
    if (bool1)
      d5 = -d5;
    double d6 = 0.5D - Math.hypot(localPoint2.x - localPoint4.x, localPoint2.y - localPoint4.y) / (2.0F * f1);
    if (d6 < 0.0D)
      d6 = 0.0D;
    if ((this.pdf.useNewGraphicsMode) && (SingleDisplay.CURRENT_BORDER_STYLE == 1) && (((bool1) && (this.pageNumber == this.pdf.getPageCount()) && (((this.separateCover) && ((this.pageNumber & 0x1) != 1)) || ((!this.separateCover) && ((this.pageNumber & 0x1) != 0)))) || ((!bool1) && (i != 2) && (this.pageNumber == 1))))
    {
      float f6 = 20.0F * (float)(0.05D - (d5 + d6));
      if ((localSwingGUI != null) && (f6 > 0.0F))
      {
        Color localColor = localSwingGUI.glowInnerColor;
        localColor = new Color(localColor.getRed(), localColor.getGreen(), localColor.getBlue(), (int)(f6 * localColor.getAlpha()));
        localObject = localSwingGUI.glowOuterColor;
        paramGraphics2D.setTransform(localAffineTransform1);
        paramGraphics2D.setClip(localRectangle);
        d8 = Math.hypot(localPoint2.getX() - localPoint6.getX(), localPoint2.getY() - localPoint6.getY());
        AffineTransform localAffineTransform2 = new AffineTransform();
        localAffineTransform2.translate(localPoint6.getX(), localPoint6.getY());
        localAffineTransform2.rotate(-Math.atan2(localPoint2.getX() - localPoint6.getX(), localPoint2.getY() - localPoint6.getY()));
        if (bool1 == bool2)
          localAffineTransform2.translate(-localSwingGUI.glowThickness, 0.0D);
        paramGraphics2D.setTransform(localAffineTransform2);
        if ((bool1 ^ bool2))
          paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, localColor, localSwingGUI.glowThickness, 0.0F, (Color)localObject));
        else
          paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, (Color)localObject, localSwingGUI.glowThickness, 0.0F, localColor));
        paramGraphics2D.fillRect(0, 0, localSwingGUI.glowThickness, (int)d8);
        double d12;
        double d10;
        if (bool1)
        {
          d12 = -localSwingGUI.glowThickness;
          d10 = (Math.atan2(localPoint6.getX() - localPoint5.getX(), localPoint6.getY() - localPoint5.getY()) / 3.141592653589793D - 0.5D) * 2.0D;
        }
        else
        {
          d12 = localSwingGUI.glowThickness;
          d10 = (-Math.atan2(localPoint6.getX() - localPoint5.getX(), localPoint6.getY() - localPoint5.getY()) / 3.141592653589793D - 0.5D) * 2.0D;
        }
        double d13;
        if (bool2)
          d13 = -localSwingGUI.glowThickness;
        else
          d13 = localSwingGUI.glowThickness;
        Point localPoint9 = new Point((int)(localPoint6.getX() + d10 * d12), (int)(localPoint6.getY() - (1.0D - d10) * d13));
        Point localPoint10 = new Point((int)((int)localPoint6.getX() - (1.0D - d10) * d12), (int)(localPoint6.getY() - d10 * d13));
        paintCornerGlow(paramGraphics2D, f6, localPoint6, localPoint9, localPoint10);
        d8 = Math.hypot(localPoint2.getX() - localPoint4.getX(), localPoint2.getY() - localPoint4.getY());
        localAffineTransform2 = new AffineTransform();
        localAffineTransform2.translate(localPoint4.getX(), localPoint4.getY());
        localAffineTransform2.rotate(-Math.atan2(localPoint2.getX() - localPoint4.getX(), localPoint2.getY() - localPoint4.getY()));
        if ((bool1 ^ bool2))
          localAffineTransform2.translate(-localSwingGUI.glowThickness, 0.0D);
        paramGraphics2D.setTransform(localAffineTransform2);
        if ((bool1 ^ bool2))
          paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, (Color)localObject, localSwingGUI.glowThickness, 0.0F, localColor));
        else
          paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, localColor, localSwingGUI.glowThickness, 0.0F, (Color)localObject));
        paramGraphics2D.fillRect(0, 0, localSwingGUI.glowThickness, (int)d8);
        localPoint2.x += localPoint6.x - localPoint9.x;
        localPoint2.y += localPoint6.y - localPoint9.y;
        localPoint2.x += localPoint10.x - localPoint6.x;
        localPoint2.y += localPoint10.y - localPoint6.y;
        paintCornerGlow(paramGraphics2D, f6, localPoint2, localPoint9, localPoint10);
        d8 = Math.hypot(localPoint6.getX() - localPoint5.getX(), localPoint6.getY() - localPoint5.getY());
        localAffineTransform2 = new AffineTransform();
        if ((bool1 ^ bool2))
        {
          localAffineTransform2.translate(localPoint5.getX(), localPoint5.getY());
          localAffineTransform2.rotate(-Math.atan2(localPoint6.getX() - localPoint5.getX(), localPoint6.getY() - localPoint5.getY()));
        }
        else
        {
          localAffineTransform2.translate(localPoint6.getX(), localPoint6.getY());
          localAffineTransform2.rotate(-Math.atan2(localPoint5.getX() - localPoint6.getX(), localPoint5.getY() - localPoint6.getY()));
        }
        paramGraphics2D.setTransform(localAffineTransform2);
        paramGraphics2D.setPaint(new GradientPaint(0.0F, 0.0F, localColor, localSwingGUI.glowThickness, 0.0F, (Color)localObject));
        paramGraphics2D.fillRect(0, 0, localSwingGUI.glowThickness, (int)d8);
      }
    }
    paramGraphics2D.setPaint(Color.BLACK);
    paramGraphics2D.setTransform(localAffineTransform1);
    paramGraphics2D.setClip(localRectangle);
    paramGraphics2D.draw(localGeneralPath1);
    paramGraphics2D.clip(localGeneralPath1);
    paramGraphics2D.setPaint(new GradientPaint((int)((d5 + d6) * 300.0D), 0.0F, new Color(0.0F, 0.0F, 0.0F, 0.0F), 0.0F, 0.0F, new Color(0.0F, 0.0F, 0.0F, (float)(d5 + d6) * 0.4F + 0.4F)));
    paramGraphics2D.translate(localPoint4.x, localPoint4.y);
    if ((bool1 ^ bool2))
    {
      paramGraphics2D.rotate(Math.atan2(localPoint5.y - localPoint4.y, localPoint5.x - localPoint4.x) + 1.570796326794897D);
      paramGraphics2D.fillRect(0, (int)-(f2 + f1), (int)((d5 + d6) * 300.0D), (int)(f2 + f1));
    }
    else
    {
      paramGraphics2D.rotate(Math.atan2(localPoint5.y - localPoint4.y, localPoint5.x - localPoint4.x) + -1.570796326794897D);
      paramGraphics2D.fillRect(0, 0, (int)((d5 + d6) * 300.0D), (int)(f2 + f1));
    }
    if (i1 != 0)
    {
      paramGraphics2D.setTransform(localAffineTransform1);
      paramGraphics2D.setClip(localRectangle);
      paramGraphics2D.clip(localGeneralPath2);
      paramGraphics2D.setPaint(new GradientPaint((int)((d5 + d6) * 150.0D), 0.0F, new Color(0.0F, 0.0F, 0.0F, 0.0F), 0.0F, 0.0F, new Color(0.0F, 0.0F, 0.0F, (float)(d5 + d6) * 0.36F + 0.24F)));
      paramGraphics2D.translate(localPoint4.x, localPoint4.y);
      if ((bool1 ^ bool2))
      {
        paramGraphics2D.rotate(Math.atan2(localPoint5.y - localPoint4.y, localPoint5.x - localPoint4.x) + -1.570796326794897D);
        paramGraphics2D.fillRect(0, 0, (int)((d5 + d6) * 150.0D), (int)(f2 + f1));
      }
      else
      {
        paramGraphics2D.rotate(Math.atan2(localPoint5.y - localPoint4.y, localPoint5.x - localPoint4.x) + 1.570796326794897D);
        paramGraphics2D.fillRect(0, (int)-(f2 + f1), (int)((d5 + d6) * 150.0D), (int)(f2 + f1));
      }
    }
  }

  private void paintCornerGlow(Graphics2D paramGraphics2D, float paramFloat, Point paramPoint1, Point paramPoint2, Point paramPoint3)
  {
    if (paramFloat <= 0.0F)
      return;
    AffineTransform localAffineTransform = paramGraphics2D.getTransform();
    Shape localShape = paramGraphics2D.getClip();
    SwingGUI localSwingGUI = (SwingGUI)this.pdf.getExternalHandler(11);
    GeneralPath localGeneralPath = new GeneralPath();
    localGeneralPath.moveTo((float)paramPoint1.getX(), (float)paramPoint1.getY());
    localGeneralPath.lineTo((float)paramPoint2.getX(), (float)paramPoint2.getY());
    localGeneralPath.lineTo((float)paramPoint3.getX(), (float)paramPoint3.getY());
    localGeneralPath.closePath();
    paramGraphics2D.setTransform(new AffineTransform());
    paramGraphics2D.setClip(this.pdf.getVisibleRect());
    paramGraphics2D.clip(localGeneralPath);
    if (localSwingGUI != null)
    {
      Color localColor = localSwingGUI.glowInnerColor;
      localColor = new Color(localColor.getRed(), localColor.getGreen(), localColor.getBlue(), (int)(paramFloat * localColor.getAlpha()));
      paramGraphics2D.setPaint(new GradientPaint((int)paramPoint1.getX(), (int)paramPoint1.getY(), localColor, (int)((paramPoint2.getX() + paramPoint3.getX()) / 2.0D), (int)((paramPoint2.getY() + paramPoint3.getY()) / 2.0D), localSwingGUI.glowOuterColor));
    }
    paramGraphics2D.fill(localGeneralPath);
    paramGraphics2D.setTransform(localAffineTransform);
    paramGraphics2D.setClip(localShape);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.swing.MultiDisplay
 * JD-Core Version:    0.6.2
 */
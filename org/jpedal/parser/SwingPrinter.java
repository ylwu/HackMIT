package org.jpedal.parser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.util.HashMap;
import java.util.Map;
import javax.print.attribute.SetOfIntegerSyntax;
import org.jpedal.PdfDecoder;
import org.jpedal.color.ColorSpaces;
import org.jpedal.display.DisplayOffsets;
import org.jpedal.display.Overlays;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.CustomFormPrint;
import org.jpedal.external.CustomPrintHintingHandler;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.PdfPrintTransform;

public class SwingPrinter
{
  private Overlays overlays;
  private int duplexGapOdd = 0;
  private int duplexGapEven = 0;
  ObjectStore objectPrintStoreRef = null;
  ExternalHandlers externalHandlers;
  private boolean centerPrintedPage = false;
  private boolean rotatePrintedPage = false;
  public boolean operationSuccessful = true;
  public int pageScalingMode = 2;
  public String pageErrorMessages = "";
  public boolean printOnlyVisible = false;
  public int logicalPageOffset = 0;
  public DynamicVectorRenderer printRender = null;
  public int lastPrintedPage = -1;
  public int currentPrintPage = 0;
  public boolean isCustomPrinting = false;
  PdfObjectReader currentPdfFile;
  public Map pageFormats = new HashMap(100);
  public int[] listOfPages;
  public boolean allowDifferentPrintPageSizes = false;
  public int start = 0;
  public int end = -1;
  public boolean oddPagesOnly = false;
  public boolean evenPagesOnly = false;
  public SetOfIntegerSyntax range;
  public boolean pagesPrintedInReverse = false;
  public boolean stopPrinting = false;
  public boolean usePDFPaperSize = false;
  public PdfStreamDecoderForPrinting currentPrintDecoder = null;
  public boolean legacyPrintMode = true;
  private float scaling;
  private int insetW;
  private int insetH;

  public int getNumberOfPages(int paramInt)
  {
    if (this.range != null)
    {
      i = 0;
      for (int j = 1; j < paramInt + 1; j++)
        if ((this.range.contains(j)) && ((!this.oddPagesOnly) || ((j & 0x1) == 1)) && ((!this.evenPagesOnly) || ((j & 0x1) == 0)))
          i++;
      return i;
    }
    int i = 1;
    if (this.end != -1)
    {
      i = this.end - this.start + 1;
      if (i < 0)
        i = 2 - i;
    }
    if ((this.oddPagesOnly) || (this.evenPagesOnly))
      return (i + 1) / 2;
    return i;
  }

  public PageFormat getPageFormat(int paramInt1, PdfPageData paramPdfPageData, int paramInt2)
    throws IndexOutOfBoundsException
  {
    if ((this.listOfPages != null) && (paramInt1 < this.listOfPages.length))
      paramInt1 = this.listOfPages[paramInt1];
    int i;
    if (this.end == -1)
      i = paramInt1 + 1;
    else if (this.end > this.start)
      i = this.start + paramInt1;
    else
      i = this.start - paramInt1;
    Object localObject = this.pageFormats.get(Integer.valueOf(i));
    if (localObject == null)
      localObject = this.pageFormats.get("standard");
    PageFormat localPageFormat = null;
    this.pageFormats.put("Align-" + i, "normal");
    if (this.usePDFPaperSize)
    {
      int j = paramPdfPageData.getCropBoxWidth(i);
      int k = paramPdfPageData.getCropBoxHeight(i);
      int m = paramPdfPageData.getRotation(i);
      if (this.allowDifferentPrintPageSizes)
      {
        int n;
        if ((m == 90) || (m == 270))
        {
          n = j;
          j = k;
          k = n;
        }
        if (j > k)
        {
          n = j;
          j = k;
          k = n;
          if (m == 90)
            this.pageFormats.put("Align-" + i, "inverted");
        }
      }
      if (localPageFormat == null)
        localPageFormat = new PageFormat();
      createCustomPaper(localPageFormat, j, k, paramInt2, paramPdfPageData);
    }
    else if (localObject != null)
    {
      localPageFormat = (PageFormat)localObject;
    }
    return localPageFormat;
  }

  private void createCustomPaper(PageFormat paramPageFormat, int paramInt1, int paramInt2, int paramInt3, PdfPageData paramPdfPageData)
  {
    Paper localPaper = new Paper();
    if ((paramInt3 == 1) || (this.allowDifferentPrintPageSizes))
    {
      localPaper.setSize(paramInt1, paramInt2);
      localPaper.setImageableArea(0.0D, 0.0D, paramInt1, paramInt2);
    }
    else
    {
      int i = 0;
      int j = 0;
      for (int k = this.start; k <= this.end; k++)
        if ((paramInt1 <= paramPdfPageData.getMediaBoxWidth(k) + 1) && (paramInt2 <= paramPdfPageData.getMediaBoxHeight(k) + 1))
        {
          i = paramPdfPageData.getMediaBoxWidth(k) + 1;
          j = paramPdfPageData.getMediaBoxHeight(k) + 1;
        }
      localPaper.setSize(i, j);
      localPaper.setImageableArea(0.0D, 0.0D, paramInt1, paramInt2);
    }
    paramPageFormat.setPaper(localPaper);
  }

  public void setPagePrintRange(SetOfIntegerSyntax paramSetOfIntegerSyntax, int paramInt)
    throws PdfException
  {
    this.range = paramSetOfIntegerSyntax;
    this.start = paramSetOfIntegerSyntax.next(0);
    int i = 0;
    for (int j = 0; j < paramInt; j++)
      if (paramSetOfIntegerSyntax.contains(j))
        i++;
    this.listOfPages = new int[i + 1];
    j = this.start;
    this.end = this.start;
    if (paramSetOfIntegerSyntax.contains(2147483647))
    {
      this.end = paramInt;
    }
    else
    {
      while (paramSetOfIntegerSyntax.next(j) != -1)
        j++;
      this.end = j;
    }
    if (this.start > this.end)
    {
      k = this.start;
      this.start = this.end;
      this.end = k;
    }
    int k = 0;
    for (int m = this.start; m < this.end + 1; m++)
      if ((paramSetOfIntegerSyntax.contains(m)) && ((!this.oddPagesOnly) || ((m & 0x1) == 1)) && ((!this.evenPagesOnly) || ((m & 0x1) == 0)))
      {
        this.listOfPages[k] = (m - this.start);
        k++;
      }
    if ((this.start < 1) || (this.end < 1) || (this.start > paramInt) || (this.end > paramInt))
      throw new PdfException(Messages.getMessage("PdfViewerPrint.InvalidPageRange") + ' ' + this.start + ' ' + this.end);
  }

  public void setPrintPageMode(int paramInt)
  {
    this.oddPagesOnly = ((paramInt & 0x10) == 16);
    this.evenPagesOnly = ((paramInt & 0x20) == 32);
    this.pagesPrintedInReverse = ((paramInt & 0x40) == 64);
  }

  public void putPageFormat(Object paramObject, PageFormat paramPageFormat)
  {
    this.pageFormats.put(paramObject, paramPageFormat);
  }

  public int getCurrentPrintPage()
  {
    return this.currentPrintPage;
  }

  public final void stopPrinting()
  {
    this.stopPrinting = true;
  }

  public void setPagePrintRange(int paramInt1, int paramInt2, int paramInt3)
    throws PdfException
  {
    this.start = paramInt1;
    this.end = paramInt2;
    if (paramInt2 == 2147483647)
      paramInt2 = paramInt3;
    if (paramInt1 > paramInt2)
    {
      int i = paramInt1;
      paramInt1 = paramInt2;
      paramInt2 = i;
    }
    if ((paramInt1 < 1) || (paramInt2 < 1) || (paramInt1 > paramInt3) || (paramInt2 > paramInt3))
      throw new PdfException(Messages.getMessage("PdfViewerPrint.InvalidPageRange") + ' ' + paramInt1 + ' ' + paramInt2);
  }

  public void useLogicalPrintOffset(int paramInt)
  {
    this.logicalPageOffset = paramInt;
  }

  public boolean isPageSuccessful()
  {
    return this.operationSuccessful;
  }

  public String getPageFailureMessage()
  {
    return this.pageErrorMessages;
  }

  public void setPrintPageScalingMode(int paramInt)
  {
    this.pageScalingMode = paramInt;
  }

  public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt1, PdfObjectReader paramPdfObjectReader, PdfDecoder paramPdfDecoder, ExternalHandlers paramExternalHandlers, float paramFloat, PdfResources paramPdfResources, int paramInt2, DecoderOptions paramDecoderOptions)
    throws PrinterException
  {
    PdfPageData localPdfPageData = paramPdfDecoder.getPdfPageData();
    AcroRenderer localAcroRenderer = paramPdfDecoder.getFormRenderer();
    int i = paramPdfDecoder.getPageCount();
    int[] arrayOfInt1 = DecoderOptions.getFormsNoPrint();
    int[] arrayOfInt2 = paramDecoderOptions.getInstance_FormsNoPrint();
    this.scaling = paramPdfDecoder.getScaling();
    this.insetW = paramPdfDecoder.getInsetW();
    this.insetH = paramPdfDecoder.getInsetH();
    this.currentPdfFile = paramPdfObjectReader;
    this.externalHandlers = paramExternalHandlers;
    if (this.legacyPrintMode)
      paramInt1 -= this.logicalPageOffset;
    else
      paramInt1++;
    PageObject localPageObject = null;
    PdfObject localPdfObject = null;
    if (this.stopPrinting)
    {
      this.stopPrinting = false;
      return 1;
    }
    int j = this.end;
    int k = this.start;
    double d = 1.0D;
    Object localObject1;
    if (this.legacyPrintMode)
    {
      if ((this.range == null) && ((this.oddPagesOnly) || (this.evenPagesOnly)))
        paramInt1 *= 2;
      else if (this.range != null)
        paramInt1 = this.listOfPages[paramInt1];
      m = 0;
      if (m != 0)
        d = paramFloat;
      if (this.pagesPrintedInReverse)
      {
        k = this.start;
        j = this.end;
        paramInt1 = this.end - paramInt1;
      }
      else if ((this.end != -1) && (this.end < this.start))
      {
        k = this.end;
        j = this.start;
        paramInt1 = this.start - paramInt1;
      }
      else
      {
        paramInt1 = this.start + paramInt1;
      }
      if (this.end == -1)
        paramInt1++;
      localObject1 = (CustomFormPrint)paramExternalHandlers.getExternalHandler(14);
      if (localObject1 != null)
        localAcroRenderer.getCompData().setCustomPrintInterface((CustomFormPrint)localObject1);
    }
    int m = 0;
    this.currentPrintPage = paramInt1;
    if (paramInt1 > i)
    {
      this.currentPrintPage = -1;
      return 1;
    }
    if (this.legacyPrintMode)
    {
      if (((this.end == -1 ? 1 : 0) | (paramInt1 >= k ? 1 : 0) & (paramInt1 <= j ? 1 : 0)) == 0);
    }
    else
    {
      this.operationSuccessful = true;
      this.pageErrorMessages = "";
      localObject1 = paramPdfObjectReader.getReferenceforPage(paramInt1);
      if ((localObject1 != null) || (this.isCustomPrinting))
      {
        if ((paramPdfObjectReader == null) && (!this.isCustomPrinting))
          throw new PrinterException("File not open - did you call closePdfFile() inside a loop and not reopen");
        if ((!this.isCustomPrinting) && (this.lastPrintedPage != paramInt1))
        {
          localPageObject = new PageObject((String)localObject1);
          paramPdfObjectReader.readObject(localPageObject);
          localPdfObject = localPageObject.getDictionary(2004251818);
        }
        if (this.lastPrintedPage != paramInt1)
        {
          this.currentPrintDecoder = new PdfStreamDecoderForPrinting(paramPdfObjectReader, true, paramPdfResources.getPdfLayerList());
          this.currentPrintDecoder.setParameters(false, true, 15, 0, false);
          paramExternalHandlers.addHandlers(this.currentPrintDecoder);
          this.currentPrintDecoder.setIntValue(-15, paramPdfDecoder.textPrint);
          if (this.objectPrintStoreRef == null)
            this.objectPrintStoreRef = new ObjectStore();
          this.currentPrintDecoder.setObjectValue(-8, this.objectPrintStoreRef);
          this.currentPrintDecoder.setObjectValue(-18, localPdfPageData);
          this.currentPrintDecoder.setIntValue(-10, paramInt1);
          if (!this.isCustomPrinting)
            try
            {
              paramPdfResources.setupResources(this.currentPrintDecoder, false, localPdfObject, paramInt2, paramPdfObjectReader);
            }
            catch (PdfException localPdfException1)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localPdfException1.getMessage());
            }
        }
        Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
        localGraphics2D.setRenderingHints(ColorSpaces.hints);
        Rectangle localRectangle1 = paramGraphics.getClipBounds();
        if (ColorSpaceConvertor.isUsingARGB)
        {
          localObject2 = localGraphics2D.getTransform();
          localGraphics2D.translate(paramPageFormat.getImageableX(), paramPageFormat.getImageableY());
          Color localColor = new Color(255, 255, 255, 1);
          localGraphics2D.setColor(localColor);
          localGraphics2D.drawLine(0, 0, 1, 1);
          localGraphics2D.setTransform((AffineTransform)localObject2);
        }
        Object localObject2 = (DynamicVectorRenderer)this.currentPrintDecoder.getObjectValue(23);
        ((DynamicVectorRenderer)localObject2).setScalingValues(localPdfPageData.getCropBoxX(paramInt1) + 1, localPdfPageData.getCropBoxHeight(paramInt1), 1.0F);
        ((DynamicVectorRenderer)localObject2).setPrintPage(paramInt1);
        ((DynamicVectorRenderer)localObject2).setCustomColorHandler((ColorHandler)paramExternalHandlers.getExternalHandler(19));
        if (this.lastPrintedPage != paramInt1)
        {
          if (!this.isCustomPrinting)
            try
            {
              this.currentPrintDecoder.decodePageContent(localPageObject);
              if (this.overlays != null)
                this.overlays.printOverlays((DynamicVectorRenderer)localObject2, paramInt1);
            }
            catch (PdfException localPdfException2)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localPdfException2.getMessage());
              throw new PrinterException(localPdfException2.getMessage());
            }
          this.lastPrintedPage = paramInt1;
        }
        if ((this.duplexGapOdd != 0) || (this.duplexGapEven != 0))
        {
          localObject3 = localGraphics2D.getClip();
          if (paramInt1 % 2 != 1)
            localGraphics2D.translate(this.duplexGapEven, 0);
          else
            localGraphics2D.translate(this.duplexGapOdd, 0);
          if (localObject3 != null)
            localGraphics2D.setClip((Shape)localObject3);
        }
        Object localObject3 = paramPdfDecoder.getDisplayOffsets();
        int n = ((DisplayOffsets)localObject3).getUserPrintOffsetX();
        int i1 = -((DisplayOffsets)localObject3).getUserPrintOffsetY();
        if ((n != 0) || (i1 != 0))
          localGraphics2D.translate(n, i1);
        CustomPrintHintingHandler localCustomPrintHintingHandler = (CustomPrintHintingHandler)paramExternalHandlers.getExternalHandler(18);
        if (localCustomPrintHintingHandler != null)
        {
          if (this.printRender != null)
            this.printRender.stopG2HintSetting(true);
          localCustomPrintHintingHandler.preprint(localGraphics2D, paramPdfDecoder);
        }
        int i2 = localPdfPageData.getCropBoxX(paramInt1);
        int i3 = localPdfPageData.getCropBoxY(paramInt1);
        int i4 = localPdfPageData.getCropBoxWidth(paramInt1);
        int i5 = localPdfPageData.getCropBoxHeight(paramInt1);
        int i6 = localPdfPageData.getRotation(paramInt1);
        Rectangle localRectangle2 = new Rectangle(i2, i3, i4, i5);
        if (this.printOnlyVisible)
        {
          localObject4 = new SingleDisplay(paramPdfDecoder, paramDecoderOptions);
          localObject5 = ((SingleDisplay)localObject4).getDisplayedRectangle();
          localRectangle2 = workoutClipping(paramPdfDecoder.getDisplayRotation(), (Rectangle)localObject5, i4, i5);
        }
        Object localObject4 = new PdfPrintTransform(this.rotatePrintedPage, this.centerPrintedPage, this.pageScalingMode, this.usePDFPaperSize);
        Object localObject5 = ((PdfPrintTransform)localObject4).getPageTransform(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, i6, paramPageFormat);
        localGraphics2D.transform((AffineTransform)localObject5);
        Shape localShape = localGraphics2D.getClip();
        Object localObject6;
        if (localShape != null)
        {
          localObject6 = new Rectangle(localGraphics2D.getClipBounds());
          localObject6 = ((Rectangle2D)localObject6).createIntersection(localRectangle2);
          localGraphics2D.setClip((Shape)localObject6);
        }
        if (!this.isCustomPrinting)
        {
          this.currentPrintDecoder.print(localGraphics2D, null, this.currentPrintPage, null, localCustomPrintHintingHandler, paramPdfDecoder);
        }
        else if (this.printRender == null)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("No data for page " + paramInt1);
        }
        else
        {
          this.printRender.setG2(localGraphics2D);
          this.printRender.paint(null, null, localRectangle1);
        }
        localGraphics2D.setClip(localShape);
        if (localAcroRenderer != null)
        {
          localAcroRenderer.createDisplayComponentsForPage(paramInt1, this.currentPrintDecoder);
          localObject6 = toMap(arrayOfInt2);
          if (localObject6 == null)
            localObject6 = toMap(arrayOfInt1);
          localShape = localGraphics2D.getClip();
          localGraphics2D.setClip(localRectangle2);
          if (!localAcroRenderer.getCompData().formsRasterizedForDisplay())
            localAcroRenderer.getCompData().renderFormsOntoG2(localGraphics2D, paramInt1, 0, i6, (Map)localObject6, localAcroRenderer.getFormFactory(), localPdfPageData.getMediaBoxHeight(paramInt1));
          localGraphics2D.setClip(localShape);
        }
        if (localCustomPrintHintingHandler != null)
          localCustomPrintHintingHandler.postprint(localGraphics2D, paramPdfDecoder);
        if (!this.currentPrintDecoder.getBooleanValue(1))
        {
          this.operationSuccessful = false;
          this.pageErrorMessages += this.currentPrintDecoder.errorTracker.getPageFailureMessage();
        }
      }
    }
    if (!this.operationSuccessful)
      m = 1;
    return m;
  }

  private static Map toMap(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt == null) || (paramArrayOfInt.length == 0))
      return null;
    int i = paramArrayOfInt.length;
    HashMap localHashMap = new HashMap(i);
    for (int m : paramArrayOfInt)
      localHashMap.put(Integer.valueOf(m), "x");
    return localHashMap;
  }

  private Rectangle workoutClipping(int paramInt1, Rectangle paramRectangle, int paramInt2, int paramInt3)
  {
    double d1 = paramRectangle.getX();
    double d2 = paramRectangle.getY();
    double d3 = paramRectangle.getWidth();
    double d4 = paramRectangle.getHeight();
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    switch (paramInt1)
    {
    case 0:
      i = (int)((paramRectangle.x - this.insetW) / this.scaling);
      j = (int)(paramInt3 - (paramRectangle.y + paramRectangle.height - this.insetH) / this.scaling);
      k = (int)(paramRectangle.width / this.scaling - 1.0F);
      m = (int)(paramRectangle.height / this.scaling);
      break;
    case 90:
      i = (int)((d2 - this.insetH) / this.scaling);
      j = (int)((d1 - this.insetW) / this.scaling);
      k = (int)(d4 / this.scaling);
      m = (int)(d3 / this.scaling);
      break;
    case 180:
      j = (int)(d2 / this.scaling - this.insetH / this.scaling);
      i = (int)(paramInt2 - (d1 + d3 - this.insetW) / this.scaling);
      k = (int)(d3 / this.scaling);
      m = (int)(d4 / this.scaling);
      break;
    case 270:
      i = (int)(paramInt2 - (d2 + d4 - this.insetH) / this.scaling);
      j = (int)(paramInt3 - (d1 + d3 - this.insetW) / this.scaling);
      k = (int)(d4 / this.scaling);
      m = (int)(d3 / this.scaling);
    }
    Rectangle localRectangle = new Rectangle(i, j, k, m);
    return localRectangle;
  }

  public void printAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    if (this.overlays == null)
      this.overlays = new Overlays();
    this.overlays.printAdditionalObjectsOverPage(paramInt, paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
  }

  public void printAdditionalObjectsOverAllPages(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    if (this.overlays == null)
      this.overlays = new Overlays();
    this.overlays.printAdditionalObjectsOverAllPages(paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
  }

  public void clear()
  {
    if (this.overlays != null)
      this.overlays.clear();
    if (this.objectPrintStoreRef != null)
      this.objectPrintStoreRef.flush();
  }

  public void setPrintIndent(int paramInt1, int paramInt2)
  {
    this.duplexGapOdd = paramInt1;
    this.duplexGapEven = paramInt2;
  }

  public void setCenterOnScaling(boolean paramBoolean)
  {
    this.centerPrintedPage = paramBoolean;
  }

  public void setAutoRotate(boolean paramBoolean)
  {
    this.rotatePrintedPage = paramBoolean;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.SwingPrinter
 * JD-Core Version:    0.6.2
 */
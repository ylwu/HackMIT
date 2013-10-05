package org.jpedal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.display.Display;
import org.jpedal.display.DisplayOffsets;
import org.jpedal.display.PageOffsets;
import org.jpedal.display.swing.MultiDisplay;
import org.jpedal.display.swing.PageFlowDisplay;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.swing.SwingMouseFunctionality;
import org.jpedal.examples.viewer.gui.swing.SwingMouseListener;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.external.RenderChangeListener;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.DecryptionFactory;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.linear.LinearParser;
import org.jpedal.linear.LinearThread;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.acroforms.actions.DefaultActionHandler;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.structuredtext.MarkedContentGenerator;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.DecoderResults;
import org.jpedal.parser.SwingPrinter;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.DPIFactory;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public class PdfDecoder extends JPanel
  implements Printable, Pageable, PdfDecoderInt
{
  private BufferedImage previewImage = null;
  private String previewText;
  private final DecoderOptions options = new DecoderOptions();
  public SwingPrinter swingPrinter = new SwingPrinter();
  private ExternalHandlers externalHandlers = new ExternalHandlers();
  private final PdfResources res = new PdfResources();
  FileAccess fileAccess = new FileAccess(this.externalHandlers, this.res, this.options);
  private DecoderResults resultsFromDecode = new DecoderResults();
  Parser parser = new Parser(this.externalHandlers, this.options, this.fileAccess, this.res, this.resultsFromDecode);
  private DPIFactory scalingdpi = new DPIFactory();
  private DisplayOffsets displayOffsets = new DisplayOffsets(this, this.externalHandlers);
  public boolean useNewGraphicsMode = true;
  Display pages = null;
  private ActionHandler formsActionHandler;
  private ActionHandler userActionHandler;
  private int scrollInterval = 10;
  private boolean isNewRotationSet = false;
  int displayRotation = 0;
  int x_size = 100;
  int y_size = 100;
  private int max_x;
  private int max_y;
  public float scaling = 1.0F;
  protected Border myBorder = null;
  private RefreshLayout viewListener = null;
  private StatusBar statusBar = null;
  public int specialMode = -1;
  public int textPrint = 0;
  private Cursor defaultCursor = null;

  public boolean isOpen()
  {
    return this.fileAccess.isOpen();
  }

  private void drawPreviewImage(Graphics2D paramGraphics2D, Rectangle paramRectangle)
  {
    if (this.previewImage != null)
    {
      int i = this.previewImage.getWidth();
      int j = this.previewImage.getHeight();
      int k = paramGraphics2D.getFontMetrics().stringWidth(this.previewText);
      int m = i > k ? i : k;
      int n = paramRectangle.x + paramRectangle.width - 40 - m;
      int i1 = (paramRectangle.y + paramRectangle.height - 20 - j) / 2;
      Composite localComposite = paramGraphics2D.getComposite();
      paramGraphics2D.setPaint(Color.BLACK);
      paramGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.7F));
      paramGraphics2D.fill(new RoundRectangle2D.Double(n - 10, i1 - 10, m + 20, j + 35, 10.0D, 10.0D));
      paramGraphics2D.setComposite(localComposite);
      paramGraphics2D.setPaint(Color.WHITE);
      n += (m - i) / 2;
      paramGraphics2D.drawImage(this.previewImage, n, i1, null);
      int i2 = (i + 20 - k) / 2;
      paramGraphics2D.drawString(this.previewText, n + i2 - 10, i1 + j + 15);
    }
  }

  public Document getMarkedContent()
  {
    return new MarkedContentGenerator().getMarkedContentTree(this.res, this.fileAccess.getPdfPageData(), getIO());
  }

  public ExternalHandlers getExternalHandler()
  {
    return this.externalHandlers;
  }

  public int getDisplayRotation()
  {
    return this.displayRotation;
  }

  DecoderOptions getOptions()
  {
    return this.options;
  }

  public int getPageNumber()
  {
    return this.fileAccess.getPageNumber();
  }

  protected void setPageNumber(int paramInt)
  {
    this.fileAccess.setPageNumber(paramInt);
  }

  public void setDisplayRotation(int paramInt)
  {
    this.displayRotation = paramInt;
  }

  public Display getPages()
  {
    return this.pages;
  }

  public int getlastPageDecoded()
  {
    return this.fileAccess.getLastPageDecoded();
  }

  public DisplayOffsets getDisplayOffsets()
  {
    return this.displayOffsets;
  }

  public Iterator getPageInfo(int paramInt)
  {
    return this.resultsFromDecode.getPageInfo(paramInt);
  }

  public OutlineData getOutlineData()
  {
    return this.res.getOutlineData();
  }

  public boolean isLoadingLinearizedPDF()
  {
    return (this.fileAccess.linearParser.linearizedBackgroundReaderer != null) && (this.fileAccess.linearParser.linearizedBackgroundReaderer.isAlive());
  }

  protected void resetMultiPageForms(int paramInt)
  {
    this.pages.resetMultiPageForms(paramInt);
  }

  public void showExpiry()
  {
    System.out.println("Trial expires in " + FileAccess.bb + " days");
  }

  public void resetViewableArea()
  {
    this.pages.resetViewableArea();
  }

  public AffineTransform setViewableArea(Rectangle paramRectangle)
    throws PdfException
  {
    return this.pages.setViewableArea(paramRectangle);
  }

  public int getPageAlignment()
  {
    return this.options.getPageAlignment();
  }

  public static void init(boolean paramBoolean)
  {
    DecoderOptions.embedWidthData = paramBoolean;
  }

  public PdfDecoder(boolean paramBoolean)
  {
    this.options.setRenderPage(paramBoolean);
    if (!FontMappings.fontsInitialised)
    {
      FontMappings.initFonts();
      FontMappings.fontsInitialised = true;
    }
    if (paramBoolean)
    {
      setLayout(null);
      setPreferredSize(new Dimension(100, 100));
    }
  }

  public PdfDecoder()
  {
    this.options.setRenderPage(true);
    setLayout(null);
    if (!FontMappings.fontsInitialised)
    {
      FontMappings.initFonts();
      FontMappings.fontsInitialised = true;
    }
    setPreferredSize(new Dimension(100, 100));
  }

  public static void disposeAllStatic()
  {
    StandardFonts.dispose();
    FontMappings.dispose();
  }

  public final void dispose()
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      this.parser.disposeObjects();
    }
    else
    {
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          PdfDecoder.this.parser.disposeObjects();
        }
      };
      SwingUtilities.invokeLater(local1);
    }
  }

  public final void closePdfFile()
  {
    if (this.pages != null)
      this.pages.stopGeneratingPage();
    this.pages.disableScreen();
    if (this.viewListener != null)
    {
      this.pages.flushPageCaches();
      removeComponentListener(this.viewListener);
      this.viewListener.dispose();
      this.viewListener = null;
    }
    if (SwingUtilities.isEventDispatchThread())
    {
      validate();
    }
    else
    {
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          PdfDecoder.this.validate();
        }
      };
      SwingUtilities.invokeLater(local2);
    }
    if (this.externalHandlers.getFormRenderer() != null)
      this.externalHandlers.getFormRenderer().getCompData().dispose();
    this.swingPrinter.clear();
    this.fileAccess.closePdfFile();
  }

  public final PdfData getPdfBackgroundData()
  {
    return this.parser.getPdfBackgroundData();
  }

  public final PdfData getPdfData()
    throws PdfException
  {
    return this.parser.getPdfData();
  }

  public final boolean hasOutline()
  {
    return this.res.hasOutline();
  }

  public final Document getOutlineAsXML()
  {
    return this.res.getOutlineAsXML(getIO());
  }

  public final PdfPageData getPdfPageData()
  {
    return this.fileAccess.getPdfPageData();
  }

  public void setPagePrintRange(int paramInt1, int paramInt2)
    throws PdfException
  {
    this.swingPrinter.setPagePrintRange(paramInt1, paramInt2, this.fileAccess.getPageCount());
  }

  public void setTextPrint(int paramInt)
  {
    this.textPrint = paramInt;
  }

  public void useLogicalPrintOffset(int paramInt)
  {
    this.swingPrinter.useLogicalPrintOffset(paramInt);
  }

  public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
    throws PrinterException
  {
    return this.swingPrinter.print(paramGraphics, paramPageFormat, paramInt, getIO(), this, this.externalHandlers, this.scaling, this.res, getPageNumber(), this.options);
  }

  public synchronized BufferedImage getPageAsHiRes(int paramInt)
    throws PdfException
  {
    return getPageAsHiRes(paramInt, false);
  }

  public synchronized BufferedImage getPageAsHiRes(int paramInt, Map paramMap)
    throws PdfException
  {
    if (paramMap != null)
      modifyNonstaticJPedalParameters(paramMap);
    return getPageAsHiRes(paramInt);
  }

  public synchronized BufferedImage getPageAsHiRes(int paramInt, Map paramMap, boolean paramBoolean)
    throws PdfException
  {
    if (paramMap != null)
      this.options.set(paramMap);
    return getPageAsHiRes(paramInt, paramBoolean);
  }

  public synchronized BufferedImage getPageAsHiRes(int paramInt, boolean paramBoolean)
    throws PdfException
  {
    return this.parser.getPageAsHiRes(paramInt, paramBoolean);
  }

  public BufferedImage getPageAsImage(int paramInt)
    throws PdfException
  {
    BufferedImage localBufferedImage = getPageAsImage(paramInt, false);
    if (this.pages != null)
    {
      AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
      localAcroRenderer.getCompData().resetScaledLocation(this.pages.getOldScaling(), this.displayRotation, 0);
    }
    return localBufferedImage;
  }

  public BufferedImage getPageAsTransparentImage(int paramInt)
    throws PdfException
  {
    return getPageAsImage(paramInt, true);
  }

  public void renderPageOntoGraphics2D(float paramFloat, final int paramInt, final Graphics2D paramGraphics2D, boolean paramBoolean)
    throws Exception
  {
    this.pages.setAcceleration(false);
    setPageParameters(paramFloat, paramInt);
    Shape localShape = paramGraphics2D.getClip();
    if (paramInt != this.fileAccess.getLastPageDecoded())
      decodePage(paramInt);
    super.paintComponent(paramGraphics2D);
    if (paramBoolean)
    {
      paintComponent(paramGraphics2D);
      paint(paramGraphics2D);
    }
    else
    {
      threadSafePaint(paramGraphics2D);
      super.paint(paramGraphics2D);
      paint(paramGraphics2D);
    }
    AffineTransform localAffineTransform = paramGraphics2D.getTransform();
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    PdfPageData localPdfPageData = this.fileAccess.getPdfPageData();
    if (localAcroRenderer.hasFormsOnPage(paramInt))
    {
      paramGraphics2D.scale(paramFloat, -paramFloat);
      paramGraphics2D.translate(0, -(localPdfPageData.getCropBoxHeight(paramInt) + localPdfPageData.getCropBoxY(paramInt)));
      if (paramBoolean)
      {
        if (SwingUtilities.isEventDispatchThread())
        {
          localAcroRenderer.createDisplayComponentsForPage(paramInt, null);
          if (!localAcroRenderer.getCompData().formsRasterizedForDisplay())
            localAcroRenderer.getCompData().renderFormsOntoG2(paramGraphics2D, paramInt, 0, this.displayRotation, null, null, localPdfPageData.getMediaBoxHeight(paramInt));
        }
        else
        {
          Runnable local3 = new Runnable()
          {
            public void run()
            {
              AcroRenderer localAcroRenderer = PdfDecoder.this.externalHandlers.getFormRenderer();
              PdfPageData localPdfPageData = PdfDecoder.this.fileAccess.getPdfPageData();
              localAcroRenderer.createDisplayComponentsForPage(paramInt, null);
              if (!localAcroRenderer.getCompData().formsRasterizedForDisplay())
                localAcroRenderer.getCompData().renderFormsOntoG2(paramGraphics2D, paramInt, 0, PdfDecoder.this.displayRotation, null, null, localPdfPageData.getMediaBoxHeight(paramInt));
            }
          };
          SwingUtilities.invokeLater(local3);
        }
      }
      else
      {
        localAcroRenderer.createDisplayComponentsForPage(paramInt, null);
        if (!localAcroRenderer.getCompData().formsRasterizedForDisplay())
          localAcroRenderer.getCompData().renderFormsOntoG2(paramGraphics2D, paramInt, 0, this.displayRotation, null, null, localPdfPageData.getMediaBoxHeight(paramInt));
      }
    }
    paramGraphics2D.setTransform(localAffineTransform);
    paramGraphics2D.setClip(localShape);
  }

  private BufferedImage getPageAsImage(int paramInt, boolean paramBoolean)
    throws PdfException
  {
    this.parser.setParms(this.displayRotation, this.scaling, 0, this.specialMode);
    return this.parser.getPageAsImage(paramInt, paramBoolean);
  }

  public float getHiResUpscaleFactor()
  {
    return this.parser.getHiResUpscaleFactor();
  }

  public final void flushObjectValues(boolean paramBoolean)
  {
    this.parser.flushObjectValues(paramBoolean);
  }

  public final PdfImageData getPdfImageData()
  {
    return this.parser.getPdfImageData();
  }

  public final PdfImageData getPdfBackgroundImageData()
  {
    return this.parser.getPdfBackgroundImageData();
  }

  public final void setRenderMode(int paramInt)
  {
    this.parser.setRenderMode(paramInt);
  }

  public final void setExtractionMode(int paramInt)
  {
    this.parser.setExtractionMode(paramInt);
  }

  public void modifyNonstaticJPedalParameters(Map paramMap)
    throws PdfException
  {
    this.options.set(paramMap);
    if (paramMap.containsKey(JPedalSettings.DISPLAY_BACKGROUND))
      setBackground(this.options.getDisplayBackgroundColor());
  }

  public static void modifyJPedalParameters(Map paramMap)
    throws PdfException
  {
    if (paramMap != null)
      DecoderOptions.modifyJPedalParameters(paramMap);
  }

  public final PdfFileInformation getFileInformationData()
  {
    return this.res.getMetaData(getIO());
  }

  public final void setExtractionMode(int paramInt, float paramFloat)
  {
    this.scaling = paramFloat;
    this.parser.setExtractionMode(paramInt, paramFloat);
  }

  public DPIFactory getDPIFactory()
  {
    return this.scalingdpi;
  }

  public final void setPageParameters(float paramFloat, int paramInt)
  {
    this.fileAccess.setPageNumber(paramInt);
    this.parser.resetMultiplyer();
    if ((getDisplayView() == 5) && (paramFloat == -100.0F))
      return;
    if (paramFloat > 0.0F)
      this.scaling = paramFloat;
    else
      paramFloat = this.scaling;
    if (this.pages != null)
      this.pages.setScaling(paramFloat);
    PdfLayerList localPdfLayerList = this.res.getPdfLayerList();
    if (localPdfLayerList != null)
    {
      boolean bool = localPdfLayerList.setZoom(this.scalingdpi.removeScaling(paramFloat));
      if (bool)
        try
        {
          decodePage(-1);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
    }
    PdfPageData localPdfPageData = this.fileAccess.getPdfPageData();
    localPdfPageData.setScalingValue(paramFloat);
    int i = localPdfPageData.getMediaBoxWidth(paramInt);
    this.max_x = localPdfPageData.getMediaBoxWidth(paramInt);
    this.max_y = localPdfPageData.getMediaBoxHeight(paramInt);
    int j = localPdfPageData.getCropBoxWidth(paramInt);
    int k = localPdfPageData.getCropBoxHeight(paramInt);
    this.x_size = ((int)(j * paramFloat));
    this.y_size = ((int)(k * paramFloat));
    if ((!this.isNewRotationSet) && (getDisplayView() != 5))
      this.displayRotation = localPdfPageData.getRotation(paramInt);
    else
      this.isNewRotationSet = false;
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    localDynamicVectorRenderer.init(i, this.max_y, this.displayRotation, this.options.getPageColor());
    if ((localDynamicVectorRenderer.getType() != 4) && (localDynamicVectorRenderer.getType() != 5) && (localDynamicVectorRenderer.getType() != 7) && (localDynamicVectorRenderer.getType() != 6))
    {
      localDynamicVectorRenderer.setValue(1, this.options.getPageColor().getRGB());
      if (this.options.getTextColor() != null)
      {
        localDynamicVectorRenderer.setValue(2, this.options.getTextColor().getRGB());
        if (this.options.getChangeTextAndLine())
          localDynamicVectorRenderer.setValue(3, 1);
        else
          localDynamicVectorRenderer.setValue(3, 0);
      }
    }
    setDisplayRotation(this.displayRotation);
    this.pages.setPageRotation(this.displayRotation);
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (localAcroRenderer != null)
      localAcroRenderer.getCompData().setForceRedraw(true);
  }

  public final void setPageParameters(float paramFloat, int paramInt1, int paramInt2)
  {
    this.isNewRotationSet = true;
    this.displayRotation = paramInt2;
    if (getDisplayView() == 5)
      this.pages.init(0.0F, this.displayRotation, 0, null, false);
    else
      setPageParameters(paramFloat, paramInt1);
  }

  public void setStatusBarObject(StatusBar paramStatusBar)
  {
    this.statusBar = paramStatusBar;
  }

  public void waitForDecodingToFinish()
  {
    this.fileAccess.waitForDecodingToFinish();
  }

  public void updatePageNumberDisplayed(int paramInt)
  {
    Object localObject = this.externalHandlers.getExternalHandler(4);
    if ((paramInt != -1) && (localObject != null))
      ((GUIFactory)localObject).setPage(paramInt);
  }

  public void setPreviewThumbnail(BufferedImage paramBufferedImage, String paramString)
  {
    this.previewImage = paramBufferedImage;
    this.previewText = paramString;
  }

  public DynamicVectorRenderer getDynamicRenderer()
  {
    return this.fileAccess.getDynamicRenderer();
  }

  public DynamicVectorRenderer getDynamicRenderer(boolean paramBoolean)
  {
    return this.fileAccess.getDynamicRenderer(paramBoolean);
  }

  public void setPDFCursor(Cursor paramCursor)
  {
    if (SingleDisplay.allowChangeCursor)
      setCursor(paramCursor);
  }

  public void setDefaultCursor(Cursor paramCursor)
  {
    this.defaultCursor = paramCursor;
    if (SingleDisplay.allowChangeCursor)
      setCursor(paramCursor);
  }

  public void setCursor(Cursor paramCursor)
  {
    if (SingleDisplay.allowChangeCursor)
      if ((paramCursor.getType() == 0) && (this.defaultCursor != null))
        super.setCursor(this.defaultCursor);
      else
        super.setCursor(paramCursor);
  }

  public final void decodePage(int paramInt)
    throws Exception
  {
    if (getDisplayView() != 1)
      return;
    boolean bool = isPageAvailable(paramInt);
    PdfObject localPdfObject = this.fileAccess.linearParser.getLinearPageObject();
    if (!bool)
      return;
    if ((bool) && (localPdfObject != null))
      this.fileAccess.readAllPageReferences(true, localPdfObject, new HashMap(1000), new HashMap(1000), paramInt, getFormRenderer(), this.res, this.options.getInsetW(), this.options.getInsetH());
    this.parser.setStatusBar(this.statusBar);
    this.parser.setParms(this.displayRotation, this.scaling, (int)this.pages.getIndent(), this.specialMode);
    this.pages.setCursorBoxOnScreen(null, paramInt == this.fileAccess.getLastPageDecoded());
    this.parser.decodePage(paramInt);
    this.pages.refreshDisplay();
  }

  public synchronized boolean isPageAvailable(int paramInt)
  {
    return this.parser.isPageAvailable(paramInt);
  }

  public void printAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    this.swingPrinter.printAdditionalObjectsOverPage(paramInt, paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
  }

  public void printAdditionalObjectsOverAllPages(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    this.swingPrinter.printAdditionalObjectsOverAllPages(paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
  }

  public void drawAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    if (paramInt == getPageNumber())
      localDynamicVectorRenderer.drawAdditionalObjectsOverPage(paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
    this.pages.refreshDisplay();
  }

  public void flushAdditionalObjectsOnPage(int paramInt)
    throws PdfException
  {
    if (paramInt == getPageNumber())
    {
      DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
      localDynamicVectorRenderer.flushAdditionalObjOnPage();
    }
    this.pages.refreshDisplay();
  }

  public boolean isHiResScreenDisplay()
  {
    return this.options.useHiResImageForDisplay();
  }

  public void useHiResScreenDisplay(boolean paramBoolean)
  {
    this.options.useHiResImageForDisplay(paramBoolean);
  }

  public final synchronized void decodePageInBackground(int paramInt)
    throws Exception
  {
    this.parser.decodePageInBackground(paramInt);
  }

  public final int getPageCount()
  {
    return this.fileAccess.getPageCount();
  }

  public final boolean isEncrypted()
  {
    return this.fileAccess.isEncrypted();
  }

  public final boolean isPasswordSupplied()
  {
    return this.fileAccess.isPasswordSupplied(getIO());
  }

  public boolean isFileViewable()
  {
    return this.fileAccess.isFileViewable(getIO());
  }

  public boolean isExtractionAllowed()
  {
    if (getIO() != null)
    {
      PdfFileReader localPdfFileReader = getIO().getObjectReader();
      DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
      return (localDecryptionFactory == null) || (localDecryptionFactory.getBooleanValue(103));
    }
    return false;
  }

  public final void setEncryptionPassword(String paramString)
    throws PdfException
  {
    if (getIO() == null)
      throw new PdfException("Must open PdfDecoder file first");
    getIO().getObjectReader().setPassword(paramString);
    if (getIO() != null)
      try
      {
        preOpen();
        this.fileAccess.openPdfFile();
        postOpen();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " opening file");
      }
  }

  public final void openPdfArray(byte[] paramArrayOfByte)
    throws PdfException
  {
    if (paramArrayOfByte == null)
      throw new RuntimeException("Attempting to open null byte stream");
    preOpen();
    if (this.fileAccess.isOpen)
      closePdfFile();
    this.fileAccess.openPdfArray(paramArrayOfByte);
    postOpen();
  }

  public void openPdfFile(String paramString, Certificate paramCertificate, PrivateKey paramPrivateKey)
    throws PdfException
  {
    this.fileAccess.setUserEncryption(paramCertificate, paramPrivateKey);
    openPdfFile(paramString);
  }

  public final void openPdfFileFromStream(Object paramObject, String paramString)
    throws PdfException
  {
    preOpen();
    this.fileAccess.openPdfFileFromStream(paramObject, paramString);
    postOpen();
  }

  public final void openPdfFile(String paramString)
    throws PdfException
  {
    if ((this.fileAccess.isOpen) && (this.fileAccess.linearParser.linearizedBackgroundReaderer == null))
      closePdfFile();
    preOpen();
    this.fileAccess.openPdfFile(paramString);
    postOpen();
  }

  public final void openPdfFile(String paramString1, String paramString2)
    throws PdfException
  {
    if (this.fileAccess.isOpen)
      closePdfFile();
    preOpen();
    this.fileAccess.openPdfFile(paramString1, paramString2);
    postOpen();
  }

  public final boolean openPdfFileFromURL(String paramString, boolean paramBoolean)
    throws PdfException
  {
    preOpen();
    InputStream localInputStream = null;
    String str = null;
    try
    {
      URL localURL = new URL(paramString);
      str = localURL.getPath().substring(localURL.getPath().lastIndexOf('/') + 1);
      localInputStream = localURL.openStream();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    boolean bool = this.fileAccess.readFile(paramBoolean, localInputStream, str, null);
    postOpen();
    return bool;
  }

  public final boolean openPdfFileFromURL(String paramString1, boolean paramBoolean, String paramString2)
    throws PdfException
  {
    InputStream localInputStream = null;
    String str = null;
    try
    {
      URL localURL = new URL(paramString1);
      str = localURL.getPath().substring(localURL.getPath().lastIndexOf('/') + 1);
      localInputStream = localURL.openStream();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    preOpen();
    boolean bool = this.fileAccess.readFile(paramBoolean, localInputStream, str, paramString2);
    postOpen();
    return bool;
  }

  public final boolean openPdfFileFromInputStream(InputStream paramInputStream, boolean paramBoolean)
    throws PdfException
  {
    String str = "inputstream" + System.currentTimeMillis() + '-' + this.fileAccess.getObjectStore().getKey() + ".pdf";
    preOpen();
    boolean bool = this.fileAccess.readFile(paramBoolean, paramInputStream, str, null);
    postOpen();
    return bool;
  }

  public final boolean openPdfFileFromInputStream(InputStream paramInputStream, boolean paramBoolean, String paramString)
    throws PdfException
  {
    String str = "inputstream" + System.currentTimeMillis() + '-' + this.fileAccess.getObjectStore().getKey() + ".pdf";
    preOpen();
    boolean bool = this.fileAccess.readFile(paramBoolean, paramInputStream, str, paramString);
    postOpen();
    return bool;
  }

  private void postOpen()
  {
    if (this.fileAccess.getPageCount() < 2)
      this.options.setDisplayView(1);
    else
      this.options.setDisplayView(this.options.getPageMode());
    setDisplayView(getDisplayView(), this.options.getPageAlignment());
    this.formsActionHandler.init(this, this.externalHandlers.getJavaScript(), getFormRenderer());
  }

  private void preOpen()
  {
    this.pages.disableScreen();
    this.pages.stopGeneratingPage();
    this.pages.resetCachedValues();
    this.fileAccess.setDecoding(true);
    this.options.setDisplayView(2);
    setDisplayView(1, this.options.getPageAlignment());
    if (this.viewListener != null)
    {
      this.pages.flushPageCaches();
      removeComponentListener(this.viewListener);
      this.viewListener.dispose();
      this.viewListener = null;
    }
    this.swingPrinter.lastPrintedPage = -1;
    this.swingPrinter.currentPrintDecoder = null;
    this.fileAccess.setDecoding(false);
    Object localObject = this.externalHandlers.getExternalHandler(6);
    this.externalHandlers.openPdfFile(localObject);
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (this.userActionHandler != null)
      this.formsActionHandler = this.userActionHandler;
    else
      this.formsActionHandler = new DefaultActionHandler();
    localAcroRenderer.resetHandler(this.formsActionHandler, this.scalingdpi.getDpi(), this.externalHandlers.getJavaScript());
    localAcroRenderer.getCompData().setRootDisplayComponent(this);
  }

  public Object getJPedalObject(int paramInt)
  {
    return this.parser.getJPedalObject(paramInt);
  }

  public void setPageMode(int paramInt)
  {
    this.options.setPageMode(paramInt);
  }

  public boolean isXMLExtraction()
  {
    return this.options.isXMLExtraction();
  }

  public void useTextExtraction()
  {
    this.options.setXMLExtraction(false);
  }

  public void useXMLExtraction()
  {
    this.options.setXMLExtraction(true);
  }

  public void clearScreen()
  {
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    localDynamicVectorRenderer.flush();
    this.pages.refreshDisplay();
  }

  public void setStreamCacheSize(int paramInt)
  {
    this.fileAccess.setStreamCacheSize(paramInt);
  }

  public boolean hasEmbeddedFonts()
  {
    return this.resultsFromDecode.hasEmbeddedFonts();
  }

  public final boolean PDFContainsEmbeddedFonts()
    throws Exception
  {
    return this.fileAccess.PDFContainsEmbeddedFonts();
  }

  public int getPageFromObjectRef(String paramString)
  {
    return getIO().convertObjectToPageNumber(paramString);
  }

  public String getInfo(int paramInt)
  {
    return this.parser.getInfo(paramInt);
  }

  public AcroRenderer getFormRenderer()
  {
    return this.externalHandlers.getFormRenderer();
  }

  public Javascript getJavaScript()
  {
    return this.externalHandlers.getJavaScript();
  }

  public boolean isPageSuccessful()
  {
    return this.swingPrinter.isPageSuccessful();
  }

  public String getPageDecodeReport()
  {
    return this.parser.getPageDecodeReport();
  }

  public String getPageFailureMessage()
  {
    return this.swingPrinter.getPageFailureMessage();
  }

  public BufferedImage getSelectedRectangleOnscreen(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    BufferedImage localBufferedImage = SingleDisplay.getSelectedRectangleOnscreen(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, getPageNumber(), this.fileAccess.getPdfPageData(), localAcroRenderer, this.fileAccess.getDynamicRenderer());
    localAcroRenderer.getCompData().resetScaledLocation(this.pages.getOldScaling(), this.displayRotation, 0);
    return localBufferedImage;
  }

  public ObjectStore getObjectStore()
  {
    return this.fileAccess.getObjectStore();
  }

  public void setObjectStore(ObjectStore paramObjectStore)
  {
    this.fileAccess.setObjectStore(paramObjectStore);
  }

  public DecoderOptions getDecoderOptions()
  {
    return this.options;
  }

  public PdfGroupingAlgorithms getGroupingObject()
    throws PdfException
  {
    return this.parser.getGroupingObject();
  }

  public PdfGroupingAlgorithms getBackgroundGroupingObject()
  {
    return this.parser.getBackgroundGroupingObject();
  }

  public final String getPDFVersion()
  {
    if (getIO() == null)
      return "";
    return getIO().getObjectReader().getType();
  }

  public void resetForNonPDFPage(int paramInt)
  {
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    localDynamicVectorRenderer.setHiResImageForDisplayMode(false);
    this.parser.setFontsInFile("");
    this.fileAccess.setPageCount(paramInt);
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (localAcroRenderer != null)
      localAcroRenderer.removeDisplayComponentsFromScreen();
    this.fileAccess.setPageData(new PdfPageData());
  }

  public void setDisplayView(int paramInt1, int paramInt2)
  {
    this.options.setPageAlignment(paramInt2);
    if (this.pages != null)
      this.pages.stopGeneratingPage();
    int i = (paramInt1 != 1) || (getDisplayView() != 1) ? 1 : 0;
    if ((i != 0) && ((getDisplayView() == 3) || (paramInt1 == 3)))
      i = 0;
    if (paramInt1 != 1)
      i = 1;
    int j = paramInt1 != getDisplayView() ? 1 : 0;
    int k = getDisplayView();
    this.options.setDisplayView(paramInt1);
    if ((k != paramInt1) && (k == 5))
      this.pages.dispose();
    Object localObject1 = this.externalHandlers.getExternalHandler(4);
    switch (paramInt1)
    {
    case 1:
      if ((this.pages == null) || (j != 0))
      {
        localObject2 = this.fileAccess.getDynamicRenderer();
        this.pages = new SingleDisplay(getPageNumber(), (DynamicVectorRenderer)localObject2, this, this.options);
      }
      break;
    case 5:
      if ((this.pages instanceof PageFlowDisplay))
        return;
      if (k != 1)
      {
        setDisplayView(1, 0);
        setDisplayView(5, 0);
        return;
      }
      ((JScrollPane)getParent().getParent()).setVerticalScrollBarPolicy(21);
      ((JScrollPane)getParent().getParent()).setHorizontalScrollBarPolicy(31);
      this.pages = new PageFlowDisplay(getPageNumber(), localObject1, this);
      break;
    default:
      if (i != 0)
      {
        this.pages = new MultiDisplay(getPageNumber(), null, paramInt1, localObject1, this, this.options, this.fileAccess);
        setDisplayRotation(this.displayRotation);
        this.pages.setPageRotation(this.displayRotation);
      }
      else
      {
        this.pages = new MultiDisplay(getPageNumber(), this.fileAccess.getDynamicRenderer(), paramInt1, localObject1, this, this.options, this.fileAccess);
      }
      localObject2 = (RenderChangeListener)this.externalHandlers.getExternalHandler(21);
      if (localObject2 != null)
        this.pages.setObjectValue(21, localObject2);
      break;
    }
    if (this.viewListener != null)
    {
      removeComponentListener(this.viewListener);
      this.viewListener.dispose();
      this.viewListener = null;
    }
    if ((k == 5) && (paramInt1 != 5))
    {
      removeAll();
      setLayout(null);
      ((JScrollPane)getParent().getParent()).setVerticalScrollBarPolicy(20);
      ((JScrollPane)getParent().getParent()).setHorizontalScrollBarPolicy(30);
      localObject2 = new javax.swing.Timer(1000, new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PdfDecoder.this.repaint();
        }
      });
      ((javax.swing.Timer)localObject2).setRepeats(false);
      ((javax.swing.Timer)localObject2).start();
    }
    if (this.fileAccess.getOffset() == null)
      this.fileAccess.setOffset(new PageOffsets(this.fileAccess.getPageCount(), this.fileAccess.getPdfPageData()));
    this.pages.setup(this.options.useHardwareAcceleration(), this.fileAccess.getOffset());
    Object localObject2 = this.fileAccess.getDynamicRenderer();
    this.pages.init(this.scaling, this.displayRotation, getPageNumber(), (DynamicVectorRenderer)localObject2, true);
    this.pages.forceRedraw();
    this.pages.refreshDisplay();
    if (this.viewListener == null)
    {
      this.viewListener = new RefreshLayout(null);
      addComponentListener(this.viewListener);
    }
    int m = getPageNumber();
    if (m > 0)
      if ((j != 0) && (paramInt1 == 1))
      {
        try
        {
          setPageParameters(this.scaling, m, this.displayRotation);
          invalidate();
          updateUI();
          decodePage(m);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
      }
      else if ((paramInt1 != 1) && (paramInt1 != 5))
      {
        int n = this.pages.getYCordForPage(m, -2.0F);
        Rectangle localRectangle = getVisibleRect();
        scrollRectToVisible(new Rectangle(0, n, (int)localRectangle.getWidth() - 1, (int)localRectangle.getHeight() - 1));
        scrollRectToVisible(new Rectangle(0, n, (int)localRectangle.getWidth() - 1, (int)localRectangle.getHeight() - 1));
      }
  }

  public boolean hasAllImages()
  {
    return this.resultsFromDecode.getImagesProcessedFully();
  }

  public boolean getPageDecodeStatus(int paramInt)
  {
    return this.resultsFromDecode.getPageDecodeStatus(paramInt);
  }

  public String getPageDecodeStatusReport(int paramInt)
  {
    return this.resultsFromDecode.getPageDecodeStatusReport(paramInt);
  }

  public void setPrintAutoRotateAndCenter(boolean paramBoolean)
  {
    this.swingPrinter.setCenterOnScaling(paramBoolean);
    this.swingPrinter.setAutoRotate(paramBoolean);
  }

  public void setPrintCurrentView(boolean paramBoolean)
  {
    this.swingPrinter.printOnlyVisible = paramBoolean;
  }

  public void addExternalHandler(Object paramObject, int paramInt)
  {
    switch (paramInt)
    {
    case 5:
      this.userActionHandler = ((ActionHandler)paramObject);
      break;
    case 26:
      SwingMouseListener.setCustomMouseFunctions((SwingMouseFunctionality)paramObject);
      break;
    case 8:
      this.pages.setThumbnailPanel((GUIThumbnailPanel)paramObject);
      break;
    default:
      this.externalHandlers.addExternalHandler(paramObject, paramInt);
    }
  }

  public Object getExternalHandler(int paramInt)
  {
    switch (paramInt)
    {
    case 16:
      return this.pages;
    case 17:
      return this.fileAccess.getOffset();
    }
    return this.externalHandlers.getExternalHandler(paramInt);
  }

  public PdfObjectReader getIO()
  {
    return this.parser.getIO();
  }

  public String getFileName()
  {
    return this.fileAccess.getFilename();
  }

  public boolean isForm()
  {
    return this.res.isForm();
  }

  public Printable getPrintable(int paramInt)
    throws IndexOutOfBoundsException
  {
    return this;
  }

  public void setAllowDifferentPrintPageSizes(boolean paramBoolean)
  {
    this.swingPrinter.allowDifferentPrintPageSizes = paramBoolean;
  }

  public TextLines getTextLines()
  {
    return this.parser.getTextLines();
  }

  public final void setInset(int paramInt1, int paramInt2)
  {
    this.options.setInset(paramInt1, paramInt2);
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (localAcroRenderer != null)
      localAcroRenderer.setInsets(paramInt1, paramInt2);
  }

  public void ensurePointIsVisible(Point paramPoint)
  {
    super.scrollRectToVisible(new Rectangle(paramPoint.x, this.y_size - paramPoint.y, this.scrollInterval, this.scrollInterval));
  }

  public void setPrintIndent(int paramInt1, int paramInt2)
  {
    this.swingPrinter.setPrintIndent(paramInt1, paramInt2);
  }

  public void setUserOffsets(int paramInt1, int paramInt2, int paramInt3)
  {
    this.displayOffsets.setUserOffsets(paramInt1, paramInt2, paramInt3);
  }

  public Point getUserOffsets(int paramInt)
  {
    return this.displayOffsets.getUserOffsets(paramInt);
  }

  public final Dimension getMaximumSize()
  {
    Dimension localDimension = null;
    int i = this.options.getDisplayView();
    if (i != 1)
      localDimension = new Dimension(this.pages.getPageSize(i)[0], this.pages.getPageSize(i)[1]);
    int j = this.options.getInsetW();
    int k = this.options.getInsetH();
    if (localDimension == null)
      if ((this.displayRotation == 90) || (this.displayRotation == 270))
        localDimension = new Dimension(this.y_size + j + j, this.x_size + k + k);
      else
        localDimension = new Dimension(this.x_size + j + j, this.y_size + k + k);
    if (localDimension == null)
      localDimension = getMinimumSize();
    return localDimension;
  }

  public final Dimension getMinimumSize()
  {
    return new Dimension(100 + this.options.getInsetW(), 100 + this.options.getInsetH());
  }

  public Dimension getPreferredSize()
  {
    return getMaximumSize();
  }

  public final void updateCursorBoxOnScreen(Rectangle paramRectangle, Color paramColor)
  {
    if (this.options.getDisplayView() != 1)
      return;
    this.pages.updateCursorBoxOnScreen(paramRectangle, paramColor, getPageNumber(), this.x_size, this.y_size);
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      super.paint(paramGraphics);
      if (!this.fileAccess.isDecoding())
        this.pages.drawCursor(paramGraphics, this.scaling);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
      this.pages.flushPageCaches();
    }
    catch (Error localError)
    {
      this.pages.flushPageCaches();
      this.pages.stopGeneratingPage();
      super.paint(paramGraphics);
    }
  }

  public void paintComponent(Graphics paramGraphics)
  {
    final RenderChangeListener localRenderChangeListener = (RenderChangeListener)this.externalHandlers.getExternalHandler(21);
    if (localRenderChangeListener != null)
      localRenderChangeListener.renderingStarted(getPageNumber());
    super.paintComponent(paramGraphics);
    if (SwingUtilities.isEventDispatchThread())
    {
      threadSafePaint(paramGraphics);
      if (localRenderChangeListener != null)
        localRenderChangeListener.renderingEnded(getPageNumber());
    }
    else
    {
      final Graphics localGraphics = paramGraphics;
      final int i = getPageNumber();
      Runnable local5 = new Runnable()
      {
        public void run()
        {
          PdfDecoder.this.threadSafePaint(localGraphics);
          if (localRenderChangeListener != null)
            localRenderChangeListener.renderingEnded(i);
        }
      };
      SwingUtilities.invokeLater(local5);
    }
  }

  synchronized void threadSafePaint(Graphics paramGraphics)
  {
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    int i = this.options.getDisplayView();
    if ((!this.fileAccess.isDecoding()) && (this.options.getRenderPage()))
    {
      Rectangle localRectangle = getVisibleRect();
      AffineTransform localAffineTransform = localGraphics2D.getTransform();
      this.pages.init(this.scaling, this.displayRotation, this.fileAccess.getPageNumber(), localDynamicVectorRenderer, false);
      this.pages.paintPage(localGraphics2D, this.externalHandlers.getFormRenderer(), this.parser.getTextLines());
      this.pages.drawBorder();
      localGraphics2D.setTransform(localAffineTransform);
      this.pages.drawFacing(localRectangle);
      if (i == 1)
        drawPreviewImage(localGraphics2D, getVisibleRect());
    }
    else
    {
      localDynamicVectorRenderer.setG2(localGraphics2D);
      localDynamicVectorRenderer.paintBackground(null);
    }
  }

  public final int getPDFWidth()
  {
    int i = this.options.getInsetW();
    if ((this.displayRotation == 90) || (this.displayRotation == 270))
      return this.y_size + i + i;
    return this.x_size + i + i;
  }

  public final int getPDFHeight()
  {
    int i = this.options.getInsetH();
    if ((this.displayRotation == 90) || (this.displayRotation == 270))
      return this.x_size + i + i;
    return this.y_size + i + i;
  }

  public final void setPDFBorder(Border paramBorder)
  {
    this.myBorder = paramBorder;
  }

  public final Border getPDFBorder()
  {
    return this.myBorder;
  }

  public void setHardwareAccelerationforScreen(boolean paramBoolean)
  {
    this.options.useHardwareAcceleration(paramBoolean);
  }

  public int getScrollInterval()
  {
    return this.scrollInterval;
  }

  public void setScrollInterval(int paramInt)
  {
    this.scrollInterval = paramInt;
  }

  public int getDisplayView()
  {
    return this.options.getDisplayView();
  }

  public void setPrintPageScalingMode(int paramInt)
  {
    this.swingPrinter.setPrintPageScalingMode(paramInt);
  }

  public void setUsePDFPaperSize(boolean paramBoolean)
  {
    this.swingPrinter.usePDFPaperSize = paramBoolean;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public int getInsetH()
  {
    return this.options.getInsetH();
  }

  public int getInsetW()
  {
    return this.options.getInsetW();
  }

  public Rectangle getCursorBoxOnScreen()
  {
    return this.pages.getCursorBoxOnScreen();
  }

  public int getNumberOfPages()
  {
    return this.swingPrinter.getNumberOfPages(this.fileAccess.getPageCount());
  }

  public PageFormat getPageFormat(int paramInt)
    throws IndexOutOfBoundsException
  {
    PageFormat localPageFormat = this.swingPrinter.getPageFormat(paramInt, this.fileAccess.getPdfPageData(), this.fileAccess.getPageCount());
    if (localPageFormat == null)
      localPageFormat = new PageFormat();
    return localPageFormat;
  }

  public PageFormat getUserSetPageFormat(int paramInt)
    throws IndexOutOfBoundsException
  {
    return this.swingPrinter.getPageFormat(paramInt, this.fileAccess.getPdfPageData(), this.fileAccess.getPageCount());
  }

  public void setCenterOnScaling(boolean paramBoolean)
  {
    this.swingPrinter.setCenterOnScaling(paramBoolean);
  }

  public void setPrintAutoRotate(boolean paramBoolean)
  {
    this.swingPrinter.setAutoRotate(paramBoolean);
  }

  public void setPageFormat(int paramInt, PageFormat paramPageFormat)
  {
    this.swingPrinter.putPageFormat(Integer.valueOf(paramInt), paramPageFormat);
  }

  public void setPageFormat(PageFormat paramPageFormat)
  {
    this.swingPrinter.putPageFormat("standard", paramPageFormat);
  }

  public void setPagePrintRange(SetOfIntegerSyntax paramSetOfIntegerSyntax)
    throws PdfException
  {
    if (paramSetOfIntegerSyntax == null)
      throw new PdfException("[PDF] null page range entered");
    this.swingPrinter.setPagePrintRange(paramSetOfIntegerSyntax, this.fileAccess.getPageCount());
  }

  public void setPrintPageMode(int paramInt)
  {
    this.swingPrinter.setPrintPageMode(paramInt);
  }

  public final void stopPrinting()
  {
    this.swingPrinter.stopPrinting();
  }

  public int getCurrentPrintPage()
  {
    return this.swingPrinter.getCurrentPrintPage();
  }

  public void resetCurrentPrintPage()
  {
    this.swingPrinter.currentPrintPage = 0;
    getFormRenderer().getCompData().resetAfterPrinting();
  }

  static
  {
    try
    {
      String str = System.getProperty("os.name");
      if (str.equals("Mac OS X"))
        DecoderOptions.isRunningOnMac = true;
      else if (str.startsWith("Windows"))
        DecoderOptions.isRunningOnWindows = true;
      else if (str.startsWith("AIX"))
        DecoderOptions.isRunningOnAIX = true;
      else if (str.equals("Linux"))
        DecoderOptions.isRunningOnLinux = true;
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException1.getMessage());
    }
    try
    {
      DecoderOptions.javaVersion = Float.parseFloat(System.getProperty("java.specification.version"));
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException2.getMessage());
    }
  }

  private class RefreshLayout extends ComponentAdapter
  {
    java.util.Timer t2 = null;

    private RefreshLayout()
    {
    }

    public void componentMoved(ComponentEvent paramComponentEvent)
    {
      startTimer();
    }

    public void componentResized(ComponentEvent paramComponentEvent)
    {
      startTimer();
    }

    private void startTimer()
    {
      if (this.t2 != null)
        this.t2.cancel();
      PageListener localPageListener = new PageListener();
      this.t2 = new java.util.Timer();
      this.t2.schedule(localPageListener, 500L);
    }

    public void dispose()
    {
      if (this.t2 != null)
        this.t2.cancel();
    }

    class PageListener extends TimerTask
    {
      PageListener()
      {
      }

      public void run()
      {
        try
        {
          if (PdfDecoder.this.pages != null)
          {
            PdfDecoder.this.pages.stopGeneratingPage();
            if (PdfDecoder.this.getPageNumber() < 1)
              PdfDecoder.this.fileAccess.setPageNumber(1);
            if (PdfDecoder.this.pages != null)
              PdfDecoder.this.pages.decodeOtherPages(PdfDecoder.this.getPageNumber(), PdfDecoder.this.fileAccess.getPageCount());
          }
          PdfDecoder.RefreshLayout.this.t2.cancel();
        }
        catch (Exception localException)
        {
          LogWriter.writeLog("Exception in run() " + localException.getMessage());
        }
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.PdfDecoder
 * JD-Core Version:    0.6.2
 */
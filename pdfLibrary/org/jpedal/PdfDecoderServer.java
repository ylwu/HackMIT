package org.jpedal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jpedal.display.Display;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.DecryptionFactory;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
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
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.DPIFactory;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public class PdfDecoderServer
  implements PdfDecoderInt
{
  private final DecoderOptions options = new DecoderOptions();
  private ExternalHandlers externalHandlers = new ExternalHandlers(true);
  private final PdfResources res = new PdfResources();
  FileAccess fileAccess = new FileAccess(this.externalHandlers, this.res, this.options);
  private DecoderResults resultsFromDecode = new DecoderResults();
  Parser parser = new Parser(this.externalHandlers, this.options, this.fileAccess, this.res, this.resultsFromDecode);
  private DPIFactory scalingdpi = new DPIFactory();
  private ActionHandler formsActionHandler;
  private ActionHandler userActionHandler;
  private boolean isNewRotationSet = false;
  int displayRotation = 0;
  int x_size = 100;
  int y_size = 100;
  int max_x;
  int max_y;
  float scaling = 1.0F;
  protected int specialMode = -1;

  public boolean isOpen()
  {
    return this.fileAccess.isOpen();
  }

  public Document getMarkedContent()
  {
    return new MarkedContentGenerator().getMarkedContentTree(this.res, this.fileAccess.getPdfPageData(), getIO());
  }

  protected ExternalHandlers getExternalHandler()
  {
    return this.externalHandlers;
  }

  protected PdfResources getRes()
  {
    return this.res;
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
    return null;
  }

  public int getlastPageDecoded()
  {
    return this.fileAccess.getLastPageDecoded();
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

  public void showExpiry()
  {
    System.out.println("Trial expires in " + FileAccess.bb + " days");
  }

  public void resetViewableArea()
  {
  }

  public int getPageAlignment()
  {
    return this.options.getPageAlignment();
  }

  public static void init(boolean paramBoolean)
  {
    DecoderOptions.embedWidthData = paramBoolean;
  }

  public PdfDecoderServer(boolean paramBoolean)
  {
    setup(paramBoolean);
  }

  public PdfDecoderServer()
  {
    setup(true);
  }

  public static void disposeAllStatic()
  {
    StandardFonts.dispose();
    FontMappings.dispose();
  }

  public final void dispose()
  {
    this.parser.disposeObjects();
  }

  public final void closePdfFile()
  {
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
    this.parser.setParms(this.displayRotation, this.scaling, 0, this.specialMode);
    return this.parser.getPageAsHiRes(paramInt, paramBoolean);
  }

  public BufferedImage getPageAsImage(int paramInt)
    throws PdfException
  {
    BufferedImage localBufferedImage = getPageAsImage(paramInt, false);
    return localBufferedImage;
  }

  public BufferedImage getPageAsTransparentImage(int paramInt)
    throws PdfException
  {
    return getPageAsImage(paramInt, true);
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
    if (paramFloat > 0.0F)
      this.scaling = paramFloat;
    else
      paramFloat = this.scaling;
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
    this.max_y = localPdfPageData.getMediaBoxHeight(paramInt);
    this.max_x = localPdfPageData.getMediaBoxWidth(paramInt);
    int j = localPdfPageData.getCropBoxWidth(paramInt);
    int k = localPdfPageData.getCropBoxHeight(paramInt);
    this.x_size = ((int)(j * paramFloat));
    this.y_size = ((int)(k * paramFloat));
    if (!this.isNewRotationSet)
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
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (localAcroRenderer != null)
      localAcroRenderer.getCompData().setForceRedraw(true);
  }

  public final void setPageParameters(float paramFloat, int paramInt1, int paramInt2)
  {
    this.isNewRotationSet = true;
    this.displayRotation = paramInt2;
    setPageParameters(paramFloat, paramInt1);
  }

  public void waitForDecodingToFinish()
  {
    this.fileAccess.waitForDecodingToFinish();
  }

  public DynamicVectorRenderer getDynamicRenderer()
  {
    return this.fileAccess.getDynamicRenderer();
  }

  public DynamicVectorRenderer getDynamicRenderer(boolean paramBoolean)
  {
    return this.fileAccess.getDynamicRenderer(paramBoolean);
  }

  public PdfObjectReader getIO()
  {
    return this.parser.getIO();
  }

  public final void decodePage(int paramInt)
    throws Exception
  {
    boolean bool = isPageAvailable(paramInt);
    PdfObject localPdfObject = this.fileAccess.linearParser.getLinearPageObject();
    if (!bool)
      return;
    if ((bool) && (localPdfObject != null))
      this.fileAccess.readAllPageReferences(true, localPdfObject, new HashMap(1000), new HashMap(1000), paramInt, getFormRenderer(), this.res, this.options.getInsetW(), this.options.getInsetH());
    this.parser.setParms(this.displayRotation, this.scaling, 0, this.specialMode);
    this.parser.decodePage(paramInt);
  }

  public synchronized boolean isPageAvailable(int paramInt)
  {
    return this.parser.isPageAvailable(paramInt);
  }

  public void drawAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
    if (paramInt == getPageNumber())
      localDynamicVectorRenderer.drawAdditionalObjectsOverPage(paramArrayOfInt, paramArrayOfColor, paramArrayOfObject);
  }

  public void flushAdditionalObjectsOnPage(int paramInt)
    throws PdfException
  {
    if (paramInt == getPageNumber())
    {
      DynamicVectorRenderer localDynamicVectorRenderer = this.fileAccess.getDynamicRenderer();
      localDynamicVectorRenderer.flushAdditionalObjOnPage();
    }
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
    this.formsActionHandler.init(null, this.externalHandlers.getJavaScript(), getFormRenderer());
  }

  private void preOpen()
  {
    this.fileAccess.setDecoding(true);
    this.options.setDisplayView(2);
    this.fileAccess.setDecoding(false);
    Object localObject = this.externalHandlers.getExternalHandler(6);
    this.externalHandlers.openPdfFile(localObject);
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (this.userActionHandler != null)
      this.formsActionHandler = this.userActionHandler;
    else
      this.formsActionHandler = new DefaultActionHandler();
    localAcroRenderer.resetHandler(this.formsActionHandler, this.scalingdpi.getDpi(), this.externalHandlers.getJavaScript());
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

  public String getPageDecodeReport()
  {
    return this.parser.getPageDecodeReport();
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

  public void addExternalHandler(Object paramObject, int paramInt)
  {
    switch (paramInt)
    {
    case 5:
      this.userActionHandler = ((ActionHandler)paramObject);
      break;
    default:
      this.externalHandlers.addExternalHandler(paramObject, paramInt);
    }
  }

  public Object getExternalHandler(int paramInt)
  {
    switch (paramInt)
    {
    case 17:
      return this.fileAccess.getOffset();
    }
    return this.externalHandlers.getExternalHandler(paramInt);
  }

  public String getFileName()
  {
    return this.fileAccess.getFilename();
  }

  public boolean isForm()
  {
    return this.res.isForm();
  }

  public TextLines getTextLines()
  {
    return this.parser.getTextLines();
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

  public int getDisplayView()
  {
    return this.options.getDisplayView();
  }

  public float getScaling()
  {
    return this.scaling;
  }

  private void setup(boolean paramBoolean)
  {
    this.options.setRenderPage(paramBoolean);
    if (!FontMappings.fontsInitialised)
    {
      FontMappings.initFonts();
      FontMappings.fontsInitialised = true;
    }
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
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.PdfDecoderServer
 * JD-Core Version:    0.6.2
 */
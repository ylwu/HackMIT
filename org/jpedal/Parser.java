package org.jpedal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.display.GUIModes;
import org.jpedal.display.PageOffsets;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.fonts.tt.TTGlyph;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.linear.LinearParser;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.acroforms.creation.SwingFormFactory;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.DecoderResults;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.FXDisplay;
import org.jpedal.render.ImageDisplay;
import org.jpedal.render.SwingDisplay;
import org.jpedal.render.output.OutputDisplay;
import org.jpedal.text.TextLines;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Rectangle;

class Parser
{
  private String decodeStatus = "";
  private StatusBar statusBar = null;
  private String fontsInFile = "";
  private String imagesInFile = "";
  private float multiplyer = 1.0F;
  private PdfImageData pdfImages = new PdfImageData();
  private PdfImageData pdfBackgroundImages = new PdfImageData();
  private PdfData pdfData;
  private PdfData pdfBackgroundData;
  private int displayRotation;
  private float scaling = 1.0F;
  private boolean isBackgroundDecoding = false;
  private final ExternalHandlers externalHandlers;
  private int extractionMode = 7;
  private int renderMode = 7;
  private Rectangle[] objectAreas = null;
  private DecoderOptions options = new DecoderOptions();
  private final FileAccess fileAcces;
  private PdfResources res = new PdfResources();
  private DecoderResults resultsFromDecode = new DecoderResults();
  private final TextLines textLines = new TextLines();
  private boolean generateGlyphOnRender;
  private int indent;
  private int specialMode;
  private boolean useJavaFX;

  float getHiResUpscaleFactor()
  {
    return this.multiplyer;
  }

  void setParms(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    this.displayRotation = paramInt1;
    this.scaling = paramFloat;
    this.indent = paramInt2;
    this.specialMode = paramInt3;
  }

  TextLines getTextLines()
  {
    return this.textLines;
  }

  Parser(ExternalHandlers paramExternalHandlers, DecoderOptions paramDecoderOptions, FileAccess paramFileAccess, PdfResources paramPdfResources, DecoderResults paramDecoderResults)
  {
    this.externalHandlers = paramExternalHandlers;
    this.options = paramDecoderOptions;
    this.fileAcces = paramFileAccess;
    this.res = paramPdfResources;
    this.resultsFromDecode = paramDecoderResults;
    if (paramExternalHandlers.getMode() == GUIModes.JAVAFX)
    {
      paramFileAccess.setDVR(new FXDisplay(1, paramFileAccess.getObjectStore(), false));
      this.useJavaFX = true;
    }
    else
    {
      paramFileAccess.setDVR(new SwingDisplay(1, paramFileAccess.getObjectStore(), false));
    }
  }

  void setExtractionMode(int paramInt, float paramFloat)
  {
    PdfPageData localPdfPageData = this.fileAcces.getPdfPageData();
    localPdfPageData.setScalingValue(paramFloat);
    this.extractionMode = paramInt;
    PdfLayerList localPdfLayerList = this.res.getPdfLayerList();
    if (localPdfLayerList != null)
    {
      boolean bool = localPdfLayerList.setZoom(paramFloat);
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
  }

  void decodePageInBackground(int paramInt)
    throws Exception
  {
    if (this.fileAcces.isDecoding())
    {
      if (LogWriter.isOutput())
      {
        LogWriter.writeLog("[PDF]WARNING - this file is being decoded already in foreground");
        LogWriter.writeLog("[PDF]Multiple access not recommended - use  waitForDecodingToFinish() to check");
      }
    }
    else if (this.isBackgroundDecoding)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("[PDF]WARNING - this file is being decoded already in background");
    }
    else
      try
      {
        this.isBackgroundDecoding = true;
        if (paramInt > this.fileAcces.getPageCount())
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Page out of bounds");
        }
        else
        {
          String str = getIO().getReferenceforPage(paramInt);
          AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
          if ((str != null) || ((localAcroRenderer.isXFA()) && (localAcroRenderer.useXFA())))
          {
            if (getIO() == null)
              throw new PdfException("File not open - did you call closePdfFile() inside a loop and not reopen");
            PageObject localPageObject = new PageObject(str);
            getIO().readObject(localPageObject);
            PdfObject localPdfObject = localPageObject.getDictionary(2004251818);
            localPageObject.setPageNumber(paramInt);
            ObjectStore localObjectStore = new ObjectStore();
            PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(getIO(), this.options.useHiResImageForDisplay(), this.res.getPdfLayerList());
            localPdfStreamDecoder.setParameters(true, false, 0, this.extractionMode, this.useJavaFX);
            localPdfStreamDecoder.setXMLExtraction(this.options.isXMLExtraction());
            this.externalHandlers.addHandlers(localPdfStreamDecoder);
            localPdfStreamDecoder.setObjectValue(-9, this.fileAcces.getFilename());
            localPdfStreamDecoder.setObjectValue(23, new ImageDisplay(this.fileAcces.getPageNumber(), false, 5000, new ObjectStore()));
            localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
            localPdfStreamDecoder.setObjectValue(-18, this.fileAcces.getPdfPageData());
            localPdfStreamDecoder.setIntValue(-10, paramInt);
            this.res.setupResources(localPdfStreamDecoder, false, localPdfObject, this.fileAcces.getPageNumber(), getIO());
            localPdfStreamDecoder.decodePageContent(localPageObject);
            this.pdfBackgroundData = ((PdfData)localPdfStreamDecoder.getObjectValue(-19));
            this.pdfBackgroundImages = ((PdfImageData)localPdfStreamDecoder.getObjectValue(-20));
          }
        }
      }
      catch (PdfException localPdfException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localPdfException.getMessage());
      }
      finally
      {
        this.isBackgroundDecoding = false;
      }
  }

  BufferedImage getPageAsImage(int paramInt, boolean paramBoolean)
    throws PdfException
  {
    BufferedImage localBufferedImage = null;
    if ((paramInt > this.fileAcces.getPageCount()) || (paramInt < 1))
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Page " + paramInt + " not in range");
    }
    else
    {
      if (getIO() == null)
        throw new PdfException("File not open - did you call closePdfFile() inside a loop and not reopen");
      String str = getIO().getReferenceforPage(paramInt);
      PdfPageData localPdfPageData = this.fileAcces.getPdfPageData();
      if ((str != null) || (this.externalHandlers.getFormRenderer().isXFA()))
      {
        PDFtoImageConvertor localPDFtoImageConvertor = new PDFtoImageConvertor(this.multiplyer, this.options);
        localBufferedImage = localPDFtoImageConvertor.convert(this.resultsFromDecode, this.displayRotation, this.res, this.externalHandlers, this.renderMode, localPdfPageData, this.externalHandlers.getFormRenderer(), this.scaling, getIO(), paramInt, paramBoolean, str);
        this.multiplyer = localPDFtoImageConvertor.getMultiplyer();
        this.objectAreas = localPDFtoImageConvertor.getAreas();
      }
      if ((!DecoderOptions.isRunningOnAIX) && (!paramBoolean) && (localBufferedImage != null))
        localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
    }
    return localBufferedImage;
  }

  void setRenderMode(int paramInt)
  {
    this.renderMode = paramInt;
    this.extractionMode = paramInt;
  }

  void setExtractionMode(int paramInt)
  {
    this.extractionMode = paramInt;
  }

  void disposeObjects()
  {
    org.jpedal.fonts.FontMappings.fontsInitialised = false;
    this.externalHandlers.dispose();
    if (this.pdfData != null)
      this.pdfData.dispose();
    this.pdfData = null;
    org.jpedal.fonts.FontMappings.defaultFont = null;
    this.fileAcces.dispose();
  }

  Object getJPedalObject(int paramInt)
  {
    switch (paramInt)
    {
    case 826881374:
      return this.res.getPdfLayerList();
    case 2004845231:
      return this.fileAcces.linearParser.getLinearObject(this.fileAcces.isOpen, getIO());
    case -1276915978:
      return this.fileAcces.linearParser.linearizedBackgroundReaderer;
    case 1869245103:
      return this.fileAcces;
    }
    return null;
  }

  synchronized BufferedImage getPageAsHiRes(int paramInt, boolean paramBoolean)
    throws PdfException
  {
    this.multiplyer = this.options.getImageDimensions(paramInt, this.fileAcces.getPdfPageData());
    BufferedImage localBufferedImage = getPageAsImage(paramInt, paramBoolean);
    return localBufferedImage;
  }

  synchronized boolean isPageAvailable(int paramInt)
  {
    return this.fileAcces.linearParser.isPageAvailable(paramInt, getIO());
  }

  PdfData getPdfBackgroundData()
  {
    return this.pdfBackgroundData;
  }

  PdfData getPdfData()
    throws PdfException
  {
    if ((this.extractionMode & 0x1) == 0)
      throw new PdfException("[PDF] Page data object requested will be empty as text extraction disabled. Enable with PdfDecoder method setExtractionMode(PdfDecoderInt.TEXT | other values");
    return this.pdfData;
  }

  PdfGroupingAlgorithms getGroupingObject()
    throws PdfException
  {
    return this.options.getGroupingObject(this.fileAcces.getLastPageDecoded(), getPdfData(), this.fileAcces.getPdfPageData());
  }

  PdfGroupingAlgorithms getBackgroundGroupingObject()
  {
    return this.options.getBackgroundGroupingObject(this.pdfBackgroundData, this.fileAcces.getPdfPageData());
  }

  PdfImageData getPdfImageData()
  {
    return this.pdfImages;
  }

  PdfImageData getPdfBackgroundImageData()
  {
    return this.pdfBackgroundImages;
  }

  void flushObjectValues(boolean paramBoolean)
  {
    if (this.pdfData != null)
      this.pdfData.flushTextList(paramBoolean);
    if ((this.pdfImages != null) && (paramBoolean))
      this.pdfImages.clearImageData();
  }

  private BufferedImage getPageAsImageForHTMLAndSVG(int paramInt, float paramFloat)
    throws Exception
  {
    PdfPageData localPdfPageData = this.fileAcces.getPdfPageData();
    int i = localPdfPageData.getRotation(paramInt);
    BufferedImage localBufferedImage;
    if (paramFloat > 1.0F)
    {
      HashMap localHashMap = new HashMap();
      if ((i == 90) || (i == 270))
        localHashMap.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[] { String.valueOf(localPdfPageData.getCropBoxHeight2D(paramInt) * paramFloat), String.valueOf(localPdfPageData.getCropBoxWidth2D(paramInt) * paramFloat) });
      else
        localHashMap.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[] { String.valueOf(localPdfPageData.getCropBoxWidth2D(paramInt) * paramFloat), String.valueOf(localPdfPageData.getCropBoxHeight2D(paramInt) * paramFloat) });
      localHashMap.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
      this.options.set(localHashMap);
      localBufferedImage = getPageAsHiRes(paramInt, false);
    }
    else
    {
      localBufferedImage = getPageAsImage(paramInt, false);
    }
    return localBufferedImage;
  }

  String getPageDecodeReport()
  {
    return this.decodeStatus;
  }

  String getInfo(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    case 373243460:
      if (this.fontsInFile == null)
        str = "No fonts defined";
      else
        str = this.fontsInFile;
      break;
    case 1026635598:
      if (this.imagesInFile == null)
        str = "No images defined as XObjects";
      else
        str = this.imagesInFile;
      break;
    default:
      str = null;
    }
    return str;
  }

  void decodePage(int paramInt)
    throws Exception
  {
    Object localObject1 = this.externalHandlers.getExternalHandler(20);
    PdfPageData localPdfPageData = this.fileAcces.getPdfPageData();
    Object localObject3;
    Object localObject6;
    Object localObject7;
    if (localObject1 != null)
    {
      OutputDisplay localOutputDisplay = (OutputDisplay)localObject1;
      if ((localOutputDisplay.getType() == 4) || (localOutputDisplay.getType() == 5) || (localOutputDisplay.getType() == 7) || (localOutputDisplay.getType() == 6))
      {
        AcroRenderer localAcroRenderer1 = this.externalHandlers.getFormRenderer();
        int k = this.renderMode;
        if (localOutputDisplay.getBooleanValue(46))
          this.renderMode = 2;
        else
          this.renderMode = 3;
        this.fileAcces.setDVR(new SwingDisplay(1, this.fileAcces.getObjectStore(), false));
        this.objectAreas = null;
        float f = localOutputDisplay.getScaling();
        if (localOutputDisplay.getBooleanValue(55))
          localObject3 = getPageAsImageForHTMLAndSVG(paramInt, f * 1.4F);
        else if (localOutputDisplay.getType() != 7)
          localObject3 = getPageAsImageForHTMLAndSVG(paramInt, f);
        else
          localObject3 = getPageAsHiRes(paramInt, false);
        int m = localOutputDisplay.convertRawPageToOutputPageNumber(paramInt);
        if (((!localOutputDisplay.getBooleanValue(47)) && (localOutputDisplay.getType() == 4)) || (!localOutputDisplay.getBooleanValue(44)))
          if ((!localOutputDisplay.getBooleanValue(44)) && (localOutputDisplay.getBooleanValue(58)))
            localOutputDisplay.writeCustom(59, localObject3);
          else
            localOutputDisplay.writeCustom(41, new Object[] { localOutputDisplay.rootDir, localOutputDisplay.fileName + "/" + m, localObject3 });
        if (localOutputDisplay.getType() == 7)
          localOutputDisplay.writeCustom(59, localObject3);
        if ((localOutputDisplay.getBooleanValue(57)) || (localOutputDisplay.getBooleanValue(2)))
        {
          if (localOutputDisplay.getBooleanValue(46))
          {
            AcroRenderer localAcroRenderer2 = this.externalHandlers.getFormRenderer();
            localObject6 = localAcroRenderer2.getFormFactory();
            localObject7 = new SwingFormFactory();
            ((FormFactory)localObject6).syncValues((SwingFormFactory)localObject7);
            localAcroRenderer2.substituteFormFactory((FormFactory)localObject7);
            this.renderMode = 3;
            boolean bool = localAcroRenderer2.getCompData().formsRasterizedForDisplay();
            localAcroRenderer2.getCompData().setRasterizeForms(true);
            localObject3 = getPageAsImage(m, false);
            localAcroRenderer2.getCompData().setRasterizeForms(bool);
            localAcroRenderer2.substituteFormFactory((FormFactory)localObject6);
          }
          int n = localPdfPageData.getCropBoxWidth(paramInt) * 25 / 100;
          localObject6 = ((BufferedImage)localObject3).getScaledInstance(n, -1, 4);
          localObject7 = new BufferedImage(((Image)localObject6).getWidth(null), ((Image)localObject6).getHeight(null), 2);
          Graphics2D localGraphics2D = ((BufferedImage)localObject7).createGraphics();
          localGraphics2D.drawImage((Image)localObject6, 0, 0, null);
          localOutputDisplay.writeCustom(41, new Object[] { localOutputDisplay.rootDir, "thumbnails/" + m, localObject7 });
        }
        localAcroRenderer1.getFormFactory().indexAllKids();
        this.externalHandlers.setFormRenderer(localAcroRenderer1);
        this.renderMode = k;
        this.fileAcces.setDVR((DynamicVectorRenderer)localObject1);
        ((OutputDisplay)this.fileAcces.getDynamicRenderer()).writeCustom(24, this.objectAreas);
      }
    }
    int i = 0;
    if (paramInt == -1)
    {
      paramInt = this.fileAcces.getLastPageDecoded();
      i = 1;
    }
    int j = paramInt;
    if (this.fileAcces.isDecoding())
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("[PDF]WARNING - this file is being decoded already - use  waitForDecodingToFinish() to check");
    }
    else
    {
      Object localObject2 = this.fileAcces.linearParser.getLinearPageObject();
      localObject3 = this.externalHandlers.getFormRenderer();
      this.fileAcces.setDecoding(true);
      this.resultsFromDecode.resetTimeout();
      try
      {
        this.fileAcces.setDecoding(true);
        PdfLayerList localPdfLayerList = this.res.getPdfLayerList();
        Object localObject5;
        if ((localPdfLayerList != null) && (localPdfLayerList.getChangesMade()))
        {
          localObject4 = localPdfLayerList.getJSCommands();
          localObject5 = this.externalHandlers.getJavaScript();
          if ((localObject5 != null) && (localObject4 != null))
            while (((Iterator)localObject4).hasNext())
              ((Javascript)localObject5).executeAction((String)((Iterator)localObject4).next());
          this.fileAcces.setLastPageDecoded(-1);
          localPdfLayerList.setChangesMade(false);
          ((AcroRenderer)localObject3).getCompData().setForceRedraw(true);
          ((AcroRenderer)localObject3).getCompData().setLayerData(localPdfLayerList);
          ((AcroRenderer)localObject3).getCompData().resetScaledLocation(this.scaling, this.displayRotation, this.indent);
        }
        Object localObject4 = this.fileAcces.getDynamicRenderer();
        this.fileAcces.setLastPageDecoded(j);
        this.decodeStatus = "";
        ((DynamicVectorRenderer)localObject4).flush();
        if ((j > this.fileAcces.getPageCount()) || (j < 1))
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Page out of bounds");
          this.fileAcces.setDecoding(false);
        }
        else
        {
          localObject5 = null;
          if (this.statusBar != null)
          {
            localObject6 = new ProgressListener();
            localObject5 = new Timer(150, (ActionListener)localObject6);
            ((Timer)localObject5).start();
          }
          this.fileAcces.setPageNumber(j);
          localObject6 = getIO().getReferenceforPage(j);
          if ((localObject6 != null) && (getIO() == null))
            throw new PdfException("File not open - did you call closePdfFile() inside a loop and not reopen");
          if (localObject2 == null)
          {
            localObject2 = new PageObject((String)localObject6);
            getIO().readObject((PdfObject)localObject2);
            getIO().checkParentForResources((PdfObject)localObject2);
          }
          localObject7 = null;
          localObject7 = new PdfStreamDecoder(getIO(), this.options.useHiResImageForDisplay(), this.res.getPdfLayerList());
          ((PdfStreamDecoder)localObject7).setXMLExtraction(this.options.isXMLExtraction());
          ((DynamicVectorRenderer)localObject4).setHiResImageForDisplayMode(this.options.useHiResImageForDisplay());
          ((DynamicVectorRenderer)localObject4).setPrintPage(j);
          ((DynamicVectorRenderer)localObject4).setCustomColorHandler((ColorHandler)this.externalHandlers.getExternalHandler(19));
          ((PdfStreamDecoder)localObject7).setParameters(true, this.options.getRenderPage(), this.renderMode, this.extractionMode, this.useJavaFX);
          this.externalHandlers.addHandlers((PdfStreamDecoder)localObject7);
          ((PdfStreamDecoder)localObject7).setObjectValue(-9, this.fileAcces.getFilename());
          ((PdfStreamDecoder)localObject7).setIntValue(-10, j);
          ((PdfStreamDecoder)localObject7).setRenderer((DynamicVectorRenderer)localObject4);
          ((PdfStreamDecoder)localObject7).setObjectValue(-8, this.fileAcces.getObjectStore());
          ((PdfStreamDecoder)localObject7).setObjectValue(-3, this.statusBar);
          ((PdfStreamDecoder)localObject7).setObjectValue(-18, localPdfPageData);
          ((PdfStreamDecoder)localObject7).setObjectValue(23, localObject4);
          int i1 = this.fileAcces.getPageNumber();
          this.res.setupResources((PdfStreamDecoder)localObject7, false, ((PdfObject)localObject2).getDictionary(2004251818), i1, getIO());
          ((DynamicVectorRenderer)localObject4).init(localPdfPageData.getMediaBoxWidth(i1), localPdfPageData.getMediaBoxHeight(i1), localPdfPageData.getRotation(i1), this.options.getPageColor());
          if ((((DynamicVectorRenderer)localObject4).getType() != 4) && (((DynamicVectorRenderer)localObject4).getType() != 5) && (((DynamicVectorRenderer)localObject4).getType() != 6) && (((DynamicVectorRenderer)localObject4).getType() != 5) && (this.options.getTextColor() != null))
          {
            ((DynamicVectorRenderer)localObject4).setValue(2, this.options.getTextColor().getRGB());
            if (this.options.getChangeTextAndLine())
              ((DynamicVectorRenderer)localObject4).setValue(3, 1);
            else
              ((DynamicVectorRenderer)localObject4).setValue(3, 0);
            ((DynamicVectorRenderer)localObject4).setValue(4, this.options.getReplacementColorThreshold());
          }
          Object localObject8;
          Object localObject9;
          Object localObject10;
          try
          {
            if (this.textLines != null)
              this.textLines.setLineAreas(null);
            if (((PdfObject)localObject2).getPageNumber() == -1)
              ((PdfObject)localObject2).setPageNumber(j);
            ((PdfStreamDecoder)localObject7).decodePageContent((PdfObject)localObject2);
            if ((this.textLines != null) && (this.extractionMode > 0))
            {
              Vector_Rectangle localVector_Rectangle = (Vector_Rectangle)((PdfStreamDecoder)localObject7).getObjectValue(-21);
              localVector_Rectangle.trim();
              localObject8 = localVector_Rectangle.get();
              localObject9 = (Vector_Int)((PdfStreamDecoder)localObject7).getObjectValue(22);
              ((Vector_Int)localObject9).trim();
              localObject10 = ((Vector_Int)localObject9).get();
              for (int i2 = 0; i2 != localObject8.length; i2++)
                this.textLines.addToLineAreas(localObject8[i2], localObject10[i2], j);
            }
          }
          catch (Error localError)
          {
            this.decodeStatus = (this.decodeStatus + "Error in decoding page " + localError.toString());
          }
          catch (Exception localException)
          {
            if (localException.getMessage().contains("JPeg 2000"))
              this.decodeStatus = (this.decodeStatus + "Error in decoding page " + localException.toString());
          }
          this.fontsInFile = ((String)((PdfStreamDecoder)localObject7).getObjectValue(373243460));
          this.imagesInFile = ((String)((PdfStreamDecoder)localObject7).getObjectValue(1026635598));
          this.pdfData = ((PdfData)((PdfStreamDecoder)localObject7).getObjectValue(-19));
          this.pdfImages = ((PdfImageData)((PdfStreamDecoder)localObject7).getObjectValue(-20));
          this.resultsFromDecode.update((PdfStreamDecoder)localObject7, true);
          if (localObject5 != null)
          {
            ((Timer)localObject5).stop();
            this.statusBar.setProgress(100);
          }
          if ((this.options.getRenderPage()) && (i == 0) && ((this.renderMode & 0x20) != 32) && (!((AcroRenderer)localObject3).ignoreForms()))
          {
            PageOffsets localPageOffsets = this.fileAcces.getOffset();
            if (localPageOffsets != null)
              ((AcroRenderer)localObject3).getCompData().setPageValues(this.scaling, this.displayRotation, this.indent, 0, 0, 1, localPageOffsets.widestPageNR, localPageOffsets.widestPageR);
            ((AcroRenderer)localObject3).createDisplayComponentsForPage(j, (PdfStreamDecoder)localObject7);
            if ((((DynamicVectorRenderer)localObject4).getType() == 6) || (((DynamicVectorRenderer)localObject4).getType() == 4) || (((DynamicVectorRenderer)localObject4).getType() == 5))
            {
              localObject8 = ((AcroRenderer)localObject3).getCompData().getFormList(true);
              localObject9 = localObject8[j].iterator();
              while (((Iterator)localObject9).hasNext())
              {
                localObject10 = ((Iterator)localObject9).next();
                if (localObject10 != null)
                  ((PdfStreamDecoder)localObject7).drawFlattenedForm((FormObject)localObject10, true, (PdfObject)localObject3.getFormResources()[0]);
              }
            }
          }
        }
      }
      finally
      {
        this.fileAcces.setDecoding(false);
        if (this.statusBar != null)
          this.statusBar.percentageDone = 100.0F;
      }
    }
    if (TTGlyph.redecodePage)
    {
      TTGlyph.redecodePage = false;
      decodePage(paramInt);
    }
    this.fileAcces.getDynamicRenderer().flagDecodingFinished();
  }

  protected PdfObjectReader getIO()
  {
    return this.fileAcces.getIO();
  }

  void resetMultiplyer()
  {
    this.multiplyer = 1.0F;
  }

  void setFontsInFile(String paramString)
  {
    this.fontsInFile = paramString;
  }

  void setStatusBar(StatusBar paramStatusBar)
  {
    this.statusBar = paramStatusBar;
  }

  class ProgressListener
    implements ActionListener
  {
    ProgressListener()
    {
    }

    public void actionPerformed(ActionEvent paramActionEvent)
    {
      Parser.this.statusBar.setProgress((int)Parser.this.statusBar.percentageDone);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.Parser
 * JD-Core Version:    0.6.2
 */
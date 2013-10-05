package org.jpedal.parser;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.scene.shape.Path;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.external.GlyphTracker;
import org.jpedal.external.ImageHandler;
import org.jpedal.external.ShapeTracker;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.glyph.T3Size;
import org.jpedal.images.SamplingFactory;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.JavaFXShape;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfShape;
import org.jpedal.objects.SwingShape;
import org.jpedal.objects.TextState;
import org.jpedal.objects.acroforms.overridingImplementations.ReadOnlyTextIcon;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FontObject;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.structuredtext.StructuredContentHandler;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.SwingDisplay;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class PdfStreamDecoder extends BaseDecoder
{
  protected GraphicsState newGS = null;
  protected byte[] pageStream = null;
  PdfLayerList layers;
  protected boolean getSamplingOnly = false;
  TextState currentTextState = new TextState();
  private Map shadingColorspacesObjects = new HashMap(50);
  private boolean isStackInitialised = false;
  private Vector_Object graphicsStateStack;
  private Vector_Object strokeColorStateStack;
  private Vector_Object nonstrokeColorStateStack;
  private Vector_Object textStateStack;
  private boolean isTTHintingRequired = false;
  Vector_Int textDirections = new Vector_Int();
  Vector_Rectangle textAreas = new Vector_Rectangle();
  public boolean ignoreColors = false;
  int depth = 0;
  int lastDataPointer = -1;
  private T3Decoder t3Decoder = null;
  private boolean removeRenderImages = false;
  private boolean textColorExtracted = false;
  private boolean colorExtracted = false;
  private boolean textExtracted = true;
  private boolean renderText = false;
  private boolean isFlattenedForm = false;
  private float flattenX = 0.0F;
  private float flattenY = 0.0F;
  private String imagesInFile = null;
  public static float currentThreshold = 0.595F;
  private boolean flattenXFormToImage = false;
  private boolean requestTimeout = false;
  private int timeoutInterval = -1;
  protected ImageHandler customImageHandler = null;
  private PdfFontFactory pdfFontFactory;
  private boolean isXMLExtraction = false;
  private StatusBar statusBar = null;
  private boolean markedContentExtracted = false;
  private PdfData pdfData = new PdfData();
  private PdfImageData pdfImages = new PdfImageData();
  protected static String indent = "";
  protected boolean isDataValid = true;
  private PdfFont currentFontData;
  protected boolean useHiResImageForDisplay = false;
  protected ObjectStore objectStoreStreamRef;
  private String formName = "";
  protected boolean isType3Font;
  public static boolean useTextPrintingForNonEmbeddedFonts = false;
  private boolean isTimeout = false;
  boolean isPrinting = false;
  private static final float[] matches = { 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };

  public PdfStreamDecoder(PdfObjectReader paramPdfObjectReader)
  {
    init(paramPdfObjectReader);
  }

  public PdfStreamDecoder(PdfObjectReader paramPdfObjectReader, boolean paramBoolean, PdfLayerList paramPdfLayerList)
  {
    if (paramPdfLayerList != null)
      this.layers = paramPdfLayerList;
    this.useHiResImageForDisplay = paramBoolean;
    init(paramPdfObjectReader);
  }

  private void init(PdfObjectReader paramPdfObjectReader)
  {
    this.cache = new PdfObjectCache();
    this.gs = new GraphicsState(this.useJavaFX);
    this.layerDecoder = new LayerDecoder();
    this.errorTracker = new ErrorTracker();
    this.pageData = new PdfPageData();
    StandardFonts.checkLoaded(1);
    this.currentPdfFile = paramPdfObjectReader;
    this.pdfFontFactory = new PdfFontFactory(paramPdfObjectReader);
  }

  public final T3Size decodePageContent(GraphicsState paramGraphicsState, byte[] paramArrayOfByte)
    throws PdfException
  {
    this.newGS = paramGraphicsState;
    this.pageStream = paramArrayOfByte;
    return decodePageContent(null);
  }

  public T3Size decodePageContent(PdfObject paramPdfObject)
    throws PdfException
  {
    try
    {
      this.imagesProcessedFully = true;
      this.imageCount = 0;
      this.isTimeout = false;
      this.layerDecoder.setPdfLayerList(this.layers);
      this.imagesInFile = null;
      if ((!this.renderDirectly) && (this.statusBar != null))
        this.statusBar.percentageDone = 0.0F;
      if (this.newGS != null)
        this.gs = this.newGS;
      else
        this.gs = new GraphicsState(false, 0, 0);
      if (this.renderPage)
      {
        if (this.current == null)
          throw new PdfException("DynamicVectorRenderer not setup PdfStreamDecoder setStore(...) should be called");
        this.current.drawClip(this.gs, this.defaultClip, false);
        this.current.paintBackground(new Rectangle(this.pageData.getCropBoxX(this.pageNum), this.pageData.getCropBoxY(this.pageNum), this.pageData.getCropBoxWidth(this.pageNum), this.pageData.getCropBoxHeight(this.pageNum)));
      }
      byte[][] arrayOfByte1 = (byte[][])null;
      if (paramPdfObject != null)
      {
        arrayOfByte1 = paramPdfObject.getKeyArray(1216184967);
        this.isDataValid = paramPdfObject.streamMayBeCorrupt();
      }
      if (paramPdfObject == null)
        this.cache.pageGroupingObj = null;
      else
        this.cache.pageGroupingObj = paramPdfObject.getDictionary(1111442775);
      byte[] arrayOfByte;
      if ((paramPdfObject != null) && (arrayOfByte1 == null))
        arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
      else if (this.pageStream != null)
        arrayOfByte = this.pageStream;
      else
        arrayOfByte = this.currentPdfFile.getObjectReader().readPageIntoStream(paramPdfObject);
      this.lastDataPointer = -1;
      if ((arrayOfByte != null) && (arrayOfByte.length > 0))
        decodeStreamIntoObjects(arrayOfByte, false);
      if (!this.isType3Font)
        this.cache.resetFonts();
      T3Size localT3Size = new T3Size();
      if (this.t3Decoder != null)
      {
        localT3Size.x = this.t3Decoder.T3maxWidth;
        localT3Size.y = this.t3Decoder.T3maxHeight;
        this.ignoreColors = this.t3Decoder.ignoreColors;
        this.t3Decoder = null;
      }
      return localT3Size;
    }
    catch (Error localError)
    {
      if ((ExternalHandlers.throwMissingCIDError) && (localError.getMessage().contains("kochi")))
        throw localError;
      this.errorTracker.addPageFailureMessage("Problem decoding page " + localError);
    }
    return null;
  }

  public void drawFlattenedForm(PdfObject paramPdfObject1, boolean paramBoolean, PdfObject paramPdfObject2)
    throws PdfException
  {
    GraphicsState localGraphicsState = this.gs;
    this.gs = new GraphicsState(this.useJavaFX);
    this.isFlattenedForm = true;
    boolean[] arrayOfBoolean = ((FormObject)paramPdfObject1).getCharacteristics();
    if ((arrayOfBoolean[0] != 0) || (arrayOfBoolean[1] != 0) || (arrayOfBoolean[5] != 0) || ((!paramPdfObject1.getBoolean(524301630)) && (paramPdfObject1.getParameterConstant(1147962727) == 1061176672)))
      return;
    Object localObject1 = null;
    PdfObject localPdfObject1 = paramPdfObject1.getDictionary(4384).getDictionary(30);
    Object localObject2 = new HashMap();
    if (localPdfObject1 != null)
      localObject2 = localPdfObject1.getOtherDictionaries();
    String str = paramPdfObject1.getName(4387);
    if ((str != null) && (str.equals(((FormObject)paramPdfObject1).getNormalOnState())))
    {
      if (localPdfObject1.getDictionary(7998) != null)
      {
        localObject1 = localPdfObject1.getDictionary(7998);
      }
      else if ((localPdfObject1.getDictionary(2045494) != null) && (str != null) && (str.equals("Off")))
      {
        localObject1 = localPdfObject1.getDictionary(2045494);
      }
      else if ((localObject2 != null) && (str != null))
      {
        localObject1 = (PdfObject)((Map)localObject2).get(str);
      }
      else if ((localObject2 != null) && (!((Map)localObject2).isEmpty()))
      {
        localObject3 = ((Map)localObject2).keySet().iterator();
        localObject5 = (String)((Iterator)localObject3).next();
        localObject4 = (PdfObject)((Map)localObject2).get(localObject5);
        localObject1 = localObject4;
      }
    }
    else if ((localPdfObject1 != null) || (paramPdfObject1.getDictionary(7451).getDictionary(25) != null))
      if ((localPdfObject1 != null) && (localPdfObject1.getDictionary(2045494) != null))
        localObject1 = localPdfObject1.getDictionary(2045494);
      else if ((paramPdfObject1.getDictionary(7451).getDictionary(25) != null) && (paramPdfObject1.getDictionary(7451).getDictionary(6422) == null))
        localObject1 = paramPdfObject1.getDictionary(7451).getDictionary(25);
      else if ((localPdfObject1 != null) && (localPdfObject1.getDecodedStream() != null))
        localObject1 = localPdfObject1;
    if (localObject1 != null)
    {
      localObject3 = ((PdfObject)localObject1).getDecodedStream();
      if (localObject3 != null)
      {
        localObject4 = new String((byte[])localObject3);
        if ((((String)localObject4).contains("BMC")) && (!((String)localObject4).contains("BT")))
          localObject1 = null;
      }
    }
    Object localObject3 = paramPdfObject1.getTextStreamValueAsByte(5137);
    if (localObject1 == null)
    {
      localObject4 = paramPdfObject1.getTextStreamValue(38);
      if ((localObject3 != null) || ((localObject4 != null) && (((String)localObject4).length() > 0)))
      {
        localObject5 = new ReadOnlyTextIcon(0, this.currentPdfFile, paramPdfObject2);
        ((ReadOnlyTextIcon)localObject5).decipherAppObject((FormObject)paramPdfObject1);
        if (localObject4 != null)
        {
          ((ReadOnlyTextIcon)localObject5).setText((String)localObject4);
          localObject1 = ((ReadOnlyTextIcon)localObject5).getFakeObject();
        }
        else if (localObject3 != null)
        {
          localObject1 = ((ReadOnlyTextIcon)localObject5).getFakeObject();
          ((PdfObject)localObject1).setDecodedStream((byte[])localObject3);
        }
      }
      if ((localObject1 == null) && (localObject3 == null))
        return;
    }
    if (localObject1 != null)
      this.currentPdfFile.checkResolved((PdfObject)localObject1);
    Object localObject4 = null;
    if (localObject1 != null)
      localObject4 = ((PdfObject)localObject1).getDecodedStream();
    if (localObject1 != null)
    {
      localObject5 = ((PdfObject)localObject1).getDictionary(2004251818);
      readResources((PdfObject)localObject5, false);
    }
    Object localObject5 = paramPdfObject1.getFloatArray(573911876);
    if (localObject5 == null)
      localObject5 = new float[] { 0.0F, 0.0F, 1.0F, 1.0F };
    if (this.isFlattenedForm)
    {
      this.flattenX = localObject5[0];
      this.flattenY = localObject5[1];
    }
    float[] arrayOfFloat1 = { 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
    if (localObject1 != null)
      arrayOfFloat1 = ((PdfObject)localObject1).getFloatArray(1145198201);
    int i = this.pageData.getRotation(this.pageNum);
    float f1 = localObject5[0];
    float f2 = localObject5[1];
    Area localArea = null;
    if (arrayOfFloat1 != null)
    {
      switch (i)
      {
      case 90:
        f1 = localObject5[2];
        if (arrayOfFloat1[4] < 0.0F)
          f1 = localObject5[0] + arrayOfFloat1[4];
        break;
      default:
        f1 = localObject5[0] + arrayOfFloat1[4];
        localArea = new Area(new Rectangle((int)localObject5[0], (int)localObject5[1], (int)localObject5[2], (int)localObject5[3]));
      }
      f2 = localObject5[1] + arrayOfFloat1[5];
      float f3 = 1.0F;
      float f4 = 1.0F;
      PdfObject localPdfObject2 = paramPdfObject1.getDictionary(4384);
      if (localPdfObject2 != null)
      {
        localPdfObject2 = localPdfObject2.getDictionary(30);
        if (localPdfObject2 != null)
        {
          float[] arrayOfFloat2 = localPdfObject2.getFloatArray(303185736);
          if ((arrayOfFloat2 != null) && (localObject5[0] != arrayOfFloat2[0]) && (localObject5[1] != arrayOfFloat2[1]) && (localObject5[2] != arrayOfFloat2[2]) && (localObject5[3] != arrayOfFloat2[3]) && (arrayOfFloat1[0] * arrayOfFloat1[3] == 1.0F) && (arrayOfFloat1[1] * arrayOfFloat1[2] == 0.0F))
          {
            float f5 = localObject5[2] - localObject5[0];
            float f6 = localObject5[3] - localObject5[1];
            float f7 = arrayOfFloat2[2] - arrayOfFloat2[0];
            float f8 = arrayOfFloat2[3] - arrayOfFloat2[1];
            if ((i != 0) && ((int)f5 != (int)f7))
            {
              f3 = f5 / f7;
              f1 -= f7 * f3;
            }
            if ((int)f6 != (int)f8)
              f4 = f6 / f8;
          }
        }
      }
      this.gs.CTM = new float[][] { { arrayOfFloat1[0] * f3, arrayOfFloat1[1], 0.0F }, { arrayOfFloat1[2], arrayOfFloat1[3] * f4, 0.0F }, { f1, f2, 1.0F } };
    }
    else
    {
      this.gs.CTM = new float[][] { { 1.0F, 0.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { f1, f2, 1.0F } };
      localArea = new Area(new Rectangle((int)localObject5[0], (int)localObject5[1], (int)localObject5[2], (int)localObject5[3]));
    }
    if (localArea != null)
      this.gs.updateClip(new Area(localArea));
    this.current.drawClip(this.gs, this.defaultClip, false);
    this.currentTextState = new TextState();
    if (paramBoolean)
    {
      BufferedImage localBufferedImage = createTransparentForm((PdfObject)localObject1, 0, 0, (int)(localObject5[2] - localObject5[0]), (int)(localObject5[3] - localObject5[1]), true);
      this.gs.CTM = new float[][] { { localBufferedImage.getWidth() / 4, 0.0F, 1.0F }, { 0.0F, localBufferedImage.getHeight() / 4, 1.0F }, { 0.0F, 0.0F, 0.0F } };
      this.gs.x = f1;
      this.gs.y = f2;
      this.gs.CTM[2][0] = f1;
      this.gs.CTM[2][1] = f2;
      this.current.drawImage(this.pageNum, localBufferedImage, this.gs, false, paramPdfObject1.getObjectRefAsString(), 0, -1);
    }
    else
    {
      setBooleanValue(24, this.isFlattenedForm);
      if (localObject4 != null)
        decodeStreamIntoObjects((byte[])localObject4, false);
    }
    this.gs.updateClip((Area)null);
    this.current.drawClip(this.gs, null, true);
    this.gs = localGraphicsState;
    this.currentTextState = this.gs.getTextState();
  }

  public void setObjectValue(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    case -9:
      setName((String)paramObject);
      break;
    case 23:
      this.current = ((DynamicVectorRenderer)paramObject);
      int i = (this.renderMode & 0x20) == 32 ? 1 : 0;
      if ((i != 0) && (this.current != null))
        this.current.setOCR(true);
      break;
    case -18:
      this.pageData = ((PdfPageData)paramObject);
      if (this.textColorExtracted)
        this.pdfData.enableTextColorDataExtraction();
      break;
    case -3:
      this.statusBar = ((StatusBar)paramObject);
      break;
    case -4:
      this.layers = ((PdfLayerList)paramObject);
      break;
    case -5:
      this.markedContentExtracted = true;
      this.contentHandler = new StructuredContentHandler(paramObject);
      break;
    case 12:
      this.customGlyphTracker = ((GlyphTracker)paramObject);
      break;
    case -6:
      this.customImageHandler = ((ImageHandler)paramObject);
      if ((this.customImageHandler != null) && (this.current != null))
        this.current.setCustomImageHandler(this.customImageHandler);
      break;
    case -7:
      this.renderDirectly = true;
      Graphics2D localGraphics2D = (Graphics2D)paramObject;
      this.defaultClip = localGraphics2D.getClip();
      break;
    case -8:
      this.objectStoreStreamRef = ((ObjectStore)paramObject);
      if (this.current != null)
      {
        this.current.setHiResImageForDisplayMode(this.useHiResImageForDisplay);
        if ((this.customImageHandler != null) && (this.current != null))
          this.current.setCustomImageHandler(this.customImageHandler);
      }
      break;
    default:
      super.setObjectValue(paramInt, paramObject);
    }
  }

  public void setBooleanValue(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 25:
      this.isPrinting = paramBoolean;
      break;
    case -16:
      this.flattenXFormToImage = paramBoolean;
      break;
    default:
      super.setBooleanValue(paramInt, paramBoolean);
    }
  }

  public void setDefaultColors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2)
  {
    this.gs.strokeColorSpace.setColor(paramPdfPaint1);
    this.gs.nonstrokeColorSpace.setColor(paramPdfPaint2);
    this.gs.setStrokeColor(paramPdfPaint1);
    this.gs.setNonstrokeColor(paramPdfPaint2);
  }

  public Object getObjectValue(int paramInt)
  {
    switch (paramInt)
    {
    case -19:
      if (DecoderOptions.embedWidthData)
        this.pdfData.widthIsEmbedded();
      return this.pdfData;
    case -20:
      return this.pdfImages;
    case -21:
      return this.textAreas;
    case 22:
      return this.textDirections;
    case 23:
      return this.current;
    case 373243460:
      return this.pdfFontFactory.getFontsInFile();
    case 1026635598:
      return this.imagesInFile;
    case 4:
      return this.pdfFontFactory.getnonEmbeddedCIDFonts();
    case 1:
      return this.cache.iterator(1);
    }
    return super.getObjectValue(paramInt);
  }

  public final void readResources(PdfObject paramPdfObject, boolean paramBoolean)
    throws PdfException
  {
    if (paramBoolean)
      this.pdfFontFactory.resetfontsInFile();
    this.currentPdfFile.checkResolved(paramPdfObject);
    this.cache.readResources(paramPdfObject, paramBoolean);
  }

  public String decodeStreamIntoObjects(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    if (paramArrayOfByte.length == 0)
      return null;
    int i = 0;
    long l = System.currentTimeMillis();
    CommandParser localCommandParser = new CommandParser(paramArrayOfByte);
    this.parser = localCommandParser;
    int j = paramArrayOfByte.length;
    int k = 0;
    int m = 0;
    Object localObject1 = "";
    Object localObject2 = "";
    Object localObject3;
    if (this.useJavaFX)
      localObject3 = new JavaFXShape();
    else
      localObject3 = new SwingShape();
    TextDecoder localTextDecoder = getTextDecoder(paramBoolean, false);
    if ((!this.renderDirectly) && (this.statusBar != null))
    {
      this.statusBar.percentageDone = 0.0F;
      this.statusBar.resetStatus("stream");
    }
    while (true)
      if ((this.requestTimeout) || ((this.timeoutInterval != -1) && (System.currentTimeMillis() - l > this.timeoutInterval)))
      {
        this.requestTimeout = false;
        this.timeoutInterval = -1;
        this.isTimeout = true;
      }
      else
      {
        if ((!this.renderDirectly) && (this.statusBar != null))
          this.statusBar.percentageDone = (90 * k / j);
        k = localCommandParser.getCommandValues(k, j, this.tokenNumber);
        int n = localCommandParser.getCommandID();
        if (k < 0)
        {
          k = -k;
          try
          {
            int i1 = Cmd.getCommandType(n);
            Object localObject4;
            Object localObject5;
            boolean bool2;
            switch (i1)
            {
            case 0:
              if (((n == 4541763) || (this.layerDecoder.isLayerVisible())) && (!this.getSamplingOnly) && ((this.renderText) || (this.textExtracted)))
              {
                localTextDecoder.setCommands(localCommandParser);
                localTextDecoder.setGS(this.gs);
                localTextDecoder.setTextState(this.currentTextState);
                localTextDecoder.setIntValue(18, this.tokenNumber);
                if ((this.renderPage) && (n == 16980))
                {
                  this.current.drawClip(this.gs, this.defaultClip, true);
                  this.current.drawTR(2);
                  this.current.flagCommand(16980, this.tokenNumber);
                }
                if ((n == 21610) || (n == 21578) || (n == 39) || (n == 34))
                {
                  this.current.flagCommand(21610, this.tokenNumber);
                  if (this.currentTextState.hasFontChanged())
                  {
                    localObject4 = this.currentTextState.getFontID();
                    PdfFont localPdfFont = resolveFont((String)localObject4);
                    if (localPdfFont != null)
                    {
                      this.currentFontData = localPdfFont;
                      this.current.drawFontBounds(this.currentFontData.getBoundingBox());
                    }
                  }
                  if (this.currentFontData == null)
                  {
                    this.currentFontData = new PdfFont(this.currentPdfFile);
                    this.currentFontData.getGlyphData().logicalfontName = StandardFonts.expandName(this.currentTextState.getFontID());
                  }
                  if (this.currentTextState.hasFontChanged())
                    this.currentTextState.setFontChanged(false);
                  localTextDecoder.setFont(this.currentFontData);
                }
                k = localTextDecoder.processToken(this.currentTextState, n, m, k);
              }
              break;
            case 2:
              if (!this.getSamplingOnly)
              {
                Shape localShape;
                switch (n)
                {
                case 66:
                  if (!this.removeRenderImages)
                  {
                    localObject4 = ShapeCommands.B(false, this.useJavaFX, false, this.gs, this.formLevel, (PdfShape)localObject3, this.layerDecoder, this.renderPage, this.current);
                    if ((this.customShapeTracker != null) && (localObject4 != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 66, (Shape)localObject4, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 98:
                  if (!this.removeRenderImages)
                  {
                    localObject4 = ShapeCommands.B(false, this.useJavaFX, true, this.gs, this.formLevel, (PdfShape)localObject3, this.layerDecoder, this.renderPage, this.current);
                    if ((this.customShapeTracker != null) && (localObject4 != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 98, (Shape)localObject4, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 25130:
                  if (!this.removeRenderImages)
                  {
                    localObject4 = ShapeCommands.B(true, this.useJavaFX, true, this.gs, this.formLevel, (PdfShape)localObject3, this.layerDecoder, this.renderPage, this.current);
                    if ((this.customShapeTracker != null) && (localObject4 != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 25130, (Shape)localObject4, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 16938:
                  if (!this.removeRenderImages)
                  {
                    localObject4 = ShapeCommands.B(true, this.useJavaFX, false, this.gs, this.formLevel, (PdfShape)localObject3, this.layerDecoder, this.renderPage, this.current);
                    if ((this.customShapeTracker != null) && (localObject4 != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 16938, (Shape)localObject4, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 99:
                  float f1 = localCommandParser.parseFloat(1);
                  float f2 = localCommandParser.parseFloat(0);
                  float f3 = localCommandParser.parseFloat(3);
                  float f4 = localCommandParser.parseFloat(2);
                  float f5 = localCommandParser.parseFloat(5);
                  float f6 = localCommandParser.parseFloat(4);
                  ((PdfShape)localObject3).addBezierCurveC(f5, f6, f3, f4, f1, f2);
                  break;
                case 100:
                  ShapeCommands.D(localCommandParser, this.gs);
                  break;
                case 70:
                  if (!this.removeRenderImages)
                    F(false, this.formLevel, (PdfShape)localObject3);
                  break;
                case 102:
                  if (!this.removeRenderImages)
                    F(false, this.formLevel, (PdfShape)localObject3);
                  break;
                case 17962:
                  if (!this.removeRenderImages)
                    F(true, this.formLevel, (PdfShape)localObject3);
                  break;
                case 26154:
                  if (!this.removeRenderImages)
                    F(true, this.formLevel, (PdfShape)localObject3);
                  break;
                case 104:
                  ((PdfShape)localObject3).closeShape();
                  break;
                case 74:
                  ShapeCommands.J(false, localCommandParser.parseInt(0), this.gs);
                  break;
                case 106:
                  ShapeCommands.J(true, localCommandParser.parseInt(0), this.gs);
                  break;
                case 108:
                  ((PdfShape)localObject3).lineTo(localCommandParser.parseFloat(1), localCommandParser.parseFloat(0));
                  break;
                case 77:
                  this.gs.setMitreLimit((int)localCommandParser.parseFloat(0));
                  break;
                case 109:
                  ((PdfShape)localObject3).moveTo(localCommandParser.parseFloat(1), localCommandParser.parseFloat(0));
                  break;
                case 110:
                  ShapeCommands.N(this.useJavaFX, (PdfShape)localObject3, this.gs, this.formLevel, this.defaultClip, this.renderPage, this.current, this.pageData, this.pageNum);
                  break;
                case 29285:
                  ((PdfShape)localObject3).appendRectangle(localCommandParser.parseFloat(3), localCommandParser.parseFloat(2), localCommandParser.parseFloat(1), localCommandParser.parseFloat(0));
                  break;
                case 83:
                  if (!this.removeRenderImages)
                  {
                    localShape = ShapeCommands.S(false, this.useJavaFX, this.layerDecoder, this.gs, (PdfShape)localObject3, this.current, this.renderPage);
                    if ((this.customShapeTracker != null) && (localShape != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 83, localShape, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 115:
                  if (!this.removeRenderImages)
                  {
                    localShape = ShapeCommands.S(true, this.useJavaFX, this.layerDecoder, this.gs, (PdfShape)localObject3, this.current, this.renderPage);
                    if ((this.customShapeTracker != null) && (localShape != null))
                      this.customShapeTracker.addShape(this.tokenNumber, 115, localShape, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
                  }
                  break;
                case 118:
                  ((PdfShape)localObject3).addBezierCurveV(localCommandParser.parseFloat(3), localCommandParser.parseFloat(2), localCommandParser.parseFloat(1), localCommandParser.parseFloat(0));
                  break;
                case 119:
                  this.gs.setLineWidth(localCommandParser.parseFloat(0));
                  break;
                case 22314:
                  ((PdfShape)localObject3).setEVENODDWindingRule();
                  ((PdfShape)localObject3).setClip(true);
                  break;
                case 87:
                  ((PdfShape)localObject3).setNONZEROWindingRule();
                  ((PdfShape)localObject3).setClip(true);
                  break;
                case 121:
                  ((PdfShape)localObject3).addBezierCurveY(localCommandParser.parseFloat(3), localCommandParser.parseFloat(2), localCommandParser.parseFloat(1), localCommandParser.parseFloat(0));
                }
              }
              break;
            case 3:
              if ((!this.getSamplingOnly) && ((this.renderPage) || (this.textColorExtracted) || (this.colorExtracted)) && (this.renderPage))
                ShadingCommands.sh(localCommandParser.generateOpAsString(0, true), this.cache, this.gs, this.isPrinting, this.shadingColorspacesObjects, this.pageNum, this.currentPdfFile, this.pageData, this.current);
              break;
            case 1:
              if ((!this.getSamplingOnly) && ((this.renderPage) || (this.textColorExtracted) || (this.colorExtracted)))
              {
                if ((n != 5456718) && (n != 7562094) && (n != 21315) && (n != 29539))
                  this.current.resetOnColorspaceChange();
                boolean bool1;
                switch (n)
                {
                case 25459:
                  localObject5 = localCommandParser.generateOpAsString(0, true);
                  bool1 = true;
                  bool2 = ((!bool1) && (((String)localObject5).equals(localObject1))) || ((bool1) && (((String)localObject5).equals(localObject2)));
                  if (bool1)
                    localObject1 = localObject5;
                  else
                    localObject2 = localObject5;
                  ColorCommands.CS(bool1, (String)localObject5, this.gs, this.cache, this.currentPdfFile, this.isPrinting, this.pageNum, this.pageData, bool2);
                  break;
                case 17235:
                  localObject5 = localCommandParser.generateOpAsString(0, true);
                  bool1 = false;
                  bool2 = ((!bool1) && (((String)localObject5).equals(localObject1))) || ((bool1) && (((String)localObject5).equals(localObject2)));
                  if (bool1)
                    localObject1 = localObject5;
                  else
                    localObject2 = localObject5;
                  ColorCommands.CS(bool1, (String)localObject5, this.gs, this.cache, this.currentPdfFile, this.isPrinting, this.pageNum, this.pageData, bool2);
                  break;
                case 29287:
                  ColorCommands.RG(true, this.gs, localCommandParser, this.cache);
                  break;
                case 21063:
                  ColorCommands.RG(false, this.gs, localCommandParser, this.cache);
                  break;
                case 5456718:
                  ColorCommands.SCN(false, this.gs, localCommandParser, this.cache);
                  break;
                case 7562094:
                  ColorCommands.SCN(true, this.gs, localCommandParser, this.cache);
                  break;
                case 21315:
                  ColorCommands.SCN(false, this.gs, localCommandParser, this.cache);
                  break;
                case 29539:
                  ColorCommands.SCN(true, this.gs, localCommandParser, this.cache);
                  break;
                case 103:
                  ColorCommands.G(true, this.gs, localCommandParser, this.cache);
                  break;
                case 71:
                  ColorCommands.G(false, this.gs, localCommandParser, this.cache);
                  break;
                case 107:
                  ColorCommands.K(true, this.gs, localCommandParser, this.cache);
                  break;
                case 75:
                  ColorCommands.K(false, this.gs, localCommandParser, this.cache);
                }
              }
              break;
            case 4:
              switch (n)
              {
              case 25453:
                CM(this.gs, localCommandParser);
                break;
              case 113:
                this.gs = Q(this.gs, true);
                break;
              case 81:
                this.gs = Q(this.gs, false);
                break;
              case 26483:
                if (!this.getSamplingOnly)
                {
                  localObject5 = (PdfObject)this.cache.GraphicsStates.get(localCommandParser.generateOpAsString(0, true));
                  this.currentPdfFile.checkResolved((PdfObject)localObject5);
                  this.gs.setMode((PdfObject)localObject5);
                  this.current.setGraphicsState(2, this.gs.getAlpha(2));
                  this.current.setGraphicsState(1, this.gs.getAlpha(1));
                }
                break;
              }
              this.gs.setTextState(this.currentTextState);
              if ((n == 25453) && (localTextDecoder != null))
                localTextDecoder.reset();
              break;
            case 5:
              if (n == 16969)
              {
                i = k;
              }
              else
              {
                PdfObject localPdfObject = null;
                bool2 = true;
                int i2;
                if (n == 17519)
                {
                  String str = localCommandParser.generateOpAsString(0, true);
                  byte[] arrayOfByte = null;
                  localPdfObject = this.cache.getXObjects(str);
                  if (localPdfObject != null)
                  {
                    arrayOfByte = localPdfObject.getUnresolvedData();
                    this.currentPdfFile.checkResolved(localPdfObject);
                    i2 = localPdfObject.getParameterConstant(1147962727);
                  }
                  if ((i2 == 373244477) && ((this.formLevel <= 100) || (k != this.lastDataPointer)))
                  {
                    this.lastDataPointer = k;
                    processXForm(k, localPdfObject, this.defaultClip, localCommandParser);
                  }
                }
                if (i2 != 373244477)
                {
                  localObject5 = new ImageDecoder(this.customImageHandler, this.objectStoreStreamRef, this.renderDirectly, this.pdfImages, this.formLevel, this.pageData, this.imagesInFile, this.formName);
                  ((ImageDecoder)localObject5).setIntValue(22, this.pageNum);
                  ((ImageDecoder)localObject5).setIntValue(19, this.formLevel);
                  ((ImageDecoder)localObject5).setHandlerValue(23, this.errorTracker);
                  ((ImageDecoder)localObject5).setRes(this.cache);
                  ((ImageDecoder)localObject5).setGS(this.gs);
                  ((ImageDecoder)localObject5).setSamplingOnly(this.getSamplingOnly);
                  ((ImageDecoder)localObject5).setIntValue(-12, this.streamType);
                  ((ImageDecoder)localObject5).setName(this.fileName);
                  ((ImageDecoder)localObject5).setFloatValue(17, this.multiplyer);
                  ((ImageDecoder)localObject5).setFloatValue(12, this.samplingUsed);
                  ((ImageDecoder)localObject5).setFileHandler(this.currentPdfFile);
                  ((ImageDecoder)localObject5).setRenderer(this.current);
                  ((ImageDecoder)localObject5).setIntValue(26, this.imageStatus);
                  ((ImageDecoder)localObject5).setParameters(this.isPageContent, this.renderPage, this.renderMode, this.extractionMode, this.isPrinting, this.isType3Font, this.useHiResImageForDisplay);
                  ((ImageDecoder)localObject5).setIntValue(14, this.imageCount);
                  if (n == 17519)
                  {
                    if ((localPdfObject != null) && (this.layerDecoder.isLayerVisible()) && ((this.layers == null) || (this.layers.isVisible(localPdfObject))) && ((this.gs.CTM == null) || (this.gs.CTM[1][1] != 0.0F) || (this.gs.CTM[1][0] == 0.0F) || (Math.abs(this.gs.CTM[1][0]) >= 0.2D)))
                      k = ((ImageDecoder)localObject5).processDOImage(localCommandParser.generateOpAsString(0, true), k, localPdfObject);
                  }
                  else if (this.layerDecoder.isLayerVisible())
                    k = ((ImageDecoder)localObject5).processIDImage(k, i, localCommandParser.getStream(), this.tokenNumber);
                  this.samplingUsed = ((ImageDecoder)localObject5).getFloatValue(12);
                  this.imageCount = ((ImageDecoder)localObject5).getIntValue(14);
                  this.imagesInFile = ((ImageDecoder)localObject5).getImagesInFile();
                  if (((ImageDecoder)localObject5).getBooleanValue(16))
                    this.hasYCCKimages = true;
                  if (((ImageDecoder)localObject5).getBooleanValue(15))
                    this.imagesProcessedFully = true;
                }
              }
              break;
            case 6:
              if ((!this.getSamplingOnly) && ((this.renderText) || (this.textExtracted)))
              {
                if (this.t3Decoder == null)
                  this.t3Decoder = new T3Decoder();
                this.t3Decoder.setCommands(localCommandParser);
                this.t3Decoder.setCommands(localCommandParser);
                this.t3Decoder.processToken(n);
              }
              break;
            }
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("[PDF] " + localException + " Processing token >" + Cmd.getCommandAsString(n) + "<>" + this.fileName + " <" + this.pageNum);
            if (!this.isDataValid)
              k = j;
            if ((localException.getMessage() != null) && (localException.getMessage().contains("JPeg 2000")))
              throw new RuntimeException("JPeg 2000 Images needs the VM parameter -Dorg.jpedal.jai=true switch turned on");
          }
          catch (OutOfMemoryError localOutOfMemoryError)
          {
            this.errorTracker.addPageFailureMessage("Memory error decoding token stream");
            if (LogWriter.isOutput())
              LogWriter.writeLog("[MEMORY] Memory error - trying to recover");
          }
          m = k;
          localCommandParser.reset();
          this.tokenNumber += 1;
        }
        if (j <= k)
          break;
      }
    if ((!this.renderDirectly) && (this.statusBar != null))
      this.statusBar.percentageDone = 100.0F;
    this.isTTHintingRequired = localTextDecoder.isTTHintingRequired();
    this.textAreas = ((Vector_Rectangle)localTextDecoder.getObjectValue(-21));
    this.textDirections = ((Vector_Int)localTextDecoder.getObjectValue(22));
    if (paramBoolean)
      return localTextDecoder.getLastTextValue();
    return "";
  }

  private PdfFont resolveFont(String paramString)
  {
    PdfFont localPdfFont = (PdfFont)this.cache.resolvedFonts.get(paramString);
    if (localPdfFont == null)
    {
      Object localObject = (PdfObject)this.cache.unresolvedFonts.get(paramString);
      if (localObject == null)
        this.cache.directFonts.remove(paramString);
      if ((this.isFlattenedForm) && (localObject == null))
      {
        String str1 = StandardFonts.expandName(paramString.replace(",", "-"));
        if ((FontMappings.fontSubstitutionAliasTable.get(str1) == null) && (FontMappings.fontSubstitutionTable.get(str1) == null))
        {
          String str2 = str1.toLowerCase();
          if (str2.contains("bold"))
            str1 = "Arial-Bold";
          else if (str2.contains("italic"))
            str1 = "Arial-Italic";
          else
            str1 = "Arial";
        }
        localObject = new FontObject("1 0 R");
        paramString = StandardFonts.expandName(str1);
        ((PdfObject)localObject).setName(Integer.valueOf(678461817), str1);
        ((PdfObject)localObject).setName(Integer.valueOf(879786873), str1);
        ((PdfObject)localObject).setConstant(1147962727, 1217103210);
      }
      this.currentPdfFile.checkResolved((PdfObject)localObject);
      if (localObject != null)
        try
        {
          localPdfFont = this.pdfFontFactory.createFont((PdfObject)localObject, paramString, this.objectStoreStreamRef, this.renderPage, this.errorTracker, this.isPrinting);
        }
        catch (PdfException localPdfException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localPdfException.getMessage());
        }
      if ((localPdfFont != null) && (!this.isFlattenedForm))
        this.cache.resolvedFonts.put(paramString, localPdfFont);
    }
    return localPdfFont;
  }

  public boolean getBooleanValue(int paramInt)
  {
    switch (paramInt)
    {
    case -1:
      return this.pdfFontFactory.hasEmbeddedFonts();
    case -2:
      if (this.contentHandler == null)
        return false;
      return this.contentHandler.hasContent();
    case 1:
      return this.errorTracker.pageSuccessful;
    case 4:
      return this.pdfFontFactory.hasNonEmbeddedCIDFonts();
    case 2:
      return this.imagesProcessedFully;
    case 8:
      return this.hasYCCKimages;
    case 16:
      return this.isTimeout;
    case 32:
      return this.isTTHintingRequired;
    }
    throw new RuntimeException("Unknown value " + paramInt);
  }

  public void dispose()
  {
    if (this.pdfData != null)
      this.pdfData.dispose();
  }

  public void setIntValue(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case -10:
      this.pageNum = paramInt2;
      break;
    case 20:
      this.textPrint = paramInt2;
      break;
    default:
      super.setIntValue(paramInt1, paramInt2);
    }
  }

  public void setXMLExtraction(boolean paramBoolean)
  {
    this.isXMLExtraction = paramBoolean;
  }

  public void setParameters(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3)
  {
    super.setParameters(paramBoolean1, paramBoolean2, paramInt1, paramInt2, paramBoolean3);
    this.renderText = ((paramBoolean2) && ((paramInt1 & 0x1) == 1));
    this.textExtracted = ((paramInt2 & 0x1) == 1);
    this.textColorExtracted = ((paramInt2 & 0x40) == 64);
    this.colorExtracted = ((paramInt2 & 0x200) == 512);
    this.removeRenderImages = ((paramBoolean2) && ((paramInt1 & 0x10) == 16));
  }

  private void processXForm(int paramInt, PdfObject paramPdfObject, Shape paramShape, CommandParser paramCommandParser)
    throws PdfException
  {
    if ((!this.layerDecoder.isLayerVisible()) || ((this.layers != null) && (!this.layers.isVisible(paramPdfObject))) || (paramPdfObject == null))
      return;
    String str1 = this.formName;
    String str2 = paramCommandParser.generateOpAsString(0, true);
    String str3 = str2;
    try
    {
      if (ImageCommands.trackImages)
        if (this.imagesInFile == null)
          this.imagesInFile = (str3 + " Form");
        else
          this.imagesInFile = (str3 + " Form\n" + this.imagesInFile);
      paramCommandParser.reset();
      byte[] arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
      if (arrayOfByte != null)
      {
        String str4 = indent;
        indent += "   ";
        Object localObject1 = new float[6];
        float[] arrayOfFloat1 = paramPdfObject.getFloatArray(1145198201);
        int i = (arrayOfFloat1 == null) || (isIdentity(arrayOfFloat1)) ? 1 : 0;
        if (arrayOfFloat1 != null)
          localObject1 = arrayOfFloat1;
        Object localObject2 = (float[][])null;
        int j = this.depth;
        float f1 = -1.0F;
        float[][] arrayOfFloat3 = new float[3][3];
        for (int k = 0; k < 3; k++)
          System.arraycopy(this.gs.CTM[k], 0, arrayOfFloat3[k], 0, 3);
        localObject2 = arrayOfFloat3;
        float[][] arrayOfFloat2 = this.gs.CTM;
        if ((arrayOfFloat1 != null) && (i == 0))
        {
          localObject3 = new float[][] { { localObject1[0], localObject1[1], 0.0F }, { localObject1[2], localObject1[3], 0.0F }, { localObject1[4], localObject1[5], 1.0F } };
          localObject3 = Matrix.multiply((float[][])localObject3, arrayOfFloat2);
          this.gs.CTM = ((float[][])localObject3);
          f1 = localObject1[0] * this.gs.getLineWidth();
          if (f1 == 0.0F)
            f1 = localObject1[1] * this.gs.getLineWidth();
          if (f1 < 0.0F)
            f1 = -f1;
        }
        this.formLevel += 1;
        if (this.formLevel == 1)
          this.formName = str2;
        else if (this.formLevel < 20)
          this.formName = (this.formName + '_' + str2);
        Object localObject3 = (GenericColorSpace)this.gs.strokeColorSpace.clone();
        GenericColorSpace localGenericColorSpace = (GenericColorSpace)this.gs.nonstrokeColorSpace.clone();
        if (f1 > 0.0F)
          this.gs.setLineWidth(f1);
        float f2 = this.gs.getAlphaMax(1);
        float f3 = this.gs.getAlphaMax(2);
        this.gs.setMaxAlpha(1, this.gs.getAlpha(1));
        if (this.formLevel == 1)
          this.gs.setMaxAlpha(2, this.gs.getAlpha(2));
        PdfObjectCache localPdfObjectCache = this.cache.copy();
        this.cache.reset(localPdfObjectCache);
        PdfObject localPdfObject1 = paramPdfObject.getDictionary(2004251818);
        readResources(localPdfObject1, false);
        this.cache.groupObj = paramPdfObject.getDictionary(1111442775);
        this.currentPdfFile.checkResolved(this.cache.groupObj);
        float[] arrayOfFloat4 = paramPdfObject.getFloatArray(303185736);
        Area localArea = null;
        int m = 0;
        if ((arrayOfFloat4 != null) && (arrayOfFloat4[2] > 1.0F) && (arrayOfFloat4[3] > 1.0F) && (this.gs.getClippingShape() == null) && (this.gs.CTM[0][1] == 0.0F) && (this.gs.CTM[1][0] == 0.0F) && (this.gs.CTM[2][1] != 0.0F) && (this.gs.CTM[2][0] < 0.0F))
        {
          localArea = setClip(paramShape, arrayOfFloat4);
          m = 1;
        }
        else if ((arrayOfFloat4 != null) && (arrayOfFloat4[0] == 0.0F) && (arrayOfFloat4[1] == 0.0F) && (arrayOfFloat4[2] > 1.0F) && (arrayOfFloat4[3] > 1.0F) && (arrayOfFloat4[2] != arrayOfFloat4[3]) && ((this.gs.CTM[0][0] > 0.99D) || (this.gs.CTM[2][1] < -1.0F)) && ((this.gs.CTM[2][0] < -1.0F) || (this.gs.CTM[2][0] > 1.0F)) && (this.gs.CTM[2][1] != 0.0F))
        {
          localArea = setClip(paramShape, arrayOfFloat4);
          m = 1;
        }
        else if ((arrayOfFloat4 != null) && (arrayOfFloat4[0] == 0.0F) && (arrayOfFloat4[1] == 0.0F) && (arrayOfFloat4[2] > 1.0F) && (arrayOfFloat4[3] > 1.0F) && (this.gs.getClippingShape() != null))
        {
          localArea = setClip(paramShape, arrayOfFloat4);
          m = 1;
        }
        else if ((this.formLevel > 1) && (arrayOfFloat4 != null) && (arrayOfFloat4[0] > 50.0F) && (arrayOfFloat4[1] > 50.0F) && (this.gs.getClippingShape() != null) && (arrayOfFloat4[0] - 1.0F > this.gs.getClippingShape().getBounds().x) && (arrayOfFloat4[1] - 1.0F > this.gs.getClippingShape().getBounds().y))
        {
          localArea = setClip(paramShape, arrayOfFloat4);
          m = 1;
        }
        if (arrayOfByte.length > 0)
        {
          PdfObject localPdfObject2 = getSMask(arrayOfFloat4);
          int n = getFirstValue(this.gs.getBM());
          int i2;
          if ((this.extractionMode & 0x400) == 1024)
          {
            arrayOfFloat4 = paramPdfObject.getFloatArray(303185736);
            int i1 = (int)arrayOfFloat4[0];
            i2 = (int)arrayOfFloat4[1];
            int i3 = (int)arrayOfFloat4[2];
            int i4 = (int)arrayOfFloat4[3];
            if (i1 < 0)
              i1 = 0;
            BufferedImage localBufferedImage = createTransparentForm(paramPdfObject, i1, i2, i3, i4, true);
            String str5 = 'R' + this.formName;
            this.pdfImages.setImageInfo(str5, this.pageNum, this.gs.CTM[2][0], this.gs.CTM[2][1], i3, i4);
            this.objectStoreStreamRef.saveStoredImage("R" + str5, localBufferedImage, false, false, "jpg");
            this.objectStoreStreamRef.saveStoredImage(str5, localBufferedImage, false, false, "jpg");
          }
          if ((!isTransparent(this.cache.groupObj)) && ((this.flattenXFormToImage) || ((this.isPrinting) && (this.gs.CTM[2][0] == 0.0F) && (this.gs.CTM[2][1] == 0.0F) && (localPdfObject2 == null) && (n != 1451587725) && (this.gs.getAlpha(2) == 1.0F)) || (((this.gs.getAlpha(2) == 1.0F) || (this.layerDecoder.layerLevel > 0) || ((this.formLevel == 1) && (this.gs.getAlpha(2) < 0.1F))) && (localPdfObject2 == null) && (n != 1451587725))))
          {
            decodeStreamIntoObjects(arrayOfByte, false);
          }
          else if ((localPdfObject2 != null) || (n == 1451587725))
          {
            createMaskForm(paramPdfObject, str2, localPdfObject2, n);
          }
          else if ((this.tokenNumber == 1) && (this.formLevel == 1))
          {
            decodeStreamIntoObjects(arrayOfByte, false);
          }
          else
          {
            DynamicVectorRenderer localDynamicVectorRenderer = this.current;
            this.current = new SwingDisplay(this.pageNum, this.objectStoreStreamRef, false);
            this.current.setHiResImageForDisplayMode(this.useHiResImageForDisplay);
            i2 = localDynamicVectorRenderer.getType();
            if ((i2 == 4) || (i2 == 5) || (i2 == 7) || (i2 == 6))
              this.current.writeCustom(26, localDynamicVectorRenderer);
            boolean bool = this.renderDirectly;
            float f4 = this.gs.getAlpha(1);
            float f5 = this.gs.getAlphaMax(1);
            float f6 = this.gs.getAlpha(2);
            float f7 = this.gs.getAlphaMax(2);
            this.currentPdfFile.checkResolved(this.cache.pageGroupingObj);
            if ((this.cache.pageGroupingObj != null) && (this.renderDirectly))
            {
              this.gs.setMaxAlpha(1, 1.0F);
            }
            else
            {
              this.gs.setMaxAlpha(1, f4);
              this.gs.setMaxAlpha(2, f6);
            }
            if ((this.renderDirectly) && ((this.cache.pageGroupingObj == null) || ((this.cache.pageGroupingObj != null) && (this.cache.groupObj != null) && ((this.cache.groupObj.getDictionary(2087749783).getParameterConstant(2087749783) == 1247168582) || (this.cache.groupObj.getDictionary(2087749783).getParameterConstant(2087749783) != this.cache.pageGroupingObj.getDictionary(2087749783).getParameterConstant(2087749783))))))
              this.gs.setMaxAlpha(2, 1.0F);
            if (!this.renderDirectly)
            {
              this.gs.setAlpha(1, 1.0F);
              this.gs.setAlpha(2, 1.0F);
            }
            this.renderDirectly = false;
            localDynamicVectorRenderer.setGraphicsState(1, 1.0F);
            localDynamicVectorRenderer.setGraphicsState(2, 1.0F);
            decodeStreamIntoObjects(arrayOfByte, false);
            this.gs.setMaxAlpha(1, f5);
            this.gs.setMaxAlpha(2, f7);
            if (!this.renderDirectly)
            {
              this.gs.setAlpha(1, f4);
              this.gs.setAlpha(2, f6);
            }
            localDynamicVectorRenderer.drawXForm(this.current, this.gs);
            this.current = localDynamicVectorRenderer;
            this.current.setGraphicsState(1, f4);
            this.current.setGraphicsState(2, f6);
            this.renderDirectly = bool;
          }
        }
        if (m != 0)
        {
          this.gs.setClippingShape(localArea);
          this.current.drawClip(this.gs, localArea, false);
        }
        this.formLevel -= 1;
        while (this.depth > j)
          restoreGraphicsState(this.gs);
        if (localObject2 != null)
          this.gs.CTM = ((float[][])localObject2);
        else if ((this.gs.CTM[0][0] == 1.0F) && (this.gs.CTM[1][1] == 1.0F))
          this.gs.CTM = new float[][] { { 1.0F, 0.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } };
        this.gs.strokeColorSpace = ((GenericColorSpace)localObject3);
        this.gs.nonstrokeColorSpace = localGenericColorSpace;
        this.cache.restore(localPdfObjectCache);
        this.gs.setMaxAlpha(1, f2);
        this.gs.setMaxAlpha(2, f3);
        indent = str4;
      }
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localError.getMessage());
      this.imagesProcessedFully = false;
      this.errorTracker.addPageFailureMessage("Error " + localError + " in DO");
      if ((ExternalHandlers.throwMissingCIDError) && (localError.getMessage().contains("kochi")))
        throw localError;
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException);
      this.imagesProcessedFully = false;
      this.errorTracker.addPageFailureMessage("Error " + localException + " in DO");
    }
    this.formName = str1;
  }

  private PdfObject getSMask(float[] paramArrayOfFloat)
  {
    PdfObject localPdfObject = null;
    if ((this.gs.SMask != null) && (this.gs.SMask.getGeneralType(489767774) == 507461173))
      return null;
    if ((this.gs.SMask != null) && (paramArrayOfFloat != null) && (paramArrayOfFloat[2] > 0.0F) && ((this.gs.SMask.getParameterConstant(608780341) != 489767739) || (this.gs.SMask.getFloatArray(4627) != null)))
    {
      localPdfObject = this.gs.SMask.getDictionary(23);
      this.currentPdfFile.checkResolved(localPdfObject);
    }
    return localPdfObject;
  }

  private static int getFirstValue(PdfArrayIterator paramPdfArrayIterator)
  {
    int i = -1;
    if ((paramPdfArrayIterator != null) && (paramPdfArrayIterator.hasMoreTokens()))
      i = paramPdfArrayIterator.getNextValueAsConstant(false);
    return i;
  }

  private void createMaskForm(PdfObject paramPdfObject1, String paramString, PdfObject paramPdfObject2, int paramInt)
    throws PdfException
  {
    float[] arrayOfFloat = paramPdfObject1.getFloatArray(303185736);
    int i = (int)arrayOfFloat[0];
    int j = (int)arrayOfFloat[1];
    int k = (int)arrayOfFloat[2];
    int m = (int)arrayOfFloat[3];
    if (i < 0)
      i = 0;
    BufferedImage localBufferedImage = null;
    if (paramPdfObject2 != null)
    {
      localBufferedImage = getImageFromPdfObject(paramPdfObject1, i, k, j, m);
      localObject = getImageFromPdfObject(paramPdfObject2, i, k, j, m);
      PdfObject localPdfObject1 = null;
      PdfObject localPdfObject2 = paramPdfObject2.getDictionary(1111442775);
      if (localPdfObject2 != null)
      {
        this.currentPdfFile.checkResolved(localPdfObject2);
        localPdfObject1 = localPdfObject2.getDictionary(2087749783);
      }
      if (localPdfObject1 != null)
        this.currentPdfFile.checkResolved(localPdfObject1);
      localBufferedImage = ImageCommands.applySmask(localBufferedImage, (BufferedImage)localObject, paramPdfObject2, true, true, localPdfObject1, paramPdfObject1, this.gs);
      if (localObject != null)
        ((BufferedImage)localObject).flush();
    }
    else if (paramInt == 1451587725)
    {
      localBufferedImage = createTransparentForm(paramPdfObject1, i, j, k, m, false);
    }
    Object localObject = new GraphicsState(this.useJavaFX);
    ((GraphicsState)localObject).CTM = new float[][] { { localBufferedImage.getWidth(), 0.0F, 1.0F }, { 0.0F, localBufferedImage.getHeight(), 1.0F }, { 0.0F, 0.0F, 0.0F } };
    if (this.isFlattenedForm)
    {
      ((GraphicsState)localObject).x = this.flattenX;
      ((GraphicsState)localObject).y = this.flattenY;
    }
    else
    {
      ((GraphicsState)localObject).x = i;
      ((GraphicsState)localObject).y = (j - localBufferedImage.getHeight());
    }
    localObject.CTM[2][0] = ((GraphicsState)localObject).x;
    localObject.CTM[2][1] = ((GraphicsState)localObject).y;
    this.current.drawImage(this.pageNum, localBufferedImage, (GraphicsState)localObject, false, paramString, 1, -1);
  }

  private BufferedImage createTransparentForm(PdfObject paramPdfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    byte[] arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
    ObjectStore localObjectStore = new ObjectStore();
    SwingDisplay localSwingDisplay = new SwingDisplay(0, false, 20, localObjectStore);
    localSwingDisplay.setHiResImageForDisplayMode(true);
    PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(this.currentPdfFile, true, null);
    if (paramBoolean)
      localPdfStreamDecoder.setParameters(true, true, 3, 65, this.useJavaFX);
    else
      localPdfStreamDecoder.setParameters(this.isPageContent, this.renderPage, this.renderMode, this.extractionMode, this.useJavaFX);
    localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
    localPdfStreamDecoder.setIntValue(19, this.formLevel);
    localPdfStreamDecoder.setFloatValue(17, this.multiplyer);
    localPdfStreamDecoder.setFloatValue(12, this.samplingUsed);
    localPdfStreamDecoder.setObjectValue(23, localSwingDisplay);
    try
    {
      PdfObject localPdfObject = paramPdfObject.getDictionary(2004251818);
      if (localPdfObject != null)
        localPdfStreamDecoder.readResources(localPdfObject, false);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    if (arrayOfByte != null)
      localPdfStreamDecoder.decodeStreamIntoObjects(arrayOfByte, false);
    int i = paramInt4;
    if (paramInt2 > paramInt4)
      i = paramInt2 - paramInt4;
    int j = 4;
    BufferedImage localBufferedImage;
    if (paramBoolean)
      localBufferedImage = new BufferedImage(j * paramInt3, j * i, 2);
    else
      localBufferedImage = new BufferedImage(paramInt3, i, 2);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    if (paramBoolean)
    {
      localGraphics2D.setColor(Color.WHITE);
      localGraphics2D.fillRect(0, 0, j * paramInt3, j * i);
      localGraphics2D.translate(0, j * i);
      localGraphics2D.scale(1.0D, -1.0D);
      localGraphics2D.scale(j, j);
    }
    else if (!this.isFlattenedForm)
    {
      localGraphics2D.translate(-paramInt1, -paramInt4);
    }
    if (!paramBoolean)
      localGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.5F));
    localSwingDisplay.setG2(localGraphics2D);
    localSwingDisplay.paint(null, null, null);
    localObjectStore.flush();
    return localBufferedImage;
  }

  private static boolean isTransparent(PdfObject paramPdfObject)
  {
    boolean bool = false;
    if (paramPdfObject != null)
    {
      String str = paramPdfObject.getName(35);
      PdfObject localPdfObject = paramPdfObject.getDictionary(2087749783);
      bool = (str != null) && (str.equals("Transparency")) && (localPdfObject != null) && (localPdfObject.getParameterConstant(2087749783) == 1785221209);
    }
    return bool;
  }

  private Area setClip(Shape paramShape, float[] paramArrayOfFloat)
  {
    float f1 = this.gs.CTM[0][0];
    if (f1 == 0.0F)
      f1 = this.gs.CTM[0][1];
    float f2 = this.gs.CTM[1][1];
    if (f2 == 0.0F)
      f2 = this.gs.CTM[1][0];
    int i;
    int j;
    int k;
    int m;
    if ((this.gs.CTM[0][1] > 0.0F) && (this.gs.CTM[1][0] < 0.0F))
    {
      i = (int)(this.gs.CTM[2][0] - paramArrayOfFloat[3]);
      j = (int)(this.gs.CTM[2][1] + paramArrayOfFloat[0]);
      k = (int)((paramArrayOfFloat[3] - paramArrayOfFloat[1]) * f1);
      m = (int)((paramArrayOfFloat[2] - paramArrayOfFloat[0]) * f2);
    }
    else if ((this.gs.CTM[0][1] < 0.0F) && (this.gs.CTM[1][0] > 0.0F))
    {
      i = (int)(this.gs.CTM[2][0] + paramArrayOfFloat[1]);
      j = (int)(this.gs.CTM[2][1] - paramArrayOfFloat[2]);
      k = (int)((paramArrayOfFloat[3] - paramArrayOfFloat[1]) * -f1);
      m = (int)((paramArrayOfFloat[2] - paramArrayOfFloat[0]) * -f2);
    }
    else
    {
      i = (int)(this.gs.CTM[2][0] + paramArrayOfFloat[0]);
      j = (int)(this.gs.CTM[2][1] + paramArrayOfFloat[1] - 1.0F);
      k = (int)(1.0F + (paramArrayOfFloat[2] - paramArrayOfFloat[0]) * f1);
      m = (int)(2.0F + (paramArrayOfFloat[3] - paramArrayOfFloat[1]) * f2);
      if (this.gs.CTM[2][1] < 0.0F)
        m = (int)(m - this.gs.CTM[2][1] * f2);
      if (this.gs.CTM[2][0] < 0.0F)
        k = (int)(k - this.gs.CTM[2][0] * f2);
      if (this.gs.CTM[1][1] < 0.0F)
        j -= m;
    }
    Area localArea1;
    if (this.gs.getClippingShape() == null)
      localArea1 = null;
    else
      localArea1 = (Area)this.gs.getClippingShape().clone();
    Area localArea2 = new Area(new Rectangle(i, j, k, m));
    this.gs.updateClip(new Area(localArea2));
    this.current.drawClip(this.gs, paramShape, false);
    return localArea1;
  }

  private static boolean isIdentity(float[] paramArrayOfFloat)
  {
    boolean bool = true;
    if (paramArrayOfFloat != null)
      for (int i = 0; i < 6; i++)
        if (paramArrayOfFloat[i] != matches[i])
        {
          bool = false;
          break;
        }
    return bool;
  }

  private BufferedImage getImageFromPdfObject(PdfObject paramPdfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws PdfException
  {
    byte[] arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
    ObjectStore localObjectStore = new ObjectStore();
    SwingDisplay localSwingDisplay = new SwingDisplay(0, false, 20, localObjectStore);
    boolean bool = true;
    PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(this.currentPdfFile, bool, null);
    localPdfStreamDecoder.setParameters(this.isPageContent, this.renderPage, this.renderMode, this.extractionMode, this.useJavaFX);
    localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
    localSwingDisplay.setHiResImageForDisplayMode(bool);
    localPdfStreamDecoder.setObjectValue(23, localSwingDisplay);
    localPdfStreamDecoder.setFloatValue(17, this.multiplyer);
    localPdfStreamDecoder.setFloatValue(12, this.samplingUsed);
    localPdfStreamDecoder.setBooleanValue(24, this.isFlattenedForm);
    localPdfStreamDecoder.setIntValue(19, this.formLevel);
    localPdfStreamDecoder.setIntValue(26, 2);
    try
    {
      PdfObject localPdfObject = paramPdfObject.getDictionary(2004251818);
      if (localPdfObject != null)
        localPdfStreamDecoder.readResources(localPdfObject, false);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    if (arrayOfByte != null)
      localPdfStreamDecoder.decodeStreamIntoObjects(arrayOfByte, false);
    localPdfStreamDecoder.dispose();
    int i = paramInt4;
    if (paramInt3 > paramInt4)
      i = paramInt3 - paramInt4;
    if (paramInt2 == 0)
      paramInt2 = 1;
    BufferedImage localBufferedImage;
    try
    {
      localBufferedImage = new BufferedImage(paramInt2, i, 2);
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localError.getMessage());
      localBufferedImage = null;
    }
    if (localBufferedImage != null)
    {
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.translate(-paramInt1, -paramInt4);
      localSwingDisplay.setG2(localGraphics2D);
      localSwingDisplay.paint(null, null, null);
      localObjectStore.flush();
    }
    return localBufferedImage;
  }

  private void pushGraphicsState(GraphicsState paramGraphicsState)
  {
    if (!this.isStackInitialised)
    {
      this.isStackInitialised = true;
      this.graphicsStateStack = new Vector_Object(10);
      this.textStateStack = new Vector_Object(10);
      this.strokeColorStateStack = new Vector_Object(20);
      this.nonstrokeColorStateStack = new Vector_Object(20);
    }
    this.depth += 1;
    this.graphicsStateStack.push(paramGraphicsState.clone());
    this.textStateStack.push(this.currentTextState.clone());
    this.nonstrokeColorStateStack.push(paramGraphicsState.nonstrokeColorSpace.clone());
    this.strokeColorStateStack.push(paramGraphicsState.strokeColorSpace.clone());
    this.current.resetOnColorspaceChange();
  }

  private GraphicsState restoreGraphicsState(GraphicsState paramGraphicsState)
  {
    boolean bool = false;
    if (!this.isStackInitialised)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No GraphicsState saved to retrieve");
      paramGraphicsState = new GraphicsState(this.useJavaFX);
      this.currentTextState = new TextState();
    }
    else
    {
      this.depth -= 1;
      bool = paramGraphicsState.hasClipChanged();
      paramGraphicsState = (GraphicsState)this.graphicsStateStack.pull();
      this.currentTextState = ((TextState)this.textStateStack.pull());
      paramGraphicsState.strokeColorSpace = ((GenericColorSpace)this.strokeColorStateStack.pull());
      paramGraphicsState.nonstrokeColorSpace = ((GenericColorSpace)this.nonstrokeColorStateStack.pull());
      if (paramGraphicsState.strokeColorSpace.getID() == -2073385820)
        paramGraphicsState.strokeColorSpace.restoreColorStatus();
      if (paramGraphicsState.nonstrokeColorSpace.getID() == -2073385820)
        paramGraphicsState.nonstrokeColorSpace.restoreColorStatus();
    }
    if (this.renderPage)
    {
      if (bool)
        this.current.drawClip(paramGraphicsState, this.defaultClip, true);
      this.current.resetOnColorspaceChange();
      this.current.drawFillColor(paramGraphicsState.getNonstrokeColor());
      this.current.drawStrokeColor(paramGraphicsState.getStrokeColor());
      this.current.setGraphicsState(2, paramGraphicsState.getAlpha(2));
      this.current.setGraphicsState(1, paramGraphicsState.getAlpha(1));
    }
    return paramGraphicsState;
  }

  private GraphicsState Q(GraphicsState paramGraphicsState, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      pushGraphicsState(paramGraphicsState);
    }
    else
    {
      paramGraphicsState = restoreGraphicsState(paramGraphicsState);
      this.currentTextState.setFontChanged(true);
    }
    return paramGraphicsState;
  }

  private static void CM(GraphicsState paramGraphicsState, CommandParser paramCommandParser)
  {
    float[][] arrayOfFloat = new float[3][3];
    arrayOfFloat[0][0] = paramCommandParser.parseFloat(5);
    arrayOfFloat[0][1] = paramCommandParser.parseFloat(4);
    arrayOfFloat[0][2] = 0.0F;
    arrayOfFloat[1][0] = paramCommandParser.parseFloat(3);
    arrayOfFloat[1][1] = paramCommandParser.parseFloat(2);
    arrayOfFloat[1][2] = 0.0F;
    arrayOfFloat[2][0] = paramCommandParser.parseFloat(1);
    arrayOfFloat[2][1] = paramCommandParser.parseFloat(0);
    arrayOfFloat[2][2] = 1.0F;
    for (int i = 0; i < 3; i++)
      System.arraycopy(paramGraphicsState.CTM, 0, paramGraphicsState.lastCTM, 0, 3);
    paramGraphicsState.CTM = Matrix.multiply(arrayOfFloat, paramGraphicsState.CTM);
    if ((paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] > 0.0F) && (paramGraphicsState.CTM[1][0] > 0.0F) && (((paramGraphicsState.CTM[1][0] < 0.01D) && (paramGraphicsState.CTM[0][1] < 0.0F)) || ((paramGraphicsState.CTM[0][0] > 100.0F) && (paramGraphicsState.CTM[0][1] == paramGraphicsState.CTM[1][0]) && (paramGraphicsState.CTM[0][1] == 1.0F))))
    {
      paramGraphicsState.CTM[0][1] = 0.0F;
      paramGraphicsState.CTM[1][0] = 0.0F;
    }
  }

  private void F(boolean paramBoolean, int paramInt, PdfShape paramPdfShape)
  {
    Object localObject;
    if ((paramInt > 0) && (this.cache.groupObj != null) && (!this.cache.groupObj.getBoolean(27)) && (this.gs.getAlphaMax(2) > 0.84F) && (this.gs.nonstrokeColorSpace.getID() == 1498837125))
    {
      localObject = this.gs.getBM();
      int i = -1;
      if ((localObject != null) && (((PdfArrayIterator)localObject).hasMoreTokens()))
        i = ((PdfArrayIterator)localObject).getNextValueAsConstant(false);
      if ((this.gs.nonstrokeColorSpace.getColor().getRGB() == -1) || ((i == 1451587725) && (this.gs.getAlpha(2) == 1.0F)))
        return;
    }
    if ((this.gs.SMask != null) && (this.gs.nonstrokeColorSpace.getID() == 1498837125))
    {
      localObject = this.gs.SMask.getFloatArray(4627);
      if ((this.gs.nonstrokeColorSpace.getColor().getRGB() == -16777216) && (localObject != null) && (localObject[0] == 1.0F))
        return;
    }
    if ((this.gs.SMask != null) && (this.gs.nonstrokeColorSpace.getID() == 1247168582))
    {
      localObject = this.gs.SMask.getFloatArray(4627);
      if ((this.gs.nonstrokeColorSpace.getColor().getRGB() == -16777216) && (localObject != null) && (localObject[0] == 0.0F))
        return;
    }
    if ((this.gs.SMask != null) && (this.gs.SMask.getDictionary(23) != null) && (this.gs.nonstrokeColorSpace.getID() == 1785221209))
    {
      if ((this.gs.nonstrokeColorSpace.getColor().getRGB() == -1) && (this.gs.getOPM() == 1.0F))
        return;
      localObject = this.gs.SMask.getFloatArray(4627);
      if ((this.gs.nonstrokeColorSpace.getColor().getRGB() == -16777216) && (localObject != null) && (localObject[0] == 1.0F) && (localObject[1] == 1.0F) && (localObject[2] == 1.0F))
        return;
      try
      {
        createSMaskFill();
      }
      catch (PdfException localPdfException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localPdfException.getMessage());
      }
      return;
    }
    if ((this.gs.SMask != null) && (this.gs.SMask.getGeneralType(489767774) != 507461173) && (this.gs.nonstrokeColorSpace.getID() == 1785221209) && (this.gs.getOPM() == 1.0F) && (this.gs.nonstrokeColorSpace.getColor().getRGB() == -16777216))
      return;
    if (this.layerDecoder.isLayerVisible())
    {
      if (paramBoolean)
        paramPdfShape.setEVENODDWindingRule();
      else
        paramPdfShape.setNONZEROWindingRule();
      paramPdfShape.closeShape();
      localObject = null;
      Path localPath = null;
      if (this.useJavaFX)
        localPath = paramPdfShape.getPath();
      else
        localObject = paramPdfShape.generateShapeFromPath(this.gs.CTM, this.gs.getLineWidth(), 70, this.current.getType());
      int j = (localObject != null) || (localPath != null) ? 1 : 0;
      if (this.customShapeTracker != null)
        if (paramBoolean)
          this.customShapeTracker.addShape(this.tokenNumber, 17962, (Shape)localObject, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
        else
          this.customShapeTracker.addShape(this.tokenNumber, 70, (Shape)localObject, this.gs.nonstrokeColorSpace.getColor(), this.gs.strokeColorSpace.getColor());
      PdfArrayIterator localPdfArrayIterator;
      int m;
      float[] arrayOfFloat2;
      if ((this.gs.nonstrokeColorSpace.getID() == 1498837125) && (this.gs.getOPM() == 1.0F))
      {
        localPdfArrayIterator = this.gs.getBM();
        m = -1;
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
          m = localPdfArrayIterator.getNextValueAsConstant(false);
        if (m == 1451587725)
        {
          arrayOfFloat2 = this.gs.nonstrokeColorSpace.getRawValues();
          if ((arrayOfFloat2 != null) && (arrayOfFloat2[3] == 1.0F))
            j = 0;
        }
      }
      int i1;
      if ((j != 0) && (this.gs.nonstrokeColorSpace.getID() == 1247168582) && (this.gs.getOPM() == 1.0F))
      {
        localPdfArrayIterator = this.gs.getBM();
        m = -1;
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
          m = localPdfArrayIterator.getNextValueAsConstant(false);
        if (m == 1451587725)
        {
          arrayOfFloat2 = this.gs.nonstrokeColorSpace.getRawValues();
          i1 = 1;
          for (float f : arrayOfFloat2)
            if (f != 0.0F)
              i1 = 0;
          if (i1 != 0)
            j = 0;
        }
      }
      if ((j != 0) && (this.gs.getAlpha(2) < 1.0F) && (this.gs.nonstrokeColorSpace.getID() == 960981604) && (this.gs.getOPM() == 1.0F) && (this.gs.nonstrokeColorSpace.getColor().getRGB() == -16777216))
      {
        int k = 1;
        float[] arrayOfFloat1 = this.gs.nonstrokeColorSpace.getRawValues();
        if (arrayOfFloat1 != null)
        {
          int n = arrayOfFloat1.length;
          for (i1 = 0; i1 < n; i1++)
            if (arrayOfFloat1[i1] > 0.0F)
            {
              k = 0;
              i1 = n;
            }
        }
        if (k != 0)
          j = 0;
      }
      if ((j != 0) && (this.renderPage))
      {
        this.gs.setStrokeColor(this.gs.strokeColorSpace.getColor());
        this.gs.setNonstrokeColor(this.gs.nonstrokeColorSpace.getColor());
        this.gs.setFillType(2);
        if (this.useJavaFX)
          this.current.drawShape(localPath, this.gs, 70);
        else
          this.current.drawShape((Shape)localObject, this.gs, 70);
      }
    }
    paramPdfShape.setClip(false);
    paramPdfShape.resetPath();
  }

  private void createSMaskFill()
    throws PdfException
  {
    PdfObject localPdfObject = this.gs.SMask.getDictionary(23);
    this.currentPdfFile.checkResolved(localPdfObject);
    float[] arrayOfFloat = localPdfObject.getFloatArray(303185736);
    int i = (int)arrayOfFloat[0];
    int j = (int)arrayOfFloat[1];
    int k = (int)arrayOfFloat[2];
    int m = (int)arrayOfFloat[3];
    if (i < 0)
      i = 0;
    BufferedImage localBufferedImage = getImageFromPdfObject(localPdfObject, i, k, j, m);
    WritableRaster localWritableRaster = localBufferedImage.getRaster();
    int n = localWritableRaster.getWidth();
    int i1 = localWritableRaster.getHeight();
    int[] arrayOfInt1 = new int[4];
    int i3 = this.gs.nonstrokeColorSpace.getColor().getRGB();
    arrayOfInt1[0] = ((byte)(i3 >> 16 & 0xFF));
    arrayOfInt1[1] = ((byte)(i3 >> 8 & 0xFF));
    arrayOfInt1[2] = ((byte)(i3 & 0xFF));
    int[] arrayOfInt2 = { 0, 0, 0, 0 };
    for (int i4 = 0; i4 < i1; i4++)
      for (int i5 = 0; i5 < n; i5++)
      {
        localWritableRaster.getPixels(i5, i4, 1, 1, arrayOfInt1);
        int i2 = (arrayOfInt1[0] == 0) && (arrayOfInt1[1] == 0) && (arrayOfInt1[2] == 0) && (arrayOfInt1[3] == 255) ? 1 : 0;
        if (i2 != 0)
        {
          localWritableRaster.setPixels(i5, i4, 1, 1, arrayOfInt2);
        }
        else
        {
          int[] arrayOfInt3 = new int[4];
          arrayOfInt3[3] = 191;
          arrayOfInt3[0] = arrayOfInt1[0];
          arrayOfInt3[1] = arrayOfInt1[1];
          arrayOfInt3[2] = arrayOfInt1[2];
          localWritableRaster.setPixels(i5, i4, 1, 1, arrayOfInt3);
        }
      }
    GraphicsState localGraphicsState = new GraphicsState(this.useJavaFX);
    localGraphicsState.CTM = new float[][] { { localBufferedImage.getWidth(), 0.0F, 1.0F }, { 0.0F, localBufferedImage.getHeight(), 1.0F }, { 0.0F, 0.0F, 0.0F } };
    localGraphicsState.x = i;
    localGraphicsState.y = (j - localBufferedImage.getHeight());
    localGraphicsState.CTM[2][0] = localGraphicsState.x;
    localGraphicsState.CTM[2][1] = localGraphicsState.y;
    this.current.drawImage(this.pageNum, localBufferedImage, localGraphicsState, false, "F" + this.tokenNumber, 1, -1);
    localBufferedImage.flush();
  }

  public TextDecoder getTextDecoder(boolean paramBoolean1, boolean paramBoolean2)
  {
    TextDecoder localTextDecoder;
    if (this.markedContentExtracted)
    {
      localTextDecoder = new TextDecoder(this.layerDecoder, this.useJavaFX);
    }
    else
    {
      localTextDecoder = new TextDecoder(this.pdfData, this.isXMLExtraction, this.layerDecoder, this.useJavaFX);
      localTextDecoder.setReturnText(paramBoolean1);
    }
    localTextDecoder.setXFA(paramBoolean2);
    if (this.customGlyphTracker != null)
      localTextDecoder.setHandlerValue(12, this.customGlyphTracker);
    if (this.contentHandler != null)
      localTextDecoder.setHandlerValue(22, this.contentHandler);
    if (this.errorTracker != null)
      localTextDecoder.setHandlerValue(23, this.errorTracker);
    localTextDecoder.setParameters(this.isPageContent, this.renderPage, this.renderMode, this.extractionMode, this.isPrinting, this.useJavaFX);
    localTextDecoder.setFileHandler(this.currentPdfFile);
    localTextDecoder.setIntValue(19, this.formLevel);
    localTextDecoder.setIntValue(20, this.textPrint);
    localTextDecoder.setBooleanValue(19, this.renderDirectly);
    localTextDecoder.setBooleanValue(21, this.generateGlyphOnRender);
    localTextDecoder.setRenderer(this.current);
    return localTextDecoder;
  }

  static
  {
    SamplingFactory.setDownsampleMode(null);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.PdfStreamDecoder
 * JD-Core Version:    0.6.2
 */
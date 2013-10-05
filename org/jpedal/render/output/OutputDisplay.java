package org.jpedal.render.output;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.PageRanges;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.fonts.HTMLFontUtils;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.tt.conversion.GlyphMapping;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.TextState;
import org.jpedal.render.BaseDisplay;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.ShapeFactory;
import org.jpedal.render.output.io.CustomIO;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.StringUtils;
import org.jpedal.utils.repositories.Vector_Rectangle;
import sun.misc.BASE64Encoder;

public class OutputDisplay extends BaseDisplay
{
  public static boolean enableNewCmapCode = false;
  public static boolean convertT1Fonts = true;
  protected String packageName = "";
  protected String javaFxFileName = "";
  protected HashMap<String, Object[]> fontsToConvert = new HashMap();
  protected HashMap<String, HashMap<String, Integer>> widths = new HashMap();
  protected Rectangle[] objectAreas = null;
  protected boolean hasEmbeddedFonts = false;
  protected Map embeddedFonts = new HashMap();
  protected Set baseFontNames = new HashSet();
  protected boolean hasEmbededFonts = false;
  private Map imagesAlreadyWritten = new HashMap();
  protected int endPage;
  protected int pageGap = 50;
  String lastGlyf = "";
  boolean keepOriginalImage = false;
  protected String isOnlineConverter;
  protected boolean requiresTransform = false;
  protected boolean requiresTransformGlobal = false;
  protected boolean requiresTextGlobal = false;
  public static final int TEXT_SVG_REALTEXT = 1;
  public static final int TEXT_SVG_SHAPETEXT_SELECTABLE = 2;
  public static final int TEXT_SVG_SHAPETEXT_NONSELECTABLE = 3;
  public static final int TEXT_SVG_REALTEXT_NOFALLBACK = 4;
  public static final int TEXT_SVG_SHAPETEXT_SELECTABLE_NOFALLBACK = 5;
  public static final int TEXT_SVG_SHAPETEXT_NONSELECTABLE_NOFALLBACK = 6;
  public static final int TEXT_IMAGE_REALTEXT = 7;
  public static final int TEXT_IMAGE_SHAPETEXT_SELECTABLE = 8;
  public static final int TEXT_IMAGE_SHAPETEXT_NONSELECTABLE = 9;
  public static final int VIEW_NOVIEWER = 0;
  public static final int VIEW_MULTIFILE = 1;
  public static final int VIEW_MULTIFILE_SPLITSPREADS = 3;
  public static final int VIEW_SINGLEFILE = 5;
  public static final int VIEW_SINGLEFILE_SPLITSPREADS = 7;
  public static final int VIEW_SINGLEFILE_HORIZONTAL = 9;
  public static final int VIEW_PAGETURNING = 11;
  public static final int VIEW_PAGETURNING_NOAJAX = 12;
  public static final int NAV_NONE = 1;
  public static final int NAV_CSS = 2;
  public static final int NAV_IMAGES = 3;
  public static final int NAV_THUMBNAILS_TOP = 4;
  public static final int NAV_THUMBNAILS_BOTTOM = 5;
  public static final int NAV_THUMBNAILS_LEFT = 6;
  public static final int NAV_THUMBNAILS_RIGHT = 7;
  protected static final int TOP = 0;
  protected static final int BOTTOM = 1;
  protected static final int LEFT = 2;
  protected static final int RIGHT = 3;
  protected String clip = null;
  protected static OutputHelper Helper = null;
  FontMapper fontMapper = null;
  String lastFontUsed = "";
  private HashMap<String, HashMap<String, GlyphMapping>> newFontCmaps = new HashMap();
  private HashMap<String, HashSet<String>> newFontMappedValues = new HashMap();
  private Map usedFontIDs = new HashMap();
  protected boolean includeClip = true;
  protected Map embeddedFontsByFontID = new HashMap();
  private Map glyfsRasterized = new HashMap();
  public static final int FontMode = 1;
  public static final int OutputThumbnails = 2;
  public static final int HasJavaScript = 13;
  public static final int CustomIO = 21;
  public static final int ConvertPDFExternalFileToOutputType = 38;
  public static final int GetIsSinglePage = 39;
  public static final int WriteImage = 41;
  protected boolean isSVGMode;
  protected boolean isTextSelectable;
  protected boolean isRealText;
  protected boolean disableImageFallback = false;
  public static final int IsSVGMode = 44;
  public static final int IsTextSelectable = 45;
  public static final int IsRealText = 46;
  public static final int DisableImageFallback = 47;
  public static final int PageTurning = 55;
  public static final int AddJavaScript = 56;
  public static final int ThumbnailNavBar = 57;
  public static final int EmbedImagesAsBase64 = 58;
  public static final int Base64Background = 59;
  protected boolean isSingleFileOutput = false;
  private boolean groupGlyphsInTJ = true;
  public static final boolean debugForms = false;
  private static final boolean DISABLE_IMAGES = false;
  private static final boolean DISABLE_SHAPE = false;
  private static final boolean DISABLE_TEXT = false;
  protected static final boolean DEBUG_TEXT_AREA = false;
  protected static final boolean DEBUG_DRAW_PAGE_BORDER = false;
  public static final int TOFILE = 0;
  public static final int TOP_SECTION = 1;
  public static final int SCRIPT = 2;
  public static final int FORM = 3;
  public static final int CSS = 4;
  public static final int FORM_CSS = 5;
  public static final int TEXT = 6;
  public static final int JSIMAGESPECIAL = 9;
  public static final int SAVE_EMBEDDED_FONT = 10;
  public static final int IMAGE_CONTROLLER = 12;
  public static final int FXMLPDFSTAGE = 13;
  public static final int FONT_AS_SHAPE = 14;
  public static final int FXMLTEXT = 15;
  public static final int FORMJS = 16;
  public static final int FORMJS_ONLOAD = 17;
  public static final int NAVBAR = 18;
  public static final int EXTERNAL_JS = 19;
  public static final int TEXTJS = 20;
  public static final int CALCULATION_ORDER = 21;
  public static final int SVGINHTML = 22;
  public static final int SVGCLIPS = 23;
  public static final int OUTLINES_ON_PAGE = 24;
  public static final int SVGBUFFER = 25;
  public static final int DVR = 26;
  public static final int LEGACY_CSS = 28;
  public static final int BOOKMARK = 29;
  protected OutputImageController imageController = null;
  protected StringBuilder script = new StringBuilder(10000);
  protected StringBuilder svgInHTML = new StringBuilder(10000);
  protected StringBuilder svgClips = new StringBuilder(10000);
  protected ArrayList<String> fxScript = new ArrayList();
  protected ArrayList<String> fxmlText = new ArrayList();
  protected ArrayList<String> calculationOrder = new ArrayList();
  protected HashMap<Integer, String> base64Images = new HashMap();
  protected HashMap<Integer, String> base64Shades = new HashMap();
  protected StringBuilder fonts_as_shapes = new StringBuilder(10000);
  protected StringBuilder formJS = new StringBuilder(10000);
  protected StringBuilder formJSOnLoad = new StringBuilder(10000);
  protected StringBuilder form = new StringBuilder(10000);
  protected StringBuilder testDivs = new StringBuilder(10000);
  protected StringBuilder topSection = new StringBuilder(10000);
  protected StringBuilder form_css = new StringBuilder(10000);
  protected StringBuilder css = new StringBuilder(10000);
  protected StringBuilder fxmlPDFStage = new StringBuilder(10000);
  protected StringBuilder navbarSection = new StringBuilder(10000);
  protected StringBuilder externalJSFile = new StringBuilder(10000);
  protected StringBuilder textJS = new StringBuilder(3000);
  protected StringBuilder svgBuffer = new StringBuilder(10000);
  protected StringBuilder legacyCss = new StringBuilder(10000);
  protected boolean userControlledImageScaling = false;
  protected int textID = 1;
  protected int shadeId = 0;
  protected int imageId = 0;
  protected int dpCount = 0;
  protected int clipCount = 0;
  boolean newClip = false;
  public String rootDir = null;
  public String fileName = null;
  protected int dx;
  protected int dy;
  protected int dr;
  protected boolean excludeMetadata = false;
  protected boolean addNavBar = false;
  protected boolean isThumbnailNavBar = false;
  protected boolean isCSSNavBar = false;
  protected boolean isImagesNavBar = false;
  protected int navPosition = 1;
  protected TextBlock currentTextBlock;
  protected TextBlock previousTextBlock;
  protected float lastFontSize;
  protected TextPosition currentTextPosition;
  protected Rectangle2D cropBox;
  protected Point2D midPoint;
  protected int canvasHeightScaled;
  protected int canvasWidthScaled;
  protected int currentColor = 0;
  protected String[] encodingType = { "UTF-8", "utf-8" };
  protected static final int JAVA_TYPE = 0;
  protected static final int OUTPUT_TYPE = 1;
  protected boolean jsImagesAdded = false;
  protected boolean hasJavascript = false;
  protected String pageNumberAsString = null;
  protected PdfPageData pageData;
  private int currentTokenNumber = -1;
  private int lastTokenNumber = 496;
  protected String imageName;
  protected float iw;
  protected float ih;
  protected double[] coords;
  protected float[][] lastTrm;
  protected CustomIO customIO = new DefaultIO();
  protected String imageArray = null;
  protected int[] currentImage;
  protected int[] currentPatternedShape;
  protected String currentPatternedShapeName;
  protected BufferedImage base64Background;
  protected String[] jsCalculationOrder;
  protected boolean addTwitter = false;
  protected String viaTwitter = "";
  protected boolean addFacebook = false;
  protected boolean addGooglePlus = false;
  protected boolean addReddit = false;
  protected boolean addLinkedIn = false;
  protected boolean addDigg = false;
  protected boolean addStumbleUpon = false;
  protected boolean addTumblr = false;
  protected boolean enableAnalytics = false;
  protected boolean enablePageTurning = false;
  protected boolean enablePageTurningAjax = false;
  protected boolean enableMagazineSplitSpreads = false;
  protected boolean enableSinglePageHorizontal = false;
  protected boolean enableTouchEvents = false;
  protected PageRanges pageRange;
  protected boolean fullNumbering;
  protected int viewMode;
  private int textMode;
  protected float scaling;
  protected String googleAnalyticsID;
  protected String pageTurningAnalyticsPrefix;
  protected String insertIntoHead;
  protected String firstPageName;
  protected boolean enableComments;
  protected String bodyBackgroundColor;
  protected boolean embedImageAsBase64;
  private int navMode;
  protected boolean useWordSpacing;
  protected boolean addJavaScript;
  private boolean convertSpacesTonbsp;
  private boolean convertPDFExternalFileToOutputType;
  protected String formTag;
  protected int fontMode;
  protected boolean enableOTFConversion;
  protected boolean writeEveryGlyf;
  protected boolean addBorder;
  protected String userHeadIndex;
  protected String userTopIndex;
  protected String userLeftIndex;
  protected String userBottomIndex;
  protected String userRightIndex;
  protected String sitemapURL;
  protected boolean inlineSVG;
  protected boolean outputThumbnails;
  protected boolean ipadCompatibilityMode;
  protected boolean IECompatibilityMode;
  protected String[] includedFonts;
  protected Integer[] pageNums;
  protected int outputPageNumber;
  protected int pageCount;
  protected boolean usingCachedImage = false;
  protected int cachedImageId = 0;
  protected int objectAreaCount = -1;
  static final int[] indices = { 1, 10, 100, 1000 };

  public OutputDisplay(int paramInt1, String paramString, int paramInt2, int paramInt3, PdfPageData paramPdfPageData, ConversionOptions paramConversionOptions)
  {
    this.type = paramInt1;
    this.rootDir = paramString;
    this.pageCount = paramInt3;
    this.endPage = this.pageCount;
    this.pageData = paramPdfPageData;
    this.rawPageNumber = paramInt2;
    this.outputPageNumber = this.rawPageNumber;
    this.objectStoreRef = new ObjectStore();
    this.addBackground = false;
    setSettings(paramConversionOptions);
    validateSettings();
    if (!this.excludeMetadata)
      if (this.outputPageNumber == 1)
      {
        this.pageNumberAsString = this.firstPageName;
        this.fileName = this.pageNumberAsString;
      }
      else
      {
        this.pageNumberAsString = StringUtils.getPageNumberAsString(this.outputPageNumber, this.pageCount);
        this.fileName = this.pageNumberAsString;
      }
    float f1 = paramPdfPageData.getCropBoxX2D(this.rawPageNumber);
    float f2 = paramPdfPageData.getCropBoxY2D(this.rawPageNumber);
    float f3 = paramPdfPageData.getCropBoxWidth2D(this.rawPageNumber);
    float f4 = paramPdfPageData.getCropBoxHeight2D(this.rawPageNumber);
    this.dx = paramPdfPageData.getCropBoxX(this.rawPageNumber);
    this.dy = paramPdfPageData.getCropBoxY(this.rawPageNumber);
    this.dr = paramPdfPageData.getRotation(this.rawPageNumber);
    this.cropBox = new Rectangle2D.Double(0.0D, 0.0D, f3, f4);
    this.midPoint = new Point2D.Double(f3 / 2.0F + f1, f4 / 2.0F + f2);
    if ((this.dr == 90) || (this.dr == 270))
    {
      this.canvasHeightScaled = ((int)(f3 * this.scaling));
      this.canvasWidthScaled = ((int)(f4 * this.scaling));
    }
    else
    {
      this.canvasWidthScaled = ((int)(f3 * this.scaling));
      this.canvasHeightScaled = ((int)(f4 * this.scaling));
    }
    this.areas = new Vector_Rectangle(100);
    this.imageAndShapeAreas = new Vector_Rectangle(100);
  }

  private void setSettings(ConversionOptions paramConversionOptions)
  {
    if ((this.type == 6) || (this.type == 7))
    {
      this.navMode = 1;
      this.textMode = (this.type == 6 ? 1 : 3);
      this.viewMode = 1;
      this.fontMode = 3;
      this.pageRange = new PageRanges(new StringBuilder().append("1 - ").append(this.pageCount).toString());
      this.scaling = getScalingValue("1.0", this.rawPageNumber);
      this.fullNumbering = true;
      this.excludeMetadata = true;
      return;
    }
    this.isOnlineConverter = System.getProperty("IsOnlineConverter");
    PageRanges localPageRanges = paramConversionOptions.getLogicalPageRange();
    if (localPageRanges != null)
    {
      this.fullNumbering = false;
      this.pageRange = localPageRanges;
    }
    else
    {
      this.fullNumbering = true;
      this.pageRange = paramConversionOptions.getRealPageRange();
    }
    this.excludeMetadata = (!paramConversionOptions.getIncludeViewer());
    this.viewMode = paramConversionOptions.getViewMode().getValue();
    this.textMode = paramConversionOptions.getTextMode().getValue();
    switch (1.$SwitchMap$org$jpedal$render$output$ConversionOptions$ScalingMode[paramConversionOptions.getScalingMode().ordinal()])
    {
    case 1:
      this.scaling = getScalingValue(new StringBuilder().append("").append(paramConversionOptions.getScaling()).toString(), this.rawPageNumber);
      break;
    case 2:
      this.scaling = getScalingValue(new StringBuilder().append("fitwidth").append(paramConversionOptions.getScalingFitWidth()).toString(), this.rawPageNumber);
      break;
    case 3:
      this.scaling = getScalingValue(new StringBuilder().append("fitheight").append(paramConversionOptions.getScalingFitHeight()).toString(), this.rawPageNumber);
      break;
    case 4:
      localObject1 = paramConversionOptions.getScalingFitWidthHeight();
      this.scaling = getScalingValue(new StringBuilder().append(localObject1[0]).append("x").append(localObject1[1]).toString(), this.rawPageNumber);
    }
    this.firstPageName = paramConversionOptions.getFirstPageName();
    this.embedImageAsBase64 = paramConversionOptions.getEmbedImagesAsBase64Stream();
    this.navMode = paramConversionOptions.getNavMode().getValue();
    this.convertSpacesTonbsp = paramConversionOptions.getConvertSpacesToNbsp();
    this.convertPDFExternalFileToOutputType = paramConversionOptions.getConvertPDFExternalFileToOutputType();
    this.formTag = paramConversionOptions.getFormTag();
    this.fontMode = paramConversionOptions.getFontMode().getValue();
    this.enableOTFConversion = paramConversionOptions.getConvertOTFFonts();
    this.writeEveryGlyf = paramConversionOptions.getKeepGlyfsSeparate();
    this.encodingType = paramConversionOptions.getEncoding();
    this.keepOriginalImage = paramConversionOptions.getKeepOriginalImage();
    this.outputThumbnails = paramConversionOptions.getOutputThumbnails();
    Object localObject1 = paramConversionOptions.getIncludedFonts();
    this.includedFonts = new String[localObject1.length];
    for (int i = 0; i < localObject1.length; i++)
    {
      Object localObject3 = localObject1[i];
      this.includedFonts[i] = localObject3.name().toLowerCase();
    }
    Object localObject2;
    if (this.type == 4)
    {
      localObject2 = (HTMLConversionOptions)paramConversionOptions;
      this.googleAnalyticsID = ((HTMLConversionOptions)localObject2).getGoogleAnalyticsID();
      if (this.googleAnalyticsID != null)
        this.enableAnalytics = true;
      this.pageTurningAnalyticsPrefix = ((HTMLConversionOptions)localObject2).getPageTurningAnalyticsPrefix();
      this.insertIntoHead = ((HTMLConversionOptions)localObject2).getInsertIntoHead();
      this.enableComments = (!((HTMLConversionOptions)localObject2).getDisableComments());
      this.bodyBackgroundColor = ((HTMLConversionOptions)localObject2).getBackgroundColor();
      this.useWordSpacing = ((HTMLConversionOptions)localObject2).getUseWordSpacing();
      this.addJavaScript = ((HTMLConversionOptions)localObject2).getAddJavaScript();
      sortSocialMediaLinks(((HTMLConversionOptions)localObject2).getSocialMediaLinks());
      this.userHeadIndex = ((HTMLConversionOptions)localObject2).getUserHeadIndex();
      this.userTopIndex = ((HTMLConversionOptions)localObject2).getUserTopIndex();
      this.userLeftIndex = ((HTMLConversionOptions)localObject2).getUserLeftIndex();
      this.userRightIndex = ((HTMLConversionOptions)localObject2).getUserRightIndex();
      this.userBottomIndex = ((HTMLConversionOptions)localObject2).getUserBottomIndex();
      this.sitemapURL = ((HTMLConversionOptions)localObject2).getSitemapURL();
      this.inlineSVG = ((HTMLConversionOptions)localObject2).getInlineSVG();
      this.ipadCompatibilityMode = true;
      this.IECompatibilityMode = ((HTMLConversionOptions)localObject2).getIECompatibilityMode();
    }
    if (this.type == 5)
    {
      localObject2 = (SVGConversionOptions)paramConversionOptions;
      this.addBorder = ((SVGConversionOptions)localObject2).getAddBorder();
    }
  }

  private void validateSettings()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = this.pageRange.next(0); i != -1; i = this.pageRange.next(i))
      localArrayList.add(Integer.valueOf(i));
    this.pageNums = ((Integer[])localArrayList.toArray(new Integer[localArrayList.size()]));
    if (!this.fullNumbering)
    {
      this.pageCount = this.pageNums.length;
      this.endPage = this.pageCount;
      this.outputPageNumber = convertRawPageToOutputPageNumber(this.rawPageNumber);
    }
    switch (this.textMode)
    {
    case 4:
      this.disableImageFallback = true;
    case 1:
      this.isSVGMode = true;
      this.isTextSelectable = true;
      this.isRealText = true;
      break;
    case 5:
      this.disableImageFallback = true;
    case 2:
      this.isSVGMode = true;
      this.isTextSelectable = true;
      this.isRealText = false;
      break;
    case 6:
      this.disableImageFallback = true;
    case 3:
      this.isSVGMode = true;
      this.isTextSelectable = false;
      this.isRealText = false;
      break;
    case 7:
      this.isSVGMode = false;
      this.isTextSelectable = true;
      this.isRealText = true;
      break;
    case 8:
      this.isSVGMode = false;
      this.isTextSelectable = true;
      this.isRealText = false;
      break;
    case 9:
      this.isSVGMode = false;
      this.isTextSelectable = false;
      this.isRealText = false;
    }
    if (this.excludeMetadata)
      this.viewMode = 0;
    switch (this.viewMode)
    {
    case 0:
      this.excludeMetadata = true;
      this.isSingleFileOutput = false;
      this.enableMagazineSplitSpreads = false;
      this.enableSinglePageHorizontal = false;
      break;
    case 1:
      this.isSingleFileOutput = false;
      this.enableMagazineSplitSpreads = false;
      this.enableSinglePageHorizontal = false;
      break;
    case 3:
      this.isSingleFileOutput = false;
      this.enableMagazineSplitSpreads = true;
      this.enableSinglePageHorizontal = false;
      break;
    case 5:
      this.isSingleFileOutput = true;
      this.enableMagazineSplitSpreads = false;
      this.enableSinglePageHorizontal = false;
      break;
    case 7:
      this.isSingleFileOutput = true;
      this.enableMagazineSplitSpreads = true;
      this.enableSinglePageHorizontal = false;
      break;
    case 9:
      this.isSingleFileOutput = true;
      this.enableMagazineSplitSpreads = false;
      this.enableSinglePageHorizontal = true;
      break;
    case 11:
      this.isSingleFileOutput = false;
      this.enablePageTurning = true;
      this.enablePageTurningAjax = true;
      break;
    case 12:
      this.isSingleFileOutput = true;
      this.enablePageTurning = true;
      this.enablePageTurningAjax = false;
    case 2:
    case 4:
    case 6:
    case 8:
    case 10:
    }
    switch (this.navMode)
    {
    case 1:
      this.addNavBar = false;
      break;
    case 2:
      this.isCSSNavBar = true;
      break;
    case 3:
      this.isImagesNavBar = true;
      break;
    case 5:
      this.navPosition = 1;
      this.isThumbnailNavBar = true;
      break;
    case 4:
      this.navPosition = 0;
      this.isThumbnailNavBar = true;
      break;
    case 6:
      this.navPosition = 2;
      this.isThumbnailNavBar = true;
      break;
    case 7:
      this.navPosition = 3;
      this.isThumbnailNavBar = true;
    }
    if ((this.isCSSNavBar) || (this.isImagesNavBar) || (this.isThumbnailNavBar))
      this.addNavBar = true;
    if (this.enablePageTurning)
      this.isSVGMode = false;
    if ((this.enablePageTurning) || ((this.isSingleFileOutput) && ((this.enableMagazineSplitSpreads) || (this.enableSinglePageHorizontal))))
    {
      this.addNavBar = false;
      this.isThumbnailNavBar = false;
      this.isImagesNavBar = false;
      this.isCSSNavBar = false;
    }
    if (this.enablePageTurning)
      this.excludeMetadata = (this.outputPageNumber != 1);
    if (this.enablePageTurningAjax)
      this.excludeMetadata = true;
    if (this.excludeMetadata)
    {
      this.fileName = String.valueOf(this.outputPageNumber);
      this.pageNumberAsString = this.fileName;
      this.addNavBar = false;
    }
    if (this.pageCount == 1)
      this.addNavBar = false;
    if ((this.addNavBar) && (this.isCSSNavBar) && (this.isSingleFileOutput))
    {
      float f1 = this.pageData.getCropBoxWidth2D(1);
      float f2 = this.pageData.getCropBoxHeight2D(1);
      for (int j = 2; j <= this.pageCount; j++)
        if ((this.pageData.getCropBoxWidth2D(j) != f1) || (this.pageData.getCropBoxHeight2D(j) != f2))
        {
          this.addNavBar = false;
          break;
        }
    }
    if (!this.addNavBar)
    {
      this.isCSSNavBar = false;
      this.isImagesNavBar = false;
      this.isThumbnailNavBar = false;
    }
    if (this.inlineSVG)
      this.inlineSVG = ((this.isSVGMode) && (this.disableImageFallback));
  }

  private float getScalingValue(String paramString, int paramInt)
  {
    float f1 = 1.527778F;
    try
    {
      if (paramString != null)
      {
        String str1;
        if (paramString.contains("x"))
        {
          str1 = paramString;
          String str2 = "x";
          String[] arrayOfString = str1.split(str2);
          float f4 = Integer.valueOf(arrayOfString[0]).intValue();
          float f5 = Integer.valueOf(arrayOfString[1]).intValue();
          f4 += 0.99F;
          f5 += 0.99F;
          float f6 = this.pageData.getCropBoxWidth2D(paramInt);
          float f7 = this.pageData.getCropBoxHeight2D(paramInt);
          float f8 = f4 / f6;
          float f9 = f5 / f7;
          f1 = f8;
          if (f1 > f9)
            f1 = f9;
        }
        else
        {
          float f2;
          float f3;
          if (paramString.contains("fitwidth"))
          {
            str1 = paramString.substring(8);
            f2 = Integer.valueOf(str1).intValue() + 0.99F;
            f3 = this.pageData.getCropBoxWidth2D(paramInt);
            f1 = f2 / f3;
          }
          else if (paramString.contains("fitheight"))
          {
            str1 = paramString.substring(9);
            f2 = Integer.valueOf(str1).intValue() + 0.99F;
            f3 = this.pageData.getCropBoxHeight2D(paramInt);
            f1 = f2 / f3;
          }
          else if (paramString.equals("none"))
          {
            f1 = 1.0F;
          }
          else
          {
            f1 *= Float.parseFloat(paramString);
          }
        }
      }
      return f1;
    }
    catch (Exception localException)
    {
    }
    return -1.0F;
  }

  private void sortSocialMediaLinks(HTMLConversionOptions.SocialMediaLinks[] paramArrayOfSocialMediaLinks)
  {
    for (HTMLConversionOptions.SocialMediaLinks localSocialMediaLinks : paramArrayOfSocialMediaLinks)
      switch (1.$SwitchMap$org$jpedal$render$output$HTMLConversionOptions$SocialMediaLinks[localSocialMediaLinks.ordinal()])
      {
      case 1:
        break;
      case 2:
        this.addTwitter = true;
        this.addFacebook = true;
        this.addGooglePlus = true;
        this.addReddit = true;
        this.addLinkedIn = true;
        this.addDigg = true;
        this.addStumbleUpon = true;
        this.addTumblr = true;
        break;
      case 3:
        this.addTwitter = true;
        break;
      case 4:
        this.addFacebook = true;
        break;
      case 5:
        this.addGooglePlus = true;
        break;
      case 6:
        this.addReddit = true;
        break;
      case 7:
        this.addLinkedIn = true;
        break;
      case 8:
        this.addDigg = true;
        break;
      case 9:
        this.addStumbleUpon = true;
        break;
      case 10:
        this.addTumblr = true;
      }
  }

  public boolean getBooleanValue(int paramInt)
  {
    switch (paramInt)
    {
    case 56:
      return this.addJavaScript;
    case 38:
      return this.convertPDFExternalFileToOutputType;
    case 47:
      return this.disableImageFallback;
    case 58:
      return this.embedImageAsBase64;
    case 39:
      return this.isSingleFileOutput;
    case 44:
      return this.isSVGMode;
    case 46:
      return this.isRealText;
    case 45:
      return this.isTextSelectable;
    case 2:
      return this.outputThumbnails;
    case 55:
      return this.enablePageTurning;
    case 57:
      return this.isThumbnailNavBar;
    case 13:
      return this.hasJavascript;
    }
    return super.getBooleanValue(paramInt);
  }

  public int getValue(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    case 1:
      i = this.fontMode;
    }
    return i;
  }

  public void init(int paramInt1, int paramInt2, int paramInt3, Color paramColor)
  {
    this.pageRotation = paramInt3;
    this.backgroundColor = paramColor;
    this.shadeId = 0;
    if (this.type == 5)
    {
      this.currentTextBlock = new SVGTextBlock();
      this.previousTextBlock = new SVGTextBlock();
    }
    else
    {
      this.currentTextBlock = new TextBlock();
      this.previousTextBlock = new TextBlock();
    }
  }

  public synchronized void writeCustom(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    case 5:
      this.form_css.append(paramObject.toString());
      break;
    case 12:
      this.imageController = ((OutputImageController)paramObject);
      this.userControlledImageScaling = (this.imageController != null);
      break;
    case 59:
      this.base64Background = ((BufferedImage)paramObject);
      break;
    case 21:
      this.customIO = ((CustomIO)paramObject);
      break;
    case 13:
      this.hasJavascript = ((Boolean)paramObject).booleanValue();
      break;
    case 24:
      this.objectAreas = ((Rectangle[])paramObject);
      break;
    case 41:
      Object[] arrayOfObject1 = (Object[])paramObject;
      this.customIO.writeImage((String)arrayOfObject1[0], (String)arrayOfObject1[1], (BufferedImage)arrayOfObject1[2]);
      break;
    case 10:
      Object[] arrayOfObject2 = (Object[])paramObject;
      PdfFont localPdfFont = (PdfFont)arrayOfObject2[0];
      String str1 = sanitizeFontName(localPdfFont.getBaseFontName());
      String str2 = (String)arrayOfObject2[2];
      String str3 = (String)arrayOfObject2[3];
      localPdfFont.resetNameForHTML(str1);
      String str4 = new StringBuilder().append(this.rootDir).append(this.fileName).append("/fonts/").toString();
      byte[] arrayOfByte = (byte[])arrayOfObject2[1];
      Object localObject;
      if ((!enableNewCmapCode) && (str2.equals("ttf")) && (!str1.contains(",")))
      {
        if ((this.embeddedFonts.containsKey(str1)) && (!this.baseFontNames.contains(localPdfFont.getBaseFontName())))
          this.baseFontNames.add(localPdfFont.getBaseFontName());
        if (this.enableOTFConversion)
          str2 = "otf";
        else
          str2 = "woff";
        localObject = getFontConversionTypes();
        writeOutFont(arrayOfObject2, str4, (String[])localObject);
      }
      else if (((str2.equals("t1")) && (convertT1Fonts)) || (str2.equals("cff")) || ((str2.equals("ttf")) && (!str1.contains(","))))
      {
        this.hasEmbeddedFonts = true;
        this.fontsToConvert.put(str1, arrayOfObject2);
        if (this.enableOTFConversion)
          str2 = "otf";
        else
          str2 = "woff";
      }
      if (arrayOfByte != null)
      {
        localObject = new StringBuilder();
        if (this.type == 6)
        {
          ((StringBuilder)localObject).append("Font.loadFont(");
          ((StringBuilder)localObject).append(this.javaFxFileName).append(".class.getResource(\"").append(this.fileName).append("/fonts/").append(str1).append('.').append(str2).append("\").toExternalForm(),10);\n");
          this.embeddedFonts.put(str1, localObject);
        }
        else
        {
          this.embeddedFonts.put(str1, getFontFaceCSS(str1, getFontConversionTypes()));
        }
        this.hasEmbededFonts = true;
        String str5 = (String)this.embeddedFontsByFontID.get(str1);
        if (str5 == null)
          this.embeddedFontsByFontID.put(str1, str3);
        else
          this.embeddedFontsByFontID.put(str1, new StringBuilder().append(str5).append(',').append(str3).toString());
      }
      break;
    default:
      throw new RuntimeException(new StringBuilder().append("Option ").append(paramInt).append(" not recognised").toString());
    }
  }

  protected String sanitizeFontName(String paramString)
  {
    paramString = paramString.replaceAll("[.+,*#~]", "-");
    return paramString;
  }

  protected String[] getFontConversionTypes()
  {
    return this.includedFonts;
  }

  protected String getFontFaceCSS(String paramString, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("@font-face {\n");
    localStringBuilder.append("\tfont-family: ").append(paramString).append(this.rawPageNumber).append(";\n");
    if (paramArrayOfString.length == 1)
    {
      localStringBuilder.append("\tsrc: url(\"").append(this.fileName).append("/fonts/").append(paramString).append('.').append(paramArrayOfString[0]).append("\");\n");
    }
    else
    {
      int i = 0;
      ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
      for (String str3 : paramArrayOfString)
        if (str3.equals("eot"))
          i = 1;
        else
          localArrayList.add(str3);
      paramArrayOfString = (String[])localArrayList.toArray(new String[localArrayList.size()]);
      if (i != 0)
      {
        localStringBuilder.append("\tsrc: url(\"").append(this.fileName).append("/fonts/").append(paramString).append('.').append("eot").append("\");\n");
        localStringBuilder.append("\tsrc: url(\"").append(this.fileName).append("/fonts/").append(paramString).append('.').append("eot?#iefix").append("\") format(\"embedded-opentype\"),\n");
      }
      else
      {
        localStringBuilder.append("\tsrc: ");
      }
      for (int j = 0; j < paramArrayOfString.length; j++)
      {
        String str1 = paramArrayOfString[j];
        String str2 = str1;
        if (str1.equals("otf"))
          str2 = "opentype";
        localStringBuilder.append("\t\turl(\"").append(this.fileName).append("/fonts/").append(paramString).append('.').append(str1).append("\") format(\"").append(str2).append("\")");
        if (j < paramArrayOfString.length - 1)
          localStringBuilder.append(",\n");
      }
      localStringBuilder.append(";\n");
    }
    localStringBuilder.append("}\n");
    return localStringBuilder.toString();
  }

  protected synchronized void writeOutFont(Object[] paramArrayOfObject, String paramString, String[] paramArrayOfString)
  {
    PdfFont localPdfFont = (PdfFont)paramArrayOfObject[0];
    String str1 = (String)paramArrayOfObject[2];
    String str2 = localPdfFont.getBaseFontName();
    byte[] arrayOfByte1 = (byte[])paramArrayOfObject[1];
    File localFile = new File(paramString);
    if (!localFile.exists())
      localFile.mkdirs();
    if (str1.equals("ttf"))
    {
      try
      {
        arrayOfByte1 = HTMLFontUtils.convertTTForHTML(arrayOfByte1);
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
      }
      if (arrayOfByte1 == null)
        return;
    }
    Collection localCollection = null;
    if (this.newFontCmaps.get(str2) != null)
      localCollection = ((HashMap)this.newFontCmaps.get(str2)).values();
    for (String str3 : paramArrayOfString)
      if (str3.equals("cff"))
      {
        this.customIO.writeFont(new StringBuilder().append(paramString).append(str2).append(".cff").toString(), arrayOfByte1);
      }
      else
      {
        byte[] arrayOfByte2 = (byte[])arrayOfByte1.clone();
        try
        {
          if (str3.equals("otf"))
            arrayOfByte2 = HTMLFontUtils.convertPSForHTMLOTF(localPdfFont, arrayOfByte1, str1, (HashMap)this.widths.get(str2), localCollection);
          else if (str3.equals("woff"))
            arrayOfByte2 = HTMLFontUtils.convertPSForHTMLWOFF(localPdfFont, arrayOfByte1, str1, (HashMap)this.widths.get(str2), localCollection);
          else if (str3.equals("eot"))
            arrayOfByte2 = HTMLFontUtils.convertPSForHTMLEOT(localPdfFont, arrayOfByte2, str1, (HashMap)this.widths.get(str2), localCollection);
          this.customIO.writeFont(new StringBuilder().append(paramString).append(str2).append('.').append(str3).toString(), arrayOfByte2);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
        }
      }
  }

  public synchronized void flagDecodingFinished()
  {
    if ((this.customIO != null) && (this.customIO.isOutputOpen()))
      completeOutput();
    String[] arrayOfString = getFontConversionTypes();
    Object[] arrayOfObject1 = this.fontsToConvert.keySet().toArray();
    for (Object localObject : arrayOfObject1)
    {
      Object[] arrayOfObject3 = (Object[])this.fontsToConvert.get(localObject);
      String str = new StringBuilder().append(this.rootDir).append(this.fileName).append("/fonts/").toString();
      this.hasEmbeddedFonts = true;
      writeOutFont(arrayOfObject3, str, arrayOfString);
    }
  }

  protected static String roundUp(double paramDouble)
  {
    return new StringBuilder().append("").append((int)(paramDouble + 0.99D)).toString();
  }

  public int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    String str = new StringBuilder().append(paramInt1 + 45).append(paramString).toString();
    this.usingCachedImage = false;
    int i = (paramGraphicsState.getClippingShape() == null) && (this.imagesAlreadyWritten.get(str) != null) ? 1 : 0;
    if (i == 0)
    {
      this.imagesAlreadyWritten.put(str, Integer.valueOf(this.imageId + 1));
    }
    else if (!this.embedImageAsBase64)
    {
      this.usingCachedImage = true;
      this.cachedImageId = ((Integer)this.imagesAlreadyWritten.get(str)).intValue();
      paramBufferedImage = null;
    }
    flushText();
    int j = 0;
    float f1 = paramGraphicsState.CTM[2][0] * this.scaling;
    float f2 = paramGraphicsState.CTM[2][1] * this.scaling;
    this.iw = ((paramGraphicsState.CTM[0][0] + Math.abs(paramGraphicsState.CTM[1][0])) * this.scaling);
    this.ih = ((paramGraphicsState.CTM[1][1] + Math.abs(paramGraphicsState.CTM[0][1])) * this.scaling);
    if (this.iw == 0.0F)
    {
      this.iw = (paramGraphicsState.CTM[1][0] * this.scaling);
      if (this.iw < 0.0F)
        this.iw = (-this.iw);
    }
    if (this.ih == 0.0F)
      this.ih = (paramGraphicsState.CTM[0][1] * this.scaling);
    if (this.iw < 0.0F)
      this.iw *= -1.0F;
    if (this.iw < 1.0F)
      this.iw = 1.0F;
    if (this.ih == 0.0F)
      this.ih = 1.0F;
    if (this.ih < 1.0F)
    {
      f2 += this.ih;
      this.ih = Math.abs(this.ih);
    }
    if (paramGraphicsState.CTM[0][0] < 0.0F)
      f1 -= this.iw;
    if ((paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] < 0.0F) && (paramGraphicsState.CTM[0][1] == 0.0F) && (paramGraphicsState.CTM[1][0] == 0.0F))
      j = 1;
    Graphics2D localGraphics2D = null;
    BufferedImage localBufferedImage = paramBufferedImage;
    int k = 0;
    int m = 0;
    if (paramGraphicsState.getClippingShape() != null)
    {
      localObject1 = paramGraphicsState.getClippingShape().getBounds2D();
      float f3 = (float)(((Rectangle2D)localObject1).getWidth() * this.scaling);
      float f4 = (float)(((Rectangle2D)localObject1).getHeight() * this.scaling);
      if (f3 < this.iw)
      {
        this.iw = f3;
        f1 = (float)(((Rectangle2D)localObject1).getMinX() * this.scaling);
        k = 1;
      }
      if (f4 < this.ih)
      {
        this.ih = f4;
        f2 = (float)(((Rectangle2D)localObject1).getMinY() * this.scaling);
        m = 1;
      }
    }
    if (k == 0)
    {
      if ((paramGraphicsState.CTM[1][0] < 0.0F) && (paramGraphicsState.CTM[0][0] != 0.0F))
        f1 += paramGraphicsState.CTM[1][0] * this.scaling;
      if ((paramGraphicsState.CTM[1][0] < 0.0F) && (paramGraphicsState.CTM[0][0] == 0.0F))
        f1 -= this.iw;
    }
    if (m == 0)
    {
      if ((paramGraphicsState.CTM[0][1] < 0.0F) && (paramGraphicsState.CTM[1][1] != 0.0F))
        f2 += paramGraphicsState.CTM[0][1] * this.scaling;
      if ((paramGraphicsState.CTM[0][1] < 0.0F) && (paramGraphicsState.CTM[1][1] == 0.0F))
        f2 -= this.ih;
    }
    switch (this.pageRotation)
    {
    case 180:
      this.coords = new double[] { this.cropBox.getWidth() - (this.iw + f1) / this.scaling, this.cropBox.getHeight() - (this.ih + f2) / this.scaling };
      break;
    case 270:
      if ((paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] > 0.0F) && (paramGraphicsState.CTM[1][0] == 0.0F) && (paramGraphicsState.CTM[0][1] == 0.0F))
        this.coords = new double[] { this.cropBox.getWidth() - (f1 + this.iw) / this.scaling, f2 / this.scaling };
      else
        this.coords = new double[] { f1 / this.scaling, f2 / this.scaling };
      break;
    default:
      this.coords = new double[] { f1 / this.scaling, f2 / this.scaling };
    }
    correctCoords(this.coords);
    this.coords[0] *= this.scaling;
    this.coords[1] *= this.scaling;
    this.coords[1] -= this.ih;
    Object localObject1 = new Rectangle2D.Double(this.coords[0], this.coords[1], this.iw, this.ih);
    Rectangle2D.Double localDouble = new Rectangle2D.Double(this.cropBox.getX() * this.scaling, this.cropBox.getY() * this.scaling, this.cropBox.getWidth() * this.scaling, this.cropBox.getHeight() * this.scaling);
    int i2 = (int)(this.coords[0] - (int)this.coords[0] + this.iw + 0.99D);
    int i3 = (int)(this.coords[1] - (int)this.coords[1] + this.ih + 0.99D);
    if ((localDouble.intersects((Rectangle2D)localObject1)) && (i2 > 0) && (i3 > 0))
    {
      if (!this.usingCachedImage)
        this.imageId += 1;
      Object localObject2;
      if ((!this.userControlledImageScaling) && (paramBufferedImage != null))
        if ((paramBufferedImage.getHeight() == 1) || ((j == 0) && (this.iw == paramBufferedImage.getWidth()) && (this.ih == paramBufferedImage.getHeight())))
        {
          localBufferedImage = paramBufferedImage;
        }
        else
        {
          if (localGraphics2D == null)
          {
            localBufferedImage = new BufferedImage(i2, i3, paramBufferedImage.getType());
            localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
            localObject2 = new AffineTransform();
            if (j != 0)
            {
              ((AffineTransform)localObject2).scale(1.0D, -1.0D);
              ((AffineTransform)localObject2).translate(0.0D, -this.ih);
            }
            localGraphics2D.setTransform((AffineTransform)localObject2);
          }
          localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
          localGraphics2D.drawImage(paramBufferedImage, 0, 0, i2, i3, null);
          if (this.pageRotation != 0)
            localBufferedImage = rotateImage(localBufferedImage, this.pageRotation);
        }
      if (this.embedImageAsBase64)
      {
        this.imageArray = createBase64ImageStream(localBufferedImage);
      }
      else
      {
        localObject2 = new StringBuilder().append(this.fileName).append("/img/").toString();
        if (i == 0)
        {
          File localFile = new File(new StringBuilder().append(this.rootDir).append((String)localObject2).toString());
          if (!localFile.exists())
            localFile.mkdirs();
          this.customIO.writeImage(this.rootDir, new StringBuilder().append((String)localObject2).append(this.imageId).toString(), localBufferedImage);
          if ((this.type == 4) && (!this.inlineSVG))
            this.imageName = new StringBuilder().append("img/").append(this.imageId).append(this.customIO.getImageTypeUsed()).toString();
          else
            this.imageName = new StringBuilder().append((String)localObject2).append(this.imageId).append(this.customIO.getImageTypeUsed()).toString();
        }
        else if ((this.type == 4) && (!this.inlineSVG))
        {
          this.imageName = new StringBuilder().append("img").append(this.cachedImageId).append(this.customIO.getImageTypeUsed()).toString();
        }
        else
        {
          this.imageName = new StringBuilder().append((String)localObject2).append(this.cachedImageId).append(this.customIO.getImageTypeUsed()).toString();
        }
      }
      int n;
      int i1;
      switch (this.pageRotation)
      {
      case 90:
        double d1 = this.pageData.getCropBoxHeight2D(paramInt1) * this.scaling - this.coords[1] - this.iw;
        double d2 = this.coords[0];
        i2 = (int)(d1 - (int)d1 + this.iw + 0.99D);
        i3 = (int)(d2 - (int)d2 + this.ih + 0.99D);
        n = (int)d1;
        i1 = (int)d2;
        break;
      case 270:
        i2 = (int)(this.coords[1] - (int)this.coords[1] + this.iw + 0.99D);
        i3 = (int)(this.coords[0] - (int)this.coords[0] + this.ih + 0.99D);
        n = (int)this.coords[1];
        i1 = (int)this.coords[0];
        break;
      default:
        i2 = (int)(this.coords[0] - (int)this.coords[0] + this.iw + 0.99D);
        i3 = (int)(this.coords[1] - (int)this.coords[1] + this.ih + 0.99D);
        n = (int)this.coords[0];
        i1 = (int)this.coords[1];
      }
      this.currentImage = new int[] { n, i1, i2, i3 };
      return -2;
    }
    return -1;
  }

  public static String createBase64ImageStream(BufferedImage paramBufferedImage)
  {
    String str = "";
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(paramBufferedImage, "PNG", localByteArrayOutputStream);
      localByteArrayOutputStream.close();
      BASE64Encoder localBASE64Encoder = new BASE64Encoder();
      str = new StringBuilder().append("data:image/png;base64,").append(localBASE64Encoder.encode(localByteArrayOutputStream.toByteArray())).toString();
      str = str.replaceAll("\r\n", "");
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
    return str;
  }

  protected void drawSVGImage(GraphicsState paramGraphicsState)
  {
    float f = paramGraphicsState.getAlpha(2);
    int i = 25;
    if (this.type == 4)
      i = 22;
    String str;
    if (this.embedImageAsBase64)
      str = this.imageArray;
    else
      str = this.imageName;
    writeCustom(i, new StringBuilder().append("<image x=\"").append(this.currentImage[0]).append("\" y=\"").append(this.currentImage[1]).append("\" width=\"").append(this.currentImage[2]).append("\" height=\"").append(this.currentImage[3]).append("\" opacity=\"").append(f).append("\" xlink:href=\"").append(str).append("\" />").toString());
  }

  protected BufferedImage rotateImage(BufferedImage paramBufferedImage, int paramInt)
  {
    BufferedImage localBufferedImage;
    if (paramInt == 180)
    {
      AffineTransform localAffineTransform = AffineTransform.getScaleInstance(-1.0D, -1.0D);
      localAffineTransform.translate(-paramBufferedImage.getWidth(null), -paramBufferedImage.getHeight(null));
      AffineTransformOp localAffineTransformOp = new AffineTransformOp(localAffineTransform, 1);
      localBufferedImage = localAffineTransformOp.filter(paramBufferedImage, null);
    }
    else
    {
      int i = paramBufferedImage.getWidth();
      int j = paramBufferedImage.getHeight();
      localBufferedImage = new BufferedImage(j, i, paramBufferedImage.getType());
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.rotate(Math.toRadians(this.pageRotation), i / 2, j / 2);
      int k = (i - j) / 2;
      if (paramInt == 90)
        localGraphics2D.drawImage(paramBufferedImage, k, k, null);
      else if (paramInt == 270)
        localGraphics2D.drawImage(paramBufferedImage, -k, -k, null);
      float f = this.iw;
      this.iw = this.ih;
      this.ih = f;
    }
    return localBufferedImage;
  }

  protected String setPrecision(double paramDouble)
  {
    String str = String.valueOf(paramDouble);
    int i = str.indexOf(46);
    int j = str.length();
    int k = j - i - 1;
    if ((i > 0) && (k > this.dpCount))
      if (this.dpCount == 0)
        str = str.substring(0, i + this.dpCount);
      else
        str = str.substring(0, i + this.dpCount + 1);
    return removeEmptyDecimals(str);
  }

  protected static String setPrecision(double paramDouble, int paramInt)
  {
    if (paramInt > 3)
      throw new RuntimeException("dp count must be less than 4");
    double d = (int)(paramDouble * indices[paramInt]) / indices[paramInt];
    if ((d > 0.98D) && (d < 1.01D))
      return "1";
    if (d == 0.0D)
      return "0";
    if (d == 0.0D)
      return "0";
    if ((d < -0.98D) && (d > -1.01D))
      return "-1";
    return String.valueOf((int)(paramDouble * indices[paramInt]) / indices[paramInt]);
  }

  public void drawClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean)
  {
  }

  protected void drawSVGClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean)
  {
    if (this.includeClip)
    {
      this.clip = null;
      Object localObject = paramGraphicsState.getClippingShape();
      if ((localObject == null) && (paramShape != null))
        localObject = paramShape;
      if (localObject != null)
      {
        this.newClip = true;
        String str = new StringBuilder().append("c").append(this.clipCount).append('_').append(this.outputPageNumber).toString();
        ShapeFactory localShapeFactory = generateSVGShape(110, this.scaling, (Shape)localObject, paramGraphicsState, new AffineTransform(), this.midPoint, this.cropBox, this.currentColor, this.pageRotation, this.pageData, this.rawPageNumber, this.includeClip, str);
        if (!localShapeFactory.isEmpty())
          this.clip = ((String)localShapeFactory.getContent());
      }
    }
  }

  public void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat)
  {
    float f1 = paramInt1;
    if (!isVisible(paramArrayOfFloat))
      return;
    if ((enableNewCmapCode) && (!paramPdfFont.isFontSubstituted()) && (paramPdfGlyph != null) && (paramString != null) && (paramPdfFont.getFontType() != 1228944679) && (this.isRealText) && ((this.fontMode == 6) || ((this.fontMode == 7) && (!StandardFonts.isStandardFont(paramPdfFont.getFontName(), true)) && (!paramPdfFont.getFontName().contains("Arial")))))
      paramString = remapGlyph(paramPdfGlyph.getGlyphNumber(), paramString, paramPdfFont.getBaseFontName());
    float[][] arrayOfFloat = new float[3][3];
    arrayOfFloat[0][0] = paramArrayOfFloat[0][0];
    arrayOfFloat[0][1] = paramArrayOfFloat[0][1];
    arrayOfFloat[0][2] = paramArrayOfFloat[0][2];
    arrayOfFloat[1][0] = paramArrayOfFloat[1][0];
    arrayOfFloat[1][1] = paramArrayOfFloat[1][1];
    arrayOfFloat[1][2] = paramArrayOfFloat[1][2];
    arrayOfFloat[2][0] = paramArrayOfFloat[2][0];
    arrayOfFloat[2][1] = paramArrayOfFloat[2][1];
    arrayOfFloat[2][2] = paramArrayOfFloat[2][2];
    if ((this.currentTokenNumber == this.lastTokenNumber) && (paramString.equals(" ")) && (this.lastGlyf.equals(" ")))
    {
      flushText();
      return;
    }
    if ((Helper != null) && (paramString.length() > 3) && (!StandardFonts.isValidGlyphName(paramString)))
      paramString = Helper.mapNonstandardGlyfName(paramString, paramPdfFont);
    Area localArea = paramGraphicsState.getClippingShape();
    if ((localArea != null) && (!localArea.getBounds().contains(new Point((int)paramArrayOfFloat[2][0] + 1, (int)paramArrayOfFloat[2][1] + 1))) && (!localArea.getBounds().contains(new Point((int)(paramArrayOfFloat[2][0] + f1 / 2.0F), (int)(paramArrayOfFloat[2][1] + f1 / 2.0F)))))
      return;
    double[] arrayOfDouble = { paramArrayOfFloat[2][0], paramArrayOfFloat[2][1] };
    correctCoords(arrayOfDouble);
    if (!this.isRealText)
    {
      if (this.isSVGMode)
      {
        TextPosition localTextPosition = this.currentTextPosition;
        this.currentTextPosition = new TextPosition(arrayOfDouble, new float[] { arrayOfFloat[0][0], arrayOfFloat[0][1], arrayOfFloat[1][0], arrayOfFloat[1][1], arrayOfFloat[2][0], arrayOfFloat[2][1] });
        rasterizeTextAsShape(paramPdfGlyph, paramGraphicsState, paramPdfFont, paramString);
        this.currentTextPosition = localTextPosition;
      }
      if (!this.isTextSelectable)
        return;
    }
    switch (this.pageRotation)
    {
    case 90:
      float f2 = paramArrayOfFloat[2][0];
      f3 = paramArrayOfFloat[2][1];
      paramArrayOfFloat = Matrix.multiply(paramArrayOfFloat, new float[][] { { 0.0F, -1.0F, 0.0F }, { 1.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      if ((paramArrayOfFloat[0][0] == 0.0F) && (paramArrayOfFloat[1][1] == 0.0F) && (paramArrayOfFloat[0][1] * paramArrayOfFloat[1][0] < 0.0F) && (paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] > 0.0F))
      {
        paramArrayOfFloat[2][0] = f3;
        paramArrayOfFloat[2][1] = ((float)(this.cropBox.getHeight() - f2));
      }
      else
      {
        paramArrayOfFloat[2][0] = f2;
        paramArrayOfFloat[2][1] = f3;
      }
      break;
    case 270:
      if ((paramArrayOfFloat[0][0] <= 0.0F) || (paramArrayOfFloat[1][1] <= 0.0F) || (paramArrayOfFloat[0][1] != 0.0F) || (paramArrayOfFloat[1][0] != 0.0F))
        paramArrayOfFloat = Matrix.multiply(paramArrayOfFloat, new float[][] { { 0.0F, 1.0F, 0.0F }, { -1.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      break;
    }
    if (paramFloat == -100.0F)
      paramFloat = paramPdfFont.getWidth(-1);
    TextState localTextState = paramGraphicsState.getTextState();
    if (localTextState.getTfs() == 0.0F)
      localTextState.setTfs(1.0F);
    if ((paramArrayOfFloat[0][0] != 0.0F) && (paramArrayOfFloat[0][1] == 0.0F) && (localTextState.getTfs() == 1.0F))
      f1 = paramArrayOfFloat[0][0];
    if ((paramString.isEmpty()) || (TextBlock.ignoreGlyf(paramString)))
      return;
    if (f1 < 0.0F)
      f1 = -f1;
    float f3 = f1 * paramFloat;
    if (!paramPdfFont.getBaseFontName().equals(this.lastFontUsed))
    {
      this.fontMapper = getFontMapper(paramPdfFont);
      this.lastFontUsed = paramPdfFont.getBaseFontName();
      String str = (String)this.embeddedFontsByFontID.get(this.lastFontUsed);
      if (str == null)
        this.embeddedFontsByFontID.put(this.lastFontUsed, "browser");
      else if (!str.contains("browser"))
        this.embeddedFontsByFontID.put(this.lastFontUsed, new StringBuilder().append(str).append(',').append("browser").toString());
    }
    int i = paramGraphicsState.getTextRenderType();
    int j = i == 1 ? paramGraphicsState.getStrokeColor().getRGB() : paramGraphicsState.getNonstrokeColor().getRGB();
    if ((this.lastTrm != null) && (paramArrayOfFloat[0][0] == this.lastTrm[0][0]) && (paramArrayOfFloat[0][1] == this.lastTrm[0][1]) && (paramArrayOfFloat[1][0] == this.lastTrm[1][0]) && (paramArrayOfFloat[1][1] == this.lastTrm[1][1]) && (paramString.equals(this.lastGlyf)))
    {
      f4 = Math.abs(paramArrayOfFloat[2][0] - this.lastTrm[2][0]);
      f5 = Math.abs(paramArrayOfFloat[2][1] - this.lastTrm[2][1]);
      f6 = 1.0F;
      if ((f4 < f6) && (f5 < f6))
        return;
    }
    float f4 = (float)arrayOfDouble[0];
    float f5 = (float)arrayOfDouble[1];
    float f6 = f4;
    float f7 = f5;
    if (this.pageRotation == 90)
    {
      f6 = paramArrayOfFloat[2][1];
      f7 = paramArrayOfFloat[2][0];
    }
    float f8 = f1 * localTextState.getLastKerningAdded();
    float f9 = f1 * localTextState.getCharacterSpacing() / localTextState.getTfs();
    float f10 = f1 * localTextState.getWordSpacing();
    if ((!this.writeEveryGlyf) && (this.currentTextBlock.isSameFont(f1, this.fontMapper, paramArrayOfFloat, j)) && (f1 == this.lastFontSize))
    {
      if (this.currentTextBlock.appendText(paramString, f3, f6, f7, this.groupGlyphsInTJ, (!this.groupGlyphsInTJ) || (this.currentTokenNumber != this.lastTokenNumber), f4, f5, f8, f10, f9));
    }
    else
    {
      flushText();
      this.currentTextPosition = new TextPosition(arrayOfDouble, new float[] { arrayOfFloat[0][0], arrayOfFloat[0][1], arrayOfFloat[1][0], arrayOfFloat[1][1], arrayOfFloat[2][0], arrayOfFloat[2][1] });
      if (paramString.charAt(0) > ' ')
      {
        float f11 = f1 * paramPdfFont.getCurrentFontSpaceWidth();
        this.lastFontSize = f1;
        if (this.type == 5)
          this.currentTextBlock = new SVGTextBlock(paramString, f1, this.fontMapper, paramArrayOfFloat, f4, f5, f3, j, f11, this.cropBox.getBounds(), paramArrayOfFloat, f6, f7, this.pageRotation, f9, f10);
        else
          this.currentTextBlock = new TextBlock(paramString, f1, this.fontMapper, paramArrayOfFloat, f4, f5, f3, j, f11, this.cropBox.getBounds(), paramArrayOfFloat, f6, f7, this.pageRotation, f9, f10);
        if (this.convertSpacesTonbsp)
          this.currentTextBlock.convertSpacesTonbsp(true);
        if (this.currentTextBlock.getRotationAngle() == 0.0F)
          this.currentTextBlock.adjustY(-f1);
      }
      else if (this.type == 5)
      {
        this.currentTextBlock = new SVGTextBlock();
      }
      else
      {
        this.currentTextBlock = new TextBlock();
      }
    }
    this.lastTokenNumber = this.currentTokenNumber;
    this.lastGlyf = paramString;
    this.lastTrm = paramArrayOfFloat;
  }

  private String remapGlyph(int paramInt, String paramString1, String paramString2)
  {
    if (paramInt != -1)
    {
      HashMap localHashMap = (HashMap)this.newFontCmaps.get(paramString2);
      HashSet localHashSet = (HashSet)this.newFontMappedValues.get(paramString2);
      if (localHashMap == null)
      {
        localHashMap = new HashMap();
        this.newFontCmaps.put(paramString2, localHashMap);
        localHashSet = new HashSet();
        this.newFontMappedValues.put(paramString2, localHashSet);
      }
      String str1 = new StringBuilder().append(paramString1).append('-').append(paramInt).toString();
      GlyphMapping localGlyphMapping1 = (GlyphMapping)localHashMap.get(str1);
      if (localGlyphMapping1 != null)
      {
        localGlyphMapping1.use();
        return localGlyphMapping1.getOutputChar();
      }
      if (paramString1.codePointCount(0, paramString1.length()) > 1)
        paramString1 = paramString1.substring(0, 1);
      if ((!localHashSet.contains(paramString1)) && (paramString1.length() > 0))
      {
        localGlyphMapping1 = new GlyphMapping(paramString1, paramInt, paramString1, true);
        localHashMap.put(str1, localGlyphMapping1);
        localHashSet.add(paramString1);
        return paramString1;
      }
      String str2 = null;
      if (!paramString1.toUpperCase().equals(paramString1))
        str2 = paramString1.toUpperCase();
      else if (!paramString1.toLowerCase().equals(paramString1))
        str2 = paramString1.toLowerCase();
      if (str2 != null)
      {
        i = 0;
        if (!localHashSet.contains(str2))
        {
          i = 1;
        }
        else
        {
          Object localObject = null;
          Iterator localIterator = localHashMap.values().iterator();
          while (localIterator.hasNext())
          {
            GlyphMapping localGlyphMapping2 = (GlyphMapping)localIterator.next();
            if (localGlyphMapping2.getOutputChar().equals(str2))
              localObject = localGlyphMapping2;
          }
          if (localObject.getGlyphNumber() == paramInt)
            i = 1;
        }
        if (i != 0)
        {
          localGlyphMapping1 = new GlyphMapping(paramString1, paramInt, str2, false);
          localHashMap.put(str1, localGlyphMapping1);
          localHashSet.add(str2);
          return str2;
        }
      }
      int i = 160;
      do
      {
        i++;
        str2 = new String(Character.toChars(i));
      }
      while (localHashSet.contains(str2));
      localGlyphMapping1 = new GlyphMapping(paramString1, paramInt, str2, false);
      localHashMap.put(str1, localGlyphMapping1);
      localHashSet.add(str2);
      return str2;
    }
    return paramString1;
  }

  private boolean isVisible(float[][] paramArrayOfFloat)
  {
    boolean bool = true;
    if (this.objectAreas != null)
    {
      int i = (int)paramArrayOfFloat[2][0] + 1;
      int j = (int)paramArrayOfFloat[2][1] + 1;
      int k = (int)(paramArrayOfFloat[2][0] + paramArrayOfFloat[0][0] - 1.0F);
      int m = (int)(paramArrayOfFloat[2][1] + paramArrayOfFloat[1][1] - 1.0F);
      int n = this.objectAreas.length;
      for (int i1 = this.objectAreaCount + 1; i1 < n; i1++)
      {
        Rectangle localRectangle = this.objectAreas[i1];
        if ((localRectangle != null) && (localRectangle.getBounds().height >= paramArrayOfFloat[1][1]) && (localRectangle.contains(i, j)))
        {
          bool = false;
          i1 = n;
        }
      }
    }
    return bool;
  }

  private void rasterizeTextAsShape(PdfGlyph paramPdfGlyph, GraphicsState paramGraphicsState, PdfFont paramPdfFont, String paramString)
  {
    if ((paramPdfGlyph != null) && (paramPdfGlyph.getShape() != null) && (!paramString.equals(" ")))
    {
      GraphicsState localGraphicsState = paramGraphicsState;
      String str2 = (String)paramPdfFont.getFontID();
      str2 = StringUtils.makeMethodSafe(str2);
      if ((this.usedFontIDs.containsKey(str2)) && (this.usedFontIDs.get(str2) != paramPdfFont.getBaseFontName()))
        str2 = new StringBuilder().append(str2).append(StringUtils.makeMethodSafe(paramPdfFont.getBaseFontName())).toString();
      else
        this.usedFontIDs.put(str2, paramPdfFont.getBaseFontName());
      String str1 = new StringBuilder().append(str2).append('_').append(paramPdfGlyph.getGlyphNumber()).toString();
      if (!Character.isLetter(str1.charAt(0)))
        str1 = new StringBuilder().append('s').append(str1).toString();
      String str3 = new StringBuilder().append(paramPdfFont.getBaseFontName()).append('.').append(str1).toString();
      boolean bool = this.glyfsRasterized.containsKey(str3);
      Area localArea = (Area)paramPdfGlyph.getShape().clone();
      localGraphicsState.setClippingShape(null);
      localGraphicsState.setFillType(paramGraphicsState.getTextRenderType());
      float f = (float)(1.0D / paramPdfFont.FontMatrix[0]);
      if (localArea.getBounds().height > 2000)
        f *= 100.0F;
      writePosition(str1, true, f);
      completeTextShape(paramGraphicsState, str1);
      if (!bool)
      {
        drawNonPatternedShape(localArea, localGraphicsState, 21610, str1, null, null);
        this.glyfsRasterized.put(str3, "x");
      }
    }
  }

  protected void completeTextShape(GraphicsState paramGraphicsState, String paramString)
  {
    throw new RuntimeException("method root completeTextShape(GraphicsState gs, String JSRoutineName) should not be called");
  }

  protected void completeSVGTextShape(GraphicsState paramGraphicsState, String paramString)
  {
    int i = 25;
    if (this.type == 4)
      i = 22;
    int j = paramGraphicsState.getFillType();
    Object localObject;
    if ((j == 2) || (j == 3))
    {
      localObject = paramGraphicsState.getNonstrokeColor();
      writeCustom(i, new StringBuilder().append("fill=\"").append(convertRGBtoHex(((PdfPaint)localObject).getRGB())).append("\" ").toString());
    }
    else
    {
      writeCustom(i, "fill=\"none\" ");
    }
    if (j == 1)
    {
      localObject = (BasicStroke)paramGraphicsState.getStroke();
      double d = ((BasicStroke)localObject).getLineWidth() * this.scaling;
      if ((d < 0.1D) && (paramGraphicsState.getStrokeColor().getRGB() == -16777216))
        writeCustom(i, "stroke-width=\"1\" ");
      else
        writeCustom(i, new StringBuilder().append("stroke-width=\"").append(d).append("\" ").toString());
      if (((BasicStroke)localObject).getMiterLimit() != 10.0F)
        writeCustom(i, new StringBuilder().append("stroke-miterlimit=\"").append(((BasicStroke)localObject).getMiterLimit()).append("\" ").toString());
      PdfPaint localPdfPaint = paramGraphicsState.getStrokeColor();
      writeCustom(i, new StringBuilder().append("stroke=\"").append(convertRGBtoHex(localPdfPaint.getRGB())).append('"').toString());
      float f = paramGraphicsState.getAlpha(1);
      if (f != 1.0F)
        writeCustom(i, new StringBuilder().append("stroke-opacity=\"").append(f).append('"').toString());
    }
    else
    {
      writeCustom(i, "stroke=\"none\" ");
    }
    writeCustom(i, " />");
  }

  protected FontMapper getFontMapper(PdfFont paramPdfFont)
  {
    return null;
  }

  public void drawShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt)
  {
    this.objectAreaCount += 1;
    if (((!this.isSVGMode) && (this.type != 6)) || (!isObjectVisible(paramShape.getBounds(), paramGraphicsState.getClippingShape())))
      return;
    flushText();
    if ((paramGraphicsState.getNonstrokeColor().isPattern()) || (paramGraphicsState.nonstrokeColorSpace.getID() == 1146450818))
    {
      drawPatternedShape(paramShape, paramGraphicsState);
    }
    else
    {
      if (paramGraphicsState.getFillType() == 2)
      {
        double d1 = paramShape.getBounds2D().getX();
        double d2 = paramShape.getBounds2D().getY();
        double d3 = paramShape.getBounds2D().getWidth();
        double d4 = paramShape.getBounds2D().getHeight();
        float f = paramGraphicsState.getCTMAdjustedLineWidth();
        if ((d4 <= 1.0D) && (f <= 1.0F))
        {
          paramGraphicsState.setFillType(1);
          paramGraphicsState.setStrokeColor(paramGraphicsState.getNonstrokeColor());
          paramGraphicsState.setCTMAdjustedLineWidth(0.1F);
          paramShape = new Line2D.Double(d1, d2, d1 + d3, d2);
        }
        if ((d3 <= 1.0D) && (f <= 1.0F))
        {
          paramGraphicsState.setFillType(1);
          paramGraphicsState.setStrokeColor(paramGraphicsState.getNonstrokeColor());
          paramGraphicsState.setCTMAdjustedLineWidth(0.1F);
          paramShape = new Line2D.Double(d1, d2, d1, d2 + d4);
        }
      }
      drawNonPatternedShape(paramShape, paramGraphicsState, paramInt, null, this.cropBox, this.midPoint);
    }
  }

  protected void drawNonPatternedShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt, String paramString, Rectangle2D paramRectangle2D, Point2D paramPoint2D)
  {
  }

  protected void drawSVGNonPatternedShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt, String paramString, Rectangle2D paramRectangle2D, Point2D paramPoint2D)
  {
    String str1 = new StringBuilder().append("").append(this.clipCount).toString();
    if (!this.newClip)
      str1 = new StringBuilder().append("").append(this.clipCount - 1).toString();
    String str2 = new StringBuilder().append("c").append(str1).append('_').append(this.outputPageNumber).toString();
    ShapeFactory localShapeFactory = generateSVGShape(paramInt, this.scaling, paramShape, paramGraphicsState, new AffineTransform(), paramPoint2D, paramRectangle2D, this.currentColor, this.pageRotation, this.pageData, this.rawPageNumber, this.includeClip, str2);
    if (!localShapeFactory.isEmpty())
    {
      int i = 25;
      if (this.type == 4)
        i = 22;
      if (paramInt == 21610)
      {
        writeCustom(14, localShapeFactory.getContent());
        writeCustom(14, new StringBuilder().append("id=\"").append(paramString).append('"').toString());
        writeCustom(14, " />\n");
      }
      else if ((this.includeClip) && (this.clip != null) && (this.newClip))
      {
        this.newClip = false;
        this.clipCount += 1;
        writeCustom(23, this.clip);
        writeCustom(i, localShapeFactory.getContent());
      }
      else
      {
        writeCustom(i, localShapeFactory.getContent());
        this.currentColor = localShapeFactory.getShapeColor();
      }
    }
  }

  public ShapeFactory generateSVGShape(int paramInt1, float paramFloat, Shape paramShape, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, Point2D paramPoint2D, Rectangle2D paramRectangle2D, int paramInt2, int paramInt3, PdfPageData paramPdfPageData, int paramInt4, boolean paramBoolean, String paramString)
  {
    throw new RuntimeException("base method generateSVGShape should not be called");
  }

  protected void drawPatternedShape(Shape paramShape, GraphicsState paramGraphicsState)
  {
    double d1 = paramShape.getBounds2D().getWidth();
    double d2 = paramShape.getBounds2D().getHeight();
    double d3 = paramShape.getBounds2D().getX();
    double d4 = paramShape.getBounds2D().getY();
    this.coords = new double[] { d3, d4 };
    correctCoords(this.coords);
    int i = (int)(this.coords[0] * this.scaling);
    int j = (int)((this.coords[0] + d1 - (int)this.coords[0]) * this.scaling + 1.0D);
    int k = (int)((this.coords[1] - d2) * this.scaling);
    int m = (int)((this.coords[1] + d2 - (int)this.coords[1]) * this.scaling + 1.0D);
    if ((j < 1) || (m < 1))
      return;
    BufferedImage localBufferedImage = new BufferedImage(j, m, 2);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    AffineTransform localAffineTransform = new AffineTransform();
    localAffineTransform.scale(this.scaling, this.scaling);
    localAffineTransform.scale(1.0D, -1.0D);
    localAffineTransform.translate(0.0D, -d2);
    localAffineTransform.translate(-d3, -d4);
    localGraphics2D.setTransform(localAffineTransform);
    int n = paramGraphicsState.getFillType();
    int i1 = this.pageData.getCropBoxHeight(this.rawPageNumber);
    int i2 = 0;
    if ((n == 2) || (n == 3))
    {
      localObject = paramGraphicsState.getNonstrokeColor();
      ((PdfPaint)localObject).setScaling(0.0D, i1, this.scaling, 0.0F, i2);
      ((PdfPaint)localObject).setRenderingType(4);
      localGraphics2D.setPaint((Paint)localObject);
      localGraphics2D.fill(paramShape);
      localGraphics2D.draw(paramShape);
    }
    if ((n == 1) || (n == 3))
    {
      localObject = paramGraphicsState.getStrokeColor();
      ((PdfPaint)localObject).setScaling(0.0D, i1, this.scaling, 0.0F, i2);
      ((PdfPaint)localObject).setRenderingType(4);
      localGraphics2D.setPaint((Paint)localObject);
      localGraphics2D.setStroke(paramGraphicsState.getStroke());
      localGraphics2D.draw(paramShape);
    }
    Object localObject = new Rectangle(i, k, j, m);
    Rectangle2D.Double localDouble = new Rectangle2D.Double(this.cropBox.getX() * this.scaling, this.cropBox.getY() * this.scaling, this.cropBox.getWidth() * this.scaling, this.cropBox.getHeight() * this.scaling);
    if (localDouble.intersects((Rectangle2D)localObject))
    {
      this.shadeId += 1;
      if ((this.pageRotation == 90) || (this.pageRotation == 270))
      {
        k = (int)(localDouble.getHeight() - k - m);
        int i3 = j;
        j = m;
        m = i3;
        i3 = i;
        i = k;
        k = i3;
        localBufferedImage = rotateImage(localBufferedImage, this.pageRotation);
      }
      if (this.embedImageAsBase64)
      {
        this.imageArray = createBase64ImageStream(localBufferedImage);
      }
      else
      {
        String str = new StringBuilder().append(this.fileName).append("/shade/").toString();
        File localFile = new File(new StringBuilder().append(this.rootDir).append(str).toString());
        if (!localFile.exists())
          localFile.mkdirs();
        this.customIO.writeImage(this.rootDir, new StringBuilder().append(str).append(this.shadeId).toString(), localBufferedImage);
        if ((this.type == 4) && (!this.inlineSVG))
          this.currentPatternedShapeName = new StringBuilder().append("shade/").append(this.shadeId).append(this.customIO.getImageTypeUsed()).toString();
        else
          this.currentPatternedShapeName = new StringBuilder().append(str).append(this.shadeId).append(this.customIO.getImageTypeUsed()).toString();
      }
      this.currentPatternedShape = new int[] { i, k, j, m };
    }
    else
    {
      this.currentPatternedShape = new int[] { -1, -1, -1, -1 };
    }
  }

  protected void drawSVGPatternedShape()
  {
    int i = 25;
    if (this.type == 4)
      i = 22;
    String str;
    if (this.embedImageAsBase64)
      str = this.imageArray;
    else
      str = this.currentPatternedShapeName;
    writeCustom(i, new StringBuilder().append("<image x=\"").append(this.currentPatternedShape[0]).append("\" y=\"").append(this.currentPatternedShape[1]).append("\" width=\"").append(this.currentPatternedShape[2]).append("\" height=\"").append(this.currentPatternedShape[3]).append("\" xlink:href=\"").append(str).append("\" />").toString());
  }

  private boolean isObjectVisible(Rectangle paramRectangle, Area paramArea)
  {
    if ((this.dx == 0) && (this.dy == 0) && (this.dr == 0))
    {
      Rectangle localRectangle;
      if (paramArea != null)
        localRectangle = paramArea.getBounds();
      else
        localRectangle = null;
      int i = paramRectangle.x;
      int j = paramRectangle.y;
      int k = paramRectangle.width + i;
      int m = paramRectangle.height + j;
      if (this.cropBox != null)
      {
        double d1 = this.cropBox.getBounds2D().getX();
        double d2 = this.cropBox.getBounds2D().getY();
        double d3 = this.cropBox.getBounds2D().getWidth() + d1;
        double d4 = this.cropBox.getBounds2D().getHeight() + d2;
        if ((k < d1) || (i > d3) || (m < d2) || (j > d4))
          return false;
      }
      if (localRectangle != null)
      {
        int n = localRectangle.x;
        int i1 = localRectangle.y;
        int i2 = localRectangle.width + n;
        int i3 = localRectangle.height + i1;
        if ((k < n) || (i > i2) || (m < i1) || (j > i3))
          return false;
      }
    }
    return true;
  }

  public final void drawXForm(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
    flushText();
    renderXForm(paramDynamicVectorRenderer, paramGraphicsState.getAlpha(1));
  }

  protected void completeOutput()
  {
  }

  protected String coordsToStringParam(double[] paramArrayOfDouble, int paramInt)
  {
    String str = "";
    for (int i = 0; i < paramInt; i++)
    {
      if (i != 0)
        str = new StringBuilder().append(str).append(",").toString();
      str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[i])).toString();
    }
    return str;
  }

  protected void correctCoords(double[] paramArrayOfDouble)
  {
    paramArrayOfDouble[0] -= this.midPoint.getX();
    paramArrayOfDouble[0] += this.cropBox.getWidth() / 2.0D;
    paramArrayOfDouble[1] -= this.midPoint.getY();
    paramArrayOfDouble[1] = (0.0D - paramArrayOfDouble[1]);
    paramArrayOfDouble[1] += this.cropBox.getHeight() / 2.0D;
  }

  protected static String rgbToColor(int paramInt)
  {
    int i = paramInt >> 16 & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    int k = paramInt & 0xFF;
    return new StringBuilder().append("rgb(").append(i).append(',').append(j).append(',').append(k).append(')').toString();
  }

  protected static int[] rgbToIntArray(int paramInt)
  {
    int i = paramInt >> 16 & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    int k = paramInt & 0xFF;
    return new int[] { i, j, k, 255 };
  }

  public static String hexColor(int paramInt)
  {
    String str = Integer.toHexString(paramInt);
    str = str.substring(2, 8);
    str = new StringBuilder().append('#').append(str).toString();
    return str;
  }

  protected void drawTextArea()
  {
  }

  protected void drawPageBorder()
  {
  }

  public void flagCommand(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 16980:
      break;
    case 21610:
      this.currentTokenNumber = paramInt2;
    }
  }

  public boolean isScalingControlledByUser()
  {
    return this.userControlledImageScaling;
  }

  protected static String setJavaFxWeight(String paramString)
  {
    String str = "";
    if (paramString.equals("normal"))
      str = "NORMAL";
    else if (paramString.equals("bold"))
      str = "BOLD";
    else if (paramString.equals("bolder"))
      str = "BLACK";
    else if (paramString.equals("lighter"))
      str = "EXTRA_LIGHT";
    else if (paramString.equals("100"))
      str = "THIN";
    else if (paramString.equals("900"))
      str = "BLACK";
    return str;
  }

  protected void flushText()
  {
    if ((this.currentTextBlock == null) || (this.currentTextBlock.isEmpty()))
      return;
    writeoutTextAsDiv(getFontScaling());
    if (!this.currentTextBlock.isEmpty())
      this.previousTextBlock = this.currentTextBlock;
    if (this.type == 5)
      this.currentTextBlock = new SVGTextBlock();
    else
      this.currentTextBlock = new TextBlock();
  }

  private float getFontScaling()
  {
    float[] arrayOfFloat = this.currentTextPosition.getRawAffine();
    if (arrayOfFloat[2] == 0.0F)
      f = arrayOfFloat[3];
    else if (arrayOfFloat[3] == 0.0F)
      f = arrayOfFloat[2];
    else
      f = (float)Math.sqrt(arrayOfFloat[2] * arrayOfFloat[2] + arrayOfFloat[3] * arrayOfFloat[3]);
    float f = Math.abs(f);
    return f;
  }

  private float getFontScalingMarksNewVersion()
  {
    float[] arrayOfFloat = this.currentTextPosition.getRawAffine();
    if (arrayOfFloat[0] == 0.0F)
    {
      System.out.println("1");
      f = arrayOfFloat[1];
    }
    else if (arrayOfFloat[1] == 0.0F)
    {
      System.out.println("2");
      f = arrayOfFloat[0];
    }
    else
    {
      System.out.println("3");
      f = (float)Math.sqrt(arrayOfFloat[2] * arrayOfFloat[2] + arrayOfFloat[3] * arrayOfFloat[3]);
    }
    float f = Math.abs(f);
    return f;
  }

  protected void writeoutTextAsDiv(float paramFloat)
  {
  }

  protected void writePosition(String paramString, boolean paramBoolean, float paramFloat)
  {
    float[] arrayOfFloat = new float[4];
    double[] arrayOfDouble = this.currentTextPosition.getCoords();
    float[][] arrayOfFloat1;
    double d1;
    double d2;
    float[][] arrayOfFloat2;
    switch (this.pageRotation)
    {
    default:
      arrayOfFloat1 = this.currentTextPosition.getTrm();
      d1 = arrayOfDouble[0] * this.scaling;
      d2 = arrayOfDouble[1] * this.scaling;
      arrayOfFloat[0] = arrayOfFloat1[0][0];
      arrayOfFloat[1] = arrayOfFloat1[0][1];
      arrayOfFloat[2] = arrayOfFloat1[1][0];
      arrayOfFloat[3] = arrayOfFloat1[1][1];
      if (paramBoolean)
      {
        if (arrayOfFloat[3] != 0.0F)
          arrayOfFloat[3] = (-arrayOfFloat[3]);
      }
      else
      {
        if ((this.type != 5) && (this.type != 6))
        {
          d1 -= arrayOfFloat[1] * this.scaling;
          d2 -= arrayOfFloat[3] * this.scaling;
        }
        if (arrayOfFloat[2] != 0.0F)
          arrayOfFloat[2] = (-arrayOfFloat[2]);
      }
      if (arrayOfFloat[1] != 1.0F)
        arrayOfFloat[1] = (-arrayOfFloat[1]);
      break;
    case 90:
      d2 = arrayOfDouble[0] * this.scaling;
      d1 = (this.cropBox.getHeight() - arrayOfDouble[1]) * this.scaling;
      if (paramBoolean)
        arrayOfFloat2 = Matrix.multiply(this.currentTextPosition.getTrm(), new float[][] { { -1.0F, 0.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      else
        arrayOfFloat2 = Matrix.multiply(new float[][] { { 0.0F, 1.0F, 0.0F }, { -1.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } }, this.currentTextPosition.getTrm());
      arrayOfFloat[0] = arrayOfFloat2[0][0];
      arrayOfFloat[1] = arrayOfFloat2[0][1];
      arrayOfFloat[2] = arrayOfFloat2[1][0];
      arrayOfFloat[3] = arrayOfFloat2[1][1];
      if (!paramBoolean)
      {
        if ((arrayOfFloat[0] < 0.0F) && (arrayOfFloat[3] < 0.0F))
        {
          arrayOfFloat[0] = (-arrayOfFloat[0]);
          arrayOfFloat[3] = (-arrayOfFloat[3]);
        }
        if (this.type != 5)
        {
          d1 += arrayOfFloat[1] * this.scaling;
          d2 -= arrayOfFloat[3] * this.scaling;
        }
        if (arrayOfFloat[0] != 0.0F)
        {
          float f = arrayOfFloat[1];
          arrayOfFloat[1] = arrayOfFloat[2];
          arrayOfFloat[2] = (-f);
        }
      }
      break;
    case 180:
      d1 = (this.cropBox.getWidth() - arrayOfDouble[0]) * this.scaling;
      d2 = (this.cropBox.getHeight() - arrayOfDouble[1]) * this.scaling;
      arrayOfFloat1 = Matrix.multiply(this.currentTextPosition.getTrm(), new float[][] { { 1.0F, 0.0F, 0.0F }, { 0.0F, -1.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      arrayOfFloat[0] = arrayOfFloat1[0][0];
      arrayOfFloat[1] = arrayOfFloat1[0][1];
      arrayOfFloat[2] = arrayOfFloat1[1][0];
      arrayOfFloat[3] = arrayOfFloat1[1][1];
      if (!paramBoolean)
      {
        arrayOfFloat[0] = (-arrayOfFloat[0]);
        if (this.type != 5)
          d2 -= arrayOfFloat[3] * this.scaling;
      }
      break;
    case 270:
      d2 = (this.cropBox.getWidth() - arrayOfDouble[0]) * this.scaling;
      d1 = arrayOfDouble[1] * this.scaling;
      if (paramBoolean)
        arrayOfFloat2 = Matrix.multiply(this.currentTextPosition.getTrm(), new float[][] { { -1.0F, 0.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      else
        arrayOfFloat2 = Matrix.multiply(this.currentTextPosition.getTrm(), new float[][] { { 0.0F, -1.0F, 0.0F }, { 1.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } });
      arrayOfFloat[0] = arrayOfFloat2[0][0];
      arrayOfFloat[1] = arrayOfFloat2[0][1];
      arrayOfFloat[2] = arrayOfFloat2[1][0];
      arrayOfFloat[3] = arrayOfFloat2[1][1];
      if (!paramBoolean)
      {
        if ((arrayOfFloat[0] < 0.0F) && (arrayOfFloat[3] < 0.0F))
        {
          arrayOfFloat[0] = (-arrayOfFloat[0]);
          arrayOfFloat[3] = (-arrayOfFloat[3]);
        }
        if (this.type != 5)
          d1 += arrayOfFloat[1] * this.scaling;
      }
      break;
    }
    for (int i = 0; i < 4; i++)
      if (arrayOfFloat[i] == -0.0D)
        arrayOfFloat[i] = 0.0F;
    if (paramBoolean)
      writeRasterizedTextPosition(paramString, arrayOfFloat, (int)d1, (int)d2, paramFloat);
    else
      writeTextPosition(arrayOfFloat, d1, d2, paramFloat);
  }

  protected void writeTextPosition(float[] paramArrayOfFloat, double paramDouble1, double paramDouble2, float paramFloat)
  {
    throw new RuntimeException("writeTextPosition(float[] aff, int tx, int ty, int scaling)");
  }

  protected void writeRasterizedTextPosition(String paramString, float[] paramArrayOfFloat, int paramInt1, int paramInt2, float paramFloat)
  {
    throw new RuntimeException("method root writeRasterizedTextPosition(String JSRoutineName, float[] aff, int tx, int ty) should not be called");
  }

  protected void writeSVGRasterizedTextPosition(String paramString, float[] paramArrayOfFloat, int paramInt1, int paramInt2, float paramFloat)
  {
    int i = 25;
    if (this.type == 4)
      i = 22;
    writeCustom(i, "<use ");
    writeCustom(i, new StringBuilder().append("xlink:href =\"#").append(paramString).append("\" ").toString());
    if ((paramArrayOfFloat[0] > 0.0F) && (paramArrayOfFloat[3] > 0.0F) && (paramArrayOfFloat[1] == 0.0F) && (paramArrayOfFloat[2] == 0.0F))
      writeCustom(i, new StringBuilder().append(" transform=\"translate(").append(paramInt1).append(',').append(paramInt2).append(") scale(").append(tidy(paramArrayOfFloat[0] / paramFloat)).append(',').append(tidy(paramArrayOfFloat[3] / paramFloat)).append(")\"").toString());
    else
      writeCustom(i, new StringBuilder().append(" transform=\"matrix(").append(tidy(paramArrayOfFloat[0] / paramFloat)).append(" , ").append(tidy(paramArrayOfFloat[1] / paramFloat)).append(", ").append(tidy(paramArrayOfFloat[2] / paramFloat)).append(" , ").append(tidy(paramArrayOfFloat[3] / paramFloat)).append(", ").append(paramInt1).append(" , ").append(paramInt2).append(")\"").toString());
  }

  protected static String tidy(float paramFloat)
  {
    return removeEmptyDecimals(String.valueOf(paramFloat));
  }

  private static String removeEmptyDecimals(String paramString)
  {
    int i = paramString.indexOf(46);
    if (i > -1)
    {
      int j = 1;
      int k = paramString.length();
      for (int m = i + 1; m < k; m++)
        if (paramString.charAt(m) != '0')
        {
          j = 0;
          m = k;
        }
      if (j != 0)
        paramString = paramString.substring(0, i);
    }
    return paramString;
  }

  public void saveAdvanceWidth(String paramString1, String paramString2, int paramInt)
  {
    paramString1 = paramString1.replace('+', '-');
    HashMap localHashMap = (HashMap)this.widths.get(paramString1);
    if (localHashMap == null)
    {
      localHashMap = new HashMap();
      this.widths.put(paramString1, localHashMap);
    }
    localHashMap.put(paramString2, Integer.valueOf(paramInt));
  }

  public String getPageAsHTMLRef(int paramInt)
  {
    if (paramInt < 1)
      paramInt = 1;
    if (paramInt > this.endPage)
      paramInt = this.endPage;
    String str1 = String.valueOf(paramInt);
    if (paramInt == 1)
    {
      str1 = this.firstPageName;
    }
    else
    {
      String str2 = String.valueOf(this.endPage);
      int i = str2.length() - str1.length();
      for (int j = 0; j < i; j++)
        str1 = new StringBuilder().append('0').append(str1).toString();
    }
    return str1;
  }

  protected String getMagazinePageAsHTMLRef(int paramInt)
  {
    if (paramInt == 1)
      return getPageAsHTMLRef(paramInt);
    if (paramInt % 2 == 0)
    {
      if (paramInt == this.endPage)
        return getPageAsHTMLRef(paramInt);
      return new StringBuilder().append(getPageAsHTMLRef(paramInt)).append('-').append(getPageAsHTMLRef(paramInt + 1)).toString();
    }
    return new StringBuilder().append(getPageAsHTMLRef(paramInt - 1)).append('-').append(getPageAsHTMLRef(paramInt)).toString();
  }

  public boolean avoidDownSamplingImage()
  {
    return this.keepOriginalImage;
  }

  protected static String convertRGBtoHex(int paramInt)
  {
    return String.format("#%06X", new Object[] { Integer.valueOf(0xFFFFFF & paramInt) });
  }

  public int getEndPage()
  {
    return this.endPage;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public int convertRawPageToOutputPageNumber(int paramInt)
  {
    if (this.fullNumbering)
      return paramInt;
    for (int i = 0; i < this.pageCount; i++)
      if (this.pageNums[i].intValue() == paramInt)
        return i + 1;
    return -1;
  }

  public boolean isIECompatibilityMode()
  {
    return this.IECompatibilityMode;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.OutputDisplay
 * JD-Core Version:    0.6.2
 */
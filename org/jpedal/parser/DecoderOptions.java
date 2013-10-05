package org.jpedal.parser;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.display.PageOffsets;
import org.jpedal.exception.PdfException;
import org.jpedal.external.JPedalHelper;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;

public class DecoderOptions
{
  public static float javaVersion = 0.0F;
  public static float highlightComposite = 0.35F;
  public static boolean showMouseBox = false;
  public static boolean showErrorMessages = false;
  public static boolean isRunningOnMac = false;
  public static boolean isRunningOnWindows = false;
  public static boolean isRunningOnAIX = false;
  public static boolean isRunningOnLinux = false;
  public static boolean embedWidthData = false;
  public static JPedalHelper Helper = null;
  private int alignment = 1;
  public int insetW = 0;
  public int insetH = 0;
  private boolean useHiResImageForDisplay = true;
  private boolean useAcceleration = true;
  private PageOffsets currentOffset;
  private boolean renderPage = false;
  private int pageMode = 1;
  public static Color highlightColor = new Color(10, 100, 170);
  public static Color backgroundColor = null;
  Color nonDrawnPageColor = Color.WHITE;
  private int displayView = 1;
  public Color altPageColor = Color.WHITE;
  public Color altTextColor = null;
  public Color altDisplayBackground = null;
  public int altColorThreshold = 255;
  boolean changeTextAndLine = false;
  private Integer instance_bestQualityMaxScaling = null;
  private int[] instance_formsNoPrint = null;
  private static int[] formsNoPrint = null;
  private static String[] extactionPageSize = null;
  private String[] instance_extactionPageSize = null;
  private static Boolean overridePageSize = null;
  private Boolean instance_overridePageSize = null;
  private Boolean instance_allowPagesSmallerThanPageSize = Boolean.FALSE;
  private boolean isXMLExtraction = true;

  public boolean isXMLExtraction()
  {
    return this.isXMLExtraction;
  }

  public void setXMLExtraction(boolean paramBoolean)
  {
    this.isXMLExtraction = paramBoolean;
  }

  public void setPageMode(int paramInt)
  {
    this.pageMode = paramInt;
  }

  public int getPageMode()
  {
    return this.pageMode;
  }

  public int getInsetH()
  {
    return this.insetH;
  }

  public int getInsetW()
  {
    return this.insetW;
  }

  public final void setInset(int paramInt1, int paramInt2)
  {
    this.insetW = paramInt1;
    this.insetH = paramInt2;
  }

  public void useXMLExtraction()
  {
    this.isXMLExtraction = true;
  }

  public PdfGroupingAlgorithms getGroupingObject(int paramInt, PdfData paramPdfData, PdfPageData paramPdfPageData)
    throws PdfException
  {
    if (paramInt == -1)
      throw new RuntimeException("No pages decoded - call decodePage(pageNumber) first");
    if (paramPdfData == null)
      return null;
    return new PdfGroupingAlgorithms(paramPdfData, paramPdfPageData, this.isXMLExtraction);
  }

  public PdfGroupingAlgorithms getBackgroundGroupingObject(PdfData paramPdfData, PdfPageData paramPdfPageData)
  {
    PdfData localPdfData = paramPdfData;
    if (localPdfData == null)
      return null;
    return new PdfGroupingAlgorithms(localPdfData, paramPdfPageData, this.isXMLExtraction);
  }

  public void set(Map paramMap)
    throws PdfException
  {
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject1 = localIterator.next();
      if ((localObject1 instanceof Integer))
      {
        Integer localInteger = (Integer)localObject1;
        Object localObject2 = paramMap.get(localInteger);
        if (localInteger.equals(JPedalSettings.UNDRAWN_PAGE_COLOR))
        {
          if ((localObject2 instanceof Integer))
            this.nonDrawnPageColor = new Color(((Integer)localObject2).intValue());
          else
            throw new PdfException("JPedalSettings.UNDRAWN_PAGE_COLOR expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.PAGE_COLOR))
        {
          if ((localObject2 instanceof Integer))
            this.altPageColor = new Color(((Integer)localObject2).intValue());
          else
            throw new PdfException("JPedalSettings.PAGE_COLOR expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.TEXT_COLOR))
        {
          if ((localObject2 instanceof Integer))
            this.altTextColor = new Color(((Integer)localObject2).intValue());
          else
            throw new PdfException("JPedalSettings.TEXT_COLOR expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.REPLACEMENT_COLOR_THRESHOLD))
        {
          if ((localObject2 instanceof Integer))
            this.altColorThreshold = ((Integer)localObject2).intValue();
          else
            throw new PdfException("JPedalSettings.TEXT_COLOR expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.DISPLAY_BACKGROUND))
        {
          if ((localObject2 instanceof Integer))
            this.altDisplayBackground = new Color(((Integer)localObject2).intValue());
          else
            throw new PdfException("JPedalSettings.TEXT_COLOR expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.CHANGE_LINEART))
        {
          if ((localObject2 instanceof Boolean))
            this.changeTextAndLine = ((Boolean)localObject2).booleanValue();
          else
            throw new PdfException("JPedalSettings.CHANGE_LINEART expects a Boolean value");
        }
        else if (localInteger.equals(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING))
        {
          if ((localObject2 instanceof Integer))
            this.instance_bestQualityMaxScaling = ((Integer)localObject2);
          else
            throw new PdfException("JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING expects a Integer value");
        }
        else if (localInteger.equals(JPedalSettings.EXTRACT_AT_PAGE_SIZE))
        {
          if ((localObject2 instanceof String[]))
            this.instance_extactionPageSize = ((String[])localObject2);
          else
            throw new PdfException("JPedalSettings.EXTRACT_AT_PAGE_SIZE expects a String[] value");
        }
        else if (localInteger.equals(JPedalSettings.IGNORE_FORMS_ON_PRINT))
        {
          if ((localObject2 instanceof int[]))
            this.instance_formsNoPrint = ((int[])localObject2);
          else
            throw new PdfException("JPedalSettings.IGNORE_FORMS_ON_PRINT expects a int[] value");
        }
        else if (localInteger.equals(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE))
        {
          if ((localObject2 instanceof Boolean))
            this.instance_overridePageSize = ((Boolean)localObject2);
          else
            throw new PdfException("JPedalSettings.EXTRACT_AT_PAGE_SIZE expects a Boolean value");
        }
        else if (localInteger.equals(JPedalSettings.ALLOW_PAGES_SMALLER_THAN_PAGE_SIZE))
        {
          if ((localObject2 instanceof Boolean))
            this.instance_allowPagesSmallerThanPageSize = ((Boolean)localObject2);
          else
            throw new PdfException("JPedalSettings.ALLOW_PAGES_SMALLER_THAN_PAGE_SIZE expects a Boolean value");
        }
        else
          setParameter(paramMap, localObject1);
      }
      else
      {
        throw new PdfException("Unknown or unsupported key (not Integer) " + localObject1);
      }
    }
  }

  private static void setParameter(Map paramMap, Object paramObject)
    throws PdfException
  {
    if ((paramObject instanceof Integer))
    {
      Integer localInteger = (Integer)paramObject;
      Object localObject1 = paramMap.get(localInteger);
      if (localInteger.equals(JPedalSettings.INVERT_HIGHLIGHT))
      {
        if ((localObject1 instanceof Boolean))
          org.jpedal.render.SwingDisplay.invertHighlight = ((Boolean)localObject1).booleanValue();
        else
          throw new PdfException("JPedalSettings.INVERT_HIGHLIGHT expects an Boolean value");
      }
      else if (localInteger.equals(JPedalSettings.TEXT_INVERTED_COLOUR))
      {
        if ((localObject1 instanceof Color))
          backgroundColor = (Color)localObject1;
        else
          throw new PdfException("JPedalSettings.TEXT_INVERTED_COLOUR expects a Color value");
      }
      else if (localInteger.equals(JPedalSettings.TEXT_HIGHLIGHT_COLOUR))
      {
        if ((localObject1 instanceof Color))
          highlightColor = (Color)localObject1;
        else
          throw new PdfException("JPedalSettings.TEXT_HIGHLIGHT_COLOUR expects a Color value");
      }
      else
      {
        Object localObject2;
        if (localInteger.equals(JPedalSettings.TEXT_PRINT_NON_EMBEDDED_FONTS))
        {
          if ((localObject1 instanceof Boolean))
          {
            localObject2 = (Boolean)localObject1;
            PdfStreamDecoder.useTextPrintingForNonEmbeddedFonts = ((Boolean)localObject2).booleanValue();
          }
          else
          {
            throw new PdfException("JPedalSettings.TEXT_PRINT_NON_EMBEDDED_FONTS expects a Boolean value");
          }
        }
        else if (localInteger.equals(JPedalSettings.DISPLAY_INVISIBLE_TEXT))
        {
          if ((localObject1 instanceof Boolean))
          {
            localObject2 = (Boolean)localObject1;
            TextDecoder.showInvisibleText = ((Boolean)localObject2).booleanValue();
          }
          else
          {
            throw new PdfException("JPedalSettings.DISPLAY_INVISIBLE_TEXT expects a Boolean value");
          }
        }
        else if (localInteger.equals(JPedalSettings.CACHE_LARGE_FONTS))
        {
          if ((localObject1 instanceof Integer))
          {
            localObject2 = (Integer)localObject1;
            org.jpedal.fonts.objects.FontData.maxSizeAllowedInMemory = ((Integer)localObject2).intValue();
          }
          else
          {
            throw new PdfException("JPedalSettings.CACHE_LARGE_FONTS expects an Integer value");
          }
        }
        else if (!localInteger.equals(JPedalSettings.IMAGE_HIRES))
          if (localInteger.equals(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING))
          {
            if ((localObject1 instanceof Integer))
              org.jpedal.PDFtoImageConvertor.bestQualityMaxScaling = (Integer)localObject1;
            else
              throw new PdfException("JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING expects a Integer value");
          }
          else if (localInteger.equals(JPedalSettings.EXTRACT_AT_PAGE_SIZE))
          {
            if ((localObject1 instanceof String[]))
              extactionPageSize = (String[])localObject1;
            else
              throw new PdfException("JPedalSettings.EXTRACT_AT_PAGE_SIZE expects a String[] value");
          }
          else if (localInteger.equals(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE))
          {
            if ((localObject1 instanceof Boolean))
              overridePageSize = (Boolean)localObject1;
            else
              throw new PdfException("JPedalSettings.EXTRACT_AT_PAGE_SIZE expects a Boolean value");
          }
          else if (localInteger.equals(JPedalSettings.IGNORE_FORMS_ON_PRINT))
          {
            if ((localObject1 instanceof int[]))
              formsNoPrint = (int[])localObject1;
            else
              throw new PdfException("JPedalSettings.IGNORE_FORMS_ON_PRINT expects a int[] value");
          }
          else if (localInteger.equals(JPedalSettings.ALLOW_PAGES_SMALLER_THAN_PAGE_SIZE))
          {
            if ((localObject1 instanceof Boolean))
              org.jpedal.PDFtoImageConvertor.allowPagesSmallerThanPageSize = (Boolean)localObject1;
            else
              throw new PdfException("JPedalSettings.ALLOW_PAGES_SMALLER_THAN_PAGE_SIZE expects a Boolean value");
          }
          else
            throw new PdfException("Unknown or unsupported key " + localInteger);
      }
    }
    else
    {
      throw new PdfException("Unknown or unsupported key (not Integer) " + paramObject);
    }
  }

  public static void modifyJPedalParameters(Map paramMap)
    throws PdfException
  {
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      setParameter(paramMap, localObject);
    }
  }

  public Color getPageColor()
  {
    return this.altPageColor;
  }

  public Color getTextColor()
  {
    return this.altTextColor;
  }

  public int getReplacementColorThreshold()
  {
    return this.altColorThreshold;
  }

  public Color getDisplayBackgroundColor()
  {
    return this.altDisplayBackground;
  }

  public boolean getChangeTextAndLine()
  {
    return this.changeTextAndLine;
  }

  public Paint getNonDrawnPageColor()
  {
    return this.nonDrawnPageColor;
  }

  public Boolean getInstance_allowPagesSmallerThanPageSize()
  {
    return this.instance_allowPagesSmallerThanPageSize;
  }

  public Integer getInstance_bestQualityMaxScaling()
  {
    return this.instance_bestQualityMaxScaling;
  }

  public static int[] getFormsNoPrint()
  {
    return formsNoPrint;
  }

  public int[] getInstance_FormsNoPrint()
  {
    return this.instance_formsNoPrint;
  }

  public Boolean getPageSizeToUse()
  {
    Boolean localBoolean = Boolean.FALSE;
    if (this.instance_overridePageSize != null)
      localBoolean = this.instance_overridePageSize;
    else if (overridePageSize != null)
      localBoolean = overridePageSize;
    return localBoolean;
  }

  public float getImageDimensions(int paramInt, PdfPageData paramPdfPageData)
  {
    float f1 = -2.0F;
    String str1 = System.getProperty("org.jpedal.pageSizeOverridesImage");
    if (str1 != null)
      if (this.instance_overridePageSize != null)
        this.instance_overridePageSize = Boolean.valueOf(Boolean.parseBoolean(str1));
      else
        overridePageSize = Boolean.valueOf(Boolean.parseBoolean(str1));
    String str2 = System.getProperty("org.jpedal.pageMaxScaling");
    if (str2 != null)
      try
      {
        if (this.instance_bestQualityMaxScaling != null)
          this.instance_bestQualityMaxScaling = Integer.valueOf(Integer.parseInt(str2));
        else
          org.jpedal.PDFtoImageConvertor.bestQualityMaxScaling = Integer.valueOf(Integer.parseInt(str2));
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    String[] arrayOfString = null;
    String str3 = System.getProperty("org.jpedal.pageSize");
    if (str3 != null)
      arrayOfString = str3.split("x");
    if (arrayOfString == null)
      if (this.instance_extactionPageSize != null)
        arrayOfString = this.instance_extactionPageSize;
      else
        arrayOfString = extactionPageSize;
    float f2 = 0.0F;
    float f3 = 0.0F;
    if (arrayOfString != null)
      if (arrayOfString.length == 2)
      {
        if ((paramPdfPageData.getRotation(paramInt) == 90) || (paramPdfPageData.getRotation(paramInt) == 270))
        {
          f2 = Float.parseFloat(arrayOfString[1]);
          f3 = Float.parseFloat(arrayOfString[0]);
        }
        else
        {
          f2 = Float.parseFloat(arrayOfString[0]);
          f3 = Float.parseFloat(arrayOfString[1]);
        }
      }
      else
        throw new RuntimeException("Invalid parameters in JVM option -DpageSize ");
    float f4 = 0.0F;
    if (arrayOfString != null)
    {
      float f6 = paramPdfPageData.getCropBoxWidth2D(paramInt);
      float f7 = paramPdfPageData.getCropBoxHeight2D(paramInt);
      f4 = f2 / f6;
      float f5 = f3 / f7;
      if (f5 < f4)
        f4 = f5;
    }
    Boolean localBoolean = getPageSizeToUse();
    if ((arrayOfString != null) && (localBoolean.booleanValue()))
      f1 = f4;
    return f1;
  }

  public boolean getRenderPage()
  {
    return this.renderPage;
  }

  public void setRenderPage(boolean paramBoolean)
  {
    this.renderPage = paramBoolean;
  }

  public boolean useHardwareAcceleration()
  {
    return this.useAcceleration;
  }

  public void useHardwareAcceleration(boolean paramBoolean)
  {
    this.useAcceleration = paramBoolean;
  }

  public boolean useHiResImageForDisplay()
  {
    return this.useHiResImageForDisplay;
  }

  public void useHiResImageForDisplay(boolean paramBoolean)
  {
    this.useHiResImageForDisplay = paramBoolean;
  }

  public int getPageAlignment()
  {
    return this.alignment;
  }

  public void setPageAlignment(int paramInt)
  {
    this.alignment = paramInt;
  }

  public void setDisplayView(int paramInt)
  {
    this.displayView = paramInt;
  }

  public int getDisplayView()
  {
    return this.displayView;
  }

  public PageOffsets getCurrentOffsets()
  {
    return this.currentOffset;
  }

  public void setCurrentOffsets(PageOffsets paramPageOffsets)
  {
    this.currentOffset = paramPageOffsets;
  }

  static
  {
    try
    {
      String str = System.getProperty("os.name");
      if (str.equals("Mac OS X"))
        isRunningOnMac = true;
      else if (str.startsWith("Windows"))
        isRunningOnWindows = true;
      else if (str.startsWith("AIX"))
        isRunningOnAIX = true;
      else if (str.equals("Linux"))
        isRunningOnLinux = true;
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.DecoderOptions
 * JD-Core Version:    0.6.2
 */
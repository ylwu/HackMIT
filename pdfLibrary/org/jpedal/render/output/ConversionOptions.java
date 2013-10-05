package org.jpedal.render.output;

import java.util.HashSet;
import java.util.Map;
import javax.print.attribute.standard.PageRanges;

public abstract class ConversionOptions
{
  private ScalingMode scalingMode = ScalingMode.SCALE;
  private PageRanges realPageRange;
  private PageRanges logicalPageRange;
  private boolean includeViewer;
  private float scaling;
  private int scalingFitWidth;
  private int scalingFitHeight;
  private int[] scalingFitWidthHeight;
  private String firstPageName;
  private boolean embedImagesAsBase64Stream;
  private boolean convertSpacesToNbsp;
  private boolean convertPDFExternalFileToOutputType;
  private String formTag;
  private FontMode fontMode;
  private boolean convertOTFFonts;
  private boolean keepGlyfsSeparate;
  private String[] encoding;
  private boolean keepOriginalImage;
  private boolean outputThumbnails;
  private boolean avoidSpaces;
  private Font[] includedFonts;
  Map<String, String> jvmOptions;
  String errors = "";

  public ConversionOptions(Map<String, String> paramMap)
  {
    this.jvmOptions = paramMap;
    if (paramMap != null)
      setValuesFromJVMProperties();
  }

  public boolean notSet(String paramString)
  {
    return (this.jvmOptions == null) || (!this.jvmOptions.containsKey(paramString));
  }

  public void setRealPageRange(PageRanges paramPageRanges)
  {
    this.realPageRange = paramPageRanges;
  }

  public void setLogicalPageRange(PageRanges paramPageRanges)
  {
    this.logicalPageRange = paramPageRanges;
  }

  public void setIncludeViewer(boolean paramBoolean)
  {
    this.includeViewer = paramBoolean;
  }

  public abstract void setViewMode(ViewMode paramViewMode);

  public abstract void setTextMode(TextMode paramTextMode);

  public void setFirstPageName(String paramString)
  {
    this.firstPageName = paramString;
  }

  public void setScaling(float paramFloat)
  {
    this.scaling = paramFloat;
    this.scalingMode = ScalingMode.SCALE;
  }

  public void setScalingFitWidth(int paramInt)
  {
    this.scalingFitWidth = paramInt;
    this.scalingMode = ScalingMode.FITWIDTH;
  }

  public void setScalingFitHeight(int paramInt)
  {
    this.scalingFitHeight = paramInt;
    this.scalingMode = ScalingMode.FITHEIGHT;
  }

  public void setScalingFitWidthHeight(int paramInt1, int paramInt2)
  {
    this.scalingFitWidthHeight = new int[] { paramInt1, paramInt2 };
    this.scalingMode = ScalingMode.FITWIDTHHEIGHT;
  }

  public abstract void setNavMode(NavMode paramNavMode);

  public void setEmbedImagesAsBase64Stream(boolean paramBoolean)
  {
    this.embedImagesAsBase64Stream = paramBoolean;
  }

  public void setConvertSpacesToNbsp(boolean paramBoolean)
  {
    this.convertSpacesToNbsp = paramBoolean;
  }

  public void setConvertPDFExternalFileToOutputType(boolean paramBoolean)
  {
    this.convertPDFExternalFileToOutputType = paramBoolean;
  }

  public void setFormTag(String paramString)
  {
    this.formTag = paramString;
  }

  public void setFontMode(FontMode paramFontMode)
  {
    this.fontMode = paramFontMode;
  }

  public void setConvertOTFFonts(boolean paramBoolean)
  {
    this.convertOTFFonts = paramBoolean;
  }

  public void setIncludedFonts(Font[] paramArrayOfFont)
  {
    this.includedFonts = paramArrayOfFont;
  }

  public void setKeepGlyfsSeparate(boolean paramBoolean)
  {
    this.keepGlyfsSeparate = paramBoolean;
  }

  public void setEncoding(String paramString1, String paramString2)
  {
    this.encoding = new String[] { paramString1, paramString2 };
  }

  public void setKeepOriginalImage(boolean paramBoolean)
  {
    this.keepOriginalImage = paramBoolean;
  }

  public void setOutputThumbnails(boolean paramBoolean)
  {
    this.outputThumbnails = paramBoolean;
  }

  public PageRanges getRealPageRange()
  {
    return this.realPageRange;
  }

  public PageRanges getLogicalPageRange()
  {
    return this.logicalPageRange;
  }

  public boolean getIncludeViewer()
  {
    return this.includeViewer;
  }

  public abstract ViewMode getViewMode();

  public abstract TextMode getTextMode();

  public ScalingMode getScalingMode()
  {
    return this.scalingMode;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public int getScalingFitWidth()
  {
    return this.scalingFitWidth;
  }

  public int getScalingFitHeight()
  {
    return this.scalingFitHeight;
  }

  public int[] getScalingFitWidthHeight()
  {
    return this.scalingFitWidthHeight;
  }

  public String getFirstPageName()
  {
    return this.firstPageName;
  }

  public abstract NavMode getNavMode();

  public boolean getEmbedImagesAsBase64Stream()
  {
    return this.embedImagesAsBase64Stream;
  }

  public boolean getConvertSpacesToNbsp()
  {
    return this.convertSpacesToNbsp;
  }

  public boolean getConvertPDFExternalFileToOutputType()
  {
    return this.convertPDFExternalFileToOutputType;
  }

  public String getFormTag()
  {
    return this.formTag;
  }

  public FontMode getFontMode()
  {
    return this.fontMode;
  }

  public boolean getConvertOTFFonts()
  {
    return this.convertOTFFonts;
  }

  public Font[] getIncludedFonts()
  {
    return this.includedFonts;
  }

  public boolean getKeepGlyfsSeparate()
  {
    return this.keepGlyfsSeparate;
  }

  public String[] getEncoding()
  {
    return this.encoding;
  }

  public boolean getKeepOriginalImage()
  {
    return this.keepOriginalImage;
  }

  public boolean getOutputThumbnails()
  {
    return this.outputThumbnails;
  }

  protected abstract void setValuesFromJVMProperties();

  protected void setValuesFromJVMProperties(String paramString)
  {
    String str = "org.jpedal.pdf2html.";
    if (paramString.equals("realPageRange"))
      setJVMRealPageRange("realPageRange");
    else if (paramString.equals("logicalPageRange"))
      setJVMLogicalPageRange("logicalPageRange");
    else if (paramString.equals("includeViewer"))
      this.includeViewer = setJVMBooleanValue("includeViewer");
    else if (paramString.equals("excludeMetaData"))
      this.includeViewer = (!setJVMBooleanValue("excludeMetaData"));
    else if (paramString.equals("viewMode"))
      setJVMViewMode("viewMode");
    else if (paramString.equals("textMode"))
      setJVMTextMode("textMode");
    else if (paramString.equals("scaling"))
      setJVMScaling("scaling");
    else if (paramString.equals("firstPageName"))
      this.firstPageName = ((String)this.jvmOptions.get(str + "firstPageName"));
    else if (paramString.equals("addNavBar"))
      setJVMNavMode("addNavBar");
    else if (paramString.equals("navMode"))
      setJVMNavMode("navMode");
    else if (paramString.equals("embedImagesAsBase64Stream"))
      this.embedImagesAsBase64Stream = setJVMBooleanValue("embedImagesAsBase64Stream");
    else if (paramString.equals("convertSpacesToNbsp"))
      this.convertSpacesToNbsp = setJVMBooleanValue("convertSpacesToNbsp");
    else if (paramString.equals("convertPDFExternalFileToOutputType"))
      this.convertPDFExternalFileToOutputType = setJVMBooleanValue("convertPDFExternalFileToOutputType");
    else if (paramString.equals("formTag"))
      this.formTag = ((String)this.jvmOptions.get(str + "formTag"));
    else if (paramString.equals("fontMode"))
      setJVMFontMode("fontMode");
    else if (paramString.equals("convertOTFFonts"))
      this.convertOTFFonts = setJVMBooleanValue("convertOTFFonts");
    else if (paramString.equals("includedFonts"))
      setJVMIncludedFonts("includedFonts");
    else if (paramString.equals("keepGlyfsSeparate"))
      this.keepGlyfsSeparate = setJVMBooleanValue("keepGlyfsSeparate");
    else if (paramString.equals("encoding"))
      setJVMEncoding("encoding");
    else if (paramString.equals("keepOriginalImage"))
      this.keepOriginalImage = setJVMBooleanValue("keepOriginalImage");
    else if (paramString.equals("outputThumbnails"))
      this.outputThumbnails = setJVMBooleanValue("outputThumbnails");
    else if (paramString.equals("avoidSpaces"))
      this.avoidSpaces = setJVMBooleanValue("avoidSpaces");
    else
      addError("Property was not recognised: " + str + paramString);
  }

  protected void addError(String paramString)
  {
    this.errors = (this.errors + paramString + '\n');
  }

  public String getErrors()
  {
    return this.errors;
  }

  protected boolean setJVMBooleanValue(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("true"))
      return true;
    if (str.equals("false"))
      return false;
    addError("Value \"" + str + "\" for " + paramString + " was not recognised. Use true or false.");
    return false;
  }

  private void setJVMRealPageRange(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    try
    {
      this.realPageRange = new PageRanges(str);
    }
    catch (Exception localException)
    {
      addError("Setting page range failed with value: " + str);
    }
  }

  private void setJVMLogicalPageRange(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    try
    {
      this.logicalPageRange = new PageRanges(str);
    }
    catch (Exception localException)
    {
      addError("Setting page range failed with value: " + str);
    }
  }

  protected abstract void setJVMViewMode(String paramString);

  protected abstract void setJVMTextMode(String paramString);

  private void setJVMScaling(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    if (str.contains("x"))
    {
      String[] arrayOfString = str.split("x");
      setScalingFitWidthHeight(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]));
    }
    else if (str.toLowerCase().contains("fitwidth"))
    {
      setScalingFitWidth(Integer.parseInt(str.substring(8)));
    }
    else if (str.toLowerCase().contains("fitheight"))
    {
      setScalingFitHeight(Integer.parseInt(str.substring(9)));
    }
    else
    {
      setScaling(Float.parseFloat(str));
    }
  }

  protected abstract void setJVMNavMode(String paramString);

  private void setJVMFontMode(String paramString)
  {
    int i = 0;
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toUpperCase();
    for (FontMode localFontMode : FontMode.values())
      if (str.equals(localFontMode.toString()))
      {
        this.fontMode = localFontMode;
        i = 1;
        break;
      }
    if (i == 0)
      addError("Setting font mode failed with value: " + str);
  }

  private void setJVMEncoding(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    try
    {
      String[] arrayOfString = str.split(",");
      this.encoding = new String[] { arrayOfString[0], arrayOfString[1] };
    }
    catch (Exception localException)
    {
      addError("Setting encoding failed with value: " + str);
    }
  }

  private void setJVMIncludedFonts(String paramString)
  {
    String str1 = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    HashSet localHashSet = new HashSet();
    int i = 0;
    str1 = str1.toLowerCase();
    for (String str2 : str1.split(","))
      if (str2.equals("woff"))
      {
        localHashSet.add(Font.WOFF);
        i = 1;
      }
      else if (str2.equals("eot"))
      {
        localHashSet.add(Font.EOT);
        i = 1;
      }
      else if (str2.equals("otf"))
      {
        localHashSet.add(Font.OTF);
        i = 1;
      }
      else if (str2.equals("cff"))
      {
        localHashSet.add(Font.CFF);
        i = 1;
      }
      else
      {
        addError("Unsupported font type: " + str2);
        i = 0;
      }
    if (i != 0)
      this.includedFonts = ((Font[])localHashSet.toArray(new Font[localHashSet.size()]));
    else
      addError("Setting included fonts failed with value: " + str1);
  }

  public static enum Font
  {
    WOFF, OTF, EOT, CFF;
  }

  public static enum ScalingMode
  {
    SCALE, FITWIDTH, FITHEIGHT, FITWIDTHHEIGHT;
  }

  public static enum FontMode
  {
    EMBED_ALL_EXCEPT_BASE_FAMILIES(7), EMBED_ALL(6), DEFAULT_ON_UNMAPPED(3), FAIL_ON_UNMAPPED(2);

    private int value;

    private FontMode(int paramInt)
    {
      this.value = paramInt;
    }

    public int getValue()
    {
      return this.value;
    }
  }

  public static abstract interface NavMode
  {
    public abstract int getValue();
  }

  public static abstract interface TextMode
  {
    public abstract int getValue();
  }

  public static abstract interface ViewMode
  {
    public abstract int getValue();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.ConversionOptions
 * JD-Core Version:    0.6.2
 */
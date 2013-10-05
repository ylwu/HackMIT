package org.jpedal.render.output;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SVGConversionOptions extends ConversionOptions
{
  private ConversionOptions.ViewMode viewMode;
  private ConversionOptions.TextMode textMode;
  private ConversionOptions.NavMode navMode;
  private boolean addBorder;

  public SVGConversionOptions(Map<String, String> paramMap)
  {
    super(paramMap);
  }

  public SVGConversionOptions()
  {
    this(null);
  }

  public void setViewMode(ConversionOptions.ViewMode paramViewMode)
  {
    this.viewMode = paramViewMode;
  }

  public void setTextMode(ConversionOptions.TextMode paramTextMode)
  {
    this.textMode = paramTextMode;
  }

  public void setNavMode(ConversionOptions.NavMode paramNavMode)
  {
    this.navMode = paramNavMode;
  }

  public void setAddBorder(boolean paramBoolean)
  {
    this.addBorder = paramBoolean;
  }

  public ConversionOptions.ViewMode getViewMode()
  {
    return this.viewMode;
  }

  public ConversionOptions.TextMode getTextMode()
  {
    return this.textMode;
  }

  public ConversionOptions.NavMode getNavMode()
  {
    return this.navMode;
  }

  public boolean getAddBorder()
  {
    return this.addBorder;
  }

  protected void setValuesFromJVMProperties()
  {
    Iterator localIterator = this.jvmOptions.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = "org.jpedal.pdf2html.";
      if (str1.startsWith(str2))
      {
        str1 = str1.substring(20);
        if (str1.equals("addBorder"))
          this.addBorder = setJVMBooleanValue("addBorder");
        else
          super.setValuesFromJVMProperties(str1);
      }
    }
  }

  protected void setJVMViewMode(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("multifile"))
      this.viewMode = ViewMode.MULTIFILE;
    else if (str.equals("singlefile"))
      this.viewMode = ViewMode.SINGLEFILE;
    else if (str.equals("multifile_splitspreads"))
      this.viewMode = ViewMode.MULTIFILE_SPLITSPREADS;
    else if (str.equals("singlefile_splitspreads"))
      this.viewMode = ViewMode.SINGLEFILE_SPLITSPREADS;
    else if (str.equals("singlefile_horizontal"))
      this.viewMode = ViewMode.SINGLEFILE_HORIZONTAL;
    else
      addError("Setting view mode failed with value: " + str);
  }

  protected void setJVMTextMode(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("image_realtext"))
      this.textMode = TextMode.IMAGE_REALTEXT;
    else if (str.equals("image_shapetext_selectable"))
      this.textMode = TextMode.IMAGE_SHAPETEXT_SELECTABLE;
    else if (str.equals("image_shapetext_nonselectable"))
      this.textMode = TextMode.IMAGE_SHAPETEXT_NONSELECTABLE;
    else if (str.equals("svg_realtext"))
      this.textMode = TextMode.SVG_REALTEXT;
    else if (str.equals("svg_shapetext_selectable"))
      this.textMode = TextMode.SVG_SHAPETEXT_SELECTABLE;
    else if (str.equals("svg_shapetext_nonselectable"))
      this.textMode = TextMode.SVG_SHAPETEXT_NONSELECTABLE;
    else
      addError("Setting text mode failed with value: " + str);
  }

  protected void setJVMNavMode(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("none"))
      this.navMode = NavMode.NONE;
    else if (str.equals("css"))
      this.navMode = NavMode.CSS;
    else if (str.equals("images"))
      this.navMode = NavMode.IMAGES;
    else
      addError("Setting nav mode failed with value: " + str);
  }

  public static enum NavMode
    implements ConversionOptions.NavMode
  {
    NONE(1), CSS(2), IMAGES(3);

    private int value;

    private NavMode(int paramInt)
    {
      this.value = paramInt;
    }

    public int getValue()
    {
      return this.value;
    }
  }

  public static enum TextMode
    implements ConversionOptions.TextMode
  {
    SVG_REALTEXT(1), SVG_SHAPETEXT_SELECTABLE(2), SVG_SHAPETEXT_NONSELECTABLE(3), IMAGE_REALTEXT(7), IMAGE_SHAPETEXT_SELECTABLE(8), IMAGE_SHAPETEXT_NONSELECTABLE(9);

    private int value;

    private TextMode(int paramInt)
    {
      this.value = paramInt;
    }

    public int getValue()
    {
      return this.value;
    }
  }

  public static enum ViewMode
    implements ConversionOptions.ViewMode
  {
    MULTIFILE(1), SINGLEFILE(5), MULTIFILE_SPLITSPREADS(3), SINGLEFILE_SPLITSPREADS(7), SINGLEFILE_HORIZONTAL(9);

    private int value;

    private ViewMode(int paramInt)
    {
      this.value = paramInt;
    }

    public int getValue()
    {
      return this.value;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.SVGConversionOptions
 * JD-Core Version:    0.6.2
 */
package org.jpedal.render.output;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HTMLConversionOptions extends ConversionOptions
{
  private ConversionOptions.ViewMode viewMode;
  private ConversionOptions.TextMode textMode;
  private String googleAnalyticsID;
  private String pageTurningAnalyticsPrefix;
  private String insertIntoHead;
  private boolean disableComments;
  private String backgroundColor;
  private ConversionOptions.NavMode navMode;
  private boolean useWordSpacing;
  private boolean addJavaScript;
  private SocialMediaLinks[] socialMediaLinks;
  private String userHeadIndex;
  private String userTopIndex;
  private String userLeftIndex;
  private String userRightIndex;
  private String userBottomIndex;
  private String sitemapURL;
  private boolean inlineSVG;
  private boolean ipadCompatibilityMode;
  private boolean IECompatibilityMode;

  public HTMLConversionOptions(Map<String, String> paramMap)
  {
    super(paramMap);
  }

  public HTMLConversionOptions()
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

  public void setGoogleAnalyticsID(String paramString)
  {
    this.googleAnalyticsID = paramString;
  }

  public void setPageTurningAnalyticsPrefix(String paramString)
  {
    this.pageTurningAnalyticsPrefix = paramString;
  }

  public void setInsertIntoHead(String paramString)
  {
    this.insertIntoHead = paramString;
  }

  public void setDisableComments(boolean paramBoolean)
  {
    this.disableComments = paramBoolean;
  }

  public void setBackgroundColor(String paramString)
  {
    this.backgroundColor = paramString;
  }

  public void setNavMode(ConversionOptions.NavMode paramNavMode)
  {
    this.navMode = paramNavMode;
  }

  public void setUseWordSpacing(boolean paramBoolean)
  {
    this.useWordSpacing = paramBoolean;
  }

  public void setAddJavaScript(boolean paramBoolean)
  {
    this.addJavaScript = paramBoolean;
  }

  public void setSocialMediaLinks(SocialMediaLinks[] paramArrayOfSocialMediaLinks)
  {
    this.socialMediaLinks = paramArrayOfSocialMediaLinks;
  }

  public void setUserHeadIndex(String paramString)
  {
    this.userHeadIndex = paramString;
  }

  public void setUserTopIndex(String paramString)
  {
    this.userTopIndex = paramString;
  }

  public void setUserLeftIndex(String paramString)
  {
    this.userLeftIndex = paramString;
  }

  public void setUserRightIndex(String paramString)
  {
    this.userRightIndex = paramString;
  }

  public void setUserBottomIndex(String paramString)
  {
    this.userBottomIndex = paramString;
  }

  public void setSitemapURL(String paramString)
  {
    this.sitemapURL = paramString;
  }

  public void setInlineSVG(boolean paramBoolean)
  {
    this.inlineSVG = paramBoolean;
  }

  public void setIECompatibilityMode(boolean paramBoolean)
  {
    this.IECompatibilityMode = paramBoolean;
  }

  public ConversionOptions.ViewMode getViewMode()
  {
    return this.viewMode;
  }

  public ConversionOptions.TextMode getTextMode()
  {
    return this.textMode;
  }

  public String getGoogleAnalyticsID()
  {
    return this.googleAnalyticsID;
  }

  public String getPageTurningAnalyticsPrefix()
  {
    return this.pageTurningAnalyticsPrefix;
  }

  public String getInsertIntoHead()
  {
    return this.insertIntoHead;
  }

  public boolean getDisableComments()
  {
    return this.disableComments;
  }

  public String getBackgroundColor()
  {
    return this.backgroundColor;
  }

  public ConversionOptions.NavMode getNavMode()
  {
    return this.navMode;
  }

  public boolean getUseWordSpacing()
  {
    return this.useWordSpacing;
  }

  public boolean getAddJavaScript()
  {
    return this.addJavaScript;
  }

  public SocialMediaLinks[] getSocialMediaLinks()
  {
    return this.socialMediaLinks;
  }

  public String getUserHeadIndex()
  {
    return this.userHeadIndex;
  }

  public String getUserTopIndex()
  {
    return this.userTopIndex;
  }

  public String getUserLeftIndex()
  {
    return this.userLeftIndex;
  }

  public String getUserRightIndex()
  {
    return this.userRightIndex;
  }

  public String getUserBottomIndex()
  {
    return this.userBottomIndex;
  }

  public String getSitemapURL()
  {
    return this.sitemapURL;
  }

  public boolean getInlineSVG()
  {
    return this.inlineSVG;
  }

  public boolean getIpadCompatibilityMode()
  {
    return this.ipadCompatibilityMode;
  }

  public boolean getIECompatibilityMode()
  {
    return this.IECompatibilityMode;
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
        if (str1.equals("googleAnalyticsID"))
          setJVMGoogleAnalyticsID("googleAnalyticsID");
        else if (str1.equals("pageTurningAnalyticsPrefix"))
          this.pageTurningAnalyticsPrefix = ((String)this.jvmOptions.get(str2 + "pageTurningAnalyticsPrefix"));
        else if (str1.equals("insertIntoHead"))
          this.insertIntoHead = ((String)this.jvmOptions.get(str2 + "insertIntoHead"));
        else if (str1.equals("disableComments"))
          this.disableComments = setJVMBooleanValue("disableComments");
        else if (str1.equals("backgroundColor"))
          this.backgroundColor = ((String)this.jvmOptions.get(str2 + "backgroundColor"));
        else if (str1.equals("useWordSpacing"))
          this.useWordSpacing = setJVMBooleanValue("useWordSpacing");
        else if (str1.equals("includeJavascript"))
          this.addJavaScript = setJVMBooleanValue("includeJavascript");
        else if (str1.equals("addJavaScript"))
          this.addJavaScript = setJVMBooleanValue("addJavaScript");
        else if (str1.equals("socialMediaLinks"))
          setJVMSocialMediaLinks("socialMediaLinks");
        else if (str1.equals("userHeadIndex"))
          this.userHeadIndex = ((String)this.jvmOptions.get(str2 + "userHeadIndex"));
        else if (str1.equals("userTopIndex"))
          this.userTopIndex = ((String)this.jvmOptions.get(str2 + "userTopIndex"));
        else if (str1.equals("userLeftIndex"))
          this.userLeftIndex = ((String)this.jvmOptions.get(str2 + "userLeftIndex"));
        else if (str1.equals("userRightIndex"))
          this.userRightIndex = ((String)this.jvmOptions.get(str2 + "userRightIndex"));
        else if (str1.equals("userBottomIndex"))
          this.userBottomIndex = ((String)this.jvmOptions.get(str2 + "userBottomIndex"));
        else if (str1.equals("sitemapURL"))
          this.sitemapURL = ((String)this.jvmOptions.get(str2 + "sitemapURL"));
        else if (str1.equals("inlineSVG"))
          this.inlineSVG = setJVMBooleanValue("inlineSVG");
        else if (str1.equals("ipadCompatibilityMode"))
          this.ipadCompatibilityMode = setJVMBooleanValue("ipadCompatibilityMode");
        else if (str1.equals("IECompatibilityMode"))
          this.IECompatibilityMode = setJVMBooleanValue("IECompatibilityMode");
        else
          super.setValuesFromJVMProperties(str1);
      }
    }
  }

  protected void setJVMViewMode(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("noviewer"))
      this.viewMode = ViewMode.NOVIEWER;
    else if (str.equals("multifile"))
      this.viewMode = ViewMode.MULTIFILE;
    else if (str.equals("singlefile"))
      this.viewMode = ViewMode.SINGLEFILE;
    else if (str.equals("multifile_splitspreads"))
      this.viewMode = ViewMode.MULTIFILE_SPLITSPREADS;
    else if (str.equals("singlefile_splitspreads"))
      this.viewMode = ViewMode.SINGLEFILE_SPLITSPREADS;
    else if (str.equals("singlefile_horizontal"))
      this.viewMode = ViewMode.SINGLEFILE_HORIZONTAL;
    else if (str.equals("pageturning"))
      this.viewMode = ViewMode.PAGETURNING;
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
    else if (str.equals("svg_realtext_nofallback"))
      this.textMode = TextMode.SVG_REALTEXT_NOFALLBACK;
    else if (str.equals("svg_shapetext_selectable_nofallback"))
      this.textMode = TextMode.SVG_SHAPETEXT_SELECTABLE_NOFALLBACK;
    else if (str.equals("svg_shapetext_nonselectable_nofallback"))
      this.textMode = TextMode.SVG_SHAPETEXT_NONSELECTABLE_NOFALLBACK;
    else
      addError("Setting text mode failed with value: " + str);
  }

  protected void setJVMGoogleAnalyticsID(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toUpperCase();
    if (str.startsWith("UA-"))
      this.googleAnalyticsID = str;
    else
      addError("Setting googleAnalyticsID failed with value: " + str + ". ID must start with UA");
  }

  protected void setJVMNavMode(String paramString)
  {
    String str = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str = str.toLowerCase();
    if (str.equals("none"))
      this.navMode = NavMode.NONE;
    else if (str.equals("css"))
      this.navMode = NavMode.CSS;
    else if (str.equals("thumbnails_bottom"))
      this.navMode = NavMode.THUMBNAILS_BOTTOM;
    else if (str.equals("thumbnails_top"))
      this.navMode = NavMode.THUMBNAILS_TOP;
    else if (str.equals("thumbnails_left"))
      this.navMode = NavMode.THUMBNAILS_LEFT;
    else if (str.equals("thumbnails_right"))
      this.navMode = NavMode.THUMBNAILS_RIGHT;
    else
      addError("Setting nav mode failed with value: " + str);
  }

  private void setJVMSocialMediaLinks(String paramString)
  {
    int i = 0;
    String str1 = (String)this.jvmOptions.get("org.jpedal.pdf2html." + paramString);
    str1 = str1.toUpperCase();
    String[] arrayOfString1 = str1.split(",");
    ArrayList localArrayList = new ArrayList();
    for (String str2 : arrayOfString1)
      for (SocialMediaLinks localSocialMediaLinks : SocialMediaLinks.values())
        if (str2.equals(localSocialMediaLinks.toString()))
        {
          localArrayList.add(localSocialMediaLinks);
          i = 1;
          break;
        }
    this.socialMediaLinks = ((SocialMediaLinks[])localArrayList.toArray(new SocialMediaLinks[localArrayList.size()]));
    if (i == 0)
      addError("Setting social media links failed with value: " + str1);
  }

  public static enum SocialMediaLinks
  {
    NONE, ALL, TWITTER, FACEBOOK, GOOGLEPLUS, REDDIT, LINKEDIN, DIGG, STUMBLEUPON, TUMBLR;
  }

  public static enum NavMode
    implements ConversionOptions.NavMode
  {
    NONE(1), CSS(2), THUMBNAILS_TOP(4), THUMBNAILS_RIGHT(7), THUMBNAILS_BOTTOM(5), THUMBNAILS_LEFT(6);

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
    SVG_REALTEXT(1), SVG_SHAPETEXT_SELECTABLE(2), SVG_SHAPETEXT_NONSELECTABLE(3), SVG_REALTEXT_NOFALLBACK(4), SVG_SHAPETEXT_SELECTABLE_NOFALLBACK(5), SVG_SHAPETEXT_NONSELECTABLE_NOFALLBACK(6), IMAGE_REALTEXT(7), IMAGE_SHAPETEXT_SELECTABLE(8), IMAGE_SHAPETEXT_NONSELECTABLE(9);

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
    NOVIEWER(0), MULTIFILE(1), SINGLEFILE(5), MULTIFILE_SPLITSPREADS(3), SINGLEFILE_SPLITSPREADS(7), SINGLEFILE_HORIZONTAL(9), PAGETURNING(11);

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
 * Qualified Name:     org.jpedal.render.output.HTMLConversionOptions
 * JD-Core Version:    0.6.2
 */
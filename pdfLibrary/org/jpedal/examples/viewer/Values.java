package org.jpedal.examples.viewer;

public class Values
{
  public static final int RUNNING_NORMAL = 0;
  public static final int RUNNING_APPLET = 1;
  public static final int RUNNING_WEBSTART = 2;
  public static final int RUNNING_JSP = 3;
  public static final int RUNNING_PLUGIN = 4;
  private boolean isEncryptOnClasspath = false;
  private boolean isPDF = true;
  private boolean isMultiTiff = false;
  private int modeOfOperation = 0;
  private long size;
  private String inputDir = null;
  private int currentPage = 1;
  private String selectedFile = null;
  private boolean formsChanged;
  private String separator = null;
  private boolean useHiresImage = true;
  public int m_x1;
  public int m_y1;
  public int m_x2;
  public int m_y2;
  public int dx;
  public int dy = 0;
  public double viewportScale = 1.0D;
  public int maxViewY = 0;
  private int pageCount = 1;
  private int maxNoOfMultiViewers;
  public static boolean isProcessing = false;

  public Values()
  {
    String str = System.getProperty("org.jpedal.securityprovider");
    try
    {
      if (str == null)
        str = "/org/bouncycastle/";
      this.isEncryptOnClasspath = (getClass().getResource(str) != null);
    }
    catch (Exception localException)
    {
      this.isEncryptOnClasspath = false;
    }
    catch (Error localError)
    {
      this.isEncryptOnClasspath = false;
    }
  }

  public static boolean isProcessing()
  {
    return isProcessing;
  }

  public static void setProcessing(boolean paramBoolean)
  {
    isProcessing = paramBoolean;
  }

  public boolean isEncrypOnClasspath()
  {
    return this.isEncryptOnClasspath;
  }

  public boolean isPDF()
  {
    return this.isPDF;
  }

  public void setPDF(boolean paramBoolean)
  {
    this.isPDF = paramBoolean;
  }

  public int getCurrentPage()
  {
    return this.currentPage;
  }

  public void setCurrentPage(int paramInt)
  {
    this.currentPage = paramInt;
  }

  public String getInputDir()
  {
    if (this.inputDir == null)
      this.inputDir = System.getProperty("user.dir");
    return this.inputDir;
  }

  public void setInputDir(String paramString)
  {
    this.inputDir = paramString;
  }

  public String getSelectedFile()
  {
    return this.selectedFile;
  }

  public void setSelectedFile(String paramString)
  {
    this.selectedFile = paramString;
  }

  public boolean isFormsChanged()
  {
    return this.formsChanged;
  }

  public void setFormsChanged(boolean paramBoolean)
  {
    this.formsChanged = paramBoolean;
  }

  public int getPageCount()
  {
    return this.pageCount;
  }

  public void setPageCount(int paramInt)
  {
    this.pageCount = paramInt;
  }

  public long getFileSize()
  {
    return this.size;
  }

  public void setFileSize(long paramLong)
  {
    this.size = paramLong;
  }

  public String getSeparator()
  {
    if (this.separator == null)
      this.separator = System.getProperty("file.separator");
    return this.separator;
  }

  public int getModeOfOperation()
  {
    return this.modeOfOperation;
  }

  public void setModeOfOperation(int paramInt)
  {
    this.modeOfOperation = paramInt;
  }

  public boolean isUseHiresImage()
  {
    return this.useHiresImage;
  }

  public void setUseHiresImage(boolean paramBoolean)
  {
    this.useHiresImage = paramBoolean;
  }

  public boolean isMultiTiff()
  {
    return this.isMultiTiff;
  }

  public void setMultiTiff(boolean paramBoolean)
  {
    this.isMultiTiff = paramBoolean;
  }

  public void setMaxMiltiViewers(int paramInt)
  {
    this.maxNoOfMultiViewers = paramInt;
  }

  public int getMaxMiltiViewers()
  {
    return this.maxNoOfMultiViewers;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.Values
 * JD-Core Version:    0.6.2
 */
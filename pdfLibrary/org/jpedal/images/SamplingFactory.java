package org.jpedal.images;

public class SamplingFactory
{
  public static int none = 0;
  public static int medium = 1;
  public static int high = 2;
  public static int print_enable = 3;
  public static int print_disable = 4;
  public static int downsampleLevel = high;
  public static boolean isPrintDownsampleEnabled = true;

  public static void setDownsampleMode(String paramString)
  {
    if (paramString == null)
      paramString = System.getProperty("org.jpedal.downsample");
    if (paramString != null)
      if (paramString.equals("high"))
        downsampleLevel = high;
      else if (paramString.equals("medium"))
        downsampleLevel = medium;
      else if (paramString.equals("none"))
        downsampleLevel = none;
      else if (paramString.equals("print_disable"))
        isPrintDownsampleEnabled = false;
      else if (paramString.equals("print_enable"))
        isPrintDownsampleEnabled = true;
  }

  public static void setDownsampleMode(int paramInt)
  {
    if ((paramInt == high) || (paramInt == medium) || (paramInt == none))
      downsampleLevel = paramInt;
    else if (paramInt == print_disable)
      isPrintDownsampleEnabled = false;
    else if (paramInt == print_enable)
      isPrintDownsampleEnabled = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.images.SamplingFactory
 * JD-Core Version:    0.6.2
 */
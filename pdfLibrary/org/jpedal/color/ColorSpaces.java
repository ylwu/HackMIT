package org.jpedal.color;

import java.awt.RenderingHints;
import java.lang.reflect.Field;
import org.jpedal.utils.LogWriter;

public class ColorSpaces
{
  public static final int ICC = 1247168582;
  public static final int CalGray = 391471749;
  public static final int DeviceGray = 1568372915;
  public static final int DeviceN = 960981604;
  public static final int Separation = -2073385820;
  public static final int Pattern = 1146450818;
  public static final int Lab = 1847602;
  public static final int Indexed = 895578984;
  public static final int DeviceRGB = 1785221209;
  public static final int CalRGB = 1008872003;
  public static final int DeviceCMYK = 1498837125;
  public static RenderingHints hints = null;

  public static final int convertNameToID(String paramString)
  {
    int i = -1;
    if (paramString.contains("Indexed"))
      i = 895578984;
    else if (paramString.contains("Separation"))
      i = -2073385820;
    else if (paramString.contains("DeviceN"))
      i = 960981604;
    else if ((paramString.contains("DeviceCMYK") | paramString.contains("CMYK")))
      i = 1498837125;
    else if (paramString.contains("CalGray"))
      i = 391471749;
    else if (paramString.contains("CalRGB"))
      i = 1008872003;
    else if (paramString.contains("Lab"))
      i = 1847602;
    else if (paramString.contains("ICCBased"))
      i = 1247168582;
    else if (paramString.contains("Pattern"))
      i = 1146450818;
    else if ((paramString.contains("DeviceRGB")) || (paramString.contains("RGB")))
      i = 1785221209;
    else if ((paramString.contains("DeviceGray")) || (paramString.indexOf('G') != -1))
      i = 1568372915;
    return i;
  }

  public static String showAsConstant(int paramInt)
  {
    Field[] arrayOfField = ColorSpaces.class.getFields();
    int i = arrayOfField.length;
    String str = null;
    for (int j = 0; j < i; j++)
      try
      {
        int k = arrayOfField[j].getInt(new ColorSpaces());
        if (k == paramInt)
        {
          str = "ColorSpaces." + arrayOfField[j].getName();
          i = j;
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return str;
  }

  public static String IDtoString(int paramInt)
  {
    switch (paramInt)
    {
    case 1247168582:
      return "ICC";
    case 391471749:
      return "CalGray";
    case 1568372915:
      return "DeviceGray";
    case 960981604:
      return "DeviceN";
    case -2073385820:
      return "Separation";
    case 1146450818:
      return "Pattern";
    case 1847602:
      return "Lab";
    case 895578984:
      return "Indexed";
    case 1785221209:
      return "DeviceRGB";
    case 1008872003:
      return "CalRGB";
    case 1498837125:
      return "DeviceCMYK";
    }
    return "unknown";
  }

  static
  {
    hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.ColorSpaces
 * JD-Core Version:    0.6.2
 */
package org.jpedal.utils;

import java.util.StringTokenizer;

public class Fonts
{
  public static String fe = "</font>";
  public static String fb = "<font ";

  public static final String cleanupTokens(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject = "";
    int i = 0;
    int j = 0;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "<>", true);
    String str2 = localStringTokenizer.nextToken();
    while (localStringTokenizer.hasMoreTokens())
    {
      if ((str2.equals("<") & localStringTokenizer.hasMoreTokens()))
      {
        String str1 = new StringBuilder().append(str2).append(localStringTokenizer.nextToken()).append(localStringTokenizer.nextToken()).toString();
        i += str1.length();
        str2 = "";
        if (str1.startsWith(fb))
          localObject = str1;
        if (str1.equals(fe))
        {
          int k = paramString.indexOf('<', i - 1);
          int m = paramString.indexOf("</", i - 1);
          if (k == m)
          {
            localStringBuilder.append(str1);
          }
          else
          {
            int n = paramString.indexOf(fb, i - 1);
            int i1 = paramString.indexOf('>', n);
            j = 0;
            if ((n != -1) && (i1 != -1))
            {
              String str3 = paramString.substring(n, i1 + 1);
              if (str3.equals(localObject))
                j = 1;
            }
            if (j == 0)
              localStringBuilder.append(str1);
          }
        }
        else if ((str1.startsWith(fb) & j))
        {
          j = 0;
        }
        else
        {
          localStringBuilder.append(str1);
        }
      }
      else
      {
        localStringBuilder.append(str2);
        i += str2.length();
        str2 = "";
      }
      if (localStringTokenizer.hasMoreTokens())
      {
        str2 = localStringTokenizer.nextToken();
        if (!localStringTokenizer.hasMoreTokens())
        {
          localStringBuilder.append(str2);
          i += str2.length();
        }
      }
    }
    return localStringBuilder.toString();
  }

  public static final String getActiveFontTag(String paramString1, String paramString2)
  {
    String str = "";
    int i = paramString1.lastIndexOf(fb);
    int j;
    if (i > -1)
    {
      j = paramString1.indexOf("\">", i);
      if (j > 0)
        str = paramString1.substring(i, j + 2);
    }
    else
    {
      i = paramString2.lastIndexOf(fb);
      if (i > -1)
      {
        j = paramString2.indexOf("\">", i);
        if (j > 0)
          str = paramString2.substring(i, j + 2);
      }
    }
    return str;
  }

  public static final String createFontToken(String paramString, int paramInt)
  {
    int i = paramString.indexOf(',');
    String str1;
    if (i != -1)
    {
      String str2 = paramString.substring(i + 1);
      paramString = paramString.substring(0, i);
      str1 = new StringBuilder().append(fb).append("face=\"").append(paramString).append("\" style=\"font-size:").append(paramInt).append("pt;font-style:").append(str2).append("\">").toString();
    }
    else
    {
      str1 = new StringBuilder().append(fb).append("face=\"").append(paramString).append("\" style=\"font-size:").append(paramInt).append("pt\">").toString();
    }
    return str1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.Fonts
 * JD-Core Version:    0.6.2
 */
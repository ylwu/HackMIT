package org.jpedal.utils;

import java.io.PrintStream;

public class Strip
{
  private static final String strip_start_token_deliminator = "<&";
  private static final String strip_end_token_deliminator = ">;";

  public static StringBuilder stripXML(String paramString, boolean paramBoolean)
  {
    if (paramString == null)
      return null;
    return stripXML(new StringBuilder(paramString), paramBoolean);
  }

  public static StringBuilder stripXML(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    int i = paramStringBuilder.length();
    StringBuilder localStringBuilder1 = new StringBuilder(i);
    StringBuilder localStringBuilder2 = new StringBuilder(i);
    if (paramStringBuilder == null)
      return localStringBuilder1;
    if (paramBoolean)
    {
      char c2 = ' ';
      int j = 0;
      int k = paramStringBuilder.length();
      for (int n = 0; n < k; n++)
      {
        char c1 = paramStringBuilder.charAt(n);
        int m = 0;
        if ((j != 0) && (c2 == '&') && ((c1 == '<') || (c1 == '&')))
        {
          localStringBuilder1.append(localStringBuilder2);
          localStringBuilder2 = new StringBuilder(5);
        }
        if ("<&".indexOf(c1) != -1)
        {
          j = 1;
          c2 = c1;
          m = 1;
        }
        if ((j != 0) && (c2 == '&') && (c1 == ' '))
        {
          j = 0;
          localStringBuilder1.append('&');
          localStringBuilder2 = new StringBuilder(5);
        }
        if (j == 0)
          localStringBuilder1.append(c1);
        else
          localStringBuilder2.append(c1);
        if (m == 0)
        {
          int i1 = ">;".indexOf(c1);
          if (((i1 == 1 ? 1 : 0) & (c2 == '&' ? 1 : 0)) != 0)
          {
            if (localStringBuilder2.toString().equals("&lt;"))
              localStringBuilder1.append('<');
            else if (localStringBuilder2.toString().equals("&gt;"))
              localStringBuilder1.append('>');
            else if (localStringBuilder2.toString().equals("&amp;"))
              localStringBuilder1.append('&');
            j = 0;
            localStringBuilder2 = new StringBuilder();
          }
          else if (((i1 == 0 ? 1 : 0) & (c2 == '<' ? 1 : 0)) != 0)
          {
            j = 0;
            localStringBuilder2 = new StringBuilder();
          }
          else if (c1 == '&')
          {
            System.out.println(new StringBuilder().append(c1).append(" ").append(c2).append(' ').append(i1).toString());
            localStringBuilder1.append('&');
          }
        }
      }
    }
    else
    {
      localStringBuilder1 = paramStringBuilder;
    }
    localStringBuilder1 = trim(localStringBuilder1);
    return localStringBuilder1;
  }

  public static StringBuilder stripXMLArrows(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    if (paramStringBuilder == null)
      return localStringBuilder1;
    if (paramBoolean)
    {
      int i = 32;
      int j = 0;
      int k = paramStringBuilder.length();
      for (int m = 0; m < k; m++)
      {
        char c = paramStringBuilder.charAt(m);
        if ("<&".indexOf(c) != -1)
        {
          j = 1;
          i = c;
        }
        if (j == 0)
          localStringBuilder1.append(c);
        else
          localStringBuilder2.append(c);
        int n = ">;".indexOf(c);
        if (((n == 1 ? 1 : 0) & (i == 38 ? 1 : 0)) != 0)
        {
          if ((!localStringBuilder2.toString().equals("&lt;")) && (!localStringBuilder2.toString().equals("&gt;")))
            localStringBuilder1.append(localStringBuilder2);
          j = 0;
          localStringBuilder2 = new StringBuilder();
        }
        else if (((n == 0 ? 1 : 0) & (i == 60 ? 1 : 0)) != 0)
        {
          j = 0;
          localStringBuilder2 = new StringBuilder();
        }
      }
    }
    else
    {
      localStringBuilder1 = paramStringBuilder;
    }
    localStringBuilder1 = trim(localStringBuilder1);
    return localStringBuilder1;
  }

  public static final StringBuilder trim(StringBuilder paramStringBuilder)
  {
    int j = paramStringBuilder.length();
    String str = paramStringBuilder.toString();
    int k = 0;
    try
    {
      for (k = j - 1; k > -1; k--)
      {
        int i = paramStringBuilder.charAt(k);
        if (i == 32)
          paramStringBuilder.deleteCharAt(k);
        else
          k = -1;
      }
    }
    catch (Exception localException)
    {
      System.out.println(new StringBuilder().append(str).append("<>").append(paramStringBuilder).append("<>").append(k).toString());
    }
    return paramStringBuilder;
  }

  public static final String removeMultipleSpacesAndReturns(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    int i = 1;
    while (i < localStringBuilder.length())
      if (((localStringBuilder.charAt(i) == ' ' ? 1 : 0) & (localStringBuilder.charAt(i - 1) == ' ' ? 1 : 0) | (localStringBuilder.charAt(i) == '\r' ? 1 : 0) & (localStringBuilder.charAt(i - 1) == '\r' ? 1 : 0)) != 0)
        localStringBuilder.deleteCharAt(i);
      else
        i++;
    return localStringBuilder.toString();
  }

  public static final String stripSpaces(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    while ((localStringBuilder.length() > 0) && (localStringBuilder.charAt(0) == ' '))
    {
      localStringBuilder.deleteCharAt(0);
      int i = localStringBuilder.length() - 1;
      while ((i > 0) && (localStringBuilder.charAt(i) == ' '))
      {
        localStringBuilder.deleteCharAt(i);
        i--;
        if (i < 0)
          break;
      }
    }
    return localStringBuilder.toString();
  }

  public static final String stripAllSpaces(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    int i = localStringBuilder.length() - 1;
    while (i > 0)
    {
      if (localStringBuilder.charAt(i) == ' ')
        localStringBuilder.deleteCharAt(i);
      i--;
      if (i < 0)
        break;
    }
    return localStringBuilder.toString();
  }

  public static final StringBuilder stripArrows(StringBuilder paramStringBuilder)
  {
    int i = paramStringBuilder.length() - 1;
    if (i >= 0)
      while (true)
      {
        if ((paramStringBuilder.charAt(i) == '<') || (paramStringBuilder.charAt(i) == '>'))
          paramStringBuilder.deleteCharAt(i);
        i--;
        if (i < 0)
          break;
      }
    return paramStringBuilder;
  }

  public static final StringBuilder stripAllSpaces(StringBuilder paramStringBuilder)
  {
    int i = paramStringBuilder.length() - 1;
    while (i > 0)
    {
      if (paramStringBuilder.charAt(i) == ' ')
        paramStringBuilder.deleteCharAt(i);
      i--;
      if (i < 0)
        break;
    }
    return paramStringBuilder;
  }

  public static final String convertToText(String paramString, boolean paramBoolean)
  {
    StringBuffer localStringBuffer;
    if (paramBoolean)
    {
      byte[] arrayOfByte1 = StringUtils.toBytes(paramString);
      int i = arrayOfByte1.length;
      int j = 0;
      int k = 0;
      for (int m = 0; m < i; m++)
        if (arrayOfByte1[m] == 60)
        {
          k = 1;
          if ((arrayOfByte1[(m + 1)] == 83) && (arrayOfByte1[(m + 2)] == 112) && (arrayOfByte1[(m + 3)] == 97) && (arrayOfByte1[(m + 4)] == 99) && (arrayOfByte1[(m + 5)] == 101))
          {
            arrayOfByte1[j] = 9;
            j++;
          }
        }
        else if (arrayOfByte1[m] == 62)
        {
          k = 0;
        }
        else if (k == 0)
        {
          arrayOfByte1[j] = arrayOfByte1[m];
          j++;
        }
      byte[] arrayOfByte2 = new byte[j];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, j);
      localStringBuffer = new StringBuffer(new String(arrayOfByte2));
    }
    else
    {
      localStringBuffer = new StringBuffer(paramString);
    }
    return localStringBuffer.toString();
  }

  public static final String stripComment(String paramString)
  {
    if (paramString != null)
    {
      int i = paramString.indexOf("**");
      if (i > 0)
        paramString = paramString.substring(0, i - 1).trim();
      if (i == 0)
        paramString = "";
    }
    return paramString;
  }

  public static StringBuilder stripAmpHash(StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    for (int j = 0; j < paramStringBuilder.length(); j++)
    {
      char c = paramStringBuilder.charAt(j);
      if (i != 0)
      {
        if (c == ';')
          i = 0;
      }
      else if (c == '&')
        i = 1;
      else
        localStringBuilder.append(c);
    }
    return localStringBuilder;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.Strip
 * JD-Core Version:    0.6.2
 */
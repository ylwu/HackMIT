package org.jpedal.utils;

import java.io.UnsupportedEncodingException;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.io.TextTokens;
import org.jpedal.parser.DecoderOptions;

public class StringUtils
{
  private static final int aInt = 97;
  private static final int zeroInt = 48;
  private static final int nineInt = 57;
  private static final int openSquareBracketInt = 91;
  private static final int closeSquareBracketInt = 93;
  private static final int openCurlyBracket = 40;
  private static final int closeCurlyBracket = 41;
  private static final int backSlashInt = 92;
  private static final int forwardSlashInt = 47;
  private static final int hashInt = 35;
  private static final int divideInt = 247;
  private static final int fullStopInt = 46;
  private static final int spaceInt = 32;
  private static final int percentInt = 37;
  private static final int minusInt = 45;
  private static final int underScoreInt = 95;
  private static final int backSlachInt = 92;
  private static final int nInt = 110;
  private static final int newLineInt = 10;
  private static final int plusInt = 43;
  private static final int pInt = 112;
  private static final int colonInt = 58;
  private static final int equalsInt = 61;
  private static final int cInt = 99;
  private static final int qInt = 113;
  private static String enc = System.getProperty("file.encoding");

  public static String toLowerCase(String paramString)
  {
    int i = paramString.length();
    char[] arrayOfChar = paramString.toCharArray();
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      int j = arrayOfChar[m];
      if ((j > 64) && (j < 91))
      {
        j = (char)(j + 32);
        arrayOfChar[m] = j;
        k = 1;
      }
    }
    if (k != 0)
      return String.copyValueOf(arrayOfChar, 0, i);
    return paramString;
  }

  public static String toUpperCase(String paramString)
  {
    int i = paramString.length();
    char[] arrayOfChar = paramString.toCharArray();
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      int j = arrayOfChar[m];
      if ((j > 96) && (j < 123))
      {
        j = (char)(j - 32);
        arrayOfChar[m] = j;
        k = 1;
      }
    }
    if (k != 0)
      return String.copyValueOf(arrayOfChar, 0, i);
    return paramString;
  }

  public static final String handleEscapeChars(String paramString)
  {
    for (int i = paramString.indexOf(92); i != -1; i = paramString.indexOf(92))
    {
      char c = paramString.charAt(i + 1);
      if (c == 'n')
        c = '\n';
      paramString = new StringBuilder().append(paramString.substring(0, i)).append(c).append(paramString.substring(i + 2, paramString.length())).toString();
    }
    return paramString;
  }

  public static final String convertHexChars(String paramString)
  {
    if (paramString == null)
      return paramString;
    int i = paramString.indexOf(35);
    if (i == -1)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramString.length();
    for (int k = 0; k < j; k++)
    {
      char c = paramString.charAt(k);
      if (c == '#')
      {
        k++;
        int m = k + 2;
        if (m > j)
          m = j;
        String str = paramString.substring(k, m);
        c = (char)Integer.parseInt(str, 16);
        k++;
        if (c != ' ')
          localStringBuilder.append(c);
      }
      else
      {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }

  public static boolean isNumber(String paramString)
  {
    byte[] arrayOfByte = toBytes(paramString);
    int i = arrayOfByte.length;
    boolean bool = true;
    for (int j = 0; j < i; j++)
      if (((arrayOfByte[j] < 48) || (arrayOfByte[j] > 57)) && (arrayOfByte[j] != 46) && ((j != 0) || (arrayOfByte[j] != 45)))
      {
        bool = false;
        j = i;
      }
    return bool;
  }

  public static String[] remove(String[] paramArrayOfString, int paramInt)
  {
    if ((paramInt < 0) || (paramInt > paramArrayOfString.length))
      return paramArrayOfString;
    String[] arrayOfString = new String[paramArrayOfString.length - 1];
    int i = 0;
    for (int j = 0; j < paramArrayOfString.length; j++)
      if (j != paramInt)
        arrayOfString[(i++)] = paramArrayOfString[j];
    return arrayOfString;
  }

  public static String makeHTMLNameSafe(String paramString)
  {
    if ((paramString == null) || (paramString.isEmpty()))
      return paramString;
    Object localObject1 = paramString.toCharArray();
    if ((paramString.indexOf(37) != -1) || (paramString.indexOf(32) != -1) || (paramString.indexOf(46) != -1) || (paramString.indexOf(43) != -1) || (paramString.indexOf(58) != -1) || (paramString.indexOf(61) != -1) || (paramString.indexOf(47) != -1) || (paramString.indexOf(92) != -1))
      for (int i = 0; i < localObject1.length; i++)
        switch (localObject1[i])
        {
        case ' ':
          localObject1[i] = 95;
          break;
        case '.':
          localObject1[i] = 45;
          break;
        case '%':
          localObject1[i] = 95;
          break;
        case '+':
          localObject1[i] = 112;
          break;
        case ':':
          localObject1[i] = 99;
          break;
        case '=':
          localObject1[i] = 113;
          break;
        case '/':
          localObject1[i] = 95;
          break;
        case '\\':
          localObject1[i] = 95;
        }
    char[] arrayOfChar1 = { '[', ']', '#', '÷', '(', ')' };
    int j = 0;
    int i1;
    int i5;
    for (i1 : localObject1)
      for (i5 : arrayOfChar1)
        if (i1 == i5)
          j++;
    if (j > 0)
    {
      int k = 0;
      char[] arrayOfChar3 = new char[localObject1.length - j];
      label452: for (??? : localObject1)
      {
        for (int i7 : arrayOfChar1)
          if (??? == i7)
            break label452;
        arrayOfChar3[(k++)] = ???;
      }
      localObject1 = arrayOfChar3;
    }
    if ((localObject1[0] >= '0') && (localObject1[0] <= '9'))
    {
      char[] arrayOfChar2 = new char[localObject1.length + 1];
      System.arraycopy(localObject1, 0, arrayOfChar2, 1, localObject1.length);
      arrayOfChar2[0] = 'a';
      localObject1 = arrayOfChar2;
    }
    paramString = new String((char[])localObject1);
    return paramString;
  }

  public static String getPageNumberAsString(int paramInt1, int paramInt2)
  {
    String str1 = String.valueOf(paramInt1);
    String str2 = String.valueOf(paramInt2);
    int i = str2.length() - str1.length();
    for (int j = 0; j < i; j++)
      str1 = new StringBuilder().append('0').append(str1).toString();
    return str1;
  }

  public static String getTextString(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    String str1 = "";
    StandardFonts.checkLoaded(6);
    Object localObject = null;
    if (paramArrayOfByte != null)
      localObject = new char[paramArrayOfByte.length * 2];
    int i = 0;
    TextTokens localTextTokens = new TextTokens(paramArrayOfByte);
    char c;
    if (localTextTokens.isUnicode())
      while (localTextTokens.hasMoreTokens())
      {
        c = localTextTokens.nextUnicodeToken();
        if (c == '\t')
        {
          localObject[i] = 32;
          i++;
        }
        else if ((c > '\037') || ((paramBoolean) && ((c == '\n') || (c == '\r'))))
        {
          localObject[i] = c;
          i++;
        }
      }
    while (localTextTokens.hasMoreTokens())
    {
      c = localTextTokens.nextToken();
      String str2 = null;
      if (c == '\t')
        str2 = " ";
      else if ((paramBoolean) && ((c == '\n') || (c == '\r')))
        str2 = String.valueOf(c);
      else if ((c > '\037') && (c < 'ý'))
        str2 = StandardFonts.getEncodedChar(6, c);
      if (str2 != null)
      {
        int j = str2.length();
        if (i + j >= localObject.length)
        {
          char[] arrayOfChar = new char[j + i + 10];
          System.arraycopy(localObject, 0, arrayOfChar, 0, localObject.length);
          localObject = arrayOfChar;
        }
        for (int k = 0; k < j; k++)
        {
          localObject[i] = str2.charAt(k);
          i++;
        }
      }
    }
    if (localObject != null)
      str1 = String.copyValueOf((char[])localObject, 0, i);
    return str1;
  }

  public static String replaceAllManual(String paramString1, int paramInt, String paramString2)
  {
    for (int i = paramString1.indexOf(paramInt); i != -1; i = paramString1.indexOf(paramInt, i + 1))
      paramString1 = new StringBuilder().append(paramString1.substring(0, i)).append(paramString2).append(paramString1.substring(i + 1)).toString();
    return paramString1;
  }

  public static String correctSpecialChars(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
      if (paramString.charAt(i) == '&')
      {
        paramString = replaceAllManual(paramString, 38, "&amp;");
        i = paramString.length();
      }
    for (i = 0; i < paramString.length(); i++)
      switch (paramString.charAt(i))
      {
      case 'á':
        paramString = replaceAllManual(paramString, 225, "&aacute;");
        break;
      case 'à':
        paramString = replaceAllManual(paramString, 224, "&agrave;");
        break;
      case 'â':
        paramString = replaceAllManual(paramString, 226, "&acirc;");
        break;
      case 'å':
        paramString = replaceAllManual(paramString, 229, "&aring;");
        break;
      case 'ã':
        paramString = replaceAllManual(paramString, 227, "&atilde;");
        break;
      case 'ä':
        paramString = replaceAllManual(paramString, 228, "&auml;");
        break;
      case 'æ':
        paramString = replaceAllManual(paramString, 230, "&aelig;");
        break;
      case 'ç':
        paramString = replaceAllManual(paramString, 231, "&ccedil;");
        break;
      case 'é':
        paramString = replaceAllManual(paramString, 233, "&eacute;");
        break;
      case 'è':
        paramString = replaceAllManual(paramString, 232, "&egrave;");
        break;
      case 'ê':
        paramString = replaceAllManual(paramString, 234, "&ecirc;");
        break;
      case 'ë':
        paramString = replaceAllManual(paramString, 235, "&euml;");
        break;
      case 'í':
        paramString = replaceAllManual(paramString, 237, "&iacute;");
        break;
      case 'ì':
        paramString = replaceAllManual(paramString, 236, "&igrave;");
        break;
      case 'î':
        paramString = replaceAllManual(paramString, 238, "&icirc;");
        break;
      case 'ï':
        paramString = replaceAllManual(paramString, 239, "&iuml;");
        break;
      case 'ñ':
        paramString = replaceAllManual(paramString, 241, "&ntilde;");
        break;
      case 'ó':
        paramString = replaceAllManual(paramString, 243, "&oacute;");
        break;
      case 'ò':
        paramString = replaceAllManual(paramString, 242, "&ograve;");
        break;
      case 'ô':
        paramString = replaceAllManual(paramString, 244, "&ocirc;");
        break;
      case 'ø':
        paramString = replaceAllManual(paramString, 248, "&oslash;");
        break;
      case 'õ':
        paramString = replaceAllManual(paramString, 245, "&otilde;");
        break;
      case 'ö':
        paramString = replaceAllManual(paramString, 246, "&ouml;");
        break;
      case 'ß':
        paramString = replaceAllManual(paramString, 223, "&szlig;");
        break;
      case 'ú':
        paramString = replaceAllManual(paramString, 250, "&uacute;");
        break;
      case 'ù':
        paramString = replaceAllManual(paramString, 249, "&ugrave;");
        break;
      case 'û':
        paramString = replaceAllManual(paramString, 251, "&ucirc;");
        break;
      case 'ü':
        paramString = replaceAllManual(paramString, 252, "&uuml;");
        break;
      case 'ÿ':
        paramString = replaceAllManual(paramString, 255, "&yuml;");
        break;
      case '’':
        paramString = replaceAllManual(paramString, 8217, "&#39;");
      }
    return paramString;
  }

  public static byte[] toBytes(String paramString)
  {
    byte[] arrayOfByte = null;
    try
    {
      arrayOfByte = paramString.getBytes(enc);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localUnsupportedEncodingException.getMessage()).toString());
    }
    return arrayOfByte;
  }

  public static String makeMethodSafe(String paramString)
  {
    String str = makeHTMLNameSafe(paramString);
    str = str.replace("-", "_");
    return str;
  }

  public static String stripIllegalCharacters(String paramString)
  {
    String str = "";
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if (((c >= ' ') || (c < 0)) && ((c <= '~') || (c >= ' ')))
        str = new StringBuilder().append(str).append(c).toString();
    }
    return str;
  }

  static
  {
    if ((!enc.equals("UTF-8")) && (!enc.equals("MacRoman")) && (!enc.equals("Cp1252")))
      if (DecoderOptions.isRunningOnMac)
        enc = "MacRoman";
      else if (DecoderOptions.isRunningOnWindows)
        enc = "Cp1252";
      else
        enc = "UTF-8";
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.StringUtils
 * JD-Core Version:    0.6.2
 */
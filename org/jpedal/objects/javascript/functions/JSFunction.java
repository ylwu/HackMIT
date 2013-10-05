package org.jpedal.objects.javascript.functions;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.StringUtils;

public class JSFunction
{
  AcroRenderer acro;
  FormObject formObject;
  int functionID = 0;
  public static final int AFDate = 1;
  public static final int AFNumber = 2;
  public static final int AFPercent = 3;
  public static final int AFRange = 4;
  public static final int AFSimple = 5;
  public static final int AFSpecial = 6;
  public static final int AFTime = 7;
  static final int AVG = 1;
  static final int SUM = 2;
  static final int PRD = 3;
  static final int MIN = 4;
  static final int MAX = 5;
  public static final int UNKNOWN = -1;
  public static final int KEYSTROKE = 1;
  public static final int VALIDATE = 2;
  public static final int FORMAT = 3;
  public static final int CALCULATE = 4;
  public boolean DECIMAL_IS_COMMA = false;
  String value = null;
  private static int staticGapformat = -1;
  private static int staticDecimalcount = -1;

  public static void setValidDataFormat(int paramInt1, int paramInt2)
  {
    staticDecimalcount = paramInt2;
    staticGapformat = paramInt1;
  }

  public static int getStaticGapFormat()
  {
    return staticGapformat;
  }

  public static int getStaticDecimalCount()
  {
    return staticDecimalcount;
  }

  public JSFunction(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    this.acro = paramAcroRenderer;
    this.formObject = paramFormObject;
  }

  public static void debug(String paramString)
  {
  }

  protected static String applyRegexp(String paramString, String[] paramArrayOfString)
  {
    String str = "";
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      Pattern localPattern = Pattern.compile(paramArrayOfString[j]);
      Matcher localMatcher = localPattern.matcher(paramString);
      if (localMatcher.matches())
      {
        j = i;
        int k = localMatcher.start();
        int m = localMatcher.end();
        str = paramString.substring(k, m);
      }
    }
    return str;
  }

  String processArray(String paramString, int paramInt)
  {
    float f1 = 0.0F;
    int i = 1;
    int j = 0;
    int k = 0;
    String[] arrayOfString = convertToArray(paramString);
    float f2 = arrayOfString.length;
    for (int m = 1; m < f2; m++)
    {
      String str1 = null;
      String str2 = arrayOfString[m].replaceAll("\"", "");
      Object[] arrayOfObject = this.acro.getFormComponents(str2, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
      if (arrayOfObject.length > 0)
      {
        FormObject localFormObject = (FormObject)arrayOfObject[0];
        str1 = localFormObject.getValue();
      }
      if ((str1 == null) || (str1.isEmpty()))
        str1 = "0";
      k = 1;
      boolean bool = str1.startsWith("-");
      if (this.DECIMAL_IS_COMMA)
      {
        str1 = str1.replaceAll("\\.", "");
        str1 = str1.replaceAll(",", ".");
      }
      else if ((str1.indexOf(44) != -1) && (!str1.contains(".")))
      {
        str1 = str1.replace(',', '.');
      }
      else
      {
        str1 = str1.replaceAll(",", "");
      }
      if (str1.indexOf(46) != -1)
        j = 1;
      float f3;
      if (bool)
        f3 = -Float.parseFloat(str1.substring(1));
      else
        f3 = Float.parseFloat(str1);
      switch (paramInt)
      {
      case 1:
        f1 += f3;
        break;
      case 2:
        f1 += f3;
        break;
      case 3:
        if (i != 0)
        {
          f1 = 1.0F;
          i = 0;
        }
        f1 *= f3;
        break;
      case 4:
        if (m == 1)
          f1 = f3;
        else if (f3 < f1)
          f1 = f3;
        break;
      case 5:
        if (m == 1)
          f1 = f3;
        else if (f3 > f1)
          f1 = f3;
        break;
      default:
        debug(new StringBuilder().append("Unsupported op ").append(paramInt).append(" in processArray").toString());
      }
    }
    if (paramInt == 1)
      f1 /= (f2 - 1.0F);
    if (j != 0)
      return String.valueOf(f1);
    if (k == 0)
      return "";
    return String.valueOf((int)f1);
  }

  public static String[] convertToArray(String paramString)
  {
    String str1 = paramString;
    int i = paramString.indexOf(40);
    int j = 0;
    int k = 0;
    String str2 = "";
    ArrayList localArrayList = new ArrayList();
    if (i != -1)
    {
      localObject = paramString.substring(0, i);
      localArrayList.add(localObject);
      j++;
      paramString = paramString.substring(i, paramString.length()).trim();
      int m = 1;
      if (paramString.endsWith(";"))
        m++;
      if (paramString.startsWith("("))
        paramString = paramString.substring(1, paramString.length() - m);
      else
        debug(new StringBuilder().append("Unknown args in ").append(str1).toString());
    }
    Object localObject = new StringTokenizer(paramString, "(,);", true);
    while (((StringTokenizer)localObject).hasMoreTokens())
    {
      for (String str3 = ((StringTokenizer)localObject).nextToken(); (((StringTokenizer)localObject).hasMoreTokens()) && (str3.startsWith("\"")) && (!str3.endsWith("\"")); str3 = new StringBuilder().append(str3).append(((StringTokenizer)localObject).nextToken()).toString());
      if ((k == 0) && (str3.equals(",")))
      {
        localArrayList.add(str2);
        str2 = "";
        j++;
      }
      else
      {
        if (str3.equals("("))
          k++;
        else if (str3.equals(")"))
          k--;
        str2 = new StringBuilder().append(str2).append(str3).toString();
      }
    }
    j++;
    localArrayList.add(str2);
    String[] arrayOfString = new String[j];
    for (int n = 0; n < j; n++)
      arrayOfString[n] = ((String)localArrayList.get(n)).trim();
    return arrayOfString;
  }

  private static String padString(String paramString, int paramInt)
  {
    int i = paramString.length();
    if (paramInt == i)
      return paramString;
    if (paramInt < i)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramInt - i;
    for (int k = 0; k < j; k++)
      localStringBuilder.append('0');
    localStringBuilder.append(paramString);
    return localStringBuilder.toString();
  }

  void maskAlert(int paramInt, Object[] paramArrayOfObject)
  {
    String str = this.formObject.getLastValidValue();
    if (str == null)
      str = "";
    this.formObject.setLastValidValue(str);
    this.formObject.updateValue(str, false, true);
    if (((String)paramArrayOfObject[0]).contains(" R"))
      paramArrayOfObject[0] = this.formObject.getTextStreamValue(36);
    reportError(paramInt, paramArrayOfObject);
  }

  String validateMask(String[] paramArrayOfString, String paramString, boolean paramBoolean)
  {
    String[] arrayOfString = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    int[] arrayOfInt = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    int i = 1;
    int j = 0;
    int k = 0;
    String str1 = null;
    int m = paramArrayOfString.length;
    if (m != 2)
    {
      String str2 = "";
      for (int n = 0; n < m; n++)
        if (n == 0)
          str2 = paramArrayOfString[n];
        else
          str2 = new StringBuilder().append(str2).append(',').append(paramArrayOfString[n]).toString();
      debug(new StringBuilder().append("Unexpected values items=").append(m).append('{').append(str2).append('}').toString());
    }
    else
    {
      boolean bool = true;
      String str3 = (String)this.formObject.getFormValue();
      if ((str3 == null) || (str3.isEmpty()))
        return "";
      int i1 = str3.lastIndexOf(32);
      if (i1 != -1)
      {
        String str4 = str3.substring(i1 + 1).toLowerCase().trim();
        if ((str4 != null) && ((str4.equals("am")) || (str4.equals("pm"))))
          str3 = str3.substring(0, i1);
      }
      String str5 = stripQuotes(paramArrayOfString[1]);
      int i2 = str5.indexOf(100);
      if (str5.charAt(i2 + 1) != 'd')
        str5 = str5.replaceFirst("d", "dd");
      StringTokenizer localStringTokenizer1 = new StringTokenizer(str5, paramString, true);
      StringTokenizer localStringTokenizer2 = new StringTokenizer(str3, paramString, true);
      StringBuilder localStringBuilder = new StringBuilder();
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      while (localStringTokenizer1.hasMoreTokens())
      {
        Object localObject = "";
        String str6;
        while (true)
        {
          str6 = localStringTokenizer1.nextToken();
          if ((!paramString.contains(str6)) || (!localStringTokenizer1.hasMoreTokens()))
            break;
          localStringBuilder.append(str6);
        }
        String str7;
        while (true)
        {
          if (!localStringTokenizer2.hasMoreTokens())
            str7 = null;
          else
            str7 = localStringTokenizer2.nextToken();
          if ((str7 != null) && (paramString.contains(str7)))
            if (!localStringTokenizer2.hasMoreTokens())
              break;
        }
        String str8;
        if (localStringTokenizer1.hasMoreTokens())
          str8 = localStringTokenizer1.nextToken();
        else
          str8 = null;
        if (str7 != null)
          localObject = padString(str7, str6.length());
        if (str6.equals("h"))
        {
          if ((paramBoolean) && (str7 == null))
            localObject = String.valueOf(localGregorianCalendar.get(10));
          else
            localObject = padString(str7, 2);
          bool = verifyNumberInRange((String)localObject, 0, 11);
        }
        else if (str6.equals("HH"))
        {
          if ((paramBoolean) && (str7 == null))
          {
            localObject = String.valueOf(localGregorianCalendar.get(11));
            localObject = padString((String)localObject, 2);
            bool = verifyNumberInRange((String)localObject, 0, 23);
          }
          else
          {
            bool = verifyNumberInRange((String)localObject, 0, 23);
          }
        }
        else if (str6.equals("MM"))
        {
          if ((paramBoolean) && (str7 == null))
          {
            localObject = String.valueOf(localGregorianCalendar.get(12));
            localObject = padString((String)localObject, 2);
            bool = verifyNumberInRange((String)localObject, 0, 59);
          }
          else
          {
            bool = verifyNumberInRange((String)localObject, 0, 59);
          }
        }
        else
        {
          int i3;
          int i4;
          if ((str6.equals("mm")) || (str6.equals("m")))
          {
            bool = verifyNumberInRange((String)localObject, 0, 12);
            if (bool)
            {
              i3 = Integer.parseInt((String)localObject);
              if ((((String)localObject).length() != str6.length()) && (str6.length() == 1))
                localObject = String.valueOf(i3);
              i4 = i3 - 1;
              if ((i4 == 1) && (i > 0))
                i -= 1;
            }
          }
          else if (str6.equals("tt"))
          {
            if ((paramBoolean) && (str7 == null))
              localObject = "am";
            bool = (((String)localObject).toLowerCase().equals("am")) || (((String)localObject).toLowerCase().equals("pm"));
          }
          else if (str6.equals("ss"))
          {
            if ((paramBoolean) && (str7 == null))
            {
              localObject = String.valueOf(localGregorianCalendar.get(13));
              localObject = padString((String)localObject, 2);
              bool = verifyNumberInRange((String)localObject, 0, 59);
            }
            else
            {
              bool = verifyNumberInRange((String)localObject, 0, 59);
            }
          }
          else if ((str6.equals("dd")) || (str6.equals("d")))
          {
            bool = verifyNumberInRange((String)localObject, 0, 31);
            if (bool)
              k = Integer.parseInt((String)localObject);
          }
          else if ((str6.equals("yyyy")) || (str6.equals("yy")))
          {
            if ((paramBoolean) && (str7 == null))
            {
              str7 = String.valueOf(localGregorianCalendar.get(1));
              bool = verifyNumberInRange(str7, 0, 9999);
            }
            else if (str6.length() != str7.length())
            {
              if (str6.length() > str7.length())
              {
                bool = false;
              }
              else if (str7.length() == 4)
              {
                bool = verifyNumberInRange(str7, 0, 9999);
                str7 = str7.substring(2);
              }
            }
            else
            {
              bool = verifyNumberInRange(str7, 0, 9999);
            }
            if ((bool) && (Integer.parseInt(str7) % 4 != 0) && (i > 0))
              i -= 1;
            localObject = str7;
          }
          else if ((str6.equals("mmm")) || (str6.equals("mmmm")))
          {
            i3 = -1;
            if (str7.length() >= 3)
              for (i4 = 0; i4 != arrayOfString.length; i4++)
              {
                str7 = str7.toLowerCase();
                int i5 = 3;
                str7 = str7.substring(0, i5).toLowerCase();
                String str9 = arrayOfString[i4].substring(0, i5).toLowerCase();
                if (str7.equals(str9))
                  i3 = i4;
              }
            if (i3 == -1)
              try
              {
                i3 = Integer.parseInt(str7) - 1;
                if (i3 < 12)
                  localObject = arrayOfString[i3];
              }
              catch (Exception localException)
              {
                localObject = null;
                bool = false;
              }
            else
              localObject = arrayOfString[i3];
            if ((i3 != 1) && (i > 0))
              i -= 1;
            if (i3 > 11)
              bool = false;
            else
              j = i3;
          }
          else
          {
            debug(new StringBuilder().append("Mask value >").append(str6).append("< not implemented").toString());
            bool = false;
          }
        }
        if (!bool)
          break;
        localStringBuilder.append((String)localObject);
        if (str8 != null)
          localStringBuilder.append(str8);
      }
      if ((j < 0) || (j > arrayOfInt.length) || (k > arrayOfInt[j] + i))
        bool = false;
      if (bool)
        str1 = localStringBuilder.toString();
    }
    return str1;
  }

  private static boolean verifyNumberInRange(String paramString, int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if ((paramString == null) || (isNotNumber(paramString)))
    {
      bool = false;
    }
    else
    {
      int i = Integer.parseInt(paramString);
      if ((i < paramInt1) || (i > paramInt2))
        bool = false;
    }
    return bool;
  }

  protected static String stripQuotes(String paramString)
  {
    if (paramString.startsWith("\""))
      paramString = paramString.substring(1, paramString.length() - 1);
    String str;
    if (paramString.startsWith("\\u"))
    {
      str = paramString.substring(2);
      paramString = String.valueOf((char)Integer.parseInt(str, 16));
    }
    else if (paramString.startsWith("\\"))
    {
      str = paramString.substring(1);
      paramString = String.valueOf((char)Integer.parseInt(str, 8));
    }
    return paramString;
  }

  protected static boolean isNotNumber(String paramString)
  {
    if (paramString.isEmpty())
      return true;
    boolean bool = false;
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length;
    for (int j = 0; j < i; j++)
      if ((arrayOfChar[j] != '.') && (arrayOfChar[j] != '-') && (arrayOfChar[j] != ',') && ((arrayOfChar[j] < '0') || (arrayOfChar[j] > '9')))
      {
        j = i;
        bool = true;
      }
    return bool;
  }

  public String getValue()
  {
    return this.value;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    return 0;
  }

  public String parseJSvariables(String paramString)
  {
    int i = paramString.indexOf("this.getField(");
    int m;
    if (i != -1)
    {
      int j = i + "this.getField(".length();
      int k = paramString.indexOf(41, j);
      String str1 = paramString.substring(j, k);
      if (str1.startsWith("\""))
        str1 = str1.substring(1, str1.length() - 1);
      m = -1;
      if (paramString.indexOf(".value", k + 1) != -1)
      {
        m = 1;
        k = paramString.indexOf(".value", k + 1) + ".value".length();
      }
      FormObject localFormObject = this.acro.getFormObject(str1);
      switch (m)
      {
      case 1:
        paramString = new StringBuilder().append(paramString.substring(0, i)).append(localFormObject.getValue()).append(paramString.substring(k)).toString();
        break;
      }
    }
    if (!StringUtils.isNumber(paramString))
      for (m = 0; m < paramString.length(); m++)
        switch (paramString.charAt(m))
        {
        case '%':
        case '*':
        case '+':
        case '-':
        case '/':
          double d1 = Double.parseDouble(paramString.substring(0, m));
          String str2 = getNextNum(paramString, m + 1);
          double d2 = Double.parseDouble(str2);
          double d3;
          switch (paramString.charAt(m))
          {
          case '*':
            d3 = d1 * d2;
            break;
          case '/':
            d3 = d1 / d2;
            break;
          case '-':
            d3 = d1 - d2;
            break;
          case '%':
            d3 = d1 % d2;
            break;
          default:
            d3 = d1 + d2;
          }
          paramString = new StringBuilder().append(d3).append(paramString.substring(m + 1 + str2.length())).toString();
          if (StringUtils.isNumber(paramString))
            break label432;
        case '&':
        case '\'':
        case '(':
        case ')':
        case ',':
        case '.':
        }
    label432: return paramString;
  }

  private static String getNextNum(String paramString, int paramInt)
  {
    int i = -1;
    for (int j = paramInt; j < paramString.length(); j++)
      switch (paramString.charAt(j))
      {
      case '.':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        break;
      case '/':
      default:
        i = j;
        break label94;
      }
    label94: if (i == -1)
      i = paramString.length();
    return paramString.substring(paramInt, i);
  }

  private void reportError(int paramInt, Object[] paramArrayOfObject)
  {
    int i = 0;
    if (i == 0)
    {
      if (!DecoderOptions.showErrorMessages)
        return;
      if (paramInt == 1)
        JOptionPane.showMessageDialog(null, new StringBuilder().append("The values entered does not match the format of the field [").append(paramArrayOfObject[0]).append(" ]").toString(), "Warning: Javascript Window", 1);
      else if (paramInt == 2)
        JOptionPane.showMessageDialog(null, new StringBuilder().append("Invalid date/time: please ensure that the date/time exists. Field [").append(paramArrayOfObject[0]).append(" ] should match format ").append(paramArrayOfObject[1]).toString(), "Warning: Javascript Window", 1);
      else if (paramInt == 3)
        JOptionPane.showMessageDialog(null, paramArrayOfObject[1], "Warning: Javascript Window", 1);
      else
        JOptionPane.showMessageDialog(null, "The values entered does not match the format of the field", "Warning: Javascript Window", 1);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.JSFunction
 * JD-Core Version:    0.6.2
 */
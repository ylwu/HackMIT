package org.jpedal.objects.javascript.defaultactions;

import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.utils.NumberUtils;
import org.jpedal.utils.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

public class JpedalDefaultJavascript
{
  private static final boolean DebugDefaultJavascript = false;
  private static final String[] format = { "'D':yyyyMMddHHmmssZ", "'D':yyyyMMddHHmmssZ'Z", "yyyy/MM/dd HH:mm:ss" };
  private Scriptable scope;
  private Context context;
  public static final String viewerType = "Exchange-Pro";
  public static final int viewerVersion = 10;
  public static int calculate = 1;
  public boolean runtimeHighlight = false;

  public JpedalDefaultJavascript(Scriptable paramScriptable, Context paramContext)
  {
    this.scope = paramScriptable;
    this.context = paramContext;
  }

  public Object printd(String paramString, Scriptable paramScriptable)
  {
    Date localDate = null;
    if (NativeJavaObject.canConvert(paramScriptable, Date.class))
      localDate = (Date)Context.jsToJava(paramScriptable, Date.class);
    if (localDate == null)
      Context.throwAsScriptRuntimeEx(new RuntimeException("Not a Date()"));
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(localDate);
    char[] arrayOfChar = paramString.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < arrayOfChar.length; i++)
      if (arrayOfChar[i] == 'm')
      {
        localStringBuffer.append('M');
      }
      else if (arrayOfChar[i] == 't')
      {
        if (arrayOfChar[(i + 1)] == 't')
        {
          localStringBuffer.append('a');
          i++;
        }
        else if (localCalendar.get(11) > 12)
        {
          localStringBuffer.append("'P'");
        }
        else
        {
          localStringBuffer.append("'A'");
        }
      }
      else if (arrayOfChar[i] == 'M')
      {
        localStringBuffer.append('m');
      }
      else if ((arrayOfChar[i] == 'd') && (arrayOfChar[(i + 1)] == 'd') && (arrayOfChar[(i + 2)] == 'd'))
      {
        if (arrayOfChar[(i + 3)] == 'd')
        {
          localStringBuffer.append("EEEE");
          i += 3;
        }
        else
        {
          localStringBuffer.append("EEE");
          i += 2;
        }
      }
      else if (arrayOfChar[i] == '\\')
      {
        localStringBuffer.append('\'');
        localStringBuffer.append(arrayOfChar[(++i)]);
        localStringBuffer.append('\'');
      }
      else
      {
        localStringBuffer.append(arrayOfChar[i]);
      }
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(new String(localStringBuffer));
    String str = localSimpleDateFormat.format(localCalendar.getTime());
    return this.context.newObject(this.scope, "String", new Object[] { str });
  }

  public Object printd(int paramInt, Scriptable paramScriptable)
  {
    Calendar localCalendar = null;
    if (NativeJavaObject.canConvert(paramScriptable, Calendar.class))
      localCalendar = (Calendar)Context.jsToJava(paramScriptable, Calendar.class);
    if (localCalendar == null)
      Context.throwAsScriptRuntimeEx(new RuntimeException("Not a Date()"));
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format[paramInt]);
    return this.context.newObject(this.scope, "String", new Object[] { localSimpleDateFormat.format(localCalendar) });
  }

  public Object printf(String paramString, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = 0;
    for (int k = paramString.indexOf("%"); k != -1; k = paramString.indexOf("%", k + 1))
    {
      localStringBuilder.append(paramString.substring(j, k));
      char[] arrayOfChar = { 'd', 'f', 's', 'x' };
      int n = 0;
      j = paramString.indexOf(arrayOfChar[(n++)], k);
      while (n < arrayOfChar.length)
      {
        int m = paramString.indexOf(arrayOfChar[n], k);
        if ((j == -1) || ((m != -1) && (m < j)))
          j = m;
        n++;
      }
      j++;
      String str2 = paramString.substring(k, j);
      String str1 = paramArrayOfString[(i++)];
      int i1 = str1.indexOf(44);
      int i2 = str1.indexOf(46);
      if ((i1 != -1) && (i2 != -1) && (i1 < i2))
      {
        localObject = new StringBuilder();
        for (int i3 = 0; i3 < str1.length(); i3++)
        {
          char c = str1.charAt(i3);
          if (c != ',')
            ((StringBuilder)localObject).append(c);
        }
        str1 = ((StringBuilder)localObject).toString();
      }
      Object localObject = convertToken(str2, str1);
      localStringBuilder.append((String)localObject);
    }
    if (j < paramString.length())
      localStringBuilder.append(paramString.substring(j));
    return this.context.newObject(this.scope, "String", new Object[] { localStringBuilder.toString() });
  }

  private static String convertToken(String paramString1, String paramString2)
  {
    if (!StringUtils.isNumber(paramString2))
      return "";
    byte[] arrayOfByte = StringUtils.toBytes(paramString2);
    double d = NumberUtils.parseDouble(0, arrayOfByte.length, arrayOfByte);
    int i = -1;
    int j = 0;
    char c = '.';
    int k = 0;
    int m = 0;
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    char[] arrayOfChar = paramString1.toCharArray();
    if (arrayOfChar[0] == '%')
    {
      int n = 1;
      int i1 = arrayOfChar.length;
      while (n < i1)
      {
        int i4;
        switch (arrayOfChar[n])
        {
        case ',':
          switch (arrayOfChar[(++n)])
          {
          case '0':
            c = '.';
            break;
          case '1':
            c = '.';
            break;
          case '2':
            c = ',';
            break;
          case '3':
            c = ',';
          }
          break;
        case '+':
          if (d > 0.0D)
            localStringBuilder2.append('+');
          else
            localStringBuilder2.append('-');
          break;
        case ' ':
          if (d > 0.0D)
            localStringBuilder2.append(' ');
          else
            localStringBuilder2.append('-');
          break;
        case '0':
          k = 1;
          break;
        case '#':
          m = 1;
          break;
        case '.':
          i = Integer.parseInt(String.valueOf(arrayOfChar[(++n)]));
          break;
        case 'd':
          localStringBuilder1.append((int)d);
          if (k != 0)
          {
            int i2 = localStringBuilder2.length() + localStringBuilder1.length();
            if (i2 < j)
              for (i4 = 0; i4 < j - i2; i4++)
                localStringBuilder2.append('0');
          }
          localStringBuilder2.append(localStringBuilder1.toString());
          break;
        case 'f':
          if (i != -1)
          {
            if (i == 0)
            {
              localStringBuilder1.append((int)d);
            }
            else
            {
              NumberFormat localNumberFormat = NumberFormat.getInstance();
              localNumberFormat.setMinimumFractionDigits(i);
              localNumberFormat.setMaximumFractionDigits(i);
              localStringBuilder1.append(localNumberFormat.format(d));
            }
          }
          else
            localStringBuilder1.append((float)d);
          if ((m != 0) && (localStringBuilder1.indexOf(".") != -1))
            localStringBuilder1.append('.');
          if (k != 0)
          {
            int i3 = localStringBuilder2.length() + localStringBuilder1.length();
            if (i3 < j)
              for (i4 = 0; i4 < j - i3; i4++)
                localStringBuilder2.append('0');
          }
          String str1 = localStringBuilder1.toString();
          str1 = str1.replace('.', c);
          localStringBuilder2.append(str1);
          break;
        case 's':
          localStringBuilder1.append(d);
          if (k != 0)
          {
            i4 = localStringBuilder2.length() + localStringBuilder1.length();
            if (i4 < j)
              for (int i5 = 0; i5 < j - i4; i5++)
                localStringBuilder2.append('0');
          }
          localStringBuilder2.append(localStringBuilder1);
          break;
        case 'x':
          i4 = (int)d;
          String str2 = Integer.toHexString(i4);
          localStringBuilder1.append(str2);
          if (k != 0)
          {
            int i6 = localStringBuilder2.length() + localStringBuilder1.length();
            if (i6 < j)
              for (int i7 = 0; i7 < j - i6; i7++)
                localStringBuilder2.append('0');
          }
          localStringBuilder2.append(localStringBuilder1);
          break;
        default:
          j = NumberUtils.parseInt(0, 1, new byte[] { (byte)arrayOfChar[n] });
        }
        n++;
      }
    }
    return localStringBuilder2.toString();
  }

  public static Map crackURL(String paramString)
  {
    if ((!paramString.startsWith("file")) && (!paramString.startsWith("http")) && (!paramString.startsWith("https")))
      return null;
    HashMap localHashMap = new HashMap();
    int i = paramString.indexOf("://");
    localHashMap.put("cScheme", paramString.substring(0, i));
    int j;
    int k;
    if (paramString.contains("@"))
    {
      j = paramString.indexOf(58, i + 3);
      k = paramString.indexOf(64);
      int m = paramString.indexOf(47, k + 1);
      localHashMap.put("cHost", paramString.substring(k + 1, m));
      localHashMap.put("cUser", paramString.substring(i + 3, j));
      localHashMap.put("cPassword", paramString.substring(j + 1, k));
      localHashMap.put("cPath", paramString.substring(m + 1));
    }
    else
    {
      j = paramString.indexOf(58, i);
      k = paramString.indexOf(47, j);
      localHashMap.put("cHost", paramString.substring(i + 3, j));
      localHashMap.put("nPort", paramString.substring(j + 1, k));
      localHashMap.put("cPath", paramString.substring(k + 1));
    }
    return localHashMap;
  }

  public static double z(String paramString, double paramDouble)
  {
    double d1 = paramDouble * 100.0D;
    double d2 = Math.round(d1);
    double d3 = d2 / 100.0D;
    return d3;
  }

  public static void beep(int paramInt)
  {
    Toolkit.getDefaultToolkit().beep();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.defaultactions.JpedalDefaultJavascript
 * JD-Core Version:    0.6.2
 */
package org.jpedal.utils;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Messages
{
  private static Map messages = null;
  private static Map reportedValueMissing = new HashMap();
  protected static ResourceBundle bundle = null;
  private static boolean isInitialised = false;

  public static void setBundle(ResourceBundle paramResourceBundle)
  {
    bundle = paramResourceBundle;
    if (!isInitialised)
      init();
  }

  public static String getMessage(String paramString)
  {
    String str = null;
    try
    {
      str = (String)messages.get(paramString);
      if (str == null)
        str = bundle.getString(paramString);
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
    }
    if (str == null)
      try
      {
        str = (String)messages.get(paramString);
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
      }
    if (str == null)
      str = paramString;
    if (str.isEmpty())
      str = new StringBuilder().append(paramString).append("<<").toString();
    return str;
  }

  private static void init()
  {
    isInitialised = true;
    Object localObject = null;
    try
    {
      messages = new HashMap();
      if (localObject == null)
        LogWriter.writeLog("Unable to open messages.properties from jar");
      Enumeration localEnumeration = bundle.getKeys();
      while (localEnumeration.hasMoreElements())
      {
        String str2 = (String)localEnumeration.nextElement();
        String str1 = (String)bundle.getObject(str2);
        if (str1 == null)
          break;
        StringBuilder localStringBuilder = new StringBuilder();
        StringTokenizer localStringTokenizer = new StringTokenizer(str1, "\\&;", true);
        int i = 0;
        while (localStringTokenizer.hasMoreTokens())
        {
          String str3;
          if (i != 0)
          {
            str3 = "&";
            i = 0;
          }
          else
          {
            str3 = localStringTokenizer.nextToken();
          }
          String str4;
          if ((localStringTokenizer.hasMoreTokens()) && (str3.equals("\\")))
          {
            str4 = localStringTokenizer.nextToken();
            int j = str4.charAt(0);
            if (j == 110)
              localStringBuilder.append('\n');
            else if (j == 32)
              localStringBuilder.append(' ');
            localStringBuilder.append(str4.substring(1));
          }
          else if ((localStringTokenizer.hasMoreTokens()) && (str3.equals("&")))
          {
            str4 = localStringTokenizer.nextToken();
            if (localStringTokenizer.hasMoreTokens())
            {
              String str5 = localStringTokenizer.nextToken();
              if (str5.equals("&"))
              {
                localStringBuilder.append('&');
                localStringBuilder.append(str4);
                i = 1;
              }
              else if (str5.equals(";"))
              {
                if (str4.startsWith("#"))
                  str4 = str4.substring(1);
                char c = (char)Integer.parseInt(str4);
                localStringBuilder.append(c);
              }
              else
              {
                if (localStringTokenizer.hasMoreTokens())
                  localStringBuilder.append('&');
                localStringBuilder.append(str4);
              }
            }
            else
            {
              localStringBuilder.append('&');
              localStringBuilder.append(str4);
            }
          }
          else
          {
            localStringBuilder.append(str3);
          }
        }
        messages.put(str2, localStringBuilder.toString());
      }
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException1).append(" loading resource bundle.\n").append("Also check you have a file in org.jpedal.international.messages to support Locale=").append(Locale.getDefault()).toString());
      System.err.println(new StringBuilder().append("Exception loading resource bundle.\nAlso check you have a file in org.jpedal.international.messages to support Locale=").append(Locale.getDefault()).toString());
    }
    if (localObject != null)
      try
      {
        localObject.close();
      }
      catch (Exception localException2)
      {
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" reading lookup table for pdf  for abobe map").toString());
      }
  }

  public static void dispose()
  {
    messages = null;
    reportedValueMissing = null;
    bundle = null;
    isInitialised = false;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.Messages
 * JD-Core Version:    0.6.2
 */
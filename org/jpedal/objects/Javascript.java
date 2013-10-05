package org.jpedal.objects;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.javascript.DefaultParser;
import org.jpedal.objects.javascript.ExpressionEngine;
import org.jpedal.objects.javascript.GenericParser;
import org.jpedal.objects.javascript.RhinoParser;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_String;

public class Javascript
{
  public static final boolean debugActionHandler = false;
  private ExpressionEngine jsParser;
  private static boolean useNewJSParser = false;
  private static boolean disableJavascript = false;
  private Map javascriptCommands = new HashMap();
  private Map javascriptTypesUsed = new HashMap();
  private Map linkedjavascriptCommands = new HashMap();
  private Map javascriptNamesObjects = new HashMap();

  public Javascript(ExpressionEngine paramExpressionEngine, AcroRenderer paramAcroRenderer)
  {
    if (disableJavascript)
      return;
    if (System.getProperty("org.jpedal.newJS") != null)
      useNewJSParser = true;
    if (paramExpressionEngine != null)
      this.jsParser = paramExpressionEngine;
    else
      try
      {
        if (!useNewJSParser)
        {
          InputStream localInputStream = DefaultParser.class.getClassLoader().getResourceAsStream("org/mozilla/javascript/Context.class");
          if (localInputStream != null)
            this.jsParser = new RhinoParser(this);
          else
            this.jsParser = new DefaultParser();
        }
        else
        {
          this.jsParser = new GenericParser(this);
        }
      }
      catch (Error localError)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError.getMessage());
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    this.jsParser.setAcroRenderer(paramAcroRenderer);
  }

  public void executeAction(String paramString)
  {
    if (disableJavascript)
      return;
    this.jsParser.executeFunctions(paramString, null);
  }

  public int execute(FormObject paramFormObject, int paramInt1, int paramInt2, char paramChar)
  {
    int i = executeCommand(paramFormObject, paramInt1, paramInt2, paramChar);
    int j = 0;
    if ((paramInt2 == 6) && ((paramInt1 != 4866) || ((paramInt1 == 4866) && ((i == 0) || (i == 3)))))
      j = 1;
    if (j != 0)
    {
      String str1 = paramFormObject.getTextStreamValue(36);
      Vector_String localVector_String = (Vector_String)this.linkedjavascriptCommands.get(str1);
      if (localVector_String != null)
      {
        localVector_String.trim();
        String[] arrayOfString1 = localVector_String.get();
        for (String str2 : arrayOfString1)
          if ((!str2.equals(str1)) || (paramInt1 == 4866));
      }
    }
    return i;
  }

  public Object getJavascriptCommand(String paramString, int paramInt)
  {
    return this.javascriptCommands.get(paramString + '-' + paramInt);
  }

  private int executeCommand(FormObject paramFormObject, int paramInt1, int paramInt2, char paramChar)
  {
    int i = 0;
    if (disableJavascript)
      return i;
    if (paramFormObject == null)
      return i;
    Object localObject = this.javascriptCommands.get(paramFormObject.getObjectRefAsString() + '-' + paramInt1);
    if (localObject == null)
      localObject = this.javascriptCommands.get(paramFormObject.getTextStreamValue(36) + '-' + paramInt1);
    if (localObject == null)
      return 0;
    if (i != 2)
      i = this.jsParser.execute(paramFormObject, paramInt1, localObject, paramInt2, paramChar);
    return i;
  }

  public void setCode(String paramString1, String paramString2)
  {
    if (disableJavascript)
      return;
    this.javascriptNamesObjects.put(paramString1, paramString2);
    this.jsParser.addCode(paramString2);
  }

  public String getJavaScript(String paramString)
  {
    String str = "";
    if (paramString == null)
    {
      Collection localCollection = this.javascriptNamesObjects.values();
      Iterator localIterator = localCollection.iterator();
      while (localIterator.hasNext())
        str = str + localIterator.next();
    }
    else
    {
      str = (String)this.javascriptNamesObjects.get(paramString);
    }
    return str;
  }

  public void closeFile()
  {
    this.javascriptTypesUsed.clear();
    this.javascriptCommands.clear();
    this.linkedjavascriptCommands.clear();
    if (disableJavascript)
      return;
    this.jsParser.closeFile();
  }

  public void storeJavascript(String paramString1, String paramString2, int paramInt)
  {
    this.javascriptCommands.put(paramString1 + '-' + paramInt, paramString2);
    this.javascriptTypesUsed.put(Integer.valueOf(paramInt), "x");
    if (paramInt == 4866)
      for (int i = 0; ; i++)
      {
        int k = paramString2.indexOf("\\\"", i);
        while (true)
        {
          i = paramString2.indexOf(34, i);
          if ((i != -1) && (k != -1))
            if (i - 1 > k)
              break;
        }
        if (i == -1)
          break;
        i++;
        int j = i;
        k = paramString2.indexOf("\\\"", i);
        while (true)
        {
          i = paramString2.indexOf(34, i);
          if ((i != -1) && (k != -1))
            if (i - 1 > k)
              break;
        }
        if (i == -1)
          break;
        String str = paramString2.substring(j, i);
        if (str != null)
        {
          Vector_String localVector_String = (Vector_String)this.linkedjavascriptCommands.get(str);
          if (localVector_String == null)
          {
            localVector_String = new Vector_String();
            localVector_String.addElement(paramString1);
          }
          else if (!localVector_String.contains(paramString1))
          {
            localVector_String.addElement(paramString1);
          }
          this.linkedjavascriptCommands.put(str, localVector_String);
        }
      }
  }

  public void dispose()
  {
    if (disableJavascript)
      return;
    this.jsParser.dispose();
  }

  public static void disableJavascript()
  {
    disableJavascript = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.Javascript
 * JD-Core Version:    0.6.2
 */
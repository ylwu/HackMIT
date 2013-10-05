package org.jpedal.objects.javascript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.SwingUtilities;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.javascript.jsobjects.JSConsole;
import org.jpedal.objects.javascript.jsobjects.JSDoc;
import org.jpedal.objects.javascript.jsobjects.JSField;
import org.jpedal.objects.raw.FormObject;

public class GenericParser
  implements ExpressionEngine
{
  private static final String[] engineOrder = { "nashorn", "rhino" };
  private AcroRenderer acroRenderer = null;
  private ScriptEngine engine = null;
  private ScriptContext context = null;
  private JSDoc docObject = null;
  private Javascript jsObject = null;
  private static final boolean debugEngine = false;
  private static ArrayList<String> erroredCode = null;

  public GenericParser(Javascript paramJavascript)
    throws Exception
  {
    this.jsObject = paramJavascript;
    ScriptEngineManager localScriptEngineManager = new ScriptEngineManager();
    for (int i = 0; (this.engine == null) && (i < engineOrder.length); i++)
      this.engine = localScriptEngineManager.getEngineByName(engineOrder[i]);
    if (this.engine == null)
      throw new Exception("Could not load a suitable ScriptEngine for parsing JavaScript, are you using a fully fledged JVM?");
    this.context = this.engine.getContext();
    setupPDFObjects(paramJavascript);
  }

  private void setupPDFObjects(Javascript paramJavascript)
  {
    try
    {
      this.docObject = new JSDoc(this.acroRenderer);
      this.context.setAttribute("JSDoc", this.docObject, 100);
      this.context.setAttribute("app", new JSApp(), 100);
      this.context.setAttribute("JSConsole", new JSConsole(), 100);
      BufferedReader localBufferedReader1 = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/org/jpedal/objects/javascript/jsobjects/JSObjects.js")));
      this.engine.eval(localBufferedReader1);
      this.engine.eval("var event = new Event();");
      BufferedReader localBufferedReader2 = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/org/jpedal/objects/javascript/aform.js")));
      this.engine.eval(localBufferedReader2);
      String str = preParseJS(paramJavascript.getJavaScript(null), true);
      if ((str != null) && (str.length() > 0))
        try
        {
          this.engine.eval(str);
        }
        catch (ScriptException localScriptException2)
        {
          localScriptException2.printStackTrace();
        }
    }
    catch (ScriptException localScriptException1)
    {
      localScriptException1.printStackTrace();
    }
  }

  public int execute(FormObject paramFormObject, int paramInt1, Object paramObject, int paramInt2, char paramChar)
  {
    if (paramChar == 65535)
      return 0;
    if ((paramObject instanceof String))
    {
      paramObject = preParseJS((String)paramObject, false);
      try
      {
        this.engine.eval(new StringBuilder().append("var event = new Event(").append(paramInt1).append(");").toString());
        this.engine.eval(new StringBuilder().append("event.target = JSDoc.getFieldByRef('").append(paramFormObject.getObjectRefAsString()).append("');").toString());
        this.engine.eval(new StringBuilder().append("event.value = '").append(String.valueOf(paramFormObject.getValue())).append("';").toString());
      }
      catch (ScriptException localScriptException1)
      {
        Logger.getLogger(GenericParser.class.getName()).log(Level.SEVERE, null, localScriptException1);
      }
      Object localObject1 = null;
      try
      {
        localObject1 = this.engine.eval((String)paramObject, this.context);
        Object localObject2 = this.engine.eval("event.target");
        Object localObject3 = this.engine.eval("event.value");
        if ((localObject2 != null) && (paramInt2 == 6))
        {
          localObject4 = (JSField)localObject2;
          ((JSField)localObject4).value = localObject3;
          boolean bool = false;
          ((JSField)localObject4).syncToGUI(bool);
        }
        if (localObject1 != null);
        Object localObject4 = this.engine.eval("event");
        Object localObject5 = this.engine.eval("event.name");
        if ((localObject4 != null) && (localObject5 != null) && (localObject5.equals("Format")))
          calcualteEvent();
      }
      catch (ScriptException localScriptException2)
      {
        localScriptException2.printStackTrace();
      }
    }
    return 0;
  }

  public void closeFile()
  {
    flush();
  }

  public boolean reportError(int paramInt, Object[] paramArrayOfObject)
  {
    return false;
  }

  public int addCode(String paramString)
  {
    paramString = preParseJS(paramString, true);
    final String str = paramString;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        try
        {
          GenericParser.this.engine.eval(str, GenericParser.this.context);
        }
        catch (ScriptException localScriptException)
        {
          localScriptException.printStackTrace();
        }
      }
    };
    SwingUtilities.invokeLater(local1);
    return 0;
  }

  public void executeFunctions(String paramString, FormObject paramFormObject)
  {
  }

  public void dispose()
  {
  }

  public void setAcroRenderer(AcroRenderer paramAcroRenderer)
  {
    this.acroRenderer = paramAcroRenderer;
    this.docObject.setAcroRenderer(paramAcroRenderer);
  }

  private void flush()
  {
    this.docObject.flush();
  }

  private void calcualteEvent()
  {
    FormObject[] arrayOfFormObject1 = this.docObject.getFormObjects();
    for (FormObject localFormObject : arrayOfFormObject1)
    {
      String str1 = localFormObject.getObjectRefAsString();
      String str2 = localFormObject.getTextStreamValue(36);
      String str3 = (String)this.jsObject.getJavascriptCommand(str2 != null ? str2 : str1, 4866);
      if (str3 != null)
      {
        str3 = preParseJS(str3, false);
        try
        {
          this.engine.eval("var event = new Event(4866);", this.context);
          this.engine.eval(new StringBuilder().append("event.target = JSDoc.getFieldByRef('").append(str1).append("');").toString(), this.context);
          this.engine.eval(str3, this.context);
          JSField localJSField = (JSField)this.engine.eval("event.target", this.context);
          Boolean localBoolean = (Boolean)this.engine.eval("event.rc", this.context);
          if ((localJSField != null) && (localBoolean.booleanValue()))
          {
            Object localObject = this.engine.eval("event.value", this.context);
            if (localObject != null)
              localJSField.value = localObject.toString();
            else
              localJSField.value = null;
            boolean bool = false;
            localJSField.syncToGUI(bool);
          }
        }
        catch (ScriptException localScriptException)
        {
          localScriptException.printStackTrace();
        }
      }
    }
  }

  private String preParseJS(String paramString, boolean paramBoolean)
  {
    paramString = addMethodstoObject(makeGlobalVars(paramString));
    paramString = fixGetFields(paramString);
    if (paramBoolean)
      paramString = new StringBuilder().append("(function() {").append(paramString).append("}).call(Doc);").toString();
    return paramString;
  }

  private String fixGetFields(String paramString)
  {
    Pattern localPattern = Pattern.compile("[^.]getField\\(");
    Matcher localMatcher = localPattern.matcher(paramString);
    while (localMatcher.find())
    {
      String str = localMatcher.group();
      paramString = paramString.replace(str, new StringBuilder().append(str.charAt(0)).append("Doc.getField(").toString());
    }
    paramString = paramString.replace("this.getField(", "Doc.getField(");
    return paramString;
  }

  private String addMethodstoObject(String paramString)
  {
    Pattern localPattern = Pattern.compile("function\\s\\w+\\((\\w+)?\\)");
    Matcher localMatcher = localPattern.matcher(paramString);
    HashMap localHashMap = new HashMap();
    while (localMatcher.find())
    {
      str1 = localMatcher.group();
      localObject = paramString.substring(localMatcher.start() + 9, localMatcher.end());
      int i = ((String)localObject).indexOf(40);
      String str3 = ((String)localObject).substring(0, i);
      String str4 = new StringBuilder().append("this.").append(str3).append(" = ").append("function").append(((String)localObject).substring(i)).toString();
      localHashMap.put(str1, str4);
    }
    String str1 = paramString;
    Object localObject = localHashMap.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str2 = (String)((Iterator)localObject).next();
      str1 = str1.replace(str2, (CharSequence)localHashMap.get(str2));
    }
    return str1;
  }

  private String makeGlobalVars(String paramString)
  {
    String str1 = trimToGlobal(paramString);
    Pattern localPattern = Pattern.compile("var\\s\\w+\\s?=\\s?");
    Matcher localMatcher = localPattern.matcher(str1);
    HashMap localHashMap = new HashMap();
    while (localMatcher.find())
    {
      str2 = localMatcher.group();
      localObject = str1.substring(localMatcher.start() + 4, localMatcher.end());
      for (int i = 0; (((String)localObject).charAt(i) != ' ') && (((String)localObject).charAt(i) != '=') && (i < ((String)localObject).length()); i++);
      String str4 = ((String)localObject).substring(0, i);
      localHashMap.put(str2.substring(0, 4 + str4.length()), new StringBuilder().append("this.").append(str4).toString());
    }
    String str2 = paramString;
    Object localObject = localHashMap.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str3 = (String)((Iterator)localObject).next();
      str2 = str2.replace(str3, (CharSequence)localHashMap.get(str3));
    }
    return str2;
  }

  private String trimToGlobal(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    for (int n = 0; n < paramString.length(); n++)
    {
      char c = paramString.charAt(n);
      if (m == 0)
      {
        if (c == '"')
          m = 1;
        else if (k == 0)
        {
          if (c == '\'')
            k = 1;
          else if (i == 0)
          {
            if (c == '{')
              i = 1;
            else if (j == 0)
            {
              if (c == '(')
                j = 1;
              else if ((c != ')') && (c != '}'))
                localStringBuilder.append(c);
            }
            else if (c == ')')
              j = 0;
          }
          else if (c == '}')
            i = 0;
        }
        else if (c == '\'')
          k = 0;
      }
      else if (c == '"')
        m = 0;
    }
    paramString = localStringBuilder.toString();
    return paramString;
  }

  private String getActionHandlerAsString(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    case 1:
      str = "ActionHandler.MOUSEPRESSED";
      break;
    case 3:
      str = "ActionHandler.MOUSECLICKED or ActionHandler.VALUESCHANGED";
      break;
    case 6:
      str = "ActionHandler.FOCUS_EVENT";
      break;
    case 4:
      str = "ActionHandler.MOUSEENTERED";
      break;
    case 5:
      str = "ActionHandler.MOUSEEXITED";
      break;
    case 2:
      str = "ActionHandler.MOUSERELEASED";
      break;
    case 0:
      str = "ActionHandler.NOMESSAGE";
      break;
    default:
      str = String.valueOf(paramInt);
    }
    return str;
  }

  public static void debugLog(String paramString)
  {
    File localFile = new File("JSErrorLog.txt");
    try
    {
      if (!localFile.exists())
      {
        localFile.createNewFile();
        System.err.println(new StringBuilder().append("Javascript error log file created: ").append(localFile.getAbsolutePath()).toString());
      }
      if (erroredCode == null)
        erroredCode = new ArrayList();
      if (erroredCode.contains(paramString))
        return;
      BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(localFile, true));
      localBufferedWriter.write(paramString);
      localBufferedWriter.close();
      erroredCode.add(paramString);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.GenericParser
 * JD-Core Version:    0.6.2
 */
package org.jpedal.objects.javascript;

import java.io.PrintStream;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.SwingUtilities;
import org.jpedal.PdfDecoder;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.javascript.defaultactions.DisplayJavascriptActions;
import org.jpedal.objects.javascript.defaultactions.JpedalDefaultJavascript;
import org.jpedal.objects.javascript.functions.JSFunction;
import org.jpedal.objects.layers.Layer;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoParser extends DefaultParser
  implements ExpressionEngine
{
  private String viewerSettings = new StringBuilder().append(AformDefaultJSscript.getViewerSettings()).append(AformDefaultJSscript.getstaticScript()).toString();
  private Context cx = null;
  private Scriptable scope = null;
  private String functions = "";
  private boolean javascriptRunning = false;
  private Javascript JSObj;

  public RhinoParser(Javascript paramJavascript)
  {
    this.JSObj = paramJavascript;
  }

  public void flush()
  {
    if ((this.acro != null) && (this.acro.getFormFactory() != null))
      if (SwingUtilities.isEventDispatchThread())
      {
        flushJS();
      }
      else
      {
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            RhinoParser.this.flushJS();
          }
        };
        try
        {
          SwingUtilities.invokeAndWait(local1);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
        }
      }
  }

  public void flushJS()
  {
    this.functions = "";
    if (this.cx != null)
      try
      {
        Context.exit();
        this.cx = null;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIllegalStateException.getMessage()).toString());
        localIllegalStateException.printStackTrace();
      }
  }

  public void setJavaScriptEnded()
  {
    this.javascriptRunning = false;
  }

  public void executeFunctions(final String paramString, final FormObject paramFormObject, final AcroRenderer paramAcroRenderer)
  {
    this.javascriptRunning = true;
    if (paramAcroRenderer.getFormFactory().getType() == 1)
    {
      if (SwingUtilities.isEventDispatchThread())
      {
        executeJS(paramString, paramFormObject, paramAcroRenderer);
      }
      else
      {
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            RhinoParser.this.executeJS(paramString, paramFormObject, paramAcroRenderer);
          }
        };
        try
        {
          SwingUtilities.invokeAndWait(local2);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
        }
      }
      while (this.javascriptRunning)
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localInterruptedException.getMessage()).toString());
          localInterruptedException.printStackTrace();
        }
    }
  }

  public void executeJS(String paramString, FormObject paramFormObject, AcroRenderer paramAcroRenderer)
  {
    try
    {
      if ((paramString.isEmpty()) && (this.functions.isEmpty()))
      {
        localObject1 = paramAcroRenderer.getActionHandler().getLayerHandler();
        if ((localObject1 != null) && (((PdfLayerList)localObject1).getChangesMade()))
        {
          if (Layer.debugLayer)
            System.out.println("changed");
          try
          {
            paramAcroRenderer.getActionHandler().getPDFDecoder().decodePage(-1);
            SwingGUI localSwingGUI1 = (SwingGUI)paramAcroRenderer.getActionHandler().getPDFDecoder().getExternalHandler(11);
            if (localSwingGUI1 != null)
              localSwingGUI1.rescanPdfLayers();
            ((PdfDecoder)paramAcroRenderer.getActionHandler().getPDFDecoder()).repaint();
          }
          catch (Exception localException2)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
            localException2.printStackTrace();
          }
        }
        this.javascriptRunning = false;
        return;
      }
      Object localObject1 = "";
      for (int i = paramString.indexOf("function "); i != -1; i = paramString.indexOf("function "))
      {
        int j = i + 8;
        int k = 0;
        for (int m = paramString.charAt(j); ; m = paramString.charAt(j++))
        {
          if (m == 123)
            k++;
          if (m == 125)
          {
            k--;
            if (k == 0)
              break;
          }
          if (j >= paramString.length())
            break;
        }
        int n = paramString.lastIndexOf(13, i);
        int i1 = paramString.lastIndexOf(10, i);
        int i2 = (i1 < n ? n : i1) + 1;
        n = paramString.indexOf(13, j);
        if (n == -1)
          n = paramString.length();
        i1 = paramString.indexOf(10, j);
        if (i1 == -1)
          i1 = paramString.length();
        int i3 = (i1 < n ? i1 : n) + 1;
        localObject1 = new StringBuilder().append((String)localObject1).append(paramString.substring(i2, i3)).toString();
        paramString = new StringBuilder().append(paramString.substring(0, i2)).append(paramString.substring(i3)).toString();
      }
      if (!((String)localObject1).isEmpty())
        addCode((String)localObject1);
      paramString = preParseCode(paramString);
      if (this.cx == null)
      {
        this.cx = Context.enter();
        this.scope = this.cx.initStandardObjects();
        addStdObject(paramAcroRenderer);
      }
      if (paramFormObject != null)
      {
        String str2 = paramFormObject.getTextStreamValue(36);
        Object localObject2 = Context.javaToJS(new PDF2JS(paramFormObject), this.scope);
        ScriptableObject.putProperty(this.scope, "event", localObject2);
        if (str2 != null)
          ScriptableObject.putProperty(this.scope, str2, localObject2);
      }
      String str1 = new StringBuilder().append(this.viewerSettings).append(this.functions).toString();
      this.cx.evaluateString(this.scope, str1, "<JS viewer Settings>", 1, null);
      this.cx.evaluateString(this.scope, paramString, "<javascript>", 1, null);
      localObject1 = paramAcroRenderer.getActionHandler().getLayerHandler();
      if ((localObject1 != null) && (((PdfLayerList)localObject1).getChangesMade()))
      {
        if (Layer.debugLayer)
          System.out.println("changed");
        try
        {
          paramAcroRenderer.getActionHandler().getPDFDecoder().decodePage(-1);
          SwingGUI localSwingGUI2 = (SwingGUI)paramAcroRenderer.getActionHandler().getPDFDecoder().getExternalHandler(11);
          if (localSwingGUI2 != null)
            localSwingGUI2.rescanPdfLayers();
          ((PdfDecoder)paramAcroRenderer.getActionHandler().getPDFDecoder()).repaint();
        }
        catch (Exception localException3)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException3.getMessage()).toString());
          localException3.printStackTrace();
        }
      }
      this.javascriptRunning = false;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    finally
    {
      PdfLayerList localPdfLayerList1;
      SwingGUI localSwingGUI3;
      PdfLayerList localPdfLayerList2 = paramAcroRenderer.getActionHandler().getLayerHandler();
      if ((localPdfLayerList2 != null) && (localPdfLayerList2.getChangesMade()))
      {
        if (Layer.debugLayer)
          System.out.println("changed");
        try
        {
          paramAcroRenderer.getActionHandler().getPDFDecoder().decodePage(-1);
          SwingGUI localSwingGUI4 = (SwingGUI)paramAcroRenderer.getActionHandler().getPDFDecoder().getExternalHandler(11);
          if (localSwingGUI4 != null)
            localSwingGUI4.rescanPdfLayers();
          ((PdfDecoder)paramAcroRenderer.getActionHandler().getPDFDecoder()).repaint();
        }
        catch (Exception localException5)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException5.getMessage()).toString());
          localException5.printStackTrace();
        }
      }
      this.javascriptRunning = false;
    }
  }

  private static String preParseCode(String paramString)
  {
    String[] arrayOfString1 = { "= (\"%.2f\",", "this.ADBE", " getField(", "\ngetField(", "\rgetField(", "(getField(", "this.getField(", "this.resetForm(", "this.pageNum", " this.getOCGs(", "\nthis.getOCGs(", "\rthis.getOCGs(", " getOCGs(", "\ngetOCGs(", "\rgetOCGs(", ".state=" };
    String[] arrayOfString2 = { "= util.z(\"%.2f\",", "ADBE", " acro.getField(", "\nacro.getField(", "\racro.getField(", "(acro.getField(", "acro.getField(", "acro.resetForm(", "acro.pageNum", " layers.getOCGs(", "\nlayers.getOCGs(", "\rlayers.getOCGs(", " layers.getOCGs(", "\nlayers.getOCGs(", "\rlayers.getOCGs(", "\rlayers.getOCGs(" };
    for (int i = 0; i < arrayOfString1.length; i++)
      paramString = checkAndReplaceCode(arrayOfString1[i], arrayOfString2[i], paramString);
    i = paramString.indexOf("printf");
    if (i != -1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int j = paramString.lastIndexOf(59, i);
      int k = paramString.lastIndexOf(123, i);
      if ((j == -1) || ((k != -1) && (k > j)))
        j = k;
      localStringBuilder.append(paramString.substring(0, j + 1));
      int m = paramString.indexOf(34, i);
      for (m = paramString.indexOf(34, m + 1); paramString.charAt(m - 1) == '\\'; m = paramString.indexOf(34, m));
      int n = paramString.indexOf(44, m);
      int i1 = paramString.indexOf(41, n);
      String str = paramString.substring(n + 1, i1);
      if (!str.equals("printfArgs"))
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str, ", ");
        localStringBuilder.append("var printfArgs=new Array();\n");
        int i2 = 0;
        while (localStringTokenizer.hasMoreTokens())
        {
          localStringBuilder.append("printfArgs[");
          localStringBuilder.append(i2++);
          localStringBuilder.append("]=");
          localStringBuilder.append(localStringTokenizer.nextToken());
          localStringBuilder.append(";\n");
        }
        localStringBuilder.append(paramString.substring(j + 1, n + 1));
        localStringBuilder.append("printfArgs");
        localStringBuilder.append(paramString.substring(i1));
        paramString = localStringBuilder.toString();
      }
    }
    paramString = checkAndReplaceCode("event.value=AFMakeNumber(acro.getField(\"sum\").value)(8)", "", paramString);
    paramString = checkAndReplaceCode("calculate = false", "calculate = 0", paramString);
    paramString = checkAndReplaceCode("calculate = true", "calculate = 1", paramString);
    paramString = checkAndReplaceCode("calculate=false", "calculate=0", paramString);
    paramString = checkAndReplaceCode("calculate=true", "calculate=1", paramString);
    return paramString;
  }

  private static String checkAndReplaceCode(String paramString1, String paramString2, String paramString3)
  {
    int i = paramString3.indexOf(paramString1);
    if (i != -1)
    {
      StringBuilder localStringBuilder = new StringBuilder(paramString3.length());
      localStringBuilder.append(paramString3.substring(0, i));
      localStringBuilder.append(paramString2);
      localStringBuilder.append(checkAndReplaceCode(paramString1, paramString2, paramString3.substring(i + paramString1.length(), paramString3.length())));
      paramString3 = localStringBuilder.toString();
    }
    return paramString3;
  }

  private void addStdObject(AcroRenderer paramAcroRenderer)
  {
    Object localObject = Context.javaToJS(new JpedalDefaultJavascript(this.scope, this.cx), this.scope);
    ScriptableObject.putProperty(this.scope, "util", localObject);
    ScriptableObject.putProperty(this.scope, "app", localObject);
    Scriptable localScriptable1 = this.cx.newObject(this.scope);
    ScriptableObject.putProperty(this.scope, "global", localScriptable1);
    Scriptable localScriptable2 = this.cx.newObject(this.scope);
    ScriptableObject.putProperty(this.scope, "ADBE", localScriptable2);
    localObject = Context.javaToJS(new DisplayJavascriptActions(), this.scope);
    ScriptableObject.putProperty(this.scope, "display", localObject);
    ScriptableObject.putProperty(this.scope, "color", localObject);
    PdfLayerList localPdfLayerList = paramAcroRenderer.getActionHandler().getLayerHandler();
    if (localPdfLayerList != null)
    {
      localObject = Context.javaToJS(localPdfLayerList, this.scope);
      ScriptableObject.putProperty(this.scope, "layers", localObject);
    }
    localObject = Context.javaToJS(paramAcroRenderer, this.scope);
    ScriptableObject.putProperty(this.scope, "acro", localObject);
  }

  public int addCode(String paramString)
  {
    this.functions = new StringBuilder().append(this.functions).append(preParseCode(paramString)).toString();
    return 0;
  }

  public Object generateJStype(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      return this.cx.newObject(this.scope, "String", new Object[] { paramString });
    if ((paramString != null) && (!paramString.isEmpty()) && (StringUtils.isNumber(paramString)) && ((paramString.length() != 1) || (paramString.indexOf(46) == -1)))
    {
      Double localDouble = Double.valueOf(paramString);
      return this.cx.newObject(this.scope, "Number", new Object[] { localDouble });
    }
    return this.cx.newObject(this.scope, "String", new Object[] { paramString });
  }

  public int execute(FormObject paramFormObject, int paramInt1, Object paramObject, int paramInt2, char paramChar)
  {
    String str1 = (String)paramObject;
    String[] arrayOfString = JSFunction.convertToArray(str1);
    String str2 = arrayOfString[0];
    int i;
    if (str2.startsWith("AF"))
    {
      i = handleAFCommands(paramFormObject, str2, str1, arrayOfString, paramInt2, paramChar);
    }
    else
    {
      executeFunctions(str1, paramFormObject, this.acro);
      i = 3;
    }
    if (paramInt1 == 22)
    {
      calcualteEvent();
      i = 3;
    }
    return i;
  }

  private void calcualteEvent()
  {
    List localList = this.acro.getCompData().getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
    Object[] arrayOfObject1 = localList.toArray();
    for (Object localObject : arrayOfObject1)
    {
      FormObject localFormObject = (FormObject)localObject;
      String str1 = localFormObject.getObjectRefAsString();
      String str2 = localFormObject.getTextStreamValue(36);
      String str3 = (String)this.JSObj.getJavascriptCommand(str2 != null ? str2 : str1, 4866);
      if (str3 != null)
        execute(localFormObject, 4866, str3, 6, ' ');
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.RhinoParser
 * JD-Core Version:    0.6.2
 */
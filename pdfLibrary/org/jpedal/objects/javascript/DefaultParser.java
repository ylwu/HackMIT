package org.jpedal.objects.javascript;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.javascript.functions.AFDate;
import org.jpedal.objects.javascript.functions.AFNumber;
import org.jpedal.objects.javascript.functions.AFPercent;
import org.jpedal.objects.javascript.functions.AFRange;
import org.jpedal.objects.javascript.functions.AFSimple;
import org.jpedal.objects.javascript.functions.AFSpecial;
import org.jpedal.objects.javascript.functions.AFTime;
import org.jpedal.objects.javascript.functions.JSFunction;
import org.jpedal.objects.raw.FormObject;

public class DefaultParser
  implements ExpressionEngine
{
  AcroRenderer acro;

  public int execute(FormObject paramFormObject, int paramInt1, Object paramObject, int paramInt2, char paramChar)
  {
    int i = 0;
    if ((paramObject instanceof String))
    {
      String str1 = (String)paramObject;
      String[] arrayOfString = JSFunction.convertToArray(str1);
      String str2 = arrayOfString[0];
      if (str2.startsWith("AF"))
        i = handleAFCommands(paramFormObject, str2, str1, arrayOfString, paramInt2, paramChar);
    }
    return i;
  }

  protected int handleAFCommands(FormObject paramFormObject, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt, char paramChar)
  {
    int i = 0;
    int j = -1;
    if (paramString2.contains("_Keystroke"))
      j = 1;
    else if (paramString2.contains("_Validate"))
      j = 2;
    else if (paramString2.contains("_Format"))
      j = 3;
    else if (paramString2.contains("_Calculate"))
      j = 4;
    if ((paramInt != 6) && ((j == 2) || (j == 3)))
    {
      JSFunction.debug("Not called on key event " + paramString2);
      return i;
    }
    if (paramString2.startsWith("AFSpecial_"))
      new AFSpecial(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString1.startsWith("AFPercent_"))
      new AFPercent(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString1.startsWith("AFSimple_"))
      new AFSimple(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString1.startsWith("AFDate_"))
      new AFDate(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString2.startsWith("AFNumber_"))
      i = new AFNumber(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString2.startsWith("AFRange_"))
      new AFRange(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else if (paramString2.startsWith("AFTime_"))
      new AFTime(this.acro, paramFormObject).execute(paramString2, paramArrayOfString, j, paramInt, paramChar);
    else
      JSFunction.debug("Unknown command " + paramString2);
    return i;
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
    return 0;
  }

  public void executeFunctions(String paramString, FormObject paramFormObject)
  {
  }

  public void dispose()
  {
    flush();
  }

  public void setAcroRenderer(AcroRenderer paramAcroRenderer)
  {
    this.acro = paramAcroRenderer;
  }

  public void flush()
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.DefaultParser
 * JD-Core Version:    0.6.2
 */
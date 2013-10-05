package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.javascript.defaultactions.JpedalDefaultJavascript;
import org.jpedal.objects.raw.FormObject;

public class AFSimple extends JSFunction
{
  public AFSimple(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 5;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    if (paramArrayOfString == null)
      debug("Unknown implementation in " + paramString);
    else if (paramArrayOfString.length < 1)
      debug("Values length is less than 1");
    else if (paramInt1 == 4)
    {
      if (JpedalDefaultJavascript.calculate > 0)
      {
        String str1 = "";
        int i = 1;
        String str2 = paramArrayOfString[i];
        int j = convertToValue(str2);
        if (j != -1)
        {
          i++;
          str2 = paramArrayOfString[i];
          String str3 = "";
          if (str2.startsWith("new Array"))
            str1 = processArray(str2, j);
          else
            debug("Unknown params " + str3 + " in " + paramString);
        }
        else
        {
          debug("Unknown command " + str2 + " in " + paramString);
        }
        this.formObject.updateValue(str1, false, true);
        this.formObject.setLastValidValue(str1);
      }
    }
    else
      debug("Unknown command " + paramArrayOfString[0] + " in " + paramString);
    return 0;
  }

  private static int convertToValue(String paramString)
  {
    int i = -1;
    if (paramString.equals("\"SUM\""))
      i = 2;
    else if (paramString.equals("\"AVG\""))
      i = 1;
    else if (paramString.equals("\"PRD\""))
      i = 3;
    else if (paramString.equals("\"MIN\""))
      i = 4;
    else if (paramString.equals("\"MAX\""))
      i = 5;
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFSimple
 * JD-Core Version:    0.6.2
 */
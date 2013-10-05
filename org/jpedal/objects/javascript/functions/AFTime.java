package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public class AFTime extends JSFunction
{
  public AFTime(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 7;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    if ((paramInt2 == 6) && ((paramInt1 == 1) || (paramInt1 == 3)))
    {
      String str = validateMask(paramArrayOfString, ":", false);
      if (str == null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.formObject.getObjectRefAsString();
        maskAlert(1, arrayOfObject);
        execute(paramString, paramArrayOfString, paramInt1, paramInt2, paramChar);
      }
      else
      {
        this.formObject.setLastValidValue(str);
        this.formObject.updateValue(str, false, true);
      }
    }
    else if (paramInt1 == 1)
    {
      JSFunction.debug("AFTime(keystroke)=" + paramString);
    }
    else if (paramInt1 == 3)
    {
      JSFunction.debug("AFTime(format)=" + paramString);
    }
    else
    {
      JSFunction.debug("Unknown command " + paramString);
    }
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFTime
 * JD-Core Version:    0.6.2
 */
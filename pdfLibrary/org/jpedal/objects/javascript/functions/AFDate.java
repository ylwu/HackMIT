package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public class AFDate extends JSFunction
{
  public AFDate(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 1;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    if (paramInt2 != 6)
    {
      JSFunction.debug("Not called on key event event=" + paramInt2 + " js=" + paramString);
      return 0;
    }
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.formObject.getObjectRefAsString();
    arrayOfObject[1] = stripQuotes(paramArrayOfString[1]);
    String str = validateMask(paramArrayOfString, ":.,/ -", true);
    if (((str == null) || (str.isEmpty())) || (str == null))
    {
      maskAlert(2, arrayOfObject);
      execute(paramString, paramArrayOfString, paramInt1, paramInt2, paramChar);
    }
    else
    {
      this.formObject.setLastValidValue(str);
      this.formObject.updateValue(str, false, true);
    }
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFDate
 * JD-Core Version:    0.6.2
 */
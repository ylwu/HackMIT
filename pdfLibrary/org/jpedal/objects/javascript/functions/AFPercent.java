package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.sun.PrintfFormat;

public class AFPercent extends AFNumber
{
  public AFPercent(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 3;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    int i = 0;
    if (paramArrayOfString == null)
    {
      debug("Unknown implementation in " + paramString);
    }
    else if (paramArrayOfString.length < 1)
    {
      debug("Values length is less than 1");
    }
    else
    {
      int j = JSFunction.getStaticDecimalCount();
      if (j == -1)
        j = Integer.parseInt(paramArrayOfString[1]);
      int k = JSFunction.getStaticGapFormat();
      if (k == -1)
        k = Integer.parseInt(paramArrayOfString[2]);
      if (paramInt1 == 1)
      {
        i = validateNumber(paramInt1, paramInt2, j, k, 0, "", true);
      }
      else if (paramInt1 == 3)
      {
        String str1 = (String)this.formObject.getFormValue();
        float f = 0.0F;
        String str2 = "";
        if ((str1 != null) && (!str1.isEmpty()))
        {
          StringBuffer localStringBuffer = convertStringToNumber(str1, k);
          if (localStringBuffer.length() > 0)
            f = Float.parseFloat(localStringBuffer.toString()) * 100.0F;
          str2 = str2 + '%' + k + '.' + j + 'f';
          str1 = new PrintfFormat(str2).sprintf(f) + '%';
        }
        else
        {
          str1 = "";
        }
        this.formObject.setLastValidValue(str1);
        this.formObject.updateValue(str1, false, true);
      }
      else
      {
        debug("Unknown type " + paramArrayOfString[0] + " in " + paramString);
      }
    }
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFPercent
 * JD-Core Version:    0.6.2
 */
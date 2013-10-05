package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public class AFRange extends JSFunction
{
  public AFRange(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 4;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    if (paramArrayOfString == null)
    {
      debug(new StringBuilder().append("Unknown implementation in ").append(paramString).toString());
    }
    else if (paramArrayOfString.length < 1)
    {
      debug("Values length is less than 1");
    }
    else if ((paramInt2 == 6) && (paramInt1 == 2))
    {
      String str1 = (String)this.formObject.getFormValue();
      str1.trim();
      if (str1.isEmpty())
      {
        this.formObject.updateValue(str1, false, true);
        this.formObject.setLastValidValue(str1);
        return 0;
      }
      float f1 = Float.parseFloat(parseJSvariables(paramArrayOfString[2]));
      float f2 = Float.parseFloat(parseJSvariables(paramArrayOfString[4]));
      String str2;
      if (isNotNumber(str1))
      {
        str1 = null;
      }
      else
      {
        str2 = str1;
        if (this.DECIMAL_IS_COMMA)
        {
          str2 = str2.replaceAll("\\.", "");
          str2 = str2.replaceAll(",", "\\.");
        }
        else
        {
          str2 = str2.replaceAll(",", "");
        }
        float f3 = Float.parseFloat(str2);
        boolean bool1 = Boolean.valueOf(paramArrayOfString[1]).booleanValue();
        boolean bool2 = Boolean.valueOf(paramArrayOfString[3]).booleanValue();
        if ((bool1) && (f3 < f1))
          str1 = null;
        else if ((!bool1) && (f3 <= f1))
          str1 = null;
        if ((bool2) && (f3 > f2))
          str1 = null;
        else if ((!bool2) && (f3 >= f2))
          str1 = null;
      }
      if (str1 == null)
      {
        str2 = paramArrayOfString[1];
        StringBuilder localStringBuilder = new StringBuilder("Invalid value: must be greater than ");
        if (paramArrayOfString[1].equals("true"))
          localStringBuilder.append("or equal to ");
        localStringBuilder.append(f1);
        localStringBuilder.append("\nand less than ");
        if (paramArrayOfString[3].equals("true"))
          localStringBuilder.append("or equal to ");
        localStringBuilder.append(f2);
        localStringBuilder.append('.');
        paramArrayOfString[1] = localStringBuilder.toString();
        maskAlert(3, paramArrayOfString);
        paramArrayOfString[1] = str2;
        execute(paramString, paramArrayOfString, paramInt1, paramInt2, paramChar);
      }
      else
      {
        this.formObject.updateValue(str1, false, true);
        this.formObject.setLastValidValue(str1);
      }
    }
    else
    {
      debug(new StringBuilder().append("Unknown command ").append(paramArrayOfString[0]).append(" in ").append(paramString).toString());
    }
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFRange
 * JD-Core Version:    0.6.2
 */
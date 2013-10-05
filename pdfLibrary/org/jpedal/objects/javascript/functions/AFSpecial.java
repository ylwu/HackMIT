package org.jpedal.objects.javascript.functions;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.sun.PrintfFormat;

public class AFSpecial extends JSFunction
{
  public AFSpecial(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 6;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
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
      int i = -1;
      int j = paramArrayOfString[1].charAt(0);
      if ((paramArrayOfString[1].length() == 1) && (j >= 48) && (j <= 51))
        i = Integer.parseInt(paramArrayOfString[1]);
      int k = 1;
      String str1 = (String)this.formObject.getFormValue();
      String str2 = "";
      if (paramInt1 == 1)
      {
        switch (i)
        {
        case 0:
          if (paramInt2 == 6)
            str2 = applyRegexp(str1, new String[] { "\\d{5}" });
          else if (paramInt2 == 2)
            str2 = applyRegexp(str1, new String[] { "\\d{0,5}" });
          break;
        case 1:
          if (paramInt2 == 6)
            str2 = applyRegexp(str1, new String[] { "\\d{5}(\\.|[- ])?\\d{4}" });
          else if (paramInt2 == 2)
            str2 = applyRegexp(str1, new String[] { "\\d{0,5}(\\.|[- ])?\\d{0,4}" });
          break;
        case 2:
          if (paramInt2 == 6)
            str2 = applyRegexp(str1, new String[] { "\\d{3}(\\.|[- ])?\\d{4}", "\\d{3}(\\.|[- ])?\\d{3}(\\.|[- ])?\\d{4}", "\\(\\d{3}\\)(\\.|[- ])?\\d{3}(\\.|[- ])?\\d{4}", "011(\\.|[- \\d])*" });
          else if (paramInt2 == 2)
            str2 = applyRegexp(str1, new String[] { "\\d{0,3}(\\.|[- ])?\\d{0,3}(\\.|[- ])?\\d{0,4}", "\\(\\d{0,3}", "\\(\\d{0,3}\\)(\\.|[- ])?\\d{0,3}(\\.|[- ])?\\d{0,4}", "\\(\\d{0,3}(\\.|[- ])?\\d{0,3}(\\.|[- ])?\\d{0,4}", "\\d{0,3}\\)(\\.|[- ])?\\d{0,3}(\\.|[- ])?\\d{0,4}", "011(\\.|[- \\d])*" });
          break;
        case 3:
          if (paramInt2 == 6)
            str2 = applyRegexp(str1, new String[] { "\\d{3}(\\.|[- ])?\\d{2}(\\.|[- ])?\\d{4}" });
          else if (paramInt2 == 2)
            str2 = applyRegexp(str1, new String[] { "\\d{0,3}(\\.|[- ])?\\d{0,2}(\\.|[- ])?\\d{0,4}" });
          break;
        default:
          if ((paramInt2 == 6) || (paramInt2 == 2))
            str2 = applyRegexp(str1, new String[] { paramArrayOfString[1] });
          break;
        }
        if (paramInt2 == 6)
        {
          if (!str2.equals(str1))
          {
            maskAlert(6, paramArrayOfString);
            execute(paramString, paramArrayOfString, paramInt1, paramInt2, paramChar);
          }
          else
          {
            this.formObject.setLastValidValue(str2);
            this.formObject.updateValue(str2, false, true);
          }
        }
        else if ((paramInt2 != 1) && (paramInt2 != 2))
          k = 0;
      }
      else if (paramInt1 == 3)
      {
        float f = 0.0F;
        String str3 = "";
        if ((str1 != null) && (!str1.isEmpty()))
        {
          switch (i)
          {
          case 0:
            str3 = "99999";
            break;
          case 1:
            str3 = "99999-9999";
            break;
          case 2:
            int m = countDigits(str1);
            if (m > 9)
              str3 = "(999) 999-9999";
            else
              str3 = "999-9999";
            break;
          case 3:
            str3 = "999-99-9999";
            break;
          default:
            k = 0;
          }
          if (k != 0)
            str2 = new PrintfFormat(str3).sprintf(f);
          this.formObject.setLastValidValue(str2);
          this.formObject.updateValue(str2, false, true);
        }
      }
      else
      {
        k = 0;
      }
      if (k == 0)
        debug("Unknown setting or command " + paramArrayOfString[0] + " in " + paramString);
    }
    return 0;
  }

  private static int countDigits(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    for (int k = 0; k < j; k++)
    {
      int m = paramString.charAt(k);
      if ((m >= 48) && (m <= 57))
        i++;
    }
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFSpecial
 * JD-Core Version:    0.6.2
 */
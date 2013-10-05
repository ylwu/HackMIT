package org.jpedal.objects.javascript.functions;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public class AFNumber extends JSFunction
{
  public AFNumber(AcroRenderer paramAcroRenderer, FormObject paramFormObject)
  {
    super(paramAcroRenderer, paramFormObject);
    this.functionID = 2;
  }

  public int execute(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2, char paramChar)
  {
    this.value = this.formObject.getObjectRefAsString();
    int i = 0;
    if (paramArrayOfString == null)
    {
      debug(new StringBuilder().append("Unknown implementation in ").append(paramString).toString());
    }
    else if (paramArrayOfString.length < 1)
    {
      debug("Values length is less than 1");
    }
    else
    {
      int j = 0;
      for (int k = 0; k < paramArrayOfString.length; k++)
        if (paramArrayOfString[k].length() < 1)
        {
          debug(new StringBuilder().append("Value[").append(k).append("] length is less than 1").toString());
          j = 1;
        }
      if (j == 0)
      {
        k = JSFunction.getStaticDecimalCount();
        if (k == -1)
          k = Integer.parseInt(paramArrayOfString[1]);
        int m = JSFunction.getStaticGapFormat();
        if (m == -1)
          m = Integer.parseInt(paramArrayOfString[2]);
        int n = Integer.parseInt(paramArrayOfString[3]);
        String str1 = stripQuotes(paramArrayOfString[5]);
        boolean bool = Boolean.valueOf(paramArrayOfString[6]).booleanValue();
        if ((m == 2) || (m == 3))
          this.DECIMAL_IS_COMMA = true;
        if (paramInt2 == 1)
        {
          String str2 = (String)this.formObject.getFormValue();
          int i1 = ((paramChar >= '0') && (paramChar <= '9')) || ((paramChar == '-') && (str2.indexOf(paramChar) == -1)) || ((paramChar == '.') && (str2.indexOf(paramChar) == -1) && (m != 2)) || ((paramChar == ',') && (str2.indexOf(paramChar) == -1) && (m == 2)) ? 1 : 0;
          if (i1 == 0)
            i = 1;
        }
        else
        {
          i = validateNumber(paramInt1, paramInt2, k, m, n, str1, bool);
        }
      }
    }
    if (i == 1)
      this.value = null;
    return i;
  }

  protected int validateNumber(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, boolean paramBoolean)
  {
    int i = 0;
    String str1 = (String)this.formObject.getFormValue();
    String str2 = "";
    if (paramInt1 == 1)
    {
      str1 = convertStringToNumber(str1, paramInt4).toString();
      if (paramInt4 > 1)
      {
        if (paramInt2 == 6)
          str2 = applyRegexp(str1, new String[] { "[+-]?\\d+([.,]\\d+)?", "[+-]?[.,]\\d+", "[+-]?\\d+[.,]" });
        else if (paramInt2 == 2)
          str2 = str1;
      }
      else if (paramInt2 == 6)
        str2 = applyRegexp(str1, new String[] { "[+-]?\\d+(\\.\\d+)?", "[+-]?\\.\\d+", "[+-]?\\d+\\." });
      else if (paramInt2 == 2)
        str2 = str1;
      if ((!str2.equals(str1)) && (str1.indexOf(45) <= 0))
      {
        this.formObject.updateValue(this.formObject.getLastValidValue(), false, true);
      }
      else if (paramInt2 == 6)
      {
        if (str1.indexOf(45) > 0)
          str1 = new StringBuilder().append(str1.charAt(0)).append(str1.substring(1, str1.length()).replaceAll("-", "")).toString();
        if ((str1 != null) && (str1.length() > -1))
        {
          if (str1.isEmpty())
          {
            str1 = "";
          }
          else
          {
            double d;
            if (this.DECIMAL_IS_COMMA)
              d = Double.parseDouble(str1.replaceAll(",", "."));
            else
              d = Double.parseDouble(str1);
            int j = d < 0.0D ? 1 : 0;
            if ((str1.charAt(0) == '-') && (d == 0.0D))
              d = 0.0D;
            if (d < 0.0D)
              d = -d;
            String str3;
            String str4;
            switch (paramInt4)
            {
            default:
              str3 = ",";
              str4 = ".";
              break;
            case 1:
              str3 = "";
              str4 = ".";
              break;
            case 2:
              str3 = ".";
              str4 = ",";
              break;
            case 3:
              str3 = "";
              str4 = ",";
            }
            String str5 = "###";
            if (!str3.isEmpty())
              str5 = new StringBuilder().append(str5).append(",").toString();
            str5 = new StringBuilder().append(str5).append("##").toString();
            if (paramInt3 != 0)
            {
              str5 = new StringBuilder().append(str5).append("0.0").toString();
              for (int k = 1; k < paramInt3; k++)
                str5 = new StringBuilder().append(str5).append('0').toString();
            }
            else
            {
              str5 = new StringBuilder().append(str5).append("#").toString();
            }
            DecimalFormatSymbols localDecimalFormatSymbols = new DecimalFormatSymbols();
            localDecimalFormatSymbols.setDecimalSeparator(str4.charAt(0));
            if (!str3.isEmpty())
              localDecimalFormatSymbols.setGroupingSeparator(str3.charAt(0));
            str1 = new DecimalFormat(str5, localDecimalFormatSymbols).format(d);
            StringBuilder localStringBuilder = new StringBuilder(str1);
            if (paramBoolean)
              localStringBuilder.insert(0, paramString);
            if ((j != 0) && ((paramInt5 == 2) || (paramInt5 == 3)))
              localStringBuilder.insert(0, '(');
            if (!paramBoolean)
              localStringBuilder.append(paramString);
            if ((j != 0) && ((paramInt5 == 2) || (paramInt5 == 3)))
              localStringBuilder.append(')');
            if ((j != 0) && (paramInt5 != 1) && (paramInt5 != 3))
              localStringBuilder.insert(0, '-');
            str1 = localStringBuilder.toString();
            if ((paramInt5 == 1) || (paramInt5 == 3))
            {
              Color localColor;
              if (j != 0)
                localColor = Color.RED;
              else
                localColor = Color.BLACK;
              if ((Component)this.formObject.getGUIComponent() != null)
                ((Component)this.formObject.getGUIComponent()).setForeground(localColor);
            }
          }
          this.formObject.updateValue(str1, false, true);
        }
      }
    }
    return i;
  }

  static StringBuffer convertStringToNumber(String paramString, int paramInt)
  {
    int i;
    if ((paramString == null) || ((i = paramString.length()) == 0))
      return new StringBuffer(0);
    StringBuffer localStringBuffer = new StringBuffer();
    int j;
    switch (paramInt)
    {
    default:
      j = 46;
      break;
    case 1:
      j = 46;
      break;
    case 2:
      j = 44;
      break;
    case 3:
      j = 44;
    }
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      char c = paramString.charAt(m);
      if (((m == 0) && (c == '-')) || ((k == 0) && ((c == '.') || (c == ','))) || ((c >= '0') && (c <= '9')))
        if ((c == '.') || (c == ','))
        {
          if (c == j)
          {
            k = 1;
            localStringBuffer.append('.');
          }
        }
        else
          localStringBuffer.append(c);
    }
    return localStringBuffer;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.functions.AFNumber
 * JD-Core Version:    0.6.2
 */